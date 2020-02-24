package ykk.xc.com.wms.bean;

import java.io.Serializable;

/**
 * 交货方式
 * @author Administrator
 *
 */
public class AssistInfo implements Serializable {
	private int id; // id
	private String parentId; // 父类id
	private String categoryName; // 类别名称
	private String uniquenessId; // 唯一id
	private String fName; // 交货名称
	private String fNumber; // 编码

	public AssistInfo() {
		super();
	}

	public int getId() {
		return id;
	}

	public String getParentId() {
		return parentId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public String getUniquenessId() {
		return uniquenessId;
	}

	public String getfName() {
		return fName;
	}

	public String getfNumber() {
		return fNumber;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public void setUniquenessId(String uniquenessId) {
		this.uniquenessId = uniquenessId;
	}

	public void setfName(String fName) {
		this.fName = fName;
	}

	public void setfNumber(String fNumber) {
		this.fNumber = fNumber;
	}

	@Override
	public String toString() {
		return "AssistInfo [id=" + id + ", parentId=" + parentId + ", categoryName=" + categoryName + ", uniquenessId="
				+ uniquenessId + ", fName=" + fName + ", fNumber=" + fNumber + "]";
	}
	
}
