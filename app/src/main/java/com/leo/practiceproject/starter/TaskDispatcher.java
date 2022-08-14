package com.leo.practiceproject.starter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

public class TaskDispatcher {
    private Long startTime = 0L;

    private List<Future<?>> futuresList = new ArrayList<>();

    private List<Task> taskList = new ArrayList<>();

    private List<Task> mainThreadTask = new ArrayList<>();

    private CountDownLatch countDownLatch = null;

    private AtomicInteger needWaiteCount = new AtomicInteger();

    private List<Task> needWaitTask = new ArrayList<>();
}
