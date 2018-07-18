package vn.com.capnuoctanhoa.thutienandroid.Class;

import java.util.Comparator;

public class CSort implements Comparator<CEntityParent> {
    private String loai;
    private int order = -1;//-1 là tăng dần, 1 là giảm dần

    public CSort(String loai, int order) {
        this.loai = loai;
        this.order = order;
    }

    @Override
    public int compare(CEntityParent o1, CEntityParent o2) {
        switch (loai) {
            case "ModifyDate":
                if (o1.getModifyDate().toString().compareTo(o2.getModifyDate().toString()) == 0)
                    return 0;
                else if (o1.getModifyDate().toString().compareTo(o2.getModifyDate().toString()) < 0)
                    return order;
                else
                    return (-1 * order);
                default://do Row2a hanhthu & dongnuoc cùng lưu MLT
                    if (o1.getRow1a().toString().compareTo(o2.getRow1a().toString()) == 0)
                        return 0;
                    else if (o1.getRow1a().toString().compareTo(o2.getRow1a().toString()) < 0)
                        return order;
                    else
                        return (-1 * order);
        }
    }
}

