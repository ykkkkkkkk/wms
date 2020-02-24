package ykk.xc.com.wms.bean;

import java.io.Serializable;

/**
 * @Description:供应商
 *
 * @author qxp 2019年3月7日 上午11:44:25
 */
public class Supplier implements Serializable {
	/* id */
	private int id;
	/* 供应商K3id */
	private int supplierId;
	/* 供应商编码 */
	private String fnumber;
	/* 供应商名称 */
	private String fname;
	/* 供应商来源账套Id */
	private int sourceAcctId;
	/* 供应商来源账套名称 */
	private String sourceAcctName;
	/* 供应商对应账套id */
	private int correspondId;
	/* 供应商对应账套名称 */
	private String correspondName;

	/* 创建时间 */
	private String createDate;
	/* 修改时间 */
	private String fModifyDate;
	/*供应商地址*/
	private String supplierAddress;
	/*供应商联系人*/
	private String supplierName;
	/*供应商联系电话*/
	private String supplierPhone;

	/*超收的是否赠送    （赠送：990655，不赠送：990656）*/
	private int isDonate;

	public Supplier() {
		super();
	}

	public int getId() {
		return id;
	}

	public int getSupplierId() {
		return supplierId;
	}

	public String getFnumber() {
		return fnumber;
	}

	public String getFname() {
		return fname;
	}

	public int getSourceAcctId() {
		return sourceAcctId;
	}

	public String getSourceAcctName() {
		return sourceAcctName;
	}

	public int getCorrespondId() {
		return correspondId;
	}

	public String getCorrespondName() {
		return correspondName;
	}

	public String getCreateDate() {
		return createDate;
	}

	public String getfModifyDate() {
		return fModifyDate;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setSupplierId(int supplierId) {
		this.supplierId = supplierId;
	}

	public void setFnumber(String fnumber) {
		this.fnumber = fnumber;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public void setSourceAcctId(int sourceAcctId) {
		this.sourceAcctId = sourceAcctId;
	}

	public void setSourceAcctName(String sourceAcctName) {
		this.sourceAcctName = sourceAcctName;
	}

	public void setCorrespondId(int correspondId) {
		this.correspondId = correspondId;
	}

	public void setCorrespondName(String correspondName) {
		this.correspondName = correspondName;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public void setfModifyDate(String fModifyDate) {
		this.fModifyDate = fModifyDate;
	}

	public String getSupplierAddress() {
		return supplierAddress;
	}

	public void setSupplierAddress(String supplierAddress) {
		this.supplierAddress = supplierAddress;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getSupplierPhone() {
		return supplierPhone;
	}

	public void setSupplierPhone(String supplierPhone) {
		this.supplierPhone = supplierPhone;
	}

	public int getIsDonate() {
		return isDonate;
	}

	public void setIsDonate(int isDonate) {
		this.isDonate = isDonate;
	}


}
