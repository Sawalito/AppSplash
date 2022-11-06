package com.example.mysplash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.mysplash.json.MyInfo;

public class menu extends AppCompatActivity {
    String aux;
    MyInfo myInfo= null;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Object object= null;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        textView= findViewById(R.id.textViewM);
        Intent intent = getIntent();
        if(intent != null){
            if(intent.getExtras() !=null){
                object = intent.getExtras().get("Objeto");
                if (object != null) {
                    if (object instanceof MyInfo) {
                        myInfo = (MyInfo) object;
                        textView.setText(myInfo.getUsuario());
                    }
                }
            }
        }
    }
}