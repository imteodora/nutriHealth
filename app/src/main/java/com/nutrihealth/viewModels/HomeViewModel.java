package com.nutrihealth.viewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.nutrihealth.model.BaseResponse;
import com.nutrihealth.model.HomeInfos;
import com.nutrihealth.repository.ProfileRepository;
import com.nutrihealth.repository.Resource;

/**
 * Created by Teo on 4/1/2018.
 */

public class HomeViewModel extends ViewModel {

    private ProfileRepository profileRepository = new ProfileRepository();

    public LiveData<Resource<HomeInfos>> getHomeUserInfosLiveData() {
        return profileRepository.getHomeUserinfosLiveData();
    }

    public void returnHomeUserInfos(Context context){
        profileRepository.returnHomeUserInfos(context);
    }
}
