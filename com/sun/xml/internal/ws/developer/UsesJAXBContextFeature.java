/*     */ package com.sun.xml.internal.ws.developer;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.istack.internal.Nullable;
/*     */ import com.sun.org.glassfish.gmbal.ManagedAttribute;
/*     */ import com.sun.org.glassfish.gmbal.ManagedData;
/*     */ import com.sun.xml.internal.bind.api.JAXBRIContext;
/*     */ import com.sun.xml.internal.bind.api.TypeReference;
/*     */ import com.sun.xml.internal.ws.api.FeatureConstructor;
/*     */ import com.sun.xml.internal.ws.api.model.SEIModel;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.util.List;
/*     */ import javax.xml.bind.JAXBException;
/*     */ import javax.xml.ws.WebServiceFeature;
/*     */ 
/*     */ @ManagedData
/*     */ public class UsesJAXBContextFeature extends WebServiceFeature
/*     */ {
/*     */   public static final String ID = "http://jax-ws.dev.java.net/features/uses-jaxb-context";
/*     */   private final JAXBContextFactory factory;
/*     */ 
/*     */   @FeatureConstructor({"value"})
/*     */   public UsesJAXBContextFeature(@NotNull Class<? extends JAXBContextFactory> factoryClass)
/*     */   {
/*     */     try
/*     */     {
/*  72 */       this.factory = ((JAXBContextFactory)factoryClass.getConstructor(new Class[0]).newInstance(new Object[0]));
/*     */     } catch (InstantiationException e) {
/*  74 */       Error x = new InstantiationError(e.getMessage());
/*  75 */       x.initCause(e);
/*  76 */       throw x;
/*     */     } catch (IllegalAccessException e) {
/*  78 */       Error x = new IllegalAccessError(e.getMessage());
/*  79 */       x.initCause(e);
/*  80 */       throw x;
/*     */     } catch (InvocationTargetException e) {
/*  82 */       Error x = new InstantiationError(e.getMessage());
/*  83 */       x.initCause(e);
/*  84 */       throw x;
/*     */     } catch (NoSuchMethodException e) {
/*  86 */       Error x = new NoSuchMethodError(e.getMessage());
/*  87 */       x.initCause(e);
/*  88 */       throw x;
/*     */     }
/*     */   }
/*     */ 
/*     */   public UsesJAXBContextFeature(@Nullable JAXBContextFactory factory)
/*     */   {
/* 100 */     this.factory = factory;
/*     */   }
/*     */ 
/*     */   public UsesJAXBContextFeature(@Nullable final JAXBRIContext context)
/*     */   {
/* 108 */     this.factory = new JAXBContextFactory() {
/*     */       @NotNull
/*     */       public JAXBRIContext createJAXBContext(@NotNull SEIModel sei, @NotNull List<Class> classesToBind, @NotNull List<TypeReference> typeReferences) throws JAXBException {
/* 111 */         return context;
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   @ManagedAttribute
/*     */   @Nullable
/*     */   public JAXBContextFactory getFactory()
/*     */   {
/* 124 */     return this.factory;
/*     */   }
/*     */ 
/*     */   @ManagedAttribute
/*     */   public String getID() {
/* 129 */     return "http://jax-ws.dev.java.net/features/uses-jaxb-context";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.developer.UsesJAXBContextFeature
 * JD-Core Version:    0.6.2
 */