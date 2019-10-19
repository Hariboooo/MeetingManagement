package com.manaka.meetingmanagement;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity  {
    SignInButton signInButton;
    private GoogleApiClient googleApiClient;
    TextView textView;
    Button btnGenerateOTP, btnSignIn,btn_check;
    EditText etPhoneNumber, etOTP;
    String phoneNumber, otp;

    private static final String PHONE_KEY = "Phone";
    FirebaseFirestore db;


    FirebaseAuth auth;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
    private String verificationCode;
    private static final int RC_SIGN_IN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        StartFirebaseLogin();
        db = FirebaseFirestore.getInstance();
    /*    GoogleSignInOptions gso =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleApiClient=new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();*/
        etPhoneNumber=(EditText) findViewById(R.id.et_phone_number);


        btn_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckNumber();


            }
        });
        btnGenerateOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                phoneNumber=etPhoneNumber.getText().toString();
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        phoneNumber,                     // Phone number to verify
                        60,                           // Timeout duration
                        TimeUnit.SECONDS,                // Unit of timeout
                        MainActivity.this,        // Activity (for callback binding)
                        mCallback);                      // OnVerificationStateChangedCallbacks
            }
        });
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addPhoneRegistration();

            }
        });

      /*  signInButton=(SignInButton)findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent,RC_SIGN_IN);
            }
        });*/


    }
    private void addPhoneRegistration() {
        Map<String, Object> newNumberRegsitration = new HashMap<>();
        String phone = etPhoneNumber.getText().toString();
        newNumberRegsitration.put(PHONE_KEY, phone);
        db.collection("PhoneNumberRegistration").document(phone).set(newNumberRegsitration).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(MainActivity.this, "Number updated",
                        Toast.LENGTH_SHORT).show();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "ERROR" + e.toString(),
                                Toast.LENGTH_SHORT).show();
                        Log.d("TAG", e.toString());
                    }
                });

        otp=etOTP.getText().toString();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, otp);
        SigninWithPhone(credential);
    }

        private void findViews() {
        btnGenerateOTP=findViewById(R.id.btn_generate_otp);
        btnSignIn=findViewById(R.id.btn_sign_in);
        etPhoneNumber=findViewById(R.id.et_phone_number);
        etOTP=findViewById(R.id.et_otp);
        btn_check = findViewById(R.id.btn_check);
    }
    private void SigninWithPhone(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential) .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(MainActivity.this,OneTimeRegistration.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(MainActivity.this,"Incorrect OTP",Toast.LENGTH_SHORT).show();

                        }
                    }
                });
         }
    private void StartFirebaseLogin() {
        auth = FirebaseAuth.getInstance();
        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                Toast.makeText(MainActivity.this,"Verification Successful SignIn to Continue",Toast.LENGTH_SHORT).show();

            }
            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(MainActivity.this,"verification failed",Toast.LENGTH_SHORT).show();
                Log.d("TAG", e.toString());
            }
            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationCode = s;
                Toast.makeText(MainActivity.this,"OTP Generation in process",Toast.LENGTH_SHORT).show();
                etOTP.setVisibility(View.VISIBLE);
                btnSignIn.setVisibility(View.VISIBLE);
                etOTP.requestFocus();
            }
        };
    }

    private void CheckNumber() {
        String phone = etPhoneNumber.getText().toString();

        DocumentReference user = db.collection("PhoneNumberRegistration").document(phone);
        user.get().addOnCompleteListener(new OnCompleteListener < DocumentSnapshot > () {
            @Override
            public void onComplete(@NonNull Task < DocumentSnapshot > task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    if(doc != null && doc.exists()) {
                            Toast.makeText(MainActivity.this, "Number Already Exist", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(MainActivity.this, GroupsActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                        }else{
                        Toast.makeText(MainActivity.this,"Number not Authenticated",Toast.LENGTH_SHORT).show();
                        btnGenerateOTP.setVisibility(View.VISIBLE);
                    }

                }
            }
        }) .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }
   /* @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }
    private void handleSignInResult(GoogleSignInResult result){
        if(result.isSuccess()){
            gotoProfile();
        }else{
            Toast.makeText(getApplicationContext(),"Sign in cancel",Toast.LENGTH_LONG).show();
        }
    }
    private void gotoProfile(){
        Intent intent=new Intent(MainActivity.this,GroupsActivity.class);
        startActivity(intent);
    }*/








}