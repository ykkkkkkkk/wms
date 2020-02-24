package ykk.xc.com.wms.bean.k3Bean;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description:订单详细实体
 *
 * @author qxp 2019年2月26日 下午4:34:06
 */
public class SeOrderEntry implements Serializable {
	private Integer fdetailid;/**/

	private String fbrno;/* 公司机构内码 */

	private Integer finterid;/* 订单内码 */

	private Integer fentryid;/* 分录号 */

	private Integer fitemid;/* 产品代码 */

	private Double fqty;/* 基本单位数量 */

	private Double fcommitqty;/* 发货数量 */

	private Double fprice;/* 单价 */

	private Double famount;/* 金额 */

	private Double ftaxrate;/* 折扣率(%) */

	private Double ftaxamount;/* 折扣额 */

	private Double ftax;/* 税金（本位币） */

	private Float fdiscount;/* 折扣 */

	private String fnote;/* 备注 */

	private Date fdate;/* 交货日期 */

	private Double fdiscountamount;/* 折扣金额 */

	private Double finvoiceqty;/* 发票数量 */

	private Double fbcommitqty;/* 退货数量 */

	private Integer ftranleadtime;/* 运输提前期 */

	private Integer fatpdeduct;/* 冲减标志 */

	private Integer fcostobjectid;/* 成本对象代码 */

	private Integer funitid;/* 单位 */

	private Double fauxbcommitqty;/* 辅助退货数量 */

	private Double fauxcommitqty;/* 辅助发货数量 */

	private Double fauxinvoiceqty;/* 辅助开票数量 */

	private Double fauxprice;/* 实际含税单价 */

	private Double fauxqty;/* 数量 */

	private Double funidiscount;/* 单位折扣额 */

	private Double ffinalamount;/* 折后金额 */

	private Integer fsourceentryid;/* 源单行号 */

	private Integer fhavemrp;/* 是否参加过MRP计算 */

	private Double fstockqty;/* 出库数量 */

	private Double fauxstockqty;/* 出库数量(辅助单位): */

	private String fbatchno;/* 物料批号 */

	private Double fcess;/* 税率(%) */

	private Date fadviceconsigndate;/* 建议交货日期 */

	private Integer fbominterid;/* 批号/客户BOM */

	private String fmapnumber;/* 对应代码 */

	private String fmapname;/* 对应名称 */

	private Integer flockflag;/* 锁库标志 */

	private Integer finforecast;/* 是否预测内 */

	private Double fallstdamount;/* 价税合计(本位币) */

	private Integer fauxpropid;/* 辅助属性 */

	private Double fauxpricediscount;/* 实际含税单价 */

	private Double ftaxamt;/* 销项税额 */

	private Integer fsourcetrantype;/* 源单类型 */

	private Integer fsourceinterid;/* 源单内码 */

	private String fsourcebillno;/* 源单单号 */

	private Integer fcontractinterid;/* 合同内码 */

	private Integer fcontractentryid;/* 合同分录 */

	private String fcontractbillno;/* 合同单号 */

	private Double fcommitinstall;/* 基本单位组装数量 */

	private Double fauxcommitinstall;/* 组装数量 */

	private Double fseccommitinstall;/* 辅助单位组装数量 */

	private Integer fplanmode;/* 计划模式 */

	private String fmtono;/* 计划跟踪号 */

	private String forderbillno;/* 客户订单号 */

	private Integer forderentryid;/* 订单行号 */

	private Integer fbomcategory;/* BOM类別 */

	private Integer forderbomstatus;/* 订单BOM状态 */

	private Integer forderbominterid;/* 订单BOM内码 */

	private Integer foutsourceinterid;/* 发送内码 */

	private Integer foutsourceentryid;/* 发送分录 */

	private Integer foutsourcetrantype;/* 发送类型 */

	/* 物流名称 不存库 */
	private String FName;
	/* 物流代码 不存库 */
	private String FNumber;
	/* 表头FbillNo */
	private String fbillNo;
	/* 采购订单内码，由其他账套采购订单下推生产账套销售订单存在 */
	private String FEntrySelfS0168;
	/* 采购订单号 */
	private String FEntrySelfS0169;
	/* 采购订单分录码 */
	private String FEntrySelfS0170;
	/* 销售订单号 */
	private String FEntrySelfS0171;
	/* 销售订单内码 */
	private String FEntrySelfS0172;
	/* 销售订单分录码 */
	private String FEntrySelfS0173;
	private SeOrder seOrder;
	private ICItem icItem;

	// 临时字段，不存表
	private double useableQty; // 可用数
	private double realQty; // 实际数
	private int isCheck; // 是否选中
	private String unitName;

