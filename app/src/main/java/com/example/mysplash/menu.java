package com.example.mysplash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mysplash.json.MyInfo;

import java.util.ArrayList;
import java.util.List;

public class menu extends AppCompatActivity {
    private ListView listView;
    private List<String> list;
    String aux;
    public static MyInfo myInfo= null;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Object object= null;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Intent intent = getIntent();
        if(intent != null){
            if(intent.getExtras() !=null){
                object = intent.getExtras().get("Objeto");
                if (object != null) {
                    if (object instanceof MyInfo) {
                        myInfo = (MyInfo) object;

                    }
                }
            }
        }
        listView = (ListView) findViewById(R.id.listViewId);
        list = new ArrayList<String>();
        for( int i = 0; i < 100; i++)
        {
            list.add( String.format( "Contraseña %d" , i + 1 ) );
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,R.layout.activity_list_view,R.id.editText2, list );
        listView.setAdapter(arrayAdapter);
    }
}