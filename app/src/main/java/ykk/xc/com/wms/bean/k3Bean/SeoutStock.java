package ykk.xc.com.wms.bean.k3Bean;

import java.io.Serializable;
import java.util.Date;

import ykk.xc.com.wms.bean.Department;

/**
 * @author 2019年5月10日 下午5:10:39
 * @Description:发货通知表
 */
public class SeoutStock implements Serializable {
    private int fdetailid; // 唯一行号
    private String fbrno;/* 公司机构内码 */

    private int finterid;/* 通知单内码 */

    private String fbillno;/* 编 号 */

    private Short ftrantype;/* 单据类型 */

    private int fsaltype;/* 销售方式 */

    private int fcustid;/* 购货单位 */

    private String fdate;/* 日期 */

    private int fstockid;/* 收货仓库 */

    private String fadd;/* 地址 */

    private String fnote;/* 退料原因 */

    private int fempid;/* 业务员 */

    private int fcheckerid;/* 审核人 */

    private int fbillerid;/* 制单人 */

    private int fmanagerid;/* 主管人 */

    private int fclosed;/* 关闭状态，0未关闭，1关闭 */

    private Short finvoiceclosed;/* 发票关闭 */

    private int fdeptid;/* 部门 */

    private int fsettleid;/* 结算方式 */

    private int ftranstatus;/* 传输状态 */

    private Double fexchangerate;/* 汇 率: */

    private int fcurrencyid;/* 币 别 */

    private Short fstatus;/* 状态 */

    private Boolean fcancellation;/* 作废 */

    private int fcurchecklevel;/* 当前审核级别 */

    private int frelatebrid;/* 订货机构 */

    private Date fcheckdate;/* 审核日期 */

    private String fexplanation;/* 摘要 */

    private String ffetchadd;/* 交货地点 */

    private int fseltrantype;/* 源单类型 */

    private int fchildren;/* 关联标识 */

    private int fbrid;/* 制单机构 */

    private int fareaps;/* 销售范围 */

    private int fmanagetype;/* 保税监管类型 */

    private int fexchangeratetype;/* 汇率类型 */
    /* 快递公司ID */
    private String fheadselfs0238;
    /* 物流方式（990157快递，990158自提） */
    private String fheadselfs0239;
    /* 预约的快递单号 */
    private String fheadselfs0243;
    /* 部门 */
    private Department department;
    /* 业务员 */
    private Emp emp;
    /* 购货人 */
    private Customer customer;
    /* k3物流公司 */
    private LogisticsCompany logcompany;

    /* 临时字段,不存表 */
    private int tempEmpId;
    private String expressNo; // 扫码的快递单号

    public String getFbrno() {
        return fbrno;
    }

    public int getFinterid() {
        return finterid;
    }

    public String getFbillno() {
        return fbillno;
    }

    public Short getFtrantype() {
        return ftrantype;
    }

    public int getFsaltype() {
        return fsaltype;
    }

    public int getFcustid() {
        return fcustid;
    }

    public String getFdate() {
        return fdate;
    }

    public int getFstockid() {
        return fstockid;
    }

    public String getFadd() {
        return fadd;
    }

    public String getFnote() {
        return fnote;
    }

    public int getFempid() {
        return fempid;
    }

    public int getFcheckerid() {
        return fcheckerid;
    }

    public int getFbillerid() {
        return fbillerid;
    }

    public int getFmanagerid() {
        return fmanagerid;
    }

    public int getFclosed() {
        return fclosed;
    }

    public Short getFinvoiceclosed() {
        return finvoiceclosed;
    }

    public int getFdeptid() {
        return fdeptid;
    }

    public int getFsettleid() {
        return fsettleid;
    }

    public int getFtranstatus() {
        return ftranstatus;
    }

    public Double getFexchangerate() {
        return fexchangerate;
    }

    public int getFcurrencyid() {
        return fcurrencyid;
    }

    public Short getFstatus() {
        return fstatus;
    }

    public Boolean getFcancellation() {
        return fcancellation;
    }

    public int getFcurchecklevel() {
        return fcurchecklevel;
    }

    public int getFrelatebrid() {
        return frelatebrid;
    }

    public Date getFcheckdate() {
        return fcheckdate;
    }

    public String getFexplanation() {
        return fexplanation;
    }

    public String getFfetchadd() {
        return ffetchadd;
    }

    public int getFseltrantype() {
        return fseltrantype;
    }

    public int getFchildren() {
        return fchildren;
    }

    public int getFbrid() {
        return fbrid;
    }

    public int getFareaps() {
        return fareaps;
    }

    public int getFmanagetype() {
        return fmanagetype;
    }

    public int getFexchangeratetype() {
        return fexchangeratetype;
    }

