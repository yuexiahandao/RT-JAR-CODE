/*    */ package com.sun.xml.internal.ws.addressing.policy;
/*    */ 
/*    */ import com.sun.xml.internal.ws.api.addressing.AddressingVersion;
/*    */ import com.sun.xml.internal.ws.policy.spi.PrefixMapper;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ public class AddressingPrefixMapper
/*    */   implements PrefixMapper
/*    */ {
/* 43 */   private static final Map<String, String> prefixMap = new HashMap();
/*    */ 
/*    */   public Map<String, String> getPrefixMap()
/*    */   {
/* 52 */     return prefixMap;
/*    */   }
/*    */ 
/*    */   static
/*    */   {
/* 46 */     prefixMap.put(AddressingVersion.MEMBER.policyNsUri, "wsap");
/* 47 */     prefixMap.put(AddressingVersion.MEMBER.nsUri, "wsa");
/* 48 */     prefixMap.put("http://www.w3.org/2007/05/addressing/metadata", "wsam");
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.addressing.policy.AddressingPrefixMapper
 * JD-Core Version:    0.6.2
 */