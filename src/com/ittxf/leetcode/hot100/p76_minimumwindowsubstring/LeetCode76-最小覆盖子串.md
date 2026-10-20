# LeetCode 76 最小覆盖子串（`count` 记的是"字符个数"，不是"种类数"）

> 对应程序：[LeetCode76_MinimumWindowSubstring.java](./LeetCode76_MinimumWindowSubstring.java)
> `main` 用 `s = "ADOBECODEBANC"`、`t = "ABC"`，实测输出 `BANC`（LeetCode 标准答案）。
> `need[128]` 记每个字符还欠几个；`count` 记"已经凑齐的**字符个数**"；窗口一旦凑齐就收缩左边界。

---

## 一、收缩阶段的实际打点

给同一份逻辑加打印，只列"凑齐后开始收缩"的那些轮（实测）：

| right | left | 窗口 | 长度 | |
| --- | --- | --- | --- | --- |
| 5 | 0 | `ADOBEC` | 6 | ← 第一次凑齐 A/B/C，刷新最小 |
| 10 | 1 | `DOBECODEBA` | 10 | |
| 10 | 2 | `OBECODEBA` | 9 | |
| 10 | 3 | `BECODEBA` | 8 | |
| 10 | 4 | `ECODEBA` | 7 | |
| 10 | 5 | `CODEBA` | 6 | |
| 12 | 6 | `ODEBANC` | 7 | |
| 12 | 7 | `DEBANC` | 6 | |
| 12 | 8 | `EBANC` | 5 | ← 刷新 |
| 12 | 9 | `BANC` | **4** | ← 最终答案 |

两个现象值得记住：

- **`left` 只增不减**，每个字符进窗口一次、出窗口一次 → `O(n)`。
- 收缩的过程里答案会反复刷新，**不是"第一次凑齐就是答案"** —— 第 5 轮那个 `ADOBEC` 长 6，最后是 4。

## 二、`count` 为什么能等于 `t.length()`

```java
if (need[rightChar] > 0) count++;   // 进窗口：还欠着，才算"凑到一个有用的"
need[rightChar]--;                  // 欠额 -1；多余字符会让它变成负数
...
char leftChar = s.charAt(left);
need[leftChar]++;                   // 出窗口：把欠额还回去
if (need[leftChar] > 0) count--;    // 还完之后仍 > 0，说明刚移走一个"有用的"，凑齐数 -1
left++;
```

`count` 的语义是**个数**：`t = "ABC"` 时要凑齐 3 个字符。所以判满是 `count == t.length()`。
如果把 `count` 当成"种类数"（用 `HashMap` 统计不同字符），条件就得改成"所有种类都够" —— 两种写法都对，
但混着写就会错。这也解释了为什么 `need` 可以变成负数：窗口里多出来的字符也得记着，收缩时才还得回去。

**两个 `> 0` 的位置不对称**：进窗口判"还欠"，出窗口判"还完仍欠"。
和 438 那题的 `> 0` / `>= 0` 是同一类符号陷阱，写反了不会崩、只会悄悄给错答案 —— 好在有差分对拍兜着。

## 三、差分对拍

固定种子 20,000 组随机（`s` 长度 1..12、`t` 长度 1..4，字母表 A..D，保证大量无法覆盖的用例）：

```
   与"枚举所有子串 + 逐字符比频次"的暴力不同 —— 0 组
```

## 四、三处边界（两处会抛异常）

```
   t 比 s 长        -> ""            （第一行 s.length() < t.length() 挡了）
   s / t 为 null    -> ""            （第一行挡了）
   t 为空串         -> StringIndexOutOfBoundsException: Index 3 out of bounds for length 3
   含非 ASCII（"中文"/"中"）-> ArrayIndexOutOfBoundsException: Index 20013 out of bounds for length 128
```

两处异常都来自题面没覆盖的输入：

- **`t` 为空**时 `t.length() == 0`，而 `count` 一开始就是 0，`while (count == t.length())` **恒真**，
  `left` 会一路缩到超过右边界，`s.charAt(left)` 越界。题面保证 `t` 长度 ≥ 1，所以交上去没事；
  想当工具方法用，第一行补一句 `t.isEmpty()` 即可。
- **`need` 是 `int[128]`**，只覆盖 ASCII：`'中'` 是 20013，直接越界。题面只含大小写字母，所以安全；
  但这也意味着这个方法**不能处理中文**（对比 `p3` 用 `Map<Character, Integer>` 就免疫）。

## 五、代价

| 输入 | 结果 |
| --- | --- |
| `s` 长 200,000、`t` 长 500（随机小写字母） | 滑动窗口 **10 ms**，结果长度 590 |
| 同规模暴力（每个起点往后扩、每步比 128 格频次） | `O(n²·\|Σ\|)`，量级上不可行 |

`O(n)` 的来源是第一节那两条：`right` 走 n 步、`left` 总共也只走 n 步，
而每一步的加减都是 `O(1)` 的数组下标运算。

## 六、一句话记住

```
   count 记"凑齐的字符个数"（不是种类），所以判满是 count == t.length()，need 允许变负；
   收缩阶段会反复刷新答案（ADOBEC 长 6 → 最终 BANC 长 4），不是第一次凑齐就完事；
   20,000 组与暴力 0 差异；|s|=20万时 10 ms；
   t 为空串会让 while 恒真、left 缩过界 -> StringIndexOutOfBoundsException；
   int[128] 只覆盖 ASCII，'中' 直接越界（p3 用 Map 就没事）。
```
