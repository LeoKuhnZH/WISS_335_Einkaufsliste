package com.example.einkaufsliste;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import java.util.Locale;

public class ItemDetailActivity extends AppCompatActivity {

    private ShoppingViewModel mViewModel;
    private int mItemId;
    private boolean mIsChecked;
    private boolean mIsEditMode = false;

    private TextView detailName, detailQuantity, detailPrice, detailTotal, detailStatus, detailComment;
    private EditText editName, editQuantity, editPrice, editComment;
    private Button buttonSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Produkt-Details");
        }

        mViewModel = new ViewModelProvider(this).get(ShoppingViewModel.class);

        // Initialize Views
        detailName = findViewById(R.id.detailName);
        detailQuantity = findViewById(R.id.detailQuantity);
        detailPrice = findViewById(R.id.detailPrice);
        detailTotal = findViewById(R.id.detailTotal);
        detailStatus = findViewById(R.id.detailStatus);
        detailComment = findViewById(R.id.detailComment);

        editName = findViewById(R.id.editName);
        editQuantity = findViewById(R.id.editQuantity);
        editPrice = findViewById(R.id.editPrice);
        editComment = findViewById(R.id.editComment);
        buttonSave = findViewById(R.id.buttonSave);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mItemId = extras.getInt("ID", -1);
            String name = extras.getString("NAME", "");
            int quantity = extras.getInt("QUANTITY", 0);
            double price = extras.getDouble("PRICE", 0.0);
            String comment = extras.getString("COMMENT", "");
            mIsChecked = extras.getBoolean("IS_CHECKED", false);

            updateUi(name, quantity, price, comment, mIsChecked);
        }

        buttonSave.setOnClickListener(v -> saveChanges());
    }

    private void updateUi(String name, int quantity, double price, String comment, boolean isChecked) {
        detailName.setText(name);
        detailQuantity.setText(String.valueOf(quantity));
        detailPrice.setText(String.format(Locale.getDefault(), "%.2f €", price));
        detailTotal.setText(String.format(Locale.getDefault(), "%.2f €", quantity * price));
        detailStatus.setText(isChecked ? "Gekauft" : "Noch zu kaufen");
        detailComment.setText(comment.isEmpty() ? "Kein Kommentar" : comment);

        editName.setText(name);
        editQuantity.setText(String.valueOf(quantity));
        editPrice.setText(String.format(Locale.US, "%.2f", price)); // Use US locale for consistent decimal point in EditText
        editComment.setText(comment);
    }

    private void toggleEditMode() {
        mIsEditMode = !mIsEditMode;
        
        int viewVisibility = mIsEditMode ? View.GONE : View.VISIBLE;
        int editVisibility = mIsEditMode ? View.VISIBLE : View.GONE;

        detailName.setVisibility(viewVisibility);
        detailQuantity.setVisibility(viewVisibility);
        detailPrice.setVisibility(viewVisibility);
        detailComment.setVisibility(viewVisibility);

        editName.setVisibility(editVisibility);
        editQuantity.setVisibility(editVisibility);
        editPrice.setVisibility(editVisibility);
        editComment.setVisibility(editVisibility);
        buttonSave.setVisibility(editVisibility);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(mIsEditMode ? "Produkt bearbeiten" : "Produkt-Details");
        }
        
        invalidateOptionsMenu(); // Refresh menu icons
    }

    private void saveChanges() {
        String name = editName.getText().toString().trim();
        String quantityStr = editQuantity.getText().toString().trim();
        String priceStr = editPrice.getText().toString().trim();
        String comment = editComment.getText().toString().trim();

        if (name.isEmpty() || quantityStr.isEmpty() || priceStr.isEmpty()) {
            Toast.makeText(this, "Bitte alle Pflichtfelder ausfüllen", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int quantity = Integer.parseInt(quantityStr);
            double price = Double.parseDouble(priceStr);

            ShoppingItem updatedItem = new ShoppingItem(name, quantity, price, comment);
            updatedItem.setId(mItemId);
            updatedItem.setChecked(mIsChecked);
            
            mViewModel.update(updatedItem);
            
            updateUi(name, quantity, price, comment, mIsChecked);
            toggleEditMode();
            Toast.makeText(this, "Änderungen gespeichert", Toast.LENGTH_SHORT).show();
            
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Ungültige Eingabe für Menge oder Preis", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item_detail, menu);
        MenuItem editItem = menu.findItem(R.id.action_edit);
        if (editItem != null) {
            editItem.setVisible(!mIsEditMode);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.action_edit) {
            toggleEditMode();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}