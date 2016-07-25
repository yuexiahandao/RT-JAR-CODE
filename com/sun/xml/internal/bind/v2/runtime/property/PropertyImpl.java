/*    */ package com.sun.xml.internal.bind.v2.runtime.property;
/*    */ 
/*    */ import com.sun.xml.internal.bind.api.AccessorException;
/*    */ import com.sun.xml.internal.bind.v2.model.runtime.RuntimePropertyInfo;
/*    */ import com.sun.xml.internal.bind.v2.runtime.JAXBContextImpl;
/*    */ import com.sun.xml.internal.bind.v2.runtime.XMLSerializer;
/*    */ import com.sun.xml.internal.bind.v2.runtime.reflect.Accessor;
/*    */ import java.io.IOException;
/*    */ import javax.xml.stream.XMLStreamException;
/*    */ import org.xml.sax.SAXException;
/*    */ 
/*    */ abstract class PropertyImpl<BeanT>
/*    */   implements Property<BeanT>
/*    */ {
/*    */   protected final String fieldName;
/* 48 */   private RuntimePropertyInfo propertyInfo = null;
/* 49 */   private boolean hiddenByOverride = false;
/*    */ 
/*    */   public PropertyImpl(JAXBContextImpl context, RuntimePropertyInfo prop) {
/* 52 */     this.fieldName = prop.getName();
/* 53 */     if (context.retainPropertyInfo)
/* 54 */       this.propertyInfo = prop;
/*    */   }
/*    */ 
/*    */   public RuntimePropertyInfo getInfo()
/*    */   {
/* 59 */     return this.propertyInfo;
/*    */   }
/*    */ 
/*    */   public void serializeBody(BeanT o, XMLSerializer w, Object outerPeer) throws SAXException, AccessorException, IOException, XMLStreamException {
/*    */   }
/*    */ 
/*    */   public void serializeURIs(BeanT o, XMLSerializer w) throws SAXException, AccessorException {
/*    */   }
/*    */ 
/*    */   public boolean hasSerializeURIAction() {
/* 69 */     return false;
/*    */   }
/*    */ 
/*    */   public Accessor getElementPropertyAccessor(String nsUri, String localName)
/*    */   {
/* 74 */     return null;
/*    */   }
/*    */   public void wrapUp() {
/*    */   }
/*    */ 
/*    */   public boolean isHiddenByOverride() {
/* 80 */     return this.hiddenByOverride;
/*    */   }
/*    */ 
/*    */   public void setHiddenByOverride(boolean hidden) {
/* 84 */     this.hiddenByOverride = hidden;
/*    */   }
/*    */ 
/*    */   public String getFieldName() {
/* 88 */     return this.fieldName;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.property.PropertyImpl
 * JD-Core Version:    0.6.2
 */