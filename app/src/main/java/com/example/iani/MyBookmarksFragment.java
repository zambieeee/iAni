package com.example.iani;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyBookmarksFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyBookmarksFragment extends Fragment {
    public MyBookmarksFragment(){
        //aasdf
    }

    private RecyclerView bookmarkRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_bookmarks, container, false);

        bookmarkRecyclerView = view.findViewById(R.id.my_bookmarks_recyclerview);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        bookmarkRecyclerView.setLayoutManager(linearLayoutManager);

        List<BookmarkModel>bookmarkModelList=new ArrayList<>();

        BookmarkAdapter bookmarkAdapter = new BookmarkAdapter(bookmarkModelList, true);
        bookmarkRecyclerView.setAdapter(bookmarkAdapter);
        bookmarkAdapter.notifyDataSetChanged();

        return view;
    }
}