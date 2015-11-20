package com.rey.mvp;

import android.support.annotation.NonNull;

public interface PresenterFactory<P extends Presenter> {
    /**
     * Create a new instance of a Presenter
     *
     * @return The Presenter instance
     */
    @NonNull
    P createPresenter();


}
