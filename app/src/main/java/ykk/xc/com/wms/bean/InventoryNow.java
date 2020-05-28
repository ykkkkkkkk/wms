package ykk.xc.com.wms.bean;

import java.io.Serializable;

import ykk.xc.com.wms.bean.k3Bean.ICItem;

/**
 * @author hongmoon
 * @version 创建时间：2018年11月27日
 * @ClassName Inventory
 * @Description 即时库存表
 */
public class InventoryNow implements Serializable {

	private int id ;
	//仓库id
	private int stockId; // k3仓库id
	//仓库实体类
	private Stock stock;
	//库区id
	private int stockAreaId;
	//库区实体类
	private StockArea stockArea;
	//货架id
	private int storageRackId;
	//货架实体类
	private StorageRack storageRack;
	//库位id
	private int stockPositionId;
	//库位实体类
	private StockPosition stockPosition;
	//容器id
	private int containerId;
	//容器实体类
	private Container container;
	//物料id
	private int icItemId; // k3物料id
	//物料实体类
	private ICItem icItem;
	//批次号
	private String batchCode;
	//序列号
	private String snCode;

	//即时库存数量
	private double nowQty;
	//锁库数量
	private double lockQty;
	//即时可用库存数量
	private double avbQty;
	//最后修改时间
	private String lastUpdateTime;
	//辅助单位数量
	private double fsecQty;
	// 生产/采购日期
	private String fkfDate;
	//保质期
	private int fkfPeriod;
	//单位id
	private int unitId;
	//单位
	private Unit unit;
	//货主id
	private int goodsOwnerId;

	//调拨方向：1：入库；2：出库，不存表，用于即时库存计算判断
	private int transferDir;

	public InventoryNow() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getStockId() {
		return stockId;
	}

	public void setStockId(int stockId) {
		this.stockId = stockId;
	}

	public Stock getStock() {
		return stock;
	}

	public void setStock(Stock stock) {
		this.stock = stock;
	}

	public int getStockAreaId() {
		return stockAreaId;
	}

	public void setStockAreaId(int stockAreaId) {
		this.stockAreaId = stockAreaId;
	}

	public StockArea getStockArea() {
		return stockArea;
	}

	public void setStockArea(StockArea stockArea) {
		this.stockArea = stockArea;
	}

	public int getStorageRackId() {
		return storageRackId;
	}

	public void setStorageRackId(int storageRackId) {
		this.storageRackId = storageRackId;
	}

	public StorageRack getStorageRack() {
		return storageRack;
	}

	public void setStorageRack(StorageRack storageRack) {
		this.storageRack = storageRack;
	}

	public int getStockPositionId() {
		return stockPositionId;
	}

	public void setStockPositionId(int stockPositionId) {
		this.stockPositionId = stockPositionId;
	}

	public StockPosition getStockPosition() {
		return stockPosition;
	}

	public void setStockPosition(StockPosition stockPosition) {
		this.stockPosition = stockPosition;
	}

	public int getContainerId() {
		return containerId;
	}

	public void setContainerId(int containerId) {
		this.containerId = containerId;
	}

	public Container getContainer() {
		return container;
	}

	public void setContainer(Container container) {
		this.container = container;
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

	public String getBatchCode() {
		return batchCode;
	}

	public void setBatchCode(String batchCode) {
		this.batchCode = batchCode;
	}

	public String getSnCode() {
		return snCode;
	}

	public void setSnCode(String snCode) {
		this.snCode = snCode;
	}

	public double getNowQty() {
		return nowQty;
	}

	public void setNowQty(double nowQty) {
		this.nowQty = nowQty;
	}

	public double getLockQty() {
		return lockQty;
	}

	public void setLockQty(double lockQty) {
		this.lockQty = lockQty;
	}

	public double getAvbQty() {
		return avbQty;
	}

	public void setAvbQty(double avbQty) {
		this.avbQty = avbQty;
	}

	public String getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public double getFsecQty() {
		return fsecQty;
	}

	public void setFsecQty(double fsecQty) {
		this.fsecQty = fsecQty;
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

	public int getGoodsOwnerId() {
		return goodsOwnerId;
	}

	public void setGoodsOwnerId(int goodsOwnerId) {
		this.goodsOwnerId = goodsOwnerId;
	}

	public int getUnitId() {
		return unitId;
	}

	public void setUnitId(int unitId) {
		this.unitId = unitId;
	}

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	public int getTransferDir() {
		return transferDir;
	}

	public void setTransferDir(int transferDir) {
		this.transferDir = transferDir;
	}

	@Override
	public String toString() {
		return "InventoryNow [id=" + id + ", stockId=" + stockId + ", stock=" + stock + ", stockAreaId=" + stockAreaId
				+ ", stockArea=" + stockArea + ", storageRackId=" + storageRackId + ", storageRack=" + storageRack
				+ ", stockPositionId=" + stockPositionId + ", stockPosition=" + stockPosition + ", containerId="
				+ containerId + ", container=" + container + ", icItemId=" + icItemId + ", icItem=" + icItem
				+ ", batchCode=" + batchCode + ", snCode=" + snCode + ", nowQty=" + nowQty + ", lockQty=" + lockQty
				+ ", avbQty=" + avbQty + ", lastUpdateTime=" + lastUpdateTime + ", fsecQty=" + fsecQty + ", fkfDate="
				+ fkfDate + ", fkfPeriod=" + fkfPeriod + ", unitId=" + unitId + ", unit=" + unit + ", goodsOwnerId="
				+ goodsOwnerId + ", transferDir=" + transferDir + "]";
	}

}