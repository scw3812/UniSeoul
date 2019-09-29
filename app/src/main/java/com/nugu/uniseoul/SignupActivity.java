package com.nugu.uniseoul;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SignupActivity  extends AppCompatActivity {

    FirebaseAuth mAuth;
    EditText nameET;
    EditText emailET;
    EditText pwET;
    Button signupBT;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance( );

        nameET =  findViewById(R.id.signup_name);

        emailET =  findViewById(R.id.signup_email);
        pwET =  findViewById(R.id.signup_password);
        signupBT =  findViewById(R.id.signup_bt);

        signupBT.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                String email = emailET.getText().toString().trim();
                createUser(nameET.getText().toString(),email,pwET.getText().toString());


            }
        });

    }


    private void createUser(String name,String email, String password) {

        if(name.equals("")) {
            Toast.makeText(SignupActivity.this, "이름을 입력해주세요.", Toast.LENGTH_SHORT).show( );
            return;
        }

        if(email.equals("")) {
            Toast.makeText(SignupActivity.this, "Email 를 입력해주세요.", Toast.LENGTH_SHORT).show( );
            return;
        }
        if(password.equals("")) {
            Toast.makeText(SignupActivity.this, "password 를 입력해주세요.", Toast.LENGTH_SHORT).show( );
            return;
        }


        System.out.println("회원가입");
        Log.d("TAG", email + " \t" + password);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override

                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(SignupActivity.this, "회원 가입 성공", Toast.LENGTH_SHORT).show();

                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name)
                                    .setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))
                                    .build();

                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d("TAG", "User profile updated.");
                                            }
                                        }
                                    });

                            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                            startActivity(intent);

                            //updateUI(user);
                        } else {
                            System.out.println("err : " + task.getException().toString());

                            if(task.getException().toString().equals("com.google.firebase.auth.FirebaseAuthUserCollisionException: The email address is already in use by another account."))
                            {
                                Toast.makeText(SignupActivity.this, "이미 존재하는 아이디가 있습니다.", Toast.LENGTH_SHORT).show();
                            }
                            if(task.getException().toString().equals("com.google.firebase.auth.FirebaseAuthInvalidCredentialsException: The email address is badly formatted."))
                            {
                                Toast.makeText(SignupActivity.this, "올바른 메일 형식이 아닙니다.", Toast.LENGTH_SHORT).show();
                            }
                            if(task.getException().toString().equals("com.google.firebase.auth.FirebaseAuthWeakPasswordException: The given password is invalid. [ Password should be at least 6 characters ]"))
                            {
                                Toast.makeText(SignupActivity.this, "PW는 6자 이상입니다.", Toast.LENGTH_SHORT).show();
                            }

                            //Toast.makeText(SignupActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();

                            Log.w("TAG", "createUserWithEmail:failure", task.getException());
                        }

                        // ...
                    }
                });
    }

}
