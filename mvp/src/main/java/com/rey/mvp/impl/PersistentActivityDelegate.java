package com.rey.mvp.impl;

import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v4.util.SimpleArrayMap;
import android.support.v4.util.SparseArrayCompat;

import com.rey.mvp.Presenter;
import com.rey.mvp.PresenterCache;
import com.rey.mvp.ViewState;
import com.rey.mvp.ViewStateCache;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Rey on 11/20/2015.
 */
public class PersistentActivityDelegate implements PresenterCache, ViewStateCache {

    private static final String KEY_NEXT_PRESENTER_ID = "com.rey.mvp.next_presenter_id";
    private static final String KEY_VIEW_STATE_TAG = "com.rey.mvp.view_state_tag";
    private static final String KEY_VIEW_STATE_DATA = "com.rey.mvp.view_state_data";

    private PersistentInstance mPersistentInstance;

    public void onCreate(Bundle savedInstanceState, Object persistentObj) {
        mPersistentInstance = (PersistentInstance) persistentObj;
        if (mPersistentInstance == null) {
            int seed = (savedInstanceState == null) ? 0 : savedInstanceState.getInt(KEY_NEXT_PRESENTER_ID);
            mPersistentInstance = new PersistentInstance(seed);

            if(savedInstanceState != null){
                try {
                    String[] tags = savedInstanceState.getStringArray(KEY_VIEW_STATE_TAG);
                    String[] data = savedInstanceState.getStringArray(KEY_VIEW_STATE_DATA);

                    if(tags != null)
                        for(int i = 0; i < tags.length; i++)
                            mPersistentInstance.mViewStatesData.put(tags[i], data[i]);
                } catch (Exception e) {
                }
            }
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(KEY_NEXT_PRESENTER_ID, mPersistentInstance.mNextPresenterId.get());

        Set<String> set = mPersistentInstance.mViewStatesData.keySet();
        String[] tags = new String[set.size()];
        String[] data = new String[tags.length];

        Iterator<String> iter = set.iterator();
        int index = 0;
        while(iter.hasNext()){
            tags[index] = iter.next();
            data[index] = mPersistentInstance.mViewStatesData.get(tags[index]);
            index++;
        }

        outState.putStringArray(KEY_VIEW_STATE_TAG, tags);
        outState.putStringArray(KEY_VIEW_STATE_DATA, data);
    }

    public Object getPersistentObject(){
        return mPersistentInstance;
    }

    @Override
    public int generatePresenterId() {
        return mPersistentInstance.mNextPresenterId.getAndIncrement();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <P extends Presenter> P getPresenter(int id) {
        P p;
        try {
            p = (P) mPersistentInstance.mPresenters.get(id);
        } catch (ClassCastException e) {
            throw new RuntimeException(e);
        }
        return p;
    }

    @Override
    public void setPresenter(int id, Presenter presenter) {
        if(presenter == null)
            mPersistentInstance.mPresenters.remove(id);
        else
            mPersistentInstance.mPresenters.put(id, presenter);
    }

    @Override
    public <S extends ViewState> S getViewState(String tag) {
        ViewState temp = mPersistentInstance.mViewStates.get(tag);
        if(temp == null)
            return null;

        S s;
        try {
            s = (S)temp;
        } catch (ClassCastException e) {
            throw new RuntimeException(e);
        }
        return s;
    }

    @Override
    public void addViewState(ViewState viewState) {
        mPersistentInstance.mViewStates.put(viewState.getTag(), viewState);
    }

    @Override
    public void saveViewStateData(ViewState viewState) {
        String state = viewState.onSave();
        if(state != null)
            mPersistentInstance.mViewStatesData.put(viewState.getTag(), state);
    }

    @Override
    public void restoreViewStateData(ViewState viewState) {
        String state = mPersistentInstance.mViewStatesData.get(viewState.getTag());
        if(state != null)
            viewState.onRestore(state);
    }

    @Override
    public void removeViewState(ViewState viewState) {
        String tag = viewState.getTag();
        mPersistentInstance.mViewStates.remove(tag);
        mPersistentInstance.mViewStatesData.remove(tag);
    }

    private static class PersistentInstance {
        private SparseArrayCompat<Presenter> mPresenters;
        private AtomicInteger mNextPresenterId;

        private SimpleArrayMap<String, ViewState> mViewStates;
        private ArrayMap<String, String> mViewStatesData;

        public PersistentInstance(int seed) {
            mPresenters = new SparseArrayCompat<>();
            mNextPresenterId = new AtomicInteger(seed);

            mViewStates = new SimpleArrayMap<>();
            mViewStatesData = new ArrayMap<>();
        }
    }
}
