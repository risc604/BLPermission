package com.demo.tomcat.blpermission;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.Arrays;


//https://stackoverflow.com/questions/32083410/cant-get-write-settings-permission/32083622#32083622
//http://caiyao.name/2016/03/02/Android-6-0%E8%BF%90%E8%A1%8C%E6%97%B6%E6%9D%83%E9%99%90%E5%B0%8F%E7%BB%93/
//https://stackoverflow.com/questions/10748861/how-to-change-screen-timeout-programmatically
//https://stackoverflow.com/questions/38321834/set-android-screen-timeout-settings-programatically
//http://techdocs.zebra.com/emdk-for-android/6-3/tutorial/tutMxDisplayManager/
//https://stackoverflow.com/questions/41457066/how-to-show-alert-dialog-repeatedly-either-user-click-ok


public class MainActivity extends AppCompatActivity {
    private final static String TAG = MainActivity.class.getSimpleName();

    private static Context context;
    static final int CODE_WRITE_SETTINGS_PERMISSION = 1;
    private static final int REQUEST_INTERNET = 10;
    int count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate() ...");
        setContentView(R.layout.activity_main);

        context = this.getApplication().getBaseContext();


        permission_denied();



        //try {
        //    if (checkSystemWritePermission())
        //    {
        //        //RingtoneManager.setActualDefaultRingtoneUri(context, RingtoneManager.TYPE_RINGTONE, newUri);
        //        Toast.makeText(context, "Set as ringtoon successfully ", Toast.LENGTH_SHORT).show();
        //    }
        //    else
        //    {
        //        Toast.makeText(context, "Allow modify system settings ==> ON ", Toast.LENGTH_LONG).show();
        //    }
        //}
        //catch (Exception e)
        //{
        //    Log.i("ringtoon",e.toString());
        //    Toast.makeText(context, "unable to set as Ringtoon ", Toast.LENGTH_SHORT).show();
        //}
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_INTERNET: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //do your task here
                    Log.w(TAG, "onRequestPermissionsResult: permissions: " + Arrays.toString(permissions) +
                    ", grantResults: " + grantResults);

                } else {
                    permission_denied();
                }
                break;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public boolean checkSystemWritePermission1() {
        boolean permission;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permission = Settings.System.canWrite(context);
        } else {
            permission = ContextCompat.checkSelfPermission(context,
                    Manifest.permission.WRITE_SETTINGS) == PackageManager.PERMISSION_GRANTED;
        }

        if (permission)
        {
            //do your code
        }
        else
        {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M)
            {
                //Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                //intent.setData(Uri.parse("package:" + context.getPackageName()));
                //startActivityForResult(intent, MainActivity.CODE_WRITE_SETTINGS_PERMISSION);
                openAndroidPermissionsMenu();
            }
            else
            {
                ActivityCompat.requestPermissions((Activity) context,
                        new String[]{Manifest.permission.WRITE_SETTINGS},
                        MainActivity.CODE_WRITE_SETTINGS_PERMISSION);
            }
        }
        return permission;
    }

    private boolean checkSystemWritePermission() {
        boolean retVal = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            retVal = Settings.System.canWrite(this);
            Log.d(TAG, "Can Write Settings: " + retVal);
            if(retVal){
                Toast.makeText(this, "Write allowed :-)", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this, "Write not allowed :-(", Toast.LENGTH_LONG).show();
                FragmentManager fm = getFragmentManager();
                PopupWritePermission dialogFragment = new PopupWritePermission();
                dialogFragment.show(fm, getString(R.string.popup_writesettings_title));
            }
        }
        return retVal;
    }

    private void openAndroidPermissionsMenu() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
            intent.setData(Uri.parse("package:" + context.getPackageName()));
            context.startActivity(intent);
        }
    }

    public void permission_denied()
    {
        // permission was not granted
        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
        // shouldShowRequestPermissionRationale will return true
        //if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                //Manifest.permission.RECORD_AUDIO)) {
        if (!Settings.System.canWrite(context))
        {
            //ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
            //        Manifest.permission.WRITE_SETTINGS)) {
            showDialogOK("This permission is important to LCD Back Light.",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            switch (which)
                            {
                                case DialogInterface.BUTTON_POSITIVE:
                                    //user enables permisssion do your task..
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    // proceed with logic by disabling the related features or quit the app.
                                    if(count==5)
                                    {
                                        finish();
                                        //finish activity
                                    }
                                    else
                                    {
                                        ActivityCompat.requestPermissions(MainActivity.this,
                                                //new String[]{Manifest.permission.RECORD_AUDIO},
                                                new String[]{Manifest.permission.WRITE_SETTINGS},
                                                REQUEST_INTERNET);
                                        ++count;
                                    }
                                    break;
                            }
                        }
                    });
        } //permission is denied (and never ask again is  checked)
        //shouldShowRequestPermissionRationale will return false
        else
        {
            Toast.makeText( getApplicationContext(),
                        "Go to settings and enable record audio permissions",
                            Toast.LENGTH_LONG).show();
        }
    }


    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }



}

