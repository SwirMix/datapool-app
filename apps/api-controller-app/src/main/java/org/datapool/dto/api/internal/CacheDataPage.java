package org.datapool.dto.api.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CacheDataPage {
    private Long totalPage = 0l;
    private Long currentPage = 0l;
    private Long size = 10l;
    private List<Map> data;
    private ArrayList columns;

    public ArrayList getColumns() {
        return columns;
    }

    public CacheDataPage setColumns(ArrayList columns) {
        this.columns = columns;
        return this;
    }

    public Long getTotalPage() {
        return totalPage;
    }

    public CacheDataPage setTotalPage(Long totalPage) {
        this.totalPage = totalPage;
        return this;
    }

    public Long getCurrentPage() {
        return currentPage;
    }

    public CacheDataPage setCurrentPage(Long currentPage) {
        this.currentPage = currentPage;
        return this;
    }

    public Long getSize() {
        return size;
    }

    public CacheDataPage setSize(Long size) {
        this.size = size;
        return this;
    }

    public List<Map> getData() {
        return data;
    }

    public CacheDataPage setData(List<Map> data) {
        this.data = data;
        return this;
    }
}
