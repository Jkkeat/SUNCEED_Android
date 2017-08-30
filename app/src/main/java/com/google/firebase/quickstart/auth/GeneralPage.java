package com.google.firebase.quickstart.auth;

import android.support.v7.app.AppCompatActivity;
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

public class GeneralPage extends AppCompatActivity implements
View.OnClickListener
{
    private static final String TAG = "GeneralPage";
 
	private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private FirebaseRecyclerAdapter<Post, viewholder> mAdapter;
    private RecyclerView mRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        try
        {
            super.onCreate(savedInstanceState);

			mAuth = FirebaseAuth.getInstance();
            mDatabase = FirebaseDatabase.getInstance().getReference();
            setContentView(R.layout.activity_general_page);

            mRecycler = (RecyclerView)findViewById(R.id.card_recycler_view);
            mRecycler.setHasFixedSize(true);
            LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            mRecycler.setLayoutManager(layoutManager);

            ArrayList<Post> PostVector = prepareData();
            DataAdapter adapter = new DataAdapter(this,PostVector);
            mRecycler.setAdapter(adapter);            
        }
        catch(Exception e)
        {
            Log.e(TAG,e.getMessage());
        }
    }

    private ArrayList<Post> prepareData()
    {
        ArrayList<Post> PostVector = new ArrayList<>();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("/posts");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
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

    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.bttn_android:
               Toast.makeText(GeneralPage.this, "Hello", Toast.LENGTH_SHORT).show();
                break;

            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
	{
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		MenuItem Login = menu.findItem(R.id.action_login);
        MenuItem Logout = menu.findItem(R.id.action_logout);
        MenuItem Cart = menu.findItem(R.id.action_cart);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null)
        {
            //Already login
            Toast.makeText(GeneralPage.this, "Welcome" + mAuth.getCurrentUser(), Toast.LENGTH_SHORT).show();
			Login.setVisible(false);
            Logout.setVisible(true);
            Cart.setVisible(true);
        }		
		else
		{
            //not yet login
			Login.setVisible(true);
            Logout.setVisible(false);
            Cart.setVisible(false);
		}
		return true;
	}
	
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();

        switch (i) {
            case R.id.action_login:
                Intent intent = new Intent(this, EmailPasswordActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_cart:
                //TODO: cart page
                return true;

            case R.id.action_logout:
                mAuth.signOut();
                Toast.makeText(GeneralPage.this, "Signing out" , Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onStart() 
	{
        super.onStart();
        
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null)
        {
            ////////////////


            mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid());
            ValueEventListener PostListener = new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    User userdata = dataSnapshot.getValue(User.class);

                   if(userdata.getlogintype() == 1234)
                    {
                        Log.d(TAG, "Welcome Admin");
                        //Log.d(TAG, "LoginType = " + userdata.getlogintype());
                        //Intent AdminLoginIntent = new Intent(GeneralPage.this,AdminPage.class);
                        //startActivity(AdminLoginIntent);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError)
                {

                }
            };
            mDatabase.addValueEventListener(PostListener);
            ///////////////////
            Toast.makeText(GeneralPage.this, "Welcome" + mAuth.getCurrentUser(), Toast.LENGTH_SHORT).show();
        }
    }
}
