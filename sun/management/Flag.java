/*     */ package sun.management;
/*     */ 
/*     */ import com.sun.management.VMOption;
/*     */ import com.sun.management.VMOption.Origin;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ class Flag
/*     */ {
/*     */   private String name;
/*     */   private Object value;
/*     */   private VMOption.Origin origin;
/*     */   private boolean writeable;
/*     */   private boolean external;
/*     */ 
/*     */   Flag(String paramString, Object paramObject, boolean paramBoolean1, boolean paramBoolean2, VMOption.Origin paramOrigin)
/*     */   {
/*  47 */     this.name = paramString;
/*  48 */     this.value = (paramObject == null ? "" : paramObject);
/*  49 */     this.origin = paramOrigin;
/*  50 */     this.writeable = paramBoolean1;
/*  51 */     this.external = paramBoolean2;
/*     */   }
/*     */ 
/*     */   Object getValue() {
/*  55 */     return this.value;
/*     */   }
/*     */ 
/*     */   boolean isWriteable() {
/*  59 */     return this.writeable;
/*     */   }
/*     */ 
/*     */   boolean isExternal() {
/*  63 */     return this.external;
/*     */   }
/*     */ 
/*     */   VMOption getVMOption() {
/*  67 */     return new VMOption(this.name, this.value.toString(), this.writeable, this.origin);
/*     */   }
/*     */ 
/*     */   static Flag getFlag(String paramString) {
/*  71 */     String[] arrayOfString = new String[1];
/*  72 */     arrayOfString[0] = paramString;
/*     */ 
/*  74 */     List localList = getFlags(arrayOfString, 1);
/*  75 */     if (localList.isEmpty()) {
/*  76 */       return null;
/*     */     }
/*     */ 
/*  79 */     return (Flag)localList.get(0);
/*     */   }
/*     */ 
/*     */   static List<Flag> getAllFlags()
/*     */   {
/*  84 */     int i = getInternalFlagCount();
/*     */ 
/*  87 */     return getFlags(null, i);
/*     */   }
/*     */ 
/*     */   private static List<Flag> getFlags(String[] paramArrayOfString, int paramInt) {
/*  91 */     Flag[] arrayOfFlag1 = new Flag[paramInt];
/*  92 */     int i = getFlags(paramArrayOfString, arrayOfFlag1, paramInt);
/*     */ 
/*  94 */     ArrayList localArrayList = new ArrayList();
/*  95 */     for (Flag localFlag : arrayOfFlag1) {
/*  96 */       if (localFlag != null) {
/*  97 */         localArrayList.add(localFlag);
/*     */       }
/*     */     }
/* 100 */     return localArrayList;
/*     */   }
/*     */   private static native String[] getAllFlagNames();
/*     */ 
/*     */   private static native int getFlags(String[] paramArrayOfString, Flag[] paramArrayOfFlag, int paramInt);
/*     */ 
/*     */   private static native int getInternalFlagCount();
/*     */ 
/*     */   static synchronized native void setLongValue(String paramString, long paramLong);
/*     */ 
/*     */   static synchronized native void setBooleanValue(String paramString, boolean paramBoolean);
/*     */ 
/*     */   static synchronized native void setStringValue(String paramString1, String paramString2);
/*     */ 
/*     */   private static native void initialize();
/*     */ 
/*     */   static {
/* 117 */     initialize();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.Flag
 * JD-Core Version:    0.6.2
 */