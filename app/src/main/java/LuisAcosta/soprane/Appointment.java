package LuisAcosta.soprane;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Appointment extends Fragment {

    private FirebaseAuth mAuth;
    private DatabaseReference db;

    private String userKey;

    private RecyclerView recView;
    private ArrayList<String> datos;
    private Button button;

    private AdaptadorAppointment adaptador;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_list, container, false);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference();

        userKey = mAuth.getCurrentUser().getUid();

        recView = view.findViewById(R.id.RecView);
        recView.setHasFixedSize(true);

        datos = new ArrayList<String>();
        adaptador = new AdaptadorAppointment(datos, "dates");

        recView.setAdapter(adaptador);
        recView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL,false));

        button = view.findViewById(R.id.buttonAdd);
        button.setText("Pedir cita");
        button.setVisibility(View.VISIBLE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Appointment_2.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        datos.clear();

        db.child("appointment").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot child: snapshot.getChildren()) {
                    if(child.child("user").getValue().toString().equals(userKey)) {
                        final String keyAppo = child.getKey();
                        String keyDoctor = child.child("doctor").getValue().toString();
                        final String date = child.child("date").getValue().toString();

                        db.child("doctors").child(keyDoctor).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String nombre = snapshot.child("name").getValue().toString() + " " + snapshot.child("second").getValue().toString();
                                datos.add(nombre + "//" + date + "//" + snapshot.child("specialty").getValue().toString() + "//" + keyAppo);
                                adaptador.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }

                adaptador.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), Appointment_4.class);
                        intent.putExtra("title", "La cita es con:");
                        intent.putExtra("doctor", adaptador.getItem(recView.getChildAdapterPosition(v)).split("//")[0]);
                        intent.putExtra("date", adaptador.getItem(recView.getChildAdapterPosition(v)).split("//")[1]);
                        intent.putExtra("specialty", adaptador.getItem(recView.getChildAdapterPosition(v)).split("//")[2]);
                        intent.putExtra("key", adaptador.getItem(recView.getChildAdapterPosition(v)).split("//")[3]);
                        startActivity(intent);
                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}