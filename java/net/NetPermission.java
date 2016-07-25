/*     */ package java.net;
/*     */ 
/*     */ import java.security.BasicPermission;
/*     */ 
/*     */ public final class NetPermission extends BasicPermission
/*     */ {
/*     */   private static final long serialVersionUID = -8343910153355041693L;
/*     */ 
/*     */   public NetPermission(String paramString)
/*     */   {
/* 186 */     super(paramString);
/*     */   }
/*     */ 
/*     */   public NetPermission(String paramString1, String paramString2)
/*     */   {
/* 203 */     super(paramString1, paramString2);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.net.NetPermission
 * JD-Core Version:    0.6.2
 */