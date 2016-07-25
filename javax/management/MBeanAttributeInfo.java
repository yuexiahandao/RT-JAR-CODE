/*     */ package javax.management;
/*     */ 
/*     */ import com.sun.jmx.mbeanserver.GetPropertyAction;
/*     */ import com.sun.jmx.mbeanserver.Introspector;
/*     */ import java.lang.reflect.Method;
/*     */ import java.security.AccessController;
/*     */ import java.util.Objects;
/*     */ 
/*     */ public class MBeanAttributeInfo extends MBeanFeatureInfo
/*     */   implements Cloneable
/*     */ {
/*  67 */   private static final long serialVersionUID = l;
/*     */ 
/*  70 */   static final MBeanAttributeInfo[] NO_ATTRIBUTES = new MBeanAttributeInfo[0];
/*     */   private final String attributeType;
/*     */   private final boolean isWrite;
/*     */   private final boolean isRead;
/*     */   private final boolean is;
/*     */ 
/*     */   public MBeanAttributeInfo(String paramString1, String paramString2, String paramString3, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
/*     */   {
/* 116 */     this(paramString1, paramString2, paramString3, paramBoolean1, paramBoolean2, paramBoolean3, (Descriptor)null);
/*     */   }
/*     */ 
/*     */   public MBeanAttributeInfo(String paramString1, String paramString2, String paramString3, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, Descriptor paramDescriptor)
/*     */   {
/* 147 */     super(paramString1, paramString3, paramDescriptor);
/*     */ 
/* 149 */     this.attributeType = paramString2;
/* 150 */     this.isRead = paramBoolean1;
/* 151 */     this.isWrite = paramBoolean2;
/* 152 */     if ((paramBoolean3) && (!paramBoolean1)) {
/* 153 */       throw new IllegalArgumentException("Cannot have an \"is\" getter for a non-readable attribute");
/*     */     }
/*     */ 
/* 156 */     if ((paramBoolean3) && (!paramString2.equals("java.lang.Boolean")) && (!paramString2.equals("boolean")))
/*     */     {
/* 158 */       throw new IllegalArgumentException("Cannot have an \"is\" getter for a non-boolean attribute");
/*     */     }
/*     */ 
/* 161 */     this.is = paramBoolean3;
/*     */   }
/*     */ 
/*     */   public MBeanAttributeInfo(String paramString1, String paramString2, Method paramMethod1, Method paramMethod2)
/*     */     throws IntrospectionException
/*     */   {
/* 184 */     this(paramString1, attributeType(paramMethod1, paramMethod2), paramString2, paramMethod1 != null, paramMethod2 != null, isIs(paramMethod1), ImmutableDescriptor.union(new Descriptor[] { Introspector.descriptorForElement(paramMethod1), Introspector.descriptorForElement(paramMethod2) }));
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/*     */     try
/*     */     {
/* 206 */       return super.clone();
/*     */     } catch (CloneNotSupportedException localCloneNotSupportedException) {
/*     */     }
/* 209 */     return null;
/*     */   }
/*     */ 
/*     */   public String getType()
/*     */   {
/* 219 */     return this.attributeType;
/*     */   }
/*     */ 
/*     */   public boolean isReadable()
/*     */   {
/* 228 */     return this.isRead;
/*     */   }
/*     */ 
/*     */   public boolean isWritable()
/*     */   {
/* 237 */     return this.isWrite;
/*     */   }
/*     */ 
/*     */   public boolean isIs()
/*     */   {
/* 246 */     return this.is;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/*     */     String str;
/* 251 */     if (isReadable()) {
/* 252 */       if (isWritable())
/* 253 */         str = "read/write";
/*     */       else
/* 255 */         str = "read-only";
/* 256 */     } else if (isWritable())
/* 257 */       str = "write-only";
/*     */     else {
/* 259 */       str = "no-access";
/*     */     }
/* 261 */     return getClass().getName() + "[" + "description=" + getDescription() + ", " + "name=" + getName() + ", " + "type=" + getType() + ", " + str + ", " + (isIs() ? "isIs, " : "") + "descriptor=" + getDescriptor() + "]";
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 284 */     if (paramObject == this)
/* 285 */       return true;
/* 286 */     if (!(paramObject instanceof MBeanAttributeInfo))
/* 287 */       return false;
/* 288 */     MBeanAttributeInfo localMBeanAttributeInfo = (MBeanAttributeInfo)paramObject;
/* 289 */     return (Objects.equals(localMBeanAttributeInfo.getName(), getName())) && (Objects.equals(localMBeanAttributeInfo.getType(), getType())) && (Objects.equals(localMBeanAttributeInfo.getDescription(), getDescription())) && (Objects.equals(localMBeanAttributeInfo.getDescriptor(), getDescriptor())) && (localMBeanAttributeInfo.isReadable() == isReadable()) && (localMBeanAttributeInfo.isWritable() == isWritable()) && (localMBeanAttributeInfo.isIs() == isIs());
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 305 */     return Objects.hash(new Object[] { getName(), getType() });
/*     */   }
/*     */ 
/*     */   private static boolean isIs(Method paramMethod) {
/* 309 */     return (paramMethod != null) && (paramMethod.getName().startsWith("is")) && ((paramMethod.getReturnType().equals(Boolean.TYPE)) || (paramMethod.getReturnType().equals(Boolean.class)));
/*     */   }
/*     */ 
/*     */   private static String attributeType(Method paramMethod1, Method paramMethod2)
/*     */     throws IntrospectionException
/*     */   {
/* 320 */     Class localClass = null;
/*     */ 
/* 322 */     if (paramMethod1 != null) {
/* 323 */       if (paramMethod1.getParameterTypes().length != 0) {
/* 324 */         throw new IntrospectionException("bad getter arg count");
/*     */       }
/* 326 */       localClass = paramMethod1.getReturnType();
/* 327 */       if (localClass == Void.TYPE) {
/* 328 */         throw new IntrospectionException("getter " + paramMethod1.getName() + " returns void");
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 333 */     if (paramMethod2 != null) {
/* 334 */       Class[] arrayOfClass = paramMethod2.getParameterTypes();
/* 335 */       if (arrayOfClass.length != 1) {
/* 336 */         throw new IntrospectionException("bad setter arg count");
/*     */       }
/* 338 */       if (localClass == null)
/* 339 */         localClass = arrayOfClass[0];
/* 340 */       else if (localClass != arrayOfClass[0]) {
/* 341 */         throw new IntrospectionException("type mismatch between getter and setter");
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 346 */     if (localClass == null) {
/* 347 */       throw new IntrospectionException("getter and setter cannot both be null");
/*     */     }
/*     */ 
/* 351 */     return localClass.getName();
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  58 */     long l = 8644704819898565848L;
/*     */     try {
/*  60 */       GetPropertyAction localGetPropertyAction = new GetPropertyAction("jmx.serial.form");
/*  61 */       String str = (String)AccessController.doPrivileged(localGetPropertyAction);
/*  62 */       if ("1.0".equals(str))
/*  63 */         l = 7043855487133450673L;
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.MBeanAttributeInfo
 * JD-Core Version:    0.6.2
 */