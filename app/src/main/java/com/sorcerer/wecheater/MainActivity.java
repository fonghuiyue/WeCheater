package com.sorcerer.wecheater;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;

public class MainActivity extends AppCompatActivity {

    public static final String PREF_NAME = "XPOSED";
    public static final String PREF_KEY = "KEY";

    private MaterialEditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (MaterialEditText) findViewById(R.id.editText);
    }

    public void apply(View view) {
        String s = editText.getText().toString();
        if (s.isEmpty()) {
            Toast.makeText(this, "不可为空", Toast.LENGTH_SHORT).show();
        } else if (s.length() > 5) {
            Toast.makeText(this, "这么大? 太贪心了吧", Toast.LENGTH_SHORT).show();
        } else {
            int res = Integer.valueOf(s);
            getSharedPreferences(PREF_NAME, MODE_WORLD_READABLE).edit().putInt(PREF_KEY, res)
                    .apply();
        }
    }

    public void jumpToOrigin(View view) {
        Intent browserIntent =
                new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://github.com/zhengmin1989/WechatSportCheat"));
        startActivity(browserIntent);
    }

    public void jumpToThis(View view) {
        Intent browserIntent =
                new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://github.com/sorcererxw/wecheater"));
        startActivity(browserIntent);
    }
}
