package com.example.mysplash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.mysplash.json.MyInfo;
import com.google.gson.Gson;

public class Registro extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        Button button5 = findViewById(R.id.button5);
        Button button4 = findViewById(R.id.button4);
        EditText usuario = findViewById(R.id.usuario);
        EditText pswd = findViewById(R.id.pswd);
        EditText mail = findViewById(R.id.email);
        CheckBox box1 = findViewById(R.id.checkBox1);
        CheckBox box2 = findViewById(R.id.checkBox2);
        CheckBox box3 = findViewById(R.id.checkBox3);
        RadioButton r1 = findViewById(R.id.radioButton3);
        RadioButton r2 = findViewById(R.id.radioButton4);
        EditText num = findViewById(R.id.num);
        EditText fec = findViewById(R.id.fec);
        MyInfo info= new MyInfo();

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Registro.this, activity_login.class);
                startActivity(intent);
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                info.setUsuario(String.valueOf(usuario.getText()));
                info.setPassword(String.valueOf(pswd.getText()));
                info.setCorreo(String.valueOf(mail.getText()));
                String[] box = new String[3];
                if(box1.isChecked()==true){
                    box[0]="opcion1";
                }else{
                    box[0]="no";
                }
                if(box2.isChecked()==true){
                    box[1]="opcion2";
                }else{
                    box[1]="no";
                }
                if(box3.isChecked()==true){
                    box[2]="opcion3";
                }else{
                    box[2]="no";
                }
                info.setConocer(box);
                if(r1.isChecked()==true){
                    info.setSexo(Boolean.TRUE);
                }
                if(r2.isChecked()==true){
                    info.setSexo(Boolean.FALSE);
                }
                info.setCel(String.valueOf(num.getText()));
                info.setDate(String.valueOf(fec.getText()));
                Object2Json(info);
            }
        });

    }
    public void Object2Json(MyInfo info){
        Gson gson =null;
        String json= null;
        String mensaje = null;
        gson =new Gson();
        json=gson.toJson(info);
        if (json != null) {
            Log.d(TAG, json);
            mensaje = "object2Json OK";
        } else {
            mensaje = "Error object2Json";
        }
        Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_LONG).show();
    }
}