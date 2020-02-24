package ykk.xc.com.wms.bean.k3Bean;

import java.io.Serializable;

public class ICStockBillEntry_K3 implements Serializable {
    /* 单据内码 */
    private Integer finterid;
    /* 分录号 */
    private Integer fentryid;
    /* 物料内码 */
    private Integer fitemid;
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
    private Integer fscbillinterid;
    /* 原单单号 */
    private String fscbillno;
    /* 单位内码 */
    private Integer funitid;
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
    private Integer fsourceentryid;
    /* 提交数量 */
    private double fcommitqty;
    /* 辅助提交数量 */
    private double fauxcommitqty;
    /* 生产/采购日期 */
    private String fkfdate;
    /* 保质期 */
    private Integer fkfperiod;
    /* 目标仓位 */
    private Integer fdcspid;
    /* 源仓位 */
    private Integer fscspid;
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
    private Integer forgbillentryid;
    /* 工序 */
    private Integer foperid;
    /* 计划价金额 */
    private double fplanamount;
    /* 委外加工入库单增加加工单价 */
    private double fprocessprice;
    /* 税率 */
    private double ftaxrate;
    /* 序列号 */
    private Integer fsnlistid;
    /* 调拨金额 */
    private double famtref;
    /* 辅助属性 */
    private Integer fauxpropid;
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
    private Integer fsourcetrantype;
    /* 源单内码 */
    private Integer fsourceinterid;
    /* 源单单号 */
    private String fsourcebillno;
    /* 合同内码 */
    private Integer fcontractinterid;
    /* 合同分录 */
    private Integer fcontractentryid;
    /* 合同单号 */
    private String fcontractbillno;
    /* 生产任务单号 */
    private String ficmobillno;
    /* 领料单=任务单内码；委外出入库单=投料单内码 */
    private Integer ficmointerid;
    /* 投料单分录号 */
    private Integer fppbomentryid;
    /* 订单内码 */
    private Integer forderinterid;
    /* 订单分录 */
    private Integer forderentryid;
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
    private Integer fscstockid;
    /* 调入仓库 */
    private Integer fdcstockid;
    /* 有效期至 */
    private String fperioddate;
    /* 成本对象组 */
    private Integer fcostobjgroupid;
    /* 成本对象 */
    private Integer fcostobjid;
    /* 分录内码 */
    private Integer fdetailid;
    /* 是否返工 */
    private Integer freproducetype;
    /* 客户BOM */
    private Integer fbominterid;
    /* 折扣率 */
    private double fdiscountrate;
    /* 折扣额 */
    private double fdiscountamount;
    /* 特价ID */
    private Integer fsepcialsaleid;
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
    private Integer fopersn;
    /* 审核标志 */
    private Short fcheckstatus;
    /* 拆分辅助数量 */
    private double fsplitsecqty;
    /* 计划模式 */
    private Integer fplanmode;
    /* 计划跟踪号 */
    private String fmtono;
    /* 辅助计量单位实存数量 */
    private double fsecqtyactual;
    /* 辅助计量单位账存数量 */
    private double fsecqtymust;
    /* 客户订单号 */
    private String fclientorderno;
    /* 订单行号 */
    private Integer fcliententryid;
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
    private Integer fisvmi;
    /* 供应商 */
    private Integer fentrysupply;
    /* 检验是否良品 */
    private Integer fchkpassitem;
    /* 发货通知内码 */
    private Integer fseoutinterid;
    /* 发货通知分录 */
    private Integer fseoutentryid;
    /* 发货通知单号 */
    private String fseoutbillno;
    /* 对账确认意见（表体） */
    private String fconfirmmementry;
    /* 产品内码 */
    private Integer ffatherproductid;
    /* 网上订单号 */
    private String folorderbillno;
    /* 退货通知单号 */
    private String freturnnoticebillno;
    /* 退货通知分录 */
    private Integer freturnnoticeentryid;
    /* 退货通知内码 */
    private Integer freturnnoticeinterid;
    /* 产品档案数量 */
    private double fproductfileqty;
    /* 采购单价 */
    private double fpurchaseprice;
    /* 采购金额 */
    private double fpurchaseamount;
    /* 采购金额钩稽金额 */
    private double fcheckamount;
    /* 发送内码 */
    private Integer foutsourceinterid;
    /* 发送分录 */
    private Integer foutsourceentryid;
    /* 发送类型 */
    private Integer foutsourcetrantype;
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

