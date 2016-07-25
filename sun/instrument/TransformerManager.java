/*     */ package sun.instrument;
/*     */ 
/*     */ import java.lang.instrument.ClassFileTransformer;
/*     */ import java.security.ProtectionDomain;
/*     */ 
/*     */ public class TransformerManager
/*     */ {
/*     */   private TransformerInfo[] mTransformerList;
/*     */   private boolean mIsRetransformable;
/*     */ 
/*     */   TransformerManager(boolean paramBoolean)
/*     */   {
/*  85 */     this.mTransformerList = new TransformerInfo[0];
/*  86 */     this.mIsRetransformable = paramBoolean;
/*     */   }
/*     */ 
/*     */   boolean isRetransformable() {
/*  90 */     return this.mIsRetransformable;
/*     */   }
/*     */ 
/*     */   public synchronized void addTransformer(ClassFileTransformer paramClassFileTransformer)
/*     */   {
/*  95 */     TransformerInfo[] arrayOfTransformerInfo1 = this.mTransformerList;
/*  96 */     TransformerInfo[] arrayOfTransformerInfo2 = new TransformerInfo[arrayOfTransformerInfo1.length + 1];
/*  97 */     System.arraycopy(arrayOfTransformerInfo1, 0, arrayOfTransformerInfo2, 0, arrayOfTransformerInfo1.length);
/*     */ 
/* 102 */     arrayOfTransformerInfo2[arrayOfTransformerInfo1.length] = new TransformerInfo(paramClassFileTransformer);
/* 103 */     this.mTransformerList = arrayOfTransformerInfo2;
/*     */   }
/*     */ 
/*     */   public synchronized boolean removeTransformer(ClassFileTransformer paramClassFileTransformer)
/*     */   {
/* 108 */     boolean bool = false;
/* 109 */     TransformerInfo[] arrayOfTransformerInfo1 = this.mTransformerList;
/* 110 */     int i = arrayOfTransformerInfo1.length;
/* 111 */     int j = i - 1;
/*     */ 
/* 115 */     int k = 0;
/* 116 */     for (int m = i - 1; m >= 0; m--) {
/* 117 */       if (arrayOfTransformerInfo1[m].transformer() == paramClassFileTransformer) {
/* 118 */         bool = true;
/* 119 */         k = m;
/* 120 */         break;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 125 */     if (bool) {
/* 126 */       TransformerInfo[] arrayOfTransformerInfo2 = new TransformerInfo[j];
/*     */ 
/* 129 */       if (k > 0) {
/* 130 */         System.arraycopy(arrayOfTransformerInfo1, 0, arrayOfTransformerInfo2, 0, k);
/*     */       }
/*     */ 
/* 138 */       if (k < j) {
/* 139 */         System.arraycopy(arrayOfTransformerInfo1, k + 1, arrayOfTransformerInfo2, k, j - k);
/*     */       }
/*     */ 
/* 145 */       this.mTransformerList = arrayOfTransformerInfo2;
/*     */     }
/* 147 */     return bool;
/*     */   }
/*     */ 
/*     */   synchronized boolean includesTransformer(ClassFileTransformer paramClassFileTransformer)
/*     */   {
/* 152 */     for (TransformerInfo localTransformerInfo : this.mTransformerList) {
/* 153 */       if (localTransformerInfo.transformer() == paramClassFileTransformer) {
/* 154 */         return true;
/*     */       }
/*     */     }
/* 157 */     return false;
/*     */   }
/*     */ 
/*     */   private TransformerInfo[] getSnapshotTransformerList()
/*     */   {
/* 166 */     return this.mTransformerList;
/*     */   }
/*     */ 
/*     */   public byte[] transform(ClassLoader paramClassLoader, String paramString, Class paramClass, ProtectionDomain paramProtectionDomain, byte[] paramArrayOfByte)
/*     */   {
/* 175 */     int i = 0;
/*     */ 
/* 177 */     TransformerInfo[] arrayOfTransformerInfo = getSnapshotTransformerList();
/*     */ 
/* 179 */     Object localObject1 = paramArrayOfByte;
/*     */ 
/* 182 */     for (int j = 0; j < arrayOfTransformerInfo.length; j++) {
/* 183 */       TransformerInfo localTransformerInfo = arrayOfTransformerInfo[j];
/* 184 */       ClassFileTransformer localClassFileTransformer = localTransformerInfo.transformer();
/* 185 */       byte[] arrayOfByte = null;
/*     */       try
/*     */       {
/* 188 */         arrayOfByte = localClassFileTransformer.transform(paramClassLoader, paramString, paramClass, paramProtectionDomain, (byte[])localObject1);
/*     */       }
/*     */       catch (Throwable localThrowable)
/*     */       {
/*     */       }
/*     */ 
/* 199 */       if (arrayOfByte != null) {
/* 200 */         i = 1;
/* 201 */         localObject1 = arrayOfByte;
/*     */       }
/*     */     }
/*     */     Object localObject2;
/* 208 */     if (i != 0) {
/* 209 */       localObject2 = localObject1;
/*     */     }
/*     */     else {
/* 212 */       localObject2 = null;
/*     */     }
/*     */ 
/* 215 */     return localObject2;
/*     */   }
/*     */ 
/*     */   int getTransformerCount()
/*     */   {
/* 221 */     TransformerInfo[] arrayOfTransformerInfo = getSnapshotTransformerList();
/* 222 */     return arrayOfTransformerInfo.length;
/*     */   }
/*     */ 
/*     */   boolean setNativeMethodPrefix(ClassFileTransformer paramClassFileTransformer, String paramString)
/*     */   {
/* 227 */     TransformerInfo[] arrayOfTransformerInfo = getSnapshotTransformerList();
/*     */ 
/* 229 */     for (int i = 0; i < arrayOfTransformerInfo.length; i++) {
/* 230 */       TransformerInfo localTransformerInfo = arrayOfTransformerInfo[i];
/* 231 */       ClassFileTransformer localClassFileTransformer = localTransformerInfo.transformer();
/*     */ 
/* 233 */       if (localClassFileTransformer == paramClassFileTransformer) {
/* 234 */         localTransformerInfo.setPrefix(paramString);
/* 235 */         return true;
/*     */       }
/*     */     }
/* 238 */     return false;
/*     */   }
/*     */ 
/*     */   String[] getNativeMethodPrefixes()
/*     */   {
/* 244 */     TransformerInfo[] arrayOfTransformerInfo = getSnapshotTransformerList();
/* 245 */     String[] arrayOfString = new String[arrayOfTransformerInfo.length];
/*     */ 
/* 247 */     for (int i = 0; i < arrayOfTransformerInfo.length; i++) {
/* 248 */       TransformerInfo localTransformerInfo = arrayOfTransformerInfo[i];
/* 249 */       arrayOfString[i] = localTransformerInfo.getPrefix();
/*     */     }
/* 251 */     return arrayOfString;
/*     */   }
/*     */ 
/*     */   private class TransformerInfo
/*     */   {
/*     */     final ClassFileTransformer mTransformer;
/*     */     String mPrefix;
/*     */ 
/*     */     TransformerInfo(ClassFileTransformer arg2)
/*     */     {
/*     */       Object localObject;
/*  49 */       this.mTransformer = localObject;
/*  50 */       this.mPrefix = null;
/*     */     }
/*     */ 
/*     */     ClassFileTransformer transformer() {
/*  54 */       return this.mTransformer;
/*     */     }
/*     */ 
/*     */     String getPrefix() {
/*  58 */       return this.mPrefix;
/*     */     }
/*     */ 
/*     */     void setPrefix(String paramString) {
/*  62 */       this.mPrefix = paramString;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.instrument.TransformerManager
 * JD-Core Version:    0.6.2
 */