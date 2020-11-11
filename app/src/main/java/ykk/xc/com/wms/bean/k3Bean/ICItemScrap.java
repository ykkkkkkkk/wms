package ykk.xc.com.wms.bean.k3Bean;

import java.io.Serializable;

/**
 * 补料单实体
 *
 */
public class ICItemScrap implements Serializable {
	private static final long serialVersionUID = 1L;

	private int finterID;
	private String fbillNO;
	private int ftranType;
	private int fstatus;
	private int fbillerID;
	private String fdate;
	private int fselTranType;
	private int ftypeID;

	public ICItemScrap() {
		super();
	}

	public int getFinterID() {
		return finterID;
	}

	public void setFinterID(int finterID) {
		this.finterID = finterID;
	}

	public String getFbillNO() {
		return fbillNO;
	}

	public void setFbillNO(String fbillNO) {
		this.fbillNO = fbillNO;
	}

	public int getFtranType() {
		return ftranType;
	}

	public void setFtranType(int ftranType) {
		this.ftranType = ftranType;
	}

	public int getFstatus() {
		return fstatus;
	}

	public void setFstatus(int fstatus) {
		this.fstatus = fstatus;
	}

	public int getFbillerID() {
		return fbillerID;
	}

	public void setFbillerID(int fbillerID) {
		this.fbillerID = fbillerID;
	}

	public String getFdate() {
		return fdate;
	}

	public void setFdate(String fdate) {
		this.fdate = fdate;
	}

	public int getFselTranType() {
		return fselTranType;
	}

	public void setFselTranType(int fselTranType) {
		this.fselTranType = fselTranType;
	}

	public int getFtypeID() {
		return ftypeID;
	}

	public void setFtypeID(int ftypeID) {
		this.ftypeID = ftypeID;
	}



}