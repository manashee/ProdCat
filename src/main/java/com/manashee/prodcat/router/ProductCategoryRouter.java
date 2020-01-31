package com.manashee.prodcat.router;

import com.manashee.prodcat.handler.ProductCategoryRouteHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static com.manashee.prodcat.constants.ProductCategoryConstants.*;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class ProductCategoryRouter {

    @Bean
    public RouterFunction<ServerResponse> routeProductCategories (ProductCategoryRouteHandler handler) {
        return RouterFunctions
                .route(GET(FIND_ALL).and ( accept( MediaType.APPLICATION_JSON)), handler::getAllItems)
                .andRoute(GET(FIND_BY_ID).and ( accept( MediaType.APPLICATION_JSON)), handler::findById)
                .andRoute(POST(POST_PROD_CAT).and(accept(MediaType.APPLICATION_JSON)), handler::create);
    }
}
