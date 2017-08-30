package com.google.firebase.quickstart.auth;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.quickstart.auth.models.Post;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.UrlConnectionDownloader;

import java.util.ArrayList;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder>
{
    private static final String TAG = "DataAdapter";
    private ArrayList<Post> PostVector;
    private Context context;

    public DataAdapter(Context context,ArrayList<Post> PostVector)
    {
        this.context = context;
        this.PostVector = PostVector;
    }

    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_post_image, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i)
    {
        try
        {
            //Add to cart button start
            viewHolder.bttn_amdroid.setOnClickListener(new View.OnClickListener() {
                                                           @Override
                                                           public void onClick(View view)
                                                           {
                                                               Toast.makeText(context, "Item "+ PostVector.get(i).getPostTitle()+"has been added to cart", Toast.LENGTH_SHORT).show();
                                                               //TODO: add cart item to current user

                                                           }
                                                       });

            //Text Title
            viewHolder.Title_android.setText(PostVector.get(i).getPostTitle());

            //Text Description
            viewHolder.Description_android.setText(PostVector.get(i).getPostDescription());

            //Text price
            viewHolder.Price_android.setText("RM "+PostVector.get(i).getPostPrice_String());

            //Image
            String imageUrl = PostVector.get(i).getPostURL();
            Picasso picasso = new Picasso.Builder(context).downloader(new UrlConnectionDownloader(context)).listener(new Picasso.Listener()
            {
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception e){
                    e.printStackTrace();
                }
            }).build();

            picasso.load(imageUrl)
                    .error(R.drawable.ic_person_white)
                    .fit()
                    .centerCrop()
                    .into(viewHolder.img_android, new Callback() {
                        @Override
                        public void onSuccess()
                        {
                            Log.d(TAG,"Success");
                        }

                        @Override
                        public void onError() {
                            Log.d(TAG,"Failed");
                        }
                    });


        }
        catch(Exception e)
        {
            Log.e(TAG,e.getMessage());
        }
    }

    @Override
    public int getItemCount()
    {
        return PostVector.size();
    }

    public void onClick(View view) {
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView Title_android;
        TextView Description_android;
        TextView Price_android;
        ImageView img_android;
        ImageButton bttn_amdroid;

        public ViewHolder(View view)
        {
            super(view);
            Title_android = (TextView)view.findViewById(R.id.Title_android);
            Description_android = (TextView)view.findViewById(R.id.Descrption_android);
            Price_android = (TextView)view.findViewById(R.id.Price_android);
            img_android = (ImageView)view.findViewById(R.id.img_android);
            bttn_amdroid = (ImageButton)view.findViewById(R.id.bttn_android);
        }
    }
}
