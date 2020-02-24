package ykk.xc.com.wms.bean.k3Bean;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

import ykk.xc.com.wms.bean.Department;

/**
 * @Description:订单实体
 *
 * @author qxp 2019年2月26日 下午4:33:59
 */
public class SeOrder implements Serializable {

	private String fbrno;/* 公司机构内码 */

	private Integer finterid;/* 订单内码 */

	private String fbillno;/* 编 号 */

	private Integer fcurrencyid;/* 币 别 */

	private Integer fcustid;/* 购货单位 */

	private String fdate;/* 日期 */

	private String fpaystyle;/* 付款方式 */

	private String fpaydate;/* 付款日期 */

	private String ffetchstyle;/* 交货方式 */

	private String ffetchdate;/* 交货日期 */

	private String ffetchadd;/* 交货地点 */

	private Integer fsalestyle;/* 销售方式 */

	private Integer fdeptid;/* 部门 */

	private Integer fempid;/* 业务员 */

	private Integer fcheckerid;/* 审核人 */

	private Integer fbillerid;/* 制单 */

	private String fnote;/* 备注 */

	private int fclosed;/* 是否关闭 */

	private Integer ftrantype;/* 单据类型 */

	private Short finvoiceclosed;/* 发票关闭 */

	private Short fbclosed;/* 已作废 */

	private Integer fmangerid;/* 主管 */

	private Integer fsettleid;/* 结算方式 */

	private Double fexchangerate;/**/

	private Boolean fdiscounttype;/* 汇 率 */

	private Short fstatus;/* 状态 */

	private Boolean fcancellation;/* 作废 */

	private Integer fcurchecklevel;/* 当前审核级别 */

	private Float ftransitaheadtime;/* 运输提前期 */

	private String fpoordbillno;/* 采购订单号 */

	private Integer frelatebrid;/* 订货机构 */

	private Integer fimport;/* 引入标志 */

	private Integer forderaffirm;/* 确认标志 */

	private Integer ftranstatus;/* 传输标志 */

	private String fuuid;/* 唯一标识 */

	private Integer fsystemtype;/* 系统类型 */

	private String fcashdiscount;/* 现金折扣 */

	private String fcheckdate;/* 审核日期 */

	private String fexplanation;/* 摘要 */

	private Date fsettledate;/* 结算日期 */

	private Integer fseltrantype;/* 源单类型 */

	private Integer fchildren;/* 关联标识 */

	private Integer fbrid;/* 制单机构 */

	private Integer fareaps;/* 销售范围 */

	private Integer fclasstypeid;/* 事务类型 */

	private Integer fmanagetype;/* 保税监管类型 */

	private Short fsysstatus;/* 系统设置 */

	private String fversionno;/* 版本号 */

	private Date fchangedate;/* 变更日期 */

	private String fchangecauses;/* 变更原因 */

	private Integer fchangemark;/* 变更标志 */

	private Integer fchangeuser;/* 变更人 */

	private String fvalidatername;/* 确认人 */

	private String fconsignee;/* 收货方 */

	private Short fprintcount;/* 打印次数 */

	private Integer fexchangeratetype;/* 汇率类型 */

	private Integer fcustaddress;/* 客户地点 */

	private Integer fplancategory;/* 计划类别 */

	private Integer fsendstatus;/* 发送标志 */

	private Integer fenterpriseid;/* 发送企业 */
	/**/
	/* 出库单号 */
	private String fheadselfs0154;
	/* 货运单号/物流单号 */
	private String fheadselfs0156;
	/* 物流公司代码 */
	private String fheadselfs0157;
	/* 在生产账套此字段：对方客户Id。其他账套：是否已同步到生产账套。默认‘A’否 */
	private String fheadselfs0158;

	// -----------此5个字段在生产账套存在，其他账套暂不存储 start----------------//
	/* 对方客户代码 */
	private String fheadselfs0159;
	/* 对方客户名称 */
	private String fheadselfs0160;
	/* 对方单据号 */
	private String fheadselfs0161;
	/* 对方制单人Id */
	private String fheadselfs0162;
	/* 对方部门Id */
	private String fheadselfs0163;

	// -----------此5个字段在生产账套存在，其他账套暂不存储 end----------------//

	/**/
	private byte[] foperdate;
	/* 店铺名称 */
	private String storeName;
	/* 物流方式 */
	private String logisticsWay;
	/* 物流编号 */
	private String logisticsNo;
	/* 快递公司编号 */
	private String logisticsComNo;
	/* 部门id */
	private int fdeptId;
	/* 部门名称 */
	private String deptName;
	/* 部门编码 */
	private String deptNumber;
	private Organization cust; // 客户对象
	private Department department; // 部门对象

