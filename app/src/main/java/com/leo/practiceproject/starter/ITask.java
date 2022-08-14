package com.leo.practiceproject.starter;

import java.util.List;
import java.util.concurrent.Executor;

public interface ITask {
    Runnable getTailRunnable();

    void setTaskCallBack(TaskCallBack callBack);

    boolean needCall();

    int priority();

    void run();

    Executor runOn();

    <T extends ITask> List<T> dependsOn();

    boolean needWait();

    boolean runOnMainThread();

    boolean onlyInMainProcess();
}
