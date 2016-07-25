/*    */ package sun.security.krb5.internal.ccache;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import sun.security.krb5.KrbException;
/*    */ import sun.security.krb5.PrincipalName;
/*    */ import sun.security.krb5.Realm;
/*    */ 
/*    */ public abstract class MemoryCredentialsCache extends CredentialsCache
/*    */ {
/*    */   private static CredentialsCache getCCacheInstance(PrincipalName paramPrincipalName)
/*    */   {
/* 51 */     return null;
/*    */   }
/*    */ 
/*    */   private static CredentialsCache getCCacheInstance(PrincipalName paramPrincipalName, File paramFile) {
/* 55 */     return null;
/*    */   }
/*    */ 
/*    */   public abstract boolean exists(String paramString);
/*    */ 
/*    */   public abstract void update(Credentials paramCredentials);
/*    */ 
/*    */   public abstract void save()
/*    */     throws IOException, KrbException;
/*    */ 
/*    */   public abstract Credentials[] getCredsList();
/*    */ 
/*    */   public abstract Credentials getCreds(PrincipalName paramPrincipalName, Realm paramRealm);
/*    */ 
/*    */   public abstract PrincipalName getPrimaryPrincipal();
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.internal.ccache.MemoryCredentialsCache
 * JD-Core Version:    0.6.2
 */