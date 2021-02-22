// Tolman Benjamin
// JAV2 - C202101 02
// MainActivity.java

//CHANGES
// 1 - Classes have been added to packages.
// 2 - Added a fragment for the book list.
// 3 - Changed program flow to be more efficient.
// 4 - Removed needless spinner option (Select a book)
// 5 - Network separated to it's own class.

package com.example.googlebooksapiandfileiodemo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.googlebooksapiandfileiodemo.fragments.BookListFragment;
import com.example.googlebooksapiandfileiodemo.objects.Book;
import com.example.googlebooksapiandfileiodemo.utility.FileUtility;
import com.example.googlebooksapiandfileiodemo.utility.NetworkUtility;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static ArrayList<Book> books = new ArrayList<>();
    private final ArrayList<Book> currentBooks = new ArrayList<>();

    private static MainActivity instance;

    public static String currentSubject = "";

    private Spinner spinner;

    private TextView noData;

    private NetworkUtility nwu;

    //Path: "/Books/BookList.obj"
    private static final String FILE_NAME = "booklist.obj";
    private static final String FOLDER_NAME = "books";
    public static final String FILE_PATH = File.separator + FOLDER_NAME + File.separator + FILE_NAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        instance = this;

        spinner = findViewById(R.id.spinner);

        //Create and ArrayAdapter using the string array in strings and a default spinner layout.
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.queries, android.R.layout.simple_spinner_item);
        //Specify the layout to use when the list of choices appears.
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Apply the adapter and on select to the spinner
        spinner.setAdapter(adapter);
        spinner.setSelection(0,false);
        spinner.setOnItemSelectedListener(this);

        noData = findViewById(R.id.no_data);
        noData.setVisibility(View.GONE);
        //writeFile(); <--- USE to overwrite with empty file for debug.
        readFile();

        TextView textView = (TextView)spinner.getSelectedView();
        String result = textView.getText().toString();
        currentSubject = result;

        nwu = new NetworkUtility(this);

        String base = "https://www.googleapis.com/books/v1/volumes?q=";

        //Start query and then build list.
        //nwu will check for connection and return to
        //write file and build list.
        nwu.beginQuery(base + result);
    }

    public static MainActivity getInstance() {
        return instance;
    }

    public void writeFile(){
        //Create books list from current list of books and write to file.
        FileUtility.writeObject(MainActivity.this, books, FILE_PATH);
    }

    public void readFile(){
        Object obj = FileUtility.readObject(MainActivity.this, FILE_PATH);
        //Load file and parse it to ArrayList of books.
        if(obj instanceof ArrayList) books = (ArrayList<Book>) obj;
    }

    private void buildList(){

        currentBooks.clear();

        for (int b=0; b<books.size(); b++) {
            if(books.get(b).getSubject().equals(currentSubject)){
                currentBooks.add(books.get(b));
                Log.d("COUNT", "current books " + currentBooks.size());
            }
        }

        if(currentBooks.size() >0)
        {
           noData.setVisibility(View.GONE);
        }
        else
        {
           noData.setVisibility(View.VISIBLE);
        }

        getSupportFragmentManager().beginTransaction().replace(
                R.id.list_fragment_holder,
                BookListFragment.newInstance(currentBooks)
        ).commit();
    }

    //These are for spinner --------------------------
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            //Get the text from the spinner selection.
            TextView textView = (TextView)spinner.getSelectedView();
            String result = textView.getText().toString();
            currentSubject = result;

            String base = "https://www.googleapis.com/books/v1/volumes?q=";

            nwu.beginQuery(base + result);
        }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    //spinner ----------------------------------------

    //This gets called from NetworkUtility after asyncTask or if no connection.
    public void finishedAsync(){

        writeFile();
        buildList();
    }
}