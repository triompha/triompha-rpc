package com.triompha.rpc.common;
public interface Function<I, O> {
    O apply(I input);
}
