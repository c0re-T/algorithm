# 算法练习 · Java 手写实现与笔记

二十三个小程序，一个知识点一份可运行的 `.java` + 一份 `.md` 笔记。笔记里不抄定义，只讲三件事：**这段代码为什么这么写、下标/参数怎么动、写错一处会长成什么样**，所有数字都是在本机实跑出来的。

`src/com/ittxf/study/` 是课程知识点（分治、查找、DP、贪心、字符串匹配、图、回溯），`src/com/ittxf/leetcode/` 是 LeetCode 题解 —— 其中 `hot100/` 下**一题一个包**，按技巧能归四组：双指针（11 / 42 / 15）、滑动窗口（3 / 239 / 438）、哈希表（1 / 49 / 128）、前缀和（560）。包名 `com.ittxf`，笔记和程序放在同一个目录下。

## 思路图

这些程序在同一根轴上：**每一轮之后，剩下的问题有多大**；哈希表那一档换的是另一个东西 —— 用一张表把"再扫一遍"变成"查一次"。

```mermaid
flowchart LR
    A["每一轮之后<br/>剩下的问题有多大"]
    A --> B["汉诺塔（分治）<br/>两份 n-1<br/>T=2T(n-1)+1"]
    A --> C["二分查找（减治）<br/>一份 n/2<br/>T=T(n/2)+1"]
    A --> D["暴力匹配 / 415 竖式加法<br/>每次少一位"]
    A --> E["KMP<br/>主串指针一动不动<br/>只滑模式串"]
    A --> F["0-1 背包<br/>不缩小，改成铺表<br/>n × capacity 格"]
    A --> G["马踏棋盘<br/>回溯 + 一步贪心<br/>把树剪成链"]
    A --> H["LeetCode 1 / 49 / 128<br/>不比大小，改查表<br/>O(n²) → O(n)"]
    A --> I["双指针 / 滑动窗口<br/>每轮丢掉一定更差的一族候选"]
    A --> J["前缀和 + 哈希<br/>把区间问题变成点对问题"]
    B -->|"实测 n=34 要 171 亿步"| R["指数：2^n-1 是下界<br/>不是实现的锅"]
    C -->|"实测 n=1e7 只要 24 轮"| L["对数：规模翻 10 倍多 3 步"]
    D -->|"1e6 数据实测 1099 万次比较"| M2["暴力匹配 O(m·n)<br/>KMP 同数据 200 万次"]
    E -->|"同数据 200 万次"| L
    F -->|"84,064 次递归调用 vs 1,275 格"| M["伪多项式：容量当数值看<br/>W=1e9 时表填不下"]
    G -->|"8x8 实测 1.2 亿次调用 → 4.7 万次"| H2["启发式不保证完胜<br/>12x12 实测 20 秒搜不完"]
    H -->|"2 万元素：暴力 38ms vs 哈希 2ms"| M3["空间换时间<br/>代价是那张表"]
    I -->|"p11 n=1.6万：暴力 63ms，双指针 0ms"| N["候选被成批排除<br/>所以指针只走 n 步"]
    J -->|"有 0 或负数时滑窗失效<br/>实测错 10,306 / 50,000 组"| L
    B -.->|"递归树里没有重复子问题<br/>记忆化救不了它"| F
    F -.->|"同一层 dp[i-1][*] 被下游反复需要"| C
    C -.->|"left / right 向中间收缩"| D
    G -.->|"同样是递归，但要靠排序剪枝"| B
    H -.->|"key 的相等性就是分组规则"| F
```

第二张图是全仓库那条主线：**写漏半句、忘了复位、两句顺序颠倒，后果通常不是报错，而是安静地给一个像模像样的错答案。**

```mermaid
flowchart TD
    E["读起来完全正常的输出"]
    E --> F1["汉诺塔：跳出条件 n == 1 没挡负数"]
    E --> F2["二分：<= 写成 < / mid±1 少一格"]
    E --> F3["415：while 少了 forward != 0"]
    E --> F4["暴力匹配：return i - j + 1"]
    E --> F5["贪心：平局用严格 <，第一个赢"]
    E --> F6["Prim：miniWeight 复位，h1/h2 不复位"]
    E --> F7["Kruskal：ends[i]=0 一身两用"]
    E --> F8["Dijkstra：65535 既是无穷大也是天花板"]
    E --> F9["Floyd：this.distance = matrix 不拷贝"]
    E --> F10["马踏棋盘：finished 是 static 且从不复位"]
    E --> F11["两数之和：put 写在 containsKey 之前"]
    E --> F12["分组：拿 char[] 当 HashMap 的 key"]
    E --> F13["最长子串：left 直接赋值，去掉 Math.max"]
    E --> F14["水桶：动了较高的那一边"]
    E --> F15["239：k &gt; n 没挡，直接开负长度数组"]
    E --> F16["560：删掉 put(0, 1)"]
    E --> F17["三数之和：去掉命中后的 left++ / right--"]
    E --> F18["438：输入里冒出一个大写字母"]
    F1 -->|"n = 0"| G1["钻约 19,988 层后 StackOverflowError"]
    F2 -->|"单点窗口 / 不存在的值"| G2["10 个目标丢 4 个；另有一种直接死循环"]
    F3 -->|"99 + 1"| G3["答案是 00，最高位整个丢掉"]
    F4 -->|"实测 92,256 次命中"| G4["92,256 次全部 +1，一次都没对过"]
    F5 -->|"同一份数据换个 HashMap 建法"| G5["选中集合从 K1 换成 K4"]
    F6 -->|"图不连通 / 有一条 20000 的边"| G6["复述上一轮的边并塞进 10000"]
    F7 -->|"边被写成 &lt;C,A&gt;"| G7["判环失效 → ArrayIndexOutOfBoundsException"]
    F8 -->|"40000 + 40000"| G8["可达的点被打成 N"]
    F9 -->|"floyd() 之后"| G9["调用方那个邻接矩阵已经不是邻接矩阵了"]
    F10 -->|"同一个 JVM 跑第二次"| G10["0 ms 打出 59 出现两次的假棋盘"]
    F11 -->|"[3,3] target 6"| G11["返回 [0,0]：自己配自己"]
    F12 -->|"6 个词"| G12["变成 6 组，每组一条"]
    F13 -->|"abba"| G13["50,000 组随机错 27,081 次"]
    F14 -->|"[1,1,0]"| G14["得 0，真值是 1"]
    F15 -->|"k=5, n=3"| G15["NegativeArraySizeException: -1"]
    F16 -->|"[3,1,1] k=3"| G16["30.39% 的随机数据悄悄漏答案"]
    F17 -->|"命中一次之后"| G17["指针原地不动 → 死循环"]
    F18 -->|"A - a = -32"| G18["26 格频次表直接越界"]
```

