package com.xichuan.emos.req;

import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
//@Data
//@ApiModel
public class SearchMessageByPageForm {
    @NotNull
    @Min(1)
    private Integer page;
    @NotNull
    @Range(min = 1,max = 40)
    private Integer length;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }
}
