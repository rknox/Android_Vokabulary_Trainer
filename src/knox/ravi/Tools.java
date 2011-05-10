package knox.ravi;

import android.content.Context;
import android.widget.Toast;

public class Tools {

	public static void showToast(Context context, String s){
		Toast toast = Toast.makeText(context, s, Toast.LENGTH_SHORT);
		toast.show();
	}
}
