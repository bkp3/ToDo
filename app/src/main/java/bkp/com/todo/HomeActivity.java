package bkp.com.todo;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class HomeActivity extends AppCompatActivity {


    private Button btn1;
    private ImageView imageUser;
    private TextView nameUser;
    private TextView phoneUser;

    private FirebaseAuth mAuth;
    private String uid;

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getUid();

        btn1 = findViewById(R.id.startbtn1);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(HomeActivity.this, "Hiiiii", Toast.LENGTH_SHORT).show();
            }
        });





        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });



        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        View header = navigationView.getHeaderView(0);

        imageUser = header.findViewById(R.id.image_nhm);
        nameUser = header.findViewById(R.id.name_nhm);
        phoneUser = header.findViewById(R.id.phone_nhm);


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("users").child(uid).child("profile").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){

                    String name = dataSnapshot.child("name").getValue(String.class);
                    String phone = dataSnapshot.child("phone").getValue(String.class);
                    nameUser.setText(name);
                    phoneUser.setText(phone);
                }else{
                    nameUser.setText("User Name");
                    phoneUser.setText("Phone");


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });








        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_notes, R.id.nav_profile, R.id.nav_logout)
                .setDrawerLayout(drawer)
                .build();


        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.bringToFront();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){

                    case R.id.nav_home:
                        break;

                    case R.id.nav_notes:
                        Toast.makeText(HomeActivity.this, "study clicked", Toast.LENGTH_SHORT).show();
                        /*Intent intent1 = new Intent(HomeActivity.this, CategoryActivity.class);
                        startActivity(intent1);*/
                        break;

                    case R.id.nav_profile:
                        Intent intent5 = new Intent(HomeActivity.this, ProfileActivity.class);
                        startActivity(intent5);
                        break;

                    case R.id.nav_logout:
                        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                        builder.setMessage("Do you want to logout?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        /*GoogleSignIn.getClient(
                                                HomeActivity.this,
                                                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
                                        ).signOut();*/
                                        FirebaseAuth.getInstance().signOut();
                                        Intent intent = new Intent(HomeActivity.this,LoginActivity.class);
                                        startActivity(intent);
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        break;

                }

                drawer.closeDrawers();

                return false;
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();


       if(id == R.id.action_about){
            Intent intent = new Intent(HomeActivity.this,AboutActivity.class);
            startActivity(intent);
            return true;
        }else if(id == R.id.action_update){
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "bkp.com.todo")));
            }catch (ActivityNotFoundException e){
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http//play.google.com/store/apps/details?id=" + getPackageName())));
            }
            return true;
        }else if(id == R.id.action_rate){
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "bkp.com.todo")));
            }catch (ActivityNotFoundException e){
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http//play.google.com/store/apps/details?id=" + getPackageName())));
            }
            return true;
        }else if(id == R.id.action_share){
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plane");
            String shareBody = "https://play.google.com/store/apps/details?id=bkp.com.todo";
            String shareSubject = "Study Course App";

            sharingIntent.putExtra(Intent.EXTRA_TEXT,shareBody);
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT,shareSubject);

            startActivity(Intent.createChooser(sharingIntent,"Sharing Using"));
            return true;
        }else if(id == R.id.action_more){
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pub:bkp399")));
            }catch (ActivityNotFoundException e){
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id=bkp399")));
            }
            return true;
        }/*else if(id == R.id.action_exit){

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Do you want to exit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //HomeActivity.super.onBackPressed();
                            finish();
                            System.exit(0);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();



        }*/

        return super.onOptionsItemSelected(item);
    }
}