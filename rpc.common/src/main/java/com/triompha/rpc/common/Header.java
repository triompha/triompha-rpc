package com.triompha.rpc.common;

import java.net.URI;


/** Type safety header */
public abstract class Header<V> {
    public static class IntegerHeader extends Header<Integer> {

        public IntegerHeader(String name) {super(name);}

        @Override
        public Integer decode(String value) { return Integer.valueOf(value); }

        @Override
        public String encode(Integer value) { return value.toString(); }

    }

    public static class StringHeader extends Header<String> {

        public StringHeader(String name) {super(name);}

        @Override
        public String decode(String value) { return value; }

        @Override
        public String encode(String value) { return value; }

    }

    public static class BooleanHeader extends Header<Boolean> {

        public BooleanHeader(String name) {super(name);}

        @Override
        public Boolean decode(String value) { return Boolean.valueOf(value); }

        @Override
        public String encode(Boolean value) { return value.toString(); }

    }

    public static class LongHeader extends Header<Long> {

        public LongHeader(String name) {super(name);}

        @Override
        public String encode(Long value) { return value.toString(); }

        @Override
        public Long decode(String value) { return Long.valueOf(value); }

    }

    public static class URIHeader extends Header<URI> {

        public URIHeader(String name) {
            super(name);
        }

        @Override
        public String encode(URI value) {
            return value.toString();
        }

        @Override
        public URI decode(String value) {
            return URI.create(value);
        }

    }


    private final String name;

    protected Header(String name) {
        this.name = name;
    }

    public abstract String encode(V value);

    public abstract V decode(String value);

    public String name() { return name; }

    @Override
    public String toString() { return getClass().getGenericSuperclass() + "#" + name(); }
}