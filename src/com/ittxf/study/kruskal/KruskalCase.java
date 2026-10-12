package com.ittxf.study.kruskal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class KruskalCase {
    private int edgeNum; // 边的个数
    private char[] vertexArr; // 顶点数组
    private int[][] matrix; // 邻接矩阵
    // 使用 INF 表示两个顶点不能连通
    private static final int INF = Integer.MAX_VALUE;
    public static void main(String[] args) {
        char[] vertex = {'A', 'B', 'C', 'D', 'E', 'F', 'G'};
        int[][] matrix = {
                {0, 12, INF, INF, INF, 16, 14},
                {12, 0, 10, INF, INF, 7, INF},
                {INF, 10, 0, 3, 5, 6, INF},
                {INF, INF, 3, 0, 4, INF, INF},
                {INF, INF, 5, 4, 0, 2, 8},
                {16, 7, 6, INF, 2, 0, 9},
                {14, INF, INF, INF, 8, 9, 0}
        };
        KruskalCase kruskalCase = new KruskalCase();
        kruskalCase.kruskalCase(vertex, matrix);
        kruskalCase.printMatrix();

        kruskalCase.kruskal();
    }

    /**
     * 初始化
     * @param vertex 顶点数组
     * @param matrix 邻接矩阵
     */
    private void kruskalCase(char[] vertex, int[][] matrix) {
        // 初始化边的个数和顶点个数
        int vertexNum = vertex.length;
        vertexArr = new char[vertexNum];
        // 初始化顶点数组，复制拷贝的方式
        for (int i = 0; i < vertexNum; i++) vertexArr[i] = vertex[i];

        // 初始化邻接矩阵，复制拷贝的方式
        this.matrix = new int[vertexNum][vertexNum];
        for (int i = 0; i < vertexNum; i++) {
            for (int j = 0; j < vertexNum; j++) this.matrix[i][j] = matrix[i][j];
        }

        // 统计边的个数
        for (int i = 0; i < vertexNum; i++) {
            for (int j = i + 1; j < vertexNum; j++) {
                if (this.matrix[i][j] != INF) edgeNum++;
            }
        }
    }

    // 打印邻接矩阵
    private void printMatrix() {
        System.out.println("邻接矩阵为：");
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (this.matrix[i][j] == INF) {
                    // 遇到10000就打印无穷大符号
                    System.out.printf("%5s", "∞");
                } else {
                    // 普通数字，占6个宽度右对齐
                    System.out.printf("%5d", this.matrix[i][j]);
                }
            }
            System.out.println();
        }
    }

    /**
     * 对边进行排序
     * @param edges 边的数组
     */
    private void sortEdges(ArrayList<EData> edges) {
        Collections.sort(edges);
    }

    /**
     * 获取顶点的下标，如果找不到，返回-1
     * @param vertex 顶点
     */
    private int getVertexIndex(char vertex) {
        for (int i = 0; i < vertexArr.length; i++) {
            if (vertexArr[i] == vertex) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 获取边的信息，放在EData[]数组中，返回
     * @return 边的数组
     */
    private ArrayList<EData> getEdges() {
        ArrayList<EData> edges = new ArrayList<>();
        for (int i = 0; i < vertexArr.length; i++) { // 遍历顶点
            for (int j = i + 1; j < vertexArr.length; j++) { // 遍历没有访问过的顶点
                if (matrix[i][j] != INF) { // 如果两个顶点之间有边
                    edges.add(new EData(vertexArr[i], vertexArr[j], matrix[i][j])); // 获取边的信息
                }
            }
        }
        return edges;
    }

    /**
     * 获取下标为i的顶点的终点，用于判断两个顶点是否在同一个集合中
     * @param ends 数组记录了各个顶点对应的终点是哪个，是在遍历过程中逐渐确定的
     * @param i 顶点的下标
     * @return 返回的就是下标为i的这个顶点对应的终点的下标
     */
    private int getVertexEnd(int[] ends, int i) {
        while (ends[i] != 0) { // 如果当前顶点的终点不是自己
            i = ends[i];
        }
        return i;
    }

    /**
     * 克鲁斯卡尔算法，用于求解最小生成树
     */
    private void kruskal() {
        int index = 0; // 表示kruskal边数组的索引
        int[] ends = new int[vertexArr.length]; // 用于保存已有最小生成树中每个顶点的终点是哪个
        EData[] res = new EData[vertexArr.length - 1]; // 保存最小生成树的边

        // 获取图中所有的边的集合，边的个数：12 条
        ArrayList<EData> edges = this.getEdges();
        System.out.println("边的集合为：" + edges + "，共" + edges.size() + "条边");

        // 按照边的权值大小进行排序（从小到大）
        sortEdges(edges);
        System.out.println("边的集合从小到大为：" + edges);
        for (int i = 0; i < edgeNum; i++) {
            // 获取边的两个顶点的下标
            int p1 = getVertexIndex(edges.get(i).start);
            int p2 = getVertexIndex(edges.get(i).end);
            // 获取边的两个顶点的终点
            int m = getVertexEnd(ends, p1);
            int n = getVertexEnd(ends, p2);
            // 如果两个顶点的终点不同，则说明没有形成环路，可以加入kruskal边数组中，即加入最小生成树
            if (m != n) {
                ends[m] = n;
                res[index++] = edges.get(i);
            }
        }
        System.out.println("顶点数组=" + Arrays.toString(vertexArr));
        System.out.println("ends数组=" + Arrays.toString(ends));
        // 输出kruskal边数组
        System.out.println("最小生成树为：");
        for (EData re : res) {
            System.out.println(re);
        }
    }
}

// 边的数据结构
class EData implements Comparable<EData> {
    char start; // 边的起点
    char end; // 边的终点
    int weight; // 边的权值

    // 构造函数
    public EData(char start, char end, int weight) {
        this.start = start;
        this.end = end;
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "EData{" +
                "<" + start +
                ", " + end +
                "> weight=" + weight +
                '}';
    }

    @Override
    public int compareTo(EData o) {
        return this.weight - o.weight;
    }
}
