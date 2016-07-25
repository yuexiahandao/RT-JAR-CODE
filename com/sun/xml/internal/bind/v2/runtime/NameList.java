/*    */ package com.sun.xml.internal.bind.v2.runtime;
/*    */ 
/*    */ public final class NameList
/*    */ {
/*    */   public final String[] namespaceURIs;
/*    */   public final boolean[] nsUriCannotBeDefaulted;
/*    */   public final String[] localNames;
/*    */   public final int numberOfElementNames;
/*    */   public final int numberOfAttributeNames;
/*    */ 
/*    */   public NameList(String[] namespaceURIs, boolean[] nsUriCannotBeDefaulted, String[] localNames, int numberElementNames, int numberAttributeNames)
/*    */   {
/* 67 */     this.namespaceURIs = namespaceURIs;
/* 68 */     this.nsUriCannotBeDefaulted = nsUriCannotBeDefaulted;
/* 69 */     this.localNames = localNames;
/* 70 */     this.numberOfElementNames = numberElementNames;
/* 71 */     this.numberOfAttributeNames = numberAttributeNames;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.NameList
 * JD-Core Version:    0.6.2
 */