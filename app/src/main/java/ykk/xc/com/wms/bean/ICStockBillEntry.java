package ykk.xc.com.wms.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ykk.xc.com.wms.bean.k3Bean.ICItem;
import ykk.xc.com.wms.comm.Comm;

/**
 * Wms 本地的出入库	Entry表
 * @author Administrator
 *
 */
public class ICStockBillEntry implements Serializable {
	private int id; 					//
	private int icstockBillId;			// 主表id
	private int finterId;				// 主表id
	private int fitemId;				// 物料id
	private int fentryId;				// 分录id
	private int fdcStockId;				// 调入仓库id
	private int fdcSPId;				// 调入库位id
	private int fscStockId;				// 调出仓库id
	private int fscSPId;				// 调出库位id
	private String fbatchNo;			// 批次
	private double fqty;				// 数量
	private double fprice;				// 单价
	private double ftaxRate;			// 税率
	private double ftaxPrice;			// 含税单价
	private int funitId;				// 单位id
	private int fsourceInterId;			// 来源内码id
	private int fsourceEntryId;			// 来源分录id
	private int fsourceTranType;		// 来源类型
	private String fsourceBillNo;		// 来源单号
	private double fsourceQty;			// 来源单数量
	private int fdetailId; 				// 来源分类唯一行标识
	// 调入仓库组
	private int stockId_wms; 			// WMS 调入--仓库id
	private int stockAreaId_wms; 		// WMS 调入--库区id
	private int storageRackId_wms; 		// WMS 调入--货架id
	private int stockPosId_wms; 		// WMS 调入--库位id
	private int containerId; 			// WMS 调入--容器id
	// 调出仓库组
	private int stockId2_wms; 			// WMS 调出--仓库id
	private int stockAreaId2_wms; 		// WMS 调出--库区id
	private int storageRackId2_wms; 	// WMS 调出--货架id
	private int stockPosId2_wms; 		// WMS 调出--库位id
	private int containerId2; 			// WMS 调出--容器id

	private String mtlNumber;			// 物料代码
	private String mtlName;				// 物料名称
	private String fmode;				// 物料规格
	private String inStockName;			// 调入仓库名称
	private String inStockPosName;		// 调入库位名称
	private String outStockName;		// 调出仓库名称
	private String outStockPosName;		// 调出库位名称
	private String unitName;			// 单位名称
	private String remark;				// 备注
	private double weight;				// 称重重量
	private int weightUnitType;			// 重量单位类型(1：千克，2：克，3：磅)
	private double referenceNum;		// 称重后计算得出的参考数量
	private double qcPassQty;			// 质检合格数
	private String fkfDate; 			// 生产/采购日期
	private int fkfPeriod;				// 保质期
	private int sourceThisId;			// 来源本身id
	private int boxBarCodeId;			// 装箱表id

	private Stock stock;
	private StockArea stockArea;
	private StorageRack storageRack;
	private StockPosition stockPos;
	private Container container;
	private Stock stock2;
	private StockArea stockArea2;
	private StorageRack storageRack2;
	private StockPosition stockPos2;
	private Container container2;
	private ICItem icItem;
	private ICStockBill icstockBill;
	private BoxBarCode boxBarCode;

	// 临时字段，不存表
	private boolean showButton; 		// 是否显示操作按钮
	private double allotQty; // 调拨数
	private String smBatchCode; // 扫码的批次号
	private String smSnCode; // 扫码的序列号
	private String strBatchCode; // 拼接的批次号
	private String k3Number; // 主表的k3Number
	private double inventoryNowQty; // 当前扫码的可用库存数
	private String pushDownType;	// 下推业务类型 =（01：外购入库，02：其他入库）

	private ICStockBillEntry sourceThis; // 来源本身对象
	private List<ICStockBillEntry_Barcode> icstockBillEntry_Barcodes; // 条码记录

	public ICStockBillEntry() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIcstockBillId() {
		return icstockBillId;
	}

	public void setIcstockBillId(int icstockBillId) {
		this.icstockBillId = icstockBillId;
	}

	public int getFinterId() {
		return finterId;
	}

	public void setFinterId(int finterId) {
		this.finterId = finterId;
	}

	public int getFitemId() {
		return fitemId;
	}

	public void setFitemId(int fitemId) {
		this.fitemId = fitemId;
	}

	public int getFentryId() {
		return fentryId;
	}

	public void setFentryId(int fentryId) {
		this.fentryId = fentryId;
	}

