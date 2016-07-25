/*     */ package org.xml.sax;
/*     */ 
/*     */ /** @deprecated */
/*     */ public class HandlerBase
/*     */   implements EntityResolver, DTDHandler, DocumentHandler, ErrorHandler
/*     */ {
/*     */   public InputSource resolveEntity(String publicId, String systemId)
/*     */     throws SAXException
/*     */   {
/* 100 */     return null;
/*     */   }
/*     */ 
/*     */   public void notationDecl(String name, String publicId, String systemId)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void unparsedEntityDecl(String name, String publicId, String systemId, String notationName)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void setDocumentLocator(Locator locator)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void startDocument()
/*     */     throws SAXException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void endDocument()
/*     */     throws SAXException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void startElement(String name, AttributeList attributes)
/*     */     throws SAXException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void endElement(String name)
/*     */     throws SAXException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void characters(char[] ch, int start, int length)
/*     */     throws SAXException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void ignorableWhitespace(char[] ch, int start, int length)
/*     */     throws SAXException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void processingInstruction(String target, String data)
/*     */     throws SAXException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void warning(SAXParseException e)
/*     */     throws SAXException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void error(SAXParseException e)
/*     */     throws SAXException
/*     */   {
/*     */   }
/*     */ 
/*     */   public void fatalError(SAXParseException e)
/*     */     throws SAXException
/*     */   {
/* 388 */     throw e;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.xml.sax.HandlerBase
 * JD-Core Version:    0.6.2
 */