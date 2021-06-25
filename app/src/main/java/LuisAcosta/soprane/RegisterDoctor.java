package LuisAcosta.soprane;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterDoctor extends Activity {

    private FirebaseAuth mAuth;
    private DatabaseReference db;

    private EditText registerSpec, registerDNI, registerName, registerSecondName, registerEmail, registerPassword;
    private Switch mostrar;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_doctor);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference();

        registerSpec = findViewById(R.id.registerSpec);
        registerDNI = findViewById(R.id.registerDNI);
        registerName = findViewById(R.id.registerName);
        registerSecondName = findViewById(R.id.registerSecondName);
        registerEmail = findViewById(R.id.registerEmail);
        registerPassword = findViewById(R.id.registerPass);
        registerPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

        mostrar = findViewById(R.id.registerMostrar);
        registerButton = findViewById(R.id.registerButton);

        mostrar.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) registerPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                else registerPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateData()){
                    db.child("doctors").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            boolean exist = false;

                            for (DataSnapshot child: snapshot.getChildren()) {
                                if(child.child("dni").getValue().toString().equals(registerDNI.getText().toString())) {
                                    exist = true;
                                    break;
                                }
                            }

                            if(!exist) createAccount(registerEmail.getText().toString(), registerPassword.getText().toString());
                            else Toast.makeText(RegisterDoctor.this, "El doctor con el DNI " + registerDNI.getText() + ", ya existe.", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
    }

    private void createAccount(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    registerDoctor(registerSpec.getText().toString(), registerDNI.getText().toString(), registerName.getText().toString(), registerSecondName.getText().toString(), registerEmail.getText().toString());
                    Toast.makeText(RegisterDoctor.this, "Se ha registrado el Doctor.", Toast.LENGTH_LONG).show();
                    mAuth.signOut();
                    finish();
                }
                else {
                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(RegisterDoctor.this, "Ese email ya existe ", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RegisterDoctor.this, "No se pudo registrar el doctor ", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    private void registerDoctor(String spec, String dni, String name, String second, String email){
        Doctor doctor = new Doctor(spec, dni, name, second, email);
        db.child("doctors").child(mAuth.getCurrentUser().getUid()).setValue(doctor);
    }

    private boolean validateData() {
        boolean valid = true;

        if (registerDNI.getText().toString().isEmpty()) {
            registerDNI.setError("Required.");
            valid = false;
        }

        if (registerName.getText().toString().isEmpty()) {
            registerName.setError("Required.");
            valid = false;
        }

        if (registerSecondName.getText().toString().isEmpty()) {
            registerSecondName.setError("Required.");
            valid = false;
        }

        if (registerSpec.getText().toString().isEmpty()) {
            registerSpec.setError("Required.");
            valid = false;
        }

        if (registerEmail.getText().toString().isEmpty()) {
            registerEmail.setError("Required.");
            valid = false;
        }

        if (registerPassword.getText().toString().isEmpty()) {
            registerPassword.setError("Required.");
            valid = false;
        }

        return valid;
    }
}