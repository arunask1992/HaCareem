package com.careem.commons;

import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.stereotype.Component;

@Component
public class WithInTransactionPointcut extends AnnotationMatchingPointcut {

    public WithInTransactionPointcut() {
        super(WithInTransaction.class);
    }
}

