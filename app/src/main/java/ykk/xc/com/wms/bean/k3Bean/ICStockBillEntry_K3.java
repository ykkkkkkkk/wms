package ykk.xc.com.wms.bean.k3Bean;

import java.io.Serializable;

import ykk.xc.com.wms.bean.Stock;

public class ICStockBillEntry_K3 implements Serializable {
    /* 单据内码 */
    private int finterid;
    /* 分录号 */
    private int fentryid;
    /* 物料内码 */
    private int fitemid;
    /* 申请数量 */
    private double fqtymust;
    /* 实际数量 */
    private double fqty;
    /* 单价 */
    private double fprice;
    /* 批次 */
    private String fbatchno;
    /* 成本 */
    private double famount;
    /* 备注 */
    private String fnote;
    /* 原单内码 */
    private int fscbillinterid;
    /* 原单单号 */
    private String fscbillno;
    /* 单位内码 */
    private int funitid;
    /* 单位成本 */
    private double fauxprice;
    /* 辅助实际数量 */
    private double fauxqty;
    /* 辅助账存数量 */
    private double fauxqtymust;
    /* 实存数量 */
    private double fqtyactual;
    /* 辅助实存数量 */
    private double fauxqtyactual;
    /* 计划价 */
    private double fplanprice;
    /* 辅助计划价 */
    private double fauxplanprice;
    /* 原分录号 */
    private int fsourceentryid;
    /* 提交数量 */
    private double fcommitqty;
    /* 辅助提交数量 */
    private double fauxcommitqty;
    /* 生产/采购日期 */
    private String fkfdate;
    /* 保质期 */
    private int fkfperiod;
    /* 目标仓位 */
    private int fdcspid;
    /* 源仓位 */
    private int fscspid;
    /* 代销单价 */
    private double fconsignprice;
    /* 代销金额 */
    private double fconsignamount;
    /* 加工费 */
    private double fprocesscost;
    /* 材料费 */
    private double fmaterialcost;
    /* 税额 */
    private double ftaxamount;
    /* 对应代码 */
    private String fmapnumber;
    /* 对应名称 */
    private String fmapname;
    /* 拆单源单行号 */
    private int forgbillentryid;
    /* 工序 */
    private int foperid;
    /* 计划价金额 */
    private double fplanamount;
    /* 委外加工入库单增加加工单价 */
    private double fprocessprice;
    /* 税率 */
    private double ftaxrate;
    /* 序列号 */
    private int fsnlistid;
    /* 调拨金额 */
    private double famtref;
    /* 辅助属性 */
    private int fauxpropid;
    /* 基本调拨单价 */
    private double fpriceref;
    /* 调拨单价 */
    private double fauxpriceref;
    /* 交货日期 */
    private String ffetchdate;
    /* 基本单位开票数量 */
    private double fqtyinvoice;
    /* 基本开票数量 */
    private double fqtyinvoicebase;
    /* 换算率 */
    private double fseccoefficient;
    /* 辅助数量 */
    private double fsecqty;
    /* 辅助执行数量 */
    private double fseccommitqty;
    /* 源单类型 */
    private int fsourcetrantype;
    /* 源单内码 */
    private int fsourceinterid;
    /* 源单单号 */
    private String fsourcebillno;
    /* 合同内码 */
    private int fcontractinterid;
    /* 合同分录 */
    private int fcontractentryid;
    /* 合同单号 */
    private String fcontractbillno;
    /* 生产任务单号 */
    private String ficmobillno;
    /* 领料单=任务单内码；委外出入库单=投料单内码 */
    private int ficmointerid;
    /* 投料单分录号 */
    private int fppbomentryid;
    /* 订单内码 */
    private int forderinterid;
    /* 订单分录 */
    private int forderentryid;
    /* 订单单号 */
    private String forderbillno;
    /* 已钩稽数量 */
    private double fallhookqty;
    /* 已钩稽金额 */
    private double fallhookamount;
    /* 本期钩稽数量 */
    private double fcurrenthookqty;
    /* 本期钩稽金额 */
    private double fcurrenthookamount;
    /* 已钩稽金额(本位币) */
    private double fstdallhookamount;
    /* 本期钩稽金额(本位币) */
    private double fstdcurrenthookamount;
    /* 调出仓库 */
    private int fscstockid;
    /* 调入仓库 */
    private int fdcstockid;
    /* 有效期至 */
    private String fperioddate;
    /* 成本对象组 */
    private int fcostobjgroupid;
    /* 成本对象 */
    private int fcostobjid;
    /* 分录内码 */
    private int fdetailid;
    /* 是否返工 */
    private int freproducetype;
    /* 客户BOM */
    private int fbominterid;
    /* 折扣率 */
    private double fdiscountrate;
    /* 折扣额 */
    private double fdiscountamount;
    /* 特价ID */
    private int fsepcialsaleid;
    /* 基本出库数量 */
    private double foutcommitqty;
    /* 辅助出库数量 */
    private double foutseccommitqty;
    /* 基本调拨扣减数量 */
    private double fdbcommitqty;
    /* 辅助调拨扣减数量 */
    private double fdbseccommitqty;
    /* 开票数量 */
    private double fauxqtyinvoice;
    /* 工序号 */
    private int fopersn;
    /* 审核标志 */
    private Short fcheckstatus;
    /* 拆分辅助数量 */
    private double fsplitsecqty;
    /* 计划模式 */
    private int fplanmode;
    /* 计划跟踪号 */
    private String fmtono;
    /* 辅助计量单位实存数量 */
    private double fsecqtyactual;
    /* 辅助计量单位账存数量 */
    private double fsecqtymust;
    /* 客户订单号 */
    private String fclientorderno;
    /* 订单行号 */
    private int fcliententryid;
    /* 拆卸成本拆分比例(%) */
    private double fcostpercentage;
    /* 坯料尺寸 */
    private String fitemsize;
    /* 坯料数 */
    private String fitemsuite;
    /* 位置号 */
    private String fpositionno;
    /* 对账标志 */
    private Byte facctcheck;
    /* 结算标志 */
    private Byte fclosing;
    /* 是否VMI */
    private int fisvmi;
    /* 供应商 */
    private int fentrysupply;
    /* 检验是否良品 */
    private int fchkpassitem;
    /* 发货通知内码 */
    private int fseoutinterid;
    /* 发货通知分录 */
    private int fseoutentryid;
    /* 发货通知单号 */
    private String fseoutbillno;
    /* 对账确认意见（表体） */
    private String fconfirmmementry;
    /* 产品内码 */
    private int ffatherproductid;
    /* 网上订单号 */
    private String folorderbillno;
    /* 退货通知单号 */
    private String freturnnoticebillno;
    /* 退货通知分录 */
    private int freturnnoticeentryid;
    /* 退货通知内码 */
    private int freturnnoticeinterid;
    /* 产品档案数量 */
    private double fproductfileqty;
    /* 采购单价 */
    private double fpurchaseprice;
    /* 采购金额 */
    private double fpurchaseamount;
    /* 采购金额钩稽金额 */
    private double fcheckamount;
    /* 发送内码 */
    private int foutsourceinterid;
    /* 发送分录 */
    private int foutsourceentryid;
    /* 发送类型 */
    private int foutsourcetrantype;
    /* 店铺名称 */
    private String fshopname;
    /* 物流费用 */
    private double fpostfee;

