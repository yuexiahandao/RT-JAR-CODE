/*    */ package com.sun.xml.internal.bind.v2.runtime;
/*    */ 
/*    */ import com.sun.istack.internal.NotNull;
/*    */ import com.sun.xml.internal.bind.api.AccessorException;
/*    */ import java.io.IOException;
/*    */ import javax.xml.namespace.QName;
/*    */ import javax.xml.stream.XMLStreamException;
/*    */ import org.xml.sax.SAXException;
/*    */ 
/*    */ public abstract class FilterTransducer<T>
/*    */   implements Transducer<T>
/*    */ {
/*    */   protected final Transducer<T> core;
/*    */ 
/*    */   protected FilterTransducer(Transducer<T> core)
/*    */   {
/* 47 */     this.core = core;
/*    */   }
/*    */ 
/*    */   public final boolean isDefault()
/*    */   {
/* 53 */     return false;
/*    */   }
/*    */ 
/*    */   public boolean useNamespace() {
/* 57 */     return this.core.useNamespace();
/*    */   }
/*    */ 
/*    */   public void declareNamespace(T o, XMLSerializer w) throws AccessorException {
/* 61 */     this.core.declareNamespace(o, w);
/*    */   }
/*    */   @NotNull
/*    */   public CharSequence print(@NotNull T o) throws AccessorException {
/* 65 */     return this.core.print(o);
/*    */   }
/*    */ 
/*    */   public T parse(CharSequence lexical) throws AccessorException, SAXException {
/* 69 */     return this.core.parse(lexical);
/*    */   }
/*    */ 
/*    */   public void writeText(XMLSerializer w, T o, String fieldName) throws IOException, SAXException, XMLStreamException, AccessorException {
/* 73 */     this.core.writeText(w, o, fieldName);
/*    */   }
/*    */ 
/*    */   public void writeLeafElement(XMLSerializer w, Name tagName, T o, String fieldName) throws IOException, SAXException, XMLStreamException, AccessorException {
/* 77 */     this.core.writeLeafElement(w, tagName, o, fieldName);
/*    */   }
/*    */ 
/*    */   public QName getTypeName(T instance) {
/* 81 */     return null;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.FilterTransducer
 * JD-Core Version:    0.6.2
 */