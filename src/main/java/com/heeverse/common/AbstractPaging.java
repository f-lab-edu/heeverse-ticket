package com.heeverse.common;

/**
 * @author jeongheekim
 * @date 2023/08/23
 */
public abstract class AbstractPaging implements Paging {

    private final int page;
    private final int size;
    private final long offset;

    public AbstractPaging(int page, int size) {
        if (page < 0) {
            throw new IllegalArgumentException("page는 0부터 시작합니다.");
        }

        if (size < 1) {
            throw new IllegalArgumentException("한 페이지에 보여질 요소의 개수는 1개 이상입니다.");
        }

        this.page = page;
        this.size = size;
        this.offset = (long) page * (long) size;

    }

    @Override
    public int getPageNumber() {
        return page;
    }

    @Override
    public int getPageSize() {
        return size;
    }

    @Override
    public long getOffset() {
        return offset;
    }
}
