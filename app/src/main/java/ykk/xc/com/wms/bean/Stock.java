package ykk.xc.com.wms.bean;

import java.io.Serializable;

/**
 * 仓库表 t_stock
 */
public class Stock implements Serializable,Cloneable {
    /*wms本地id*/
    private int id;
    /*erp仓库id*/
    private int fitemId;
    /*erp仓库名称*/
    private String fname;
    /*erp仓库代码*/
    private String fnumber;
    /*wms仓库名称*/
    private String stockName;
    /*wms仓库代码*/
    private String stockNumber;
    /*条码*/
    private String barcode;
    /*是否允许负库存，Y代表允许，N代表不允许*/
    private String funderStock;
    /*是否启用库区管理，Y代表启用，N代表不启用*/
    private String useStockArea;
    /*是否启用货架管理，Y代表启用，N代表不启用*/
    private String useStorageRack;
    //erp仓位组id
    private int fspGroupId;
    //是否进行仓位组管理 1：开启仓位管理   0：不开启仓位管理
    private int fisStockMgr;
    //创建时间
    private String createTime;

    // 临时字段，不存表
    private String className; // 前段用到的，请勿删除

    public Stock() {
        super();
    }

    @Override
    public Object clone() {
        Stock stock = null;
        try{
            stock = (Stock) super.clone();  //浅复制
        }catch(CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return stock;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFitemId() {
        return fitemId;
    }

    public void setFitemId(int fitemId) {
        this.fitemId = fitemId;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getFnumber() {
        return fnumber;
    }

    public void setFnumber(String fnumber) {
        this.fnumber = fnumber;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public String getStockNumber() {
        return stockNumber;
    }

    public void setStockNumber(String stockNumber) {
        this.stockNumber = stockNumber;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getFunderStock() {
        return funderStock;
    }

    public void setFunderStock(String funderStock) {
        this.funderStock = funderStock;
    }

    public String getUseStockArea() {
        return useStockArea;
    }

    public void setUseStockArea(String useStockArea) {
        this.useStockArea = useStockArea;
    }

    public String getUseStorageRack() {
        return useStorageRack;
    }

    public void setUseStorageRack(String useStorageRack) {
        this.useStorageRack = useStorageRack;
    }

    public int getFspGroupId() {
        return fspGroupId;
    }

    public void setFspGroupId(int fspGroupId) {
        this.fspGroupId = fspGroupId;
    }

    public int getFisStockMgr() {
        return fisStockMgr;
    }

    public void setFisStockMgr(int fisStockMgr) {
        this.fisStockMgr = fisStockMgr;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }


}
