package com.rey.mvp;

public interface PresenterCache {

    int generatePresenterId();

    <P extends Presenter> P getPresenter(int id);

    void setPresenter(int id, Presenter presenter);
}
