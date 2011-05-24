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
import knox.ravi.VocabularyTrainer;

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

public class DOAHelper extends Activity {

	private SQLiteConnectionFactory factObj;
	private SQLiteDatabase database;

	public DOAHelper(Context context) {
		factObj = new SQLiteConnectionFactory(context);
		database = factObj.getReadableDatabase();
	}

	public DOAHelper open() throws SQLException {
		database = factObj.getWritableDatabase();
		return this;
	}

	public void close() {
		factObj.close();
	}

	public SQLiteDatabase getReadableDatabase() {
		return factObj.getReadableDatabase();
	}

	public SQLiteDatabase getWritableDatabase() {
		return factObj.getWritableDatabase();
	}

	public void persistValues(ContentValues values) {
		getWritableDatabase().insertOrThrow(TABLE_NAME, null, values);
		Log.d(TAG, "Persisted Values");
	}

	public void resetDb() {
		getWritableDatabase().delete(TABLE_NAME, null, null);
		Log.d(TAG, "Deleted all records");
	}

	public void resetAllGuessed() {
		ContentValues cv = new ContentValues();
		cv.put(GUESSED_CONSECUTIVELY, 0);
		getWritableDatabase().update(TABLE_NAME, cv, null, null);
		Log.d(TAG, "Reseted field: 'guessed' on all Vocables");
	}

	@SuppressWarnings("unchecked")
	public int updateDb(String path) {
		ContentValues values = new ContentValues();
		int rowCount = 0;
		List<Vocable> newVocables = new ArrayList<Vocable>();
		try {
			Log.d(TAG, "Try updating from XML file");
			Document doc = XMLHandler.getXMLFile(path);
			Element root = doc.getRootElement();
			String german = "";
			String english = "";

			for (Iterator<Element> i = root.elementIterator("vocable"); i
					.hasNext();) {
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

	/**
	 * Returns all Vocables
	 * 
	 * @param context
	 * @return
	 */
	public List<Vocable> getVocables(String[] from) {
		List<Vocable> list = new ArrayList<Vocable>();
		open();
		Cursor cursor = database.query(TABLE_NAME, from, GUESSED_CONSECUTIVELY
				+ " < " + MAX_GUESSED_CONS, null, null, null, null);
		startManagingCursor(cursor);

		while (cursor.moveToNext()) {
			list.add(new Vocable(cursor.getInt(0), cursor.getString(1), cursor
					.getString(2), cursor.getInt(3)));
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

	// TODO Improve performance, watch out for ConcurrentModificationException
	public List<Vocable> removeDuplicate(List<Vocable> vocables) {
		List<Vocable> existing = new Vocable().getVocables(
				VocabularyTrainer.getContext(), false);
		List<Vocable> returnList = new ArrayList<Vocable>();
		List<Vocable> temp = vocables;
		boolean add;
		for (Vocable vocable : temp) {
			add = true;
			for (Vocable exists : existing) {
				if (vocable.getGerman().equalsIgnoreCase(exists.getGerman())
						&& vocable.getEnglish().equalsIgnoreCase(
								exists.getEnglish())) {
					add = false;
				}
			}
			if (add) {
				returnList.add(new Vocable(vocable.getGerman(), vocable
						.getEnglish()));
			}
		}
		return returnList;
	}

//	public void persistValues(ContentValues values) {
//		SQLiteDatabase db = getWritableDatabase();
//		db.insertOrThrow(TABLE_NAME, null, values);
//		db.close();
//		Log.d(TAG, "Persisted Values");
//	}
}
