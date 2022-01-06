package com.pitt.papersearchengine;

import java.util.LinkedHashMap;

public class Query {
    private String queryContent;
    private String queryType;
    private String docId1;
    private String docId2;
    private String docId3;
    private String docId4;
    private String docId5;
    private String docId6;
    private String docId7;

    private String resultContent;
    private LinkedHashMap<String,String> typeOptions;

    public Query(){
        // populate search options
        typeOptions = new LinkedHashMap<>();
        typeOptions.put("key","general keyword");
        typeOptions.put("tt","title");
        typeOptions.put("au","author");
        typeOptions.put("as","abstract");
        typeOptions.put("is","institution");
        typeOptions.put("ar","author region");

    }

    public String getQueryContent() {
        return queryContent;
    }

    public void setQueryContent(String queryContent) {
        this.queryContent = queryContent;
    }

    public String getQueryType() {
        return queryType;
    }

    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }

    public LinkedHashMap<String, String> getTypeOptions() {
        return typeOptions;
    }

    public String getDocId1() {
        return docId1;
    }

    public void setDocId1(String docId) {
        this.docId1 = docId;
    }

    public String getResultContent() {
        return resultContent;
    }

    public void setResultContent(String resultContent) {
        this.resultContent = resultContent;
    }
}
