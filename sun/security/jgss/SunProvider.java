/*    */ package sun.security.jgss;
/*    */ 
/*    */ import java.security.AccessController;
/*    */ import java.security.PrivilegedAction;
/*    */ import java.security.Provider;
/*    */ 
/*    */ public final class SunProvider extends Provider
/*    */ {
/*    */   private static final long serialVersionUID = -238911724858694198L;
/*    */   private static final String INFO = "Sun (Kerberos v5, SPNEGO)";
/* 61 */   public static final SunProvider INSTANCE = new SunProvider();
/*    */ 
/*    */   public SunProvider()
/*    */   {
/* 65 */     super("SunJGSS", 1.7D, "Sun (Kerberos v5, SPNEGO)");
/*    */ 
/* 67 */     AccessController.doPrivileged(new PrivilegedAction()
/*    */     {
/*    */       public Void run() {
/* 70 */         SunProvider.this.put("GssApiMechanism.1.2.840.113554.1.2.2", "sun.security.jgss.krb5.Krb5MechFactory");
/*    */ 
/* 72 */         SunProvider.this.put("GssApiMechanism.1.3.6.1.5.5.2", "sun.security.jgss.spnego.SpNegoMechFactory");
/*    */ 
/* 78 */         return null;
/*    */       }
/*    */     });
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.jgss.SunProvider
 * JD-Core Version:    0.6.2
 */