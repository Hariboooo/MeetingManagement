package com.manaka.meetingmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class OneTimeRegistration extends AppCompatActivity {

    private static final String NAME_KEY = "Name";
    private static final String EMAIL_KEY = "Email";
    private static final String PHONE_KEY = "Phone";
    FirebaseFirestore db;
    Button register;
    EditText name,email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_time_registration);
        db = FirebaseFirestore.getInstance();

        name=(EditText) findViewById(R.id.input_name);
        email=(EditText) findViewById(R.id.input_email);
        register=(Button) findViewById(R.id.btn_signup);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewPhoneRegistration();

            }
        });

    }

    
    private void addNewPhoneRegistration() {
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        String phon = user.getPhoneNumber();
        String phone = phon.replace("+91","");

        Map<String, Object> newPhoneRegsitration = new HashMap<>();
        String rname = name.getText().toString();
        String remail = email.getText().toString();
        newPhoneRegsitration.put(NAME_KEY, rname);
        newPhoneRegsitration.put(EMAIL_KEY, remail);
        newPhoneRegsitration.put(PHONE_KEY, phone);
        if (rname != ("") && remail != ("")) {


            db.collection("PhoneNumberRegistration").document(phone).set(newPhoneRegsitration).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(OneTimeRegistration.this, "User Registered",
                            Toast.LENGTH_SHORT).show();
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(OneTimeRegistration.this, "ERROR" + e.toString(),
                                    Toast.LENGTH_SHORT).show();
                            Log.d("TAG", e.toString());
                        }
                    });

        }
        else{
            Toast.makeText(OneTimeRegistration.this, "Please fill the details" ,Toast.LENGTH_SHORT).show();
        }
        Intent intent = new Intent(OneTimeRegistration.this,GroupsActivity.class);
        startActivity(intent);
    }


}
