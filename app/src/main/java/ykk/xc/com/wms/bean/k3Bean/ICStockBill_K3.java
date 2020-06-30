package ykk.xc.com.wms.bean.k3Bean;

import java.io.Serializable;

import ykk.xc.com.wms.bean.Department;
import ykk.xc.com.wms.bean.Supplier;

/**
 * @author 2019年4月28日 下午4:56:44
 * @Description:
 */
public class ICStockBill_K3 implements Serializable {
    /* 单据内码 */
    private int finterid;
    /* 单据类型 */
    private int ftrantype;
    /* 单据日期 */
    private String fdate;
    /* 单据编号 */
    private String fbillno;
    /* 备注 */
    private String fnote;
    /* 收入库房id */
    private int fdcstockid;
    /* 发出库房id */
    private int fscstockid;
    /* 部门id */
    private int fdeptid;
    /* 业务员id */
    private int fempid;
    /* 供应商id */
    private int fsupplyid;
    private int fcustId; // 客户id
    /* 记账人id */
    private int fposterid;
    /* 审核人id */
    private int fcheckerid;
    /* 发货人id */
    private int ffmanagerid;
    /* 保管人id */
    private int fsmanagerid;
    /* 制单人id */
    private int fbillerid;
    /* 退货单号 */
    private int freturnbillinterid;
    /* 已废弃 */
    private String fscbillno;
    /* 状态 0-未审核，1-已审核 */
    private int fstatus;
    /* 作废状态。0未作废，1已作废 */
    private Boolean fcancellation;
    /* 原单内码 */
    private int forgbillinterid;
    /* 单据类别 */
    private int fbilltypeid;
    /* 唯一标识 */
    private String fuuid;
    /* 关联发票号 */
    private int frelateinvoiceid;
    /* 摘要 */
    private String fexplanation;
    /* 交货地点 */
    private String ffetchadd;
    /* 交货日期 */
    private String ffetchdate;

    private int fmanagerid;
    /* 调拨类型 */
    private int freftype;
    /* 源单类型 */
    private int fseltrantype;
    /* 对方单据号 */
    private String fpoordbillno;
    /* 应收款日期 */
    private String fsettledate;
    /* 收货方 */
    private String fconsignee;
    /* 打印次数 */
    private int fprintcount;
    /* 收件人 */
    private String freceiver;
    /* 收货地址 */
    private String fconsigneeadd;
    /* 收件人手机 */
    private String freceivermobile;

    /* 红蓝色 */
    private int frob;

    /* 在电商账套，代表源账套出库单号FbillNo。在生产账套代表：对方订单号 */
    private String fheadselfb0163;

    // -------------------start 以下字段在生产账套中存在-------------------//
    /* 生产账套表示对方客户id，电商账套表示来源单据内码 */
    private String fheadselfb0164;
    /* 生产账号--对方客户代码，电商账号--销售出库单号 */
    private String fheadselfb0165;
    /* 对方客户名称 */
    private String fheadselfb0166;
    /* 对方制单人id */
    private String fheadselfb0167;
    /* 对方部门id */
    private String fheadselfb0168;
    /* 是否已下推至别的账套作为销售出库单 （A未下推，B已下推） */
    private String fheadselfb0169;

    /* 是否已下推至别的账套作为采购入库单（A未下推，B已下推） */
    private String fheadselfb0170;
    /* 是否退货到线束  A未下推，B已下推*/
    private String fHeadSelfA0148;
    // -------------------end 以上字段在生产账套中存在-------------------//
    /* 销售出库单所属账套id */
    private int deptId;
    /* 部门名称 */
    private String deptName;
    /* 部门编码 */
    private String deptNumber;
    /* 客户代码 */
    private String fnumber;
    /* 客户名称 */
    private String fname;
    /* 发货人代码 不存库 */
    private String ffmanagercode;
    /* 保管人代码 不存库 */
    private String fsmanagercode;
    /* 供应商代码 不存库 */
    private String supplierCode;

    /* 部门 */
    private Department department;
    /* 业务员 */
    private Emp emp;
    private ICStockBillEntry_K3 icstockBillEntry;
    /* 供应商 */
    private Supplier supplier;

