package com.example.giftly;

import android.app.Application;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Giftly extends Application {
    ExecutorService executorService = Executors.newFixedThreadPool(4);
}
