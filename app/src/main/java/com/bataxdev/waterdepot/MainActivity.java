package com.bataxdev.waterdepot;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.bataxdev.waterdepot.data.model.ProductModel;
import com.bataxdev.waterdepot.helper.DownloadImageTask;
import com.bataxdev.waterdepot.ui.LoginActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private String user_name;
    private String user_email;
    private Uri user_image = null;
    private String user_uid;
    private boolean user_is_verified;
    private String user_phone_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser == null)
        {
            Intent login = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(login);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_order, R.id.nav_profile, R.id.nav_setting)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        View headerView = navigationView.getHeaderView(0);
        ImageView profileImage = headerView.findViewById(R.id.image_profile);
        TextView profileName = headerView.findViewById(R.id.nav_header_username);
        TextView profileEmail = headerView.findViewById(R.id.nav_header_email);


        if(currentUser != null){
            this.user_name = currentUser.getDisplayName();
            this.user_email  = currentUser.getEmail();
            this.user_image = currentUser.getPhotoUrl();
            this.user_uid = currentUser.getUid();
            this.user_is_verified = currentUser.isEmailVerified();
            this.user_phone_number = currentUser.getPhoneNumber();

            if(this.user_image != null && !this.user_image.equals(Uri.EMPTY)) {
                new DownloadImageTask(profileImage).execute(this.user_image.toString());
            }
            if(this.user_name != null)  profileName.setText(this.user_name);
            profileEmail.setText(this.user_email);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.action_logout:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void logout()
    {
        FirebaseAuth.getInstance().signOut();
        Intent login = new Intent(this, LoginActivity.class);
        startActivity(login);
        finish();
    }
}