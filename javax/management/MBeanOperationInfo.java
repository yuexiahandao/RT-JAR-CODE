/*     */ package javax.management;
/*     */ 
/*     */ import com.sun.jmx.mbeanserver.Introspector;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Arrays;
/*     */ import java.util.Objects;
/*     */ 
/*     */ public class MBeanOperationInfo extends MBeanFeatureInfo
/*     */   implements Cloneable
/*     */ {
/*     */   static final long serialVersionUID = -6178860474881375330L;
/*  46 */   static final MBeanOperationInfo[] NO_OPERATIONS = new MBeanOperationInfo[0];
/*     */   public static final int INFO = 0;
/*     */   public static final int ACTION = 1;
/*     */   public static final int ACTION_INFO = 2;
/*     */   public static final int UNKNOWN = 3;
/*     */   private final String type;
/*     */   private final MBeanParameterInfo[] signature;
/*     */   private final int impact;
/*     */   private final transient boolean arrayGettersSafe;
/*     */ 
/*     */   public MBeanOperationInfo(String paramString, Method paramMethod)
/*     */   {
/* 108 */     this(paramMethod.getName(), paramString, methodSignature(paramMethod), paramMethod.getReturnType().getName(), 3, Introspector.descriptorForElement(paramMethod));
/*     */   }
/*     */ 
/*     */   public MBeanOperationInfo(String paramString1, String paramString2, MBeanParameterInfo[] paramArrayOfMBeanParameterInfo, String paramString3, int paramInt)
/*     */   {
/* 134 */     this(paramString1, paramString2, paramArrayOfMBeanParameterInfo, paramString3, paramInt, (Descriptor)null);
/*     */   }
/*     */ 
/*     */   public MBeanOperationInfo(String paramString1, String paramString2, MBeanParameterInfo[] paramArrayOfMBeanParameterInfo, String paramString3, int paramInt, Descriptor paramDescriptor)
/*     */   {
/* 161 */     super(paramString1, paramString2, paramDescriptor);
/*     */ 
/* 163 */     if ((paramArrayOfMBeanParameterInfo == null) || (paramArrayOfMBeanParameterInfo.length == 0))
/* 164 */       paramArrayOfMBeanParameterInfo = MBeanParameterInfo.NO_PARAMS;
/*     */     else
/* 166 */       paramArrayOfMBeanParameterInfo = (MBeanParameterInfo[])paramArrayOfMBeanParameterInfo.clone();
/* 167 */     this.signature = paramArrayOfMBeanParameterInfo;
/* 168 */     this.type = paramString3;
/* 169 */     this.impact = paramInt;
/* 170 */     this.arrayGettersSafe = MBeanInfo.arrayGettersSafe(getClass(), MBeanOperationInfo.class);
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/*     */     try
/*     */     {
/* 188 */       return super.clone();
/*     */     } catch (CloneNotSupportedException localCloneNotSupportedException) {
/*     */     }
/* 191 */     return null;
/*     */   }
/*     */ 
/*     */   public String getReturnType()
/*     */   {
/* 201 */     return this.type;
/*     */   }
/*     */ 
/*     */   public MBeanParameterInfo[] getSignature()
/*     */   {
/* 227 */     if (this.signature == null)
/*     */     {
/* 230 */       return MBeanParameterInfo.NO_PARAMS;
/* 231 */     }if (this.signature.length == 0) {
/* 232 */       return this.signature;
/*     */     }
/* 234 */     return (MBeanParameterInfo[])this.signature.clone();
/*     */   }
/*     */ 
/*     */   private MBeanParameterInfo[] fastGetSignature() {
/* 238 */     if (this.arrayGettersSafe)
/*     */     {
/* 242 */       if (this.signature == null)
/* 243 */         return MBeanParameterInfo.NO_PARAMS;
/* 244 */       return this.signature;
/* 245 */     }return getSignature();
/*     */   }
/*     */ 
/*     */   public int getImpact()
/*     */   {
/* 255 */     return this.impact;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/*     */     String str;
/* 261 */     switch (getImpact()) { case 1:
/* 262 */       str = "action"; break;
/*     */     case 2:
/* 263 */       str = "action/info"; break;
/*     */     case 0:
/* 264 */       str = "info"; break;
/*     */     case 3:
/* 265 */       str = "unknown"; break;
/*     */     default:
/* 266 */       str = "(" + getImpact() + ")";
/*     */     }
/* 268 */     return getClass().getName() + "[" + "description=" + getDescription() + ", " + "name=" + getName() + ", " + "returnType=" + getReturnType() + ", " + "signature=" + Arrays.asList(fastGetSignature()) + ", " + "impact=" + str + ", " + "descriptor=" + getDescriptor() + "]";
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 292 */     if (paramObject == this)
/* 293 */       return true;
/* 294 */     if (!(paramObject instanceof MBeanOperationInfo))
/* 295 */       return false;
/* 296 */     MBeanOperationInfo localMBeanOperationInfo = (MBeanOperationInfo)paramObject;
/* 297 */     return (Objects.equals(localMBeanOperationInfo.getName(), getName())) && (Objects.equals(localMBeanOperationInfo.getReturnType(), getReturnType())) && (Objects.equals(localMBeanOperationInfo.getDescription(), getDescription())) && (localMBeanOperationInfo.getImpact() == getImpact()) && (Arrays.equals(localMBeanOperationInfo.fastGetSignature(), fastGetSignature())) && (Objects.equals(localMBeanOperationInfo.getDescriptor(), getDescriptor()));
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 313 */     return Objects.hash(new Object[] { getName(), getReturnType() });
/*     */   }
/*     */ 
/*     */   private static MBeanParameterInfo[] methodSignature(Method paramMethod) {
/* 317 */     Class[] arrayOfClass = paramMethod.getParameterTypes();
/* 318 */     Annotation[][] arrayOfAnnotation = paramMethod.getParameterAnnotations();
/* 319 */     return parameters(arrayOfClass, arrayOfAnnotation);
/*     */   }
/*     */ 
/*     */   static MBeanParameterInfo[] parameters(Class<?>[] paramArrayOfClass, Annotation[][] paramArrayOfAnnotation)
/*     */   {
/* 324 */     MBeanParameterInfo[] arrayOfMBeanParameterInfo = new MBeanParameterInfo[paramArrayOfClass.length];
/*     */ 
/* 326 */     assert (paramArrayOfClass.length == paramArrayOfAnnotation.length);
/*     */ 
/* 328 */     for (int i = 0; i < paramArrayOfClass.length; i++) {
/* 329 */       Descriptor localDescriptor = Introspector.descriptorForAnnotations(paramArrayOfAnnotation[i]);
/* 330 */       String str = "p" + (i + 1);
/* 331 */       arrayOfMBeanParameterInfo[i] = new MBeanParameterInfo(str, paramArrayOfClass[i].getName(), "", localDescriptor);
/*     */     }
/*     */ 
/* 335 */     return arrayOfMBeanParameterInfo;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.MBeanOperationInfo
 * JD-Core Version:    0.6.2
 */