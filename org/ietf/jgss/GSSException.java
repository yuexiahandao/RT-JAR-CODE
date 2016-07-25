/*     */ package org.ietf.jgss;
/*     */ 
/*     */ public class GSSException extends Exception
/*     */ {
/*     */   private static final long serialVersionUID = -2706218945227726672L;
/*     */   public static final int BAD_BINDINGS = 1;
/*     */   public static final int BAD_MECH = 2;
/*     */   public static final int BAD_NAME = 3;
/*     */   public static final int BAD_NAMETYPE = 4;
/*     */   public static final int BAD_STATUS = 5;
/*     */   public static final int BAD_MIC = 6;
/*     */   public static final int CONTEXT_EXPIRED = 7;
/*     */   public static final int CREDENTIALS_EXPIRED = 8;
/*     */   public static final int DEFECTIVE_CREDENTIAL = 9;
/*     */   public static final int DEFECTIVE_TOKEN = 10;
/*     */   public static final int FAILURE = 11;
/*     */   public static final int NO_CONTEXT = 12;
/*     */   public static final int NO_CRED = 13;
/*     */   public static final int BAD_QOP = 14;
/*     */   public static final int UNAUTHORIZED = 15;
/*     */   public static final int UNAVAILABLE = 16;
/*     */   public static final int DUPLICATE_ELEMENT = 17;
/*     */   public static final int NAME_NOT_MN = 18;
/*     */   public static final int DUPLICATE_TOKEN = 19;
/*     */   public static final int OLD_TOKEN = 20;
/*     */   public static final int UNSEQ_TOKEN = 21;
/*     */   public static final int GAP_TOKEN = 22;
/* 178 */   private static String[] messages = { "Channel binding mismatch", "Unsupported mechanism requested", "Invalid name provided", "Name of unsupported type provided", "Invalid input status selector", "Token had invalid integrity check", "Specified security context expired", "Expired credentials detected", "Defective credential detected", "Defective token detected", "Failure unspecified at GSS-API level", "Security context init/accept not yet called or context deleted", "No valid credentials provided", "Unsupported QOP value", "Operation unauthorized", "Operation unavailable", "Duplicate credential element requested", "Name contains multi-mechanism elements", "The token was a duplicate of an earlier token", "The token's validity period has expired", "A later token has already been processed", "An expected per-message token was not received" };
/*     */   private int major;
/* 216 */   private int minor = 0;
/*     */ 
/* 223 */   private String minorMessage = null;
/*     */ 
/* 231 */   private String majorString = null;
/*     */ 
/*     */   public GSSException(int paramInt)
/*     */   {
/* 241 */     if (validateMajor(paramInt))
/* 242 */       this.major = paramInt;
/*     */     else
/* 244 */       this.major = 11;
/*     */   }
/*     */ 
/*     */   GSSException(int paramInt, String paramString)
/*     */   {
/* 256 */     if (validateMajor(paramInt))
/* 257 */       this.major = paramInt;
/*     */     else
/* 259 */       this.major = 11;
/* 260 */     this.majorString = paramString;
/*     */   }
/*     */ 
/*     */   public GSSException(int paramInt1, int paramInt2, String paramString)
/*     */   {
/* 280 */     if (validateMajor(paramInt1))
/* 281 */       this.major = paramInt1;
/*     */     else {
/* 283 */       this.major = 11;
/*     */     }
/* 285 */     this.minor = paramInt2;
/* 286 */     this.minorMessage = paramString;
/*     */   }
/*     */ 
/*     */   public int getMajor()
/*     */   {
/* 302 */     return this.major;
/*     */   }
/*     */ 
/*     */   public int getMinor()
/*     */   {
/* 316 */     return this.minor;
/*     */   }
/*     */ 
/*     */   public String getMajorString()
/*     */   {
/* 329 */     if (this.majorString != null) {
/* 330 */       return this.majorString;
/*     */     }
/* 332 */     return messages[(this.major - 1)];
/*     */   }
/*     */ 
/*     */   public String getMinorString()
/*     */   {
/* 348 */     return this.minorMessage;
/*     */   }
/*     */ 
/*     */   public void setMinor(int paramInt, String paramString)
/*     */   {
/* 363 */     this.minor = paramInt;
/* 364 */     this.minorMessage = paramString;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 375 */     return "GSSException: " + getMessage();
/*     */   }
/*     */ 
/*     */   public String getMessage()
/*     */   {
/* 385 */     if (this.minor == 0) {
/* 386 */       return getMajorString();
/*     */     }
/* 388 */     return getMajorString() + " (Mechanism level: " + getMinorString() + ")";
/*     */   }
/*     */ 
/*     */   private boolean validateMajor(int paramInt)
/*     */   {
/* 398 */     if ((paramInt > 0) && (paramInt <= messages.length)) {
/* 399 */       return true;
/*     */     }
/* 401 */     return false;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.ietf.jgss.GSSException
 * JD-Core Version:    0.6.2
 */