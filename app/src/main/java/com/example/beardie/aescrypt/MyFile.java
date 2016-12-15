package com.example.beardie.aescrypt;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * Created by beardie on 01.12.16.
 */

public class MyFile {

    ArrayList<String> fileNames;
    ListView listView;

    static {
        System.loadLibrary("native-lib");
    }

    private native byte[] encryptData(byte[] decr, int length);

    private native byte[] decryptData(byte[] encr, int length);

    public void encryptFile(ArrayList<String> fileNames){
        for (int i = 0; i < fileNames.size(); i++) {
            String name = fileNames.get(i);
            File file = new File(Environment.getExternalStorageDirectory().getPath() + "/decrypt/" + name);
            FileInputStream fileStream = null;
            try {
                fileStream = new FileInputStream(file);
                int fileLength = (int) file.length();
                int padding = fileLength % 16;
                if (padding != 0){
                    fileLength = fileLength + padding;
                }
                byte []arr= new byte[fileLength];
                fileStream.read(arr,0,arr.length);
                byte[] enc = encryptData(arr, fileLength);

                File outputFile = new File(Environment.getExternalStorageDirectory().getPath() + "/encrypt/" + name + ".enc");
                if (!outputFile.exists()){
                    outputFile.createNewFile();
                }
                FileOutputStream fos = new FileOutputStream(outputFile);
                fos.write(enc);
                fos.close();

                file.delete();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void decryptFile(String fileName){
        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/encrypt/" + fileName);

        FileInputStream fileStream = null;
        try {
            fileStream = new FileInputStream(file);
            int fileLength = (int)file.length();
            int padding = fileLength % 16;

            if (padding != 0){
                fileLength = fileLength + padding;
            }

            byte []arr= new byte[fileLength];
            fileStream.read(arr,0,arr.length);
            byte[] enc = decryptData(arr, fileLength);

            fileName = fileName.substring(0, fileName.length() - 4);
            File outputFile = new File(Environment.getExternalStorageDirectory().getPath() +"/decrypt/"+ fileName);
            if (!outputFile.exists()){
                outputFile.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(outputFile);
            fos.write(enc);
            fos.close();

            file.delete();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ArrayList<String> getEncryptFiles(){
        ArrayList<String> myList = new ArrayList<String>();

        File folder = new File(Environment.getExternalStorageDirectory().getPath() + "/encrypt");
        File list[] = folder.listFiles();

        if (list != null){
            for( int i = 0; i < list.length; i++)
            {
                myList.add( list[i].getName() );
                Log.d("DEBUG", list[i].getName());
            }
        }
        return myList;
    }

    public void createDirectory(String directory){
        File folder = new File(Environment.getExternalStorageDirectory().getPath() + "/" + directory);
        boolean success;
        if (!folder.exists()) {
            success = folder.mkdirs();
            if (success) {
                Log.d("DEBUG", "Create directory /" + directory);
            } else {
                Log.d("DEBUG", "Failure create directory /" + directory);
            }
        } else {
            Log.d("DEBUG", "Directory /" + directory + " already exists");
        }
    }



}
