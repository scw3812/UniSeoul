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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;

public class LoginActivity extends AppCompatActivity {

    private GoogleSignInClient mGoogleSignInClient;
    private int RC_SIGN_IN = 10;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private Button login;
    private Button signup;

    private EditText TextEmail;
    private EditText TextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance( );

        Intent intent = getIntent();

        //
        login = (Button) findViewById(R.id.login_login);
        signup = (Button) findViewById(R.id.login_signup);

        TextEmail = findViewById(R.id.signup_email);
        TextPassword = findViewById(R.id.signup_password);

        login = findViewById(R.id.login_login);
        signup = findViewById(R.id.login_signup);

        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                String email = TextEmail.getText().toString().trim();
                loginUser(email, TextPassword.getText().toString());
            }
        });

        signup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LoginActivity.this,SignupActivity.class);
                startActivity(intent);

                /*
                String email = TextEmail.getText().toString().trim();
                createUser(email, TextPassword.getText().toString());
                 */
            }

        });



        //google

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail( )
                .build( );

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        SignInButton button = (SignInButton) findViewById(R.id.login_button);

        button.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent( );
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{

                }
            }
        };


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }
    }

    /*
    private void createUser(String email, String password) {
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
                            Toast.makeText(LoginActivity.this, "회원 가입 성공", Toast.LENGTH_SHORT).show();

                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName("Jane Q. User")
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


                            //updateUI(user);
                        } else {
                            Log.w("TAG", "createUserWithEmail:failure", task.getException());
                        }

                        // ...
                    }
                });
    }

     */


    private void loginUser(String email, String password){
        if(email.equals("")||password.equals("")) {
            Toast.makeText(LoginActivity.this, "Email / password를 확인해주세요.", Toast.LENGTH_SHORT).show( );
            return;
        }

        Log.d("login success", email + " \t" + password);
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            Log.d("TAG", "signInWithEmail:success");

                            Toast.makeText(LoginActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithEmail:failure", task.getException());

                            System.out.println("err : " + task.getException().toString());

                            if(task.getException().toString().equals("com.google.firebase.auth.FirebaseAuthInvalidCredentialsException: The email address is badly formatted."))
                            {
                                Toast.makeText(LoginActivity.this, "올바른 메일 형식이 아닙니다.", Toast.LENGTH_SHORT).show();
                            }
                            if(task.getException().toString().equals("com.google.firebase.auth.FirebaseAuthInvalidCredentialsException: The password is invalid or the user does not have a password."))
                            {
                                Toast.makeText(LoginActivity.this, "ID / PW 를 확인해주세요.", Toast.LENGTH_SHORT).show();
                            }
                            if(task.getException().toString().equals("com.google.firebase.auth.FirebaseAuthInvalidUserException: There is no user record corresponding to this identifier. The user may have been deleted."))
                            {
                                Toast.makeText(LoginActivity.this, "ID / PW 를 확인해주세요.", Toast.LENGTH_SHORT).show();
                            }
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(LoginActivity.this,"login success",Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();

                            String user_name = mAuth.getCurrentUser().getDisplayName();
                            String user_email = mAuth.getCurrentUser().getEmail();

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                            //updateUI(user);



                        } else {
                            // If sign in fails, display a message to the user.
                            Snackbar.make(findViewById(R.id.login_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();


                            //updateUI(null);

                        }

                        // ...
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
}
