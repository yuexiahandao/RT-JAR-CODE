package sun.reflect;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import sun.misc.VM;

/**
 * 用于反射的类
 */
public class Reflection {
    private static volatile Map<Class, String[]> fieldFilterMap = localHashMap;

    private static volatile Map<Class, String[]> methodFilterMap = new HashMap();

    @CallerSensitive
    /**
     * 本地方法，获得调用的类
     */
    public static native Class getCallerClass();

    @Deprecated
    @CallerSensitive
    public static Class getCallerClass(int paramInt) {
        if (VM.allowGetCallerClass()) {
            return getCallerClass0(paramInt + 1);
        }
        throw new UnsupportedOperationException("This method has been disabled by a system property");
    }

    @CallerSensitive
    private static native Class getCallerClass0(int paramInt);

    private static native int getClassAccessFlags(Class paramClass);

    public static boolean quickCheckMemberAccess(Class paramClass, int paramInt) {
        return Modifier.isPublic(getClassAccessFlags(paramClass) & paramInt);
    }

    public static void ensureMemberAccess(Class paramClass1, Class paramClass2, Object paramObject, int paramInt)
            throws IllegalAccessException {
        if ((paramClass1 == null) || (paramClass2 == null)) {
            throw new InternalError();
        }

        if (!verifyMemberAccess(paramClass1, paramClass2, paramObject, paramInt))
            throw new IllegalAccessException("Class " + paramClass1.getName() + " can not access a member of class " + paramClass2.getName() + " with modifiers \"" + Modifier.toString(paramInt) + "\"");
    }

    public static boolean verifyMemberAccess(Class paramClass1, Class paramClass2, Object paramObject, int paramInt) {
        int i = 0;
        boolean bool = false;

        if (paramClass1 == paramClass2) {
            return true;
        }

        if (!Modifier.isPublic(getClassAccessFlags(paramClass2))) {
            bool = isSameClassPackage(paramClass1, paramClass2);
            i = 1;
            if (!bool) {
                return false;
            }

        }

        if (Modifier.isPublic(paramInt)) {
            return true;
        }

        int j = 0;

        if (Modifier.isProtected(paramInt)) {
            if (isSubclassOf(paramClass1, paramClass2)) {
                j = 1;
            }
        }

        if ((j == 0) && (!Modifier.isPrivate(paramInt))) {
            if (i == 0) {
                bool = isSameClassPackage(paramClass1, paramClass2);

                i = 1;
            }

            if (bool) {
                j = 1;
            }
        }

        if (j == 0) {
            return false;
        }

        if (Modifier.isProtected(paramInt)) {
            Class localClass = paramObject == null ? paramClass2 : paramObject.getClass();
            if (localClass != paramClass1) {
                if (i == 0) {
                    bool = isSameClassPackage(paramClass1, paramClass2);
                    i = 1;
                }
                if ((!bool) &&
                        (!isSubclassOf(localClass, paramClass1))) {
                    return false;
                }
            }

        }

        return true;
    }

    private static boolean isSameClassPackage(Class paramClass1, Class paramClass2) {
        return isSameClassPackage(paramClass1.getClassLoader(), paramClass1.getName(), paramClass2.getClassLoader(), paramClass2.getName());
    }

    private static boolean isSameClassPackage(ClassLoader paramClassLoader1, String paramString1, ClassLoader paramClassLoader2, String paramString2) {
        if (paramClassLoader1 != paramClassLoader2) {
            return false;
        }
        int i = paramString1.lastIndexOf('.');
        int j = paramString2.lastIndexOf('.');
        if ((i == -1) || (j == -1)) {
            return i == j;
        }
        int k = 0;
        int m = 0;

        if (paramString1.charAt(k) == '[') {
            do
                k++;
            while (paramString1.charAt(k) == '[');
            if (paramString1.charAt(k) != 'L') {
                throw new InternalError("Illegal class name " + paramString1);
            }
        }
        if (paramString2.charAt(m) == '[') {
            do
                m++;
            while (paramString2.charAt(m) == '[');
            if (paramString2.charAt(m) != 'L') {
                throw new InternalError("Illegal class name " + paramString2);
            }

        }

        int n = i - k;
        int i1 = j - m;

        if (n != i1) {
            return false;
        }
        return paramString1.regionMatches(false, k, paramString2, m, n);
    }

