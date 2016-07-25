/*     */ package java.sql;
/*     */ 
/*     */ import java.security.BasicPermission;
/*     */ 
/*     */ public final class SQLPermission extends BasicPermission
/*     */ {
/*     */   static final long serialVersionUID = -1439323187199563495L;
/*     */ 
/*     */   public SQLPermission(String paramString)
/*     */   {
/* 130 */     super(paramString);
/*     */   }
/*     */ 
/*     */   public SQLPermission(String paramString1, String paramString2)
/*     */   {
/* 149 */     super(paramString1, paramString2);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.sql.SQLPermission
 * JD-Core Version:    0.6.2
 */