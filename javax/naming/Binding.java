/*     */ package javax.naming;
/*     */ 
/*     */ public class Binding extends NameClassPair
/*     */ {
/*     */   private Object boundObj;
/*     */   private static final long serialVersionUID = 8839217842691845890L;
/*     */ 
/*     */   public Binding(String paramString, Object paramObject)
/*     */   {
/*  73 */     super(paramString, null);
/*  74 */     this.boundObj = paramObject;
/*     */   }
/*     */ 
/*     */   public Binding(String paramString, Object paramObject, boolean paramBoolean)
/*     */   {
/*  96 */     super(paramString, null, paramBoolean);
/*  97 */     this.boundObj = paramObject;
/*     */   }
/*     */ 
/*     */   public Binding(String paramString1, String paramString2, Object paramObject)
/*     */   {
/* 114 */     super(paramString1, paramString2);
/* 115 */     this.boundObj = paramObject;
/*     */   }
/*     */ 
/*     */   public Binding(String paramString1, String paramString2, Object paramObject, boolean paramBoolean)
/*     */   {
/* 137 */     super(paramString1, paramString2, paramBoolean);
/* 138 */     this.boundObj = paramObject;
/*     */   }
/*     */ 
/*     */   public String getClassName()
/*     */   {
/* 150 */     String str = super.getClassName();
/* 151 */     if (str != null) {
/* 152 */       return str;
/*     */     }
/* 154 */     if (this.boundObj != null) {
/* 155 */       return this.boundObj.getClass().getName();
/*     */     }
/* 157 */     return null;
/*     */   }
/*     */ 
/*     */   public Object getObject()
/*     */   {
/* 168 */     return this.boundObj;
/*     */   }
/*     */ 
/*     */   public void setObject(Object paramObject)
/*     */   {
/* 177 */     this.boundObj = paramObject;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 192 */     return super.toString() + ":" + getObject();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.naming.Binding
 * JD-Core Version:    0.6.2
 */