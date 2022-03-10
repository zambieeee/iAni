package com.example.iani;

import android.app.Dialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.w3c.dom.Text;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter {

    private List<CartItemModel> cartItemModelList;
    private int lastPosition = -1;
    private TextView cartTotalAmount;
    private boolean showDeleteBtn;

    public CartAdapter(List<CartItemModel> cartItemModelList, TextView cartTotalAmount, boolean showDeleteBtn) {
        this.cartItemModelList = cartItemModelList;
        this.cartTotalAmount = cartTotalAmount;
        this.showDeleteBtn = showDeleteBtn;
    }

    @Override
    public int getItemViewType(int position) {
        switch (cartItemModelList.get(position).getType()){
            case 0:
                return CartItemModel.CART_ITEM;
            case 1:
                return CartItemModel.TOTAL_AMOUNT;
            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType){
            case CartItemModel.CART_ITEM:
                View cartItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout,parent, false);
                return new CartItemViewholder(cartItemView);
            case CartItemModel.TOTAL_AMOUNT:
                View cartTotalView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_total_amount_layout,parent, false);
                return new CartTotalAmountViewholder(cartTotalView);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (cartItemModelList.get(position).getType()){
            case CartItemModel.CART_ITEM:
                String productID = cartItemModelList.get(position).getProductID();
                String resource = cartItemModelList.get(position).getProductImage();
                String title = cartItemModelList.get(position).getProductTitle();
                String productPrice = cartItemModelList.get(position).getProductPrice();
                boolean inStock = cartItemModelList.get(position).isInStock();

                ((CartItemViewholder)holder).setItemDetails(productID,resource,title,productPrice,position,inStock);
                break;
            case CartItemModel.TOTAL_AMOUNT:
                int totalItems = 0;
                int totalItemPrice = 0;
                String deliveryPrice;
                int totalAmount;

                for(int x = 0;x < cartItemModelList.size(); x++){

                    if(cartItemModelList.get(x).getType() == CartItemModel.CART_ITEM && cartItemModelList.get(x).isInStock()){
                        totalItems++;
                        totalItemPrice = totalItemPrice + Integer.parseInt(cartItemModelList.get(x).getProductPrice());
                    }
                }

                if(totalItemPrice > 500) {
                    deliveryPrice = "FREE";
                    totalAmount = totalItemPrice;
                }else {
                    deliveryPrice = "60";
                    totalAmount = totalItemPrice + 60;
                }

                ((CartTotalAmountViewholder)holder).setTotalAmount(totalItems,totalItemPrice,deliveryPrice,totalAmount);
                break;
            default:
                return;
        }

        if(lastPosition<position){
            Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.fade_in);
            holder.itemView.setAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return cartItemModelList.size();
    }

    class CartItemViewholder extends RecyclerView.ViewHolder{

        private ImageView productImage;
        private TextView productTitle;
        private TextView productPrice;
        private TextView productQuantity;

        private LinearLayout deleteBtn;

        public CartItemViewholder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image);
            productTitle=itemView.findViewById(R.id.product_titlecart);
            productPrice=itemView.findViewById(R.id.product_pricecart);
            productQuantity=itemView.findViewById(R.id.product_quantity);

            deleteBtn = itemView.findViewById(R.id.remove_item_btn);
        }

        private void setItemDetails (String productID,String resource, String title, String productPriceText, int position, boolean inStock){
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.mipmap.placeholder_image)).into(productImage);
            productTitle.setText(title);

            if (inStock) {
                productPrice.setText("PHP " + productPriceText);
                productPrice.setTextColor(Color.parseColor("#000000"));

                productQuantity.setOnClickListener(v -> {
                    final Dialog quantityDialog = new Dialog(itemView.getContext());
                    quantityDialog.setContentView(R.layout.quantity_dialog);
                    quantityDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                    quantityDialog.setCancelable(false);
                    final EditText quantityNo = quantityDialog.findViewById(R.id.dialog_quantity);
                    Button cancelBtn = quantityDialog.findViewById(R.id.dialog_cancel_btn);
                    Button okBtn = quantityDialog.findViewById(R.id.dialog_ok_btn);

                    cancelBtn.setOnClickListener(v1 -> quantityDialog.dismiss());

                    okBtn.setOnClickListener(v12 -> {
                        productQuantity.setText("Qty: " + quantityNo.getText());
                        quantityDialog.dismiss();
                    });
                    quantityDialog.show();
                });
            }else {
                productPrice.setText("Out of Stock");
                productPrice.setTextColor(itemView.getContext().getResources().getColor(R.color.colorPrimary));

                productQuantity.setVisibility(View.INVISIBLE);
            }

            if(showDeleteBtn){
                deleteBtn.setVisibility(View.VISIBLE);
            }else {
                deleteBtn.setVisibility(View.GONE);
            }

            deleteBtn.setOnClickListener(v -> {
                 if(!ProductDetailsActivity.running_cart_query){
                     ProductDetailsActivity.running_cart_query = true;
                     DBqueries.removeFromCart(position, itemView.getContext(), cartTotalAmount);
                 }
            });

        }
    }

    class CartTotalAmountViewholder extends RecyclerView.ViewHolder{

        private TextView totalItems;
        private TextView totalItemPrice;
        private TextView deliveryPrice;
        private TextView totalAmount;

        public CartTotalAmountViewholder(@NonNull View itemView) {
            super(itemView);

            totalItems=itemView.findViewById(R.id.total_items);
            totalItemPrice=itemView.findViewById(R.id.total_items_price);
            deliveryPrice=itemView.findViewById(R.id.delivery_price);
            totalAmount = itemView.findViewById(R.id.total_price);
        }
        private void setTotalAmount(int totalItemText, int totalItemPriceText, String deliveryPriceText, int totalAmountText){
            totalItems.setText("Price (" + totalItemText + " items)");
            totalItemPrice.setText("PHP " + totalItemPriceText);
            if(deliveryPriceText.equals("FREE")){
                deliveryPrice.setText(deliveryPriceText);
            }else {
                deliveryPrice.setText("PHP "+ deliveryPriceText);
            }
            totalAmount.setText("PHP " + Integer.toString(totalAmountText));
            cartTotalAmount.setText("PHP " + Integer.toString(totalAmountText));

            LinearLayout parent = (LinearLayout) cartTotalAmount.getParent().getParent();
            if (totalItemPriceText == 0){
                DBqueries.cartItemModelList.remove(DBqueries.cartItemModelList.size() - 1);
                parent.setVisibility(View.GONE);
            }else {
                parent.setVisibility(View.VISIBLE);
            }

        }
    }
}
