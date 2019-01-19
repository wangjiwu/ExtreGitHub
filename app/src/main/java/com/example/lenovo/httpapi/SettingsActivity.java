package com.example.lenovo.httpapi;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.app.ActionBar;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class SettingsActivity extends AppCompatActivity {
    public static final String PREFERENCE_PACKAGE ="com.example.lenovo.httpapi";
    public static int MODE = Context.MODE_WORLD_READABLE +Context.MODE_WORLD_WRITEABLE;
    public static final String PREFERENCE_NAME = "SaveSetting";

    TextView sign_out, share;
    ImageView go_back;
    String username = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        sign_out = findViewById(R.id.sign_out);
        share = findViewById(R.id.share);
        go_back = findViewById(R.id.go_back);
        username = getIntent().getStringExtra("username");
        go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences( PREFERENCE_NAME, MODE );
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove(username);
                editor.putString("loginIn", "none");

                editor.commit();


                Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);

                startActivity(intent);

            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_SEND);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Share");
                intent.putExtra(Intent.EXTRA_TEXT, "我正在用ExtreGithub 这个APP， 要来看看嘛٩(๑❛ᴗ❛๑)۶ , github地址如下： https://github.com/wangjiwu/ExtreGithub" );
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Intent.createChooser(intent, getTitle()));

            }
        });



    }


}
