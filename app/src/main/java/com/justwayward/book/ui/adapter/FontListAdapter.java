package com.justwayward.book.ui.adapter;

import android.os.Environment;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.justwayward.book.R;
import com.justwayward.book.bean.FontListBean;
import com.justwayward.book.ui.activity.FontListActivity;
import com.justwayward.book.utils.LogUtils;
import com.justwayward.book.utils.SharedPreferencesUtil;

import java.io.File;
import java.util.List;

/**
 * Created by gaoyuan on 2018/1/19.
 */

public class FontListAdapter extends BaseQuickAdapter<FontListBean, BaseViewHolder> {

    private int loadingPosition = -1;

    public FontListAdapter(int layoutResId, @Nullable List<FontListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FontListBean item) {

        String font = SharedPreferencesUtil.getInstance().getString("font");
        TextView tvM = helper.getView(R.id.tv_m);
        TextView tvTile = helper.getView(R.id.tv_title);
        TextView tvDownload = helper.getView(R.id.tv_down_load);
        TextView tvUse = helper.getView(R.id.tv_use);
        TextView tvLoading = helper.getView(R.id.tv_loading);
        ImageView imgFont = helper.getView(R.id.img_font);

        if (helper.getPosition() == 0) {
            if (TextUtils.isEmpty(font)) {
                tvUse.setVisibility(View.VISIBLE);
            } else {
                tvUse.setVisibility(View.GONE);
            }
            tvTile.setVisibility(View.VISIBLE);
            imgFont.setVisibility(View.GONE);
            tvM.setVisibility(View.GONE);
            tvDownload.setVisibility(View.GONE);

        } else {
            LogUtils.e("字体：" + font + "\n" + FontListActivity.getName(item.getPath()));

            tvTile.setVisibility(View.GONE);
            imgFont.setVisibility(View.VISIBLE);
            tvM.setVisibility(View.VISIBLE);
            setImg(item.getPic(), imgFont);
            tvM.setText(item.getStorage());

            if (getFile(item.getPath())) {
                tvDownload.setVisibility(View.GONE);
            } else {
                tvDownload.setVisibility(View.VISIBLE);
            }

            if (!TextUtils.isEmpty(font) && font.contains(FontListActivity.getName(item.getPath()))) {
                tvUse.setVisibility(View.VISIBLE);
            } else {
                tvUse.setVisibility(View.GONE);
            }
        }

        if (loadingPosition != -1 && helper.getPosition() == loadingPosition) {
            tvDownload.setVisibility(View.GONE);
            tvUse.setVisibility(View.GONE);
            tvLoading.setVisibility(View.VISIBLE);
        }


        helper.addOnClickListener(R.id.tv_down_load);
    }


    public void setDownloading(int position) {
        loadingPosition = position;
        notifyDataSetChanged();
    }


    public boolean getFile(String Path) { //搜索目录，扩展名，是否进入子文件夹
        File file = new File(Environment.getExternalStorageDirectory() + "/" + mContext.getPackageName());
        File[] files = file.listFiles();
        if (files == null || files.length == 0) {
            return false;
        }
        for (int i = 0; i < files.length; i++) {
            File f = files[i];
            LogUtils.e("文件名字：" + f.getName());
            if (f.isFile()) {
                if (FontListActivity.getName(Path).equals(f.getName())) { //判断扩展名

                    return true;
                }
            }
        }
        return false;
    }


    private void setImg(String pic, ImageView img) {
        if (pic.contains("songjianti")) {
            img.setImageDrawable(mContext.getDrawable(R.drawable.songjianti));
        } else if (pic.contains("fangzhenglantingxianhei")) {
            img.setImageDrawable(mContext.getDrawable(R.drawable.fangzhenglantingqianhei));
        } else if (pic.contains("heiti")) {
            img.setImageDrawable(mContext.getDrawable(R.drawable.heiti));
        } else if (pic.contains("huawenxingkai")) {
            img.setImageDrawable(mContext.getDrawable(R.drawable.huawenxingkai));
        } else if (pic.contains("lishu")) {
            img.setImageDrawable(mContext.getDrawable(R.drawable.lishu));
        } else if (pic.contains("weiruanyahei")) {
            img.setImageDrawable(mContext.getDrawable(R.drawable.weiruanyahei));
        }
    }

}