	public String getFbrno() {
		return fbrno;
	}

	public void setFbrno(String fbrno) {
		this.fbrno = fbrno == null ? null : fbrno.trim();
	}

	public Integer getFinterid() {
		return finterid;
	}

	public void setFinterid(Integer finterid) {
		this.finterid = finterid;
	}

	public String getFbillno() {
		return fbillno;
	}

	public void setFbillno(String fbillno) {
		this.fbillno = fbillno == null ? null : fbillno.trim();
	}

	public Integer getFcurrencyid() {
		return fcurrencyid;
	}

	public void setFcurrencyid(Integer fcurrencyid) {
		this.fcurrencyid = fcurrencyid;
	}

	public Integer getFcustid() {
		return fcustid;
	}

	public void setFcustid(Integer fcustid) {
		this.fcustid = fcustid;
	}

	public String getFdate() {
		return fdate;
	}

	public void setFdate(String fdate) {
		this.fdate = fdate;
	}

	public String getFpaystyle() {
		return fpaystyle;
	}

	public void setFpaystyle(String fpaystyle) {
		this.fpaystyle = fpaystyle == null ? null : fpaystyle.trim();
	}

	public String getFfetchstyle() {
		return ffetchstyle;
	}

	public void setFfetchstyle(String ffetchstyle) {
		this.ffetchstyle = ffetchstyle == null ? null : ffetchstyle.trim();
	}

	public String getFfetchadd() {
		return ffetchadd;
	}

	public void setFfetchadd(String ffetchadd) {
		this.ffetchadd = ffetchadd == null ? null : ffetchadd.trim();
	}

	public Integer getFsalestyle() {
		return fsalestyle;
	}

	public void setFsalestyle(Integer fsalestyle) {
		this.fsalestyle = fsalestyle;
	}

	public Integer getFdeptid() {
		return fdeptid;
	}

	public void setFdeptid(Integer fdeptid) {
		this.fdeptid = fdeptid;
	}

	public Integer getFempid() {
		return fempid;
	}

