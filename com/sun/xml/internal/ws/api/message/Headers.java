/*     */ package com.sun.xml.internal.ws.api.message;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.xml.internal.bind.api.Bridge;
/*     */ import com.sun.xml.internal.bind.api.JAXBRIContext;
/*     */ import com.sun.xml.internal.bind.v2.runtime.MarshallerImpl;
/*     */ import com.sun.xml.internal.ws.api.SOAPVersion;
/*     */ import com.sun.xml.internal.ws.message.DOMHeader;
/*     */ import com.sun.xml.internal.ws.message.StringHeader;
/*     */ import com.sun.xml.internal.ws.message.jaxb.JAXBHeader;
/*     */ import com.sun.xml.internal.ws.message.saaj.SAAJHeader;
/*     */ import com.sun.xml.internal.ws.message.stream.StreamHeader11;
/*     */ import com.sun.xml.internal.ws.message.stream.StreamHeader12;
/*     */ import javax.xml.bind.JAXBElement;
/*     */ import javax.xml.bind.Marshaller;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.soap.SOAPHeaderElement;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamReader;
/*     */ import org.w3c.dom.Element;
/*     */ 
/*     */ public abstract class Headers
/*     */ {
/*     */   /** @deprecated */
/*     */   public static Header create(SOAPVersion soapVersion, Marshaller m, Object o)
/*     */   {
/*  78 */     return new JAXBHeader(((MarshallerImpl)m).getContext(), o);
/*     */   }
/*     */ 
/*     */   public static Header create(JAXBRIContext context, Object o)
/*     */   {
/*  85 */     return new JAXBHeader(context, o);
/*     */   }
/*     */ 
/*     */   public static Header create(SOAPVersion soapVersion, Marshaller m, QName tagName, Object o)
/*     */   {
/* 100 */     return create(soapVersion, m, new JAXBElement(tagName, o.getClass(), o));
/*     */   }
/*     */ 
/*     */   public static Header create(Bridge bridge, Object jaxbObject)
/*     */   {
/* 107 */     return new JAXBHeader(bridge, jaxbObject);
/*     */   }
/*     */ 
/*     */   public static Header create(SOAPHeaderElement header)
/*     */   {
/* 114 */     return new SAAJHeader(header);
/*     */   }
/*     */ 
/*     */   public static Header create(Element node)
/*     */   {
/* 121 */     return new DOMHeader(node);
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public static Header create(SOAPVersion soapVersion, Element node)
/*     */   {
/* 129 */     return create(node);
/*     */   }
/*     */ 
/*     */   public static Header create(SOAPVersion soapVersion, XMLStreamReader reader)
/*     */     throws XMLStreamException
/*     */   {
/* 140 */     switch (1.$SwitchMap$com$sun$xml$internal$ws$api$SOAPVersion[soapVersion.ordinal()]) {
/*     */     case 1:
/* 142 */       return new StreamHeader11(reader);
/*     */     case 2:
/* 144 */       return new StreamHeader12(reader);
/*     */     }
/* 146 */     throw new AssertionError();
/*     */   }
/*     */ 
/*     */   public static Header create(QName name, String value)
/*     */   {
/* 158 */     return new StringHeader(name, value);
/*     */   }
/*     */ 
/*     */   public static Header createMustUnderstand(@NotNull SOAPVersion soapVersion, @NotNull QName name, @NotNull String value)
/*     */   {
/* 169 */     return new StringHeader(name, value, soapVersion, true);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.message.Headers
 * JD-Core Version:    0.6.2
 */