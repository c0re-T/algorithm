package com.ittxf.study.dijkstra;

import java.util.Arrays;

public class DijkstraAlgorithm {
    public static final int N = 65535;
    public static void main(String[] args) {
        char[] vertex = {'A', 'B', 'C', 'D', 'E', 'F', 'G'};
        int[][] matrix = {
                {N,5,7,N,N,N,2},
                {5,N,N,9,N,N,3},
                {7,N,N,N,8,N,N},
                {N,9,N,N,N,4,N},
                {N,N,8,N,N,5,4},
                {N,N,N,4,5,N,6},
                {2,3,N,N,4,6,N}
        };
        // 创建图的邻接矩阵
        Graph graph = new Graph(vertex, matrix);
        // 测试
        graph.showResult();
        // 测试迪杰斯特拉算法
        graph.dijkstra(6);
        graph.show();
    }
}

class Graph {
    private char[] vertex; // 顶点数组
    private int[][] matrix; // 邻接矩阵
    private VisitedVertex visitedVertex; // 记录访问情况和最短路径

    // 构造方法
    public Graph(char[] vertex, int[][] matrix) {
        this.vertex = vertex;
        this.matrix = matrix;
    }

    // 显示结果
    public void showResult() {
        for (int[] ints : matrix) {
            System.out.println(Arrays.toString(ints));
        }
    }

    /**
     * 按照Dijkstra算法，计算出发顶点到其他顶点的最短路径
     * @param index 出发顶点下标
     */
    public void dijkstra(int index) {
        visitedVertex = new VisitedVertex(vertex.length, index);
        update(index); // 更新index顶点到周围顶点的距离和周围顶点的前驱顶点
        for (int i = 1; i < vertex.length; i++) {
            index = visitedVertex.updateArr();
            update(index);
        }
    }

    // 更新index下标顶点到周围顶点的距离和周围顶点的前驱顶点
    private void update(int index) {
        int len = 0;
        // 根据遍历我们的邻接矩阵的第index行
        for (int j = 0; j < matrix[index].length; j++) {
            // len 含义是：出发顶点到index顶点的距离 + 从index顶点到j顶点的距离的和
            len = visitedVertex.getDis(index) + matrix[index][j];
            // 如果len比出发顶点到j顶点的距离小并且j顶点没有被访问过，更新
            if (len < visitedVertex.getDis(j) && !visitedVertex.in(j)) {
                visitedVertex.updatePre(j, index); // 更新j顶点的前驱为index顶点
                visitedVertex.updateDis(j, len); // 更新出发顶点到j顶点的距离
            }
        }
    }

    public void show() {
        visitedVertex.show();
    }
}

class VisitedVertex {
    int[] alreadyArr; // 记录各个顶点是否已经访问过，1表示已访问，0表示未访问，会不断更新
    int[] distance; // 从起始顶点到其他顶点的距离，会不断更新
    int[] pre; // 记录到达每个顶点的前驱顶点，会不断更新

    /**
     * 构造方法
     * @param length 顶点个数
     * @param index 起始顶点下标
     */
    public VisitedVertex(int length, int index) {
        alreadyArr = new int[length];
        distance = new int[length];
        pre = new int[length];
        Arrays.fill(distance, 65535); // 初始化距离为65535
        alreadyArr[index] = 1; // 初始化起始顶点为已访问
        this.distance[index] = 0; // 初始化起始顶点的距离为0
    }

    /**
     * 判断顶点是否已经访问过
     * @param index 顶点下标
     * @return 如果访问过返回true，否则返回false
     */
    public boolean in(int index) {
        return alreadyArr[index] == 1;
    }

    /**
     * 更新出发顶点到index顶点的距离
     * @param index 顶点下标
     * @param len 距离
     */
    public void updateDis(int index, int len) {
        this.distance[index] = len;
    }

    /**
     * 更新出发顶点到index顶点的前驱顶点为pre
     * @param index 顶点下标
     * @param pre 前驱顶点下标
     */
    public void updatePre(int index, int pre) {
        this.pre[index] = pre;
    }

    /**
     * 返回出发顶点到index顶点的距离
     * @param index 顶点下标
     * @return 出发顶点到index顶点的距离
     */
    public int getDis(int index) {
        return distance[index];
    }

    // 继续选择并返回新的访问顶点，比如这里的G完后，就是A点作为新的访问顶点(注意不是出发顶点)
    public int updateArr() {
        int min = 65535, index = 0;
        for (int i = 0; i < alreadyArr.length; i++) {
            if (alreadyArr[i] == 0 && distance[i] < min) {
                min = distance[i];
                index = i;
            }
        }
        // 标记为已访问
        alreadyArr[index] = 1;
        return index;
    }

    // 显示结果
    // 即三个数组的情况
    public void show() {
        System.out.println("alreadyArr = " + Arrays.toString(alreadyArr));
        System.out.println("distance = " + Arrays.toString(distance));
        System.out.println("pre = " + Arrays.toString(pre));

        char[] vertex = {'A', 'B', 'C', 'D', 'E', 'F', 'G'};
        int count = 0;
        for (int i : distance) {
            if (i != 65535) {
                System.out.println(vertex[count] + " (" + i + ")");
            }else {
                System.out.println("N");
            }
            count++;
        }
        System.out.println();
    }
}