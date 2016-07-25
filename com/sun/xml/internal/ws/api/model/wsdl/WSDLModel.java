/*     */ package com.sun.xml.internal.ws.api.model.wsdl;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.xml.internal.ws.api.policy.PolicyResolver;
/*     */ import com.sun.xml.internal.ws.api.policy.PolicyResolverFactory;
/*     */ import com.sun.xml.internal.ws.api.server.Container;
/*     */ import com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension;
/*     */ import com.sun.xml.internal.ws.api.wsdl.parser.XMLEntityResolver;
/*     */ import com.sun.xml.internal.ws.api.wsdl.parser.XMLEntityResolver.Parser;
/*     */ import com.sun.xml.internal.ws.policy.PolicyMap;
/*     */ import com.sun.xml.internal.ws.wsdl.parser.RuntimeWSDLParser;
/*     */ import java.io.IOException;
/*     */ import java.util.Map;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public abstract interface WSDLModel extends WSDLExtensible
/*     */ {
/*     */   public abstract WSDLPortType getPortType(@NotNull QName paramQName);
/*     */ 
/*     */   public abstract WSDLBoundPortType getBinding(@NotNull QName paramQName);
/*     */ 
/*     */   public abstract WSDLBoundPortType getBinding(@NotNull QName paramQName1, @NotNull QName paramQName2);
/*     */ 
/*     */   public abstract WSDLService getService(@NotNull QName paramQName);
/*     */ 
/*     */   @NotNull
/*     */   public abstract Map<QName, ? extends WSDLPortType> getPortTypes();
/*     */ 
/*     */   @NotNull
/*     */   public abstract Map<QName, WSDLBoundPortType> getBindings();
/*     */ 
/*     */   @NotNull
/*     */   public abstract Map<QName, ? extends WSDLService> getServices();
/*     */ 
/*     */   /** @deprecated */
/*     */   public abstract PolicyMap getPolicyMap();
/*     */ 
/*     */   public static class WSDLParser
/*     */   {
/*     */     @NotNull
/*     */     public static WSDLModel parse(XMLEntityResolver.Parser wsdlEntityParser, XMLEntityResolver resolver, boolean isClientSide, WSDLParserExtension[] extensions)
/*     */       throws IOException, XMLStreamException, SAXException
/*     */     {
/* 136 */       return parse(wsdlEntityParser, resolver, isClientSide, Container.NONE, extensions);
/*     */     }
/*     */ 
/*     */     @NotNull
/*     */     public static WSDLModel parse(XMLEntityResolver.Parser wsdlEntityParser, XMLEntityResolver resolver, boolean isClientSide, @NotNull Container container, WSDLParserExtension[] extensions)
/*     */       throws IOException, XMLStreamException, SAXException
/*     */     {
/* 153 */       return parse(wsdlEntityParser, resolver, isClientSide, container, PolicyResolverFactory.create(), extensions);
/*     */     }
/*     */ 
/*     */     @NotNull
/*     */     public static WSDLModel parse(XMLEntityResolver.Parser wsdlEntityParser, XMLEntityResolver resolver, boolean isClientSide, @NotNull Container container, PolicyResolver policyResolver, WSDLParserExtension[] extensions)
/*     */       throws IOException, XMLStreamException, SAXException
/*     */     {
/* 172 */       return RuntimeWSDLParser.parse(wsdlEntityParser, resolver, isClientSide, container, policyResolver, extensions);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.model.wsdl.WSDLModel
 * JD-Core Version:    0.6.2
 */