package com.sun.beans.finder;

import java.beans.PersistenceDelegate;
import java.util.HashMap;
import java.util.Map;

public final class PersistenceDelegateFinder extends InstanceFinder<PersistenceDelegate> {
    private final Map<Class<?>, PersistenceDelegate> registry;

    public PersistenceDelegateFinder() {
        super(PersistenceDelegate.class, true, "PersistenceDelegate", new String[0]);
        this.registry = new HashMap();
    }

    public void register(Class<?> paramClass, PersistenceDelegate paramPersistenceDelegate) {
        synchronized (this.registry) {
            if (paramPersistenceDelegate != null) {
                this.registry.put(paramClass, paramPersistenceDelegate);
            } else
                this.registry.remove(paramClass);
        }
    }

    public PersistenceDelegate find(Class<?> paramClass) {
        PersistenceDelegate localPersistenceDelegate;
        synchronized (this.registry) {
            localPersistenceDelegate = (PersistenceDelegate) this.registry.get(paramClass);
        }
        return localPersistenceDelegate != null ? localPersistenceDelegate : (PersistenceDelegate) super.find(paramClass);
    }
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.beans.finder.PersistenceDelegateFinder
 * JD-Core Version:    0.6.2
 */