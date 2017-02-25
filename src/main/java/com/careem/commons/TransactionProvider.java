package com.careem.commons;

public class TransactionProvider extends AbstractTransactionProvider {

    @Override
    protected TransactionSynchronizationHelper getTransactionSynchronizationHelper() throws Throwable {
        return BeanUtil.getBean(EcommerceTransactionSynchronization.class);
    }
}
