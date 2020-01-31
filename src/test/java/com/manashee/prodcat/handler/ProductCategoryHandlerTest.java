package com.manashee.prodcat.handler;

import com.manashee.prodcat.constants.ProductCategoryConstants;
import com.manashee.prodcat.document.ProductCategory;
import com.manashee.prodcat.repository.ProductCategoryReactiveRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
@DirtiesContext
@AutoConfigureWebTestClient
@ActiveProfiles("test")
public class ProductCategoryHandlerTest {

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    ProductCategoryReactiveRepository productCategoryReactiveRepository;

    List<ProductCategory> list = Arrays.asList(
            new ProductCategory(null, "Sea Food", "Everything fishy", "s3://xyz/fish")
            , new ProductCategory(null, "Poultry", "Everything chick", "s3://xyz/chick")
            , new ProductCategory("ABC", "Children", "Everything kiddy", "s3://xyz/kid")
    );

    @Before
    public void setup() {
        productCategoryReactiveRepository
                .deleteAll()
                .thenMany(Flux.fromIterable(list))
                .flatMap(productCategoryReactiveRepository::save)
                .doOnNext( productCategory -> System.out.println(productCategory))
                .blockLast();
    }


    @Test
    public void getAllItems (){

        webTestClient
                .get()
                .uri(ProductCategoryConstants.FIND_ALL)
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(ProductCategory.class)
                .hasSize(3);
    }


    @Test
    public void findById (){

        webTestClient
                .get()
                .uri("/v1/fun/prodcat/ABC")
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(ProductCategory.class)
                .hasSize(1);
    }

    @Test
    public void findById2 (){

        webTestClient
                .get()
                .uri("/v1/fun/prodcat/DEF")
                .exchange()
                .expectStatus()
                .isNotFound();
    }


    @Test
    public void create (){

        ProductCategory productCategory = new ProductCategory(null,"Meat","Mutton","s3://xyz/mutton");

        webTestClient
                .post()
                .uri("/v1/fun/prodcat")
                .body(Mono.just(productCategory),ProductCategory.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo("Meat")
        .consumeWith( entityExchangeResult -> System.out.println(entityExchangeResult));

    }

}
