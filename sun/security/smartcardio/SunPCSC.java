/*    */ package sun.security.smartcardio;
/*    */ 
/*    */ import java.security.AccessController;
/*    */ import java.security.PrivilegedAction;
/*    */ import java.security.Provider;
/*    */ import javax.smartcardio.CardTerminals;
/*    */ import javax.smartcardio.TerminalFactorySpi;
/*    */ 
/*    */ public final class SunPCSC extends Provider
/*    */ {
/*    */   private static final long serialVersionUID = 6168388284028876579L;
/*    */ 
/*    */   public SunPCSC()
/*    */   {
/* 43 */     super("SunPCSC", 1.7D, "Sun PC/SC provider");
/* 44 */     AccessController.doPrivileged(new PrivilegedAction() {
/*    */       public Void run() {
/* 46 */         SunPCSC.this.put("TerminalFactory.PC/SC", "sun.security.smartcardio.SunPCSC$Factory");
/* 47 */         return null;
/*    */       }
/*    */     });
/*    */   }
/*    */ 
/*    */   public static final class Factory extends TerminalFactorySpi {
/*    */     public Factory(Object paramObject) throws PCSCException {
/* 54 */       if (paramObject != null) {
/* 55 */         throw new IllegalArgumentException("SunPCSC factory does not use parameters");
/*    */       }
/*    */ 
/* 59 */       PCSC.checkAvailable();
/* 60 */       PCSCTerminals.initContext();
/*    */     }
/*    */ 
/*    */     protected CardTerminals engineTerminals()
/*    */     {
/* 67 */       return new PCSCTerminals();
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.smartcardio.SunPCSC
 * JD-Core Version:    0.6.2
 */