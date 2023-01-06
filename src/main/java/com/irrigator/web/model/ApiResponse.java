package com.irrigator.web.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    public ApiResponse(T data) {
        this.data = data;
        this.success = true;
    }

    public ApiResponse(String error) {
        this.error = error;
        this.success = false;
    }

    public T data;
    public String error;
    public boolean success;
}
