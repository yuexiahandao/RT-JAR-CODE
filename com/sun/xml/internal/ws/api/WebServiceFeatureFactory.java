/*    */ package com.sun.xml.internal.ws.api;
/*    */ 
/*    */ import com.sun.xml.internal.ws.binding.WebServiceFeatureList;
/*    */ import java.lang.annotation.Annotation;
/*    */ import javax.xml.ws.WebServiceFeature;
/*    */ 
/*    */ public class WebServiceFeatureFactory
/*    */ {
/*    */   public static WSFeatureList getWSFeatureList(Iterable<Annotation> ann)
/*    */   {
/* 49 */     WebServiceFeatureList list = new WebServiceFeatureList();
/* 50 */     list.parseAnnotations(ann);
/* 51 */     return list;
/*    */   }
/*    */ 
/*    */   public static WebServiceFeature getWebServiceFeature(Annotation ann)
/*    */   {
/* 63 */     return WebServiceFeatureList.getFeature(ann);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.WebServiceFeatureFactory
 * JD-Core Version:    0.6.2
 */