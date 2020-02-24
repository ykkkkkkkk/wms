package ykk.xc.com.wms.bean;

import java.io.Serializable;

/**
 * 库位表 stock_position
 */
public class StockPosition implements Serializable {

    //wms本地库位Id
    private int id;
    //erp库位Id
    private int fitemId;
    //erp库位编码
    private String fnumber;
    //erp库位名称
    private String fname;

    //wms库位编码
    private String stockPositionNumber;
    //wms库位名称
    private String stockPositionName;

    /*货架Id*/
    private int storageRackId;
    /*货架*/
    private StorageRack storageRack;
    /*库区Id*/
    private int stockAreaId;
    /*库区*/
    private StockArea stockArea;
    /*仓库id 记录本地id*/
    private int stockId;
    /*k3仓库id 记录k3仓库id*/
    private int stockK3Id;
    private Stock stock;

    // 仓位组Id
    private int fspGroupId;

    //创建时间
    private String createTime;

    // 临时字段，不存表
    private String className; // 前段用到的，请勿删除

    public StockPosition() {
        super();
    }

    public int getId() {
        return id;
    }

    public int getStockAreaId() {
        return stockAreaId;
    }

    public void setStockAreaId(int stockAreaId) {
        this.stockAreaId = stockAreaId;
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

    public int getStorageRackId() {
        return storageRackId;
    }

    public void setStorageRackId(int storageRackId) {
        this.storageRackId = storageRackId;
    }

    public StorageRack getStorageRack() {
        return storageRack;
    }

    public void setStorageRack(StorageRack storageRack) {
        this.storageRack = storageRack;
    }

    public StockArea getStockArea() {
        return stockArea;
    }

    public void setStockArea(StockArea stockArea) {
        this.stockArea = stockArea;
    }

    public int getStockId() {
        return stockId;
    }

    public void setStockId(int stockId) {
        this.stockId = stockId;
    }

    public int getStockK3Id() {
        return stockK3Id;
    }

    public void setStockK3Id(int stockK3Id) {
        this.stockK3Id = stockK3Id;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public int getFspGroupId() {
        return fspGroupId;
    }

    public void setFspGroupId(int fspGroupId) {
        this.fspGroupId = fspGroupId;
    }

    public String getStockPositionNumber() {
        return stockPositionNumber;
    }

    public void setStockPositionNumber(String stockPositionNumber) {
        this.stockPositionNumber = stockPositionNumber;
    }

    public String getStockPositionName() {
        return stockPositionName;
    }

    public void setStockPositionName(String stockPositionName) {
        this.stockPositionName = stockPositionName;
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

    @Override
    public String toString() {
        return "StockPosition [id=" + id + ", fitemId=" + fitemId + ", fnumber=" + fnumber + ", fname=" + fname
                + ", stockPositionNumber=" + stockPositionNumber + ", stockPositionName=" + stockPositionName
                + ", storageRackId=" + storageRackId + ", storageRack=" + storageRack + ", stockAreaId=" + stockAreaId
                + ", stockArea=" + stockArea + ", stockId=" + stockId + ", stockK3Id=" + stockK3Id + ", stock=" + stock
                + ", fspGroupId=" + fspGroupId + ", createTime=" + createTime + "]";
    }

}

