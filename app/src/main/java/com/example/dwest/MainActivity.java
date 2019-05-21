package com.example.dwest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.Manifest;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    private ExternalStorageHandler storageHandler;
    private LocationManager locationManager;
    private String voiceUrl, storageLocation;
    private EditText editText;
    private String URL,lattitude, longitude;
    private String state = Environment.getExternalStorageState();


    public static final String STORAGE = "com.example.dwest";


    private static final int PERMISSIONS_CALL_PHONE = 2;
    private static final int PERMISSIONS_READ_EXTERNAL_STORAGE = 1;
    private static final int PERMISSION_GET_LOCATION = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storageHandler = new ExternalStorageHandler(this);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (!hasReadPermission()) {
            requestReadPermission();
        } else {
            initContent();
        }
    }


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSIONS_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state) || Environment.MEDIA_MOUNTED.equals(state)) {
                        Toast.makeText(this, getString(R.string.storage_granted), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, getString(R.string.storage_external_unavailable), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(this, getString(R.string.storage_denied), Toast.LENGTH_LONG).show();
                }
                initContent();
                break;
            case PERMISSIONS_CALL_PHONE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, R.string.call_granted, Toast.LENGTH_SHORT).show();
                    callFunction();
                } else {
                    Toast.makeText(this, R.string.call_not_granted, Toast.LENGTH_SHORT).show();
                }

                break;
            }
            case PERMISSION_GET_LOCATION:{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    callFunction();
                }
            }

        }
    }

    private void initContent() {
        setContentView(R.layout.activity_main);
        findViewById(R.id.callBtn).setOnClickListener(onClickListener);
        URL = "com.example.dwest.URL";

    }

    final View.OnClickListener onClickListener = new View.OnClickListener() {
        public void onClick(final View v) {
            switch (v.getId()) {
                case R.id.callBtn:
                    checkCallPermission();
                    break;
            }
        }
    };

    private void checkCallPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            callFunction();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_CALL_PHONE);
        }
    }

    private String formatDate(){
        DateFormat df = new SimpleDateFormat("dd-MMMM HH:mm:ss a");
        Date date = new Date(System.currentTimeMillis());
        return df.format(date);
    }


    private void callFunction()throws SecurityException{
        editText = (EditText) findViewById(R.id.editTextNumber);

        String number = editText.getText().toString();
        String dateString = formatDate();

        if (storageHandler.getSaveNumber()){
            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                    (MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_GET_LOCATION);

            }
            else{
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                if(location !=null){
                    double lati = location.getLatitude();
                    double longi = location.getLongitude();
                    lattitude = String.valueOf(lati);
                    longitude = String.valueOf(longi);

                    storageHandler.saveNumbers(number, dateString, lattitude,longitude);
                }else{
                    storageHandler.saveNumbers(number, dateString, "???","???");
                }



            }
        }

        placeCall(number);
    }

    private void placeCall(String number)throws SecurityException {
        number = number.replace("#", Uri.encode("#"));
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + number));
        startActivity(intent);
    }

    private boolean hasReadPermission(){
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestReadPermission(){
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},PERMISSIONS_READ_EXTERNAL_STORAGE);
    }


    private void checkStoragePermission()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
        {
            setDownloadPath();
        }
        else
        {
            Toast.makeText(this, getString(R.string.download_not_allowed), Toast.LENGTH_SHORT).show();
        }
    }


    private void setDownloadPath(){
        voiceUrl = getResources().getString(R.string.voice_url);

        storageLocation = storageHandler.getSoundsPath();

        Intent intent = new Intent(this,DownloadSound.class);

        intent.putExtra(URL, voiceUrl);
        intent.putExtra(STORAGE, storageLocation);

        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;

        switch (item.getItemId()) {
            case R.id.call_settings:
                i = new Intent(this, Settings.class);
                startActivity(i);
                return true;
            case R.id.call_list:
                i = new Intent(this, CallList.class);
                startActivity(i);
                return true;
            case R.id.download_sounds:
                checkStoragePermission();
                return true;
            case R.id.googleMap:
                i = new Intent(this, MapsActivity.class);
                startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

}

