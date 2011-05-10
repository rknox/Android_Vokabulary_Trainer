package knox.ravi;

import java.util.List;
import static knox.ravi.Constants.MAX_GUESSED_CONS;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Trainer extends Activity implements OnClickListener {

	private final static String TAG = "TRAINER";
	
	private List<Vocable> vocables;
	private TextView text;
	private TextView errorLabel;
	private Button answerButton1;
	private Button answerButton2;
	private Button answerButton3;
	private Button stopButton;
	
	private Vocable random;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.trainer);
		Vocable voc = new Vocable();
		vocables = voc.getVocables(this);
		
		text = (TextView) findViewById(R.id.word_to_translate);
		errorLabel = (TextView) findViewById(R.id.false_label);
		
		answerButton1 = (Button) findViewById(R.id.answer_1_button);
		answerButton1.setOnClickListener(this);
		answerButton2 = (Button) findViewById(R.id.answer_2_button);
		answerButton2.setOnClickListener(this);
		answerButton3 = (Button) findViewById(R.id.answer_3_button);
		answerButton3.setOnClickListener(this);
		stopButton = (Button) findViewById(R.id.stop_button);
		stopButton.setOnClickListener(this);

		initText();

	}
	
	/**
	 * Set Text on TextView and Buttons
	 */
	private void initText() {
		Log.d(TAG, "Initializing UI-text");
		Button[] answerButtons = { answerButton1, answerButton2, answerButton3 };
		random = vocables.get((int) (Math.random() * vocables.size()));
		text.setText(random.getEnglish());
		int randomButton = (int) (Math.random() * 3);
		Log.d(TAG, "Randomnumber: " + random + "Randobutton: " + randomButton);
		for (int i = 0; i <= 2; i++) {
			if (i == randomButton) {		
				answerButtons[i].setText(random.getGerman());
			} 
			else {
				int randomNumber = (int) (Math.random() * vocables.size());
				answerButtons[i].setText(vocables.get(randomNumber).getGerman());
			}
		}
		Log.d(TAG, "UI-text initialized...");
	}

	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.answer_1_button:
				check(random, 0);
			break;
			case R.id.answer_2_button:
				check(random, 1);
			break;
			case R.id.answer_3_button:
				check(random, 2);
			break;
			case R.id.stop_button:
				finish();
		}
		if(random.getGuessed() > MAX_GUESSED_CONS){
			vocables.remove(random);
			Vocable.checkListSize(vocables);
			Log.d(TAG, "REMOVED " + random.getId() + " FROM LIST");
		}
	}

	private void check(Vocable vocable, int i) {
		
		Button[] answerButtons = { answerButton1, answerButton2, answerButton3 };
		Log.d(TAG, "Checking answer..." + vocable.getGerman() + " - " + answerButtons[i].getText());
		if (vocable.getGerman().equals(answerButtons[i].getText())) {
			vocable.increaseGuessed(vocable, this);
			initText();
			errorLabel.setText("");
		}
		else{
			errorLabel.setText("Wrong");
			vocable.resetGuessed(vocable, this);
		}
	}

}
