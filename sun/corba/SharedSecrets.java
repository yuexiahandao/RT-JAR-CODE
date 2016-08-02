package sun.corba;

import com.sun.corba.se.impl.io.ValueUtility;
import sun.misc.Unsafe;

public class SharedSecrets {
    private static final Unsafe unsafe = Unsafe.getUnsafe();
    private static JavaCorbaAccess javaCorbaAccess;

    public static JavaCorbaAccess getJavaCorbaAccess() {
        if (javaCorbaAccess == null) {
            unsafe.ensureClassInitialized(ValueUtility.class);
        }
        return javaCorbaAccess;
    }

    public static void setJavaCorbaAccess(JavaCorbaAccess paramJavaCorbaAccess) {
        javaCorbaAccess = paramJavaCorbaAccess;
    }
}