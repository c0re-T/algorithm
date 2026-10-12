package com.ittxf.study.greedy;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class GreedyAlgorithm {
    public static void main(String[] args) {
        // 创建广播电台，放入map中
        Map<String, HashSet<String>> broadcasts = new HashMap<String, HashSet<String>>();
        // 各个广播电台覆盖的地区放入对应的set
        HashSet<String> hashSet1 = new HashSet<>();
        hashSet1.add("北京");
        hashSet1.add("上海");
        hashSet1.add("天津");

        HashSet<String> hashSet2 = new HashSet<>();
        hashSet2.add("广州");
        hashSet2.add("北京");
        hashSet2.add("深圳");

        HashSet<String> hashSet3 = new HashSet<>();
        hashSet3.add("成都");
        hashSet3.add("上海");
        hashSet3.add("杭州");

        HashSet<String> hashSet4 = new HashSet<>();
        hashSet4.add("上海");
        hashSet4.add("天津");

        HashSet<String> hashSet5 = new HashSet<>();
        hashSet5.add("杭州");
        hashSet5.add("大连");

        // 将各个广播电台放入到map中
        broadcasts.put("K1", hashSet1);
        broadcasts.put("K2", hashSet2);
        broadcasts.put("K3", hashSet3);
        broadcasts.put("K4", hashSet4);
        broadcasts.put("K5", hashSet5);
        System.out.println("广播电台的集合：" + broadcasts);

        // 存放所有地区
        HashSet<String> allAreas = new HashSet<>();
        allAreas.addAll(hashSet1);
        allAreas.addAll(hashSet2);
        allAreas.addAll(hashSet3);
        allAreas.addAll(hashSet4);
        allAreas.addAll(hashSet5);
        System.out.println("所有地区：" + allAreas);

        // 存放选择的广播电台集合
        HashSet<String> selects = new HashSet<>();

        // 定义一个临时的集合，在遍历的过程中，存放遍历过程中的电台覆盖的地区和当前还没有覆盖的地区的交集
        HashSet<String> tempSet = new HashSet<>();

        // 定义给maxKey，保存在一次遍历过程中，能够覆盖最大未覆盖的地区对应的电台的key
        // 如果maxKey 不为null，则会加入到 selects
        int maxCount;
        String maxKey;
        while (allAreas.size() != 0) { // 如果allAreas 不为0，则表示还没有覆盖到所有的地区
            maxCount = 0;
            maxKey = null;
            // 遍历 broadcasts，取出每一个广播电台覆盖的地区和tempSet取交集
            for (String key : broadcasts.keySet()) { // 取出所有key的set集合
                tempSet.clear(); // 清空缓存
                tempSet.addAll(broadcasts.get(key)); // 将当前广播电台覆盖的地区加入到tempSet中
                tempSet.retainAll(allAreas); // 求出tempSet和allAreas交集，结果存放在tempSet中
                // 如果tempSet的大小大于0，说明当前广播电台有未覆盖的地区，将maxCount和tempSet.size()比较，取最大值
                if (tempSet.size() > 0 && (maxCount < tempSet.size())) {
                    maxCount = tempSet.size();
                    maxKey = key;
                }
            }
            if (maxKey != null) { // 如果maxKey 不为null，则会加入到 selects
                selects.add(maxKey);
                // 将maxKey覆盖的地区从allAreas中移除
                allAreas.removeAll(broadcasts.get(maxKey));
            } else break;
        }
        System.out.println("得到的选择结果是：" + selects);
    }
}
