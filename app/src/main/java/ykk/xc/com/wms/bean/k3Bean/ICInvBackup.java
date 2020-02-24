package ykk.xc.com.wms.bean.k3Bean;

import java.io.Serializable;

import ykk.xc.com.wms.bean.Container;
import ykk.xc.com.wms.bean.Stock;
import ykk.xc.com.wms.bean.StockArea;
import ykk.xc.com.wms.bean.StockPosition;
import ykk.xc.com.wms.bean.StorageRack;
import ykk.xc.com.wms.bean.User;

/**
 * 盘点备份表
 */
public class ICInvBackup implements Serializable {
	private int id; // id
	private int finterId; // 方案id
	private int stockId; // 仓库id
	private int mtlId; // 物料id
	private double fauxQty; // 账存数
	private double fauxQtyAct; // 实存数
	private double fauxCheckQty; // 盘点数
	private double realQty; // 当时盘点的输入的数
	private String createDate; // 创建日期
	private int createUserId; // 创建人
	private int toK3; // 是否提交到K3  1: 未提交	3:已提交
	private int stockPosId; // K3  库位id
	private int stockId_wms; // WMS 仓库id
	private int stockAreaId_wms; // WMS 库区id
	private int storageRackId_wms; // WMS 货架id
	private int stockPosId_wms; // WMS 库位id
	private int containerId; // WMS 容器id
	private double weight;				// 称重重量
	private int weightUnitType;			// 重量单位类型(1：千克，2：克，3：磅)
	private double minPackQty; // 最小包装数
	private int repeatStatus; // 复盘状态（ 0：未复盘，1：已复盘 ）
	private double repeatQty; // 复盘数
	private int repeatUserId; // 复盘人
	private String repeatDate; // 复盘时间

	// 临时字段，不存表
	private String stockName; // 仓库名称
	private String mtlNumber; // 物料代码
	private String mtlName; // 物料名称
	private String unitName; // 单位名称
	private String fmodel; // 物料规格
	private String fbatchNo; // 物料批次
	private String accountName; // 账号名称

	private Stock stock;
	private StockArea stockArea;
	private StorageRack storageRack;
	private StockPosition stockPos;
	private Container container;
	private ICItem icItem;
	private User user;

	private boolean check; // 是否选中

	public ICInvBackup() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getFinterId() {
		return finterId;
	}

	public void setFinterId(int finterId) {
		this.finterId = finterId;
	}

	public int getStockId() {
		return stockId;
	}

	public void setStockId(int stockId) {
		this.stockId = stockId;
	}

	public int getMtlId() {
		return mtlId;
	}

	public void setMtlId(int mtlId) {
		this.mtlId = mtlId;
	}

	public double getFauxQty() {
		return fauxQty;
	}

	public void setFauxQty(double fauxQty) {
		this.fauxQty = fauxQty;
	}

	public double getFauxQtyAct() {
		return fauxQtyAct;
	}

	public void setFauxQtyAct(double fauxQtyAct) {
		this.fauxQtyAct = fauxQtyAct;
	}

	public double getFauxCheckQty() {
		return fauxCheckQty;
	}

	public void setFauxCheckQty(double fauxCheckQty) {
		this.fauxCheckQty = fauxCheckQty;
	}

	public String getStockName() {
		return stockName;
	}

	public void setStockName(String stockName) {
		this.stockName = stockName;
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

	public String getFmodel() {
		return fmodel;
	}

	public void setFmodel(String fmodel) {
		this.fmodel = fmodel;
	}

	public String getFbatchNo() {
		return fbatchNo;
	}

	public void setFbatchNo(String fbatchNo) {
		this.fbatchNo = fbatchNo;
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

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public int getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(int createUserId) {
		this.createUserId = createUserId;
	}

	public int getToK3() {
		return toK3;
	}

	public void setToK3(int toK3) {
		this.toK3 = toK3;
	}

	public int getStockPosId() {
		return stockPosId;
	}

	public void setStockPosId(int stockPosId) {
		this.stockPosId = stockPosId;
	}

	public int getStockId_wms() {
		return stockId_wms;
	}

	public void setStockId_wms(int stockId_wms) {
		this.stockId_wms = stockId_wms;
	}

	public int getStockAreaId_wms() {
		return stockAreaId_wms;
	}

	public void setStockAreaId_wms(int stockAreaId_wms) {
		this.stockAreaId_wms = stockAreaId_wms;
	}

	public int getStorageRackId_wms() {
		return storageRackId_wms;
	}

	public void setStorageRackId_wms(int storageRackId_wms) {
		this.storageRackId_wms = storageRackId_wms;
	}

	public int getStockPosId_wms() {
		return stockPosId_wms;
	}

	public void setStockPosId_wms(int stockPosId_wms) {
		this.stockPosId_wms = stockPosId_wms;
	}

	public int getContainerId() {
		return containerId;
	}

	public void setContainerId(int containerId) {
		this.containerId = containerId;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
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

	public ICItem getIcItem() {
		return icItem;
	}

	public void setIcItem(ICItem icItem) {
		this.icItem = icItem;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public int getWeightUnitType() {
		return weightUnitType;
	}

	public void setWeightUnitType(int weightUnitType) {
		this.weightUnitType = weightUnitType;
	}

	public double getMinPackQty() {
		return minPackQty;
	}

	public void setMinPackQty(double minPackQty) {
		this.minPackQty = minPackQty;
	}

	public int getRepeatStatus() {
		return repeatStatus;
	}

	public void setRepeatStatus(int repeatStatus) {
		this.repeatStatus = repeatStatus;
	}

	public double getRepeatQty() {
		return repeatQty;
	}

	public void setRepeatQty(double repeatQty) {
		this.repeatQty = repeatQty;
	}

	public int getRepeatUserId() {
		return repeatUserId;
	}

	public void setRepeatUserId(int repeatUserId) {
		this.repeatUserId = repeatUserId;
	}

	public String getRepeatDate() {
		return repeatDate;
	}

	public void setRepeatDate(String repeatDate) {
		this.repeatDate = repeatDate;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
