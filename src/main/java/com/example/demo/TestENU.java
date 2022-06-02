package com.example.demo;

/**
 * @author wuchonghua
 * @create 2022-06-01 15:08
 */
public class TestENU {

    public static double[] wgs84ToEcef(double lat, double lon, double h) {
        double a = 6378137;
        double b = 6356752.3142;
        double f = (a - b) / a;
        double e_sq = f * (2 - f);
        double lamb = Math.toRadians(lat);
        double phi = Math.toRadians(lon);
        System.out.println("lamb"+lamb);
        System.out.println("phi"+phi);
        double s = Math.sin(lamb);
        double N = a / Math.sqrt(1 - e_sq * s * s);
        double sin_lambda = Math.sin(lamb);
        double cos_lambda = Math.cos(lamb);
        double sin_phi = Math.sin(phi);
        double cos_phi = Math.cos(phi);
        double x = (h + N) * cos_lambda * cos_phi;
        double y = (h + N) * cos_lambda * sin_phi;
        double z = (h + (1 - e_sq) * N) * sin_lambda;
        return new double[]{x,y,z};
    }

    public static double[] ecefToEnu(double x, double y, double z, double lat, double lng, double height) {
        double a = 6378137;
        double b = 6356752.3142;
        double f = (a - b) / a;
        double e_sq = f * (2 - f);
        double lamb = Math.toRadians(lat);
        double phi = Math.toRadians(lng);
        System.out.println("lamb2"+lamb);
        System.out.println("phi2"+phi);
        double s = Math.sin(lamb);
        double N = a / Math.sqrt(1 - e_sq * s * s);
        double sin_lambda = Math.sin(lamb);
        double cos_lambda = Math.cos(lamb);
        double sin_phi = Math.sin(phi);
        double cos_phi = Math.cos(phi);
        double x0 = (height + N) * cos_lambda * cos_phi;
        double y0 = (height + N) * cos_lambda * sin_phi;
        double z0 = (height + (1 - e_sq) * N) * sin_lambda;
        double xd = x - x0;
        double yd = y - y0;
        double zd = z - z0;
        double t = -cos_phi * xd - sin_phi * yd;
        double xEast = -sin_phi * xd + cos_phi * yd;
        double yNorth = t * sin_lambda + cos_lambda * zd;
        double zUp = cos_lambda * cos_phi * xd + cos_lambda * sin_phi * yd + sin_lambda * zd;
        return new double[] { xEast, yNorth, zUp };
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        //gpggaH "10.800000000"10.8
        //gpslat "31.494067233"31.48999455
        //gpslon "121.947391683"121.9440825
        //gpggaH2 "12.700000000"12.7
        //gpslat2 "31.489996667"31.489996667
        //gpslon2 "121.947391667"121.94408167
//        double[] arr1 = wgs84ToEcef(31.48999455, 121.9440825, 10.800000000);//此处经纬度是需要比对的偏移经纬度
//        double[] xyz=ecefToEnu(arr1[0],arr1[1], arr1[2],31.489996667, 121.94408167,  12.700000000);//此处经纬度是站点经纬度

//        double[] arr1 = wgs84ToEcef(36.7399177551, 116.9395751953, 0);//此处经纬度是需要比对的偏移经纬度
//        double[] xyz=ecefToEnu(arr1[0],arr1[1], arr1[2],37, 117,  10.3);//此处经纬度是站点经纬度

        double[] arr1 = wgs84ToEcef(37, 117,  10.3);//此处经纬度是需要比对的偏移经纬度
        double[] xyz=ecefToEnu(arr1[0],arr1[1], arr1[2],36.7399177551, 116.9395751953, 0);//此处经纬度是站点经纬度
        System.out.println("xyz[0]"+xyz[0]);
        System.out.println("xyz[1]"+xyz[1]);
        System.out.println("xyz[2]"+xyz[2]);
    }
}
