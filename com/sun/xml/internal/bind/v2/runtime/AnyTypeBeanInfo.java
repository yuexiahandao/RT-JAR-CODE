/*     */ package com.sun.xml.internal.bind.v2.runtime;
/*     */ 
/*     */ import com.sun.xml.internal.bind.v2.model.runtime.RuntimeTypeInfo;
/*     */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.DomLoader;
/*     */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.Loader;
/*     */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.UnmarshallingContext;
/*     */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiTypeLoader;
/*     */ import java.io.IOException;
/*     */ import javax.xml.bind.annotation.W3CDomHandler;
/*     */ import javax.xml.bind.helpers.ValidationEventImpl;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import org.w3c.dom.Attr;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.NamedNodeMap;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ final class AnyTypeBeanInfo extends JaxBeanInfo<Object>
/*     */   implements AttributeAccessor
/*     */ {
/*  57 */   private boolean nilIncluded = false;
/*     */ 
/* 173 */   private static final W3CDomHandler domHandler = new W3CDomHandler();
/* 174 */   private static final DomLoader domLoader = new DomLoader(domHandler);
/* 175 */   private final XsiTypeLoader substLoader = new XsiTypeLoader(this);
/*     */ 
/*     */   public AnyTypeBeanInfo(JAXBContextImpl grammar, RuntimeTypeInfo anyTypeInfo)
/*     */   {
/*  60 */     super(grammar, anyTypeInfo, Object.class, new QName("http://www.w3.org/2001/XMLSchema", "anyType"), false, true, false);
/*     */   }
/*     */ 
/*     */   public String getElementNamespaceURI(Object element) {
/*  64 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public String getElementLocalName(Object element) {
/*  68 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public Object createInstance(UnmarshallingContext context) {
/*  72 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public boolean reset(Object element, UnmarshallingContext context)
/*     */   {
/*  77 */     return false;
/*     */   }
/*     */ 
/*     */   public String getId(Object element, XMLSerializer target)
/*     */   {
/*  88 */     return null;
/*     */   }
/*     */ 
/*     */   public void serializeBody(Object element, XMLSerializer target) throws SAXException, IOException, XMLStreamException {
/*  92 */     NodeList childNodes = ((Element)element).getChildNodes();
/*  93 */     int len = childNodes.getLength();
/*  94 */     for (int i = 0; i < len; i++) {
/*  95 */       Node child = childNodes.item(i);
/*  96 */       switch (child.getNodeType()) {
/*     */       case 3:
/*     */       case 4:
/*  99 */         target.text(child.getNodeValue(), null);
/* 100 */         break;
/*     */       case 1:
/* 102 */         target.writeDom((Element)child, domHandler, null, null);
/*     */       case 2:
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void serializeAttributes(Object element, XMLSerializer target) throws SAXException {
/* 109 */     NamedNodeMap al = ((Element)element).getAttributes();
/* 110 */     int len = al.getLength();
/* 111 */     for (int i = 0; i < len; i++) {
/* 112 */       Attr a = (Attr)al.item(i);
/*     */ 
/* 114 */       String uri = a.getNamespaceURI();
/* 115 */       if (uri == null) uri = "";
/* 116 */       String local = a.getLocalName();
/* 117 */       String name = a.getName();
/* 118 */       if (local == null) local = name;
/* 119 */       if ((uri.equals("http://www.w3.org/2001/XMLSchema-instance")) && ("nil".equals(local))) {
/* 120 */         this.isNilIncluded = true;
/*     */       }
/* 122 */       if (!name.startsWith("xmlns"))
/*     */       {
/* 124 */         target.attribute(uri, local, a.getValue());
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/* 129 */   public void serializeRoot(Object element, XMLSerializer target) throws SAXException { target.reportError(new ValidationEventImpl(1, Messages.UNABLE_TO_MARSHAL_NON_ELEMENT.format(new Object[] { element.getClass().getName() }), null, null)); }
/*     */ 
/*     */ 
/*     */   public void serializeURIs(Object element, XMLSerializer target)
/*     */   {
/* 138 */     NamedNodeMap al = ((Element)element).getAttributes();
/* 139 */     int len = al.getLength();
/* 140 */     NamespaceContext2 context = target.getNamespaceContext();
/* 141 */     for (int i = 0; i < len; i++) {
/* 142 */       Attr a = (Attr)al.item(i);
/* 143 */       if ("xmlns".equals(a.getPrefix())) {
/* 144 */         context.force(a.getValue(), a.getLocalName());
/*     */       }
/* 147 */       else if ("xmlns".equals(a.getName())) {
/* 148 */         if ((element instanceof Element)) {
/* 149 */           context.declareNamespace(a.getValue(), null, false);
/*     */         }
/*     */         else
/* 152 */           context.force(a.getValue(), "");
/*     */       }
/*     */       else
/*     */       {
/* 156 */         String nsUri = a.getNamespaceURI();
/* 157 */         if ((nsUri != null) && (nsUri.length() > 0))
/* 158 */           context.declareNamespace(nsUri, a.getPrefix(), true); 
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/* 163 */   public Transducer<Object> getTransducer() { return null; }
/*     */ 
/*     */   public Loader getLoader(JAXBContextImpl context, boolean typeSubstitutionCapable)
/*     */   {
/* 167 */     if (typeSubstitutionCapable) {
/* 168 */       return this.substLoader;
/*     */     }
/* 170 */     return domLoader;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.AnyTypeBeanInfo
 * JD-Core Version:    0.6.2
 */