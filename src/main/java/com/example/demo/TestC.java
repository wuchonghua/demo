package com.example.demo;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.RealMatrix;

/**
 * @author wuchonghua
 * @create 2022-06-07 13:49
 */
public class TestC {

    /**
     *
     * 独立坐标转标准坐标
     * @param X 独立坐标x
     * @param Y 独立坐标y
     * @param offsetX x平移
     * @param offsetY y平移
     * @param angle 旋转角度 单位弧度
     * @param K 尺度因子
     * @return 标准坐标系x，y
     */
    public  static double [] coordinateI2N(double X,double Y ,double offsetX,double offsetY,double angle,double K){

        double []  result  = new double[2];
        result[0]=offsetX+ X*K*Math.cos(angle) - Y*K*Math.sin(angle);
        result[1]=offsetY+ X*K*Math.sin(angle) + Y*K*Math.cos(angle);
//        int total = ((ownMoney[0] << 8) | (ownMoney[1] & 0xFF )   &0xFF );
        return result;
    }
    /**
     *
     * 标准坐标转独立坐标
     * @param X 标准坐标x
     * @param Y 标准坐标y
     * @param offsetX x平移
     * @param offsetY y平移
     * @param angle 旋转角度
     * @param K 尺度因子
     * @return 独立坐标系x，y
     */
    public  static double [] coordinateN2I(double X,double Y ,double offsetX,double offsetY,double angle,double K){

        double []  result  = new double[2];
        result[1] = (Y- offsetY - (X -offsetX)*Math.tan(angle))/(Math.tan(angle)*K*Math.sin(angle) + K*Math.cos(angle));
        result[0]=(X - offsetX + result[1]*K*Math.sin(angle))/(K*Math.cos(angle));
        return result;
    }

    //计算代码
    /**
     * 计算经度取决于对应点的选取，以及选取对应点的数据
     * @param B 系数矩阵数组
     * @param L 观测向量矩阵数组
     * @return double [] x偏移 y偏移 旋转角（单位弧度） 尺度因子
     */
    public  static  double [] getConver4Param(double [] [] B,double [] [] L){
        RealMatrix matrixL = new Array2DRowRealMatrix(L);
        RealMatrix matrixB = new Array2DRowRealMatrix(B);

        RealMatrix BT =matrixB.transpose();
        RealMatrix X = inverseMatrix(BT.multiply(matrixB)).multiply(BT).multiply(matrixL); //参数矩阵
        double dx = X.getEntry(0,0); //x平移
        double dy = X.getEntry(1,0); //y平移
        double a = X.getEntry(2,0);  //旋转角度
        double b = X.getEntry(3,0);  //尺度因子
        return  new double[]{dx,dy,Math.atan(b/(a+1)),Math.sqrt(((a+1)*(a+1))+(b*b))};

    }

    /**
     * 矩阵逆计算
     * @param A
     * @return
     */
    public static RealMatrix inverseMatrix(RealMatrix A) {
        RealMatrix result = new LUDecomposition(A).getSolver().getInverse();
        return result;
    }

    public static void main(String[] args) {
        //DX001	一级	3685531.985 	509941.326
        //DX002	一级	3685257.723 	510107.768

        //DX001	一级	3685028.654 	509939.968
        //DX002	一级	3684754.429 	510106.388
        double x1 = 3686180.5650;//转换前x1
        double y1 = 509422.6660;//转换前y1
        double x11 = 3685677.1450;//转换后x11
        double y11 = 509421.3790 ;//转换后y11
        double x2 = 3674949.9960 ;//转换前x2
        double y2 = 502072.6390;//转换前y2
        double x22 = 3674448.1100;//转换后x22
        double y22 = 502072.3560;//转换后y22

        double [] [] L = new double[2*2][1]; //数据对的数量*2
        L[0][0] =x11-x1;
        L[1][0] =y11-y1;
        L[2][0] =x22-x2;
        L[3][0] =y22-y2;
        double B[][] = new double[4][4];
        B[0][0] =1;
        B[0][1] =0;
        B[0][2] =x1;
        B[0][3] =-y1;
        B[1][0] =0;
        B[1][1] =1;
        B[1][2] =y1;
        B[1][3] =x1;
        B[2][0] =1;
        B[2][1] =0;
        B[2][2] =x2;
        B[2][3] =-y2;
        B[3][0] =0;
        B[3][1] =1;
        B[3][2] =y2;
        B[3][3] =x2;
     //*//*
        RealMatrix matrixL = new Array2DRowRealMatrix(L);
        RealMatrix matrixB = new Array2DRowRealMatrix(B);

        RealMatrix BT =matrixB.transpose();
        RealMatrix X = inverseMatrix(BT.multiply(matrixB)).multiply(BT).multiply(matrixL);
        double a = X.getEntry(2,0);
        double b = X.getEntry(3,0);
        System.out.println(X);//*//*
        double[] r =  getConver4Param(B,L);
        System.out.println(r[2]);
        System.out.println(r[3]);
    }

