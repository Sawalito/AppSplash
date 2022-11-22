package com.example.mysplash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.mysplash.des.MyDesUtil;
import com.example.mysplash.json.MyInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class activity_login extends AppCompatActivity {
    //DES
    public static final String KEY = "+4xij6jQRSBdCymMxweza/uMYo+o0EUg";
    private String testClaro = "Hola mundo";
    private String testDesCifrado;
    //Atributos
    public String correo;
    public String mensaje;
    public static List<MyInfo> list;
    public static String TAG = "mensaje";
    public static String TOG = "error";
    public static String json = null;
    public static String usr,pswd;
    private Button button1, button2, button3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        button2 = findViewById(R.id.buttonM);
        button1 = findViewById(R.id.button);
        button3 = findViewById(R.id.button3);
        EditText usuario = findViewById(R.id.user);
        EditText pswds = findViewById(R.id.pswds);
        Read();
        json2List(json);
        if (json == null || json.length() == 0){
            button1.setEnabled(false);
            button3.setEnabled(false);
        }
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usr = String.valueOf(usuario.getText());
                pswd = String.valueOf(pswds.getText());
                pswd = Metodos.bytesToHex(Metodos.createSha1(pswd));
                acceso(usr , pswd);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity_login.this, Registro.class);
                startActivity(intent);
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //DES
                MyDesUtil myDesUtil = null;
                myDesUtil = new MyDesUtil( );
                myDesUtil.addStringKeyBase64(KEY);
                //DES
                usr = String.valueOf(usuario.getText());
                if(usr.equals("")){
                    Toast.makeText(getApplicationContext(), "Llena el campo de Usuario", Toast.LENGTH_LONG).show();
                }else{
                    int i=0;
                    for(MyInfo inf : list){
                        if(inf.getUsuario().equals(usr)){
                            correo=inf.getCorreo();
                            mensaje="<html><h1>Registro para una app????</h1></html>";
                            correo=myDesUtil.cifrar(correo);
                            mensaje=myDesUtil.cifrar(mensaje);
                            i=1;
                        }
                    }
                    if(i==1){
                        Log.i(TAG,usr);
                        Log.i(TAG,correo);
                        Log.i(TAG,mensaje);
                        if( sendInfo( correo,mensaje ) )
                        {
                            Toast.makeText(getBaseContext() , "Se envío el texto" , Toast.LENGTH_LONG );
                            return;
                        }
                        Toast.makeText(getBaseContext() , "Error en el envío" , Toast.LENGTH_LONG );
                    }else{
                        if(i==0){
                            Log.i(TAG,"no hay usuarios");
                            Toast.makeText(getBaseContext() , "No existen usuarios" , Toast.LENGTH_LONG );
                            return;
                        }
                    }
                }
            }
        });
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
        return false;
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
    private File getFile( )
    {
        return new File( getDataDir() , Registro.archivo );
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
    public void acceso(String usr , String pswd){
        int i=0;
        if(usr.equals("")||pswd.equals("")){
            Toast.makeText(getApplicationContext(), "Llena los campos", Toast.LENGTH_LONG).show();
        }else{
            for(MyInfo myInfo : list){
                if(myInfo.getUsuario().equals(usr)&&myInfo.getPassword().equals(pswd)){
                    Intent intent = new Intent(activity_login.this, menu.class);
                    intent.putExtra("Objeto", myInfo);
                    startActivity(intent);
                    i=1;
                }
            }
            if(i==0){
                Toast.makeText(getApplicationContext(), "El usuario o contraseña son incorrectos", Toast.LENGTH_LONG).show();
            }
        }
    }


    public boolean sendInfo( String correo ,String mensaje)
    {
        JsonObjectRequest jsonObjectRequest = null;
        JSONObject jsonObject = null;
        String url = "https://us-central1-nemidesarrollo.cloudfunctions.net/function-test";
        RequestQueue requestQueue = null;
        if( correo == null || correo.length() == 0 )
        {
            return false;
        }
        jsonObject = new JSONObject( );
        try
        {
            jsonObject.put("correo" , correo );
            jsonObject.put("mensaje", mensaje);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                Log.i(TAG, response.toString());
            }
        } , new  Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TOG, error.toString());
            }
        } );
        requestQueue = Volley.newRequestQueue( getBaseContext() );
        requestQueue.add(jsonObjectRequest);

        return true;
    }
}