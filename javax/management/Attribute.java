/*     */ package javax.management;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public class Attribute
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 2484220110589082382L;
/*     */   private String name;
/*  52 */   private Object value = null;
/*     */ 
/*     */   public Attribute(String paramString, Object paramObject)
/*     */   {
/*  64 */     if (paramString == null) {
/*  65 */       throw new RuntimeOperationsException(new IllegalArgumentException("Attribute name cannot be null "));
/*     */     }
/*     */ 
/*  68 */     this.name = paramString;
/*  69 */     this.value = paramObject;
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/*  79 */     return this.name;
/*     */   }
/*     */ 
/*     */   public Object getValue()
/*     */   {
/*  88 */     return this.value;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 101 */     if (!(paramObject instanceof Attribute)) {
/* 102 */       return false;
/*     */     }
/* 104 */     Attribute localAttribute = (Attribute)paramObject;
/*     */ 
/* 106 */     if (this.value == null) {
/* 107 */       if (localAttribute.getValue() == null) {
/* 108 */         return this.name.equals(localAttribute.getName());
/*     */       }
/* 110 */       return false;
/*     */     }
/*     */ 
/* 114 */     return (this.name.equals(localAttribute.getName())) && (this.value.equals(localAttribute.getValue()));
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 124 */     return this.name.hashCode() ^ (this.value == null ? 0 : this.value.hashCode());
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 133 */     return getName() + " = " + getValue();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.Attribute
 * JD-Core Version:    0.6.2
 */