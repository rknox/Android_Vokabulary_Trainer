package knox.ravi;

import android.provider.BaseColumns;

public interface Constants extends BaseColumns {

	public static final String TABLE_NAME = "voc_ger_eng";
	
	public static final String GERMAN = "german";
	public static final String ENGLISH = "english";
	public static final String GUESSED_CONSECUTIVELY = "guessed_consecutively";
	public static final String XML_FILE_NAME = "vocables.xml";
	
	public static final String TAG = "VocableTrainer";
	
	public static final int MAX_GUESSED_CONS = 15;
}
