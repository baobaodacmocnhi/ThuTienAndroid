package vn.com.capnuoctanhoa.thutienandroid.Class;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class CLocal {
    //Android 1.5 	Cupcake 	27/4/2009
    //Android 1.6	Donut 	15/9/2009
    //Android 2.0 - 2.1 	Eclair 	26/9/2009 (phát hành lần đầu)
    //Android 2.2 - 2.2.3 	Froyo 	20/5/2010 (phát hành lần đầu)
    //Android 2.3 - 2.3.7 	Gingerbread 	6/12/2010 (phát hành lần đầu)
    //Android 3.0 - 3.2.6 	Honeycomb 	22/2/2011 (phát hành lần đầu)
    //Android 4.0 - 4.0.4 	Ice Cream Sandwich 	18/10/2011 (phát hành lần đầu)
    //Android 4.1 - 4.3.1 	Jelly Bean 	9/7/2012 (phát hành lần đầu)
    //Android 4.4 - 4.4.4 	KitKat 	31/10/2013 (phát hành lần đầu)
    //Android 5.0 - 5.1.1 	Lollipop 	12/11/2014 (phát hành lần đầu)
    //Android 6.0 - 6.0.1 	Marshmallow 	5/10/2015 (phát hành lần đầu)
    //Android 7.0 - 7.1.2 	Nougat 	22/8/2016 (phát hành lần đầu)
    //Android 8.0 - 8.1 	Oreo 	21/8/2017 (phát hành lần đầu)

    public static SharedPreferences sharedPreferencesre;
    public static String Path = "/data/data/vn.com.capnuoctanhoa.thutienandroid/files";
    public static String pathRoot = Environment.getExternalStorageDirectory() + "/TanHoa/";
    public static String pathFile = pathRoot + "/File/";
    public static String pathPicture = pathRoot + "/Picture/";
    public static String fileName_SharedPreferences = "my_configuration";
    public static SimpleDateFormat DateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    public static JSONArray jsonHanhThu, jsonDongNuoc, jsonDongNuocChild, jsonMessage, jsonTo, jsonNhanVien;
    public static String MaNV = "", HoTen = "", MaTo = "", DienThoai = "", ThermalPrinter = "";
    public static boolean Doi = false, ToTruong = false;
    public static ArrayList<CEntityParent> listHanhThu, listHanhThuView, listDongNuoc;

    public static void initialCLocal() {
        SharedPreferences.Editor editor = CLocal.sharedPreferencesre.edit();
        editor.putString("Username", "");
        editor.putString("Password", "");
        editor.putString("MaNV", "");
        editor.putString("HoTen", "");
        editor.putString("MaTo", "");
        editor.putString("DienThoai", "");
        editor.putString("jsonHanhThu", "");
        editor.putString("jsonDongNuoc", "");
        editor.putString("jsonMessage", "");
        editor.putString("jsonTo", "");
        editor.putString("jsonNhanVien", "");
        editor.putBoolean("Doi", false);
        editor.putBoolean("ToTruong", false);
        editor.putBoolean("Login", false);
        editor.putString("ThermalPrinter", "");
        editor.commit();
        editor.remove("jsonHanhThu_HoaDonDienTu").commit();
        editor.remove("jsonDongNuocChild").commit();
        MaNV = HoTen = MaTo = DienThoai = "";
        Doi = ToTruong = false;
        jsonHanhThu = jsonDongNuoc = jsonDongNuocChild = jsonMessage = jsonTo = jsonNhanVien = null;
        listHanhThu = listHanhThuView = listDongNuoc = null;
    }

    public static boolean checkNetworkAvailable(Activity activity) {
        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    public static boolean checkBluetoothAvaible() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter != null)
            return bluetoothAdapter.isEnabled();
        else
            return false;
    }

    public static void openBluetoothSettings(Activity activity) {
        Intent intent = new Intent();
        intent.setAction(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS);
        activity.startActivity(intent);
    }

    public static void setOnBluetooth(Activity activity) {
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        activity.startActivityForResult(intent, 1);
    }

    public static void showPopupMessage(Activity activity, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Thông Báo");
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public static void showToastMessage(Activity activity, String message) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    //update list to Json
    public static void updateArrayListToJson() {
        SharedPreferences.Editor editor = CLocal.sharedPreferencesre.edit();
        if (CLocal.listHanhThu != null)
            editor.putString("jsonHanhThu", new Gson().toJsonTree(CLocal.listHanhThu).getAsJsonArray().toString());
        if (CLocal.listDongNuoc != null) {
            editor.putString("jsonDongNuoc", new Gson().toJsonTree(CLocal.listDongNuoc).getAsJsonArray().toString());
        }
        editor.commit();
    }

    public static void updateJSON(JSONArray jsonArray, String ID, String Key, String Value) {
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.getString("ID").equals(ID)) {
                    jsonObject.put(Key, Value);
                    //thiết lập ModifyDate để sort
                    jsonObject.put("ModifyDate", new Date().toString());
                    break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //update giá trí child
    public static void updateValueChild(ArrayList<CEntityParent> lst, String NameUpdate, String ValueUpdate, String MaHD) {
        try {
            for (int i = 0; i < lst.size(); i++) {
                for (int j = 0; j < lst.get(i).getLstHoaDon().size(); j++)
                    if (lst.get(i).getLstHoaDon().get(j).getMaHD().equals(MaHD) == true) {
                        //update child
                        switch (NameUpdate) {
                            case "GiaiTrach":
                                lst.get(i).getLstHoaDon().get(j).setGiaiTrach(Boolean.parseBoolean(ValueUpdate));
                                break;
                            case "TamThu":
                                lst.get(i).getLstHoaDon().get(j).setTamThu(Boolean.parseBoolean(ValueUpdate));
                                break;
                            case "ThuHo":
                                lst.get(i).getLstHoaDon().get(j).setThuHo(Boolean.parseBoolean(ValueUpdate));
                                break;
                            case "PhiMoNuocThuHo":
                                lst.get(i).getLstHoaDon().get(j).setPhiMoNuocThuHo(ValueUpdate);
                                break;
                            case "DaThu":
                                lst.get(i).getLstHoaDon().get(j).setDangNgan_DienThoai(Boolean.parseBoolean(ValueUpdate));
                                break;
                            case "InPhieuBao":
                                lst.get(i).getLstHoaDon().get(j).setInPhieuBao_Ngay(ValueUpdate);
                                break;
                        }
                        //gọi update lại parent
                        updateTinhTrangParent(lst, i);
                    }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //update tình trạng parent
    public static void updateTinhTrangParent(ArrayList<CEntityParent> lst, int i) {
        //update TinhTrang
        int ThuHo = 0, TamThu = 0, GiaiTrach = 0, DangNgan_DienThoai = 0, TBDongNuoc = 0, LenhHuy = 0, PhiMoNuocThuHo = 0;
        for (CEntityChild item : lst.get(i).getLstHoaDon()) {
            if (item.isGiaiTrach() == true)
                GiaiTrach++;
            else if (item.isTamThu() == true)
                TamThu++;
            else if (item.isThuHo() == true) {
                ThuHo++;
                if (Integer.parseInt(item.getPhiMoNuocThuHo()) > 0)
                    PhiMoNuocThuHo++;
            } else if (item.isLenhHuy() == true)
                LenhHuy++;
            else if (item.isTBDongNuoc() == true)
                TBDongNuoc++;

            if (item.isDangNgan_DienThoai() == true)
                DangNgan_DienThoai++;
        }

        lst.get(i).setGiaiTrach(false);
        lst.get(i).setTamThu(false);
        lst.get(i).setThuHo(false);
        lst.get(i).setLenhHuy(false);
        lst.get(i).setTBDongNuoc(false);
        lst.get(i).setDangNgan_DienThoai(false);
        lst.get(i).setTinhTrang("");

        if (GiaiTrach == lst.get(i).getLstHoaDon().size()) {
            lst.get(i).setGiaiTrach(true);
            lst.get(i).setTinhTrang("Giải Trách");
        } else if (TamThu == lst.get(i).getLstHoaDon().size()) {
            lst.get(i).setTamThu(true);
            lst.get(i).setTinhTrang("Tạm Thu");
        } else if (ThuHo == lst.get(i).getLstHoaDon().size()) {
            lst.get(i).setThuHo(true);
            String str = "Thu Hộ";
            if (PhiMoNuocThuHo == lst.get(i).getLstHoaDon().size())
                str += " (" + lst.get(i).getLstHoaDon().get(0).getPhiMoNuocThuHo().substring(0, lst.get(i).getLstHoaDon().get(0).getPhiMoNuocThuHo().length() - 3) + "k)";
            lst.get(i).setTinhTrang(str);
        } else if (LenhHuy == lst.get(i).getLstHoaDon().size()) {
            lst.get(i).setLenhHuy(true);
        } else if (TBDongNuoc == lst.get(i).getLstHoaDon().size()) {
            lst.get(i).setTBDongNuoc(true);
        }

        if (DangNgan_DienThoai == lst.get(i).getLstHoaDon().size()) {
            lst.get(i).setDangNgan_DienThoai(true);
            lst.get(i).setTinhTrang("Đã Thu");
        }
        //goi update lại json hệ thống
//        updateArrayListToJson();
    }

    //update tình trạng parent
    public static void updateTinhTrangParent(ArrayList<CEntityParent> lst,CEntityParent entityParentUpdate) {
        for (int i=0;i<lst.size();i++)
        if(lst.get(i).getID().equals(entityParentUpdate.getID())){
            lst.get(i).setCEntityParent(entityParentUpdate);
        }
        //goi update lại json hệ thống
        updateArrayListToJson();
    }

    //update tình trạng parent
    public static CEntityParent updateTinhTrangParent(CEntityParent en) {
        //update TinhTrang
        int ThuHo = 0, TamThu = 0, GiaiTrach = 0, DangNgan_DienThoai = 0, TBDongNuoc = 0, LenhHuy = 0, PhiMoNuocThuHo = 0;
        for (CEntityChild item : en.getLstHoaDon()) {
            if (item.isGiaiTrach() == true)
                GiaiTrach++;
            else if (item.isTamThu() == true)
                TamThu++;
            else if (item.isThuHo() == true) {
                ThuHo++;
                if (Integer.parseInt(item.getPhiMoNuocThuHo()) > 0)
                    PhiMoNuocThuHo++;
            } else if (item.isLenhHuy() == true)
                LenhHuy++;
            else if (item.isTBDongNuoc() == true)
                TBDongNuoc++;

            if (item.isDangNgan_DienThoai() == true)
                DangNgan_DienThoai++;
        }

        en.setGiaiTrach(false);
        en.setTamThu(false);
        en.setThuHo(false);
        en.setLenhHuy(false);
        en.setTBDongNuoc(false);
        en.setDangNgan_DienThoai(false);
        en.setTinhTrang("");

        if (GiaiTrach == en.getLstHoaDon().size()) {
            en.setGiaiTrach(true);
            en.setTinhTrang("Giải Trách");
        } else if (TamThu == en.getLstHoaDon().size()) {
            en.setTamThu(true);
            en.setTinhTrang("Tạm Thu");
        } else if (ThuHo == en.getLstHoaDon().size()) {
            en.setThuHo(true);
            String str = "Thu Hộ";
            if (PhiMoNuocThuHo == en.getLstHoaDon().size())
                str += " (" + en.getLstHoaDon().get(0).getPhiMoNuocThuHo().substring(0, en.getLstHoaDon().get(0).getPhiMoNuocThuHo().length() - 3) + "k)";
            en.setTinhTrang(str);
        } else if (LenhHuy == en.getLstHoaDon().size()) {
            en.setLenhHuy(true);
        } else if (TBDongNuoc == en.getLstHoaDon().size()) {
            en.setTBDongNuoc(true);
        }

        if (DangNgan_DienThoai == en.getLstHoaDon().size()) {
            en.setDangNgan_DienThoai(true);
            en.setTinhTrang("Đã Thu");
        }
        return en;
    }

    //convert tiền thành chữ
    public static String formatMoney(String price, String symbol) {

        NumberFormat format = new DecimalFormat("#,##0.00");// #,##0.00 ¤ (¤:// Currency symbol)
        format.setCurrency(Currency.getInstance(Locale.US));//Or default locale

        price = (!TextUtils.isEmpty(price)) ? price : "0";
        price = price.trim();
        price = format.format(Double.parseDouble(price));
        price = price.replaceAll(",", "\\.");

        if (price.endsWith(".00")) {
            int centsIndex = price.lastIndexOf(".00");
            if (centsIndex != -1) {
                price = price.substring(0, centsIndex);
            }
        }
        price = String.format("%s " + symbol, price);
        return price;
    }

    public File createFile(Activity activity) {
        File filesDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File file = null;
        try {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            file = File.createTempFile(timeStamp, ".jpg", filesDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public static Bitmap imageOreintationValidator(Bitmap bitmap, String path) {
        ExifInterface ei;
        try {
            ei = new ExifInterface(path);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    bitmap = rotateImage(bitmap, 90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    bitmap = rotateImage(bitmap, 180);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    bitmap = rotateImage(bitmap, 270);
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {

        Bitmap bitmap = null;
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        try {
            bitmap = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                    matrix, true);
        } catch (OutOfMemoryError err) {
            err.printStackTrace();
        }
        return bitmap;
    }

    public static String convertBitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        String str = Base64.encodeToString(stream.toByteArray(), Base64.NO_WRAP);
        return str;
    }

    public static String getPathFromUri(final Context context, final Uri uri) {

//        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (DocumentsContract.isDocumentUri(context, uri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    }

                    // TODO handle non-primary volumes
                }
                // DownloadsProvider
                else if (isDownloadsDocument(uri)) {

                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                    return getDataColumn(context, contentUri, null, null);
                }
                // MediaProvider
                else if (isMediaDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }

                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[]{
                            split[1]
                    };

                    return getDataColumn(context, contentUri, selection, selectionArgs);
                }
            }
            // MediaStore (and general)
            else if ("content".equalsIgnoreCase(uri.getScheme())) {

                // Return the remote address
                if (isGooglePhotosUri(uri))
                    return uri.getLastPathSegment();

                return getDataColumn(context, uri, null, null);
            }
            // File
            else if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    //region Thermal Printer

    // Created by imrankst1221@gmail.com
    // UNICODE 0x23 = #
    public static final byte[] UNICODE_TEXT = new byte[]{0x23, 0x23, 0x23,
            0x23, 0x23, 0x23, 0x23, 0x23, 0x23, 0x23, 0x23, 0x23, 0x23, 0x23, 0x23,
            0x23, 0x23, 0x23, 0x23, 0x23, 0x23, 0x23, 0x23, 0x23, 0x23, 0x23, 0x23,
            0x23, 0x23, 0x23};

    private static String hexStr = "0123456789ABCDEF";
    private static String[] binaryArray = {"0000", "0001", "0010", "0011",
            "0100", "0101", "0110", "0111", "1000", "1001", "1010", "1011",
            "1100", "1101", "1110", "1111"};

    public static byte[] decodeBitmap(Bitmap bmp) {
        int bmpWidth = bmp.getWidth();
        int bmpHeight = bmp.getHeight();

        List<String> list = new ArrayList<String>(); //binaryString list
        StringBuffer sb;


        int bitLen = bmpWidth / 8;
        int zeroCount = bmpWidth % 8;

        String zeroStr = "";
        if (zeroCount > 0) {
            bitLen = bmpWidth / 8 + 1;
            for (int i = 0; i < (8 - zeroCount); i++) {
                zeroStr = zeroStr + "0";
            }
        }

        for (int i = 0; i < bmpHeight; i++) {
            sb = new StringBuffer();
            for (int j = 0; j < bmpWidth; j++) {
                int color = bmp.getPixel(j, i);

                int r = (color >> 16) & 0xff;
                int g = (color >> 8) & 0xff;
                int b = color & 0xff;

                // if color close to white，bit='0', else bit='1'
                if (r > 160 && g > 160 && b > 160)
                    sb.append("0");
                else
                    sb.append("1");
            }
            if (zeroCount > 0) {
                sb.append(zeroStr);
            }
            list.add(sb.toString());
        }

        List<String> bmpHexList = binaryListToHexStringList(list);
        String commandHexString = "1D763000";
        String widthHexString = Integer
                .toHexString(bmpWidth % 8 == 0 ? bmpWidth / 8
                        : (bmpWidth / 8 + 1));
        if (widthHexString.length() > 2) {
//            Log.e("decodeBitmap error", " width is too large");
            return null;
        } else if (widthHexString.length() == 1) {
            widthHexString = "0" + widthHexString;
        }
        widthHexString = widthHexString + "00";

        String heightHexString = Integer.toHexString(bmpHeight);
        if (heightHexString.length() > 2) {
//            Log.e("decodeBitmap error", " height is too large");
            return null;
        } else if (heightHexString.length() == 1) {
            heightHexString = "0" + heightHexString;
        }
        heightHexString = heightHexString + "00";

        List<String> commandList = new ArrayList<String>();
        commandList.add(commandHexString + widthHexString + heightHexString);
        commandList.addAll(bmpHexList);

        return hexList2Byte(commandList);
    }

    public static List<String> binaryListToHexStringList(List<String> list) {
        List<String> hexList = new ArrayList<String>();
        for (String binaryStr : list) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < binaryStr.length(); i += 8) {
                String str = binaryStr.substring(i, i + 8);

                String hexString = myBinaryStrToHexString(str);
                sb.append(hexString);
            }
            hexList.add(sb.toString());
        }
        return hexList;

    }

    public static String myBinaryStrToHexString(String binaryStr) {
        String hex = "";
        String f4 = binaryStr.substring(0, 4);
        String b4 = binaryStr.substring(4, 8);
        for (int i = 0; i < binaryArray.length; i++) {
            if (f4.equals(binaryArray[i]))
                hex += hexStr.substring(i, i + 1);
        }
        for (int i = 0; i < binaryArray.length; i++) {
            if (b4.equals(binaryArray[i]))
                hex += hexStr.substring(i, i + 1);
        }

        return hex;
    }

    public static byte[] hexList2Byte(List<String> list) {
        List<byte[]> commandList = new ArrayList<byte[]>();

        for (String hexStr : list) {
            commandList.add(hexStringToBytes(hexStr));
        }
        byte[] bytes = sysCopy(commandList);
        return bytes;
    }

    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    public static byte[] sysCopy(List<byte[]> srcArrays) {
        int len = 0;
        for (byte[] srcArray : srcArrays) {
            len += srcArray.length;
        }
        byte[] destArray = new byte[len];
        int destLen = 0;
        for (byte[] srcArray : srcArrays) {
            System.arraycopy(srcArray, 0, destArray, destLen, srcArray.length);
            destLen += srcArray.length;
        }
        return destArray;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    //endregion

    //region ConvertMoneyToWord

    public static HashMap<String, String> hm_tien = new HashMap<String, String>() {
        {
            put("0", "không");
            put("1", "một");
            put("2", "hai");
            put("3", "ba");
            put("4", "bốn");
            put("5", "năm");
            put("6", "sáu");
            put("7", "bảy");
            put("8", "tám");
            put("9", "chín");
        }
    };
    public static HashMap<String, String> hm_hanh = new HashMap<String, String>() {
        {
            put("1", "đồng");
            put("2", "mươi");
            put("3", "trăm");
            put("4", "nghìn");
            put("5", "mươi");
            put("6", "trăm");
            put("7", "triệu");
            put("8", "mươi");
            put("9", "trăm");
            put("10", "tỷ");
            put("11", "mươi");
            put("12", "trăm");
            put("13", "nghìn");
            put("14", "mươi");
            put("15", "trăm");

        }
    };

    public static String ConvertMoneyToWord(String money) {
        String kq = "";
        money = money.replace(".", "");
        String arr_temp[] = money.split(",");
        if (!TextUtils.isDigitsOnly(arr_temp[0])) {
            return "";
        }
        String m = arr_temp[0];
        int dem = m.length();
        String dau = "";
        int flag10 = 1;
        while (!m.equals("")) {
            if (m.length() <= 3 && m.length() > 1 && Long.parseLong(m) == 0) {

            } else {
                dau = m.substring(0, 1);
                if (dem % 3 == 1 && m.startsWith("1") && flag10 == 0) {
                    kq += "mốt ";
                    flag10 = 0;
                } else if (dem % 3 == 2 && m.startsWith("1")) {
                    kq += "mười ";
                    flag10 = 1;
                } else if (dem % 3 == 2 && m.startsWith("0") && m.length() >= 2 && !m.substring(1, 2).equals("0")) {
                    //System.out.println("a  "+m.substring(1, 2));
                    kq += "lẻ ";
                    flag10 = 1;
                } else {
                    if (!m.startsWith("0")) {
                        kq += hm_tien.get(dau) + " ";
                        flag10 = 0;
                    }
                }
                if (dem % 3 != 1 && m.startsWith("0") && m.length() > 1) {
                } else {
                    if (dem % 3 == 2 && (m.startsWith("1") || m.startsWith("0"))) {//mười
                    } else {
                        kq += hm_hanh.get(dem + "") + " ";
                    }
                }
            }
            m = m.substring(1);
            dem = m.length();
        }
        kq = kq.substring(0, kq.length() - 1);
        return kq;
    }

    //endregion

}
