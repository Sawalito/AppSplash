package com.example.mysplash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.PatternsCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.PatternMatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
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


public class Registro extends AppCompatActivity {
    private Button button4;
    private static final String TAG = "MainActivity";
    public static final String archivo = "archivo.json";
    String json = null;
    String usr = null;
    String password=null;
    String email = null;
    public static boolean activado;
    public static List<MyInfo> list =new ArrayList<MyInfo>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        Spinner spinner = findViewById(R.id.spinner);
        String [] opciones = {"Norte","Sur","Centro"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, opciones);
        spinner.setAdapter(adapter);
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

                usr= String.valueOf(usuario.getText());
                password = String.valueOf(pswd.getText());
                email= String.valueOf(mail.getText());

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
                if(r1.isChecked()==true){
                    activado=true;
                }
                if(r2.isChecked()==true){
                    activado=true;
                }
                //Validaciones
                if(usr.equals("")||password.equals("")||email.equals("")){
                    Log.d(TAG,"vacio");
                    Log.d(TAG,usr);
                    Log.d(TAG,password);
                    Log.d(TAG,email);
                    Toast.makeText(getApplicationContext(), "LLena los campos", Toast.LENGTH_LONG).show();

                }else{
                    if(validarEmail(email)){
                        if(list.isEmpty()){
                            Log.d(TAG,"lleno");
                            info.setUsuario(usr);
                            info.setPassword(Metodos.bytesToHex(Metodos.createSha1(String.valueOf(pswd.getText()))));
                            info.setCel(String.valueOf(num.getText()));
                            info.setDate(String.valueOf(fec.getText()));
                            info.setConocer(box);
                            info.setCorreo(String.valueOf(mail.getText()));
                            info.setRegion(spinner.getSelectedItem().toString());
                            info.setSexo(activado);
                            List2Json(info,list);
                        }else{
                            if(usuarios(list,usr)){
                                Log.d(TAG,"esta ocupado mano");
                                Toast.makeText(getApplicationContext(), "El nombre de usuario está ocupado, cambialo", Toast.LENGTH_LONG).show();
                            }else{
                                info.setUsuario(usr);
                                info.setPassword(Metodos.bytesToHex(Metodos.createSha1(String.valueOf(pswd.getText()))));
                                info.setCel(String.valueOf(num.getText()));
                                info.setDate(String.valueOf(fec.getText()));
                                info.setConocer(box);
                                info.setCorreo(String.valueOf(mail.getText()));
                                info.setRegion(spinner.getSelectedItem().toString());
                                info.setSexo(activado);
                                List2Json(info,list);
                            }
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "Introdusca un correo válido", Toast.LENGTH_LONG).show();
                    }

                }
            }
        });

    }
    public boolean validarEmail(String email){
        boolean bandera;
        if(email.isEmpty()){
            bandera=false;
        }else{
            if(PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()){
                bandera=true;
            }else{
                bandera=false;
            }
        }
        return bandera;
    }
    public boolean usuarios(List<MyInfo> list,String usr){
        boolean bandera = false;
        for(MyInfo informacion : list){
            if(informacion.getUsuario().equals(usr)){
                bandera=true;
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