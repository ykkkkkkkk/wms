package ykk.xc.com.wms.bean;

import java.io.Serializable;

import ykk.xc.com.wms.bean.k3Bean.ICItem;

/**
 * 投料单调拨单字表实体类
 * @author hongmoon
 *
 */
public class PPBomTransferEntry implements Serializable {
	private static final long serialVersionUID = 1L;

	//ID
	private int id;

	//主表ID
	private int ppBomTransferId;

	private PPBomTransfer ppBomTransfer;

	//物料ID
	private int icItemId;

	private ICItem icItem;

	//ERP申请数量
	private double erpQty;

	//MRP仓数量
	private double mrpQty;

	//物料最小领用数量
	private double minQty;

	//实际申请数量
	private double applyQty;

	//实际应发数量（APP里进行调拨拣货操作时，按这个数量进行拣货操作）
	private double mustQty;

	//实发数量（App里进行拣货操作后，更新这个字段的数量）
	private double realQty;

	//物料id
	private int unitId;

	private Unit unit;

	//分录类型 1：仓库调车间，2：车间内调拨
	private int entryType;

	//调出仓库
	private int outStockId;

	private Stock outStock;

	//调出库区
	private int outStockAreaId;

	private StockArea outStockArea;

	//调出货架
	private int outStorageRackId;

	private StorageRack outStorageRack;

	//调出库位
	private int outStockPositionId;

	private StockPosition outStockPosition;

	//调出容器
	private int outContainerId;

	private Container outContainer;

	//调入仓库
	private int inStockId;

	private Stock inStock;

	//调入库区
	private int inStockAreaId;

	private StockArea inStockArea;

	//调入货架
	private int inStorageRackId;

	private StorageRack inStorageRack;

	//调入库位
	private int inStockPositionId;

	private StockPosition inStockPosition;

	//调入容器
	private int inContainerId;

	private Container inContainer;

	//投料调拨单主表单号，用于插入子表时，查询主表id值使用，不存表
	private String masterBillNo;

	/* 临时字段，不存表  */
	private double useableQty; // 可用数

	public PPBomTransferEntry() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIcItemId() {
		return icItemId;
	}

	public void setIcItemId(int icItemId) {
		this.icItemId = icItemId;
	}

	public ICItem getIcItem() {
		return icItem;
	}

	public void setIcItem(ICItem icItem) {
		this.icItem = icItem;
	}

	public double getErpQty() {
		return erpQty;
	}

	public void setErpQty(double erpQty) {
		this.erpQty = erpQty;
	}

	public double getMrpQty() {
		return mrpQty;
	}

	public void setMrpQty(double mrpQty) {
		this.mrpQty = mrpQty;
	}

	public double getMinQty() {
		return minQty;
	}

	public void setMinQty(double minQty) {
		this.minQty = minQty;
	}

	public double getApplyQty() {
		return applyQty;
	}

	public void setApplyQty(double applyQty) {
		this.applyQty = applyQty;
	}

	public double getRealQty() {
		return realQty;
	}

	public void setRealQty(double realQty) {
		this.realQty = realQty;
	}

	public int getOutStockId() {
		return outStockId;
	}

	public void setOutStockId(int outStockId) {
		this.outStockId = outStockId;
	}

	public Stock getOutStock() {
		return outStock;
	}

	public void setOutStock(Stock outStock) {
		this.outStock = outStock;
	}

	public int getOutStockAreaId() {
		return outStockAreaId;
	}

	public void setOutStockAreaId(int outStockAreaId) {
		this.outStockAreaId = outStockAreaId;
	}

	public StockArea getOutStockArea() {
		return outStockArea;
	}

	public void setOutStockArea(StockArea outStockArea) {
		this.outStockArea = outStockArea;
	}

	public int getOutStorageRackId() {
		return outStorageRackId;
	}

	public void setOutStorageRackId(int outStorageRackId) {
		this.outStorageRackId = outStorageRackId;
	}

	public StorageRack getOutStorageRack() {
		return outStorageRack;
	}

	public void setOutStorageRack(StorageRack outStorageRack) {
		this.outStorageRack = outStorageRack;
	}

	public int getOutStockPositionId() {
		return outStockPositionId;
	}

	public void setOutStockPositionId(int outStockPositionId) {
		this.outStockPositionId = outStockPositionId;
	}

	public StockPosition getOutStockPosition() {
		return outStockPosition;
	}

	public void setOutStockPosition(StockPosition outStockPosition) {
		this.outStockPosition = outStockPosition;
	}

	public int getOutContainerId() {
		return outContainerId;
	}

	public void setOutContainerId(int outContainerId) {
		this.outContainerId = outContainerId;
	}

	public Container getOutContainer() {
		return outContainer;
	}

	public void setOutContainer(Container outContainer) {
		this.outContainer = outContainer;
	}

	public int getInStockId() {
		return inStockId;
	}

	public void setInStockId(int inStockId) {
		this.inStockId = inStockId;
	}

	public Stock getInStock() {
		return inStock;
	}

	public void setInStock(Stock inStock) {
		this.inStock = inStock;
	}

	public int getInStockAreaId() {
		return inStockAreaId;
	}

	public void setInStockAreaId(int inStockAreaId) {
		this.inStockAreaId = inStockAreaId;
	}

	public StockArea getInStockArea() {
		return inStockArea;
	}

	public void setInStockArea(StockArea inStockArea) {
		this.inStockArea = inStockArea;
	}

	public int getInStorageRackId() {
		return inStorageRackId;
	}

	public void setInStorageRackId(int inStorageRackId) {
		this.inStorageRackId = inStorageRackId;
	}

	public StorageRack getInStorageRack() {
		return inStorageRack;
	}

	public void setInStorageRack(StorageRack inStorageRack) {
		this.inStorageRack = inStorageRack;
	}

	public int getInStockPositionId() {
		return inStockPositionId;
	}

	public void setInStockPositionId(int inStockPositionId) {
		this.inStockPositionId = inStockPositionId;
	}

	public StockPosition getInStockPosition() {
		return inStockPosition;
	}

	public void setInStockPosition(StockPosition inStockPosition) {
		this.inStockPosition = inStockPosition;
	}

	public int getInContainerId() {
		return inContainerId;
	}

	public void setInContainerId(int inContainerId) {
		this.inContainerId = inContainerId;
	}

	public int getPpBomTransferId() {
		return ppBomTransferId;
	}

	public void setPpBomTransferId(int ppBomTransferId) {
		this.ppBomTransferId = ppBomTransferId;
	}

	public PPBomTransfer getPpBomTransfer() {
		return ppBomTransfer;
	}

	public void setPpBomTransfer(PPBomTransfer ppBomTransfer) {
		this.ppBomTransfer = ppBomTransfer;
	}

	public Container getInContainer() {
		return inContainer;
	}

	public void setInContainer(Container inContainer) {
		this.inContainer = inContainer;
	}

	public int getUnitId() {
		return unitId;
	}

	public void setUnitId(int unitId) {
		this.unitId = unitId;
	}

	public int getEntryType() {
		return entryType;
	}

	public void setEntryType(int entryType) {
		this.entryType = entryType;
	}

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	public double getMustQty() {
		return mustQty;
	}

	public void setMustQty(double mustQty) {
		this.mustQty = mustQty;
	}

	public String getMasterBillNo() {
		return masterBillNo;
	}

	public void setMasterBillNo(String masterBillNo) {
		this.masterBillNo = masterBillNo;
	}

	public double getUseableQty() {
		return useableQty;
	}

	public void setUseableQty(double useableQty) {
		this.useableQty = useableQty;
	}


}
