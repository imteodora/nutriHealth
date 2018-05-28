package com.nutrihealth.repository;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.os.AsyncTask;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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

    public LiveData<Resource<BaseResponse>> getProfileLiveData() {
        return profileInfoLiveData;
    }

    public LiveData<Resource<ProfileInfos>> getUserInfosLiveData() {
        return userInfosLiveData;
    }

    public LiveData<Resource<HomeInfos>> getHomeUserinfosLiveData() {
        return homeUserInfosLiveData;
    }

    public void editProfileInfos(final ProfileInfos profileInfos) {

        //get firebase user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);
        //build child
        mDatabase.child("users").child(user.getUid()).setValue(profileInfos);

        profileInfoLiveData.setValue(Resource.success(new BaseResponse("Succes")));


    }

    public void returnUserInfos() {

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        mDatabase.child("users").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ProfileInfos profileInfos = dataSnapshot.getValue(ProfileInfos.class);
                userInfosLiveData.setValue(Resource.success(profileInfos));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                userInfosLiveData.postValue(Resource.<ProfileInfos>error("Ne pare rau a aparut o eroare."));
            }

        });


    }

    public void returnHomeUserInfos(final Context context) {

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        mDatabase.child("users").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ProfileInfos profileInfos = dataSnapshot.getValue(ProfileInfos.class);
                String base64Image = PrefsManager.getInstance(context).getKeyPicture();
                int actualWeight = profileInfos.getActualWeight();
                int idealWeight = profileInfos.getIdealWeight();
                int kcalPerDay = profileInfos.getKcalPerDay();
                homeUserInfosLiveData.setValue(Resource.success(new HomeInfos(base64Image, actualWeight, idealWeight, kcalPerDay)));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                homeUserInfosLiveData.postValue(Resource.<HomeInfos>error("Ne pare rau a aparut o eroare."));
            }

        });




    }

}
