/*    */ package com.sun.corba.se.impl.ior;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ 
/*    */ public class ObjectAdapterIdArray extends ObjectAdapterIdBase
/*    */ {
/*    */   private final String[] objectAdapterId;
/*    */ 
/*    */   public ObjectAdapterIdArray(String[] paramArrayOfString)
/*    */   {
/* 36 */     this.objectAdapterId = paramArrayOfString;
/*    */   }
/*    */ 
/*    */   public ObjectAdapterIdArray(String paramString1, String paramString2)
/*    */   {
/* 43 */     this.objectAdapterId = new String[2];
/* 44 */     this.objectAdapterId[0] = paramString1;
/* 45 */     this.objectAdapterId[1] = paramString2;
/*    */   }
/*    */ 
/*    */   public int getNumLevels()
/*    */   {
/* 50 */     return this.objectAdapterId.length;
/*    */   }
/*    */ 
/*    */   public Iterator iterator()
/*    */   {
/* 55 */     return Arrays.asList(this.objectAdapterId).iterator();
/*    */   }
/*    */ 
/*    */   public String[] getAdapterName()
/*    */   {
/* 60 */     return (String[])this.objectAdapterId.clone();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.ior.ObjectAdapterIdArray
 * JD-Core Version:    0.6.2
 */