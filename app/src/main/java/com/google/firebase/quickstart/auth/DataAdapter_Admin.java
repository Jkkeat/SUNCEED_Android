package com.google.firebase.quickstart.auth;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.quickstart.auth.models.Post;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.UrlConnectionDownloader;

import java.util.ArrayList;

public class DataAdapter_Admin extends RecyclerView.Adapter<DataAdapter_Admin.ViewHolder>
{
    private static final String TAG = "DataAdapter_Amin";
    private ArrayList<Post> PostVector;
    private Context context;
    private int postSelected_id


    public DataAdapter_Admin(Context context, ArrayList<Post> PostVector)
    {
        this.context = context;
        this.PostVector = PostVector;
    }
    @Override
    public void onBindViewHolder(final DataAdapter_Admin.ViewHolder viewHolder, final int i)
    {
        try
        {

            //Add to cart button start
            viewHolder.ChkBox_amdroid.setOnCheckedChangeListener(new OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    // TODO Auto-generated method stub

                    if (buttonView.isChecked())
                    {
                        Toast.makeText(context, "Post "+viewHolder.getAdapterPosition() + "Selected",Toast.LENGTH_SHORT);

                    }
                    else
                    {
                    }

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

    @Override
    public DataAdapter_Admin.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_post_image_admin, viewGroup, false);
        return new DataAdapter_Admin.ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView Title_android;
        TextView Description_android;
        TextView Price_android;
        ImageView img_android;
        CheckBox ChkBox_amdroid;

        public ViewHolder(View view)
        {
            super(view);
            Title_android = (TextView)view.findViewById(R.id.Title_android_admin);
            Description_android = (TextView)view.findViewById(R.id.Descrption_android_admin);
            Price_android = (TextView)view.findViewById(R.id.Price_android_admin);
            img_android = (ImageView)view.findViewById(R.id.img_android_admin);
            ChkBox_amdroid = (CheckBox)view.findViewById(R.id.Post_checkbox);
        }
    }
}
