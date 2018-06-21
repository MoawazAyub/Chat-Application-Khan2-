package com.example.mr.khan2;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class nameActivity extends AppCompatActivity {

    private Toolbar nameActivityToolbar;
    private EditText name;
    private Button updateName;

    private DatabaseReference dbRef_1;
    private FirebaseUser fbUser_1;

    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);

        name = (EditText)findViewById(R.id.name_text_view);
        updateName = (Button)findViewById(R.id.update_name);

        //fbUser_1 = FirebaseAuth.getInstance().getCurrentUser();
        //String uid = fbUser_1.getUid();

        //-------------------------------------
        SharedPreferences sharedPreferences = getSharedPreferences("Myphone",getApplicationContext().MODE_PRIVATE);
        String uid = sharedPreferences.getString("PhoneNum","");

        dbRef_1 = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        dbRef_1.addValueEventListener(new ValueEventListener() {
                                          @Override
                                          public void onDataChange(DataSnapshot dataSnapshot) {
                                              String name_1 = dataSnapshot.child("Name").getValue().toString();
                                              name.setText(name_1);
                                          }

                                          @Override
                                          public void onCancelled(DatabaseError databaseError) {

                                          }
                                      }
        );



        updateName.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        progress = new ProgressDialog(nameActivity.this);
                        progress.setTitle("Saving Changes");
                        progress.setMessage("Please wait. Changes are being saved.");
                        progress.show();

                        String name_1 = name.getText().toString();
                        dbRef_1.child("Name").setValue(name_1).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    progress.dismiss();
                                    Toast.makeText(getApplicationContext(), "Changes Saved Successfully.", Toast.LENGTH_LONG).show();
                                }
                                else {
                                    Toast.makeText(getApplicationContext(), "Changes could not be saved.", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                }
        );

    }
}
