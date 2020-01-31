package com.manashee.prodcat.repository;

import com.manashee.prodcat.document.ProductCategory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

@DataMongoTest
@RunWith(SpringRunner.class)
public class ProductCategoryReactiveRepositoryTest {

    @Autowired
    ProductCategoryReactiveRepository productCategoryReactiveRepository;


    List<ProductCategory> list = Arrays.asList(
            new ProductCategory(null, "Sea Food", "Everything fishy", "s3://xyz/fish")
            , new ProductCategory(null, "Poultry", "Everything chick", "s3://xyz/chick")
            , new ProductCategory("ABC", "Children", "Everything kiddy", "s3://xyz/kid")
    );


    @Before
    public void setup(){
        productCategoryReactiveRepository
                .deleteAll()
                .thenMany(Flux.fromIterable(list))
                .flatMap( productCategoryReactiveRepository::save)
                .doOnNext( cat -> System.out.println(" Product Category : " + cat ))
                .blockLast();
    }

    @Test
    public void getAllCategories() {
        StepVerifier.create ( productCategoryReactiveRepository.findAll() )
                .expectSubscription()
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    public void getProductCategoryById ( ) {

        StepVerifier.create ( productCategoryReactiveRepository.findById("ABC") )
                .expectSubscription()
                .expectNextMatches( productCategory -> productCategory.getName().equals("Children"))
                .verifyComplete();
    }

    @Test
    public void deleteById () {

        StepVerifier.create(
            productCategoryReactiveRepository.deleteById("ABC"))
                .expectSubscription()
                .verifyComplete();

        StepVerifier.create( productCategoryReactiveRepository.existsById("ABC"))
                .expectSubscription()
                .expectNext(Boolean.FALSE)
                .verifyComplete();
    }

    @Test
    public void save() {

        ProductCategory productCategory = new ProductCategory(null, "Deals", "Everything cheap", "s3://xyz/deals");

        StepVerifier.create( productCategoryReactiveRepository.save(productCategory) )
                .expectSubscription()
                .expectNextMatches(productCategory1 -> productCategory1.getName().equals("Deals"))
                .verifyComplete();
    }


}
