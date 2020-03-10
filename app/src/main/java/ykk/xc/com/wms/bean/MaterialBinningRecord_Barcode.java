package ykk.xc.com.wms.bean;

import java.io.Serializable;

/**
 * 物料装箱记录类
 *
 * @author Administrator
 *
 */
public class MaterialBinningRecord_Barcode implements Serializable {
	private static final long serialVersionUID = 1L;

	private int id;
	private int parentId;					//
	private String barcode;					// 条码号
	private String batchCode;				// 批次号
	private String snCode;					// 序列号
	private double fqty;					// 使用数
	private char isUniqueness; 				// 是否唯一条码（Y:是，N：否）
	private String createUserName;			// 创建人名称
	private String createDate;				// 创建日期

	// 临时字段，不存表


	public MaterialBinningRecord_Barcode() {
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

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
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

	public double getFqty() {
		return fqty;
	}

	public void setFqty(double fqty) {
		this.fqty = fqty;
	}

	public char getIsUniqueness() {
		return isUniqueness;
	}

	public void setIsUniqueness(char isUniqueness) {
		this.isUniqueness = isUniqueness;
	}

	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}





}
