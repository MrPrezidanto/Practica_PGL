package LuisAcosta.soprane;

import androidx.annotation.NonNull;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class RegisterUser extends Activity {

    private FirebaseAuth mAuth;
    private DatabaseReference db;

    private EditText registerDNI, registerName, registerSecondName, registerDate, registerEmail, registerPassword;
    private Switch mostrar;
    private Button registerButton;

    private boolean admin;

    private Calendar calendario = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        if(this.getIntent().getStringExtra("admin").equals("admin")) admin = true;
        else admin = false;

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference();

        registerDNI = findViewById(R.id.registerDNI);
        registerName = findViewById(R.id.registerName);
        registerSecondName = findViewById(R.id.registerSecondName);
        registerDate = findViewById(R.id.registerDate);
        registerEmail = findViewById(R.id.registerEmail);
        registerPassword = findViewById(R.id.registerPass);
        registerPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

        mostrar = findViewById(R.id.registerMostrar);
        registerButton = findViewById(R.id.registerButton);

        registerDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(RegisterUser.this, date,
                        calendario.get(Calendar.YEAR),
                        calendario.get(Calendar.MONTH),
                        calendario.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

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
                    db.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
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
                            else Toast.makeText(RegisterUser.this, "El usuairo con el DNI " + registerDNI.getText() + ", ya existe.", Toast.LENGTH_LONG).show();
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
                    registerUser(registerDNI.getText().toString(), registerName.getText().toString(), registerSecondName.getText().toString(), registerDate.getText().toString() ,registerEmail.getText().toString());
                    Toast.makeText(RegisterUser.this, "Se ha registrado el usuario.", Toast.LENGTH_LONG).show();
                    finish();
                }
                else {
                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(RegisterUser.this, "Ese email ya existe ", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RegisterUser.this, "No se pudo registrar el usuario ", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    private void registerUser(String dni, String name, String second, String date, String email){
        User user = new User(dni, name, second, date, email);
        db.child("users").child(mAuth.getCurrentUser().getUid()).setValue(user);
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

        if (registerDate.getText().toString().isEmpty()) {
            registerDate.setError("Required.");
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

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int a, int m, int d) {
            calendario.set(Calendar.YEAR, a);
            calendario.set(Calendar.MONTH, m);
            calendario.set(Calendar.DAY_OF_MONTH, d);
            actualizarInput();
        }
    };

    private void actualizarInput() {
        String formato = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(formato, Locale.US);
        registerDate.setText(sdf.format(calendario.getTime()));
        registerDate.setError(null);
    }
}