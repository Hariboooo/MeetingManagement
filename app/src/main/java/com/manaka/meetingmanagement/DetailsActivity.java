package com.manaka.meetingmanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends AppCompatActivity {

    private UsersListAdapter usersListAdapter;
    private List<Users> Userslist;
    private RecyclerView mMainList;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Userslist = new ArrayList<>();
        usersListAdapter=new UsersListAdapter(Userslist);
        mMainList = (RecyclerView) findViewById(R.id.main_list);
        mMainList.setHasFixedSize(true);
        mMainList.setLayoutManager(new LinearLayoutManager(this));
        mMainList.setAdapter(usersListAdapter);
        db = FirebaseFirestore.getInstance();


        db.collection("PhoneNumberRegistration").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if(e!=null){

                }
                for(DocumentChange doc: documentSnapshots.getDocumentChanges()){
                    if(doc.getType()==DocumentChange.Type.ADDED){
                        Users users = doc.getDocument().toObject(Users.class);
                        Userslist.add(users);
                        usersListAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }


}
