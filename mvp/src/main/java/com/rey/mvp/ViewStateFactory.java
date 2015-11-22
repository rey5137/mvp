package com.rey.mvp;

import android.support.annotation.NonNull;

/**
 * Created by Rey on 11/16/2015.
 */
public interface ViewStateFactory<S extends ViewState> {

    /**
     * Create a new instance of a ViewState
     *
     * @return The ViewState instance
     */
    @NonNull
    S createViewState();

    /**
     * Generate a unique tag for ViewState
     * @return
     */
    String getViewStateTag();
}