    /* 单据头订单号，不存库，传递进存储过程，用来处理单据头FInterId与单据体FInterId对应 */
    private String fbillNo;
    /* 发货仓库代码，不存库 */
    private String fdcstockcode;
    /* 物流名称，不存库 */
    private String fname;
    /* 物流代码 ，不存库 */
    private String fnumber;
    /* 临时字段，单位名称，跨库单据传递需用到 */
    private String unitName;

    /* 以下六个字段由销售订单下推携带过来，采购订单内码 */
    private String fentryselfb0179;
    /* 采购订单号 */
    private String fentryselfb0180;
    /* 采购订单分录码 */
    private String fentryselfb0181;
    /* 销售订单号 */
    private String fentryselfb0182;
    /* 销售订单内码 */
    private String fentryselfb0183;
    /* 销售订单分录码 */
    private String fentryselfb0184;
    /* 退货理由id */
    private int returnReasonId;
    /* (生产帐号)退货理由id */
    private int fentryselfb0185;
    /* 客户销售单价 */
    private double custSalesPrice;

    private ICStockBill_K3 stockBill;
    private ICItem icItem;
    private Stock inStock;
    private Stock outStock;

    // 临时字段，不存表
    private int scanningRecordId; // 扫码记录表id
    private String salOrderNo; // 销售订单号
    private double sumRealQty; // 实际出入库总数
    private double realQty; // 实际出入库数量
    private double useableQty; // 可用数
    private int isCheck; // 是否选中
    private String reason; // 退货原因
    private double logPrice; // 快递费


