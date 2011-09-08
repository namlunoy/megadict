package com.megadict.business.recommending;

import java.util.Collections;
import java.util.List;

import com.megadict.business.AbstractWorkerTask;
import com.megadict.model.Dictionary;

public class RecommendTask extends AbstractWorkerTask<String, Void, List<String>> {
	private final Dictionary model;
	private boolean cancelled = false;

	public RecommendTask(final Dictionary model) {
		super();
		this.model = model;
	}

	@Override
	protected void onCancelled() {
		cancelled = true;
	}

	@Override
	protected List<String> doInBackground(final String... params) {
		while (!cancelled) {
			return model.recommendWord(params[0]);
		}
		return Collections.emptyList();
	}
}