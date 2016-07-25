/*     */ package javax.management;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public class ObjectInstance
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -4099952623687795850L;
/*     */   private ObjectName name;
/*     */   private String className;
/*     */ 
/*     */   public ObjectInstance(String paramString1, String paramString2)
/*     */     throws MalformedObjectNameException
/*     */   {
/*  75 */     this(new ObjectName(paramString1), paramString2);
/*     */   }
/*     */ 
/*     */   public ObjectInstance(ObjectName paramObjectName, String paramString)
/*     */   {
/*  93 */     if (paramObjectName.isPattern()) {
/*  94 */       IllegalArgumentException localIllegalArgumentException = new IllegalArgumentException("Invalid name->" + paramObjectName.toString());
/*     */ 
/*  97 */       throw new RuntimeOperationsException(localIllegalArgumentException);
/*     */     }
/*  99 */     this.name = paramObjectName;
/* 100 */     this.className = paramString;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 113 */     if (!(paramObject instanceof ObjectInstance)) {
/* 114 */       return false;
/*     */     }
/* 116 */     ObjectInstance localObjectInstance = (ObjectInstance)paramObject;
/* 117 */     if (!this.name.equals(localObjectInstance.getObjectName())) return false;
/* 118 */     if (this.className == null)
/* 119 */       return localObjectInstance.getClassName() == null;
/* 120 */     return this.className.equals(localObjectInstance.getClassName());
/*     */   }
/*     */ 
/*     */   public int hashCode() {
/* 124 */     int i = this.className == null ? 0 : this.className.hashCode();
/* 125 */     return this.name.hashCode() ^ i;
/*     */   }
/*     */ 
/*     */   public ObjectName getObjectName()
/*     */   {
/* 134 */     return this.name;
/*     */   }
/*     */ 
/*     */   public String getClassName()
/*     */   {
/* 143 */     return this.className;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 152 */     return getClassName() + "[" + getObjectName() + "]";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.ObjectInstance
 * JD-Core Version:    0.6.2
 */