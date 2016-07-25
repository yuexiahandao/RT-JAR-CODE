/*    */ package com.sun.xml.internal.ws.config.management.policy;
/*    */ 
/*    */ import com.sun.xml.internal.ws.policy.spi.PrefixMapper;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ public class ManagementPrefixMapper
/*    */   implements PrefixMapper
/*    */ {
/* 41 */   private static final Map<String, String> prefixMap = new HashMap();
/*    */ 
/*    */   public Map<String, String> getPrefixMap()
/*    */   {
/* 48 */     return prefixMap;
/*    */   }
/*    */ 
/*    */   static
/*    */   {
/* 44 */     prefixMap.put("http://java.sun.com/xml/ns/metro/management", "sunman");
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.config.management.policy.ManagementPrefixMapper
 * JD-Core Version:    0.6.2
 */