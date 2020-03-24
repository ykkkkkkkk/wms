package ykk.xc.com.wms.bean.k3Bean;

import java.io.Serializable;

import ykk.xc.com.wms.bean.Container;
import ykk.xc.com.wms.bean.Stock;
import ykk.xc.com.wms.bean.StockArea;
import ykk.xc.com.wms.bean.StockPosition;
import ykk.xc.com.wms.bean.StorageRack;
import ykk.xc.com.wms.bean.Unit;
import ykk.xc.com.wms.comm.Comm;

/**
 * @Description:物料实体
 *
 * @author qxp 2019年2月26日 下午4:33:41
 */
public class ICItem implements Serializable{
	/*wms本地id*/
	private int id;
	/* K3物料id */
	private int fitemid;
	/* 物料规格型号 */
	private String fmodel;
	/* 物料名称 */
	private String fname;
	/* 是否禁用 */
	private Short fdeleted;
	/* 物料代码 */
	private String fnumber;
	/* 物料属性id 1:外购,2:自制,3:委外加工*/
	private int ferpclsid;
	/* 物料主计量单位id */
	private int funitid;
	/*物料主计量单位*/
	private Unit unit;

	/* 物料辅计量单位id */
	private int assistUnitid;
	/*物料辅计量单位*/
	private Unit assistUnit;
	/*主辅计量单位换算率 如：主计量单位=辅计量单位X换算率 */
	private double unitConvertRatio;

	/* 物料数量精度 */
	private Short fqtydecimal;
	/* 物料最低库存量 */
	private double flowlimit;
	/* 物料最高库存量 */
	private double fhighlimit;
	/* 物料安全存量 */
	private double fsecinv;
	/* 物料分类id */
	private int ftypeid;

	/* 是否启用批次号管理。Y代表启用，N代表不启用*/
	private String batchManager;

	/* 是否启用序列号管理。Y代表启用，N代表不启用*/
	private String snManager;

	/* 是否赠品，Y代表是，N代表否 */
	private String isComplimentary;

	/* 是否启用保质期管理，Y代表启用，N代表不启用 */
	private String isQualityPeriodManager;
	/* 质保期单位id */
	private Integer qualityPeriodUnitId;
	/*保质期单位*/
	private Unit qualityPeriodUnit;
	/* 质保期 */
	private double qualityPeriod;

	/* 旧物料编码 */
	private String oldItemNumber;
	/* 旧物料名称 */
	private String oldItemName;

	/*默认仓库id*/
	private Integer stockId;
	private Stock stock; // 仓库

	/*默认库区id*/
	private Integer stockAreaId;
	private StockArea stockArea; // 库区

	/*默认货架id*/
	private Integer storageRackId;
	private StorageRack storageRack; // 货架

	/*默认库位id*/
	private Integer stockPositionId;
	private StockPosition stockPos; // 库位

	/* 适用车型  对应数据库F_104*/
	private String suitVehicleType;

	/*功能说明  对应数据库F_108*/
	private String functionDescription;

	private String fbarcode;

	/*默认容器id*/
	private Integer containerId;

	/*默认容器*/
	private Container container;

	/*物料出库规则，FIFO代表先进先出，LIFO代表后进先出，SBFO代表批次指定*/
	private String outStockRule;

	/*最小包装数量*/
	private double minPackQty;

	/*单位重量*/
	private double unitWeight;

	/*是否称重计量，M代表强制称重计量，Y代表参考称重计量，N代表不称重计量*/
	private String calByWeight;

	/*是否按最小包装数量领用，Y代表是，N代表不是*/
	private String packByMinQty;

	/*是否启用容器管理，Y代表启用，N代表不启用*/
	private String useContainer;

	// 临时字段，不加表
	private double inventoryQty; // 即时库存
	private double realQty; //
	private double weight; // 重量
	private String batchCode; // 批次号
	private String snCode;	 // 序列号
	private boolean check; // 是否选中

	public ICItem() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getFitemid() {
		return fitemid;
	}

	public void setFitemid(int fitemid) {
		this.fitemid = fitemid;
	}

	public String getFmodel() {
		return fmodel;
	}

