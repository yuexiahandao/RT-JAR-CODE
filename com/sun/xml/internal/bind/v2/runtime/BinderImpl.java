/*     */ package com.sun.xml.internal.bind.v2.runtime;
/*     */ 
/*     */ import com.sun.xml.internal.bind.unmarshaller.InfosetScanner;
/*     */ import com.sun.xml.internal.bind.v2.runtime.output.DOMOutput;
/*     */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.InterningXmlVisitor;
/*     */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.SAXConnector;
/*     */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.UnmarshallerImpl;
/*     */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.UnmarshallingContext;
/*     */ import javax.xml.bind.Binder;
/*     */ import javax.xml.bind.JAXBElement;
/*     */ import javax.xml.bind.JAXBException;
/*     */ import javax.xml.bind.PropertyException;
/*     */ import javax.xml.bind.ValidationEventHandler;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.validation.Schema;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public class BinderImpl<XmlNode> extends Binder<XmlNode>
/*     */ {
/*     */   private final JAXBContextImpl context;
/*     */   private UnmarshallerImpl unmarshaller;
/*     */   private MarshallerImpl marshaller;
/*     */   private final InfosetScanner<XmlNode> scanner;
/*  81 */   private final AssociationMap<XmlNode> assoc = new AssociationMap();
/*     */ 
/*     */   BinderImpl(JAXBContextImpl _context, InfosetScanner<XmlNode> scanner) {
/*  84 */     this.context = _context;
/*  85 */     this.scanner = scanner;
/*     */   }
/*     */ 
/*     */   private UnmarshallerImpl getUnmarshaller() {
/*  89 */     if (this.unmarshaller == null)
/*  90 */       this.unmarshaller = new UnmarshallerImpl(this.context, this.assoc);
/*  91 */     return this.unmarshaller;
/*     */   }
/*     */ 
/*     */   private MarshallerImpl getMarshaller() {
/*  95 */     if (this.marshaller == null)
/*  96 */       this.marshaller = new MarshallerImpl(this.context, this.assoc);
/*  97 */     return this.marshaller;
/*     */   }
/*     */ 
/*     */   public void marshal(Object jaxbObject, XmlNode xmlNode) throws JAXBException {
/* 101 */     if ((xmlNode == null) || (jaxbObject == null))
/* 102 */       throw new IllegalArgumentException();
/* 103 */     getMarshaller().marshal(jaxbObject, createOutput(xmlNode));
/*     */   }
/*     */ 
/*     */   private DOMOutput createOutput(XmlNode xmlNode)
/*     */   {
/* 108 */     return new DOMOutput((Node)xmlNode, this.assoc);
/*     */   }
/*     */ 
/*     */   public Object updateJAXB(XmlNode xmlNode) throws JAXBException
/*     */   {
/* 113 */     return associativeUnmarshal(xmlNode, true, null);
/*     */   }
/*     */ 
/*     */   public Object unmarshal(XmlNode xmlNode) throws JAXBException {
/* 117 */     return associativeUnmarshal(xmlNode, false, null);
/*     */   }
/*     */ 
/*     */   public <T> JAXBElement<T> unmarshal(XmlNode xmlNode, Class<T> expectedType) throws JAXBException {
/* 121 */     if (expectedType == null) throw new IllegalArgumentException();
/* 122 */     return (JAXBElement)associativeUnmarshal(xmlNode, true, expectedType);
/*     */   }
/*     */ 
/*     */   public void setSchema(Schema schema) {
/* 126 */     getMarshaller().setSchema(schema);
/* 127 */     getUnmarshaller().setSchema(schema);
/*     */   }
/*     */ 
/*     */   public Schema getSchema() {
/* 131 */     return getUnmarshaller().getSchema();
/*     */   }
/*     */ 
/*     */   private Object associativeUnmarshal(XmlNode xmlNode, boolean inplace, Class expectedType) throws JAXBException {
/* 135 */     if (xmlNode == null) {
/* 136 */       throw new IllegalArgumentException();
/*     */     }
/* 138 */     JaxBeanInfo bi = null;
/* 139 */     if (expectedType != null) {
/* 140 */       bi = this.context.getBeanInfo(expectedType, true);
/*     */     }
/* 142 */     InterningXmlVisitor handler = new InterningXmlVisitor(getUnmarshaller().createUnmarshallerHandler(this.scanner, inplace, bi));
/*     */ 
/* 144 */     this.scanner.setContentHandler(new SAXConnector(handler, this.scanner.getLocator()));
/*     */     try {
/* 146 */       this.scanner.scan(xmlNode);
/*     */     } catch (SAXException e) {
/* 148 */       throw this.unmarshaller.createUnmarshalException(e);
/*     */     }
/*     */ 
/* 151 */     return handler.getContext().getResult();
/*     */   }
/*     */ 
/*     */   public XmlNode getXMLNode(Object jaxbObject) {
/* 155 */     if (jaxbObject == null)
/* 156 */       throw new IllegalArgumentException();
/* 157 */     AssociationMap.Entry e = this.assoc.byPeer(jaxbObject);
/* 158 */     if (e == null) return null;
/* 159 */     return e.element();
/*     */   }
/*     */ 
/*     */   public Object getJAXBNode(XmlNode xmlNode) {
/* 163 */     if (xmlNode == null)
/* 164 */       throw new IllegalArgumentException();
/* 165 */     AssociationMap.Entry e = this.assoc.byElement(xmlNode);
/* 166 */     if (e == null) return null;
/* 167 */     if (e.outer() != null) return e.outer();
/* 168 */     return e.inner();
/*     */   }
/*     */ 
/*     */   public XmlNode updateXML(Object jaxbObject) throws JAXBException {
/* 172 */     return updateXML(jaxbObject, getXMLNode(jaxbObject));
/*     */   }
/*     */ 
/*     */   public XmlNode updateXML(Object jaxbObject, XmlNode xmlNode) throws JAXBException {
/* 176 */     if ((jaxbObject == null) || (xmlNode == null)) throw new IllegalArgumentException();
/*     */ 
/* 181 */     Element e = (Element)xmlNode;
/* 182 */     Node ns = e.getNextSibling();
/* 183 */     Node p = e.getParentNode();
/* 184 */     p.removeChild(e);
/*     */ 
/* 188 */     JaxBeanInfo bi = this.context.getBeanInfo(jaxbObject, true);
/* 189 */     if (!bi.isElement()) {
/* 190 */       jaxbObject = new JAXBElement(new QName(e.getNamespaceURI(), e.getLocalName()), bi.jaxbType, jaxbObject);
/*     */     }
/*     */ 
/* 193 */     getMarshaller().marshal(jaxbObject, p);
/* 194 */     Node newNode = p.getLastChild();
/* 195 */     p.removeChild(newNode);
/* 196 */     p.insertBefore(newNode, ns);
/*     */ 
/* 198 */     return newNode;
/*     */   }
/*     */ 
/*     */   public void setEventHandler(ValidationEventHandler handler) throws JAXBException {
/* 202 */     getUnmarshaller().setEventHandler(handler);
/* 203 */     getMarshaller().setEventHandler(handler);
/*     */   }
/*     */ 
/*     */   public ValidationEventHandler getEventHandler() {
/* 207 */     return getUnmarshaller().getEventHandler();
/*     */   }
/*     */ 
/*     */   public Object getProperty(String name) throws PropertyException {
/* 211 */     if (name == null) {
/* 212 */       throw new IllegalArgumentException(Messages.NULL_PROPERTY_NAME.format(new Object[0]));
/*     */     }
/*     */ 
/* 215 */     if (excludeProperty(name)) {
/* 216 */       throw new PropertyException(name);
/*     */     }
/*     */ 
/* 219 */     Object prop = null;
/* 220 */     PropertyException pe = null;
/*     */     try
/*     */     {
/* 223 */       return getMarshaller().getProperty(name);
/*     */     }
/*     */     catch (PropertyException p) {
/* 226 */       pe = p;
/*     */       try
/*     */       {
/* 230 */         return getUnmarshaller().getProperty(name);
/*     */       }
/*     */       catch (PropertyException p) {
/* 233 */         pe = p;
/*     */ 
/* 236 */         pe.setStackTrace(Thread.currentThread().getStackTrace()); } 
/* 237 */     }throw pe;
/*     */   }
/*     */ 
/*     */   public void setProperty(String name, Object value) throws PropertyException {
/* 241 */     if (name == null) {
/* 242 */       throw new IllegalArgumentException(Messages.NULL_PROPERTY_NAME.format(new Object[0]));
/*     */     }
/*     */ 
/* 245 */     if (excludeProperty(name)) {
/* 246 */       throw new PropertyException(name, value);
/*     */     }
/*     */ 
/* 249 */     PropertyException pe = null;
/*     */     try
/*     */     {
/* 252 */       getMarshaller().setProperty(name, value);
/* 253 */       return;
/*     */     } catch (PropertyException p) {
/* 255 */       pe = p;
/*     */       try
/*     */       {
/* 259 */         getUnmarshaller().setProperty(name, value);
/* 260 */         return;
/*     */       } catch (PropertyException p) {
/* 262 */         pe = p;
/*     */ 
/* 267 */         pe.setStackTrace(Thread.currentThread().getStackTrace()); } 
/* 268 */     }throw pe;
/*     */   }
/*     */ 
/*     */   private boolean excludeProperty(String name) {
/* 272 */     return (name.equals("com.sun.xml.internal.bind.characterEscapeHandler")) || (name.equals("com.sun.xml.internal.bind.xmlDeclaration")) || (name.equals("com.sun.xml.internal.bind.xmlHeaders"));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.BinderImpl
 * JD-Core Version:    0.6.2
 */