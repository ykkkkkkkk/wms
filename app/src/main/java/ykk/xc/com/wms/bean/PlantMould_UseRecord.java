package ykk.xc.com.wms.bean;

import java.io.Serializable;

/**
 * 工装模具表使用记录表
 * @author Administrator
 *
 */
public class PlantMould_UseRecord implements Serializable {

	private int id;
	private int parentId; 				// 物料id
	private char sourceType;			// 来源数据类型	1：容器，2：工装模具
	private char useType;				// 使用类型	L：领用，H：归还
	private String useBegDate;			// 使用开始日期
	private String useEndDate;			// 使用结束日期
	private String useMan;				// 使用人
	private int useCount;				// 使用次数
	private String purpose;				// 用途
	private String sourceNo;			// 对应的工单号
	private int useStockId;				// 领用仓库id
	private int useStockAreaId;			// 领用库区id
	private int useStorageRackId;		// 领用货架id
	private int useStockPosId;			// 领用库位id
	private int useContainerId;			// 领用容器id
	private int returnStockId;			// 归还仓库id
	private int returnStockAreaId;		// 归还库区id
	private int returnStorageRackId;	// 归还货架id
	private int returnStockPosId;		// 归还库位id
	private int returnContainerId;		// 归还容器id
	private String remark;				// 备注
	private String createDate;			// 创建日期

	private Stock useStock;
	private StockArea useStockArea;
	private StorageRack useStorageRack;
	private StockPosition useStockPos;
	private Container useContainer;
	private Stock returnStock;
	private StockArea returnStockArea;
	private StorageRack returnStorageRack;
	private StockPosition returnStockPos;
	private Container returnContainer;

	public PlantMould_UseRecord() {
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

	public char getSourceType() {
		return sourceType;
	}

	public void setSourceType(char sourceType) {
		this.sourceType = sourceType;
	}

	public char getUseType() {
		return useType;
	}

	public void setUseType(char useType) {
		this.useType = useType;
	}

	public String getUseBegDate() {
		return useBegDate;
	}

	public void setUseBegDate(String useBegDate) {
		this.useBegDate = useBegDate;
	}

	public String getUseEndDate() {
		return useEndDate;
	}

	public void setUseEndDate(String useEndDate) {
		this.useEndDate = useEndDate;
	}

	public String getUseMan() {
		return useMan;
	}

	public void setUseMan(String useMan) {
		this.useMan = useMan;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public String getSourceNo() {
		return sourceNo;
	}

	public void setSourceNo(String sourceNo) {
		this.sourceNo = sourceNo;
	}

	public int getUseStockId() {
		return useStockId;
	}

	public void setUseStockId(int useStockId) {
		this.useStockId = useStockId;
	}

	public int getUseStockAreaId() {
		return useStockAreaId;
	}

	public void setUseStockAreaId(int useStockAreaId) {
		this.useStockAreaId = useStockAreaId;
	}

	public int getUseStorageRackId() {
		return useStorageRackId;
	}

	public void setUseStorageRackId(int useStorageRackId) {
		this.useStorageRackId = useStorageRackId;
	}

	public int getUseStockPosId() {
		return useStockPosId;
	}

	public void setUseStockPosId(int useStockPosId) {
		this.useStockPosId = useStockPosId;
	}

	public int getUseContainerId() {
		return useContainerId;
	}

	public void setUseContainerId(int useContainerId) {
		this.useContainerId = useContainerId;
	}

	public int getReturnStockId() {
		return returnStockId;
	}

	public void setReturnStockId(int returnStockId) {
		this.returnStockId = returnStockId;
	}

	public int getReturnStockAreaId() {
		return returnStockAreaId;
	}

	public void setReturnStockAreaId(int returnStockAreaId) {
		this.returnStockAreaId = returnStockAreaId;
	}

	public int getReturnStorageRackId() {
		return returnStorageRackId;
	}

	public void setReturnStorageRackId(int returnStorageRackId) {
		this.returnStorageRackId = returnStorageRackId;
	}

	public int getReturnStockPosId() {
		return returnStockPosId;
	}

	public void setReturnStockPosId(int returnStockPosId) {
		this.returnStockPosId = returnStockPosId;
	}

	public int getReturnContainerId() {
		return returnContainerId;
	}

	public void setReturnContainerId(int returnContainerId) {
		this.returnContainerId = returnContainerId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public Stock getUseStock() {
		return useStock;
	}

	public void setUseStock(Stock useStock) {
		this.useStock = useStock;
	}

	public StockArea getUseStockArea() {
		return useStockArea;
	}

	public void setUseStockArea(StockArea useStockArea) {
		this.useStockArea = useStockArea;
	}

	public StorageRack getUseStorageRack() {
		return useStorageRack;
	}

	public void setUseStorageRack(StorageRack useStorageRack) {
		this.useStorageRack = useStorageRack;
	}

	public StockPosition getUseStockPos() {
		return useStockPos;
	}

	public void setUseStockPos(StockPosition useStockPos) {
		this.useStockPos = useStockPos;
	}

	public Container getUseContainer() {
		return useContainer;
	}

	public void setUseContainer(Container useContainer) {
		this.useContainer = useContainer;
	}

	public Stock getReturnStock() {
		return returnStock;
	}

	public void setReturnStock(Stock returnStock) {
		this.returnStock = returnStock;
	}

	public StockArea getReturnStockArea() {
		return returnStockArea;
	}

	public void setReturnStockArea(StockArea returnStockArea) {
		this.returnStockArea = returnStockArea;
	}

	public StorageRack getReturnStorageRack() {
		return returnStorageRack;
	}

	public void setReturnStorageRack(StorageRack returnStorageRack) {
		this.returnStorageRack = returnStorageRack;
	}

	public StockPosition getReturnStockPos() {
		return returnStockPos;
	}

	public void setReturnStockPos(StockPosition returnStockPos) {
		this.returnStockPos = returnStockPos;
	}

	public Container getReturnContainer() {
		return returnContainer;
	}

	public void setReturnContainer(Container returnContainer) {
		this.returnContainer = returnContainer;
	}

	public int getUseCount() {
		return useCount;
	}

	public void setUseCount(int useCount) {
		this.useCount = useCount;
	}


}

