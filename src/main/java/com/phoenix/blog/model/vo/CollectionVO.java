package com.phoenix.blog.model.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CollectionVO {
    String collectionId;
    String collectionName;
    String collectionUserName;
    String collectionReviseTime;
}
