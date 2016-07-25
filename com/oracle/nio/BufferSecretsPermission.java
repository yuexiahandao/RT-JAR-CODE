package com.oracle.nio;

import java.security.BasicPermission;

public final class BufferSecretsPermission extends BasicPermission {
    private static final long serialVersionUID = 0L;

    public BufferSecretsPermission(String paramString) {
        super(paramString);
        if (!paramString.equals("access"))
            throw new IllegalArgumentException();
    }
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.oracle.nio.BufferSecretsPermission
 * JD-Core Version:    0.6.2
 */