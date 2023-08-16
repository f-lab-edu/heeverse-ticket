package com.heeverse.common;

public interface SerialNumber<T, E extends SerialTokenDto>  {
    T generate(E serialTokenDto);
}
