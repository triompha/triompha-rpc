package com.triompha.rpc.common;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public abstract class Content {
    public static final Content NO_CONTENT = new Content() {
        @Override
        public byte[] bytes() { return new byte[0]; }

        @Override
        public String toString() { return "<No content>"; }
    };

    public static Content content(final byte[] bytes) {
        return new Content() {
            @Override
            public byte[] bytes() { return bytes; }

            @Override
            public String toString() { return bytes + " [" + bytes.length + ']'; }

        };
    }

    public static Content content(final String value) {
        return new Content() {
            @Override
            public byte[] bytes() {
                try {
                    // compatibly with android api level 7
                    return value.getBytes("UTF8");
                } catch (UnsupportedEncodingException e) {
                    throw new IllegalStateException(e);
                }
            }

            @Override
            public String toString() { return value; }
        };
    }

    public static <T> Content content(final T value, final Encoder<T> encoder) {
        return new Content() {
            @Override
            public byte[] bytes() { return encoder.encode(value); }

            @Override
            public String toString() { return value.toString(); }

        };
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof Content) {
            Content content = (Content) obj;
            return Arrays.equals(bytes(), content.bytes());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(bytes());
    }

    public abstract byte[] bytes();

    public interface Encoder<T> {
        byte[] encode(T value);
    }
}