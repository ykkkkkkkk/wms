package ykk.xc.com.wms.bean.k3Bean;

import java.io.Serializable;

/**
 * 快递公司
 * @author Administrator
 *
 */
public class ExpressCompany implements Serializable {
	private int fitemId;
	private String fnumber;
	private String fname;
	
	public ExpressCompany() {
		super();
	}
	
	public int getFitemId() {
		return fitemId;
	}
	public void setFitemId(int fitemId) {
		this.fitemId = fitemId;
	}
	public String getFnumber() {
		return fnumber;
	}
	public void setFnumber(String fnumber) {
		this.fnumber = fnumber;
	}
	public String getFname() {
		return fname;
	}
	public void setFname(String fname) {
		this.fname = fname;
	}
	
	
}
