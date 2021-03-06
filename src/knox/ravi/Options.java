package knox.ravi;

import dao.DOAHelper;
import dao.XMLHandler;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class Options extends Activity implements OnClickListener {

	private Intent intent;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.options);

		View resetGuessedButton = findViewById(R.id.reset_guessed_button);
		resetGuessedButton.setOnClickListener(this);
		View updateDbButton = findViewById(R.id.update_db);
		updateDbButton.setOnClickListener(this);
		View resetDbButton = findViewById(R.id.reset_db);
		resetDbButton.setOnClickListener(this);
		View returnButton = findViewById(R.id.go_to_main_menu);
		returnButton.setOnClickListener(this);
		View writeXMLButton = findViewById(R.id.write_xml);
		writeXMLButton.setOnClickListener(this);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.update_db:
			intent = new Intent("filechooser.intent.action.Launch");
			startActivityForResult(intent, 0);
			break;
		case R.id.reset_guessed_button:
			new DOAHelper(VocabularyTrainer.getContext()).resetAllGuessed();
			Tools.showToast(this, "All reseted...");
			break;
		case R.id.reset_db:
			startActivity(new Intent(this, ConfirmDbReset.class));
			break;
		case R.id.go_to_main_menu:
			intent = new Intent(this, VocabularyTrainer.class);
			startActivity(intent);
			break;
		case R.id.write_xml:
			XMLHandler.writeXML();
			break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data != null) {
			Bundle bundle = data.getExtras();
			String pathToXML = bundle.getString("path");
			int rows = new DOAHelper(VocabularyTrainer.getContext()).updateDb(pathToXML);
			String notify = "Inserted " + rows + " new vocabularies";
			Tools.showToast(this, notify);
			startActivity(new Intent(this, VocabularyTrainer.class));
		}
	}

}
