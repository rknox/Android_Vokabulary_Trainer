package knox.ravi;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class ConfirmDbReset extends Activity implements OnClickListener{
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.confirm);
		
		View confirmButton = findViewById(R.id.confirm_yes);
		confirmButton.setOnClickListener(this);
		View cancelButton = findViewById(R.id.confirm_no);
		cancelButton.setOnClickListener(this);
	}

	public void onClick(View v) {
		if(v.getId() == R.id.confirm_yes){TrainerData.resetDb(this);super.onBackPressed();}
		else if (v.getId() == R.id.confirm_no) {super.onBackPressed();}		
	}

}
