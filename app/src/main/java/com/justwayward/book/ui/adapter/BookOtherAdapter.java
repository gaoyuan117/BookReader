package com.justwayward.book.ui.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.justwayward.book.R;
import com.justwayward.book.api.CoomonApi;
import com.justwayward.book.bean.BookCityBean;
import com.justwayward.book.ui.activity.BookDetailActivity;
import com.justwayward.book.view.MyGridView;

import java.util.List;

/**
 * Created by gaoyuan on 2017/11/19.
 */

public class BookOtherAdapter extends BaseQuickAdapter<BookCityBean, BaseViewHolder> {

    public BookOtherAdapter(@LayoutRes int layoutResId, @Nullable List<BookCityBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final BookCityBean item) {
        helper.setText(R.id.tv_type,item.getType());
        LinearLayout layout = helper.getView(R.id.ll_more);
        CoomonApi.check(mContext,layout, item.getType());

        MyGridView gridView = helper.getView(R.id.grid_view);
        gridView.setNumColumns(1);

        BookCityOtherItemAdapter adapter = new BookCityOtherItemAdapter(mContext, item.getList());

        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                BookDetailActivity.startActivity(mContext,item.getList().get(i).getId()+"");
            }
        });

    }
}
