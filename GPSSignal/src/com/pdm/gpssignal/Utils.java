package com.pdm.gpssignal;

import java.sql.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Utils {

	public static boolean isGPSEnabaled(Context context) {

		boolean GPSstate = false;
		LocationManager locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);

		GPSstate = locationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);

		return GPSstate;
	}

	@SuppressLint("SimpleDateFormat")
	public static String getTimeString(long milliSeconds) {
		Date date = new Date(milliSeconds);
		SimpleDateFormat fm = new SimpleDateFormat("HH:mm:ss");
		return "Data received on " + fm.format(date);
	}

	public static String getLatitudeString(double lat) {
		DecimalFormat df = new DecimalFormat("#.#######");
		String nLat = df.format(lat);
		return "<b>Latitude: </b>" + nLat;
	}

	public static String getLongitudeString(double lon) {
		DecimalFormat df = new DecimalFormat("#.#######");
		String nLon = df.format(lon);
		return "<b>Longitude: </b>" + nLon;
	}

	public static String getAltitudeString(double alt) {
		DecimalFormat df = new DecimalFormat("#.##");
		String nAlt = df.format(alt);
		return "<b>Altitude: </b>" + nAlt + "m";
	}

	public static String getAccuracyString(float acc) {
		DecimalFormat df = new DecimalFormat("#.##");
		String nAcc = df.format(acc);
		return "<b>Accuracy: </b>" + nAcc + "m";
	}

	public static String getSpeedString(float speed) {
		DecimalFormat df = new DecimalFormat("#.##");
		int spd = (int) ((speed * 3600) / 1000);
		String nSpeed = df.format(spd);
		return "<b>Speed: </b>" + nSpeed + "km/hr";
	}

	public static boolean isNetWorkConnected(Context context) {
		ConnectivityManager cm = (ConnectivityManager) (context
				.getSystemService(Context.CONNECTIVITY_SERVICE));
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni == null) {
			// There are no active networks.
			return false;
		} else
			return true;
	}

}
