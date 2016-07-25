/*    */ package com.sun.xml.internal.ws.addressing;
/*    */ 
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
/* 55 */     super(binding, seiModel, wsdlPort);
/*    */   }
/*    */ 
/*    */   private Marshaller createMarshaller() throws JAXBException {
/* 59 */     Marshaller marshaller = jc.createMarshaller();
/* 60 */     marshaller.setProperty("jaxb.fragment", Boolean.TRUE);
/* 61 */     return marshaller;
/*    */   }
/*    */ 
/*    */   public final void getProblemActionDetail(String action, Element element)
/*    */   {
/* 66 */     ProblemAction pa = new ProblemAction(action);
/*    */     try {
/* 68 */       createMarshaller().marshal(pa, element);
/*    */     } catch (JAXBException e) {
/* 70 */       throw new WebServiceException(e);
/*    */     }
/*    */   }
/*    */ 
/*    */   public final void getInvalidMapDetail(QName name, Element element)
/*    */   {
/* 76 */     ProblemHeaderQName phq = new ProblemHeaderQName(name);
/*    */     try {
/* 78 */       createMarshaller().marshal(phq, element);
/*    */     } catch (JAXBException e) {
/* 80 */       throw new WebServiceException(e);
/*    */     }
/*    */   }
/*    */ 
/*    */   public final void getMapRequiredDetail(QName name, Element element)
/*    */   {
/* 86 */     getInvalidMapDetail(name, element);
/*    */   }
/*    */ 
/*    */   static
/*    */   {
/*    */     try
/*    */     {
/* 47 */       jc = JAXBContext.newInstance(new Class[] { ProblemAction.class, ProblemHeaderQName.class });
/*    */     }
/*    */     catch (JAXBException e) {
/* 50 */       throw new WebServiceException(e);
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.addressing.WsaTubeHelperImpl
 * JD-Core Version:    0.6.2
 */