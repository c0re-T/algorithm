package com.ittxf.study.floyd;

import java.util.Arrays;

public class FloydAlgorithm {
    public static void main(String[] args) {
        final int N = 65535;
        // 创建顶点数组
        char[] vertex = { 'A', 'B', 'C', 'D', 'E', 'F', 'G' };
        // 创建邻接矩阵
        int[][] matrix = {
                { 0, 5, 7, N, N, N, 2 },
                { 5, 0, N, N, N, 3, N },
                { 7, N, 0, N, 8, N, N },
                { N, 9, N, 0, N, 4, N },
                { N, N, 8, N, 0, 5, 4 },
                { N, N, N, 4, 5, 0, 6 },
                { 2, 3, N, N, 4, 6, 0 }
        };
        // 创建图
        Graph graph = new Graph(vertex.length, vertex, matrix);
        graph.floyd(); // 调用弗洛伊德算法
        graph.show();

    }
}

class Graph {
    private char[] vertex; // 顶点数组
    private int[][] distance; // 保存，从各个顶点出发到其它顶点的距离，最后的结果，也是保留在该数组
    private int[][] pre; // 保存到达目标顶点的前驱顶点

    /**
     * 构造器
     * @param length  顶点数组的长度，即顶点个数
     * @param vertex  顶点数组
     * @param matrix  邻接矩阵
     */
    public Graph(int length, char[] vertex, int[][] matrix) {
        this.vertex = vertex;
        this.distance = matrix;
        this.pre = new int[length][length];
        // 初始化pre数组，注意存放的是前驱顶点的下标
        for (int i = 0; i < length; i++) {
            Arrays.fill(pre[i], i); // pre[i]数组填充为i，表示从顶点i到顶点i的前驱顶点为i
        }
    }

    // 显示pre数组和dis数组
    public void show() {
        // 便于阅读
        char[] vertex = this.vertex;
        for (int k = 0; k < distance.length; k++) {
            for (int i = 0; i < distance.length; i++) { // 遍历pre数组的一行数据
                System.out.print(vertex[pre[k][i]] + " ");
            }
            System.out.println();
            for (int i = 0; i < distance.length; i++) { // 遍历dis数组的一行数据
                System.out.print("(" + vertex[k] + "到" + vertex[i] + "的最短距离为" + distance[k][i] + ") ");
            }
            System.out.println();
            System.out.println();
        }
    }

    //弗洛伊德算法，比较容易理解，而且更容易实现
    public void floyd() {
        int len = 0; // 保存距离
        for (int k = 0; k < distance.length; k++) { // 遍历中间顶点，k表示中间顶点的下标
            for (int i = 0; i < distance.length; i++) { // 遍历顶点，i表示起始顶点的下标
                for (int j = 0; j < distance.length; j++) { // 遍历顶点，j表示结束顶点的下标
                    len = distance[i][k] + distance[k][j]; // 通过顶点k出发，到达顶点i和顶点j的距离
                    if (len < distance[i][j]) { // 如果小于，说明找到了更短的路径
                        distance[i][j] = len; // 更新距离
                        pre[i][j] = pre[k][j]; // 更新前驱顶点
                    }
                }
            }
        }
    }

}

