package com.example.litepaltest;

import org.litepal.crud.DataSupport;

/**
 * Created by qianyl on 2018/1/15.
 */

public class Category extends DataSupport{
    private int id;
    private String categoryName;
    private int categoryCode;
    public void setId(int id){
        this.id=id;
    }
    public void setCategoryName(String categoryName){
        this.categoryName=categoryName;
    }
    public void setCategoryCode(int categoryCode){
        this.categoryCode=categoryCode;
    }


}
