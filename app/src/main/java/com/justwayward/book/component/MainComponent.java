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
package com.justwayward.book.component;

import com.justwayward.book.ui.activity.MainActivity;
import com.justwayward.book.ui.activity.SettingActivity;
import com.justwayward.book.ui.activity.WifiBookActivity;
import com.justwayward.book.ui.fragment.RecommendFragment;

import dagger.Component;

@Component(dependencies = AppComponent.class)
public interface MainComponent {
    MainActivity inject(MainActivity activity);

    RecommendFragment inject(RecommendFragment fragment);

    SettingActivity inject(SettingActivity activity);
    WifiBookActivity inject(WifiBookActivity activity);
}