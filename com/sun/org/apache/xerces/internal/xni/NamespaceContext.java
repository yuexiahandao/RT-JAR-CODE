/*    */ package com.sun.org.apache.xerces.internal.xni;
/*    */ 
/*    */ import java.util.Enumeration;
/*    */ 
/*    */ public abstract interface NamespaceContext
/*    */ {
/* 49 */   public static final String XML_URI = "http://www.w3.org/XML/1998/namespace".intern();
/*    */ 
/* 56 */   public static final String XMLNS_URI = "http://www.w3.org/2000/xmlns/".intern();
/*    */ 
/*    */   public abstract void pushContext();
/*    */ 
/*    */   public abstract void popContext();
/*    */ 
/*    */   public abstract boolean declarePrefix(String paramString1, String paramString2);
/*    */ 
/*    */   public abstract String getURI(String paramString);
/*    */ 
/*    */   public abstract String getPrefix(String paramString);
/*    */ 
/*    */   public abstract int getDeclaredPrefixCount();
/*    */ 
/*    */   public abstract String getDeclaredPrefixAt(int paramInt);
/*    */ 
/*    */   public abstract Enumeration getAllPrefixes();
/*    */ 
/*    */   public abstract void reset();
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.xni.NamespaceContext
 * JD-Core Version:    0.6.2
 */