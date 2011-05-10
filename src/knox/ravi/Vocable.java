package knox.ravi;

import static android.provider.BaseColumns._ID;
import static knox.ravi.Constants.ENGLISH;
import static knox.ravi.Constants.GERMAN;
import static knox.ravi.Constants.GUESSED_CONSECUTIVELY;
import static knox.ravi.Constants.TABLE_NAME;
import static knox.ravi.Constants.TAG;
import static knox.ravi.Constants.MAX_GUESSED_CONS;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Vocable extends Activity {

	private String german;
	private String english;
	private int guessed;
	private int id;
	private static List<Vocable> vocables = new ArrayList<Vocable>();
	private TrainerData data;
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
	 * @param guessed the guessed to set
	 */
	public void setGuessed(int guessed) {
		this.guessed = guessed;
	}

	public List<Vocable> getVocableList() {
		vocables.add(new Vocable("Hallo", "Hello"));
//		vocables.add(new Vocable("Tisch", "Table"));
//		vocables.add(new Vocable("Wasser", "Water"));
//		vocables.add(new Vocable("Pflanze", "Plant"));
//		vocables.add(new Vocable("Messer", "Knife"));
//		vocables.add(new Vocable("Stift", "Pen"));
//		vocables.add(new Vocable("Stein", "Stone"));
//		vocables.add(new Vocable("Münze", "Coin"));
//		vocables.add(new Vocable("Bild", "Picture"));
//		vocables.add(new Vocable("Papier", "Paper"));
		return vocables;
	}

	public List<Vocable> getVocables(Context context) {
		data = new TrainerData(context);
		List<Vocable> list = new ArrayList<Vocable>();
		Cursor cursor;
		try {
			SQLiteDatabase db = data.getReadableDatabase();
			cursor = db.query(TABLE_NAME, FROM, GUESSED_CONSECUTIVELY + " < " + MAX_GUESSED_CONS, null, null, null, null);
			startManagingCursor(cursor);

			while (cursor.moveToNext()) {
				list.add(new Vocable(cursor.getInt(0), cursor.getString(1),
						cursor.getString(2), cursor.getInt(3)));
				Log.d(TAG, "Added Vocable to list: " + cursor.getInt(0));
			}
		} finally {
			data.close();

		}
		list = checkListSize(list);
		
		return list;
	}

	

	public static List<Vocable> checkListSize(List<Vocable> list) {
		if(list.size()<1){
			list.add(new Vocable("done", "done"));
			list.add(new Vocable("done", "done"));
			list.add(new Vocable("done", "done"));
		}
		return list;
	}

	public void increaseGuessed(Vocable vocable, Context context) {
		int guessed = vocable.getGuessed();
		guessed++;
		SQLiteDatabase db = TrainerData.getWritableDatabaseInstance(context);
		String sql = "UPDATE " + TABLE_NAME + " SET " + GUESSED_CONSECUTIVELY + " = " + guessed + " WHERE " + _ID + " = " + vocable.getId() + ";" ;
		db.execSQL(sql);
		db.close();
		vocable.setGuessed(guessed);
	}

	public void resetGuessed(Vocable vocable, Context context) {
		SQLiteDatabase db = TrainerData.getWritableDatabaseInstance(context);
		String sql = "UPDATE " + TABLE_NAME + " SET " + GUESSED_CONSECUTIVELY + " = 0 WHERE " + _ID + " = " + vocable.getId() + ";" ;
		db.execSQL(sql);
		db.close();
		vocable.setGuessed(0);
		Log.d(TAG, "Reseted guessed for : " + vocable.getGerman());
	}
	
	private SQLiteDatabase getWritableDatabaseInstance(Context context){
		data = new TrainerData(context);
		return data.getWritableDatabase();
	}
}
