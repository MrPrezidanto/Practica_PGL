package LuisAcosta.soprane;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class AdaptadorAdmin extends RecyclerView.Adapter<AdaptadorAdmin.RecyclerViewHolder> implements View.OnClickListener {

    private View.OnClickListener listener;
    private LinkedHashMap<String, Object> datos;

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        public RecyclerViewHolder(View itemView) {
            super(itemView);
        }

        public void bindData(String dni, String name, String second) {
            TextView txtTitulo = itemView.findViewById(R.id.LblTitulo);
            txtTitulo.setText(dni);
            TextView txtSubtitulo = itemView.findViewById(R.id.LblSubTitulo);
            txtSubtitulo.setText(name + " " + second);
        }
    }

    public AdaptadorAdmin(LinkedHashMap<String, Object> datos) {
        this.datos = datos;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listitem_recycler, viewGroup, false);

        itemView.setOnClickListener(this);

        RecyclerViewHolder tvh = new RecyclerViewHolder(itemView);

        return tvh;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder viewHolder, int pos) {

        Object object = (new ArrayList<Object>(datos.values())).get(pos);

        if(object.getClass().getSimpleName().equals("User")){
            User item = (User) object;
            viewHolder.bindData(item.getDNI(), item.getName(), item.getSecond());
        }else {
            Doctor item = (Doctor) object;
            viewHolder.bindData(item.getDNI(), item.getName(), item.getSecond());
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
        return (new ArrayList<String>(datos.keySet())).get(position);
    }
}
