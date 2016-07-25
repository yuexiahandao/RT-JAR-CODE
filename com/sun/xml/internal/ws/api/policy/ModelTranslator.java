/*    */ package com.sun.xml.internal.ws.api.policy;
/*    */ 
/*    */ import com.sun.xml.internal.ws.config.management.policy.ManagementAssertionCreator;
/*    */ import com.sun.xml.internal.ws.policy.PolicyException;
/*    */ import com.sun.xml.internal.ws.policy.privateutil.PolicyLogger;
/*    */ import com.sun.xml.internal.ws.policy.sourcemodel.PolicyModelTranslator;
/*    */ import com.sun.xml.internal.ws.policy.spi.PolicyAssertionCreator;
/*    */ import com.sun.xml.internal.ws.resources.ManagementMessages;
/*    */ import java.util.Arrays;
/*    */ 
/*    */ public class ModelTranslator extends PolicyModelTranslator
/*    */ {
/* 46 */   private static final PolicyLogger LOGGER = PolicyLogger.getLogger(ModelTranslator.class);
/*    */ 
/* 48 */   private static final PolicyAssertionCreator[] JAXWS_ASSERTION_CREATORS = { new ManagementAssertionCreator() };
/*    */   private static final ModelTranslator translator;
/*    */   private static final PolicyException creationException;
/*    */ 
/*    */   private ModelTranslator()
/*    */     throws PolicyException
/*    */   {
/* 70 */     super(Arrays.asList(JAXWS_ASSERTION_CREATORS));
/*    */   }
/*    */ 
/*    */   public static ModelTranslator getTranslator()
/*    */     throws PolicyException
/*    */   {
/* 80 */     if (creationException != null) {
/* 81 */       throw ((PolicyException)LOGGER.logSevereException(creationException));
/*    */     }
/*    */ 
/* 84 */     return translator;
/*    */   }
/*    */ 
/*    */   static
/*    */   {
/* 56 */     ModelTranslator tempTranslator = null;
/* 57 */     PolicyException tempException = null;
/*    */     try {
/* 59 */       tempTranslator = new ModelTranslator();
/*    */     } catch (PolicyException e) {
/* 61 */       tempException = e;
/* 62 */       LOGGER.warning(ManagementMessages.WSM_1007_FAILED_MODEL_TRANSLATOR_INSTANTIATION(), e);
/*    */     } finally {
/* 64 */       translator = tempTranslator;
/* 65 */       creationException = tempException;
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.policy.ModelTranslator
 * JD-Core Version:    0.6.2
 */