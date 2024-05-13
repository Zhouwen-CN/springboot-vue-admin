package com.yeeiee;

import java.util.*;

public class Test {
    public static void main(String[] args) {

        System.out.println("===============Set=================");
        Set<Integer> set1 = new HashSet<>(Arrays.asList(1, 2, 3, 4));
        Set<Integer> set2 = new HashSet<>(Arrays.asList(3, 4, 5, 6));
        // 交集
        Set<Integer> intersectionSet = new HashSet<>(set1);
        intersectionSet.retainAll(set2);
        System.out.println("交集：" + intersectionSet);

        // 并集
        Set<Integer> unionSet = new HashSet<>(set1);
        unionSet.addAll(set2);
        System.out.println("并集：" + unionSet);

        // 差集
        Set<Integer> differenceSet = new HashSet<>(set1);
        differenceSet.removeAll(set2);
        System.out.println("差集：" + differenceSet);

        System.out.println("===============List=================");
        List<Integer> list1 = new ArrayList<>(Arrays.asList(1, 2, 3, 4));
        List<Integer> list2 = new ArrayList<>(Arrays.asList(3, 4, 5, 6));
        // 交集
        List<Integer> intersectionList = new ArrayList<>(list1);
        intersectionList.retainAll(list2);
        System.out.println("交集：" + intersectionSet);

        // 并集
        List<Integer> unionList = new ArrayList<>(list1);
        unionList.addAll(list2);
        System.out.println("并集：" + unionList);

        // 差集
        List<Integer> differenceList = new ArrayList<>(list1);
        differenceList.removeAll(list2);
        System.out.println("差集：" + differenceList);

    }
}
