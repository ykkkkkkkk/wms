package ykk.xc.com.wms.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 包装物条码类，用于记录对每个包装物使用流水号生成唯一条码
 * @author Administrator
 *
 */
public class BoxBarCode implements Serializable {
	/*id*/
	private int id;
	/*包装物id*/
	private int boxId;
	/*包装物生成的唯一码*/
	private String barCode;
	/*	箱子的状态( 0代表创建，1:开箱， 2:封箱  )    */
	private int status;
	/*箱子净重*/
	private double roughWeight;
	/*包装物*/
	private Box box;

	/*生码日期*/
	private String createDateTime;
	/*打印次数*/
	private int printNumber;
	private double realWeight;	// 实际重量（千克）
	private String realVolume;	// 实际体积
	private int isInStock;	// 是否入库
	private int isOutStock;	// 是否出库

	// 临时数据, 不存表
	List<MaterialBinningRecord> listMbr; // 物料装箱记录

	public BoxBarCode() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getBoxId() {
		return boxId;
	}

	public void setBoxId(int boxId) {
		this.boxId = boxId;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public double getRoughWeight() {
		return roughWeight;
	}

	public void setRoughWeight(double roughWeight) {
		this.roughWeight = roughWeight;
	}

	public Box getBox() {
		return box;
	}

	public void setBox(Box box) {
		this.box = box;
	}

	public String getCreateDateTime() {
		return createDateTime;
	}

	public void setCreateDateTime(String createDateTime) {
		this.createDateTime = createDateTime;
	}

	public int getPrintNumber() {
		return printNumber;
	}

	public void setPrintNumber(int printNumber) {
		this.printNumber = printNumber;
	}

	public double getRealWeight() {
		return realWeight;
	}

	public void setRealWeight(double realWeight) {
		this.realWeight = realWeight;
	}

	public String getRealVolume() {
		return realVolume;
	}

	public void setRealVolume(String realVolume) {
		this.realVolume = realVolume;
	}

	public List<MaterialBinningRecord> getListMbr() {
		return listMbr;
	}

	public void setListMbr(List<MaterialBinningRecord> listMbr) {
		this.listMbr = listMbr;
	}

	public int getIsInStock() {
		return isInStock;
	}

	public void setIsInStock(int isInStock) {
		this.isInStock = isInStock;
	}

	public int getIsOutStock() {
		return isOutStock;
	}

	public void setIsOutStock(int isOutStock) {
		this.isOutStock = isOutStock;
	}
}