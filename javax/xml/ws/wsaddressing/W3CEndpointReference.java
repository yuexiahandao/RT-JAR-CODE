/*     */ package javax.xml.ws.wsaddressing;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.xml.bind.JAXBContext;
/*     */ import javax.xml.bind.JAXBElement;
/*     */ import javax.xml.bind.JAXBException;
/*     */ import javax.xml.bind.Marshaller;
/*     */ import javax.xml.bind.Unmarshaller;
/*     */ import javax.xml.bind.annotation.XmlAnyAttribute;
/*     */ import javax.xml.bind.annotation.XmlAnyElement;
/*     */ import javax.xml.bind.annotation.XmlElement;
/*     */ import javax.xml.bind.annotation.XmlRootElement;
/*     */ import javax.xml.bind.annotation.XmlType;
/*     */ import javax.xml.bind.annotation.XmlValue;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.transform.Result;
/*     */ import javax.xml.transform.Source;
/*     */ import javax.xml.ws.EndpointReference;
/*     */ import javax.xml.ws.WebServiceException;
/*     */ import org.w3c.dom.Element;
/*     */ 
/*     */ @XmlRootElement(name="EndpointReference", namespace="http://www.w3.org/2005/08/addressing")
/*     */ @XmlType(name="EndpointReferenceType", namespace="http://www.w3.org/2005/08/addressing")
/*     */ public final class W3CEndpointReference extends EndpointReference
/*     */ {
/*  72 */   private final JAXBContext w3cjc = getW3CJaxbContext();
/*     */ 
/*     */   @XmlElement(name="Address", namespace="http://www.w3.org/2005/08/addressing")
/*     */   private Address address;
/*     */ 
/*     */   @XmlElement(name="ReferenceParameters", namespace="http://www.w3.org/2005/08/addressing")
/*     */   private Elements referenceParameters;
/*     */ 
/*     */   @XmlElement(name="Metadata", namespace="http://www.w3.org/2005/08/addressing")
/*     */   private Elements metadata;
/*     */ 
/*     */   @XmlAnyAttribute
/*     */   Map<QName, String> attributes;
/*     */ 
/*     */   @XmlAnyElement
/*     */   List<Element> elements;
/*     */   protected static final String NS = "http://www.w3.org/2005/08/addressing";
/*     */ 
/*     */   protected W3CEndpointReference() {  } 
/*     */   public W3CEndpointReference(Source source) { try { W3CEndpointReference epr = (W3CEndpointReference)this.w3cjc.createUnmarshaller().unmarshal(source, W3CEndpointReference.class).getValue();
/*  93 */       this.address = epr.address;
/*  94 */       this.metadata = epr.metadata;
/*  95 */       this.referenceParameters = epr.referenceParameters;
/*  96 */       this.elements = epr.elements;
/*  97 */       this.attributes = epr.attributes;
/*     */     } catch (JAXBException e) {
/*  99 */       throw new WebServiceException("Error unmarshalling W3CEndpointReference ", e);
/*     */     } catch (ClassCastException e) {
/* 101 */       throw new WebServiceException("Source did not contain W3CEndpointReference", e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void writeTo(Result result)
/*     */   {
/*     */     try
/*     */     {
/* 110 */       Marshaller marshaller = this.w3cjc.createMarshaller();
/* 111 */       marshaller.marshal(this, result);
/*     */     } catch (JAXBException e) {
/* 113 */       throw new WebServiceException("Error marshalling W3CEndpointReference. ", e);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static JAXBContext getW3CJaxbContext() {
/*     */     try {
/* 119 */       return JAXBContext.newInstance(new Class[] { W3CEndpointReference.class });
/*     */     } catch (JAXBException e) {
/* 121 */       throw new WebServiceException("Error creating JAXBContext for W3CEndpointReference. ", e);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class Address
/*     */   {
/*     */ 
/*     */     @XmlValue
/*     */     String uri;
/*     */ 
/*     */     @XmlAnyAttribute
/*     */     Map<QName, String> attributes;
/*     */   }
/*     */ 
/*     */   private static class Elements
/*     */   {
/*     */ 
/*     */     @XmlAnyElement
/*     */     List<Element> elements;
/*     */ 
/*     */     @XmlAnyAttribute
/*     */     Map<QName, String> attributes;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.ws.wsaddressing.W3CEndpointReference
 * JD-Core Version:    0.6.2
 */