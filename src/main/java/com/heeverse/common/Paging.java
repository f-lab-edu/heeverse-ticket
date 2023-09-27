package com.heeverse.common;

/**
 * @author jeongheekim
 * @date 2023/08/23
 */
public interface Paging {
    int getPageNumber();
    int getPageSize();
    long getOffset();

    Sorting getSorting();

    static Paging ofSize(int size) {return PagingRequest.of(0, size);}
}