    /**
     * 高斯投影坐标反算
     * @param X 平面坐标x
     * @param Y 平面坐标y
     * @param L0 中央经线
     * @return 经纬度
     */
    public static double [] GaussProjInvCal(double X, double Y ,double L0) {
        double lat ,lon;
        Y-=500000;
        double []  result  = new double[2];
        double iPI = 0.0174532925199433;//pi/180
        double a = 6378137.0; //长半轴 m
        double b = 6356752.31414; //短半轴 m
        double f = 1/298.257222101;//扁率 a-b/a
        double e = 0.0818191910428; //第一偏心率 Math.sqrt(5)
        double ee = Math.sqrt(a*a-b*b)/b; //第二偏心率
        double bf = 0; //底点纬度
        double a0 = 1+(3*e*e/4) + (45*e*e*e*e/64) + (175*e*e*e*e*e*e/256) + (11025*e*e*e*e*e*e*e*e/16384) + (43659*e*e*e*e*e*e*e*e*e*e/65536);
        double b0 = X/(a*(1-e*e)*a0);
        double c1 = 3*e*e/8 +3*e*e*e*e/16 + 213*e*e*e*e*e*e/2048 + 255*e*e*e*e*e*e*e*e/4096;
        double c2 = 21*e*e*e*e/256 + 21*e*e*e*e*e*e/256 + 533*e*e*e*e*e*e*e*e/8192;
        double c3 = 151*e*e*e*e*e*e*e*e/6144 + 151*e*e*e*e*e*e*e*e/4096;
        double c4 = 1097*e*e*e*e*e*e*e*e/131072;
        bf = b0 + c1*Math.sin(2*b0) + c2*Math.sin(4*b0) +c3*Math.sin(6*b0) + c4*Math.sin(8*b0); // bf =b0+c1*sin2b0 + c2*sin4b0 + c3*sin6b0 +c4*sin8b0 +...
        double tf = Math.tan(bf);
        double n2 = ee*ee*Math.cos(bf)*Math.cos(bf); //第二偏心率平方成bf余弦平方
        double c = a*a/b;
        double v=Math.sqrt(1+ ee*ee*Math.cos(bf)*Math.cos(bf));
        double mf = c/(v*v*v); //子午圈半径
        double nf = c/v;//卯酉圈半径

        //纬度计算
        lat=bf-(tf/(2*mf)*Y)*(Y/nf) * (1-1/12*(5+3*tf*tf+n2-9*n2*tf*tf)*(Y*Y/(nf*nf))+1/360*(61+90*tf*tf+45*tf*tf*tf*tf)*(Y*Y*Y*Y/(nf*nf*nf*nf)));
        // lon=(1/Math.cos(bf))*(Y/nf) *(1 - (1/6 *(1+2*tf*tf+n2)*(Y*Y/(nf*nf))) + 1/120*(5+28*tf*tf+24*tf*tf*tf*tf+6*n2+8*n2*tf*tf)*(Y*Y*Y*Y/(nf*nf*nf*nf)));
        //经度偏差
        lon=1/(nf*Math.cos(bf))*Y -(1/(6*nf*nf*nf*Math.cos(bf)))*(1+2*tf*tf +n2)*Y*Y*Y + (1/(120*nf*nf*nf*nf*nf*Math.cos(bf)))*(5+28*tf*tf+24*tf*tf*tf*tf)*Y*Y*Y*Y*Y;
        result[0] =lat/iPI;
        result[1] =L0+lon/iPI;
        return result;
    }
}
