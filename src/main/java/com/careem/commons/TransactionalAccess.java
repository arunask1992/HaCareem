package com.careem.commons;

import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
public class TransactionalAccess {

    @Transactional(rollbackOn = Exception.class)
    public BaseModel<?> persist(BaseModel<?> object) {
        return object.persist();
    }
}

