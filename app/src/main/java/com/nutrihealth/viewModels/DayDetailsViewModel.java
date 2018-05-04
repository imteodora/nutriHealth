package com.nutrihealth.viewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.nutrihealth.model.Product;
import com.nutrihealth.repository.HistoryRepository;
import com.nutrihealth.repository.Resource;

import java.util.List;

/**
 * Created by Teo on 5/4/2018.
 */

public class DayDetailsViewModel extends AndroidViewModel {

    private HistoryRepository historyRepository;

    public DayDetailsViewModel(Application application) {
        super(application);
        historyRepository = new HistoryRepository(application);

    }

    public LiveData<Resource<List<Product>>> getTodayHistoryLiveData() {
        return historyRepository.getTodayHistoryLiveData();
    }

    public void getAllProductsForToday(String date){
        historyRepository.getTodayHistory(date);
    }
}
