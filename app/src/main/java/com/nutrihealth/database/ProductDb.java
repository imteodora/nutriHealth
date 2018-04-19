package com.nutrihealth.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

/**
 * Created by Teo on 4/19/2018.
 */

@Entity(tableName = "products")
public class ProductDb {

    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "type")
    private int type;

    @ColumnInfo(name = "kcal")
    private String kcal;

    @ColumnInfo(name = "date")
    private String date;

    public ProductDb(String name, int type, String kcal, String date) {
        this.name = name;
        this.type = type;
        this.kcal = kcal;
        this.date = date;
    }

    public int getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }

    public String getKcal() {
        return kcal;
    }

    public String getDate() {
        return date;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setKcal(String kcal) {
        this.kcal = kcal;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }
}
