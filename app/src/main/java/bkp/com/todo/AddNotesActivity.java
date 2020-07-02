package bkp.com.todo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AddNotesActivity extends AppCompatActivity {

    private String notesTag, notesDetail, saveCurrentDate, saveCurrentTime;

    private Button AddNewNotesButton;
    private EditText notesTagEdt, notesDetailEdt;
    private String noteRandomKey;
    private DatabaseReference NotesRef;

    private FirebaseAuth mAuth;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);

        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getUid();

        //write------------->
        NotesRef = FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("notes");


        notesTagEdt = (EditText)findViewById(R.id.notes_tag_aan);
        notesDetailEdt = (EditText)findViewById(R.id.notes_detail_aan);

        AddNewNotesButton = (Button)findViewById(R.id.notes_add_aan);

        AddNewNotesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateProductData();
            }
        });
    }

    private void validateProductData() {

        notesTag = notesTagEdt.getText().toString();
        notesDetail = notesDetailEdt.getText().toString();

        if(TextUtils.isEmpty(notesTag)){
            Toast.makeText(this,"Please Write Note Tag",Toast.LENGTH_LONG).show();
        }else if(TextUtils.isEmpty(notesDetail)){
            Toast.makeText(this,"Please Write Note Detail",Toast.LENGTH_LONG).show();
        }else{
            StoreProductInformation();
        }

    }

    private void StoreProductInformation() {

        Calendar calender = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calender.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calender.getTime());

        noteRandomKey = saveCurrentDate + saveCurrentTime;

        HashMap<String, Object> mp = new HashMap<>();
        mp.put("nid",noteRandomKey);
        mp.put("date",saveCurrentDate);
        mp.put("time",saveCurrentTime);
        mp.put("tag",notesTag);
        mp.put("detail",notesDetail);

        NotesRef.child(noteRandomKey).updateChildren(mp).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){



                    Intent intent = new Intent(AddNotesActivity.this, HomeActivity.class);
                    startActivity(intent);


                    Toast.makeText(AddNotesActivity.this,"Note added successfully...",Toast.LENGTH_LONG).show();
                }else{
                    String message=task.getException().toString();
                    Toast.makeText(AddNotesActivity.this,"ERROR: " + message,Toast.LENGTH_LONG).show();
                }

            }
        });
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AddNotesActivity.this,HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            Intent intent = new Intent(AddNotesActivity.this,HomeActivity.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }



}

