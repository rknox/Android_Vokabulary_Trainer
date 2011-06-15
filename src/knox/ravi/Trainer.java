package knox.ravi;

import java.util.List;

import dao.DOAHelper;
import static knox.ravi.Constants.MAX_GUESSED_CONS;
import android.app.Activity;
import android.content.Intent;
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
	private Button answerButton1, answerButton2, answerButton3, answerButton4,
			stopButton;
	private Vocable random;
	private int language;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.trainer);
		vocables = new Vocable().getVocables(this, true);

		text = (TextView) findViewById(R.id.word_to_translate);
		errorLabel = (TextView) findViewById(R.id.false_label);

		answerButton1 = (Button) findViewById(R.id.answer_1_button);
		answerButton1.setOnClickListener(this);
		answerButton2 = (Button) findViewById(R.id.answer_2_button);
		answerButton2.setOnClickListener(this);
		answerButton3 = (Button) findViewById(R.id.answer_3_button);
		answerButton3.setOnClickListener(this);
		answerButton4 = (Button) findViewById(R.id.answer_4_button);
		answerButton4.setOnClickListener(this);
		stopButton = (Button) findViewById(R.id.stop_button);
		stopButton.setOnClickListener(this);

		initText();

	}

	/**
	 * Set Text on TextView and Buttons
	 */
	private void initText() {
		String translateFrom = "";
		String tranlsateTo = "";
		Log.d(TAG, "Initializing UI-text");
		Button[] answerButtons = { answerButton1, answerButton2, answerButton3,
				answerButton4 };
		random = vocables.get((int) (Math.random() * vocables.size()));
		language = (int) (Math.random() * 2);
		int randomButton = (int) (Math.random() * 4);
		if (language == 1) {
			text.setText(random.getEnglish());
			translateFrom = random.getEnglish();
			tranlsateTo = random.getGerman();
		} else {
			text.setText(random.getGerman());
			translateFrom = random.getGerman();
			tranlsateTo = random.getEnglish();
		}
		// check if Vocable reached 2/3 of MAX_GUESSED. If yes User should type the translation
		if ((random.getGuessed() > MAX_GUESSED_CONS * 0.6) && (vocables.size() > 1)) {
			Log.d(TAG, "IN MAX_GUESSED * 0.6");
			Intent intent = new Intent(VocabularyTrainer.getContext(), InputDialog.class);
			Bundle bundle = new Bundle();
			bundle.putString("native", translateFrom);
			bundle.putString("translation", tranlsateTo);
			intent.putExtras(bundle);
			startActivityForResult(intent, 0);
		} else {
			for (int i = 0; i <= 3; i++) {
				if (i == randomButton) {
					if (language == 1) {
						answerButtons[i].setText(random.getGerman());
					} else {
						answerButtons[i].setText(random.getEnglish());
					}

				} else {
					int randomNumber = (int) (Math.random() * vocables.size());
					if (language == 1) {
						answerButtons[i].setText(vocables.get(randomNumber)
								.getGerman());
					} else {
						answerButtons[i].setText(vocables.get(randomNumber)
								.getEnglish());
					}
				}
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
		case R.id.answer_4_button:
			check(random, 3);
			break;
		case R.id.stop_button:
			finish();
		}
		if (random.getGuessed() > MAX_GUESSED_CONS) {
			vocables.remove(random);
			Vocable.checkListSize(vocables);
			Log.d(TAG, "REMOVED " + random.getId() + " FROM LIST");
		}
	}

	private void check(Vocable vocable, int i) {
		Button[] answerButtons = { answerButton1, answerButton2, answerButton3,
				answerButton4 };
		Log.d(TAG, "Checking answer..." + vocable.getGerman() + " - "
				+ answerButtons[i].getText());
		if ((vocable.getGerman().equals(answerButtons[i].getText()) || (vocable
				.getEnglish().equals(answerButtons[i].getText())))) {
			vocable.increaseGuessed(vocable, this);
			initText();
			errorLabel.setText("");
		} else {
			errorLabel.setText("Wrong");
			vocable.resetGuessed(vocable, this);
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(vocables.size()<1){
			initText();
		}
		if (data != null) {
			Bundle bundle = data.getExtras();
			if(bundle.getBoolean("result")){
				random.increaseGuessed(random, this);
				Log.i(TAG, "TYPED RIGHT!!!");
			}else{
				Tools.showToast(this, "Wrong...");
			}
			initText();
		}
	}
}