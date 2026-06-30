package com.example.einkaufsliste;

import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Locale;

public class ShoppingListAdapter extends ListAdapter<ShoppingItem, ShoppingListAdapter.ShoppingItemViewHolder> {

    private final OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onDeleteClick(ShoppingItem item);
        void onCheckToggle(ShoppingItem item);
        void onItemClick(ShoppingItem item);
    }

    public ShoppingListAdapter(@NonNull DiffUtil.ItemCallback<ShoppingItem> diffCallback, OnItemClickListener listener) {
        super(diffCallback);
        this.mListener = listener;
    }

    @NonNull
    @Override
    public ShoppingItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_shopping_list, parent, false);
        return new ShoppingItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShoppingItemViewHolder holder, int position) {
        ShoppingItem current = getItem(position);
        holder.bind(current, mListener);
    }

    static class ShoppingItemViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewName;
        private final TextView textViewTotal;
        private final TextView textViewQuantityPrice;
        private final TextView textViewComment;
        private final CheckBox checkBox;
        private final ImageButton buttonDelete;

        public ShoppingItemViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewTotal = itemView.findViewById(R.id.textViewTotal);
            textViewQuantityPrice = itemView.findViewById(R.id.textViewQuantityPrice);
            textViewComment = itemView.findViewById(R.id.textViewComment);
            checkBox = itemView.findViewById(R.id.checkBox);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }

        public void bind(ShoppingItem item, OnItemClickListener listener) {
            textViewName.setText(item.getName());
            textViewTotal.setText(String.format(Locale.getDefault(), "%.2f €", item.getTotalPrice()));
            textViewQuantityPrice.setText(String.format(Locale.getDefault(), "%d x %.2f €", item.getQuantity(), item.getPrice()));
            textViewComment.setText(item.getComment());

            // Remove listener before setting state to avoid infinite loops or unnecessary calls
            checkBox.setOnCheckedChangeListener(null);
            checkBox.setChecked(item.isChecked());
            
            // Apply initial visual state
            updateVisualState(item.isChecked());

            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                item.setChecked(isChecked);
                updateVisualState(isChecked); // Instant feedback
                listener.onCheckToggle(item);
            });

            buttonDelete.setOnClickListener(v -> listener.onDeleteClick(item));
            itemView.setOnClickListener(v -> listener.onItemClick(item));
        }

        private void updateVisualState(boolean isChecked) {
            int flags;
            int textColor;
            int secondaryTextColor;
            float alpha;

            if (isChecked) {
                flags = textViewName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG;
                textColor = Color.parseColor("#9E9E9E");
                secondaryTextColor = Color.parseColor("#9E9E9E");
                alpha = 0.6f;
            } else {
                flags = textViewName.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG);
                textColor = Color.BLACK;
                secondaryTextColor = Color.parseColor("#757575");
                alpha = 1.0f;
            }

            textViewName.setPaintFlags(flags);
            textViewTotal.setPaintFlags(flags);
            textViewQuantityPrice.setPaintFlags(flags);
            textViewComment.setPaintFlags(flags);

            textViewName.setTextColor(textColor);
            textViewTotal.setTextColor(textColor);
            textViewQuantityPrice.setTextColor(secondaryTextColor);
            textViewComment.setTextColor(secondaryTextColor);

            itemView.setAlpha(alpha);
        }
    }

    static class ShoppingItemDiff extends DiffUtil.ItemCallback<ShoppingItem> {
        @Override
        public boolean areItemsTheSame(@NonNull ShoppingItem oldItem, @NonNull ShoppingItem newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull ShoppingItem oldItem, @NonNull ShoppingItem newItem) {
            return oldItem.getName().equals(newItem.getName()) &&
                    oldItem.getQuantity() == newItem.getQuantity() &&
                    oldItem.getPrice() == newItem.getPrice() &&
                    oldItem.getComment().equals(newItem.getComment()) &&
                    oldItem.isChecked() == newItem.isChecked();
        }
    }
}