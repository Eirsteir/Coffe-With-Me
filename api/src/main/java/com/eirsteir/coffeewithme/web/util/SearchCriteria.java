package com.eirsteir.coffeewithme.web.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SearchCriteria {

    private String key;
    private SearchOperation operation;
    private Object value;

}