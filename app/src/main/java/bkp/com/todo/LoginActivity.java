package bkp.com.todo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEdt, passwordEdt;
    private Button loginBtn;

    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private TextView newuser, forgot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        emailEdt = findViewById(R.id.email_al);
        passwordEdt = findViewById(R.id.password_al);
        loginBtn = findViewById(R.id.signin_al);

        newuser = findViewById(R.id.newuser_al);
        forgot = findViewById(R.id.forgot_al);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = emailEdt.getText().toString();
                String password = passwordEdt.getText().toString();

                if(email.isEmpty()){
                    Toast.makeText(LoginActivity.this, "fill email.", Toast.LENGTH_SHORT).show();
                }else if(password.isEmpty()){
                    Toast.makeText(LoginActivity.this, "fill password.", Toast.LENGTH_SHORT).show();
                }else{
                    loginUser(email, password);
                }

            }
        });

        newuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ForgotActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.
        updateUI(user);
    }

    private void loginUser(String email, String password) {

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    updateUI(user);
                }else {
                    Toast.makeText(LoginActivity.this, "Authentication failed: " + task.getException(),
                            Toast.LENGTH_SHORT).show();
                    //updateUI(null);
                }

            }
        });

    }

    private void updateUI(FirebaseUser user) {
        user = mAuth.getCurrentUser();
        /*-------- Check if user is already logged in or not--------*/
        if (user != null) {
            /*------------ If user's email is verified then access login -----------*/

            Toast.makeText(LoginActivity.this, "Login Success.",
                    Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
        }
        /*else {
                Toast.makeText(LoginActivity.this, "Your Email is not verified.",
                        Toast.LENGTH_SHORT).show();
        }*/
    }

}

