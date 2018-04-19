package com.nutrihealth.viewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.nutrihealth.database.ProductDb;
import com.nutrihealth.model.BaseResponse;
import com.nutrihealth.model.Product;
import com.nutrihealth.model.ProfileInfos;
import com.nutrihealth.repository.HistoryRepository;
import com.nutrihealth.repository.Resource;

import java.util.List;

/**
 * Created by Teo on 4/19/2018.
 */

public class TodayViewModel extends AndroidViewModel {

    private HistoryRepository historyRepository;

    public TodayViewModel(Application application) {
        super(application);
        historyRepository = new HistoryRepository(application);

    }

    public LiveData<Resource<List<Product>>> getTodayHistoryLiveData() {
        return historyRepository.getTodayHistoryLiveData();
    }

    public LiveData<Resource<BaseResponse>> getSetProductLiveData() {
        return historyRepository.getSetProductLiveData();
    }

    public void addProduct(Product product){
        historyRepository.setProduct(new ProductDb(product.getName(),product.getType(),product.getKcal(),product.getDate()));
    }

    public void getAllProductsForToday(String date){
        historyRepository.getTodayHistory(date);
    }
}
