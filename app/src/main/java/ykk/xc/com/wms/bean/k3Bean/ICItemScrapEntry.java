package ykk.xc.com.wms.bean.k3Bean;

import java.io.Serializable;

import ykk.xc.com.wms.bean.Department;
import ykk.xc.com.wms.bean.Unit;

/**
 *  补料单分录实体
 */
public class ICItemScrapEntry implements Serializable {
	private static final long serialVersionUID = 1L;

	private int finterID;
	private int fentryID;
	private int fitemID;
	private int funitID;
	private double fqty;		// 报废数
	private double fqtySupply;	// 补料数
	private int fworkShop;		// 部门id

	private ICItemScrap icitemScrap;
	private ICItem icItem;
	private Unit unit;
	private Department dept;

	// 临时字段，不存表
	private double usableQty;	// 可用数

	public ICItemScrapEntry() {
		super();
	}

	public int getFinterID() {
		return finterID;
	}

	public void setFinterID(int finterID) {
		this.finterID = finterID;
	}

	public int getFentryID() {
		return fentryID;
	}

	public void setFentryID(int fentryID) {
		this.fentryID = fentryID;
	}

	public int getFitemID() {
		return fitemID;
	}

	public void setFitemID(int fitemID) {
		this.fitemID = fitemID;
	}

	public int getFunitID() {
		return funitID;
	}

	public void setFunitID(int funitID) {
		this.funitID = funitID;
	}

	public double getFqty() {
		return fqty;
	}

	public void setFqty(double fqty) {
		this.fqty = fqty;
	}

	public double getFqtySupply() {
		return fqtySupply;
	}

	public void setFqtySupply(double fqtySupply) {
		this.fqtySupply = fqtySupply;
	}

	public int getFworkShop() {
		return fworkShop;
	}

	public void setFworkShop(int fworkShop) {
		this.fworkShop = fworkShop;
	}

	public ICItemScrap getIcitemScrap() {
		return icitemScrap;
	}

	public void setIcitemScrap(ICItemScrap icitemScrap) {
		this.icitemScrap = icitemScrap;
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

	public Department getDept() {
		return dept;
	}

	public void setDept(Department dept) {
		this.dept = dept;
	}

	public double getUsableQty() {
		return usableQty;
	}

	public void setUsableQty(double usableQty) {
		this.usableQty = usableQty;
	}




}