package com.example.dwest;

import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.Manifest;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.MultiSelectListPreference;
import android.content.SharedPreferences;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.content.pm.PackageManager;
import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Settings extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener
{
    private ExternalStorageHandler storageHandler;
    private SharedPreferences sharedPrefs;

    private EditTextPreference soundType, storageLocation, storageSource;
    private ListPreference soundList;
    private MultiSelectListPreference deleteList;
    private String state;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        sharedPrefs.registerOnSharedPreferenceChangeListener(this);
        init();
    }


    private void init()
    {
        storageHandler = new ExternalStorageHandler(this);
        state = Environment.getExternalStorageState();

        soundType = (EditTextPreference) getPreferenceScreen().findPreference("soundType");
        soundList = (ListPreference) getPreferenceScreen().findPreference("voiceFile");
        storageLocation = (EditTextPreference) getPreferenceScreen().findPreference("storageLocation");
        storageSource = (EditTextPreference) getPreferenceScreen().findPreference("storageSource");
        deleteList = (MultiSelectListPreference) getPreferenceScreen().findPreference("deleteFile");


        String vURL = formatVoiceUrl(getString(R.string.voice_url));

        storageLocation.setSummary(storageHandler.getSoundsPath());
        storageSource.setSummary(vURL);


        storageSource.setSelectable(false);
        storageLocation.setSelectable(false);

        setSoundType();
        updateLists();
    }

    private String formatVoiceUrl(String str){

        str = str.substring(0, str.length() - 1);

        return str;

    }


    private void setSoundType(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            soundType.setSummary(getResources().getString(R.string.settings_click));
        }
        else
        {
            soundType.setSummary(getResources().getString(R.string.settings_voice));
        }
    }


    private String formatVoiceText(String string)
    {
        if(!string.equals(""))
        {
            int startIdx = 0;
            int formattedIdx = string.lastIndexOf('/');

            String formattedStr = string.substring(startIdx,formattedIdx);

            startIdx  = formattedStr.lastIndexOf('/') + 1;
            int length = formattedStr.length();
            String finalString = formattedStr.substring(startIdx,length);

            storageHandler.setSelectedVoice(finalString);

            return finalString;
        }
        else
        {
            storageHandler.setSelectedVoice(getResources().getString(R.string.select_voice_settings_sum));

            return getResources().getString(R.string.select_voice_settings_sum);

        }
    }




    @Override
    public void onSharedPreferenceChanged(SharedPreferences prefs, String key)
    {
        if(key.equals("storageLocation")){

        }
        else if(key.equals("save_numbers"))
        {
            boolean checked = prefs.getBoolean("save_numbers", true);

            storageHandler.setSaveNumber(checked);
        }

        else if(key.equals("voiceFile"))
        {
            String selectedVoice = formatVoiceText(sharedPrefs.getString(key,""));
            soundList.setSummary(selectedVoice);
        }

        else if(key.equals("deleteFile"))
        {
            Set<String> soundFiles = sharedPrefs.getStringSet(key, new HashSet<String>());
            boolean voiceDeleted = false;
            boolean soundListEmpty = soundList.getEntries().length == 0;

            for(String str : soundFiles)
            {
                storageHandler.deleteDir(new File(str));

                if(soundList.getValue().equals(str) && !voiceDeleted)
                {
                    voiceDeleted = true;
                }
            }

            updateLists();

            deleteList.setValues(new HashSet<String>());



            if(voiceDeleted)
            {
                soundList.setValue("");
                soundList.setSummary(getResources().getString(R.string.select_voice_settings_sum));

            }

            if(soundListEmpty)
            {
                storageHandler.deleteDir(new File(storageHandler.getSoundsPath()));
                soundType.setSummary(getResources().getString(R.string.settings_click));
            }
        }
    }

    private void updateLists()
    {
        String storageLocation = storageHandler.getSoundsPath();
        boolean externalReady = (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)|| Environment.MEDIA_MOUNTED.equals(state) && storageHandler.isDir(storageLocation));


        if (externalReady)
        {
            List<String> entries = new ArrayList<String>();
            List<String> entryValues = new ArrayList<String>();

            File[] soundDirs = new File(storageLocation).listFiles();


            for (File f : soundDirs)
            {
                if (f.isDirectory())
                {
                    entries.add(f.getName());
                    entryValues.add(storageLocation + f.getName() + "/");
                }
            }

            int entriesSize = entries.size();
            int entryValuesSize = entryValues.size();

            String[] arrayEntries = entries.toArray(new String[entriesSize]);
            String[] arrayEntryValues = entryValues.toArray(new String[entryValuesSize]);

            setEntries(arrayEntries, arrayEntryValues);

        }

        soundList.setSummary(storageHandler.getSelectedVoice());
    }


    public void setEntries(String[] arrayEntries, String[] arrayEntryValues){
        soundList.setEntries(arrayEntries);
        soundList.setEntryValues(arrayEntryValues);

        deleteList.setEntries(arrayEntries);
        deleteList.setEntryValues(arrayEntryValues);
    }
}