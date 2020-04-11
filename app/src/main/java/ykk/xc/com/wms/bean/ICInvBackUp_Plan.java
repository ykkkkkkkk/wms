package ykk.xc.com.wms.bean;

import java.io.Serializable;
import java.util.List;

/**
 * WMS盘点方案表
 * @author Administrator
 *
 */
public class ICInvBackUp_Plan implements Serializable {
	private static final long serialVersionUID = 1L;

	private int id;				// 自增长id
	private String fname;		// 方案名称
	private String remark;		// 备注
	private String createDate;	// 创建日期
	private int createUserId;	// 创建人id
	private String status;		// 状态 	A:创建，B:审核，C:未审核，E:关闭

	private List<Stock> listStock;

	public ICInvBackUp_Plan() {
		super();
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFname() {
		return fname;
	}
	public void setFname(String fname) {
		this.fname = fname;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public int getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(int createUserId) {
		this.createUserId = createUserId;
	}
	public List<Stock> getListStock() {
		return listStock;
	}
	public void setListStock(List<Stock> listStock) {
		this.listStock = listStock;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}


}

