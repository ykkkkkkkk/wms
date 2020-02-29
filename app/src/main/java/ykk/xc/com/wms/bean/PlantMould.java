package ykk.xc.com.wms.bean;

import java.io.Serializable;

/**
 * 工装模具表
 * @author Administrator
 *
 */
public class PlantMould implements Serializable{

	private int id;
	private int fitemId; 				// 物料id
	private String fname;				// 模具名称
	private String purpose;				// 用途
	private int useCount;				// 使用次数
	private int stockId;				// 默认仓库id
	private int stockAreaId;			// 默认库区id
	private int storageRackId;			// 默认货架id
	private int stockPosId;				// 默认库位id
	private int containerId;			// 默认的容器id
	private double worth;				// 价值
	private int period;					// 生命周期
	private int unitId;					// 生命周期单位id
	private String dutyMan;				// 责任人
	private char useType;				// 使用类型	L：领用，H：归还
	private char status;				// 状态 A：正常，B：损坏
	private int createUserId;			// 创建人
	private String createDate;			// 创建日期

	private Stock stock;
	private StockArea stockArea;
	private StorageRack storageRack;
	private StockPosition stockPos;
	private Container container;

	public PlantMould() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getFitemId() {
		return fitemId;
	}

	public void setFitemId(int fitemId) {
		this.fitemId = fitemId;
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public int getUseCount() {
		return useCount;
	}

	public void setUseCount(int useCount) {
		this.useCount = useCount;
	}

	public int getStockId() {
		return stockId;
	}

	public void setStockId(int stockId) {
		this.stockId = stockId;
	}

	public int getStockAreaId() {
		return stockAreaId;
	}

	public void setStockAreaId(int stockAreaId) {
		this.stockAreaId = stockAreaId;
	}

	public int getStorageRackId() {
		return storageRackId;
	}

	public void setStorageRackId(int storageRackId) {
		this.storageRackId = storageRackId;
	}

	public int getStockPosId() {
		return stockPosId;
	}

	public void setStockPosId(int stockPosId) {
		this.stockPosId = stockPosId;
	}

	public int getContainerId() {
		return containerId;
	}

	public void setContainerId(int containerId) {
		this.containerId = containerId;
	}

	public double getWorth() {
		return worth;
	}

	public void setWorth(double worth) {
		this.worth = worth;
	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	public int getUnitId() {
		return unitId;
	}

	public void setUnitId(int unitId) {
		this.unitId = unitId;
	}

	public String getDutyMan() {
		return dutyMan;
	}

	public void setDutyMan(String dutyMan) {
		this.dutyMan = dutyMan;
	}

	public char getUseType() {
		return useType;
	}

	public void setUseType(char useType) {
		this.useType = useType;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public int getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(int createUserId) {
		this.createUserId = createUserId;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public Stock getStock() {
		return stock;
	}

	public void setStock(Stock stock) {
		this.stock = stock;
	}

	public StockArea getStockArea() {
		return stockArea;
	}

	public void setStockArea(StockArea stockArea) {
		this.stockArea = stockArea;
	}

	public StorageRack getStorageRack() {
		return storageRack;
	}

	public void setStorageRack(StorageRack storageRack) {
		this.storageRack = storageRack;
	}

	public StockPosition getStockPos() {
		return stockPos;
	}

	public void setStockPos(StockPosition stockPos) {
		this.stockPos = stockPos;
	}

	public Container getContainer() {
		return container;
	}

	public void setContainer(Container container) {
		this.container = container;
	}


}
