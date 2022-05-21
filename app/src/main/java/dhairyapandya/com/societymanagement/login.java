package dhairyapandya.com.societymanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {
    EditText username, password;
    TextView forgotpassword;
    Button signupbutton, register;
    SignInButton btn;
    FirebaseAuth firebaseAuth;
    AlertDialog.Builder reset_alert;
    LayoutInflater inflater;
    ProgressBar Bar;
    //    GoogleSignInOptions gso;
//    GoogleSignInClient signInClient;
    public static final int GOOGLE_CODE = 10005;
    GoogleSignInClient mGoogleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = findViewById(R.id.Email);
        password = findViewById(R.id.password);
        signupbutton = findViewById(R.id.SignupButton);
        forgotpassword=findViewById(R.id.forgotpassword);
        register = findViewById(R.id.REGISTER);
        firebaseAuth=FirebaseAuth.getInstance();
        reset_alert = new AlertDialog.Builder(this);
        inflater=this.getLayoutInflater();
        btn=findViewById(R.id.signinwalla);
        Bar=findViewById(R.id.progressBar);




//Google sign in
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.default_web_client_id))
//                .requestEmail()
//                .build();
//
//        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                signIn();
//            }
//        });










//google font setting in the button

//register Button
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bar.setVisibility(View.VISIBLE);
                startActivity(new Intent(getApplicationContext(),register.class));
                Bar.setVisibility(View.INVISIBLE);

            }
        });

        //Signupbutton
        signupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //extract the data
                //validate the data
                Bar.setVisibility(View.VISIBLE);

                if (username.getText().toString().isEmpty()) {
                    Bar.setVisibility(View.INVISIBLE);
                    username.setError("Username is missing");
                    return;
                }
                if (password.getText().toString().isEmpty()) {
                    Bar.setVisibility(View.INVISIBLE);
                    password.setError("Password is missing");
                    return;
                }

                //make users entry in the application
                firebaseAuth.signInWithEmailAndPassword(username.getText().toString(), password.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                //login is sucessful
                                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                finish();//So that user never jumps to login activity again when he press on back button
//                        status=0;
//                        Intent i = new Intent(login.this,profilefragment.class);
//                        i.putExtra("STATUS",status);
//                        startActivity(i);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(login.this, e.getMessage(),Toast.LENGTH_SHORT).show();
                        Bar.setVisibility(View.INVISIBLE);

                    }
                });
            }
        });


//        code for forgot password

        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start alert dialog
                View view = inflater.inflate(R.layout.resetpop,null);
                reset_alert.setTitle("Reset Forgot Password ?").setMessage("Enter Your Email to get password reset link.")
                        .setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //validate the email Address
                                EditText email = view.findViewById(R.id.reset_email_pop);
                                if(email.getText().toString().isEmpty())
                                {
                                    email.setError("Required Field");
                                    return;
                                }

                                //set the reset link
                                firebaseAuth.sendPasswordResetEmail(email.getText().toString())
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(login.this, "Reset Email Sent", Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        }).setNegativeButton("Cancel",null).setView(view).create().show();
            }
        });
        // Configure Google Sign In
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken("405537836325-k77po960k26eli8807d3uh62fqprbqhv.apps.googleusercontent.com")
//                .requestEmail()
//                .build();
//        signInClient = GoogleSignIn.getClient(this, gso);
//
//        GoogleSignInAccount signInAccount=GoogleSignIn.getLastSignedInAccount(this);
//        if(signInAccount!=null){
//            Toast.makeText(this, "User Already logged in", Toast.LENGTH_SHORT).show();
//            startActivity(new Intent(this,MainActivity.class));
//        }
//        signinwallabtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent sign =signInClient.getSignInIntent();
//                startActivityForResult(sign,GOOGLE_SIGN_IN_CODE);
//            }
//        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
//            status=0;
//            Intent i = new Intent(login.this,profilefragment.class);
//            i.putExtra("STATUS",status);
//            startActivity(i);
        }
    }
}