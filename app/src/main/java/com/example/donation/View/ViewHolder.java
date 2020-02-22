package com.example.donation.View;

import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.donation.R;
import com.squareup.picasso.Picasso;

public class ViewHolder extends RecyclerView.ViewHolder {

    View mview;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);

        mview = itemView;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mClickListener.onItemClick(view, getAdapterPosition());
            }
        });

       itemView.setOnLongClickListener(new View.OnLongClickListener() {
           @Override
           public boolean onLongClick(View view) {

               mClickListener.onItemClick(view, getAdapterPosition());
               return true; //false //New Code 14/01/63
       }
       });

    }

    public void setDetails(Context ctx, String name, String adress, String phonenumber) { //String image ใช้กับคู่ Picasso.get().load(image).into(mImage);

        TextView mName = mview.findViewById(R.id.rName);
        TextView madress = mview.findViewById(R.id.rAdress);//add New
        TextView mphonenumber = mview.findViewById(R.id.rPhonenumber);//add New

//        ImageView mImage = mview.findViewById(R.id.rImage);

        mName.setText(name);
        madress.setText(adress);//add New
        mphonenumber.setText(phonenumber);//add New

//        Picasso.get().load(image).into(mImage);

    }

    private ViewHolder.ClickListener mClickListener;


    public interface ClickListener {

        void onItemClick(View view,int position);
        void onItemLongClick(View view,int position);
    }

    public void setOnClickListener(ViewHolder.ClickListener clickListener) {

        mClickListener = clickListener;
    }

}
