package com.careem.commons;

import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.stereotype.Component;

@Component
public class PointcutAdvisor extends DefaultPointcutAdvisor {

    public PointcutAdvisor() {
        super(new WithInTransactionPointcut(), new TransactionProvider());
    }

}