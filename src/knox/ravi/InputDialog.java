package knox.ravi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

public class InputDialog extends Activity{
	private String message;
	private String translation;
	private String value = "";
	private boolean result;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getIntent().getExtras();
        translation = bundle.getString("translation");
        message = bundle.getString("native");
        result = false;
        startDialog();
        
    }

	public  void startDialog() {
		
		final EditText input = new EditText(this);
		new AlertDialog.Builder(this)
	    .setTitle("Update Status")
	    .setMessage(message)
	    .setView(input)
	    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int whichButton) {
				value = input.getText().toString();
				if(value.equalsIgnoreCase(translation)){
					result = true;
				}
				Intent intenData = new Intent();
		    	intenData.putExtra("result", result);
		    	setResult(RESULT_OK, intenData);
		        finish();
	        }
	    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int whichButton) {
	        	Intent intenData = new Intent();
		    	intenData.putExtra("result", result);
		    	setResult(RESULT_OK, intenData);
		        finish();
	        }
	    }).show();
		
	}

}
