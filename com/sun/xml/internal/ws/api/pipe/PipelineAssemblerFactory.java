/*    */ package com.sun.xml.internal.ws.api.pipe;
/*    */ 
/*    */ import com.sun.xml.internal.ws.api.BindingID;
/*    */ import com.sun.xml.internal.ws.util.ServiceFinder;
/*    */ import com.sun.xml.internal.ws.util.pipe.StandalonePipeAssembler;
/*    */ import java.util.logging.Logger;
/*    */ 
/*    */ /** @deprecated */
/*    */ public abstract class PipelineAssemblerFactory
/*    */ {
/* 95 */   private static final Logger logger = Logger.getLogger(PipelineAssemblerFactory.class.getName());
/*    */ 
/*    */   public abstract PipelineAssembler doCreate(BindingID paramBindingID);
/*    */ 
/*    */   public static PipelineAssembler create(ClassLoader classLoader, BindingID bindingId)
/*    */   {
/* 82 */     for (PipelineAssemblerFactory factory : ServiceFinder.find(PipelineAssemblerFactory.class, classLoader)) {
/* 83 */       PipelineAssembler assembler = factory.doCreate(bindingId);
/* 84 */       if (assembler != null) {
/* 85 */         logger.fine(factory.getClass() + " successfully created " + assembler);
/* 86 */         return assembler;
/*    */       }
/*    */ 
/*    */     }
/*    */ 
/* 92 */     return new StandalonePipeAssembler();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.pipe.PipelineAssemblerFactory
 * JD-Core Version:    0.6.2
 */