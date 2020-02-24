package ykk.xc.com.wms.bean.pur;

import java.io.Serializable;
import java.util.Arrays;

import ykk.xc.com.wms.bean.Department;
import ykk.xc.com.wms.bean.Supplier;
import ykk.xc.com.wms.bean.k3Bean.Emp;
import ykk.xc.com.wms.bean.k3Bean.SeOrder;

/**
 * @Description:采购订单
 *
 * @author qxp 2019年3月13日 下午2:45:47
 */
public class POOrder implements Serializable {
	/* 采购订单内码 */
	private Integer finterid;
	/* 单据类型 */
	private Integer ftrantype;
	/* 采购订单号 */
	private String fbillno;
	/* 供应商内码 */
	private Integer fsupplyid;
	/* 单据日期 */
	private String fdate;
	/* 业务员 */
	private Integer fempid;
	/* 部门 */
	private Integer fdeptid;
	/* 币别 */
	private Integer fcurrencyid;
	/* 审核人 */
	private Integer fcheckerid;
	/* 制单人 */
	private Integer fbillerid;
	/* 主管 */
	private Integer fmangerid;
	/* 关单标志 */
	private Short fclosed;
	/* 传输状态 */
	private Integer ftranstatus;
	/* 汇率 */
	private Double fexchangerate;
	/* 状态 */
	private Short fstatus;
	/* 作废 */
	private Boolean fcancellation;
	/* 采购方式 */
	private Integer fpostyle;
	/* 当前审核级别 */
	private Integer fcurchecklevel;
	/* 供货机构 */
	private Integer frelatebrid;
	/* 确认标志 */
	private Integer forderaffirm;
	/* 现金折扣 */
	private String fcashdiscount;
	/* 审核日期 */
	private String fcheckdate;
	/* 摘要 */
	private String fexplanation;
	/* 交货地点 */
	private String ffetchadd;
	/* 结算日期 */
	private String fsettledate;
	/* 结算方式 */
	private Integer fsettleid;
	/* 源单类型 */
	private Integer fseltrantype;
	/* 关联标识 */
	private Integer fchildren;
	/* 制单机构 */
	private Integer fbrid;
	/* 分销订单号 */
	private String fpoordbillno;
	/* 采购范围 */
	private Integer fareaps;
	/* 事务类型 */
	private Integer fclasstypeid;
	/* 订单金额 */
	private double ftotalcostfor;
	/* 最近修改日期 */
	private String flastmodydate;
	/* 保税监管类型 */
	private Integer fmanagetype;
	/* 版本号 */
	private String fversionno;
	/* 变更日期 */
	private String fchangedate;
	/* 变更原因 */
	private String fchangecauses;
	/* 变更人 */
	private Integer fchangeuser;
	/* 确认人 */
	private String fvalidatername;
	/* 收货方 */
	private String fconsignee;
	/* 打印次数 */
	private Short fprintcount;
	/* 汇率类型 */
	private Integer fexchangeratetype;
	/* 交货地点 */
	private String fdeliveryplace;
	/* 采购模式 */
	private Integer fpomode;
	/* 计划类别 */
	private Integer fplancategory;
	/* 业务员 */
	private String eFname;
	/* 供应商 */
	private String sFname;
	/* 部门 */
	private String dFname;
	/* 销售订单号 */
	private String fheadselfp0257;
	/* 对方客户id */
	private String fheadselfp0258;
	/* 订单摘要 */
	private String fheadselfp0259;
	/* 全渠通出库单号 */
	private String fheadselfp0260;
	/* 物流单号 */
	private String fheadselfp0261;
	/* 是否下推至生产账套形成销售订单，A为下推，B已下推 */
	private String fheadselfp0262;

	private SeOrder seorder;

	private byte[] foperdate;
	/* 供应商 */
	private Supplier supplier;
	/* 部门 */
	private Department department;
	/* 业务员 */
	private Emp emp;

	//	 临时字段，不存表

	private String deptCode; // 部门代码
	private String empCode; // 业务员代码


	public String getFheadselfp0262() {
		return fheadselfp0262;
	}

	public void setFheadselfp0262(String fheadselfp0262) {
		this.fheadselfp0262 = fheadselfp0262;
	}

	public SeOrder getSeorder() {
		return seorder;
	}

	public void setSeorder(SeOrder seorder) {
		this.seorder = seorder;
	}

	public String getFheadselfp0259() {
		return fheadselfp0259;
	}

	public void setFheadselfp0259(String fheadselfp0259) {
		this.fheadselfp0259 = fheadselfp0259;
	}

	public String getFheadselfp0260() {
		return fheadselfp0260;
	}

