package com.careem.commons;

import org.springframework.stereotype.Component;

@Component
public class TransactionProvider extends AbstractTransactionProvider {

    @Override
    protected TransactionSynchronizationHelper getTransactionSynchronizationHelper() throws Throwable {
        return BeanUtil.getBean(EcommerceTransactionSynchronization.class);
    }
}
