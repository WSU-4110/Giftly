package com.example.giftly;

import android.app.Application;
import android.content.Context;

import com.example.giftly.handler.FireBaseClient;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Giftly extends Application {

    public static ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));
    //multithreading
    public static FireBaseClient client = new FireBaseClient();
}

/*todo
 * Implement edit gift feature
 * Implement join event feature
 *
 */