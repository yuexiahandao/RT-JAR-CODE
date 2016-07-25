/*     */ package javax.xml.soap;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ 
/*     */ public abstract class MessageFactory
/*     */ {
/*     */   static final String DEFAULT_MESSAGE_FACTORY = "com.sun.xml.internal.messaging.saaj.soap.ver1_1.SOAPMessageFactory1_1Impl";
/*     */   private static final String MESSAGE_FACTORY_PROPERTY = "javax.xml.soap.MessageFactory";
/*     */ 
/*     */   public static MessageFactory newInstance()
/*     */     throws SOAPException
/*     */   {
/*     */     try
/*     */     {
/* 103 */       MessageFactory factory = (MessageFactory)FactoryFinder.find("javax.xml.soap.MessageFactory", "com.sun.xml.internal.messaging.saaj.soap.ver1_1.SOAPMessageFactory1_1Impl", false);
/*     */ 
/* 108 */       if (factory != null) {
/* 109 */         return factory;
/*     */       }
/* 111 */       return newInstance("SOAP 1.1 Protocol");
/*     */     }
/*     */     catch (Exception ex) {
/* 114 */       throw new SOAPException("Unable to create message factory for SOAP: " + ex.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public static MessageFactory newInstance(String protocol)
/*     */     throws SOAPException
/*     */   {
/* 146 */     return SAAJMetaFactory.getInstance().newMessageFactory(protocol);
/*     */   }
/*     */ 
/*     */   public abstract SOAPMessage createMessage()
/*     */     throws SOAPException;
/*     */ 
/*     */   public abstract SOAPMessage createMessage(MimeHeaders paramMimeHeaders, InputStream paramInputStream)
/*     */     throws IOException, SOAPException;
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.soap.MessageFactory
 * JD-Core Version:    0.6.2
 */