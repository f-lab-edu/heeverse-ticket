package com.heeverse.common;

public interface SerialNumber<T>  {
    T generate(SerialTokenDto serialTokenDto);
}
