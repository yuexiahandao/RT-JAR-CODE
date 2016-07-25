/*    */ package com.sun.xml.internal.ws.api.client;
/*    */ 
/*    */ import com.sun.org.glassfish.gmbal.ManagedAttribute;
/*    */ import com.sun.org.glassfish.gmbal.ManagedData;
/*    */ import com.sun.xml.internal.ws.api.FeatureConstructor;
/*    */ import javax.xml.ws.WebServiceFeature;
/*    */ 
/*    */ @ManagedData
/*    */ public class SelectOptimalEncodingFeature extends WebServiceFeature
/*    */ {
/*    */   public static final String ID = "http://java.sun.com/xml/ns/jaxws/client/selectOptimalEncoding";
/*    */ 
/*    */   public SelectOptimalEncodingFeature()
/*    */   {
/* 73 */     this.enabled = true;
/*    */   }
/*    */ 
/*    */   @FeatureConstructor({"enabled"})
/*    */   public SelectOptimalEncodingFeature(boolean enabled)
/*    */   {
/* 84 */     this.enabled = enabled;
/*    */   }
/*    */ 
/*    */   @ManagedAttribute
/*    */   public String getID()
/*    */   {
/* 92 */     return "http://java.sun.com/xml/ns/jaxws/client/selectOptimalEncoding";
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.client.SelectOptimalEncodingFeature
 * JD-Core Version:    0.6.2
 */