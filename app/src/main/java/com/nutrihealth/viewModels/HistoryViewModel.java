package com.nutrihealth.viewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.nutrihealth.model.HistoryDay;
import com.nutrihealth.model.Product;
import com.nutrihealth.repository.HistoryRepository;
import com.nutrihealth.repository.Resource;

import java.util.List;

/**
 * Created by Teo on 4/19/2018.
 */

public class HistoryViewModel extends AndroidViewModel {

    private HistoryRepository historyRepository;

    public HistoryViewModel(Application application) {
        super(application);
        historyRepository = new HistoryRepository(application);
    }

    public LiveData<Resource<List<HistoryDay>>> getHistoryLiveData() {
        return historyRepository.getHistoryLiveData();
    }

    public void sendGetHistoryRequest(){
        historyRepository.getHistory();
    }
}
