/*     */ package javax.xml.soap;
/*     */ 
/*     */ /** @deprecated */
/*     */ public class SOAPElementFactory
/*     */ {
/*     */   private SOAPFactory soapFactory;
/*     */ 
/*     */   private SOAPElementFactory(SOAPFactory soapFactory)
/*     */   {
/*  46 */     this.soapFactory = soapFactory;
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public SOAPElement create(Name name)
/*     */     throws SOAPException
/*     */   {
/*  70 */     return this.soapFactory.createElement(name);
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public SOAPElement create(String localName)
/*     */     throws SOAPException
/*     */   {
/*  92 */     return this.soapFactory.createElement(localName);
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public SOAPElement create(String localName, String prefix, String uri)
/*     */     throws SOAPException
/*     */   {
/* 118 */     return this.soapFactory.createElement(localName, prefix, uri);
/*     */   }
/*     */ 
/*     */   public static SOAPElementFactory newInstance()
/*     */     throws SOAPException
/*     */   {
/*     */     try
/*     */     {
/* 131 */       return new SOAPElementFactory(SOAPFactory.newInstance());
/*     */     } catch (Exception ex) {
/* 133 */       throw new SOAPException("Unable to create SOAP Element Factory: " + ex.getMessage());
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.soap.SOAPElementFactory
 * JD-Core Version:    0.6.2
 */