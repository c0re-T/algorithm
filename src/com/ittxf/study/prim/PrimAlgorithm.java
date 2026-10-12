package com.ittxf.study.prim;

public class PrimAlgorithm {
    public static void main(String[] args) {
        // 创建图的节点数据
        char[] data = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G'};
        // 创建图的邻接矩阵
        int[][] adjMatrix = {
                {10000,5,7,10000,10000,10000,2},
                {5,10000,10000,9,10000,10000,3},
                {7,10000,10000,10000,8,10000,10000},
                {10000,9,10000,10000,10000,4,10000},
                {10000,10000,8,10000,10000,5,4},
                {10000,10000,10000,4,5,10000,6},
                {2,3,10000,10000,4,6,10000},
        };
        Graph graph = new Graph(data.length);
        MiniSpanTree miniSpanTree = new MiniSpanTree();
        miniSpanTree.createGraph(graph, data.length, data, adjMatrix);
        miniSpanTree.showGraph(graph);

        miniSpanTree.prim(graph, 0);
    }
}

/**
 * 创建最小生成树
 */
class MiniSpanTree {

    /**
     * 创建图
     * @param graph 图
     * @param vertexCount 节点个数
     * @param data 节点数据
     * @param adjMatrix 邻接矩阵
     */
    public void createGraph(Graph graph, int vertexCount, char[] data, int[][] adjMatrix) {
        for (int i = 0; i < vertexCount; i++) { // 遍历节点
            graph.data[i] = data[i]; // 节点数据
            for (int j = 0; j < vertexCount; j++) { // 遍历要连接的节点
                graph.adjMatrix[i][j] = adjMatrix[i][j]; // 邻接矩阵
            }
        }
    }

    // 显示图的邻接矩阵
    public void showGraph(Graph graph) {
        for (int i = 0; i < graph.vertexCount; i++) {
            for (int j = 0; j < graph.vertexCount; j++) {
                if (graph.adjMatrix[i][j] == 10000) {
                    // 遇到10000就打印无穷大符号
                    System.out.printf("%4s", "∞");
                } else {
                    // 普通数字，占6个宽度右对齐
                    System.out.printf("%4d", graph.adjMatrix[i][j]);
                }
            }
            System.out.println();
        }
    }

    /**
     * prim算法
     * @param graph 图
     * @param start 开始节点
     */
    public void prim(Graph graph, int start) {
        int[] visited = new int[graph.vertexCount]; // 用于标识节点是否被访问过，0表示未访问，1表示已访问
        visited[start] = 1; // 当前节点记录为已访问
        int h1 = start, h2 = start; // h1 和 h2 分别表示最小生成树的两个节点
        int miniWeight = 10000; // 用于保存最小生成树的权值
        int totalWeight = 0; // 用于保存最小生成树的总权值
        for (int k = 1; k < graph.vertexCount; k++) { // 遍历所有节点，边数为N-1

            // 遍历所有已访问节点和未访问节点，找到已访问节点和未访问节点之间的最小权值
            for (int i = 0; i < graph.vertexCount; i++) { // 遍历所有已访问节点
                for (int j = 0; j < graph.vertexCount; j++) { // 遍历所有未访问节点
                    if (visited[i] == 1 && visited[j] == 0 && graph.adjMatrix[i][j] < miniWeight) {
                        // 替换最小权值和对应的节点
                        miniWeight = graph.adjMatrix[i][j];
                        h1 = i;
                        h2 = j;
                    }
                }
            }
            // 输出最小边的权值
            System.out.println("边< " + graph.data[h1] + ", " + graph.data[h2] + " > 权值：" + miniWeight);
            visited[h2] = 1; // 将未访问节点记录为已访问
            totalWeight += miniWeight; // 累加最小权值
            miniWeight = 10000; // 重置最小权值为无穷大
        }
        System.out.println("最小生成树的总权值为：" + totalWeight);
    }
}

/**
 * 创建图
 */
class Graph {
    int vertexCount; // 节点个数
    char[] data; // 节点数据
    int[][] adjMatrix; // 邻接矩阵

    public Graph(int vertexCount) {
        this.vertexCount = vertexCount;
        data = new char[vertexCount];
        adjMatrix = new int[vertexCount][vertexCount];
    }
}

