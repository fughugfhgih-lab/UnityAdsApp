package com.example.unityadsapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.unity3d.ads.IUnityAdsInitializationListener;
import com.unity3d.ads.UnityAds;
import com.unity3d.services.banners.BannerErrorInfo;
import com.unity3d.services.banners.BannerView;
import com.unity3d.services.banners.UnityBannerSize;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity implements IUnityAdsInitializationListener {

    // Apna Unity Dashboard ka Game ID yaha daala gaya hai
    String unityGameID = "800003159";

    // testMode = true rakho jab tak testing kar rahe ho
    // Real device par live ads dekhne ke liye ise false karna, lekin
    // uske pehle Unity Dashboard par app properly setup + approved hona chahiye
    Boolean testMode = true;

    String topAdUnitId = "topBanner";
    String bottomAdUnitId = "bottomBanner";

    private BannerView.IListener bannerListener = new BannerView.IListener() {
        @Override
        public void onBannerLoaded(BannerView bannerAdView) {
            Log.v("UnityAdsExample", "onBannerLoaded: " + bannerAdView.getPlacementId());
            (bannerAdView.getPlacementId().equals("topBanner") ? hideTopBannerButton : hideBottomBannerButton).setEnabled(true);
        }

        @Override
        public void onBannerFailedToLoad(BannerView bannerAdView, BannerErrorInfo errorInfo) {
            Log.e("UnityAdsExample", "Unity Ads failed to load banner for " + bannerAdView.getPlacementId()
                    + " with error: [" + errorInfo.errorCode + "] " + errorInfo.errorMessage);
        }

        @Override
        public void onBannerClick(BannerView bannerAdView) {
            Log.v("UnityAdsExample", "onBannerClick: " + bannerAdView.getPlacementId());
        }

        @Override
        public void onBannerLeftApplication(BannerView bannerAdView) {
            Log.v("UnityAdsExample", "onBannerLeftApplication: " + bannerAdView.getPlacementId());
        }
    };

    BannerView topBanner;
    BannerView bottomBanner;
    RelativeLayout topBannerView;
    RelativeLayout bottomBannerView;
    Button showTopBannerButton;
    Button showBottomBannerButton;
    Button hideTopBannerButton;
    Button hideBottomBannerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Unity Ads initialize karo apne Game ID ke saath
        UnityAds.initialize(getApplicationContext(), unityGameID, testMode, this);

        showTopBannerButton = findViewById(R.id.loadTopBanner);
        showBottomBannerButton = findViewById(R.id.loadBottomBanner);
        hideTopBannerButton = findViewById(R.id.hideTopBanner);
        hideBottomBannerButton = findViewById(R.id.hideBottomBanner);

        showTopBannerButton.setEnabled(false);
        showBottomBannerButton.setEnabled(false);
        hideTopBannerButton.setEnabled(false);
        hideBottomBannerButton.setEnabled(false);

        showTopBannerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTopBannerButton.setEnabled(false);
                topBanner = new BannerView(view.getContext(), topAdUnitId, new UnityBannerSize(320, 50));
                topBanner.setListener(bannerListener);
                topBannerView = findViewById(R.id.topBanner);
                LoadBannerAd(topBanner, topBannerView);
            }
        });

        showBottomBannerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomBannerButton.setEnabled(false);
                bottomBanner = new BannerView(view.getContext(), bottomAdUnitId, new UnityBannerSize(320, 50));
                bottomBanner.setListener(bannerListener);
                bottomBannerView = findViewById(R.id.bottomBanner);
                LoadBannerAd(bottomBanner, bottomBannerView);
            }
        });

        hideTopBannerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                topBannerView.removeAllViews();
                topBannerView = null;
                topBanner = null;
                showTopBannerButton.setEnabled(true);
            }
        });

        hideBottomBannerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomBannerView.removeAllViews();
                bottomBannerView = null;
                bottomBanner = null;
                showBottomBannerButton.setEnabled(true);
            }
        });
    }

    public void LoadBannerAd(BannerView bannerView, RelativeLayout bannerLayout) {
        bannerView.load();
        bannerLayout.addView(bannerView);
    }

    @Override
    public void onInitializationComplete() {
        showTopBannerButton.setEnabled(true);
        showBottomBannerButton.setEnabled(true);
    }

    @Override
    public void onInitializationFailed(UnityAds.UnityAdsInitializationError error, String message) {
        Log.e("UnityAdsExample", "Unity Ads init failed: " + error + " - " + message);
    }
}
