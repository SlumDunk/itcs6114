package com.uncc;

/**
 * @Author: zerongliu
 * @Date: 12/4/18 19:37
 * @Description: the vertex class
 */
public class Vertex {
    private String value;

    public Vertex(String value) {
        this.value = value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this.value.equals(((Vertex) obj).getValue())) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }
}
