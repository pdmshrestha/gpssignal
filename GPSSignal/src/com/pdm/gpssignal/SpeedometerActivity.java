package com.pdm.gpssignal;

import java.text.DecimalFormat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

public class SpeedometerActivity extends Activity implements LocationListener {

	private TextView speedometerTv;
	private LocationManager locationManager;
	private AlertDialog alertDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_speedometer);
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		speedometerTv = (TextView) findViewById(R.id.speedometerTv);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (Utils.isGPSEnabaled(SpeedometerActivity.this)) {
			locationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER,
					MainActivity.LOC_UPDATE_FREQ_TIME,
					MainActivity.LOC_UPDATE_MIN_DIS, SpeedometerActivity.this);
		} else {
			displayGPSAlert();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		locationManager.removeUpdates(SpeedometerActivity.this);
	}

	@Override
	public void onLocationChanged(Location location) {
		float speed = location.getSpeed();
		DecimalFormat df = new DecimalFormat("#.##");
		int spd = (int) ((speed * 3600) / 1000);
		String nSpeed = df.format(spd);
		speedometerTv.setText(nSpeed);
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	@Override
	public void onProviderEnabled(String provider) {
		if (provider.equalsIgnoreCase(LocationManager.GPS_PROVIDER)) {
			if (alertDialog != null) {
				if (alertDialog.isShowing()) {
					alertDialog.dismiss();
				}
			}
		}
	}

	@Override
	public void onProviderDisabled(String provider) {
		if (provider.equalsIgnoreCase(LocationManager.GPS_PROVIDER)) {
			displayGPSAlert();
		}
	}

	private void displayGPSAlert() {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("GPS is disabled.");
		builder.setMessage("Application needs your GPS data. Please turn on your location.");
		builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				SpeedometerActivity.this
						.startActivity(new Intent(
								android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
			}
		});
		builder.setNegativeButton("cancel",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				});
		alertDialog = builder.create();
		alertDialog.show();
	}

}
