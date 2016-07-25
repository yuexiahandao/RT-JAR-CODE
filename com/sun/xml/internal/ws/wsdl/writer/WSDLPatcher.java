/*     */ package com.sun.xml.internal.ws.wsdl.writer;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.istack.internal.Nullable;
/*     */ import com.sun.xml.internal.ws.addressing.W3CAddressingConstants;
/*     */ import com.sun.xml.internal.ws.api.server.PortAddressResolver;
/*     */ import com.sun.xml.internal.ws.util.xml.XMLStreamReaderToXMLStreamWriter;
/*     */ import com.sun.xml.internal.ws.wsdl.parser.WSDLConstants;
/*     */ import java.util.logging.Logger;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamReader;
/*     */ import javax.xml.stream.XMLStreamWriter;
/*     */ 
/*     */ public final class WSDLPatcher extends XMLStreamReaderToXMLStreamWriter
/*     */ {
/*     */   private static final String NS_XSD = "http://www.w3.org/2001/XMLSchema";
/*  49 */   private static final QName SCHEMA_INCLUDE_QNAME = new QName("http://www.w3.org/2001/XMLSchema", "include");
/*  50 */   private static final QName SCHEMA_IMPORT_QNAME = new QName("http://www.w3.org/2001/XMLSchema", "import");
/*  51 */   private static final QName SCHEMA_REDEFINE_QNAME = new QName("http://www.w3.org/2001/XMLSchema", "redefine");
/*     */ 
/*  53 */   private static final Logger logger = Logger.getLogger("com.sun.xml.internal.ws.wsdl.patcher");
/*     */   private final DocumentLocationResolver docResolver;
/*     */   private final PortAddressResolver portAddressResolver;
/*     */   private String targetNamespace;
/*     */   private QName serviceName;
/*     */   private QName portName;
/*     */   private String portAddress;
/*     */   private boolean inEpr;
/*     */   private boolean inEprAddress;
/*     */ 
/*     */   public WSDLPatcher(@NotNull PortAddressResolver portAddressResolver, @NotNull DocumentLocationResolver docResolver)
/*     */   {
/*  83 */     this.portAddressResolver = portAddressResolver;
/*  84 */     this.docResolver = docResolver;
/*     */   }
/*     */ 
/*     */   protected void handleAttribute(int i) throws XMLStreamException
/*     */   {
/*  89 */     QName name = this.in.getName();
/*  90 */     String attLocalName = this.in.getAttributeLocalName(i);
/*     */ 
/*  92 */     if (((name.equals(SCHEMA_INCLUDE_QNAME)) && (attLocalName.equals("schemaLocation"))) || ((name.equals(SCHEMA_IMPORT_QNAME)) && (attLocalName.equals("schemaLocation"))) || ((name.equals(SCHEMA_REDEFINE_QNAME)) && (attLocalName.equals("schemaLocation"))) || ((name.equals(WSDLConstants.QNAME_IMPORT)) && (attLocalName.equals("location"))))
/*     */     {
/*  98 */       String relPath = this.in.getAttributeValue(i);
/*  99 */       String actualPath = getPatchedImportLocation(relPath);
/* 100 */       if (actualPath == null) {
/* 101 */         return;
/*     */       }
/*     */ 
/* 104 */       logger.fine("Fixing the relative location:" + relPath + " with absolute location:" + actualPath);
/*     */ 
/* 106 */       writeAttribute(i, actualPath);
/* 107 */       return;
/*     */     }
/*     */ 
/* 110 */     if ((name.equals(WSDLConstants.NS_SOAP_BINDING_ADDRESS)) || (name.equals(WSDLConstants.NS_SOAP12_BINDING_ADDRESS)))
/*     */     {
/* 113 */       if (attLocalName.equals("location")) {
/* 114 */         this.portAddress = this.in.getAttributeValue(i);
/* 115 */         String value = getAddressLocation();
/* 116 */         if (value != null) {
/* 117 */           logger.fine("Service:" + this.serviceName + " port:" + this.portName + " current address " + this.portAddress + " Patching it with " + value);
/*     */ 
/* 119 */           writeAttribute(i, value);
/* 120 */           return;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 125 */     super.handleAttribute(i);
/*     */   }
/*     */ 
/*     */   private void writeAttribute(int i, String value)
/*     */     throws XMLStreamException
/*     */   {
/* 135 */     String nsUri = this.in.getAttributeNamespace(i);
/* 136 */     if (nsUri != null)
/* 137 */       this.out.writeAttribute(this.in.getAttributePrefix(i), nsUri, this.in.getAttributeLocalName(i), value);
/*     */     else
/* 139 */       this.out.writeAttribute(this.in.getAttributeLocalName(i), value);
/*     */   }
/*     */ 
/*     */   protected void handleStartElement() throws XMLStreamException
/*     */   {
/* 144 */     QName name = this.in.getName();
/*     */ 
/* 146 */     if (name.equals(WSDLConstants.QNAME_DEFINITIONS)) {
/* 147 */       String value = this.in.getAttributeValue(null, "targetNamespace");
/* 148 */       if (value != null)
/* 149 */         this.targetNamespace = value;
/*     */     }
/* 151 */     else if (name.equals(WSDLConstants.QNAME_SERVICE)) {
/* 152 */       String value = this.in.getAttributeValue(null, "name");
/* 153 */       if (value != null)
/* 154 */         this.serviceName = new QName(this.targetNamespace, value);
/*     */     }
/* 156 */     else if (name.equals(WSDLConstants.QNAME_PORT)) {
/* 157 */       String value = this.in.getAttributeValue(null, "name");
/* 158 */       if (value != null)
/* 159 */         this.portName = new QName(this.targetNamespace, value);
/*     */     }
/* 161 */     else if (name.equals(W3CAddressingConstants.WSA_EPR_QNAME)) {
/* 162 */       if ((this.serviceName != null) && (this.portName != null))
/* 163 */         this.inEpr = true;
/*     */     }
/* 165 */     else if ((name.equals(W3CAddressingConstants.WSA_ADDRESS_QNAME)) && 
/* 166 */       (this.inEpr)) {
/* 167 */       this.inEprAddress = true;
/*     */     }
/*     */ 
/* 170 */     super.handleStartElement();
/*     */   }
/*     */ 
/*     */   protected void handleEndElement() throws XMLStreamException
/*     */   {
/* 175 */     QName name = this.in.getName();
/* 176 */     if (name.equals(WSDLConstants.QNAME_SERVICE)) {
/* 177 */       this.serviceName = null;
/* 178 */     } else if (name.equals(WSDLConstants.QNAME_PORT)) {
/* 179 */       this.portName = null;
/* 180 */     } else if (name.equals(W3CAddressingConstants.WSA_EPR_QNAME)) {
/* 181 */       if (this.inEpr)
/* 182 */         this.inEpr = false;
/*     */     }
/* 184 */     else if ((name.equals(W3CAddressingConstants.WSA_ADDRESS_QNAME)) && 
/* 185 */       (this.inEprAddress)) {
/* 186 */       String value = getAddressLocation();
/* 187 */       if (value != null) {
/* 188 */         logger.fine("Fixing EPR Address for service:" + this.serviceName + " port:" + this.portName + " address with " + value);
/*     */ 
/* 190 */         this.out.writeCharacters(value);
/*     */       }
/* 192 */       this.inEprAddress = false;
/*     */     }
/*     */ 
/* 195 */     super.handleEndElement();
/*     */   }
/*     */ 
/*     */   protected void handleCharacters()
/*     */     throws XMLStreamException
/*     */   {
/* 201 */     if (this.inEprAddress) {
/* 202 */       String value = getAddressLocation();
/* 203 */       if (value != null)
/*     */       {
/* 205 */         return;
/*     */       }
/*     */     }
/* 208 */     super.handleCharacters();
/*     */   }
/*     */ 
/*     */   @Nullable
/*     */   private String getPatchedImportLocation(String relPath)
/*     */   {
/* 219 */     return this.docResolver.getLocationFor(null, relPath);
/*     */   }
/*     */ 
/*     */   private String getAddressLocation()
/*     */   {
/* 229 */     return (this.portAddressResolver == null) || (this.portName == null) ? null : this.portAddressResolver.getAddressFor(this.serviceName, this.portName.getLocalPart(), this.portAddress);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.wsdl.writer.WSDLPatcher
 * JD-Core Version:    0.6.2
 */