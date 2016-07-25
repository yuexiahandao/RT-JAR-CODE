/*     */ package javax.xml.soap;
/*     */ 
/*     */ import javax.xml.namespace.QName;
/*     */ import org.w3c.dom.Element;
/*     */ 
/*     */ public abstract class SOAPFactory
/*     */ {
/*     */   private static final String SOAP_FACTORY_PROPERTY = "javax.xml.soap.SOAPFactory";
/*     */   static final String DEFAULT_SOAP_FACTORY = "com.sun.xml.internal.messaging.saaj.soap.ver1_1.SOAPFactory1_1Impl";
/*     */ 
/*     */   public SOAPElement createElement(Element domElement)
/*     */     throws SOAPException
/*     */   {
/*  83 */     throw new UnsupportedOperationException("createElement(org.w3c.dom.Element) must be overridden by all subclasses of SOAPFactory.");
/*     */   }
/*     */ 
/*     */   public abstract SOAPElement createElement(Name paramName)
/*     */     throws SOAPException;
/*     */ 
/*     */   public SOAPElement createElement(QName qname)
/*     */     throws SOAPException
/*     */   {
/* 126 */     throw new UnsupportedOperationException("createElement(QName) must be overridden by all subclasses of SOAPFactory.");
/*     */   }
/*     */ 
/*     */   public abstract SOAPElement createElement(String paramString)
/*     */     throws SOAPException;
/*     */ 
/*     */   public abstract SOAPElement createElement(String paramString1, String paramString2, String paramString3)
/*     */     throws SOAPException;
/*     */ 
/*     */   public abstract Detail createDetail()
/*     */     throws SOAPException;
/*     */ 
/*     */   public abstract SOAPFault createFault(String paramString, QName paramQName)
/*     */     throws SOAPException;
/*     */ 
/*     */   public abstract SOAPFault createFault()
/*     */     throws SOAPException;
/*     */ 
/*     */   public abstract Name createName(String paramString1, String paramString2, String paramString3)
/*     */     throws SOAPException;
/*     */ 
/*     */   public abstract Name createName(String paramString)
/*     */     throws SOAPException;
/*     */ 
/*     */   public static SOAPFactory newInstance()
/*     */     throws SOAPException
/*     */   {
/*     */     try
/*     */     {
/* 264 */       SOAPFactory factory = (SOAPFactory)FactoryFinder.find("javax.xml.soap.SOAPFactory", "com.sun.xml.internal.messaging.saaj.soap.ver1_1.SOAPFactory1_1Impl", false);
/*     */ 
/* 266 */       if (factory != null)
/* 267 */         return factory;
/* 268 */       return newInstance("SOAP 1.1 Protocol");
/*     */     } catch (Exception ex) {
/* 270 */       throw new SOAPException("Unable to create SOAP Factory: " + ex.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public static SOAPFactory newInstance(String protocol)
/*     */     throws SOAPException
/*     */   {
/* 297 */     return SAAJMetaFactory.getInstance().newSOAPFactory(protocol);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.soap.SOAPFactory
 * JD-Core Version:    0.6.2
 */