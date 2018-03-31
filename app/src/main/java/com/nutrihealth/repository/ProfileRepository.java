package com.nutrihealth.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.nutrihealth.model.ProfileInfos;
import com.nutrihealth.model.SuccesResponse;

/**
 * Created by Teo on 3/31/2018.
 */

public class ProfileRepository {

    private MutableLiveData<Resource<SuccesResponse>> profileInfoLiveData = new MutableLiveData<Resource<SuccesResponse>>();

    public LiveData<Resource<SuccesResponse>> getProfileLiveData(){
        return profileInfoLiveData;
    }

    public void editProfileInfos(ProfileInfos profileInfos){

    }

}
