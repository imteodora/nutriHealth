package com.nutrihealth.repository;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.os.AsyncTask;

import com.nutrihealth.R;
import com.nutrihealth.app.NutriHealthApp;
import com.nutrihealth.constants.Constants;
import com.nutrihealth.model.HomeInfos;
import com.nutrihealth.model.ProfileInfos;
import com.nutrihealth.model.BaseResponse;
import com.nutrihealth.prefs.PrefsManager;
import com.nutrihealth.utils.WeightUtils;

/**
 * Created by Teo on 3/31/2018.
 */

public class ProfileRepository {

    private MutableLiveData<Resource<BaseResponse>> profileInfoLiveData = new MutableLiveData<Resource<BaseResponse>>();
    private MutableLiveData<Resource<ProfileInfos>> userInfosLiveData = new MutableLiveData<Resource<ProfileInfos>>();
    private MutableLiveData<Resource<HomeInfos>> homeUserInfosLiveData = new MutableLiveData<>();

    public LiveData<Resource<BaseResponse>> getProfileLiveData(){
        return profileInfoLiveData;
    }
    public LiveData<Resource<ProfileInfos>> getUserInfosLiveData(){
        return userInfosLiveData;
    }
    public LiveData<Resource<HomeInfos>> getHomeUserinfosLiveData(){
        return homeUserInfosLiveData;
    }

    public void editProfileInfos(final ProfileInfos profileInfos, final Context context){

        @SuppressLint("StaticFieldLeak") AsyncTask<Void, Void, Void> saveProfileInfoTask = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                profileInfoLiveData.postValue(Resource.<BaseResponse>loading());
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... voids) {

                PrefsManager.getInstance(context).putKeyName(profileInfos.getName());
                PrefsManager.getInstance(context).putKeyActualWeight(profileInfos.getActualWeight());
                PrefsManager.getInstance(context).putKeyGender(profileInfos.getGender());
                PrefsManager.getInstance(context).putKeyHeight(profileInfos.getHeight());
                PrefsManager.getInstance(context).putKeyAge(profileInfos.getAge());
                PrefsManager.getInstance(context).putKeyActivityLvl(profileInfos.getActivityLvl());

                int idealWeight = WeightUtils.calculateIdealWeight(profileInfos.getHeight(),profileInfos.getAge(),profileInfos.getGender());
                PrefsManager.getInstance(context).putKeyIdealWeight(idealWeight);

                double activity = 1;
                switch (profileInfos.getActivityLvl()){
                    case 0:
                        activity = Constants.SPORT_ACTIVITY_LVL_1;
                        break;
                    case 1:
                        activity = Constants.SPORT_ACTIVITY_LVL_2;
                        break;
                    case 2:
                        activity = Constants.SPORT_ACTIVITY_LVL_3;
                        break;
                    case 3:
                        activity = Constants.SPORT_ACTIVITY_LVL_4;
                        break;
                    case 4:
                        activity = Constants.SPORT_ACTIVITY_LVL_5;
                        break;

                }

                int kcalPerDay = WeightUtils.calculateCalPerDay(profileInfos.getHeight(), profileInfos.getAge(),idealWeight,profileInfos.getGender(),activity);
                PrefsManager.getInstance(context).putKeyKcalPerDay(kcalPerDay);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                profileInfoLiveData.postValue(Resource.success(new BaseResponse(context.getResources().getString(R.string.profile_infos_saved))));
                super.onPostExecute(aVoid);
            }
        };

        saveProfileInfoTask.execute();

    }

    public void returnUserInfos(final Context context){
        @SuppressLint("StaticFieldLeak") AsyncTask<Void, Void, ProfileInfos> getUserInfos = new AsyncTask<Void, Void, ProfileInfos>() {

            @Override
            protected void onPreExecute() {
                userInfosLiveData.postValue(Resource.<ProfileInfos>loading());
                super.onPreExecute();
            }

            @Override
            protected ProfileInfos doInBackground(Void... voids) {

                String name = PrefsManager.getInstance(context).getKeyName();
                int actualWeight = PrefsManager.getInstance(context).getKeyActualWeight();
                int height = PrefsManager.getInstance(context).getKeyHeight();
                String gender = PrefsManager.getInstance(context).getKeyGender();
                int age = PrefsManager.getInstance(context).getKeyAge();
                int activityLvl = PrefsManager.getInstance(context).getKeyActivityLvl();

                return new ProfileInfos(name, actualWeight, gender, height, age, activityLvl);
            }

            @Override
            protected void onPostExecute(ProfileInfos profileInfos) {
                userInfosLiveData.postValue(Resource.success(profileInfos));
                super.onPostExecute(profileInfos);
            }
        };

        getUserInfos.execute();
    }

    public void returnHomeUserInfos(final Context context){
        @SuppressLint("StaticFieldLeak") AsyncTask<Void, Void, HomeInfos> getUserInfos = new AsyncTask<Void, Void, HomeInfos>() {

            @Override
            protected void onPreExecute() {
                homeUserInfosLiveData.postValue(Resource.<HomeInfos>loading());
                super.onPreExecute();
            }

            @Override
            protected HomeInfos doInBackground(Void... voids) {

                String base64Image = PrefsManager.getInstance(context).getKeyPicture();
                int actualWeight = PrefsManager.getInstance(context).getKeyActualWeight();
                int idealWeight = PrefsManager.getInstance(context).getKeyIdealWeight();
                int kcalPerDay = PrefsManager.getInstance(context).getKeyKcalPerDay();

                return new HomeInfos(base64Image,actualWeight,idealWeight,kcalPerDay);
            }

            @Override
            protected void onPostExecute(HomeInfos homeInfos) {
                homeUserInfosLiveData.postValue(Resource.success(homeInfos));
                super.onPostExecute(homeInfos);
            }
        };

        getUserInfos.execute();
    }

}
