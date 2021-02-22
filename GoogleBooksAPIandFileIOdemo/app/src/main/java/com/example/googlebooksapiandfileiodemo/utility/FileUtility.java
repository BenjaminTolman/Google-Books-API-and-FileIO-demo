// Tolman Benjamin
// JAV2 - C202101 02
// FileUtility.java

package com.example.googlebooksapiandfileiodemo.utility;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class FileUtility {

    private static final String TAG = "FILE UTILITY LOG";

    public static void writeObject(Context context, Serializable _obj, String _filePath){

        try{
            //STEP 1: Build the full path.
            File privateStorage = context.getFilesDir();
            File filePath = new File(privateStorage, _filePath);

            //STEP 2: Create the directory if it does not already exist.
            File directory = filePath.getParentFile();
            if(directory != null && !directory.exists()){
                if(!directory.mkdirs()){
                    Log.wtf(TAG, "Failed to make directory " + directory.getAbsolutePath());
                }
            }
            //STEP 3: Create the file if it does not already exist.
            if(!filePath.exists()){
                if(!filePath.createNewFile()){
                    Log.wtf(TAG, "Failed to create file " + filePath.getAbsolutePath());
                }
            }

            //STEP 4: Write the object to the file.
            FileOutputStream fos = new FileOutputStream(filePath);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(_obj);
            fos.close();

        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public static Object readObject(Context context, String _filePath){
        Object objResult = null;
        try {
            //Step 1: Build the full path.
            File privateStorage = context.getFilesDir();
            File filePath = new File(privateStorage, _filePath);
            //Step 2: Read the object from the file.
            FileInputStream fis = new FileInputStream(filePath);
            ObjectInputStream ois = new ObjectInputStream(fis);
            objResult = ois.readObject();
            fis.close();
        }
        catch (ClassNotFoundException | IOException e){
            e.printStackTrace();
        }
        return objResult;
    }
}
