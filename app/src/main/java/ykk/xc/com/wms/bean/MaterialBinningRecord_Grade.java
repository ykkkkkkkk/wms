package ykk.xc.com.wms.bean;

import java.io.Serializable;

/**
 * 物料装箱等级表
 *
 * @author Administrator
 *
 */
public class MaterialBinningRecord_Grade implements Serializable {

	private int boxBarCodeId;			// 当前箱码id
	private int parentBoxBarCodeId;		// 上级箱码id
	private String barcode;				// 条码号
	private int isLast;					// 是否为末级包装( 装物料的包装 )
	private String createDate;			// 创建日期
	private String createUserName;		// 创建人

	// 临时字段，不存表
	private int level; 	// 记录等级的


	public MaterialBinningRecord_Grade() {
		super();
	}

	public int getBoxBarCodeId() {
		return boxBarCodeId;
	}

	public void setBoxBarCodeId(int boxBarCodeId) {
		this.boxBarCodeId = boxBarCodeId;
	}

	public int getParentBoxBarCodeId() {
		return parentBoxBarCodeId;
	}

	public void setParentBoxBarCodeId(int parentBoxBarCodeId) {
		this.parentBoxBarCodeId = parentBoxBarCodeId;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public int getIsLast() {
		return isLast;
	}

	public void setIsLast(int isLast) {
		this.isLast = isLast;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}





}
