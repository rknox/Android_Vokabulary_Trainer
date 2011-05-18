package dao;

import static android.provider.BaseColumns._ID;
import static knox.ravi.Constants.ENGLISH;
import static knox.ravi.Constants.GERMAN;
import static knox.ravi.Constants.GUESSED_CONSECUTIVELY;
import static knox.ravi.Constants.TABLE_NAME;
import static knox.ravi.Constants.TAG;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteConnectionFactory extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 7;
	private static final String DATABASE_NAME = "vocabulary_trainer.db";
	private static final String createTabel = "CREATE TABLE " + TABLE_NAME
			+ " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + GERMAN
			+ " TEXT NOT NULL, " + ENGLISH + " TEXT NOT NULL, "
			+ GUESSED_CONSECUTIVELY + " INTEGER);";
	private static final String dropTable = "DROP TABLE IF EXISTS "
			+ TABLE_NAME;

	public SQLiteConnectionFactory(Context context) {
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
	


}
