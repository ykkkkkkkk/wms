package ykk.xc.com.wms.bean.k3Bean;

import java.io.Serializable;

/**
 * @Description:k3物流公司
 *
 * @author 2019年5月14日 上午9:36:58
 */
public class LogisticsCompany implements Serializable {

    /* 物流公司内码 */
    private Integer FItemID;
    /* 电话 */
    private String F_103;
    /* 联系人 */
    private String F_104;
    /* 地址 */
    private String F_105;
    /* 物流公司代码 */
    private String FNumber;
    /* 物流公司名称 */
    private String FName;

    public Integer getFItemID() {
        return FItemID;
    }

    public void setFItemID(Integer FItemID) {
        this.FItemID = FItemID;
    }

    public String getF_104() {
        return F_104;
    }

    public void setF_104(String f_104) {
        F_104 = f_104;
    }

    public String getF_105() {
        return F_105;
    }

    public void setF_105(String f_105) {
        F_105 = f_105;
    }

    public String getF_103() {
        return F_103;
    }

    public void setF_103(String f_103) {
        F_103 = f_103;
    }

    public String getFNumber() {
        return FNumber;
    }

    public void setFNumber(String FNumber) {
        this.FNumber = FNumber == null ? null : FNumber.trim();
    }

    public String getFName() {
        return FName;
    }

    public void setFName(String FName) {
        this.FName = FName == null ? null : FName.trim();
    }

    @Override
    public String toString() {
        return "LogisticsCompany [FItemID=" + FItemID + ", F_103=" + F_103 + ", F_104=" + F_104 + ", F_105=" + F_105
                + ", FNumber=" + FNumber + ", FName=" + FName + "]";
    }


}