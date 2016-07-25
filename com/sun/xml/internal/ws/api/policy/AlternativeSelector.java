/*    */ package com.sun.xml.internal.ws.api.policy;
/*    */ 
/*    */ import com.sun.xml.internal.ws.policy.EffectiveAlternativeSelector;
/*    */ import com.sun.xml.internal.ws.policy.EffectivePolicyModifier;
/*    */ import com.sun.xml.internal.ws.policy.PolicyException;
/*    */ 
/*    */ public class AlternativeSelector extends EffectiveAlternativeSelector
/*    */ {
/*    */   public static void doSelection(EffectivePolicyModifier modifier)
/*    */     throws PolicyException
/*    */   {
/* 39 */     ValidationProcessor validationProcessor = ValidationProcessor.getInstance();
/* 40 */     selectAlternatives(modifier, validationProcessor);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.policy.AlternativeSelector
 * JD-Core Version:    0.6.2
 */