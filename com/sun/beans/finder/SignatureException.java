/*    */ package com.sun.beans.finder;
/*    */ 
/*    */ final class SignatureException extends RuntimeException
/*    */ {
/*    */   SignatureException(Throwable paramThrowable)
/*    */   {
/* 29 */     super(paramThrowable);
/*    */   }
/*    */ 
/*    */   NoSuchMethodException toNoSuchMethodException(String paramString) {
/* 33 */     Throwable localThrowable = getCause();
/* 34 */     if ((localThrowable instanceof NoSuchMethodException)) {
/* 35 */       return (NoSuchMethodException)localThrowable;
/*    */     }
/* 37 */     NoSuchMethodException localNoSuchMethodException = new NoSuchMethodException(paramString);
/* 38 */     localNoSuchMethodException.initCause(localThrowable);
/* 39 */     return localNoSuchMethodException;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.beans.finder.SignatureException
 * JD-Core Version:    0.6.2
 */