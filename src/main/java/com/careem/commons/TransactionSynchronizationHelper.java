package com.careem.commons;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ReflectiveMethodInvocation;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import static java.util.Arrays.asList;

public abstract class TransactionSynchronizationHelper {


    @Transactional
    public Object openTransactionAndAddTransactionCompletionHookAndDo(MethodInvocation invocation) throws Throwable {
        addTransactionCompletionHook();
        Object[] argumentsInSession = attachToSession(invocation.getArguments());
        ReflectiveMethodInvocation method = (ReflectiveMethodInvocation) invocation;
        return method.invocableClone(argumentsInSession).proceed();
    }

    private Object[] attachToSession(Object[] arguments) {
        return asList(arguments).stream().map(a -> {
            boolean isEntity = a instanceof BaseModel;
            return isEntity ? ((BaseModel) a).getId() != null ? BeanUtil.getBean(EntityManager.class).merge(a) : a : a;
        }).toArray();
    }

    public abstract void addTransactionCompletionHook();

}
