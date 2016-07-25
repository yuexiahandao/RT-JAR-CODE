/*    */ package com.sun.xml.internal.bind.v2.runtime.property;
/*    */ 
/*    */ import com.sun.xml.internal.bind.api.AccessorException;
/*    */ import com.sun.xml.internal.bind.v2.model.core.PropertyKind;
/*    */ import com.sun.xml.internal.bind.v2.model.runtime.RuntimeValuePropertyInfo;
/*    */ import com.sun.xml.internal.bind.v2.runtime.JAXBContextImpl;
/*    */ import com.sun.xml.internal.bind.v2.runtime.XMLSerializer;
/*    */ import com.sun.xml.internal.bind.v2.runtime.reflect.Accessor;
/*    */ import com.sun.xml.internal.bind.v2.runtime.reflect.TransducedAccessor;
/*    */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.ChildLoader;
/*    */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.ValuePropertyLoader;
/*    */ import com.sun.xml.internal.bind.v2.util.QNameMap;
/*    */ import java.io.IOException;
/*    */ import javax.xml.stream.XMLStreamException;
/*    */ import org.xml.sax.SAXException;
/*    */ 
/*    */ public final class ValueProperty<BeanT> extends PropertyImpl<BeanT>
/*    */ {
/*    */   private final TransducedAccessor<BeanT> xacc;
/*    */   private final Accessor<BeanT, ?> acc;
/*    */ 
/*    */   public ValueProperty(JAXBContextImpl context, RuntimeValuePropertyInfo prop)
/*    */   {
/* 65 */     super(context, prop);
/* 66 */     this.xacc = TransducedAccessor.get(context, prop);
/* 67 */     this.acc = prop.getAccessor();
/*    */   }
/*    */ 
/*    */   public final void serializeBody(BeanT o, XMLSerializer w, Object outerPeer) throws SAXException, AccessorException, IOException, XMLStreamException {
/* 71 */     if (this.xacc.hasValue(o))
/* 72 */       this.xacc.writeText(w, o, this.fieldName);
/*    */   }
/*    */ 
/*    */   public void serializeURIs(BeanT o, XMLSerializer w) throws SAXException, AccessorException {
/* 76 */     this.xacc.declareNamespace(o, w);
/*    */   }
/*    */ 
/*    */   public boolean hasSerializeURIAction() {
/* 80 */     return this.xacc.useNamespace();
/*    */   }
/*    */ 
/*    */   public void buildChildElementUnmarshallers(UnmarshallerChain chainElem, QNameMap<ChildLoader> handlers) {
/* 84 */     handlers.put(StructureLoaderBuilder.TEXT_HANDLER, new ChildLoader(new ValuePropertyLoader(this.xacc), null));
/*    */   }
/*    */ 
/*    */   public PropertyKind getKind()
/*    */   {
/* 89 */     return PropertyKind.VALUE;
/*    */   }
/*    */ 
/*    */   public void reset(BeanT o) throws AccessorException {
/* 93 */     this.acc.set(o, null);
/*    */   }
/*    */ 
/*    */   public String getIdValue(BeanT bean) throws AccessorException, SAXException {
/* 97 */     return this.xacc.print(bean).toString();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.property.ValueProperty
 * JD-Core Version:    0.6.2
 */