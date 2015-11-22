package com.rey.mvp;

/**
 * Created by Rey on 11/16/2015.
 */
public interface ViewStateCache {

    <S extends ViewState> S getViewState(String tag);

    void addViewState(ViewState viewState);

    void saveViewStateData(ViewState viewState);

    void restoreViewStateData(ViewState viewState);

    void removeViewState(ViewState viewState);
}
