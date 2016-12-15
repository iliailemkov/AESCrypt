package com.example.beardie.aescrypt;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{

    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pref = PreferenceManager.getDefaultSharedPreferences(this);

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

        Button button2 = (Button)findViewById(R.id.button2);

        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showNewPasswordAlert();
            }
        });
        showPasswordAlert();
    }

    void showPasswordAlert(){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Password");

        alertDialog.setMessage("Enter Password");
        final EditText input = new EditText(MainActivity.this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        input.setTransformationMethod(PasswordTransformationMethod.getInstance());

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);

        alertDialog.setPositiveButton("Done", null);

        final AlertDialog mAlertDialog = alertDialog.create();

        mAlertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {

                Button b = mAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        // TODO Do something
                        String pass = pref.getString("App password","");
                        if (String.valueOf(input.getText()).equals(pass) || (pass.length() == 0 && input.getText().length() == 0)){
                            mAlertDialog.dismiss();
                        } else {
                            Toast.makeText(getApplicationContext(),"Invalid Password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        mAlertDialog.show();

    }

    void showNewPasswordAlert(){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

        alertDialog.setTitle("Password");

        alertDialog.setMessage("New Password");

        final EditText input = new EditText(MainActivity.this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        input.setTransformationMethod(PasswordTransformationMethod.getInstance());

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);

        alertDialog.setPositiveButton("Done", null);

        final AlertDialog mAlertDialog = alertDialog.create();
        mAlertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {

                Button b = mAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        // TODO Do something
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("App password", String.valueOf(input.getText()));
                        editor.apply();
                        mAlertDialog.dismiss();
                    }
                });
            }
        });
        mAlertDialog.show();

    }
}
