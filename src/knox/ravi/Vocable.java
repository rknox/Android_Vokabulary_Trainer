package knox.ravi;

import static android.provider.BaseColumns._ID;
import static knox.ravi.Constants.ENGLISH;
import static knox.ravi.Constants.GERMAN;
import static knox.ravi.Constants.GUESSED_CONSECUTIVELY;
import static knox.ravi.Constants.MAX_GUESSED_CONS;
import static knox.ravi.Constants.TABLE_NAME;
import static knox.ravi.Constants.TAG;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import dao.DOAHelper;

public class Vocable extends Activity {

	private String german;
	private String english;
	private int guessed;
	private int id;
	private static String[] FROM = { _ID, GERMAN, ENGLISH,
			GUESSED_CONSECUTIVELY };

	public Vocable() {

	}

	public Vocable(String german, String english) {
		this.german = german;
		this.english = english;
		this.guessed = 0;
	}

	/**
	 * @param german
	 * @param english
	 * @param guessed
	 */
	public Vocable(String german, String english, int guessed) {
		this.german = german;
		this.english = english;
		this.guessed = guessed;
	}

	/**
	 * @param id
	 * @param german
	 * @param english
	 * @param guessed
	 */
	public Vocable(int id, String german, String english, int guessed) {
		this.id = id;
		this.german = german;
		this.english = english;
		this.guessed = guessed;
	}

	/**
	 * @return the german
	 */
	public String getGerman() {
		return german;
	}

	/**
	 * @return the english
	 */
	public String getEnglish() {
		return english;
	}

	/**
	 * @return the guessed
	 */
	public int getGuessed() {
		return guessed;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param guessed
	 *            the guessed to set
	 */
	public void setGuessed(int guessed) {
		this.guessed = guessed;
	}

	/**
	 * Returns a list of vocables
	 * 
	 * @param context
	 * @return
	 */
	public List<Vocable> getVocables(Context context, boolean withPlaceholder) {
		List<Vocable> list = new ArrayList<Vocable>();
		Cursor cursor = new DOAHelper(context).getReadableDatabase().query(TABLE_NAME,
				FROM, GUESSED_CONSECUTIVELY + " < " + MAX_GUESSED_CONS, null,
				null, null, "german");
		startManagingCursor(cursor);
		while (cursor.moveToNext()) {
			list.add(new Vocable(cursor.getInt(0), cursor.getString(1), cursor
					.getString(2), cursor.getInt(3)));
			Log.d(TAG, "Added Vocable to list: " + cursor.getInt(0));
		}
		if (withPlaceholder) {
			list = checkListSize(list);
		}
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

	// TODO Execute proper db.update() method that comes with SQLitehelper
	// combine with Trainerdata.resetAllGuessed?!
	public void increaseGuessed(Vocable vocable, Context context) {
		int guessed = vocable.getGuessed();
		guessed++;
		String sql = "UPDATE " + TABLE_NAME + " SET " + GUESSED_CONSECUTIVELY
				+ " = " + guessed + " WHERE " + _ID + " = " + vocable.getId()
				+ ";";
		new DOAHelper(context).getWritableDatabase().execSQL(sql);
		vocable.setGuessed(guessed);
	}

	// TODO Execute proper db.update() method that comes with SQLitehelper
	public void resetGuessed(Vocable vocable, Context context) {
		String sql = "UPDATE " + TABLE_NAME + " SET " + GUESSED_CONSECUTIVELY
				+ " = 0 WHERE " + _ID + " = " + vocable.getId() + ";";
		new DOAHelper(context).getWritableDatabase().execSQL(sql);
		vocable.setGuessed(0);
		Log.d(TAG, "Reseted guessed for : " + vocable.getGerman());
	}
}
