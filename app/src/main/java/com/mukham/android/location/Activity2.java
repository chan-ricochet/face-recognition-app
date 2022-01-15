package com.mukham.android.location;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

public class Activity2 extends AppCompatActivity {


    private Button b1,b2;
    private EditText editText;
    private TextView t1;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    AttendanceData attendanceData;
    String id=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity2);}


    @Override
    protected void onStart() {
        super.onStart();
        editText = findViewById(R.id.editText);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        attendanceData = new AttendanceData();
        b1 = findViewById(R.id.button1);
        b2 = findViewById(R.id.button2);
        t1 = findViewById(R.id.textView2);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                id = UUID.randomUUID().toString();;

                Calendar c = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String ctime = sdf.format(c.getTime());

                String name = editText.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(Activity2.this, "Please add some data.", Toast.LENGTH_SHORT).show();
                } else {
                    addDatatoFirebase(id, name, ctime);
                }
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayAttendance();
            }
        });
    }



    private void addDatatoFirebase(String id, String name, String time) {

        attendanceData.setAttId(id);
        attendanceData.setAttName(name);
        attendanceData.setAttTime(time);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                databaseReference.child(id).setValue(attendanceData);
                Toast.makeText(Activity2.this, "Data added", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Activity2.this, "Failed to add data " + error, Toast.LENGTH_SHORT).show();

            }

        });
    }

    private void displayAttendance() {
        if(id==null)
        { t1.setText("Please mark your attendance first");
        return;}
        databaseReference = FirebaseDatabase.getInstance().getReference().child(id);
        Query lastQuery = databaseReference.orderByKey();
        lastQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               AttendanceData newnode = new AttendanceData(dataSnapshot.child("attId").getValue(String.class),
                            dataSnapshot.child("attName").getValue(String.class), dataSnapshot.child("attTime").getValue(String.class));
               t1.setText(newnode.getAttId() + "\n" + newnode.getAttName() + " " + newnode.getAttTime());

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(Activity2.this, "Error while retrieving", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
