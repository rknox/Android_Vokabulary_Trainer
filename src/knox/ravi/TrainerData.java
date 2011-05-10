package knox.ravi;

import static android.provider.BaseColumns._ID;
import static knox.ravi.Constants.ENGLISH;
import static knox.ravi.Constants.GERMAN;
import static knox.ravi.Constants.GUESSED_CONSECUTIVELY;
import static knox.ravi.Constants.TABLE_NAME;
import static knox.ravi.Constants.TAG;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import filechooser.FileChooser;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.sax.StartElementListener;
import android.text.InputFilter.LengthFilter;
import android.util.Log;
import android.widget.Toast;

public class TrainerData extends SQLiteOpenHelper {

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

	public int updateDb(String path) {
		/**
		 * TODO check for existing vocables in database
		 */
		ContentValues values = new ContentValues();
		int rowCount = 0;
		try {
			Log.d(TAG, "Try updating from XML file");	
			Document doc = XMLHandler.getXMLFile(path);
			Element root = doc.getRootElement();
			String german = "";
			String english = "";
			List<Vocable> vocables = new Vocable().getVocableList();
			for (Iterator<Element> i = root.elementIterator("vocable"); i
					.hasNext();) {
				Element e = i.next();
				for (Iterator<Element> j = e.elementIterator(); j.hasNext();) {
					Element element = j.next();
					if (!vocables.contains(element)) {
						if (element.getName().equals("german")) {
							german = element.getText();
						}
						if (element.getName().equals("english")) {
							english = element.getText();
						}
					}
				}
				values.put(GERMAN, german);
				values.put(ENGLISH, english);
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
		getWritableDatabaseInstance(context).update(TABLE_NAME, cv, null, null);
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
	
	public static void resetDb(Context context){
		getWritableDatabaseInstance(context).delete(TABLE_NAME, null, null);
		Log.d(TAG, "Deleted all records");
	}

}
