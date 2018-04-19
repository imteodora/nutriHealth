package com.nutrihealth.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by Teo on 4/19/2018.
 */


@Dao
public interface ProductDao {

    @Query("SELECT * FROM products")
    List<ProductDb> getAll();

    @Query("SELECT * FROM products WHERE date LIKE :date")
    List<ProductDb> getAllForToday(String date);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public void insertProduct(ProductDb productDb);
}
