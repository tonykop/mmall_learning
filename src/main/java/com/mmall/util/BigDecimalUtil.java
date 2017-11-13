package com.mmall.util;

import java.math.BigDecimal;

/**
 * 商业运算保持运算精度工具类
 * Created by tony on 2017/10/23.
 */
public class BigDecimalUtil {

    private BigDecimalUtil(){

    }

    /**
     * 加
     * @param v1
     * @param v2
     * @return
     */
    public static BigDecimal add(Double v1, Double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2);
    }

    /**
     * 减
     * @param v1
     * @param v2
     * @return
     */
    public static BigDecimal sub(Double v1, Double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2);
    }

    /**
     * 乘
     * @param v1
     * @param v2
     * @return
     */
    public static BigDecimal mul(Double v1, Double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2);
    }

    /**
     * 除
     * @param v1
     * @param v2
     * @return
     */
    public static BigDecimal div(Double v1, Double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, 2, BigDecimal.ROUND_HALF_UP);   //保留两位小数并四舍五入
    }

}
