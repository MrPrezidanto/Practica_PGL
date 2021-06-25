package LuisAcosta.soprane;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTabHost;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ViewAdmin extends Fragment {

    private FragmentTabHost tabHost;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_users, container, false);

        tabHost = view.findViewById(R.id.tabhostAdmin);
        tabHost.setup(getActivity(), getChildFragmentManager(), android.R.id.tabcontent);

        tabHost.addTab(tabHost.newTabSpec("Users").setIndicator("Usuarios"), ViewUsers.class, null);
        tabHost.addTab(tabHost.newTabSpec("Doctors").setIndicator("Doctores"), ViewDoctors.class, null);

        tabHost.setCurrentTab(0);

        return view;
    }
}