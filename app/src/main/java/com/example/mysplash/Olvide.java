  package com.example.mysplash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.mysplash.Service.DbUsuarios;
import com.example.mysplash.Service.UsuariosDBService;
import com.example.mysplash.des.MyDesUtil;
import com.example.mysplash.json.MyInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

  public class Olvide extends AppCompatActivity {
      public static List<MyInfo> list;
      public static String json = null;
      public static String TAG = "mensaje";
      public static String TOG = "error";
      public static String cadena= null;
      public MyDesUtil myDesUtil= new MyDesUtil().addStringKeyBase64(Registro.KEY);
      public String usr=null;
      public String correo,mensaje;
      EditText usuario,email;
      Button button,button1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_olvide);
        usuario= findViewById(R.id.user);
        email=findViewById(R.id.mail);
        button = findViewById(R.id.recuperar);
        button1 = findViewById(R.id.login);

        DbUsuarios dbUsuarios = new DbUsuarios(Olvide.this);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Olvide.this, activity_login.class);
                startActivity(intent);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usr = String.valueOf(usuario.getText());
                correo= String.valueOf(email.getText());
                MyInfo User = dbUsuarios.GetUsuario(usr,correo);
                if(usr.equals("")&&email.equals("")){
                    Toast.makeText(getApplicationContext(), "Complete algún campo", Toast.LENGTH_LONG).show();
                }else{
                    if(User == null){
                        Toast.makeText(getApplicationContext(), "El usuario o correo no existen", Toast.LENGTH_LONG).show();
                    }else{
                        correo=User.getCorreo();
                        String contra=User.getPassword();
                        String nueva = String.format("%d",(int)(Math.random()*1000));
                        mensaje="<html>\n" +
                                "    <head>\n" +
                                "        <style>\n" +
                                "            \n" +
                                "\t\tbody{\n" +
                                "\t\t\tbackground: #f7f9fc;\n" +
                                "\t\t\tfont-family: Helvetica, Arial;\n" +
                                "\t\t}\n" +
                                "\t\t#wrapper \n" +
                                "\t\t{\n" +
                                "\t\t\tbackground: #ffffff;\n" +
                                "\t\t\t-webkit-box-shadow: 0px 16px 46px -22px rgba(0,0,0,0.75);\n" +
                                "\t\t\t-moz-box-shadow: 0px 16px 46px -22px rgba(0,0,0,0.75);\n" +
                                "\t\t\tbox-shadow: 0px 16px 46px -22px rgba(0,0,0,0.75);\n" +
                                "\t\t\t\n" +
                                "\t\t\twidth: 300px;\n" +
                                "\t\t\tpadding-bottom: 10px;\n" +
                                "\t\t\t\n" +
                                "\t\t\tmargin: -180px 0 0 -150px;\n" +
                                "\t\t\tposition: absolute;\n" +
                                "\t\t\ttop: 50%;\n" +
                                "\t\t\tleft: 50%;\n" +
                                "\t\t\t-webkit-border-radius: 5px;\n" +
                                "\t\t\t-moz-border-radius: 5px;\n" +
                                "\t\t\tborder-radius: 5px;\n" +
                                "\t\t}\n" +
                                "\t\tp{\n" +
                                "\t\t\ttext-align: center;\n" +
                                "\t\t}\n" +
                                "\t\th2{\n" +
                                "\t\t\tfont-weight: normal;\n" +
                                "\t\t\ttext-align: center;\n" +
                                "\t\t}\n" +
                                "\t\ta{\n" +
                                "\t\t\tcolor: #000;\n" +
                                "\t\t\ttext-decoration: none;\n" +
                                "\t\t}\n" +
                                "\t\ta:hover{\n" +
                                "\t\t\ttext-decoration: underline;\n" +
                                "\t\t}\n" +
                                "\t\t#top-pattern{\n" +
                                "\t\t\tmargin-top: -8px;\n" +
                                "\t\t\theight: 8px;\n" +
                                "\t\t\tbackground: url(\"https://sendy.colorlib.com/img/top-pattern2.gif\") repeat-x 0 0;\n" +
                                "\t\t\tbackground-size: auto 8px;\n" +
                                "\t\t}\n" +
                                "\t\n" +
                                "\n" +
                                "        </style>\n" +
                                "    </head>\n" +
                                "    <body>\n" +
                                "        <div id=\"top-pattern\"></div>\n" +
                                "        <div id=\"wrapper\">\n" +
                                "\t\t\t<h2>Recupere su contraseña</h2>\n" +
                                "            <h2>Su contraseña antigua era " +
                                contra +
                                "ahora es " +
                                nueva +
                                "</h2>\n" +
                                "            <p><img src=\"https://sendy.colorlib.com/img/email-notifications/subscribed.gif\" height=\"150\"></p>\n" +
                                "\t\t</div>\n" +
                                "    </body>\n" +
                                "</html>";
                        correo=myDesUtil.cifrar(correo);
                        mensaje=myDesUtil.cifrar(mensaje);
                        boolean f = dbUsuarios.AlterUser(usr,nueva);
                        if(f){
                            if(sendInfo(correo,mensaje)){
                                Toast.makeText(getApplicationContext(), "Se ha enviado una contraseña a su correo", Toast.LENGTH_LONG).show();
                            }else{Toast.makeText(getApplicationContext(), "Error con sendinfo", Toast.LENGTH_LONG).show();}

                        }else{
                            Toast.makeText(getApplicationContext(), "Error al enviar correo", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });


    }
      public boolean sendInfo( String correo ,String mensaje)
      {
          JsonObjectRequest jsonObjectRequest = null;
          JSONObject jsonObject = null;
          String url = "https://us-central1-nemidesarrollo.cloudfunctions.net/envio_correo";
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
              String hola = jsonObject.toString();
              Log.i(TAG,hola);
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
                  Log.e  (TOG, error.toString());
              }
          } );
          requestQueue = Volley.newRequestQueue( getBaseContext() );
          requestQueue.add(jsonObjectRequest);

          return true;
      }

}