package ykk.xc.com.wms.bean.k3Bean;

import java.io.Serializable;

import ykk.xc.com.wms.bean.Stock;

/**
 * @Description:发货通知单表体
 *
 * @author 2019年5月10日 下午5:11:31
 */
public class SeoutStockEntry implements Serializable {
	private int fdetailid; // 唯一行号
	private String fbrno;/* 公司机构内码 */

	private int finterid;/* 通知单内码 */

	private int fentryid;/* 分录号 */

	private int fitemid;/* 产品代码 */

	private double fqty;/* 基本单位数量 */

	private double fcommitqty;/* 发货数量 */

	private double fprice;/* 单价 */

	private double famount;/* 金额 */

	private String forderinterid;/* 销售订单 */

	private String fdate;/* 日期 */

	private String fnote;/* 备注 */

	private double finvoiceqty;/* 开票数量 */

	private double fbcommitqty;/* 退货数量 */

	private int funitid;/* 单位 */

	private double fauxbcommitqty;/* 辅助退货数量 */

	private double fauxcommitqty;/* 辅助发货数量 */

	private double fauxinvoiceqty;/* 辅助开票数量 */

	private double fauxprice;/* 单价 */

	private double fauxqty;/* 数量 */

	private int fsourceentryid;/* 源单行号 */

	private String fmapnumber;/* 对应代码 */

	private String fmapname;/* 对应名称 */

	private int fauxpropid;/* 辅助属性 */

	private String fbatchno;/* 批号 */

	private String fcheckdate;/* 审核日期 */

	private String fexplanation;/* 摘要 */

	private String ffetchadd;/* 交货地点 */

	private String ffetchdate;/* 交货日期 */

	private double fseccoefficient;/* 换算率 */

	private double fsecqty;/* 辅助数量 */

	private double fseccommitqty;/* 辅助执行数量 */

	private int fsourcetrantype;/* 源单类型 */

	private int fsourceinterid;/* 源单内码 */

	private String fsourcebillno;/* 源单单号 */

	private int fcontractinterid;/* 合同内码 */

	private int fcontractentryid;/* 合同分录 */

	private String fcontractbillno;/* 合同单号 */

	private int forderentryid;/* 订单分录 */

	private String forderbillno;/* 订单单号 */

	private int fstockid;/* 仓库 */

	private int fplanmode;/* 计划模式 */

	private String fmtono;/* 计划跟踪号 */

	private double fstockqty;/* 基本单位出库数量 */

	private double fauxstockqty;/* 出库数量 */

	private double fsecstockqty;/* 辅助单位出库数量 */

	private SeoutStock seOutStock;
	private ICItem icItem;
	private Stock stock; // 仓库

	// 临时字段，不存表
	private double useableQty; // 可用数
	private int isCheck; // 是否选中


	public String getFbrno() {
		return fbrno;
	}

	public int getFinterid() {
		return finterid;
	}

	public int getFentryid() {
		return fentryid;
	}

	public int getFitemid() {
		return fitemid;
	}

	public double getFqty() {
		return fqty;
	}

	public double getFcommitqty() {
		return fcommitqty;
	}

	public double getFprice() {
		return fprice;
	}

	public double getFamount() {
		return famount;
	}

	public String getForderinterid() {
		return forderinterid;
	}

	public String getFdate() {
		return fdate;
	}

	public String getFnote() {
		return fnote;
	}

	public double getFinvoiceqty() {
		return finvoiceqty;
	}

	public double getFbcommitqty() {
		return fbcommitqty;
	}

	public int getFunitid() {
		return funitid;
	}

	public double getFauxbcommitqty() {
		return fauxbcommitqty;
	}

	public double getFauxcommitqty() {
		return fauxcommitqty;
	}

	public double getFauxinvoiceqty() {
		return fauxinvoiceqty;
	}

	public double getFauxprice() {
		return fauxprice;
	}

	public double getFauxqty() {
		return fauxqty;
	}

	public int getFsourceentryid() {
		return fsourceentryid;
	}

	public String getFmapnumber() {
		return fmapnumber;
	}

	public String getFmapname() {
		return fmapname;
	}

	public int getFauxpropid() {
		return fauxpropid;
	}

	public String getFbatchno() {
		return fbatchno;
	}

