package com.manaka.meetingmanagement;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class UsersListAdapter extends RecyclerView.Adapter<UsersListAdapter.ViewHolder> {

    public List<Users> Userslist;

    public UsersListAdapter(List<Users> Userslist){

        this.Userslist = Userslist;

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.nameText.setText(Userslist.get(position).getName());
        holder.phoneText.setText(Userslist.get(position).getPhone());
        holder.eventText.setText(Userslist.get(position).getEvent());
        holder.placeText.setText(Userslist.get(position).getPlace());

    }

    @Override
    public int getItemCount() {
    return Userslist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        View mView;
        public TextView nameText;
        public TextView phoneText;
        public TextView eventText;
        public TextView placeText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

            nameText = (TextView) mView.findViewById(R.id.name_text);
            phoneText = (TextView) mView.findViewById(R.id.phone_text);
            eventText = (TextView) mView.findViewById(R.id.event_text);
            placeText = (TextView) mView.findViewById(R.id.place_text);

        }
    }
}
