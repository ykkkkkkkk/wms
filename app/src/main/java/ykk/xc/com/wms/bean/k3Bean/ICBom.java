package ykk.xc.com.wms.bean.k3Bean;

import java.io.Serializable;

import ykk.xc.com.wms.bean.Stock;
import ykk.xc.com.wms.bean.StockPosition;
import ykk.xc.com.wms.bean.Unit;

/**
 * 金蝶BOM单
 * @author hongmoon
 *
 */
public class ICBom implements Serializable {
	private static final long serialVersionUID = 1L;

	private int finterId;			// 单据内码
	private int fitemId;			// 物料id
	private double fqty;			// 数量
	private int funitId;			// 单位

	// 临时字段，不加表
	private int mtlStockId;			// 物料默认仓库id
	private int mtlStockPosId;			// 物料默认库位id

	private Stock stock;
	private StockPosition stockPos;
	private ICItem icItem;
	private Unit unit;

	public ICBom() {
		super();
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

	public double getFqty() {
		return fqty;
	}

	public void setFqty(double fqty) {
		this.fqty = fqty;
	}

	public int getFunitId() {
		return funitId;
	}

	public void setFunitId(int funitId) {
		this.funitId = funitId;
	}

	public int getMtlStockId() {
		return mtlStockId;
	}

	public void setMtlStockId(int mtlStockId) {
		this.mtlStockId = mtlStockId;
	}

	public int getMtlStockPosId() {
		return mtlStockPosId;
	}

	public void setMtlStockPosId(int mtlStockPosId) {
		this.mtlStockPosId = mtlStockPosId;
	}

	public Stock getStock() {
		return stock;
	}

	public void setStock(Stock stock) {
		this.stock = stock;
	}

	public StockPosition getStockPos() {
		return stockPos;
	}

	public void setStockPos(StockPosition stockPos) {
		this.stockPos = stockPos;
	}

	public ICItem getIcItem() {
		return icItem;
	}

	public void setIcItem(ICItem icItem) {
		this.icItem = icItem;
	}

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

}
