/*     */ package javax.management;
/*     */ 
/*     */ import java.util.Objects;
/*     */ 
/*     */ public class MBeanParameterInfo extends MBeanFeatureInfo
/*     */   implements Cloneable
/*     */ {
/*     */   static final long serialVersionUID = 7432616882776782338L;
/*  44 */   static final MBeanParameterInfo[] NO_PARAMS = new MBeanParameterInfo[0];
/*     */   private final String type;
/*     */ 
/*     */   public MBeanParameterInfo(String paramString1, String paramString2, String paramString3)
/*     */   {
/*  62 */     this(paramString1, paramString2, paramString3, (Descriptor)null);
/*     */   }
/*     */ 
/*     */   public MBeanParameterInfo(String paramString1, String paramString2, String paramString3, Descriptor paramDescriptor)
/*     */   {
/*  80 */     super(paramString1, paramString3, paramDescriptor);
/*     */ 
/*  82 */     this.type = paramString2;
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/*     */     try
/*     */     {
/*  98 */       return super.clone();
/*     */     } catch (CloneNotSupportedException localCloneNotSupportedException) {
/*     */     }
/* 101 */     return null;
/*     */   }
/*     */ 
/*     */   public String getType()
/*     */   {
/* 111 */     return this.type;
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 115 */     return getClass().getName() + "[" + "description=" + getDescription() + ", " + "name=" + getName() + ", " + "type=" + getType() + ", " + "descriptor=" + getDescriptor() + "]";
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 136 */     if (paramObject == this)
/* 137 */       return true;
/* 138 */     if (!(paramObject instanceof MBeanParameterInfo))
/* 139 */       return false;
/* 140 */     MBeanParameterInfo localMBeanParameterInfo = (MBeanParameterInfo)paramObject;
/* 141 */     return (Objects.equals(localMBeanParameterInfo.getName(), getName())) && (Objects.equals(localMBeanParameterInfo.getType(), getType())) && (Objects.equals(localMBeanParameterInfo.getDescription(), getDescription())) && (Objects.equals(localMBeanParameterInfo.getDescriptor(), getDescriptor()));
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 148 */     return Objects.hash(new Object[] { getName(), getType() });
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.MBeanParameterInfo
 * JD-Core Version:    0.6.2
 */