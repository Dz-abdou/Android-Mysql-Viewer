package com.example.mysql.models;

import androidx.annotation.Nullable;

public class Cell {

    @Nullable
    private Object data;

    public Cell(@Nullable Object data) {
        this.data = data;
    }

    @Nullable
    public Object getData() {
        return this.data;
    }

    public void setData(Object object) {
        this.data = object;
    }

}
