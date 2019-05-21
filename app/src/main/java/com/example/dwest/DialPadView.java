package com.example.dwest;


import android.os.Environment;
import android.widget.TableLayout;
import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.widget.EditText;
import android.content.Context;
import android.view.View;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.AttributeSet;
import android.view.KeyEvent;


public class DialPadView extends TableLayout implements SharedPreferences.OnSharedPreferenceChangeListener{

    private EditText editText;

    private ImageButton buttonOne, buttonTwo, buttonThree,
            buttonFour, buttonFive, buttonSix,
            buttonSeven, buttonEight, buttonNine,
            buttonZero, buttonStar, buttonPound,
            buttonCall, buttonErase;


    private SoundPool sPool;
    private Context context;

    private int soundOne, soundTwo, soundThree,
            soundFour, soundFive, soundSix,
            soundSeven, soundEight, soundNine,
            soundStar, soundZero, soundPound;

    private ExternalStorageHandler storageHandler;

    private String soundPath;
    private String state;


    public DialPadView(Context context, AttributeSet attrs) {

        super(context, attrs);
        this.context = context;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.dial_pad_view, this, true);

        init(context);
    }


    private void init(Context context) {
        storageHandler = new ExternalStorageHandler(context);
        sPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        setFocusable(true);
        setFocusableInTouchMode(true);
        state = Environment.getExternalStorageState();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        preferences.registerOnSharedPreferenceChangeListener(this);
        soundPath = preferences.getString("voiceFile","");


        declareButtons();
        declareSound(soundPath, context);
    }

    private void declareButtons() {
        // Kopplar samtliga imagesbutton.
        buttonOne = (ImageButton) findViewById(R.id.dialOneBtn);
        buttonTwo = (ImageButton) findViewById(R.id.dialTwoBtn);
        buttonThree = (ImageButton) findViewById(R.id.dialThreeBtn);
        buttonFour = (ImageButton) findViewById(R.id.dialFourBtn);
        buttonFive = (ImageButton) findViewById(R.id.dialFiveBtn);
        buttonSix = (ImageButton) findViewById(R.id.dialSixBtn);
        buttonSeven = (ImageButton) findViewById(R.id.dialSevenBtn);
        buttonEight =(ImageButton) findViewById(R.id.dialEightBtn);
        buttonNine =(ImageButton) findViewById(R.id.dialNineBtn);
        buttonZero = (ImageButton)findViewById(R.id.dialZeroBtn);
        buttonStar = (ImageButton) findViewById(R.id.dialStarBtn);
        buttonPound =(ImageButton) findViewById(R.id.dialPoundBtn);
        buttonCall = (ImageButton) findViewById(R.id.callBtn);
        buttonErase = (ImageButton) findViewById(R.id.eraseBtn);
        editText = (EditText) findViewById(R.id.editTextNumber);

        // Sätter onClickListener på alla knappar.
        buttonOne.setOnClickListener(onClickListener);
        buttonTwo.setOnClickListener(onClickListener);
        buttonThree.setOnClickListener(onClickListener);
        buttonFour.setOnClickListener(onClickListener);
        buttonFive.setOnClickListener(onClickListener);
        buttonSix.setOnClickListener(onClickListener);
        buttonSeven.setOnClickListener(onClickListener);
        buttonEight.setOnClickListener(onClickListener);
        buttonNine.setOnClickListener(onClickListener);
        buttonZero.setOnClickListener(onClickListener);
        buttonStar.setOnClickListener(onClickListener);
        buttonPound.setOnClickListener(onClickListener);
        buttonErase.setOnClickListener(onClickListener);
        buttonCall.setOnClickListener(onClickListener);

        buttonErase.setOnLongClickListener(onLongClickListener);
    }

    private void declareSound(String path, Context context) {

        if(ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)|| Environment.MEDIA_MOUNTED.equals(state))&& storageHandler.isDir(path)){

            soundOne = sPool.load(path + "one.mp3", 1);
            soundTwo = sPool.load(path + "two.mp3", 1);
            soundThree = sPool.load(path+"three.mp3", 1);
            soundFour = sPool.load(path + "four.mp3", 1);
            soundFive = sPool.load(path + "five.mp3", 1);
            soundSix = sPool.load(path + "six.mp3", 1);
            soundSeven = sPool.load(path + "seven.mp3", 1);
            soundEight = sPool.load(path + "eight.mp3", 1);
            soundNine = sPool.load(path + "nine.mp3", 1);
            soundStar = sPool.load(path + "star.mp3", 1);
            soundZero = sPool.load(path + "zero.mp3", 1);
            soundPound = sPool.load(path + "pound.mp3", 1);
        } else
        {

            soundOne = 0;
            soundTwo = 0;
            soundThree = 0;
            soundFour = 0;
            soundFive = 0;
            soundSix = 0;
            soundSeven = 0;
            soundEight = 0;
            soundNine = 0;
            soundStar = 0;
            soundZero = 0;
            soundPound = 0;

        }
    }

    private void playDigitSound(int sound) {
        if(sound != 0){
            sPool.play(sound, 1, 1, 1, 0, 1f);
        }
    }

    final OnLongClickListener onLongClickListener = new OnLongClickListener() {
        public boolean onLongClick(final View v) {
            switch (v.getId()) {
                case R.id.eraseBtn:
                    clearNumber();
                    break;
            }
            return true;
        }
    };

    private OnClickListener onClickListener = new OnClickListener() {

        public void onClick(final View v) {
            switch (v.getId()) {
                case R.id.dialOneBtn:
                    playDigitSound(soundOne);
                    appendNumber("1");
                    break;
                case R.id.dialTwoBtn:
                    playDigitSound(soundTwo);
                    appendNumber("2");
                    break;
                case R.id.dialThreeBtn:
                    playDigitSound(soundThree);
                    appendNumber("3");
                    break;
                case R.id.dialFourBtn:
                    playDigitSound(soundFour);
                    appendNumber("4");
                    break;
                case R.id.dialFiveBtn:
                    playDigitSound(soundFive);
                    appendNumber("5");
                    break;
                case R.id.dialSixBtn:
                    playDigitSound(soundSix);
                    appendNumber("6");
                    break;
                case R.id.dialSevenBtn:
                    playDigitSound(soundSeven);
                    appendNumber("7");
                    break;
                case R.id.dialEightBtn:
                    playDigitSound(soundEight);
                    appendNumber("8");
                    break;
                case R.id.dialNineBtn:
                    playDigitSound(soundNine);
                    appendNumber("9");
                    break;
                case R.id.dialStarBtn:
                    playDigitSound(soundStar);
                    appendNumber("*");
                    break;
                case R.id.dialZeroBtn:
                    playDigitSound(soundZero);
                    appendNumber("0");
                    break;
                case R.id.dialPoundBtn:
                    playDigitSound(soundPound);
                    appendNumber("#");
                    break;
                case R.id.eraseBtn:
                    eraseNumber();
                    break;
            }
        }
    };


    private ImageButton getButton(KeyEvent event) {
        ImageButton button = null;

        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_1:
                button = buttonOne;
                break;
            case KeyEvent.KEYCODE_2:
                button = buttonTwo;
                break;
            case KeyEvent.KEYCODE_3:
                button = buttonThree;
                break;
            case KeyEvent.KEYCODE_4:
                button = buttonFour;
                break;
            case KeyEvent.KEYCODE_5:
                button = buttonFive;
                break;
            case KeyEvent.KEYCODE_6:
                button = buttonSix;
                break;
            case KeyEvent.KEYCODE_7:
                button = buttonSeven;
                break;
            case KeyEvent.KEYCODE_8:
                button = buttonEight;
                break;
            case KeyEvent.KEYCODE_9:
                button = buttonNine;
                break;
            case KeyEvent.KEYCODE_STAR:
                button = buttonStar;
                break;
            case KeyEvent.KEYCODE_0:
                button = buttonZero;
                break;
            case KeyEvent.KEYCODE_POUND:
                button = buttonPound;
                break;

        }
        return button;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        ImageButton imgButton = getButton(event);

        if (!(imgButton == null) && !imgButton.isPressed()) {
            imgButton.performClick();
            imgButton.setPressed(true);
            return true;
        }

        return false;

    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        final ImageButton imgButton = getButton(event);

        if (!(imgButton == null)) {
            imgButton.postDelayed(new Runnable() {
                public void run() {
                    imgButton.setPressed(false);
                }
            }, 300);
            return true;
        }
        return false;
    }


    private void appendNumber(String s){
        editText.append(s);
    }

    private void eraseNumber(){
        Editable text = editText.getText();

        if(!(text.length() <= 0)){
            text.delete(text.length() - 1, text.length());
        }
    }

    private void clearNumber(){
        editText.getText().clear();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (s.equals("voiceFile"))
        {
            String newPath = sharedPreferences.getString(s, "");
            declareSound(newPath, getContext());
        }
    }
}