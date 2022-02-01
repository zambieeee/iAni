package com.example.iani;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.Viewholder>{

    private List<MyOrderItemModel> myOrderItemModelList;

    public MyOrderAdapter(List<MyOrderItemModel> myOrderItemModelList){
        this.myOrderItemModelList=myOrderItemModelList;
    }

    @NonNull
    @Override
    public MyOrderAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_order_item_layout, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyOrderAdapter.Viewholder holder, int position) {
        int resource = myOrderItemModelList.get(position).getProductImage();
        int rating = myOrderItemModelList.get(position).getRating();
        String title=myOrderItemModelList.get(position).getProductTitle();
        String deliveredDate = myOrderItemModelList.get(position).getDeliveryStatus();
        holder.setData(resource,title,deliveredDate,rating);
    }

    @Override
    public int getItemCount() {
        return myOrderItemModelList.size();
    }

    class Viewholder extends RecyclerView.ViewHolder{

        private ImageView productImage;
        private ImageView orderIndicator;
        private TextView productTitle;
        private TextView deliveryStatus;
        private LinearLayout rateContainer;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            productImage=itemView.findViewById(R.id.product_image_myorders);
            productTitle=itemView.findViewById(R.id.product_title_myorders);
            orderIndicator=itemView.findViewById(R.id.order_status_indicator);
            deliveryStatus=itemView.findViewById(R.id.order_delivered_date);
            rateContainer = itemView.findViewById(R.id.rate_now_container);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent orderDetailsIntent = new Intent(itemView.getContext() ,OrderDetailsActivity.class);
                    itemView.getContext().startActivity(orderDetailsIntent);
                }
            });
        }
        private void setData(int resource, String title, String deliveredDate, int rating){
            productImage.setImageResource(resource);
            productTitle.setText(title);
            if (deliveredDate.equals("Cancelled")){
                orderIndicator.setImageTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.Red)));
            }else {
                orderIndicator.setImageTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.colorPrimary)));
            }
            deliveryStatus.setText(deliveredDate);

            //rating layout
            setRating(rating);
            for (int x = 0;x<rateContainer.getChildCount();x++){
                final int starPosition = x;
                rateContainer.getChildAt(x).setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        setRating(starPosition);
                    }
                });
            }
            //rating layout
        }
        private void setRating(int starPosition) {
            for (int x=0;x<rateContainer.getChildCount();x++){
                ImageView starBtn = (ImageView)rateContainer.getChildAt(x);
                starBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#bebebe")));
                if (x<=starPosition){
                    starBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#65bc60")));
                }
            }
        }
    }
}
