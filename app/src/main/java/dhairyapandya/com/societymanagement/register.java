package dhairyapandya.com.societymanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class register extends AppCompatActivity {
    public static final String TAG = "TAG";
    EditText Name, Mobilenumber, Emailaddress, Password, Confirmpassword;
    Button Signupbutton, login;
    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    String userID;
    ProgressBar Bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Mobilenumber = findViewById(R.id.Mobilenumber);
        Emailaddress = findViewById(R.id.Email);
        Name = findViewById(R.id.Name);
        Password = findViewById(R.id.password);
        Confirmpassword = findViewById(R.id.Cpassword);
        Signupbutton = findViewById(R.id.SignupButton);
        login = findViewById(R.id.REGISTER);
        fAuth = FirebaseAuth.getInstance();
        Bar = findViewById(R.id.progressBar);
        fstore= FirebaseFirestore.getInstance();
        //code for sending user to login screen
//        progressBar.setVisibility(View.GONE);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), login.class));
                finish();
            }
        });
        //register button code
        Signupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = Name.getText().toString();
                String mobilenumber = Mobilenumber.getText().toString();
                String email = Emailaddress.getText().toString();
                String password = Password.getText().toString();
                String confirmpasword = Confirmpassword.getText().toString();

                //if user is already logged in then directly to main activity
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }

                if (name.isEmpty()) {
                    Name.setError("Name is required");
                    return;
                }

                if (mobilenumber.isEmpty()) {
                    Mobilenumber.setError("Mobile is required");
                    return;
                }
                if (email.isEmpty()) {
                    Emailaddress.setError("Email is required");
                    return;
                }
                if (password.isEmpty()) {
                    Password.setError("Password is required");
                    return;
                }
                if (confirmpasword.isEmpty()) {
                    Confirmpassword.setError("Confirm Password is required");
                    return;
                }
                if (!password.equals(confirmpasword)) {
                    Confirmpassword.setError("Password do not match");
                    return;
                }
                // data is validated
//                progressBar.d
                Bar.setVisibility(View.VISIBLE);
                Toast.makeText(register.this, "Data Validated", Toast.LENGTH_SHORT).show();

                fAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        //Storing the data in the firestore
                        userID=fAuth.getCurrentUser().getUid();
                        DocumentReference documentReference=fstore.collection("users").document(userID);
                        Map<String,Object> user = new HashMap<>();
                        user.put("Name",name);
                        user.put("Mobile Number",mobilenumber);
                        user.put("Mail ID",email);
                        user.put("Password",password);

                        ((DocumentReference) documentReference).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d(TAG,"onSucess: user Profile is created for "+userID);
                            }
                        });
                        //sending the user in the main activity
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Bar.setVisibility(View.INVISIBLE);

                        Toast.makeText(register.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}