package com.vanbios.beaconsranging.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.vanbios.beaconsranging.MainActivity;

/**
 * Created by Ihor Bilous on 29.12.2015.
 */
public abstract class CommonFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    protected void addFragment(CommonFragment f){
        ((MainActivity) getActivity()).addFragment(f);
    }

    protected void replaceFragment(CommonFragment f){
        ((MainActivity) getActivity()).replaceFragment(f);
    }

    protected void popAll(){
        getFragmentManager().popBackStackImmediate(0, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    protected void popFragment() {
        getFragmentManager().popBackStack();
        //((MainActivity) getActivity()).popFragment();
    }

    protected void popFragmentByTag(String tag) {
        ((MainActivity) getActivity()).popFragmentByTag(tag);
    }

    public abstract String getTitle();

}