/*      */ package sun.security.tools.policytool;
/*      */ 
/*      */ import java.util.LinkedList;
/*      */ 
/*      */ class TaggedList extends java.awt.List
/*      */ {
/* 3650 */   private java.util.List<Object> data = new LinkedList();
/*      */ 
/* 3652 */   public TaggedList(int paramInt, boolean paramBoolean) { super(paramInt, paramBoolean); }
/*      */ 
/*      */   public Object getObject(int paramInt)
/*      */   {
/* 3656 */     return this.data.get(paramInt);
/*      */   }
/*      */   @Deprecated
/*      */   public void add(String paramString) {
/* 3660 */     throw new AssertionError("should not call add in TaggedList");
/*      */   }
/*      */   public void addTaggedItem(String paramString, Object paramObject) {
/* 3663 */     super.add(paramString);
/* 3664 */     this.data.add(paramObject);
/*      */   }
/*      */   @Deprecated
/*      */   public void replaceItem(String paramString, int paramInt) {
/* 3668 */     throw new AssertionError("should not call replaceItem in TaggedList");
/*      */   }
/*      */   public void replaceTaggedItem(String paramString, Object paramObject, int paramInt) {
/* 3671 */     super.replaceItem(paramString, paramInt);
/* 3672 */     this.data.set(paramInt, paramObject);
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public void remove(int paramInt) {
/* 3677 */     super.remove(paramInt);
/*      */   }
/*      */   public void removeTaggedItem(int paramInt) {
/* 3680 */     super.remove(paramInt);
/* 3681 */     this.data.remove(paramInt);
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.tools.policytool.TaggedList
 * JD-Core Version:    0.6.2
 */