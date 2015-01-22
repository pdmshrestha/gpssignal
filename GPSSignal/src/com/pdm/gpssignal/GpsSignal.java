package com.pdm.gpssignal;

import android.app.Application;

public class GpsSignal extends Application {

	@Override
	public void onCreate() {
		try {
			Class.forName("android.os.AsyncTask");
		} catch (Throwable ignore) {
		}
		super.onCreate();
	}

}
