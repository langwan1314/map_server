package com.youngo.core.json;

interface ResponseWrapper {

    boolean hasJsonMixins();

    JsonResponse getJsonResponse();

    Object getOriginalResponse();

}
