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

import vn.com.capnuoctanhoa.thutienandroid.CLocal;
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
    Spinner spnNam,spnKy,spnFromDot,spnToDot;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_danh_sach_hanh_thu, container, false);

        spnNam = (Spinner) rootView.findViewById(R.id.spnNam);
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(getActivity(), android.R.layout.simple_spinner_item, CLocal.arrayspnNam);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnNam.setAdapter(adapter);

        spnKy = (Spinner) rootView.findViewById(R.id.spnKy);
         adapter = new ArrayAdapter<Integer>(getActivity(), android.R.layout.simple_spinner_item, CLocal.arrayspnKy);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnKy.setAdapter(adapter);

        spnFromDot = (Spinner) rootView.findViewById(R.id.spnFromDot);
         adapter = new ArrayAdapter<Integer>(getActivity(), android.R.layout.simple_spinner_item, CLocal.arrayspnDot);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnFromDot.setAdapter(adapter);

        spnToDot = (Spinner) rootView.findViewById(R.id.spnToDot);
         adapter = new ArrayAdapter<Integer>(getActivity(), android.R.layout.simple_spinner_item, CLocal.arrayspnDot);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnToDot.setAdapter(adapter);

        return rootView;
    }

}
