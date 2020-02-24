package ykk.xc.com.wms.bean.k3Bean;

import java.io.Serializable;

/**
 * @Description:k3单位
 *
 * @author 2019年5月27日 下午3:07:19
 */
public class MeasureUnit implements Serializable {

	/*单位内码  */
	private int fMeasureUnitID;
	/*单位名称*/
	private String fName;
	/*单位代码*/
	private String fNumber;
	/*单位内码2，同单位ID*/
	private int fitemID;

	public MeasureUnit() {
		super();
	}

	public int getfMeasureUnitID() {
		return fMeasureUnitID;
	}

	public void setfMeasureUnitID(int fMeasureUnitID) {
		this.fMeasureUnitID = fMeasureUnitID;
	}

	public String getfName() {
		return fName;
	}

	public void setfName(String fName) {
		this.fName = fName;
	}

	public String getfNumber() {
		return fNumber;
	}

	public void setfNumber(String fNumber) {
		this.fNumber = fNumber;
	}

	public int getFitemID() {
		return fitemID;
	}

	public void setFitemID(int fitemID) {
		this.fitemID = fitemID;
	}

	@Override
	public String toString() {
		return "MeasureUnit [fMeasureUnitID=" + fMeasureUnitID + ", fName=" + fName + ", fNumber=" + fNumber
				+ ", fitemID=" + fitemID + "]";
	}

}
