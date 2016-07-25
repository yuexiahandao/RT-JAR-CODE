package com.sun.beans.finder;

final class SignatureException extends RuntimeException {
    SignatureException(Throwable paramThrowable) {
        super(paramThrowable);
    }

    NoSuchMethodException toNoSuchMethodException(String paramString) {
        Throwable localThrowable = getCause();
        if ((localThrowable instanceof NoSuchMethodException)) {
            return (NoSuchMethodException) localThrowable;
        }
        NoSuchMethodException localNoSuchMethodException = new NoSuchMethodException(paramString);
        localNoSuchMethodException.initCause(localThrowable);
        return localNoSuchMethodException;
    }
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.beans.finder.SignatureException
 * JD-Core Version:    0.6.2
 */