	public SeOrderEntry() {
		super();
	}

	public Integer getFdetailid() {
		return fdetailid;
	}

	public void setFdetailid(Integer fdetailid) {
		this.fdetailid = fdetailid;
	}

	public String getFbrno() {
		return fbrno;
	}

	public void setFbrno(String fbrno) {
		this.fbrno = fbrno;
	}

	public Integer getFinterid() {
		return finterid;
	}

	public void setFinterid(Integer finterid) {
		this.finterid = finterid;
	}

	public Integer getFentryid() {
		return fentryid;
	}

	public void setFentryid(Integer fentryid) {
		this.fentryid = fentryid;
	}

	public Integer getFitemid() {
		return fitemid;
	}

	public void setFitemid(Integer fitemid) {
		this.fitemid = fitemid;
	}

	public Double getFqty() {
		return fqty;
	}

	public void setFqty(Double fqty) {
		this.fqty = fqty;
	}

	public Double getFcommitqty() {
		return fcommitqty;
	}

	public void setFcommitqty(Double fcommitqty) {
		this.fcommitqty = fcommitqty;
	}

	public Double getFprice() {
		return fprice;
	}

	public void setFprice(Double fprice) {
		this.fprice = fprice;
	}

	public Double getFamount() {
		return famount;
	}

	public void setFamount(Double famount) {
		this.famount = famount;
	}

	public Double getFtaxrate() {
		return ftaxrate;
	}

	public void setFtaxrate(Double ftaxrate) {
		this.ftaxrate = ftaxrate;
	}

	public Double getFtaxamount() {
		return ftaxamount;
	}

	public void setFtaxamount(Double ftaxamount) {
		this.ftaxamount = ftaxamount;
	}

	public Double getFtax() {
		return ftax;
	}

	public void setFtax(Double ftax) {
		this.ftax = ftax;
	}

	public Float getFdiscount() {
		return fdiscount;
	}

	public void setFdiscount(Float fdiscount) {
		this.fdiscount = fdiscount;
	}

	public String getFnote() {
		return fnote;
	}

	public void setFnote(String fnote) {
		this.fnote = fnote;
	}

	public Date getFdate() {
		return fdate;
	}

	public void setFdate(Date fdate) {
		this.fdate = fdate;
	}

	public Double getFdiscountamount() {
		return fdiscountamount;
	}

	public void setFdiscountamount(Double fdiscountamount) {
		this.fdiscountamount = fdiscountamount;
	}

	public Double getFinvoiceqty() {
		return finvoiceqty;
	}

	public void setFinvoiceqty(Double finvoiceqty) {
		this.finvoiceqty = finvoiceqty;
	}

	public Double getFbcommitqty() {
		return fbcommitqty;
	}

	public void setFbcommitqty(Double fbcommitqty) {
		this.fbcommitqty = fbcommitqty;
	}

	public Integer getFtranleadtime() {
		return ftranleadtime;
	}

	public void setFtranleadtime(Integer ftranleadtime) {
		this.ftranleadtime = ftranleadtime;
	}

	public Integer getFatpdeduct() {
		return fatpdeduct;
	}

	public void setFatpdeduct(Integer fatpdeduct) {
		this.fatpdeduct = fatpdeduct;
	}

	public Integer getFcostobjectid() {
		return fcostobjectid;
	}

	public void setFcostobjectid(Integer fcostobjectid) {
		this.fcostobjectid = fcostobjectid;
	}

	public Integer getFunitid() {
		return funitid;
	}

	public void setFunitid(Integer funitid) {
		this.funitid = funitid;
	}

	public Double getFauxbcommitqty() {
		return fauxbcommitqty;
	}

	public void setFauxbcommitqty(Double fauxbcommitqty) {
		this.fauxbcommitqty = fauxbcommitqty;
	}

	public Double getFauxcommitqty() {
		return fauxcommitqty;
	}

	public void setFauxcommitqty(Double fauxcommitqty) {
		this.fauxcommitqty = fauxcommitqty;
	}

	public Double getFauxinvoiceqty() {
		return fauxinvoiceqty;
	}

	public void setFauxinvoiceqty(Double fauxinvoiceqty) {
		this.fauxinvoiceqty = fauxinvoiceqty;
	}

	public Double getFauxprice() {
		return fauxprice;
	}

	public void setFauxprice(Double fauxprice) {
		this.fauxprice = fauxprice;
	}

	public Double getFauxqty() {
		return fauxqty;
	}

	public void setFauxqty(Double fauxqty) {
		this.fauxqty = fauxqty;
	}

