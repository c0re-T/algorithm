# LeetCode 49 字母异位词分组（key 必须是 String，`char[]` 当 key 会一组一个）

> 对应程序：[LeetCode49_GroupAnagrams.java](./LeetCode49_GroupAnagrams.java)
> 思路：给每个词造一个"同组同 key"的规范化字符串 —— 把字母排序 —— 然后按 key 装桶。
> 题面约束：只含小写字母，长度 1..100，数组长度 ≤ 10⁴。

---

## 一、`main` 的输出

```
[[eat, tea, ate], [bat], [tan, nat]]
```

分组过程（key 是排序后的串）：

| 词 | `Arrays.sort` 后的 key | 桶 |
| --- | --- | --- |
| eat | `aet` | `aet → [eat]` |
| tea | `aet` | `aet → [eat, tea]` |
| tan | `ant` | `ant → [tan]` |
| ate | `aet` | `aet → [eat, tea, ate]` |
| nat | `ant` | `ant → [tan, nat]` |
| bat | `abt` | `abt → [bat]` |

三个 key：`aet`、`abt`、`ant`。**注意输出的组顺序是 `aet, abt, ant` 吗？不是 —— 是 `eat组, bat组, tan组`**，
因为 `new ArrayList<>(map.values())` 拿的是 `HashMap` 的遍历顺序，既不是插入顺序也不是 key 的字典序。这一处第三、四节展开。

## 二、`new String(chars)` 不是多余的包装

```java
char[] chars = str.toCharArray();
Arrays.sort(chars);
String key = new String(chars);     // ← 这一句是关键，不是"转一下更顺眼"
```

如果直接把 `chars`（`char[]`）当 key，实测同一批 6 个词得到的是 **6 组**（应该是 3 组）：

```
   HashMap 比较 key 时先用 hashCode、再用 equals；数组这两个都退化成"按引用"，
   所以每一次 Arrays.sort 出来的新数组都是一个新 key，谁也不等于谁
```

**分组算法的全部逻辑都压在"key 的相等性"上**，所以 key 必须是一个按内容比较的类型。
`String` 的 `equals`/`hashCode` 按字符内容算 ✓ —— 这也是为什么这里非要绕一圈 `new String(chars)`。
（同一个道理在 哈希表 那类手写实现里更直白：他 dataStructures 仓库里 `HashTableDemo` 用 `id % size` 定位桶，
key 的哈希本身就是设计的一部分。）

## 三、容器决定分组的输出顺序

同一份数据、同一套分组，只换 `Map` 的实现（实测）：

```
   HashMap      : [[eat, tea, ate], [bat], [tan, nat]]
   LinkedHashMap: [[eat, tea, ate], [tan, nat], [bat]]
   TreeMap      : [[bat], [eat, tea, ate], [tan, nat]]
   内容是否同一套分组（排序后比对）：true
```

- `LinkedHashMap` 是**插入顺序**：第一个遇到的 key 是 `aet`(eat)，然后 `ant`(tan)，最后 `abt`(bat)。
- `TreeMap` 是**key 的字典序**：`abt`(bat) < `aet`(eat) < `ant`(tan) ✓ 和输出一致。
- `HashMap` 两者都不是 —— 由 `String.hashCode()` 和桶数量决定。

**三组内容完全相同，只是排列不同。** 题面说"可以按任意顺序返回答案"，所以他的 `HashMap` 写法是对的；
但要拿输出做断言、或者想把结果拿去 diff，就得固定成 `LinkedHashMap`（组内顺序本来就是插入序，`HashMap` 那版组内也是插入序，只有组的先后会变）。
（这和 贪婪算法电台覆盖 那篇里 `HashMap` 逐个 `put` vs `putAll` 换出两种答案是同一类问题，那边连"内容"都变了，是因为算法拿遍历顺序当了决策依据；这边只影响展示顺序，决策与顺序无关。）

## 四、和一份独立实现对拍

拿"按字母计数生成签名"做参考实现（完全不排序：统计每个字符出现次数拼成串），随机 5,000 组（长度 1..6，字母 a-d）：

```
   分组与计数法参考实现不同 —— 0 组
```

**排序 key 和计数 key 定义的是同一个等价类**，所以两份独立实现互相印证 ✓。

## 五、边界输入实测

```
   空数组            -> []
   {"", "", "a"}     -> [[, ], [a]]        ← 空串的 key 是空串，两个空串自成一桶
   {"eat","eat"}     -> [[eat, eat]]       ← 完全重复的两个词进同一组，组里两条
   {"Aa","aA","aa"}  -> [[Aa, aA], [aa]]   ← 大小写敏感：'A'(65) 排在 'a'(97) 前面
   {"a-1","1-a"}     -> [[a-1, 1-a]]       ← 符号和数字也照排不误
```

两点值得记：

- **打印含空串的组会很难读**（`[[, ], [a]]`），调试时看到这种"像语法错误"的输出别以为是 bug。
- 后两个例子已经**超出题面约定**（只含小写字母）。他这份实现不会拒绝，只会安静地按码元排序 ——
  `"Aa"` 和 `"aa"` 被分成两组是"按字符排序"的自然结果，但人类语感里它们是同一个词。真要忽略大小写得自己 `toLowerCase()`。
  （和 LeetCode415 笔记第八节同一类事：这类手写方法没有输入校验，超出约定时给的是"另一个答案"而不是错误。）

