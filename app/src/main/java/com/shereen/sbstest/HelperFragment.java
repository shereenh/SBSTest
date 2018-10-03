package com.shereen.sbstest;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.shereen.sbstest.gson.TopLevel;
import com.shereen.sbstest.rest.RestClient;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class HelperFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TopLevel topLevel;
    TopLevel globalTopLevel;
    private Disposable disposable;


    private static final String TAG = HelperFragment.class.getSimpleName();


    private OnFragmentInteractionListener mListener;

    public HelperFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static HelperFragment newInstance(String param1, String param2) {
        HelperFragment fragment = new HelperFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        System.out.println("Fragment Created!");
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        TextView textView = new TextView(getActivity());
//        textView.setText(R.string.hello_blank_fragment);
//        return textView;
//    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onResultReceived(topLevel);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onResultReceived(TopLevel topLevel);
    }

    public void onRequestButtonClicked(){
        System.out.println("Request button clicked!");
        makeRestCall();
    }

    void makeRestCall(){

        disposable = RestClient.getInstance()
                .doSBSCall()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Consumer<TopLevel>() {
                            @Override
                            public void accept(TopLevel topLevel) throws Exception {
                                Log.i(TAG, "RxJava2: Response from server for toplevel ...");
                                globalTopLevel = topLevel;
                                if(topLevel==null){
                                    System.out.println("This object is NULL!!!");
                                }else{
                                    System.out.println("This object is not null...");
                                    System.out.println(topLevel.getStatus());
                                    if (mListener != null) {
                                        mListener.onResultReceived(topLevel);
                                    }
                                }




                            }
                        },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable t) throws Exception {
                                Log.i(TAG, "RxJava2, HTTP Error: " + t.getMessage());
                                Toast.makeText(getActivity().getApplicationContext(),
                                        getResources().getString(R.string.connection_error)
                                        , Toast.LENGTH_LONG).show();
                            }
                        }
                );

    }
}
