package vn.com.capnuoctanhoa.thutienandroid;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.json.JSONArray;

public class CLocal {
    public static SharedPreferences sharedPreferencesre;
    public static String Path = "/data/data/vn.com.capnuoctanhoa.thutienandroid/files";
    public static String FileName = "my_data";
    public static Integer[] arrayspnNam = new Integer[]{2018, 2019, 2020};
    public static Integer[] arrayspnKy = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
    public static Integer[] arrayspnDot = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20};
    public static int Color_ChuaThu = Color.TRANSPARENT;
    public static int Color_DaThu = Color.GREEN;
    public static int Color_ChuyenKhoan = Color.RED;
    public static JSONArray jsonArray_HanhThu, jsonArray_DongNuoc;

    public static boolean CheckNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    /*private void LoadListView() {
        try {
            File directory = new File(CLocal.Path);
            if (directory.length() > 0) {
                File[] files = directory.listFiles();
                ArrayList<String> array = new ArrayList<>();
                for (int i = 0; i < files.length; i++) {
                    array.add(files[i].getName());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, array);
                lstView.setAdapter(adapter);
            }
        } catch (Exception e) {
        }
    }*/

        /*private String GetFile(String fileName) {
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
        } catch (Exception e) {
        }
        return "";
    }*/


}
