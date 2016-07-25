/*    */ package javax.xml.soap;
/*    */ 
/*    */ public abstract class SOAPConnectionFactory
/*    */ {
/*    */   static final String DEFAULT_SOAP_CONNECTION_FACTORY = "com.sun.xml.internal.messaging.saaj.client.p2p.HttpSOAPConnectionFactory";
/*    */   private static final String SF_PROPERTY = "javax.xml.soap.SOAPConnectionFactory";
/*    */ 
/*    */   public static SOAPConnectionFactory newInstance()
/*    */     throws SOAPException, UnsupportedOperationException
/*    */   {
/*    */     try
/*    */     {
/* 67 */       return (SOAPConnectionFactory)FactoryFinder.find("javax.xml.soap.SOAPConnectionFactory", "com.sun.xml.internal.messaging.saaj.client.p2p.HttpSOAPConnectionFactory");
/*    */     }
/*    */     catch (Exception ex)
/*    */     {
/* 71 */       throw new SOAPException("Unable to create SOAP connection factory: " + ex.getMessage());
/*    */     }
/*    */   }
/*    */ 
/*    */   public abstract SOAPConnection createConnection()
/*    */     throws SOAPException;
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.soap.SOAPConnectionFactory
 * JD-Core Version:    0.6.2
 */