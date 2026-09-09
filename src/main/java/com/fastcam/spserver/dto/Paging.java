package com.fastcam.spserver.dto;

import lombok.Data;

@Data
public class Paging {
    private int page =1;
    private int displayRow=3;
    private int startNum;
    private int totalCount;
    private int totalPage;

    public void calPaging() {
        totalPage = (int)Math.ceil( totalCount/(double)displayRow );
        startNum = (page-1)*displayRow;
    }

}


