# LeetCode 131 分割回文串（回溯：切一刀、验一下、再往下切）

> 对应程序：[LeetCode131_PalindromePartitioning.java](./LeetCode131_PalindromePartitioning.java)
> `main` 传的是 `"aab"`，实测：
>
> ```
> [[a, a, b], [aa, b]]
> ```

---

## 一、`i` 是这一段起点，`j` 是这一段的终点

```java
for (int j = i; j < s.length(); j++) {
    if (isPalindrome(s, i, j)) {
        path.add(s.substring(i, j + 1));
        backtrack(res, path, s, j + 1);
        path.removeLast();
    }
}
```

每一层决定"下一刀切在哪"：`[i, j]` 是回文才切，切完从 `j + 1` 继续；`i == s.length()` 说明整串切完了，收一份结果。`isPalindrome` 双指针从两头往中间走，长度 1 也算回文，所以"一个字符一段"永远合法。深度最多 n 层，n 大也不会爆栈，风险在结果条数。

## 二、穷举对拍

| 字母表 | 长度 | 字符串数 | 与"枚举所有切法再筛回文"参考的差异 |
| --- | --- | --- | --- |
| `{a,b}` | 0..8 | 511 | 0 |
| `{a,b,c}` | 0..6 | 1,093 | 0 |

`"abacdc"` 实测给 4 组：`[a,b,a,c,d,c]`、`[a,b,a,cdc]`、`[aba,c,d,c]`、`[aba,cdc]`，手工核过没有漏也没有重复。

## 三、忘了 `removeLast` 会拼出越来越长的假路径

| 输入 | 原版 | 删掉撤销 |
| --- | --- | --- |
| `"aa"` | `[[a,a], [aa]]` | `[[a,a], [a,a,aa]]` |
| `"aab"` | `[[a,a,b], [aa,b]]` | `[[a,a,b], [a,a,b,aa,b]]` |

511 个二元串里 **506 个**结果不同。原因是 `path` 只增不减，下一条分支带着上一条的残留继续走；而 `"a"`、`"ab"` 这类只有单一路径的输入看起来完全正常 —— 又是"main 测不出来"的一类。

## 四、`removeLast()` 是 JDK 21 API

和 p39 一样：本机 JDK 21 默认编译通过，`javac --release 17` 直接

```
error: cannot find symbol
        path.removeLast();
            ^
  symbol:   method removeLast()
```

要兼容低版本，写成 `path.remove(path.size() - 1)`。

## 五、边界与最坏情况

| 输入 | 结果 |
| --- | --- |
| `""` | `[[]]`（size=1，内层 size=0）—— 空串被当成"切完了"的一个解 |
| `null` | `NullPointerException: Cannot invoke "String.length()" because "<parameter3>" is null` |
| `"a"` | `[[a]]` |
| `"ab"` | `[[a,b]]` |

全是同一个字符时每个位置都可以切，条数 = **2ⁿ⁻¹**，实测与这个数逐一相符：

| n | 条数 | 耗时 |
| --- | --- | --- |
| 16 | 32,768 | 2 ms |
| 18 | 131,072 | 10 ms |
| 20 | 524,288 | 69 ms |
| 21 | 1,048,576 | 105 ms |
| 22 | 2,097,152 | 212 ms |
| 23 | 4,194,304 | 373 ms |

同一实例连调两次结果相同，`res` / `path` 都是局部变量。

## 六、一句话记住

```
   i 是本段起点，j 是本段终点；[i, j] 是回文才切，切完从 j + 1 递归；
   511 个二元串 + 1,093 个三元串与"枚举切法"参考 0 差异；
   删掉 path.removeLast() 后 506/511 出错："aa" 变 [[a,a],[a,a,aa]]，"aab" 的路径越拼越长；
   removeLast() 是 JDK 21 API，--release 17 编译不过；
   空串返回 [[]]，null NPE；
   全同字符条数 = 2ⁿ⁻¹：n=23 的 419 万条 373 ms —— 瓶颈是输出条数，不是栈。
```
