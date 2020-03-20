package ykk.xc.com.wms.bean.k3Bean;

import java.io.Serializable;

import ykk.xc.com.wms.bean.Department;

/**
 * @author 2019年5月10日 下午5:10:39
 * @Description:发货通知表
 */
public class SeoutStock implements Serializable {
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

    private Short fstatus;/* 状态 ( 0：未审核，1：审核，2：k3自己下推部分到出库，3：结案 ) */

    private Boolean fcancellation;/* 作废 */

    private int fcurchecklevel;/* 当前审核级别 */

    private int frelatebrid;/* 订货机构 */

    private String fcheckdate;/* 审核日期 */

    private String fexplanation;/* 摘要 */

    private String ffetchadd;/* 交货地点 */

    private int fseltrantype;/* 源单类型 */

    private int fchildren;/* 关联标识 */

    private int fbrid;/* 制单机构 */

    private int fareaps;/* 销售范围 */

    private int fmanagetype;/* 保税监管类型 */

    private int fexchangeratetype;/* 汇率类型 */

    private int fprintcount; /* 打印次数 */

    private int fheadselfs0241;	// 发货方式( 发货运:990664），送货:990665 )

    /* 部门 */
    private Department dept;
    /* 业务员 */
    private Emp emp;
    /* 购货人 */
    private Customer cust;

    /* 临时字段,不存表 */


    public SeoutStock() {
        super();
    }

    public String getFbrno() {
        return fbrno;
    }

    public void setFbrno(String fbrno) {
        this.fbrno = fbrno;
    }
    public int getFinterid() {
        return finterid;
    }
    public void setFinterid(int finterid) {
        this.finterid = finterid;
    }
    public String getFbillno() {
        return fbillno;
    }
    public void setFbillno(String fbillno) {
        this.fbillno = fbillno;
    }
    public Short getFtrantype() {
        return ftrantype;
    }
    public void setFtrantype(Short ftrantype) {
        this.ftrantype = ftrantype;
    }
    public int getFsaltype() {
        return fsaltype;
    }
    public void setFsaltype(int fsaltype) {
        this.fsaltype = fsaltype;
    }
    public int getFcustid() {
        return fcustid;
    }
    public void setFcustid(int fcustid) {
        this.fcustid = fcustid;
    }
    public String getFdate() {
        return fdate;
    }
    public void setFdate(String fdate) {
        this.fdate = fdate;
    }
    public int getFstockid() {
        return fstockid;
    }
    public void setFstockid(int fstockid) {
        this.fstockid = fstockid;
    }
    public String getFadd() {
        return fadd;
    }
    public void setFadd(String fadd) {
        this.fadd = fadd;
    }
    public String getFnote() {
        return fnote;
    }
    public void setFnote(String fnote) {
        this.fnote = fnote;
    }
    public int getFempid() {
        return fempid;
    }
    public void setFempid(int fempid) {
        this.fempid = fempid;
    }
    public int getFcheckerid() {
        return fcheckerid;
    }
    public void setFcheckerid(int fcheckerid) {
        this.fcheckerid = fcheckerid;
    }
    public int getFbillerid() {
        return fbillerid;
    }
    public void setFbillerid(int fbillerid) {
        this.fbillerid = fbillerid;
    }
    public int getFmanagerid() {
        return fmanagerid;
    }
    public void setFmanagerid(int fmanagerid) {
        this.fmanagerid = fmanagerid;
    }
    public int getFclosed() {
        return fclosed;
    }
    public void setFclosed(int fclosed) {
        this.fclosed = fclosed;
    }
    public Short getFinvoiceclosed() {
        return finvoiceclosed;
    }
    public void setFinvoiceclosed(Short finvoiceclosed) {
        this.finvoiceclosed = finvoiceclosed;
    }
    public int getFdeptid() {
        return fdeptid;
    }
    public void setFdeptid(int fdeptid) {
        this.fdeptid = fdeptid;
    }
    public int getFsettleid() {
        return fsettleid;
    }
    public void setFsettleid(int fsettleid) {
        this.fsettleid = fsettleid;
    }
    public int getFtranstatus() {
        return ftranstatus;
    }
    public void setFtranstatus(int ftranstatus) {
        this.ftranstatus = ftranstatus;
    }
    public Double getFexchangerate() {
        return fexchangerate;
    }
    public void setFexchangerate(Double fexchangerate) {
        this.fexchangerate = fexchangerate;
    }
    public int getFcurrencyid() {
        return fcurrencyid;
    }
    public void setFcurrencyid(int fcurrencyid) {
        this.fcurrencyid = fcurrencyid;
    }
    public Short getFstatus() {
        return fstatus;
    }
    public void setFstatus(Short fstatus) {
        this.fstatus = fstatus;
    }
    public Boolean getFcancellation() {
        return fcancellation;
    }
    public void setFcancellation(Boolean fcancellation) {
        this.fcancellation = fcancellation;
    }
    public int getFcurchecklevel() {
        return fcurchecklevel;
    }
    public void setFcurchecklevel(int fcurchecklevel) {
        this.fcurchecklevel = fcurchecklevel;
    }
    public int getFrelatebrid() {
        return frelatebrid;
    }
    public void setFrelatebrid(int frelatebrid) {
        this.frelatebrid = frelatebrid;
    }
    public String getFcheckdate() {
        return fcheckdate;
    }
    public void setFcheckdate(String fcheckdate) {
        this.fcheckdate = fcheckdate;
    }
    public String getFexplanation() {
        return fexplanation;
    }
    public void setFexplanation(String fexplanation) {
        this.fexplanation = fexplanation;
    }
    public String getFfetchadd() {
        return ffetchadd;
    }
    public void setFfetchadd(String ffetchadd) {
        this.ffetchadd = ffetchadd;
    }
    public int getFseltrantype() {
        return fseltrantype;
    }
    public void setFseltrantype(int fseltrantype) {
        this.fseltrantype = fseltrantype;
    }
    public int getFchildren() {
        return fchildren;
    }
    public void setFchildren(int fchildren) {
        this.fchildren = fchildren;
    }
    public int getFbrid() {
        return fbrid;
    }
    public void setFbrid(int fbrid) {
        this.fbrid = fbrid;
    }
    public int getFareaps() {
        return fareaps;
    }
    public void setFareaps(int fareaps) {
        this.fareaps = fareaps;
    }
    public int getFmanagetype() {
        return fmanagetype;
    }
    public void setFmanagetype(int fmanagetype) {
        this.fmanagetype = fmanagetype;
    }
    public int getFexchangeratetype() {
        return fexchangeratetype;
    }
    public void setFexchangeratetype(int fexchangeratetype) {
        this.fexchangeratetype = fexchangeratetype;
    }
    public int getFprintcount() {
        return fprintcount;
    }
    public void setFprintcount(int fprintcount) {
        this.fprintcount = fprintcount;
    }
    public int getFheadselfs0241() {
        return fheadselfs0241;
    }
    public void setFheadselfs0241(int fheadselfs0241) {
        this.fheadselfs0241 = fheadselfs0241;
    }
    public Department getDept() {
        return dept;
    }
    public void setDept(Department dept) {
        this.dept = dept;
    }
    public Emp getEmp() {
        return emp;
    }
    public void setEmp(Emp emp) {
        this.emp = emp;
    }
    public Customer getCust() {
        return cust;
    }
    public void setCust(Customer cust) {
        this.cust = cust;
    }





}
