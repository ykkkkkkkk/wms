package ykk.xc.com.wms.bean.k3Bean;

import java.io.Serializable;

/**
 * @Description:客户
 *
 * @author qxp 2019年3月7日 下午5:17:16
 */
public class Customer implements Serializable {

	private Integer id;
	/* 客户K3Id */
	private int fitemId;
	/* 客户名称 */
	private String fname;
	/* 客户代码 */
	private String fnumber;
	/* 客户来源账套Id */
	private int sourceAcctId;
	/* 客户来源账套名称 */
	private String sourceAcctName;
	/* 客户对应账套id */
	private int correspondId;
	/* 客户对应账套名称 */
	private String correspondName;
	/* 对应账套数据库实例名称 */
	private String correspondDBName;

	private String createDate;

	private String fModifyDate;

	/* 用于接收k3客户地址信息 */
	private String customerAddress;
	/* 用于接收k3客户电话信息 */
	private String customerPhone;
	/* 用于接收k3客户收件人姓名 */
	private String customerName;

	// 临时字段，不存表

	/* 客户所属账套 (电商：990163。内销：990164。外销：990165) */
	private String belongAcc;

	public Customer() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getFitemId() {
		return fitemId;
	}

	public void setFitemId(int fitemId) {
		this.fitemId = fitemId;
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getFnumber() {
		return fnumber;
	}

	public void setFnumber(String fnumber) {
		this.fnumber = fnumber;
	}

	public int getSourceAcctId() {
		return sourceAcctId;
	}

	public void setSourceAcctId(int sourceAcctId) {
		this.sourceAcctId = sourceAcctId;
	}

	public String getSourceAcctName() {
		return sourceAcctName;
	}

	public void setSourceAcctName(String sourceAcctName) {
		this.sourceAcctName = sourceAcctName;
	}

	public int getCorrespondId() {
		return correspondId;
	}

	public void setCorrespondId(int correspondId) {
		this.correspondId = correspondId;
	}

	public String getCorrespondName() {
		return correspondName;
	}

	public void setCorrespondName(String correspondName) {
		this.correspondName = correspondName;
	}

	public String getCorrespondDBName() {
		return correspondDBName;
	}

	public void setCorrespondDBName(String correspondDBName) {
		this.correspondDBName = correspondDBName;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getfModifyDate() {
		return fModifyDate;
	}

	public void setfModifyDate(String fModifyDate) {
		this.fModifyDate = fModifyDate;
	}

	public String getCustomerAddress() {
		return customerAddress;
	}

	public void setCustomerAddress(String customerAddress) {
		this.customerAddress = customerAddress;
	}

	public String getCustomerPhone() {
		return customerPhone;
	}

	public void setCustomerPhone(String customerPhone) {
		this.customerPhone = customerPhone;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getBelongAcc() {
		return belongAcc;
	}

	public void setBelongAcc(String belongAcc) {
		this.belongAcc = belongAcc;
	}


}
