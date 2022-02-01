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
 * Use the {@link ProductOtherDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductOtherDetailsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProductOtherDetailsFragment() {
        // Required empty public constructor
    }

    private RecyclerView productOtherDetailsRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_other_details, container, false);

        productOtherDetailsRecyclerView = view.findViewById(R.id.product_otherdetails_recyclerview);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(view.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        productOtherDetailsRecyclerView.setLayoutManager(linearLayoutManager);

        List<ProductOtherDetailsModel> productOtherDetailsModelList = new ArrayList<>();
        productOtherDetailsModelList.add(new ProductOtherDetailsModel("Category","Vegetables"));
        productOtherDetailsModelList.add(new ProductOtherDetailsModel("Stock","99"));
        productOtherDetailsModelList.add(new ProductOtherDetailsModel("Location","Pacita"));

        ProductOtherDetailsAdapter productOtherDetailsAdapter=new ProductOtherDetailsAdapter(productOtherDetailsModelList);
        productOtherDetailsRecyclerView.setAdapter(productOtherDetailsAdapter);
        productOtherDetailsAdapter.notifyDataSetChanged();

        return view;
    }
}