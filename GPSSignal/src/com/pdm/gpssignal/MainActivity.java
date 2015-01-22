package com.pdm.gpssignal;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.GpsStatus.Listener;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class MainActivity extends ActionBarActivity implements
		LocationListener, OnClickListener, Listener {

	public static final int LOC_UPDATE_FREQ_TIME = 0; // in milliseconds
	public static final int LOC_UPDATE_MIN_DIS = 1; // in meters
	public static final int ADDRESS_UPDATE_FREQ_TIME = 30000; // in millisecondsss

	private AlertDialog alertDialog;
	private LocationManager locationManager;
	private TextView timeTV;
	private TextView latitudeTV;
	private TextView longitudeTV;
	private TextView altitudeTV;
	private TextView accuracyTV;
	private TextView satellitesTV;
	private TextView speedTV;
	private TextView addressTV;

	private Button copyBtn, speedometerBtn;

	private GetAddressTask getAddressTask;

	private double lat, lon, alt;
	private float speed, acc;
	private long gpsTime;
	private String satellitesStr, finalAddress;

	private Location curLocation;

	AdView mAdView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		timeTV = (TextView) findViewById(R.id.timeTv);
		latitudeTV = (TextView) findViewById(R.id.latitudeTv);
		longitudeTV = (TextView) findViewById(R.id.longitudeTv);
		altitudeTV = (TextView) findViewById(R.id.altitudeTv);
		accuracyTV = (TextView) findViewById(R.id.accuracyTv);
		satellitesTV = (TextView) findViewById(R.id.satellitesTv);
		speedTV = (TextView) findViewById(R.id.speedTv);
		addressTV = (TextView) findViewById(R.id.addressTv);

		copyBtn = (Button) findViewById(R.id.copyBtn);
		copyBtn.setOnClickListener(MainActivity.this);

		speedometerBtn = (Button) findViewById(R.id.speedometerBtn);
		speedometerBtn.setOnClickListener(MainActivity.this);

		// load ad
		mAdView = (AdView) findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().addTestDevice(
				"FCCB25DEA3A03E9B287B0858B1714823").build();
		mAdView.loadAd(adRequest);

	}

	@Override
	protected void onResume() {
		super.onResume();
		if (Utils.isGPSEnabaled(MainActivity.this)) {
			locationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER, LOC_UPDATE_FREQ_TIME,
					LOC_UPDATE_MIN_DIS, MainActivity.this);
			locationManager.addGpsStatusListener(MainActivity.this);
		} else {
			displayGPSAlert();
		}
		if (mAdView != null) {
			mAdView.resume();
		}

	}

	@Override
	protected void onPause() {

		super.onPause();
		locationManager.removeUpdates(MainActivity.this);
		locationManager.removeGpsStatusListener(MainActivity.this);

		if (getAddressTask != null) {
			getAddressTask.cancel(true);
		}
		if (mAdView != null) {
			mAdView.pause();
		}

	}

	/** Called before the activity is destroyed */
	@Override
	public void onDestroy() {
		if (mAdView != null) {
			mAdView.destroy();
		}
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		// Handle action bar item clicks here. The action bar will
//		// automatically handle clicks on the Home/Up button, so long
//		// as you specify a parent activity in AndroidManifest.xml.
//		// int id = item.getItemId();
//		// if (id == R.id.action_about) {
//		//
//		// return true;
//		// }
//		return super.onOptionsItemSelected(item);
//	}

	private void displayGPSAlert() {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("GPS is disabled.");
		builder.setMessage("Application needs your GPS data. Please turn on your location.");
		builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				MainActivity.this
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

	@Override
	public void onLocationChanged(Location location) {

		gpsTime = location.getTime();

		lat = location.getLatitude();
		lon = location.getLongitude();
		alt = location.getAltitude();
		speed = location.getSpeed();

		if (location.hasSpeed()) {
			if (curLocation != null) {
				long timeDifference = gpsTime - curLocation.getTime();
				//System.out.println("time diff: " + timeDifference);
				if (timeDifference > ADDRESS_UPDATE_FREQ_TIME) {
					if (getAddressTask != null) {
						getAddressTask.cancel(true);
						getAddressTask = null;
					}
					//System.out.println("Looking for address.");
					getAddressTask = new MainActivity.GetAddressTask();
					getAddressTask.execute(location);
					curLocation = location;
				}
			} else {
				if (getAddressTask != null) {
					getAddressTask.cancel(true);
					getAddressTask = null;
				}
				getAddressTask = new MainActivity.GetAddressTask();
				getAddressTask.execute(location);
				curLocation = location;
			}

		}

		acc = location.getAccuracy();

		timeTV.setText(Html.fromHtml(Utils.getTimeString(gpsTime)));
		latitudeTV.setText(Html.fromHtml(Utils.getLatitudeString(lat)));
		longitudeTV.setText(Html.fromHtml(Utils.getLongitudeString(lon)));
		altitudeTV.setText(Html.fromHtml(Utils.getAltitudeString(alt)));
		speedTV.setText(Html.fromHtml(Utils.getSpeedString(speed)));
		accuracyTV.setText(Html.fromHtml(Utils.getAccuracyString(acc)));

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
		// display alert if disabled provider is gps
		if (provider.equalsIgnoreCase(LocationManager.GPS_PROVIDER)) {
			displayGPSAlert();
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onClick(View v) {
		if (v == copyBtn) {

			StringBuilder text = new StringBuilder()
					.append(Utils.getTimeString(gpsTime)).append("\n")
					.append(Utils.getLatitudeString(lat)).append("\n")
					.append(Utils.getLongitudeString(lon)).append("\n")
					.append(Utils.getAltitudeString(alt)).append("\n")
					.append(Utils.getAccuracyString(acc)).append("\n")
					.append(Utils.getSpeedString(speed)).append("\n")
					.append(satellitesStr).append("\n")
					.append("Address: " + finalAddress);

			android.text.ClipboardManager cm = (android.text.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

			String finalText = text.toString().replace("<b>", "");
			finalText = finalText.replace("</b>", "");

			cm.setText(finalText);

			Toast.makeText(getApplicationContext(), "Copied to clipboard",
					Toast.LENGTH_SHORT).show();
		} else if (v == speedometerBtn) {
			startActivity(new Intent(MainActivity.this,
					SpeedometerActivity.class));
		}

	}

	@Override
	public void onGpsStatusChanged(int event) {

		if (event == GpsStatus.GPS_EVENT_SATELLITE_STATUS) {

			int usedSatellites = 0;
			int totalSatellites = 0;

			GpsStatus gpsStatus = locationManager.getGpsStatus(null);

			for (GpsSatellite sat : gpsStatus.getSatellites()) {
				if (sat.usedInFix()) {
					usedSatellites++;
				}
				totalSatellites++;
			}
			satellitesStr = "<b>Satallites: </b>"
					+ Integer.toString(usedSatellites) + "/"
					+ Integer.toString(totalSatellites);

			satellitesTV.setText(Html.fromHtml(satellitesStr));
		}

	}

	private class GetAddressTask extends AsyncTask<Location, Void, String> {

		@Override
		protected String doInBackground(Location... params) {
			Geocoder geocoder = new Geocoder(MainActivity.this,
					Locale.getDefault());
			// System.out.println(Locale.getDefault());
			Location loc = params[0];

			if (!Utils.isNetWorkConnected(MainActivity.this)) {
				return "<b>Address: </b>no internet.";
			}

			if (!Geocoder.isPresent()) {
				return "<b>Address: </b>geocoder not present";
			}

			List<Address> addresses = null;

			try {
				addresses = geocoder.getFromLocation(loc.getLatitude(),
						loc.getLongitude(), 1);
			} catch (IOException e) {
				e.printStackTrace();
				return "<b>Address: </b>not available";
			}

			if (addresses != null && addresses.size() > 0) {
				Address address = addresses.get(0);

				String street = address.getMaxAddressLineIndex() > 0 ? address
						.getAddressLine(0) : "";
				String postalCode = address.getPostalCode() != null ? ", "
						+ address.getPostalCode() : "";
				String city = address.getLocality() != null ? ", "
						+ address.getLocality() : "";
				String country = address.getCountryName() != null ? ", "
						+ address.getCountryName() : "";

				StringBuilder addressText = new StringBuilder().append(street)
						.append(postalCode).append(city).append(country);
				finalAddress = addressText.toString();
				return "<b>Address: </b>" + finalAddress;
			} else {
				return "<b>Address: </b>not found";
			}

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			addressTV.setText(Html.fromHtml("<b>Address: </b>searching..."));
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			addressTV.setText(Html.fromHtml(result));
		}

	}

}
