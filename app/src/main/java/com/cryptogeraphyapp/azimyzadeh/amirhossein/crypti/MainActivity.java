package com.cryptogeraphyapp.azimyzadeh.amirhossein.crypti;
/**
 * @author : Amirhossein Azimyzadeh
 * @version : 1.0.0
 * @since : fall 2018*/

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class  MainActivity extends AppCompatActivity implements View.OnClickListener {

    final static String TAG = MainActivity.class.getSimpleName();

    public static int[] keyArrayInteger;
    public static String inputFromUserString;
    public static String outputFromProgram;
    public static char separator = '_';
    public static int keyGeneratorCounter = 0 ;
    RelativeLayout encryptingButton, decryptingButton, outputLayout , helpLayout;
    RelativeLayout mainLayout;
    ImageView circleInfo, keyCopy;
    EditText inputFromUser, keyFromUser;
    static EditText output;
    static ProgressDialog progressBar;


    public static void setClipboard(Context context, String text) {
        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
        clipboard.setPrimaryClip(clip);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        //set push down anim
        PushDownAnim.setPushDownAnimTo(circleInfo, encryptingButton, decryptingButton, keyCopy , helpLayout)
                .setScale(PushDownAnim.MODE_SCALE, 0.9f)
                .setOnClickListener(this);
        PushDownAnim.setPushDownAnimTo(outputLayout, output).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (!output.getText().toString().equals(""))
                    setClipboard(MainActivity.this, outputFromProgram);
                Toast.makeText(MainActivity.this, "Content Copied", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        String key;
        key = keyFromUser.getText().toString().trim();
        output.setTextColor(Color.WHITE);
        inputFromUserString = inputFromUser.getText().toString().trim();
        if (v.getId() == circleInfo.getId()) {
            keyArrayInteger = createRandomKey();
            String keyRandom = "";
            keyRandom += String.valueOf(keyArrayInteger[0]) + separator;
            for (int i = 1; i < keyArrayInteger.length - 1; i++) {
                keyRandom += String.valueOf(keyArrayInteger[i]) + separator;
            }
            keyRandom += String.valueOf(keyArrayInteger[keyArrayInteger.length - 1]);
            keyFromUser.setText(keyRandom);

        } else if (v.getId() == encryptingButton.getId()) {
            if (!key.equals("")) {
                setKeyArray(key);
                if (isTrueKey()) {
                    encrypting();
                }
            }

        } else if (v.getId() == decryptingButton.getId()) {
            if (!key.equals("")) {
                setKeyArray(key);
                if (isTrueKey()) {
                    decrypting();
                }
            }

        } else if (v.getId() == keyCopy.getId()) {
            if (!keyFromUser.getText().toString().trim().equals("")) {
                setClipboard(this, keyFromUser.getText().toString().trim());
                Toast.makeText(MainActivity.this, "Key Copied", Toast.LENGTH_SHORT).show();
            }
        }
        else if(v.getId()==helpLayout.getId()){
            Intent intent  =  new Intent(MainActivity.this,HelpAndAboutActivity.class);
            startActivity(intent);
        }
    }

    private void initViews() {
        encryptingButton = findViewById(R.id.encrypting_button);
        decryptingButton = findViewById(R.id.decrypting_button);
        circleInfo = findViewById(R.id.circle_info_image);
        inputFromUser = findViewById(R.id.input_from_user);
        keyFromUser = findViewById(R.id.key_input_edit_text);
        output = findViewById(R.id.output_text_view);
        mainLayout = findViewById(R.id.main_relative_layout);
        outputLayout = findViewById(R.id.output_relative_layout);
        keyCopy = findViewById(R.id.key_image);
        helpLayout =  findViewById(R.id.help_icon);
    }

    private void decrypting() {
        try {
            startProgressBar("Decrypting");
            Log.d(TAG, "progress bar started.");
            final CodeBlockChaining cbc = new CodeBlockChaining(keyArrayInteger, 1, separator);
            cbc.setEncrypted(inputFromUserString.replace("\n", ""));

            Thread thread = new Thread(new Runnable() {
                Test handler=new Test();
                @Override
                public void run() {
                    outputFromProgram = cbc.decryptingAll();

                    Message message = new Message();

                    Bundle bundle = new Bundle();
                    bundle.putString("message", outputFromProgram);

                    message.setData(bundle);

                    handler.sendMessage(message);
                }
            });
            thread.start();

        } catch (Exception e) {
            output.setTextColor(Color.YELLOW);
            output.setText("SOMETHING WENT WRONG !");
        }
    }

    private void encrypting() {
        try {
            startProgressBar("Encrypting");
            Log.d(TAG, "progress bar started.");
            final CodeBlockChaining cbc = new CodeBlockChaining(keyArrayInteger, 1, separator);
            cbc.setOriginal(inputFromUserString.replace("\n", ""));

            Thread thread = new Thread(new Runnable() {
                Test handler=new Test();
                @Override
                public void run() {
                    cbc.encryptingAll();
                    outputFromProgram = cbc.getEncrypted();

                    Message message = new Message();

                    Bundle bundle = new Bundle();
                    bundle.putString("message", outputFromProgram);

                    message.setData(bundle);

                    handler.sendMessage(message);
                }
            });
            thread.start();

        } catch (Exception e) {
            output.setTextColor(Color.YELLOW);
            output.setText("SOMETHING WENT WRONG !");
        }
    }

    public void setKeyArray(String key) {
        String[] keyArrayString = key.split(String.valueOf(separator));
        try {
            keyArrayInteger = new int[keyArrayString.length];
            for (int i = 0; i < keyArrayInteger.length; i++) {
                keyArrayInteger[i] = Integer.valueOf(keyArrayString[i]);
            }
        } catch (Exception e) {
            keyArrayInteger = null;
            Toast.makeText(this, "WRONG KEY", Toast.LENGTH_LONG).show();
        }
    }

    public boolean isTrueKey() {
        if (keyArrayInteger == null) {
            ErrorShakeKey();
            return false;
        }
        if (keyArrayInteger.length < 16) {
            ErrorShakeKey();
            Toast.makeText(this, "KEY IS TOO SHORT (key>=16)", Toast.LENGTH_SHORT).show();
            return false;
        }
        int[] tempArray = new int[keyArrayInteger.length];
        for (int i = 0; i < keyArrayInteger.length; i++)
            tempArray[i] = keyArrayInteger[i];
        Arrays.sort(tempArray);
        for (int i = 0; i < keyArrayInteger.length; i++) {
            if (tempArray[i] != i + 1) {
                ErrorShakeKey();
                Toast.makeText(this, "WRONG KEY", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    public int[] createRandomKey() {
        if(keyGeneratorCounter<5) {
            int[] random = new int[16];
            List<Integer> list = new LinkedList<>();
            for (int i = 0; i < random.length; i++)
                list.add(i + 1);
            Collections.shuffle(list);
            for (int i = 0; i < random.length; i++) {
                random[i] = list.get(i);
            }
            keyGeneratorCounter++;
            Toast.makeText(this, "Key Generated", Toast.LENGTH_SHORT).show();
            return random;
        }
        Toast.makeText(this,"you can't generate key more than 5 times !",Toast.LENGTH_LONG).show();
        return keyArrayInteger;
    }

    public void startProgressBar(String title) {
        progressBar = new ProgressDialog(MainActivity.this);
        progressBar.setMessage("it depends on your device performance ...");
        progressBar.setTitle(title);
        progressBar.setCancelable(false);
        progressBar.show();
    }
    public void ErrorShakeKey(){
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(100);
        RelativeLayout view =  findViewById(R.id.key_input_layout);
            Animation anim = new TranslateAnimation(-5,5,0,0);
            anim.setDuration(50);
            anim.setRepeatMode(Animation.REVERSE);
            anim.setRepeatCount(8);
            view.startAnimation(anim);
        }

    static class Test extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            output.setText(msg.getData().getString("message"));
            progressBar.dismiss();
            Log.d(TAG, "progress bar dismissed.");
        }

    }
}