## 四份图程序用的其实是两张图

四份程序都叫 `Graph`、都是 7 个顶点 A..G 的邻接矩阵，很容易以为是同一张图。逐格核过之后是这样的：

| 程序 | "无边"用的常量 | 对角线 | 上三角边数 | 对称性 | 结果 |
| --- | --- | --- | --- | --- | --- |
| `PrimAlgorithm` | `10000` | 10000 | 10 | 完全对称 | 最小生成树总权 25 |
| `KruskalCase` | `Integer.MAX_VALUE` | 0 | 12 | 完全对称 | **另一张图**，最小生成树总权 36 |
| `DijkstraAlgorithm` | `65535` | 65535 | 10 | 完全对称 | **和 Prim 同一张图**，从 G 出发 `[2,3,9,10,4,6,0]` |
| `FloydAlgorithm` | `65535` | 0 | 9 | **6 处不对称** | B 的三条边只填了一半，实际是张有向图 |

三个能直接拿来当自查用的结论：

- **Prim 和 Dijkstra 是同一张图**（10 条边逐条一致），所以可以对着看"最小生成树 ≠ 最短路"：Prim 选出的树里 A→D 只能走 `A-G-E-F-D` 花 15，而真正的最短路是 `A-G-F-D` 花 12（那条 `G-F(6)` 压根没进生成树）。
- **Kruskal 是另一张图**（12 条边），所以 25 和 36 之间没有任何可比性。
- **Floyd 的矩阵漏填了 `B→D` 和 `B→G`、又多出一条只有去程的 `B→F`**，从 B 出发的 4 对距离因此和对称矩阵上的 Dijkstra 不一致（其余 42 对全一致）。它不报错、输出很漂亮 —— 这类录入错误只能靠"矩阵和自己转置比一遍"来抓。

## 知识点清单

