package ykk.xc.com.wms.bean.k3Bean;

import java.io.Serializable;

/**
 * 自定义的	退货理由表
 * @author Administrator
 * k3的库里面并没有这个表，是查询(t_Item)表的条件为FItemClassID = 3002
 */
public class ReturnReason implements Serializable {
	private int fitemId;
	private String fname; // 退货理由

	public int getFitemId() {
		return fitemId;
	}
	public String getFname() {
		return fname;
	}
	public void setFitemId(int fitemId) {
		this.fitemId = fitemId;
	}
	public void setFname(String fname) {
		this.fname = fname;
	}

	public ReturnReason() {
		super();
	}

}
