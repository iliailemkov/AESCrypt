package com.example.beardie.aescrypt;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final MyFile myFile = new MyFile();
        myFile.createDirectory("encrypt");
        myFile.createDirectory("decrypt");

        ArrayList<String> encList = myFile.getEncryptFiles();
        final ListView listView = (ListView) findViewById(R.id.list_new);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(listView.getContext(), android.R.layout.simple_list_item_1, encList);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selected = listView.getItemAtPosition(i).toString();
                myFile.decryptFile(selected);
                adapter.remove(selected);
                adapter.notifyDataSetChanged();
            }
        });

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File fileDecrypt = new File(Environment.getExternalStorageDirectory().getPath() + "/decrypt");
                ArrayList<String> fileList = new ArrayList<String>();
                for(int i = 0; i < fileDecrypt.list().length; i++){
                    fileList.add(i, fileDecrypt.list()[i]);
                }
                myFile.encryptFile(fileList);
                adapter.clear();
                adapter.addAll(myFile.getEncryptFiles());
                adapter.notifyDataSetChanged();
            }
        });

    }

}
