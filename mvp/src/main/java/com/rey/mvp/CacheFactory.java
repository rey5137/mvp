package com.rey.mvp;

/**
 * Created by Rey on 11/20/2015.
 */
public interface CacheFactory {

    PresenterCache getPresenterCache();

    ViewStateCache getViewStateCache();
}
