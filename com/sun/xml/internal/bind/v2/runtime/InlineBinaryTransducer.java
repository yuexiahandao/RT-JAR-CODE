/*    */ package com.sun.xml.internal.bind.v2.runtime;
/*    */ 
/*    */ import com.sun.istack.internal.NotNull;
/*    */ import com.sun.xml.internal.bind.api.AccessorException;
/*    */ import java.io.IOException;
/*    */ import javax.xml.stream.XMLStreamException;
/*    */ import org.xml.sax.SAXException;
/*    */ 
/*    */ public class InlineBinaryTransducer<V> extends FilterTransducer<V>
/*    */ {
/*    */   public InlineBinaryTransducer(Transducer<V> core)
/*    */   {
/* 44 */     super(core);
/*    */   }
/*    */ 
/*    */   @NotNull
/*    */   public CharSequence print(@NotNull V o) throws AccessorException {
/* 49 */     XMLSerializer w = XMLSerializer.getInstance();
/* 50 */     boolean old = w.setInlineBinaryFlag(true);
/*    */     try {
/* 52 */       return this.core.print(o);
/*    */     } finally {
/* 54 */       w.setInlineBinaryFlag(old);
/*    */     }
/*    */   }
/*    */ 
/*    */   public void writeText(XMLSerializer w, V o, String fieldName) throws IOException, SAXException, XMLStreamException, AccessorException
/*    */   {
/* 60 */     boolean old = w.setInlineBinaryFlag(true);
/*    */     try {
/* 62 */       this.core.writeText(w, o, fieldName);
/*    */     } finally {
/* 64 */       w.setInlineBinaryFlag(old);
/*    */     }
/*    */   }
/*    */ 
/*    */   public void writeLeafElement(XMLSerializer w, Name tagName, V o, String fieldName) throws IOException, SAXException, XMLStreamException, AccessorException
/*    */   {
/* 70 */     boolean old = w.setInlineBinaryFlag(true);
/*    */     try {
/* 72 */       this.core.writeLeafElement(w, tagName, o, fieldName);
/*    */     } finally {
/* 74 */       w.setInlineBinaryFlag(old);
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.InlineBinaryTransducer
 * JD-Core Version:    0.6.2
 */