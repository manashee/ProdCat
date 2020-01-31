package com.manashee.prodcat.handler;

import com.manashee.prodcat.document.ProductCategory;
import com.manashee.prodcat.repository.ProductCategoryReactiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.BodyInserters.fromObject;

@Component
public class ProductCategoryRouteHandler {
    @Autowired
    ProductCategoryReactiveRepository productCategoryReactiveRepository;

    static Mono<ServerResponse> notFound = ServerResponse.notFound().build();

    public Mono<ServerResponse> getAllItems ( ServerRequest serverRequest ){
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(productCategoryReactiveRepository.findAll(), ProductCategory.class);
    }

    public Mono<ServerResponse> findById ( ServerRequest serverRequest ){
        String id = serverRequest.pathVariable("id");
        Mono<ProductCategory> productCategoryMono = productCategoryReactiveRepository.findById(id);

        return productCategoryMono
                .flatMap(
                        productCategory ->
                                ServerResponse
                                .ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(fromObject(productCategory))
                )
                .switchIfEmpty(notFound);
    }

    public Mono<ServerResponse> create ( ServerRequest serverRequest ){

        Mono<ProductCategory> productCategoryMono = serverRequest.bodyToMono(ProductCategory.class);

        return productCategoryMono
                .flatMap( productCategory -> ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(productCategoryReactiveRepository
                        .save(productCategory), ProductCategory.class));
    }
}
