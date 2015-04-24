package com.triompha.rpc.common;

import java.util.*;

import static java.util.Collections.singletonList;

/** Message is immutable value, you would get new instance if you change it. */
@SuppressWarnings("unchecked")
public abstract class Message<M extends Message<M, S>, S> {

    private final S                         startLine;
    private final Map<String, List<String>> headers;
    private final Content                   body;

    protected Message(S startLine, Content body) {
        this(startLine, Collections.<String, List<String>>emptyMap(), body);
        //根据body内容直接 定义出header的len
        if(!(body==null||body.bytes()==null||body.bytes().length==0)){
            set(HeaderNames.CONTENT_LENTH, body.bytes().length+"");
        }
    }

    protected Message(S startLine, Map<String, List<String>> headers, Content body) {
        this.startLine = startLine;
        this.headers = headers;
        this.body = body;
    }

    public M startLine(S value) {return copy(value, headers, body);}

    public S startLine() {return startLine;}

    public M body(Content value) {return copy(startLine, headers, value);}

    public Content body() { return body; }

    public Map<String, List<String>> headers() {return headers;}

    /** New a M with the header if value is present */
    public <V> M set(Header<V> header, V value) {
        if (value == null) return (M) this;
        return copy(startLine,
                    ListMaps.put(ListMaps.filterKeys(headers, withOut(header)), header.name(), singletonList(header.encode(value))),
                    body);
    }

    public M set(String header, String value) {
        if (header == null || value == null) return (M) this;
        return copy(startLine,
                    ListMaps.put(ListMaps.filterKeys(headers, withOut(header)), header, singletonList(value)),
                    body);
    }

    public M set(String header,List<String> values){
        if (header == null || values == null || values.isEmpty()) return (M) this;
        return copy(startLine,
                    ListMaps.put(ListMaps.filterKeys(headers, withOut(header)), header, values),
                    body);
    }

    /** New a M with the header if values is present */
    public <V> M set(Header<V> header, List<V> values) {
        if (values == null || values.isEmpty()) return (M) this;
        return copy(startLine,
                    ListMaps.put(ListMaps.filterKeys(headers, withOut(header)), header.name(), ListMaps.transform(values, encodeBy(header))),
                    body);
    }

    public <A, B> M set(Header<A> h1, A v1, Header<B> h2, B v2) {
        return set(h1, v1).set(h2, v2);
    }

    public <A, B, C> M set(Header<A> h1, A v1, Header<B> h2, B v2, Header<C> h3, C v3) {
        return set(h1, v1).set(h2, v2).set(h3, v3);
    }

    public <A, B, C, D> M set(Header<A> h1, A v1, Header<B> h2, B v2, Header<C> h3, C v3, Header<D> h4, D v4) {
        return set(h1, v1).set(h2, v2).set(h3, v3).set(h4, v4);
    }

    public <A, B, C, D, E> M set(Header<A> h1, A v1, Header<B> h2, B v2, Header<C> h3, C v3, Header<D> h4, D v4, Header<E> h5, E v5) {
        return set(h1, v1).set(h2, v2).set(h3, v3).set(h4, v4).set(h5, v5);
    }

    /** New a M prepend the new value for the header, if the value is present. */
    public <V> M push(Header<V> header, V value) {
        if (value == null) return (M) this;
        final List<String> values = headers.get(header.name());
        if (values == null) return set(header, value);
        return copy(startLine,
                    ListMaps.put(ListMaps.filterKeys(headers, withOut(header)), header.name(), ListMaps.prepend(values, header.encode(value))),
                    body);
    }

    /** New a M prepend the new values for the header, if the values is present. */
    public <V> M push(Header<V> header, List<V> values) {
        if (values == null || values.isEmpty()) return (M) this;
        final List<String> cur = headers.get(header.name());
        if (cur == null) return set(header, values);
        return copy(startLine,
                    ListMaps.put(ListMaps.filterKeys(headers, withOut(header)), header.name(), ListMaps.prepend(cur, ListMaps.transform(values, encodeBy(header)))),
                    body);
    }

    /** Take first value of this header away, or remove this header if there is only one value. */
    public <V> M pop(Header<V> header) {
        final List<String> values = headers.get(header.name());
        if (values == null) return (M) this;
        if (values.isEmpty() || values.size() == 1) return remove0(header);
        return copy(startLine,
                    ListMaps.put(ListMaps.filterKeys(headers, withOut(header)), header.name(), ListMaps.prepend(values.subList(1, values.size()))),
                    body);
    }

    /** Remove all values of this header. */
    public <V> M remove(Header<V> header) {
        final List<V> value = get(header);
        if (value.isEmpty()) return (M) this;
        return remove0(header);
    }

    public M remove(String header) {
        final List<String> value = get(header);
        if (value.isEmpty()) return (M) this;
        return remove0(header);
    }

    /** Get first value of the header, it would throw {@link java.util.NoSuchElementException} if header is absent. */
    public <V> V peek(Header<V> header) {
        final List<V> cur = get(header);
        if (cur.isEmpty()) throw new NoSuchElementException(header.toString());
        return cur.get(0);
    }

    /** Get first value of the header, it would return default value if header is absent. */
    public <V> V peek(Header<V> header, V defaultValue) {
        final List<V> cur = get(header);
        if (cur.isEmpty()) return defaultValue;
        return cur.get(0);
    }

    public String peek(String header){
        List<String> list = get(header);
        return list.isEmpty()?null:list.get(0);
    }

    /** Get all values of the header, it would be empty list if header is absent. */
    public <V> List<V> get(Header<V> header) {
        final List<String> cur = headers.get(header.name());
        if (cur == null) return Collections.emptyList();
        return ListMaps.transform(cur, decodeBy(header));
    }

    public List<String> get(String header){
        final List<String> cur = headers.get(header);
        if (cur == null) return Collections.emptyList();
        return cur;
    }

    public <V> boolean contains(Header<V> header) {
        return headers.containsKey(header.name());
    }

    public boolean contains(String header){
        return headers.containsKey(header);
    }

    @Override
    @SuppressWarnings("all")
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Message)) return false;

        Message message = (Message) o;

        if (!body.equals(message.body)) return false;
        if (!headers.equals(message.headers)) return false;
        if (!startLine.equals(message.startLine)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(new Object[]{startLine, headers, body});
    }

    @Override
    public String toString() {
        return "Message{startLine=" + startLine + ", headers=" + headers + ", body=" + body + '}';
    }

    protected abstract M copy(S startLine, Map<String, List<String>> headers, Content body);

    private <V> M remove0(Header<V> header) {
        return copy(startLine,
                    ListMaps.newListMap(ListMaps.filterKeys(headers, withOut(header))),
                    body);
    }

    private M remove0(String header) {
        return copy(startLine,
                    ListMaps.newListMap(ListMaps.filterKeys(headers, withOut(header))),
                    body);
    }

    private static <V> Function<V, String> encodeBy(final Header<V> header) {
        return new Function<V, String>() {
            @Override
            public String apply(V input) {
                return header.encode(input);
            }
        };
    }

    private static <V> Function<String, V> decodeBy(final Header<V> header) {
        return new Function<String, V>() {
            @Override
            public V apply(String input) {
                return header.decode(input);
            }
        };
    }

    private static Function<String, Boolean> withOut(final Header<?> header) {
        return withOut(header.name());
    }

    private static Function<String, Boolean> withOut(final String header) {
        return new Function<String, Boolean>() {
            @Override
            public Boolean apply(String input) {
                return !input.equals(header);
            }
        };
    }

}
