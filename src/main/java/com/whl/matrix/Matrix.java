package com.whl.matrix;

import it.unisa.dia.gas.jpbc.Element;


/**
 * 作者：whl
 * 日期：2023-01-05 0:10
 * 描述：矩阵库
 */
public class Matrix {

    /**矩阵乘以矩阵
     * 一个 a[n*m] 矩阵，乘积 b[m*p] 矩阵，得到 n*p 矩阵
     * @param a
     * @param b
     * @return
     */
    public static Element[][] multMM(Element[][] a, Element[][] b) throws Exception {
        int M = a[0].length;//M为a的列数
        int N = a.length;//N为a的行数
        int P = b[0].length;//P为b的列数
        int Q = b.length;//Q为b的行数

        Element[][] c = new Element[N][P];//矩阵c取a的行数，b的列数

        if(M != Q){
            throw new Exception("矩阵a的列数与b的行数不相等");
        }

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < P; j++) {
                for (int k = 0; k < M; k++) {
//                    c[i][j] += a[i][k] * b[k][j];
                    c[i][j] = c[i][j].add(a[i][k].mul(b[k][j]));
                }
            }
        }
        return c;
    }

    /**
     * 向量乘以矩阵
     * 就是相当于只有一行n列的矩阵与矩阵相乘，比二维矩阵相乘少一层循环。
     */
    public static Element[] multVM(Element[] y,Element[][] a)
    {
        int N = y.length;
        Element[] c = new Element[N];
        int M = y.length;
        if( M != a[0].length)
        {
            //抛出异常
        }
        for(int i = 0; i < N; i++)
        {
            for(int j = 0; j < M; j++)
            {
//                c[i] += a[i][j] * y[i];
                c[i] = c[i].add(a[i][j].mul(y[i]));
            }
        }
        return c;
    }

    /**
     * 矩阵乘以向量
     * 就是相当于只有一行n列的矩阵与矩阵相乘，比二维矩阵相乘少一层循环。
     */
    public static Element[][] multMV(Element[][] M,Element[][] V)
    {
        int l = M.length;//矩阵M的行
        int n = M[0].length;//矩阵M的列
        int k = V.length;//列向量的行数
        Element[][] c = new Element[l][1];

        if( l != k)
        {
            //抛出异常
        }
        for(int i = 0; i < k; i++)
        {
            for(int j = 0; j < l; j++)
            {

            }
        }
        return c;
    }

//    public static void main(String[] args) throws Exception {
//        Element[][] M = {
//                {2,3,4,5},
//                {4,5,6,7},
//                {7,8,9,10}
//        };
//        double[][] V = {
//                {2},
//                {6},
//                {8},
//                {6}
//        };
//
////        System.out.println(M.length);
//        Element[][] result = multMM(M, V);
//        for (int i = 0; i < result.length; i++) {
//            for (int j = 0; j < result[0].length; j++) {
//                if(j == 0){
//                    System.out.printf("");
//                }
//                System.out.printf(result[i][j] + "");
//            }
//            System.out.println();
//        }
//    }
}
