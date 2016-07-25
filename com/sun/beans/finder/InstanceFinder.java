package com.sun.beans.finder;

class InstanceFinder<T> {
    private static final String[] EMPTY = new String[0];
    private final Class<? extends T> type;
    private final boolean allow;
    private final String suffix;
    private volatile String[] packages;

    InstanceFinder(Class<? extends T> paramClass, boolean paramBoolean, String paramString, String[] paramArrayOfString) {
        this.type = paramClass;
        this.allow = paramBoolean;
        this.suffix = paramString;
        this.packages = ((String[]) paramArrayOfString.clone());
    }

    public String[] getPackages() {
        return (String[]) this.packages.clone();
    }

    public void setPackages(String[] paramArrayOfString) {
        this.packages = ((paramArrayOfString != null) && (paramArrayOfString.length > 0) ? (String[]) paramArrayOfString.clone() : EMPTY);
    }

    public T find(Class<?> paramClass) {
        if (paramClass == null) {
            return null;
        }
        String str1 = paramClass.getName() + this.suffix;
        Object localObject = instantiate(paramClass, str1);
        if (localObject != null) {
            return localObject;
        }
        if (this.allow) {
            localObject = instantiate(paramClass, null);
            if (localObject != null) {
                return localObject;
            }
        }
        int i = str1.lastIndexOf('.') + 1;
        if (i > 0) {
            str1 = str1.substring(i);
        }
        for (String str2 : this.packages) {
            localObject = instantiate(paramClass, str2, str1);
            if (localObject != null) {
                return localObject;
            }
        }
        return null;
    }

    protected T instantiate(Class<?> paramClass, String paramString) {
        if (paramClass != null) {
            try {
                if (paramString != null) {
                    paramClass = ClassFinder.findClass(paramString, paramClass.getClassLoader());
                }
                if (this.type.isAssignableFrom(paramClass)) {
                    return paramClass.newInstance();
                }
            } catch (Exception localException) {
            }
        }
        return null;
    }

    protected T instantiate(Class<?> paramClass, String paramString1, String paramString2) {
        return instantiate(paramClass, paramString1 + '.' + paramString2);
    }
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.beans.finder.InstanceFinder
 * JD-Core Version:    0.6.2
 */