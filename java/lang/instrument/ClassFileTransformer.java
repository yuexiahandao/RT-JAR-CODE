package java.lang.instrument;

import java.security.ProtectionDomain;

public abstract interface ClassFileTransformer {
    public abstract byte[] transform(ClassLoader paramClassLoader, String paramString, Class<?> paramClass, ProtectionDomain paramProtectionDomain, byte[] paramArrayOfByte)
            throws IllegalClassFormatException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.instrument.ClassFileTransformer
 * JD-Core Version:    0.6.2
 */