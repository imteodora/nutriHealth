package com.nutrihealth.viewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.nutrihealth.model.ProfileInfos;
import com.nutrihealth.model.SuccesResponse;
import com.nutrihealth.repository.ProfileRepository;
import com.nutrihealth.repository.Resource;

/**
 * Created by Teo on 3/31/2018.
 */

public class ProfileViewModel extends ViewModel {

    private ProfileRepository profileRepository;

    public LiveData<Resource<SuccesResponse>> getEditProfileLiveData() {
        return profileRepository.getProfileLiveData();
    }

    public void editProfileInfos(ProfileInfos profileInfos) {
        profileRepository.editProfileInfos(profileInfos);
    }

}
