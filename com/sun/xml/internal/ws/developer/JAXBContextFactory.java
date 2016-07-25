/*    */ package com.sun.xml.internal.ws.developer;
/*    */ 
/*    */ import com.sun.istack.internal.NotNull;
/*    */ import com.sun.xml.internal.bind.api.JAXBRIContext;
/*    */ import com.sun.xml.internal.bind.api.TypeReference;
/*    */ import com.sun.xml.internal.ws.api.model.SEIModel;
/*    */ import java.util.List;
/*    */ import javax.xml.bind.JAXBException;
/*    */ 
/*    */ public abstract interface JAXBContextFactory
/*    */ {
/* 95 */   public static final JAXBContextFactory DEFAULT = new JAXBContextFactory() {
/*    */     @NotNull
/*    */     public JAXBRIContext createJAXBContext(@NotNull SEIModel sei, @NotNull List<Class> classesToBind, @NotNull List<TypeReference> typeReferences) throws JAXBException {
/* 98 */       return JAXBRIContext.newInstance((Class[])classesToBind.toArray(new Class[classesToBind.size()]), typeReferences, null, sei.getTargetNamespace(), false, null);
/*    */     }
/* 95 */   };
/*    */ 
/*    */   @NotNull
/*    */   public abstract JAXBRIContext createJAXBContext(@NotNull SEIModel paramSEIModel, @NotNull List<Class> paramList, @NotNull List<TypeReference> paramList1)
/*    */     throws JAXBException;
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.developer.JAXBContextFactory
 * JD-Core Version:    0.6.2
 */