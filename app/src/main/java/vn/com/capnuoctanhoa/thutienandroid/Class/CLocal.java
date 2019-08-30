package vn.com.capnuoctanhoa.thutienandroid.Class;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
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
    public static String MaNV = "", HoTen = "", MaTo = "";
    public static boolean Doi = false, ToTruong = false;
    public static ArrayList<CEntityParent> listHanhThu, listDongNuoc;

    public static void initialCLocal() {
        SharedPreferences.Editor editor = CLocal.sharedPreferencesre.edit();
        editor.putString("Username", "");
        editor.putString("Password", "");
        editor.putString("MaNV", "");
        editor.putString("HoTen", "");
        editor.putString("MaTo", "");
        editor.putString("jsonHanhThu", "");
        editor.putString("jsonDongNuoc", "");
        editor.putString("jsonMessage", "");
        editor.putString("jsonTo", "");
        editor.putString("jsonNhanVien", "");
        editor.putBoolean("Doi", false);
        editor.putBoolean("ToTruong", false);
        editor.putBoolean("Login", false);
        editor.commit();
        editor.remove("jsonHanhThu_HoaDonDienTu").commit();
        editor.remove("jsonDongNuocChild").commit();
        MaNV = HoTen = MaTo = "";
        Doi = ToTruong = false;
        jsonHanhThu = jsonDongNuoc = jsonDongNuocChild = jsonMessage = jsonTo = jsonNhanVien = null;
        listHanhThu = listDongNuoc = null;
    }

    public static boolean checkNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    public static void showPopupMessage(Context context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
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

    public static void updateArrayList(ArrayList<CEntityParent> lst, String NameUpdate, String ValueUpdate, String MaHD) {
        try {
            for (int i = 0; i < lst.size(); i++) {
                for (int j = 0; j < lst.get(i).getLstHoaDon().size(); j++)
                    if (lst.get(i).getLstHoaDon().get(j).getMaHD().equals(MaHD) == true) {
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
                            case "PhiMoNuoc":
                                lst.get(i).getLstHoaDon().get(j).setPhiMoNuoc(ValueUpdate);
                                break;
                            case "DaThu":
                                lst.get(i).getLstHoaDon().get(j).setDaThu(Boolean.parseBoolean(ValueUpdate));
                                break;
                            case "InPhieuBao":
                                lst.get(i).getLstHoaDon().get(j).setInPhieuBao(Boolean.parseBoolean(ValueUpdate));
                                break;
                        }
                    }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