    /* 以下六个字段由销售订单下推携带过来，采购订单内码 */
    private int fentryselfb0179;
    /* 采购订单号 */
    private String fentryselfb0180;
    /* 采购订单分录码 */
    private int fentryselfb0181;
    /* 销售订单号 */
    private String fentryselfb0182;
    /* 销售订单内码 */
    private int fentryselfb0183;
    /* 销售订单分录码 */
    private int fentryselfb0184;
    /* 退货理由id */
    private int returnReasonId;
    /* (生产帐号)退货理由id */
    private int fentryselfb0185;
    /* 客户销售单价 */
    private double custSalesPrice;

    private ICStockBill_K3 stockBill;
    private ICItem icItem;

    // 临时字段，不存表
    private int scanningRecordId; // 扫码记录表id
    private String salOrderNo; // 销售订单号
    private double sumRealQty; // 实际出入库总数
    private double realQty; // 实际出入库数量
    private double useableQty; // 可用数
    private int isCheck; // 是否选中


    public Integer getFinterid() {
        return finterid;
    }

    public Integer getFentryid() {
        return fentryid;
    }

    public Integer getFitemid() {
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

    public Integer getFscbillinterid() {
        return fscbillinterid;
    }

    public String getFscbillno() {
        return fscbillno;
    }

    public Integer getFunitid() {
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

    public Integer getFsourceentryid() {
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

    public Integer getFkfperiod() {
        return fkfperiod;
    }

    public Integer getFdcspid() {
        return fdcspid;
    }

    public Integer getFscspid() {
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

    public Integer getForgbillentryid() {
        return forgbillentryid;
    }

    public Integer getFoperid() {
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

    public Integer getFsnlistid() {
        return fsnlistid;
    }

    public double getFamtref() {
        return famtref;
    }

    public Integer getFauxpropid() {
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

    public Integer getFsourcetrantype() {
        return fsourcetrantype;
    }

    public Integer getFsourceinterid() {
        return fsourceinterid;
    }

    public String getFsourcebillno() {
        return fsourcebillno;
    }

    public Integer getFcontractinterid() {
        return fcontractinterid;
    }

    public Integer getFcontractentryid() {
        return fcontractentryid;
    }

    public String getFcontractbillno() {
        return fcontractbillno;
    }

    public String getFicmobillno() {
        return ficmobillno;
    }

    public Integer getFicmointerid() {
        return ficmointerid;
    }

    public Integer getFppbomentryid() {
        return fppbomentryid;
    }

    public Integer getForderinterid() {
        return forderinterid;
    }

    public Integer getForderentryid() {
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

    public Integer getFscstockid() {
        return fscstockid;
    }

    public Integer getFdcstockid() {
        return fdcstockid;
    }

    public String getFperioddate() {
        return fperioddate;
    }

    public Integer getFcostobjgroupid() {
        return fcostobjgroupid;
    }

    public Integer getFcostobjid() {
        return fcostobjid;
    }

    public Integer getFdetailid() {
        return fdetailid;
    }

    public Integer getFreproducetype() {
        return freproducetype;
    }

    public Integer getFbominterid() {
        return fbominterid;
    }

    public double getFdiscountrate() {
        return fdiscountrate;
    }

    public double getFdiscountamount() {
        return fdiscountamount;
    }

    public Integer getFsepcialsaleid() {
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

    public Integer getFopersn() {
        return fopersn;
    }

    public Short getFcheckstatus() {
        return fcheckstatus;
    }

    public double getFsplitsecqty() {
        return fsplitsecqty;
    }

    public Integer getFplanmode() {
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

    public Integer getFcliententryid() {
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

    public Integer getFisvmi() {
        return fisvmi;
    }

    public Integer getFentrysupply() {
        return fentrysupply;
    }

    public Integer getFchkpassitem() {
        return fchkpassitem;
    }

    public Integer getFseoutinterid() {
        return fseoutinterid;
    }

    public Integer getFseoutentryid() {
        return fseoutentryid;
    }

    public String getFseoutbillno() {
        return fseoutbillno;
    }

    public String getFconfirmmementry() {
        return fconfirmmementry;
    }

    public Integer getFfatherproductid() {
        return ffatherproductid;
    }

    public String getFolorderbillno() {
        return folorderbillno;
    }

    public String getFreturnnoticebillno() {
        return freturnnoticebillno;
    }

    public Integer getFreturnnoticeentryid() {
        return freturnnoticeentryid;
    }

    public Integer getFreturnnoticeinterid() {
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

    public Integer getFoutsourceinterid() {
        return foutsourceinterid;
    }

    public Integer getFoutsourceentryid() {
        return foutsourceentryid;
    }

    public Integer getFoutsourcetrantype() {
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

    public int getFentryselfb0179() {
        return fentryselfb0179;
    }

    public String getFentryselfb0180() {
        return fentryselfb0180;
    }

    public int getFentryselfb0181() {
        return fentryselfb0181;
    }

    public String getFentryselfb0182() {
        return fentryselfb0182;
    }

    public int getFentryselfb0183() {
        return fentryselfb0183;
    }

    public int getFentryselfb0184() {
        return fentryselfb0184;
    }

    public ICStockBill_K3 getStockBill() {
        return stockBill;
    }

    public ICItem getIcItem() {
        return icItem;
    }

    public void setFinterid(Integer finterid) {
        this.finterid = finterid;
    }

    public void setFentryid(Integer fentryid) {
        this.fentryid = fentryid;
    }

    public void setFitemid(Integer fitemid) {
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

    public void setFscbillinterid(Integer fscbillinterid) {
        this.fscbillinterid = fscbillinterid;
    }

    public void setFscbillno(String fscbillno) {
        this.fscbillno = fscbillno;
    }

    public void setFunitid(Integer funitid) {
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

    public void setFsourceentryid(Integer fsourceentryid) {
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

    public void setFkfperiod(Integer fkfperiod) {
        this.fkfperiod = fkfperiod;
    }

    public void setFdcspid(Integer fdcspid) {
        this.fdcspid = fdcspid;
    }

    public void setFscspid(Integer fscspid) {
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

    public void setForgbillentryid(Integer forgbillentryid) {
        this.forgbillentryid = forgbillentryid;
    }

    public void setFoperid(Integer foperid) {
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

    public void setFsnlistid(Integer fsnlistid) {
        this.fsnlistid = fsnlistid;
    }

    public void setFamtref(double famtref) {
        this.famtref = famtref;
    }

    public void setFauxpropid(Integer fauxpropid) {
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

    public void setFsourcetrantype(Integer fsourcetrantype) {
        this.fsourcetrantype = fsourcetrantype;
    }

    public void setFsourceinterid(Integer fsourceinterid) {
        this.fsourceinterid = fsourceinterid;
    }

    public void setFsourcebillno(String fsourcebillno) {
        this.fsourcebillno = fsourcebillno;
    }

    public void setFcontractinterid(Integer fcontractinterid) {
        this.fcontractinterid = fcontractinterid;
    }

    public void setFcontractentryid(Integer fcontractentryid) {
        this.fcontractentryid = fcontractentryid;
    }

    public void setFcontractbillno(String fcontractbillno) {
        this.fcontractbillno = fcontractbillno;
    }

    public void setFicmobillno(String ficmobillno) {
        this.ficmobillno = ficmobillno;
    }

    public void setFicmointerid(Integer ficmointerid) {
        this.ficmointerid = ficmointerid;
    }

    public void setFppbomentryid(Integer fppbomentryid) {
        this.fppbomentryid = fppbomentryid;
    }

    public void setForderinterid(Integer forderinterid) {
        this.forderinterid = forderinterid;
    }

    public void setForderentryid(Integer forderentryid) {
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

    public void setFscstockid(Integer fscstockid) {
        this.fscstockid = fscstockid;
    }

    public void setFdcstockid(Integer fdcstockid) {
        this.fdcstockid = fdcstockid;
    }

    public void setFperioddate(String fperioddate) {
        this.fperioddate = fperioddate;
    }

    public void setFcostobjgroupid(Integer fcostobjgroupid) {
        this.fcostobjgroupid = fcostobjgroupid;
    }

    public void setFcostobjid(Integer fcostobjid) {
        this.fcostobjid = fcostobjid;
    }

    public void setFdetailid(Integer fdetailid) {
        this.fdetailid = fdetailid;
    }

    public void setFreproducetype(Integer freproducetype) {
        this.freproducetype = freproducetype;
    }

    public void setFbominterid(Integer fbominterid) {
        this.fbominterid = fbominterid;
    }

    public void setFdiscountrate(double fdiscountrate) {
        this.fdiscountrate = fdiscountrate;
    }

    public void setFdiscountamount(double fdiscountamount) {
        this.fdiscountamount = fdiscountamount;
    }

    public void setFsepcialsaleid(Integer fsepcialsaleid) {
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

    public void setFopersn(Integer fopersn) {
        this.fopersn = fopersn;
    }

    public void setFcheckstatus(Short fcheckstatus) {
        this.fcheckstatus = fcheckstatus;
    }

    public void setFsplitsecqty(double fsplitsecqty) {
        this.fsplitsecqty = fsplitsecqty;
    }

    public void setFplanmode(Integer fplanmode) {
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

    public void setFcliententryid(Integer fcliententryid) {
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

    public void setFisvmi(Integer fisvmi) {
        this.fisvmi = fisvmi;
    }

    public void setFentrysupply(Integer fentrysupply) {
        this.fentrysupply = fentrysupply;
    }

    public void setFchkpassitem(Integer fchkpassitem) {
        this.fchkpassitem = fchkpassitem;
    }

    public void setFseoutinterid(Integer fseoutinterid) {
        this.fseoutinterid = fseoutinterid;
    }

    public void setFseoutentryid(Integer fseoutentryid) {
        this.fseoutentryid = fseoutentryid;
    }

    public void setFseoutbillno(String fseoutbillno) {
        this.fseoutbillno = fseoutbillno;
    }

    public void setFconfirmmementry(String fconfirmmementry) {
        this.fconfirmmementry = fconfirmmementry;
    }

    public void setFfatherproductid(Integer ffatherproductid) {
        this.ffatherproductid = ffatherproductid;
    }

    public void setFolorderbillno(String folorderbillno) {
        this.folorderbillno = folorderbillno;
    }

    public void setFreturnnoticebillno(String freturnnoticebillno) {
        this.freturnnoticebillno = freturnnoticebillno;
    }

    public void setFreturnnoticeentryid(Integer freturnnoticeentryid) {
        this.freturnnoticeentryid = freturnnoticeentryid;
    }

    public void setFreturnnoticeinterid(Integer freturnnoticeinterid) {
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

    public void setFoutsourceinterid(Integer foutsourceinterid) {
        this.foutsourceinterid = foutsourceinterid;
    }

    public void setFoutsourceentryid(Integer foutsourceentryid) {
        this.foutsourceentryid = foutsourceentryid;
    }

    public void setFoutsourcetrantype(Integer foutsourcetrantype) {
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

    public void setFentryselfb0179(int fentryselfb0179) {
        this.fentryselfb0179 = fentryselfb0179;
    }

    public void setFentryselfb0180(String fentryselfb0180) {
        this.fentryselfb0180 = fentryselfb0180;
    }

    public void setFentryselfb0181(int fentryselfb0181) {
        this.fentryselfb0181 = fentryselfb0181;
    }

    public void setFentryselfb0182(String fentryselfb0182) {
        this.fentryselfb0182 = fentryselfb0182;
    }

    public void setFentryselfb0183(int fentryselfb0183) {
        this.fentryselfb0183 = fentryselfb0183;
    }

    public void setFentryselfb0184(int fentryselfb0184) {
        this.fentryselfb0184 = fentryselfb0184;
    }

    public void setStockBill(ICStockBill_K3 stockBill) {
        this.stockBill = stockBill;
    }

    public void setIcItem(ICItem icItem) {
        this.icItem = icItem;
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

    public double getSumRealQty() {
        return sumRealQty;
    }

    public void setSumRealQty(double sumRealQty) {
        this.sumRealQty = sumRealQty;
    }

    public int getReturnReasonrId() {
        return returnReasonId;
    }

    public void setReturnReasonrId(int returnReasonId) {
        this.returnReasonId = returnReasonId;
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

    public double getCustSalesPrice() {
        return custSalesPrice;
    }

    public void setCustSalesPrice(double custSalesPrice) {
        this.custSalesPrice = custSalesPrice;
    }

    @Override
    public String toString() {
        return "Icstockbillentry [finterid=" + finterid + ", fentryid=" + fentryid + ", fitemid=" + fitemid
                + ", fqtymust=" + fqtymust + ", fqty=" + fqty + ", fprice=" + fprice + ", fbatchno=" + fbatchno
                + ", famount=" + famount + ", fnote=" + fnote + ", fscbillinterid=" + fscbillinterid + ", fscbillno="
                + fscbillno + ", funitid=" + funitid + ", fauxprice=" + fauxprice + ", fauxqty=" + fauxqty
                + ", fauxqtymust=" + fauxqtymust + ", fqtyactual=" + fqtyactual + ", fauxqtyactual=" + fauxqtyactual
                + ", fplanprice=" + fplanprice + ", fauxplanprice=" + fauxplanprice + ", fsourceentryid="
                + fsourceentryid + ", fcommitqty=" + fcommitqty + ", fauxcommitqty=" + fauxcommitqty + ", fkfdate="
                + fkfdate + ", fkfperiod=" + fkfperiod + ", fdcspid=" + fdcspid + ", fscspid=" + fscspid
                + ", fconsignprice=" + fconsignprice + ", fconsignamount=" + fconsignamount + ", fprocesscost="
                + fprocesscost + ", fmaterialcost=" + fmaterialcost + ", ftaxamount=" + ftaxamount + ", fmapnumber="
                + fmapnumber + ", fmapname=" + fmapname + ", forgbillentryid=" + forgbillentryid + ", foperid="
                + foperid + ", fplanamount=" + fplanamount + ", fprocessprice=" + fprocessprice + ", ftaxrate="
                + ftaxrate + ", fsnlistid=" + fsnlistid + ", famtref=" + famtref + ", fauxpropid=" + fauxpropid
                + ", fpriceref=" + fpriceref + ", fauxpriceref=" + fauxpriceref + ", ffetchdate=" + ffetchdate
                + ", fqtyinvoice=" + fqtyinvoice + ", fqtyinvoicebase=" + fqtyinvoicebase + ", fseccoefficient="
                + fseccoefficient + ", fsecqty=" + fsecqty + ", fseccommitqty=" + fseccommitqty + ", fsourcetrantype="
                + fsourcetrantype + ", fsourceinterid=" + fsourceinterid + ", fsourcebillno=" + fsourcebillno
                + ", fcontractinterid=" + fcontractinterid + ", fcontractentryid=" + fcontractentryid
                + ", fcontractbillno=" + fcontractbillno + ", ficmobillno=" + ficmobillno + ", ficmointerid="
                + ficmointerid + ", fppbomentryid=" + fppbomentryid + ", forderinterid=" + forderinterid
                + ", forderentryid=" + forderentryid + ", forderbillno=" + forderbillno + ", fallhookqty=" + fallhookqty
                + ", fallhookamount=" + fallhookamount + ", fcurrenthookqty=" + fcurrenthookqty
                + ", fcurrenthookamount=" + fcurrenthookamount + ", fstdallhookamount=" + fstdallhookamount
                + ", fstdcurrenthookamount=" + fstdcurrenthookamount + ", fscstockid=" + fscstockid + ", fdcstockid="
                + fdcstockid + ", fperioddate=" + fperioddate + ", fcostobjgroupid=" + fcostobjgroupid + ", fcostobjid="
                + fcostobjid + ", fdetailid=" + fdetailid + ", freproducetype=" + freproducetype + ", fbominterid="
                + fbominterid + ", fdiscountrate=" + fdiscountrate + ", fdiscountamount=" + fdiscountamount
                + ", fsepcialsaleid=" + fsepcialsaleid + ", foutcommitqty=" + foutcommitqty + ", foutseccommitqty="
                + foutseccommitqty + ", fdbcommitqty=" + fdbcommitqty + ", fdbseccommitqty=" + fdbseccommitqty
                + ", fauxqtyinvoice=" + fauxqtyinvoice + ", fopersn=" + fopersn + ", fcheckstatus=" + fcheckstatus
                + ", fsplitsecqty=" + fsplitsecqty + ", fplanmode=" + fplanmode + ", fmtono=" + fmtono
                + ", fsecqtyactual=" + fsecqtyactual + ", fsecqtymust=" + fsecqtymust + ", fclientorderno="
                + fclientorderno + ", fcliententryid=" + fcliententryid + ", fcostpercentage=" + fcostpercentage
                + ", fitemsize=" + fitemsize + ", fitemsuite=" + fitemsuite + ", fpositionno=" + fpositionno
                + ", facctcheck=" + facctcheck + ", fclosing=" + fclosing + ", fisvmi=" + fisvmi + ", fentrysupply="
                + fentrysupply + ", fchkpassitem=" + fchkpassitem + ", fseoutinterid=" + fseoutinterid
                + ", fseoutentryid=" + fseoutentryid + ", fseoutbillno=" + fseoutbillno + ", fconfirmmementry="
                + fconfirmmementry + ", ffatherproductid=" + ffatherproductid + ", folorderbillno=" + folorderbillno
                + ", freturnnoticebillno=" + freturnnoticebillno + ", freturnnoticeentryid=" + freturnnoticeentryid
                + ", freturnnoticeinterid=" + freturnnoticeinterid + ", fproductfileqty=" + fproductfileqty
                + ", fpurchaseprice=" + fpurchaseprice + ", fpurchaseamount=" + fpurchaseamount + ", fcheckamount="
                + fcheckamount + ", foutsourceinterid=" + foutsourceinterid + ", foutsourceentryid=" + foutsourceentryid
                + ", foutsourcetrantype=" + foutsourcetrantype + ", fshopname=" + fshopname + ", fpostfee=" + fpostfee
                + ", fbillNo=" + fbillNo + ", fdcstockcode=" + fdcstockcode + ", fname=" + fname + ", fnumber="
                + fnumber + ", fentryselfb0179=" + fentryselfb0179 + ", fentryselfb0180=" + fentryselfb0180
                + ", fentryselfb0181=" + fentryselfb0181 + ", fentryselfb0182=" + fentryselfb0182 + ", fentryselfb0183="
                + fentryselfb0183 + ", fentryselfb0184=" + fentryselfb0184 + ", stockBill=" + stockBill + ", icItem="
                + icItem + "]";
    }


}