    public int getFinterid() {
        return finterid;
    }

    public int getFentryid() {
        return fentryid;
    }

    public int getFitemid() {
        return fitemid;
    }

    public double getFqtymust() {
        return fqtymust;
    }

    public double getFqty() {
        return fqty;
    }

    public double getFprice() {
        return fprice;
    }

    public String getFbatchno() {
        return fbatchno;
    }

    public double getFamount() {
        return famount;
    }

    public String getFnote() {
        return fnote;
    }

    public int getFscbillinterid() {
        return fscbillinterid;
    }

    public String getFscbillno() {
        return fscbillno;
    }

    public int getFunitid() {
        return funitid;
    }

    public double getFauxprice() {
        return fauxprice;
    }

    public double getFauxqty() {
        return fauxqty;
    }

    public double getFauxqtymust() {
        return fauxqtymust;
    }

    public double getFqtyactual() {
        return fqtyactual;
    }

    public double getFauxqtyactual() {
        return fauxqtyactual;
    }

    public double getFplanprice() {
        return fplanprice;
    }

    public double getFauxplanprice() {
        return fauxplanprice;
    }

    public int getFsourceentryid() {
        return fsourceentryid;
    }

    public double getFcommitqty() {
        return fcommitqty;
    }

    public double getFauxcommitqty() {
        return fauxcommitqty;
    }

    public String getFkfdate() {
        return fkfdate;
    }

    public int getFkfperiod() {
        return fkfperiod;
    }

    public int getFdcspid() {
        return fdcspid;
    }

    public int getFscspid() {
        return fscspid;
    }

    public double getFconsignprice() {
        return fconsignprice;
    }

    public double getFconsignamount() {
        return fconsignamount;
    }

    public double getFprocesscost() {
        return fprocesscost;
    }

    public double getFmaterialcost() {
        return fmaterialcost;
    }

    public double getFtaxamount() {
        return ftaxamount;
    }

    public String getFmapnumber() {
        return fmapnumber;
    }

    public String getFmapname() {
        return fmapname;
    }

    public int getForgbillentryid() {
        return forgbillentryid;
    }

    public int getFoperid() {
        return foperid;
    }

    public double getFplanamount() {
        return fplanamount;
    }

    public double getFprocessprice() {
        return fprocessprice;
    }

    public double getFtaxrate() {
        return ftaxrate;
    }

    public int getFsnlistid() {
        return fsnlistid;
    }

    public double getFamtref() {
        return famtref;
    }

    public int getFauxpropid() {
        return fauxpropid;
    }

    public double getFpriceref() {
        return fpriceref;
    }

    public double getFauxpriceref() {
        return fauxpriceref;
    }

    public String getFfetchdate() {
        return ffetchdate;
    }

    public double getFqtyinvoice() {
        return fqtyinvoice;
    }

    public double getFqtyinvoicebase() {
        return fqtyinvoicebase;
    }

    public double getFseccoefficient() {
        return fseccoefficient;
    }

    public double getFsecqty() {
        return fsecqty;
    }

    public double getFseccommitqty() {
        return fseccommitqty;
    }

    public int getFsourcetrantype() {
        return fsourcetrantype;
    }

    public int getFsourceinterid() {
        return fsourceinterid;
    }

    public String getFsourcebillno() {
        return fsourcebillno;
    }

    public int getFcontractinterid() {
        return fcontractinterid;
    }

    public int getFcontractentryid() {
        return fcontractentryid;
    }

    public String getFcontractbillno() {
        return fcontractbillno;
    }

    public String getFicmobillno() {
        return ficmobillno;
    }

    public int getFicmointerid() {
        return ficmointerid;
    }

    public int getFppbomentryid() {
        return fppbomentryid;
    }

