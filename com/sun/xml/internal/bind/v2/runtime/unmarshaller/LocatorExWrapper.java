/*    */ package com.sun.xml.internal.bind.v2.runtime.unmarshaller;
/*    */ 
/*    */ import javax.xml.bind.ValidationEventLocator;
/*    */ import javax.xml.bind.helpers.ValidationEventLocatorImpl;
/*    */ import org.xml.sax.Locator;
/*    */ 
/*    */ class LocatorExWrapper
/*    */   implements LocatorEx
/*    */ {
/*    */   private final Locator locator;
/*    */ 
/*    */   public LocatorExWrapper(Locator locator)
/*    */   {
/* 42 */     this.locator = locator;
/*    */   }
/*    */ 
/*    */   public ValidationEventLocator getLocation() {
/* 46 */     return new ValidationEventLocatorImpl(this.locator);
/*    */   }
/*    */ 
/*    */   public String getPublicId() {
/* 50 */     return this.locator.getPublicId();
/*    */   }
/*    */ 
/*    */   public String getSystemId() {
/* 54 */     return this.locator.getSystemId();
/*    */   }
/*    */ 
/*    */   public int getLineNumber() {
/* 58 */     return this.locator.getLineNumber();
/*    */   }
/*    */ 
/*    */   public int getColumnNumber() {
/* 62 */     return this.locator.getColumnNumber();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.unmarshaller.LocatorExWrapper
 * JD-Core Version:    0.6.2
 */