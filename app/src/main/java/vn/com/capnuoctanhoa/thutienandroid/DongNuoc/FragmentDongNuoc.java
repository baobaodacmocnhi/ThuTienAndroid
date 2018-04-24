package vn.com.capnuoctanhoa.thutienandroid.DongNuoc;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import vn.com.capnuoctanhoa.thutienandroid.R;

public class FragmentDongNuoc extends Fragment {
    private View rootView;
private String MaDN;
private BottomNavigationView bottomNavigationView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_dong_nuoc, container, false);

        try {
            Bundle bundle = getArguments();
            if (bundle != null) {
                MaDN = bundle.getString("MaDN");
            }
        } catch (Exception ex) {
        }

        bottomNavigationView=(BottomNavigationView)rootView.findViewById(R.id.bottom_navigation);
bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.bottom_btnKiemTra:
                Toast.makeText(getActivity(), "kiểm tra", Toast.LENGTH_SHORT).show();
                break;
            case R.id.bottom_btnThem:
                Toast.makeText(getActivity(), "đóng nước", Toast.LENGTH_SHORT).show();
                break;
        };
        return true;
    }
});


        return  rootView;
    }


}
