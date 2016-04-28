package com.leqisoft.inter;

/**
 * Created by zhuleqi on 16/3/19.
 */
public interface Callback<P,T> {
    public T call(P p);
}
