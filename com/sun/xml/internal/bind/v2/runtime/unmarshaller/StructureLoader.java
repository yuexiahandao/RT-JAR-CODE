/*     */ package com.sun.xml.internal.bind.v2.runtime.unmarshaller;
/*     */ 
/*     */ import com.sun.xml.internal.bind.api.AccessorException;
/*     */ import com.sun.xml.internal.bind.v2.runtime.ClassBeanInfoImpl;
/*     */ import com.sun.xml.internal.bind.v2.runtime.JAXBContextImpl;
/*     */ import com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo;
/*     */ import com.sun.xml.internal.bind.v2.runtime.Name;
/*     */ import com.sun.xml.internal.bind.v2.runtime.property.AttributeProperty;
/*     */ import com.sun.xml.internal.bind.v2.runtime.property.Property;
/*     */ import com.sun.xml.internal.bind.v2.runtime.property.StructureLoaderBuilder;
/*     */ import com.sun.xml.internal.bind.v2.runtime.property.UnmarshallerChain;
/*     */ import com.sun.xml.internal.bind.v2.runtime.reflect.Accessor;
/*     */ import com.sun.xml.internal.bind.v2.runtime.reflect.TransducedAccessor;
/*     */ import com.sun.xml.internal.bind.v2.util.QNameMap;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.xml.namespace.QName;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public final class StructureLoader extends Loader
/*     */ {
/*  66 */   private final QNameMap<ChildLoader> childUnmarshallers = new QNameMap();
/*     */   private ChildLoader catchAll;
/*     */   private ChildLoader textHandler;
/*     */   private QNameMap<TransducedAccessor> attUnmarshallers;
/*     */   private Accessor<Object, Map<QName, String>> attCatchAll;
/*     */   private final JaxBeanInfo beanInfo;
/*     */   private int frameSize;
/* 267 */   private static final QNameMap<TransducedAccessor> EMPTY = new QNameMap();
/*     */ 
/*     */   public StructureLoader(ClassBeanInfoImpl beanInfo)
/*     */   {
/* 102 */     super(true);
/* 103 */     this.beanInfo = beanInfo;
/*     */   }
/*     */ 
/*     */   public void init(JAXBContextImpl context, ClassBeanInfoImpl beanInfo, Accessor<?, Map<QName, String>> attWildcard)
/*     */   {
/* 114 */     UnmarshallerChain chain = new UnmarshallerChain(context);
/* 115 */     for (ClassBeanInfoImpl bi = beanInfo; bi != null; bi = bi.superClazz) {
/* 116 */       for (int i = bi.properties.length - 1; i >= 0; i--) {
/* 117 */         Property p = bi.properties[i];
/*     */ 
/* 119 */         switch (1.$SwitchMap$com$sun$xml$internal$bind$v2$model$core$PropertyKind[p.getKind().ordinal()]) {
/*     */         case 1:
/* 121 */           if (this.attUnmarshallers == null)
/* 122 */             this.attUnmarshallers = new QNameMap();
/* 123 */           AttributeProperty ap = (AttributeProperty)p;
/* 124 */           this.attUnmarshallers.put(ap.attName.toQName(), ap.xacc);
/* 125 */           break;
/*     */         case 2:
/*     */         case 3:
/*     */         case 4:
/*     */         case 5:
/* 130 */           p.buildChildElementUnmarshallers(chain, this.childUnmarshallers);
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 136 */     this.frameSize = chain.getScopeSize();
/*     */ 
/* 138 */     this.textHandler = ((ChildLoader)this.childUnmarshallers.get(StructureLoaderBuilder.TEXT_HANDLER));
/* 139 */     this.catchAll = ((ChildLoader)this.childUnmarshallers.get(StructureLoaderBuilder.CATCH_ALL));
/*     */ 
/* 141 */     if (attWildcard != null) {
/* 142 */       this.attCatchAll = attWildcard;
/*     */ 
/* 145 */       if (this.attUnmarshallers == null)
/* 146 */         this.attUnmarshallers = EMPTY;
/*     */     } else {
/* 148 */       this.attCatchAll = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void startElement(UnmarshallingContext.State state, TagName ea) throws SAXException
/*     */   {
/* 154 */     UnmarshallingContext context = state.getContext();
/*     */ 
/* 158 */     assert (!this.beanInfo.isImmutable());
/*     */ 
/* 161 */     Object child = context.getInnerPeer();
/*     */ 
/* 163 */     if ((child != null) && (this.beanInfo.jaxbType != child.getClass())) {
/* 164 */       child = null;
/*     */     }
/* 166 */     if (child != null) {
/* 167 */       this.beanInfo.reset(child, context);
/*     */     }
/* 169 */     if (child == null) {
/* 170 */       child = context.createInstance(this.beanInfo);
/*     */     }
/* 172 */     context.recordInnerPeer(child);
/*     */ 
/* 174 */     state.setTarget(child);
/*     */ 
/* 176 */     fireBeforeUnmarshal(this.beanInfo, child, state);
/*     */ 
/* 179 */     context.startScope(this.frameSize);
/*     */ 
/* 181 */     if (this.attUnmarshallers != null) {
/* 182 */       Attributes atts = ea.atts;
/* 183 */       for (int i = 0; i < atts.getLength(); i++) {
/* 184 */         String auri = atts.getURI(i);
/*     */ 
/* 186 */         String alocal = atts.getLocalName(i);
/* 187 */         if ("".equals(alocal)) {
/* 188 */           alocal = atts.getQName(i);
/*     */         }
/* 190 */         String avalue = atts.getValue(i);
/* 191 */         TransducedAccessor xacc = (TransducedAccessor)this.attUnmarshallers.get(auri, alocal);
/*     */         try {
/* 193 */           if (xacc != null) {
/* 194 */             xacc.parse(child, avalue);
/* 195 */           } else if (this.attCatchAll != null) {
/* 196 */             String qname = atts.getQName(i);
/* 197 */             if (atts.getURI(i).equals("http://www.w3.org/2001/XMLSchema-instance"))
/*     */               continue;
/* 199 */             Object o = state.getTarget();
/* 200 */             Map map = (Map)this.attCatchAll.get(o);
/* 201 */             if (map == null)
/*     */             {
/* 205 */               if (this.attCatchAll.valueType.isAssignableFrom(HashMap.class)) {
/* 206 */                 map = new HashMap();
/*     */               }
/*     */               else
/*     */               {
/* 210 */                 context.handleError(Messages.UNABLE_TO_CREATE_MAP.format(new Object[] { this.attCatchAll.valueType }));
/* 211 */                 return;
/*     */               }
/* 213 */               this.attCatchAll.set(o, map);
/*     */             }
/*     */ 
/* 217 */             int idx = qname.indexOf(':');
/*     */             String prefix;
/*     */             String prefix;
/* 218 */             if (idx < 0) prefix = ""; else {
/* 219 */               prefix = qname.substring(0, idx);
/*     */             }
/* 221 */             map.put(new QName(auri, alocal, prefix), avalue);
/*     */           }
/*     */         } catch (AccessorException e) {
/* 224 */           handleGenericException(e, true);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void childElement(UnmarshallingContext.State state, TagName arg) throws SAXException
/*     */   {
/* 232 */     ChildLoader child = (ChildLoader)this.childUnmarshallers.get(arg.uri, arg.local);
/* 233 */     if (child == null) {
/* 234 */       child = this.catchAll;
/* 235 */       if (child == null) {
/* 236 */         super.childElement(state, arg);
/* 237 */         return;
/*     */       }
/*     */     }
/*     */ 
/* 241 */     state.setLoader(child.loader);
/* 242 */     state.setReceiver(child.receiver);
/*     */   }
/*     */ 
/*     */   public Collection<QName> getExpectedChildElements()
/*     */   {
/* 247 */     return this.childUnmarshallers.keySet();
/*     */   }
/*     */ 
/*     */   public Collection<QName> getExpectedAttributes()
/*     */   {
/* 252 */     return this.attUnmarshallers.keySet();
/*     */   }
/*     */ 
/*     */   public void text(UnmarshallingContext.State state, CharSequence text) throws SAXException
/*     */   {
/* 257 */     if (this.textHandler != null)
/* 258 */       this.textHandler.loader.text(state, text);
/*     */   }
/*     */ 
/*     */   public void leaveElement(UnmarshallingContext.State state, TagName ea) throws SAXException
/*     */   {
/* 263 */     state.getContext().endScope(this.frameSize);
/* 264 */     fireAfterUnmarshal(this.beanInfo, state.getTarget(), state.getPrev());
/*     */   }
/*     */ 
/*     */   public JaxBeanInfo getBeanInfo()
/*     */   {
/* 270 */     return this.beanInfo;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.unmarshaller.StructureLoader
 * JD-Core Version:    0.6.2
 */