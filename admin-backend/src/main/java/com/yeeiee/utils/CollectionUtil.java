package com.yeeiee.utils;

import lombok.val;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * <p>
 * 集合工具类
 * </p>
 *
 * @author chen
 * @since 2025-01-03
 */
public final class CollectionUtil {
    /**
     * 转树形结构，空间换时间，也可以使用递归
     *
     * @param list        待转换的列表
     * @param getId       获取id
     * @param getPid      获取Pid
     * @param getChildren 获取子节点列表
     * @param setChildren 设置子节点列表
     * @param checkRoot   判断当前节点是否根节点
     * @param <I>         id类型
     * @param <T>         列表项类型
     * @return 树形结构
     */
    public static <I, T> List<T> makeTree(Collection<T> list,
                                          Function<T, I> getId,
                                          Function<T, I> getPid,
                                          Function<T, List<T>> getChildren,
                                          BiConsumer<T, List<T>> setChildren,
                                          Predicate<I> checkRoot) {
        // 结果集
        val result = new ArrayList<T>();
        val map = new HashMap<I, T>();

        // 将所有节点添加到map中
        for (T item : list) {
            val id = getId.apply(item);
            setChildren.accept(item, new ArrayList<>());
            map.put(id, item);
        }

        for (T item : list) {
            // 获取当前节点的父节点ID
            val prentId = getPid.apply(item);

            // 如果父ID是根结点，则添加到结果集中
            if (checkRoot.test(prentId)) {
                result.add(item);
            }

            // 从map中当前节点的父节点
            val parent = map.get(prentId);

            // 如果父节点存在，则将当前节点存入父节点的children中
            if (parent != null) {
                val children = getChildren.apply(parent);
                children.add(item);
            }
        }
        return result;
    }

    /**
     * @param l1  当前列表
     * @param l2  更新列表
     * @param <T> 列表项类型
     * @return left：需要删除的列表，right：需要新增的列表
     */
    public static <T> Pair<HashSet<T>, HashSet<T>> differenceSet(List<T> l1, List<T> l2) {
        val s1 = new HashSet<>(l1);
        val s2 = new HashSet<>(l2);

        s1.removeAll(s2);

        val s3 = new HashSet<>(l1);

        s2.removeAll(s3);

        return Pair.of(s1, s2);
    }
}
