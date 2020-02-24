package ykk.xc.com.wms.bean;

import java.io.Serializable;

/**
 * 货架实体类
 * @author hongmoon
 *
 */
public class StorageRack implements Serializable {

	/*id*/
	private int id;

	/*货架编号*/
	private String fnumber;

	/*货架层数*/
	private int floorNumber;

	/*货架库区Id*/
	private int stockAreaId;

	/*库区*/
	private StockArea stockArea;
	/*仓库id 记录本地id*/
	private int stockId;
	/*k3仓库id 记录k3仓库id*/
	private int stockK3Id;
	private Stock stock;

	// 仓位组Id
	private int fspGroupId;

	// 临时字段，不存表
	private String className; // 前段用到的，请勿删除

	public StorageRack() {
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

	public int getFloorNumber() {
		return floorNumber;
	}

	public void setFloorNumber(int floorNumber) {
		this.floorNumber = floorNumber;
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

	public int getStockId() {
		return stockId;
	}

	public void setStockId(int stockId) {
		this.stockId = stockId;
	}

	public int getStockK3Id() {
		return stockK3Id;
	}

	public void setStockK3Id(int stockK3Id) {
		this.stockK3Id = stockK3Id;
	}

	public Stock getStock() {
		return stock;
	}

	public void setStock(Stock stock) {
		this.stock = stock;
	}

	public int getFspGroupId() {
		return fspGroupId;
	}

	public void setFspGroupId(int fspGroupId) {
		this.fspGroupId = fspGroupId;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	@Override
	public String toString() {
		return "StorageRack [id=" + id + ", fnumber=" + fnumber + ", floorNumber=" + floorNumber + ", stockAreaId="
				+ stockAreaId + ", stockArea=" + stockArea + ", stockId=" + stockId + ", stockK3Id=" + stockK3Id
				+ ", stock=" + stock + ", fspGroupId=" + fspGroupId + "]";
	}


}