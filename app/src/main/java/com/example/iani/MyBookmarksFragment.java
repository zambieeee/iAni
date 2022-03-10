package com.example.iani;

import android.app.Dialog;
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
    private Dialog loadingDialog;
    public static BookmarkAdapter bookmarkAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_bookmarks, container, false);

        ///loading dialog
        loadingDialog = new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getContext().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();
        ///loading dialog

        bookmarkRecyclerView = view.findViewById(R.id.my_bookmarks_recyclerview);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        bookmarkRecyclerView.setLayoutManager(linearLayoutManager);

        if(DBqueries.bookmarkModelList.size() == 0){
            DBqueries.bookmarks.clear();
            DBqueries.loadBookmarks(getContext(),loadingDialog, true);
        }else {
            loadingDialog.dismiss();
        }

        bookmarkAdapter = new BookmarkAdapter(DBqueries.bookmarkModelList, true);
        bookmarkRecyclerView.setAdapter(bookmarkAdapter);
        bookmarkAdapter.notifyDataSetChanged();

        return view;
    }
}