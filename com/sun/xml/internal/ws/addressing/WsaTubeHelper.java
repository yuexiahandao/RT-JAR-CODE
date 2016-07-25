/*     */ package com.sun.xml.internal.ws.addressing;
/*     */ 
/*     */ import com.sun.istack.internal.Nullable;
/*     */ import com.sun.xml.internal.bind.api.TypeReference;
/*     */ import com.sun.xml.internal.ws.addressing.model.InvalidAddressingHeaderException;
/*     */ import com.sun.xml.internal.ws.addressing.model.MissingAddressingHeaderException;
/*     */ import com.sun.xml.internal.ws.api.SOAPVersion;
/*     */ import com.sun.xml.internal.ws.api.WSBinding;
/*     */ import com.sun.xml.internal.ws.api.addressing.AddressingVersion;
/*     */ import com.sun.xml.internal.ws.api.message.Message;
/*     */ import com.sun.xml.internal.ws.api.message.Packet;
/*     */ import com.sun.xml.internal.ws.api.model.SEIModel;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundOperation;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundPortType;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLFault;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLInput;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLOperation;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLOutput;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
/*     */ import com.sun.xml.internal.ws.model.CheckedExceptionImpl;
/*     */ import com.sun.xml.internal.ws.model.JavaMethodImpl;
/*     */ import com.sun.xml.internal.ws.model.wsdl.WSDLInputImpl;
/*     */ import com.sun.xml.internal.ws.model.wsdl.WSDLOperationImpl;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.soap.Detail;
/*     */ import javax.xml.soap.SOAPBody;
/*     */ import javax.xml.soap.SOAPConstants;
/*     */ import javax.xml.soap.SOAPException;
/*     */ import javax.xml.soap.SOAPFactory;
/*     */ import javax.xml.soap.SOAPFault;
/*     */ import javax.xml.soap.SOAPMessage;
/*     */ import javax.xml.ws.WebServiceException;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ 
/*     */ public abstract class WsaTubeHelper
/*     */ {
/*     */   protected SEIModel seiModel;
/*     */   protected WSDLPort wsdlPort;
/*     */   protected WSBinding binding;
/*     */   protected final SOAPVersion soapVer;
/*     */   protected final AddressingVersion addVer;
/*     */ 
/*     */   public WsaTubeHelper(WSBinding binding, SEIModel seiModel, WSDLPort wsdlPort)
/*     */   {
/*  64 */     this.binding = binding;
/*  65 */     this.wsdlPort = wsdlPort;
/*  66 */     this.seiModel = seiModel;
/*  67 */     this.soapVer = binding.getSOAPVersion();
/*  68 */     this.addVer = binding.getAddressingVersion();
/*     */   }
/*     */ 
/*     */   public String getFaultAction(Packet requestPacket, Packet responsePacket)
/*     */   {
/*  73 */     String action = null;
/*  74 */     if (this.seiModel != null) {
/*  75 */       action = getFaultActionFromSEIModel(requestPacket, responsePacket);
/*     */     }
/*  77 */     if (action != null) {
/*  78 */       return action;
/*     */     }
/*  80 */     action = this.addVer.getDefaultFaultAction();
/*  81 */     if (this.wsdlPort != null) {
/*  82 */       QName wsdlOp = requestPacket.getWSDLOperation();
/*  83 */       if (wsdlOp != null) {
/*  84 */         WSDLBoundOperation wbo = this.wsdlPort.getBinding().get(wsdlOp);
/*  85 */         return getFaultAction(wbo, responsePacket);
/*     */       }
/*     */     }
/*  88 */     return action;
/*     */   }
/*     */ 
/*     */   String getFaultActionFromSEIModel(Packet requestPacket, Packet responsePacket) {
/*  92 */     String action = null;
/*  93 */     if ((this.seiModel == null) || (this.wsdlPort == null))
/*  94 */       return action;
/*     */     try
/*     */     {
/*  97 */       SOAPMessage sm = responsePacket.getMessage().copy().readAsSOAPMessage();
/*  98 */       if (sm == null) {
/*  99 */         return action;
/*     */       }
/* 101 */       if (sm.getSOAPBody() == null) {
/* 102 */         return action;
/*     */       }
/* 104 */       if (sm.getSOAPBody().getFault() == null) {
/* 105 */         return action;
/*     */       }
/* 107 */       Detail detail = sm.getSOAPBody().getFault().getDetail();
/* 108 */       if (detail == null) {
/* 109 */         return action;
/*     */       }
/* 111 */       String ns = detail.getFirstChild().getNamespaceURI();
/* 112 */       String name = detail.getFirstChild().getLocalName();
/*     */ 
/* 114 */       QName wsdlOp = requestPacket.getWSDLOperation();
/* 115 */       JavaMethodImpl jm = (JavaMethodImpl)this.seiModel.getJavaMethodForWsdlOperation(wsdlOp);
/* 116 */       for (CheckedExceptionImpl ce : jm.getCheckedExceptions()) {
/* 117 */         if ((ce.getDetailType().tagName.getLocalPart().equals(name)) && (ce.getDetailType().tagName.getNamespaceURI().equals(ns)))
/*     */         {
/* 119 */           return ce.getFaultAction();
/*     */         }
/*     */       }
/* 122 */       return action;
/*     */     } catch (SOAPException e) {
/* 124 */       throw new WebServiceException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   String getFaultAction(@Nullable WSDLBoundOperation wbo, Packet responsePacket) {
/* 129 */     String action = this.addVer.getDefaultFaultAction();
/* 130 */     if (wbo == null)
/* 131 */       return action;
/*     */     try
/*     */     {
/* 134 */       SOAPMessage sm = responsePacket.getMessage().copy().readAsSOAPMessage();
/* 135 */       if (sm == null) {
/* 136 */         return action;
/*     */       }
/* 138 */       if (sm.getSOAPBody() == null) {
/* 139 */         return action;
/*     */       }
/* 141 */       if (sm.getSOAPBody().getFault() == null) {
/* 142 */         return action;
/*     */       }
/* 144 */       Detail detail = sm.getSOAPBody().getFault().getDetail();
/* 145 */       if (detail == null) {
/* 146 */         return action;
/*     */       }
/* 148 */       String ns = detail.getFirstChild().getNamespaceURI();
/* 149 */       String name = detail.getFirstChild().getLocalName();
/*     */ 
/* 151 */       WSDLOperation o = wbo.getOperation();
/*     */ 
/* 153 */       WSDLFault fault = o.getFault(new QName(ns, name));
/* 154 */       if (fault == null) {
/* 155 */         return action;
/*     */       }
/* 157 */       WSDLOperationImpl impl = (WSDLOperationImpl)o;
/* 158 */       return fault.getAction();
/*     */     }
/*     */     catch (SOAPException e)
/*     */     {
/* 162 */       throw new WebServiceException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public String getInputAction(Packet packet) {
/* 167 */     String action = null;
/*     */ 
/* 169 */     if (this.wsdlPort != null) {
/* 170 */       QName wsdlOp = packet.getWSDLOperation();
/* 171 */       if (wsdlOp != null) {
/* 172 */         WSDLBoundOperation wbo = this.wsdlPort.getBinding().get(wsdlOp);
/* 173 */         WSDLOperation op = wbo.getOperation();
/* 174 */         action = op.getInput().getAction();
/*     */       }
/*     */     }
/*     */ 
/* 178 */     return action;
/*     */   }
/*     */ 
/*     */   public String getEffectiveInputAction(Packet packet)
/*     */   {
/* 190 */     if ((packet.soapAction != null) && (!packet.soapAction.equals("")))
/* 191 */       return packet.soapAction;
/* 192 */     String action = null;
/*     */ 
/* 194 */     if (this.wsdlPort != null) {
/* 195 */       QName wsdlOp = packet.getWSDLOperation();
/* 196 */       if (wsdlOp != null) {
/* 197 */         WSDLBoundOperation wbo = this.wsdlPort.getBinding().get(wsdlOp);
/* 198 */         WSDLOperation op = wbo.getOperation();
/* 199 */         action = op.getInput().getAction();
/*     */       } else {
/* 201 */         action = packet.soapAction;
/*     */       }
/*     */     } else { action = packet.soapAction; }
/*     */ 
/* 205 */     return action;
/*     */   }
/*     */ 
/*     */   public boolean isInputActionDefault(Packet packet) {
/* 209 */     if (this.wsdlPort == null)
/* 210 */       return false;
/* 211 */     QName wsdlOp = packet.getWSDLOperation();
/* 212 */     if (wsdlOp == null)
/* 213 */       return false;
/* 214 */     WSDLBoundOperation wbo = this.wsdlPort.getBinding().get(wsdlOp);
/* 215 */     WSDLOperation op = wbo.getOperation();
/* 216 */     return ((WSDLOperationImpl)op).getInput().isDefaultAction();
/*     */   }
/*     */ 
/*     */   public String getSOAPAction(Packet packet)
/*     */   {
/* 221 */     String action = "";
/*     */ 
/* 223 */     if ((packet == null) || (packet.getMessage() == null)) {
/* 224 */       return action;
/*     */     }
/* 226 */     if (this.wsdlPort == null) {
/* 227 */       return action;
/*     */     }
/* 229 */     QName opName = packet.getWSDLOperation();
/* 230 */     if (opName == null) {
/* 231 */       return action;
/*     */     }
/* 233 */     WSDLBoundOperation op = this.wsdlPort.getBinding().get(opName);
/* 234 */     action = op.getSOAPAction();
/* 235 */     return action;
/*     */   }
/*     */ 
/*     */   public String getOutputAction(Packet packet)
/*     */   {
/* 240 */     String action = null;
/* 241 */     QName wsdlOp = packet.getWSDLOperation();
/* 242 */     if (wsdlOp != null) {
/* 243 */       if (this.seiModel != null) {
/* 244 */         JavaMethodImpl jm = (JavaMethodImpl)this.seiModel.getJavaMethodForWsdlOperation(wsdlOp);
/* 245 */         if ((jm != null) && (jm.getOutputAction() != null) && (!jm.getOutputAction().equals(""))) {
/* 246 */           return jm.getOutputAction();
/*     */         }
/*     */       }
/* 249 */       if (this.wsdlPort != null) {
/* 250 */         WSDLBoundOperation wbo = this.wsdlPort.getBinding().get(wsdlOp);
/* 251 */         return getOutputAction(wbo);
/*     */       }
/*     */     }
/* 254 */     return action;
/*     */   }
/*     */ 
/*     */   String getOutputAction(@Nullable WSDLBoundOperation wbo) {
/* 258 */     String action = "http://jax-ws.dev.java.net/addressing/output-action-not-set";
/* 259 */     if (wbo != null) {
/* 260 */       WSDLOutput op = wbo.getOperation().getOutput();
/* 261 */       if (op != null)
/* 262 */         action = op.getAction();
/*     */     }
/* 264 */     return action;
/*     */   }
/*     */   public SOAPFault createInvalidAddressingHeaderFault(InvalidAddressingHeaderException e, AddressingVersion av) {
/* 267 */     QName name = e.getProblemHeader();
/* 268 */     QName subsubcode = e.getSubsubcode();
/* 269 */     QName subcode = av.invalidMapTag;
/* 270 */     String faultstring = String.format(av.getInvalidMapText(), new Object[] { name, subsubcode });
/*     */     try
/*     */     {
/*     */       SOAPFault fault;
/* 275 */       if (this.soapVer == SOAPVersion.SOAP_12) {
/* 276 */         SOAPFactory factory = SOAPVersion.SOAP_12.saajSoapFactory;
/* 277 */         SOAPFault fault = factory.createFault();
/* 278 */         fault.setFaultCode(SOAPConstants.SOAP_SENDER_FAULT);
/* 279 */         fault.appendFaultSubcode(subcode);
/* 280 */         fault.appendFaultSubcode(subsubcode);
/* 281 */         getInvalidMapDetail(name, fault.addDetail());
/*     */       } else {
/* 283 */         SOAPFactory factory = SOAPVersion.SOAP_11.saajSoapFactory;
/* 284 */         fault = factory.createFault();
/* 285 */         fault.setFaultCode(subsubcode);
/*     */       }
/*     */ 
/* 288 */       fault.setFaultString(faultstring);
/*     */ 
/* 290 */       return fault;
/*     */     } catch (SOAPException se) {
/* 292 */       throw new WebServiceException(se);
/*     */     }
/*     */   }
/*     */ 
/*     */   public SOAPFault newMapRequiredFault(MissingAddressingHeaderException e) {
/* 297 */     QName subcode = this.addVer.mapRequiredTag;
/* 298 */     QName subsubcode = this.addVer.mapRequiredTag;
/* 299 */     String faultstring = this.addVer.getMapRequiredText();
/*     */     try
/*     */     {
/*     */       SOAPFault fault;
/* 304 */       if (this.soapVer == SOAPVersion.SOAP_12) {
/* 305 */         SOAPFactory factory = SOAPVersion.SOAP_12.saajSoapFactory;
/* 306 */         SOAPFault fault = factory.createFault();
/* 307 */         fault.setFaultCode(SOAPConstants.SOAP_SENDER_FAULT);
/* 308 */         fault.appendFaultSubcode(subcode);
/* 309 */         fault.appendFaultSubcode(subsubcode);
/* 310 */         getMapRequiredDetail(e.getMissingHeaderQName(), fault.addDetail());
/*     */       } else {
/* 312 */         SOAPFactory factory = SOAPVersion.SOAP_11.saajSoapFactory;
/* 313 */         fault = factory.createFault();
/* 314 */         fault.setFaultCode(subsubcode);
/*     */       }
/*     */ 
/* 317 */       fault.setFaultString(faultstring);
/*     */ 
/* 319 */       return fault;
/*     */     } catch (SOAPException se) {
/* 321 */       throw new WebServiceException(se);
/*     */     }
/*     */   }
/*     */ 
/*     */   public abstract void getProblemActionDetail(String paramString, Element paramElement);
/*     */ 
/*     */   public abstract void getInvalidMapDetail(QName paramQName, Element paramElement);
/*     */ 
/*     */   public abstract void getMapRequiredDetail(QName paramQName, Element paramElement);
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.addressing.WsaTubeHelper
 * JD-Core Version:    0.6.2
 */