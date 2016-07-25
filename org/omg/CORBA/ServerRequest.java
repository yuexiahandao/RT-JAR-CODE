/*     */ package org.omg.CORBA;
/*     */ 
/*     */ public abstract class ServerRequest
/*     */ {
/*     */   @Deprecated
/*     */   public String op_name()
/*     */   {
/*  85 */     return operation();
/*     */   }
/*     */ 
/*     */   public String operation()
/*     */   {
/* 106 */     throw new NO_IMPLEMENT();
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public void params(NVList paramNVList)
/*     */   {
/* 143 */     arguments(paramNVList);
/*     */   }
/*     */ 
/*     */   public void arguments(NVList paramNVList)
/*     */   {
/* 174 */     throw new NO_IMPLEMENT();
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public void result(Any paramAny)
/*     */   {
/* 208 */     set_result(paramAny);
/*     */   }
/*     */ 
/*     */   public void set_result(Any paramAny)
/*     */   {
/* 238 */     throw new NO_IMPLEMENT();
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public void except(Any paramAny)
/*     */   {
/* 259 */     set_exception(paramAny);
/*     */   }
/*     */ 
/*     */   public void set_exception(Any paramAny)
/*     */   {
/* 289 */     throw new NO_IMPLEMENT();
/*     */   }
/*     */ 
/*     */   public abstract Context ctx();
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.ServerRequest
 * JD-Core Version:    0.6.2
 */