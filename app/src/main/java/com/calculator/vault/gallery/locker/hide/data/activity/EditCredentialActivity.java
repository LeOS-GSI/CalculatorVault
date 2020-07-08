package com.calculator.vault.gallery.locker.hide.data.activity;

import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.calculator.vault.gallery.locker.hide.data.R;
import com.calculator.vault.gallery.locker.hide.data.ads.AdsListener;
import com.calculator.vault.gallery.locker.hide.data.ads.IntAdsHelper;
import com.calculator.vault.gallery.locker.hide.data.common.OnSingleClickListener;
import com.calculator.vault.gallery.locker.hide.data.common.Share;
import com.calculator.vault.gallery.locker.hide.data.common.SharedPrefs;
import com.calculator.vault.gallery.locker.hide.data.common.Utils;
import com.calculator.vault.gallery.locker.hide.data.data.DecoyDatabase;
import com.calculator.vault.gallery.locker.hide.data.data.ImageVideoDatabase;
import com.calculator.vault.gallery.locker.hide.data.model.CredentialsModel;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubInterstitial;

public class EditCredentialActivity extends AppCompatActivity implements View.OnClickListener
        /*MoPubInterstitial.InterstitialAdListener*//*InterstitialAdHelper.onInterstitialAdListener*/ {

    private static final String TAG = "EditCredentialActivity";
    private EditText moEtTitleCredText, moEtUsernameCredText;
    private EditText moEtwebsiteCredText, moEtPasswordCredText;
    private EditText moEtNotesCredText;
    private TextView ll_save_creds, btnClearCredentialsE;
    private String mscredTitle, mscredWebsite, mscredUsername, mscredPassword, mscredNotes;
    private ImageVideoDatabase imageVideoDatabase = new ImageVideoDatabase(this);
    private int miID;
    private ImageView iv_back;
    private DecoyDatabase decoyDatabase = new DecoyDatabase(this);
    private String isDecoy;
    private RelativeLayout moRlMain;
    //private FrameLayout moFlNativeAds;

    private ImageView moIvMoreApp, moIvBlast;

//    private boolean isInterstitialAdLoaded = false;
//    private MoPubInterstitial mInterstitial;
//    TODO Admob Ads
//    private InterstitialAd interstitial;

//    @Override
//    public void onInterstitialLoaded(MoPubInterstitial moPubInterstitial) {
//        if (Share.isNeedToAdShow(EditCredentialActivity.this)) {
//            isInterstitialAdLoaded = true;
//            moIvBlast.setVisibility(View.GONE);
//            moIvMoreApp.setVisibility(View.VISIBLE);
//        }
//    }
//
//    @Override
//    public void onInterstitialFailed(MoPubInterstitial moPubInterstitial, MoPubErrorCode moPubErrorCode) {
//        Log.e(TAG, "onInterstitialFailed: "+ moPubErrorCode);
//        isInterstitialAdLoaded = false;
//        if (Utils.isNetworkConnected(EditCredentialActivity.this)) {
//            mInterstitial.load();
//        }
//        moIvBlast.setVisibility(View.GONE);
//        moIvMoreApp.setVisibility(View.GONE);
//    }
//
//    @Override
//    public void onInterstitialShown(MoPubInterstitial moPubInterstitial) {
//
//    }
//
//    @Override
//    public void onInterstitialClicked(MoPubInterstitial moPubInterstitial) {
//
//    }
//
//    @Override
//    public void onInterstitialDismissed(MoPubInterstitial moPubInterstitial) {
//        isInterstitialAdLoaded = false;
//        if (Utils.isNetworkConnected(EditCredentialActivity.this)) {
//            mInterstitial.load();
//        }
//        moIvBlast.setVisibility(View.GONE);
//        moIvMoreApp.setVisibility(View.GONE);
//    }

//    @Override
//    public void onLoad() {
//        isInterstitialAdLoaded = true;
//        moIvBlast.setVisibility(View.GONE);
//        moIvMoreApp.setVisibility(View.VISIBLE);
//    }
//
//    @Override
//    public void onFailed() {
//        isInterstitialAdLoaded = false;
//        if (interstitial.getAdUnitId().equalsIgnoreCase(getString(R.string.inter_ad_unit_id1))){
//            interstitial = InterstitialAdHelper.getInstance().load2(EditCredentialActivity.this, this);
//        }else if (interstitial.getAdUnitId().equalsIgnoreCase(getString(R.string.inter_ad_unit_id2))){
//            interstitial = InterstitialAdHelper.getInstance().load(EditCredentialActivity.this, this);
//        }else {
//            interstitial = InterstitialAdHelper.getInstance().load1(EditCredentialActivity.this, this);
//        }
//
//        moIvBlast.setVisibility(View.GONE);
//        moIvMoreApp.setVisibility(View.GONE);
//    }
//
//    @Override
//    public void onClosed() {
//        isInterstitialAdLoaded = false;
//        interstitial = InterstitialAdHelper.getInstance().load1(EditCredentialActivity.this, this);
//
//        moIvBlast.setVisibility(View.GONE);
//        moIvMoreApp.setVisibility(View.GONE);
//
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_credential);

