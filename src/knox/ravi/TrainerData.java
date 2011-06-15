package knox.ravi;

import static android.provider.BaseColumns._ID;
import static knox.ravi.Constants.ENGLISH;
import static knox.ravi.Constants.GERMAN;
import static knox.ravi.Constants.GUESSED_CONSECUTIVELY;
import static knox.ravi.Constants.TABLE_NAME;
import static knox.ravi.Constants.TAG;
import static knox.ravi.Constants.XML_FILE_NAME;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import dao.XMLHandler;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

public class TrainerData extends SQLiteOpenHelper{

	private static final int DATABASE_VERSION = 6;
	private static final String DATABASE_NAME = "vocabulary_trainer.db";
	private static final String createTabel = "CREATE TABLE " + TABLE_NAME
			+ " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + GERMAN
			+ " TEXT NOT NULL, " + ENGLISH + " TEXT NOT NULL, "
			+ GUESSED_CONSECUTIVELY + " INTEGER);";
	private static final String dropTable = "DROP TABLE IF EXISTS "
			+ TABLE_NAME;

	public TrainerData(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(createTabel);
		Log.d(TAG, "CALL INITIALISING DATABASE");
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(dropTable);
		onCreate(db);
	}

	@SuppressWarnings("unchecked")
	public int updateDb(String path) {
		/**
		 * TODO check for existing vocables in database
		 */
		ContentValues values = new ContentValues();
		int rowCount = 0;
		List<Vocable> newVocables = new ArrayList<Vocable>();
		try {
			Log.d(TAG, "Try updating from XML file");	
			Document doc = XMLHandler.getXMLFile(path);
			Element root = doc.getRootElement();
			String german = "";
			String english = "";

			for (Iterator<Element> i = root.elementIterator("vocable"); i.hasNext();) {
				Element e = i.next();
				for (Iterator<Element> j = e.elementIterator(); j.hasNext();) {
					Element element = j.next();
						if (element.getName().equals("german")) {
							german = element.getText();
						}
						if (element.getName().equals("english")) {
							english = element.getText();
						}
				}
				newVocables.add(new Vocable(german, english, 0));
			}
			newVocables = removeDuplicate(newVocables);
			
			for (Vocable vocable : newVocables) {
				values.put(GERMAN, vocable.getGerman());
				values.put(ENGLISH, vocable.getEnglish());
				values.put(GUESSED_CONSECUTIVELY, 0);
				persistValues(values);
				Log.d(TAG, german + "---" + english);
				rowCount++;
			}
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.d(TAG, "Updated Database");
		return rowCount;
	}

	public static void resetAllGuessed(Context context) {
		ContentValues cv = new ContentValues();
		cv.put(GUESSED_CONSECUTIVELY, 0);
		SQLiteDatabase db = getWritableDatabaseInstance(context);
		db.update(TABLE_NAME, cv, null, null);
		db.close();
		Log.d(TAG, "Reseted field: 'guessed' on all Vocables");
	}

	public static SQLiteDatabase getWritableDatabaseInstance(Context context) {
		return new TrainerData(context).getWritableDatabase();
	}

	public void persistValues(ContentValues values) {
		SQLiteDatabase db = getWritableDatabase();
		db.insertOrThrow(TABLE_NAME, null, values);
		db.close();
		Log.d(TAG, "Persisted Values");
	}
	
	public SQLiteDatabase getReadableSQLiteDatabase(){
		return getReadableDatabase();
	}
	public static void resetDb(Context context){
		SQLiteDatabase db = getWritableDatabaseInstance(context);
		db.delete(TABLE_NAME, null, null);
		db.close();
		Tools.showToast(VocabularyTrainer.getContext(), "Reseted Database");
		Log.d(TAG, "Deleted all records");
	}

	public static void writeXML() {
		List<Vocable> vocables = new Vocable().getVocables(VocabularyTrainer.getContext(), false);
		String path = Environment.getExternalStorageDirectory()+ "/dropbox/" + XML_FILE_NAME;
		XMLHandler.writeList(vocables, path);
	}

	//TODO Improve performance, watch out for ConcurrentModificationException
	public List<Vocable> removeDuplicate(List<Vocable> vocables){
		List<Vocable> existing = new Vocable().getVocables(VocabularyTrainer.getContext(), false);
		List<Vocable> returnList = new ArrayList<Vocable>();
		List<Vocable> temp = vocables;
		boolean add;
		for (Vocable vocable : temp) {
			add = true;
			for (Vocable exist : existing) {
				if(vocable.getGerman().equalsIgnoreCase(exist.getGerman())){
					add = false;
				}
			}
			if(add){returnList.add(new Vocable(vocable.getGerman(), vocable.getEnglish()));};
		}
		return returnList;
	}
}
