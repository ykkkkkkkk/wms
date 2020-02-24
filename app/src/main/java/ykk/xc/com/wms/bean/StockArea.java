package ykk.xc.com.wms.bean;

import java.io.Serializable;

/**
 * 库区表 stock_area
 */
public class StockArea implements Serializable {

	/*wms本地id*/
	private int id;
	/*库区代码*/
	private String fnumber;
	/*库区名称*/
	private String fname;

	/*条码*/
	private String barcode;
	/*仓库id 记录本地id*/
	private int stockId;
	/*k3仓库id 记录k3仓库id*/
	private int stockK3Id;
	private Stock stock;

	// 临时字段，不存表
	private String className; // 前段用到的，请勿删除

	public StockArea() {
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
	public String getFnumber() {
		return fnumber;
	}
	public void setFnumber(String fnumber) {
		this.fnumber = fnumber;
	}
	public String getFname() {
		return fname;
	}
	public void setFname(String fname) {
		this.fname = fname;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public Stock getStock() {
		return stock;
	}
	public void setStock(Stock stock) {
		this.stock = stock;
	}
	public int getStockK3Id() {
		return stockK3Id;
	}
	public void setStockK3Id(int stockK3Id) {
		this.stockK3Id = stockK3Id;
	}
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	@Override
	public String toString() {
		return "StockArea [id=" + id + ", fnumber=" + fnumber + ", fname=" + fname + ", barcode=" + barcode
				+ ", stockId=" + stockId + ", stockK3Id=" + stockK3Id + ", stock=" + stock + "]";
	}

}
