package com.whl.policy;

/**
 * 作者：whl
 * 日期：2023-01-11 15:12
 * 描述：
 */
public class AA {
    private Integer[][] matrix;//矩阵M
    private String[] rho;//映射ρ
    private Integer[] attr_path;//omega的下标
    private Integer[] omega;//omegai的值组成的数组

    public AA() {
    }

    public AA(Integer[][] matrix, String[] rho) {
        this.matrix = matrix;
        this.rho = rho;
    }

    //矩阵行数
    public int matrixGetL(){
        return matrix.length;
    }
    //矩阵列数
    public int matrixGetN(){
        return matrix[0].length;
    }

    //获取矩阵第i行的元素
    public Integer[] matrixRow_i(Integer index){
        Integer[] row = new Integer[matrix[0].length];
        for (int i = 0; i < matrix[0].length; i++) {
            row[i] = matrix[index][i];
        }
        return row;
    }

    public Integer[][] getMatrix() {
        return matrix;
    }

    public void setMatrix(Integer[][] matrix) {
        this.matrix = matrix;
    }

    public String[] getRho() {
        return rho;
    }

    public void setRho(String[] rho) {
        this.rho = rho;
    }

    public Integer[] getAttr_path() {
        return attr_path;
    }

    public void setAttr_path(Integer[] attr_path) {
        this.attr_path = attr_path;
    }

    public Integer[] getOmega() {
        return omega;
    }

    public void setOmega(Integer[] omega) {
        this.omega = omega;
    }
}
