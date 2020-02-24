package ykk.xc.com.wms.bean.pur;

import java.io.Serializable;

import ykk.xc.com.wms.bean.k3Bean.ICItem;

/**
 * @Description:收料通知单单分录
 *
 */
public class POInStockEntry implements Serializable {
	/* 分录内码 */
	private int fdetailid;
	/* 采购订单内码 */
	private int finterid;
	/* 分录号 */
	private int fentryid;
	/* 物料内码 */
	private int fitemid;
	/* 订货数量 */
	private double fqty;
	/* 到货数量 */
	private double fcommitqty;
	/* 单价 */
	private double fprice;
	/* 金额 */
	private double famount;
	/* 备注 */
	private String fnote;
	/* 基本计量单位发票的数量     */
	private double finvoiceQty;
	/* 实退数量   */
	private double fbcommitQty;
	/* 基本单位检验数量  */
	private double fqcheckQty;
	/* 检验数量 */
	private double fauxQCheckQty;
	/* 单位内码 */
	private int funitid;
	private double fauxBCommitQty;
	private double fauxCommitQty;
	private double fauxInvoiceQty;
	private double fauxPrice;
	/* 辅助单价 */
	private double fauxprice;
	/* 辅助订货数量 */
	private double fauxqty;
	/* 原分录号 */
	private int fsourceentryid;
	private int fqtyPass;
	private int fauxQtyPass;
	private String fbatchNo;
	private int fdcSPID;
	private int fauxPropId;
	private int fdcStockId;
	private String ffetchDate;
	private int fsecCoefficient;
	private double fsecQty;
	private double fsecCommitQty;
	private int fsourceTranType;
	private int fsourceInterId;
	private String fsourceBillNo;
	private int fcontractInterId;
	private int fcontractEntryId;
	private String fcontractBillNo;
	private int forderInterId;
	private int forderEntryId;
	private String forderBillNo;
	private int fstockId;
	private String FPeriodDate;
	private double fnotPassQty;
	private double fnpCommitQty;
	private double fsampleBreakQty;
	private double fconPassQty;
	private double fconCommitQty;
	private double fauxNotPassQty;
	private double fauxNPCommitQty;
	private double fauxSampleBreakQty;
	private double fauxConPassQty;
	private double fauxConCommitQty;
	private double fauxRelateQty;
	private double frelateQty;
	private double fbackQty;
	private double fauxBackQty;
	private double fsecBackQty;
	private double fsecConCommitQty;
	private int fplanMode;					// 计划模式
	private String fmTONo;					// 计划跟踪号
	private int forderType;
	private int fentryAccessoryCount;
	private int fdeliveryNoticeEntryId;
	private int fdeliveryNoticeFId;
	private int fdischarged;
	private int fcheckMethodl;
	private double fscrapQty;
	private double fauxScrapQty;
	private double fsecScrapQty;
	private double fsecConCommiqty;
	private double fscrapInCommitQty;
	private double fauxScrapInCommitQty;
	private double fsecScrapInCommitQty;
	private double fsecQtyPass;
	private double fsecConPassQty;
	private double fsecNotPassQty;
	private double fsecSampleBreakQty;
	private double fsecRelateQty;
	private double fsecQCheckQty;
	private int fbackType;
	private int ftime;
	private int fsampleConclusion;
	private String fsamBillNo;
	private int fsamInterId;
	private int fsamEntryId;
	private double fpickQty;
	private double fauxPickQty;
	private double fsecPickQty;
	private double fqtyMust;
	private double fauxQtyMust;



	private String mtlName;
	/* 接收物料代码 */
	private String mtlNumber;
	/* 表头FbillNO */
	private String fbillno;

	// 收料主表对象
	private POInStock poInStock;
	private ICItem icItem;

	// 临时字段，不存表
	private double useableQty; // 可用数
	private double realQty; // 实际数
	private String stockName;
	private String unitName;
	private int isCheck; // 是否选中


	public POInStockEntry() {
	}


	public int getFdetailid() {
		return fdetailid;
	}


	public void setFdetailid(int fdetailid) {
		this.fdetailid = fdetailid;
	}


