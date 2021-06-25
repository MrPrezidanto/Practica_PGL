package LuisAcosta.soprane;

import androidx.annotation.NonNull;

import android.app.Activity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends Activity {

    private FirebaseAuth mAuth;

    private EditText loginEmail, loginPass;
    private TextView resetPass;
    private Switch mostrar;
    private Button loginButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        loginEmail = findViewById(R.id.loginEmail);
        loginPass = findViewById(R.id.loginPass);
        loginPass.setTransformationMethod(PasswordTransformationMethod.getInstance());

        resetPass = findViewById(R.id.resetPass);
        SpannableString ss = new SpannableString("Restablecer Contraseña");
        ss.setSpan(new UnderlineSpan(), 0, ss.length(), 0);
        resetPass.setText(ss);
        mostrar = findViewById(R.id.loginMostrar);
        loginButton = findViewById(R.id.loginButton);

        resetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!loginEmail.getText().toString().isEmpty()) sendPasswordReset();
                else loginEmail.setError("Required.");
            }
        });

        mostrar.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) loginPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                else loginPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn(loginEmail.getText().toString(), loginPass.getText().toString());
            }
        });
    }

    private void signIn(String email, String password) {
        if (!validateForm(email, password)) {
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    finish();
                } else {
                    Toast.makeText(Login.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean validateForm(String email, String password) {
        boolean valid = true;

        if (email.isEmpty()) {
            loginEmail.setError("Required.");
            valid = false;
        }

        if (password.isEmpty()) {
            loginPass.setError("Required.");
            valid = false;
        }

        return valid;
    }

    private void sendPasswordReset() {
        mAuth.sendPasswordResetEmail(loginEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) Toast.makeText(Login.this, "Se ha enviado un email", Toast.LENGTH_LONG).show();
            }
        });
    }
}