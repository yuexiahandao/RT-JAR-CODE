/*    */ package com.sun.corba.se.impl.ior;
/*    */ 
/*    */ import com.sun.corba.se.spi.ior.ObjectAdapterId;
/*    */ import java.util.Iterator;
/*    */ import org.omg.CORBA_2_3.portable.OutputStream;
/*    */ 
/*    */ abstract class ObjectAdapterIdBase
/*    */   implements ObjectAdapterId
/*    */ {
/*    */   public boolean equals(Object paramObject)
/*    */   {
/* 37 */     if (!(paramObject instanceof ObjectAdapterId)) {
/* 38 */       return false;
/*    */     }
/* 40 */     ObjectAdapterId localObjectAdapterId = (ObjectAdapterId)paramObject;
/*    */ 
/* 42 */     Iterator localIterator1 = iterator();
/* 43 */     Iterator localIterator2 = localObjectAdapterId.iterator();
/*    */ 
/* 45 */     while ((localIterator1.hasNext()) && (localIterator2.hasNext())) {
/* 46 */       String str1 = (String)localIterator1.next();
/* 47 */       String str2 = (String)localIterator2.next();
/*    */ 
/* 49 */       if (!str1.equals(str2)) {
/* 50 */         return false;
/*    */       }
/*    */     }
/* 53 */     return localIterator1.hasNext() == localIterator2.hasNext();
/*    */   }
/*    */ 
/*    */   public int hashCode()
/*    */   {
/* 58 */     int i = 17;
/* 59 */     Iterator localIterator = iterator();
/* 60 */     while (localIterator.hasNext()) {
/* 61 */       String str = (String)localIterator.next();
/* 62 */       i = 37 * i + str.hashCode();
/*    */     }
/* 64 */     return i;
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 69 */     StringBuffer localStringBuffer = new StringBuffer();
/* 70 */     localStringBuffer.append("ObjectAdapterID[");
/* 71 */     Iterator localIterator = iterator();
/* 72 */     int i = 1;
/* 73 */     while (localIterator.hasNext()) {
/* 74 */       String str = (String)localIterator.next();
/*    */ 
/* 76 */       if (i != 0)
/* 77 */         i = 0;
/*    */       else {
/* 79 */         localStringBuffer.append("/");
/*    */       }
/* 81 */       localStringBuffer.append(str);
/*    */     }
/*    */ 
/* 84 */     localStringBuffer.append("]");
/*    */ 
/* 86 */     return localStringBuffer.toString();
/*    */   }
/*    */ 
/*    */   public void write(OutputStream paramOutputStream)
/*    */   {
/* 91 */     paramOutputStream.write_long(getNumLevels());
/* 92 */     Iterator localIterator = iterator();
/* 93 */     while (localIterator.hasNext()) {
/* 94 */       String str = (String)localIterator.next();
/* 95 */       paramOutputStream.write_string(str);
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.ior.ObjectAdapterIdBase
 * JD-Core Version:    0.6.2
 */