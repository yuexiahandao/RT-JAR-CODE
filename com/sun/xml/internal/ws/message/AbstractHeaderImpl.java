/*     */ package com.sun.xml.internal.ws.message;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.xml.internal.bind.api.Bridge;
/*     */ import com.sun.xml.internal.bind.api.BridgeContext;
/*     */ import com.sun.xml.internal.ws.api.SOAPVersion;
/*     */ import com.sun.xml.internal.ws.api.addressing.AddressingVersion;
/*     */ import com.sun.xml.internal.ws.api.addressing.WSEndpointReference;
/*     */ import com.sun.xml.internal.ws.api.message.Header;
/*     */ import com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory;
/*     */ import java.util.Set;
/*     */ import javax.xml.bind.JAXBException;
/*     */ import javax.xml.bind.Unmarshaller;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamReader;
/*     */ import org.xml.sax.helpers.AttributesImpl;
/*     */ 
/*     */ public abstract class AbstractHeaderImpl
/*     */   implements Header
/*     */ {
/* 141 */   protected static final AttributesImpl EMPTY_ATTS = new AttributesImpl();
/*     */ 
/*     */   /** @deprecated */
/*     */   public final <T> T readAsJAXB(Bridge<T> bridge, BridgeContext context)
/*     */     throws JAXBException
/*     */   {
/*  63 */     return readAsJAXB(bridge);
/*     */   }
/*     */ 
/*     */   public <T> T readAsJAXB(Unmarshaller unmarshaller) throws JAXBException {
/*     */     try {
/*  68 */       return unmarshaller.unmarshal(readHeader());
/*     */     } catch (Exception e) {
/*  70 */       throw new JAXBException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public <T> T readAsJAXB(Bridge<T> bridge) throws JAXBException {
/*     */     try {
/*  76 */       return bridge.unmarshal(readHeader());
/*     */     } catch (XMLStreamException e) {
/*  78 */       throw new JAXBException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public WSEndpointReference readAsEPR(AddressingVersion expected)
/*     */     throws XMLStreamException
/*     */   {
/*  86 */     XMLStreamReader xsr = readHeader();
/*  87 */     WSEndpointReference epr = new WSEndpointReference(xsr, expected);
/*  88 */     XMLStreamReaderFactory.recycle(xsr);
/*  89 */     return epr;
/*     */   }
/*     */ 
/*     */   public boolean isIgnorable(@NotNull SOAPVersion soapVersion, @NotNull Set<String> roles)
/*     */   {
/*  94 */     String v = getAttribute(soapVersion.nsUri, "mustUnderstand");
/*  95 */     if ((v == null) || (!parseBool(v))) return true;
/*     */ 
/*  98 */     return !roles.contains(getRole(soapVersion));
/*     */   }
/*     */   @NotNull
/*     */   public String getRole(@NotNull SOAPVersion soapVersion) {
/* 102 */     String v = getAttribute(soapVersion.nsUri, soapVersion.roleAttributeName);
/* 103 */     if (v == null)
/* 104 */       v = soapVersion.implicitRole;
/* 105 */     return v;
/*     */   }
/*     */ 
/*     */   public boolean isRelay() {
/* 109 */     String v = getAttribute(SOAPVersion.SOAP_12.nsUri, "relay");
/* 110 */     if (v == null) return false;
/* 111 */     return parseBool(v);
/*     */   }
/*     */ 
/*     */   public String getAttribute(QName name) {
/* 115 */     return getAttribute(name.getNamespaceURI(), name.getLocalPart());
/*     */   }
/*     */ 
/*     */   protected final boolean parseBool(String value)
/*     */   {
/* 124 */     if (value.length() == 0) {
/* 125 */       return false;
/*     */     }
/* 127 */     char ch = value.charAt(0);
/* 128 */     return (ch == 't') || (ch == '1');
/*     */   }
/*     */ 
/*     */   public String getStringContent() {
/*     */     try {
/* 133 */       XMLStreamReader xsr = readHeader();
/* 134 */       xsr.nextTag();
/* 135 */       return xsr.getElementText(); } catch (XMLStreamException e) {
/*     */     }
/* 137 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.message.AbstractHeaderImpl
 * JD-Core Version:    0.6.2
 */