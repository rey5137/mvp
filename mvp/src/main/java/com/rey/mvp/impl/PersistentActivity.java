package com.rey.mvp.impl;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.rey.mvp.CacheFactory;
import com.rey.mvp.PresenterCache;
import com.rey.mvp.ViewStateCache;

/**
 * An Activity that implement CacheFactory
 */
public class PersistentActivity extends AppCompatActivity implements CacheFactory {

    private PersistentActivityDelegate mDelegate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mDelegate = new PersistentActivityDelegate();
        mDelegate.onCreate(savedInstanceState, getLastCustomNonConfigurationInstance());

        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mDelegate.onSaveInstanceState(outState);
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return mDelegate.getPersistentObject();
    }

    @Override
    public PresenterCache getPresenterCache() {
        return mDelegate;
    }

    @Override
    public ViewStateCache getViewStateCache() {
        return mDelegate;
    }
}
