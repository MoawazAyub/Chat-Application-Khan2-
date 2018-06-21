package com.example.mr.khan2;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class userProfile extends AppCompatActivity {

    private ImageView profileImage;
    private TextView profileStatus;
    private TextView profileName;
    private TextView profileFriends;
    private ProgressDialog progressDialog;

    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        String userID = getIntent().getStringExtra("userID");

        dbRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
        profileImage = (ImageView)findViewById(R.id.profile_image_imageView);
        profileName = (TextView)findViewById(R.id.profile_name_text_view);
        profileStatus = (TextView)findViewById(R.id.profile_status_text_view);
        profileFriends = (TextView)findViewById(R.id.profile_friends_text_view);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading User Data");
        progressDialog.setMessage("User data is being loaded...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("Name").getValue().toString();
                String status = dataSnapshot.child("Status").getValue().toString();
                String image = dataSnapshot.child("Image_Url").getValue().toString();

                profileName.setText(name);
                profileStatus.setText(status);

                Picasso.with(userProfile.this).load(image).placeholder(R.drawable.arrow_blue_load_loading).into(profileImage);

                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
