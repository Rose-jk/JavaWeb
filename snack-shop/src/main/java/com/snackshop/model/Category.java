package com.snackshop.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

@Data
@Document(collection = "categories")
public class Category {
    @Id
    private String id;

    @Indexed(unique = true)
    private String name;

    private String description;
    private String icon;
    private String color;
    private Integer productCount = 0;
    private Integer sortOrder = 0;
    private Boolean active = true;
}