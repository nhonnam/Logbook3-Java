package com.example.logbook3.Activities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.logbook3.Models.User;
import com.example.logbook3.R;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ContactViewHolder> {
    private List<User> users;
    private OnDeleteClickListener onDeleteClickListener;

    public interface OnDeleteClickListener {
        void onDeleteClick(User person);
    }

    public UserAdapter(List<User> users, OnDeleteClickListener onDeleteClickListener) {
        this.users = users;
        this.onDeleteClickListener = onDeleteClickListener;
    }

    public UserAdapter(List<User> users) {
        this.users = users;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_card, parent, false);
        return new ContactViewHolder(itemView);
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView name, dob, email;
        ImageView image;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.userName);
            dob = itemView.findViewById(R.id.userDob);
            email = itemView.findViewById(R.id.userEmail);
            image = itemView.findViewById(R.id.userImage);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        User user = users.get(position);
        holder.name.setText(user.name);
        holder.dob.setText(user.dob);
        holder.email.setText(user.email);
        holder.image.setImageResource(user.image);

        holder.itemView.setOnClickListener(v -> {
            if (onDeleteClickListener != null) {
                onDeleteClickListener.onDeleteClick(users.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}