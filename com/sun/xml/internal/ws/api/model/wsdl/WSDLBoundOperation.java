/*    */ package com.sun.xml.internal.ws.api.model.wsdl;
/*    */ 
/*    */ import com.sun.istack.internal.NotNull;
/*    */ import com.sun.istack.internal.Nullable;
/*    */ import java.util.Map;
/*    */ import javax.jws.WebParam.Mode;
/*    */ import javax.xml.namespace.QName;
/*    */ 
/*    */ public abstract interface WSDLBoundOperation extends WSDLObject, WSDLExtensible
/*    */ {
/*    */   @NotNull
/*    */   public abstract QName getName();
/*    */ 
/*    */   @NotNull
/*    */   public abstract String getSOAPAction();
/*    */ 
/*    */   @NotNull
/*    */   public abstract WSDLOperation getOperation();
/*    */ 
/*    */   @NotNull
/*    */   public abstract WSDLBoundPortType getBoundPortType();
/*    */ 
/*    */   public abstract ANONYMOUS getAnonymous();
/*    */ 
/*    */   @Nullable
/*    */   public abstract WSDLPart getPart(@NotNull String paramString, @NotNull WebParam.Mode paramMode);
/*    */ 
/*    */   @NotNull
/*    */   public abstract Map<String, WSDLPart> getInParts();
/*    */ 
/*    */   @NotNull
/*    */   public abstract Map<String, WSDLPart> getOutParts();
/*    */ 
/*    */   @NotNull
/*    */   public abstract Iterable<? extends WSDLBoundFault> getFaults();
/*    */ 
/*    */   @Nullable
/*    */   public abstract QName getReqPayloadName();
/*    */ 
/*    */   public static enum ANONYMOUS
/*    */   {
/* 72 */     optional, required, prohibited;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundOperation
 * JD-Core Version:    0.6.2
 */