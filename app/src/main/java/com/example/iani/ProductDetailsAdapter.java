package com.example.iani;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class ProductDetailsAdapter extends FragmentPagerAdapter {

    private int totalTabs;
    private String productDescription;
    private List<ProductOtherDetailsModel> productOtherDetailsModelList;

    public ProductDetailsAdapter(@NonNull FragmentManager fm, int totalTabs, String productDescription, List<ProductOtherDetailsModel> productOtherDetailsModelList) {
        super(fm);
        this.totalTabs = totalTabs;
        this.productDescription = productDescription;
        this.productOtherDetailsModelList = productOtherDetailsModelList;
    }

    @NonNull
    @Override
    public Fragment getItem(int i) {
        switch (i){
            case 0:
                ProductDescriptionFragment productDescriptionFragment1 = new ProductDescriptionFragment();
                productDescriptionFragment1.body = productDescription;
                return productDescriptionFragment1;
            case 1:
                ProductOtherDetailsFragment productOtherDetailsFragment = new ProductOtherDetailsFragment();
                productOtherDetailsFragment.productOtherDetailsModelList = productOtherDetailsModelList;
                return productOtherDetailsFragment;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
