package com.careem.commons;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ReflectiveMethodInvocation;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
public class EcommerceTransactionSynchronization extends TransactionSynchronizationHelper {

    @Transactional
    public Object openTransactionAndAddTransactionCompletionHookAndDo(MethodInvocation invocation) throws Throwable {
        ReflectiveMethodInvocation method = (ReflectiveMethodInvocation) invocation;
        return method.invocableClone(invocation.getArguments()).proceed();
    }

    public  void addTransactionCompletionHook() {

    }
}
