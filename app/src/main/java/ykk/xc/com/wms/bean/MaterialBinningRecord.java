package ykk.xc.com.wms.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ykk.xc.com.wms.bean.k3Bean.ICItem;

/**
 * 物料装箱记录类
 *
 * @author Administrator
 *
 */
public class MaterialBinningRecord implements Serializable {
	private static final long serialVersionUID = 1L;

	private int id;
	private int status;						//
	private char type; 						//
	private char packType;					// 包装类型（A:单装，B:混装）
	private int boxId;						// 包装箱id
	private int boxBarCodeId; 				// 包装条码id
	private int parentBoxBarCodeId;			// 上级--包装条码id
	private int fitemId;					// 物料id
	private double fqty;					// 包装数
	private int fsourceInterId;				// 来源单表头id
	private int fsourceEntryId;				// 来源单分录id
	private String fsourceNo;				// 来源单号
	private double fsourceQty;				// 来源数量
	private double fsourceHightLimitQty;	// 来源入库上限数量
	private String createDate;				// 创建日期
	private int createUserId;				// 创建人id
	private String modifyDate;				// 修改日期
	private int modifyUserId;				// 修改人id

	private BoxBarCode boxBarCode;
	private Box box;
	private ICItem icItem;

	// 临时字段，不存表
	private List<MaterialBinningRecord_Barcode> mbrBarcodes;
	private String createUserName;	// 创建人名称
	private double useableQty;		// 可用数
    private int rowNo;	// 前端行号
	private int autoCreateParentBoxBarCodeId; // 记录自动生码的上级箱码id
	private String parentBarcode;	// 前端上级箱码

	public MaterialBinningRecord() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public char getType() {
		return type;
	}

	public void setType(char type) {
		this.type = type;
	}

	public char getPackType() {
		return packType;
	}

	public void setPackType(char packType) {
		this.packType = packType;
	}

	public int getBoxBarCodeId() {
		return boxBarCodeId;
	}

	public void setBoxBarCodeId(int boxBarCodeId) {
		this.boxBarCodeId = boxBarCodeId;
	}

	public int getParentBoxBarCodeId() {
		return parentBoxBarCodeId;
	}

	public void setParentBoxBarCodeId(int parentBoxBarCodeId) {
		this.parentBoxBarCodeId = parentBoxBarCodeId;
	}

	public int getFitemId() {
		return fitemId;
	}

	public void setFitemId(int fitemId) {
		this.fitemId = fitemId;
	}

	public double getFqty() {
		return fqty;
	}

	public void setFqty(double fqty) {
		this.fqty = fqty;
	}

	public int getFsourceInterId() {
		return fsourceInterId;
	}

	public void setFsourceInterId(int fsourceInterId) {
		this.fsourceInterId = fsourceInterId;
	}

	public int getFsourceEntryId() {
		return fsourceEntryId;
	}

	public void setFsourceEntryId(int fsourceEntryId) {
		this.fsourceEntryId = fsourceEntryId;
	}

	public String getFsourceNo() {
		return fsourceNo;
	}

	public void setFsourceNo(String fsourceNo) {
		this.fsourceNo = fsourceNo;
	}

	public double getFsourceQty() {
		return fsourceQty;
	}

	public void setFsourceQty(double fsourceQty) {
		this.fsourceQty = fsourceQty;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public int getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(int createUserId) {
		this.createUserId = createUserId;
	}

	public String getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
	}

	public int getModifyUserId() {
		return modifyUserId;
	}

	public void setModifyUserId(int modifyUserId) {
		this.modifyUserId = modifyUserId;
	}

	public BoxBarCode getBoxBarCode() {
		return boxBarCode;
	}

	public void setBoxBarCode(BoxBarCode boxBarCode) {
		this.boxBarCode = boxBarCode;
	}

	public ICItem getIcItem() {
		return icItem;
	}

	public void setIcItem(ICItem icItem) {
		this.icItem = icItem;
	}

	public List<MaterialBinningRecord_Barcode> getMbrBarcodes() {
		if(mbrBarcodes == null) {
			mbrBarcodes = new ArrayList<>() ;
		}
		return mbrBarcodes;
	}

	public void setMbrBarcodes(List<MaterialBinningRecord_Barcode> mbrBarcodes) {
		this.mbrBarcodes = mbrBarcodes;
	}

	public int getBoxId() {
		return boxId;
	}

	public void setBoxId(int boxId) {
		this.boxId = boxId;
	}

	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	public Box getBox() {
		return box;
	}


	public void setBox(Box box) {
		this.box = box;
	}

	public double getUseableQty() {
		return useableQty;
	}


	public void setUseableQty(double useableQty) {
		this.useableQty = useableQty;
	}

	public double getFsourceHightLimitQty() {
		return fsourceHightLimitQty;
	}


	public void setFsourceHightLimitQty(double fsourceHightLimitQty) {
		this.fsourceHightLimitQty = fsourceHightLimitQty;
	}

    public int getRowNo() {
        return rowNo;
    }


    public void setRowNo(int rowNo) {
        this.rowNo = rowNo;
    }

	public String getParentBarcode() {
		return parentBarcode;
	}


	public void setParentBarcode(String parentBarcode) {
		this.parentBarcode = parentBarcode;
	}

	public int getAutoCreateParentBoxBarCodeId() {
		return autoCreateParentBoxBarCodeId;
	}


	public void setAutoCreateParentBoxBarCodeId(int autoCreateParentBoxBarCodeId) {
		this.autoCreateParentBoxBarCodeId = autoCreateParentBoxBarCodeId;
	}

}
