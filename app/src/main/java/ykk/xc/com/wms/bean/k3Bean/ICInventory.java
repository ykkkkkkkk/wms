package ykk.xc.com.wms.bean.k3Bean;

import java.io.Serializable;

/**
 * @Description:k3即时库存
 * @author 2019年5月15日 上午10:40:41
 */
public class ICInventory implements Serializable {
	/* 物料内码 */
	private Integer fItemID;
	/* 仓库内码 */
	private Integer fStockID;
	/* 辅助属性内码 */
	private Integer fAuxPropID;
	/* 批次 */
	private String fBatchNo;
	/* 库位内码 */
	private Integer fStockPlaceID;
	/* 保质期 */
	private Integer fKfPeriod;
	/* 生产/采购日期 */
	private String fKfDate;
	/* 机构代码 */
	private String fBrNo;
	/* 计划跟踪号 */
	private String fMTONo;
	/* 存货数量 */
	private double fQty;
	/* 存货余额 */
	private double fBal;
	/* 锁库数量 */
	private double fQtyLock;
	/* 辅助计量单位数量 */
	private double fSecQty;

	// 临时字段,不存表
	private String mtlNumber;
	private String mtlName;
	private String unitName;
	private String stockName;
	private double realQty;
	private boolean check; // 是否选中
	private int createUserId;

	public Integer getfItemID() {
		return fItemID;
	}

	public void setfItemID(Integer fItemID) {
		this.fItemID = fItemID;
	}

	public Integer getfStockID() {
		return fStockID;
	}

	public void setfStockID(Integer fStockID) {
		this.fStockID = fStockID;
	}

	public Integer getfAuxPropID() {
		return fAuxPropID;
	}

	public void setfAuxPropID(Integer fAuxPropID) {
		this.fAuxPropID = fAuxPropID;
	}

	public String getfBatchNo() {
		return fBatchNo;
	}

	public void setfBatchNo(String fBatchNo) {
		this.fBatchNo = fBatchNo;
	}

	public Integer getfStockPlaceID() {
		return fStockPlaceID;
	}

	public void setfStockPlaceID(Integer fStockPlaceID) {
		this.fStockPlaceID = fStockPlaceID;
	}

	public Integer getfKfPeriod() {
		return fKfPeriod;
	}

	public void setfKfPeriod(Integer fKfPeriod) {
		this.fKfPeriod = fKfPeriod;
	}

	public String getfKfDate() {
		return fKfDate;
	}

	public void setfKfDate(String fKfDate) {
		this.fKfDate = fKfDate;
	}

	public String getfBrNo() {
		return fBrNo;
	}

	public void setfBrNo(String fBrNo) {
		this.fBrNo = fBrNo;
	}

	public String getfMTONo() {
		return fMTONo;
	}

	public void setfMTONo(String fMTONo) {
		this.fMTONo = fMTONo;
	}

	public double getfQty() {
		return fQty;
	}

	public void setfQty(double fQty) {
		this.fQty = fQty;
	}

	public double getfBal() {
		return fBal;
	}

	public void setfBal(double fBal) {
		this.fBal = fBal;
	}

	public double getfQtyLock() {
		return fQtyLock;
	}

	public void setfQtyLock(double fQtyLock) {
		this.fQtyLock = fQtyLock;
	}

	public double getfSecQty() {
		return fSecQty;
	}

	public void setfSecQty(double fSecQty) {
		this.fSecQty = fSecQty;
	}

	public String getMtlNumber() {
		return mtlNumber;
	}

	public void setMtlNumber(String mtlNumber) {
		this.mtlNumber = mtlNumber;
	}

	public String getMtlName() {
		return mtlName;
	}

	public void setMtlName(String mtlName) {
		this.mtlName = mtlName;
	}

	public String getStockName() {
		return stockName;
	}

	public void setStockName(String stockName) {
		this.stockName = stockName;
	}

	public boolean isCheck() {
		return check;
	}

	public void setCheck(boolean check) {
		this.check = check;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public double getRealQty() {
		return realQty;
	}

	public void setRealQty(double realQty) {
		this.realQty = realQty;
	}

	public int getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(int createUserId) {
		this.createUserId = createUserId;
	}

}