    public int getForderinterid() {
        return forderinterid;
    }

    public int getForderentryid() {
        return forderentryid;
    }

    public String getForderbillno() {
        return forderbillno;
    }

    public double getFallhookqty() {
        return fallhookqty;
    }

    public double getFallhookamount() {
        return fallhookamount;
    }

    public double getFcurrenthookqty() {
        return fcurrenthookqty;
    }

    public double getFcurrenthookamount() {
        return fcurrenthookamount;
    }

    public double getFstdallhookamount() {
        return fstdallhookamount;
    }

    public double getFstdcurrenthookamount() {
        return fstdcurrenthookamount;
    }

    public int getFscstockid() {
        return fscstockid;
    }

    public int getFdcstockid() {
        return fdcstockid;
    }

    public String getFperioddate() {
        return fperioddate;
    }

    public int getFcostobjgroupid() {
        return fcostobjgroupid;
    }

    public int getFcostobjid() {
        return fcostobjid;
    }

    public int getFdetailid() {
        return fdetailid;
    }

    public int getFreproducetype() {
        return freproducetype;
    }

    public int getFbominterid() {
        return fbominterid;
    }

    public double getFdiscountrate() {
        return fdiscountrate;
    }

    public double getFdiscountamount() {
        return fdiscountamount;
    }

    public int getFsepcialsaleid() {
        return fsepcialsaleid;
    }

    public double getFoutcommitqty() {
        return foutcommitqty;
    }

    public double getFoutseccommitqty() {
        return foutseccommitqty;
    }

    public double getFdbcommitqty() {
        return fdbcommitqty;
    }

    public double getFdbseccommitqty() {
        return fdbseccommitqty;
    }

    public double getFauxqtyinvoice() {
        return fauxqtyinvoice;
    }

    public int getFopersn() {
        return fopersn;
    }

    public Short getFcheckstatus() {
        return fcheckstatus;
    }

    public double getFsplitsecqty() {
        return fsplitsecqty;
    }

    public int getFplanmode() {
        return fplanmode;
    }

    public String getFmtono() {
        return fmtono;
    }

    public double getFsecqtyactual() {
        return fsecqtyactual;
    }

    public double getFsecqtymust() {
        return fsecqtymust;
    }

    public String getFclientorderno() {
        return fclientorderno;
    }

    public int getFcliententryid() {
        return fcliententryid;
    }

    public double getFcostpercentage() {
        return fcostpercentage;
    }

    public String getFitemsize() {
        return fitemsize;
    }

    public String getFitemsuite() {
        return fitemsuite;
    }

    public String getFpositionno() {
        return fpositionno;
    }

    public Byte getFacctcheck() {
        return facctcheck;
    }

    public Byte getFclosing() {
        return fclosing;
    }

    public int getFisvmi() {
        return fisvmi;
    }

    public int getFentrysupply() {
        return fentrysupply;
    }

    public int getFchkpassitem() {
        return fchkpassitem;
    }

    public int getFseoutinterid() {
        return fseoutinterid;
    }

    public int getFseoutentryid() {
        return fseoutentryid;
    }

    public String getFseoutbillno() {
        return fseoutbillno;
    }

    public String getFconfirmmementry() {
        return fconfirmmementry;
    }

    public int getFfatherproductid() {
        return ffatherproductid;
    }

    public String getFolorderbillno() {
        return folorderbillno;
    }

    public String getFreturnnoticebillno() {
        return freturnnoticebillno;
    }

    public int getFreturnnoticeentryid() {
        return freturnnoticeentryid;
    }

    public int getFreturnnoticeinterid() {
        return freturnnoticeinterid;
    }

    public double getFproductfileqty() {
        return fproductfileqty;
    }

    public double getFpurchaseprice() {
        return fpurchaseprice;
    }

    public double getFpurchaseamount() {
        return fpurchaseamount;
    }

    public double getFcheckamount() {
        return fcheckamount;
    }

    public int getFoutsourceinterid() {
        return foutsourceinterid;
    }

    public int getFoutsourceentryid() {
        return foutsourceentryid;
    }

    public int getFoutsourcetrantype() {
        return foutsourcetrantype;
    }

    public String getFshopname() {
        return fshopname;
    }

    public double getFpostfee() {
        return fpostfee;
    }

