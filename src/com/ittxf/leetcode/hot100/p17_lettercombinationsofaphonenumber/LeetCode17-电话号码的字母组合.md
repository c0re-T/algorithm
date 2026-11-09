# LeetCode 17 电话号码的字母组合（回溯：一层对应一个数字）

> 对应程序：[LeetCode17_LetterCombinationsOfAPhoneNumber.java](./LeetCode17_LetterCombinationsOfAPhoneNumber.java)
> `main` 传的是 `"23"`，实测：
>
> ```
> [ad, ae, af, bd, be, bf, cd, ce, cf]
> ```

---

## 一、递归深度 = 数字个数，每层走一遍这个数字的字母

```java
for (char c : mapping[digits.charAt(index) - '0'].toCharArray()) {
    backtrack(res, digits, index + 1, sb.append(c), mapping);
    sb.deleteCharAt(sb.length() - 1);   // 撤销选择
}
```

`index` 是"已经吃掉几个数字"，`sb` 是已拼出来的前缀。触底条件是 `index == digits.length()`。数字个数就是递归深度，而结果条数是**各数字字母数之积**：`"23"` → 3×3 = 9 条，`"234"` 实测 27 条，`"79"` 实测 16 条（7 和 9 各有 4 个字母）。

同一个 `StringBuilder` 传下去、返回时 `deleteCharAt` 撤回，是这份代码唯一需要小心的地方。把那一行删掉实测：

```
"2"  原版=[a, b, c]        忘记撤销=[a, ab, abc]
"23" 原版=[ad, ae, af, ...] 忘记撤销=[ad, ade, adef, adefbd, ...]
```

`s` 一直变长、从不缩短，拼出来的串越来越长。20,000 组随机输入里 **16,700 组非空输入全部出错**，只有空串那 3,300 组侥幸对上。而 `main` 那组"看起来还挺像回事"。

换成"每层 `new StringBuilder(sb).append(c)`"（靠值传递代替撤销）实测与原地撤销 **0 差异、耗时也一样**（长度 8 都是 1 ms）。两种写法都行，这份选了省内存的那种。

## 二、`mapping` 的前两格是 `"0"` 和 `"1"`

```java
String[] mapping = {"0", "1", "abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz"};
```

题面保证输入只含 `2-9`，所以这两格本来用不上。但它们是**字面量的 "0" 和 "1"**，于是：

| 输入 | 结果 |
| --- | --- |
| `"0"` | `[0]` |
| `"1"` | `[1]` |
| `"01"` | `[01]` |
| `"10"` | `[10]` |
| `"20"` | `[a0, b0, c0]` |

不报错，把数字原样当字母拼进了结果。如果填 `""`（空串），这些输入会返回空列表，语义更接近"没有对应字母"；填 `null` 则会 NPE。三种选择都说得通，但**当前这种是唯一会悄悄产出 "0"/"1" 字符的**。

## 三、边界与异常

| 输入 | 结果 |
| --- | --- |
| `""` | `[]`（提前 `return res`） |
| `"2"` / `"79"` / `"234"` | `[a, b, c]` / 16 条 / 27 条 |
| `null` | `NullPointerException: Cannot invoke "String.isEmpty()" because "<parameter1>" is null` |
| `"*"` | `ArrayIndexOutOfBoundsException: Index -6 out of bounds for length 10` |
| `" 2"`（带空格） | `ArrayIndexOutOfBoundsException: Index -16 out of bounds for length 10` |
| `"2a"` | `ArrayIndexOutOfBoundsException: Index 49 out of bounds for length 10` |

`digits.charAt(index) - '0'` 是唯一的守门人：小于 `'0'` 的字符算出负数、大于 `'9'` 的算出 ≥10，都直接撞数组边界。**崩得干脆**，比静默出错好。题面保证只有 2-9，所以线上不触发。

## 四、`res` 是局部变量，所以这个 `Solution` 可以反复用

```
第1次 "23" = 9 条
第2次 "2"  = [a, b, c]
第3次 "23" = 9 条
```

对照：把结果集改成共享字段且不清空，同样三次调用给出 **9 → 12 → 21** 条，第三次把前两次的结果全带上了（`others/permute` 那篇就是这个毛病）。

## 五、20,000 组随机对拍

数字串长度 0..5、字符取自 2-9，参考实现用**迭代笛卡尔积**（一层层把前缀列表乘上新字母），和递归完全不同的写法：

```
原版 vs 迭代笛卡尔积: 差异 = 0
条数 != 各字母数之积（非空串）: 0
输出不是字典序: 0
```

条数那一项只有 3,300 组"不符" —— 全是空串（`count("")` 按空积算出来是 1，实际返回 0 条），非空输入 0 例外。输出恰好是字典序，因为 `mapping` 里的字母本身有序、循环从左往右。

## 六、规模：输出条数先撞死，栈从来不是瓶颈

递归深度只有 `digits.length()`，但条数是乘积：

| 输入 | 条数 | 耗时 |
| --- | --- | --- |
| 8 个 `'7'` | 65,536（4⁸） | 1 ms |
| 10 个 `'2'` | 59,049（3¹⁰） | 1 ms |
| 12 个 `'2'` | 531,441（3¹²） | 11 ms |
| 14 个 `'2'` | 4,782,969（3¹⁴） | 190 ms |
| **16 个 `'2'`** | **43,046,721（3¹⁶）** | **`OutOfMemoryError: Java heap space`**（`-Xmx2g`） |

深度 16 层，栈毫无压力；死的是堆。所以这份的规模上限由**结果集大小**决定，和递归深度无关。题面数字串长度 ≤ 4（最多 81 条），离这条线还很远。

## 七、一句话记住

```
   index 数的是"吃掉几个数字"，深度 = digits.length()，条数 = 各数字字母数之积；
   sb 必须在返回时 deleteCharAt 撤销：删掉后 "2" 变成 [a, ab, abc]，16,700/20,000 非空输入全错；
   每层 new 一个 StringBuilder 代替撤销，实测 0 差异、耗时相同；
   mapping[0]="0"、mapping[1]="1"，所以 "0"→[0]、"1"→[1]、"01"→[01]，不报错；
   非数字字符靠 c - '0' 撞数组边界：'*' → Index -6、'2a' → Index 49；
   res 是局部变量，同一实例三次调用 9 → [a,b,c] → 9；共享字段版会 9 → 12 → 21；
   20,000 组与迭代笛卡尔积 0 差异，条数/字典序各 0 例外；
   深度只有 16 层，3¹⁶ = 4,304 万条时就 OOM —— 瓶颈是输出条数，不是栈。
```