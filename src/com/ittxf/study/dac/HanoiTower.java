package com.ittxf.study.dac;

public class HanoiTower {
    public static void main(String[] args) {
        hanoiTower(5, 'A', 'B', 'C');
    }

    /**
     * 汉诺塔问题
     * 使用分治思想解决
     * @param n 盘子数量
     * @param a 源柱子，从这里移动
     * @param b 辅助柱子，帮助移动盘子
     * @param c 目标柱子，最终存放盘子
     */
    public static void hanoiTower(int n, char a, char b, char c) {
        if (n == 1) { // 如果只有一个盘
            System.out.println("第1个盘从" + a + "->" + c);
        } else { // 如果我们有n >= 2情况，我们总是可以看做是两个盘 最下边的一个盘 和 上面的所有盘
            // 1 先把最上面的所有盘A->B，移动过程会使用到C
            hanoiTower(n - 1, a, c, b);
            // 2 把最下边的一个盘A->C
            System.out.println("第" + n + "个盘从" + a + "->" + c);
            // 3 把最上面的所有盘B->C，移动过程会使用到A
            hanoiTower(n - 1, b, a, c);
        }
    }
}
