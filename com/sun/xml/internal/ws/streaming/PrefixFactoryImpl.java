/*    */ package com.sun.xml.internal.ws.streaming;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ public class PrefixFactoryImpl
/*    */   implements PrefixFactory
/*    */ {
/*    */   private String _base;
/*    */   private int _next;
/*    */   private Map _cachedUriToPrefixMap;
/*    */ 
/*    */   public PrefixFactoryImpl(String base)
/*    */   {
/* 39 */     this._base = base;
/* 40 */     this._next = 1;
/*    */   }
/*    */ 
/*    */   public String getPrefix(String uri) {
/* 44 */     String prefix = null;
/*    */ 
/* 46 */     if (this._cachedUriToPrefixMap == null)
/* 47 */       this._cachedUriToPrefixMap = new HashMap();
/*    */     else {
/* 49 */       prefix = (String)this._cachedUriToPrefixMap.get(uri);
/*    */     }
/*    */ 
/* 52 */     if (prefix == null) {
/* 53 */       prefix = this._base + Integer.toString(this._next++);
/* 54 */       this._cachedUriToPrefixMap.put(uri, prefix);
/*    */     }
/*    */ 
/* 57 */     return prefix;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.streaming.PrefixFactoryImpl
 * JD-Core Version:    0.6.2
 */