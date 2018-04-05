package vn.com.capnuoctanhoa.thutienandroid.HanhThu;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import vn.com.capnuoctanhoa.thutienandroid.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HanhThu.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HanhThu#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HanhThu extends Fragment {
    private View rootView;
    Spinner spnTimTheo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_hanh_thu, container, false);

        String[] arraySpinner = new String[] { "Tất Cả", "Chưa Thu", "Đã Thu","Chuyển Khoản"};
        spnTimTheo = (Spinner) rootView.findViewById(R.id.spnTimTheo);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnTimTheo.setAdapter(adapter);

        return rootView;
    }


}
