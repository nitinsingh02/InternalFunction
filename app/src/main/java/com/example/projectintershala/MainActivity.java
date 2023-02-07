package com.example.projectintershala;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectintershala.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity<location> extends AppCompatActivity {
    ActivityMainBinding binding;
    FirebaseDatabase db;
    DatabaseReference reference;
    EditText FullNameET;
    TextView IMEITXT, INStxt, BCSTXT, BCtxt, location, Timestamp, camertxt;
    ImageView camera;
    Button btnstatus;
    final int REQUEST_CODE = 101;
    String imei;
    IntentFilter intentfilter;
    int deviceStatus;
    String currentBatteryStatus = "Battery Info";
    int view = R.layout.activity_main;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)

    private static final int REQUEST_CODE_PERMISSION = 2;
    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final int RequestPermissionCode = 1;

    GPSTracker gps;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        FullNameET = findViewById(R.id.FullNameET);
        IMEITXT = findViewById(R.id.IMEITXT);
        INStxt = findViewById(R.id.INStxt);
        BCSTXT = findViewById(R.id.BCSTXT);
        BCtxt = findViewById(R.id.BCtxt);
        location = findViewById(R.id.location);
        Timestamp = findViewById(R.id.Timestamp);
        camertxt = findViewById(R.id.camertxt);
        camera = findViewById(R.id.camera);

        btnstatus = findViewById(R.id.btnstatus);
        intentfilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);


        try {
            if (ActivityCompat.checkSelfPermission(this, mPermission)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{mPermission},
                        REQUEST_CODE_PERMISSION);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

        // in the below line, we are checking for permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // if permissions are not provided we are requesting for permissions.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_CODE);
        }


        camertxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 7);
            }
        });


        binding.btnstatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                imei = telephonyManager.getImei();
                IMEITXT.setText(imei);
                MainActivity.this.registerReceiver(broadcastreceiver, intentfilter);
                loadDeatils();
                BatteryPercentage();
                locationMethod();
                dataTime();

                String firstImei = IMEITXT.getText().toString().trim();
                String secoundIcs = INStxt.getText().toString().trim();
                String thirdBcs = BCSTXT.getText().toString().trim();
                String fourthBc = BCtxt.getText().toString().trim();
                String fivethL = location.getText().toString().trim();
                String sixthTS = Timestamp.getText().toString().trim();
                String seventhName = FullNameET.getText().toString().trim();


                firstImei = binding.IMEITXT.getText().toString();
                secoundIcs = binding.INStxt.getText().toString();
                thirdBcs = binding.BCSTXT.getText().toString();
                fourthBc = binding.BCtxt.getText().toString();
                fivethL = binding.location.getText().toString();
                sixthTS = binding.Timestamp.getText().toString();
                seventhName = binding.FullNameET.getText().toString();


                if (!firstImei.isEmpty() && !secoundIcs.isEmpty() && !thirdBcs.isEmpty() && !fourthBc.isEmpty() && !fivethL.isEmpty()
                        && sixthTS.isEmpty() && seventhName.isEmpty()) {

                    Model model = new Model(firstImei, secoundIcs, thirdBcs, fourthBc, fivethL, sixthTS,seventhName);
                    db = FirebaseDatabase.getInstance();
                    reference = db.getReference("User");
                    reference.child(String.valueOf(FullNameET)).setValue("user").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            binding.IMEITXT.setText("");
                            binding.INStxt.setText("");
                            binding.BCSTXT.setText("");
                            binding.BCtxt.setText("");
                            binding.location.setText("");
                            binding.Timestamp.setText("");
                            binding.FullNameET.setText("");
                            Toast.makeText(MainActivity.this, "Succesfully Updated", Toast.LENGTH_SHORT).show();
                        }
                    });
                }


            }
        });


    }

    private void dataTime() {
        String dateTime;
        Calendar calendar;
        SimpleDateFormat simpleDateFormat;

        Long dateValueInLong = System.currentTimeMillis();
        Timestamp.setText(dateValueInLong.toString());


        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss aaa z");
        dateTime = simpleDateFormat.format(calendar.getTime()).toString();
        Timestamp.setText(dateTime);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 7 && resultCode == RESULT_OK) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            camera.setImageBitmap(bitmap);
        }
    }


    public void EnableRuntimePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                Manifest.permission.CAMERA)) {
            Toast.makeText(MainActivity.this, "CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.CAMERA}, RequestPermissionCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] result) {
        super.onRequestPermissionsResult(requestCode, permissions, result);
        super.onRequestPermissionsResult(requestCode, permissions, result);
        switch (requestCode) {
            case RequestPermissionCode:
                if (result.length > 0 && result[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this, "Permission Granted, Now your application can access CAMERA.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this, "Permission Canceled, Now your application cannot access CAMERA.", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }


    private void locationMethod() {
        gps = new GPSTracker(MainActivity.this);

        if (gps.canGetLocation()) {

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            location.setText("Your Location is - \nLat: "
                    + latitude + "\nLong: " + longitude);
        } else {

            gps.showSettingsAlert();
        }


    }


    private void BatteryPercentage() {

        BatteryManager bm = (BatteryManager) getSystemService(BATTERY_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            int percentage = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
            BCtxt.setText("" + percentage + " %");
        }
    }


    BroadcastReceiver broadcastreceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            deviceStatus = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);


            if (deviceStatus == BatteryManager.BATTERY_STATUS_CHARGING) {

                BCSTXT.setText("Charging ");
            }

            if (deviceStatus == BatteryManager.BATTERY_STATUS_DISCHARGING) {

                BCSTXT.setText("Discharging");
            }

            if (deviceStatus == BatteryManager.BATTERY_STATUS_FULL) {

                BCSTXT.setText("Battery Full ");
            }

            if (deviceStatus == BatteryManager.BATTERY_STATUS_UNKNOWN) {

                BCSTXT.setText(" Unknown");
            }

            if (deviceStatus == BatteryManager.BATTERY_STATUS_NOT_CHARGING) {

                BCSTXT.setText(" Not Charging ");


            }

        }
    };


    private void loadDeatils() {

        if (isConnected()) {
            INStxt.setText("Internet Connected");
        } else {
            INStxt.setText("No Internet Connection");
        }

    }


    public boolean isConnected() {
        boolean connected = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return connected;

    }
}