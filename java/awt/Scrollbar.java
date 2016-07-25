/*      */ package java.awt;
/*      */ 
/*      */ import java.awt.event.AdjustmentEvent;
/*      */ import java.awt.event.AdjustmentListener;
/*      */ import java.awt.peer.ScrollbarPeer;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.util.EventListener;
/*      */ import javax.accessibility.Accessible;
/*      */ import javax.accessibility.AccessibleContext;
/*      */ import javax.accessibility.AccessibleRole;
/*      */ import javax.accessibility.AccessibleState;
/*      */ import javax.accessibility.AccessibleStateSet;
/*      */ import javax.accessibility.AccessibleValue;
/*      */ 
/*      */ public class Scrollbar extends Component
/*      */   implements Adjustable, Accessible
/*      */ {
/*      */   public static final int HORIZONTAL = 0;
/*      */   public static final int VERTICAL = 1;
/*      */   int value;
/*      */   int maximum;
/*      */   int minimum;
/*      */   int visibleAmount;
/*      */   int orientation;
/*  247 */   int lineIncrement = 1;
/*      */ 
/*  258 */   int pageIncrement = 10;
/*      */   transient boolean isAdjusting;
/*      */   transient AdjustmentListener adjustmentListener;
/*      */   private static final String base = "scrollbar";
/*  274 */   private static int nameCounter = 0;
/*      */   private static final long serialVersionUID = 8451667562882310543L;
/* 1152 */   private int scrollbarSerializedDataVersion = 1;
/*      */ 
/*      */   private static native void initIDs();
/*      */ 
/*      */   public Scrollbar()
/*      */     throws HeadlessException
/*      */   {
/*  357 */     this(1, 0, 10, 0, 100);
/*      */   }
/*      */ 
/*      */   public Scrollbar(int paramInt)
/*      */     throws HeadlessException
/*      */   {
/*  376 */     this(paramInt, 0, 10, 0, 100);
/*      */   }
/*      */ 
/*      */   public Scrollbar(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*      */     throws HeadlessException
/*      */   {
/*  406 */     GraphicsEnvironment.checkHeadless();
/*  407 */     switch (paramInt1) {
/*      */     case 0:
/*      */     case 1:
/*  410 */       this.orientation = paramInt1;
/*  411 */       break;
/*      */     default:
/*  413 */       throw new IllegalArgumentException("illegal scrollbar orientation");
/*      */     }
/*  415 */     setValues(paramInt2, paramInt3, paramInt4, paramInt5);
/*      */   }
/*      */ 
/*      */   String constructComponentName()
/*      */   {
/*  423 */     synchronized (Scrollbar.class) {
/*  424 */       return "scrollbar" + nameCounter++;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void addNotify()
/*      */   {
/*  434 */     synchronized (getTreeLock()) {
/*  435 */       if (this.peer == null)
/*  436 */         this.peer = getToolkit().createScrollbar(this);
/*  437 */       super.addNotify();
/*      */     }
/*      */   }
/*      */ 
/*      */   public int getOrientation()
/*      */   {
/*  450 */     return this.orientation;
/*      */   }
/*      */ 
/*      */   public void setOrientation(int paramInt)
/*      */   {
/*  466 */     synchronized (getTreeLock()) {
/*  467 */       if (paramInt == this.orientation) {
/*  468 */         return;
/*      */       }
/*  470 */       switch (paramInt) {
/*      */       case 0:
/*      */       case 1:
/*  473 */         this.orientation = paramInt;
/*  474 */         break;
/*      */       default:
/*  476 */         throw new IllegalArgumentException("illegal scrollbar orientation");
/*      */       }
/*      */ 
/*  479 */       if (this.peer != null) {
/*  480 */         removeNotify();
/*  481 */         addNotify();
/*  482 */         invalidate();
/*      */       }
/*      */     }
/*  485 */     if (this.accessibleContext != null)
/*  486 */       this.accessibleContext.firePropertyChange("AccessibleState", paramInt == 1 ? AccessibleState.HORIZONTAL : AccessibleState.VERTICAL, paramInt == 1 ? AccessibleState.VERTICAL : AccessibleState.HORIZONTAL);
/*      */   }
/*      */ 
/*      */   public int getValue()
/*      */   {
/*  503 */     return this.value;
/*      */   }
/*      */ 
/*      */   public void setValue(int paramInt)
/*      */   {
/*  533 */     setValues(paramInt, this.visibleAmount, this.minimum, this.maximum);
/*      */   }
/*      */ 
/*      */   public int getMinimum()
/*      */   {
/*  544 */     return this.minimum;
/*      */   }
/*      */ 
/*      */   public void setMinimum(int paramInt)
/*      */   {
/*  577 */     setValues(this.value, this.visibleAmount, paramInt, this.maximum);
/*      */   }
/*      */ 
/*      */   public int getMaximum()
/*      */   {
/*  588 */     return this.maximum;
/*      */   }
/*      */ 
/*      */   public void setMaximum(int paramInt)
/*      */   {
/*  619 */     if (paramInt == -2147483648) {
/*  620 */       paramInt = -2147483647;
/*      */     }
/*      */ 
/*  623 */     if (this.minimum >= paramInt) {
/*  624 */       this.minimum = (paramInt - 1);
/*      */     }
/*      */ 
/*  629 */     setValues(this.value, this.visibleAmount, this.minimum, paramInt);
/*      */   }
/*      */ 
/*      */   public int getVisibleAmount()
/*      */   {
/*  655 */     return getVisible();
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public int getVisible()
/*      */   {
/*  664 */     return this.visibleAmount;
/*      */   }
/*      */ 
/*      */   public void setVisibleAmount(int paramInt)
/*      */   {
/*  705 */     setValues(this.value, paramInt, this.minimum, this.maximum);
/*      */   }
/*      */ 
/*      */   public void setUnitIncrement(int paramInt)
/*      */   {
/*  725 */     setLineIncrement(paramInt);
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public synchronized void setLineIncrement(int paramInt)
/*      */   {
/*  734 */     int i = paramInt < 1 ? 1 : paramInt;
/*      */ 
/*  736 */     if (this.lineIncrement == i) {
/*  737 */       return;
/*      */     }
/*  739 */     this.lineIncrement = i;
/*      */ 
/*  741 */     ScrollbarPeer localScrollbarPeer = (ScrollbarPeer)this.peer;
/*  742 */     if (localScrollbarPeer != null)
/*  743 */       localScrollbarPeer.setLineIncrement(this.lineIncrement);
/*      */   }
/*      */ 
/*      */   public int getUnitIncrement()
/*      */   {
/*  761 */     return getLineIncrement();
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public int getLineIncrement()
/*      */   {
/*  770 */     return this.lineIncrement;
/*      */   }
/*      */ 
/*      */   public void setBlockIncrement(int paramInt)
/*      */   {
/*  790 */     setPageIncrement(paramInt);
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public synchronized void setPageIncrement(int paramInt)
/*      */   {
/*  799 */     int i = paramInt < 1 ? 1 : paramInt;
/*      */ 
/*  801 */     if (this.pageIncrement == i) {
/*  802 */       return;
/*      */     }
/*  804 */     this.pageIncrement = i;
/*      */ 
/*  806 */     ScrollbarPeer localScrollbarPeer = (ScrollbarPeer)this.peer;
/*  807 */     if (localScrollbarPeer != null)
/*  808 */       localScrollbarPeer.setPageIncrement(this.pageIncrement);
/*      */   }
/*      */ 
/*      */   public int getBlockIncrement()
/*      */   {
/*  826 */     return getPageIncrement();
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public int getPageIncrement()
/*      */   {
/*  835 */     return this.pageIncrement;
/*      */   }
/*      */ 
/*      */   public void setValues(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*      */     int i;
/*  873 */     synchronized (this) {
/*  874 */       if (paramInt3 == 2147483647) {
/*  875 */         paramInt3 = 2147483646;
/*      */       }
/*  877 */       if (paramInt4 <= paramInt3) {
/*  878 */         paramInt4 = paramInt3 + 1;
/*      */       }
/*      */ 
/*  881 */       long l = paramInt4 - paramInt3;
/*  882 */       if (l > 2147483647L) {
/*  883 */         l = 2147483647L;
/*  884 */         paramInt4 = paramInt3 + (int)l;
/*      */       }
/*  886 */       if (paramInt2 > (int)l) {
/*  887 */         paramInt2 = (int)l;
/*      */       }
/*  889 */       if (paramInt2 < 1) {
/*  890 */         paramInt2 = 1;
/*      */       }
/*      */ 
/*  893 */       if (paramInt1 < paramInt3) {
/*  894 */         paramInt1 = paramInt3;
/*      */       }
/*  896 */       if (paramInt1 > paramInt4 - paramInt2) {
/*  897 */         paramInt1 = paramInt4 - paramInt2;
/*      */       }
/*      */ 
/*  900 */       i = this.value;
/*  901 */       this.value = paramInt1;
/*  902 */       this.visibleAmount = paramInt2;
/*  903 */       this.minimum = paramInt3;
/*  904 */       this.maximum = paramInt4;
/*  905 */       ScrollbarPeer localScrollbarPeer = (ScrollbarPeer)this.peer;
/*  906 */       if (localScrollbarPeer != null) {
/*  907 */         localScrollbarPeer.setValues(paramInt1, this.visibleAmount, paramInt3, paramInt4);
/*      */       }
/*      */     }
/*      */ 
/*  911 */     if ((i != paramInt1) && (this.accessibleContext != null))
/*  912 */       this.accessibleContext.firePropertyChange("AccessibleValue", Integer.valueOf(i), Integer.valueOf(paramInt1));
/*      */   }
/*      */ 
/*      */   public boolean getValueIsAdjusting()
/*      */   {
/*  928 */     return this.isAdjusting;
/*      */   }
/*      */ 
/*      */   public void setValueIsAdjusting(boolean paramBoolean)
/*      */   {
/*      */     boolean bool;
/*  941 */     synchronized (this) {
/*  942 */       bool = this.isAdjusting;
/*  943 */       this.isAdjusting = paramBoolean;
/*      */     }
/*      */ 
/*  946 */     if ((bool != paramBoolean) && (this.accessibleContext != null))
/*  947 */       this.accessibleContext.firePropertyChange("AccessibleState", bool ? AccessibleState.BUSY : null, paramBoolean ? AccessibleState.BUSY : null);
/*      */   }
/*      */ 
/*      */   public synchronized void addAdjustmentListener(AdjustmentListener paramAdjustmentListener)
/*      */   {
/*  972 */     if (paramAdjustmentListener == null) {
/*  973 */       return;
/*      */     }
/*  975 */     this.adjustmentListener = AWTEventMulticaster.add(this.adjustmentListener, paramAdjustmentListener);
/*  976 */     this.newEventsOnly = true;
/*      */   }
/*      */ 
/*      */   public synchronized void removeAdjustmentListener(AdjustmentListener paramAdjustmentListener)
/*      */   {
/*  995 */     if (paramAdjustmentListener == null) {
/*  996 */       return;
/*      */     }
/*  998 */     this.adjustmentListener = AWTEventMulticaster.remove(this.adjustmentListener, paramAdjustmentListener);
/*      */   }
/*      */ 
/*      */   public synchronized AdjustmentListener[] getAdjustmentListeners()
/*      */   {
/* 1015 */     return (AdjustmentListener[])getListeners(AdjustmentListener.class);
/*      */   }
/*      */ 
/*      */   public <T extends EventListener> T[] getListeners(Class<T> paramClass)
/*      */   {
/* 1049 */     AdjustmentListener localAdjustmentListener = null;
/* 1050 */     if (paramClass == AdjustmentListener.class)
/* 1051 */       localAdjustmentListener = this.adjustmentListener;
/*      */     else {
/* 1053 */       return super.getListeners(paramClass);
/*      */     }
/* 1055 */     return AWTEventMulticaster.getListeners(localAdjustmentListener, paramClass);
/*      */   }
/*      */ 
/*      */   boolean eventEnabled(AWTEvent paramAWTEvent)
/*      */   {
/* 1060 */     if (paramAWTEvent.id == 601) {
/* 1061 */       if (((this.eventMask & 0x100) != 0L) || (this.adjustmentListener != null))
/*      */       {
/* 1063 */         return true;
/*      */       }
/* 1065 */       return false;
/*      */     }
/* 1067 */     return super.eventEnabled(paramAWTEvent);
/*      */   }
/*      */ 
/*      */   protected void processEvent(AWTEvent paramAWTEvent)
/*      */   {
/* 1086 */     if ((paramAWTEvent instanceof AdjustmentEvent)) {
/* 1087 */       processAdjustmentEvent((AdjustmentEvent)paramAWTEvent);
/* 1088 */       return;
/*      */     }
/* 1090 */     super.processEvent(paramAWTEvent);
/*      */   }
/*      */ 
/*      */   protected void processAdjustmentEvent(AdjustmentEvent paramAdjustmentEvent)
/*      */   {
/* 1118 */     AdjustmentListener localAdjustmentListener = this.adjustmentListener;
/* 1119 */     if (localAdjustmentListener != null)
/* 1120 */       localAdjustmentListener.adjustmentValueChanged(paramAdjustmentEvent);
/*      */   }
/*      */ 
/*      */   protected String paramString()
/*      */   {
/* 1134 */     return super.paramString() + ",val=" + this.value + ",vis=" + this.visibleAmount + ",min=" + this.minimum + ",max=" + this.maximum + (this.orientation == 1 ? ",vert" : ",horz") + ",isAdjusting=" + this.isAdjusting;
/*      */   }
/*      */ 
/*      */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*      */     throws IOException
/*      */   {
/* 1175 */     paramObjectOutputStream.defaultWriteObject();
/*      */ 
/* 1177 */     AWTEventMulticaster.save(paramObjectOutputStream, "adjustmentL", this.adjustmentListener);
/* 1178 */     paramObjectOutputStream.writeObject(null);
/*      */   }
/*      */ 
/*      */   private void readObject(ObjectInputStream paramObjectInputStream)
/*      */     throws ClassNotFoundException, IOException, HeadlessException
/*      */   {
/* 1198 */     GraphicsEnvironment.checkHeadless();
/* 1199 */     paramObjectInputStream.defaultReadObject();
/*      */     Object localObject;
/* 1202 */     while (null != (localObject = paramObjectInputStream.readObject())) {
/* 1203 */       String str = ((String)localObject).intern();
/*      */ 
/* 1205 */       if ("adjustmentL" == str) {
/* 1206 */         addAdjustmentListener((AdjustmentListener)paramObjectInputStream.readObject());
/*      */       }
/*      */       else
/* 1209 */         paramObjectInputStream.readObject();
/*      */     }
/*      */   }
/*      */ 
/*      */   public AccessibleContext getAccessibleContext()
/*      */   {
/* 1230 */     if (this.accessibleContext == null) {
/* 1231 */       this.accessibleContext = new AccessibleAWTScrollBar();
/*      */     }
/* 1233 */     return this.accessibleContext;
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*  288 */     Toolkit.loadLibraries();
/*  289 */     if (!GraphicsEnvironment.isHeadless())
/*  290 */       initIDs();
/*      */   }
/*      */ 
/*      */   protected class AccessibleAWTScrollBar extends Component.AccessibleAWTComponent
/*      */     implements AccessibleValue
/*      */   {
/*      */     private static final long serialVersionUID = -344337268523697807L;
/*      */ 
/*      */     protected AccessibleAWTScrollBar()
/*      */     {
/* 1243 */       super();
/*      */     }
/*      */ 
/*      */     public AccessibleStateSet getAccessibleStateSet()
/*      */     {
/* 1259 */       AccessibleStateSet localAccessibleStateSet = super.getAccessibleStateSet();
/* 1260 */       if (Scrollbar.this.getValueIsAdjusting()) {
/* 1261 */         localAccessibleStateSet.add(AccessibleState.BUSY);
/*      */       }
/* 1263 */       if (Scrollbar.this.getOrientation() == 1)
/* 1264 */         localAccessibleStateSet.add(AccessibleState.VERTICAL);
/*      */       else {
/* 1266 */         localAccessibleStateSet.add(AccessibleState.HORIZONTAL);
/*      */       }
/* 1268 */       return localAccessibleStateSet;
/*      */     }
/*      */ 
/*      */     public AccessibleRole getAccessibleRole()
/*      */     {
/* 1278 */       return AccessibleRole.SCROLL_BAR;
/*      */     }
/*      */ 
/*      */     public AccessibleValue getAccessibleValue()
/*      */     {
/* 1291 */       return this;
/*      */     }
/*      */ 
/*      */     public Number getCurrentAccessibleValue()
/*      */     {
/* 1300 */       return Integer.valueOf(Scrollbar.this.getValue());
/*      */     }
/*      */ 
/*      */     public boolean setCurrentAccessibleValue(Number paramNumber)
/*      */     {
/* 1309 */       if ((paramNumber instanceof Integer)) {
/* 1310 */         Scrollbar.this.setValue(paramNumber.intValue());
/* 1311 */         return true;
/*      */       }
/* 1313 */       return false;
/*      */     }
/*      */ 
/*      */     public Number getMinimumAccessibleValue()
/*      */     {
/* 1323 */       return Integer.valueOf(Scrollbar.this.getMinimum());
/*      */     }
/*      */ 
/*      */     public Number getMaximumAccessibleValue()
/*      */     {
/* 1332 */       return Integer.valueOf(Scrollbar.this.getMaximum());
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.Scrollbar
 * JD-Core Version:    0.6.2
 */