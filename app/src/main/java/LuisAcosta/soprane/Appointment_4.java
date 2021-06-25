package LuisAcosta.soprane;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Appointment_4 extends AppCompatActivity {

    private DatabaseReference db;

    private TextView title, txtDoctor, txtDate, txtSpecialty;
    private Button back, cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_4);

        db = FirebaseDatabase.getInstance().getReference();

        title = findViewById(R.id.app_4_label);
        txtDoctor = findViewById(R.id.txtDoctor);
        txtDate = findViewById(R.id.txtDate);
        txtSpecialty = findViewById(R.id.txtSpecialty);

        title.setText(getIntent().getStringExtra("title"));
        txtDoctor.setText("Doctor: " + getIntent().getStringExtra("doctor"));
        txtDate.setText("Fecha: " + getIntent().getStringExtra("date"));
        txtSpecialty.setText("Especialidad: " + getIntent().getStringExtra("specialty"));

        back = findViewById(R.id.backAppointment);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if(!getIntent().getStringExtra("key").isEmpty()){
            cancel = findViewById(R.id.cancelAppointment);
            cancel.setVisibility(View.VISIBLE);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    db.child("appointment").child(getIntent().getStringExtra("key")).removeValue();
                    finish();
                }
            });
        }
    }
}