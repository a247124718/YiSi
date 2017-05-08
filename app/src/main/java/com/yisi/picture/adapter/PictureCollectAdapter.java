package com.yisi.picture.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.yisi.picture.R;
import com.yisi.picture.adapter.base.BaseAdapter;
import com.yisi.picture.adapter.viewholder.PictureCollectViewHolder;
import com.yisi.picture.application.YiSiApplication;
import com.yisi.picture.bean.YiSiImage;
import com.yisi.picture.utils.GlideUtils;

import java.util.List;

/**
 * Created by chenql on 2017/5/4.
 */

public class PictureCollectAdapter extends BaseAdapter<PictureCollectViewHolder, YiSiImage> {
    public PictureCollectAdapter(List<YiSiImage> dataList) {
        super(dataList);
    }

    @Override
    public PictureCollectViewHolder holder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_picture_collect, viewGroup, false);
        return new PictureCollectViewHolder(view, onItemClickListener);
    }

    @Override
    protected void bindHolder(PictureCollectViewHolder holder, int position) {
        if (position % 2 != 0) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.getImageView().getLayoutParams();
            layoutParams.leftMargin = YiSiApplication.mGlobleContext.getResources().getDimensionPixelOffset(R.dimen.px1);
            holder.getImageView().setLayoutParams(layoutParams);
        }
        GlideUtils.displayImage(mDataList.get(position).getImg_url(), holder.getImageView());
    }
}