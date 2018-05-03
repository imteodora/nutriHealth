package com.nutrihealth.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RadioButton;

import com.nutrihealth.R;
import com.nutrihealth.base.BaseActivity;
import com.nutrihealth.constants.Constants;
import com.nutrihealth.databinding.ActivityProfileBinding;
import com.nutrihealth.model.BaseResponse;
import com.nutrihealth.model.ProfileInfos;
import com.nutrihealth.prefs.PrefsManager;
import com.nutrihealth.repository.Resource;
import com.nutrihealth.utils.BitmapUtils;
import com.nutrihealth.utils.FontUtils;
import com.nutrihealth.utils.InputValidator;
import com.nutrihealth.utils.IntentStarter;
import com.nutrihealth.utils.PermissionUtil;
import com.nutrihealth.utils.WeightUtils;
import com.nutrihealth.viewModels.ProfileViewModel;
import com.nutrihealth.views.CustomToolbar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Teo on 3/30/2018.
 */

public class ProfileActivity extends BaseActivity {

    private static final int CAMERA_PERMISSION_REQ_CODE = 1071;
    private static final int READ_EXTERNAL_STORAGE_REQ_CODE = 1453;

    private static final int TAKE_PHOTO_REQUEST_CODE = 1919;
    private static final int PICK_PHOTO_REQUEST_CODE = 1923;

    private ActivityProfileBinding binding;
    private ProfileViewModel viewModel;

    private Uri cameraImageFileUri;
    private int selectedLvl = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(ProfileActivity.this).get(ProfileViewModel.class);
        binding = DataBindingUtil.setContentView(ProfileActivity.this, R.layout.activity_profile);

        binding.setProfileActivity(this);
        binding.setShowProgressBar(false);


        setUpViews();
        listenToLiveData();

