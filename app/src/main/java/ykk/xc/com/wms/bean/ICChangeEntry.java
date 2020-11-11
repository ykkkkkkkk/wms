package ykk.xc.com.wms.bean;

import java.io.Serializable;

import ykk.xc.com.wms.bean.k3Bean.ICItem;

/**
 * Wms 本地的装卸表	主表
 * @author Administrator
 *
 */
public class ICChangeEntry implements Serializable {
	private static final long serialVersionUID = 1L;

	private int id;					// wms自增长id
	private int parentId;			// ICChange自增长id
	private int fentryID;
	private int fid;
	private int findex;
	private int fmtrlType;
	private int fitemID;
	private int fauxPropID;
	private int funitID;
	private double fqty;
	private double fbaseQty;
	private double fauxQty_Base;
	private int fsecUnitID;
	private double fsecQty;
	private double fsecCoefficient;
	private double fqty_Base;
	private String fkfDate;
	private int fkfPeriod;
	private String fperiodDate;
	private String fbatchNo;
	private int fstockID;
	private int fsPID;
	private int fclassID_SRC;
	private String fbIllNo_SRC;
	private int fentryID_SRC;
	private int fid_SRC;
	private double fcostPercentage;
	private int fplanMode;
	private String fmtoNo;
	private double fstockQty;
      
	private ICChange icChange;
	private ICItem icItem;
	private Unit unit;
	private Stock stock;
	private StockPosition stockPos;

	// 临时字段，不存表
	private double bom_FQty;	// ICBOM表中的fqty
	private double inventoryQty;	// 库存数

	public ICChangeEntry() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public int getFentryID() {
		return fentryID;
	}

	public void setFentryID(int fentryID) {
		this.fentryID = fentryID;
	}

	public int getFid() {
		return fid;
	}

	public void setFid(int fid) {
		this.fid = fid;
	}

	public int getFindex() {
		return findex;
	}

	public void setFindex(int findex) {
		this.findex = findex;
	}

	public int getFmtrlType() {
		return fmtrlType;
	}

	public void setFmtrlType(int fmtrlType) {
		this.fmtrlType = fmtrlType;
	}

	public int getFitemID() {
		return fitemID;
	}

	public void setFitemID(int fitemID) {
		this.fitemID = fitemID;
	}

	public int getFauxPropID() {
		return fauxPropID;
	}

	public void setFauxPropID(int fauxPropID) {
		this.fauxPropID = fauxPropID;
	}

	public int getFunitID() {
		return funitID;
	}

	public void setFunitID(int funitID) {
		this.funitID = funitID;
	}

	public double getFqty() {
		return fqty;
	}

	public void setFqty(double fqty) {
		this.fqty = fqty;
	}

	public double getFbaseQty() {
		return fbaseQty;
	}

	public void setFbaseQty(double fbaseQty) {
		this.fbaseQty = fbaseQty;
	}

	public double getFauxQty_Base() {
		return fauxQty_Base;
	}

	public void setFauxQty_Base(double fauxQty_Base) {
		this.fauxQty_Base = fauxQty_Base;
	}

	public int getFsecUnitID() {
		return fsecUnitID;
	}

	public void setFsecUnitID(int fsecUnitID) {
		this.fsecUnitID = fsecUnitID;
	}

	public double getFsecQty() {
		return fsecQty;
	}

	public void setFsecQty(double fsecQty) {
		this.fsecQty = fsecQty;
	}

	public double getFsecCoefficient() {
		return fsecCoefficient;
	}

	public void setFsecCoefficient(double fsecCoefficient) {
		this.fsecCoefficient = fsecCoefficient;
	}

	public double getFqty_Base() {
		return fqty_Base;
	}

	public void setFqty_Base(double fqty_Base) {
		this.fqty_Base = fqty_Base;
	}

	public String getFkfDate() {
		return fkfDate;
	}

	public void setFkfDate(String fkfDate) {
		this.fkfDate = fkfDate;
	}

	public int getFkfPeriod() {
		return fkfPeriod;
	}

	public void setFkfPeriod(int fkfPeriod) {
		this.fkfPeriod = fkfPeriod;
	}

	public String getFperiodDate() {
		return fperiodDate;
	}

	public void setFperiodDate(String fperiodDate) {
		this.fperiodDate = fperiodDate;
	}

	public String getFbatchNo() {
		return fbatchNo;
	}

	public void setFbatchNo(String fbatchNo) {
		this.fbatchNo = fbatchNo;
	}

	public int getFstockID() {
		return fstockID;
	}

	public void setFstockID(int fstockID) {
		this.fstockID = fstockID;
	}

	public int getFsPID() {
		return fsPID;
	}

	public void setFsPID(int fsPID) {
		this.fsPID = fsPID;
	}

	public int getFclassID_SRC() {
		return fclassID_SRC;
	}

	public void setFclassID_SRC(int fclassID_SRC) {
		this.fclassID_SRC = fclassID_SRC;
	}

	public String getFbIllNo_SRC() {
		return fbIllNo_SRC;
	}

	public void setFbIllNo_SRC(String fbIllNo_SRC) {
		this.fbIllNo_SRC = fbIllNo_SRC;
	}

	public int getFentryID_SRC() {
		return fentryID_SRC;
	}

	public void setFentryID_SRC(int fentryID_SRC) {
		this.fentryID_SRC = fentryID_SRC;
	}

	public int getFid_SRC() {
		return fid_SRC;
	}

	public void setFid_SRC(int fid_SRC) {
		this.fid_SRC = fid_SRC;
	}

	public double getFcostPercentage() {
		return fcostPercentage;
	}

	public void setFcostPercentage(double fcostPercentage) {
		this.fcostPercentage = fcostPercentage;
	}

	public int getFplanMode() {
		return fplanMode;
	}

	public void setFplanMode(int fplanMode) {
		this.fplanMode = fplanMode;
	}

	public String getFmtoNo() {
		return fmtoNo;
	}

	public void setFmtoNo(String fmtoNo) {
		this.fmtoNo = fmtoNo;
	}

	public double getFstockQty() {
		return fstockQty;
	}

	public void setFstockQty(double fstockQty) {
		this.fstockQty = fstockQty;
	}

	public ICChange getIcChange() {
		return icChange;
	}

	public void setIcChange(ICChange icChange) {
		this.icChange = icChange;
	}

	public ICItem getIcItem() {
		return icItem;
	}

	public void setIcItem(ICItem icItem) {
		this.icItem = icItem;
	}

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	public Stock getStock() {
		return stock;
	}

	public void setStock(Stock stock) {
		this.stock = stock;
	}

	public StockPosition getStockPos() {
		return stockPos;
	}

	public void setStockPos(StockPosition stockPos) {
		this.stockPos = stockPos;
	}

	public double getBom_FQty() {
		return bom_FQty;
	}

	public void setBom_FQty(double bom_FQty) {
		this.bom_FQty = bom_FQty;
	}

	public double getInventoryQty() {
		return inventoryQty;
	}

	public void setInventoryQty(double inventoryQty) {
		this.inventoryQty = inventoryQty;
	}
}
