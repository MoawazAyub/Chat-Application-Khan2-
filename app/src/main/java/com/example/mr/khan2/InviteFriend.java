package com.example.mr.khan2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class InviteFriend extends AppCompatActivity {

    private EditText phoneNumber;
    private Button sendMsgButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friend);

        phoneNumber = (EditText)findViewById(R.id.phoneNumber);
        sendMsgButton = (Button)findViewById(R.id.sendMessage);

        sendSMS();
    }


    public void sendSMS() {
        final String contactNumber = phoneNumber.getText().toString();


        sendMsgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
      /*          Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("hello" + contactNumber));
                intent.putExtra("sms_body", message);
                startActivity(intent); */


                try {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(contactNumber, null, "hello hi", null, null);
                    Toast.makeText(getApplicationContext(), "SMS Sent!",
                            Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),
                            "SMS faild, please try again later!",
                            Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
 /*               Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.putExtra("sms_body", "default content");
                sendIntent.setType("vnd.android-dir/mms-sms");
                startActivity(sendIntent);  */
            }
        });

    }
}
