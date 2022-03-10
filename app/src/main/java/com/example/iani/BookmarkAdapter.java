package com.example.iani;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.ViewHolder> {

    private List<BookmarkModel> bookmarkModelList;
    private Boolean bookmark;
    private int lastPosition =-1;

    public BookmarkAdapter(List<BookmarkModel> bookmarkModelList, Boolean bookmark){
        this.bookmarkModelList = bookmarkModelList;
        this.bookmark = bookmark;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.bookmarked_items_layout,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String productId = bookmarkModelList.get(position).getProductId();
        String resource = bookmarkModelList.get(position).getProductImage();
        String title= bookmarkModelList.get(position).getProductTitle();
        String rating= bookmarkModelList.get(position).getRating();
        long totalRatings= bookmarkModelList.get(position).getTotalRatings();
        String productPrice= bookmarkModelList.get(position).getProductPrice();
        boolean paymentMethod= bookmarkModelList.get(position).isCOD();
        holder.setData(productId,resource,title,rating,totalRatings,productPrice,paymentMethod, position);

        if(lastPosition<position){
            Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.fade_in);
            holder.itemView.setAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return bookmarkModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView productImage;
        private TextView productTitle;
        private TextView rating;
        private TextView totalRatings;
        private TextView productPrice;
        private TextView paymentMethod;
        private ImageView deleteBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage=itemView.findViewById(R.id.bookmark_product_image);
            productTitle=itemView.findViewById(R.id.bookmark_product_title);
            rating=itemView.findViewById(R.id.tv_product_rating_miniview);
            totalRatings=itemView.findViewById(R.id.total_ratings);
            productPrice=itemView.findViewById(R.id.bookmark_product_price);
            paymentMethod=itemView.findViewById(R.id.bookmark_payment_method);
            deleteBtn=itemView.findViewById(R.id.bookmark_delete_btn);
        }

        private void setData(String productId, String resource, String title, String averageRate, long totalRatingsNo, String price, boolean COD, int index){
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.mipmap.placeholder_image)).into(productImage);
            productTitle.setText(title);
            rating.setText(averageRate);
            totalRatings.setText("("+totalRatingsNo+")ratings");
            productPrice.setText("PHP "+price);
            if(COD){
                paymentMethod.setVisibility(View.VISIBLE);
            }else {
                paymentMethod.setVisibility(View.INVISIBLE);
            }

            if (bookmark){
                deleteBtn.setVisibility(View.VISIBLE);
            }else {
                deleteBtn.setVisibility(View.GONE);
            }

            deleteBtn.setOnClickListener(v -> {
                if(!ProductDetailsActivity.running_wishlist_query) {
                    ProductDetailsActivity.running_wishlist_query = true;
                    DBqueries.removeFromBookmarks(index, itemView.getContext());
                }
            });
            itemView.setOnClickListener(v -> {
                Intent productDetailsIntent = new Intent(itemView.getContext(),ProductDetailsActivity.class);
                productDetailsIntent.putExtra("PRODUCT_ID", productId);
                itemView.getContext().startActivity(productDetailsIntent);
            });
        }
    }
}
