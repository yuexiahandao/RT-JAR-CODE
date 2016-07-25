/*    */ package com.sun.xml.internal.ws.addressing.v200408;
/*    */ 
/*    */ import com.sun.xml.internal.ws.addressing.WsaTubeHelper;
/*    */ import com.sun.xml.internal.ws.api.WSBinding;
/*    */ import com.sun.xml.internal.ws.api.model.SEIModel;
/*    */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
/*    */ import javax.xml.bind.JAXBContext;
/*    */ import javax.xml.bind.JAXBException;
/*    */ import javax.xml.bind.Marshaller;
/*    */ import javax.xml.namespace.QName;
/*    */ import javax.xml.ws.WebServiceException;
/*    */ import org.w3c.dom.Element;
/*    */ 
/*    */ public class WsaTubeHelperImpl extends WsaTubeHelper
/*    */ {
/*    */   static final JAXBContext jc;
/*    */ 
/*    */   public WsaTubeHelperImpl(WSDLPort wsdlPort, SEIModel seiModel, WSBinding binding)
/*    */   {
/* 56 */     super(binding, seiModel, wsdlPort);
/*    */   }
/*    */ 
/*    */   private Marshaller createMarshaller() throws JAXBException {
/* 60 */     Marshaller marshaller = jc.createMarshaller();
/* 61 */     marshaller.setProperty("jaxb.fragment", Boolean.TRUE);
/* 62 */     return marshaller;
/*    */   }
/*    */ 
/*    */   public final void getProblemActionDetail(String action, Element element)
/*    */   {
/* 67 */     ProblemAction pa = new ProblemAction(action);
/*    */     try {
/* 69 */       createMarshaller().marshal(pa, element);
/*    */     } catch (JAXBException e) {
/* 71 */       throw new WebServiceException(e);
/*    */     }
/*    */   }
/*    */ 
/*    */   public final void getInvalidMapDetail(QName name, Element element)
/*    */   {
/* 77 */     ProblemHeaderQName phq = new ProblemHeaderQName(name);
/*    */     try {
/* 79 */       createMarshaller().marshal(phq, element);
/*    */     } catch (JAXBException e) {
/* 81 */       throw new WebServiceException(e);
/*    */     }
/*    */   }
/*    */ 
/*    */   public final void getMapRequiredDetail(QName name, Element element)
/*    */   {
/* 87 */     getInvalidMapDetail(name, element);
/*    */   }
/*    */ 
/*    */   static
/*    */   {
/*    */     try
/*    */     {
/* 48 */       jc = JAXBContext.newInstance(new Class[] { ProblemAction.class, ProblemHeaderQName.class });
/*    */     }
/*    */     catch (JAXBException e) {
/* 51 */       throw new WebServiceException(e);
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.addressing.v200408.WsaTubeHelperImpl
 * JD-Core Version:    0.6.2
 */