package ykk.xc.com.wms.bean;

import java.io.Serializable;

/**
 * 调拨记录表
 */
public class StockTransferRecord implements Serializable {
	private int id;
	private int outStockId; // 调出仓库
	private String outStockNumber;
	private String outStockName;
	private int outStockPosId; // 调出库位
	private String outStockPosNumber;
	private String outStockPosName;
	private int inStockId; // 调入仓库
	private String inStockNumber;
	private String inStockName;
	private int inStockPosId; // 调入库位
	private String inStockPosNumber;
	private String inStockPosName;
	private int icItemId; // 物料
	private String icItemNumber;
	private String icItemName;
	private double allotQty; // 调拨数
	private String timesTamp; // 时间戳
	private int createUserId;
	private String createUserName;
	private String createDate;
	private String accountName; // 账号名称（电商账号，生产账号）

	public StockTransferRecord() {
		super();
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getOutStockId() {
		return outStockId;
	}
	public void setOutStockId(int outStockId) {
		this.outStockId = outStockId;
	}
	public String getOutStockNumber() {
		return outStockNumber;
	}
	public void setOutStockNumber(String outStockNumber) {
		this.outStockNumber = outStockNumber;
	}
	public String getOutStockName() {
		return outStockName;
	}
	public void setOutStockName(String outStockName) {
		this.outStockName = outStockName;
	}
	public int getOutStockPosId() {
		return outStockPosId;
	}
	public void setOutStockPosId(int outStockPosId) {
		this.outStockPosId = outStockPosId;
	}
	public String getOutStockPosNumber() {
		return outStockPosNumber;
	}
	public void setOutStockPosNumber(String outStockPosNumber) {
		this.outStockPosNumber = outStockPosNumber;
	}
	public String getOutStockPosName() {
		return outStockPosName;
	}
	public void setOutStockPosName(String outStockPosName) {
		this.outStockPosName = outStockPosName;
	}
	public int getInStockId() {
		return inStockId;
	}
	public void setInStockId(int inStockId) {
		this.inStockId = inStockId;
	}
	public String getInStockNumber() {
		return inStockNumber;
	}
	public void setInStockNumber(String inStockNumber) {
		this.inStockNumber = inStockNumber;
	}
	public String getInStockName() {
		return inStockName;
	}
	public void setInStockName(String inStockName) {
		this.inStockName = inStockName;
	}
	public int getInStockPosId() {
		return inStockPosId;
	}
	public void setInStockPosId(int inStockPosId) {
		this.inStockPosId = inStockPosId;
	}
	public String getInStockPosNumber() {
		return inStockPosNumber;
	}
	public void setInStockPosNumber(String inStockPosNumber) {
		this.inStockPosNumber = inStockPosNumber;
	}
	public String getInStockPosName() {
		return inStockPosName;
	}
	public void setInStockPosName(String inStockPosName) {
		this.inStockPosName = inStockPosName;
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
	public double getAllotQty() {
		return allotQty;
	}
	public void setAllotQty(double allotQty) {
		this.allotQty = allotQty;
	}
	public String getTimesTamp() {
		return timesTamp;
	}
	public void setTimesTamp(String timesTamp) {
		this.timesTamp = timesTamp;
	}
	public int getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(int createUserId) {
		this.createUserId = createUserId;
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
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

}