| 板块 | 程序 | 笔记 | 核心点 |
| --- | --- | --- | --- |
| 分治 | `HanoiTower.java` | [汉诺塔.md](src/com/ittxf/study/dac/汉诺塔.md) | 把形参读成一句自然语言「把 n 个盘从 a 搬到 c，可借 b」；**实测 n=1..12 的逐步移动对照真棋盘模拟，0 次违规、每次都真的搬完**；打印次数 = 方法调用次数 = 2^n-1，而最大递归深度只有 n —— 慢在步数不在栈；纯计数实测 n=24→34 是 16ms→16.4s（正好 2^10 倍），**打印比递归本身贵约 360 倍**；`n == 1` 少了 `<=` → `hanoiTower(0, ...)` 实测钻到约 2 万层爆栈 |
| 查找 | `BinarySearchNoRecursion.java` | [二分查找非递归.md](src/com/ittxf/study/binarysearchnorecursion/二分查找非递归.md) | **`<=` / `mid-1` / `mid+1` 是一整套**：去掉 `=` 实测丢 4/10 个答案，少写一格实测死循环，而**能命中的 target 表现完全正常**；重复值返回"路上第一个撞到的"；**不检查数组有没有序**：2 万组随机乱序数组实测 64.81% 漏答、0 次假阳性；8 万数据实测目标在下标 0 时线性 1 次比较 vs 二分 16 次；`n` 从 10 到 1000 万实测轮数 4→24 = `ceil(log2 n)` |
| LeetCode | `LeetCode415_AddStrings.java` | [LeetCode415-字符串相加.md](src/com/ittxf/leetcode/LeetCode415-字符串相加.md) | 逐位推演 `"456"+"77"="533"`；**删掉 `forward != 0` 实测 `"99"+"1"`→`"00"`，而不进位的用例照样对**；穷举确认进位只会是 0/1；20 万位相加实测 6ms，`BigInteger` 981ms；`-'0'` 是纯算术，实测 `"12a"+"3"`→`"172"`、`"1 2"+"3"`→`"06-5"` |
| 哈希表 | `p1_twosum/LeetCode1_TwoSum.java` | [LeetCode1-两数之和.md](src/com/ittxf/leetcode/hot100/p1_twosum/LeetCode1-两数之和.md) | 把"找一对"换成"找补数"，一遍扫 + 一张表；**`containsKey` 写在 `put` 之前是"不许自己配自己"的唯一保证** —— 实测把两句调换：`[3,3]/6` 返回 `[0,0]`、`[1,1,1]/2` 返回 `[0,0]`、单个 `3` 配 `6` 也返回 `[0,0]`（应为无解），而 `main` 里那组 `[2,7,11,15]` 恰好测不出来；同一个值只留最后一个下标 → 解不唯一时返回的不是最靠前那对（60,000 组里 8,581 次不同，**全部合法**）；120,000 组随机对拍四类失败全 0；无解返回 `new int[0]` → 按长度 2 取值实测 AIOOBE；`target - nums[i]` 在题面 ±10⁹ 下不溢出但只剩约 7% 余量；实测 n=2 万 哈希 2ms vs 暴力 38ms、n=200 万 哈希 135ms（暴力要 2 万亿次比较）；`containsKey+get` 改成一次 `get` 实测只差在噪声里（5ms vs 4ms）；**注释里那份暴力没有 `break`，返回的是最后一组解**（实测 `[0,2]` vs `[1,3]`） |
| 哈希表 | `p49_groupanagrams/LeetCode49_GroupAnagrams.java` | [LeetCode49-字母异位词分组.md](src/com/ittxf/leetcode/hot100/p49_groupanagrams/LeetCode49-字母异位词分组.md) | 全部逻辑压在"key 按内容相等"上，所以 `new String(chars)` 不能省 —— **实测拿 `char[]` 当 key 会把 6 个词分成 6 组（应 3 组）**，因为数组的 `hashCode`/`equals` 都按引用；输出的组顺序由容器决定（`HashMap` / `LinkedHashMap` / `TreeMap` 实测三种顺序、内容同一套，`TreeMap` 那版能对上 key 的字典序 `abt<aet<ant`）；与"按字母计数"的独立实现对拍 5,000 组 **0 差异**；排序 key vs 26 槽计数 key **看词长定胜负**：短词排序赢（59 vs 133ms）、2000 字符时计数碾压（97 vs 1,031ms），而**把槽扩到 128 反而比排序还慢**（137 vs ~100ms）；`computeIfAbsent` 实测比 `containsKey+put+get` 快 1.4 倍（41→29ms / 20 万词）；含空串时打印成 `[[, ], [a]]` 容易误读；注释那份用了 JDK 21 的 `List.reversed()`，实测 `--release 17` **编译不过** |
| 双指针 | `p11_containerwithmostwater/LeetCode11_ContainerWithMostWater.java` | [LeetCode11-盛最多水的容器.md](src/com/ittxf/leetcode/hot100/p11_containerwithmostwater/LeetCode11-盛最多水的容器.md) | `min(两柱高) × 宽度`，宽度是 `right - left` 不是柱高；**只能动较矮的一边**——动较高的那边等于把"高度不可能更高、宽度还变小"的一族候选留下当答案，实测反过来写 **20,000 组随机里错 12,980 组（64.90%）**，最短反例只要 3 个数：`[1,1,0]` 正确 1、反过来写得 0；他的示例（9 根柱子）逐轮打点核对，第 2 轮就取到 49 之后全是徒劳；20,000 组与暴力 **0 差异**；n=16,000 实测双指针 0 ms vs 暴力 63 ms；这份没有 null 判断（`p42`、`p3` 有），传 null 直接 NPE |
| 双指针 | `p42_trappingtainwater/LeetCode42_TrappingRainWater.java` | [LeetCode42-接雨水.md](src/com/ittxf/leetcode/hot100/p42_trappingtainwater/LeetCode42-接雨水.md) | 每格的水 = `min(左最高, 右最高) - height[i]`，难点是"没扫到的那一侧"；`leftMax < rightMax` 就是在担保**右边已经有一根不比 leftMax 矮的柱子**，所以左格可以立刻结算 —— 每轮恰好有一侧定案，故 `O(n)` 时间 `O(1)` 空间；真实逐轮打点（11 轮、贡献 `下标2=1、4=1、5=2、6=1、9=1`，合计 6）**结算顺序不是从左到右**：下标 11/10/9/8 先被右支结掉；20,000 组与逐列暴力 **0 差异**，把 `<` 换成 `<=` 也 0 差异；`[5,1,5]`→4、`[2,0,2]`→2、长度 ≤2 直接 0、null 也挡了 |
| 双指针+去重 | `p49_3sum/LeetCode49_3Sum.java` | [LeetCode15-三数之和.md](src/com/ittxf/leetcode/hot100/p49_3sum/LeetCode15-三数之和.md) | 排序是"内层双指针方向唯一"和"三处跳重"的共同前提，`nums[i] > 0` 就 `break` 只在 target=0 时成立；20,000 组与"暴力+按值去重"**0 差异**；**三处跳重各自管什么，是逐个删掉测出来的**：删外层 `continue` → 40.88% 的随机实例条数变多、他的示例也从 2 组变 3 组；删内层两个 `while` → 18.84% 变多但**他的示例完全测不出来**（`[-2,0,0,2,2]` 才会重复）；删命中后的 `left++; right--` → **指针原地不动，直接死循环**（不是给错答案）；注释那份三重循环在他的示例上多给 1 组（3 vs 2），随机数据 61.25% 组数对不上；方法里 `Arrays.sort(nums)` 把入参排序了 + 多打一行；n=300 实测 0 ms vs 三重循环 22 ms；**题号写成了 49**（三数之和是第 15 题，与同层 `p49_groupanagrams` 撞号） |
| 原地划分 | `p283_movezeroes/LeetCode283_MoveZeroes.java` | [LeetCode283-移动零.md](src/com/ittxf/leetcode/hot100/p283_movezeroes/LeetCode283-移动零.md) | 两段式（非零前移 + 尾部补零）能原地的全部理由是一条不变量 **`index <= i` 恒成立**，写目标永远不越过读目标（20,000 组与"另开数组"的稳定划分参考 **0 差异**）；`nums[index] = nums[i]` 在没有零的数组上是纯粹的自写自（`[1,2,3,4]` 实测 4 次），存在但无害；与交换写法结果逐组一致而**写次数更少**（20,000 组里交换更多 14,663 次、相等 5,337 次、覆盖更多的情况 0 次）；注释那份额外数组版里 `Arrays.fill` 是多余的（`new int[n]` 本就全 0），也不合题面"原地"要求；这份没挡 null |
| 滑动窗口 | `p3_.../LeetCode3_LongestSubstringWithoutRepeatingCharacters.java` | [LeetCode3-无重复字符的最长子串.md](src/com/ittxf/leetcode/hot100/p3_longestsubstringwithoutrepeatingcharacters/LeetCode3-无重复字符的最长子串.md) | `main` 用 `"1R1T7"` → 4；**`left = Math.max(left, map.get(c) + 1)` 里那个 max 是"left 只许前进"的守门人**：直接赋值时 `"abba"` 得 3（真值 2），50,000 组随机 **错 27,081 次（54.16%）**，而且它同时是 O(n) 复杂度论证的前提（指针一旦能后退就不是线性了）；注释说 value 存的是"索引 + 1"，代码存的其实是 `right`、`+1` 写在取值处 —— 照着注释改会造成"双重 +1"，实测 200,000 组**错 101,958 次**，最短反例 `"aab"`（2 → 1），而 `"abcabc"`、`"abba"` 在这种错法下仍然对；用 `Map` 而不是定长数组，才让大写 `R`/`T` 照跑（对比 438 越界） |
| 滑动窗口 | `p239_slidingwindowmaximum/LeetCode239_SlidingWindowMaximum.java` | [LeetCode239-滑动窗口最大值.md](src/com/ittxf/leetcode/hot100/p239_slidingwindowmaximum/LeetCode239-滑动窗口最大值.md) | 队列存**下标**而不是值（值会重复，判不出"滑出窗口"）；四步顺序：扔过期 → 扔被压 → 进队 → 记录；逐轮打点看他的 `[1,3,-1,-3,5,3,6,7]`：`i=6` 时下标 4 是被新值**压掉**的而不是过期扔掉的 —— 真正在瘦身的是第二步；20,000 组随机对拍 0 差异，**队尾 `<` 还是 `<=`、队头 `if` 还是 `while` 都不影响正确性**（注释里"一定要用 `<=`"不准确，它只让队列平均长度从 2.70 变 3.04）；`k=1`/`k=n`/空数组/`k=0` 都正常，但 **`k > n` 抛 `NegativeArraySizeException: -1`**（`new int[n-k+1]`），开头的 `n==0||k==0` 挡不住；注释那份 `O(nk)` 在 n=32万/k=1600 上实测 216 ms vs 6 ms |
| 滑动窗口 | `p438_.../LeetCode438_FindAllAnagramsInAString.java` | [LeetCode438-找到字符串中所有字母异位词.md](src/com/ittxf/leetcode/hot100/p438_findallanagramsinastring/LeetCode438-找到字符串中所有字母异位词.md) | `need[]` 中途会**变负数**（表示窗口里这个字符超额了），所以进窗口判 `> 0`、出窗口判 `>= 0` 两处符号故意不对称，写成一样就错报命中；8,000 组随机与"每个窗口排序"版 **0 差异**，`"cbaebabacd"/"abc"` → `[0, 6]`；他 `main` 那个长串命中 150 个起点、间隔恰好 26；排序版是**对的**只是慢 —— 实测 |s|=10万、|p|=100 时 2 ms vs 157 ms，但**在他 main 那个 3,900 长度上两种写法都测不出差别**（规模不够大的复现等于没复现）；`'A' - 'a' = -32` → 26 格数组 **AIOOBE**，题面保证小写所以不触发，但用定长数组就等于把字符集写进契约（p3 用 Map 就免疫）；注释里那句"一定要用 `<=`"同 239 一样只是避免冗余 |
| 前缀和 | `p560_subarraysumequalsk/LeetCode560_SubarraySumEqualsK.java` | [LeetCode560-和为K的子数组.md](src/com/ittxf/leetcode/hot100/p560_subarraysumequalsk/LeetCode560-和为K的子数组.md) | 区间和换成"两个前缀和之差"，于是 `count += map.get(preSum - k)` 累加的是**次数**不是 1；`put(0, 1)` 是空前缀，删掉后 `[3,1,1]/3` 从 1 变 0、`[1,2,3]/3` 从 2 变 1、`[0,0,0]/0` 从 6 变 3，**20,000 组随机里 30.39% 会漏、69.61% 悄悄混过去**；把 `getOrDefault+1` 写成 `put(x,1)` 同理（4,235/20,000 组出错，但 `[1,1,1]/2` 仍然对）；**"能不能用滑窗"分四档实测**：严格全正数且 `k>=1` 时滑窗 50,000 组全对（固定右端点最多一个左端点），允许 0 就错 10,306/50,000，`k=0` 时把空区间数进去 20,000/20,000 全错，含负数则单调性彻底没了（题面 `[-1000,1000]` 正落在最后一档）；20,000 组与两重循环暴力 0 差异 |
| 哈希集合 | `p128_longestconsecutivesequence/LeetCode128_LongestConsecutiveSequence.java` | [LeetCode128-最长连续序列.md](src/com/ittxf/leetcode/hot100/p128_longestconsecutivesequence/LeetCode128-最长连续序列.md) | 一句 `if (!set.contains(num - 1))` 让每个连续段只展开一次，`O(L²)` → `O(L)`；20,000 组随机（含空数组、重复、跨零段）与排序扫描 **0 差异**，空数组不需要特判；**`num - 1` / `currentNum + 1` 是裸 int 加减**：`{MIN_VALUE, MAX_VALUE}` 实测返回 2（真值 1），因为两端互相"承认相邻"，题面 ±10⁹ 挡得住；`System.out.println(numSet)` 那行是巧合升序——换 `{100,3,7,250,1,99}` 就打成 `[1, 3, 99, 100, 7, 250]`；注释那份排序版 `O(n log n)`、要特判空数组、`else` 里有一行死代码，还会把调用方的数组就地排序 |
| 动态规划 | `KnapsackProblem.java` | [0-1背包动态规划.md](src/com/ittxf/study/dynamic/0-1背包动态规划.md) | 两个分支都回到 `i-1` 行才是"0-1"的来历；4,000 组随机实例实测 `dp` 值、反推方案总重量、总价值**三项全部 0 次失败**（含 481 组平局）；平局时 `>` 偏向"放"→ 报出的是下标较大的物品；重量 0 的物品实测把 1100 算成 1000；自顶向下写法 n=24 要 **84,064 次调用**，表格只需 1,275 格；一维滚动数组结果全同、省 2000 倍内存，**但没有 `path` 就反推不出方案** |
| 贪婪算法 | `GreedyAlgorithm.java` | [贪婪算法电台覆盖.md](src/com/ittxf/study/greedy/贪婪算法电台覆盖.md) | 穷举 32 个子集确认最少 4 个且**可行解只有两种**；平局用严格 `<` → **同一份数据只换 HashMap 的构造方式实测得到两种答案**，被支配的 K4 会顶掉 K1；`else break` 是必需的；3,000 组随机集合覆盖实测**贪心 8.30% 的实例比最优多花电台** |
| 字符串匹配 | `ViolenceMatch.java`、`KMPAlgorithm.java` | [暴力匹配与KMP.md](src/com/ittxf/study/kmp/暴力匹配与KMP.md) | **暴力那份的 `return i - j + 1` 是错的**：`main` 打印 3 而真实是 2，20 万组里 92,256 次命中**全部恰好 +1**，同一批数据上 `kmpSearch` 与 `indexOf` **全对**；比较次数实测：他自己那组几乎看不出差距（36 vs 32），换成 `A^n B` 就是暴力的 `\|pat\|²`（144）对线性（34），100 万字符实测 1,099 万 vs 200 万次；`kmpNext("")` 实测抛 AIOOBE |
| 最小生成树 | `PrimAlgorithm.java` | [Prim最小生成树.md](src/com/ittxf/study/prim/Prim最小生成树.md) | 7 个起点分别跑总权都是 25，与独立实现吻合；**`miniWeight` 每轮复位而 `h1/h2` 从不复位**，图不连通时实测复述上一轮的边并塞进 10000（总权 10004 而参考判定不连通）；权值 ≥ 10000 的真边永远看不见；O(V³)，实测 V=600 要判断约 2.15 亿格花 45ms |
| 最小生成树 | `KruskalCase.java` | [Kruskal最小生成树.md](src/com/ittxf/study/kruskal/Kruskal最小生成树.md) | 12 条边收 6 丢 6、总权 36 与独立实现一致；`ends[i]=0` 一身两用，靠"边恒为 `i<j`"才碰不到，写成 `<C,A>` 实测成环后数组越界；不连通时空槽打成字面量 `null`；`compareTo` 用减法实测会溢出（这份数据够不到） |
| 最短路径 | `DijkstraAlgorithm.java` | [Dijkstra最短路径.md](src/com/ittxf/study/dijkstra/Dijkstra最短路径.md) | 从 G 出发 7 轮实测逐轮复盘，**49 次 `len` 计算里只有 7 次真的改到 `distance`**；与参考实现**逐起点对比，7 个起点全部一致**，且和 Floyd 在同一张图上 49 对全等；**`65535` 既是无穷大也是天花板**，实测 `40000+40000` 的路径被判成"不可达"（参考给 80000）；加一条有向负边 `E→A=-10` 实测给 A=2，而 Bellman-Ford 是 -14，且**贪心参考实现跟着一起错**；`updateArr()` 的 `index` 不复位在不连通时退回起点白跑几轮（数字侥幸没错，但孤立点的 `alreadyArr` 永远是 0）；`show()` 里写死了 `{'A'..'G'}`，8 顶点的图**算对了却打印崩** |
| 最短路径 | `FloydAlgorithm.java` | [Floyd所有顶点最短路径.md](src/com/ittxf/study/floyd/Floyd所有顶点最短路径.md) | 实测 **343 次 `len` 计算（= 7³）、37 次更新**；`pre[i][j] = pre[k][j]` 的含义是"借 k→j 已算好的走法"，用它还原 **42 对路径实测全部合法且逐段加总等于 `distance`**；对角线必须填 0（换成 65535 后 A→A 变成 4，7 格变怪数）；**`this.distance = matrix` 不拷贝**，实测 `floyd()` 之后调用方那个数组从 `[0,5,7,N,N,N,2]` 变成 `[0,5,7,12,6,8,2]`（Prim/Kruskal 那两份都是逐格拷的）；INF 换成 `Integer.MAX_VALUE` 实测溢出成 -2，把两张不连通的块加成负数墙；V=800 时一次 Floyd 146ms、跑 800 次 Dijkstra 要 413ms |
| 回溯 | `HorseChessboard.java` | [马踏棋盘.md](src/com/ittxf/study/horse/马踏棋盘.md) | 输出的 8×8 棋盘**逐格验过：1..64 各一次、相邻编号全是合法马步**；实测那层 Warnsdorff 排序把 8×8 从"15 秒搜不完（1.2 亿次调用、只填到 49 格）"压到 **47,156 次调用 / 几十毫秒**，6×6 是 122 万 → 1,523（约 800 倍），5×5 直接 25 次调用零回溯；代价是每层递归要跑 25 次 `next()`（共 118 万次）；**起点从 (0,0) 挪到 (0,1)，调用量从 47,156 掉到 71（664 倍）**；`finished` 是 static 且从不复位 → 同一 JVM 第二次跑只用 64 次调用就打出一张"59 出现两次"的假棋盘；`next()` 用列去比 `X`、用行去比 `Y`，`visited` 又写成 `row * X + column` → **只有正方棋盘能掩盖这处行列互换**，实测 4×6/6×4/2×8/3×4/4×5 全部几步之内 AIOOBE；12×12 以上实测 20 秒搜不完 |

