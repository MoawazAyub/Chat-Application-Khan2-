package com.example.mr.khan2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

/**
 * Created by MR on 12/3/2017.
 */

public class ForwardContacts extends AppCompatActivity {

    TextView person_name,person_email;
    RecyclerView recyclerView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    public FirebaseRecyclerAdapter<Show_Chat_Activity_Data_items, ForwardContacts.Show_Chat_ViewHolder> mFirebaseAdapter;
    ProgressBar progressBar;
    LinearLayoutManager mLinearLayoutManager;
    private FirebaseAuth fbAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_chat_layout);


        fbAuth = FirebaseAuth.getInstance();



        // tx = (TextView) view.findViewById(R.id.ppp);
        //tx.setText("i am a good boh");

        firebaseDatabase = FirebaseDatabase.getInstance();

        myRef = firebaseDatabase.getReference("Users");
        myRef.keepSynced(true);


        progressBar = (ProgressBar) findViewById(R.id.show_chat_progressBar2);

        //Recycler View
        recyclerView = (RecyclerView) findViewById(R.id.show_chat_recyclerView);

        mLinearLayoutManager = new LinearLayoutManager(getApplicationContext());
        //mLinearLayoutManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(mLinearLayoutManager);

    }

    @Override
    public void onStart() {
        super.onStart();


        progressBar.setVisibility(ProgressBar.VISIBLE);
        //Log.d("LOGGED", "Will Start Calling populateViewHolder : ");
        //Log.d("LOGGED", "IN onStart ");


        mFirebaseAdapter = new FirebaseRecyclerAdapter<Show_Chat_Activity_Data_items, ForwardContacts.Show_Chat_ViewHolder>(Show_Chat_Activity_Data_items.class, R.layout.show_chat_single_item, ForwardContacts.Show_Chat_ViewHolder.class, myRef) {


            public void populateViewHolder(final ForwardContacts.Show_Chat_ViewHolder viewHolder, Show_Chat_Activity_Data_items model, final int position) {
                //Log.d("LOGGED", "populateViewHolder Called: ");
                progressBar.setVisibility(ProgressBar.INVISIBLE);

                if (!model.getName().equals("Null")) {
                    viewHolder.Person_Name(model.getName());
                    viewHolder.Person_Image(model.getThumb());
                    //viewHolder.Person_Email(model.getEmail());
                    //===============================

                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Myphone",getApplicationContext().getApplicationContext().MODE_PRIVATE);
                    String temp = sharedPreferences.getString("PhoneNum","");

                    //===============================

                    if(model.getPhone().equals(temp))
                    {
                        //viewHolder.itemView.setVisibility(View.GONE);
                        viewHolder.Layout_hide();

                        //recyclerView.getChildAdapterPosition(viewHolder.itemView.getRootView());
                        // viewHolder.itemView.set;


                    }
                    else
                        viewHolder.Person_Email(model.getStatus());
                }


                //OnClick Item
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(final View v) {

                        DatabaseReference ref = mFirebaseAdapter.getRef(position);
                        ref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                String retrieve_name = dataSnapshot.child("Name").getValue(String.class);
                                //String retrieve_Email = dataSnapshot.child("Email").getValue(String.class);
                                //String retrieve_url = dataSnapshot.child("Image_URL").getValue(String.class);
                                String retrieve_phone = dataSnapshot.child("Phone").getValue(String.class);


                                if(!ChatConversationActivity.forwardcon.contains(retrieve_phone))
                                {
                                    ChatConversationActivity.forwardcon.add(retrieve_phone);
                                    Toast t1 = Toast.makeText(getApplicationContext(),"Contact "+retrieve_name+" added to list",Toast.LENGTH_SHORT);
                                    t1.show();
                                }

                                else if(ChatConversationActivity.forwardcon.contains(retrieve_phone))
                                {
                                    ChatConversationActivity.forwardcon.remove(retrieve_phone);
                                    Toast t1 = Toast.makeText(getApplicationContext(),"Contact "+retrieve_name+" removed from list",Toast.LENGTH_SHORT);
                                    t1.show();
                                }




                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                });//-----------------------------------setting on click lisner



            }
        };

        recyclerView.setAdapter(mFirebaseAdapter);



    }


    //===================================================

    //View Holder For Recycler View
    public static class Show_Chat_ViewHolder extends RecyclerView.ViewHolder {
        private final TextView person_name, person_email;
        private final ImageView person_image;
        private final LinearLayout layout;
        final LinearLayout.LayoutParams params;
        static View mView;

        public Show_Chat_ViewHolder(final View itemView) {
            super(itemView);
            mView = itemView;
            person_name = (TextView) itemView.findViewById(R.id.chat_persion_name);
            person_email = (TextView) itemView.findViewById(R.id.chat_persion_email);
            person_image = (ImageView) itemView.findViewById(R.id.chat_persion_image);
            layout = (LinearLayout)itemView.findViewById(R.id.show_chat_single_item_layout);
            params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }




        private void Person_Name(String title) {
            // Log.d("LOGGED", "Setting Name: ");
            person_name.setText(title);
        }
        private void Layout_hide() {
            params.height = 0;
            //itemView.setLayoutParams(params);
            layout.setLayoutParams(params);

        }


        private void Person_Email(String title) {
            person_email.setText(title);
        }


        private void Person_Image(String thumbNail_image) {
/*
            if (!url.equals("Null")) {
                Glide.with(itemView.getContext())
                        .load(url)
                        .crossFade()
                        .thumbnail(0.5f)
                        .placeholder(R.drawable.arrow_blue_load_loading)
                        .bitmapTransform(new CircleTransform(itemView.getContext()))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(person_image);
            }/////*/

            //CircleImageView userImage =(CircleImageView)mView.findViewById(R.id.chat_persion_image);
            if(!thumbNail_image.equals("Null")) {
                Picasso.with(itemView.getContext()).load(thumbNail_image).placeholder(R.drawable.arrow_blue_load_loading).into(person_image);
            }

        }


    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();

        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }

    //====================================================
}
