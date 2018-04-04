package vn.com.capnuoctanhoa.thutienandroid.HanhThu;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import vn.com.capnuoctanhoa.thutienandroid.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DanhSachHanhThu.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DanhSachHanhThu#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DanhSachHanhThu extends Fragment {
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_danh_sach_hanh_thu, container, false);
        return rootView;
    }

}
