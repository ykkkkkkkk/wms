package ykk.xc.com.wms.bean;

import java.io.Serializable;

/**
 * 容器实体类
 * @author hongmoon
 *
 */
public class Container implements Serializable {
	private static final long serialVersionUID = 1L;

	/*id*/
	private int id;
	/*容器编码*/
	private String fnumber;
	/*容器规格*/
	private String size;
	/*是否固定库位 Y代表是，N代表不是*/
	private String fixStockPosition;
	/*默认仓库ID*/
	private int stockId;
	/*默认库区ID*/
	private int stockAreaId;
	/*默认货架ID*/
	private int storageRackId;
	/*默认库位ID*/
	private int stockPositionId;

	private Stock stock;
	private StockArea stockArea;
	private StorageRack storageRack;
	private StockPosition stockPosition;
	private BarCodeTable barCodeTable;
	//条码表ID
	private int barCodeTableId;
	/*条码*/
	private String barcode;

	public Container() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFnumber() {
		return fnumber;
	}

	public void setFnumber(String fnumber) {
		this.fnumber = fnumber;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getFixStockPosition() {
		return fixStockPosition;
	}

	public void setFixStockPosition(String fixStockPosition) {
		this.fixStockPosition = fixStockPosition;
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

	public int getStockPositionId() {
		return stockPositionId;
	}

	public void setStockPositionId(int stockPositionId) {
		this.stockPositionId = stockPositionId;
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

	public StockPosition getStockPosition() {
		return stockPosition;
	}

	public void setStockPosition(StockPosition stockPosition) {
		this.stockPosition = stockPosition;
	}

	public BarCodeTable getBarCodeTable() {
		return barCodeTable;
	}

	public void setBarCodeTable(BarCodeTable barCodeTable) {
		this.barCodeTable = barCodeTable;
	}

	public int getBarCodeTableId() {
		return barCodeTableId;
	}

	public void setBarCodeTableId(int barCodeTableId) {
		this.barCodeTableId = barCodeTableId;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
}