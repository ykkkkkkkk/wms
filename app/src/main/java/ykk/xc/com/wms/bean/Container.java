package ykk.xc.com.wms.bean;

import java.io.Serializable;

/**
 * 容器实体类
 * @author hongmoon
 *
 */
public class Container implements Serializable {

	/*id*/
	private Integer id;

	/*容器编码*/
	private String fnumber;

	/*容器规格*/
	private String size;

	public Container() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFnumber() {
		return fnumber;
	}

	public void setFnumber(String fnumber) {
		this.fnumber = fnumber;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	@Override
	public String toString() {
		return "Container [id=" + id + ", fnumber=" + fnumber + ", size=" + size + "]";
	}


}
