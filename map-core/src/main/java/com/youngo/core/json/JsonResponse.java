package com.youngo.core.json;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface JsonResponse {
    JsonMixin[] mixins();
}
