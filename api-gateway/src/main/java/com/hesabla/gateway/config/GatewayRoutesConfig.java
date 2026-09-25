package com.hesabla.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.cloud.gateway.server.mvc.filter.LoadBalancerFilterFunctions.lb;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.web.servlet.function.RequestPredicates.path;

/**
 * DİQQƏT: lb:// sxemi ilə yükdaşıma (load balancing) üçün .before(uri("lb://..."))
 * İŞLƏMİR (adi HTTP client bunu "unroutable protocol scheme" kimi rədd edir).
 * Bunun əvəzinə XÜSUSİ filter funksiyası lazımdır: LoadBalancerFilterFunctions.lb(serviceId),
 * .filter(...) ilə tətbiq olunur, .before(uri(...)) ilə YOX.
 */
@Configuration
public class GatewayRoutesConfig {

    @Bean
    public RouterFunction<ServerResponse> authServiceRoute() {
        return route("auth-service")
                .route(path("/api/auth/**"), http())
                .filter(lb("AUTH-SERVICE"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> invoicesRoute() {
        return route("invoicing-invoices")
                .route(path("/api/invoices/**"), http())
                .filter(lb("INVOICING-SERVICE"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> customersRoute() {
        return route("invoicing-customers")
                .route(path("/api/customers/**"), http())
                .filter(lb("INVOICING-SERVICE"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> productsRoute() {
        return route("invoicing-products")
                .route(path("/api/products/**"), http())
                .filter(lb("INVOICING-SERVICE"))
                .build();
    }
}