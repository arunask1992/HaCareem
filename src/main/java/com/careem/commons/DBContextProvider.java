package com.careem.commons;

import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
@WithInTransaction
public class DBContextProvider {
    public <T> T withDBConnection(Supplier<T> actionToPerform) {
        return actionToPerform.get();
    }
    public void withDBConnection(Runnable block) {
        block.run();
    }
}
