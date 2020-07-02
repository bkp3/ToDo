package bkp.com.todo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private TextView nameTxt, emailTxt, phoneTxt, genderTxt, dobTxt;

    private FirebaseAuth mAuth;
    private String uid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getUid();

        nameTxt = findViewById(R.id.name_ap);
        emailTxt = findViewById(R.id.email_ap);
        phoneTxt = findViewById(R.id.phone_ap);
        genderTxt = findViewById(R.id.gender_ap);
        dobTxt = findViewById(R.id.dob_ap);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("users").child(uid).child("profile").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){

                    String name = dataSnapshot.child("name").getValue(String.class);
                    String phone = dataSnapshot.child("phone").getValue(String.class);
                    String email = dataSnapshot.child("email").getValue(String.class);
                    String gender = dataSnapshot.child("gender").getValue(String.class);
                    String dob = dataSnapshot.child("dob").getValue(String.class);

                    nameTxt.setText("Name :- " + name);
                    emailTxt.setText("Email :- " + email);
                    phoneTxt.setText("Phone :- " + phone);
                    genderTxt.setText("Gender :- " + gender);
                    dobTxt.setText("DOB :- " + dob);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ProfileActivity.this,HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            Intent intent = new Intent(ProfileActivity.this,HomeActivity.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}