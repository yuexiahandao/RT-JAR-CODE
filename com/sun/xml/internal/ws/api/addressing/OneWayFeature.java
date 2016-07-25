/*     */ package com.sun.xml.internal.ws.api.addressing;
/*     */ 
/*     */ import com.sun.org.glassfish.gmbal.ManagedAttribute;
/*     */ import com.sun.org.glassfish.gmbal.ManagedData;
/*     */ import com.sun.xml.internal.ws.api.FeatureConstructor;
/*     */ import javax.xml.ws.WebServiceFeature;
/*     */ 
/*     */ @ManagedData
/*     */ public class OneWayFeature extends WebServiceFeature
/*     */ {
/*     */   public static final String ID = "http://java.sun.com/xml/ns/jaxws/addressing/oneway";
/*     */   private WSEndpointReference replyTo;
/*     */   private WSEndpointReference from;
/*     */   private String relatesToID;
/*     */ 
/*     */   public OneWayFeature()
/*     */   {
/*  69 */     this.enabled = true;
/*     */   }
/*     */ 
/*     */   public OneWayFeature(boolean enabled)
/*     */   {
/*  79 */     this.enabled = enabled;
/*     */   }
/*     */ 
/*     */   public OneWayFeature(boolean enabled, WSEndpointReference replyTo)
/*     */   {
/*  89 */     this.enabled = enabled;
/*  90 */     this.replyTo = replyTo;
/*     */   }
/*     */ 
/*     */   @FeatureConstructor({"enabled", "replyTo", "from", "relatesTo"})
/*     */   public OneWayFeature(boolean enabled, WSEndpointReference replyTo, WSEndpointReference from, String relatesTo)
/*     */   {
/* 103 */     this.enabled = enabled;
/* 104 */     this.replyTo = replyTo;
/* 105 */     this.from = from;
/* 106 */     this.relatesToID = relatesTo;
/*     */   }
/*     */ 
/*     */   @ManagedAttribute
/*     */   public String getID()
/*     */   {
/* 114 */     return "http://java.sun.com/xml/ns/jaxws/addressing/oneway";
/*     */   }
/*     */ 
/*     */   @ManagedAttribute
/*     */   public WSEndpointReference getReplyTo()
/*     */   {
/* 124 */     return this.replyTo;
/*     */   }
/*     */ 
/*     */   public void setReplyTo(WSEndpointReference address)
/*     */   {
/* 133 */     this.replyTo = address;
/*     */   }
/*     */ 
/*     */   @ManagedAttribute
/*     */   public WSEndpointReference getFrom()
/*     */   {
/* 143 */     return this.from;
/*     */   }
/*     */ 
/*     */   public void setFrom(WSEndpointReference address)
/*     */   {
/* 152 */     this.from = address;
/*     */   }
/*     */ 
/*     */   @ManagedAttribute
/*     */   public String getRelatesToID()
/*     */   {
/* 162 */     return this.relatesToID;
/*     */   }
/*     */ 
/*     */   public void setRelatesToID(String id)
/*     */   {
/* 171 */     this.relatesToID = id;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.addressing.OneWayFeature
 * JD-Core Version:    0.6.2
 */