## 一条从这二十三个程序里读出来的共性

写漏一处、忘复位一个变量、两句顺序颠倒 —— 二十七处里**七处会当场抛异常**（汉诺塔爆栈、KMP 空模式串、Kruskal 判环失效后越界、
马踏棋盘非正方越界、Dijkstra 的 `show()` 遇到 8 个顶点、239 的 `k > n`、438 的一个大写字母），
**两处直接编译不过**（分组注释版的 `List.reversed()` 在 `--release 17` 下、两个 `Solution` 放进同一个包），
**两处变成死循环**（二分的 `mid±1` 少一格、三数之和去掉收尾的 `left++/right--`），
剩下十六处都是安静地给出一个格式完全正常的错答案 / 次优答案 / 被改坏的数据：

| 漏写/混用/顺序颠倒的那一处 | 触发输入 | 实测后果 |
| --- | --- | --- |
| 汉诺塔 `if (n == 1)` 没挡 `n <= 1` | `hanoiTower(0, ...)` | 一路递归到负数，钻约 19,988 层后 `StackOverflowError` |
| 二分 `while (left <= right)` 的 `=` | 答案落在单点窗口那一轮 | 10 个目标值丢 4 个，返回 -1，从不返回错下标 |
| 二分 `right = mid` / `left = mid` 少一格 | 查询一个不存在的值 | 死循环（实测 1000 步仍未收敛，能命中的用例一切正常） |
| 415 `while` 少了 `forward != 0` | 最高位产生进位 | `"99"+"1"` 得 `"00"`，不进位的用例完全正确 |
| 暴力匹配 `return i - j + 1` | 任何一次命中 | 92,256 次命中 92,256 次 +1，程序照常打印 |
| KMP `next[0] = 0` | 空模式串 | `ArrayIndexOutOfBoundsException`（崩得倒是干脆） |
| 贪心平局时用严格 `<` + `HashMap` 顺序 | 两个电台覆盖数相同 | 同一份数据两种容器构造 → 两种答案 |
| Prim `h1/h2` 不复位 | 某一轮找不到跨堆边（不连通、有向矩阵、边权 ≥ 10000） | 打印上一轮的边 + 权值 10000，总权值凭空多 10000 |
| Kruskal `ends[i]=0` 当哨兵 | 边的第二个端点是下标 0（本程序生成不出） | 合并变空操作 → 成环 → `res` 数组越界 |
| Dijkstra `65535` 当无穷大 | 路径总长 ≥ 65535 | 实测 `40000+40000` 的点被打成 "N"（不可达） |
| Dijkstra `show()` 写死顶点表 | 8 个顶点的图 | 算对了，打印时 `Index 7 out of bounds for length 7` |
| Floyd `this.distance = matrix` | 任何一次 `floyd()` | 调用方的邻接矩阵被就地改成距离表，原图丢失 |
| Floyd 矩阵只填了一半 | B 的三条边 | 从 B 出发的 4 对距离与对称图不一致，其余 42 对全对 |
| 马踏棋盘 `finished` 是 static | 同一 JVM 跑第二次 | 64 次调用"秒出"一张编号重复的假棋盘 |
| 马踏棋盘 `next()` 行列用反 + `visited` 用 `row * X` | 任何 X ≠ Y 的棋盘 | 几步之内 `ArrayIndexOutOfBoundsException` |
| 两数之和 `put` 写在 `containsKey` 之前 | `[3,3]` target 6（任何 `2·x == target`） | 返回 `[0,0]`：同一个元素用了两次；**题面示例测不出来** |
| 分组拿 `char[]` 当 key | 任何输入 | 6 个词分成 6 组（数组的 `hashCode`/`equals` 按引用，谁也不等于谁） |
| 分组注释版用 `List.reversed()` | 放开注释 + `--release 17` | `错误: 找不到符号 方法 reversed()`（JDK 21 才有） |
| 两个 `class Solution` 放进同一个包 | 第二道题写进同一层目录 | `错误: 重复: pkgdemo.Solution`，编译直接顶掉 |
| 最长子串 `left` 去掉 `Math.max` | 重复字符在窗口左侧外出现过（`abba`） | 50,000 组随机错 27,081 次；顺带把"指针只前进"这条 O(n) 前提也废了 |
| 水桶 `if` 反过来（动较高的一边） | `[1, 1, 0]` | 得 0，真值 1；20,000 组随机错 12,980 组 |
| 三数之和去掉命中后的 `left++; right--` | 任何一次命中（且当前值不重复） | **死循环**：指针原地不动，`sum` 永远是 0 |
| 239 只挡 `n == 0 \|\| k == 0` | `k > n`（`new int[n - k + 1]`） | `NegativeArraySizeException: -1`，信息里只有一个 `-1` |
| 438 用 `int[26]` 当哈希表 | 输入含大写（`'A' - 'a' = -32`） | `ArrayIndexOutOfBoundsException`，题面保证小写才不触发 |
| 560 删掉 `preSumMap.put(0, 1)` | 答案里有从下标 0 开始的段 | 20,000 组随机漏 30.39%，其余 69.61% 悄悄对得上 |
| 560 顺手改用滑动窗口 | 数组含 0（`k>=1`）或 `k = 0`，或含负数 | 全正数且 `k>=1` 时 50,000 组全对；含 0 错 10,306；`k=0` 时把空区间数进去，20,000 组全错 |
| 128 的 `num - 1` / `currentNum + 1` | `{Integer.MIN_VALUE, Integer.MAX_VALUE}` | 返回 2（真值 1）：两端互相"承认相邻"，起点判断被绕开 |

