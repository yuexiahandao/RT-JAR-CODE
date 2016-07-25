/*    */ package com.sun.xml.internal.ws.model.wsdl;
/*    */ 
/*    */ import com.sun.istack.internal.NotNull;
/*    */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLObject;
/*    */ import javax.xml.stream.Location;
/*    */ import javax.xml.stream.XMLStreamReader;
/*    */ import org.xml.sax.Locator;
/*    */ import org.xml.sax.helpers.LocatorImpl;
/*    */ 
/*    */ abstract class AbstractObjectImpl
/*    */   implements WSDLObject
/*    */ {
/*    */   private final int lineNumber;
/*    */   private final String systemId;
/*    */ 
/*    */   AbstractObjectImpl(XMLStreamReader xsr)
/*    */   {
/* 46 */     Location loc = xsr.getLocation();
/* 47 */     this.lineNumber = loc.getLineNumber();
/* 48 */     this.systemId = loc.getSystemId();
/*    */   }
/*    */ 
/*    */   AbstractObjectImpl(String systemId, int lineNumber) {
/* 52 */     this.systemId = systemId;
/* 53 */     this.lineNumber = lineNumber;
/*    */   }
/*    */   @NotNull
/*    */   public final Locator getLocation() {
/* 57 */     LocatorImpl loc = new LocatorImpl();
/* 58 */     loc.setSystemId(this.systemId);
/* 59 */     loc.setLineNumber(this.lineNumber);
/* 60 */     return loc;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.model.wsdl.AbstractObjectImpl
 * JD-Core Version:    0.6.2
 */