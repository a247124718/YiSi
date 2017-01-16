package com.yisi.picture.presenter;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

import com.yisi.picture.R;
import com.yisi.picture.activity.ImageOperateActivity;
import com.yisi.picture.adapter.ImageOperatePagerAdapter;
import com.yisi.picture.base.BasePresenterImpl;
import com.yisi.picture.bean.YiSiImage;
import com.yisi.picture.model.ImageOperaOperateModel;
import com.yisi.picture.model.inter.IImageOperateModel;
import com.yisi.picture.presenter.inter.IImageOperaPre;
import com.yisi.picture.utils.IntentKey;

import java.util.List;

import static com.yisi.picture.model.ImageOperaOperateModel.TYPE_ONLY_SHOW;

/**
 * Created by roy on 2017/2/11.
 */

public class ImageOperateOperaPreImpl extends BasePresenterImpl<ImageOperateActivity, IImageOperateModel> implements IImageOperaPre, ViewPager.OnPageChangeListener {
    private int type_id;
    private int open_type;

    private List<YiSiImage> mYiSiImages;
    private ImageOperatePagerAdapter adapter;

    public ImageOperateOperaPreImpl(ImageOperateActivity baseView) {
        super(baseView);
        type_id = getChildIntent().getIntExtra(IntentKey.KEY_PLANT_TYPE, 0);
        open_type = getChildIntent().getIntExtra(IntentKey.KEY_OPEN_TYPE, TYPE_ONLY_SHOW);

    }

    @Override
    protected IImageOperateModel setModel() {
        return new ImageOperaOperateModel(this);
    }

    @Override
    public Intent getChildIntent() {
        return mView.getIntent();
    }

    @Override
    public void getData() {
        mModel.getData(type_id);
    }

    @Override
    public void onSuccess(List<YiSiImage> yiSiImages, int position) {
        if (yiSiImages != null) {
            if (yiSiImages.size() == 0) {
                Toast.makeText(mView, R.string.no_plant, Toast.LENGTH_SHORT).show();
            }
            addChangeAlbum(yiSiImages);
            mYiSiImages = yiSiImages;
            if (yiSiImages.size() != 0) {
                if (adapter != null)
                    adapter = null;
                adapter = new ImageOperatePagerAdapter(yiSiImages);
                adapter.setOnPincherViewClickListener(new ImageOperatePagerAdapter.onPincherViewClickListener() {
                    @Override
                    public void onClick() {
                        mView.finish();
                    }
                });
                mView.updataTextView(position + "/" + (mYiSiImages.size() - 1));
                mView.setViewPagerAdapter(adapter);
                mView.getViewPager().setCurrentItem(position - 1);
                mView.getViewPager().clearOnPageChangeListeners();
                mView.getViewPager().addOnPageChangeListener(this);
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (open_type == 2) {
            if (position != mYiSiImages.size() - 1)
                mView.updataTextView(position + 1 + "/" + (mYiSiImages.size() - 1));
        } else
            mView.updataTextView(position + 1 + "/" + (mYiSiImages.size() - 1));

        //仅仅在最后一张，并且套图模式的情况下 才请求下一套图
        if (position == mYiSiImages.size() - 1 && open_type == 2) {
            type_id = type_id - 1;
            Toast.makeText(mView, R.string.next_plant, Toast.LENGTH_SHORT).show();
            getData();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    /**
     * 增加一张作为转换页面使用的
     *
     * @param yiSiImages
     */
    private void addChangeAlbum(List<YiSiImage> yiSiImages) {
        YiSiImage yiSiImage = new YiSiImage();
        yiSiImage.setImg_url("www");
        yiSiImages.add(yiSiImage);
    }
}
