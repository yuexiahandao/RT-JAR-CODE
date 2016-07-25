/*    */ package com.sun.xml.internal.bind.v2.runtime;
/*    */ 
/*    */ import com.sun.xml.internal.bind.api.AccessorException;
/*    */ import java.io.IOException;
/*    */ import javax.xml.namespace.QName;
/*    */ import javax.xml.stream.XMLStreamException;
/*    */ import org.xml.sax.SAXException;
/*    */ 
/*    */ public class SchemaTypeTransducer<V> extends FilterTransducer<V>
/*    */ {
/*    */   private final QName schemaType;
/*    */ 
/*    */   public SchemaTypeTransducer(Transducer<V> core, QName schemaType)
/*    */   {
/* 53 */     super(core);
/* 54 */     this.schemaType = schemaType;
/*    */   }
/*    */ 
/*    */   public CharSequence print(V o) throws AccessorException
/*    */   {
/* 59 */     XMLSerializer w = XMLSerializer.getInstance();
/* 60 */     QName old = w.setSchemaType(this.schemaType);
/*    */     try {
/* 62 */       return this.core.print(o);
/*    */     } finally {
/* 64 */       w.setSchemaType(old);
/*    */     }
/*    */   }
/*    */ 
/*    */   public void writeText(XMLSerializer w, V o, String fieldName) throws IOException, SAXException, XMLStreamException, AccessorException
/*    */   {
/* 70 */     QName old = w.setSchemaType(this.schemaType);
/*    */     try {
/* 72 */       this.core.writeText(w, o, fieldName);
/*    */     } finally {
/* 74 */       w.setSchemaType(old);
/*    */     }
/*    */   }
/*    */ 
/*    */   public void writeLeafElement(XMLSerializer w, Name tagName, V o, String fieldName) throws IOException, SAXException, XMLStreamException, AccessorException
/*    */   {
/* 80 */     QName old = w.setSchemaType(this.schemaType);
/*    */     try {
/* 82 */       this.core.writeLeafElement(w, tagName, o, fieldName);
/*    */     } finally {
/* 84 */       w.setSchemaType(old);
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.SchemaTypeTransducer
 * JD-Core Version:    0.6.2
 */