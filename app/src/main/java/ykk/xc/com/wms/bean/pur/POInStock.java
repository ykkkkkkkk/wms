package ykk.xc.com.wms.bean.pur;

import java.io.Serializable;

import ykk.xc.com.wms.bean.Department;
import ykk.xc.com.wms.bean.Supplier;
import ykk.xc.com.wms.bean.k3Bean.Emp;

/**
 * @Description:收料通知单单
 */
public class POInStock implements Serializable {
	/* 采购订单内码 */
	private int finterid;
	/* 采购订单号 */
	private String fbillno;
	/* 币别 */
	private int fcurrencyid;
	/* 单据类型 */
	private int ftrantype;
	/* 供应商内码 */
	private int fsupplyid;
	/* 部门 */
	private int fdeptid;
	/* 业务员 */
	private int fempid;
	/* 单据日期 */
	private String fdate;
	/* 收料仓库id */
	private int fstockId;
	/* 记账人 */
	private int fposterId;
	/* 审核人 */
	private int fcheckerid;
	/* 制单人 */
	private int fbillerid;
	/* 主管   */
	private int ffmanagerId;
	/* 主管人   */
	private int fsmanagerId;
	private String fcnnBillNo;
	/* 关单标志 */
	private int fclosed;
	private String fnote;
	private int finvoiceClosed;
	private int fbclosed;
	private String fcreateDate;
	private String fcheckDate;
	/* 汇率 */
	private double fexchangeRate;
	private int fstatus;
	/* 作废 */
	private int fcancellation;
	private int fupStockWhenSave;
	/* 采购方式 */
	private int fpoStyle;
	/* 当前审核级别 */
	private int fcurchecklevel;
	/* 供货机构 */
	private int frelatebrid;
	/* 摘要 */
	private String fexplanation;
	/* 交货地点 */
	private String ffetchadd;
	/* 源单类型 */
	private int fseltrantype;
	/* 关联标识 */
	private int fchildren;
	/* 制单机构 */
	private int fbrid;
	/* 传输状态 */
	private int ftranstatus;
	/* 采购范围 */
	private int fareaps;
	/* 保税监管类型 */
	private int fmanagetype;
	/* 12510－外购入库类型，12511－订单委外类型  */
	private int fbizType;
	/* 汇率类型 */
	private int fexchangeratetype;
	/* 采购模式 */
	private int fpomode;
	/* 打印次数 */
	private Short fprintcount;
	/* 审核日期 */
	private String fcheckdate;
	/* 分销订单号 */
	private String fpoordbillno;

	/* 供应商 */
	private Supplier supplier;
	/* 部门 */
	private Department department;
	/* 业务员 */
	private Emp emp;

	//	 临时字段，不存表
	private String deptCode; // 部门代码
	private String empCode; // 业务员代码
	private String empName;
	private String suppName;
	private String deptName;
	private String stockName;