    public String getFheadselfs0238() {
        return fheadselfs0238;
    }

    public String getFheadselfs0239() {
        return fheadselfs0239;
    }

    public String getFheadselfs0243() {
        return fheadselfs0243;
    }

    public Department getDepartment() {
        return department;
    }

    public Emp getEmp() {
        return emp;
    }

    public Customer getCustomer() {
        return customer;
    }

    public LogisticsCompany getLogcompany() {
        return logcompany;
    }

    public void setFbrno(String fbrno) {
        this.fbrno = fbrno;
    }

    public void setFinterid(int finterid) {
        this.finterid = finterid;
    }

    public void setFbillno(String fbillno) {
        this.fbillno = fbillno;
    }

    public void setFtrantype(Short ftrantype) {
        this.ftrantype = ftrantype;
    }

    public void setFsaltype(int fsaltype) {
        this.fsaltype = fsaltype;
    }

    public void setFcustid(int fcustid) {
        this.fcustid = fcustid;
    }

    public void setFdate(String fdate) {
        this.fdate = fdate;
    }

    public void setFstockid(int fstockid) {
        this.fstockid = fstockid;
    }

    public void setFadd(String fadd) {
        this.fadd = fadd;
    }

    public void setFnote(String fnote) {
        this.fnote = fnote;
    }

    public void setFempid(int fempid) {
        this.fempid = fempid;
    }

    public void setFcheckerid(int fcheckerid) {
        this.fcheckerid = fcheckerid;
    }

    public void setFbillerid(int fbillerid) {
        this.fbillerid = fbillerid;
    }

    public void setFmanagerid(int fmanagerid) {
        this.fmanagerid = fmanagerid;
    }

    public void setFclosed(int fclosed) {
        this.fclosed = fclosed;
    }

    public void setFinvoiceclosed(Short finvoiceclosed) {
        this.finvoiceclosed = finvoiceclosed;
    }

    public void setFdeptid(int fdeptid) {
        this.fdeptid = fdeptid;
    }

    public void setFsettleid(int fsettleid) {
        this.fsettleid = fsettleid;
    }

    public void setFtranstatus(int ftranstatus) {
        this.ftranstatus = ftranstatus;
    }

    public void setFexchangerate(Double fexchangerate) {
        this.fexchangerate = fexchangerate;
    }

    public void setFcurrencyid(int fcurrencyid) {
        this.fcurrencyid = fcurrencyid;
    }

    public void setFstatus(Short fstatus) {
        this.fstatus = fstatus;
    }

    public void setFcancellation(Boolean fcancellation) {
        this.fcancellation = fcancellation;
    }

    public void setFcurchecklevel(int fcurchecklevel) {
        this.fcurchecklevel = fcurchecklevel;
    }

    public void setFrelatebrid(int frelatebrid) {
        this.frelatebrid = frelatebrid;
    }

    public void setFcheckdate(Date fcheckdate) {
        this.fcheckdate = fcheckdate;
    }

    public void setFexplanation(String fexplanation) {
        this.fexplanation = fexplanation;
    }

    public void setFfetchadd(String ffetchadd) {
        this.ffetchadd = ffetchadd;
    }

    public void setFseltrantype(int fseltrantype) {
        this.fseltrantype = fseltrantype;
    }

    public void setFchildren(int fchildren) {
        this.fchildren = fchildren;
    }

    public void setFbrid(int fbrid) {
        this.fbrid = fbrid;
    }

    public void setFareaps(int fareaps) {
        this.fareaps = fareaps;
    }

    public void setFmanagetype(int fmanagetype) {
        this.fmanagetype = fmanagetype;
    }

    public void setFexchangeratetype(int fexchangeratetype) {
        this.fexchangeratetype = fexchangeratetype;
    }

    public void setFheadselfs0238(String fheadselfs0238) {
        this.fheadselfs0238 = fheadselfs0238;
    }

    public void setFheadselfs0239(String fheadselfs0239) {
        this.fheadselfs0239 = fheadselfs0239;
    }

    public void setFheadselfs0243(String fheadselfs0243) {
        this.fheadselfs0243 = fheadselfs0243;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public void setEmp(Emp emp) {
        this.emp = emp;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setLogcompany(LogisticsCompany logcompany) {
        this.logcompany = logcompany;
    }

    public int getTempEmpId() {
        return tempEmpId;
    }

    public void setTempEmpId(int tempEmpId) {
        this.tempEmpId = tempEmpId;
    }

    public String getExpressNo() {
        return expressNo;
    }

    public void setExpressNo(String expressNo) {
        this.expressNo = expressNo;
    }

    public int getFdetailid() {
        return fdetailid;
    }

    public void setFdetailid(int fdetailid) {
        this.fdetailid = fdetailid;
    }

}