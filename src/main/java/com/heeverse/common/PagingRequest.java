package com.heeverse.common;


/**
 * @author jeongheekim
 * @date 2023/08/23
 */
public class PagingRequest extends AbstractPaging {
    private final Sorting sorting;

    public PagingRequest(int page, int size, Sorting sorting) {
        super(page, size);
        this.sorting = sorting;
    }

    public static PagingRequest of(int page, int size) {
        return of(page, size, Sorting.ASC);
    }

    public static PagingRequest of(int page, int size, Sorting asc) {
        return new PagingRequest(page, size, asc);
    }

    @Override
    public Sorting getSorting() {
        return sorting;
    }
}
