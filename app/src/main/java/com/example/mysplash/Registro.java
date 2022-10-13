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
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.example.mysplash.*;

public class Registro extends AppCompatActivity {
    private Button button4;
    private static final String TAG = "MainActivity";
    public static final String archivo = "archivo.json";
    String json = null;
    String usr = null;
    String password=null;
    public static List<MyInfo> list =new ArrayList<MyInfo>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        button4 = findViewById(R.id.button4);
        Button button5 = findViewById(R.id.button5);
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
        Read();
        json2List(json);
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
                MyInfo info= new MyInfo();
                info.setUsuario(String.valueOf(usuario.getText()));
                info.setPassword(Metodos.bytesToHex(Metodos.createSha1(String.valueOf(pswd.getText()))));
                info.setCorreo(String.valueOf(mail.getText()));
                usr = String.valueOf(usuario.getText());
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
                List2Json(info,list);
            }
        });

    }
    public int usuarios(List<MyInfo> list,MyInfo myInfo){
      int bandera = 0;
        for(MyInfo datos : list){
            if(datos.getUsuario().equals(myInfo.getUsuario())){
                bandera = 1;
            }
        }
      return bandera;
    }
    public void Objet2Json(MyInfo info){
        Gson gson =null;
        String json= null;
        String mensaje = null;
        gson =new Gson();
        json = gson.toJson(info);
        if (json != null) {
            Log.d(TAG, json);
            mensaje = "object2Json OK";
        } else {
            mensaje = "Error object2Json";
        }
        Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_LONG).show();
    }
    public void List2Json(MyInfo info,List<MyInfo> list){
        Gson gson =null;
        String json= null;
        gson =new Gson();
        list.add(info);
        json =gson.toJson(list, ArrayList.class);
        if (json == null)
        {
            Log.d(TAG, "Error json");
        }
        else
        {
            Log.d(TAG, json);
            writeFile(json);
        }
        Toast.makeText(getApplicationContext(), "Ok", Toast.LENGTH_LONG).show();
    }
    private boolean writeFile(String text){
        File file =null;
        FileOutputStream fileOutputStream =null;
        try{
            file=getFile();
            fileOutputStream = new FileOutputStream( file );
            fileOutputStream.write( text.getBytes(StandardCharsets.UTF_8) );
            fileOutputStream.close();
            Log.d(TAG, "Hola");
            return true;
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return false;
    }
    private File getFile(){
        return new File(getDataDir(),archivo);
    }

    public boolean Read(){
        if(!isFileExits()){
            return false;
        }
        File file = getFile();
        FileInputStream fileInputStream = null;
        byte[] bytes = null;
        bytes = new byte[(int)file.length()];
        try {
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bytes);
            json=new String(bytes);
            Log.d(TAG,json);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    private boolean isFileExits( )
    {
        File file = getFile( );
        if( file == null )
        {
            return false;
        }
        return file.isFile() && file.exists();
    }
    public void json2List( String json )
    {
        Gson gson = null;
        String mensaje = null;
        if (json == null || json.length() == 0)
        {
            Toast.makeText(getApplicationContext(), "Error json null or empty", Toast.LENGTH_LONG).show();
            return;
        }
        gson = new Gson();
        Type listType = new TypeToken<ArrayList<MyInfo>>(){}.getType();
        list = gson.fromJson(json, listType);
        if (list == null || list.size() == 0 )
        {
            Toast.makeText(getApplicationContext(), "Error list is null or empty", Toast.LENGTH_LONG).show();
            return;
        }
    }

}