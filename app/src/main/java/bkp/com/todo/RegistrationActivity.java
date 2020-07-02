package bkp.com.todo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class RegistrationActivity extends AppCompatActivity {

    final Calendar myCalendar = Calendar.getInstance();

    private EditText nameEdt, phoneEdt, emailEdt, genderEdt, dobEdt, passwordEdt;
    private Button signupBtn;
    private TextView aaTxt;

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
        aaTxt = findViewById(R.id.already_ar);

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        dobEdt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(RegistrationActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        aaTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });


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
                            //Toast.makeText(RegistrationActivity.this, "Now you can login", Toast.LENGTH_SHORT).show();

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

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        dobEdt.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(RegistrationActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            Intent intent = new Intent(RegistrationActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


}