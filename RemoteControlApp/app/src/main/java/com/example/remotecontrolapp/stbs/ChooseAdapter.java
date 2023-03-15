package com.example.remotecontrolapp.stbs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.remotecontrolapp.OnItemClickedListener;
import com.example.remotecontrolapp.R;

import java.util.ArrayList;

public class ChooseAdapter extends RecyclerView.Adapter<ChooseAdapter.ChooseViewHolder> {

    String TAG = this.getClass().getSimpleName();


    private Context context;
    private ArrayList<Stb> stbs;
    private OnItemClickedListener clickedListener;

    public ChooseAdapter(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public ChooseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.stb_view, parent, false);
        final ChooseViewHolder holder = new ChooseViewHolder(view);
        view.setOnClickListener(view1 -> {
            if(clickedListener != null){
                clickedListener.onItemClicked(holder.getAdapterPosition());
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChooseViewHolder holder, int position) {
        Stb item = stbs.get(position);
        holder.getNameView().setText(item.getServiceName());
    }

    @Override
    public int getItemCount() {
        return stbs.size();
    }

    public void registerClickListener(OnItemClickedListener clickListener){
        this.clickedListener = clickListener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void refresh(ArrayList<Stb> stbs){
        this.stbs = stbs;
        notifyDataSetChanged();
    }

    public class ChooseViewHolder extends RecyclerView.ViewHolder{

        private View root;
        private TextView tv;

        public ChooseViewHolder(View view){
            super(view);
            root = view;
            tv = view.findViewById(R.id.tv_box_name);


        }

        private TextView getNameView(){
            return tv;
        }

        public View getRoot(){
            return root;
        }


    }
}
