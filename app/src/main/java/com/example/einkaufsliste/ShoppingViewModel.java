package com.example.einkaufsliste;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ShoppingViewModel extends AndroidViewModel {
    private final ShoppingItemDao mDao;
    private final LiveData<List<ShoppingItem>> mAllItems;

    public ShoppingViewModel(@NonNull Application application) {
        super(application);
        AppDatabase db = AppDatabase.getDatabase(application);
        mDao = db.shoppingItemDao();
        mAllItems = mDao.getAllItems();
    }

    public LiveData<List<ShoppingItem>> getAllItems() {
        return mAllItems;
    }

    public void insert(ShoppingItem item) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mDao.insert(item);
        });
    }

    public void update(ShoppingItem item) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mDao.update(item);
        });
    }

    public void delete(ShoppingItem item) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mDao.delete(item);
        });
    }
}