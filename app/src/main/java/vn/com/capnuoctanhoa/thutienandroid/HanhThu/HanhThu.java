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

public class HanhThu extends Fragment {
    private View rootView;
    Spinner spnTimTheo;
    ListView lstView;
    String FileName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_hanh_thu, container, false);

        lstView=(ListView) rootView.findViewById(R.id.lstView);

        Bundle bundle = getArguments();
        if (bundle != null) {
            FileName = bundle.getString("FileName");
            LoadListView(FileName);
        }

        String[] arraySpinner = new String[]{"Tất Cả", "Chưa Thu", "Đã Thu", "Chuyển Khoản"};
        spnTimTheo = (Spinner) rootView.findViewById(R.id.spnTimTheo);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnTimTheo.setAdapter(adapter);



        return rootView;
    }

    private void LoadListView(String fileName) {
        try {
            ArrayList<CViewEntity> list = new ArrayList<CViewEntity>();
            JSONArray jsonArray = new JSONArray(GetFile(fileName));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                CViewEntity entity = new CViewEntity();

                entity.setID(jsonObject.getString("SOHOADON"));
                entity.setName1(jsonObject.getString("DANHBA"));
                entity.setName2(jsonObject.getString("TONGCONG"));
                entity.setContent(jsonObject.getString("SO") + " " + jsonObject.getString("DUONG"));

                list.add(entity);
            }

            CViewAdapter adapter = new CViewAdapter(getActivity(), list);
            lstView.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String GetFile(String fileName) {
        try {
            FileInputStream inputStream = getContext().openFileInput(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String data = "";
            StringBuilder builder = new StringBuilder();
            while ((data = reader.readLine()) != null) {
                builder.append(data);
                builder.append("\n");
            }
            inputStream.close();
            return builder.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }


}