	public void setFmodel(String fmodel) {
		this.fmodel = fmodel;
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public Short getFdeleted() {
		return fdeleted;
	}

	public void setFdeleted(Short fdeleted) {
		this.fdeleted = fdeleted;
	}

	public String getFnumber() {
		return fnumber;
	}

	public void setFnumber(String fnumber) {
		this.fnumber = fnumber;
	}

	public int getFerpclsid() {
		return ferpclsid;
	}

	public void setFerpclsid(int ferpclsid) {
		this.ferpclsid = ferpclsid;
	}

	public int getFunitid() {
		return funitid;
	}

	public void setFunitid(int funitid) {
		this.funitid = funitid;
	}

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	public Short getFqtydecimal() {
		return fqtydecimal;
	}

	public void setFqtydecimal(Short fqtydecimal) {
		this.fqtydecimal = fqtydecimal;
	}

	public double getFlowlimit() {
		return flowlimit;
	}

	public void setFlowlimit(double flowlimit) {
		this.flowlimit = flowlimit;
	}

	public double getFhighlimit() {
		return fhighlimit;
	}

	public void setFhighlimit(double fhighlimit) {
		this.fhighlimit = fhighlimit;
	}

	public double getFsecinv() {
		return fsecinv;
	}

	public void setFsecinv(double fsecinv) {
		this.fsecinv = fsecinv;
	}

	public int getFtypeid() {
		return ftypeid;
	}

	public void setFtypeid(int ftypeid) {
		this.ftypeid = ftypeid;
	}

	public String getBatchManager() {
		return Comm.isNULLS(batchManager);
	}

	public void setBatchManager(String batchManager) {
		this.batchManager = batchManager;
	}

	public String getSnManager() {
		return Comm.isNULLS(snManager);
	}

	public void setSnManager(String snManager) {
		this.snManager = snManager;
	}

	public String getIsComplimentary() {
		return isComplimentary;
	}

	public void setIsComplimentary(String isComplimentary) {
		this.isComplimentary = isComplimentary;
	}

	public String getIsQualityPeriodManager() {
		return Comm.isNULLS(isQualityPeriodManager);
	}

	public void setIsQualityPeriodManager(String isQualityPeriodManager) {
		this.isQualityPeriodManager = isQualityPeriodManager;
	}

	public Integer getQualityPeriodUnitId() {
		return qualityPeriodUnitId;
	}

	public void setQualityPeriodUnitId(Integer qualityPeriodUnitId) {
		this.qualityPeriodUnitId = qualityPeriodUnitId;
	}

	public Unit getQualityPeriodUnit() {
		return qualityPeriodUnit;
	}

	public void setQualityPeriodUnit(Unit qualityPeriodUnit) {
		this.qualityPeriodUnit = qualityPeriodUnit;
	}

	public double getQualityPeriod() {
		return qualityPeriod;
	}

	public void setQualityPeriod(double qualityPeriod) {
		this.qualityPeriod = qualityPeriod;
	}

	public String getOldItemNumber() {
		return oldItemNumber;
	}

	public void setOldItemNumber(String oldItemNumber) {
		this.oldItemNumber = oldItemNumber;
	}

	public String getOldItemName() {
		return oldItemName;
	}

	public void setOldItemName(String oldItemName) {
		this.oldItemName = oldItemName;
	}

	public Integer getStockId() {
		return stockId;
	}

	public void setStockId(Integer stockId) {
		this.stockId = stockId;
	}

	public Stock getStock() {
		return stock;
	}

	public void setStock(Stock stock) {
		this.stock = stock;
	}

	public Integer getStockAreaId() {
		return stockAreaId;
	}

	public void setStockAreaId(Integer stockAreaId) {
		this.stockAreaId = stockAreaId;
	}

	public StockArea getStockArea() {
		return stockArea;
	}

	public void setStockArea(StockArea stockArea) {
		this.stockArea = stockArea;
	}

	public Integer getStorageRackId() {
		return storageRackId;
	}

	public void setStorageRackId(Integer storageRackId) {
		this.storageRackId = storageRackId;
	}

	public StorageRack getStorageRack() {
		return storageRack;
	}

	public void setStorageRack(StorageRack storageRack) {
		this.storageRack = storageRack;
	}

	public Integer getStockPositionId() {
		return stockPositionId;
	}

	public void setStockPositionId(Integer stockPositionId) {
		this.stockPositionId = stockPositionId;
	}

	public StockPosition getStockPos() {
		return stockPos;
	}

	public void setStockPos(StockPosition stockPos) {
		this.stockPos = stockPos;
	}

	public double getInventoryQty() {
		return inventoryQty;
	}

	public void setInventoryQty(double inventoryQty) {
		this.inventoryQty = inventoryQty;
	}

	public String getSuitVehicleType() {
		return suitVehicleType;
	}

	public void setSuitVehicleType(String suitVehicleType) {
		this.suitVehicleType = suitVehicleType;
	}

	public String getFunctionDescription() {
		return functionDescription;
	}

	public void setFunctionDescription(String functionDescription) {
		this.functionDescription = functionDescription;
	}

	public String getFbarcode() {
		return fbarcode;
	}

	public void setFbarcode(String fbarcode) {
		this.fbarcode = fbarcode;
	}

	public boolean isCheck() {
		return check;
	}

	public void setCheck(boolean check) {
		this.check = check;
	}

	public Integer getContainerId() {
		return containerId;
	}

	public void setContainerId(Integer containerId) {
		this.containerId = containerId;
	}

	public Container getContainer() {
		return container;
	}

	public void setContainer(Container container) {
		this.container = container;
	}

	public String getOutStockRule() {
		return outStockRule;
	}

	public void setOutStockRule(String outStockRule) {
		this.outStockRule = outStockRule;
	}

	public double getMinPackQty() {
		return minPackQty;
	}

	public void setMinPackQty(double minPackQty) {
		this.minPackQty = minPackQty;
	}

	public double getUnitWeight() {
		return unitWeight;
	}

	public void setUnitWeight(double unitWeight) {
		this.unitWeight = unitWeight;
	}

	public String getCalByWeight() {
		return Comm.isNULL2(calByWeight,"N");
	}

	public void setCalByWeight(String calByWeight) {
		this.calByWeight = calByWeight;
	}

	public String getPackByMinQty() {
		return packByMinQty;
	}

	public void setPackByMinQty(String packByMinQty) {
		this.packByMinQty = packByMinQty;
	}

	public int getAssistUnitid() {
		return assistUnitid;
	}

	public void setAssistUnitid(int assistUnitid) {
		this.assistUnitid = assistUnitid;
	}

	public Unit getAssistUnit() {
		return assistUnit;
	}

	public void setAssistUnit(Unit assistUnit) {
		this.assistUnit = assistUnit;
	}

	public double getUnitConvertRatio() {
		return unitConvertRatio;
	}

	public void setUnitConvertRatio(double unitConvertRatio) {
		this.unitConvertRatio = unitConvertRatio;
	}

	public String getUseContainer() {
		return Comm.isNULLS(useContainer);
	}

	public void setUseContainer(String useContainer) {
		this.useContainer = useContainer;
	}

	public double getRealQty() {
		return realQty;
	}

	public void setRealQty(double realQty) {
		this.realQty = realQty;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
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

}