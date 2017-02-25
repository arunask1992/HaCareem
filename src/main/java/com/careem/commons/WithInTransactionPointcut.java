package com.careem.commons;

import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;

public class WithInTransactionPointcut extends AnnotationMatchingPointcut {

    public WithInTransactionPointcut() {
        super(WithInTransaction.class);
    }
}

