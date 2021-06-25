package LuisAcosta.soprane;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Appointment_2 extends AppCompatActivity {

    private DatabaseReference db;

    private RecyclerView recView;
    private ArrayList<String> datos;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_view_list);

        db = FirebaseDatabase.getInstance().getReference();
        datos = new ArrayList<String>();

        button = findViewById(R.id.buttonAdd);
        button.setVisibility(View.GONE);
        recView = findViewById(R.id.RecView);
        recView.setHasFixedSize(true);

        db.child("doctors").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child: snapshot.getChildren()) {
                    String a = child.child("specialty").getValue().toString();
                    if(!datos.contains(a)) datos.add(a);
                }

                final AdaptadorAppointment adaptador = new AdaptadorAppointment(datos, "specialty");

                adaptador.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Appointment_2.this, Appointment_3.class);
                        intent.putExtra("specialty", adaptador.getItem(recView.getChildAdapterPosition(v)));
                        startActivity(intent);
                        finish();
                    }
                });

                recView.setAdapter(adaptador);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        recView.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL,false));
    }
}