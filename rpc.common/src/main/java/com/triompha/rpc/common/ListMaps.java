package com.triompha.rpc.common;

import java.util.*;

public class ListMaps {
    public static <K, V> Map<K, List<V>> filterKeys(Map<K, List<V>> headers, Function<K, Boolean> predicate) {
        Map<K, List<V>> map = new HashMap<K, List<V>>();
        for (Map.Entry<K, List<V>> entry : headers.entrySet()) {
            if (predicate.apply(entry.getKey())) map.put(entry.getKey(), entry.getValue());
        }
        return map;
    }

    public static <K, V> Map<K, List<V>> put(Map<K, List<V>> map, K name, List<V> values) {
        map.put(name, values);
        return map;
    }

    public static <K, V> Map<K, List<V>> concat(Map<K, List<V>> map, K name, V... values) {
        Map<K, List<V>> m = new HashMap<K, List<V>>(map);
        m.put(name, prepend(map.get(name), values));
        return m;
    }

    public static <T> List<T> prepend(List<T> values, T... more) {
        return prepend(values, Arrays.asList(more));
    }

    public static <K, V> List<K> transform(List<V> values, Function<V, K> function) {
        List<K> list = new ArrayList<K>(values.size());
        for (V v : values) {
            list.add(function.apply(v));
        }
        return list;
    }

    public static <T> List<T> prepend(List<T> values, List<T> more) {
        if (values == null) {
            return new ArrayList<T>(more);
        } else {
            final List<T> ts = new ArrayList<T>(values.size() + more.size());
            if (!more.isEmpty()) ts.addAll(more);
            ts.addAll(values);
            return ts;
        }
    }

    public static <T> List<T> append(List<T> values, List<T> more) {
        if (values == null) {
            return new ArrayList<T>(more);
        } else {
            final List<T> ts = new ArrayList<T>(values.size() + more.size());
            ts.addAll(values);
            if (!more.isEmpty()) ts.addAll(more);
            return ts;
        }
    }

    public static <K, V> Map<K, List<V>> newListMap(Map<K, List<V>> values) {
        return new HashMap<K, List<V>>(values);
    }

    public static <K, V> Map<K, List<V>> of(K k, V v) {
        return Collections.singletonMap(k, Collections.singletonList(v));
    }

}
