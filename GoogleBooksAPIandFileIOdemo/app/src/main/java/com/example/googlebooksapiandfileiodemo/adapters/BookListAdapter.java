
package com.example.googlebooksapiandfileiodemo.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.googlebooksapiandfileiodemo.R;
import com.example.googlebooksapiandfileiodemo.objects.Book;

import java.util.ArrayList;

public class BookListAdapter extends BaseAdapter{

    private static final String TAG = "BookListAdapter";
    public static final int ID_CONSTANT = 0x01000000;

    final ArrayList<Book> mCollection;
    final Context mContext;

    //Constructor.
    public BookListAdapter(ArrayList<Book> mCollection, Context mContext) {
        this.mCollection = mCollection;
        this.mContext = mContext;
    }

    @Override
    //This returns the number of items int the collection.
    public int getCount() {
        if(mCollection != null){
            return mCollection.size();
        }
        else
        {
            Log.d(TAG, "Error getting count of items in collection");
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        //Return item at position
        if(mCollection != null && position >= 0 && position <= mCollection.size()){
            return mCollection.get(position);
        }
        else {
            Log.d(TAG, "Error getting item in collection");
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        // Return an item id for an item at a specified position.
        if(mCollection != null && position >= 0 && position <= mCollection.size()){
            return ID_CONSTANT + position;
        }
        else
        {
            Log.d(TAG, "getItemId: There was a problem assigning the ID");
            return 0;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Return a child view for an item at a specified position.
        if(convertView == null) {
            //Is the recyclable view we are using from our created context.
            convertView = LayoutInflater.from(mContext).inflate(R.layout.book_layout, parent, false);
        }

        Book book = (Book) getItem(position);

        if(position <= getCount())
        {
            TextView bookName = convertView.findViewById(R.id.textView);
            TextView bookPublisher = convertView.findViewById(R.id.textView2);

            bookName.setText(book.getTitle());

            String pub = book.getPublisher();
            if(pub.equals(""))
            {
                bookPublisher.setText("Unknown Publisher");
            }
            else
            {
                bookPublisher.setText(pub);
            }
        }

        return convertView;
    }
}
