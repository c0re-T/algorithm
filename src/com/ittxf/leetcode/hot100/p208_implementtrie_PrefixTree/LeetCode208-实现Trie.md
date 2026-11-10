# LeetCode 208 实现 Trie（前缀树：26 个槽 + 一个结尾标记）

> 对应程序：[LeetCode208_ImplementTrie_PrefixTree.java](./LeetCode208_ImplementTrie_PrefixTree.java)
> `main` 的序列与实测输出：
>
> ```
> true false true
> ```

---

## 一、`insert` / `search` / `startsWith` 共用一个 `commonPrefix`

```java
private TrieNode commonPrefix(String prefix) {
    TrieNode node = root;
    for (char c : prefix.toCharArray()) {
        if (node.children[c - 'a'] == null) return null;
        node = node.children[c - 'a'];
    }
    return node;
}
```

三个 API 的差别只在拿到节点之后怎么判：

| API | 判定 |
| --- | --- |
| `insert` | 路径不存在就建，走完把 `node.isEnd = true` |
| `search` | `node != null && node.isEnd` |
| `startsWith` | `node != null`（不管 `isEnd`） |

抽成一个 `commonPrefix` 让"走路径"这件事只写一遍 —— 这是这份实现比"三个方法各抄一遍循环"好的地方。

实测语义（先只插 `"app"`）：

```
search("app")=true   search("ap")=false   startsWith("ap")=true   startsWith("appl")=false
```

再插 `"apple"`：`search("app")` 仍为 `true`（`isEnd` 早就在），`search("apple")=true`，但 `search("appl")` 仍是 `false` —— "appl" 只是路径上的一段，不是谁的结尾。

## 二、`main` 里有一行结果被丢掉了

```java
obj.startsWith("app");      // 返回 True
```

没有 `println`、没有赋值，返回值直接丢弃。注释写着"返回 True"，但 `main` 从头到尾没显示过它。实测这一行确实是 `true`，问题只是**看不到**。README 里"main 只演示 happy path"那条说的就是这一类：注释里的断言没有被程序输出验证过。

## 三、只认小写字母，其它字符当场越界

`children[c - 'a']` 是唯一的守门人，而 `children` 长度是 26：

| 输入 | 结果 |
| --- | --- |
| `insert("Apple")` | `ArrayIndexOutOfBoundsException: Index -32 out of bounds for length 26` |
| `insert("app1e")` | `ArrayIndexOutOfBoundsException: Index -48 out of bounds for length 26` |
| `insert("a b")` | `ArrayIndexOutOfBoundsException: Index -65 out of bounds for length 26` |
| `insert("中文")` | `ArrayIndexOutOfBoundsException: Index 19916 out of bounds for length 26` |
| `search("AP")` | `ArrayIndexOutOfBoundsException: Index -32 out of bounds for length 26` |
| `insert(null)` / `search(null)` / `startsWith(null)` | `NullPointerException: Cannot invoke "String.toCharArray()" because "<parameter1>" is null` |

题面保证只有小写英文字母。要支持更多字符就得把 `TrieNode[]` 换成 `HashMap<Character, TrieNode>`（省内存、但每次查有哈希开销），或者先做一次字符校验。

## 四、空串的语义

| 操作 | 结果 |
| --- | --- |
| 空树上 `search("")` | `false` |
| 空树上 `startsWith("")` | `true` |
| `insert("")` 之后 `search("")` | `true` |

`insert("")` 一个循环都不进，直接 `root.isEnd = true` —— 于是"空前缀"变成一个被插入过的单词。`startsWith("")` 恒为 `true`，因为 `commonPrefix("")` 原样返回 `root`（不是 `null`）。这一对行为在空串上不自洽（空树 `search("")` 是 false、`startsWith("")` 是 true），但题面 `1 ≤ word.length()`，碰不到。

## 五、20,000 组随机对拍

每轮新建一个 `Trie`，随机做 1..8 次 `insert`（长度 0..4、字母 `a..d`），每次插入后立刻查一个随机前缀，与 `HashSet<String>` + 前缀集合比对：

```
search 差异 = 0
startsWith 差异 = 0
```

`HashSet` 判"完整单词"、把所有前缀塞进另一个集合判"前缀存在" —— 和树的走法完全无关，0 差异说明 `isEnd` 与 `commonPrefix` 的配合没错。

## 六、节点数与内存：共享前缀省的是节点，不是槽

先看小样本 —— 插 8 个词、共 42 个字符：

```
节点数 = 27（含 root）—— 比字符总数少 15 个
```

`app` / `apple` / `application` / `ape` 共享了 `ap` 那一截，所以节点数少于字符数。

再看随机数据（20 万个长 6..10 的词，共 160 万字符）：**节点数 977,455**，约为字符总数的 61.1%。共享率取决于前缀重合度，随机词自然只有六成。

内存实测（30 万个随机词、1,432,515 个节点，`System.gc()` 前后对比堆占用）：

```
增量 196 MB，每节点约 143~144 字节
```

144 字节 = `TrieNode` 对象（对象头 + `TrieNode[]` 引用 + `boolean`，对齐到 24 字节）+ `TrieNode[26]` 数组（16 字节头 + 26×4 引用 = 120 字节）。**每个节点都固定开 26 个槽，哪怕只用到 1 个** —— 这是空间换时间最直接的地方。改成 `HashMap<Character, ...>` 能把空槽省掉，代价是每次跳转多一次哈希。

## 七、插入 / 查找耗时

| 词数 | insert | search（全部命中） |
| --- | --- | --- |
| 100,000 | 22 ms | 15 ms |
| 500,000 | 178 ms | 78 ms |
| 1,000,000 | 360 ms | 128 ms |

单次操作是 `O(词长)`，和词数无关，所以总耗时随词数线性增长（长度 5..10）。

## 八、对象是累积的，不能当一次性容器用

```
insert("a")   之后 search("a")=true
再 insert("ab")：search("a")=true, search("ab")=true
```

`Trie` 没有 `clear`（第 208 题也不需要），同一个对象上的插入只会越积越多。想清空只能 `new` 一个 —— 这不是 bug，但和 `Solution` 那种"纯函数"习惯不同，跨用例复用要小心。

## 九、一句话记住

```
   insert 建路径 + 末尾 isEnd，search 要 node != null && node.isEnd，startsWith 只要 node != null；
   三者共用 commonPrefix —— 走路径这件事只写一遍；
   main 里 startsWith("app") 那行**没打印**，注释说"返回 True"但看不到；
   children[c - 'a'] 只认小写：'A' → Index -32、'1' → -48、空格 → -65、中文 → 19916；
   insert("") 会把 root.isEnd 置 true（search("") 从 false 变 true），startsWith("") 恒 true；
   20,000 组与 HashSet + 前缀集合 0 差异；
   8 个词 42 字符 → 27 个节点；20 万随机词 160 万字符 → 977,455 个节点（61.1%）；
   每节点固定 26 个槽：实测 144 字节/节点，144 万节点 196 MB；
   插入/查找是 O(词长)：100 万词 insert 360 ms、search 128 ms；
   对象是累积的，没有 clear，跨用例要 new。
```