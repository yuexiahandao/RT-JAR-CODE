/*     */ package sun.misc;
/*     */ 
/*     */ public class MessageUtils
/*     */ {
/*     */   public static String subst(String paramString1, String paramString2)
/*     */   {
/*  42 */     String[] arrayOfString = { paramString2 };
/*  43 */     return subst(paramString1, arrayOfString);
/*     */   }
/*     */ 
/*     */   public static String subst(String paramString1, String paramString2, String paramString3) {
/*  47 */     String[] arrayOfString = { paramString2, paramString3 };
/*  48 */     return subst(paramString1, arrayOfString);
/*     */   }
/*     */ 
/*     */   public static String subst(String paramString1, String paramString2, String paramString3, String paramString4)
/*     */   {
/*  53 */     String[] arrayOfString = { paramString2, paramString3, paramString4 };
/*  54 */     return subst(paramString1, arrayOfString);
/*     */   }
/*     */ 
/*     */   public static String subst(String paramString, String[] paramArrayOfString) {
/*  58 */     StringBuffer localStringBuffer = new StringBuffer();
/*  59 */     int i = paramString.length();
/*  60 */     for (int j = 0; (j >= 0) && (j < i); j++) {
/*  61 */       char c = paramString.charAt(j);
/*  62 */       if (c == '%') {
/*  63 */         if (j != i) {
/*  64 */           int k = Character.digit(paramString.charAt(j + 1), 10);
/*  65 */           if (k == -1) {
/*  66 */             localStringBuffer.append(paramString.charAt(j + 1));
/*  67 */             j++;
/*  68 */           } else if (k < paramArrayOfString.length) {
/*  69 */             localStringBuffer.append(paramArrayOfString[k]);
/*  70 */             j++;
/*     */           }
/*     */         }
/*     */       }
/*  74 */       else localStringBuffer.append(c);
/*     */     }
/*     */ 
/*  77 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   public static String substProp(String paramString1, String paramString2) {
/*  81 */     return subst(System.getProperty(paramString1), paramString2);
/*     */   }
/*     */ 
/*     */   public static String substProp(String paramString1, String paramString2, String paramString3) {
/*  85 */     return subst(System.getProperty(paramString1), paramString2, paramString3);
/*     */   }
/*     */ 
/*     */   public static String substProp(String paramString1, String paramString2, String paramString3, String paramString4)
/*     */   {
/*  90 */     return subst(System.getProperty(paramString1), paramString2, paramString3, paramString4);
/*     */   }
/*     */ 
/*     */   public static native void toStderr(String paramString);
/*     */ 
/*     */   public static native void toStdout(String paramString);
/*     */ 
/*     */   public static void err(String paramString)
/*     */   {
/* 111 */     toStderr(paramString + "\n");
/*     */   }
/*     */ 
/*     */   public static void out(String paramString) {
/* 115 */     toStdout(paramString + "\n");
/*     */   }
/*     */ 
/*     */   public static void where()
/*     */   {
/* 121 */     Throwable localThrowable = new Throwable();
/* 122 */     StackTraceElement[] arrayOfStackTraceElement = localThrowable.getStackTrace();
/* 123 */     for (int i = 1; i < arrayOfStackTraceElement.length; i++)
/* 124 */       toStderr("\t" + arrayOfStackTraceElement[i].toString() + "\n");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.misc.MessageUtils
 * JD-Core Version:    0.6.2
 */