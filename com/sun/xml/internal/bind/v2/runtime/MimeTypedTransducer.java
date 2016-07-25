/*    */ package com.sun.xml.internal.bind.v2.runtime;
/*    */ 
/*    */ import com.sun.xml.internal.bind.api.AccessorException;
/*    */ import java.io.IOException;
/*    */ import javax.activation.MimeType;
/*    */ import javax.xml.stream.XMLStreamException;
/*    */ import org.xml.sax.SAXException;
/*    */ 
/*    */ public final class MimeTypedTransducer<V> extends FilterTransducer<V>
/*    */ {
/*    */   private final MimeType expectedMimeType;
/*    */ 
/*    */   public MimeTypedTransducer(Transducer<V> core, MimeType expectedMimeType)
/*    */   {
/* 52 */     super(core);
/* 53 */     this.expectedMimeType = expectedMimeType;
/*    */   }
/*    */ 
/*    */   public CharSequence print(V o) throws AccessorException
/*    */   {
/* 58 */     XMLSerializer w = XMLSerializer.getInstance();
/* 59 */     MimeType old = w.setExpectedMimeType(this.expectedMimeType);
/*    */     try {
/* 61 */       return this.core.print(o);
/*    */     } finally {
/* 63 */       w.setExpectedMimeType(old);
/*    */     }
/*    */   }
/*    */ 
/*    */   public void writeText(XMLSerializer w, V o, String fieldName) throws IOException, SAXException, XMLStreamException, AccessorException
/*    */   {
/* 69 */     MimeType old = w.setExpectedMimeType(this.expectedMimeType);
/*    */     try {
/* 71 */       this.core.writeText(w, o, fieldName);
/*    */     } finally {
/* 73 */       w.setExpectedMimeType(old);
/*    */     }
/*    */   }
/*    */ 
/*    */   public void writeLeafElement(XMLSerializer w, Name tagName, V o, String fieldName) throws IOException, SAXException, XMLStreamException, AccessorException
/*    */   {
/* 79 */     MimeType old = w.setExpectedMimeType(this.expectedMimeType);
/*    */     try {
/* 81 */       this.core.writeLeafElement(w, tagName, o, fieldName);
/*    */     } finally {
/* 83 */       w.setExpectedMimeType(old);
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.MimeTypedTransducer
 * JD-Core Version:    0.6.2
 */