    public String getFbillNo() {
        return fbillNo;
    }

    public String getFdcstockcode() {
        return fdcstockcode;
    }

    public String getFname() {
        return fname;
    }

    public String getFnumber() {
        return fnumber;
    }

    public String getFentryselfb0180() {
        return fentryselfb0180;
    }

    public String getFentryselfb0182() {
        return fentryselfb0182;
    }

    public String getFentryselfb0183() {
        return fentryselfb0183;
    }

    public void setFentryselfb0183(String fentryselfb0183) {
        this.fentryselfb0183 = fentryselfb0183;
    }

    public String getFentryselfb0184() {
        return fentryselfb0184;
    }

    public void setFentryselfb0184(String fentryselfb0184) {
        this.fentryselfb0184 = fentryselfb0184;
    }

    public String getFentryselfb0179() {
        return fentryselfb0179;
    }

    public void setFentryselfb0179(String fentryselfb0179) {
        this.fentryselfb0179 = fentryselfb0179;
    }

    public ICStockBill_K3 getStockBill() {
        return stockBill;
    }

    public ICItem getIcItem() {
        return icItem;
    }

    public void setFinterid(int finterid) {
        this.finterid = finterid;
    }

    public void setFentryid(int fentryid) {
        this.fentryid = fentryid;
    }

    public void setFitemid(int fitemid) {
        this.fitemid = fitemid;
    }

    public void setFqtymust(double fqtymust) {
        this.fqtymust = fqtymust;
    }

    public void setFqty(double fqty) {
        this.fqty = fqty;
    }

    public void setFprice(double fprice) {
        this.fprice = fprice;
    }

    public void setFbatchno(String fbatchno) {
        this.fbatchno = fbatchno;
    }

    public void setFamount(double famount) {
        this.famount = famount;
    }

    public void setFnote(String fnote) {
        this.fnote = fnote;
    }

    public void setFscbillinterid(int fscbillinterid) {
        this.fscbillinterid = fscbillinterid;
    }

    public void setFscbillno(String fscbillno) {
        this.fscbillno = fscbillno;
    }

    public void setFunitid(int funitid) {
        this.funitid = funitid;
    }

    public void setFauxprice(double fauxprice) {
        this.fauxprice = fauxprice;
    }

    public void setFauxqty(double fauxqty) {
        this.fauxqty = fauxqty;
    }

    public void setFauxqtymust(double fauxqtymust) {
        this.fauxqtymust = fauxqtymust;
    }

    public void setFqtyactual(double fqtyactual) {
        this.fqtyactual = fqtyactual;
    }

    public void setFauxqtyactual(double fauxqtyactual) {
        this.fauxqtyactual = fauxqtyactual;
    }

    public void setFplanprice(double fplanprice) {
        this.fplanprice = fplanprice;
    }

    public void setFauxplanprice(double fauxplanprice) {
        this.fauxplanprice = fauxplanprice;
    }

    public void setFsourceentryid(int fsourceentryid) {
        this.fsourceentryid = fsourceentryid;
    }

    public void setFcommitqty(double fcommitqty) {
        this.fcommitqty = fcommitqty;
    }

    public void setFauxcommitqty(double fauxcommitqty) {
        this.fauxcommitqty = fauxcommitqty;
    }

    public void setFkfdate(String fkfdate) {
        this.fkfdate = fkfdate;
    }

    public void setFkfperiod(int fkfperiod) {
        this.fkfperiod = fkfperiod;
    }

    public void setFdcspid(int fdcspid) {
        this.fdcspid = fdcspid;
    }

    public void setFscspid(int fscspid) {
        this.fscspid = fscspid;
    }

    public void setFconsignprice(double fconsignprice) {
        this.fconsignprice = fconsignprice;
    }

    public void setFconsignamount(double fconsignamount) {
        this.fconsignamount = fconsignamount;
    }

    public void setFprocesscost(double fprocesscost) {
        this.fprocesscost = fprocesscost;
    }

    public void setFmaterialcost(double fmaterialcost) {
        this.fmaterialcost = fmaterialcost;
    }

    public void setFtaxamount(double ftaxamount) {
        this.ftaxamount = ftaxamount;
    }

    public void setFmapnumber(String fmapnumber) {
        this.fmapnumber = fmapnumber;
    }

