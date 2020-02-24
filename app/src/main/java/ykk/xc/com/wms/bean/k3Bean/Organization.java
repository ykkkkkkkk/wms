package ykk.xc.com.wms.bean.k3Bean;

import java.io.Serializable;

/**
 * @Description:生产账套客户实体
 *
 * @author qxp 2019年3月5日 上午11:22:50
 */
public class Organization implements Serializable {
	/* 客户内码 */
	private int fitemId;
	/* 客户名称 */
	private String fName;
	/* 客户代码 */
	private String fNumber;
	/* 客户所属账套 (电商：990163。内销：990164。外销：990165) */
	private String belongAcc;

	public Organization() {
		super();
	}

	public int getFitemId() {
		return fitemId;
	}

	public String getfName() {
		return fName;
	}

	public String getfNumber() {
		return fNumber;
	}

	public void setFitemId(int fitemId) {
		this.fitemId = fitemId;
	}

	public void setfName(String fName) {
		this.fName = fName;
	}

	public void setfNumber(String fNumber) {
		this.fNumber = fNumber;
	}

	public String getBelongAcc() {
		return belongAcc;
	}

	public void setBelongAcc(String belongAcc) {
		this.belongAcc = belongAcc;
	}

	@Override
	public String toString() {
		return "Organization [fitemId=" + fitemId + ", fName=" + fName + ", fNumber=" + fNumber + "]";
	}

}