	public void setFheadselfp0260(String fheadselfp0260) {
		this.fheadselfp0260 = fheadselfp0260;
	}

	public String getFheadselfp0261() {
		return fheadselfp0261;
	}

	public void setFheadselfp0261(String fheadselfp0261) {
		this.fheadselfp0261 = fheadselfp0261;
	}

	public String getFheadselfp0257() {
		return fheadselfp0257;
	}

	public void setFheadselfp0257(String fheadselfp0257) {
		this.fheadselfp0257 = fheadselfp0257;
	}

	public String getFheadselfp0258() {
		return fheadselfp0258;
	}

	public void setFheadselfp0258(String fheadselfp0258) {
		this.fheadselfp0258 = fheadselfp0258;
	}

	public String geteFname() {
		return eFname;
	}

	public void seteFname(String eFname) {
		this.eFname = eFname;
	}

	public String getsFname() {
		return sFname;
	}

	public void setsFname(String sFname) {
		this.sFname = sFname;
	}

	public String getdFname() {
		return dFname;
	}

	public void setdFname(String dFname) {
		this.dFname = dFname;
	}

	public Integer getFinterid() {
		return finterid;
	}

	public void setFinterid(Integer finterid) {
		this.finterid = finterid;
	}

	public Integer getFtrantype() {
		return ftrantype;
	}

	public void setFtrantype(Integer ftrantype) {
		this.ftrantype = ftrantype;
	}

	public String getFbillno() {
		return fbillno;
	}

	public void setFbillno(String fbillno) {
		this.fbillno = fbillno == null ? null : fbillno.trim();
	}

	public Integer getFsupplyid() {
		return fsupplyid;
	}

	public void setFsupplyid(Integer fsupplyid) {
		this.fsupplyid = fsupplyid;
	}

	public String getFdate() {
		return fdate;
	}

	public void setFdate(String fdate) {
		this.fdate = fdate;
	}

	public Integer getFempid() {
		return fempid;
	}

	public void setFempid(Integer fempid) {
		this.fempid = fempid;
	}

	public Integer getFdeptid() {
		return fdeptid;
	}

	public void setFdeptid(Integer fdeptid) {
		this.fdeptid = fdeptid;
	}

	public Integer getFcurrencyid() {
		return fcurrencyid;
	}

	public void setFcurrencyid(Integer fcurrencyid) {
		this.fcurrencyid = fcurrencyid;
	}

	public Integer getFcheckerid() {
		return fcheckerid;
	}

	public void setFcheckerid(Integer fcheckerid) {
		this.fcheckerid = fcheckerid;
	}

	public Integer getFbillerid() {
		return fbillerid;
	}

	public void setFbillerid(Integer fbillerid) {
		this.fbillerid = fbillerid;
	}

	public Integer getFmangerid() {
		return fmangerid;
	}

	public void setFmangerid(Integer fmangerid) {
		this.fmangerid = fmangerid;
	}

	public Short getFclosed() {
		return fclosed;
	}

	public void setFclosed(Short fclosed) {
		this.fclosed = fclosed;
	}

	public Integer getFtranstatus() {
		return ftranstatus;
	}

	public void setFtranstatus(Integer ftranstatus) {
		this.ftranstatus = ftranstatus;
	}

	public Double getFexchangerate() {
		return fexchangerate;
	}

