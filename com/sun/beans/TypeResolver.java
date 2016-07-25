package com.sun.beans;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import sun.reflect.generics.reflectiveObjects.GenericArrayTypeImpl;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

public final class TypeResolver {
    private static final WeakCache<Type, Map<Type, Type>> CACHE = new WeakCache();

    public static Type resolveInClass(Class<?> paramClass, Type paramType) {
        return resolve(getActualType(paramClass), paramType);
    }

    public static Type[] resolveInClass(Class<?> paramClass, Type[] paramArrayOfType) {
        return resolve(getActualType(paramClass), paramArrayOfType);
    }

    public static Type resolve(Type paramType1, Type paramType2) {
        if ((paramType2 instanceof Class))
            return paramType2;
        Object localObject1;
        if ((paramType2 instanceof GenericArrayType)) {
            localObject1 = ((GenericArrayType) paramType2).getGenericComponentType();
            localObject1 = resolve(paramType1, (Type) localObject1);
            return (localObject1 instanceof Class) ? Array.newInstance((Class) localObject1, 0).getClass() : GenericArrayTypeImpl.make((Type) localObject1);
        }
        Type[] arrayOfType1;
        if ((paramType2 instanceof ParameterizedType)) {
            localObject1 = (ParameterizedType) paramType2;
            arrayOfType1 = resolve(paramType1, ((ParameterizedType) localObject1).getActualTypeArguments());
            return ParameterizedTypeImpl.make((Class) ((ParameterizedType) localObject1).getRawType(), arrayOfType1, ((ParameterizedType) localObject1).getOwnerType());
        }

        if ((paramType2 instanceof WildcardType)) {
            localObject1 = (WildcardType) paramType2;
            arrayOfType1 = resolve(paramType1, ((WildcardType) localObject1).getUpperBounds());
            Type[] arrayOfType2 = resolve(paramType1, ((WildcardType) localObject1).getLowerBounds());
            return new WildcardTypeImpl(arrayOfType1, arrayOfType2);
        }
        if ((paramType2 instanceof TypeVariable)) {
            synchronized (CACHE) {
                localObject1 = (Map) CACHE.get(paramType1);
                if (localObject1 == null) {
                    localObject1 = new HashMap();
                    prepare((Map) localObject1, paramType1);
                    CACHE.put(paramType1, localObject1);
                }
            }
            ???=(Type) ((Map) localObject1).get(paramType2);
            if (( ???==null)||( ???.equals(paramType2))){
                return paramType2;
            }
            ???=fixGenericArray((Type) ? ??);

            return resolve(paramType1, (Type) ? ??);
        }
        throw new IllegalArgumentException("Bad Type kind: " + paramType2.getClass());
    }

    public static Type[] resolve(Type paramType, Type[] paramArrayOfType) {
        int i = paramArrayOfType.length;
        Type[] arrayOfType = new Type[i];
        for (int j = 0; j < i; j++) {
            arrayOfType[j] = resolve(paramType, paramArrayOfType[j]);
        }
        return arrayOfType;
    }

    public static Class<?> erase(Type paramType) {
        if ((paramType instanceof Class))
            return (Class) paramType;
        Object localObject;
        if ((paramType instanceof ParameterizedType)) {
            localObject = (ParameterizedType) paramType;
            return (Class) ((ParameterizedType) localObject).getRawType();
        }
        Type[] arrayOfType;
        if ((paramType instanceof TypeVariable)) {
            localObject = (TypeVariable) paramType;
            arrayOfType = ((TypeVariable) localObject).getBounds();
            return 0 < arrayOfType.length ? erase(arrayOfType[0]) : Object.class;
        }

        if ((paramType instanceof WildcardType)) {
            localObject = (WildcardType) paramType;
            arrayOfType = ((WildcardType) localObject).getUpperBounds();
            return 0 < arrayOfType.length ? erase(arrayOfType[0]) : Object.class;
        }

        if ((paramType instanceof GenericArrayType)) {
            localObject = (GenericArrayType) paramType;
            return Array.newInstance(erase(((GenericArrayType) localObject).getGenericComponentType()), 0).getClass();
        }
        throw new IllegalArgumentException("Unknown Type kind: " + paramType.getClass());
    }

    public static Class[] erase(Type[] paramArrayOfType) {
        int i = paramArrayOfType.length;
        Class[] arrayOfClass = new Class[i];
        for (int j = 0; j < i; j++) {
            arrayOfClass[j] = erase(paramArrayOfType[j]);
        }
        return arrayOfClass;
    }

    private static void prepare(Map<Type, Type> paramMap, Type paramType) {
        Class localClass = (Class) ((paramType instanceof Class) ? paramType : ((ParameterizedType) paramType).getRawType());

        TypeVariable[] arrayOfTypeVariable = localClass.getTypeParameters();

        Type[] arrayOfType = (paramType instanceof Class) ? arrayOfTypeVariable : ((ParameterizedType) paramType).getActualTypeArguments();

        assert (arrayOfTypeVariable.length == arrayOfType.length);
        for (int i = 0; i < arrayOfTypeVariable.length; i++) {
            paramMap.put(arrayOfTypeVariable[i], arrayOfType[i]);
        }
        Type localType1 = localClass.getGenericSuperclass();
        if (localType1 != null) {
            prepare(paramMap, localType1);
        }
        for (Type localType2 : localClass.getGenericInterfaces()) {
            prepare(paramMap, localType2);
        }

        if (((paramType instanceof Class)) && (arrayOfTypeVariable.length > 0))
            for (???=paramMap.entrySet().iterator();
        ((Iterator) ? ??).hasNext();){
            Map.Entry localEntry = (Map.Entry) ((Iterator) ? ??).next();
            localEntry.setValue(erase((Type) localEntry.getValue()));
        }
    }

    private static Type fixGenericArray(Type paramType) {
        if ((paramType instanceof GenericArrayType)) {
            Type localType = ((GenericArrayType) paramType).getGenericComponentType();
            localType = fixGenericArray(localType);
            if ((localType instanceof Class)) {
                return Array.newInstance((Class) localType, 0).getClass();
            }
        }
        return paramType;
    }

    private static Type getActualType(Class<?> paramClass) {
        TypeVariable[] arrayOfTypeVariable = paramClass.getTypeParameters();
        return arrayOfTypeVariable.length == 0 ? paramClass : ParameterizedTypeImpl.make(paramClass, arrayOfTypeVariable, paramClass.getEnclosingClass());
    }
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.beans.TypeResolver
 * JD-Core Version:    0.6.2
 */