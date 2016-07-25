/*    */ package com.sun.xml.internal.bind.v2.model.core;
/*    */ 
/*    */ import javax.xml.namespace.QName;
/*    */ 
/*    */ public abstract interface NonElement<T, C> extends TypeInfo<T, C>
/*    */ {
/* 41 */   public static final QName ANYTYPE_NAME = new QName("http://www.w3.org/2001/XMLSchema", "anyType");
/*    */ 
/*    */   public abstract QName getTypeName();
/*    */ 
/*    */   public abstract boolean isSimpleType();
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.model.core.NonElement
 * JD-Core Version:    0.6.2
 */