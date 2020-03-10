package ykk.xc.com.wms.bean;

import java.io.Serializable;

import ykk.xc.com.wms.bean.k3Bean.ICItem;
import ykk.xc.com.wms.comm.Comm;

/**
 * 条码表
 * @author Administrator
 *
 */
public class BarCodeTable implements Serializable {

	/*id*/
	private int id;
	/*序列号*/
	private String snCode;
	/*批次号*/
	private String batchCode;
	/*方案id*/
	/**
	 * 11代表物料
	 * 21代表生产任务单
	 */
	private int caseId;
	/*关联单据id*/
	private int relationBillId;
	/*关联单据号*/
	private String relationBillNumber;
	/*k3对应单据分录的id值*/
	private int relationBillEntryId;
	/*打印次数*/
	private int printNumber;
	/*项目id*/
	private int icItemId;
	/*项目代码*/
	private String icItemNumber;
	/*项目名称*/
	private String icItemName;
	private ICItem icItem;
	/*条码*/
	private String barcode;
	/*创建时间*/
	private String createDateTime;
	/*创建人名称*/
	private String createrName;
	/*条码数量*/
	private double barcodeQty;

	// 临时字段，不存表
	private String relationObj; // 关联对象

	public BarCodeTable() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSnCode() {
		return Comm.isNULLS(snCode);
	}

	public void setSnCode(String snCode) {
		this.snCode = snCode;
	}

	public String getBatchCode() {
		return Comm.isNULLS(batchCode);
	}

	public void setBatchCode(String batchCode) {
		this.batchCode = batchCode;
	}

	public int getCaseId() {
		return caseId;
	}

	public void setCaseId(int caseId) {
		this.caseId = caseId;
	}

	public int getRelationBillId() {
		return relationBillId;
	}

	public void setRelationBillId(int relationBillId) {
		this.relationBillId = relationBillId;
	}

	public String getRelationBillNumber() {
		return relationBillNumber;
	}

	public void setRelationBillNumber(String relationBillNumber) {
		this.relationBillNumber = relationBillNumber;
	}

	public int getRelationBillEntryId() {
		return relationBillEntryId;
	}

	public void setRelationBillEntryId(int relationBillEntryId) {
		this.relationBillEntryId = relationBillEntryId;
	}

	public int getPrintNumber() {
		return printNumber;
	}

	public void setPrintNumber(int printNumber) {
		this.printNumber = printNumber;
	}

	public int getIcItemId() {
		return icItemId;
	}

	public void setIcItemId(int icItemId) {
		this.icItemId = icItemId;
	}

	public String getIcItemNumber() {
		return icItemNumber;
	}

	public void setIcItemNumber(String icItemNumber) {
		this.icItemNumber = icItemNumber;
	}

	public String getIcItemName() {
		return icItemName;
	}

	public void setIcItemName(String icItemName) {
		this.icItemName = icItemName;
	}

	public ICItem getIcItem() {
		return icItem;
	}

	public void setIcItem(ICItem icItem) {
		this.icItem = icItem;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getCreateDateTime() {
		return createDateTime;
	}

	public void setCreateDateTime(String createDateTime) {
		this.createDateTime = createDateTime;
	}

	public String getCreaterName() {
		return createrName;
	}

	public void setCreaterName(String createrName) {
		this.createrName = createrName;
	}

	public double getBarcodeQty() {
		return barcodeQty;
	}

	public void setBarcodeQty(double barcodeQty) {
		this.barcodeQty = barcodeQty;
	}

	public String getRelationObj() {
		return relationObj;
	}

	public void setRelationObj(String relationObj) {
		this.relationObj = relationObj;
	}

}
