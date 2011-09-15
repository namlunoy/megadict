package com.megadict.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.megadict.R;
import com.megadict.activity.base.AbstractListActivity;
import com.megadict.adapter.DictionaryAdapter;
import com.megadict.application.MegaDictApp;
import com.megadict.bean.ManageComponent;
import com.megadict.business.WikiAdder;
import com.megadict.business.scanning.DictionaryScanner;
import com.megadict.model.ChosenModel;
import com.megadict.utility.DatabaseHelper;
import com.megadict.utility.Utility;

public class ManageActivity extends AbstractListActivity {
	private DictionaryScanner scanner;
	private ManageComponent manageComponent;
	private WikiAdder wikiAdder;
	private Cursor listViewCursor;

	public ManageActivity() {
		super(R.layout.manage);
	}

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Get application-scoped variables.
		scanner = ((MegaDictApp) getApplication()).scanner;

		// Create or open database.
		final SQLiteDatabase database = DatabaseHelper.getDatabase(this);
		listViewCursor =
				ChosenModel.selectChosenDictionaryIDsNameAndEnabled(database);
		final DictionaryAdapter adapter =
				new DictionaryAdapter(this, listViewCursor);
		setListAdapter(adapter);

		final ProgressDialog progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("Scanning storage... ");

		manageComponent =
				new ManageComponent(progressDialog, listViewCursor);
		wikiAdder = new WikiAdder(this, manageComponent, scanner);

		// Ask for updating models regardless of whether the models change.
		final Intent returnedIntent = new Intent();
		returnedIntent.putExtra(DictionaryScanner.MODEL_CHANGED, true);
		setResult(Activity.RESULT_OK, returnedIntent);
	}

	@Override
	protected void onStop() {
		super.onStop();
		finish();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		listViewCursor.close();
	}

	@Override
	public boolean onMenuItemSelected(final int featureId, final MenuItem item) {
		if (item.getItemId() == R.id.rescanMenuItem) {
			doRescanning();
		} else if (item.getItemId() == R.id.wikiMenuItem) {
			wikiAdder.showDialog();
		}
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		final MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.manage_menu, menu);
		return true;
	}

	// ======================= Private functions =================== //
	private void doRescanning() {
		if(scanner.didAllRescanTasksFinish()) {
			scanner.rescan(manageComponent);
		} else {
			Utility.messageBox(this, R.string.scanning);
		}
	}
}