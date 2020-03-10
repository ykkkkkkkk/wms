package ykk.xc.com.wms.bean.prod;

import java.io.Serializable;

import ykk.xc.com.wms.bean.Department;
import ykk.xc.com.wms.bean.k3Bean.Customer;
import ykk.xc.com.wms.bean.k3Bean.ICItem;

/**
 * 金蝶生产任务单bean
 *
 * @author Administrator
 */
public class ProdOrder implements Serializable {

    /*生产任务单id*/
    private int prodId;
    /*生产任务单号*/
    private String prodNo;
    /* 制单日期 */
    private String fconfirmDate;
    /*生产车间id*/
    private int workShopId;
    /*生产车间代码*/
    private String deptNumber;
    /*生产车间名称*/
    private String deptName;
    /*物料id*/
    private int icItemId;
    /*物料代码*/
    private String icItemNumber;
    /*物料名称*/
    private String icItemName;
    /*物料单位id*/
    private int unitId;
    /*物料单位代码*/
    private String unitNumber;
    /*物料单位名称*/
    private String unitName;
    /*生产数量*/
    private double fqty;
    /*生码数量 ， 对应FHeadSelfJ01102 字段*/
    private double createCodeQty;
    /*销售订单分录id*/
    private int salOrderEntryId;
    /*销售订单id*/
    private int salOrderId;
    /*销售订单号*/
    private String salOrderNo;
    /*生产任务单备注*/
    private String prodNote;
    /*生产任务单单据日期*/
    private String prodBillDate;
    /*销售订单客户id*/
    private int custId;
    /*销售订单客户代码*/
    private String custNumber;
    /*销售订单客户名称*/
    private String custName;
    /*生产任务单单据状态（ 0：计划，1：下达，3：结案）*/
    private String prodStatus;
    /*生产任务单是否关闭标志*/
    private String prodClosed;
    //生产订单是否装箱（990662代表是，990663代表否）
    private int prodBinning;
    private double fauxInHighLimitQty;	// 完工入库上限数量


    private ICItem icItem;
    // 客户对象
    private Customer cust;
    private Department department; // 部门对象

    // 临时字段，不存表
    private int isCheck; // 是否选中
    private String strBarcode; // 拼接的单号
    private double useableQty; // 可用数量
    private String barcodeCreateDate; // 条码创建日期

    public ProdOrder() {
        super();
    }

    public int getProdId() {
        return prodId;
    }

    public void setProdId(int prodId) {
        this.prodId = prodId;
    }

    public String getProdNo() {
        return prodNo;
    }

    public void setProdNo(String prodNo) {
        this.prodNo = prodNo;
    }

    public int getWorkShopId() {
        return workShopId;
    }

    public void setWorkShopId(int workShopId) {
        this.workShopId = workShopId;
    }

    public String getDeptNumber() {
        return deptNumber;
    }

    public void setDeptNumber(String deptNumber) {
        this.deptNumber = deptNumber;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public int getIcItemId() {
        return icItemId;
    }

    public void setIcItemId(int icItemId) {
        this.icItemId = icItemId;
    }

    public String getIcItemNumber() {
        return icItemNumber;
    }

    public void setIcItemNumber(String icItemNumber) {
        this.icItemNumber = icItemNumber;
    }

    public String getIcItemName() {
        return icItemName;
    }

    public void setIcItemName(String icItemName) {
        this.icItemName = icItemName;
    }

    public int getUnitId() {
        return unitId;
    }

    public void setUnitId(int unitId) {
        this.unitId = unitId;
    }

    public String getUnitNumber() {
        return unitNumber;
    }

    public void setUnitNumber(String unitNumber) {
        this.unitNumber = unitNumber;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }


    public int getSalOrderEntryId() {
        return salOrderEntryId;
    }

    public void setSalOrderEntryId(int salOrderEntryId) {
        this.salOrderEntryId = salOrderEntryId;
    }

    public int getSalOrderId() {
        return salOrderId;
    }

    public void setSalOrderId(int salOrderId) {
        this.salOrderId = salOrderId;
    }

    public String getSalOrderNo() {
        return salOrderNo;
    }

    public void setSalOrderNo(String salOrderNo) {
        this.salOrderNo = salOrderNo;
    }

    public String getProdNote() {
        return prodNote;
    }

    public void setProdNote(String prodNote) {
        this.prodNote = prodNote;
    }

    public String getProdBillDate() {
        return prodBillDate;
    }

    public void setProdBillDate(String prodBillDate) {
        this.prodBillDate = prodBillDate;
    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public String getCustNumber() {
        return custNumber;
    }

    public void setCustNumber(String custNumber) {
        this.custNumber = custNumber;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getProdStatus() {
        return prodStatus;
    }

    public void setProdStatus(String prodStatus) {
        this.prodStatus = prodStatus;
    }

    public String getProdClosed() {
        return prodClosed;
    }

    public void setProdClosed(String prodClosed) {
        this.prodClosed = prodClosed;
    }

    public double getFqty() {
        return fqty;
    }

    public void setFqty(double fqty) {
        this.fqty = fqty;
    }

    public ICItem getIcItem() {
        return icItem;
    }

    public void setIcItem(ICItem icItem) {
        this.icItem = icItem;
    }

    public int getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(int isCheck) {
        this.isCheck = isCheck;
    }

    public String getStrBarcode() {
        return strBarcode;
    }

    public void setStrBarcode(String strBarcode) {
        this.strBarcode = strBarcode;
    }

    public double getUseableQty() {
        return useableQty;
    }

    public void setUseableQty(double useableQty) {
        this.useableQty = useableQty;
    }

    public Customer getCust() {
        return cust;
    }

    public Department getDepartment() {
        return department;
    }

    public void setCust(Customer cust) {
        this.cust = cust;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }


    public double getCreateCodeQty() {
        return createCodeQty;
    }

    public void setCreateCodeQty(double createCodeQty) {
        this.createCodeQty = createCodeQty;
    }

    public String getBarcodeCreateDate() {
        return barcodeCreateDate;
    }

    public void setBarcodeCreateDate(String barcodeCreateDate) {
        this.barcodeCreateDate = barcodeCreateDate;
    }

    public String getFconfirmDate() {
        return fconfirmDate;
    }

    public void setFconfirmDate(String fconfirmDate) {
        this.fconfirmDate = fconfirmDate;
    }

    public int getProdBinning() {
        return prodBinning;
    }

    public void setProdBinning(int prodBinning) {
        this.prodBinning = prodBinning;
    }

    public double getFauxInHighLimitQty() {
        return fauxInHighLimitQty;
    }

    public void setFauxInHighLimitQty(double fauxInHighLimitQty) {
        this.fauxInHighLimitQty = fauxInHighLimitQty;
    }

}