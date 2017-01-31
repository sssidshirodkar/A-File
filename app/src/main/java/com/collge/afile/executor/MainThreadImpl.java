/*
 * Copyright (C) 2014 Pedro Vicente Gómez Sánchez.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.collge.afile.executor;

import android.os.Handler;
import android.os.Looper;


/**
 * MainThread implementation based on a Handler instantiated over the main looper obtained from
 * Looper class.
 *
 * @author Saghayam nadar
 */
class MainThreadImpl implements MainThread {

    private Handler handler;

    MainThreadImpl() {
        this.handler = new Handler(Looper.getMainLooper());
    }

    public void post(Runnable runnable) {
        handler.post(runnable);
    }
}
