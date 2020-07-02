package bkp.com.todo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegistrationActivity extends AppCompatActivity {

    private EditText nameEdt, phoneEdt, emailEdt, genderEdt, dobEdt, passwordEdt;
    private Button signupBtn;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();

        nameEdt = findViewById(R.id.name_ar);
        phoneEdt = findViewById(R.id.phone_ar);
        emailEdt = findViewById(R.id.email_ar);
        genderEdt = findViewById(R.id.gender_ar);
        dobEdt = findViewById(R.id.dob_ar);
        passwordEdt = findViewById(R.id.password_ar);

        signupBtn = findViewById(R.id.signup_ar);


        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });
    }

    private void register() {

        String name = nameEdt.getText().toString();
        String phone = phoneEdt.getText().toString();
        String email = emailEdt.getText().toString();
        String gender = genderEdt.getText().toString();
        String dob = dobEdt.getText().toString();
        String password = passwordEdt.getText().toString();

        if(name.equals("")){
            Toast.makeText(this, "please fill name", Toast.LENGTH_SHORT).show();
        }else if(phone.equals("")){
            Toast.makeText(this, "please fill phone", Toast.LENGTH_SHORT).show();
        }else if(email.equals("")){
            Toast.makeText(this, "please fill email", Toast.LENGTH_SHORT).show();
        }else if(gender.equals("")){
            Toast.makeText(this, "please fill gender", Toast.LENGTH_SHORT).show();
        }else if(dob.equals("")){
            Toast.makeText(this, "please fill dob", Toast.LENGTH_SHORT).show();
        }else if(password.equals("")){
            Toast.makeText(this, "please fill password", Toast.LENGTH_SHORT).show();
        }else{

            createUser(name, phone, email, gender, dob, password);

        }
    }

    private void createUser(String name, String phone, String email, String gender, String dob, String password) {

        final HashMap<String, Object>mp = new HashMap<>();
        mp.put("name",name);
        mp.put("phone",phone);
        mp.put("email",email);
        mp.put("gender",gender);
        mp.put("dob",dob);
        mp.put("password",password);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "createUserWithEmail:success");
                            //FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);

                            Toast.makeText(RegistrationActivity.this, "Registration successful.", Toast.LENGTH_SHORT).show();
                            Toast.makeText(RegistrationActivity.this, "Now you can login", Toast.LENGTH_SHORT).show();

                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

                            ref.child("users").child(mAuth.getUid()).child("profile").updateChildren(mp);

                            Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();




                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegistrationActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });

    }


}