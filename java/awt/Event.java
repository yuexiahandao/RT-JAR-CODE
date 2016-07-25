/*     */ package java.awt;
/*     */ 
/*     */ import java.awt.event.KeyEvent;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public class Event
/*     */   implements Serializable
/*     */ {
/*     */   private transient long data;
/*     */   public static final int SHIFT_MASK = 1;
/*     */   public static final int CTRL_MASK = 2;
/*     */   public static final int META_MASK = 4;
/*     */   public static final int ALT_MASK = 8;
/*     */   public static final int HOME = 1000;
/*     */   public static final int END = 1001;
/*     */   public static final int PGUP = 1002;
/*     */   public static final int PGDN = 1003;
/*     */   public static final int UP = 1004;
/*     */   public static final int DOWN = 1005;
/*     */   public static final int LEFT = 1006;
/*     */   public static final int RIGHT = 1007;
/*     */   public static final int F1 = 1008;
/*     */   public static final int F2 = 1009;
/*     */   public static final int F3 = 1010;
/*     */   public static final int F4 = 1011;
/*     */   public static final int F5 = 1012;
/*     */   public static final int F6 = 1013;
/*     */   public static final int F7 = 1014;
/*     */   public static final int F8 = 1015;
/*     */   public static final int F9 = 1016;
/*     */   public static final int F10 = 1017;
/*     */   public static final int F11 = 1018;
/*     */   public static final int F12 = 1019;
/*     */   public static final int PRINT_SCREEN = 1020;
/*     */   public static final int SCROLL_LOCK = 1021;
/*     */   public static final int CAPS_LOCK = 1022;
/*     */   public static final int NUM_LOCK = 1023;
/*     */   public static final int PAUSE = 1024;
/*     */   public static final int INSERT = 1025;
/*     */   public static final int ENTER = 10;
/*     */   public static final int BACK_SPACE = 8;
/*     */   public static final int TAB = 9;
/*     */   public static final int ESCAPE = 27;
/*     */   public static final int DELETE = 127;
/*     */   private static final int WINDOW_EVENT = 200;
/*     */   public static final int WINDOW_DESTROY = 201;
/*     */   public static final int WINDOW_EXPOSE = 202;
/*     */   public static final int WINDOW_ICONIFY = 203;
/*     */   public static final int WINDOW_DEICONIFY = 204;
/*     */   public static final int WINDOW_MOVED = 205;
/*     */   private static final int KEY_EVENT = 400;
/*     */   public static final int KEY_PRESS = 401;
/*     */   public static final int KEY_RELEASE = 402;
/*     */   public static final int KEY_ACTION = 403;
/*     */   public static final int KEY_ACTION_RELEASE = 404;
/*     */   private static final int MOUSE_EVENT = 500;
/*     */   public static final int MOUSE_DOWN = 501;
/*     */   public static final int MOUSE_UP = 502;
/*     */   public static final int MOUSE_MOVE = 503;
/*     */   public static final int MOUSE_ENTER = 504;
/*     */   public static final int MOUSE_EXIT = 505;
/*     */   public static final int MOUSE_DRAG = 506;
/*     */   private static final int SCROLL_EVENT = 600;
/*     */   public static final int SCROLL_LINE_UP = 601;
/*     */   public static final int SCROLL_LINE_DOWN = 602;
/*     */   public static final int SCROLL_PAGE_UP = 603;
/*     */   public static final int SCROLL_PAGE_DOWN = 604;
/*     */   public static final int SCROLL_ABSOLUTE = 605;
/*     */   public static final int SCROLL_BEGIN = 606;
/*     */   public static final int SCROLL_END = 607;
/*     */   private static final int LIST_EVENT = 700;
/*     */   public static final int LIST_SELECT = 701;
/*     */   public static final int LIST_DESELECT = 702;
/*     */   private static final int MISC_EVENT = 1000;
/*     */   public static final int ACTION_EVENT = 1001;
/*     */   public static final int LOAD_FILE = 1002;
/*     */   public static final int SAVE_FILE = 1003;
/*     */   public static final int GOT_FOCUS = 1004;
/*     */   public static final int LOST_FOCUS = 1005;
/*     */   public Object target;
/*     */   public long when;
/*     */   public int id;
/*     */   public int x;
/*     */   public int y;
/*     */   public int key;
/*     */   public int modifiers;
/*     */   public int clickCount;
/*     */   public Object arg;
/*     */   public Event evt;
/* 551 */   private static final int[][] actionKeyCodes = { { 36, 1000 }, { 35, 1001 }, { 33, 1002 }, { 34, 1003 }, { 38, 1004 }, { 40, 1005 }, { 37, 1006 }, { 39, 1007 }, { 112, 1008 }, { 113, 1009 }, { 114, 1010 }, { 115, 1011 }, { 116, 1012 }, { 117, 1013 }, { 118, 1014 }, { 119, 1015 }, { 120, 1016 }, { 121, 1017 }, { 122, 1018 }, { 123, 1019 }, { 154, 1020 }, { 145, 1021 }, { 20, 1022 }, { 144, 1023 }, { 19, 1024 }, { 155, 1025 } };
/*     */ 
/* 589 */   private boolean consumed = false;
/*     */   private static final long serialVersionUID = 5488922509400504703L;
/*     */ 
/*     */   private static native void initIDs();
/*     */ 
/*     */   public Event(Object paramObject1, long paramLong, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, Object paramObject2)
/*     */   {
/* 630 */     this.target = paramObject1;
/* 631 */     this.when = paramLong;
/* 632 */     this.id = paramInt1;
/* 633 */     this.x = paramInt2;
/* 634 */     this.y = paramInt3;
/* 635 */     this.key = paramInt4;
/* 636 */     this.modifiers = paramInt5;
/* 637 */     this.arg = paramObject2;
/* 638 */     this.data = 0L;
/* 639 */     this.clickCount = 0;
/* 640 */     switch (paramInt1) {
/*     */     case 201:
/*     */     case 203:
/*     */     case 204:
/*     */     case 205:
/*     */     case 601:
/*     */     case 602:
/*     */     case 603:
/*     */     case 604:
/*     */     case 605:
/*     */     case 606:
/*     */     case 607:
/*     */     case 701:
/*     */     case 702:
/*     */     case 1001:
/* 655 */       this.consumed = true;
/* 656 */       break;
/*     */     }
/*     */   }
/*     */ 
/*     */   public Event(Object paramObject, long paramLong, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*     */   {
/* 679 */     this(paramObject, paramLong, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, null);
/*     */   }
/*     */ 
/*     */   public Event(Object paramObject1, int paramInt, Object paramObject2)
/*     */   {
/* 694 */     this(paramObject1, 0L, paramInt, 0, 0, 0, 0, paramObject2);
/*     */   }
/*     */ 
/*     */   public void translate(int paramInt1, int paramInt2)
/*     */   {
/* 714 */     this.x += paramInt1;
/* 715 */     this.y += paramInt2;
/*     */   }
/*     */ 
/*     */   public boolean shiftDown()
/*     */   {
/* 731 */     return (this.modifiers & 0x1) != 0;
/*     */   }
/*     */ 
/*     */   public boolean controlDown()
/*     */   {
/* 747 */     return (this.modifiers & 0x2) != 0;
/*     */   }
/*     */ 
/*     */   public boolean metaDown()
/*     */   {
/* 764 */     return (this.modifiers & 0x4) != 0;
/*     */   }
/*     */ 
/*     */   void consume()
/*     */   {
/* 773 */     switch (this.id) {
/*     */     case 401:
/*     */     case 402:
/*     */     case 403:
/*     */     case 404:
/* 778 */       this.consumed = true;
/* 779 */       break;
/*     */     }
/*     */   }
/*     */ 
/*     */   boolean isConsumed()
/*     */   {
/* 791 */     return this.consumed;
/*     */   }
/*     */ 
/*     */   static int getOldEventKey(KeyEvent paramKeyEvent)
/*     */   {
/* 803 */     int i = paramKeyEvent.getKeyCode();
/* 804 */     for (int j = 0; j < actionKeyCodes.length; j++) {
/* 805 */       if (actionKeyCodes[j][0] == i) {
/* 806 */         return actionKeyCodes[j][1];
/*     */       }
/*     */     }
/* 809 */     return paramKeyEvent.getKeyChar();
/*     */   }
/*     */ 
/*     */   char getKeyEventChar()
/*     */   {
/* 821 */     for (int i = 0; i < actionKeyCodes.length; i++) {
/* 822 */       if (actionKeyCodes[i][1] == this.key) {
/* 823 */         return 65535;
/*     */       }
/*     */     }
/* 826 */     return (char)this.key;
/*     */   }
/*     */ 
/*     */   protected String paramString()
/*     */   {
/* 843 */     String str = "id=" + this.id + ",x=" + this.x + ",y=" + this.y;
/* 844 */     if (this.key != 0) {
/* 845 */       str = str + ",key=" + this.key;
/*     */     }
/* 847 */     if (shiftDown()) {
/* 848 */       str = str + ",shift";
/*     */     }
/* 850 */     if (controlDown()) {
/* 851 */       str = str + ",control";
/*     */     }
/* 853 */     if (metaDown()) {
/* 854 */       str = str + ",meta";
/*     */     }
/* 856 */     if (this.target != null) {
/* 857 */       str = str + ",target=" + this.target;
/*     */     }
/* 859 */     if (this.arg != null) {
/* 860 */       str = str + ",arg=" + this.arg;
/*     */     }
/* 862 */     return str;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 877 */     return getClass().getName() + "[" + paramString() + "]";
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 598 */     Toolkit.loadLibraries();
/* 599 */     if (!GraphicsEnvironment.isHeadless())
/* 600 */       initIDs();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.Event
 * JD-Core Version:    0.6.2
 */