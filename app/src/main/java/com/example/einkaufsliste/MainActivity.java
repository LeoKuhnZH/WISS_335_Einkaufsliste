package com.example.einkaufsliste;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private ShoppingViewModel mViewModel;
    private TextView textViewGrandTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        textViewGrandTotal = findViewById(R.id.textViewGrandTotal);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        
        final ShoppingListAdapter adapter = new ShoppingListAdapter(new ShoppingListAdapter.ShoppingItemDiff(), new ShoppingListAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClick(ShoppingItem item) {
                mViewModel.delete(item);
            }

            @Override
            public void onCheckToggle(ShoppingItem item) {
                mViewModel.update(item);
            }

            @Override
            public void onItemClick(ShoppingItem item) {
                Intent intent = new Intent(MainActivity.this, ItemDetailActivity.class);
                intent.putExtra("ID", item.getId());
                intent.putExtra("NAME", item.getName());
                intent.putExtra("QUANTITY", item.getQuantity());
                intent.putExtra("PRICE", item.getPrice());
                intent.putExtra("COMMENT", item.getComment());
                intent.putExtra("IS_CHECKED", item.isChecked());
                startActivity(intent);
            }
        });

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mViewModel = new ViewModelProvider(this).get(ShoppingViewModel.class);
        mViewModel.getAllItems().observe(this, items -> {
            adapter.submitList(items);
            updateGrandTotal(items);
        });

        findViewById(R.id.fabAdd).setOnClickListener(view -> showAddItemDialog());
    }

    private void updateGrandTotal(List<ShoppingItem> items) {
        double total = 0;
        for (ShoppingItem item : items) {
            total += item.getTotalPrice();
        }
        textViewGrandTotal.setText(String.format(Locale.getDefault(), "%.2f €", total));
    }

    private void showAddItemDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Produkt hinzufügen");

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_add_item, null);
        final EditText inputName = viewInflated.findViewById(R.id.editTextName);
        final EditText inputQuantity = viewInflated.findViewById(R.id.editTextQuantity);
        final EditText inputPrice = viewInflated.findViewById(R.id.editTextPrice);
        final EditText inputComment = viewInflated.findViewById(R.id.editTextComment);

        builder.setView(viewInflated);

        builder.setPositiveButton("Hinzufügen", (dialog, which) -> {
            String name = inputName.getText().toString().trim();
            String quantityStr = inputQuantity.getText().toString().trim();
            String priceStr = inputPrice.getText().toString().trim();
            String comment = inputComment.getText().toString().trim();

            if (name.isEmpty() || quantityStr.isEmpty() || priceStr.isEmpty()) {
                Toast.makeText(MainActivity.this, "Bitte alle Pflichtfelder ausfüllen", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                int quantity = Integer.parseInt(quantityStr);
                double price = Double.parseDouble(priceStr);
                ShoppingItem item = new ShoppingItem(name, quantity, price, comment);
                mViewModel.insert(item);
            } catch (NumberFormatException e) {
                Toast.makeText(MainActivity.this, "Ungültige Eingabe für Menge oder Preis", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Abbrechen", (dialog, which) -> dialog.cancel());

        builder.show();
    }
}