package com.example.googlebooksapiandfileiodemo.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.googlebooksapiandfileiodemo.adapters.BookListAdapter;
import com.example.googlebooksapiandfileiodemo.objects.Book;
import com.example.googlebooksapiandfileiodemo.R;
import com.example.googlebooksapiandfileiodemo.fragments.BookListFragment;

import java.util.ArrayList;

public class BookListFragment extends Fragment {

    public static final String ARG_ID = "ARG_ID";

    public static BookListFragment newInstance(ArrayList<Book> books) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_ID, books);
        BookListFragment fragment = new BookListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.book_list_fragment, container, false);

        Bundle args = getArguments();
        ArrayList<Book> booksList = new ArrayList<>();
        if(args != null){
           booksList = (ArrayList<Book>)args.getSerializable(ARG_ID);
        }

        //rebuild list
        BookListAdapter bookAdapter = new BookListAdapter(booksList, getContext());
        ListView books = view.findViewById(R.id.books_list);
        books.setAdapter(bookAdapter);

        return view;
    }
}
