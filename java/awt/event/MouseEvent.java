/*      */ package java.awt.event;
/*      */ 
/*      */ import java.awt.Component;
/*      */ import java.awt.GraphicsEnvironment;
/*      */ import java.awt.IllegalComponentStateException;
/*      */ import java.awt.Point;
/*      */ import java.awt.Toolkit;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import sun.awt.SunToolkit;
/*      */ 
/*      */ public class MouseEvent extends InputEvent
/*      */ {
/*      */   public static final int MOUSE_FIRST = 500;
/*      */   public static final int MOUSE_LAST = 507;
/*      */   public static final int MOUSE_CLICKED = 500;
/*      */   public static final int MOUSE_PRESSED = 501;
/*      */   public static final int MOUSE_RELEASED = 502;
/*      */   public static final int MOUSE_MOVED = 503;
/*      */   public static final int MOUSE_ENTERED = 504;
/*      */   public static final int MOUSE_EXITED = 505;
/*      */   public static final int MOUSE_DRAGGED = 506;
/*      */   public static final int MOUSE_WHEEL = 507;
/*      */   public static final int NOBUTTON = 0;
/*      */   public static final int BUTTON1 = 1;
/*      */   public static final int BUTTON2 = 2;
/*      */   public static final int BUTTON3 = 3;
/*      */   int x;
/*      */   int y;
/*      */   private int xAbs;
/*      */   private int yAbs;
/*      */   int clickCount;
/*      */   int button;
/*  376 */   boolean popupTrigger = false;
/*      */   private static final long serialVersionUID = -991214153494842848L;
/*      */   private static int cachedNumberOfButtons;
/*  624 */   private transient boolean shouldExcludeButtonFromExtModifiers = false;
/*      */ 
/*      */   private static native void initIDs();
/*      */ 
/*      */   public Point getLocationOnScreen()
/*      */   {
/*  425 */     return new Point(this.xAbs, this.yAbs);
/*      */   }
/*      */ 
/*      */   public int getXOnScreen()
/*      */   {
/*  442 */     return this.xAbs;
/*      */   }
/*      */ 
/*      */   public int getYOnScreen()
/*      */   {
/*  459 */     return this.yAbs;
/*      */   }
/*      */ 
/*      */   public MouseEvent(Component paramComponent, int paramInt1, long paramLong, int paramInt2, int paramInt3, int paramInt4, int paramInt5, boolean paramBoolean, int paramInt6)
/*      */   {
/*  554 */     this(paramComponent, paramInt1, paramLong, paramInt2, paramInt3, paramInt4, 0, 0, paramInt5, paramBoolean, paramInt6);
/*  555 */     Point localPoint = new Point(0, 0);
/*      */     try {
/*  557 */       localPoint = paramComponent.getLocationOnScreen();
/*  558 */       this.xAbs = (localPoint.x + paramInt3);
/*  559 */       this.yAbs = (localPoint.y + paramInt4);
/*      */     } catch (IllegalComponentStateException localIllegalComponentStateException) {
/*  561 */       this.xAbs = 0;
/*  562 */       this.yAbs = 0;
/*      */     }
/*      */   }
/*      */ 
/*      */   public MouseEvent(Component paramComponent, int paramInt1, long paramLong, int paramInt2, int paramInt3, int paramInt4, int paramInt5, boolean paramBoolean)
/*      */   {
/*  618 */     this(paramComponent, paramInt1, paramLong, paramInt2, paramInt3, paramInt4, paramInt5, paramBoolean, 0);
/*      */   }
/*      */ 
/*      */   public int getModifiersEx()
/*      */   {
/*  630 */     int i = this.modifiers;
/*  631 */     if (this.shouldExcludeButtonFromExtModifiers) {
/*  632 */       i &= (InputEvent.getMaskForButton(getButton()) ^ 0xFFFFFFFF);
/*      */     }
/*  634 */     return i & 0xFFFFFFC0;
/*      */   }
/*      */ 
/*      */   public MouseEvent(Component paramComponent, int paramInt1, long paramLong, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, boolean paramBoolean, int paramInt8)
/*      */   {
/*  733 */     super(paramComponent, paramInt1, paramLong, paramInt2);
/*  734 */     this.x = paramInt3;
/*  735 */     this.y = paramInt4;
/*  736 */     this.xAbs = paramInt5;
/*  737 */     this.yAbs = paramInt6;
/*  738 */     this.clickCount = paramInt7;
/*  739 */     this.popupTrigger = paramBoolean;
/*  740 */     if (paramInt8 < 0) {
/*  741 */       throw new IllegalArgumentException("Invalid button value :" + paramInt8);
/*      */     }
/*  743 */     if (paramInt8 > 3) {
/*  744 */       if (!Toolkit.getDefaultToolkit().areExtraMouseButtonsEnabled()) {
/*  745 */         throw new IllegalArgumentException("Extra mouse events are disabled " + paramInt8);
/*      */       }
/*  747 */       if (paramInt8 > cachedNumberOfButtons) {
/*  748 */         throw new IllegalArgumentException("Nonexistent button " + paramInt8);
/*      */       }
/*      */ 
/*  759 */       if ((getModifiersEx() != 0) && (
/*  760 */         (paramInt1 == 502) || (paramInt1 == 500))) {
/*  761 */         this.shouldExcludeButtonFromExtModifiers = true;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  766 */     this.button = paramInt8;
/*      */ 
/*  768 */     if ((getModifiers() != 0) && (getModifiersEx() == 0))
/*  769 */       setNewModifiers();
/*  770 */     else if ((getModifiers() == 0) && ((getModifiersEx() != 0) || (paramInt8 != 0)) && (paramInt8 <= 3))
/*      */     {
/*  774 */       setOldModifiers();
/*      */     }
/*      */   }
/*      */ 
/*      */   public int getX()
/*      */   {
/*  786 */     return this.x;
/*      */   }
/*      */ 
/*      */   public int getY()
/*      */   {
/*  797 */     return this.y;
/*      */   }
/*      */ 
/*      */   public Point getPoint()
/*      */   {
/*      */     int i;
/*      */     int j;
/*  810 */     synchronized (this) {
/*  811 */       i = this.x;
/*  812 */       j = this.y;
/*      */     }
/*  814 */     return new Point(i, j);
/*      */   }
/*      */ 
/*      */   public synchronized void translatePoint(int paramInt1, int paramInt2)
/*      */   {
/*  828 */     this.x += paramInt1;
/*  829 */     this.y += paramInt2;
/*      */   }
/*      */ 
/*      */   public int getClickCount()
/*      */   {
/*  838 */     return this.clickCount;
/*      */   }
/*      */ 
/*      */   public int getButton()
/*      */   {
/*  894 */     return this.button;
/*      */   }
/*      */ 
/*      */   public boolean isPopupTrigger()
/*      */   {
/*  910 */     return this.popupTrigger;
/*      */   }
/*      */ 
/*      */   public static String getMouseModifiersText(int paramInt)
/*      */   {
/*  939 */     StringBuilder localStringBuilder = new StringBuilder();
/*  940 */     if ((paramInt & 0x8) != 0) {
/*  941 */       localStringBuilder.append(Toolkit.getProperty("AWT.alt", "Alt"));
/*  942 */       localStringBuilder.append("+");
/*      */     }
/*  944 */     if ((paramInt & 0x4) != 0) {
/*  945 */       localStringBuilder.append(Toolkit.getProperty("AWT.meta", "Meta"));
/*  946 */       localStringBuilder.append("+");
/*      */     }
/*  948 */     if ((paramInt & 0x2) != 0) {
/*  949 */       localStringBuilder.append(Toolkit.getProperty("AWT.control", "Ctrl"));
/*  950 */       localStringBuilder.append("+");
/*      */     }
/*  952 */     if ((paramInt & 0x1) != 0) {
/*  953 */       localStringBuilder.append(Toolkit.getProperty("AWT.shift", "Shift"));
/*  954 */       localStringBuilder.append("+");
/*      */     }
/*  956 */     if ((paramInt & 0x20) != 0) {
/*  957 */       localStringBuilder.append(Toolkit.getProperty("AWT.altGraph", "Alt Graph"));
/*  958 */       localStringBuilder.append("+");
/*      */     }
/*  960 */     if ((paramInt & 0x10) != 0) {
/*  961 */       localStringBuilder.append(Toolkit.getProperty("AWT.button1", "Button1"));
/*  962 */       localStringBuilder.append("+");
/*      */     }
/*  964 */     if ((paramInt & 0x8) != 0) {
/*  965 */       localStringBuilder.append(Toolkit.getProperty("AWT.button2", "Button2"));
/*  966 */       localStringBuilder.append("+");
/*      */     }
/*  968 */     if ((paramInt & 0x4) != 0) {
/*  969 */       localStringBuilder.append(Toolkit.getProperty("AWT.button3", "Button3"));
/*  970 */       localStringBuilder.append("+");
/*      */     }
/*      */ 
/*  980 */     for (int j = 1; j <= cachedNumberOfButtons; j++) {
/*  981 */       int i = InputEvent.getMaskForButton(j);
/*  982 */       if (((paramInt & i) != 0) && (localStringBuilder.indexOf(Toolkit.getProperty("AWT.button" + j, "Button" + j)) == -1))
/*      */       {
/*  985 */         localStringBuilder.append(Toolkit.getProperty("AWT.button" + j, "Button" + j));
/*  986 */         localStringBuilder.append("+");
/*      */       }
/*      */     }
/*      */ 
/*  990 */     if (localStringBuilder.length() > 0) {
/*  991 */       localStringBuilder.setLength(localStringBuilder.length() - 1);
/*      */     }
/*  993 */     return localStringBuilder.toString();
/*      */   }
/*      */ 
/*      */   public String paramString()
/*      */   {
/* 1003 */     StringBuilder localStringBuilder = new StringBuilder(80);
/*      */ 
/* 1005 */     switch (this.id) {
/*      */     case 501:
/* 1007 */       localStringBuilder.append("MOUSE_PRESSED");
/* 1008 */       break;
/*      */     case 502:
/* 1010 */       localStringBuilder.append("MOUSE_RELEASED");
/* 1011 */       break;
/*      */     case 500:
/* 1013 */       localStringBuilder.append("MOUSE_CLICKED");
/* 1014 */       break;
/*      */     case 504:
/* 1016 */       localStringBuilder.append("MOUSE_ENTERED");
/* 1017 */       break;
/*      */     case 505:
/* 1019 */       localStringBuilder.append("MOUSE_EXITED");
/* 1020 */       break;
/*      */     case 503:
/* 1022 */       localStringBuilder.append("MOUSE_MOVED");
/* 1023 */       break;
/*      */     case 506:
/* 1025 */       localStringBuilder.append("MOUSE_DRAGGED");
/* 1026 */       break;
/*      */     case 507:
/* 1028 */       localStringBuilder.append("MOUSE_WHEEL");
/* 1029 */       break;
/*      */     default:
/* 1031 */       localStringBuilder.append("unknown type");
/*      */     }
/*      */ 
/* 1035 */     localStringBuilder.append(",(").append(this.x).append(",").append(this.y).append(")");
/* 1036 */     localStringBuilder.append(",absolute(").append(this.xAbs).append(",").append(this.yAbs).append(")");
/*      */ 
/* 1038 */     if ((this.id != 506) && (this.id != 503)) {
/* 1039 */       localStringBuilder.append(",button=").append(getButton());
/*      */     }
/*      */ 
/* 1042 */     if (getModifiers() != 0) {
/* 1043 */       localStringBuilder.append(",modifiers=").append(getMouseModifiersText(this.modifiers));
/*      */     }
/*      */ 
/* 1046 */     if (getModifiersEx() != 0)
/*      */     {
/* 1049 */       localStringBuilder.append(",extModifiers=").append(getModifiersExText(getModifiersEx()));
/*      */     }
/*      */ 
/* 1052 */     localStringBuilder.append(",clickCount=").append(this.clickCount);
/*      */ 
/* 1054 */     return localStringBuilder.toString();
/*      */   }
/*      */ 
/*      */   private void setNewModifiers()
/*      */   {
/* 1062 */     if ((this.modifiers & 0x10) != 0) {
/* 1063 */       this.modifiers |= 1024;
/*      */     }
/* 1065 */     if ((this.modifiers & 0x8) != 0) {
/* 1066 */       this.modifiers |= 2048;
/*      */     }
/* 1068 */     if ((this.modifiers & 0x4) != 0) {
/* 1069 */       this.modifiers |= 4096;
/*      */     }
/* 1071 */     if ((this.id == 501) || (this.id == 502) || (this.id == 500))
/*      */     {
/* 1075 */       if ((this.modifiers & 0x10) != 0) {
/* 1076 */         this.button = 1;
/* 1077 */         this.modifiers &= -13;
/* 1078 */         if (this.id != 501)
/* 1079 */           this.modifiers &= -1025;
/*      */       }
/* 1081 */       else if ((this.modifiers & 0x8) != 0) {
/* 1082 */         this.button = 2;
/* 1083 */         this.modifiers &= -21;
/* 1084 */         if (this.id != 501)
/* 1085 */           this.modifiers &= -2049;
/*      */       }
/* 1087 */       else if ((this.modifiers & 0x4) != 0) {
/* 1088 */         this.button = 3;
/* 1089 */         this.modifiers &= -25;
/* 1090 */         if (this.id != 501) {
/* 1091 */           this.modifiers &= -4097;
/*      */         }
/*      */       }
/*      */     }
/* 1095 */     if ((this.modifiers & 0x8) != 0) {
/* 1096 */       this.modifiers |= 512;
/*      */     }
/* 1098 */     if ((this.modifiers & 0x4) != 0) {
/* 1099 */       this.modifiers |= 256;
/*      */     }
/* 1101 */     if ((this.modifiers & 0x1) != 0) {
/* 1102 */       this.modifiers |= 64;
/*      */     }
/* 1104 */     if ((this.modifiers & 0x2) != 0) {
/* 1105 */       this.modifiers |= 128;
/*      */     }
/* 1107 */     if ((this.modifiers & 0x20) != 0)
/* 1108 */       this.modifiers |= 8192;
/*      */   }
/*      */ 
/*      */   private void setOldModifiers()
/*      */   {
/* 1116 */     if ((this.id == 501) || (this.id == 502) || (this.id == 500))
/*      */     {
/* 1120 */       switch (this.button) {
/*      */       case 1:
/* 1122 */         this.modifiers |= 16;
/* 1123 */         break;
/*      */       case 2:
/* 1125 */         this.modifiers |= 8;
/* 1126 */         break;
/*      */       case 3:
/* 1128 */         this.modifiers |= 4;
/*      */       }
/*      */     }
/*      */     else {
/* 1132 */       if ((this.modifiers & 0x400) != 0) {
/* 1133 */         this.modifiers |= 16;
/*      */       }
/* 1135 */       if ((this.modifiers & 0x800) != 0) {
/* 1136 */         this.modifiers |= 8;
/*      */       }
/* 1138 */       if ((this.modifiers & 0x1000) != 0) {
/* 1139 */         this.modifiers |= 4;
/*      */       }
/*      */     }
/* 1142 */     if ((this.modifiers & 0x200) != 0) {
/* 1143 */       this.modifiers |= 8;
/*      */     }
/* 1145 */     if ((this.modifiers & 0x100) != 0) {
/* 1146 */       this.modifiers |= 4;
/*      */     }
/* 1148 */     if ((this.modifiers & 0x40) != 0) {
/* 1149 */       this.modifiers |= 1;
/*      */     }
/* 1151 */     if ((this.modifiers & 0x80) != 0) {
/* 1152 */       this.modifiers |= 2;
/*      */     }
/* 1154 */     if ((this.modifiers & 0x2000) != 0)
/* 1155 */       this.modifiers |= 32;
/*      */   }
/*      */ 
/*      */   private void readObject(ObjectInputStream paramObjectInputStream)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/* 1165 */     paramObjectInputStream.defaultReadObject();
/* 1166 */     if ((getModifiers() != 0) && (getModifiersEx() == 0))
/* 1167 */       setNewModifiers();
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*  390 */     NativeLibLoader.loadLibraries();
/*  391 */     if (!GraphicsEnvironment.isHeadless()) {
/*  392 */       initIDs();
/*      */     }
/*  394 */     Toolkit localToolkit = Toolkit.getDefaultToolkit();
/*  395 */     if ((localToolkit instanceof SunToolkit)) {
/*  396 */       cachedNumberOfButtons = ((SunToolkit)localToolkit).getNumberOfButtons();
/*      */     }
/*      */     else
/*      */     {
/*  400 */       cachedNumberOfButtons = 3;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.event.MouseEvent
 * JD-Core Version:    0.6.2
 */