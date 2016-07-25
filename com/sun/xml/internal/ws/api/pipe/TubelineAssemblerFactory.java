/*     */ package com.sun.xml.internal.ws.api.pipe;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.istack.internal.Nullable;
/*     */ import com.sun.xml.internal.ws.api.BindingID;
/*     */ import com.sun.xml.internal.ws.api.pipe.helper.PipeAdapter;
/*     */ import com.sun.xml.internal.ws.api.server.Container;
/*     */ import com.sun.xml.internal.ws.util.ServiceFinder;
/*     */ import com.sun.xml.internal.ws.util.pipe.StandaloneTubeAssembler;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ public abstract class TubelineAssemblerFactory
/*     */ {
/* 133 */   private static final Logger logger = Logger.getLogger(TubelineAssemblerFactory.class.getName());
/*     */ 
/*     */   public abstract TubelineAssembler doCreate(BindingID paramBindingID);
/*     */ 
/*     */   /** @deprecated */
/*     */   public static TubelineAssembler create(ClassLoader classLoader, BindingID bindingId)
/*     */   {
/*  67 */     return create(classLoader, bindingId, null);
/*     */   }
/*     */ 
/*     */   public static TubelineAssembler create(ClassLoader classLoader, BindingID bindingId, @Nullable Container container)
/*     */   {
/*  83 */     if (container != null)
/*     */     {
/*  85 */       TubelineAssemblerFactory taf = (TubelineAssemblerFactory)container.getSPI(TubelineAssemblerFactory.class);
/*  86 */       if (taf != null) {
/*  87 */         TubelineAssembler a = taf.doCreate(bindingId);
/*  88 */         if (a != null) {
/*  89 */           return a;
/*     */         }
/*     */       }
/*     */     }
/*  93 */     for (TubelineAssemblerFactory factory : ServiceFinder.find(TubelineAssemblerFactory.class, classLoader)) {
/*  94 */       TubelineAssembler assembler = factory.doCreate(bindingId);
/*  95 */       if (assembler != null) {
/*  96 */         logger.fine(factory.getClass() + " successfully created " + assembler);
/*  97 */         return assembler;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 102 */     for (PipelineAssemblerFactory factory : ServiceFinder.find(PipelineAssemblerFactory.class, classLoader)) {
/* 103 */       PipelineAssembler assembler = factory.doCreate(bindingId);
/* 104 */       if (assembler != null) {
/* 105 */         logger.fine(factory.getClass() + " successfully created " + assembler);
/* 106 */         return new TubelineAssemblerAdapter(assembler);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 111 */     return new StandaloneTubeAssembler();
/*     */   }
/*     */ 
/*     */   private static class TubelineAssemblerAdapter implements TubelineAssembler {
/*     */     private PipelineAssembler assembler;
/*     */ 
/*     */     TubelineAssemblerAdapter(PipelineAssembler assembler) {
/* 118 */       this.assembler = assembler;
/*     */     }
/*     */     @NotNull
/*     */     public Tube createClient(@NotNull ClientTubeAssemblerContext context) {
/* 122 */       ClientPipeAssemblerContext ctxt = new ClientPipeAssemblerContext(context.getAddress(), context.getWsdlModel(), context.getService(), context.getBinding(), context.getContainer());
/*     */ 
/* 125 */       return PipeAdapter.adapt(this.assembler.createClient(ctxt));
/*     */     }
/*     */     @NotNull
/*     */     public Tube createServer(@NotNull ServerTubeAssemblerContext context) {
/* 129 */       return PipeAdapter.adapt(this.assembler.createServer((ServerPipeAssemblerContext)context));
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.pipe.TubelineAssemblerFactory
 * JD-Core Version:    0.6.2
 */