    public void setFmapname(String fmapname) {
        this.fmapname = fmapname;
    }

    public void setForgbillentryid(int forgbillentryid) {
        this.forgbillentryid = forgbillentryid;
    }

    public void setFoperid(int foperid) {
        this.foperid = foperid;
    }

    public void setFplanamount(double fplanamount) {
        this.fplanamount = fplanamount;
    }

    public void setFprocessprice(double fprocessprice) {
        this.fprocessprice = fprocessprice;
    }

    public void setFtaxrate(double ftaxrate) {
        this.ftaxrate = ftaxrate;
    }

    public void setFsnlistid(int fsnlistid) {
        this.fsnlistid = fsnlistid;
    }

    public void setFamtref(double famtref) {
        this.famtref = famtref;
    }

    public void setFauxpropid(int fauxpropid) {
        this.fauxpropid = fauxpropid;
    }

    public void setFpriceref(double fpriceref) {
        this.fpriceref = fpriceref;
    }

    public void setFauxpriceref(double fauxpriceref) {
        this.fauxpriceref = fauxpriceref;
    }

    public void setFfetchdate(String ffetchdate) {
        this.ffetchdate = ffetchdate;
    }

    public void setFqtyinvoice(double fqtyinvoice) {
        this.fqtyinvoice = fqtyinvoice;
    }

    public void setFqtyinvoicebase(double fqtyinvoicebase) {
        this.fqtyinvoicebase = fqtyinvoicebase;
    }

    public void setFseccoefficient(double fseccoefficient) {
        this.fseccoefficient = fseccoefficient;
    }

    public void setFsecqty(double fsecqty) {
        this.fsecqty = fsecqty;
    }

    public void setFseccommitqty(double fseccommitqty) {
        this.fseccommitqty = fseccommitqty;
    }

    public void setFsourcetrantype(int fsourcetrantype) {
        this.fsourcetrantype = fsourcetrantype;
    }

    public void setFsourceinterid(int fsourceinterid) {
        this.fsourceinterid = fsourceinterid;
    }

    public void setFsourcebillno(String fsourcebillno) {
        this.fsourcebillno = fsourcebillno;
    }

    public void setFcontractinterid(int fcontractinterid) {
        this.fcontractinterid = fcontractinterid;
    }

    public void setFcontractentryid(int fcontractentryid) {
        this.fcontractentryid = fcontractentryid;
    }

    public void setFcontractbillno(String fcontractbillno) {
        this.fcontractbillno = fcontractbillno;
    }

    public void setFicmobillno(String ficmobillno) {
        this.ficmobillno = ficmobillno;
    }

    public void setFicmointerid(int ficmointerid) {
        this.ficmointerid = ficmointerid;
    }

    public void setFppbomentryid(int fppbomentryid) {
        this.fppbomentryid = fppbomentryid;
    }

    public void setForderinterid(int forderinterid) {
        this.forderinterid = forderinterid;
    }

    public void setForderentryid(int forderentryid) {
        this.forderentryid = forderentryid;
    }

    public void setForderbillno(String forderbillno) {
        this.forderbillno = forderbillno;
    }

    public void setFallhookqty(double fallhookqty) {
        this.fallhookqty = fallhookqty;
    }

    public void setFallhookamount(double fallhookamount) {
        this.fallhookamount = fallhookamount;
    }

    public void setFcurrenthookqty(double fcurrenthookqty) {
        this.fcurrenthookqty = fcurrenthookqty;
    }

    public void setFcurrenthookamount(double fcurrenthookamount) {
        this.fcurrenthookamount = fcurrenthookamount;
    }

    public void setFstdallhookamount(double fstdallhookamount) {
        this.fstdallhookamount = fstdallhookamount;
    }

    public void setFstdcurrenthookamount(double fstdcurrenthookamount) {
        this.fstdcurrenthookamount = fstdcurrenthookamount;
    }

    public void setFscstockid(int fscstockid) {
        this.fscstockid = fscstockid;
    }

    public void setFdcstockid(int fdcstockid) {
        this.fdcstockid = fdcstockid;
    }

    public void setFperioddate(String fperioddate) {
        this.fperioddate = fperioddate;
    }

