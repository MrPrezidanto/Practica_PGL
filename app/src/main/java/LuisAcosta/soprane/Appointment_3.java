package LuisAcosta.soprane;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class Appointment_3 extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference db;

    private RecyclerView recView;
    private ArrayList<String> datos;
    private TextView lblSpecialty;
    private RecyclerView recViewDoctores;
    private EditText appointmentDate;
    private List<String> appointment;
    private String nameDoctor, specialty;
    private Calendar calendario = Calendar.getInstance();

    private String doctorKey, userKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_3);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference();
        userKey = mAuth.getCurrentUser().getUid();
        datos = new ArrayList<String>();

        lblSpecialty = findViewById(R.id.LblSpecialty);
        lblSpecialty.setText(getIntent().getStringExtra("specialty"));

        appointment = null;
        appointmentDate = findViewById(R.id.appointmentDate);

        appointmentDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(Appointment_3.this, date,
                        calendario.get(Calendar.YEAR),
                        calendario.get(Calendar.MONTH),
                        calendario.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        recView = findViewById(R.id.RecViewDoctores);
        recView.setHasFixedSize(true);


        db.child("doctors").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child: snapshot.getChildren()) {
                    specialty = child.child("specialty").getValue().toString();
                    if(specialty.equals(getIntent().getStringExtra("specialty"))){
                        String key = child.getKey();
                        String name = child.child("name").getValue().toString();
                        String second = child.child("second").getValue().toString();
                        String email = child.child("email").getValue().toString();
                        datos.add(name + "/" + second + "/" + email + "/" + key);
                    }
                }

                final AdaptadorAppointment adaptador = new AdaptadorAppointment(datos, "doctors");

                adaptador.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        doctorKey = adaptador.getItem(recView.getChildAdapterPosition(v)).split("/")[3];
                        nameDoctor = adaptador.getItem(recView.getChildAdapterPosition(v)).split("/")[0] + " " + adaptador.getItem(recView.getChildAdapterPosition(v)).split("/")[1];
                        db.child("appointment").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                appointment = new ArrayList<>();

                                for (DataSnapshot child: snapshot.getChildren()) {
                                    if(child.child("doctor").getValue().toString().equals(doctorKey)) appointment.add(child.child("date").getValue().toString());
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });

                recView.setAdapter(adaptador);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        recView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));

        Button button = findViewById(R.id.buttonAppointment);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateForm()){
                    if(!appointment.contains(appointmentDate.getText().toString())){
                        appointment.add(appointmentDate.getText().toString());
                        String uid = UUID.randomUUID().toString();
                        db.child("appointment").child(uid).child("doctor").setValue(doctorKey);
                        db.child("appointment").child(uid).child("user").setValue(userKey);
                        db.child("appointment").child(uid).child("date").setValue(appointmentDate.getText().toString());

                        Intent intent = new Intent(Appointment_3.this, Appointment_4.class);
                        intent.putExtra("title", "Se ha reservado cita con:");
                        intent.putExtra("doctor", nameDoctor);
                        intent.putExtra("date", appointmentDate.getText().toString());
                        intent.putExtra("specialty", specialty);
                        intent.putExtra("key", "");
                        startActivity(intent);
                        finish();
                    } else Toast.makeText(Appointment_3.this, "Esa fecha no esta disponible", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean validateForm() {
        boolean valid = true;

        if (appointment == null) {
            Toast.makeText(Appointment_3.this, "Selecciona un doctor.", Toast.LENGTH_LONG).show();
            valid = false;
        }

        if (appointmentDate.getText().toString().isEmpty()) {
            appointmentDate.setError("Required.");
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
        appointmentDate.setText(sdf.format(calendario.getTime()));
        appointmentDate.setError(null);
    }
}