package com.manashee.prodcat.document;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProductCategory {
    @Id
    private String id;
    private String name;
    private String description;
    private String imageUri;
}