	public POInStock() {
		super();
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
	public int getFcurrencyid() {
		return fcurrencyid;
	}
	public void setFcurrencyid(int fcurrencyid) {
		this.fcurrencyid = fcurrencyid;
	}
	public int getFtrantype() {
		return ftrantype;
	}
	public void setFtrantype(int ftrantype) {
		this.ftrantype = ftrantype;
	}
	public int getFsupplyid() {
		return fsupplyid;
	}
	public void setFsupplyid(int fsupplyid) {
		this.fsupplyid = fsupplyid;
	}
	public int getFdeptid() {
		return fdeptid;
	}
	public void setFdeptid(int fdeptid) {
		this.fdeptid = fdeptid;
	}
	public int getFempid() {
		return fempid;
	}
	public void setFempid(int fempid) {
		this.fempid = fempid;
	}
	public String getFdate() {
		return fdate;
	}
	public void setFdate(String fdate) {
		this.fdate = fdate;
	}
	public int getFstockId() {
		return fstockId;
	}
	public void setFstockId(int fstockId) {
		this.fstockId = fstockId;
	}
	public int getFposterId() {
		return fposterId;
	}
	public void setFposterId(int fposterId) {
		this.fposterId = fposterId;
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
	public int getFfmanagerId() {
		return ffmanagerId;
	}
	public void setFfmanagerId(int ffmanagerId) {
		this.ffmanagerId = ffmanagerId;
	}
	public int getFsmanagerId() {
		return fsmanagerId;
	}
	public void setFsmanagerId(int fsmanagerId) {
		this.fsmanagerId = fsmanagerId;
	}
	public String getFcnnBillNo() {
		return fcnnBillNo;
	}
	public void setFcnnBillNo(String fcnnBillNo) {
		this.fcnnBillNo = fcnnBillNo;
	}
	public int getFclosed() {
		return fclosed;
	}
	public void setFclosed(int fclosed) {
		this.fclosed = fclosed;
	}
	public String getFnote() {
		return fnote;
	}
	public void setFnote(String fnote) {
		this.fnote = fnote;
	}
	public int getFinvoiceClosed() {
		return finvoiceClosed;
	}
	public void setFinvoiceClosed(int finvoiceClosed) {
		this.finvoiceClosed = finvoiceClosed;
	}
	public int getFbclosed() {
		return fbclosed;
	}
	public void setFbclosed(int fbclosed) {
		this.fbclosed = fbclosed;
	}
	public String getFcreateDate() {
		return fcreateDate;
	}
	public void setFcreateDate(String fcreateDate) {
		this.fcreateDate = fcreateDate;
	}
	public String getFcheckDate() {
		return fcheckDate;
	}
	public void setFcheckDate(String fcheckDate) {
		this.fcheckDate = fcheckDate;
	}
	public double getFexchangeRate() {
		return fexchangeRate;
	}
	public void setFexchangeRate(double fexchangeRate) {
		this.fexchangeRate = fexchangeRate;
	}
	public int getFstatus() {
		return fstatus;
	}
	public void setFstatus(int fstatus) {
		this.fstatus = fstatus;
	}
	public int getFcancellation() {
		return fcancellation;
	}
	public void setFcancellation(int fcancellation) {
		this.fcancellation = fcancellation;
	}
	public int getFupStockWhenSave() {
		return fupStockWhenSave;
	}
	public void setFupStockWhenSave(int fupStockWhenSave) {
		this.fupStockWhenSave = fupStockWhenSave;
	}
	public int getFpoStyle() {
		return fpoStyle;
	}
	public void setFpoStyle(int fpoStyle) {
		this.fpoStyle = fpoStyle;
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
	public int getFtranstatus() {
		return ftranstatus;
	}
	public void setFtranstatus(int ftranstatus) {
		this.ftranstatus = ftranstatus;
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
	public int getFbizType() {
		return fbizType;
	}
	public void setFbizType(int fbizType) {
		this.fbizType = fbizType;
	}
	public int getFexchangeratetype() {
		return fexchangeratetype;
	}
	public void setFexchangeratetype(int fexchangeratetype) {
		this.fexchangeratetype = fexchangeratetype;
	}
	public int getFpomode() {
		return fpomode;
	}
	public void setFpomode(int fpomode) {
		this.fpomode = fpomode;
	}
	public Short getFprintcount() {
		return fprintcount;
	}
	public void setFprintcount(Short fprintcount) {
		this.fprintcount = fprintcount;
	}
	public String getFcheckdate() {
		return fcheckdate;
	}
	public void setFcheckdate(String fcheckdate) {
		this.fcheckdate = fcheckdate;
	}
	public String getFpoordbillno() {
		return fpoordbillno;
	}
	public void setFpoordbillno(String fpoordbillno) {
		this.fpoordbillno = fpoordbillno;
	}
	public Supplier getSupplier() {
		return supplier;
	}
	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}
	public Department getDepartment() {
		return department;
	}
	public void setDepartment(Department department) {
		this.department = department;
	}
	public Emp getEmp() {
		return emp;
	}
	public void setEmp(Emp emp) {
		this.emp = emp;
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
	public String getEmpName() {
		return empName;
	}
	public void setEmpName(String empName) {
		this.empName = empName;
	}
	public String getSuppName() {
		return suppName;
	}
	public void setSuppName(String suppName) {
		this.suppName = suppName;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getStockName() {
		return stockName;
	}
	public void setStockName(String stockName) {
		this.stockName = stockName;
	}


}