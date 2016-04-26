package com.vanbios.beaconsranging.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.vanbios.beaconsranging.R;
import com.vanbios.beaconsranging.objects.IBeaconBase;
import com.vanbios.beaconsranging.singleton.InfoSingleton;
import com.vanbios.beaconsranging.util.ToastUtils;

/**
 * Created by Ihor Bilous on 29.12.2015.
 */
public class FrgSearch extends Fragment {

    private View view;
    private TextView tvResult;
    private RadioButton rbEntry;
    private EditText etMajor, etMinor;
    public static final int ENTRY = 1, ADVERTISING = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frg_search, container, false);
        initViews();
        return view;
    }

    private void initViews() {
        etMajor = (EditText) view.findViewById(R.id.etFrgSearchMajor);
        etMinor = (EditText) view.findViewById(R.id.etFrgSearchMinor);
        rbEntry = (RadioButton) view.findViewById(R.id.rbFrgSearchEntry);
        tvResult = (TextView) view.findViewById(R.id.tvFrgSearchResult);
        TextView tvBeacon = (TextView) view.findViewById(R.id.tvFrgSearchBeacon);
        TextView tvStore = (TextView) view.findViewById(R.id.tvFrgSearchStore);
        tvBeacon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchBeacon(1);
            }
        });
        tvStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchBeacon(2);
            }
        });
    }

    private void searchBeacon(int mode) {
        long startTime = System.currentTimeMillis();
        String strMaj = etMajor.getText().toString();
        String strMin = etMinor.getText().toString();
        if (validateValues(strMaj, strMin)) {
            int minor = Integer.parseInt(strMin.length() >= 6 ? strMin.substring(0, 6) : strMin);
            int major = Integer.parseInt(strMaj.length() >= 6 ? strMaj.substring(0, 6) : strMaj);

            IBeaconBase beacon = InfoSingleton.getInstance().findBeaconByMajorMinor(
                    rbEntry.isChecked() ? ENTRY : ADVERTISING, major, minor);

            if (beacon != null) {
                long searchTime = System.currentTimeMillis() - startTime;
                switch (mode) {
                    case 1: {
                        tvResult.setText(
                                String.format(
                                        getString(R.string.placeholder_beacon_search_info),
                                        beacon.getIdBeacon(),
                                        beacon.getMajor(),
                                        beacon.getMinor(),
                                        searchTime)
                        );
                        break;
                    }
                    case 2: {
                        tvResult.setText(
                                String.format(
                                        getString(R.string.placeholder_store_search_info),
                                        beacon.getIdStore(),
                                        beacon.getStoreName(),
                                        beacon.getIdTradeCenter(),
                                        beacon.getTradeCenterName(),
                                        beacon.getFloor(),
                                        searchTime)
                        );
                        break;
                    }
                }
            } else
                tvResult.setText(getString(R.string.no_search_results));
        } else
            ToastUtils.showClosableToast(getActivity(), getString(R.string.type_correct_major_minor), 2);
    }

    private boolean validateValues(String strMaj, String strMin) {
        return (strMaj != null && strMin != null
                && strMaj.length() > 0 && strMin.length() > 0);
    }
}
