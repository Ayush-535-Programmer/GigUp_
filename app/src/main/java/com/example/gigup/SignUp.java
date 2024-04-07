package com.example.gigup;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.gigup.Model.TaskDetail;
import com.example.gigup.Model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

public class SignUp extends AppCompatActivity {

    TextView go_signIn;

    EditText tv_email,tv_password,tv_name,tv_eid;
    Button btn_signup;
    //Firebase
    private FirebaseAuth auth;
    FirebaseDatabase database;

    ProgressDialog progressDialog;
    ImageView imgView;
    FirebaseStorage storage;
    String suri;
    Button uploadImg;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();

        String university = intent.getStringExtra("University_Name");


        go_signIn = findViewById(R.id.login);
        tv_email = findViewById(R.id.email_signUp);
        tv_name = findViewById(R.id.userName);
        tv_password = findViewById(R.id.password);
        btn_signup = findViewById(R.id.btn_signUp);
        tv_eid = findViewById(R.id.enrolmentId);
        uploadImg = findViewById(R.id.uploadImage);
        imgView = findViewById(R.id.imgView);

        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();// For Sign up
        database = FirebaseDatabase.getInstance();//To save Value on Firebase

        progressDialog = new ProgressDialog(SignUp.this);
        progressDialog.setTitle("Creating Account");
        progressDialog.setMessage("We are creating your account");

        String email = tv_email.getText().toString();
        String pass = tv_password.getText().toString();
        btn_signup.setOnClickListener(view -> {
            try {
                progressDialog.show();
                if(isValidEmail(tv_email.getText().toString())) {
                    if (!tv_name.getText().toString().isEmpty() && !tv_email.getText().toString().isEmpty() && !tv_password.getText().toString().isEmpty() && !tv_eid.getText().toString().isEmpty() && !suri.isEmpty()) {
                        auth.createUserWithEmailAndPassword
                                (tv_email.getText().toString(), tv_password.getText().toString()).addOnCompleteListener(task -> {
                            progressDialog.dismiss();
                            if (task.isSuccessful()) {
                                String id = Objects.requireNonNull(task.getResult().getUser()).getUid();
                                User user = new User(tv_name.getText().toString(),university,tv_eid.getText().toString(),suri);
                                TaskDetail mod = new TaskDetail("0");

                                try {
                                    database.getReference().child("Company").child(id).setValue(user);
                                    database.getReference().child("Tasks").child("TD").setValue(mod);
                                }catch (Exception e){
                                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }

                                finish();
                                Intent intent2 = new Intent(SignUp.this, User_Home_Page.class);
                                startActivity(intent2);


                                //Toast.makeText(SignUp.this, "User Created Successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(SignUp.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(SignUp.this, "Please Fill all Details", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(this, "Please fill valid Mail id", Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception e){
                progressDialog.dismiss();
                Toast.makeText(SignUp.this,e.getMessage()+" Failed to sign in Please Try Again Later",Toast.LENGTH_SHORT).show();
            }
        });

        uploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,33);
            }
        });

        go_signIn.setOnClickListener(view -> {
            Intent open = new Intent(SignUp.this,signin_activity.class);
            startActivity(open);
            finish();
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && data.getData() != null) {
            Uri file = data.getData();
            imgView.setImageURI(file);

            String pathString = "profile_pictures/"+auth.getUid();
            StorageReference reference = storage.getReference().child(pathString);


            reference.putFile(file).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            suri = uri.toString();
//                            database.getReference().child("Users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
//                                    .child("profilepic").setValue(uri.toString());
//                            Toast.makeText(SignUp.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }

    public static boolean isValidEmail(String target) {
        return target != null && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}