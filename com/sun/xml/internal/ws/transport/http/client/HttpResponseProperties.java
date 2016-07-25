/*    */ package com.sun.xml.internal.ws.transport.http.client;
/*    */ 
/*    */ import com.sun.istack.internal.NotNull;
/*    */ import com.sun.xml.internal.ws.api.PropertySet;
/*    */ import com.sun.xml.internal.ws.api.PropertySet.Property;
/*    */ import com.sun.xml.internal.ws.api.PropertySet.PropertyMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ 
/*    */ final class HttpResponseProperties extends PropertySet
/*    */ {
/*    */   private final HttpClientTransport deferedCon;
/* 67 */   private static final PropertySet.PropertyMap model = parse(HttpResponseProperties.class);
/*    */ 
/*    */   public HttpResponseProperties(@NotNull HttpClientTransport con)
/*    */   {
/* 46 */     this.deferedCon = con;
/*    */   }
/*    */ 
/*    */   @PropertySet.Property({"javax.xml.ws.http.response.headers"})
/*    */   public Map<String, List<String>> getResponseHeaders() {
/* 51 */     return this.deferedCon.getHeaders();
/*    */   }
/*    */ 
/*    */   @PropertySet.Property({"javax.xml.ws.http.response.code"})
/*    */   public int getResponseCode() {
/* 56 */     return this.deferedCon.statusCode;
/*    */   }
/*    */ 
/*    */   protected PropertySet.PropertyMap getPropertyMap()
/*    */   {
/* 61 */     return model;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.transport.http.client.HttpResponseProperties
 * JD-Core Version:    0.6.2
 */