//        mInterstitial = new MoPubInterstitial(this, getString(R.string.mopub_int_id));
//        mInterstitial.setInterstitialAdListener(this);
//            mInterstitial.load();
            IntAdsHelper.loadInterstitialAds(EditCredentialActivity.this, new AdsListener() {
                @Override
                public void onLoad() {
                    moIvBlast.setVisibility(View.GONE);
                    moIvMoreApp.setVisibility(View.VISIBLE);
                }

                @Override
                public void onClosed() {
                    moIvBlast.setVisibility(View.GONE);
                    moIvMoreApp.setVisibility(View.GONE);
                }
            });


//        TODO Admob Ads
//        interstitial = InterstitialAdHelper.getInstance().load1(EditCredentialActivity.this, this);

        initView();
        isDecoy = SharedPrefs.getString(EditCredentialActivity.this, SharedPrefs.DecoyPassword, "false");
        initViewListener();
        initViewAction();
    }

    private void initViewAction() {
        miID = getIntent().getIntExtra("editIntentID", 0);
        CredentialsModel credentialsModel;
        if (isDecoy.equals("true")) {
            credentialsModel = decoyDatabase.getSingleCredentialData(miID);
        } else {
            credentialsModel = imageVideoDatabase.getSingleCredentialData(miID);
        }

        Log.e("EditActivity", "initViewAction: ID: " + miID);

        moEtTitleCredText.setText(credentialsModel.getTitleText());
        Log.e("EditActivity", "initViewAction: Title: " + credentialsModel.getTitleText());
        moEtTitleCredText.setSelection(moEtTitleCredText.getText().length());

        moEtwebsiteCredText.setText(credentialsModel.getWebsite());
        Log.e("EditActivity", "initViewAction: Website: " + credentialsModel.getWebsite());
        moEtwebsiteCredText.setSelection(moEtwebsiteCredText.getText().length());

        moEtPasswordCredText.setText(credentialsModel.getPassword());
        Log.e("EditActivity", "initViewAction: Password: " + credentialsModel.getPassword());
        moEtPasswordCredText.setSelection(moEtPasswordCredText.getText().length());

        moEtUsernameCredText.setText(credentialsModel.getUsername());
        Log.e("EditActivity", "initViewAction: Username: " + credentialsModel.getUsername());
        moEtUsernameCredText.setSelection(moEtUsernameCredText.getText().length());

        moEtNotesCredText.setText(credentialsModel.getNotes());
        Log.e("EditActivity", "initViewAction: Notes: " + credentialsModel.getNotes());
        moEtNotesCredText.setSelection(moEtNotesCredText.getText().length());

        findViewById(R.id.iv_copy_title).setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (!moEtTitleCredText.getText().toString().trim().isEmpty())
                    Utils.copyToClipBoard(EditCredentialActivity.this, "Title", moEtTitleCredText.getText().toString().trim());
            }
        });

        findViewById(R.id.iv_copy_website).setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (!moEtwebsiteCredText.getText().toString().trim().isEmpty())
                    Utils.copyToClipBoard(EditCredentialActivity.this, "Website", moEtwebsiteCredText.getText().toString().trim());
            }
        });

        findViewById(R.id.iv_copy_username).setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (!moEtUsernameCredText.getText().toString().trim().isEmpty())
                    Utils.copyToClipBoard(EditCredentialActivity.this, "Username", moEtUsernameCredText.getText().toString().trim());
            }
        });

        findViewById(R.id.iv_copy_password).setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (!moEtPasswordCredText.getText().toString().trim().isEmpty())
                    Utils.copyToClipBoard(EditCredentialActivity.this, "Password", moEtPasswordCredText.getText().toString());
            }
        });

        findViewById(R.id.iv_copy_notes).setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (!moEtNotesCredText.getText().toString().trim().isEmpty())
                    Utils.copyToClipBoard(EditCredentialActivity.this, "Notes", moEtNotesCredText.getText().toString().trim());
            }
        });

    }

    private void initViewListener() {
        ll_save_creds.setOnClickListener(this);
        btnClearCredentialsE.setOnClickListener(this);
        iv_back.setOnClickListener(this);

        moRlMain.getViewTreeObserver().addOnGlobalLayoutListener(
                () -> {

                    Rect r = new Rect();
                    moRlMain.getWindowVisibleDisplayFrame(r);
                    int screenHeight = moRlMain.getRootView().getHeight();

                    // r.bottom is the position above soft keypad or device button.
                    // if keypad is shown, the r.bottom is smaller than that before.
                    int keypadHeight = screenHeight - r.bottom;

                    Log.d("TAG", "keypadHeight = " + keypadHeight);

                    if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
                        Log.e("Keyboard", "onGlobalLayout: Open");
                        //moFlNativeAds.setVisibility(View.GONE);
                    } else {
                        Log.e("Keyboard", "onGlobalLayout: Close");
                        //moFlNativeAds.setVisibility(View.VISIBLE);
                    }
                });

        moIvMoreApp.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (Share.isNeedToAdShow(EditCredentialActivity.this)) {
                    if (IntAdsHelper.isInterstitialLoaded()) {
                        moIvMoreApp.setVisibility(View.GONE);
                        moIvBlast.setVisibility(View.VISIBLE);
                        ((AnimationDrawable) moIvBlast.getBackground()).start();
                        IntAdsHelper.showInterstitialAd();
//                    TODO Admob Ads
//                    interstitial.show();
                    }
                }
            }
        });

    }

    private void initView() {
        moEtTitleCredText = findViewById(R.id.et_titleCredText);
        moEtwebsiteCredText = findViewById(R.id.et_websiteCredText);
        moEtUsernameCredText = findViewById(R.id.et_usernameCredText);
        moEtPasswordCredText = findViewById(R.id.et_PasswordCredText);
        moEtNotesCredText = findViewById(R.id.et_NotesCredText);
        ll_save_creds = findViewById(R.id.ll_save_creds);
        btnClearCredentialsE = findViewById(R.id.btnClearCredentialsE);
        iv_back = findViewById(R.id.iv_back);
        moRlMain = findViewById(R.id.rl_main);
        //moFlNativeAds = findViewById(R.id.fl_adplaceholder);

        // Gift Ads
        moIvMoreApp = findViewById(R.id.iv_more_app);
        moIvBlast = findViewById(R.id.iv_blast);
        moIvMoreApp.setVisibility(View.GONE);
        moIvMoreApp.setBackgroundResource(R.drawable.animation_list_filling);
        ((AnimationDrawable) moIvMoreApp.getBackground()).start();

    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (Share.isNeedToAdShow(EditCredentialActivity.this)) {
//            Share.loadNative(EditCredentialActivity.this);
//        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_save_creds:

                mscredTitle = moEtTitleCredText.getText().toString().trim();

                if (TextUtils.isEmpty(mscredTitle)) {

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                    alertDialogBuilder.setMessage("Title field cannot be Empty");

                    alertDialogBuilder.setNegativeButton("OK", (dialog, which) -> dialog.dismiss());

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                } else {
                    addCredentialsInDatabase();
                }

                break;
            case R.id.btnClearCredentialsE:
//                private EditText moEtTitleCredText, moEtUsernameCredText;
//                private EditText moEtwebsiteCredText, moEtPasswordCredText;
//                private EditText moEtNotesCredText;
                moEtTitleCredText.setText("");
                moEtUsernameCredText.setText("");
                moEtwebsiteCredText.setText("");
                moEtPasswordCredText.setText("");
                moEtNotesCredText.setText("");
                break;
            case R.id.iv_back:
                onBackPressed();
                break;
        }
    }

    private void addCredentialsInDatabase() {


        mscredTitle = moEtTitleCredText.getText().toString();
        mscredWebsite = moEtwebsiteCredText.getText().toString();
        mscredUsername = moEtUsernameCredText.getText().toString();
        mscredPassword = moEtPasswordCredText.getText().toString();
        mscredNotes = moEtNotesCredText.getText().toString();

        CredentialsModel credentialsModel = new CredentialsModel();
        credentialsModel.setTitleText(mscredTitle);
        credentialsModel.setWebsite(mscredWebsite);
        credentialsModel.setUsername(mscredUsername);
        credentialsModel.setPassword(mscredPassword);
        credentialsModel.setNotes(mscredNotes);
        credentialsModel.setID(miID);

        if (isDecoy.equals("true")) {
            decoyDatabase.updateSingleCredential(credentialsModel);
        } else {
            imageVideoDatabase.updateSingleCredential(credentialsModel);
        }
        finish();

    }
}
