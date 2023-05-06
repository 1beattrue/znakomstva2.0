package edu.mirea.onebeattrue.znakomstva.ui.chat;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.mirea.onebeattrue.znakomstva.databinding.ItemMessageBinding;

public class ViewHolder extends RecyclerView.ViewHolder{

    protected ItemMessageBinding binding;

    public ViewHolder(ItemMessageBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
}
