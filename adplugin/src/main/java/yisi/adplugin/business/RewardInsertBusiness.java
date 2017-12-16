package yisi.adplugin.business;


import yisi.adplugin.place.AdMobInterstitialAd;
import yisi.adplugin.place.BatFullInterstitialAd;
import yisi.adplugin.place.BatNativeInterstitialAd;
import yisi.adplugin.bean.Ad;
import yisi.adplugin.bean.BaseAd;

/**
 * Created by chenql on 2017/6/5.
 */

public class RewardInsertBusiness extends BaseAdBusiness {

    private static RewardInsertBusiness ourInstance = new RewardInsertBusiness();

    public static RewardInsertBusiness getInstance() {
        return ourInstance;
    }

    @Override
    protected BaseAd getAdPlanment() {
        return mAdconfig.getRewardAd();
    }

    @Override
    public void bindAdPlatform(Ad ad) {
        switch (ad.getProvider()) {
            case PLATFORM_ADMOB:
                mBaseAdPlace = new AdMobInterstitialAd();
                break;

            case PLATFORM_FACEBOOK:
                break;

            case PLATFORM_FB_NATIVE:

                break;
            case PLATFORM_BAT:
                mBaseAdPlace = new BatNativeInterstitialAd();
                break;

            case PLATFORM_BAT_FULL:
                mBaseAdPlace = new BatFullInterstitialAd();
                break;

            case PLATFORM_LUOMI:
                break;

            default:
                break;
        }
    }

    @Override
    public String getPlanmentString() {
        return "Reward Ad";
    }

    @Override
    public void onAdFailed(String message) {

        super.onAdFailed(message);
    }
}