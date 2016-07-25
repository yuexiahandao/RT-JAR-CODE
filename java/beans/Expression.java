/*     */ package java.beans;
/*     */ 
/*     */ public class Expression extends Statement
/*     */ {
/*  49 */   private static Object unbound = new Object();
/*     */ 
/*  51 */   private Object value = unbound;
/*     */ 
/*     */   @ConstructorProperties({"target", "methodName", "arguments"})
/*     */   public Expression(Object paramObject, String paramString, Object[] paramArrayOfObject)
/*     */   {
/*  72 */     super(paramObject, paramString, paramArrayOfObject);
/*     */   }
/*     */ 
/*     */   public Expression(Object paramObject1, Object paramObject2, String paramString, Object[] paramArrayOfObject)
/*     */   {
/*  97 */     this(paramObject2, paramString, paramArrayOfObject);
/*  98 */     setValue(paramObject1);
/*     */   }
/*     */ 
/*     */   public void execute()
/*     */     throws Exception
/*     */   {
/* 121 */     setValue(invoke());
/*     */   }
/*     */ 
/*     */   public Object getValue()
/*     */     throws Exception
/*     */   {
/* 152 */     if (this.value == unbound) {
/* 153 */       setValue(invoke());
/*     */     }
/* 155 */     return this.value;
/*     */   }
/*     */ 
/*     */   public void setValue(Object paramObject)
/*     */   {
/* 169 */     this.value = paramObject;
/*     */   }
/*     */ 
/*     */   String instanceName(Object paramObject) {
/* 173 */     return paramObject == unbound ? "<unbound>" : super.instanceName(paramObject);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 180 */     return instanceName(this.value) + "=" + super.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.beans.Expression
 * JD-Core Version:    0.6.2
 */