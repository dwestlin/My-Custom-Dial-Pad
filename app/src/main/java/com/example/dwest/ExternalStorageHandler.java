package com.example.dwest;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import org.json.JSONArray;
import org.json.JSONException;




public class ExternalStorageHandler
{
    private SharedPreferences sharedPrefs;
    private String soundsPath;
    private String state;
    private DatabaseHelper helper;


    public ExternalStorageHandler(Context context)
    {

        sharedPrefs = context.getSharedPreferences("PrefsFile", 0);
        soundsPath  = Environment.getExternalStorageDirectory().getAbsolutePath() + "/dialpad/sounds/";
        state = Environment.getExternalStorageState();
        helper = new DatabaseHelper(context);
    }

    public String getSoundsPath()
    {

        return soundsPath;
    }


    public void setSelectedVoice(String voice)
    {
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString("voiceFile", voice);
        editor.commit();
    }

    public String getSelectedVoice()
    {
        return sharedPrefs.getString("voiceFile", "");
    }



    public void saveNumbers(String number, String date, String latitude, String longitude)
    {
        CallData cd = new CallData();

        cd.setNumber(number);
        cd.setDate(date);
        cd.setLatitude(latitude);
        cd.setLongitude(longitude);

        helper.insertCall(cd);

    }

    public void setSaveNumber(boolean checked){
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putBoolean("save_numbers",checked);
        editor.commit();
    }


    public boolean getSaveNumber()
    {
        return sharedPrefs.getBoolean("save_numbers",false);
    }

    public boolean isDir(String location)
    {
        File dir = new File(location);

        if(dir.isDirectory()) return true;
        else return false;
    }

    public void createDir(String location)
    {
        File dir = new File(location);

        dir.mkdirs();
    }

    public void deleteDir(File dir)
    {
        File[] file = dir.listFiles();

        if (file.length != 0)
        {
            for (File f : file)
            {
                if (f.isDirectory())
                {
                    deleteDir(f);
                }
                else
                {
                    f.delete();
                }
            }
        }

        dir.delete();
    }


}