结论很一致：**"跑一个正常用例"能通过的密度太低**。上面每个坑都是靠专门去撞边界（不存在的值、单点窗口、空串、0 个盘、平局、孤立的顶点、恰好等于无穷大的权值、重量 0 的物品、非正方棋盘、**恰好带重复元素的数组**、含 0 的数组、`k` 比数组还长、一个大写字母、`int` 两端、同一个 JVM 跑第二遍）才暴露出来的。

顺带一条这批题解共同暴露的习惯：**方法里的调试打印和就地改入参**（`p49_3sum` 排完序打一行、`p128` 打 `numSet`、Floyd 直接接管矩阵、三份图程序里只有两份是逐格拷贝的），本地看很直观，交上去要么多输出一行、要么把调用方的数据改坏。

## 目录结构

```text
src/com/ittxf/
├── study/
│   ├── dac/                         HanoiTower.java                + 汉诺塔.md
│   ├── binarysearchnorecursion/     BinarySearchNoRecursion.java   + 二分查找非递归.md
│   ├── dynamic/                     KnapsackProblem.java           + 0-1背包动态规划.md
│   ├── greedy/                      GreedyAlgorithm.java           + 贪婪算法电台覆盖.md
│   ├── kmp/                         ViolenceMatch.java          ┐
│                                    KMPAlgorithm.java           ┴ 暴力匹配与KMP.md
│   ├── prim/                        PrimAlgorithm.java           + Prim最小生成树.md
│   ├── kruskal/                     KruskalCase.java              + Kruskal最小生成树.md
│   ├── dijkstra/                    DijkstraAlgorithm.java        + Dijkstra最短路径.md
│   ├── floyd/                       FloydAlgorithm.java           + Floyd所有顶点最短路径.md
│   └── horse/                       HorseChessboard.java          + 马踏棋盘.md
└── leetcode/
    ├── LeetCode415_AddStrings.java                                 + LeetCode415-字符串相加.md
    └── hot100/                                                     一题一包，每包里 .java + 同名 .md
        ├── p1_twosum/                   LeetCode1_TwoSum.java        + LeetCode1-两数之和.md
        ├── p3_longestsubstringwithoutrepeatingcharacters/
        │                                 LeetCode3_...Characters.java + LeetCode3-无重复字符的最长子串.md
        ├── p11_containerwithmostwater/  LeetCode11_ContainerWithMostWater.java + LeetCode11-盛最多水的容器.md
        ├── p42_trappingtainwater/       LeetCode42_TrappingRainWater.java + LeetCode42-接雨水.md
        ├── p49_3sum/                    LeetCode49_3Sum.java         + LeetCode15-三数之和.md
        ├── p49_groupanagrams/           LeetCode49_GroupAnagrams.java + LeetCode49-字母异位词分组.md
        ├── p128_longestconsecutivesequence/
        │                                 LeetCode128_...Sequence.java + LeetCode128-最长连续序列.md
        ├── p239_slidingwindowmaximum/   LeetCode239_SlidingWindowMaximum.java + LeetCode239-滑动窗口最大值.md
        ├── p283_movezeroes/             LeetCode283_MoveZeroes.java  + LeetCode283-移动零.md
        ├── p438_findallanagramsinastring/
        │                                 LeetCode438_...String.java   + LeetCode438-找到字符串中所有字母异位词.md
        └── p560_subarraysumequalsk/     LeetCode560_SubarraySumEqualsK.java + LeetCode560-和为K的子数组.md
```

