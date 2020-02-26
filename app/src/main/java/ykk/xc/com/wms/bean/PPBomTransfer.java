package ykk.xc.com.wms.bean;

import java.io.Serializable;

/**
 * 投料单调拨单主表实体类
 * @author hongmoon
 *
 */
public class PPBomTransfer implements Serializable {
	private static final long serialVersionUID = 1L;

	//调拨单本地自增id
	private Integer id;

	//源单类型 1：代表投料单-生产任务单，2：代表投料单-委外订单
	private int sourceBillType;

	//调拨单号
	private String billNo;

	//制单日期
	private String billDate;

	//制单人
	private String billCreater;

	//状态 A：代表创建，B：审核，C：代表部分调拨，D：代表完全调拨
	private String billStatus;

	//审核日期
	private String billCheckDate;

	//单据审核人
	private String billChecker;

	//调拨操作日期
	private String billTransferDate;

	//调拨操作人
	private String billTransferMan;

	//调出仓库
	private Integer outStockId;

	private Stock outStock;

	//调入仓库
	private Integer inStockId;

	private Stock inStock;

	//调拨车间/委外供应商
	private Integer deptId;

	private Department dept;

	private Supplier supplier;

	public PPBomTransfer() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getSourceBillType() {
		return sourceBillType;
	}

	public void setSourceBillType(int sourceBillType) {
		this.sourceBillType = sourceBillType;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public String getBillDate() {
		return billDate;
	}

	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}

	public String getBillCreater() {
		return billCreater;
	}

	public void setBillCreater(String billCreater) {
		this.billCreater = billCreater;
	}

	public String getBillStatus() {
		return billStatus;
	}

	public void setBillStatus(String billStatus) {
		this.billStatus = billStatus;
	}

	public Integer getOutStockId() {
		return outStockId;
	}

	public void setOutStockId(Integer outStockId) {
		this.outStockId = outStockId;
	}

	public Stock getOutStock() {
		return outStock;
	}

	public void setOutStock(Stock outStock) {
		this.outStock = outStock;
	}

	public Integer getInStockId() {
		return inStockId;
	}

	public void setInStockId(Integer inStockId) {
		this.inStockId = inStockId;
	}

	public Stock getInStock() {
		return inStock;
	}

	public void setInStock(Stock inStock) {
		this.inStock = inStock;
	}

	public Integer getDeptId() {
		return deptId;
	}

	public void setDeptId(Integer deptId) {
		this.deptId = deptId;
	}

	public Department getDept() {
		return dept;
	}

	public void setDept(Department dept) {
		this.dept = dept;
	}

	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	public String getBillCheckDate() {
		return billCheckDate;
	}

	public void setBillCheckDate(String billCheckDate) {
		this.billCheckDate = billCheckDate;
	}

	public String getBillChecker() {
		return billChecker;
	}

	public void setBillChecker(String billChecker) {
		this.billChecker = billChecker;
	}

	public String getBillTransferDate() {
		return billTransferDate;
	}

	public void setBillTransferDate(String billTransferDate) {
		this.billTransferDate = billTransferDate;
	}

	public String getBillTransferMan() {
		return billTransferMan;
	}

	public void setBillTransferMan(String billTransferMan) {
		this.billTransferMan = billTransferMan;
	}

	@Override
	public String toString() {
		return "PPBomTransfer [id=" + id + ", sourceBillType=" + sourceBillType + ", billNo=" + billNo + ", billDate="
				+ billDate + ", billCreater=" + billCreater + ", billStatus=" + billStatus + ", billCheckDate="
				+ billCheckDate + ", billChecker=" + billChecker + ", billTransferDate=" + billTransferDate
				+ ", billTransferMan=" + billTransferMan + ", outStockId=" + outStockId + ", outStock=" + outStock
				+ ", inStockId=" + inStockId + ", inStock=" + inStock + ", deptId=" + deptId + ", dept=" + dept
				+ ", supplier=" + supplier + "]";
	}

}
