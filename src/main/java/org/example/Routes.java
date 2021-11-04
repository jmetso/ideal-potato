package org.example;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

@Component
public class Routes extends RouteBuilder {

    public void configure() {

        restConfiguration()
                .component("servlet")
                .bindingMode(RestBindingMode.json);

        rest("/v1")
            .get("/hello")
                .produces("text/plain")
                .to("direct:hello")
            .get("/hello/{name}")
                .produces("text/plain")
                .to("direct:hello-name")
            .get("/timeout/{value}")
                .produces("text/plain")
                .to("direct:timeout");
        
        from("direct:hello")
                .routeId("hello-world")
                .log("direct:hello")
                .transform().constant("Hello World!");

        from("direct:hello-name")
                .routeId("hello-name")
                .log("direct:hello-name")
                .transform().simple("Hello ${header.name}!");

        from("direct:timeout")
                .log("direct:timeout, ${header.value}")
                .delay(simple("${header.value}"))
                .to("direct:hello");

    }

}