	public int getFinterid() {
		return finterid;
	}


	public void setFinterid(int finterid) {
		this.finterid = finterid;
	}


	public int getFentryid() {
		return fentryid;
	}


	public void setFentryid(int fentryid) {
		this.fentryid = fentryid;
	}


	public int getFitemid() {
		return fitemid;
	}


	public void setFitemid(int fitemid) {
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


	public String getFnote() {
		return fnote;
	}


	public void setFnote(String fnote) {
		this.fnote = fnote;
	}


	public double getFinvoiceQty() {
		return finvoiceQty;
	}


	public void setFinvoiceQty(double finvoiceQty) {
		this.finvoiceQty = finvoiceQty;
	}


	public double getFbcommitQty() {
		return fbcommitQty;
	}


	public void setFbcommitQty(double fbcommitQty) {
		this.fbcommitQty = fbcommitQty;
	}


	public double getFqcheckQty() {
		return fqcheckQty;
	}


	public void setFqcheckQty(double fqcheckQty) {
		this.fqcheckQty = fqcheckQty;
	}


	public double getFauxQCheckQty() {
		return fauxQCheckQty;
	}


	public void setFauxQCheckQty(double fauxQCheckQty) {
		this.fauxQCheckQty = fauxQCheckQty;
	}


	public int getFunitid() {
		return funitid;
	}


	public void setFunitid(int funitid) {
		this.funitid = funitid;
	}


	public double getFauxBCommitQty() {
		return fauxBCommitQty;
	}


	public void setFauxBCommitQty(double fauxBCommitQty) {
		this.fauxBCommitQty = fauxBCommitQty;
	}


	public double getFauxCommitQty() {
		return fauxCommitQty;
	}


	public void setFauxCommitQty(double fauxCommitQty) {
		this.fauxCommitQty = fauxCommitQty;
	}


	public double getFauxInvoiceQty() {
		return fauxInvoiceQty;
	}


	public void setFauxInvoiceQty(double fauxInvoiceQty) {
		this.fauxInvoiceQty = fauxInvoiceQty;
	}


	public double getFauxPrice() {
		return fauxPrice;
	}


	public void setFauxPrice(double fauxPrice) {
		this.fauxPrice = fauxPrice;
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


	public int getFsourceentryid() {
		return fsourceentryid;
	}


	public void setFsourceentryid(int fsourceentryid) {
		this.fsourceentryid = fsourceentryid;
	}


	public int getFqtyPass() {
		return fqtyPass;
	}


	public void setFqtyPass(int fqtyPass) {
		this.fqtyPass = fqtyPass;
	}


	public int getFauxQtyPass() {
		return fauxQtyPass;
	}


	public void setFauxQtyPass(int fauxQtyPass) {
		this.fauxQtyPass = fauxQtyPass;
	}


	public String getFbatchNo() {
		return fbatchNo;
	}


	public void setFbatchNo(String fbatchNo) {
		this.fbatchNo = fbatchNo;
	}


	public int getFdcSPID() {
		return fdcSPID;
	}


	public void setFdcSPID(int fdcSPID) {
		this.fdcSPID = fdcSPID;
	}


	public int getFauxPropId() {
		return fauxPropId;
	}


	public void setFauxPropId(int fauxPropId) {
		this.fauxPropId = fauxPropId;
	}


	public int getFdcStockId() {
		return fdcStockId;
	}


	public void setFdcStockId(int fdcStockId) {
		this.fdcStockId = fdcStockId;
	}


	public String getFfetchDate() {
		return ffetchDate;
	}


	public void setFfetchDate(String ffetchDate) {
		this.ffetchDate = ffetchDate;
	}


	public int getFsecCoefficient() {
		return fsecCoefficient;
	}


	public void setFsecCoefficient(int fsecCoefficient) {
		this.fsecCoefficient = fsecCoefficient;
	}


	public double getFsecQty() {
		return fsecQty;
	}


	public void setFsecQty(double fsecQty) {
		this.fsecQty = fsecQty;
	}


	public double getFsecCommitQty() {
		return fsecCommitQty;
	}


	public void setFsecCommitQty(double fsecCommitQty) {
		this.fsecCommitQty = fsecCommitQty;
	}


	public int getFsourceTranType() {
		return fsourceTranType;
	}


	public void setFsourceTranType(int fsourceTranType) {
		this.fsourceTranType = fsourceTranType;
	}


	public int getFsourceInterId() {
		return fsourceInterId;
	}


	public void setFsourceInterId(int fsourceInterId) {
		this.fsourceInterId = fsourceInterId;
	}


	public String getFsourceBillNo() {
		return fsourceBillNo;
	}


	public void setFsourceBillNo(String fsourceBillNo) {
		this.fsourceBillNo = fsourceBillNo;
	}


	public int getFcontractInterId() {
		return fcontractInterId;
	}


	public void setFcontractInterId(int fcontractInterId) {
		this.fcontractInterId = fcontractInterId;
	}


	public int getFcontractEntryId() {
		return fcontractEntryId;
	}


	public void setFcontractEntryId(int fcontractEntryId) {
		this.fcontractEntryId = fcontractEntryId;
	}


	public String getFcontractBillNo() {
		return fcontractBillNo;
	}


	public void setFcontractBillNo(String fcontractBillNo) {
		this.fcontractBillNo = fcontractBillNo;
	}


	public int getForderInterId() {
		return forderInterId;
	}


	public void setForderInterId(int forderInterId) {
		this.forderInterId = forderInterId;
	}


	public int getForderEntryId() {
		return forderEntryId;
	}


	public void setForderEntryId(int forderEntryId) {
		this.forderEntryId = forderEntryId;
	}


	public String getForderBillNo() {
		return forderBillNo;
	}


	public void setForderBillNo(String forderBillNo) {
		this.forderBillNo = forderBillNo;
	}


	public int getFstockId() {
		return fstockId;
	}


	public void setFstockId(int fstockId) {
		this.fstockId = fstockId;
	}


	public String getFPeriodDate() {
		return FPeriodDate;
	}


	public void setFPeriodDate(String fPeriodDate) {
		FPeriodDate = fPeriodDate;
	}


	public double getFnotPassQty() {
		return fnotPassQty;
	}


	public void setFnotPassQty(double fnotPassQty) {
		this.fnotPassQty = fnotPassQty;
	}


	public double getFnpCommitQty() {
		return fnpCommitQty;
	}


	public void setFnpCommitQty(double fnpCommitQty) {
		this.fnpCommitQty = fnpCommitQty;
	}


	public double getFsampleBreakQty() {
		return fsampleBreakQty;
	}


	public void setFsampleBreakQty(double fsampleBreakQty) {
		this.fsampleBreakQty = fsampleBreakQty;
	}


	public double getFconPassQty() {
		return fconPassQty;
	}


	public void setFconPassQty(double fconPassQty) {
		this.fconPassQty = fconPassQty;
	}


	public double getFconCommitQty() {
		return fconCommitQty;
	}


	public void setFconCommitQty(double fconCommitQty) {
		this.fconCommitQty = fconCommitQty;
	}


	public double getFauxNotPassQty() {
		return fauxNotPassQty;
	}


	public void setFauxNotPassQty(double fauxNotPassQty) {
		this.fauxNotPassQty = fauxNotPassQty;
	}


	public double getFauxNPCommitQty() {
		return fauxNPCommitQty;
	}


	public void setFauxNPCommitQty(double fauxNPCommitQty) {
		this.fauxNPCommitQty = fauxNPCommitQty;
	}


	public double getFauxSampleBreakQty() {
		return fauxSampleBreakQty;
	}


	public void setFauxSampleBreakQty(double fauxSampleBreakQty) {
		this.fauxSampleBreakQty = fauxSampleBreakQty;
	}


	public double getFauxConPassQty() {
		return fauxConPassQty;
	}


	public void setFauxConPassQty(double fauxConPassQty) {
		this.fauxConPassQty = fauxConPassQty;
	}


	public double getFauxConCommitQty() {
		return fauxConCommitQty;
	}


	public void setFauxConCommitQty(double fauxConCommitQty) {
		this.fauxConCommitQty = fauxConCommitQty;
	}


	public double getFauxRelateQty() {
		return fauxRelateQty;
	}


	public void setFauxRelateQty(double fauxRelateQty) {
		this.fauxRelateQty = fauxRelateQty;
	}


	public double getFrelateQty() {
		return frelateQty;
	}


	public void setFrelateQty(double frelateQty) {
		this.frelateQty = frelateQty;
	}


	public double getFbackQty() {
		return fbackQty;
	}


	public void setFbackQty(double fbackQty) {
		this.fbackQty = fbackQty;
	}


	public double getFauxBackQty() {
		return fauxBackQty;
	}


	public void setFauxBackQty(double fauxBackQty) {
		this.fauxBackQty = fauxBackQty;
	}


	public double getFsecBackQty() {
		return fsecBackQty;
	}


	public void setFsecBackQty(double fsecBackQty) {
		this.fsecBackQty = fsecBackQty;
	}


	public double getFsecConCommitQty() {
		return fsecConCommitQty;
	}


	public void setFsecConCommitQty(double fsecConCommitQty) {
		this.fsecConCommitQty = fsecConCommitQty;
	}


	public int getFplanMode() {
		return fplanMode;
	}


	public void setFplanMode(int fplanMode) {
		this.fplanMode = fplanMode;
	}


	public String getFmTONo() {
		return fmTONo;
	}


	public void setFmTONo(String fmTONo) {
		this.fmTONo = fmTONo;
	}


	public int getForderType() {
		return forderType;
	}


	public void setForderType(int forderType) {
		this.forderType = forderType;
	}


	public int getFentryAccessoryCount() {
		return fentryAccessoryCount;
	}


	public void setFentryAccessoryCount(int fentryAccessoryCount) {
		this.fentryAccessoryCount = fentryAccessoryCount;
	}


	public int getFdeliveryNoticeEntryId() {
		return fdeliveryNoticeEntryId;
	}


	public void setFdeliveryNoticeEntryId(int fdeliveryNoticeEntryId) {
		this.fdeliveryNoticeEntryId = fdeliveryNoticeEntryId;
	}


	public int getFdeliveryNoticeFId() {
		return fdeliveryNoticeFId;
	}


	public void setFdeliveryNoticeFId(int fdeliveryNoticeFId) {
		this.fdeliveryNoticeFId = fdeliveryNoticeFId;
	}


	public int getFdischarged() {
		return fdischarged;
	}


	public void setFdischarged(int fdischarged) {
		this.fdischarged = fdischarged;
	}


	public int getFcheckMethodl() {
		return fcheckMethodl;
	}


	public void setFcheckMethodl(int fcheckMethodl) {
		this.fcheckMethodl = fcheckMethodl;
	}


	public double getFscrapQty() {
		return fscrapQty;
	}


	public void setFscrapQty(double fscrapQty) {
		this.fscrapQty = fscrapQty;
	}


	public double getFauxScrapQty() {
		return fauxScrapQty;
	}


	public void setFauxScrapQty(double fauxScrapQty) {
		this.fauxScrapQty = fauxScrapQty;
	}


	public double getFsecScrapQty() {
		return fsecScrapQty;
	}


	public void setFsecScrapQty(double fsecScrapQty) {
		this.fsecScrapQty = fsecScrapQty;
	}


	public double getFsecConCommiqty() {
		return fsecConCommiqty;
	}


	public void setFsecConCommiqty(double fsecConCommiqty) {
		this.fsecConCommiqty = fsecConCommiqty;
	}


	public double getFscrapInCommitQty() {
		return fscrapInCommitQty;
	}


	public void setFscrapInCommitQty(double fscrapInCommitQty) {
		this.fscrapInCommitQty = fscrapInCommitQty;
	}


	public double getFauxScrapInCommitQty() {
		return fauxScrapInCommitQty;
	}


	public void setFauxScrapInCommitQty(double fauxScrapInCommitQty) {
		this.fauxScrapInCommitQty = fauxScrapInCommitQty;
	}


	public double getFsecScrapInCommitQty() {
		return fsecScrapInCommitQty;
	}


	public void setFsecScrapInCommitQty(double fsecScrapInCommitQty) {
		this.fsecScrapInCommitQty = fsecScrapInCommitQty;
	}


	public double getFsecQtyPass() {
		return fsecQtyPass;
	}


	public void setFsecQtyPass(double fsecQtyPass) {
		this.fsecQtyPass = fsecQtyPass;
	}


	public double getFsecConPassQty() {
		return fsecConPassQty;
	}


	public void setFsecConPassQty(double fsecConPassQty) {
		this.fsecConPassQty = fsecConPassQty;
	}


	public double getFsecNotPassQty() {
		return fsecNotPassQty;
	}


	public void setFsecNotPassQty(double fsecNotPassQty) {
		this.fsecNotPassQty = fsecNotPassQty;
	}


	public double getFsecSampleBreakQty() {
		return fsecSampleBreakQty;
	}


	public void setFsecSampleBreakQty(double fsecSampleBreakQty) {
		this.fsecSampleBreakQty = fsecSampleBreakQty;
	}


	public double getFsecRelateQty() {
		return fsecRelateQty;
	}


	public void setFsecRelateQty(double fsecRelateQty) {
		this.fsecRelateQty = fsecRelateQty;
	}


	public double getFsecQCheckQty() {
		return fsecQCheckQty;
	}


	public void setFsecQCheckQty(double fsecQCheckQty) {
		this.fsecQCheckQty = fsecQCheckQty;
	}


	public int getFbackType() {
		return fbackType;
	}


	public void setFbackType(int fbackType) {
		this.fbackType = fbackType;
	}


	public int getFtime() {
		return ftime;
	}


	public void setFtime(int ftime) {
		this.ftime = ftime;
	}


	public int getFsampleConclusion() {
		return fsampleConclusion;
	}


	public void setFsampleConclusion(int fsampleConclusion) {
		this.fsampleConclusion = fsampleConclusion;
	}


	public String getFsamBillNo() {
		return fsamBillNo;
	}


	public void setFsamBillNo(String fsamBillNo) {
		this.fsamBillNo = fsamBillNo;
	}


	public int getFsamInterId() {
		return fsamInterId;
	}


	public void setFsamInterId(int fsamInterId) {
		this.fsamInterId = fsamInterId;
	}


	public int getFsamEntryId() {
		return fsamEntryId;
	}


	public void setFsamEntryId(int fsamEntryId) {
		this.fsamEntryId = fsamEntryId;
	}


	public double getFpickQty() {
		return fpickQty;
	}


	public void setFpickQty(double fpickQty) {
		this.fpickQty = fpickQty;
	}


	public double getFauxPickQty() {
		return fauxPickQty;
	}


	public void setFauxPickQty(double fauxPickQty) {
		this.fauxPickQty = fauxPickQty;
	}


	public double getFsecPickQty() {
		return fsecPickQty;
	}


	public void setFsecPickQty(double fsecPickQty) {
		this.fsecPickQty = fsecPickQty;
	}


	public double getFqtyMust() {
		return fqtyMust;
	}


	public void setFqtyMust(double fqtyMust) {
		this.fqtyMust = fqtyMust;
	}


	public double getFauxQtyMust() {
		return fauxQtyMust;
	}


	public void setFauxQtyMust(double fauxQtyMust) {
		this.fauxQtyMust = fauxQtyMust;
	}


	public String getMtlName() {
		return mtlName;
	}


	public void setMtlName(String mtlName) {
		this.mtlName = mtlName;
	}


	public String getMtlNumber() {
		return mtlNumber;
	}


	public void setMtlNumber(String mtlNumber) {
		this.mtlNumber = mtlNumber;
	}


	public String getFbillno() {
		return fbillno;
	}


	public void setFbillno(String fbillno) {
		this.fbillno = fbillno;
	}


	public POInStock getPoInStock() {
		return poInStock;
	}


	public void setPoInStock(POInStock poInStock) {
		this.poInStock = poInStock;
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


	public String getStockName() {
		return stockName;
	}


	public void setStockName(String stockName) {
		this.stockName = stockName;
	}



}