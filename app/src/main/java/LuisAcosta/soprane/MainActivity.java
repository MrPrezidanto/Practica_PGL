package LuisAcosta.soprane;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference db;
    private FirebaseUser fireUser;

    private Toolbar appbar;
    private DrawerLayout drawerLayout;
    private NavigationView navView;

    private TextView user;
    private TextView email;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference();

        appbar = findViewById(R.id.toolbar);
        setSupportActionBar(appbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_nav_menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerLayout = findViewById(R.id.drawer_layout);

        navView = findViewById(R.id.navview);
        navView.setCheckedItem(R.id.inicio);

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                boolean fragmentTransaction = false;
                Fragment fragment = null;
                Intent intent = null;

                switch (menuItem.getItemId()) {
                    case R.id.inicio:
                        fragment = new Home();
                        fragmentTransaction = true;
                        break;
                    case R.id.admin:
                        fragment = new ViewAdmin();
                        fragmentTransaction = true;
                        break;
                    case R.id.profile:
                        fragment = new Profile();
                        fragmentTransaction = true;
                        break;
                    case R.id.appointment:
                        fragment = new Appointment();
                        fragmentTransaction = true;
                        break;
                    case R.id.signIn:
                        intent = new Intent(MainActivity.this, LogUser.class).putExtra("admin", "no");
                        intent.putExtra("tab", 0);
                        startActivity(intent);
                        break;
                    case R.id.register:
                        intent = new Intent(MainActivity.this, LogUser.class).putExtra("admin", "no");
                        intent.putExtra("tab", 1);
                        startActivity(intent);
                        break;
                    case R.id.signOut:
                        signOut();
                        break;
                }

                if(fragmentTransaction) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
                    menuItem.setChecked(true);
                    //getSupportActionBar().setTitle(menuItem.getTitle());
                }

                drawerLayout.closeDrawers();
                return true;
            }
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new Home()).commit();
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void signOut() {
        mAuth.signOut();
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new Home()).commit();
        onResume();
    }

    private void reloadUser(){
        View headerView = navView.getHeaderView(0);
        user = headerView.findViewById(R.id.header_user);
        email = headerView.findViewById(R.id.header_email);

        fireUser = mAuth.getCurrentUser();

        if(fireUser != null){
            db.child("users").child(fireUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    user.setText(snapshot.child("name").getValue().toString() + " " + snapshot.child("second").getValue().toString());
                    user.setVisibility(View.VISIBLE);
                    email.setText(snapshot.child("email").getValue().toString());
                    email.setVisibility(View.VISIBLE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else {
            user.setText("");
            user.setVisibility(View.GONE);
            email.setText("");
            email.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        reloadUser();
        navView.setCheckedItem(R.id.home);

        if(fireUser == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new Home()).commit();
            navView.getMenu().findItem(R.id.admin).setVisible(false);
            navView.getMenu().findItem(R.id.profile).setVisible(false);
            navView.getMenu().findItem(R.id.appointment).setVisible(false);
            navView.getMenu().findItem(R.id.signIn).setVisible(true);
            navView.getMenu().findItem(R.id.register).setVisible(true);
            navView.getMenu().findItem(R.id.signOut).setVisible(false);
        }
        else {
            navView.getMenu().findItem(R.id.profile).setVisible(true);
            navView.getMenu().findItem(R.id.appointment).setVisible(true);
            navView.getMenu().findItem(R.id.signIn).setVisible(false);
            navView.getMenu().findItem(R.id.register).setVisible(false);
            navView.getMenu().findItem(R.id.signOut).setVisible(true);

            if(fireUser.getUid().equals("vc77NzsNsyfQQW5nnaBB2DWz7kT2")) navView.getMenu().findItem(R.id.admin).setVisible(true);
            else navView.getMenu().findItem(R.id.admin).setVisible(false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAuth.signOut();
    }
}