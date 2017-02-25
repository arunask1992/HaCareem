package com.careem.commons;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.annotation.Annotation;

import static java.util.Arrays.asList;

public abstract class AbstractTransactionProvider implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        return needTransaction(invocation)
                ? getTransactionSynchronizationHelper().openTransactionAndAddTransactionCompletionHookAndDo(invocation)
                : invocation.proceed();
    }

    protected abstract TransactionSynchronizationHelper getTransactionSynchronizationHelper() throws Throwable;

    private boolean needTransaction(MethodInvocation invocation) {
        Annotation[] allMethodAnnotations = invocation.getMethod().getDeclaredAnnotations();
        return asList(allMethodAnnotations).stream().noneMatch(a -> a.annotationType().equals(NoTransaction.class));
    }
}

