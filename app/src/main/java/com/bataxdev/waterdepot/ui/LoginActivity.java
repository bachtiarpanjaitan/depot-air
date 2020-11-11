package com.bataxdev.waterdepot.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.bataxdev.waterdepot.MainActivity;
import com.bataxdev.waterdepot.R;
import com.bataxdev.waterdepot.data.model.UserModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;
import com.google.firebase.database.*;
import org.jetbrains.annotations.NotNull;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);
        setContentView(R.layout.activity_login);

        //final ProgressBar loading = findViewById(R.id.loading);
        Button btnLogin = findViewById(R.id.login);
        SignInButton btnLoginGoogle = findViewById(R.id.sign_in_google);
        btnLogin.setEnabled(true);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            final TextView username = findViewById(R.id.username);
            final TextView password = findViewById(R.id.password);
            @Override
            public void onClick(View v) {
                if(username.getText().toString().trim() == ""){
                    Toast.makeText(LoginActivity.this, "Email tidak boleh kosong",0).show();
                }

                if(password.getText().toString().trim() == ""){
                    Toast.makeText(LoginActivity.this, "Password tidak boleh kosong",0).show();
                }

                //loading.setProgress(0,true);
                //loading.setVisibility(View.VISIBLE);
                mAuth.signInWithEmailAndPassword(username.getText().toString(),password.getText().toString())
                    .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                //loading.setProgress(100, true);
                                //loading.setVisibility(View.INVISIBLE);
                                Intent main = new Intent(LoginActivity.this,MainActivity.class);
                                startActivity(main);
                            }else{
                                Toast.makeText(LoginActivity.this, "Gagal Login atau email password tidak dikenali",0).show();
                                //loading.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
            }
        });

        btnLoginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signIn = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signIn,RC_SIGN_IN);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN)
        {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if(account != null)
                firebaseAuthWithGoogle(account.getIdToken());
            }catch (ApiException e){

            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        final ProgressBar loading = findViewById(R.id.loading);
        try {
            //loading.setProgress(0,true);
            //loading.setVisibility(View.VISIBLE);
            AuthCredential credential = GoogleAuthProvider.getCredential(idToken,null);
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                //loading.setProgress(100, true);
                                //loading.setVisibility(View.INVISIBLE);
                                FirebaseUser user = mAuth.getCurrentUser();

                                DatabaseReference users = FirebaseDatabase.getInstance().getReference().child("users");

                                //users.child(user.getUid()).removeValue();

                                UserModel user_model = new UserModel();
                                user_model.setName(user.getDisplayName());
                                user_model.setPhone(user.getPhoneNumber());
                                user_model.setEmail(user.getEmail());
                                user_model.setUid(user.getUid());
                                user_model.setImage(user.getPhotoUrl().toString());
                                user_model.setAdmin(false);

                                users.child(user.getUid()).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                        if(!snapshot.exists())
                                        {
                                            users.child(user.getUid()).setValue(user_model);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                    }
                                });

                                Intent main = new Intent(LoginActivity.this,MainActivity.class);
                                startActivity(main);
                            }else{
                                Toast.makeText(LoginActivity.this, "Gagal Login atau email password tidak dikenali",0).show();
                                //loading.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
        }catch (Exception e){}
    }

    @Override
    public void onClick(View v) {

    }
}