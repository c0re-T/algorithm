# LeetCode 74 搜索二维矩阵（把矩阵当一维有序数组二分）

> 对应程序：[LeetCode74_SearchA2DMatrix.java](./LeetCode74_SearchA2DMatrix.java)
> `main` 在 `{{1,3,5,7},{10,11,16,20},{23,30,34,60}}` 里找 3 和 13，实测：
>
> ```
> true
> false
> ```

---

## 一、`mid` 映射回矩阵：一行 n 个，除 n 取行、模 n 取列

```java
int rt = m * n - 1;
while (lt <= rt) {
    int mid = lt + (rt - lt) / 2;
    if (matrix[mid / n][mid % n] == target) return true;
    ...
}
```

题面给的是"每行升序、下一行首元素大于上一行末元素"，所以**按行拼起来就是一个严格升序的一维数组**。这里不真的拼，`mid / n` 和 `mid % n` 就是那个一维下标到二维坐标的映射。二分骨架与一维完全一样。

## 二、穷举与随机对拍

5,000 张随机严格递增矩阵（1..4 行 × 1..4 列，行主序填递增值），每张把目标扫过值域上下各多两个：

```
82,616 组查询：与暴力逐格扫描 0 差异
```

1×1 矩阵命中/未命中也都对。

## 三、`matrix[0].length` 是唯一列数来源，锯齿矩阵会被它坑

| 矩阵 | 目标 | 结果 |
| --- | --- | --- |
| `new int[0][]` | 任意 | `ArrayIndexOutOfBoundsException: Index 0 out of bounds for length 0` |
| `{{}}`（1 行 0 列） | 1 / 0 | `false` / `false`（`rt = -1`，循环不进） |
| `{{1,2,3},{4}}`（第 0 行长、后面短） | 4 | `ArrayIndexOutOfBoundsException: Index 1 out of bounds for length 1` |
| `{{1},{1,2}}`（第 0 行短、后面长） | 2 | `false` —— **静默漏掉**（只看得到第 0 列） |

`null` 是 `NullPointerException: Cannot read the array length because "<parameter1>" is null`。题面保证矩形，所以这些只在题面之外。

## 四、注释里那份"先展平再二分"

```java
int[] flatMatrix = Arrays.stream(matrix).flatMapToInt(Arrays::stream).toArray();
```

结果也对，但**先把整张矩阵复制一遍**：1000×1000 实测多花 **13 ms / 4,000,000 字节**，而直接 `mid / n` 的版本单次查询约 **1 µs**。规模大时这份拷贝完全是白给的。另外注释版用 `while (lt < rt)`，最后单独检查 `flatMatrix[lt] == target`，逻辑等价。

## 五、边界与副作用

- 入参只读，同一张矩阵连调两次结果一致。
- `mid = lt + (rt - lt) / 2` 不溢出；`m * n - 1` 在可分配内存范围内也不会溢出。
- 空矩阵崩在 `matrix[0]`，与 p240 一样，用前先判 `m == 0`。

## 六、一句话记住

```
   行主序严格递增，就把矩阵当一维数组二分：matrix[mid / n][mid % n]；
   5,000 张随机矩阵、82,616 组查询与暴力 0 差异；
   空矩阵崩在 matrix[0]，{{}} 给 false；
   锯齿矩阵第 0 行更长就 AIOOBE、更短就静默漏列（{{1},{1,2}} 找 2 给 false）；
   注释里的展平版结果对，但 1000×1000 要多花 13 ms / 4 MB，直接映射单次约 1 µs；
   入参只读。
```
