/*    */ package com.sun.xml.internal.ws.encoding.policy;
/*    */ 
/*    */ import com.sun.xml.internal.ws.policy.spi.PrefixMapper;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ public class EncodingPrefixMapper
/*    */   implements PrefixMapper
/*    */ {
/* 38 */   private static final Map<String, String> prefixMap = new HashMap();
/*    */ 
/*    */   public Map<String, String> getPrefixMap()
/*    */   {
/* 48 */     return prefixMap;
/*    */   }
/*    */ 
/*    */   static
/*    */   {
/* 41 */     prefixMap.put("http://schemas.xmlsoap.org/ws/2004/09/policy/encoding", "wspe");
/* 42 */     prefixMap.put("http://schemas.xmlsoap.org/ws/2004/09/policy/optimizedmimeserialization", "wsoma");
/* 43 */     prefixMap.put("http://java.sun.com/xml/ns/wsit/2006/09/policy/encoding/client", "cenc");
/* 44 */     prefixMap.put("http://java.sun.com/xml/ns/wsit/2006/09/policy/fastinfoset/service", "fi");
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.encoding.policy.EncodingPrefixMapper
 * JD-Core Version:    0.6.2
 */