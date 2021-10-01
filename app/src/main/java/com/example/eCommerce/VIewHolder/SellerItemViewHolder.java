package com.example.eCommerce.VIewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eCommerce.Interface.ItemClickListener;
import com.example.eCommerce.R;


public class SellerItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView productItemName,productItemDescription,productItemPrice,productItemStatus;
    public ImageView productItemImage;
    public ItemClickListener itemClickListener;

    public SellerItemViewHolder(@NonNull View itemView) {
        super(itemView);

        productItemImage=itemView.findViewById(R.id.seller_product_item_image);
        productItemName=itemView.findViewById(R.id.seller_product_item_name);
        productItemDescription=itemView.findViewById(R.id.seller_product_item_description);
        productItemPrice=itemView.findViewById(R.id.seller_product_item_price);
        productItemStatus=itemView.findViewById(R.id.seller_product_item_state);

    }

    public void setItemClickListener(ItemClickListener listener){
        this.itemClickListener=listener;
    }


    @Override
    public void onClick(View v) {

        itemClickListener.onClick(v,getAdapterPosition(),false);
    }


}

