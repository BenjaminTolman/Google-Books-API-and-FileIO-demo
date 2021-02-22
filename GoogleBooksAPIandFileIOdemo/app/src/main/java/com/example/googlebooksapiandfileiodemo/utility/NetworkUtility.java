
package com.example.googlebooksapiandfileiodemo.utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import com.example.googlebooksapiandfileiodemo.MainActivity;
import com.example.googlebooksapiandfileiodemo.objects.Book;

import org.apache.commons.io.IOUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

public class NetworkUtility {

    private final Context mContext;

    public NetworkUtility(Context mContext) {

        this.mContext = mContext;
    }

    public void beginQuery(String queryLink){
        //Get connectivity manager to test connectivity first.
        ConnectivityManager mgr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        if(mgr != null) //Could be any connection so test network inside here.
        {
            //Check for wifi and the like.
            NetworkInfo info = mgr.getActiveNetworkInfo();

            if(info != null){

                boolean isConnected = info.isConnected();

                if(isConnected)
                {
                    DataTask task = new DataTask();
                    task.execute(queryLink);
                }
            }

            //If not connected then give a warning box and also build list from data.
            else {
                Toast.makeText(mContext, "Not connected, showing stored data.", Toast.LENGTH_SHORT).show();

                //Return this here to build the list in MainActivity.
                MainActivity.getInstance().finishedAsync();
            }
        }
    }

    //This will get the data from the link.
    private String getNetworkData(String urlString){

        try{
            //Get the url string at the start from arg
            URL url = new URL(urlString);

            //Set connection as URL openConnection.
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

            //Connect
            connection.connect();

            //Create Input Stream
            InputStream is = connection.getInputStream();

            //Get string from input stream
            String data = IOUtil.toString(is);

            //Close input stream
            is.close();

            //Disconnect
            connection.disconnect();

            return data;

        }

        //Catch exception, print errors and return null.
        catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    private class DataTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            //Get the network data for the first string(URL) in the args.
            return getNetworkData(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try{
                //Create JSON object from returned json text.
                JSONObject outerMostObject = new JSONObject(s);

                //Get the items array from inside the outerMostObject
                JSONArray items = outerMostObject.getJSONArray("items");

                //Cycle through the items and create books from the parsed data.
                for(int i = 0; i < items.length(); i++){
                    JSONObject item = items.getJSONObject(i);
                    JSONObject volumeInfo = item.getJSONObject("volumeInfo");

                    String title = "";
                    String publisher = "";

                    if(volumeInfo.has("title")){
                        title = volumeInfo.getString("title");
                    }
                    if(volumeInfo.has("publisher"))
                    {
                        publisher = volumeInfo.getString("publisher");
                    }

                    boolean collected = false;
                    for (int b=0; b<MainActivity.books.size(); b++) {
                        if(MainActivity.books.get(b).getTitle().equals(title)){
                            collected = true;
                            Log.d("TITLE", "did NOT add " + title );
                        }
                    }
                    if(!collected)
                    {
                        MainActivity.books.add(new Book(title, publisher, MainActivity.currentSubject));
                        Log.d("TITLE", "Added " + title );
                    }
                }

                //Return this here to build the list in MainActivity.
                //After adding any new items write the file to list.
                MainActivity.getInstance().finishedAsync();
            }

            catch(JSONException e)
            {
                e.printStackTrace();
            }
        }
    }
}
