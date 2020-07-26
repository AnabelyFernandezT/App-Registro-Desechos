package com.caircb.rcbtracegadere.processes;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class OffLineWorker extends Worker {

    private static final String TAG = "OffLineWorker";

    public OffLineWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        Log.e(TAG,"do work: work is done...");

        return Result.success();
    }
}
