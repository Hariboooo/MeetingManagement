package com.manaka.meetingmanagement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class GroupsActivity extends AppCompatActivity {

    private Spinner spinnerEvent, spinnerPlace;
    private Button btnSubmit;
    private static final String NAME_KEY = "Name";
    private static final String EMAIL_KEY = "Email";
    private static final String PHONE_KEY = "Phone";
    private static final String PLACE_KEY = "Place";
    private static final String EVENT_KEY = "Event";
    String name,email;
    String listName,listEvent,listPlace;
    FirebaseFirestore db;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);

        db = FirebaseFirestore.getInstance();
        addListenerOnButton();
        addListenerOnSpinnerItemSelection();

       }







    // add items into spinner dynamically

    public void addListenerOnSpinnerItemSelection() {
        spinnerEvent = (Spinner) findViewById(R.id.spinnerEvent);
        spinnerEvent.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }
    private void ReadSingleContact() {
        FirebaseUser userr= FirebaseAuth.getInstance().getCurrentUser();
        String phon = userr.getPhoneNumber();
        String phone = phon.replace("+91","");
        DocumentReference user = db.collection("PhoneNumberRegistration").document(phone);
            user.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task< DocumentSnapshot > task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    name = doc.get("Name").toString();
                    email = doc.get("Email").toString();
                    addPhoneRegistration();

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }
    private void addPhoneRegistration() {

        Map<String, Object> newNumberRegsitration = new HashMap<>();
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        String phon = user.getPhoneNumber();
        String phone = phon.replace("+91","");
        newNumberRegsitration.put(NAME_KEY, name);
        newNumberRegsitration.put(EMAIL_KEY, email);
        newNumberRegsitration.put(PHONE_KEY, phone);
        newNumberRegsitration.put(PLACE_KEY, spinnerPlace.getSelectedItem());
        newNumberRegsitration.put(EVENT_KEY, spinnerEvent.getSelectedItem());
        db.collection("PhoneNumberRegistration").document(phone).set(newNumberRegsitration).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(GroupsActivity.this, "Event Details updated",
                        Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(GroupsActivity.this, "ERROR" + e.toString(),
                                Toast.LENGTH_SHORT).show();
                        Log.d("TAG", e.toString());
                    }
                });

        Intent i = new Intent(GroupsActivity.this,DetailsActivity.class);
        startActivity(i);
    }
    // get the selected dropdown list_item value
    public void addListenerOnButton() {

        spinnerEvent = (Spinner) findViewById(R.id.spinnerEvent);
        spinnerPlace = (Spinner) findViewById(R.id.spinnerPlace);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                ReadSingleContact();
            }

        });
    }

}