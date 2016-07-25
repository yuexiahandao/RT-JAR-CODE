/*     */ package com.sun.xml.internal.ws.fault;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.istack.internal.Nullable;
/*     */ import com.sun.xml.internal.bind.api.Bridge;
/*     */ import com.sun.xml.internal.bind.api.JAXBRIContext;
/*     */ import com.sun.xml.internal.bind.api.TypeReference;
/*     */ import com.sun.xml.internal.ws.api.SOAPVersion;
/*     */ import com.sun.xml.internal.ws.api.message.Message;
/*     */ import com.sun.xml.internal.ws.api.model.CheckedException;
/*     */ import com.sun.xml.internal.ws.api.model.ExceptionType;
/*     */ import com.sun.xml.internal.ws.encoding.soap.SerializationException;
/*     */ import com.sun.xml.internal.ws.message.FaultMessage;
/*     */ import com.sun.xml.internal.ws.message.jaxb.JAXBMessage;
/*     */ import com.sun.xml.internal.ws.model.CheckedExceptionImpl;
/*     */ import com.sun.xml.internal.ws.util.DOMUtil;
/*     */ import com.sun.xml.internal.ws.util.StringUtils;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.ReflectPermission;
/*     */ import java.security.AccessControlContext;
/*     */ import java.security.AccessController;
/*     */ import java.security.Permissions;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.ProtectionDomain;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.xml.bind.JAXBContext;
/*     */ import javax.xml.bind.JAXBException;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.soap.Detail;
/*     */ import javax.xml.soap.DetailEntry;
/*     */ import javax.xml.soap.SOAPFault;
/*     */ import javax.xml.transform.dom.DOMResult;
/*     */ import javax.xml.ws.ProtocolException;
/*     */ import javax.xml.ws.WebServiceException;
/*     */ import javax.xml.ws.soap.SOAPFaultException;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ 
/*     */ public abstract class SOAPFaultBuilder
/*     */ {
/* 555 */   private static final JAXBRIContext JAXB_CONTEXT = createJAXBContext();
/*     */ 
/* 539 */   private static final Logger logger = Logger.getLogger(SOAPFaultBuilder.class.getName());
/*     */   public static boolean captureStackTrace;
/* 546 */   static final String CAPTURE_STACK_TRACE_PROPERTY = SOAPFaultBuilder.class.getName() + ".captureStackTrace";
/*     */ 
/*     */   abstract DetailType getDetail();
/*     */ 
/*     */   abstract void setDetail(DetailType paramDetailType);
/*     */ 
/*     */   @Nullable
/*     */   public QName getFirstDetailEntryName()
/*     */   {
/*  90 */     DetailType dt = getDetail();
/*  91 */     if (dt != null) {
/*  92 */       Node entry = dt.getDetail(0);
/*  93 */       if (entry != null) {
/*  94 */         return new QName(entry.getNamespaceURI(), entry.getLocalName());
/*     */       }
/*     */     }
/*  97 */     return null;
/*     */   }
/*     */ 
/*     */   abstract String getFaultString();
/*     */ 
/*     */   public Throwable createException(Map<QName, CheckedExceptionImpl> exceptions)
/*     */     throws JAXBException
/*     */   {
/* 109 */     DetailType dt = getDetail();
/* 110 */     Node detail = null;
/* 111 */     if (dt != null) detail = dt.getDetail(0);
/*     */ 
/* 114 */     if ((detail == null) || (exceptions == null))
/*     */     {
/* 117 */       return attachServerException(getProtocolException());
/*     */     }
/*     */ 
/* 121 */     QName detailName = new QName(detail.getNamespaceURI(), detail.getLocalName());
/* 122 */     CheckedExceptionImpl ce = (CheckedExceptionImpl)exceptions.get(detailName);
/* 123 */     if (ce == null)
/*     */     {
/* 125 */       return attachServerException(getProtocolException());
/*     */     }
/*     */ 
/* 129 */     if (ce.getExceptionType().equals(ExceptionType.UserDefined)) {
/* 130 */       return attachServerException(createUserDefinedException(ce));
/*     */     }
/*     */ 
/* 133 */     Class exceptionClass = ce.getExceptionClass();
/*     */     try {
/* 135 */       Constructor constructor = exceptionClass.getConstructor(new Class[] { String.class, (Class)ce.getDetailType().type });
/* 136 */       Exception exception = (Exception)constructor.newInstance(new Object[] { getFaultString(), getJAXBObject(detail, ce) });
/* 137 */       return attachServerException(exception);
/*     */     } catch (Exception e) {
/* 139 */       throw new WebServiceException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public static Message createSOAPFaultMessage(@NotNull SOAPVersion soapVersion, @NotNull ProtocolException ex, @Nullable QName faultcode)
/*     */   {
/* 153 */     Object detail = getFaultDetail(null, ex);
/* 154 */     if (soapVersion == SOAPVersion.SOAP_12)
/* 155 */       return createSOAP12Fault(soapVersion, ex, detail, null, faultcode);
/* 156 */     return createSOAP11Fault(soapVersion, ex, detail, null, faultcode);
/*     */   }
/*     */ 
/*     */   public static Message createSOAPFaultMessage(SOAPVersion soapVersion, CheckedExceptionImpl ceModel, Throwable ex)
/*     */   {
/* 179 */     return createSOAPFaultMessage(soapVersion, ceModel, ex, null);
/*     */   }
/*     */ 
/*     */   public static Message createSOAPFaultMessage(SOAPVersion soapVersion, CheckedExceptionImpl ceModel, Throwable ex, QName faultCode)
/*     */   {
/* 188 */     Object detail = getFaultDetail(ceModel, ex);
/* 189 */     if (soapVersion == SOAPVersion.SOAP_12)
/* 190 */       return createSOAP12Fault(soapVersion, ex, detail, ceModel, faultCode);
/* 191 */     return createSOAP11Fault(soapVersion, ex, detail, ceModel, faultCode);
/*     */   }
/*     */ 
/*     */   public static Message createSOAPFaultMessage(SOAPVersion soapVersion, String faultString, QName faultCode)
/*     */   {
/* 218 */     if (faultCode == null)
/* 219 */       faultCode = getDefaultFaultCode(soapVersion);
/* 220 */     return createSOAPFaultMessage(soapVersion, faultString, faultCode, null);
/*     */   }
/*     */ 
/*     */   public static Message createSOAPFaultMessage(SOAPVersion soapVersion, SOAPFault fault) {
/* 224 */     switch (2.$SwitchMap$com$sun$xml$internal$ws$api$SOAPVersion[soapVersion.ordinal()]) {
/*     */     case 1:
/* 226 */       return JAXBMessage.create(JAXB_CONTEXT, new SOAP11Fault(fault), soapVersion);
/*     */     case 2:
/* 228 */       return JAXBMessage.create(JAXB_CONTEXT, new SOAP12Fault(fault), soapVersion);
/*     */     }
/* 230 */     throw new AssertionError();
/*     */   }
/*     */ 
/*     */   private static Message createSOAPFaultMessage(SOAPVersion soapVersion, String faultString, QName faultCode, Element detail)
/*     */   {
/* 235 */     switch (2.$SwitchMap$com$sun$xml$internal$ws$api$SOAPVersion[soapVersion.ordinal()]) {
/*     */     case 1:
/* 237 */       return JAXBMessage.create(JAXB_CONTEXT, new SOAP11Fault(faultCode, faultString, null, detail), soapVersion);
/*     */     case 2:
/* 239 */       return JAXBMessage.create(JAXB_CONTEXT, new SOAP12Fault(faultCode, faultString, detail), soapVersion);
/*     */     }
/* 241 */     throw new AssertionError();
/*     */   }
/*     */ 
/*     */   final void captureStackTrace(@Nullable Throwable t)
/*     */   {
/* 250 */     if (t == null) return;
/* 251 */     if (!captureStackTrace) return;
/*     */     try
/*     */     {
/* 254 */       Document d = DOMUtil.createDom();
/* 255 */       ExceptionBean.marshal(t, d);
/*     */ 
/* 257 */       DetailType detail = getDetail();
/* 258 */       if (detail == null) {
/* 259 */         setDetail(detail = new DetailType());
/*     */       }
/* 261 */       detail.getDetails().add(d.getDocumentElement());
/*     */     }
/*     */     catch (JAXBException e) {
/* 264 */       logger.log(Level.WARNING, "Unable to capture the stack trace into XML", e);
/*     */     }
/*     */   }
/*     */ 
/*     */   private <T extends Throwable> T attachServerException(T t)
/*     */   {
/* 272 */     DetailType detail = getDetail();
/* 273 */     if (detail == null) return t;
/*     */ 
/* 275 */     for (Element n : detail.getDetails()) {
/* 276 */       if (ExceptionBean.isStackTraceXml(n)) {
/*     */         try {
/* 278 */           t.initCause(ExceptionBean.unmarshal(n));
/*     */         }
/*     */         catch (JAXBException e) {
/* 281 */           logger.log(Level.WARNING, "Unable to read the capture stack trace in the fault", e);
/*     */         }
/* 283 */         return t;
/*     */       }
/*     */     }
/*     */ 
/* 287 */     return t;
/*     */   }
/*     */ 
/*     */   protected abstract Throwable getProtocolException();
/*     */ 
/*     */   private Object getJAXBObject(Node jaxbBean, CheckedException ce) throws JAXBException {
/* 293 */     Bridge bridge = ce.getBridge();
/* 294 */     return bridge.unmarshal(jaxbBean);
/*     */   }
/*     */ 
/*     */   private Exception createUserDefinedException(CheckedExceptionImpl ce) {
/* 298 */     Class exceptionClass = ce.getExceptionClass();
/* 299 */     Class detailBean = ce.getDetailBean();
/*     */     try {
/* 301 */       Node detailNode = (Node)getDetail().getDetails().get(0);
/* 302 */       Object jaxbDetail = getJAXBObject(detailNode, ce);
/*     */       try
/*     */       {
/* 305 */         exConstructor = exceptionClass.getConstructor(new Class[] { String.class, detailBean });
/* 306 */         return (Exception)exConstructor.newInstance(new Object[] { getFaultString(), jaxbDetail });
/*     */       } catch (NoSuchMethodException e) {
/* 308 */         Constructor exConstructor = exceptionClass.getConstructor(new Class[] { String.class });
/* 309 */         return (Exception)exConstructor.newInstance(new Object[] { getFaultString() });
/*     */       }
/*     */     } catch (Exception e) {
/* 312 */       throw new WebServiceException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static String getWriteMethod(Field f) {
/* 317 */     return "set" + StringUtils.capitalize(f.getName());
/*     */   }
/*     */ 
/*     */   private static Object getFaultDetail(CheckedExceptionImpl ce, Throwable exception) {
/* 321 */     if (ce == null)
/* 322 */       return null;
/* 323 */     if (ce.getExceptionType().equals(ExceptionType.UserDefined))
/* 324 */       return createDetailFromUserDefinedException(ce, exception);
/*     */     try
/*     */     {
/* 327 */       Method m = exception.getClass().getMethod("getFaultInfo", new Class[0]);
/* 328 */       return m.invoke(exception, new Object[0]);
/*     */     } catch (Exception e) {
/* 330 */       throw new SerializationException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static Object createDetailFromUserDefinedException(CheckedExceptionImpl ce, Object exception) {
/* 335 */     Class detailBean = ce.getDetailBean();
/* 336 */     Field[] fields = detailBean.getDeclaredFields();
/*     */     try {
/* 338 */       Object detail = detailBean.newInstance();
/* 339 */       for (Field f : fields) {
/* 340 */         Method em = exception.getClass().getMethod(getReadMethod(f), new Class[0]);
/*     */         try {
/* 342 */           Method sm = detailBean.getMethod(getWriteMethod(f), new Class[] { em.getReturnType() });
/* 343 */           sm.invoke(detail, new Object[] { em.invoke(exception, new Object[0]) });
/*     */         }
/*     */         catch (NoSuchMethodException ne) {
/* 346 */           Field sf = detailBean.getField(f.getName());
/* 347 */           sf.set(detail, em.invoke(exception, new Object[0]));
/*     */         }
/*     */       }
/* 350 */       return detail;
/*     */     } catch (Exception e) {
/* 352 */       throw new SerializationException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static String getReadMethod(Field f) {
/* 357 */     if (f.getType().isAssignableFrom(Boolean.TYPE))
/* 358 */       return "is" + StringUtils.capitalize(f.getName());
/* 359 */     return "get" + StringUtils.capitalize(f.getName());
/*     */   }
/*     */ 
/*     */   private static Message createSOAP11Fault(SOAPVersion soapVersion, Throwable e, Object detail, CheckedExceptionImpl ce, QName faultCode) {
/* 363 */     SOAPFaultException soapFaultException = null;
/* 364 */     String faultString = null;
/* 365 */     String faultActor = null;
/* 366 */     Throwable cause = e.getCause();
/* 367 */     if ((e instanceof SOAPFaultException))
/* 368 */       soapFaultException = (SOAPFaultException)e;
/* 369 */     else if ((cause != null) && ((cause instanceof SOAPFaultException))) {
/* 370 */       soapFaultException = (SOAPFaultException)e.getCause();
/*     */     }
/* 372 */     if (soapFaultException != null) {
/* 373 */       QName soapFaultCode = soapFaultException.getFault().getFaultCodeAsQName();
/* 374 */       if (soapFaultCode != null) {
/* 375 */         faultCode = soapFaultCode;
/*     */       }
/* 377 */       faultString = soapFaultException.getFault().getFaultString();
/* 378 */       faultActor = soapFaultException.getFault().getFaultActor();
/*     */     }
/*     */ 
/* 381 */     if (faultCode == null) {
/* 382 */       faultCode = getDefaultFaultCode(soapVersion);
/*     */     }
/*     */ 
/* 385 */     if (faultString == null) {
/* 386 */       faultString = e.getMessage();
/* 387 */       if (faultString == null) {
/* 388 */         faultString = e.toString();
/*     */       }
/*     */     }
/* 391 */     Element detailNode = null;
/* 392 */     QName firstEntry = null;
/* 393 */     if ((detail == null) && (soapFaultException != null)) {
/* 394 */       detailNode = soapFaultException.getFault().getDetail();
/* 395 */       firstEntry = getFirstDetailEntryName((Detail)detailNode);
/* 396 */     } else if (ce != null) {
/*     */       try {
/* 398 */         DOMResult dr = new DOMResult();
/* 399 */         ce.getBridge().marshal(detail, dr);
/* 400 */         detailNode = (Element)dr.getNode().getFirstChild();
/* 401 */         firstEntry = getFirstDetailEntryName(detailNode);
/*     */       }
/*     */       catch (JAXBException e1) {
/* 404 */         faultString = e.getMessage();
/* 405 */         faultCode = getDefaultFaultCode(soapVersion);
/*     */       }
/*     */     }
/* 408 */     SOAP11Fault soap11Fault = new SOAP11Fault(faultCode, faultString, faultActor, detailNode);
/*     */ 
/* 411 */     if (ce == null) {
/* 412 */       soap11Fault.captureStackTrace(e);
/*     */     }
/* 414 */     Message msg = JAXBMessage.create(JAXB_CONTEXT, soap11Fault, soapVersion);
/* 415 */     return new FaultMessage(msg, firstEntry);
/*     */   }
/*     */   @Nullable
/*     */   private static QName getFirstDetailEntryName(@Nullable Detail detail) {
/* 419 */     if (detail != null) {
/* 420 */       Iterator it = detail.getDetailEntries();
/* 421 */       if (it.hasNext()) {
/* 422 */         DetailEntry entry = (DetailEntry)it.next();
/* 423 */         return getFirstDetailEntryName(entry);
/*     */       }
/*     */     }
/* 426 */     return null;
/*     */   }
/*     */   @NotNull
/*     */   private static QName getFirstDetailEntryName(@NotNull Element entry) {
/* 430 */     return new QName(entry.getNamespaceURI(), entry.getLocalName());
/*     */   }
/*     */ 
/*     */   private static Message createSOAP12Fault(SOAPVersion soapVersion, Throwable e, Object detail, CheckedExceptionImpl ce, QName faultCode) {
/* 434 */     SOAPFaultException soapFaultException = null;
/* 435 */     CodeType code = null;
/* 436 */     String faultString = null;
/* 437 */     String faultRole = null;
/* 438 */     String faultNode = null;
/* 439 */     Throwable cause = e.getCause();
/* 440 */     if ((e instanceof SOAPFaultException))
/* 441 */       soapFaultException = (SOAPFaultException)e;
/* 442 */     else if ((cause != null) && ((cause instanceof SOAPFaultException))) {
/* 443 */       soapFaultException = (SOAPFaultException)e.getCause();
/*     */     }
/* 445 */     if (soapFaultException != null) {
/* 446 */       SOAPFault fault = soapFaultException.getFault();
/* 447 */       QName soapFaultCode = fault.getFaultCodeAsQName();
/* 448 */       if (soapFaultCode != null) {
/* 449 */         faultCode = soapFaultCode;
/* 450 */         code = new CodeType(faultCode);
/* 451 */         Iterator iter = fault.getFaultSubcodes();
/* 452 */         boolean first = true;
/* 453 */         SubcodeType subcode = null;
/* 454 */         while (iter.hasNext()) {
/* 455 */           QName value = (QName)iter.next();
/* 456 */           if (first) {
/* 457 */             SubcodeType sct = new SubcodeType(value);
/* 458 */             code.setSubcode(sct);
/* 459 */             subcode = sct;
/* 460 */             first = false;
/*     */           }
/*     */           else {
/* 463 */             subcode = fillSubcodes(subcode, value);
/*     */           }
/*     */         }
/*     */       }
/* 466 */       faultString = soapFaultException.getFault().getFaultString();
/* 467 */       faultRole = soapFaultException.getFault().getFaultActor();
/* 468 */       faultNode = soapFaultException.getFault().getFaultNode();
/*     */     }
/*     */ 
/* 471 */     if (faultCode == null) {
/* 472 */       faultCode = getDefaultFaultCode(soapVersion);
/* 473 */       code = new CodeType(faultCode);
/* 474 */     } else if (code == null) {
/* 475 */       code = new CodeType(faultCode);
/*     */     }
/*     */ 
/* 478 */     if (faultString == null) {
/* 479 */       faultString = e.getMessage();
/* 480 */       if (faultString == null) {
/* 481 */         faultString = e.toString();
/*     */       }
/*     */     }
/*     */ 
/* 485 */     ReasonType reason = new ReasonType(faultString);
/* 486 */     Element detailNode = null;
/* 487 */     QName firstEntry = null;
/* 488 */     if ((detail == null) && (soapFaultException != null)) {
/* 489 */       detailNode = soapFaultException.getFault().getDetail();
/* 490 */       firstEntry = getFirstDetailEntryName((Detail)detailNode);
/* 491 */     } else if (detail != null) {
/*     */       try {
/* 493 */         DOMResult dr = new DOMResult();
/* 494 */         ce.getBridge().marshal(detail, dr);
/* 495 */         detailNode = (Element)dr.getNode().getFirstChild();
/* 496 */         firstEntry = getFirstDetailEntryName(detailNode);
/*     */       }
/*     */       catch (JAXBException e1) {
/* 499 */         faultString = e.getMessage();
/* 500 */         faultCode = getDefaultFaultCode(soapVersion);
/*     */       }
/*     */     }
/*     */ 
/* 504 */     SOAP12Fault soap12Fault = new SOAP12Fault(code, reason, faultNode, faultRole, detailNode);
/*     */ 
/* 507 */     if (ce == null) {
/* 508 */       soap12Fault.captureStackTrace(e);
/*     */     }
/* 510 */     Message msg = JAXBMessage.create(JAXB_CONTEXT, soap12Fault, soapVersion);
/* 511 */     return new FaultMessage(msg, firstEntry);
/*     */   }
/*     */ 
/*     */   private static SubcodeType fillSubcodes(SubcodeType parent, QName value) {
/* 515 */     SubcodeType newCode = new SubcodeType(value);
/* 516 */     parent.setSubcode(newCode);
/* 517 */     return newCode;
/*     */   }
/*     */ 
/*     */   private static QName getDefaultFaultCode(SOAPVersion soapVersion) {
/* 521 */     return soapVersion.faultCodeServer;
/*     */   }
/*     */ 
/*     */   public static SOAPFaultBuilder create(Message msg)
/*     */     throws JAXBException
/*     */   {
/* 531 */     return (SOAPFaultBuilder)msg.readPayloadAsJAXB(JAXB_CONTEXT.createUnmarshaller());
/*     */   }
/*     */ 
/*     */   private static JAXBRIContext createJAXBContext()
/*     */   {
/* 561 */     if (isJDKRuntime()) {
/* 562 */       Permissions permissions = new Permissions();
/* 563 */       permissions.add(new RuntimePermission("accessClassInPackage.com.sun.xml.internal.ws.fault"));
/* 564 */       permissions.add(new ReflectPermission("suppressAccessChecks"));
/* 565 */       return (JAXBRIContext)AccessController.doPrivileged(new PrivilegedAction()
/*     */       {
/*     */         public JAXBRIContext run()
/*     */         {
/*     */           try {
/* 570 */             return (JAXBRIContext)JAXBContext.newInstance(new Class[] { SOAP11Fault.class, SOAP12Fault.class });
/*     */           } catch (JAXBException e) {
/* 572 */             throw new Error(e);
/*     */           }
/*     */         }
/*     */       }
/*     */       , new AccessControlContext(new ProtectionDomain[] { new ProtectionDomain(null, permissions) }));
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 581 */       return (JAXBRIContext)JAXBContext.newInstance(new Class[] { SOAP11Fault.class, SOAP12Fault.class });
/*     */     } catch (JAXBException e) {
/* 583 */       throw new Error(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static boolean isJDKRuntime()
/*     */   {
/* 589 */     return SOAPFaultBuilder.class.getName().contains("internal");
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/* 550 */       captureStackTrace = Boolean.getBoolean(CAPTURE_STACK_TRACE_PROPERTY);
/*     */     }
/*     */     catch (SecurityException e)
/*     */     {
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.fault.SOAPFaultBuilder
 * JD-Core Version:    0.6.2
 */