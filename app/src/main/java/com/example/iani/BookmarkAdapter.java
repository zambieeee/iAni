package com.example.iani;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
        String resource = bookmarkModelList.get(position).getProductImage();
        String title= bookmarkModelList.get(position).getProductTitle();
        String rating= bookmarkModelList.get(position).getRating();
        long totalRatings= bookmarkModelList.get(position).getTotalRatings();
        String productPrice= bookmarkModelList.get(position).getProductPrice();
        boolean paymentMethod= bookmarkModelList.get(position).isCOD();
        holder.setData(resource,title,rating,totalRatings,productPrice,paymentMethod);

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
            rating=itemView.findViewById(R.id.bookmark_tv_product_rating_miniview);
            totalRatings=itemView.findViewById(R.id.bookmark_total_ratings);
            productPrice=itemView.findViewById(R.id.bookmark_product_price);
            paymentMethod=itemView.findViewById(R.id.bookmark_payment_method);
            deleteBtn=itemView.findViewById(R.id.bookmark_delete_btn);
        }

        private void setData(String resource, String title, String averageRate, long totalRatingsNo, String price, boolean COD){
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.home)).into(productImage);
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

            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(itemView.getContext(), "delete", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
