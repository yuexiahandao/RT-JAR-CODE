/*     */ package javax.management.remote.rmi;
/*     */ 
/*     */ import java.security.ProtectionDomain;
/*     */ 
/*     */ class NoCallStackClassLoader extends ClassLoader
/*     */ {
/*     */   private final String[] classNames;
/*     */   private final byte[][] byteCodes;
/*     */   private final String[] referencedClassNames;
/*     */   private final ClassLoader referencedClassLoader;
/*     */   private final ProtectionDomain protectionDomain;
/*     */ 
/*     */   public NoCallStackClassLoader(String paramString, byte[] paramArrayOfByte, String[] paramArrayOfString, ClassLoader paramClassLoader, ProtectionDomain paramProtectionDomain)
/*     */   {
/*  85 */     this(new String[] { paramString }, new byte[][] { paramArrayOfByte }, paramArrayOfString, paramClassLoader, paramProtectionDomain);
/*     */   }
/*     */ 
/*     */   public NoCallStackClassLoader(String[] paramArrayOfString1, byte[][] paramArrayOfByte, String[] paramArrayOfString2, ClassLoader paramClassLoader, ProtectionDomain paramProtectionDomain)
/*     */   {
/*  94 */     super(null);
/*     */ 
/*  97 */     if ((paramArrayOfString1 == null) || (paramArrayOfString1.length == 0) || (paramArrayOfByte == null) || (paramArrayOfString1.length != paramArrayOfByte.length) || (paramArrayOfString2 == null) || (paramProtectionDomain == null))
/*     */     {
/* 100 */       throw new IllegalArgumentException();
/* 101 */     }for (int i = 0; i < paramArrayOfString1.length; i++) {
/* 102 */       if ((paramArrayOfString1[i] == null) || (paramArrayOfByte[i] == null))
/* 103 */         throw new IllegalArgumentException();
/*     */     }
/* 105 */     for (i = 0; i < paramArrayOfString2.length; i++) {
/* 106 */       if (paramArrayOfString2[i] == null) {
/* 107 */         throw new IllegalArgumentException();
/*     */       }
/*     */     }
/* 110 */     this.classNames = paramArrayOfString1;
/* 111 */     this.byteCodes = paramArrayOfByte;
/* 112 */     this.referencedClassNames = paramArrayOfString2;
/* 113 */     this.referencedClassLoader = paramClassLoader;
/* 114 */     this.protectionDomain = paramProtectionDomain;
/*     */   }
/*     */ 
/*     */   protected Class<?> findClass(String paramString)
/*     */     throws ClassNotFoundException
/*     */   {
/* 123 */     for (int i = 0; i < this.classNames.length; i++) {
/* 124 */       if (paramString.equals(this.classNames[i])) {
/* 125 */         return defineClass(this.classNames[i], this.byteCodes[i], 0, this.byteCodes[i].length, this.protectionDomain);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 134 */     if (this.referencedClassLoader != null) {
/* 135 */       for (i = 0; i < this.referencedClassNames.length; i++) {
/* 136 */         if (paramString.equals(this.referencedClassNames[i])) {
/* 137 */           return this.referencedClassLoader.loadClass(paramString);
/*     */         }
/*     */       }
/*     */     }
/* 141 */     throw new ClassNotFoundException(paramString);
/*     */   }
/*     */ 
/*     */   public static byte[] stringToBytes(String paramString)
/*     */   {
/* 175 */     int i = paramString.length();
/* 176 */     byte[] arrayOfByte = new byte[i];
/* 177 */     for (int j = 0; j < i; j++)
/* 178 */       arrayOfByte[j] = ((byte)paramString.charAt(j));
/* 179 */     return arrayOfByte;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.remote.rmi.NoCallStackClassLoader
 * JD-Core Version:    0.6.2
 */