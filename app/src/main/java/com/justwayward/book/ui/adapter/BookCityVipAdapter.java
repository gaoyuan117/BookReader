package com.justwayward.book.ui.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.justwayward.book.R;
import com.justwayward.book.view.MyGridView;

import java.util.List;

/**
 * Created by gaoyuan on 2017/11/20.
 */

public class BookCityVipAdapter extends BaseQuickAdapter<String,BaseViewHolder>{

    public BookCityVipAdapter(@LayoutRes int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        MyGridView gridView = helper.getView(R.id.grid_view2);

//        BookCityHomeItem2Adapter adapter = new BookCityHomeItem2Adapter(mContext, item.);
//        gridView.setAdapter(adapter);
    }
}
