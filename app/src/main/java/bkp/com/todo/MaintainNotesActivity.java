package bkp.com.todo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class MaintainNotesActivity extends AppCompatActivity {

    private String saveCurrentDate, saveCurrentTime;
    private String noteRandomKey;


    private Button applyChangesBtn, deleteBtn;
    private EditText tag, detail;

    private String noteID = "";
    private DatabaseReference notesRef;

    private FirebaseAuth mAuth;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintain_notes);

        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getUid();


        noteID = getIntent().getStringExtra("nid");
        notesRef = FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("notes").child(noteID);

        deleteBtn = findViewById(R.id.delete_note_amn);
        applyChangesBtn = findViewById(R.id.note_update_amn);


        tag = findViewById(R.id.note_tag_amn);
        detail = findViewById(R.id.note_detail_amn);

        displaySpecificProductInfo();

        applyChangesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                applyChanges();

            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteThisProduct();
            }
        });
    }

    private void deleteThisProduct() {

        notesRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Intent intent = new Intent(MaintainNotesActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();

                Toast.makeText(MaintainNotesActivity.this, "Note is deleted successfully.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void applyChanges() {

        Calendar calender = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calender.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calender.getTime());

        noteRandomKey = saveCurrentDate + saveCurrentTime;

        String pTag = tag.getText().toString();
        String pDetail = detail.getText().toString();

        if(pTag.equals("")){
            Toast.makeText(this, "Write down note tag.", Toast.LENGTH_SHORT).show();
        }else if(pDetail.equals("")){
            Toast.makeText(this, "Write down detail.", Toast.LENGTH_SHORT).show();
        }else{

            HashMap<String, Object> mp = new HashMap<>();
            mp.put("nid",noteID);
            mp.put("tag",pTag);
            mp.put("detail",pDetail);
//            mp.put("date",saveCurrentDate);
//            mp.put("time",saveCurrentTime);


            notesRef.updateChildren(mp).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if(task.isSuccessful()){
                        Toast.makeText(MaintainNotesActivity.this, "Changes applied successfully.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MaintainNotesActivity.this,HomeActivity.class);
                        startActivity(intent);
                        finish();

                    }
                }
            });

        }

    }

    private void displaySpecificProductInfo() {

        notesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){

                    String nTag = dataSnapshot.child("tag").getValue().toString();
                    String nDetail = dataSnapshot.child("detail").getValue().toString();

                    tag.setText(nTag);
                    detail.setText(nDetail);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MaintainNotesActivity.this,HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            Intent intent = new Intent(MaintainNotesActivity.this,HomeActivity.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


}