/*    */ package javax.xml.soap;
/*    */ 
/*    */ public abstract class SAAJMetaFactory
/*    */ {
/*    */   private static final String META_FACTORY_CLASS_PROPERTY = "javax.xml.soap.MetaFactory";
/*    */   static final String DEFAULT_META_FACTORY_CLASS = "com.sun.xml.internal.messaging.saaj.soap.SAAJMetaFactoryImpl";
/*    */ 
/*    */   static SAAJMetaFactory getInstance()
/*    */     throws SOAPException
/*    */   {
/*    */     try
/*    */     {
/* 73 */       return (SAAJMetaFactory)FactoryFinder.find("javax.xml.soap.MetaFactory", "com.sun.xml.internal.messaging.saaj.soap.SAAJMetaFactoryImpl");
/*    */     }
/*    */     catch (Exception e)
/*    */     {
/* 79 */       throw new SOAPException("Unable to create SAAJ meta-factory" + e.getMessage());
/*    */     }
/*    */   }
/*    */ 
/*    */   protected abstract MessageFactory newMessageFactory(String paramString)
/*    */     throws SOAPException;
/*    */ 
/*    */   protected abstract SOAPFactory newSOAPFactory(String paramString)
/*    */     throws SOAPException;
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.soap.SAAJMetaFactory
 * JD-Core Version:    0.6.2
 */