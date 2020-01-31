package com.manashee.prodcat.repository;

import com.manashee.prodcat.document.ProductCategory;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ProductCategoryReactiveRepository extends ReactiveMongoRepository<ProductCategory,String> {
}
