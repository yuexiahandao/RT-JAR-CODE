package com.sun.beans.finder;

import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

abstract class AbstractFinder<T extends Member> {
    private final Class<?>[] args;

    protected AbstractFinder(Class<?>[] paramArrayOfClass) {
        this.args = paramArrayOfClass;
    }

    protected abstract Class<?>[] getParameters(T paramT);

    protected abstract boolean isVarArgs(T paramT);

    protected boolean isValid(T paramT) {
        return Modifier.isPublic(paramT.getModifiers());
    }

    final T find(T[] paramArrayOfT)
            throws NoSuchMethodException {
        HashMap localHashMap = new HashMap();

        Object localObject1 = null;
        Object localObject2 = null;
        int i = 0;
        T ?;
        Class[] arrayOfClass1;
        for (?:paramArrayOfT){
            if (isValid( ?)){
                arrayOfClass1 = getParameters( ?);
                if (arrayOfClass1.length == this.args.length) {
                    PrimitiveWrapperMap.replacePrimitivesWithWrappers(arrayOfClass1);
                    if (isAssignable(arrayOfClass1, this.args)) {
                        if (localObject1 == null) {
                            localObject1 =?;
                            localObject2 = arrayOfClass1;
                        } else {
                            boolean bool1 = isAssignable(localObject2, arrayOfClass1);
                            boolean bool3 = isAssignable(arrayOfClass1, localObject2);

                            if ((bool3) && (bool1)) {
                                bool1 = ! ?.isSynthetic();
                                bool3 = !localObject1.isSynthetic();
                            }
                            if (bool3 == bool1) {
                                i = 1;
                            } else if (bool1) {
                                localObject1 =?;
                                localObject2 = arrayOfClass1;
                                i = 0;
                            }
                        }
                    }
                }
                if (isVarArgs( ?)){
                    int m = arrayOfClass1.length - 1;
                    if (m <= this.args.length) {
                        Class[] arrayOfClass2 = new Class[this.args.length];
                        System.arraycopy(arrayOfClass1, 0, arrayOfClass2, 0, m);
                        if (m < this.args.length) {
                            Class localClass = arrayOfClass1[m].getComponentType();
                            if (localClass.isPrimitive()) {
                                localClass = PrimitiveWrapperMap.getType(localClass.getName());
                            }
                            for (int n = m; n < this.args.length; n++) {
                                arrayOfClass2[n] = localClass;
                            }
                        }
                        localHashMap.put( ?, arrayOfClass2);
                    }
                }
            }
        }
        for (?:paramArrayOfT){
            arrayOfClass1 = (Class[]) localHashMap.get( ?);
            if ((arrayOfClass1 != null) &&
                    (isAssignable(arrayOfClass1, this.args))) {
                if (localObject1 == null) {
                    localObject1 =?;
                    localObject2 = arrayOfClass1;
                } else {
                    boolean bool2 = isAssignable(localObject2, arrayOfClass1);
                    boolean bool4 = isAssignable(arrayOfClass1, localObject2);

                    if ((bool4) && (bool2)) {
                        bool2 = ! ?.isSynthetic();
                        bool4 = !localObject1.isSynthetic();
                    }
                    if (bool4 == bool2) {
                        if (localObject2 == localHashMap.get(localObject1))
                            i = 1;
                    } else if (bool2) {
                        localObject1 =?;
                        localObject2 = arrayOfClass1;
                        i = 0;
                    }
                }
            }

        }

        if (i != 0) {
            throw new NoSuchMethodException("Ambiguous methods are found");
        }
        if (localObject1 == null) {
            throw new NoSuchMethodException("Method is not found");
        }
        return localObject1;
    }

    private boolean isAssignable(Class<?>[] paramArrayOfClass1, Class<?>[] paramArrayOfClass2) {
        for (int i = 0; i < this.args.length; i++) {
            if ((null != this.args[i]) &&
                    (!paramArrayOfClass1[i].isAssignableFrom(paramArrayOfClass2[i]))) {
                return false;
            }
        }

        return true;
    }
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.beans.finder.AbstractFinder
 * JD-Core Version:    0.6.2
 */