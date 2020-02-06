package vn.com.capnuoctanhoa.thutienandroid.Class;

public class CHoaDon {
    private String MaHD;
    private String Ky;
    private String TongCong;
    private boolean Selected;

    public CHoaDon() {
        MaHD = "";
        Ky = "";
        TongCong = "";
        Selected = false;
    }

    public String getMaHD() {
        return MaHD;
    }

    public void setMaHD(String maHD) {
        MaHD = maHD;
    }

    public String getKy() {
        return Ky;
    }

    public void setKy(String ky) {
        Ky = ky;
    }

    public String getTongCong() {
        return TongCong;
    }

    public void setTongCong(String tongCong) {
        TongCong = tongCong;
    }

    public boolean isSelected() {
        return Selected;
    }

    public void setSelected(boolean selected) {
        Selected = selected;
    }

    @Override
    public String toString() {
        return Ky + " : " + CLocal.formatMoney(TongCong, "đ");
    }
}
