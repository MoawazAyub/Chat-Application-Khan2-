package com.example.mr.khan2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class SettingsActivity extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    private TextView userName;
    private TextView userStaus;
    private CircleImageView image;

    private Button userChangeImage;
    private Button userChangeStatus;
    private Button userChangeName;
    public static final int GALLERY_PICK = 1;
    private ProgressDialog progressDialog;

    private StorageReference imageStore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        userName = (TextView) findViewById(R.id.textView4);
        userStaus = (TextView) findViewById(R.id.textView5);
        image = (CircleImageView) findViewById(R.id.circleImageView);

        userChangeStatus = (Button) findViewById(R.id.setting_update_status);
        userChangeImage = (Button) findViewById(R.id.update_photo);
        userChangeName = (Button) findViewById(R.id.settings_update_name);
        imageStore = FirebaseStorage.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();



        SharedPreferences sharedPreferences = getSharedPreferences("Myphone",getApplicationContext().MODE_PRIVATE);
        final String current_uid = sharedPreferences.getString("PhoneNum","");

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);
        databaseReference.keepSynced(true);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("Name").getValue().toString();
                String phone = dataSnapshot.child("Phone").getValue().toString();
                String image1 = dataSnapshot.child("Image_Url").getValue().toString();
                String thumb = dataSnapshot.child("Thumb").getValue().toString();
                String status = dataSnapshot.child("Status").getValue().toString();

                userName.setText(name);
                userStaus.setText(status);

                if(!image1.equals("Null")) {
                    Picasso.with(SettingsActivity.this).load(image1).placeholder(R.drawable.arrow_blue_load_loading).into(image);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //-----------------------------------------------------------------button lisners

        userChangeStatus.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       // Intent intent = new Intent("com.example.a123.whatsapp_2.statusActivity");
                        //startActivity(intent);
                        Intent intent = new Intent(SettingsActivity.this,statusActivity.class);
                        //  intent.putExtra("Phone1", UserID);

                        startActivity(intent);
                    }
                }
        );


  /*      userChangeName.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent("com.example.a123.whatsapp_2.nameActivity");
                        startActivity(intent);
                    }
                }
        ); */


        userChangeName.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Intent intenter = new Intent("com.example.a123.whatsapp_2.nameActivity");
                        //startActivity(intenter);

                        Intent intent = new Intent(SettingsActivity.this,nameActivity.class);
                        //  intent.putExtra("Phone1", UserID);

                        startActivity(intent);
                    }
                }
        );


        userChangeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_PICK);
                //        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);

/*                CropImage.activity(Uri.EMPTY)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(user_screen.this);        */
            }
        });
    }//---------------------------end of on create

        //------------------------------------------------------------------------


    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();

            CropImage.activity(imageUri)
                    .setAspectRatio(1,1)
                    .setMinCropWindowSize(500,500)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                progressDialog = new ProgressDialog(SettingsActivity.this);
                progressDialog.setTitle("Uploading Image");
                progressDialog.setMessage("Image is being uploaded.");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                Uri resultUri = result.getUri();
                File thumbNailFilePath = new File(resultUri.getPath());

                String currUserId = firebaseUser.getUid();
                //             Bitmap thumbNailBitmap_1;
                Bitmap thumbNailBitmap;
                ByteArrayOutputStream baos;
                byte[] thumbByte = new byte[0];

                try {
                    thumbNailBitmap = new Compressor(this).setMaxWidth(200).setMaxHeight(200).setQuality(70).compressToBitmap(thumbNailFilePath);
                    baos = new ByteArrayOutputStream();
                    thumbNailBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    thumbByte = baos.toByteArray();
                    //                thumbNailBitmap_1 = thumbNailBitmap;
                } catch (IOException e) {
                    e.printStackTrace();
                }




                StorageReference storeRef = imageStore.child("display_images").child(currUserId + ".jpg");
                final StorageReference thumbFilePath = imageStore.child("display_images").child("thumbsNails").child(currUserId + ".jpg");


                final byte[] finalThumbByte = thumbByte;
                storeRef.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()) {
                            //            Toast.makeText(user_screen.this,"In Progress",Toast.LENGTH_LONG).show();
                            final String downloadURL = task.getResult().getDownloadUrl().toString();

                            UploadTask uploadTask = thumbFilePath.putBytes(finalThumbByte);
                            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> thumbTask) {

                                    final String thumbNaildownloadURL = thumbTask.getResult().getDownloadUrl().toString();

                                    if (thumbTask.isSuccessful()) {

                                        Map hashMapUpdate = new HashMap<>();
                                        hashMapUpdate.put("Image_Url",downloadURL);
                                        hashMapUpdate.put("Thumb",thumbNaildownloadURL);

                                        databaseReference.updateChildren(hashMapUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(SettingsActivity.this,"Picture successfully uploaded.",Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });

                                    }
                                    else {
                                        Toast.makeText(SettingsActivity.this,"Thumbnail could not be uploaded.",Toast.LENGTH_LONG).show();
                                        progressDialog.dismiss();
                                    }
                                }
                            });


                        }
                        else {
                            Toast.makeText(SettingsActivity.this,"Picture could not be uploaded.",Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                    }
                });

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    public static String randomStringGenerator() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(30);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }



}//--------------------------------------end of on everythinf




