package com.heeverse.common;


/**
 * @author jeongheekim
 * @date 2023/08/23
 */
public class PagingRequest extends AbstractPaging {
    private final SORTING sorting;

    public PagingRequest(int page, int size, SORTING sorting) {
        super(page, size);
        this.sorting = sorting;
    }

    public static PagingRequest of(int page, int size) {
        return of(page, size, SORTING.ASC);
    }

    public static PagingRequest of(int page, int size, SORTING asc) {
        return new PagingRequest(page, size, asc);
    }

    @Override
    public SORTING getSorting() {
        return sorting;
    }
}
