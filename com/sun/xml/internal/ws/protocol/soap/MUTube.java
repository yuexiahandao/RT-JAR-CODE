/*     */ package com.sun.xml.internal.ws.protocol.soap;
/*     */ 
/*     */ import com.sun.xml.internal.ws.api.SOAPVersion;
/*     */ import com.sun.xml.internal.ws.api.WSBinding;
/*     */ import com.sun.xml.internal.ws.api.message.Header;
/*     */ import com.sun.xml.internal.ws.api.message.HeaderList;
/*     */ import com.sun.xml.internal.ws.api.message.Message;
/*     */ import com.sun.xml.internal.ws.api.pipe.Tube;
/*     */ import com.sun.xml.internal.ws.api.pipe.TubeCloner;
/*     */ import com.sun.xml.internal.ws.api.pipe.helper.AbstractFilterTubeImpl;
/*     */ import com.sun.xml.internal.ws.binding.SOAPBindingImpl;
/*     */ import com.sun.xml.internal.ws.fault.SOAPFaultBuilder;
/*     */ import com.sun.xml.internal.ws.message.DOMHeader;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import java.util.logging.Logger;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.soap.SOAPElement;
/*     */ import javax.xml.soap.SOAPException;
/*     */ import javax.xml.soap.SOAPFactory;
/*     */ import javax.xml.soap.SOAPFault;
/*     */ import javax.xml.ws.WebServiceException;
/*     */ import javax.xml.ws.soap.SOAPBinding;
/*     */ import javax.xml.ws.soap.SOAPFaultException;
/*     */ 
/*     */ abstract class MUTube extends AbstractFilterTubeImpl
/*     */ {
/*     */   private static final String MU_FAULT_DETAIL_LOCALPART = "NotUnderstood";
/*  63 */   private static final QName MU_HEADER_DETAIL = new QName(SOAPVersion.SOAP_12.nsUri, "NotUnderstood");
/*     */ 
/*  65 */   protected static final Logger logger = Logger.getLogger("com.sun.xml.internal.ws.soap.decoder");
/*     */   private static final String MUST_UNDERSTAND_FAULT_MESSAGE_STRING = "One or more mandatory SOAP header blocks not understood";
/*     */   protected final SOAPVersion soapVersion;
/*     */   protected SOAPBindingImpl binding;
/*     */ 
/*     */   protected MUTube(WSBinding binding, Tube next)
/*     */   {
/*  74 */     super(next);
/*     */ 
/*  76 */     if (!(binding instanceof SOAPBinding)) {
/*  77 */       throw new WebServiceException("MUPipe should n't be used for bindings other than SOAP.");
/*     */     }
/*     */ 
/*  80 */     this.binding = ((SOAPBindingImpl)binding);
/*  81 */     this.soapVersion = binding.getSOAPVersion();
/*     */   }
/*     */ 
/*     */   protected MUTube(MUTube that, TubeCloner cloner) {
/*  85 */     super(that, cloner);
/*  86 */     this.binding = that.binding;
/*  87 */     this.soapVersion = that.soapVersion;
/*     */   }
/*     */ 
/*     */   public final Set<QName> getMisUnderstoodHeaders(HeaderList headers, Set<String> roles, Set<QName> handlerKnownHeaders)
/*     */   {
/* 100 */     Set notUnderstoodHeaders = null;
/* 101 */     for (int i = 0; i < headers.size(); i++) {
/* 102 */       if (!headers.isUnderstood(i)) {
/* 103 */         Header header = headers.get(i);
/* 104 */         if (!header.isIgnorable(this.soapVersion, roles)) {
/* 105 */           QName qName = new QName(header.getNamespaceURI(), header.getLocalPart());
/*     */ 
/* 107 */           if ((!this.binding.understandsHeader(qName)) && 
/* 108 */             (!handlerKnownHeaders.contains(qName))) {
/* 109 */             logger.info("Element not understood=" + qName);
/* 110 */             if (notUnderstoodHeaders == null)
/* 111 */               notUnderstoodHeaders = new HashSet();
/* 112 */             notUnderstoodHeaders.add(qName);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 118 */     return notUnderstoodHeaders;
/*     */   }
/*     */ 
/*     */   final SOAPFaultException createMUSOAPFaultException(Set<QName> notUnderstoodHeaders)
/*     */   {
/*     */     try
/*     */     {
/* 128 */       SOAPFault fault = this.soapVersion.saajSoapFactory.createFault("One or more mandatory SOAP header blocks not understood", this.soapVersion.faultCodeMustUnderstand);
/*     */ 
/* 131 */       fault.setFaultString("MustUnderstand headers:" + notUnderstoodHeaders + " are not understood");
/*     */ 
/* 133 */       return new SOAPFaultException(fault);
/*     */     } catch (SOAPException e) {
/* 135 */       throw new WebServiceException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   final Message createMUSOAPFaultMessage(Set<QName> notUnderstoodHeaders)
/*     */   {
/*     */     try
/*     */     {
/* 150 */       String faultString = "One or more mandatory SOAP header blocks not understood";
/* 151 */       if (this.soapVersion == SOAPVersion.SOAP_11) {
/* 152 */         faultString = "MustUnderstand headers:" + notUnderstoodHeaders + " are not understood";
/*     */       }
/* 154 */       Message muFaultMessage = SOAPFaultBuilder.createSOAPFaultMessage(this.soapVersion, faultString, this.soapVersion.faultCodeMustUnderstand);
/*     */ 
/* 157 */       if (this.soapVersion == SOAPVersion.SOAP_12) {
/* 158 */         addHeader(muFaultMessage, notUnderstoodHeaders);
/*     */       }
/* 160 */       return muFaultMessage;
/*     */     } catch (SOAPException e) {
/* 162 */       throw new WebServiceException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static void addHeader(Message m, Set<QName> notUnderstoodHeaders) throws SOAPException {
/* 167 */     for (QName qname : notUnderstoodHeaders) {
/* 168 */       SOAPElement soapEl = SOAPVersion.SOAP_12.saajSoapFactory.createElement(MU_HEADER_DETAIL);
/* 169 */       soapEl.addNamespaceDeclaration("abc", qname.getNamespaceURI());
/* 170 */       soapEl.setAttribute("qname", "abc:" + qname.getLocalPart());
/* 171 */       Header header = new DOMHeader(soapEl);
/* 172 */       m.getHeaders().add(header);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.protocol.soap.MUTube
 * JD-Core Version:    0.6.2
 */