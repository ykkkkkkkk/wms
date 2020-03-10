package ykk.xc.com.wms.bean;

import java.io.Serializable;

public class Unit implements Serializable{
	/*单位ID*/
	private int id;
	/*单位内码*/
	private int funitId;
	/*分配内码*/
	private String fmasterId;
	/*单位编码*/
	private String unitNumber;
	/*单位名称*/
	private String unitName;
	/*K3数据状态*/
	private String dataStatus;
	/*wms非物理删除标识*/
	private String isDelete;
	/*k3是否禁用*/
	private String enabled;
	/*修改日期*/
	private String fModifyDate;

	public Unit() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getFunitId() {
		return funitId;
	}

	public void setFunitId(int funitId) {
		this.funitId = funitId;
	}

	public String getFmasterId() {
		return fmasterId;
	}

	public void setFmasterId(String fmasterId) {
		this.fmasterId = fmasterId;
	}

	public String getUnitNumber() {
		return unitNumber;
	}

	public void setUnitNumber(String unitNumber) {
		this.unitNumber = unitNumber;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getDataStatus() {
		return dataStatus;
	}

	public void setDataStatus(String dataStatus) {
		this.dataStatus = dataStatus;
	}

	public String getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(String isDelete) {
		this.isDelete = isDelete;
	}

	public String getEnabled() {
		return enabled;
	}

	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}

	public String getfModifyDate() {
		return fModifyDate;
	}

	public void setfModifyDate(String fModifyDate) {
		this.fModifyDate = fModifyDate;
	}

	@Override
	public String toString() {
		return "Unit [id=" + id + ", funitId=" + funitId + ", fmasterId=" + fmasterId + ", unitNumber=" + unitNumber
				+ ", unitName=" + unitName + ", dataStatus=" + dataStatus + ", isDelete=" + isDelete + ", enabled="
				+ enabled + ", fModifyDate=" + fModifyDate + "]";
	}

}