`p42_trappingtainwater` 这个目录名少了个 `r`（`trapping` 写成 `trappingt`），类名 `LeetCode42_TrappingRainWater` 是对的；
`p49_3sum` 里装的是第 15 题（三数之和），和旁边真正的 49 题撞了号 —— 两处都只是命名，不影响编译和运行。

`prim` / `kruskal` / `dijkstra` / `floyd` 四个包各自在同一文件里带了辅助类（`MiniSpanTree`+`Graph`、`EData`、`Graph`+`VisitedVertex`、`Graph`），所以是"一个文件一个知识点"。
`dijkstra` 和 `floyd` 两个包里都有叫 `Graph` 的类；`hot100/` 下 10 个包里的辅助类**全都叫 `Solution`**（LeetCode 网页模板就这么给的），**全靠包名区分 —— 把它们挪进同一个包会立刻 `错误: 重复`**（实测）。
`out/`（IDEA 编译输出）、`.idea/`、`*.iml` 不入库。

## 怎么跑

二十三个 `.java` 都带 `main`，跑起来不需要任何输入，直接打印结果。IDEA 里点绿色三角即可。命令行（本机 JDK 21.0.12.1，二十三个文件一次编译全部通过）：

```powershell
# 单个程序
javac -encoding UTF-8 -d out src\com\ittxf\leetcode\hot100\p1_twosum\LeetCode1_TwoSum.java
java -cp out com.ittxf.leetcode.hot100.p1_twosum.LeetCode1_TwoSum

# 整个 src 一次编译
javac -encoding UTF-8 -d out (Get-ChildItem -Recurse src -Filter *.java).FullName
```

