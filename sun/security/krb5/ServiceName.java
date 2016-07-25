/*    */ package sun.security.krb5;
/*    */ 
/*    */ public class ServiceName extends PrincipalName
/*    */ {
/*    */   public ServiceName(String paramString, int paramInt)
/*    */     throws RealmException
/*    */   {
/* 39 */     super(paramString, paramInt);
/*    */   }
/*    */ 
/*    */   public ServiceName(String paramString) throws RealmException {
/* 43 */     this(paramString, 0);
/*    */   }
/*    */ 
/*    */   public ServiceName(String paramString1, String paramString2) throws RealmException {
/* 47 */     this(paramString1, 0);
/* 48 */     setRealm(paramString2);
/*    */   }
/*    */ 
/*    */   public ServiceName(String paramString1, String paramString2, String paramString3)
/*    */     throws KrbException
/*    */   {
/* 54 */     super(paramString1, paramString2, paramString3, 2);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.ServiceName
 * JD-Core Version:    0.6.2
 */