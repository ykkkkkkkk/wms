package ykk.xc.com.wms.bean;

import java.io.Serializable;

/**
 * 部门信息	t_Department
 */
public class Department implements Serializable{

	//K3部门id
	private Integer fitemID;
	//部门条码
	private String barcode;
	//K3部门编码
	private String departmentNumber;
	//K3部门名称
	private String departmentName;
	//K3部门使用组织id
	private String departmentUseOrgId;
	//部门属性 1070-车间,1071-非车间
	private int departmentProperty;
	//调入仓库
	private Integer inStockId;
	/*K3数据状态*/
	private String dataStatus;
	/*wms非物理删除标识*/
	private String isDelete;
	/*k3是否禁用*/
	private String enabled;
	//K3修改日期
	private String fModifyDate;
	//前缀标识（用于生产订单生成生产序号时使用）
	private String prefix;
	//是否属于装卸部门，1属于，2不属于
	private int isload;

	public Department() {
		super();
	}

	public Integer getFitemID() {
		return fitemID;
	}
	public void setFitemID(Integer fitemID) {
		this.fitemID = fitemID;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getDepartmentNumber() {
		return departmentNumber;
	}
	public void setDepartmentNumber(String departmentNumber) {
		this.departmentNumber = departmentNumber;
	}

	public int getDepartmentProperty() {
		return departmentProperty;
	}

	public void setDepartmentProperty(int departmentProperty) {
		this.departmentProperty = departmentProperty;
	}
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	public String getDepartmentUseOrgId() {
		return departmentUseOrgId;
	}
	public void setDepartmentUseOrgId(String departmentUseOrgId) {
		this.departmentUseOrgId = departmentUseOrgId;
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

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public Integer getInStockId() {
		return inStockId;
	}

	public void setInStockId(Integer inStockId) {
		this.inStockId = inStockId;
	}

	public int getIsload() {
		return isload;
	}

	public void setIsload(int isload) {
		this.isload = isload;
	}

	@Override
	public String toString() {
		return "Department [fitemID=" + fitemID + ", barcode=" + barcode + ", departmentNumber="
				+ departmentNumber + ", departmentName=" + departmentName + ", departmentUseOrgId=" + departmentUseOrgId
				+ ", departmentProperty=" + departmentProperty + ", inStockId=" + inStockId + ", dataStatus="
				+ dataStatus + ", isDelete=" + isDelete + ", enabled=" + enabled + ", fModifyDate=" + fModifyDate
				+ ", prefix=" + prefix + ", isload=" + isload + "]";
	}

}
