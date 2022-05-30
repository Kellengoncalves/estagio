package com.example.projetoestagio.model;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@ApiModel
public class ExceptionResponse {

    private int status_code;
    private String message;

}
