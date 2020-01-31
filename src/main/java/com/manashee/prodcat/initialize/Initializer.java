package com.manashee.prodcat.initialize;

import com.manashee.prodcat.document.ProductCategory;
import com.manashee.prodcat.repository.ProductCategoryReactiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;

@Component
@Profile("!test")
public class Initializer implements CommandLineRunner {

    @Autowired
    ProductCategoryReactiveRepository productCategoryReactiveRepository;

    List<ProductCategory> list = Arrays.asList(
            new ProductCategory(null, "Sea Food", "Everything fishy", "s3://xyz/fish")
            , new ProductCategory(null, "Poultry", "Everything chick", "s3://xyz/chick")
            , new ProductCategory("ABC", "Children", "Everything kiddy", "s3://xyz/kid")
    );

    @Override
    public void run(String... args) throws Exception {
        productCategoryReactiveRepository.deleteAll()
        .thenMany(Flux.fromIterable(list))
                .flatMap(productCategoryReactiveRepository::save)
                .thenMany(productCategoryReactiveRepository.findAll())
                .subscribe( productCategory -> System.out.println(" Inserted : " + productCategory));


    }
}


