package com.kuartz.core.data.jpa.entity.query;

import static com.querydsl.core.types.PathMetadataFactory.*;
import com.kuartz.core.data.jpa.entity.KuartzEntity;


import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * KuartzEntityQuery is a Querydsl query type for KuartzEntity
 */
@Generated("com.querydsl.codegen.SupertypeSerializer")
public class KuartzEntityQuery extends EntityPathBase<KuartzEntity> {

    private static final long serialVersionUID = 510655367L;

    public static final KuartzEntityQuery kuartzEntity = new KuartzEntityQuery("kuartzEntity");

    public final DateTimePath<java.time.Instant> createdAt = createDateTime("createdAt", java.time.Instant.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.time.Instant> updatedAt = createDateTime("updatedAt", java.time.Instant.class);

    public KuartzEntityQuery(String variable) {
        super(KuartzEntity.class, forVariable(variable));
    }

    public KuartzEntityQuery(Path<? extends KuartzEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public KuartzEntityQuery(PathMetadata metadata) {
        super(KuartzEntity.class, metadata);
    }

}

