package com.example.iani;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ProductDetailsAdapter extends FragmentPagerAdapter {

    private int totalTabs;

    public ProductDetailsAdapter(@NonNull FragmentManager fm, int totalTabs) {
        super(fm);
        this.totalTabs=totalTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int i) {
        switch (i){
            case 0:
                ProductDescriptionFragment productDescriptionFragment1 = new ProductDescriptionFragment();
                return productDescriptionFragment1;
            case 1:
                ProductOtherDetailsFragment productOtherDetailsFragment = new ProductOtherDetailsFragment();
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