Git Bash / WSL 下中文输出要加一个参数，否则控制台会按 GBK 出字节（实测同一个程序：不加参数输出 `b5da 31b8 f6...`，加了输出 `e7ac ac31 e4b8 aa...` 才是 UTF-8 的"第1个盘"）：

```bash
java -Dstdout.encoding=UTF-8 -cp out com.ittxf.leetcode.hot100.p49_groupanagrams.LeetCode49_GroupAnagrams
```

各程序的预期输出（都是实测）：

```
com.ittxf.study.dac.HanoiTower                     31 行 "第k个盘从X->Y"（main 里写的是 n=5）
com.ittxf.study.binarysearchnorecursion…           目标值 7 的索引为 6
com.ittxf.study.dynamic.KnapsackProblem            3 行 dp 表 + 4 行 path 表 + 放入物品 3、1 + 最大价值 3500
com.ittxf.study.greedy.GreedyAlgorithm             得到的选择结果是：[K1, K2, K3, K5]
com.ittxf.study.kmp.ViolenceMatch                  index = 3            ← 正确答案是 2，见笔记第一节
com.ittxf.study.kmp.KMPAlgorithm                   [0, 0, 0, 0, 1, 2, 0] / index = 15
com.ittxf.study.prim.PrimAlgorithm                 7×7 邻接矩阵 + 6 条边 + 总权值 25
com.ittxf.study.kruskal.KruskalCase                邻接矩阵 + 12 条边排序 + ends 数组 + 6 条树边
com.ittxf.study.dijkstra.DijkstraAlgorithm         7 行矩阵 + 三个数组 + A(2)…G(0)，从 G 出发
com.ittxf.study.floyd.FloydAlgorithm               7 组两行（前驱名字 + 距离），G 行 = 2 3 9 10 4 6 0
com.ittxf.study.horse.HorseChessboard              耗时 ~80 ms + 一张填满 1..64 的 8×8 棋盘
com.ittxf.leetcode.LeetCode415_AddStrings          533
com.ittxf.leetcode.hot100.p1_twosum…               [0, 1]
com.ittxf.leetcode.hot100.p3_…                     4
com.ittxf.leetcode.hot100.p11_…                    49
com.ittxf.leetcode.hot100.p42_…                    6
com.ittxf.leetcode.hot100.p49_3sum…                [-4, -1, -1, 0, 1, 2] 然后 [[-1, -1, 2], [-1, 0, 1]]
com.ittxf.leetcode.hot100.p49_groupanagrams…       [[eat, tea, ate], [bat], [tan, nat]]
com.ittxf.leetcode.hot100.p128_…                   [0, 1, 2, 3, 4, 5, 6, 7, 8] 然后 9
com.ittxf.leetcode.hot100.p239_…                   [3, 3, 5, 5, 6, 7]
com.ittxf.leetcode.hot100.p283_…                   [1, 3, 12, 0, 0]
com.ittxf.leetcode.hot100.p438_…                   150 个起点：[4, 30, 56, 82, 108, ...]（间隔 26）
com.ittxf.leetcode.hot100.p560_…                   2
```

