package dao;

import static knox.ravi.Constants.ENGLISH;
import static knox.ravi.Constants.GERMAN;
import static knox.ravi.Constants.GUESSED_CONSECUTIVELY;
import static knox.ravi.Constants.MAX_GUESSED_CONS;
import static knox.ravi.Constants.TABLE_NAME;
import static knox.ravi.Constants.TAG;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import knox.ravi.Vocable;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DOAHelper extends Activity{

	private SQLiteConnectionFactory factObj;
	private SQLiteDatabase database;
	private Context context;
	
	public DOAHelper(Context context){
		this.context = context;
//		database = factObj.getReadableDatabase();
		Log.d(TAG, "IN CONSTRUCTOR " + factObj);
	}
	
	public DOAHelper open()throws SQLException{
		factObj = new SQLiteConnectionFactory(context);
		database = factObj.getWritableDatabase();
		return this;
	}
	
	public void close() {
		factObj.close();
	}
	public  SQLiteDatabase getReadableDatabase(){
		return factObj.getReadableDatabase();
	}
	
	public  SQLiteDatabase getWritableDatabase(){
		return factObj.getWritableDatabase();
	}
	
	public  void persistValues(Context context, ContentValues values) {
		getWritableDatabase().insertOrThrow(TABLE_NAME, null, values);
		Log.d(TAG, "Persisted Values");
	}
	
	public  void resetDb(Context context){
		getWritableDatabase().delete(TABLE_NAME, null, null);
		Log.d(TAG, "Deleted all records");
	}
	
	public  void resetAllGuessed() {
		ContentValues cv = new ContentValues();
		cv.put(GUESSED_CONSECUTIVELY, 0);
		getWritableDatabase().update(TABLE_NAME, cv, null, null);
		Log.d(TAG, "Reseted field: 'guessed' on all Vocables");
	}
	
	@SuppressWarnings("unchecked")
	public int updateDb(Context context, String path, List<Vocable> vocables) {
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
			for (Iterator<Element> i = root.elementIterator("vocable"); i.hasNext();) {
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
				persistValues(context, values);
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
	
	/**
	 * Returns all Vocables
	 * @param context
	 * @return
	 */
	public List<Vocable> getVocables(String[] from) {
		List<Vocable> list = new ArrayList<Vocable>();
		open();
			Cursor cursor = database.query(TABLE_NAME, from, GUESSED_CONSECUTIVELY + " < "
					+ MAX_GUESSED_CONS, null, null, null, null);
			startManagingCursor(cursor);

			while (cursor.moveToNext()) {
				list.add(new Vocable(cursor.getInt(0), cursor.getString(1),
						cursor.getString(2), cursor.getInt(3)));
				Log.d(TAG, "Added Vocable to list: " + cursor.getInt(0));
			}

		list = checkListSize(list);
		return list;
	}
	
	public static List<Vocable> checkListSize(List<Vocable> list) {
		if (list.size() < 1) {
			list.add(new Vocable("done", "done"));
			list.add(new Vocable("done", "done"));
			list.add(new Vocable("done", "done"));
			list.add(new Vocable("done", "done"));
		}
		return list;
	}
}
