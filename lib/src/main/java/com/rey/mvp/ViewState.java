package com.rey.mvp;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Rey on 11/16/2015.
 */
public interface ViewState {

    /**
     * Get an unique tag to save this ViewState's state to ViewStateCache.
     */
    String getTag();

    /**
     * Set this ViewState's tag.
     * @param tag
     */
    void setTag(String tag);

    /**
     * Called when restore the state of this ViewState.
     * @param data
     */
    void onRestore(@NonNull String data);

    /**
     * Called when save this ViewState's data.
     * @return
     */
    @Nullable
    String onSave();

    /**
     * Called when bind this ViewState to a Presenter. Note that a ViewState can be bound to many Presenters.
     * @param presenter
     */
    void onBind(Presenter presenter);

    /**
     * Called when unbind this ViewState from a Presenter.
     * @param presenter
     * @return This ViewState isn't bound with any Presenter or not. If true, we should remove this ViewState from cache.
     */
    boolean onUnbind(Presenter presenter);
}
