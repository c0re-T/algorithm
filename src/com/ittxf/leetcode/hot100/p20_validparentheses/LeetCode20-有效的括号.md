# LeetCode 20 有效的括号（拿输入数组自己当栈）

> 对应程序：[LeetCode20_ValidParentheses.java](./LeetCode20_ValidParentheses.java)
> `main` 传的是 `"([])"`，实测：
>
> ```
> true
> ```

---

## 一、左括号写入"对应的右括号"，右括号来比对

```java
char[] ch = s.toCharArray();
int top = 0;
for (char c : ch) {
    switch (c) {
        case '(': ch[top++] = ')'; break;
        case '[': ch[top++] = ']'; break;
        case '{': ch[top++] = '}'; break;
        default:
            if (top == 0 || ch[--top] != c) return false;
    }
}
return top == 0;
```

关键是**不用另开栈**：`ch` 本身当栈，`top` 是栈顶。左括号进来时把"期待的右括号"写进 `ch[top]`；右括号进来时先 `--top` 再比对。写的位置 `top` 永远不超过当前读到的下标 `i`（每步最多压一个），所以**只覆盖已经读过或正在读的字符**，不会干扰后面的遍历。

## 二、对拍验证"就地改数组"没出问题

用真正的 `Deque<Character>` + `Map` 写参考实现，穷举字母表 `()[]{}` 长度 0..7 的所有字符串：

```
穷举 335,923 组：0 差异
随机 200,000 组（长度 1..40）：0 差异
```

如果就地写数组会踩到未读字符，这两组对拍一定会炸出来 —— 没有。

## 三、奇数长度剪枝只是提前返回

```java
if (s.length() % 2 != 0) return false;
```

去掉这行再对拍：335,923 组 **0 差异**。奇数长度不可能完全匹配，删掉只是多走一遍循环，不影响正确性。

## 四、非括号字符会被当成右括号

`switch` 的 `default` 分支不区分右括号和其他字符：

| 输入 | 结果 | 说明 |
| --- | --- | --- |
| `""` | true | 空串合法 |
| `"a"` | false | 长度 1 先被奇数剪枝挡掉 |
| `"ab"` | false | `'a'` 走 default，`top == 0` |
| `"(a)"` | false | 长度 3，奇数剪枝 |
| `"([)]"` | false | 交叉嵌套 |
| `"([])"` | true | 正常嵌套 |
| `null` | `NullPointerException: Cannot invoke "String.length()" because "<parameter1>" is null` | |

## 五、深嵌套不吃栈

数组栈 + 循环，`"(" × 200,000 + ")" × 200,000` 实测 **true**，没有递归、不会 `StackOverflowError`。

## 六、一句话记住

```
   拿 char[] 自己当栈：左括号写入对应右括号，右括号来时 --top 比对；
   写的位置 top ≤ 当前下标 i，只覆盖读过的字符；
   穷举 335,923 组（长度 ≤7）+ 随机 200,000 组与 Deque 版 0 差异；
   奇数长度剪枝去掉也 0 差异，只是省一遍循环；
   非括号字符走 default 当右括号；空串 true，null NPE；
   20 万个左括号深嵌套直接 true，数组栈不怕深。
```
