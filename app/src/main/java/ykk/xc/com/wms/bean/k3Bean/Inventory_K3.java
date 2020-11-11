package ykk.xc.com.wms.bean.k3Bean;

import java.io.Serializable;

/**
 * @Description:k3即时库存
 * @author 2019年5月15日 上午10:40:41
 */
public class Inventory_K3 implements Serializable {
	/* 物料内码 */
	private int fitemID;
	/* 仓库内码 */
	private int fstockID;
	/* 辅助属性内码 */
	private int fauxPropID;
	/* 批次 */
	private String fbatchNo;
	/* 库位内码 */
	private int fstockPlaceID;
	/* 保质期 */
	private int fkfPeriod;
	/* 生产/采购日期 */
	private String fkfDate;
	/* 机构代码 */
	private String fbrNo;
	/* 计划跟踪号 */
	private String fmTONo;
	/* 存货数量 */
	private double fqty;
	/* 存货余额 */
	private double fbal;
	/* 锁库数量 */
	private double fqtyLock;
	/* 辅助计量单位数量 */
	private double fsecQty;

	// 临时字段,不存表
	private String mtlNumber;
	private String mtlName;
	private String unitName;
	private String stockName;
	private double realQty;
	private boolean check; // 是否选中
	private int createUserId;
	
	public Inventory_K3() {
		super();
	}
	
	public int getFitemID() {
		return fitemID;
	}
	public void setFitemID(int fitemID) {
		this.fitemID = fitemID;
	}
	public int getFstockID() {
		return fstockID;
	}
	public void setFstockID(int fstockID) {
		this.fstockID = fstockID;
	}
	public int getFauxPropID() {
		return fauxPropID;
	}
	public void setFauxPropID(int fauxPropID) {
		this.fauxPropID = fauxPropID;
	}
	public String getFbatchNo() {
		return fbatchNo;
	}
	public void setFbatchNo(String fbatchNo) {
		this.fbatchNo = fbatchNo;
	}
	public int getFstockPlaceID() {
		return fstockPlaceID;
	}
	public void setFstockPlaceID(int fstockPlaceID) {
		this.fstockPlaceID = fstockPlaceID;
	}
	public int getFkfPeriod() {
		return fkfPeriod;
	}
	public void setFkfPeriod(int fkfPeriod) {
		this.fkfPeriod = fkfPeriod;
	}
	public String getFkfDate() {
		return fkfDate;
	}
	public void setFkfDate(String fkfDate) {
		this.fkfDate = fkfDate;
	}
	public String getFbrNo() {
		return fbrNo;
	}
	public void setFbrNo(String fbrNo) {
		this.fbrNo = fbrNo;
	}
	public String getFmTONo() {
		return fmTONo;
	}
	public void setFmTONo(String fmTONo) {
		this.fmTONo = fmTONo;
	}
	public double getFqty() {
		return fqty;
	}
	public void setFqty(double fqty) {
		this.fqty = fqty;
	}
	public double getFbal() {
		return fbal;
	}
	public void setFbal(double fbal) {
		this.fbal = fbal;
	}
	public double getFqtyLock() {
		return fqtyLock;
	}
	public void setFqtyLock(double fqtyLock) {
		this.fqtyLock = fqtyLock;
	}
	public double getFsecQty() {
		return fsecQty;
	}
	public void setFsecQty(double fsecQty) {
		this.fsecQty = fsecQty;
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
	public double getRealQty() {
		return realQty;
	}
	public void setRealQty(double realQty) {
		this.realQty = realQty;
	}
	public boolean isCheck() {
		return check;
	}
	public void setCheck(boolean check) {
		this.check = check;
	}
	public int getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(int createUserId) {
		this.createUserId = createUserId;
	}

	
}