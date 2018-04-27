package vn.com.capnuoctanhoa.thutienandroid.HanhThu;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import vn.com.capnuoctanhoa.thutienandroid.CLocal;
import vn.com.capnuoctanhoa.thutienandroid.CViewAdapter;
import vn.com.capnuoctanhoa.thutienandroid.CViewEntity;
import vn.com.capnuoctanhoa.thutienandroid.R;

public class FragmentHanhThu extends Fragment {
    private View rootView;
    Spinner spnTimTheo;
    ListView lstView;
    String FileName;
    JSONArray jsonArray;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_hanh_thu, container, false);

        /*Bundle bundle = getArguments();
        if (bundle != null) {
            FileName = bundle.getString("FileName");
            try {
                jsonArray = new JSONArray(GetFile(FileName));
                LoadListView(jsonArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }*/

        return rootView;
    }


}
