package com.nutrihealth.repository;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nutrihealth.database.ProductDao;
import com.nutrihealth.database.ProductDb;
import com.nutrihealth.database.ProductRoomDatabase;
import com.nutrihealth.model.BaseResponse;
import com.nutrihealth.model.HistoryDay;
import com.nutrihealth.model.Product;
import com.nutrihealth.model.ProfileInfos;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Teo on 4/19/2018.
 */

public class HistoryRepository {

    private ProductDao productDao;

    private MutableLiveData<Resource<List<HistoryDay>>> historyLiveData = new MutableLiveData<Resource<List<HistoryDay>>>();
    private MutableLiveData<Resource<List<Product>>> todayHistoryLiveData = new MutableLiveData<Resource<List<Product>>>();
    private MutableLiveData<Resource<BaseResponse>> setProductLiveData = new MutableLiveData<Resource<BaseResponse>>();

    public LiveData<Resource<List<HistoryDay>>> getHistoryLiveData(){
        return historyLiveData;
    }
    public LiveData<Resource<List<Product>>> getTodayHistoryLiveData(){
        return todayHistoryLiveData;
    }
    public LiveData<Resource<BaseResponse>> getSetProductLiveData(){
        return setProductLiveData;
    }

    public HistoryRepository(Application application) {
        ProductRoomDatabase db = ProductRoomDatabase.getDatabase(application);
        productDao = db.productDao();
    }

    public void getHistory(){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.keepSynced(true);
        Query query = reference.child("products");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                List<HistoryDay> historyDayList = new ArrayList<>();
                if (dataSnapshot.exists()) {

                    // dataSnapshot is the "issue" node with all children with id 0
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        Product product = issue.getValue(Product.class);

                        if(product.getUserId().equals(user.getUid())){
                            String date = product.getDate();

                            boolean updated = false;
                            for(HistoryDay d : historyDayList){
                                if(d.getDate().equals(date)){
                                    int kcal = Integer.parseInt(d.getKcal()) + Integer.parseInt(product.getKcal());
                                    d.setKcal(String.valueOf(kcal));
                                    updated = true;
                                }
                            }

                            if(updated == false){
                                historyDayList.add(new HistoryDay(product.getDate(), product.getKcal()));
                            }
                        }
                    }
                }
                historyLiveData.setValue(Resource.<List<HistoryDay>>success(historyDayList));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                historyLiveData.setValue(Resource.<List<HistoryDay>>error("Ne pare rau a aparut o eroare"));
            }
        });


    }

    public void getTodayHistory(final String date){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.keepSynced(true);
        Query query = reference.child("products");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                List<Product> productList = new ArrayList<>();
                if (dataSnapshot.exists()) {

                    // dataSnapshot is the "issue" node with all children with id 0
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        Product product = issue.getValue(Product.class);

                        if(product.getUserId().equals(user.getUid()) && product.getDate().equals(date)){
                            productList.add(product);
                        }
                    }
                }
                todayHistoryLiveData.setValue(Resource.success(productList));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                todayHistoryLiveData.setValue(Resource.<List<Product>>error("Ne pare rau a aparut o eroare"));
            }
        });


    }

    public void setProduct(Product product){


        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);
        //build child
        mDatabase.child("products").push().setValue(product);
        setProductLiveData.setValue(Resource.success(new BaseResponse("Produsul a fost adaugat cu succes")));
    }
}
