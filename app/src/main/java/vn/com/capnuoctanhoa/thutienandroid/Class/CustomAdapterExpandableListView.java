package vn.com.capnuoctanhoa.thutienandroid.Class;

import android.app.Activity;

import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import vn.com.capnuoctanhoa.thutienandroid.DongNuoc.ActivityDongNuoc;
import vn.com.capnuoctanhoa.thutienandroid.DongNuoc.ActivityDongNuoc2;
import vn.com.capnuoctanhoa.thutienandroid.DongNuoc.ActivityDongTien;
import vn.com.capnuoctanhoa.thutienandroid.DongNuoc.ActivityMoNuoc;
import vn.com.capnuoctanhoa.thutienandroid.HanhThu.ActivityHoaDonDienTu_ThuTien;
import vn.com.capnuoctanhoa.thutienandroid.R;

public class CustomAdapterExpandableListView extends BaseExpandableListAdapter implements Filterable {
    private Activity activity;
    private ArrayList<CViewParent> mOriginalValues;
    private ArrayList<CViewParent> mDisplayedValues;
    private String action;

    public CustomAdapterExpandableListView(Activity activity, ArrayList<CViewParent> mDisplayedValues, String action) {
        this.activity = activity;
        this.mDisplayedValues = mDisplayedValues;
        this.action = action;
    }

