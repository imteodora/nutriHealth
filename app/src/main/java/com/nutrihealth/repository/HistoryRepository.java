package com.nutrihealth.repository;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;

import com.nutrihealth.database.ProductDao;
import com.nutrihealth.database.ProductDb;
import com.nutrihealth.database.ProductRoomDatabase;
import com.nutrihealth.model.BaseResponse;
import com.nutrihealth.model.HistoryDay;
import com.nutrihealth.model.Product;

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

        @SuppressLint("StaticFieldLeak") AsyncTask<Void, Void, List<ProductDb>> getHistoryTask = new AsyncTask<Void, Void, List<ProductDb>>() {
            @Override
            protected List<ProductDb> doInBackground(Void... voids) {
                List<ProductDb> productDbList = productDao.getAll();
                return productDbList;
            }

            @Override
            protected void onPostExecute(List<ProductDb> productDbList) {
                super.onPostExecute(productDbList);
                List<HistoryDay> historyDayList = new ArrayList<>();

                for(ProductDb p : productDbList){
                    String date = p.getDate();

                    boolean updated = false;
                    for(HistoryDay d : historyDayList){
                        if(d.getDate().equals(date)){
                            int kcal = Integer.parseInt(d.getKcal()) + Integer.parseInt(p.getKcal());
                            d.setKcal(String.valueOf(kcal));
                            updated = true;
                        }
                    }

                    if(updated == false){
                        historyDayList.add(new HistoryDay(p.getDate(), p.getKcal()));
                    }
                }

                historyLiveData.setValue(Resource.success(historyDayList));
            }
        };
        getHistoryTask.execute();


    }

    public void getTodayHistory(final String date){

        @SuppressLint("StaticFieldLeak") AsyncTask<Void, Void, List<ProductDb>> getTodayHistoryTask = new AsyncTask<Void, Void, List<ProductDb>>() {
            @Override
            protected List<ProductDb> doInBackground(Void... voids) {
                List<ProductDb> productDbList = productDao.getAllForToday(date);
                return productDbList;
            }

            @Override
            protected void onPostExecute(List<ProductDb> productDbList) {
                super.onPostExecute(productDbList);
                List<Product> productList = new ArrayList<>();

                for(ProductDb p : productDbList)
                    productList.add(new Product(p.getName(),p.getKcal(), p.getType(), p.getDate()));

                todayHistoryLiveData.setValue(Resource.success(productList));
            }
        };
        getTodayHistoryTask.execute();




    }

    public void setProduct(final ProductDb product){
        @SuppressLint("StaticFieldLeak") AsyncTask<Void, Void, Void> insertProducTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                productDao.insertProduct(product);
                return null;
            }

            @Override
            protected void onPostExecute(Void offerEntity) {
                super.onPostExecute(offerEntity);
                setProductLiveData.setValue(Resource.success(new BaseResponse("Informatiile au fost adaugate cu succes")));
            }
        };
        insertProducTask.execute();
    }
}