## 六、代价：排序 key 和计数 key 谁便宜，看词长

实测两种 key 的生成成本（同一批数据，都用 `HashMap`）：

| 数据 | 排序 key | 26 槽计数 key |
| --- | --- | --- |
| 20 万词 × 长度 6..11 | **59 ms** | 133 ms |
| 2 万词 × 200 字符 | 103 ms | **32 ms** |
| 2 万词 × 2000 字符 | 1,031 ms | **97 ms** |

结论不是"计数法更快"，而是**看词长**：

```
   排序 key  = O(k log k) + 一个长度为 k 的字符串
   计数 key  = O(k + 26) + 一个固定长度（约 52 字符）的签名串
   k = 10  时：排序只比"扫 26 个槽 + 拼签名"贵一点，固定开销反而赢 → 59 vs 133 ms
   k = 2000 时：log k ≈ 11，排序是计数的十几倍工作量 → 1,031 vs 97 ms（10.6 倍）
```

另一条容易翻车的路我也实测了：把计数的槽数从 26 扩到 128（想兼容符号和数字），**反而比排序还慢**。
同一批 2 万词 × 200 字符，三种做法：

```
   排序 key        ~90-103 ms
   26 槽计数 key    32 ms      ← 便宜
   128 槽计数 key  137 ms      ← 比排序还慢
```

每个词都要固定扫 128 次并拼一个长签名，常数全花在扫描上，把 O(k) 的优势吃光了。
**"理论更优"的写法不实测就别下结论。**

内存那一侧排序 key 是吃亏的：每组要存一个和词等长的 key，词越长 key 越长（2000 字符时 key 本身就占 4 KB）。

## 七、`containsKey` 那三回哈希查找

```java
if (!map.containsKey(key)) { map.put(key, new ArrayList<>()); }
map.get(key).add(str);         // containsKey、put、get：同一个桶走了三遍
```

换成一行 `map.computeIfAbsent(key, x -> new ArrayList<>()).add(str)` 语义完全一样。实测 20 万词：

```
   containsKey+put+get 41 ms（9661 组）| computeIfAbsent 29 ms（9661 组）   分组内容一致: true
```

**1.4 倍**，组数与内容完全相同。这是这轮实测里唯一一处"少一次哈希查找真省下看得见的钱"的地方
（对照 1 题里 `containsKey+get` → `get` 的改动，实测只有噪声级差别）。

## 八、`main` 下面注释掉的那份：O(n²) 且依赖 JDK 21

注释版是另一种思路：一边扫一边拿 `visited[]` 归组，词两两比较排序结果。两个实测到的点：

1. **`result.reversed()` 是 JDK 21 才有的 API**（`SequencedCollection`）。同一份代码：

```
   javac RevTest.java            -> 通过
   javac --release 17 RevTest.java -> 错误: 找不到符号  方法 reversed()
```

   他本机命令行是 JDK 21，所以注释里这句一旦放开能编；但如果这个项目被拿去做 JDK 17 的构建（或 CI 用 17），
   这行会直接编译失败。想兼容就写 `Collections.reverse(result)`。
2. 那份的复杂度是 O(n² × k log k)（每个 i 都对后面的 j 重排一遍词），且**没有任何 memoization**：
   `strs[i]` 的排序在每一轮内层循环里被重算一次。它是拿来对照思路的，不适合当实现。

## 九、包结构的一处隐患（顺带实测）

他现在是一题一个包（`p1_twosum`、`p49_groupanagrams`），两个文件里都写着 `class Solution`，
靠不同包名互不干扰 —— 这是"题解类"最常见的命名冲突解法。实测把两个 `class Solution` 放进同一个包：

```
错误: 重复: pkgdemo.Solution
class Solution {}
```

**一个包里只能有一个 `Solution`。** 以后如果他想把 hot100 的题都堆在 `leetcode/hot100/` 一层，
第二个文件的 `class Solution` 就会当场把编译顶掉；保持现在"一题一包"，或者把辅助类改成题目相关的名字
（`TwoSumSolution`、`AnagramSolution`）都行。

## 十、一句话记住

```
   分组算法的全部正确性挂在"key 按内容相等"上，所以 new String(chars) 不能省 ——
   拿 char[] 当 key 实测变成 6 组（应为 3 组）；
   输出的组顺序由容器决定（HashMap / LinkedHashMap / TreeMap 实测三种顺序，内容同一套），
   题面允许任意顺序，但要 diff 结果就得钉死顺序；
   排序 key 和计数 key 是同一个等价类（5000 组随机对拍 0 差异），谁快看词长：
   短词排序赢（59 vs 133ms），长词计数碾压（97 vs 1031ms），把槽扩到 128 反而更慢；
   computeIfAbsent 实测比 containsKey+put+get 快 1.4 倍；
   注释那份用了 JDK 21 的 List.reversed()，--release 17 实测编不过。
```
