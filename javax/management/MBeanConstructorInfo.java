/*     */ package javax.management;
/*     */ 
/*     */ import com.sun.jmx.mbeanserver.Introspector;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.util.Arrays;
/*     */ import java.util.Objects;
/*     */ 
/*     */ public class MBeanConstructorInfo extends MBeanFeatureInfo
/*     */   implements Cloneable
/*     */ {
/*     */   static final long serialVersionUID = 4433990064191844427L;
/*  46 */   static final MBeanConstructorInfo[] NO_CONSTRUCTORS = new MBeanConstructorInfo[0];
/*     */   private final transient boolean arrayGettersSafe;
/*     */   private final MBeanParameterInfo[] signature;
/*     */ 
/*     */   public MBeanConstructorInfo(String paramString, Constructor<?> paramConstructor)
/*     */   {
/*  69 */     this(paramConstructor.getName(), paramString, constructorSignature(paramConstructor), Introspector.descriptorForElement(paramConstructor));
/*     */   }
/*     */ 
/*     */   public MBeanConstructorInfo(String paramString1, String paramString2, MBeanParameterInfo[] paramArrayOfMBeanParameterInfo)
/*     */   {
/*  86 */     this(paramString1, paramString2, paramArrayOfMBeanParameterInfo, null);
/*     */   }
/*     */ 
/*     */   public MBeanConstructorInfo(String paramString1, String paramString2, MBeanParameterInfo[] paramArrayOfMBeanParameterInfo, Descriptor paramDescriptor)
/*     */   {
/* 106 */     super(paramString1, paramString2, paramDescriptor);
/*     */ 
/* 108 */     if ((paramArrayOfMBeanParameterInfo == null) || (paramArrayOfMBeanParameterInfo.length == 0))
/* 109 */       paramArrayOfMBeanParameterInfo = MBeanParameterInfo.NO_PARAMS;
/*     */     else
/* 111 */       paramArrayOfMBeanParameterInfo = (MBeanParameterInfo[])paramArrayOfMBeanParameterInfo.clone();
/* 112 */     this.signature = paramArrayOfMBeanParameterInfo;
/* 113 */     this.arrayGettersSafe = MBeanInfo.arrayGettersSafe(getClass(), MBeanConstructorInfo.class);
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/*     */     try
/*     */     {
/* 131 */       return super.clone();
/*     */     } catch (CloneNotSupportedException localCloneNotSupportedException) {
/*     */     }
/* 134 */     return null;
/*     */   }
/*     */ 
/*     */   public MBeanParameterInfo[] getSignature()
/*     */   {
/* 152 */     if (this.signature.length == 0) {
/* 153 */       return this.signature;
/*     */     }
/* 155 */     return (MBeanParameterInfo[])this.signature.clone();
/*     */   }
/*     */ 
/*     */   private MBeanParameterInfo[] fastGetSignature() {
/* 159 */     if (this.arrayGettersSafe) {
/* 160 */       return this.signature;
/*     */     }
/* 162 */     return getSignature();
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 166 */     return getClass().getName() + "[" + "description=" + getDescription() + ", " + "name=" + getName() + ", " + "signature=" + Arrays.asList(fastGetSignature()) + ", " + "descriptor=" + getDescriptor() + "]";
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 189 */     if (paramObject == this)
/* 190 */       return true;
/* 191 */     if (!(paramObject instanceof MBeanConstructorInfo))
/* 192 */       return false;
/* 193 */     MBeanConstructorInfo localMBeanConstructorInfo = (MBeanConstructorInfo)paramObject;
/* 194 */     return (Objects.equals(localMBeanConstructorInfo.getName(), getName())) && (Objects.equals(localMBeanConstructorInfo.getDescription(), getDescription())) && (Arrays.equals(localMBeanConstructorInfo.fastGetSignature(), fastGetSignature())) && (Objects.equals(localMBeanConstructorInfo.getDescriptor(), getDescriptor()));
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 207 */     return Objects.hash(new Object[] { getName() }) ^ Arrays.hashCode(fastGetSignature());
/*     */   }
/*     */ 
/*     */   private static MBeanParameterInfo[] constructorSignature(Constructor<?> paramConstructor) {
/* 211 */     Class[] arrayOfClass = paramConstructor.getParameterTypes();
/* 212 */     Annotation[][] arrayOfAnnotation = paramConstructor.getParameterAnnotations();
/* 213 */     return MBeanOperationInfo.parameters(arrayOfClass, arrayOfAnnotation);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.MBeanConstructorInfo
 * JD-Core Version:    0.6.2
 */