	public void setFempid(Integer fempid) {
		this.fempid = fempid;
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

	public String getFnote() {
		return fnote;
	}

	public void setFnote(String fnote) {
		this.fnote = fnote == null ? null : fnote.trim();
	}

	public int getFclosed() {
		return fclosed;
	}

	public void setFclosed(int fclosed) {
		this.fclosed = fclosed;
	}

	public Integer getFtrantype() {
		return ftrantype;
	}

	public void setFtrantype(Integer ftrantype) {
		this.ftrantype = ftrantype;
	}

	public Short getFinvoiceclosed() {
		return finvoiceclosed;
	}

	public void setFinvoiceclosed(Short finvoiceclosed) {
		this.finvoiceclosed = finvoiceclosed;
	}

	public Short getFbclosed() {
		return fbclosed;
	}

	public void setFbclosed(Short fbclosed) {
		this.fbclosed = fbclosed;
	}

	public Integer getFmangerid() {
		return fmangerid;
	}

	public void setFmangerid(Integer fmangerid) {
		this.fmangerid = fmangerid;
	}

	public Integer getFsettleid() {
		return fsettleid;
	}

	public void setFsettleid(Integer fsettleid) {
		this.fsettleid = fsettleid;
	}

	public Double getFexchangerate() {
		return fexchangerate;
	}

	public void setFexchangerate(Double fexchangerate) {
		this.fexchangerate = fexchangerate;
	}

	public Boolean getFdiscounttype() {
		return fdiscounttype;
	}

	public void setFdiscounttype(Boolean fdiscounttype) {
		this.fdiscounttype = fdiscounttype;
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

	public Integer getFcurchecklevel() {
		return fcurchecklevel;
	}

	public void setFcurchecklevel(Integer fcurchecklevel) {
		this.fcurchecklevel = fcurchecklevel;
	}

	public Float getFtransitaheadtime() {
		return ftransitaheadtime;
	}

	public void setFtransitaheadtime(Float ftransitaheadtime) {
		this.ftransitaheadtime = ftransitaheadtime;
	}

	public String getFpoordbillno() {
		return fpoordbillno;
	}

	public void setFpoordbillno(String fpoordbillno) {
		this.fpoordbillno = fpoordbillno == null ? null : fpoordbillno.trim();
	}

	public Integer getFrelatebrid() {
		return frelatebrid;
	}

	public void setFrelatebrid(Integer frelatebrid) {
		this.frelatebrid = frelatebrid;
	}

	public Integer getFimport() {
		return fimport;
	}

	public void setFimport(Integer fimport) {
		this.fimport = fimport;
	}

	public Integer getForderaffirm() {
		return forderaffirm;
	}

	public void setForderaffirm(Integer forderaffirm) {
		this.forderaffirm = forderaffirm;
	}

	public Integer getFtranstatus() {
		return ftranstatus;
	}

	public void setFtranstatus(Integer ftranstatus) {
		this.ftranstatus = ftranstatus;
	}

	public String getFuuid() {
		return fuuid;
	}

	public void setFuuid(String fuuid) {
		this.fuuid = fuuid == null ? null : fuuid.trim();
	}

	public Integer getFsystemtype() {
		return fsystemtype;
	}

	public void setFsystemtype(Integer fsystemtype) {
		this.fsystemtype = fsystemtype;
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

	public Date getFsettledate() {
		return fsettledate;
	}

	public void setFsettledate(Date fsettledate) {
		this.fsettledate = fsettledate;
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

	public Integer getFmanagetype() {
		return fmanagetype;
	}

	public void setFmanagetype(Integer fmanagetype) {
		this.fmanagetype = fmanagetype;
	}

	public Short getFsysstatus() {
		return fsysstatus;
	}

	public void setFsysstatus(Short fsysstatus) {
		this.fsysstatus = fsysstatus;
	}

	public String getFversionno() {
		return fversionno;
	}

	public void setFversionno(String fversionno) {
		this.fversionno = fversionno == null ? null : fversionno.trim();
	}

	public Date getFchangedate() {
		return fchangedate;
	}

	public void setFchangedate(Date fchangedate) {
		this.fchangedate = fchangedate;
	}

	public String getFchangecauses() {
		return fchangecauses;
	}

	public void setFchangecauses(String fchangecauses) {
		this.fchangecauses = fchangecauses == null ? null : fchangecauses.trim();
	}

	public Integer getFchangemark() {
		return fchangemark;
	}

	public void setFchangemark(Integer fchangemark) {
		this.fchangemark = fchangemark;
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

	public Integer getFcustaddress() {
		return fcustaddress;
	}

	public void setFcustaddress(Integer fcustaddress) {
		this.fcustaddress = fcustaddress;
	}

	public Integer getFplancategory() {
		return fplancategory;
	}

	public void setFplancategory(Integer fplancategory) {
		this.fplancategory = fplancategory;
	}

	public Integer getFsendstatus() {
		return fsendstatus;
	}

	public void setFsendstatus(Integer fsendstatus) {
		this.fsendstatus = fsendstatus;
	}

	public Integer getFenterpriseid() {
		return fenterpriseid;
	}

	public void setFenterpriseid(Integer fenterpriseid) {
		this.fenterpriseid = fenterpriseid;
	}

	public String getFheadselfs0154() {
		return fheadselfs0154;
	}

	public void setFheadselfs0154(String fheadselfs0154) {
		this.fheadselfs0154 = fheadselfs0154 == null ? null : fheadselfs0154.trim();
	}

	public String getFheadselfs0156() {
		return fheadselfs0156;
	}

	public void setFheadselfs0156(String fheadselfs0156) {
		this.fheadselfs0156 = fheadselfs0156 == null ? null : fheadselfs0156.trim();
	}

	public String getFheadselfs0157() {
		return fheadselfs0157;
	}

	public void setFheadselfs0157(String fheadselfs0157) {
		this.fheadselfs0157 = fheadselfs0157;
	}

	public byte[] getFoperdate() {
		return foperdate;
	}

	public void setFoperdate(byte[] foperdate) {
		this.foperdate = foperdate;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getLogisticsWay() {
		return logisticsWay;
	}

	public void setLogisticsWay(String logisticsWay) {
		this.logisticsWay = logisticsWay;
	}

	public String getLogisticsNo() {
		return logisticsNo;
	}

	public void setLogisticsNo(String logisticsNo) {
		this.logisticsNo = logisticsNo;
	}

	public String getLogisticsComNo() {
		return logisticsComNo;
	}

	public void setLogisticsComNo(String logisticsComNo) {
		this.logisticsComNo = logisticsComNo;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getDeptNumber() {
		return deptNumber;
	}

	public void setDeptNumber(String deptNumber) {
		this.deptNumber = deptNumber;
	}

	public String getFheadselfs0158() {
		return fheadselfs0158;
	}

	public void setFheadselfs0158(String fheadselfs0158) {
		this.fheadselfs0158 = fheadselfs0158;
	}

	public String getFheadselfs0159() {
		return fheadselfs0159;
	}

	public void setFheadselfs0159(String fheadselfs0159) {
		this.fheadselfs0159 = fheadselfs0159;
	}

	public String getFheadselfs0160() {
		return fheadselfs0160;
	}

	public void setFheadselfs0160(String fheadselfs0160) {
		this.fheadselfs0160 = fheadselfs0160;
	}

	public String getFheadselfs0161() {
		return fheadselfs0161;
	}

	public void setFheadselfs0161(String fheadselfs0161) {
		this.fheadselfs0161 = fheadselfs0161;
	}

	public String getFheadselfs0162() {
		return fheadselfs0162;
	}

	public void setFheadselfs0162(String fheadselfs0162) {
		this.fheadselfs0162 = fheadselfs0162;
	}

	public String getFheadselfs0163() {
		return fheadselfs0163;
	}

	public void setFheadselfs0163(String fheadselfs0163) {
		this.fheadselfs0163 = fheadselfs0163;
	}

	public int getFdeptId() {
		return fdeptId;
	}

	public void setFdeptId(int fdeptId) {
		this.fdeptId = fdeptId;
	}

	public Organization getCust() {
		return cust;
	}

	public Department getDepartment() {
		return department;
	}

	public void setCust(Organization cust) {
		this.cust = cust;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	@Override
	public String toString() {
		return "SeOrder [fbrno=" + fbrno + ", finterid=" + finterid + ", fbillno=" + fbillno + ", fcurrencyid="
				+ fcurrencyid + ", fcustid=" + fcustid + ", fdate=" + fdate + ", fpaystyle=" + fpaystyle + ", fpaydate="
				+ fpaydate + ", ffetchstyle=" + ffetchstyle + ", ffetchdate=" + ffetchdate + ", ffetchadd=" + ffetchadd
				+ ", fsalestyle=" + fsalestyle + ", fdeptid=" + fdeptid + ", fempid=" + fempid + ", fcheckerid="
				+ fcheckerid + ", fbillerid=" + fbillerid + ", fnote=" + fnote + ", fclosed=" + fclosed + ", ftrantype="
				+ ftrantype + ", finvoiceclosed=" + finvoiceclosed + ", fbclosed=" + fbclosed + ", fmangerid="
				+ fmangerid + ", fsettleid=" + fsettleid + ", fexchangerate=" + fexchangerate + ", fdiscounttype="
				+ fdiscounttype + ", fstatus=" + fstatus + ", fcancellation=" + fcancellation + ", fcurchecklevel="
				+ fcurchecklevel + ", ftransitaheadtime=" + ftransitaheadtime + ", fpoordbillno=" + fpoordbillno
				+ ", frelatebrid=" + frelatebrid + ", fimport=" + fimport + ", forderaffirm=" + forderaffirm
				+ ", ftranstatus=" + ftranstatus + ", fuuid=" + fuuid + ", fsystemtype=" + fsystemtype
				+ ", fcashdiscount=" + fcashdiscount + ", fcheckdate=" + fcheckdate + ", fexplanation=" + fexplanation
				+ ", fsettledate=" + fsettledate + ", fseltrantype=" + fseltrantype + ", fchildren=" + fchildren
				+ ", fbrid=" + fbrid + ", fareaps=" + fareaps + ", fclasstypeid=" + fclasstypeid + ", fmanagetype="
				+ fmanagetype + ", fsysstatus=" + fsysstatus + ", fversionno=" + fversionno + ", fchangedate="
				+ fchangedate + ", fchangecauses=" + fchangecauses + ", fchangemark=" + fchangemark + ", fchangeuser="
				+ fchangeuser + ", fvalidatername=" + fvalidatername + ", fconsignee=" + fconsignee + ", fprintcount="
				+ fprintcount + ", fexchangeratetype=" + fexchangeratetype + ", fcustaddress=" + fcustaddress
				+ ", fplancategory=" + fplancategory + ", fsendstatus=" + fsendstatus + ", fenterpriseid="
				+ fenterpriseid + ", fheadselfs0154=" + fheadselfs0154 + ", fheadselfs0156=" + fheadselfs0156
				+ ", fheadselfs0157=" + fheadselfs0157 + ", fheadselfs0158=" + fheadselfs0158 + ", fheadselfs0159="
				+ fheadselfs0159 + ", fheadselfs0160=" + fheadselfs0160 + ", fheadselfs0161=" + fheadselfs0161
				+ ", fheadselfs0162=" + fheadselfs0162 + ", fheadselfs0163=" + fheadselfs0163 + ", foperdate="
				+ Arrays.toString(foperdate) + ", storeName=" + storeName + ", logisticsWay=" + logisticsWay
				+ ", logisticsNo=" + logisticsNo + ", logisticsComNo=" + logisticsComNo + ", fdeptId=" + fdeptId
				+ ", deptName=" + deptName + ", deptNumber=" + deptNumber + ", cust=" + cust + ", department="
				+ department + "]";
	}

}