package com.vanbios.beaconsranging.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.vanbios.beaconsranging.R;
import com.vanbios.beaconsranging.simulate.TransmittingBeaconSimulator;
import com.vanbios.beaconsranging.util.ToastUtils;

/**
 * Created by Ihor Bilous on 29.12.2015.
 */
public class FrgTransmitting extends Fragment {
    private TransmittingBeaconSimulator transmittingBeaconSimulator;

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frg_transmitting, container, false);
        initViews();
        return view;
    }

    private void initViews() {
        TextView tvEnableTransmitting = (TextView) view.findViewById(R.id.tvFrgTransmittingEnableTransmitting);
        TextView tvDisableTransmitting = (TextView) view.findViewById(R.id.tvFrgTransmittingDisableTransmitting);
        final EditText etMajor = (EditText) view.findViewById(R.id.etFrgTransmittingMajor);
        final EditText etMinor = (EditText) view.findViewById(R.id.etFrgTransmittingMinor);

        tvEnableTransmitting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (transmittingBeaconSimulator == null)
                    transmittingBeaconSimulator = new TransmittingBeaconSimulator(getActivity());

                String strMaj = etMajor.getText().toString();
                String strMin = etMinor.getText().toString();
                if (validateValues(strMaj, strMin)) {
                    int minor = Integer.parseInt(strMin.length() >= 6 ? strMin.substring(0, 6) : strMin);
                    int major = Integer.parseInt(strMaj.length() >= 6 ? strMaj.substring(0, 6) : strMaj);

                    transmittingBeaconSimulator.setMajor(major);
                    transmittingBeaconSimulator.setMinor(minor);

                    boolean enable = transmittingBeaconSimulator.startTransmitting();
                    ToastUtils.showClosableToast(getActivity(),
                            enable ? "Эмуляция включена" : "Эмуляция не поддерживается на данном девайсе", 2);
                } else
                    ToastUtils.showClosableToast(getActivity(), getString(R.string.type_correct_major_minor), 2);
            }
        });

        tvDisableTransmitting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean disable = transmittingBeaconSimulator != null
                        && transmittingBeaconSimulator.stopTransmitting();
                ToastUtils.showClosableToast(getActivity(),
                        disable ? "Эмуляция выключена" : "Эмуляция еще не включена", 2);
            }
        });
    }

    private boolean validateValues(String strMaj, String strMin) {
        return (strMaj != null && strMin != null
                && strMaj.length() > 0 && strMin.length() > 0);
    }
}
