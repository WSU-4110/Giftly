package com.example.giftly;

import java.util.ArrayList;
import java.util.List;

public class ThemeSubject {
    private List<ThemeObserver> observers = new ArrayList<>();

    public void registerObserver(ThemeObserver observer) {
        observers.add(observer);
    }

    public void unregisterObserver(ThemeObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers(int newColor) {
        for (ThemeObserver observer : observers) {
            observer.onThemeChanged(newColor);
        }
    }
}
