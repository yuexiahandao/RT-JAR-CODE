/*    */ package com.sun.xml.internal.ws.api.policy;
/*    */ 
/*    */ import com.sun.xml.internal.ws.addressing.policy.AddressingPrefixMapper;
/*    */ import com.sun.xml.internal.ws.config.management.policy.ManagementPrefixMapper;
/*    */ import com.sun.xml.internal.ws.encoding.policy.EncodingPrefixMapper;
/*    */ import com.sun.xml.internal.ws.policy.sourcemodel.PolicySourceModel;
/*    */ import com.sun.xml.internal.ws.policy.sourcemodel.wspolicy.NamespaceVersion;
/*    */ import com.sun.xml.internal.ws.policy.spi.PrefixMapper;
/*    */ import java.util.Arrays;
/*    */ 
/*    */ public class SourceModel extends PolicySourceModel
/*    */ {
/* 46 */   private static final PrefixMapper[] JAXWS_PREFIX_MAPPERS = { new AddressingPrefixMapper(), new EncodingPrefixMapper(), new ManagementPrefixMapper() };
/*    */ 
/*    */   private SourceModel(NamespaceVersion nsVersion)
/*    */   {
/* 62 */     this(nsVersion, null, null);
/*    */   }
/*    */ 
/*    */   private SourceModel(NamespaceVersion nsVersion, String policyId, String policyName)
/*    */   {
/* 74 */     super(nsVersion, policyId, policyName, Arrays.asList(JAXWS_PREFIX_MAPPERS));
/*    */   }
/*    */ 
/*    */   public static PolicySourceModel createSourceModel(NamespaceVersion nsVersion)
/*    */   {
/* 84 */     return new SourceModel(nsVersion);
/*    */   }
/*    */ 
/*    */   public static PolicySourceModel createSourceModel(NamespaceVersion nsVersion, String policyId, String policyName)
/*    */   {
/* 97 */     return new SourceModel(nsVersion, policyId, policyName);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.policy.SourceModel
 * JD-Core Version:    0.6.2
 */