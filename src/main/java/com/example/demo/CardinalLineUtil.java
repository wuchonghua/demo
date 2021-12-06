package com.example.demo;

import lombok.Data;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CardinalLineUtil {

    public static void main(String[] args) {
        CoordinateXY xy1 = new CoordinateXY();
//        xy1.x = 186.375234386534;
//        xy1.y = 683.543482554611;
        xy1.x = 6.804539427645e+05;
        xy1.y = 5.422483642942e+06;
//        double hdg = 2.267456090169;
        double hdg = 5.287405485081e+00;
        CoordinateUV uv1 = getUVByXY(xy1, xy1, hdg);
        System.out.println(uv1);
        CoordinateXY xy2 = new CoordinateXY();
//        xy2.x = 181.105930602760;
//        xy2.y = 691.450559412129;
        xy2.x = 680454.9385277651968570802505193;
        xy2.y = 5422483.7348957778818865628080043;
        CoordinateUV uv2 = getUVByXY(xy2, xy1, hdg);
        System.out.println(uv2);
        CoordinateXY xy3 = new CoordinateXY();
        xy3.x = 180.131621274631;
        xy3.y = 693.481309524737;
        CoordinateUV uv3 = getUVByXY(xy3, xy1, hdg);
        System.out.println(uv3);
        Coefficient c = getCoefficient(uv1, uv1, uv2, uv2, 0.5);
        System.out.println(c);
    }

    @Data
    static class CoordinateUV {
        private double u;
        private double v;
    }

    static double[][] getCardinalMatrix(double tension) {
        double[][] d = new double[4][4];
        d[0][0] = -tension; d[0][1] = 2.0 - tension; d[0][2] = tension - 2.0; d[0][3] = tension;
        d[1][0] = 2.0 * tension; d[1][1] = tension - 3.0; d[1][2] = 3.0- 2 * tension; d[1][3] = -tension;
        d[2][0] = -tension; d[2][1] = 0.0; d[2][2] = tension; d[2][3] = 0.0;
        d[3][0] = 0; d[3][1] = 1.0; d[3][2] = 0.0; d[3][3] = 0.0;
        return d;
    }

    @Data
    static class SingleCoefficient {
        double a;
        double b;
        double c;
        double d;
    }

    @Data
    static class Coefficient {
        SingleCoefficient u;
        SingleCoefficient v;
    }

    static Coefficient getCoefficient(CoordinateUV c1, CoordinateUV c2, CoordinateUV c3, CoordinateUV c4, double tension) {
        Coefficient c = new Coefficient();
        SingleCoefficient u = new SingleCoefficient();
        SingleCoefficient v = new SingleCoefficient();

        double[][] d = getCardinalMatrix(tension);
        u.a = d[3][0] * c1.u + d[3][1] * c2.u + d[3][2] * c3.u + d[3][3] * c4.u;
        u.b = d[2][0] * c1.u + d[2][1] * c2.u + d[2][2] * c3.u + d[2][3] * c4.u;
        u.c = d[1][0] * c1.u + d[1][1] * c2.u + d[1][2] * c3.u + d[1][3] * c4.u;
        u.d = d[0][0] * c1.u + d[0][1] * c2.u + d[0][2] * c3.u + d[0][3] * c4.u;

        v.a = d[3][0] * c1.v + d[3][1] * c2.v + d[3][2] * c3.v + d[3][3] * c4.v;
        v.b = d[2][0] * c1.v + d[2][1] * c2.v + d[2][2] * c3.v + d[2][3] * c4.v;
        v.c = d[1][0] * c1.v + d[1][1] * c2.v + d[1][2] * c3.v + d[1][3] * c4.v;
        v.d = d[0][0] * c1.v + d[0][1] * c2.v + d[0][2] * c3.v + d[0][3] * c4.v;

        c.u = u;
        c.v = v;
        return c;
    }

    @Data
    static class CoordinateXY {
        private double x;
        private double y;
    }

    static CoordinateUV getUVByXY(CoordinateXY cXy, CoordinateXY cXyStart, double hdg) {
        double a = Math.cos(hdg);
        double b = Math.sin(hdg);
        CoordinateUV uv = new CoordinateUV();
        uv.v = (cXy.y - cXyStart.y - (b * cXy.x / a) + (b * cXyStart.x / a)) / (a + (b * b / a));
        uv.u = (cXy.x - cXyStart.x + b * uv.v) / a;
        return uv;
    }




}