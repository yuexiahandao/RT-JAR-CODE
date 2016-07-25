/*    */ package com.sun.xml.internal.stream;
/*    */ 
/*    */ import com.sun.org.apache.xerces.internal.xni.XMLResourceIdentifier;
/*    */ import com.sun.org.apache.xerces.internal.xni.XNIException;
/*    */ import com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import javax.xml.stream.XMLEventReader;
/*    */ import javax.xml.stream.XMLResolver;
/*    */ import javax.xml.stream.XMLStreamException;
/*    */ import javax.xml.stream.XMLStreamReader;
/*    */ 
/*    */ public class StaxEntityResolverWrapper
/*    */ {
/*    */   XMLResolver fStaxResolver;
/*    */ 
/*    */   public StaxEntityResolverWrapper(XMLResolver resolver)
/*    */   {
/* 47 */     this.fStaxResolver = resolver;
/*    */   }
/*    */ 
/*    */   public void setStaxEntityResolver(XMLResolver resolver) {
/* 51 */     this.fStaxResolver = resolver;
/*    */   }
/*    */ 
/*    */   public XMLResolver getStaxEntityResolver() {
/* 55 */     return this.fStaxResolver;
/*    */   }
/*    */ 
/*    */   public StaxXMLInputSource resolveEntity(XMLResourceIdentifier resourceIdentifier) throws XNIException, IOException
/*    */   {
/* 60 */     Object object = null;
/*    */     try {
/* 62 */       object = this.fStaxResolver.resolveEntity(resourceIdentifier.getPublicId(), resourceIdentifier.getLiteralSystemId(), resourceIdentifier.getBaseSystemId(), null);
/*    */ 
/* 64 */       return getStaxInputSource(object);
/*    */     } catch (XMLStreamException streamException) {
/* 66 */       throw new XNIException(streamException);
/*    */     }
/*    */   }
/*    */ 
/*    */   StaxXMLInputSource getStaxInputSource(Object object) {
/* 71 */     if (object == null) return null;
/*    */ 
/* 73 */     if ((object instanceof InputStream)) {
/* 74 */       return new StaxXMLInputSource(new XMLInputSource(null, null, null, (InputStream)object, null));
/*    */     }
/* 76 */     if ((object instanceof XMLStreamReader))
/* 77 */       return new StaxXMLInputSource((XMLStreamReader)object);
/* 78 */     if ((object instanceof XMLEventReader)) {
/* 79 */       return new StaxXMLInputSource((XMLEventReader)object);
/*    */     }
/*    */ 
/* 82 */     return null;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.stream.StaxEntityResolverWrapper
 * JD-Core Version:    0.6.2
 */