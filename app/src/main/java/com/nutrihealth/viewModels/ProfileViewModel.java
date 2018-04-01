package com.nutrihealth.viewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.nutrihealth.model.ProfileInfos;
import com.nutrihealth.model.BaseResponse;
import com.nutrihealth.repository.ProfileRepository;
import com.nutrihealth.repository.Resource;

/**
 * Created by Teo on 3/31/2018.
 */

public class ProfileViewModel extends ViewModel {

    private ProfileRepository profileRepository = new ProfileRepository();

    public LiveData<Resource<BaseResponse>> getEditProfileLiveData() {
        return profileRepository.getProfileLiveData();
    }

    public LiveData<Resource<ProfileInfos>> getUserInfosLiveData() {
        return profileRepository.getUserInfosLiveData();
    }

    public void editProfileInfos(ProfileInfos profileInfos, Context context) {
        profileRepository.editProfileInfos(profileInfos, context);
    }

    public void returnUserInfos(Context context){
        profileRepository.returnUserInfos(context);
    }

}
