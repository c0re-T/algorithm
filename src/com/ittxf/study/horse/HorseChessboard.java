package com.ittxf.study.horse;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;

public class HorseChessboard {
    private static int X; // 棋盘的行数
    private static int Y; // 棋盘的列数
    private static boolean visited[]; // 记录棋盘位置是否被访问过，初始化为false
    private static boolean finished; // 记录是否完成，初始化为false
    public static void main(String[] args) {
        // 测试：8 * 8 的棋盘，从位置(0, 0)开始，是否能走完所有位置？
        X = 8;
        Y = 8;
        int row = 1; // 起始位置行
        int col = 1; // 起始位置列
        visited = new boolean[X * Y]; // 初始化为false
        int[][] chessboard = new int[X][Y]; // 创建棋盘
        // 测一下耗时
        long start = System.currentTimeMillis();
        traversalChessboard(chessboard, row - 1, col - 1, 1);
        long end = System.currentTimeMillis();
        System.out.println("耗时: " + (end - start) + " ms");

        System.out.println("棋盘情况：");
        for (int[] ints : chessboard) {
            for (int i : ints) {
                System.out.print(i + "\t");
            }
            System.out.println();
        }
    }

    /**
     * 根据当前位置，计算马儿还能走哪些位置，并返回结果
     * @param curPoint 当前位置
     * @return 可以走的位置集合
     */
    public static ArrayList<Point> next(Point curPoint) {
        ArrayList<Point> points = new ArrayList<>();
        Point p1 = new Point(); // 判断是否可以走这个位置
        if ((p1.x = curPoint.x - 2) >= 0 && (p1.y = curPoint.y - 1) >= 0) {
            points.add(new Point(p1));
        }
        if ((p1.x = curPoint.x - 1) >= 0 && (p1.y = curPoint.y - 2) >= 0) {
            points.add(new Point(p1));
        }
        if ((p1.x = curPoint.x - 2) >= 0 && (p1.y = curPoint.y + 1) < Y) {
            points.add(new Point(p1));
        }
        if ((p1.x = curPoint.x - 1) >= 0 && (p1.y = curPoint.y + 2) < Y) {
            points.add(new Point(p1));
        }
        if ((p1.x = curPoint.x + 2) < X && (p1.y = curPoint.y - 1) >= 0) {
            points.add(new Point(p1));
        }
        if ((p1.x = curPoint.x + 1) < X && (p1.y = curPoint.y - 2) >= 0) {
            points.add(new Point(p1));
        }
        if ((p1.x = curPoint.x + 2) < X && (p1.y = curPoint.y + 1) < Y) {
            points.add(new Point(p1));
        }
        if ((p1.x = curPoint.x + 1) < X && (p1.y = curPoint.y + 2) < Y) {
            points.add(new Point(p1));
        }
        return points;
    }

    /**
     * 完成骑士周游问题的算法
     * @param chessboard 棋盘
     * @param row 当前位置行
     * @param column 当前位置列
     * @param step 第几步，初始位置也算一步，从1开始
     */
    public static void traversalChessboard(int[][] chessboard, int row, int column, int step) {
        chessboard[row][column] = step;
        visited[row * X + column] = true; // 标记该位置已访问，row * X + column 表示一维数组的下标
        // 获取当前位置可以走的下一个位置的集合
        ArrayList<Point> ps = next(new Point(column, row));
        // 对ps进行排序，排序规则：下一步的位置的可走位置的个数，进行递增排序
        sortNext(ps);
        // 遍历ps
        while (!ps.isEmpty()) {
            Point p = ps.removeFirst(); // 取出下一个可以走的位置
            if (!visited[p.y * X + p.x]) { // 如果该位置没有被访问过
                traversalChessboard(chessboard, p.y, p.x, step + 1); // 继续递归
            }
        }
        // 没有走完，回溯
        //说明： step < X * Y 成立的情况有两种
        // 1 棋盘到目前位置，仍然没有走完
        // 2 棋盘处于一个回溯过程
        if (step < X * Y && !finished) {
            chessboard[row][column] = 0; // 回溯
            visited[row * X + column] = false;
        } else finished = true;
    }

    // 根据当前这一步的棋盘位置，获取所有可能的下一步的位置
    public static void sortNext(ArrayList<Point> ps) { // 获取当前点的下一个点的集合
        ps.sort(new Comparator<Point>() {
            @Override
            public int compare(Point o1, Point o2) {
                // 根据下一步的位置可走的步数进行排序
                int size = next(o1).size();
                int size2 = next(o2).size();
                // if (size < size2) return -1;
                // else if (size > size2) return 1;
                // else return 0;
                return Integer.compare(size, size2); // 递增排序
            }
        });
    }
}
