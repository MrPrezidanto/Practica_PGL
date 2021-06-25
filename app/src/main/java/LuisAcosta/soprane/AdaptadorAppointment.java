package LuisAcosta.soprane;


import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class AdaptadorAppointment extends RecyclerView.Adapter<AdaptadorAppointment.RecyclerViewHolder> implements View.OnClickListener {

    private View.OnClickListener listener;
    private LinkedHashMap<String, Object> doctores;
    private ArrayList<String> datos;
    private String type;

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        public RecyclerViewHolder(View itemView) {
            super(itemView);
        }

        public void bindSpecialty(String specialty) {
            ImageView imageView = itemView.findViewById(R.id.imageAppointment);
            String s = "@drawable/" + Normalizer.normalize(specialty.toLowerCase(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
            int i = itemView.getResources().getIdentifier(s, null, imageView.getContext().getPackageName());
            Drawable d = imageView.getContext().getDrawable(i);
            imageView.setBackground(d);

            TextView txtTitulo = itemView.findViewById(R.id.LblAppointment);
            txtTitulo.setText(specialty);
        }

        public void bindDoctor(String name, String second, String email) {
            TextView txtTitulo = itemView.findViewById(R.id.LblTitulo);
            txtTitulo.setTextSize(18);
            txtTitulo.setText(name + " " + second);
            TextView txtSubtitulo = itemView.findViewById(R.id.LblSubTitulo);
            txtSubtitulo.setTextSize(14);
            txtSubtitulo.setText(email);
        }

        public void bindDates(String name, String date) {
            TextView txtTitulo = itemView.findViewById(R.id.LblTitulo);
            txtTitulo.setTextSize(18);
            txtTitulo.setText(name);
            TextView txtSubtitulo = itemView.findViewById(R.id.LblSubTitulo);
            txtSubtitulo.setTextSize(14);
            txtSubtitulo.setText(date);
        }
    }

    public AdaptadorAppointment(ArrayList<String> datos, String type) {
        this.datos = datos;
        this.type = type;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView;

        if(type.equals("specialty")) itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listitem_appointment, viewGroup, false);
        else itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listitem_recycler, viewGroup, false);

        itemView.setOnClickListener(this);

        RecyclerViewHolder tvh = new RecyclerViewHolder(itemView);

        return tvh;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder viewHolder, int pos) {
        if(type.equals("specialty")) viewHolder.bindSpecialty(datos.get(pos));
        else if(type.equals("doctors")) {
            String[] x = datos.get(pos).split("/");
            viewHolder.bindDoctor(x[0],x[1],x[2]);
        }
        else {
            String[] x = datos.get(pos).split("//");
            viewHolder.bindDates(x[0],x[1]);
        }
    }

    @Override
    public int getItemCount() {
        return datos.size();
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if(listener != null)
            listener.onClick(view);
    }

    public String getItem(int position) {
        return datos.get(position);
    }
}