	public int getFdcStockId() {
		return fdcStockId;
	}

	public void setFdcStockId(int fdcStockId) {
		this.fdcStockId = fdcStockId;
	}

	public int getFdcSPId() {
		return fdcSPId;
	}

	public void setFdcSPId(int fdcSPId) {
		this.fdcSPId = fdcSPId;
	}

	public int getFscStockId() {
		return fscStockId;
	}

	public void setFscStockId(int fscStockId) {
		this.fscStockId = fscStockId;
	}

	public int getFscSPId() {
		return fscSPId;
	}

	public void setFscSPId(int fscSPId) {
		this.fscSPId = fscSPId;
	}

	public String getFbatchNo() {
		return fbatchNo;
	}

	public void setFbatchNo(String fbatchNo) {
		this.fbatchNo = fbatchNo;
	}

	public double getFqty() {
		return fqty;
	}

	public void setFqty(double fqty) {
		this.fqty = fqty;
	}

	public double getFprice() {
		return fprice;
	}

	public void setFprice(double fprice) {
		this.fprice = fprice;
	}

	public int getFunitId() {
		return funitId;
	}

	public void setFunitId(int funitId) {
		this.funitId = funitId;
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

	public String getFmode() {
		return Comm.isNULLS(fmode);
	}

	public void setFmode(String fmode) {
		this.fmode = fmode;
	}

	public String getInStockName() {
		return inStockName;
	}

	public void setInStockName(String inStockName) {
		this.inStockName = inStockName;
	}

	public String getInStockPosName() {
		return inStockPosName;
	}

	public void setInStockPosName(String inStockPosName) {
		this.inStockPosName = inStockPosName;
	}

	public String getOutStockName() {
		return outStockName;
	}

	public void setOutStockName(String outStockName) {
		this.outStockName = outStockName;
	}

	public String getOutStockPosName() {
		return outStockPosName;
	}

	public void setOutStockPosName(String outStockPosName) {
		this.outStockPosName = outStockPosName;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public double getFtaxRate() {
		return ftaxRate;
	}

	public void setFtaxRate(double ftaxRate) {
		this.ftaxRate = ftaxRate;
	}

	public double getFtaxPrice() {
		return ftaxPrice;
	}

	public void setFtaxPrice(double ftaxPrice) {
		this.ftaxPrice = ftaxPrice;
	}

	public int getFsourceEntryId() {
		return fsourceEntryId;
	}

	public void setFsourceEntryId(int fsourceEntryId) {
		this.fsourceEntryId = fsourceEntryId;
	}

	public int getFsourceTranType() {
		return fsourceTranType;
	}

	public void setFsourceTranType(int fsourceTranType) {
		this.fsourceTranType = fsourceTranType;
	}

	public String getFsourceBillNo() {
		return fsourceBillNo;
	}

	public void setFsourceBillNo(String fsourceBillNo) {
		this.fsourceBillNo = fsourceBillNo;
	}

	public int getFsourceInterId() {
		return fsourceInterId;
	}

	public void setFsourceInterId(int fsourceInterId) {
		this.fsourceInterId = fsourceInterId;
	}

	public int getFdetailId() {
		return fdetailId;
	}

	public void setFdetailId(int fdetailId) {
		this.fdetailId = fdetailId;
	}

	public boolean isShowButton() {
		return showButton;
	}

	public void setShowButton(boolean showButton) {
		this.showButton = showButton;
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

	public double getFsourceQty() {
		return fsourceQty;
	}

	public void setFsourceQty(double fsourceQty) {
		this.fsourceQty = fsourceQty;
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

	public double getReferenceNum() {
		return referenceNum;
	}

	public void setReferenceNum(double referenceNum) {
		this.referenceNum = referenceNum;
	}

	public double getQcPassQty() {
		return qcPassQty;
	}

	public void setQcPassQty(double qcPassQty) {
		this.qcPassQty = qcPassQty;
	}

	public String getFkfDate() {
		return fkfDate;
	}

	public void setFkfDate(String fkfDate) {
		this.fkfDate = fkfDate;
	}

	public ICStockBill getIcstockBill() {
		return icstockBill;
	}

	public void setIcstockBill(ICStockBill icstockBill) {
		this.icstockBill = icstockBill;
	}

	public double getAllotQty() {
		return allotQty;
	}

	public void setAllotQty(double allotQty) {
		this.allotQty = allotQty;
	}

	public int getFkfPeriod() {
		return fkfPeriod;
	}

	public void setFkfPeriod(int fkfPeriod) {
		this.fkfPeriod = fkfPeriod;
	}

	public List<ICStockBillEntry_Barcode> getIcstockBillEntry_Barcodes() {
		if(icstockBillEntry_Barcodes == null) {
			icstockBillEntry_Barcodes = new ArrayList<>();
		}
		return icstockBillEntry_Barcodes;
	}

	public void setIcstockBillEntry_Barcodes(List<ICStockBillEntry_Barcode> icstockBillEntry_Barcodes) {
		this.icstockBillEntry_Barcodes = icstockBillEntry_Barcodes;
	}

	public String getSmBatchCode() {
		return smBatchCode;
	}

	public void setSmBatchCode(String smBatchCode) {
		this.smBatchCode = smBatchCode;
	}

	public String getSmSnCode() {
		return smSnCode;
	}

	public void setSmSnCode(String smSnCode) {
		this.smSnCode = smSnCode;
	}

	public String getStrBatchCode() {
		// 存在大写的逗号（，）,且大于1
		if(Comm.isNULLS(strBatchCode).indexOf("，") > -1 && Comm.isNULLS(strBatchCode).length() > 0) {
			return strBatchCode.substring(0, strBatchCode.length()-1);
		}
		return strBatchCode;
	}

	public void setStrBatchCode(String strBatchCode) {
		this.strBatchCode = strBatchCode;
	}

	public int getStockId2_wms() {
		return stockId2_wms;
	}

	public void setStockId2_wms(int stockId2_wms) {
		this.stockId2_wms = stockId2_wms;
	}

	public int getStockAreaId2_wms() {
		return stockAreaId2_wms;
	}

	public void setStockAreaId2_wms(int stockAreaId2_wms) {
		this.stockAreaId2_wms = stockAreaId2_wms;
	}

	public int getStorageRackId2_wms() {
		return storageRackId2_wms;
	}

	public void setStorageRackId2_wms(int storageRackId2_wms) {
		this.storageRackId2_wms = storageRackId2_wms;
	}

	public int getStockPosId2_wms() {
		return stockPosId2_wms;
	}

	public void setStockPosId2_wms(int stockPosId2_wms) {
		this.stockPosId2_wms = stockPosId2_wms;
	}

	public int getContainerId2() {
		return containerId2;
	}

	public void setContainerId2(int containerId2) {
		this.containerId2 = containerId2;
	}

	public Stock getStock2() {
		return stock2;
	}

	public void setStock2(Stock stock2) {
		this.stock2 = stock2;
	}

	public StockArea getStockArea2() {
		return stockArea2;
	}

	public void setStockArea2(StockArea stockArea2) {
		this.stockArea2 = stockArea2;
	}

	public StorageRack getStorageRack2() {
		return storageRack2;
	}

	public void setStorageRack2(StorageRack storageRack2) {
		this.storageRack2 = storageRack2;
	}

	public StockPosition getStockPos2() {
		return stockPos2;
	}

	public void setStockPos2(StockPosition stockPos2) {
		this.stockPos2 = stockPos2;
	}

	public Container getContainer2() {
		return container2;
	}

	public void setContainer2(Container container2) {
		this.container2 = container2;
	}

	public int getSourceThisId() {
		return sourceThisId;
	}

	public void setSourceThisId(int sourceThisId) {
		this.sourceThisId = sourceThisId;
	}

	public ICStockBillEntry getSourceThis() {
		return sourceThis;
	}

	public void setSourceThis(ICStockBillEntry sourceThis) {
		this.sourceThis = sourceThis;
	}

	public String getK3Number() {
		return k3Number;
	}

	public void setK3Number(String k3Number) {
		this.k3Number = k3Number;
	}

	public double getInventoryNowQty() {
		return inventoryNowQty;
	}

	public void setInventoryNowQty(double inventoryNowQty) {
		this.inventoryNowQty = inventoryNowQty;
	}

	public int getBoxBarCodeId() {
		return boxBarCodeId;
	}

	public void setBoxBarCodeId(int boxBarCodeId) {
		this.boxBarCodeId = boxBarCodeId;
	}

	public BoxBarCode getBoxBarCode() {
		return boxBarCode;
	}

	public void setBoxBarCode(BoxBarCode boxBarCode) {
		this.boxBarCode = boxBarCode;
	}

	public String getPushDownType() {
		return pushDownType;
	}

	public void setPushDownType(String pushDownType) {
		this.pushDownType = pushDownType;
	}

}