	public Double getFunidiscount() {
		return funidiscount;
	}

	public void setFunidiscount(Double funidiscount) {
		this.funidiscount = funidiscount;
	}

	public Double getFfinalamount() {
		return ffinalamount;
	}

	public void setFfinalamount(Double ffinalamount) {
		this.ffinalamount = ffinalamount;
	}

	public Integer getFsourceentryid() {
		return fsourceentryid;
	}

	public void setFsourceentryid(Integer fsourceentryid) {
		this.fsourceentryid = fsourceentryid;
	}

	public Integer getFhavemrp() {
		return fhavemrp;
	}

	public void setFhavemrp(Integer fhavemrp) {
		this.fhavemrp = fhavemrp;
	}

	public Double getFstockqty() {
		return fstockqty;
	}

	public void setFstockqty(Double fstockqty) {
		this.fstockqty = fstockqty;
	}

	public Double getFauxstockqty() {
		return fauxstockqty;
	}

	public void setFauxstockqty(Double fauxstockqty) {
		this.fauxstockqty = fauxstockqty;
	}

	public String getFbatchno() {
		return fbatchno;
	}

	public void setFbatchno(String fbatchno) {
		this.fbatchno = fbatchno;
	}

	public Double getFcess() {
		return fcess;
	}

	public void setFcess(Double fcess) {
		this.fcess = fcess;
	}

	public Date getFadviceconsigndate() {
		return fadviceconsigndate;
	}

	public void setFadviceconsigndate(Date fadviceconsigndate) {
		this.fadviceconsigndate = fadviceconsigndate;
	}

	public Integer getFbominterid() {
		return fbominterid;
	}

	public void setFbominterid(Integer fbominterid) {
		this.fbominterid = fbominterid;
	}

	public String getFmapnumber() {
		return fmapnumber;
	}

	public void setFmapnumber(String fmapnumber) {
		this.fmapnumber = fmapnumber;
	}

	public String getFmapname() {
		return fmapname;
	}

	public void setFmapname(String fmapname) {
		this.fmapname = fmapname;
	}

	public Integer getFlockflag() {
		return flockflag;
	}

	public void setFlockflag(Integer flockflag) {
		this.flockflag = flockflag;
	}

	public Integer getFinforecast() {
		return finforecast;
	}

	public void setFinforecast(Integer finforecast) {
		this.finforecast = finforecast;
	}

	public Double getFallstdamount() {
		return fallstdamount;
	}

	public void setFallstdamount(Double fallstdamount) {
		this.fallstdamount = fallstdamount;
	}

	public Integer getFauxpropid() {
		return fauxpropid;
	}

	public void setFauxpropid(Integer fauxpropid) {
		this.fauxpropid = fauxpropid;
	}

	public Double getFauxpricediscount() {
		return fauxpricediscount;
	}

	public void setFauxpricediscount(Double fauxpricediscount) {
		this.fauxpricediscount = fauxpricediscount;
	}

	public Double getFtaxamt() {
		return ftaxamt;
	}

	public void setFtaxamt(Double ftaxamt) {
		this.ftaxamt = ftaxamt;
	}

	public Integer getFsourcetrantype() {
		return fsourcetrantype;
	}

	public void setFsourcetrantype(Integer fsourcetrantype) {
		this.fsourcetrantype = fsourcetrantype;
	}

	public Integer getFsourceinterid() {
		return fsourceinterid;
	}

	public void setFsourceinterid(Integer fsourceinterid) {
		this.fsourceinterid = fsourceinterid;
	}

	public String getFsourcebillno() {
		return fsourcebillno;
	}

	public void setFsourcebillno(String fsourcebillno) {
		this.fsourcebillno = fsourcebillno;
	}

	public Integer getFcontractinterid() {
		return fcontractinterid;
	}

	public void setFcontractinterid(Integer fcontractinterid) {
		this.fcontractinterid = fcontractinterid;
	}

	public Integer getFcontractentryid() {
		return fcontractentryid;
	}

	public void setFcontractentryid(Integer fcontractentryid) {
		this.fcontractentryid = fcontractentryid;
	}

	public String getFcontractbillno() {
		return fcontractbillno;
	}

	public void setFcontractbillno(String fcontractbillno) {
		this.fcontractbillno = fcontractbillno;
	}

	public Double getFcommitinstall() {
		return fcommitinstall;
	}

	public void setFcommitinstall(Double fcommitinstall) {
		this.fcommitinstall = fcommitinstall;
	}

	public Double getFauxcommitinstall() {
		return fauxcommitinstall;
	}

	public void setFauxcommitinstall(Double fauxcommitinstall) {
		this.fauxcommitinstall = fauxcommitinstall;
	}

