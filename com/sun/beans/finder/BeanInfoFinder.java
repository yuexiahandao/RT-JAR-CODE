package com.sun.beans.finder;

import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

public final class BeanInfoFinder extends InstanceFinder<BeanInfo> {
    private static final String DEFAULT = "sun.beans.infos";
    private static final String DEFAULT_NEW = "com.sun.beans.infos";

    public BeanInfoFinder() {
        super(BeanInfo.class, true, "BeanInfo", new String[]{"sun.beans.infos"});
    }

    private static boolean isValid(Class<?> paramClass, Method paramMethod) {
        return (paramMethod != null) && (paramMethod.getDeclaringClass().isAssignableFrom(paramClass));
    }

    protected BeanInfo instantiate(Class<?> paramClass, String paramString1, String paramString2) {
        if ("sun.beans.infos".equals(paramString1)) {
            paramString1 = "com.sun.beans.infos";
        }

        BeanInfo localBeanInfo = (!"com.sun.beans.infos".equals(paramString1)) || ("ComponentBeanInfo".equals(paramString2)) ? (BeanInfo) super.instantiate(paramClass, paramString1, paramString2) : null;

        if (localBeanInfo != null) {
            BeanDescriptor localBeanDescriptor = localBeanInfo.getBeanDescriptor();
            if (localBeanDescriptor != null) {
                if (paramClass.equals(localBeanDescriptor.getBeanClass()))
                    return localBeanInfo;
            } else {
                PropertyDescriptor[] arrayOfPropertyDescriptor = localBeanInfo.getPropertyDescriptors();
                Method localMethod;
                if (arrayOfPropertyDescriptor != null) {
                    for (Object localObject3 : arrayOfPropertyDescriptor) {
                        localMethod = localObject3.getReadMethod();
                        if (localMethod == null) {
                            localMethod = localObject3.getWriteMethod();
                        }
                        if (isValid(paramClass, localMethod))
                            return localBeanInfo;
                    }
                } else {
                    ???=localBeanInfo.getMethodDescriptors();
                    if (???!=null){
                        for (localMethod:???){
                            if (isValid(paramClass, localMethod.getMethod())) {
                                return localBeanInfo;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.beans.finder.BeanInfoFinder
 * JD-Core Version:    0.6.2
 */