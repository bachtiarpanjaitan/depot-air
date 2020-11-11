package com.bataxdev.waterdepot;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import com.bataxdev.waterdepot.helper.DownloadImageTask;
import com.bataxdev.waterdepot.ui.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import com.google.firebase.database.*;
import com.google.firebase.messaging.FirebaseMessaging;
import org.jetbrains.annotations.NotNull;
public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private String user_name;
    private String user_email;
    private Uri user_image = null;
    private String user_uid;
    private boolean user_is_verified;
    private String user_phone_number;

    private DatabaseReference user;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    public static final String TAG = "MAIN_ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get Token Notif
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<String> task) {
                if(!task.isSuccessful()) {return;}
                Log.i("TOKEN",task.getResult().toString());
            }
        });

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        if(currentUser == null || currentUser.getUid() == "" || currentUser.isAnonymous())
        {
            Intent login = new Intent(this, LoginActivity.class);
            startActivity(login);
            finish();
            return;
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

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

        user = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid());
        user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    navigationView.getMenu().findItem(R.id.nav_product).setVisible(false);
                    navigationView.getMenu().findItem(R.id.nav_order_list).setVisible(false);
                    navigationView.getMenu().findItem(R.id.nav_report).setVisible(false);
                    navigationView.getMenu().findItem(R.id.nav_user).setVisible(false);
                }else{
                    navigationView.getMenu().findItem(R.id.nav_order).setVisible(false);
                    navigationView.getMenu().findItem(R.id.nav_profile).setVisible(false);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

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
    protected void onStart() {
        super.onStart();

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

    public void sendNotification(String title,String message, String channel_id){
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);
        String ChannelId = channel_id;
        if(channel_id.isEmpty())ChannelId = getString(R.string.default_notification_channel_id);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,ChannelId)
                .setSmallIcon(R.drawable.ic_message)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel notificationChannel = new NotificationChannel(ChannelId,"Notifikasi",NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        notificationManager.notify(0,notificationBuilder.build());
    }
}