    @Override
    public int getGroupCount() {
        return this.mDisplayedValues.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        ArrayList<CViewChild> lstChild = this.mDisplayedValues.get(groupPosition).getListChild();
        return lstChild.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.mDisplayedValues.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        ArrayList<CViewChild> lstChild = this.mDisplayedValues.get(groupPosition).getListChild();
        return lstChild.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    private class ViewHolder {
        TextView STT;
        TextView ID;
        TextView Row1a;
        TextView Row1b;
        TextView Row2a;
        TextView Row2b;
        TextView Row3a;
        TextView Row3b;
        TextView Row4a;
        TextView Row4b;
        ConstraintLayout layoutParent;
        Button btnMenu;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.custom_listview_parent, null);
            holder = new ViewHolder();
            holder.STT = (TextView) convertView.findViewById(R.id.lvSTT);
            holder.ID = (TextView) convertView.findViewById(R.id.lvID);
            holder.Row1a = (TextView) convertView.findViewById(R.id.lvRow1a);
            holder.Row1b = (TextView) convertView.findViewById(R.id.lvRow1b);
            holder.Row2a = (TextView) convertView.findViewById(R.id.lvRow2a);
            holder.Row2b = (TextView) convertView.findViewById(R.id.lvRow2b);
            holder.Row3a = (TextView) convertView.findViewById(R.id.lvRow3a);
            holder.Row3b = (TextView) convertView.findViewById(R.id.lvRow3b);
            holder.Row4a = (TextView) convertView.findViewById(R.id.lvRow4a);
            holder.Row4b = (TextView) convertView.findViewById(R.id.lvRow4b);
            holder.layoutParent = (ConstraintLayout) convertView.findViewById(R.id.layoutParent);
            holder.btnMenu = (Button) convertView.findViewById(R.id.btnMenu);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final CViewParent map = (CViewParent) getGroup(groupPosition);
        holder.STT.setText(map.getSTT());
        holder.ID.setText(map.getID());
        if (map.getRow1a().isEmpty() == true && map.getRow1b().isEmpty() == true) {
            holder.Row1a.setVisibility(View.GONE);
            holder.Row1b.setVisibility(View.GONE);
        } else {
            holder.Row1a.setText(map.getRow1a());
            holder.Row1b.setText(map.getRow1b());
        }
        if (map.getRow2a().isEmpty() == true && map.getRow2b().isEmpty() == true) {
            holder.Row2a.setVisibility(View.GONE);
            holder.Row2b.setVisibility(View.GONE);
        } else {
            holder.Row2a.setText(map.getRow2a());
            holder.Row2b.setText(map.getRow2b());
        }
        if (map.getRow3a().isEmpty() == true && map.getRow3b().isEmpty() == true) {
            holder.Row3a.setVisibility(View.GONE);
            holder.Row3b.setVisibility(View.GONE);
        } else {
            holder.Row3a.setText(map.getRow3a());
            holder.Row3b.setText(map.getRow3b());
        }
        if (map.getRow4a().isEmpty() == true && map.getRow4b().isEmpty() == true) {
            holder.Row4a.setVisibility(View.GONE);
            holder.Row4b.setVisibility(View.GONE);
        } else {
            holder.Row4a.setText(map.getRow4a());
            holder.Row4b.setText(map.getRow4b());
        }

        if (map.getGiaiTrach() == true || map.getTamThu() == true || map.getThuHo() == true)
            holder.layoutParent.setBackgroundColor(activity.getResources().getColor(R.color.colorGiaiTrach));
        else if (map.getLenhHuy() == true)
            holder.layoutParent.setBackgroundColor(activity.getResources().getColor(R.color.colorLenhHuy));
        else if (map.getTBDongNuoc() == true)
            holder.layoutParent.setBackgroundColor(activity.getResources().getColor(R.color.colorDongNuoc));
        else
            holder.layoutParent.setBackgroundColor(activity.getResources().getColor(R.color.colorChuaThu));

        holder.btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                PopupMenu popup = new PopupMenu(activity, v);
                if (action.contains("HanhThu") == true) {
                    popup.getMenuInflater().inflate(R.menu.menu_thutien, popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            int id = menuItem.getItemId();
                            int i = Integer.parseInt(map.getSTT()) - 1;
                            CLocal.indexPosition = i;
                            Intent intent;
                            switch (id) {
                                case R.id.action_ThuTien:
                                    intent = new Intent(activity, ActivityHoaDonDienTu_ThuTien.class);
                                    intent.putExtra("STT", String.valueOf(i));
                                    activity.startActivity(intent);
                                    break;
                                case R.id.action_TBDongNuoc:
                                    MyAsyncTask_XuLyTrucTiep_Extra myAsyncTask_xuLyTrucTiep_hd0 = new MyAsyncTask_XuLyTrucTiep_Extra();
                                    myAsyncTask_xuLyTrucTiep_hd0.execute(new String[]{"TBDongNuoc", String.valueOf(i)});
                                    break;
                            }
                            return true;
                        }
                    });
                    popup.show();
                } else if (action.contains("DongNuoc") == true) {
                    popup.getMenuInflater().inflate(R.menu.menu_dongnuoc, popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            int id = menuItem.getItemId();
                            int i = Integer.parseInt(map.getSTT()) - 1;
                            CLocal.indexPosition = i;
                            Intent intent;
                            switch (id) {
                                case R.id.action_DongNuoc1:
                                    intent = new Intent(activity, ActivityDongNuoc.class);
                                    intent.putExtra("STT", String.valueOf(i));
                                    activity.startActivity(intent);
                                    break;
                                case R.id.action_DongNuoc2:
                                    intent = new Intent(activity, ActivityDongNuoc2.class);
                                    intent.putExtra("STT", String.valueOf(i));
                                    activity.startActivity(intent);
                                    break;
                                case R.id.action_MoNuoc:
                                    intent = new Intent(activity, ActivityMoNuoc.class);
                                    intent.putExtra("STT", String.valueOf(i));
                                    activity.startActivity(intent);
                                    break;
                                case R.id.action_DongTien:
                                    intent = new Intent(activity, ActivityDongTien.class);
                                    intent.putExtra("STT", String.valueOf(i));
                                    activity.startActivity(intent);
                                    break;
                                case R.id.action_PhieuBao2:
                                    MyAsyncTask_XuLyTrucTiep_Extra myAsyncTask_xuLyTrucTiep_hd0 = new MyAsyncTask_XuLyTrucTiep_Extra();
                                    myAsyncTask_xuLyTrucTiep_hd0.execute(new String[]{"InPhieuBao2", String.valueOf(i),"2"});
                                    break;
                            }
                            return true;
                        }
                    });
                    popup.show();
                }
            }
        });

        return convertView;
    }

    private class ViewHolderChild {
        TextView ID;
        TextView Row1a;
        TextView Row1b;
        TextView Row2a;
        TextView Row2b;
        TextView Row3a;
        TextView Row3b;
        TextView Row4a;
        TextView Row4b;
        ConstraintLayout layoutChild;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolderChild holder;
        LayoutInflater inflater = activity.getLayoutInflater();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.custom_listview_child, null);
            holder = new ViewHolderChild();
            holder.ID = (TextView) convertView.findViewById(R.id.lvID);
            holder.Row1a = (TextView) convertView.findViewById(R.id.lvRow1a);
            holder.Row1b = (TextView) convertView.findViewById(R.id.lvRow1b);
            holder.Row2a = (TextView) convertView.findViewById(R.id.lvRow2a);
            holder.Row2b = (TextView) convertView.findViewById(R.id.lvRow2b);
            holder.Row3a = (TextView) convertView.findViewById(R.id.lvRow3a);
            holder.Row3b = (TextView) convertView.findViewById(R.id.lvRow3b);
            holder.Row4a = (TextView) convertView.findViewById(R.id.lvRow4a);
            holder.Row4b = (TextView) convertView.findViewById(R.id.lvRow4b);
            holder.layoutChild = (ConstraintLayout) convertView.findViewById(R.id.layoutChild);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolderChild) convertView.getTag();
        }

        CViewChild map = (CViewChild) getChild(groupPosition, childPosition);
        holder.ID.setText(map.getID());
        holder.Row1a.setText(map.getRow1a());
        holder.Row1b.setText(map.getRow1b());
        if (map.getRow1a().isEmpty() == true && map.getRow1b().isEmpty() == true) {
            holder.Row1a.setVisibility(View.GONE);
            holder.Row1b.setVisibility(View.GONE);
        } else {
            holder.Row1a.setText(map.getRow1a());
            holder.Row1b.setText(map.getRow1b());
        }
        if (map.getRow2a().isEmpty() == true && map.getRow2b().isEmpty() == true) {
            holder.Row2a.setVisibility(View.GONE);
            holder.Row2b.setVisibility(View.GONE);
        } else {
            holder.Row2a.setText(map.getRow2a());
            holder.Row2b.setText(map.getRow2b());
        }
        if (map.getRow3a().isEmpty() == true && map.getRow3b().isEmpty() == true) {
            holder.Row3a.setVisibility(View.GONE);
            holder.Row3b.setVisibility(View.GONE);
        } else {
            holder.Row3a.setText(map.getRow3a());
            holder.Row3b.setText(map.getRow3b());
        }
        if (map.getRow4a().isEmpty() == true && map.getRow4b().isEmpty() == true) {
            holder.Row4a.setVisibility(View.GONE);
            holder.Row4b.setVisibility(View.GONE);
        } else {
            holder.Row4a.setText(map.getRow4a());
            holder.Row4b.setText(map.getRow4b());
        }
        if (map.getGiaiTrach() == true || map.getTamThu() == true || map.getThuHo() == true)
            holder.layoutChild.setBackgroundColor(activity.getResources().getColor(R.color.colorGiaiTrach));
        else if (map.getLenhHuy() == true)
            holder.layoutChild.setBackgroundColor(activity.getResources().getColor(R.color.colorLenhHuy));
        else if (map.getTBDongNuoc() == true)
            holder.layoutChild.setBackgroundColor(activity.getResources().getColor(R.color.colorDongNuoc));
        else
            holder.layoutChild.setBackgroundColor(activity.getResources().getColor(R.color.colorChuaThu));

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mDisplayedValues = (ArrayList<CViewParent>) results.values; // has the filtered values
                notifyDataSetChanged();  // notifies the data with new filtered values
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                ArrayList<CViewParent> FilteredArrList = new ArrayList<CViewParent>();

                if (mOriginalValues == null) {
                    mOriginalValues = new ArrayList<CViewParent>(mDisplayedValues); // saves the original data in mOriginalValues
                }

                /********
                 *
                 *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
                 *  else does the Filtering and returns FilteredArrList(Filtered)
                 *
                 ********/
                if (constraint == null || constraint.length() == 0) {
                    // set the Original result to return
                    results.count = mOriginalValues.size();
                    results.values = mOriginalValues;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < mOriginalValues.size(); i++) {
//                        String data = mOriginalValues.get(i).getName1();
                        if (mOriginalValues.get(i).getRow1a().toLowerCase().startsWith(constraint.toString())
                                || mOriginalValues.get(i).getRow1b().toLowerCase().startsWith(constraint.toString())
                                || mOriginalValues.get(i).getRow2a().toLowerCase().startsWith(constraint.toString())
                                || mOriginalValues.get(i).getRow2b().toLowerCase().startsWith(constraint.toString())
                                || mOriginalValues.get(i).getRow3a().toLowerCase().startsWith(constraint.toString())
                                || mOriginalValues.get(i).getRow3b().toLowerCase().startsWith(constraint.toString())
                                || mOriginalValues.get(i).getRow4a().toLowerCase().startsWith(constraint.toString())
                                || mOriginalValues.get(i).getRow4b().toLowerCase().startsWith(constraint.toString())) {
                            CViewParent entity = new CViewParent();
                            entity.setSTT(mOriginalValues.get(i).getSTT());
                            entity.setID(mOriginalValues.get(i).getID());
                            entity.setRow1a(mOriginalValues.get(i).getRow1a());
                            entity.setRow1b(mOriginalValues.get(i).getRow1b());
                            entity.setRow2a(mOriginalValues.get(i).getRow2a());
                            entity.setRow2b(mOriginalValues.get(i).getRow2b());
                            entity.setRow3a(mOriginalValues.get(i).getRow3a());
                            entity.setRow3b(mOriginalValues.get(i).getRow3b());
                            entity.setRow4a(mOriginalValues.get(i).getRow4a());
                            entity.setRow4b(mOriginalValues.get(i).getRow4b());
                            entity.setGiaiTrach(mOriginalValues.get(i).getGiaiTrach());
                            entity.setTamThu(mOriginalValues.get(i).getTamThu());
                            entity.setThuHo(mOriginalValues.get(i).getThuHo());
                            entity.setLenhHuy(mOriginalValues.get(i).getLenhHuy());
                            entity.setListChild(mOriginalValues.get(i).getListChild());

                            FilteredArrList.add(entity);
                        }
                    }
                    // set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                return results;
            }
        };
        return filter;
    }

    public class MyAsyncTask_XuLyTrucTiep_Extra extends AsyncTask<String, Void, String[]> {
        ProgressDialog progressDialog;
        CWebservice ws = new CWebservice();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(activity);
            progressDialog.setTitle("Thông Báo");
            progressDialog.setMessage("Đang xử lý...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected String[] doInBackground(String... strings) {
            try {
                String result = "";
                String[] results = new String[]{};
                String MaHDs = "";
//                SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Date dateCapNhat = new Date();
                int STT=-1;
                switch (strings[0]) {
                    case "HD0":
                        for (int i = 0; i < CLocal.listHanhThuView.size(); i++) {
                            for (int j = 0; j < CLocal.listHanhThuView.get(i).getLstHoaDon().size(); j++)
                                if (CLocal.listHanhThuView.get(i).getLstHoaDon().get(j).isGiaiTrach() == false
                                        && CLocal.listHanhThuView.get(i).getLstHoaDon().get(j).isThuHo() == false
                                        && CLocal.listHanhThuView.get(i).getLstHoaDon().get(j).isTamThu() == false
                                        && CLocal.listHanhThuView.get(i).getLstHoaDon().get(j).isDangNgan_DienThoai() == false
                                        && Integer.parseInt(CLocal.listHanhThuView.get(i).getLstHoaDon().get(j).getTieuThu()) == 0) {
                                    result = ws.XuLy_HoaDonDienTu("DangNgan", CLocal.MaNV, CLocal.listHanhThuView.get(i).getLstHoaDon().get(j).getMaHD(), CLocal.DateFormat.format(dateCapNhat), "", CLocal.listHanhThuView.get(i).getMaKQDN(), "false");
                                    results = result.split(";");
                                    if (Boolean.parseBoolean(results[0]) == true) {
                                        CLocal.listHanhThuView.get(i).getLstHoaDon().get(j).setDangNgan_DienThoai(true);
                                        CLocal.listHanhThuView.get(i).getLstHoaDon().get(j).setGiaiTrach(true);
                                        CLocal.listHanhThuView.get(i).getLstHoaDon().get(j).setNgayGiaiTrach(CLocal.DateFormat.format(dateCapNhat));
                                        CLocal.listHanhThuView.get(i).getLstHoaDon().get(j).setMaNV_DangNgan(CLocal.MaNV);
                                        CLocal.listHanhThuView.get(i).getLstHoaDon().get(j).setXoaDangNgan_Ngay_DienThoai("");
                                    }
                                }

                            if (Boolean.parseBoolean(results[0]) == false && results.length == 5) {
                                CLocal.updateValueChild(CLocal.listHanhThu, results[2], results[3], results[4]);
                                CLocal.updateValueChild(CLocal.listHanhThuView, results[2], results[3], results[4]);
                            }
                            CLocal.updateTinhTrangParent(CLocal.listHanhThuView, i);
                            CLocal.updateTinhTrangParent(CLocal.listHanhThu, CLocal.listHanhThuView.get(i));
                        }
                        break;
                    case "TBDongNuoc":
                         STT = Integer.parseInt(strings[1]);
                        for (int j = 0; j < CLocal.listHanhThuView.get(STT).getLstHoaDon().size(); j++)
                            if (CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).isGiaiTrach() == false
                                    && CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).isThuHo() == false
                                    && CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).isTamThu() == false
                                    && CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).isDangNgan_DienThoai() == false
                                    && CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).getTBDongNuoc_Ngay().equals("") == true) {
                                if (MaHDs.equals("") == true)
                                    MaHDs += CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).getMaHD();
                                else
                                    MaHDs += "," + CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).getMaHD();
                            }
                        Date dt2 = dateCapNhat;
                        Calendar c2 = Calendar.getInstance();
                        c2.setTime(dt2);
                        c2.add(Calendar.DATE, 3);
                        dt2 = c2.getTime();
                        result = ws.XuLy_HoaDonDienTu("TBDongNuoc", CLocal.MaNV, MaHDs, CLocal.DateFormat.format(dateCapNhat), CLocal.DateFormat.format(dt2), CLocal.listHanhThuView.get(STT).getMaKQDN(), "false");
                        results = result.split(";");
                        if (Boolean.parseBoolean(results[0]) == true) {
                            for (int j = 0; j < CLocal.listHanhThuView.get(STT).getLstHoaDon().size(); j++)
                                if (MaHDs.contains(CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).getMaHD())) {
                                    CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).setTBDongNuoc_Ngay(CLocal.DateFormat.format(dateCapNhat));
                                    CLocal.listHanhThuView.get(STT).getLstHoaDon().get(j).setTBDongNuoc_NgayHen(CLocal.DateFormat.format(dt2));
                                }
                        }
                        break;
                    case "InPhieuBao2":
                         STT = Integer.parseInt(strings[1]);
                        for (int j = 0; j < CLocal.listDongNuocView.get(STT).getLstHoaDon().size(); j++)
                            if (CLocal.listDongNuocView.get(STT).getLstHoaDon().get(j).isGiaiTrach() == false
                                    && CLocal.listDongNuocView.get(STT).getLstHoaDon().get(j).isThuHo() == false
                                    && CLocal.listDongNuocView.get(STT).getLstHoaDon().get(j).isTamThu() == false
                                    && CLocal.listDongNuocView.get(STT).getLstHoaDon().get(j).isDangNgan_DienThoai() == false
                                    && CLocal.listDongNuocView.get(STT).getLstHoaDon().get(j).getInPhieuBao2_Ngay().equals("") == true) {
                                if (MaHDs.equals("") == true)
                                    MaHDs += CLocal.listDongNuocView.get(STT).getLstHoaDon().get(j).getMaHD();
                                else
                                    MaHDs += "," + CLocal.listDongNuocView.get(STT).getLstHoaDon().get(j).getMaHD();
                            }
                        Date dt = dateCapNhat;
                        Calendar c = Calendar.getInstance();
                        c.setTime(dt);
                        c.add(Calendar.DATE, Integer.parseInt(strings[2]));
                        dt = c.getTime();
                        if(MaHDs.equals("")==false) {
                            result = ws.XuLy_HoaDonDienTu("PhieuBao2", CLocal.MaNV, MaHDs, CLocal.DateFormat.format(dateCapNhat), CLocal.DateFormat.format(dt), CLocal.listDongNuocView.get(STT).getMaKQDN(), "false");
                            results = result.split(";");
                            if (Boolean.parseBoolean(results[0]) == true) {
                                for (int j = 0; j < CLocal.listDongNuocView.get(STT).getLstHoaDon().size(); j++)
                                    if (MaHDs.contains(CLocal.listDongNuocView.get(STT).getLstHoaDon().get(j).getMaHD())) {
                                        CLocal.listDongNuocView.get(STT).getLstHoaDon().get(j).setInPhieuBao2_Ngay(CLocal.DateFormat.format(dateCapNhat));
                                        CLocal.listDongNuocView.get(STT).getLstHoaDon().get(j).setInPhieuBao2_NgayHen(CLocal.DateFormat.format(dt));
                                    }
                                if (CLocal.serviceThermalPrinter != null)
                                    CLocal.serviceThermalPrinter.printPhieuBao2(CLocal.listDongNuocView.get(STT));
                            }
                        }
                        else
                        if (CLocal.serviceThermalPrinter != null)
                            CLocal.serviceThermalPrinter.printPhieuBao2(CLocal.listDongNuocView.get(STT));
                }

                return results;
            } catch (Exception ex) {
                return new String[]{"false;" + ex.getMessage()};
            }
        }

        @Override
        protected void onPostExecute(String[] strings) {
            super.onPostExecute(strings);
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            if (Boolean.parseBoolean(strings[0]) == true) {
                CLocal.showPopupMessage(activity, "THÀNH CÔNG", "center");
            } else
                CLocal.showPopupMessage(activity, "THẤT BẠI\n" + strings[1], "center");
        }
    }
}
