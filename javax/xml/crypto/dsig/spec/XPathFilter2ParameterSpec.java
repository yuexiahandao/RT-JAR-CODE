/*    */ package javax.xml.crypto.dsig.spec;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ 
/*    */ public final class XPathFilter2ParameterSpec
/*    */   implements TransformParameterSpec
/*    */ {
/*    */   private final List xPathList;
/*    */ 
/*    */   public XPathFilter2ParameterSpec(List paramList)
/*    */   {
/* 63 */     if (paramList == null) {
/* 64 */       throw new NullPointerException("xPathList cannot be null");
/*    */     }
/* 66 */     ArrayList localArrayList = new ArrayList(paramList);
/* 67 */     if (localArrayList.isEmpty()) {
/* 68 */       throw new IllegalArgumentException("xPathList cannot be empty");
/*    */     }
/* 70 */     int i = localArrayList.size();
/* 71 */     for (int j = 0; j < i; j++) {
/* 72 */       if (!(localArrayList.get(j) instanceof XPathType)) {
/* 73 */         throw new ClassCastException("xPathList[" + j + "] is not a valid type");
/*    */       }
/*    */     }
/*    */ 
/* 77 */     this.xPathList = Collections.unmodifiableList(localArrayList);
/*    */   }
/*    */ 
/*    */   public List getXPathList()
/*    */   {
/* 90 */     return this.xPathList;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.crypto.dsig.spec.XPathFilter2ParameterSpec
 * JD-Core Version:    0.6.2
 */