/*     */ package javax.management.openmbean;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.HashSet;
/*     */ import java.util.Objects;
/*     */ import javax.management.Descriptor;
/*     */ import javax.management.MBeanAttributeInfo;
/*     */ import javax.management.MBeanConstructorInfo;
/*     */ import javax.management.MBeanInfo;
/*     */ import javax.management.MBeanNotificationInfo;
/*     */ import javax.management.MBeanOperationInfo;
/*     */ 
/*     */ public class OpenMBeanInfoSupport extends MBeanInfo
/*     */   implements OpenMBeanInfo
/*     */ {
/*     */   static final long serialVersionUID = 4349395935420511492L;
/*  64 */   private transient Integer myHashCode = null;
/*  65 */   private transient String myToString = null;
/*     */ 
/*     */   public OpenMBeanInfoSupport(String paramString1, String paramString2, OpenMBeanAttributeInfo[] paramArrayOfOpenMBeanAttributeInfo, OpenMBeanConstructorInfo[] paramArrayOfOpenMBeanConstructorInfo, OpenMBeanOperationInfo[] paramArrayOfOpenMBeanOperationInfo, MBeanNotificationInfo[] paramArrayOfMBeanNotificationInfo)
/*     */   {
/* 119 */     this(paramString1, paramString2, paramArrayOfOpenMBeanAttributeInfo, paramArrayOfOpenMBeanConstructorInfo, paramArrayOfOpenMBeanOperationInfo, paramArrayOfMBeanNotificationInfo, (Descriptor)null);
/*     */   }
/*     */ 
/*     */   public OpenMBeanInfoSupport(String paramString1, String paramString2, OpenMBeanAttributeInfo[] paramArrayOfOpenMBeanAttributeInfo, OpenMBeanConstructorInfo[] paramArrayOfOpenMBeanConstructorInfo, OpenMBeanOperationInfo[] paramArrayOfOpenMBeanOperationInfo, MBeanNotificationInfo[] paramArrayOfMBeanNotificationInfo, Descriptor paramDescriptor)
/*     */   {
/* 181 */     super(paramString1, paramString2, attributeArray(paramArrayOfOpenMBeanAttributeInfo), constructorArray(paramArrayOfOpenMBeanConstructorInfo), operationArray(paramArrayOfOpenMBeanOperationInfo), paramArrayOfMBeanNotificationInfo == null ? null : (MBeanNotificationInfo[])paramArrayOfMBeanNotificationInfo.clone(), paramDescriptor);
/*     */   }
/*     */ 
/*     */   private static MBeanAttributeInfo[] attributeArray(OpenMBeanAttributeInfo[] paramArrayOfOpenMBeanAttributeInfo)
/*     */   {
/* 193 */     if (paramArrayOfOpenMBeanAttributeInfo == null)
/* 194 */       return null;
/* 195 */     MBeanAttributeInfo[] arrayOfMBeanAttributeInfo = new MBeanAttributeInfo[paramArrayOfOpenMBeanAttributeInfo.length];
/* 196 */     System.arraycopy(paramArrayOfOpenMBeanAttributeInfo, 0, arrayOfMBeanAttributeInfo, 0, paramArrayOfOpenMBeanAttributeInfo.length);
/*     */ 
/* 198 */     return arrayOfMBeanAttributeInfo;
/*     */   }
/*     */ 
/*     */   private static MBeanConstructorInfo[] constructorArray(OpenMBeanConstructorInfo[] paramArrayOfOpenMBeanConstructorInfo)
/*     */   {
/* 203 */     if (paramArrayOfOpenMBeanConstructorInfo == null)
/* 204 */       return null;
/* 205 */     MBeanConstructorInfo[] arrayOfMBeanConstructorInfo = new MBeanConstructorInfo[paramArrayOfOpenMBeanConstructorInfo.length];
/* 206 */     System.arraycopy(paramArrayOfOpenMBeanConstructorInfo, 0, arrayOfMBeanConstructorInfo, 0, paramArrayOfOpenMBeanConstructorInfo.length);
/*     */ 
/* 208 */     return arrayOfMBeanConstructorInfo;
/*     */   }
/*     */ 
/*     */   private static MBeanOperationInfo[] operationArray(OpenMBeanOperationInfo[] paramArrayOfOpenMBeanOperationInfo)
/*     */   {
/* 213 */     if (paramArrayOfOpenMBeanOperationInfo == null)
/* 214 */       return null;
/* 215 */     MBeanOperationInfo[] arrayOfMBeanOperationInfo = new MBeanOperationInfo[paramArrayOfOpenMBeanOperationInfo.length];
/* 216 */     System.arraycopy(paramArrayOfOpenMBeanOperationInfo, 0, arrayOfMBeanOperationInfo, 0, paramArrayOfOpenMBeanOperationInfo.length);
/* 217 */     return arrayOfMBeanOperationInfo;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 255 */     if (paramObject == null) {
/* 256 */       return false;
/*     */     }
/*     */ 
/*     */     OpenMBeanInfo localOpenMBeanInfo;
/*     */     try
/*     */     {
/* 263 */       localOpenMBeanInfo = (OpenMBeanInfo)paramObject;
/*     */     } catch (ClassCastException localClassCastException) {
/* 265 */       return false;
/*     */     }
/*     */ 
/* 273 */     if (!Objects.equals(getClassName(), localOpenMBeanInfo.getClassName())) {
/* 274 */       return false;
/*     */     }
/*     */ 
/* 279 */     if (!sameArrayContents(getAttributes(), localOpenMBeanInfo.getAttributes())) {
/* 280 */       return false;
/*     */     }
/*     */ 
/* 284 */     if (!sameArrayContents(getConstructors(), localOpenMBeanInfo.getConstructors())) {
/* 285 */       return false;
/*     */     }
/*     */ 
/* 289 */     if (!sameArrayContents(getOperations(), localOpenMBeanInfo.getOperations()))
/*     */     {
/* 291 */       return false;
/*     */     }
/*     */ 
/* 295 */     if (!sameArrayContents(getNotifications(), localOpenMBeanInfo.getNotifications())) {
/* 296 */       return false;
/*     */     }
/*     */ 
/* 300 */     return true;
/*     */   }
/*     */ 
/*     */   private static <T> boolean sameArrayContents(T[] paramArrayOfT1, T[] paramArrayOfT2) {
/* 304 */     return new HashSet(Arrays.asList(paramArrayOfT1)).equals(new HashSet(Arrays.asList(paramArrayOfT2)));
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 345 */     if (this.myHashCode == null) {
/* 346 */       int i = 0;
/* 347 */       if (getClassName() != null) {
/* 348 */         i += getClassName().hashCode();
/*     */       }
/* 350 */       i += arraySetHash(getAttributes());
/* 351 */       i += arraySetHash(getConstructors());
/* 352 */       i += arraySetHash(getOperations());
/* 353 */       i += arraySetHash(getNotifications());
/* 354 */       this.myHashCode = Integer.valueOf(i);
/*     */     }
/*     */ 
/* 359 */     return this.myHashCode.intValue();
/*     */   }
/*     */ 
/*     */   private static <T> int arraySetHash(T[] paramArrayOfT) {
/* 363 */     return new HashSet(Arrays.asList(paramArrayOfT)).hashCode();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 391 */     if (this.myToString == null) {
/* 392 */       this.myToString = (getClass().getName() + "(mbean_class_name=" + getClassName() + ",attributes=" + Arrays.asList(getAttributes()).toString() + ",constructors=" + Arrays.asList(getConstructors()).toString() + ",operations=" + Arrays.asList(getOperations()).toString() + ",notifications=" + Arrays.asList(getNotifications()).toString() + ",descriptor=" + getDescriptor() + ")");
/*     */     }
/*     */ 
/* 413 */     return this.myToString;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.openmbean.OpenMBeanInfoSupport
 * JD-Core Version:    0.6.2
 */