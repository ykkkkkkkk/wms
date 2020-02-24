package ykk.xc.com.wms.bean.pur;


import java.io.Serializable;

import ykk.xc.com.wms.bean.k3Bean.ICItem;

/**
 * @Description:采购订单分录
 *
 * @author qxp 2019年3月13日 下午2:45:54
 */
public class POOrderEntry implements Serializable {
	/* 分录内码 */
	private Integer fdetailid;
	/* 采购订单内码 */
	private Integer finterid;
	/* 分录号 */
	private Integer fentryid;
	/* 物料内码 */
	private Integer fitemid;
	/* 订货数量 */
	private double fqty;
	/* 到货数量 */
	private double fcommitqty;
	/* 交货日期 */
	private String fdate;
	/* 单价 */
	private double fprice;
	/* 金额 */
	private double famount;
	/* 折扣率 */
	private double ftaxrate;
	/* 税金（本位币） */
	private double ftax;
	/* 税额 */
	private double ftaxamount;
	/* 备注 */
	private String fnote;
	/* 单位内码 */
	private Integer funitid;
	/* 辅助到货数量 */
	private double fauxcommitqty;
	/* 辅助单价 */
	private double fauxprice;
	/* 辅助订货数量 */
	private double fauxqty;
	/* 原分录号 */
	private Integer fsourceentryid;
	/* 税率 */
	private double fcess;
	/* 入库数量 */
	private double fstockqty;
	/* 辅助入库数量 */
	private double fauxstockqty;
	/* 对应代码 */
	private String fmapnumber;
	/* 对应名称 */
	private String fmapname;
	/* 价税合计 */
	private double fallamount;
	/* 辅助属性 */
	private Integer fauxpropid;
	/* 实际含税单价 */
	private double fauxpricediscount;
	/* 实际含税单价（本位币） */
	private double fpricediscount;
	/* 基本单位开票数量 */
	private double fqtyinvoice;
	/* 基本计量单位开票数量 */
	private double fqtyinvoicebase;
	/* 含税单价 */
	private double fauxtaxprice;
	/* 含税单价（本位币） */
	private double ftaxprice;
	/* 换算率 */
	private double fseccoefficient;
	/* 辅助数量 */
	private double fsecqty;
	/* 辅助执行数量 */
	private double fseccommitqty;
	/* 源单类型 */
	private Integer fsourcetrantype;
	/* 源单内码 */
	private Integer fsourceinterid;
	/* 源单单号 */
	private String fsourcebillno;
	/* 合同内码 */
	private Integer fcontractinterid;
	/* 合同分录 */
	private Integer fcontractentryid;
	/* 合同单号 */
	private String fcontractbillno;
	/* 锁单标记 */
	private Integer fmrplockflag;
	/* 开票数量 */
	private double fauxqtyinvoice;
	/* 行业务关闭标志 */
	private Integer fmrpclosed;
	/* 对应代码 */
	private Integer fmapid;
	/* 源产地 */
	private Integer fsproducingareaid;
	/* 折扣额 */
	private double famtdiscount;
	/* 累计核销金额(本位币) */
	private double fcheckamount;
	/* MRP自动关闭标志 */
	private Integer fmrpautoclosed;
	/* 辅助单位入库数量 */
	private double fsecstockqty;
	/* 辅助单位开票数量 */
	private double fsecinvoiceqty;
	/* 计划模式 */
	private Integer fplanmode;
	/* 计划跟踪号 */
	private String fmtono;
	/* 检验方式 */
	private Integer fcheckmethod;
	/* 接收物料名称 */
	private String FName;
	/* 接收物料代码 */
	private String FNumber;
	/* 表头FbillNO */
	private String fbillno;

	// 采购主表对象
	private POOrder poOrder;
	private ICItem icItem;

	// 临时字段，不存表
	private double useableQty; // 可用数
	private double realQty; // 实际数
	private int isCheck; // 是否选中

	/* 临时字段，单位名称 */
	private String unitName;

