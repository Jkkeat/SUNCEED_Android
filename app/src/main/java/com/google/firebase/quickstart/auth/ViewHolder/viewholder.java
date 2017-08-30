package com.google.firebase.quickstart.auth.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import com.google.firebase.quickstart.auth.R;
import com.google.firebase.quickstart.auth.models.Post;
import com.squareup.picasso.Picasso;
import android.content.Context;
public class viewholder extends RecyclerView.ViewHolder{

    public ImageView mImage;


    public Context context;

    public viewholder(View itemview, Context context)
    {
        super(itemview);
        this.context = context;
        mImage = (ImageView)itemview.findViewById(R.id.imageView);
    }

    public void bindToPost(Post post, View.OnClickListener starClickListener)
    {
        //Use pisaca to load photo
       // post.Post_URL;

        Picasso.with(context).load("http://i.imgur.com/DvpvklR.png").into(mImage);
        mImage.setOnClickListener(starClickListener);
    }

}