	public Double getFseccommitinstall() {
		return fseccommitinstall;
	}

	public void setFseccommitinstall(Double fseccommitinstall) {
		this.fseccommitinstall = fseccommitinstall;
	}

	public Integer getFplanmode() {
		return fplanmode;
	}

	public void setFplanmode(Integer fplanmode) {
		this.fplanmode = fplanmode;
	}

	public String getFmtono() {
		return fmtono;
	}

	public void setFmtono(String fmtono) {
		this.fmtono = fmtono;
	}

	public String getForderbillno() {
		return forderbillno;
	}

	public void setForderbillno(String forderbillno) {
		this.forderbillno = forderbillno;
	}

	public Integer getForderentryid() {
		return forderentryid;
	}

	public void setForderentryid(Integer forderentryid) {
		this.forderentryid = forderentryid;
	}

	public Integer getFbomcategory() {
		return fbomcategory;
	}

	public void setFbomcategory(Integer fbomcategory) {
		this.fbomcategory = fbomcategory;
	}

	public Integer getForderbomstatus() {
		return forderbomstatus;
	}

	public void setForderbomstatus(Integer forderbomstatus) {
		this.forderbomstatus = forderbomstatus;
	}

	public Integer getForderbominterid() {
		return forderbominterid;
	}

	public void setForderbominterid(Integer forderbominterid) {
		this.forderbominterid = forderbominterid;
	}

	public Integer getFoutsourceinterid() {
		return foutsourceinterid;
	}

	public void setFoutsourceinterid(Integer foutsourceinterid) {
		this.foutsourceinterid = foutsourceinterid;
	}

	public Integer getFoutsourceentryid() {
		return foutsourceentryid;
	}

	public void setFoutsourceentryid(Integer foutsourceentryid) {
		this.foutsourceentryid = foutsourceentryid;
	}

	public Integer getFoutsourcetrantype() {
		return foutsourcetrantype;
	}

	public void setFoutsourcetrantype(Integer foutsourcetrantype) {
		this.foutsourcetrantype = foutsourcetrantype;
	}

	public String getFName() {
		return FName;
	}

	public void setFName(String fName) {
		FName = fName;
	}

	public String getFNumber() {
		return FNumber;
	}

	public void setFNumber(String fNumber) {
		FNumber = fNumber;
	}

	public String getFbillNo() {
		return fbillNo;
	}

	public void setFbillNo(String fbillNo) {
		this.fbillNo = fbillNo;
	}

	public String getFEntrySelfS0168() {
		return FEntrySelfS0168;
	}

	public void setFEntrySelfS0168(String fEntrySelfS0168) {
		FEntrySelfS0168 = fEntrySelfS0168;
	}

	public String getFEntrySelfS0169() {
		return FEntrySelfS0169;
	}

	public void setFEntrySelfS0169(String fEntrySelfS0169) {
		FEntrySelfS0169 = fEntrySelfS0169;
	}

	public String getFEntrySelfS0170() {
		return FEntrySelfS0170;
	}

	public void setFEntrySelfS0170(String fEntrySelfS0170) {
		FEntrySelfS0170 = fEntrySelfS0170;
	}

	public String getFEntrySelfS0171() {
		return FEntrySelfS0171;
	}

	public void setFEntrySelfS0171(String fEntrySelfS0171) {
		FEntrySelfS0171 = fEntrySelfS0171;
	}

	public String getFEntrySelfS0172() {
		return FEntrySelfS0172;
	}

	public void setFEntrySelfS0172(String fEntrySelfS0172) {
		FEntrySelfS0172 = fEntrySelfS0172;
	}

	public String getFEntrySelfS0173() {
		return FEntrySelfS0173;
	}

	public void setFEntrySelfS0173(String fEntrySelfS0173) {
		FEntrySelfS0173 = fEntrySelfS0173;
	}

	public SeOrder getSeOrder() {
		return seOrder;
	}

	public void setSeOrder(SeOrder seOrder) {
		this.seOrder = seOrder;
	}

	public ICItem getIcItem() {
		return icItem;
	}

	public void setIcItem(ICItem icItem) {
		this.icItem = icItem;
	}

	public double getUseableQty() {
		return useableQty;
	}

	public void setUseableQty(double useableQty) {
		this.useableQty = useableQty;
	}

	public double getRealQty() {
		return realQty;
	}

	public void setRealQty(double realQty) {
		this.realQty = realQty;
	}

	public int getIsCheck() {
		return isCheck;
	}

	public void setIsCheck(int isCheck) {
		this.isCheck = isCheck;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}



}