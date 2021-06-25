package LuisAcosta.soprane;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends Fragment {

    private FirebaseAuth mAuth;
    private DatabaseReference db;
    private String fireUser;
    private String dataBase;

    private TextView shareLabel, dni, name, second, share, email;
    private boolean button = false;

    public Profile(){
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference();
        fireUser = mAuth.getCurrentUser().getUid();
        dataBase = "users";
    }

    public Profile(String user, String d){
        db = FirebaseDatabase.getInstance().getReference();
        fireUser = user;
        dataBase = d;
        button = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        dni = view.findViewById(R.id.profileDNI);
        name = view.findViewById(R.id.profileName);
        second = view.findViewById(R.id.profileSecond);
        shareLabel = view.findViewById(R.id.profileShareLabel);
        share = view.findViewById(R.id.profileShare);
        email = view.findViewById(R.id.profileEmail);

        db.child(dataBase).child(fireUser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dni.setText(snapshot.child("dni").getValue().toString());
                name.setText(snapshot.child("name").getValue().toString());
                second.setText(snapshot.child("second").getValue().toString());
                email.setText(snapshot.child("email").getValue().toString());

                if(dataBase.equals("users")) share.setText(snapshot.child("date").getValue().toString());
                else{
                    shareLabel.setText("Especialidad: ");
                    share.setText(snapshot.child("specialty").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if(button){
            Button back = view.findViewById(R.id.bProfileBack);
            back.setVisibility(View.VISIBLE);

            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new ViewAdmin()).commit();
                }
            });
        }

        return view;
    }
}