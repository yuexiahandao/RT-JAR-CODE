package com.sun.beans;

import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.Arrays;

final class WildcardTypeImpl
        implements WildcardType {
    private final Type[] upperBounds;
    private final Type[] lowerBounds;

    WildcardTypeImpl(Type[] paramArrayOfType1, Type[] paramArrayOfType2) {
        this.upperBounds = paramArrayOfType1;
        this.lowerBounds = paramArrayOfType2;
    }

    public Type[] getUpperBounds() {
        return (Type[]) this.upperBounds.clone();
    }

    public Type[] getLowerBounds() {
        return (Type[]) this.lowerBounds.clone();
    }

    public boolean equals(Object paramObject) {
        if ((paramObject instanceof WildcardType)) {
            WildcardType localWildcardType = (WildcardType) paramObject;
            return (Arrays.equals(this.upperBounds, localWildcardType.getUpperBounds())) && (Arrays.equals(this.lowerBounds, localWildcardType.getLowerBounds()));
        }

        return false;
    }

    public int hashCode() {
        return Arrays.hashCode(this.upperBounds) ^ Arrays.hashCode(this.lowerBounds);
    }

    public String toString() {
        Type[] arrayOfType;
        StringBuilder localStringBuilder;
        if (this.lowerBounds.length == 0) {
            if ((this.upperBounds.length == 0) || (Object.class == this.upperBounds[0])) {
                return "?";
            }
            arrayOfType = this.upperBounds;
            localStringBuilder = new StringBuilder("? extends ");
        } else {
            arrayOfType = this.lowerBounds;
            localStringBuilder = new StringBuilder("? super ");
        }
        for (int i = 0; i < arrayOfType.length; i++) {
            if (i > 0) {
                localStringBuilder.append(" & ");
            }
            localStringBuilder.append((arrayOfType[i] instanceof Class) ? ((Class) arrayOfType[i]).getName() : arrayOfType[i].toString());
        }

        return localStringBuilder.toString();
    }
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.beans.WildcardTypeImpl
 * JD-Core Version:    0.6.2
 */