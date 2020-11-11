package ykk.xc.com.wms.bean;

import java.io.Serializable;

/**
 * Wms 本地的装卸表	主表
 * @author Administrator
 *
 */
public class ICChange implements Serializable {
	private static final long serialVersionUID = 1L;

	private int id;					// wms自增长id
	private int fid;
	private int fclassTypeID;
	private int ftranType;
	private String fbillNo;
	private String fdate;
	private double ffee;
	private int fcurrencyID;
	private double fexchangeRate;
	private String fnote;
	private int fcheck;
	private int fbillerID;
	private int fempID;
	private int fstatus;
	private int fdeptID;
	private String fcheckDate;
	private int fcheckerID;
	private int fexchangeRateType;
	
	private int createUserId;
	private String createUserName;
	private String createDate;
	
	public ICChange() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getFid() {
		return fid;
	}

	public void setFid(int fid) {
		this.fid = fid;
	}

	public int getFclassTypeID() {
		return fclassTypeID;
	}

	public void setFclassTypeID(int fclassTypeID) {
		this.fclassTypeID = fclassTypeID;
	}

	public int getFtranType() {
		return ftranType;
	}

	public void setFtranType(int ftranType) {
		this.ftranType = ftranType;
	}

	public String getFbillNo() {
		return fbillNo;
	}

	public void setFbillNo(String fbillNo) {
		this.fbillNo = fbillNo;
	}

	public String getFdate() {
		return fdate;
	}

	public void setFdate(String fdate) {
		this.fdate = fdate;
	}

	public double getFfee() {
		return ffee;
	}

	public void setFfee(double ffee) {
		this.ffee = ffee;
	}

	public int getFcurrencyID() {
		return fcurrencyID;
	}

	public void setFcurrencyID(int fcurrencyID) {
		this.fcurrencyID = fcurrencyID;
	}

	public double getFexchangeRate() {
		return fexchangeRate;
	}

	public void setFexchangeRate(double fexchangeRate) {
		this.fexchangeRate = fexchangeRate;
	}

	public String getFnote() {
		return fnote;
	}

	public void setFnote(String fnote) {
		this.fnote = fnote;
	}

	public int getFcheck() {
		return fcheck;
	}

	public void setFcheck(int fcheck) {
		this.fcheck = fcheck;
	}

	public int getFbillerID() {
		return fbillerID;
	}

	public void setFbillerID(int fbillerID) {
		this.fbillerID = fbillerID;
	}

	public int getFempID() {
		return fempID;
	}

	public void setFempID(int fempID) {
		this.fempID = fempID;
	}

	public int getFstatus() {
		return fstatus;
	}

	public void setFstatus(int fstatus) {
		this.fstatus = fstatus;
	}

	public int getFdeptID() {
		return fdeptID;
	}

	public void setFdeptID(int fdeptID) {
		this.fdeptID = fdeptID;
	}

	public String getFcheckDate() {
		return fcheckDate;
	}

	public void setFcheckDate(String fcheckDate) {
		this.fcheckDate = fcheckDate;
	}

	public int getFcheckerID() {
		return fcheckerID;
	}

	public void setFcheckerID(int fcheckerID) {
		this.fcheckerID = fcheckerID;
	}

	public int getFexchangeRateType() {
		return fexchangeRateType;
	}

	public void setFexchangeRateType(int fexchangeRateType) {
		this.fexchangeRateType = fexchangeRateType;
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
	
	
	
}
