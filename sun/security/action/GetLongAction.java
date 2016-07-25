/*     */ package sun.security.action;
/*     */ 
/*     */ import java.security.PrivilegedAction;
/*     */ 
/*     */ public class GetLongAction
/*     */   implements PrivilegedAction<Long>
/*     */ {
/*     */   private String theProp;
/*     */   private long defaultVal;
/*  69 */   private boolean defaultSet = false;
/*     */ 
/*     */   public GetLongAction(String paramString)
/*     */   {
/*  78 */     this.theProp = paramString;
/*     */   }
/*     */ 
/*     */   public GetLongAction(String paramString, long paramLong)
/*     */   {
/*  89 */     this.theProp = paramString;
/*  90 */     this.defaultVal = paramLong;
/*  91 */     this.defaultSet = true;
/*     */   }
/*     */ 
/*     */   public Long run()
/*     */   {
/* 107 */     Long localLong = Long.getLong(this.theProp);
/* 108 */     if ((localLong == null) && (this.defaultSet))
/* 109 */       return new Long(this.defaultVal);
/* 110 */     return localLong;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.action.GetLongAction
 * JD-Core Version:    0.6.2
 */