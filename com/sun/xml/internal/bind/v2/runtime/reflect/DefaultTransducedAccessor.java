/*    */ package com.sun.xml.internal.bind.v2.runtime.reflect;
/*    */ 
/*    */ import com.sun.xml.internal.bind.api.AccessorException;
/*    */ import com.sun.xml.internal.bind.v2.runtime.Name;
/*    */ import com.sun.xml.internal.bind.v2.runtime.XMLSerializer;
/*    */ import java.io.IOException;
/*    */ import javax.xml.stream.XMLStreamException;
/*    */ import org.xml.sax.SAXException;
/*    */ 
/*    */ public abstract class DefaultTransducedAccessor<T> extends TransducedAccessor<T>
/*    */ {
/*    */   public abstract String print(T paramT)
/*    */     throws AccessorException, SAXException;
/*    */ 
/*    */   public void writeLeafElement(XMLSerializer w, Name tagName, T o, String fieldName)
/*    */     throws SAXException, AccessorException, IOException, XMLStreamException
/*    */   {
/* 54 */     w.leafElement(tagName, print(o), fieldName);
/*    */   }
/*    */ 
/*    */   public void writeText(XMLSerializer w, T o, String fieldName) throws AccessorException, SAXException, IOException, XMLStreamException {
/* 58 */     w.text(print(o), fieldName);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.reflect.DefaultTransducedAccessor
 * JD-Core Version:    0.6.2
 */