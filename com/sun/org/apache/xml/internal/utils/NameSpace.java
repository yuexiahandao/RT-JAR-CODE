/*    */ package com.sun.org.apache.xml.internal.utils;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ 
/*    */ public class NameSpace
/*    */   implements Serializable
/*    */ {
/*    */   static final long serialVersionUID = 1471232939184881839L;
/* 39 */   public NameSpace m_next = null;
/*    */   public String m_prefix;
/*    */   public String m_uri;
/*    */ 
/*    */   public NameSpace(String prefix, String uri)
/*    */   {
/* 58 */     this.m_prefix = prefix;
/* 59 */     this.m_uri = uri;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.utils.NameSpace
 * JD-Core Version:    0.6.2
 */