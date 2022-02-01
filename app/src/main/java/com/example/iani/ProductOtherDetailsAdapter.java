package com.example.iani;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

public class ProductOtherDetailsAdapter extends RecyclerView.Adapter<ProductOtherDetailsAdapter.ViewHolder> {

    private List<ProductOtherDetailsModel> productOtherDetailsModelList;

    public ProductOtherDetailsAdapter(List<ProductOtherDetailsModel> productOtherDetailsModelList) {
        this.productOtherDetailsModelList = productOtherDetailsModelList;
    }

    @NonNull
    @Override
    public ProductOtherDetailsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_otherdetails_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductOtherDetailsAdapter.ViewHolder holder, int position) {
        String featureTitle = productOtherDetailsModelList.get(position).getFeatureName();
        String featureDetail = productOtherDetailsModelList.get(position).getFeatureValue();
        holder.setFeatures(featureTitle, featureDetail);
    }

    @Override
    public int getItemCount() {
        return productOtherDetailsModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView featureName;
        private TextView featureValue;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            featureName=itemView.findViewById(R.id.feature_name);
            featureValue=itemView.findViewById(R.id.feature_value);
        }
        private void setFeatures(String featureTitle, String featureDetail){
            featureName.setText(featureTitle);
            featureValue.setText(featureDetail);
        }
    }
}