	public POOrderEntry() {
	}

	public Integer getFdetailid() {
		return fdetailid;
	}

	public void setFdetailid(Integer fdetailid) {
		this.fdetailid = fdetailid;
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

	public double getFqty() {
		return fqty;
	}

	public void setFqty(double fqty) {
		this.fqty = fqty;
	}

	public double getFcommitqty() {
		return fcommitqty;
	}

	public void setFcommitqty(double fcommitqty) {
		this.fcommitqty = fcommitqty;
	}

	public String getFdate() {
		return fdate;
	}

	public void setFdate(String fdate) {
		this.fdate = fdate;
	}

	public double getFprice() {
		return fprice;
	}

	public void setFprice(double fprice) {
		this.fprice = fprice;
	}

	public double getFamount() {
		return famount;
	}

	public void setFamount(double famount) {
		this.famount = famount;
	}

	public double getFtaxrate() {
		return ftaxrate;
	}

	public void setFtaxrate(double ftaxrate) {
		this.ftaxrate = ftaxrate;
	}

	public double getFtax() {
		return ftax;
	}

	public void setFtax(double ftax) {
		this.ftax = ftax;
	}

	public double getFtaxamount() {
		return ftaxamount;
	}

	public void setFtaxamount(double ftaxamount) {
		this.ftaxamount = ftaxamount;
	}

	public String getFnote() {
		return fnote;
	}

	public void setFnote(String fnote) {
		this.fnote = fnote;
	}

	public Integer getFunitid() {
		return funitid;
	}

	public void setFunitid(Integer funitid) {
		this.funitid = funitid;
	}

	public double getFauxcommitqty() {
		return fauxcommitqty;
	}

	public void setFauxcommitqty(double fauxcommitqty) {
		this.fauxcommitqty = fauxcommitqty;
	}

	public double getFauxprice() {
		return fauxprice;
	}

	public void setFauxprice(double fauxprice) {
		this.fauxprice = fauxprice;
	}

	public double getFauxqty() {
		return fauxqty;
	}

	public void setFauxqty(double fauxqty) {
		this.fauxqty = fauxqty;
	}

	public Integer getFsourceentryid() {
		return fsourceentryid;
	}

	public void setFsourceentryid(Integer fsourceentryid) {
		this.fsourceentryid = fsourceentryid;
	}

	public double getFcess() {
		return fcess;
	}

	public void setFcess(double fcess) {
		this.fcess = fcess;
	}

	public double getFstockqty() {
		return fstockqty;
	}

	public void setFstockqty(double fstockqty) {
		this.fstockqty = fstockqty;
	}

	public double getFauxstockqty() {
		return fauxstockqty;
	}

	public void setFauxstockqty(double fauxstockqty) {
		this.fauxstockqty = fauxstockqty;
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

	public double getFallamount() {
		return fallamount;
	}

	public void setFallamount(double fallamount) {
		this.fallamount = fallamount;
	}

	public Integer getFauxpropid() {
		return fauxpropid;
	}

	public void setFauxpropid(Integer fauxpropid) {
		this.fauxpropid = fauxpropid;
	}

	public double getFauxpricediscount() {
		return fauxpricediscount;
	}

	public void setFauxpricediscount(double fauxpricediscount) {
		this.fauxpricediscount = fauxpricediscount;
	}

	public double getFpricediscount() {
		return fpricediscount;
	}

	public void setFpricediscount(double fpricediscount) {
		this.fpricediscount = fpricediscount;
	}

	public double getFqtyinvoice() {
		return fqtyinvoice;
	}

	public void setFqtyinvoice(double fqtyinvoice) {
		this.fqtyinvoice = fqtyinvoice;
	}

	public double getFqtyinvoicebase() {
		return fqtyinvoicebase;
	}

	public void setFqtyinvoicebase(double fqtyinvoicebase) {
		this.fqtyinvoicebase = fqtyinvoicebase;
	}

	public double getFauxtaxprice() {
		return fauxtaxprice;
	}

	public void setFauxtaxprice(double fauxtaxprice) {
		this.fauxtaxprice = fauxtaxprice;
	}

	public double getFtaxprice() {
		return ftaxprice;
	}

	public void setFtaxprice(double ftaxprice) {
		this.ftaxprice = ftaxprice;
	}

	public double getFseccoefficient() {
		return fseccoefficient;
	}

	public void setFseccoefficient(double fseccoefficient) {
		this.fseccoefficient = fseccoefficient;
	}

	public double getFsecqty() {
		return fsecqty;
	}

	public void setFsecqty(double fsecqty) {
		this.fsecqty = fsecqty;
	}

	public double getFseccommitqty() {
		return fseccommitqty;
	}

	public void setFseccommitqty(double fseccommitqty) {
		this.fseccommitqty = fseccommitqty;
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

	public Integer getFmrplockflag() {
		return fmrplockflag;
	}

	public void setFmrplockflag(Integer fmrplockflag) {
		this.fmrplockflag = fmrplockflag;
	}

	public double getFauxqtyinvoice() {
		return fauxqtyinvoice;
	}

	public void setFauxqtyinvoice(double fauxqtyinvoice) {
		this.fauxqtyinvoice = fauxqtyinvoice;
	}

	public Integer getFmrpclosed() {
		return fmrpclosed;
	}

	public void setFmrpclosed(Integer fmrpclosed) {
		this.fmrpclosed = fmrpclosed;
	}

	public Integer getFmapid() {
		return fmapid;
	}

	public void setFmapid(Integer fmapid) {
		this.fmapid = fmapid;
	}

	public Integer getFsproducingareaid() {
		return fsproducingareaid;
	}

	public void setFsproducingareaid(Integer fsproducingareaid) {
		this.fsproducingareaid = fsproducingareaid;
	}

	public double getFamtdiscount() {
		return famtdiscount;
	}

	public void setFamtdiscount(double famtdiscount) {
		this.famtdiscount = famtdiscount;
	}

	public double getFcheckamount() {
		return fcheckamount;
	}

	public void setFcheckamount(double fcheckamount) {
		this.fcheckamount = fcheckamount;
	}

	public Integer getFmrpautoclosed() {
		return fmrpautoclosed;
	}

	public void setFmrpautoclosed(Integer fmrpautoclosed) {
		this.fmrpautoclosed = fmrpautoclosed;
	}

	public double getFsecstockqty() {
		return fsecstockqty;
	}

	public void setFsecstockqty(double fsecstockqty) {
		this.fsecstockqty = fsecstockqty;
	}

	public double getFsecinvoiceqty() {
		return fsecinvoiceqty;
	}

	public void setFsecinvoiceqty(double fsecinvoiceqty) {
		this.fsecinvoiceqty = fsecinvoiceqty;
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

	public Integer getFcheckmethod() {
		return fcheckmethod;
	}

	public void setFcheckmethod(Integer fcheckmethod) {
		this.fcheckmethod = fcheckmethod;
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

	public String getFbillno() {
		return fbillno;
	}

	public void setFbillno(String fbillno) {
		this.fbillno = fbillno;
	}

	public POOrder getPoOrder() {
		return poOrder;
	}

	public void setPoOrder(POOrder poOrder) {
		this.poOrder = poOrder;
	}

	public double getUseableQty() {
		return useableQty;
	}

	public int getIsCheck() {
		return isCheck;
	}

	public void setUseableQty(double useableQty) {
		this.useableQty = useableQty;
	}

	public void setIsCheck(int isCheck) {
		this.isCheck = isCheck;
	}

	public ICItem getIcItem() {
		return icItem;
	}

	public void setIcItem(ICItem icItem) {
		this.icItem = icItem;
	}

	public double getRealQty() {
		return realQty;
	}

	public void setRealQty(double realQty) {
		this.realQty = realQty;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

}