package com.example.gigup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Objects;

public class signin_activity extends AppCompatActivity {

    TextView go_signup;
    EditText tv_email,tv_password;
    Button btn_login,btn_google;

    ProgressDialog progressDialog;
    FirebaseAuth auth;

    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signin);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Intent intent = getIntent();

        String university = intent.getStringExtra("University_Name");
        go_signup = findViewById(R.id.signup);
        tv_email = findViewById(R.id.email);
        tv_password = findViewById(R.id.password);
        btn_login = findViewById(R.id.login);
        btn_google = findViewById(R.id.google);

        progressDialog = new ProgressDialog(signin_activity.this);
        progressDialog.setTitle("Login");
        progressDialog.setMessage("We are Logging in your account");

        auth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        btn_google.setOnClickListener(view -> signIn());

        btn_login.setOnClickListener(view -> {
            try {
                if(tv_email.getText().toString().isEmpty()){
                    tv_email.setError("Enter your email");
                }
                if(tv_password.getText().toString().isEmpty()){
                    tv_password.setError("Enter your password");
                }
                progressDialog.show();
                //if (!tv_email.getText().toString().equals("") && !tv_password.getText().toString().equals("")) {
                auth.signInWithEmailAndPassword(tv_email.getText().toString(),tv_password.getText().toString())
                        .addOnCompleteListener(task -> {
                            progressDialog.dismiss();
                            if (task.isSuccessful()) {
                                finish();
                                Intent intent2 = new Intent(signin_activity.this, User_Home_Page.class);
                                startActivity(intent2);
                            } else {
                                Toast.makeText(signin_activity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
            catch (Exception e){
                progressDialog.dismiss();
                Toast.makeText(signin_activity.this,e.getMessage()+" Failed to sign in Please Try Again Later",Toast.LENGTH_SHORT).show();
            }

        });


        if(auth.getCurrentUser()!=null){
            finish();
            Intent intent2 = new Intent(signin_activity.this, User_Home_Page.class);
            startActivity(intent2);
        }

        go_signup.setOnClickListener(view -> {
            Intent open = new Intent(signin_activity.this,SignUp.class);
            String data = university;
            intent.putExtra("University_Name",data);
            startActivity(intent);
            finish();
            startActivity(open);
        });

    }

    int RC_SIGN_IN = 53;

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            //handleSignInResult(task);
            try{
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());


            } catch (ApiException e) {
                //e.printStackTrace();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken){
        try {
            AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
            auth.signInWithCredential(credential)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            Log.d("TAG", "signInWithCredential:success");
                            finish();
                            Intent intent = new Intent(signin_activity.this, User_Home_Page.class);
                            startActivity(intent);
                            //updateUI(user);
                        } else {
                            Toast.makeText(signin_activity.this, "Failed " + task.getException(), Toast.LENGTH_SHORT).show();
                            Log.w("TAG", "SignInFailed", task.getException());
                        }
                    });
        }
        catch (Exception e){
            Toast.makeText(signin_activity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
}