/*    */ package com.sun.xml.internal.ws.developer;
/*    */ 
/*    */ import java.lang.annotation.Annotation;
/*    */ import java.lang.annotation.Documented;
/*    */ import java.lang.annotation.Retention;
/*    */ import java.lang.annotation.RetentionPolicy;
/*    */ import java.lang.annotation.Target;
/*    */ import javax.xml.ws.spi.WebServiceFeatureAnnotation;
/*    */ 
/*    */ @Target({java.lang.annotation.ElementType.TYPE, java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.FIELD})
/*    */ @Retention(RetentionPolicy.RUNTIME)
/*    */ @Documented
/*    */ @WebServiceFeatureAnnotation(id="http://java.sun.com/xml/ns/jaxws/2004/08/addressing", bean=MemberSubmissionAddressingFeature.class)
/*    */ public @interface MemberSubmissionAddressing
/*    */ {
/*    */   public abstract boolean enabled();
/*    */ 
/*    */   public abstract boolean required();
/*    */ 
/*    */   public abstract Validation validation();
/*    */ 
/*    */   public static enum Validation
/*    */   {
/* 99 */     LAX, STRICT;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.developer.MemberSubmissionAddressing
 * JD-Core Version:    0.6.2
 */