    // 临时字段
    private String expressNo; // 快递单号

    //生产销售出库下推内销，外销销售出库查询临时字段
    private String contact;//联系人
    private String contactPhone;//联系电话
    private String defaultAddress;//默认地址
    private String province;//省
    private String city;//市
    private String region;//区
    private String minAddress;//详细地址
    private String actMan;//代发收货人
    private String conPhone;//收货人电话
    private String logPayType;//快递费方式
    private String logName;//物流公司
    private Double postage;//邮资
    private String delivery;//发货方式
    private String logNumber;//物流单号
    private String empName;//业务员

    private Customer customer;


    public int getFinterid() {
        return finterid;
    }
    public int getFtrantype() {
        return ftrantype;
    }
    public String getFdate() {
        return fdate;
    }
    public String getFbillno() {
        return fbillno;
    }
    public String getFnote() {
        return fnote;
    }
    public int getFdcstockid() {
        return fdcstockid;
    }
    public int getFscstockid() {
        return fscstockid;
    }
    public int getFdeptid() {
        return fdeptid;
    }
    public int getFempid() {
        return fempid;
    }
    public int getFsupplyid() {
        return fsupplyid;
    }
    public int getFposterid() {
        return fposterid;
    }
    public int getFcheckerid() {
        return fcheckerid;
    }
    public int getFfmanagerid() {
        return ffmanagerid;
    }
    public int getFsmanagerid() {
        return fsmanagerid;
    }
    public int getFbillerid() {
        return fbillerid;
    }
    public int getFreturnbillinterid() {
        return freturnbillinterid;
    }
    public String getFscbillno() {
        return fscbillno;
    }
    public int getFstatus() {
        return fstatus;
    }
    public Boolean getFcancellation() {
        return fcancellation;
    }
    public int getForgbillinterid() {
        return forgbillinterid;
    }
    public int getFbilltypeid() {
        return fbilltypeid;
    }
    public String getFuuid() {
        return fuuid;
    }
    public int getFrelateinvoiceid() {
        return frelateinvoiceid;
    }
    public String getFexplanation() {
        return fexplanation;
    }
    public String getFfetchadd() {
        return ffetchadd;
    }
    public String getFfetchdate() {
        return ffetchdate;
    }
    public int getFmanagerid() {
        return fmanagerid;
    }
    public int getFreftype() {
        return freftype;
    }
    public int getFseltrantype() {
        return fseltrantype;
    }
    public String getFpoordbillno() {
        return fpoordbillno;
    }
    public String getFsettledate() {
        return fsettledate;
    }
    public String getFconsignee() {
        return fconsignee;
    }
    public int getFprintcount() {
        return fprintcount;
    }
    public String getFreceiver() {
        return freceiver;
    }
    public String getFconsigneeadd() {
        return fconsigneeadd;
    }
    public String getFreceivermobile() {
        return freceivermobile;
    }
    public int getFrob() {
        return frob;
    }
    public String getFheadselfb0163() {
        return fheadselfb0163;
    }
    public String getFheadselfb0164() {
        return fheadselfb0164;
    }
    public String getFheadselfb0165() {
        return fheadselfb0165;
    }
    public String getFheadselfb0166() {
        return fheadselfb0166;
    }
    public String getFheadselfb0167() {
        return fheadselfb0167;
    }
    public String getFheadselfb0168() {
        return fheadselfb0168;
    }
    public String getFheadselfb0169() {
        return fheadselfb0169;
    }
    public String getFheadselfb0170() {
        return fheadselfb0170;
    }
    public int getDeptId() {
        return deptId;
    }
    public String getDeptName() {
        return deptName;
    }
    public String getDeptNumber() {
        return deptNumber;
    }
    public String getFnumber() {
        return fnumber;
    }
    public String getFname() {
        return fname;
    }
    public String getFfmanagercode() {
        return ffmanagercode;
    }
    public String getFsmanagercode() {
        return fsmanagercode;
    }
    public String getSupplierCode() {
        return supplierCode;
    }
    public Department getDepartment() {
        return department;
    }
    public Emp getEmp() {
        return emp;
    }
    public Supplier getSupplier() {
        return supplier;
    }
    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }
    public void setFinterid(int finterid) {
        this.finterid = finterid;
    }
    public void setFtrantype(int ftrantype) {
        this.ftrantype = ftrantype;
    }
    public void setFdate(String fdate) {
        this.fdate = fdate;
    }
    public void setFbillno(String fbillno) {
        this.fbillno = fbillno;
    }
    public void setFnote(String fnote) {
        this.fnote = fnote;
    }
    public void setFdcstockid(int fdcstockid) {
        this.fdcstockid = fdcstockid;
    }
    public void setFscstockid(int fscstockid) {
        this.fscstockid = fscstockid;
    }
    public void setFdeptid(int fdeptid) {
        this.fdeptid = fdeptid;
    }
    public void setFempid(int fempid) {
        this.fempid = fempid;
    }
    public void setFsupplyid(int fsupplyid) {
        this.fsupplyid = fsupplyid;
    }
    public void setFposterid(int fposterid) {
        this.fposterid = fposterid;
    }
    public void setFcheckerid(int fcheckerid) {
        this.fcheckerid = fcheckerid;
    }
    public void setFfmanagerid(int ffmanagerid) {
        this.ffmanagerid = ffmanagerid;
    }
    public void setFsmanagerid(int fsmanagerid) {
        this.fsmanagerid = fsmanagerid;
    }
    public void setFbillerid(int fbillerid) {
        this.fbillerid = fbillerid;
    }
    public void setFreturnbillinterid(int freturnbillinterid) {
        this.freturnbillinterid = freturnbillinterid;
    }
    public void setFscbillno(String fscbillno) {
        this.fscbillno = fscbillno;
    }
    public void setFstatus(int fstatus) {
        this.fstatus = fstatus;
    }
    public void setFcancellation(Boolean fcancellation) {
        this.fcancellation = fcancellation;
    }
    public void setForgbillinterid(int forgbillinterid) {
        this.forgbillinterid = forgbillinterid;
    }
    public void setFbilltypeid(int fbilltypeid) {
        this.fbilltypeid = fbilltypeid;
    }
    public void setFuuid(String fuuid) {
        this.fuuid = fuuid;
    }
    public void setFrelateinvoiceid(int frelateinvoiceid) {
        this.frelateinvoiceid = frelateinvoiceid;
    }
    public void setFexplanation(String fexplanation) {
        this.fexplanation = fexplanation;
    }
    public void setFfetchadd(String ffetchadd) {
        this.ffetchadd = ffetchadd;
    }
    public void setFfetchdate(String ffetchdate) {
        this.ffetchdate = ffetchdate;
    }
    public void setFmanagerid(int fmanagerid) {
        this.fmanagerid = fmanagerid;
    }
    public void setFreftype(int freftype) {
        this.freftype = freftype;
    }
    public void setFseltrantype(int fseltrantype) {
        this.fseltrantype = fseltrantype;
    }
    public void setFpoordbillno(String fpoordbillno) {
        this.fpoordbillno = fpoordbillno;
    }
    public void setFsettledate(String fsettledate) {
        this.fsettledate = fsettledate;
    }
    public void setFconsignee(String fconsignee) {
        this.fconsignee = fconsignee;
    }
    public void setFprintcount(int fprintcount) {
        this.fprintcount = fprintcount;
    }
    public void setFreceiver(String freceiver) {
        this.freceiver = freceiver;
    }
    public void setFconsigneeadd(String fconsigneeadd) {
        this.fconsigneeadd = fconsigneeadd;
    }
    public void setFreceivermobile(String freceivermobile) {
        this.freceivermobile = freceivermobile;
    }
    public void setFrob(int frob) {
        this.frob = frob;
    }
    public void setFheadselfb0163(String fheadselfb0163) {
        this.fheadselfb0163 = fheadselfb0163;
    }
    public void setFheadselfb0164(String fheadselfb0164) {
        this.fheadselfb0164 = fheadselfb0164;
    }
    public void setFheadselfb0165(String fheadselfb0165) {
        this.fheadselfb0165 = fheadselfb0165;
    }
    public void setFheadselfb0166(String fheadselfb0166) {
        this.fheadselfb0166 = fheadselfb0166;
    }
    public void setFheadselfb0167(String fheadselfb0167) {
        this.fheadselfb0167 = fheadselfb0167;
    }
    public void setFheadselfb0168(String fheadselfb0168) {
        this.fheadselfb0168 = fheadselfb0168;
    }
    public void setFheadselfb0169(String fheadselfb0169) {
        this.fheadselfb0169 = fheadselfb0169;
    }
    public void setFheadselfb0170(String fheadselfb0170) {
        this.fheadselfb0170 = fheadselfb0170;
    }
    public String getfHeadSelfA0148() {
        return fHeadSelfA0148;
    }
    public void setfHeadSelfA0148(String fHeadSelfA0148) {
        this.fHeadSelfA0148 = fHeadSelfA0148;
    }
    public void setDeptId(int deptId) {
        this.deptId = deptId;
    }
    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }
    public void setDeptNumber(String deptNumber) {
        this.deptNumber = deptNumber;
    }
    public void setFnumber(String fnumber) {
        this.fnumber = fnumber;
    }
    public void setFname(String fname) {
        this.fname = fname;
    }
    public void setFfmanagercode(String ffmanagercode) {
        this.ffmanagercode = ffmanagercode;
    }
    public void setFsmanagercode(String fsmanagercode) {
        this.fsmanagercode = fsmanagercode;
    }
    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }
    public void setDepartment(Department department) {
        this.department = department;
    }
    public void setEmp(Emp emp) {
        this.emp = emp;
    }

    public ICStockBillEntry_K3 getIcstockBillEntry() {
        return icstockBillEntry;
    }
    public void setIcstockBillEntry(ICStockBillEntry_K3 icstockBillEntry) {
        this.icstockBillEntry = icstockBillEntry;
    }

    public String getExpressNo() {
        return expressNo;
    }

    public void setExpressNo(String expressNo) {
        this.expressNo = expressNo;
    }

    public String getContact() {
        return contact;
    }
    public void setContact(String contact) {
        this.contact = contact;
    }
    public String getContactPhone() {
        return contactPhone;
    }
    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }
    public String getDefaultAddress() {
        return defaultAddress;
    }
    public void setDefaultAddress(String defaultAddress) {
        this.defaultAddress = defaultAddress;
    }
    public String getProvince() {
        return province;
    }
    public void setProvince(String province) {
        this.province = province;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getRegion() {
        return region;
    }
    public void setRegion(String region) {
        this.region = region;
    }
    public String getMinAddress() {
        return minAddress;
    }
    public void setMinAddress(String minAddress) {
        this.minAddress = minAddress;
    }
    public String getActMan() {
        return actMan;
    }
    public void setActMan(String actMan) {
        this.actMan = actMan;
    }
    public String getConPhone() {
        return conPhone;
    }
    public void setConPhone(String conPhone) {
        this.conPhone = conPhone;
    }
    public String getLogPayType() {
        return logPayType;
    }
    public void setLogPayType(String logPayType) {
        this.logPayType = logPayType;
    }
    public String getLogName() {
        return logName;
    }
    public void setLogName(String logName) {
        this.logName = logName;
    }
    public Double getPostage() {
        return postage;
    }
    public void setPostage(Double postage) {
        this.postage = postage;
    }
    public String getDelivery() {
        return delivery;
    }
    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }
    public String getLogNumber() {
        return logNumber;
    }
    public void setLogNumber(String logNumber) {
        this.logNumber = logNumber;
    }

    public String getEmpName() {
        return empName;
    }
    public void setEmpName(String empName) {
        this.empName = empName;
    }
    public int getFcustId() {
        return fcustId;
    }
    public void setFcustId(int fcustId) {
        this.fcustId = fcustId;
    }
    public Customer getCustomer() {
        return customer;
    }
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }


}