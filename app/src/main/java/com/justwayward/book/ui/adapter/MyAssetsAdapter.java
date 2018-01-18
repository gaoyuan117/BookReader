package com.justwayward.book.ui.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.justwayward.book.R;
import com.justwayward.book.bean.RechargeListBean;
import com.justwayward.book.ui.activity.MyAssetsActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by gaoyuan on 2017/11/21.
 */

public class MyAssetsAdapter extends BaseAdapter {
    private Context context;
    private List<RechargeListBean> mList;
    private boolean isSelect = false;
    public List<LinearLayout> llList = new ArrayList<>();
    public List<TextView> tvMoneyList = new ArrayList<>();
    public List<TextView> tvJinDouList = new ArrayList<>();
    public List<TextView> tvAddJinDouList = new ArrayList<>();
    public String inputMoney;


    public MyAssetsAdapter(Context context, List<RechargeListBean> mList) {
        this.context = context;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder holder;

        if (view == null) {
            view = View.inflate(context, R.layout.item_my_pay_assets, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
            llList.add(holder.layout);
            tvAddJinDouList.add(holder.tvAddJindou);
            tvJinDouList.add(holder.tvJindou);
            tvMoneyList.add(holder.tvMoney);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        RechargeListBean bean = mList.get(i);


        if (bean.getAdd_time() == -1) {
            holder.ll_input.setVisibility(View.GONE);
            holder.layout.setVisibility(View.GONE);
            holder.tvOther.setVisibility(View.VISIBLE);

            holder.ll_input.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.et_input.requestFocus();
                    holder.et_input.setSelection(holder.et_input.getText().length());//将光标移至文字末尾
                    openKeybord(holder.et_input, context);
                }
            });
        } else {
            holder.tvOther.setVisibility(View.GONE);
            holder.layout.setVisibility(View.VISIBLE);
            holder.ll_input.setVisibility(View.GONE);

            holder.tvMoney.setText(bean.getMoney() + "元");
            holder.tvJindou.setText(bean.getCoin() + "金豆");
            holder.tvAddJindou.setText("+" + bean.getCoin() + "金豆");
        }

        if (isSelect) {
            if (bean.getAdd_time() == -1) {
                holder.tvOther.setVisibility(View.GONE);
                holder.ll_input.setVisibility(View.VISIBLE);
                holder.ll_input.setSelected(true);
                holder.et_input.setText("");
                inputMoney = "";
                holder.ll_input.performClick();
            }
        }


        holder.et_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    holder.tv_input_jindou.setText(0 + "金豆");
                    holder.tv_input_add_jindou.setText("+" + 0 + "金豆");
                    return;
                }

                Double d = Double.valueOf(s.toString());
                double jindou = d * MyAssetsActivity.rate;
                double add = d * MyAssetsActivity.give;
                holder.tv_input_jindou.setText(jindou + "金豆");
                holder.tv_input_add_jindou.setText("+" + add + "金豆");
                inputMoney = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                String temp = s.toString();
                int posDot = temp.indexOf(".");
                if (posDot <= 0) return;
                if (temp.length() - posDot - 1 > 2) {
                    s.delete(posDot + 3, posDot + 4);
                }
            }
        });

        return view;
    }

    /**
     * 设置选中状态
     *
     * @param position
     */
    public void setAllFalse(int position) {
        Log.e("gy", "position：" + (mList.get(position).getAdd_time()));

        for (int i = 0; i < llList.size(); i++) {
            if (i == position) {
                llList.get(i).setSelected(true);
            } else {
                llList.get(i).setSelected(false);
            }
        }

        for (int i = 0; i < tvJinDouList.size(); i++) {
            if (i == position) {
                tvJinDouList.get(i).setSelected(true);
            } else {
                tvJinDouList.get(i).setSelected(false);
            }
        }

        for (int i = 0; i < tvAddJinDouList.size(); i++) {
            if (i == position) {
                tvAddJinDouList.get(i).setSelected(true);
            } else {
                tvAddJinDouList.get(i).setSelected(false);
            }
        }

        for (int i = 0; i < tvMoneyList.size(); i++) {
            if (i == position) {
                tvMoneyList.get(i).setSelected(true);
            } else {
                tvMoneyList.get(i).setSelected(false);
            }
        }
    }


    /**
     * 设置是否输入的金额
     */
    public void setSelect(boolean select) {
        isSelect = select;
        notifyDataSetChanged();
    }


    static class ViewHolder {
        @Bind(R.id.tv_money)
        TextView tvMoney;
        @Bind(R.id.tv_jindou)
        TextView tvJindou;
        @Bind(R.id.tv_add_jindou)
        TextView tvAddJindou;
        @Bind(R.id.tv_other)
        TextView tvOther;
        @Bind(R.id.ll_assets)
        LinearLayout layout;
        @Bind(R.id.ll_input)
        LinearLayout ll_input;
        @Bind(R.id.tv_input_jindou)
        TextView tv_input_jindou;
        @Bind(R.id.tv_input_add_jindou)
        TextView tv_input_add_jindou;
        @Bind(R.id.et_input)
        EditText et_input;


        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public void openKeybord(EditText mEditText, Context mContext) {
        InputMethodManager imm = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY);
    }
}
