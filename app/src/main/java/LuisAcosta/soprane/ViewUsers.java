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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedHashMap;

public class ViewUsers extends Fragment {

    private DatabaseReference db;

    private RecyclerView recView;
    private LinkedHashMap<String, Object> datos;
    private Button button;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_list, container, false);

        db = FirebaseDatabase.getInstance().getReference();
        datos = new LinkedHashMap<String, Object>();

        recView = view.findViewById(R.id.RecView);
        recView.setHasFixedSize(true);

        db.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child: snapshot.getChildren()) {
                    datos.put(child.getKey(), child.getValue(User.class));
                }

                final AdaptadorAdmin adaptador = new AdaptadorAdmin(datos);

                adaptador.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new Profile(adaptador.getItem(recView.getChildAdapterPosition(v)), "users")).commit();

                    }
                });

                recView.setAdapter(adaptador);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        recView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL,false));


        return view;
    }
}