        if (getIntent().hasExtra(Constants.EDIT_INFO_CODE)) {
            binding.setShowProgressBar(true);
            viewModel.returnUserInfos();
            binding.profileToolbar.setOnBackButtonPressedListener(new CustomToolbar.OnBackButtonPressedListener() {
                @Override
                public void onBackButtonPressed() {
                    finish();
                    overridePendingTransition(R.anim.activity_slide_back_in_down, R.anim.activity_slide_out_back_down);
                }
            });

        }

    }

    private void listenToLiveData() {

        viewModel.getEditProfileLiveData().observe(ProfileActivity.this, new Observer<Resource<BaseResponse>>() {
            @Override
            public void onChanged(@Nullable Resource<BaseResponse> baseResponseResource) {
                if (baseResponseResource.getState() == Resource.State.LOADING) {
                    binding.setShowProgressBar(true);
                } else if (baseResponseResource.getState() == Resource.State.ERROR) {
                    binding.setShowProgressBar(false);
                    showCustomDialog(getResources().getString(R.string.error_title), baseResponseResource.getMessage(), DialogType.ERROR, null);
                } else if (baseResponseResource.getState() == Resource.State.SUCCESS) {
                    binding.setShowProgressBar(false);
                    showCustomDialog(getResources().getString(R.string.success), getResources().getString(R.string.profile_infos_saved), DialogType.SUCCESS, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dismissCustomDialog();
                            IntentStarter.gotoHomeActivity(ProfileActivity.this, true);
                        }
                    });

                }
            }
        });

        viewModel.getUserInfosLiveData().observe(ProfileActivity.this, new Observer<Resource<ProfileInfos>>() {
            @Override
            public void onChanged(@Nullable Resource<ProfileInfos> profileInfosResource) {
                if (profileInfosResource.getState() == Resource.State.LOADING) {
                    binding.setShowProgressBar(true);
                } else if (profileInfosResource.getState() == Resource.State.ERROR) {
                    binding.setShowProgressBar(false);
                    showCustomDialog(getResources().getString(R.string.error_title), profileInfosResource.getMessage(), DialogType.ERROR, null);
                } else if (profileInfosResource.getState() == Resource.State.SUCCESS) {
                    binding.setShowProgressBar(false);

                    populateViews(profileInfosResource.getData());

                }
            }
        });
    }

    private void setDividerColor(NumberPicker picker, int color) {

        java.lang.reflect.Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (java.lang.reflect.Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
                    ColorDrawable colorDrawable = new ColorDrawable(color);
                    pf.set(picker, colorDrawable);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                }
                catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.activity_slide_back_in_down, R.anim.activity_slide_out_back_down);
    }

    private void populateViews(ProfileInfos data) {
        String base64Image = PrefsManager.getInstance(ProfileActivity.this).getKeyPicture();
        binding.userProfileIv.setImageBitmap(BitmapUtils.decodeBase64(base64Image));

        binding.nameEt.setText(data.getName());
        binding.currentWeightEt.setText(Integer.toString(data.getActualWeight()));
        binding.heightEt.setText(Integer.toString(data.getHeight()));
        binding.ageEt.setText(Integer.toString(data.getAge()));

        selectedLvl = data.getActivityLvl();
        switch (data.getActivityLvl()) {
            case 0:
                binding.everyDaySportEt.setText(getResources().getString(R.string.lvl_1));
                break;
            case 1:
                binding.everyDaySportEt.setText(getResources().getString(R.string.lvl_2));
                break;
            case 2:
                binding.everyDaySportEt.setText(getResources().getString(R.string.lvl_3));
                break;
            case 3:
                binding.everyDaySportEt.setText(getResources().getString(R.string.lvl_4));
                break;
            case 4:
                binding.everyDaySportEt.setText(getResources().getString(R.string.lvl_5));
                break;
        }

        if (data.getGender().equals(Constants.GENDER_M)) {
            binding.mRb.setChecked(true);
        } else {
            binding.fRb.setChecked(true);
        }


    }

    private void setUpViews() {

        setStatusBarColor(R.color.black);
        setupUI(binding.profileParent);
        binding.ageEt.setEnabled(true);
        binding.setShowProgressBar(false);
        setFontToButtons();

    }

    private void setFontToButtons() {

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Bold.ttf");
        binding.saveButton.setTypeface(typeface);
    }

    public void onSelectAgePressed(View view) {

        final Dialog pickAgeDialog = new Dialog(ProfileActivity.this);
        pickAgeDialog.setContentView(R.layout.dialog_number_picker);

        Button saveBtn = (Button) pickAgeDialog.findViewById(R.id.save_button);
        final NumberPicker agePicker = (NumberPicker) pickAgeDialog.findViewById(R.id.numberPicker);
        agePicker.setMaxValue(90); // max value 100
        agePicker.setMinValue(18);   // min value 0

        setDividerColor(agePicker, ContextCompat.getColor(ProfileActivity.this,R.color.light_blue));
        String selectedAge = binding.ageEt.getText().toString();
        if(!InputValidator.isInputEmpty(selectedAge)){
            agePicker.setValue(Integer.valueOf(selectedAge));
        }
        agePicker.setWrapSelectorWheel(false);

        Window window = pickAgeDialog.getWindow();
        window.setGravity(Gravity.TOP);
        window.getAttributes().windowAnimations = R.style.DialogAnimationUpToDown;
        window.setBackgroundDrawableResource(android.R.color.white);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.ageEt.setText(String.valueOf(agePicker.getValue()));
                pickAgeDialog.dismiss();
            }
        });
        pickAgeDialog.show();
    }

    public void onSelectSports(View view) {
        showOneChoiceDialog();
    }

    public void showOneChoiceDialog() {

        final CharSequence[] items = new CharSequence[Constants.NUMBER_OF_ACTIVITIES_TYPES];
        items[0] = getResources().getString(R.string.activity_lvl_1);
        items[1] = getResources().getString(R.string.activity_lvl_2);
        items[2] = getResources().getString(R.string.activity_lvl_3);
        items[3] = getResources().getString(R.string.activity_lvl_4);
        items[4] = getResources().getString(R.string.activity_lvl_5);


        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Bold.ttf");

        final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(ProfileActivity.this);
        builder.setTitle(FontUtils.typeface(typeface, getResources().getString(R.string.choose_activities_level)));
        builder.setSingleChoiceItems(items, selectedLvl, null);
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.dialog_choose_activity, null);
        builder.setView(dialogLayout);
        Button closeButton = dialogLayout.findViewById(R.id.save_button);

        final android.support.v7.app.AlertDialog alert = builder.create();
        alert.show();
        alert.getButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE).setTypeface(typeface);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dismiss();
                int selectedPosition = alert.getListView().getCheckedItemPosition();
                selectedLvl = selectedPosition;
                switch (selectedPosition) {
                    case 0:
                        binding.everyDaySportEt.setText(getResources().getString(R.string.lvl_1));
                        break;
                    case 1:
                        binding.everyDaySportEt.setText(getResources().getString(R.string.lvl_2));
                        break;
                    case 2:
                        binding.everyDaySportEt.setText(getResources().getString(R.string.lvl_3));
                        break;
                    case 3:
                        binding.everyDaySportEt.setText(getResources().getString(R.string.lvl_4));
                        break;
                    case 4:
                        binding.everyDaySportEt.setText(getResources().getString(R.string.lvl_5));
                        break;
                }
            }
        });


        Window window = alert.getWindow();
        window.setGravity(Gravity.TOP);
        window.getAttributes().windowAnimations = R.style.DialogAnimationUpToDown;
        window.setBackgroundDrawableResource(android.R.color.white);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    }

    public void onEditProfileImagePressed(View view) {
        showUpdatePictureDialog();
    }

    public void onSaveInfosPressed(View view) {

        binding.nameEt.setError(null);
        binding.currentWeightEt.setError(null);
        binding.heightEt.setError(null);
        binding.ageEt.setError(null);

        String name = binding.nameEt.getText().toString();
        String currentWeight = binding.currentWeightEt.getText().toString();
        String gender = null;
        String height = binding.heightEt.getText().toString();
        String age = binding.ageEt.getText().toString();
        String activityType = binding.everyDaySportEt.getText().toString();

        int idRadioButton = binding.genderGroup.getCheckedRadioButtonId();
        View radioButton = binding.genderGroup.findViewById(idRadioButton);
        int idx = binding.genderGroup.indexOfChild(radioButton);
        if (idx != -1) {
            RadioButton r = (RadioButton) binding.genderGroup.getChildAt(idx);
            gender = r.getText().toString();
        }

        if (InputValidator.isInputEmpty(name)) {
            showCustomDialog(getResources().getString(R.string.error_title), getResources().getString(R.string.error_no_name), DialogType.ERROR, null);
            binding.nameEt.setError(getResources().getString(R.string.error_no_input));
            return;
        }

        if (InputValidator.isInputEmpty(currentWeight)) {
            showCustomDialog(getResources().getString(R.string.error_title), getResources().getString(R.string.error_no_current_weight), DialogType.ERROR, null);
            binding.currentWeightEt.setError(getResources().getString(R.string.error_no_input));
            return;
        }

        if (gender == null) {
            showCustomDialog(getResources().getString(R.string.error_title), getResources().getString(R.string.error_no_gender), DialogType.ERROR, null);
            return;
        }

        if (InputValidator.isInputEmpty(height)) {
            showCustomDialog(getResources().getString(R.string.error_title), getResources().getString(R.string.error_no_height), DialogType.ERROR, null);
            binding.heightEt.setError(getResources().getString(R.string.error_no_input));
            return;
        }

        if (InputValidator.isInputEmpty(age)) {
            showCustomDialog(getResources().getString(R.string.error_title), getResources().getString(R.string.error_no_age), DialogType.ERROR, null);
            return;
        }

        if (InputValidator.isInputEmpty(activityType)) {
            showCustomDialog(getResources().getString(R.string.error_title), getResources().getString(R.string.error_no_activity_type), DialogType.ERROR, null);
            return;
        }

        double activity = 1;
        switch (selectedLvl) {
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

        int idealWeight = WeightUtils.calculateIdealWeight(Integer.parseInt(height), Integer.parseInt(age), gender);
        int kcalPerDay = WeightUtils.calculateCalPerDay(Integer.parseInt(height),  Integer.parseInt(age), idealWeight,gender, activity);

        PrefsManager.getInstance(ProfileActivity.this).setKeyKcalPerDay(kcalPerDay);
        binding.setShowProgressBar(true);
        viewModel.editProfileInfos(new ProfileInfos(name, Integer.parseInt(currentWeight), gender, Integer.parseInt(height), Integer.parseInt(age), selectedLvl, idealWeight,kcalPerDay));
    }

    private void showUpdatePictureDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this)
                .setTitle(getResources().getString(R.string.edit_profile_pic))
                .setNegativeButton(getResources().getString(R.string.from_gallery), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        askForReadExternalStoragePermission();
                    }
                })
                .setPositiveButton(getResources().getString(R.string.take_a_pic), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        askForCameraPermission();
                    }
                });
        builder.show();
    }

    private void askForCameraPermission() {
        final String permissions[] = {Manifest.permission.CAMERA};
        if (PermissionUtil.hasPermissions(ProfileActivity.this, permissions)) {
            addPictureFromCamera();
        } else {
            ActivityCompat.requestPermissions(ProfileActivity.this, permissions, CAMERA_PERMISSION_REQ_CODE);
        }
    }

    private void askForReadExternalStoragePermission() {
        final String permissions[] = {Manifest.permission.READ_EXTERNAL_STORAGE};
        if (PermissionUtil.hasPermissions(ProfileActivity.this, permissions)) {
            addPictureFromGallery();
        } else {
            ActivityCompat.requestPermissions(ProfileActivity.this, permissions, READ_EXTERNAL_STORAGE_REQ_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERMISSION_REQ_CODE) {
            if (checkAllPermissionsGranted(grantResults)) {
                addPictureFromCamera();
            }
        } else if (requestCode == READ_EXTERNAL_STORAGE_REQ_CODE) {
            if (checkAllPermissionsGranted(grantResults)) {
                addPictureFromGallery();
            }
        }
    }

    private boolean checkAllPermissionsGranted(@NonNull int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void addPictureFromGallery() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, PICK_PHOTO_REQUEST_CODE);
    }

    private void addPictureFromCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        getApplicationContext().getPackageName() + ".my.package.name.provider",
                        photoFile);
                cameraImageFileUri = photoURI;
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, TAKE_PHOTO_REQUEST_CODE);
            }
        }

    }

    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PICK_PHOTO_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    Uri imageUri = data.getData();
                    Bitmap resizedBitmap = null;
                    try {
                        Bitmap bitmapFromUri = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        resizedBitmap = BitmapUtils.resizeImage(ProfileActivity.this, bitmapFromUri);

                        ExifInterface ei = new ExifInterface(BitmapUtils.getRealPathFromUri(ProfileActivity.this, imageUri));
                        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

                        Bitmap rotatedBitmap = resizedBitmap;

                        switch (orientation) {
                            case ExifInterface.ORIENTATION_ROTATE_270:
                                rotatedBitmap = BitmapUtils.rotateBitmap(rotatedBitmap, 270);
                                break;

                            case ExifInterface.ORIENTATION_ROTATE_90:
                                rotatedBitmap = BitmapUtils.rotateBitmap(rotatedBitmap, 90);
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_180:
                                rotatedBitmap = BitmapUtils.rotateBitmap(rotatedBitmap, 180);
                                break;
                        }


                        resizedBitmap = rotatedBitmap;
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (resizedBitmap != null) {

                        binding.userProfileIv.setImageBitmap(resizedBitmap);
                        PrefsManager.getInstance(ProfileActivity.this).putKeyPicture(BitmapUtils.encodeBitmapToBase64(resizedBitmap));
                    }


                }
                break;
            case TAKE_PHOTO_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    File imgFile = new File(mCurrentPhotoPath);

                    if (imgFile.exists()) {

                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        Bitmap bitmapPhoto = null;
                        try {
                            bitmapPhoto = BitmapUtils.resizeImage(ProfileActivity.this, myBitmap);

                            ExifInterface ei = new ExifInterface(mCurrentPhotoPath);
                            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

                            Bitmap rotatedBitmap = bitmapPhoto;

                            switch (orientation) {
                                case ExifInterface.ORIENTATION_ROTATE_270:
                                    rotatedBitmap = BitmapUtils.rotateBitmap(rotatedBitmap, 270);
                                    break;

                                case ExifInterface.ORIENTATION_ROTATE_90:
                                    rotatedBitmap = BitmapUtils.rotateBitmap(rotatedBitmap, 90);
                                    break;
                                case ExifInterface.ORIENTATION_ROTATE_180:
                                    rotatedBitmap = BitmapUtils.rotateBitmap(rotatedBitmap, 180);
                                    break;
                            }


                            bitmapPhoto = rotatedBitmap;
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if (bitmapPhoto != null) {

                            binding.userProfileIv.setImageBitmap(bitmapPhoto);
                            PrefsManager.getInstance(ProfileActivity.this).putKeyPicture(BitmapUtils.encodeBitmapToBase64(bitmapPhoto));

                        }


                    }


                }
                break;
            default:
                break;
        }
    }
}