	public void setFexchangerate(Double fexchangerate) {
		this.fexchangerate = fexchangerate;
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

	public Integer getFpostyle() {
		return fpostyle;
	}

	public void setFpostyle(Integer fpostyle) {
		this.fpostyle = fpostyle;
	}

	public Integer getFcurchecklevel() {
		return fcurchecklevel;
	}

	public void setFcurchecklevel(Integer fcurchecklevel) {
		this.fcurchecklevel = fcurchecklevel;
	}

	public Integer getFrelatebrid() {
		return frelatebrid;
	}

	public void setFrelatebrid(Integer frelatebrid) {
		this.frelatebrid = frelatebrid;
	}

	public Integer getForderaffirm() {
		return forderaffirm;
	}

	public void setForderaffirm(Integer forderaffirm) {
		this.forderaffirm = forderaffirm;
	}

	public String getFcashdiscount() {
		return fcashdiscount;
	}

	public void setFcashdiscount(String fcashdiscount) {
		this.fcashdiscount = fcashdiscount == null ? null : fcashdiscount.trim();
	}

	public String getFexplanation() {
		return fexplanation;
	}

	public void setFexplanation(String fexplanation) {
		this.fexplanation = fexplanation == null ? null : fexplanation.trim();
	}

	public String getFfetchadd() {
		return ffetchadd;
	}

	public void setFfetchadd(String ffetchadd) {
		this.ffetchadd = ffetchadd == null ? null : ffetchadd.trim();
	}

	public Integer getFsettleid() {
		return fsettleid;
	}

	public void setFsettleid(Integer fsettleid) {
		this.fsettleid = fsettleid;
	}

	public Integer getFseltrantype() {
		return fseltrantype;
	}

	public void setFseltrantype(Integer fseltrantype) {
		this.fseltrantype = fseltrantype;
	}

	public Integer getFchildren() {
		return fchildren;
	}

	public void setFchildren(Integer fchildren) {
		this.fchildren = fchildren;
	}

	public Integer getFbrid() {
		return fbrid;
	}

	public void setFbrid(Integer fbrid) {
		this.fbrid = fbrid;
	}

	public String getFpoordbillno() {
		return fpoordbillno;
	}

	public void setFpoordbillno(String fpoordbillno) {
		this.fpoordbillno = fpoordbillno == null ? null : fpoordbillno.trim();
	}

	public Integer getFareaps() {
		return fareaps;
	}

	public void setFareaps(Integer fareaps) {
		this.fareaps = fareaps;
	}

	public Integer getFclasstypeid() {
		return fclasstypeid;
	}

	public void setFclasstypeid(Integer fclasstypeid) {
		this.fclasstypeid = fclasstypeid;
	}

	public double getFtotalcostfor() {
		return ftotalcostfor;
	}

	public void setFtotalcostfor(double ftotalcostfor) {
		this.ftotalcostfor = ftotalcostfor;
	}

	public Integer getFmanagetype() {
		return fmanagetype;
	}

	public void setFmanagetype(Integer fmanagetype) {
		this.fmanagetype = fmanagetype;
	}

	public String getFversionno() {
		return fversionno;
	}

	public void setFversionno(String fversionno) {
		this.fversionno = fversionno == null ? null : fversionno.trim();
	}

	public String getFchangecauses() {
		return fchangecauses;
	}

	public void setFchangecauses(String fchangecauses) {
		this.fchangecauses = fchangecauses == null ? null : fchangecauses.trim();
	}

	public Integer getFchangeuser() {
		return fchangeuser;
	}

	public void setFchangeuser(Integer fchangeuser) {
		this.fchangeuser = fchangeuser;
	}

	public String getFvalidatername() {
		return fvalidatername;
	}

	public void setFvalidatername(String fvalidatername) {
		this.fvalidatername = fvalidatername == null ? null : fvalidatername.trim();
	}

	public String getFconsignee() {
		return fconsignee;
	}

	public void setFconsignee(String fconsignee) {
		this.fconsignee = fconsignee == null ? null : fconsignee.trim();
	}

	public Short getFprintcount() {
		return fprintcount;
	}

	public void setFprintcount(Short fprintcount) {
		this.fprintcount = fprintcount;
	}

	public Integer getFexchangeratetype() {
		return fexchangeratetype;
	}

	public void setFexchangeratetype(Integer fexchangeratetype) {
		this.fexchangeratetype = fexchangeratetype;
	}

	public String getFdeliveryplace() {
		return fdeliveryplace;
	}

	public void setFdeliveryplace(String fdeliveryplace) {
		this.fdeliveryplace = fdeliveryplace == null ? null : fdeliveryplace.trim();
	}

	public Integer getFpomode() {
		return fpomode;
	}

	public void setFpomode(Integer fpomode) {
		this.fpomode = fpomode;
	}

	public Integer getFplancategory() {
		return fplancategory;
	}

	public void setFplancategory(Integer fplancategory) {
		this.fplancategory = fplancategory;
	}

	public byte[] getFoperdate() {
		return foperdate;
	}

	public void setFoperdate(byte[] foperdate) {
		this.foperdate = foperdate;
	}

	public String getFcheckdate() {
		return fcheckdate;
	}

	public void setFcheckdate(String fcheckdate) {
		this.fcheckdate = fcheckdate;
	}

	public String getFsettledate() {
		return fsettledate;
	}

	public void setFsettledate(String fsettledate) {
		this.fsettledate = fsettledate;
	}

	public String getFlastmodydate() {
		return flastmodydate;
	}

	public void setFlastmodydate(String flastmodydate) {
		this.flastmodydate = flastmodydate;
	}

	public String getFchangedate() {
		return fchangedate;
	}

	public void setFchangedate(String fchangedate) {
		this.fchangedate = fchangedate;
	}

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public String getEmpCode() {
		return empCode;
	}

	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}

	public Supplier getSupplier() {
		return supplier;
	}

	public Department getDepartment() {
		return department;
	}

	public Emp getEmp() {
		return emp;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public void setEmp(Emp emp) {
		this.emp = emp;
	}

}