    public void setFcostobjgroupid(int fcostobjgroupid) {
        this.fcostobjgroupid = fcostobjgroupid;
    }

    public void setFcostobjid(int fcostobjid) {
        this.fcostobjid = fcostobjid;
    }

    public void setFdetailid(int fdetailid) {
        this.fdetailid = fdetailid;
    }

    public void setFreproducetype(int freproducetype) {
        this.freproducetype = freproducetype;
    }

    public void setFbominterid(int fbominterid) {
        this.fbominterid = fbominterid;
    }

    public void setFdiscountrate(double fdiscountrate) {
        this.fdiscountrate = fdiscountrate;
    }

    public void setFdiscountamount(double fdiscountamount) {
        this.fdiscountamount = fdiscountamount;
    }

    public void setFsepcialsaleid(int fsepcialsaleid) {
        this.fsepcialsaleid = fsepcialsaleid;
    }

    public void setFoutcommitqty(double foutcommitqty) {
        this.foutcommitqty = foutcommitqty;
    }

    public void setFoutseccommitqty(double foutseccommitqty) {
        this.foutseccommitqty = foutseccommitqty;
    }

    public void setFdbcommitqty(double fdbcommitqty) {
        this.fdbcommitqty = fdbcommitqty;
    }

    public void setFdbseccommitqty(double fdbseccommitqty) {
        this.fdbseccommitqty = fdbseccommitqty;
    }

    public void setFauxqtyinvoice(double fauxqtyinvoice) {
        this.fauxqtyinvoice = fauxqtyinvoice;
    }

    public void setFopersn(int fopersn) {
        this.fopersn = fopersn;
    }

    public void setFcheckstatus(Short fcheckstatus) {
        this.fcheckstatus = fcheckstatus;
    }

    public void setFsplitsecqty(double fsplitsecqty) {
        this.fsplitsecqty = fsplitsecqty;
    }

    public void setFplanmode(int fplanmode) {
        this.fplanmode = fplanmode;
    }

    public void setFmtono(String fmtono) {
        this.fmtono = fmtono;
    }

    public void setFsecqtyactual(double fsecqtyactual) {
        this.fsecqtyactual = fsecqtyactual;
    }

    public void setFsecqtymust(double fsecqtymust) {
        this.fsecqtymust = fsecqtymust;
    }

    public void setFclientorderno(String fclientorderno) {
        this.fclientorderno = fclientorderno;
    }

    public void setFcliententryid(int fcliententryid) {
        this.fcliententryid = fcliententryid;
    }

    public void setFcostpercentage(double fcostpercentage) {
        this.fcostpercentage = fcostpercentage;
    }

    public void setFitemsize(String fitemsize) {
        this.fitemsize = fitemsize;
    }

    public void setFitemsuite(String fitemsuite) {
        this.fitemsuite = fitemsuite;
    }

    public void setFpositionno(String fpositionno) {
        this.fpositionno = fpositionno;
    }

    public void setFacctcheck(Byte facctcheck) {
        this.facctcheck = facctcheck;
    }

    public void setFclosing(Byte fclosing) {
        this.fclosing = fclosing;
    }

    public void setFisvmi(int fisvmi) {
        this.fisvmi = fisvmi;
    }

    public void setFentrysupply(int fentrysupply) {
        this.fentrysupply = fentrysupply;
    }

    public void setFchkpassitem(int fchkpassitem) {
        this.fchkpassitem = fchkpassitem;
    }

    public void setFseoutinterid(int fseoutinterid) {
        this.fseoutinterid = fseoutinterid;
    }

    public void setFseoutentryid(int fseoutentryid) {
        this.fseoutentryid = fseoutentryid;
    }

    public void setFseoutbillno(String fseoutbillno) {
        this.fseoutbillno = fseoutbillno;
    }

    public void setFconfirmmementry(String fconfirmmementry) {
        this.fconfirmmementry = fconfirmmementry;
    }

    public void setFfatherproductid(int ffatherproductid) {
        this.ffatherproductid = ffatherproductid;
    }

    public void setFolorderbillno(String folorderbillno) {
        this.folorderbillno = folorderbillno;
    }

    public void setFreturnnoticebillno(String freturnnoticebillno) {
        this.freturnnoticebillno = freturnnoticebillno;
    }

