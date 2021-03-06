package com.example.mr.khan2;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class FriendsFragment extends Fragment {

    TextView person_name,person_email;
    RecyclerView recyclerView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    public FirebaseRecyclerAdapter<Show_Chat_Activity_Data_items, FriendsFragment.Show_Chat_ViewHolder> mFirebaseAdapter;
    ProgressBar progressBar;
    LinearLayoutManager mLinearLayoutManager;
    private FirebaseAuth fbAuth;



    public FriendsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        fbAuth = FirebaseAuth.getInstance();

        View view = inflater.inflate(R.layout.fragment_friends, container, false);

        // tx = (TextView) view.findViewById(R.id.ppp);
        //tx.setText("i am a good boh");

        firebaseDatabase = FirebaseDatabase.getInstance();

        myRef = firebaseDatabase.getReference("Users");
        myRef.keepSynced(true);


        progressBar = (ProgressBar) view.findViewById(R.id.show_chat_progressBar3);

        //pppppppppppppppppppppppppppppppppppp

        FloatingActionButton fab = (FloatingActionButton)view.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //           Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //                   .setAction("Action", null).show();

                //                 Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                //                  startActivityForResult(intent, 1);
                //                Toast.makeText(getContext(), "The username is invalid.", Toast.LENGTH_SHORT).show();
                Intent i;
                i = new Intent(Intent.ACTION_INSERT);
                i.setType(ContactsContract.Contacts.CONTENT_TYPE);
                if (Integer.valueOf(Build.VERSION.SDK) > 14)
                    i.putExtra("finishActivityOnSaveCompleted", true); // Fix for 4.0.3 +
                startActivityForResult(i, 1);
            }
        });


        //pppppppppppppppppppppppppp

        //Recycler View
        recyclerView = (RecyclerView)view.findViewById(R.id.show_chat_recyclerView1);

        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        //mLinearLayoutManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(mLinearLayoutManager);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();


        progressBar.setVisibility(ProgressBar.VISIBLE);
        //Log.d("LOGGED", "Will Start Calling populateViewHolder : ");
        //Log.d("LOGGED", "IN onStart ");


        mFirebaseAdapter = new FirebaseRecyclerAdapter<Show_Chat_Activity_Data_items, FriendsFragment.Show_Chat_ViewHolder>(Show_Chat_Activity_Data_items.class, R.layout.show_chat_single_item, FriendsFragment.Show_Chat_ViewHolder.class, myRef) {


            public void populateViewHolder(final FriendsFragment.Show_Chat_ViewHolder viewHolder, Show_Chat_Activity_Data_items model, final int position) {
                //Log.d("LOGGED", "populateViewHolder Called: ");
                progressBar.setVisibility(ProgressBar.INVISIBLE);

                if (!model.getName().equals("Null")) {
                    viewHolder.Person_Name(model.getName());
                    viewHolder.Person_Image(model.getThumb());
                    //viewHolder.Person_Email(model.getEmail());
                    //===============================

                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Myphone",getActivity().getApplicationContext().MODE_PRIVATE);
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

                                //String retrieve_name = dataSnapshot.child("Name").getValue(String.class);
                                //String retrieve_Email = dataSnapshot.child("Email").getValue(String.class);
                                //String retrieve_url = dataSnapshot.child("Image_URL").getValue(String.class);
                                String retrieve_phone = dataSnapshot.child("Phone").getValue(String.class);



                                Intent intent = new Intent(getActivity(), userProfile.class);
                                //intent.putExtra("image_id", retrieve_url);
                                //intent.putExtra("email", retrieve_Email);
                                //intent.putExtra("name", retrieve_name);
                                intent.putExtra("userID", retrieve_phone);


                                startActivity(intent);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                });
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

    //====================================================


}
