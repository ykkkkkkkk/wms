package ykk.xc.com.wms.bean;

/**
 * 系统设置实体类
 * @author Administrator
 *
 */
public class SystemSet {

	/*id*/
	private Integer id;

	/*设置项*/
	private EnumDict setItem;

	/*设置项的值*/
	private Integer value;

	/*设置项功能描述*/
	private String describe;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public EnumDict getSetItem() {
		return setItem;
	}

	public void setSetItem(EnumDict setItem) {
		this.setItem = setItem;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public SystemSet() {
		super();
	}

	@Override
	public String toString() {
		return "SystemSet [id=" + id + ", setItem=" + setItem + ", value=" + value + ", describe=" + describe + "]";
	}

}