几处要注意的：

- `ViolenceMatch` 打印的 `index = 3` 是**错的**（`indexOf` 给 2），`return` 语句本身多写了 `+1`；同一份写法在 `KMPAlgorithm` 里是对的，见那篇笔记第一、二节。
- `HanoiTower` 的 `main` 里 n 写死为 5。改成 25 会打印 33,554,431 行（实测约每秒 290 万行，要 11 秒），想试规模请先把它接到命令行参数上。
- `HorseChessboard` 只能跑正方棋盘：`X` 和 `Y` 改成不等（哪怕 4×6）会在几步之内 `ArrayIndexOutOfBoundsException`，原因见那篇笔记第六、七节；`main` 里 `row = 1; col = 1;` 传参时 `-1`，实际从 `(0,0)` 角上出发 —— 这是实测最难的起点（47,156 次调用），改成 `row = 1; col = 2`（即 `(0,1)`）实测只要 71 次。
- `LeetCode49` 那行输出里组的先后（`eat, bat, tan`）**既不是插入顺序也不是字典序**，是 `HashMap` 的桶顺序；换 `LinkedHashMap`/`TreeMap` 会得到另外两种顺序而内容不变，见那篇笔记第三节。
- `LeetCode1_TwoSum` 的 `[0, 1]` 是对的，但这份代码的正确性挂在"`containsKey` 写在 `put` 前面"这个顺序上 —— 调换之后 `[3,3]/6` 会返回 `[0,0]`，而那组示例恰好测不出来（见那篇笔记第二节）。
- 四个图程序的矩阵**不是同一张**，无穷大常量也各不相同（10000 / `Integer.MAX_VALUE` / 65535），对照前面那张表再看输出。
- 题解的辅助类都叫 `Solution`、图的辅助类都叫 `Graph`，靠"一题一包 / 一章一包"避开重名，**别把它们并到同一个包里**。
- 二十三个 `main` 都只演示一组数据。笔记里的边界结论 —— 不存在的值、单点窗口、空串、0 个盘、平局、孤立的顶点、`≥ 65535` 的路径长度、重量 0 的物品、非正方棋盘、**恰好带重复元素的数组**、同一个 JVM 跑第二遍 —— 都得自己换入参才能复现，各篇笔记里的实测表就是照着这个目的写的。
