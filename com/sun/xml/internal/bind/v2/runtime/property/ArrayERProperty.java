/*     */ package com.sun.xml.internal.bind.v2.runtime.property;
/*     */ 
/*     */ import com.sun.xml.internal.bind.api.AccessorException;
/*     */ import com.sun.xml.internal.bind.v2.model.runtime.RuntimePropertyInfo;
/*     */ import com.sun.xml.internal.bind.v2.runtime.JAXBContextImpl;
/*     */ import com.sun.xml.internal.bind.v2.runtime.Name;
/*     */ import com.sun.xml.internal.bind.v2.runtime.NameBuilder;
/*     */ import com.sun.xml.internal.bind.v2.runtime.XMLSerializer;
/*     */ import com.sun.xml.internal.bind.v2.runtime.reflect.Accessor;
/*     */ import com.sun.xml.internal.bind.v2.runtime.reflect.Lister;
/*     */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.ChildLoader;
/*     */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.Loader;
/*     */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.Receiver;
/*     */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.Scope;
/*     */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.TagName;
/*     */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.UnmarshallingContext;
/*     */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.UnmarshallingContext.State;
/*     */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader;
/*     */ import com.sun.xml.internal.bind.v2.util.QNameMap;
/*     */ import java.io.IOException;
/*     */ import java.util.Collection;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ abstract class ArrayERProperty<BeanT, ListT, ItemT> extends ArrayProperty<BeanT, ListT, ItemT>
/*     */ {
/*     */   protected final Name wrapperTagName;
/*     */   protected final boolean isWrapperNillable;
/*     */ 
/*     */   protected ArrayERProperty(JAXBContextImpl grammar, RuntimePropertyInfo prop, QName tagName, boolean isWrapperNillable)
/*     */   {
/*  73 */     super(grammar, prop);
/*  74 */     if (tagName == null)
/*  75 */       this.wrapperTagName = null;
/*     */     else
/*  77 */       this.wrapperTagName = grammar.nameBuilder.createElementName(tagName);
/*  78 */     this.isWrapperNillable = isWrapperNillable;
/*     */   }
/*     */ 
/*     */   public final void serializeBody(BeanT o, XMLSerializer w, Object outerPeer)
/*     */     throws SAXException, AccessorException, IOException, XMLStreamException
/*     */   {
/* 135 */     Object list = this.acc.get(o);
/*     */ 
/* 137 */     if (list != null) {
/* 138 */       if (this.wrapperTagName != null) {
/* 139 */         w.startElement(this.wrapperTagName, null);
/* 140 */         w.endNamespaceDecls(list);
/* 141 */         w.endAttributes();
/*     */       }
/*     */ 
/* 144 */       serializeListBody(o, w, list);
/*     */ 
/* 146 */       if (this.wrapperTagName != null) {
/* 147 */         w.endElement();
/*     */       }
/*     */     }
/* 150 */     else if (this.isWrapperNillable) {
/* 151 */       w.startElement(this.wrapperTagName, null);
/* 152 */       w.writeXsiNilTrue();
/* 153 */       w.endElement();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected abstract void serializeListBody(BeanT paramBeanT, XMLSerializer paramXMLSerializer, ListT paramListT)
/*     */     throws IOException, XMLStreamException, SAXException, AccessorException;
/*     */ 
/*     */   protected abstract void createBodyUnmarshaller(UnmarshallerChain paramUnmarshallerChain, QNameMap<ChildLoader> paramQNameMap);
/*     */ 
/*     */   public final void buildChildElementUnmarshallers(UnmarshallerChain chain, QNameMap<ChildLoader> loaders)
/*     */   {
/* 174 */     if (this.wrapperTagName != null) {
/* 175 */       UnmarshallerChain c = new UnmarshallerChain(chain.context);
/* 176 */       QNameMap m = new QNameMap();
/* 177 */       createBodyUnmarshaller(c, m);
/* 178 */       Loader loader = new ItemsLoader(this.acc, this.lister, m);
/* 179 */       if ((this.isWrapperNillable) || (chain.context.allNillable))
/* 180 */         loader = new XsiNilLoader(loader);
/* 181 */       loaders.put(this.wrapperTagName, new ChildLoader(loader, null));
/*     */     } else {
/* 183 */       createBodyUnmarshaller(chain, loaders);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final class ItemsLoader extends Loader
/*     */   {
/*     */     private final Accessor acc;
/*     */     private final Lister lister;
/*     */     private final QNameMap<ChildLoader> children;
/*     */ 
/*     */     public ItemsLoader(Accessor acc, Lister lister, QNameMap<ChildLoader> children)
/*     */     {
/*  90 */       super();
/*  91 */       this.acc = acc;
/*  92 */       this.lister = lister;
/*  93 */       this.children = children;
/*     */     }
/*     */ 
/*     */     public void startElement(UnmarshallingContext.State state, TagName ea) throws SAXException
/*     */     {
/*  98 */       UnmarshallingContext context = state.getContext();
/*  99 */       context.startScope(1);
/*     */ 
/* 101 */       state.setTarget(state.getPrev().getTarget());
/*     */ 
/* 104 */       context.getScope(0).start(this.acc, this.lister);
/*     */     }
/*     */ 
/*     */     public void childElement(UnmarshallingContext.State state, TagName ea)
/*     */       throws SAXException
/*     */     {
/* 111 */       ChildLoader child = (ChildLoader)this.children.get(ea.uri, ea.local);
/* 112 */       if (child == null) {
/* 113 */         child = (ChildLoader)this.children.get(StructureLoaderBuilder.CATCH_ALL);
/*     */       }
/* 115 */       if (child == null) {
/* 116 */         super.childElement(state, ea);
/* 117 */         return;
/*     */       }
/* 119 */       state.setLoader(child.loader);
/* 120 */       state.setReceiver(child.receiver);
/*     */     }
/*     */ 
/*     */     public void leaveElement(UnmarshallingContext.State state, TagName ea) throws SAXException
/*     */     {
/* 125 */       state.getContext().endScope(1);
/*     */     }
/*     */ 
/*     */     public Collection<QName> getExpectedChildElements()
/*     */     {
/* 130 */       return this.children.keySet();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected final class ReceiverImpl
/*     */     implements Receiver
/*     */   {
/*     */     private final int offset;
/*     */ 
/*     */     protected ReceiverImpl(int offset)
/*     */     {
/* 194 */       this.offset = offset;
/*     */     }
/*     */ 
/*     */     public void receive(UnmarshallingContext.State state, Object o) throws SAXException {
/* 198 */       state.getContext().getScope(this.offset).add(ArrayERProperty.this.acc, ArrayERProperty.this.lister, o);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.property.ArrayERProperty
 * JD-Core Version:    0.6.2
 */