    public void setFreturnnoticeentryid(int freturnnoticeentryid) {
        this.freturnnoticeentryid = freturnnoticeentryid;
    }

    public void setFreturnnoticeinterid(int freturnnoticeinterid) {
        this.freturnnoticeinterid = freturnnoticeinterid;
    }

    public void setFproductfileqty(double fproductfileqty) {
        this.fproductfileqty = fproductfileqty;
    }

    public void setFpurchaseprice(double fpurchaseprice) {
        this.fpurchaseprice = fpurchaseprice;
    }

    public void setFpurchaseamount(double fpurchaseamount) {
        this.fpurchaseamount = fpurchaseamount;
    }

    public void setFcheckamount(double fcheckamount) {
        this.fcheckamount = fcheckamount;
    }

    public void setFoutsourceinterid(int foutsourceinterid) {
        this.foutsourceinterid = foutsourceinterid;
    }

    public void setFoutsourceentryid(int foutsourceentryid) {
        this.foutsourceentryid = foutsourceentryid;
    }

    public void setFoutsourcetrantype(int foutsourcetrantype) {
        this.foutsourcetrantype = foutsourcetrantype;
    }

    public void setFshopname(String fshopname) {
        this.fshopname = fshopname;
    }

    public void setFpostfee(double fpostfee) {
        this.fpostfee = fpostfee;
    }

    public void setFbillNo(String fbillNo) {
        this.fbillNo = fbillNo;
    }

    public void setFdcstockcode(String fdcstockcode) {
        this.fdcstockcode = fdcstockcode;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public void setFnumber(String fnumber) {
        this.fnumber = fnumber;
    }

    public void setFentryselfb0180(String fentryselfb0180) {
        this.fentryselfb0180 = fentryselfb0180;
    }

    public void setFentryselfb0182(String fentryselfb0182) {
        this.fentryselfb0182 = fentryselfb0182;
    }

    public void setStockBill(ICStockBill_K3 stockBill) {
        this.stockBill = stockBill;
    }

    public void setIcItem(ICItem icItem) {
        this.icItem = icItem;
    }

    public String getFentryselfb0181() {
        return fentryselfb0181;
    }

    public void setFentryselfb0181(String fentryselfb0181) {
        this.fentryselfb0181 = fentryselfb0181;
    }

    public int getScanningRecordId() {
        return scanningRecordId;
    }

    public void setScanningRecordId(int scanningRecordId) {
        this.scanningRecordId = scanningRecordId;
    }

    public String getSalOrderNo() {
        return salOrderNo;
    }

    public void setSalOrderNo(String salOrderNo) {
        this.salOrderNo = salOrderNo;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public double getSumRealQty() {
        return sumRealQty;
    }

    public void setSumRealQty(double sumRealQty) {
        this.sumRealQty = sumRealQty;
    }

    public int getReturnReasonId() {
        return returnReasonId;
    }

    public void setReturnReasonId(int returnReasonrId) {
        this.returnReasonId = returnReasonrId;
    }

    public double getRealQty() {
        return realQty;
    }

    public void setRealQty(double realQty) {
        this.realQty = realQty;
    }

    public double getUseableQty() {
        return useableQty;
    }

    public int getIsCheck() {
        return isCheck;
    }

    public void setUseableQty(double useableQty) {
        this.useableQty = useableQty;
    }

    public void setIsCheck(int isCheck) {
        this.isCheck = isCheck;
    }

    public int getFentryselfb0185() {
        return fentryselfb0185;
    }

    public void setFentryselfb0185(int fentryselfb0185) {
        this.fentryselfb0185 = fentryselfb0185;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public double getLogPrice() {
        return logPrice;
    }

    public void setLogPrice(double logPrice) {
        this.logPrice = logPrice;
    }

    public double getCustSalesPrice() {
        return custSalesPrice;
    }

    public void setCustSalesPrice(double custSalesPrice) {
        this.custSalesPrice = custSalesPrice;
    }

    public Stock getInStock() {
        return inStock;
    }

    public void setInStock(Stock inStock) {
        this.inStock = inStock;
    }

    public Stock getOutStock() {
        return outStock;
    }

    public void setOutStock(Stock outStock) {
        this.outStock = outStock;
    }

}