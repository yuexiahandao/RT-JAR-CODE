/*    */ package com.sun.xml.internal.ws.api.policy;
/*    */ 
/*    */ import com.sun.xml.internal.ws.addressing.policy.AddressingPolicyValidator;
/*    */ import com.sun.xml.internal.ws.config.management.policy.ManagementPolicyValidator;
/*    */ import com.sun.xml.internal.ws.encoding.policy.EncodingPolicyValidator;
/*    */ import com.sun.xml.internal.ws.policy.AssertionValidationProcessor;
/*    */ import com.sun.xml.internal.ws.policy.PolicyException;
/*    */ import com.sun.xml.internal.ws.policy.spi.PolicyAssertionValidator;
/*    */ import java.util.Arrays;
/*    */ 
/*    */ public class ValidationProcessor extends AssertionValidationProcessor
/*    */ {
/* 44 */   private static final PolicyAssertionValidator[] JAXWS_ASSERTION_VALIDATORS = { new AddressingPolicyValidator(), new EncodingPolicyValidator(), new ManagementPolicyValidator() };
/*    */ 
/*    */   private ValidationProcessor()
/*    */     throws PolicyException
/*    */   {
/* 58 */     super(Arrays.asList(JAXWS_ASSERTION_VALIDATORS));
/*    */   }
/*    */ 
/*    */   public static ValidationProcessor getInstance()
/*    */     throws PolicyException
/*    */   {
/* 68 */     return new ValidationProcessor();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.policy.ValidationProcessor
 * JD-Core Version:    0.6.2
 */