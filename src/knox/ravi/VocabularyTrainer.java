package knox.ravi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class VocabularyTrainer extends Activity implements OnClickListener {
	
	private static Context context;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		context = getApplicationContext();
		View continueButtion = findViewById(R.id.options_button);
		continueButtion.setOnClickListener(this);
		View startNewGameButton = findViewById(R.id.new_game_button);
		startNewGameButton.setOnClickListener(this);
		View aboutButton = findViewById(R.id.about_button);
		aboutButton.setOnClickListener(this);
		View exitButton = findViewById(R.id.exit_button);
		exitButton.setOnClickListener(this);
	}

	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.new_game_button:
			intent = new Intent(this, Trainer.class);
			startActivity(intent);
			break;
		case R.id.options_button:
			intent = new Intent(this, Options.class);
			startActivity(intent);
			break;
		case R.id.about_button:
			intent = new Intent(this, About.class);
			startActivity(intent);
			break;
		case R.id.exit_button:
			finish();
			break;
		}
	}
	
	public static Context getContext(){
		return context;
		
	}
}