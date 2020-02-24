package ykk.xc.com.wms;

import org.junit.Test;

import ykk.xc.com.wms.comm.Comm;
import ykk.xc.com.wms.util.BigdecimalUtil;

public class MyTestTest {

    @Test
    public void main() {
        double a = 1234.6562;
        System.out.print(BigdecimalUtil.round(a, 0));
    }
}