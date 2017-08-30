package com.google.firebase.quickstart.auth;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.quickstart.auth.models.Post;
import com.google.firebase.quickstart.auth.ViewHolder.viewholder;
import com.google.firebase.quickstart.auth.models.User;

import java.util.ArrayList;

public class AdminPage extends AppCompatActivity
{

    private static final String TAG = "AdminPage";

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private FirebaseRecyclerAdapter<Post, viewholder> mAdapter;
    private RecyclerView mRecycler;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        try
        {
            mAuth = FirebaseAuth.getInstance();
            mDatabase = FirebaseDatabase.getInstance().getReference();
            setContentView(R.layout.activity_admin_page);

            mRecycler = (RecyclerView)findViewById(R.id.card_recycler_view_Admin);
            mRecycler.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            mRecycler.setLayoutManager(layoutManager);

            ArrayList<Post> PostVector = prepareData();
            DataAdapter_Admin adapter = new DataAdapter_Admin(this,PostVector);
            mRecycler.setAdapter(adapter);

        }
        catch(Exception e)
        {
            Log.d(TAG,e.getMessage());
        }
    }

    private ArrayList<Post> prepareData()
    {
        ArrayList<Post> PostVector = new ArrayList<>();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("/posts");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                Post post = dataSnapshot.getValue(Post.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        Post post_obj = new Post("https://api.learn2crack.com/android/images/donut.png",
                50,
                1,
                true,
                "Donut",
                "A delicious donut");
        PostVector.add(post_obj);

        Post post_obj1 = new Post("https://api.learn2crack.com/android/images/eclair.png",
                20.5,
                2,
                true,
                "Cake",
                "A delicious and colourful cake");
        PostVector.add(post_obj1);

        Post post_obj2 = new Post("https://api.learn2crack.com/android/images/froyo.png",
                40.6,
                3,
                true,
                "Bread",
                "A tasty bread");
        PostVector.add(post_obj2);

        Post post_obj3 = new Post("https://www.incredibleegg.org/wp-content/themes/incredibleegg/assets/images/facts-density.png",
                44.6,
                4,
                true,
                "Vege",
                "A eggPlant");
        PostVector.add(post_obj3);

        Post post_obj4 = new Post("https://www.incredibleegg.org/wp-content/themes/incredibleegg/assets/images/facts-main-egg.png",
                50.6,
                5,
                true,
                "Egg",
                "A Good");
        PostVector.add(post_obj4);

        return PostVector;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop()
    {
        super.onStop();
    }


}
