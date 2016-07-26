/*     */
package java.lang.invoke;
/*     */ 
/*     */

import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.ArrayList;
/*     */ import sun.invoke.WrapperInstance;
/*     */ import sun.reflect.CallerSensitive;
/*     */ import sun.reflect.Reflection;
/*     */ import sun.reflect.misc.ReflectUtil;

/*     */
/*     */ public class MethodHandleProxies
/*     */ {
    /*     */
    @CallerSensitive
/*     */ public static <T> T asInterfaceInstance(final Class<T> paramClass, MethodHandle paramMethodHandle)
/*     */ {
/* 144 */
        if ((!paramClass.isInterface()) || (!Modifier.isPublic(paramClass.getModifiers())))
/* 145 */ throw new IllegalArgumentException("not a public interface: " + paramClass.getName());
/*     */
        MethodHandle localMethodHandle1;
/* 147 */
        if (System.getSecurityManager() != null) {
/* 148 */
            localObject1 = Reflection.getCallerClass();
/* 149 */
            localObject2 = localObject1 != null ? ((Class) localObject1).getClassLoader() : null;
/* 150 */
            ReflectUtil.checkProxyPackageAccess((ClassLoader) localObject2, new Class[]{paramClass});
/* 151 */
            localMethodHandle1 = maybeBindCaller(paramMethodHandle, (Class) localObject1);
/*     */
        } else {
/* 153 */
            localMethodHandle1 = paramMethodHandle;
/*     */
        }
/* 155 */
        Object localObject1 = paramClass.getClassLoader();
/* 156 */
        if (localObject1 == null) {
/* 157 */
            localObject2 = Thread.currentThread().getContextClassLoader();
/* 158 */
            localObject1 = localObject2 != null ? localObject2 : ClassLoader.getSystemClassLoader();
/*     */
        }
/* 160 */
        Object localObject2 = getSingleNameMethods(paramClass);
/* 161 */
        if (localObject2 == null)
/* 162 */ throw new IllegalArgumentException("not a single-method interface: " + paramClass.getName());
/* 163 */
        final MethodHandle[] arrayOfMethodHandle = new MethodHandle[localObject2.length];
/*     */
        Object localObject3;
/*     */
        Object localObject4;
/* 164 */
        for (int i = 0; i < localObject2.length; i++) {
/* 165 */
            localObject3 = localObject2[i];
/* 166 */
            localObject4 = MethodType.methodType(localObject3.getReturnType(), localObject3.getParameterTypes());
/* 167 */
            MethodHandle localMethodHandle2 = localMethodHandle1.asType((MethodType) localObject4);
/* 168 */
            localMethodHandle2 = localMethodHandle2.asType(localMethodHandle2.type().changeReturnType(Object.class));
/* 169 */
            arrayOfMethodHandle[i] = localMethodHandle2.asSpreader([Ljava.lang.Object.class, ((MethodType) localObject4).parameterCount())
            ;
/*     */
        }
/* 171 */
        final InvocationHandler local1 = new InvocationHandler() {
            /*     */
            private Object getArg(String paramAnonymousString) {
/* 173 */
                if (paramAnonymousString == "getWrapperInstanceTarget") return this.val$target;
/* 174 */
                if (paramAnonymousString == "getWrapperInstanceType") return paramClass;
/* 175 */
                throw new AssertionError();
/*     */
            }

            /*     */
            public Object invoke(Object paramAnonymousObject, Method paramAnonymousMethod, Object[] paramAnonymousArrayOfObject) throws Throwable {
/* 178 */
                for (int i = 0; i < this.val$methods.length; i++) {
/* 179 */
                    if (paramAnonymousMethod.equals(this.val$methods[i]))
/* 180 */ return arrayOfMethodHandle[i].invokeExact(paramAnonymousArrayOfObject);
/*     */
                }
/* 182 */
                if (paramAnonymousMethod.getDeclaringClass() == WrapperInstance.class)
/* 183 */ return getArg(paramAnonymousMethod.getName());
/* 184 */
                if (MethodHandleProxies.isObjectMethod(paramAnonymousMethod))
/* 185 */
                    return MethodHandleProxies.callObjectMethod(paramAnonymousObject, paramAnonymousMethod, paramAnonymousArrayOfObject);
/* 186 */
                throw new InternalError("bad proxy method: " + paramAnonymousMethod);
/*     */
            }
/*     */
        };
/* 191 */
        if (System.getSecurityManager() != null)
/*     */ {
/* 194 */
            localObject4 = localObject1;
/* 195 */
            localObject3 = AccessController.doPrivileged(new PrivilegedAction() {
                /*     */
                public Object run() {
/* 197 */
                    return Proxy.newProxyInstance(this.val$loader, new Class[]{paramClass, WrapperInstance.class}, local1);
/*     */
                }
/*     */ 
/*     */
            });
/*     */
        }
/*     */
        else
/*     */ {
/* 204 */
            localObject3 = Proxy.newProxyInstance((ClassLoader) localObject1, new Class[]{paramClass, WrapperInstance.class}, local1);
/*     */
        }
/*     */ 
/* 208 */
        return paramClass.cast(localObject3);
/*     */
    }

    /*     */
/*     */
    private static MethodHandle maybeBindCaller(MethodHandle paramMethodHandle, Class<?> paramClass) {
/* 212 */
        if ((paramClass == null) || (paramClass.getClassLoader() == null)) {
/* 213 */
            return paramMethodHandle;
/*     */
        }
/* 215 */
        MethodHandle localMethodHandle = MethodHandleImpl.bindCaller(paramMethodHandle, paramClass);
/* 216 */
        if (paramMethodHandle.isVarargsCollector()) {
/* 217 */
            MethodType localMethodType = localMethodHandle.type();
/* 218 */
            int i = localMethodType.parameterCount();
/* 219 */
            return localMethodHandle.asVarargsCollector(localMethodType.parameterType(i - 1));
/*     */
        }
/* 221 */
        return localMethodHandle;
/*     */
    }

    /*     */
/*     */
    public static boolean isWrapperInstance(Object paramObject)
/*     */ {
/* 231 */
        return paramObject instanceof WrapperInstance;
/*     */
    }

    /*     */
/*     */
    private static WrapperInstance asWrapperInstance(Object paramObject) {
/*     */
        try {
/* 236 */
            if (paramObject != null)
/* 237 */ return (WrapperInstance) paramObject;
/*     */
        } catch (ClassCastException localClassCastException) {
/*     */
        }
/* 240 */
        throw new IllegalArgumentException("not a wrapper instance");
/*     */
    }

    /*     */
/*     */
    public static MethodHandle wrapperInstanceTarget(Object paramObject)
/*     */ {
/* 254 */
        return asWrapperInstance(paramObject).getWrapperInstanceTarget();
/*     */
    }

    /*     */
/*     */
    public static Class<?> wrapperInstanceType(Object paramObject)
/*     */ {
/* 267 */
        return asWrapperInstance(paramObject).getWrapperInstanceType();
/*     */
    }

    /*     */
/*     */
    private static boolean isObjectMethod(Method paramMethod)
/*     */ {
/* 272 */
        switch (paramMethod.getName()) {
/*     */
            case "toString":
/* 274 */
                return (paramMethod.getReturnType() == String.class) && (paramMethod.getParameterTypes().length == 0);
/*     */
            case "hashCode":
/* 277 */
                return (paramMethod.getReturnType() == Integer.TYPE) && (paramMethod.getParameterTypes().length == 0);
/*     */
            case "equals":
/* 280 */
                return (paramMethod.getReturnType() == Boolean.TYPE) && (paramMethod.getParameterTypes().length == 1) && (paramMethod.getParameterTypes()[0] == Object.class);
/*     */
        }
/*     */ 
/* 284 */
        return false;
/*     */
    }

    /*     */
/*     */
    private static Object callObjectMethod(Object paramObject, Method paramMethod, Object[] paramArrayOfObject)
/*     */ {
/* 289 */
        assert (isObjectMethod(paramMethod)) : paramMethod;
/* 290 */
        switch (paramMethod.getName()) {
/*     */
            case "toString":
/* 292 */
                return paramObject.getClass().getName() + "@" + Integer.toHexString(paramObject.hashCode());
/*     */
            case "hashCode":
/* 294 */
                return Integer.valueOf(System.identityHashCode(paramObject));
/*     */
            case "equals":
/* 296 */
                return Boolean.valueOf(paramObject == paramArrayOfObject[0]);
/*     */
        }
/* 298 */
        return null;
/*     */
    }

    /*     */
/*     */
    private static Method[] getSingleNameMethods(Class<?> paramClass)
/*     */ {
/* 303 */
        ArrayList localArrayList = new ArrayList();
/* 304 */
        Object localObject = null;
/* 305 */
        for (Method localMethod : paramClass.getMethods())
/* 306 */
            if ((!isObjectMethod(localMethod)) &&
/* 307 */         (Modifier.isAbstract(localMethod.getModifiers()))) {
/* 308 */
                String str = localMethod.getName();
/* 309 */
                if (localObject == null)
/* 310 */ localObject = str;
/* 311 */
                else if (!localObject.equals(str))
/* 312 */ return null;
/* 313 */
                localArrayList.add(localMethod);
/*     */
            }
/* 315 */
        if (localObject == null) return null;
/* 316 */
        return (Method[]) localArrayList.toArray(new Method[localArrayList.size()]);
/*     */
    }
/*     */
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.invoke.MethodHandleProxies
 * JD-Core Version:    0.6.2
 */