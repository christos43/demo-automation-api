package utils.factory;

import api.RestfulBookerApi;
import utils.Test;

import java.util.Objects;

public class WebServiceFactory {

    final Test test;
    RestfulBookerApi restfulBookerApi;

    public WebServiceFactory(Test test) {
        this.test = test;
    }

    public RestfulBookerApi restfulBookerApi() {
        return Objects.requireNonNullElseGet(restfulBookerApi, () -> restfulBookerApi = new RestfulBookerApi(test));
    }

}
