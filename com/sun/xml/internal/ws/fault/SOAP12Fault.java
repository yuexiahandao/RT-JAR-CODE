/*     */ package com.sun.xml.internal.ws.fault;
/*     */ 
/*     */ import com.sun.xml.internal.ws.api.SOAPVersion;
/*     */ import com.sun.xml.internal.ws.util.DOMUtil;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import javax.xml.bind.annotation.XmlAccessType;
/*     */ import javax.xml.bind.annotation.XmlAccessorType;
/*     */ import javax.xml.bind.annotation.XmlElement;
/*     */ import javax.xml.bind.annotation.XmlRootElement;
/*     */ import javax.xml.bind.annotation.XmlTransient;
/*     */ import javax.xml.bind.annotation.XmlType;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.soap.Detail;
/*     */ import javax.xml.soap.SOAPException;
/*     */ import javax.xml.soap.SOAPFactory;
/*     */ import javax.xml.soap.SOAPFault;
/*     */ import javax.xml.ws.WebServiceException;
/*     */ import javax.xml.ws.soap.SOAPFaultException;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ 
/*     */ @XmlRootElement(name="Fault", namespace="http://www.w3.org/2003/05/soap-envelope")
/*     */ @XmlAccessorType(XmlAccessType.FIELD)
/*     */ @XmlType(name="", propOrder={"code", "reason", "node", "role", "detail"})
/*     */ class SOAP12Fault extends SOAPFaultBuilder
/*     */ {
/*     */ 
/*     */   @XmlTransient
/*     */   private static final String ns = "http://www.w3.org/2003/05/soap-envelope";
/*     */ 
/*     */   @XmlElement(namespace="http://www.w3.org/2003/05/soap-envelope", name="Code")
/*     */   private CodeType code;
/*     */ 
/*     */   @XmlElement(namespace="http://www.w3.org/2003/05/soap-envelope", name="Reason")
/*     */   private ReasonType reason;
/*     */ 
/*     */   @XmlElement(namespace="http://www.w3.org/2003/05/soap-envelope", name="Node")
/*     */   private String node;
/*     */ 
/*     */   @XmlElement(namespace="http://www.w3.org/2003/05/soap-envelope", name="Role")
/*     */   private String role;
/*     */ 
/*     */   @XmlElement(namespace="http://www.w3.org/2003/05/soap-envelope", name="Detail")
/*     */   private DetailType detail;
/*     */ 
/*     */   SOAP12Fault()
/*     */   {
/*     */   }
/*     */ 
/*     */   SOAP12Fault(CodeType code, ReasonType reason, String node, String role, DetailType detail)
/*     */   {
/* 108 */     this.code = code;
/* 109 */     this.reason = reason;
/* 110 */     this.node = node;
/* 111 */     this.role = role;
/* 112 */     this.detail = detail;
/*     */   }
/*     */ 
/*     */   SOAP12Fault(CodeType code, ReasonType reason, String node, String role, Element detailObject) {
/* 116 */     this.code = code;
/* 117 */     this.reason = reason;
/* 118 */     this.node = node;
/* 119 */     this.role = role;
/* 120 */     if (detailObject != null)
/* 121 */       if ((detailObject.getNamespaceURI().equals("http://www.w3.org/2003/05/soap-envelope")) && (detailObject.getLocalName().equals("Detail"))) {
/* 122 */         this.detail = new DetailType();
/* 123 */         for (Element detailEntry : DOMUtil.getChildElements(detailObject))
/* 124 */           this.detail.getDetails().add(detailEntry);
/*     */       }
/*     */       else {
/* 127 */         this.detail = new DetailType(detailObject);
/*     */       }
/*     */   }
/*     */ 
/*     */   SOAP12Fault(SOAPFault fault)
/*     */   {
/* 133 */     this.code = new CodeType(fault.getFaultCodeAsQName());
/*     */     try {
/* 135 */       fillFaultSubCodes(fault);
/*     */     } catch (SOAPException e) {
/* 137 */       throw new WebServiceException(e);
/*     */     }
/*     */ 
/* 140 */     this.reason = new ReasonType(fault.getFaultString());
/* 141 */     this.role = fault.getFaultRole();
/* 142 */     this.node = fault.getFaultNode();
/* 143 */     if (fault.getDetail() != null) {
/* 144 */       this.detail = new DetailType();
/* 145 */       Iterator iter = fault.getDetail().getDetailEntries();
/* 146 */       while (iter.hasNext()) {
/* 147 */         Element fd = (Element)iter.next();
/* 148 */         this.detail.getDetails().add(fd);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   SOAP12Fault(QName code, String reason, Element detailObject) {
/* 154 */     this(new CodeType(code), new ReasonType(reason), null, null, detailObject);
/*     */   }
/*     */ 
/*     */   CodeType getCode() {
/* 158 */     return this.code;
/*     */   }
/*     */ 
/*     */   ReasonType getReason() {
/* 162 */     return this.reason;
/*     */   }
/*     */ 
/*     */   String getNode() {
/* 166 */     return this.node;
/*     */   }
/*     */ 
/*     */   String getRole() {
/* 170 */     return this.role;
/*     */   }
/*     */ 
/*     */   DetailType getDetail()
/*     */   {
/* 175 */     return this.detail;
/*     */   }
/*     */ 
/*     */   void setDetail(DetailType detail)
/*     */   {
/* 180 */     this.detail = detail;
/*     */   }
/*     */ 
/*     */   String getFaultString()
/*     */   {
/* 185 */     return ((TextType)this.reason.texts().get(0)).getText();
/*     */   }
/*     */ 
/*     */   protected Throwable getProtocolException() {
/*     */     try {
/* 190 */       SOAPFault fault = SOAPVersion.SOAP_12.saajSoapFactory.createFault();
/* 191 */       if (this.reason != null) {
/* 192 */         for (TextType tt : this.reason.texts()) {
/* 193 */           fault.setFaultString(tt.getText());
/*     */         }
/*     */       }
/*     */ 
/* 197 */       if (this.code != null) {
/* 198 */         fault.setFaultCode(this.code.getValue());
/* 199 */         fillFaultSubCodes(fault, this.code.getSubcode());
/*     */       }
/*     */       Detail detail;
/* 202 */       if ((this.detail != null) && (this.detail.getDetail(0) != null)) {
/* 203 */         detail = fault.addDetail();
/* 204 */         for (Node obj : this.detail.getDetails()) {
/* 205 */           Node n = fault.getOwnerDocument().importNode(obj, true);
/* 206 */           detail.appendChild(n);
/*     */         }
/*     */       }
/*     */ 
/* 210 */       if (this.node != null) {
/* 211 */         fault.setFaultNode(this.node);
/*     */       }
/*     */ 
/* 214 */       return new SOAPFaultException(fault);
/*     */     } catch (SOAPException e) {
/* 216 */       throw new WebServiceException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void fillFaultSubCodes(SOAPFault fault, SubcodeType subcode)
/*     */     throws SOAPException
/*     */   {
/* 224 */     if (subcode != null) {
/* 225 */       fault.appendFaultSubcode(subcode.getValue());
/* 226 */       fillFaultSubCodes(fault, subcode.getSubcode());
/*     */     }
/*     */   }
/*     */ 
/*     */   private void fillFaultSubCodes(SOAPFault fault)
/*     */     throws SOAPException
/*     */   {
/* 234 */     Iterator subcodes = fault.getFaultSubcodes();
/* 235 */     SubcodeType firstSct = null;
/* 236 */     while (subcodes.hasNext()) {
/* 237 */       QName subcode = (QName)subcodes.next();
/* 238 */       if (firstSct == null) {
/* 239 */         firstSct = new SubcodeType(subcode);
/* 240 */         this.code.setSubcode(firstSct);
/*     */       }
/*     */       else {
/* 243 */         SubcodeType nextSct = new SubcodeType(subcode);
/* 244 */         firstSct.setSubcode(nextSct);
/* 245 */         firstSct = nextSct;
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.fault.SOAP12Fault
 * JD-Core Version:    0.6.2
 */