/*    */ package com.sun.org.apache.xml.internal.dtm.ref;
/*    */ 
/*    */ import java.util.Hashtable;
/*    */ import java.util.Vector;
/*    */ 
/*    */ public class CustomStringPool extends DTMStringPool
/*    */ {
/* 48 */   final Hashtable m_stringToInt = new Hashtable();
/*    */   public static final int NULL = -1;
/*    */ 
/*    */   public void removeAllElements()
/*    */   {
/* 63 */     this.m_intToString.removeAllElements();
/* 64 */     if (this.m_stringToInt != null)
/* 65 */       this.m_stringToInt.clear();
/*    */   }
/*    */ 
/*    */   public String indexToString(int i)
/*    */     throws ArrayIndexOutOfBoundsException
/*    */   {
/* 75 */     return (String)this.m_intToString.elementAt(i);
/*    */   }
/*    */ 
/*    */   public int stringToIndex(String s)
/*    */   {
/* 81 */     if (s == null) return -1;
/* 82 */     Integer iobj = (Integer)this.m_stringToInt.get(s);
/* 83 */     if (iobj == null) {
/* 84 */       this.m_intToString.addElement(s);
/* 85 */       iobj = new Integer(this.m_intToString.size());
/* 86 */       this.m_stringToInt.put(s, iobj);
/*    */     }
/* 88 */     return iobj.intValue();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.dtm.ref.CustomStringPool
 * JD-Core Version:    0.6.2
 */