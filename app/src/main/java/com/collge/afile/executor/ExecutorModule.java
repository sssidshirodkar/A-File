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


import com.collge.afile.interactor.UIInteractor;

/**
 * Module created to provide every dependency related with our execution service. Main
 * dependencies provided by this module are: ThreadExecutor and MainThreadImpl.
 *
 * @author Saghayam Nadar
 */

public final class ExecutorModule {

    private final static ExecutorModule module = new ExecutorModule();
    UIThreadExecutor executor;
    MainThreadImpl mainThread;

    private ExecutorModule() {

        executor = new UIThreadExecutor();
        mainThread = new MainThreadImpl();
    }

    public static ExecutorModule provideExecutor() {
        return module;
    }

    UIExecutor provideExecutor(UIThreadExecutor threadExecutor) {
        return threadExecutor;
    }

    MainThread provideMainThread(MainThreadImpl mainThread) {
        return mainThread;
    }

    public void runOnUiThread(Runnable runnable) {
        mainThread.post(runnable);
    }

    public void submitTask(UIInteractor interactor) {
        executor.run(interactor);
    }

    /**
     * task to executed in single thread
     *
     * @param interactor
     */

    public void submitSingleThreadTask(UIInteractor interactor) {
        executor.run(interactor);
    }

}
