/**
 * Copyright 2016 JustWayward Team
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.justwayward.book.ui.contract;

import android.content.Context;

import com.justwayward.book.base.BaseContract;
import com.justwayward.book.bean.BookBean;
import com.justwayward.book.bean.ChapterListBean;

import java.util.List;

/**
 * @author lfh.
 * @date 2016/8/7.
 */
public interface BookReadContract {

    interface View extends BaseContract.BaseView {
        void showBookToc(List<ChapterListBean> list);

//        void showChapterRead(ChapterRead.Chapter data, int chapter);
        void showChapterRead(BookBean data, int chapter);

        void netError(int chapter);//添加网络处理异常接口

        Context getContext();
    }

    interface Presenter<T> extends BaseContract.BasePresenter<T> {
        void getBookMixAToc(String bookId, String view);

        void getChapterRead(String chapterId,String sourceId);


    }

}
