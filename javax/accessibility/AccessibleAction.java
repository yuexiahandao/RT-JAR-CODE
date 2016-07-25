/*    */ package javax.accessibility;
/*    */ 
/*    */ public abstract interface AccessibleAction
/*    */ {
/* 56 */   public static final String TOGGLE_EXPAND = new String("toggleexpand");
/*    */ 
/* 63 */   public static final String INCREMENT = new String("increment");
/*    */ 
/* 71 */   public static final String DECREMENT = new String("decrement");
/*    */ 
/* 78 */   public static final String CLICK = new String("click");
/*    */ 
/* 85 */   public static final String TOGGLE_POPUP = new String("toggle popup");
/*    */ 
/*    */   public abstract int getAccessibleActionCount();
/*    */ 
/*    */   public abstract String getAccessibleActionDescription(int paramInt);
/*    */ 
/*    */   public abstract boolean doAccessibleAction(int paramInt);
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.accessibility.AccessibleAction
 * JD-Core Version:    0.6.2
 */