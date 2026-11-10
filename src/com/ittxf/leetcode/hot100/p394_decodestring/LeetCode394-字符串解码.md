# LeetCode 394 字符串解码（双栈：倍数 + 已拼字符串）

> 对应程序：[LeetCode394_DecodeString.java](./LeetCode394_DecodeString.java)
> `main` 跑四组示例，实测：
>
> ```
> aaabcbc
> accaccacc
> abcabccdcdcdef
> abccdcdcdxyz
> ```

---

## 一、数字累加、`[` 压栈、`]` 弹栈拼接

```java
if (Character.isDigit(c)) {
    multi = multi * 10 + (c - '0');       // 处理 10、100 这样的多位数
} else if (c == '[') {
    numStack.push(multi);
    strStack.push(currentStr);
    multi = 0;
    currentStr = new StringBuilder();
} else if (c == ']') {
    int curMulti = numStack.pop();
    StringBuilder prevStr = strStack.pop();
    prevStr.append(currentStr.toString().repeat(curMulti));
    currentStr = prevStr;
} else currentStr.append(c);
```

遇到 `[` 时把"外面的倍数"和"外面已经拼好的字符串"一起压栈，然后重新开始攒括号里的内容；遇到 `]` 时把括号里的内容重复 `curMulti` 次、接回外层。`repeat` 不用自己写循环。嵌套靠两个栈天然支持。

## 二、对拍

按文法随机生成 20,000 个合法串（最多 4 层嵌套、倍数 1..12、字母小段），与递归下降解析器对比：

```
20,000 组：0 差异
```

`"3[a2[c]]"` → `accaccacc`、`"2[abc]3[cd]ef"` → `abcabccdcdcdef`、`"10[a]"` → 10 个 a、`"100[ab]"` → 200 字符，全部正确。

## 三、非法输入：要么静默丢内容，要么抛异常

| 输入 | 结果 | 原因 |
| --- | --- | --- |
| `"[abc]"`（没有数字） | `""` | `multi` 是 0，`repeat(0)` 把内容删光 |
| `"3[a"`（缺 `]`） | `"a"` | 循环结束直接返回 `currentStr`，倍数和 `[` 被丢掉 |
| `"2[ab3[c"` | `"c"` | 连已经拼好的 `"ab"` 一起丢，只剩最内层 |
| `"a]"`（孤立右括号） | `NoSuchElementException` | `numStack.pop()` 空栈 |
| `null` | `NullPointerException`（`toCharArray`） | |

题面保证输入合法，所以这些只在题面之外。

## 四、全角数字会被 `Character.isDigit` 放进来

`'２'`（U+FF12）也满足 `isDigit`，但 `'２' - '0'` 不是 2：

```
"２[a]" -> 65,250 个 a
```

`'２'` 的码点是 65298，减去 `'0'`（48）得到 **65250**，于是 `repeat(65250)` 静默生成 6 万多字符。题面只给 ASCII 数字，不会触发。

## 五、规模：输出多长就要多少内存

| 输入 | 输出长度 |
| --- | --- |
| `"1000000[a]"` | 1,000,000 |
| `"100000000[a]"` | 100,000,000（3 GB 堆下能跑） |
| `"2[" × 50 + "a" + "]" × 50` | 要求 2⁵⁰ 长度 → `OutOfMemoryError: Required length exceeds implementation limit` |

瓶颈是输出长度本身，不是栈深度；两层 `StringBuilder` 栈最多也就嵌套层数那么深。

## 六、一句话记住

```
   数字用 multi = multi*10 + digit 攒；'[' 把倍数和外层字符串压栈；']' 弹出并 repeat 后接回去；
   文法随机 20,000 组与递归下降参考 0 差异，多位数 10[a] / 100[ab] 都对；
   非法输入会静默丢内容："[abc]" -> ""（repeat(0)）、"3[a" -> "a"、"2[ab3[c" -> "c"；孤立 ] 抛 NoSuchElementException；
   全角 '２' 被 isDigit 放进来，'２'-'0' = 65250 -> 输出 6.5 万个 a；
   输出多长就要多少内存：1e8 个 a 能跑，50 层 2[...] 直接 OOM。
```
