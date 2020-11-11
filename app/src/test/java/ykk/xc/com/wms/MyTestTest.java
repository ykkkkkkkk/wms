package ykk.xc.com.wms;

import org.junit.Test;

import java.math.BigDecimal;

import ykk.xc.com.wms.comm.Comm;
import ykk.xc.com.wms.util.BigdecimalUtil;

public class MyTestTest {

    @Test
    public void main() {
        double a = 1.195;
        System.out.print(BigdecimalUtil.round(a, 1));
    }
}