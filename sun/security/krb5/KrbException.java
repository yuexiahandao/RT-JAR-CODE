/*     */ package sun.security.krb5;
/*     */ 
/*     */ import sun.security.krb5.internal.KRBError;
/*     */ import sun.security.krb5.internal.Krb5;
/*     */ 
/*     */ public class KrbException extends Exception
/*     */ {
/*     */   private static final long serialVersionUID = -4993302876451928596L;
/*     */   private int returnCode;
/*     */   private KRBError error;
/*     */ 
/*     */   public KrbException(String paramString)
/*     */   {
/*  45 */     super(paramString);
/*     */   }
/*     */ 
/*     */   public KrbException(int paramInt) {
/*  49 */     this.returnCode = paramInt;
/*     */   }
/*     */ 
/*     */   public KrbException(int paramInt, String paramString) {
/*  53 */     this(paramString);
/*  54 */     this.returnCode = paramInt;
/*     */   }
/*     */ 
/*     */   public KrbException(KRBError paramKRBError) {
/*  58 */     this.returnCode = paramKRBError.getErrorCode();
/*  59 */     this.error = paramKRBError;
/*     */   }
/*     */ 
/*     */   public KrbException(KRBError paramKRBError, String paramString) {
/*  63 */     this(paramString);
/*  64 */     this.returnCode = paramKRBError.getErrorCode();
/*  65 */     this.error = paramKRBError;
/*     */   }
/*     */ 
/*     */   public KRBError getError() {
/*  69 */     return this.error;
/*     */   }
/*     */ 
/*     */   public int returnCode()
/*     */   {
/*  74 */     return this.returnCode;
/*     */   }
/*     */ 
/*     */   public String returnCodeSymbol() {
/*  78 */     return returnCodeSymbol(this.returnCode);
/*     */   }
/*     */ 
/*     */   public static String returnCodeSymbol(int paramInt) {
/*  82 */     return "not yet implemented";
/*     */   }
/*     */ 
/*     */   public String returnCodeMessage() {
/*  86 */     return Krb5.getErrorMessage(this.returnCode);
/*     */   }
/*     */ 
/*     */   public static String errorMessage(int paramInt) {
/*  90 */     return Krb5.getErrorMessage(paramInt);
/*     */   }
/*     */ 
/*     */   public String krbErrorMessage()
/*     */   {
/*  95 */     StringBuffer localStringBuffer = new StringBuffer("krb_error " + this.returnCode);
/*  96 */     String str = getMessage();
/*  97 */     if (str != null) {
/*  98 */       localStringBuffer.append(" ");
/*  99 */       localStringBuffer.append(str);
/*     */     }
/* 101 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   public String getMessage()
/*     */   {
/* 111 */     StringBuffer localStringBuffer = new StringBuffer();
/* 112 */     int i = returnCode();
/* 113 */     if (i != 0) {
/* 114 */       localStringBuffer.append(returnCodeMessage());
/* 115 */       localStringBuffer.append(" (").append(returnCode()).append(')');
/*     */     }
/* 117 */     String str = super.getMessage();
/* 118 */     if ((str != null) && (str.length() != 0)) {
/* 119 */       if (i != 0)
/* 120 */         localStringBuffer.append(" - ");
/* 121 */       localStringBuffer.append(str);
/*     */     }
/* 123 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 127 */     return "KrbException: " + getMessage();
/*     */   }
/*     */ 
/*     */   public int hashCode() {
/* 131 */     int i = 17;
/* 132 */     i = 37 * i + this.returnCode;
/* 133 */     if (this.error != null) {
/* 134 */       i = 37 * i + this.error.hashCode();
/*     */     }
/* 136 */     return i;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject) {
/* 140 */     if (this == paramObject) {
/* 141 */       return true;
/*     */     }
/*     */ 
/* 144 */     if (!(paramObject instanceof KrbException)) {
/* 145 */       return false;
/*     */     }
/*     */ 
/* 148 */     KrbException localKrbException = (KrbException)paramObject;
/* 149 */     if (this.returnCode != localKrbException.returnCode) {
/* 150 */       return false;
/*     */     }
/* 152 */     return this.error == null ? false : localKrbException.error == null ? true : this.error.equals(localKrbException.error);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.KrbException
 * JD-Core Version:    0.6.2
 */