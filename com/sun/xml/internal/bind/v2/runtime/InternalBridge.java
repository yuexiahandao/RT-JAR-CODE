/*    */ package com.sun.xml.internal.bind.v2.runtime;
/*    */ 
/*    */ import com.sun.xml.internal.bind.api.Bridge;
/*    */ import java.io.IOException;
/*    */ import javax.xml.stream.XMLStreamException;
/*    */ import org.xml.sax.SAXException;
/*    */ 
/*    */ abstract class InternalBridge<T> extends Bridge<T>
/*    */ {
/*    */   protected InternalBridge(JAXBContextImpl context)
/*    */   {
/* 42 */     super(context);
/*    */   }
/*    */ 
/*    */   public JAXBContextImpl getContext() {
/* 46 */     return this.context;
/*    */   }
/*    */ 
/*    */   abstract void marshal(T paramT, XMLSerializer paramXMLSerializer)
/*    */     throws IOException, SAXException, XMLStreamException;
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.InternalBridge
 * JD-Core Version:    0.6.2
 */