    static boolean isSubclassOf(Class paramClass1, Class paramClass2) {
        while (paramClass1 != null) {
            if (paramClass1 == paramClass2) {
                return true;
            }
            paramClass1 = paramClass1.getSuperclass();
        }
        return false;
    }

    public static synchronized void registerFieldsToFilter(Class paramClass, String[] paramArrayOfString) {
        fieldFilterMap = registerFilter(fieldFilterMap, paramClass, paramArrayOfString);
    }

    public static synchronized void registerMethodsToFilter(Class paramClass, String[] paramArrayOfString) {
        methodFilterMap = registerFilter(methodFilterMap, paramClass, paramArrayOfString);
    }

    private static Map<Class, String[]> registerFilter(Map<Class, String[]> paramMap, Class paramClass, String[] paramArrayOfString) {
        if (paramMap.get(paramClass) != null) {
            throw new IllegalArgumentException("Filter already registered: " + paramClass);
        }

        paramMap = new HashMap(paramMap);
        paramMap.put(paramClass, paramArrayOfString);
        return paramMap;
    }

    public static Field[] filterFields(Class paramClass, Field[] paramArrayOfField) {
        if (fieldFilterMap == null) {
            return paramArrayOfField;
        }
        return (Field[]) filter(paramArrayOfField, (String[]) fieldFilterMap.get(paramClass));
    }

    public static Method[] filterMethods(Class paramClass, Method[] paramArrayOfMethod) {
        if (methodFilterMap == null) {
            return paramArrayOfMethod;
        }
        return (Method[]) filter(paramArrayOfMethod, (String[]) methodFilterMap.get(paramClass));
    }

    private static Member[] filter(Member[] paramArrayOfMember, String[] paramArrayOfString) {
        if ((paramArrayOfString == null) || (paramArrayOfMember.length == 0)) {
            return paramArrayOfMember;
        }
        int i = 0;
        int n;
        for (Member localMember : paramArrayOfMember) {
            n = 0;
            for (Object localObject2 : paramArrayOfString) {
                if (localMember.getName() == localObject2) {
                    n = 1;
                    break;
                }
            }
            if (n == 0) {
                i++;
            }
        }
        ???=(Member[]) Array.newInstance(paramArrayOfMember[0].getClass(), i);

        ???=0;
        for (???:paramArrayOfMember){
            ???=0;
            for (String str : paramArrayOfString) {
                if (((Member) ? ??).getName() == str){
                    ???=1;
                    break;
                }
            }
            if (???==0){
                ???[( ???++)]=???;
            }
        }
        return???;
    }

    public static boolean isCallerSensitive(Method paramMethod) {
        ClassLoader localClassLoader = paramMethod.getDeclaringClass().getClassLoader();
        if ((localClassLoader == null) || (isExtClassLoader(localClassLoader))) {
            return paramMethod.isAnnotationPresent(CallerSensitive.class);
        }
        return false;
    }

    private static boolean isExtClassLoader(ClassLoader paramClassLoader) {
        ClassLoader localClassLoader = ClassLoader.getSystemClassLoader();
        while (localClassLoader != null) {
            if ((localClassLoader.getParent() == null) && (localClassLoader == paramClassLoader)) {
                return true;
            }
            localClassLoader = localClassLoader.getParent();
        }
        return false;
    }

    static {
        HashMap localHashMap = new HashMap();
        localHashMap.put(Reflection.class, new String[]{"fieldFilterMap", "methodFilterMap"});

        localHashMap.put(System.class, new String[]{"security"});
    }
}
