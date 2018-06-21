package com.example.mr.khan2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = "PhoneAuth";

    public EditText phoneText;
    private EditText codeText;
    private Button verifyButton;
    private Button sendButton;
    private Button resendButton;
    private Button signOutButton;



    private String phoneVerificationId;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            verificationCallbacks;
    private PhoneAuthProvider.ForceResendingToken resendToken;

    private FirebaseAuth fbAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        phoneText = (EditText) findViewById(R.id.phoneText);
        codeText = (EditText) findViewById(R.id.codeText);
        verifyButton = (Button) findViewById(R.id.verifyButton);
        sendButton = (Button) findViewById(R.id.sendButton);
        resendButton = (Button) findViewById(R.id.resendButton);
        signOutButton = (Button) findViewById(R.id.signoutButton);


        verifyButton.setEnabled(false);
        resendButton.setEnabled(false);


        fbAuth = FirebaseAuth.getInstance();


    }

    public void sendCode(View view) {

        Toast t1 = Toast.makeText(getApplicationContext(),"Send code",Toast.LENGTH_SHORT);
        t1.show();
        String phoneNumber = phoneText.getText().toString();

        setUpVerificatonCallbacks();

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                verificationCallbacks);
    }

    private void setUpVerificatonCallbacks() {

        Toast t1 = Toast.makeText(getApplicationContext(),"VerficationCall Back",Toast.LENGTH_SHORT);
        t1.show();

        verificationCallbacks =
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                    @Override
                    public void onVerificationCompleted(
                            PhoneAuthCredential credential) {



                        Toast toast=Toast.makeText(getApplicationContext(),"Verification completed",Toast.LENGTH_SHORT);
                        toast.show();




                        resendButton.setEnabled(false);
                        verifyButton.setEnabled(false);
                        codeText.setText("");
                        signInWithPhoneAuthCredential(credential);

                        //---------------------------sending to main stuff



                        //Intent intent = new Intent(MainActivity.this,onepage.class);

                        //startActivity(intent);

                        //---------------------------
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        Toast t1 = Toast.makeText(getApplicationContext(),"verfication failed",Toast.LENGTH_SHORT);
                        t1.show();

                        if (e instanceof FirebaseAuthInvalidCredentialsException) {
                            // Invalid request
                            Log.d(TAG, "Invalid credential: "
                                    + e.getLocalizedMessage());
                        } else if (e instanceof FirebaseTooManyRequestsException) {
                            // SMS quota exceeded
                            Log.d(TAG, "SMS Quota exceeded.");
                        }
                    }

                    @Override
                    public void onCodeSent(String verificationId,
                                           PhoneAuthProvider.ForceResendingToken token) {

                        phoneVerificationId = verificationId;
                        resendToken = token;

                        verifyButton.setEnabled(true);
                        sendButton.setEnabled(false);
                        resendButton.setEnabled(true);
                    }
                };
    }

    public void verifyCode(View view) {

        String code = codeText.getText().toString();

        PhoneAuthCredential credential =
                PhoneAuthProvider.getCredential(phoneVerificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        fbAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            codeText.setText("");
                            Toast toast=Toast.makeText(getApplicationContext(),"Signed in",Toast.LENGTH_SHORT);
                            resendButton.setEnabled(false);
                            verifyButton.setEnabled(false);
                            FirebaseUser user = task.getResult().getUser();

                        } else {
                            if (task.getException() instanceof
                                    FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }

    public void resendCode(View view) {

        String phoneNumber = phoneText.getText().toString();

        setUpVerificatonCallbacks();

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                this,
                verificationCallbacks,
                resendToken);
    }


    public void signOut(View view) {


        FirebaseUser user = fbAuth.getCurrentUser();
        if(user != null)
        {
        String UserID=phoneText.getText().toString();
        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

        DatabaseReference ref1= mRootRef.child("Users").child(UserID);

        ref1.child("Name").setValue(getContactName(UserID,getApplicationContext()));
        ref1.child("Image_Url").setValue("Null");
        ref1.child("Status").setValue("Hi i am using what app");
        ref1.child("Phone").setValue(UserID);
        ref1.child("Thumb").setValue("Null");
        String usertoken = FirebaseInstanceId.getInstance().getToken();
        ref1.child("device_token").setValue(usertoken);

        //----------------Shared Preference
            SharedPreferences sharedPreferences = getSharedPreferences("Myphone",getApplicationContext().MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("PhoneNum",UserID);
            editor.commit();





        Intent intent = new Intent(MainActivity.this,onepage.class);
          //  intent.putExtra("Phone1", UserID);

        startActivity(intent);
        }

    }


    //((((((((((((((((((((((((((((((((((((((((

    public String getContactName(final String phoneNumber, Context context)
    {
        try{
        Uri uri=Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));

        String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME};

        String contactName="";
        Cursor cursor=context.getContentResolver().query(uri,projection,null,null,null);

        if (cursor != null) {
            if(cursor.moveToFirst()) {
                contactName=cursor.getString(0);
            }
            cursor.close();
        }

        if(contactName == null)
        {
            contactName = "Unknown Guy";
        }

        return contactName;}
        catch (Exception e){
            return "unknown guy";
        }
    }

    //(((((((((((((((((((((((((((((((((((((((((
}