	public String getFcheckdate() {
		return fcheckdate;
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

	public double getFseccoefficient() {
		return fseccoefficient;
	}

	public double getFsecqty() {
		return fsecqty;
	}

	public double getFseccommitqty() {
		return fseccommitqty;
	}

	public int getFsourcetrantype() {
		return fsourcetrantype;
	}

	public int getFsourceinterid() {
		return fsourceinterid;
	}

	public String getFsourcebillno() {
		return fsourcebillno;
	}

	public int getFcontractinterid() {
		return fcontractinterid;
	}

	public int getFcontractentryid() {
		return fcontractentryid;
	}

	public String getFcontractbillno() {
		return fcontractbillno;
	}

	public int getForderentryid() {
		return forderentryid;
	}

	public String getForderbillno() {
		return forderbillno;
	}

	public int getFstockid() {
		return fstockid;
	}

	public int getFplanmode() {
		return fplanmode;
	}

	public String getFmtono() {
		return fmtono;
	}

	public double getFstockqty() {
		return fstockqty;
	}

	public double getFauxstockqty() {
		return fauxstockqty;
	}

	public double getFsecstockqty() {
		return fsecstockqty;
	}

	public SeoutStock getSeOutStock() {
		return seOutStock;
	}

	public ICItem getIcItem() {
		return icItem;
	}

	public Stock getStock() {
		return stock;
	}

	public void setFbrno(String fbrno) {
		this.fbrno = fbrno;
	}

	public void setFinterid(int finterid) {
		this.finterid = finterid;
	}

	public void setFentryid(int fentryid) {
		this.fentryid = fentryid;
	}

	public void setFitemid(int fitemid) {
		this.fitemid = fitemid;
	}

	public void setFqty(double fqty) {
		this.fqty = fqty;
	}

	public void setFcommitqty(double fcommitqty) {
		this.fcommitqty = fcommitqty;
	}

	public void setFprice(double fprice) {
		this.fprice = fprice;
	}

	public void setFamount(double famount) {
		this.famount = famount;
	}

	public void setForderinterid(String forderinterid) {
		this.forderinterid = forderinterid;
	}

	public void setFdate(String fdate) {
		this.fdate = fdate;
	}

	public void setFnote(String fnote) {
		this.fnote = fnote;
	}

	public void setFinvoiceqty(double finvoiceqty) {
		this.finvoiceqty = finvoiceqty;
	}

	public void setFbcommitqty(double fbcommitqty) {
		this.fbcommitqty = fbcommitqty;
	}

	public void setFunitid(int funitid) {
		this.funitid = funitid;
	}

	public void setFauxbcommitqty(double fauxbcommitqty) {
		this.fauxbcommitqty = fauxbcommitqty;
	}

	public void setFauxcommitqty(double fauxcommitqty) {
		this.fauxcommitqty = fauxcommitqty;
	}

	public void setFauxinvoiceqty(double fauxinvoiceqty) {
		this.fauxinvoiceqty = fauxinvoiceqty;
	}

	public void setFauxprice(double fauxprice) {
		this.fauxprice = fauxprice;
	}

	public void setFauxqty(double fauxqty) {
		this.fauxqty = fauxqty;
	}

	public void setFsourceentryid(int fsourceentryid) {
		this.fsourceentryid = fsourceentryid;
	}

	public void setFmapnumber(String fmapnumber) {
		this.fmapnumber = fmapnumber;
	}

	public void setFmapname(String fmapname) {
		this.fmapname = fmapname;
	}

	public void setFauxpropid(int fauxpropid) {
		this.fauxpropid = fauxpropid;
	}

	public void setFbatchno(String fbatchno) {
		this.fbatchno = fbatchno;
	}

	public void setFcheckdate(String fcheckdate) {
		this.fcheckdate = fcheckdate;
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

	public void setFseccoefficient(double fseccoefficient) {
		this.fseccoefficient = fseccoefficient;
	}

	public void setFsecqty(double fsecqty) {
		this.fsecqty = fsecqty;
	}

	public void setFseccommitqty(double fseccommitqty) {
		this.fseccommitqty = fseccommitqty;
	}

	public void setFsourcetrantype(int fsourcetrantype) {
		this.fsourcetrantype = fsourcetrantype;
	}

	public void setFsourceinterid(int fsourceinterid) {
		this.fsourceinterid = fsourceinterid;
	}

	public void setFsourcebillno(String fsourcebillno) {
		this.fsourcebillno = fsourcebillno;
	}

	public void setFcontractinterid(int fcontractinterid) {
		this.fcontractinterid = fcontractinterid;
	}

	public void setFcontractentryid(int fcontractentryid) {
		this.fcontractentryid = fcontractentryid;
	}

	public void setFcontractbillno(String fcontractbillno) {
		this.fcontractbillno = fcontractbillno;
	}

	public void setForderentryid(int forderentryid) {
		this.forderentryid = forderentryid;
	}

	public void setForderbillno(String forderbillno) {
		this.forderbillno = forderbillno;
	}

	public void setFstockid(int fstockid) {
		this.fstockid = fstockid;
	}

	public void setFplanmode(int fplanmode) {
		this.fplanmode = fplanmode;
	}

	public void setFmtono(String fmtono) {
		this.fmtono = fmtono;
	}

	public void setFstockqty(double fstockqty) {
		this.fstockqty = fstockqty;
	}

	public void setFauxstockqty(double fauxstockqty) {
		this.fauxstockqty = fauxstockqty;
	}

	public void setFsecstockqty(double fsecstockqty) {
		this.fsecstockqty = fsecstockqty;
	}

	public void setSeOutStock(SeoutStock seOutStock) {
		this.seOutStock = seOutStock;
	}

	public void setIcItem(ICItem icItem) {
		this.icItem = icItem;
	}

	public void setStock(Stock stock) {
		this.stock = stock;
	}

	public double getUseableQty() {
		return useableQty;
	}

	public void setUseableQty(double useableQty) {
		this.useableQty = useableQty;
	}

	public int getIsCheck() {
		return isCheck;
	}

	public void setIsCheck(int isCheck) {
		this.isCheck = isCheck;
	}

	public int getFdetailid() {
		return fdetailid;
	}

	public void setFdetailid(int fdetailid) {
		this.fdetailid = fdetailid;
	}

}