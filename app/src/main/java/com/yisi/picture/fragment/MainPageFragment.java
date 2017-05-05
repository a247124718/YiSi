package com.yisi.picture.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.flyco.tablayout.SlidingTabLayout;
import com.yisi.picture.R;
import com.yisi.picture.activity.ImageOperateActivity;
import com.yisi.picture.activity.MainActivity;
import com.yisi.picture.adapter.MainContentPagerAdapter;
import com.yisi.picture.application.YiSiApplication;
import com.yisi.picture.base.BaseFragment;
import com.yisi.picture.bean.PlantBrowse;
import com.yisi.picture.fragment.inter.IMainFragment;
import com.yisi.picture.model.ImageOperaOperateModel;
import com.yisi.picture.presenter.MainFragmentPreImpl;
import com.yisi.picture.presenter.inter.IMainFragmentPre;
import com.yisi.picture.utils.IntentKey;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by roy on 2017/1/14.
 */

public class MainPageFragment extends BaseFragment implements BaseSliderView.OnSliderClickListener, IMainFragment {

    private SliderLayout mSliderLayout;
    private SlidingTabLayout mSlidingTabLayout;
    private IMainFragmentPre mainFragmentPre;
    private ViewPager mViewPager;
    private ProgressBar progressBar;
    private AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;
    private View mEveryDayView;
    private boolean mHasPictureMode = false;


    public boolean isHasPictureMode() {
        return mHasPictureMode;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainFragmentPre = new MainFragmentPreImpl(this);
    }

    @Override
    protected void initViews() {
        mSliderLayout = findview(R.id.main_fragment_slider);
        mSlidingTabLayout = findview(R.id.main_fragment_slidertab);
        mViewPager = findview(R.id.main_fragment_vp_content);
        progressBar = findview(R.id.main_fragment_process);
        mAppBarLayout = findview(R.id.main_appbar);
        mEveryDayView = findview(R.id.ll_everyday);
        mToolbar = findview(R.id.mian_fragment_toolbar);
        mToolbar.setTitle("爱Yisi");
        mToolbar.setTitleTextColor(0xffffffff);
        mToolbar.setNavigationIcon(R.mipmap.category);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).openDrawer();
            }
        });
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(final AppBarLayout appBarLayout, int verticalOffset) {
                if (getResources().getDimensionPixelOffset(R.dimen.px850) == Math.abs(verticalOffset)) {
                    YiSiApplication.postDelay(new Runnable() {
                        @Override
                        public void run() {
                            appBarLayout.removeView(mSliderLayout);
                            appBarLayout.removeView(mToolbar);
                            appBarLayout.removeView(mEveryDayView);
                        }
                    },500);
                    ((MainActivity) getActivity()).setmCommonTabLayoutVisible(View.GONE);
                    refreshChildRefreshState(true);
                }
            }
        });
    }

    private void refreshChildRefreshState(boolean refresh) {
        mHasPictureMode = refresh;
        List<Fragment> fragments = getChildFragmentManager().getFragments();
        for (Fragment fragment : fragments) {
            ((MainPageChildFragment) fragment).setPtr(refresh);
            ((MainPageChildFragment) fragment).refreshState();
        }
    }

    @Override
    protected int getContentResouce() {
        return R.layout.fragment_main;
    }

    @Override
    protected void initData() {
        mainFragmentPre.requestBannerAndShow();
        mainFragmentPre.requestContentAndShow();
    }


    @Override
    public void onSliderClick(BaseSliderView slider) {
        TextSliderView textSliderView = (TextSliderView) slider;
        Intent intent = new Intent(getContext(), ImageOperateActivity.class);
        intent.putExtra(IntentKey.KEY_PLANT_TYPE, textSliderView.getPlant_id());
        intent.putExtra(IntentKey.KEY_OPEN_TYPE, ImageOperaOperateModel.TYPE_REQUEST_SHOW);
        startActivity(intent);
    }

    @Override
    public void initSliderBannerData(List<PlantBrowse> mainSliderBanners) {
        for (int i = 0; i < mainSliderBanners.size(); i++) {
            TextSliderView textSliderView = new TextSliderView(getContext());
            textSliderView.plant_id(mainSliderBanners.get(i).getPlant_id())
                    .description(mainSliderBanners.get(i).getTitle())
                    .image(mainSliderBanners.get(i).getImg_url())
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);
            mSliderLayout.addSlider(textSliderView);
        }

//        mSliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Right_Bottom);
//        mSliderLayout.setDuration(4000);
    }

    @Override
    public void initMainPageChildPage(String[] titles, ArrayList<Fragment> fragments) {
        MainContentPagerAdapter adapter = new MainContentPagerAdapter(getChildFragmentManager(), fragments);
        mViewPager.setAdapter(adapter);
        mSlidingTabLayout.setViewPager(mViewPager, titles);
        onLoadingSuccess();
    }

    @Override
    public void onLoadingPage() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoadingSuccess() {
        mSlidingTabLayout.setVisibility(View.VISIBLE);
        mSliderLayout.setVisibility(View.VISIBLE);
        mViewPager.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onLoadingFail() {

    }

    public boolean onBackPressed() {
        //如果是View不可见的情况下，则back
        if (((MainActivity) getActivity()).getmCommonTabLayoutVisiablity() == View.GONE) {
            ((MainActivity) getActivity()).setmCommonTabLayoutVisible(View.VISIBLE);
            mAppBarLayout.addView(mSliderLayout, 0);//放在最上面
            mAppBarLayout.addView(mEveryDayView, 0);
            mAppBarLayout.addView(mToolbar, 0);
            mainFragmentPre.onRecoverState(mSlidingTabLayout.getCurrentTab());
            refreshChildRefreshState(false);
            return true;
        } else {
            return false;
        }
    }
}
