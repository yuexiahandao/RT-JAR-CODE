/*     */ package com.sun.java.swing.plaf.windows;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Point;
/*     */ import java.util.EnumMap;
/*     */ import javax.swing.JComponent;
/*     */ import sun.awt.windows.ThemeReader;
/*     */ 
/*     */ class TMSchema
/*     */ {
/*     */   public static enum Control
/*     */   {
/*  65 */     BUTTON, 
/*  66 */     COMBOBOX, 
/*  67 */     EDIT, 
/*  68 */     HEADER, 
/*  69 */     LISTBOX, 
/*  70 */     LISTVIEW, 
/*  71 */     MENU, 
/*  72 */     PROGRESS, 
/*  73 */     REBAR, 
/*  74 */     SCROLLBAR, 
/*  75 */     SPIN, 
/*  76 */     TAB, 
/*  77 */     TOOLBAR, 
/*  78 */     TRACKBAR, 
/*  79 */     TREEVIEW, 
/*  80 */     WINDOW;
/*     */   }
/*     */ 
/*     */   public static enum Part
/*     */   {
/*  88 */     MENU(TMSchema.Control.MENU, 0), 
/*  89 */     MP_BARBACKGROUND(TMSchema.Control.MENU, 7), 
/*  90 */     MP_BARITEM(TMSchema.Control.MENU, 8), 
/*  91 */     MP_POPUPBACKGROUND(TMSchema.Control.MENU, 9), 
/*  92 */     MP_POPUPBORDERS(TMSchema.Control.MENU, 10), 
/*  93 */     MP_POPUPCHECK(TMSchema.Control.MENU, 11), 
/*  94 */     MP_POPUPCHECKBACKGROUND(TMSchema.Control.MENU, 12), 
/*  95 */     MP_POPUPGUTTER(TMSchema.Control.MENU, 13), 
/*  96 */     MP_POPUPITEM(TMSchema.Control.MENU, 14), 
/*  97 */     MP_POPUPSEPARATOR(TMSchema.Control.MENU, 15), 
/*  98 */     MP_POPUPSUBMENU(TMSchema.Control.MENU, 16), 
/*     */ 
/* 100 */     BP_PUSHBUTTON(TMSchema.Control.BUTTON, 1), 
/* 101 */     BP_RADIOBUTTON(TMSchema.Control.BUTTON, 2), 
/* 102 */     BP_CHECKBOX(TMSchema.Control.BUTTON, 3), 
/* 103 */     BP_GROUPBOX(TMSchema.Control.BUTTON, 4), 
/*     */ 
/* 105 */     CP_COMBOBOX(TMSchema.Control.COMBOBOX, 0), 
/* 106 */     CP_DROPDOWNBUTTON(TMSchema.Control.COMBOBOX, 1), 
/* 107 */     CP_BACKGROUND(TMSchema.Control.COMBOBOX, 2), 
/* 108 */     CP_TRANSPARENTBACKGROUND(TMSchema.Control.COMBOBOX, 3), 
/* 109 */     CP_BORDER(TMSchema.Control.COMBOBOX, 4), 
/* 110 */     CP_READONLY(TMSchema.Control.COMBOBOX, 5), 
/* 111 */     CP_DROPDOWNBUTTONRIGHT(TMSchema.Control.COMBOBOX, 6), 
/* 112 */     CP_DROPDOWNBUTTONLEFT(TMSchema.Control.COMBOBOX, 7), 
/* 113 */     CP_CUEBANNER(TMSchema.Control.COMBOBOX, 8), 
/*     */ 
/* 116 */     EP_EDIT(TMSchema.Control.EDIT, 0), 
/* 117 */     EP_EDITTEXT(TMSchema.Control.EDIT, 1), 
/*     */ 
/* 119 */     HP_HEADERITEM(TMSchema.Control.HEADER, 1), 
/* 120 */     HP_HEADERSORTARROW(TMSchema.Control.HEADER, 4), 
/*     */ 
/* 122 */     LBP_LISTBOX(TMSchema.Control.LISTBOX, 0), 
/*     */ 
/* 124 */     LVP_LISTVIEW(TMSchema.Control.LISTVIEW, 0), 
/*     */ 
/* 126 */     PP_PROGRESS(TMSchema.Control.PROGRESS, 0), 
/* 127 */     PP_BAR(TMSchema.Control.PROGRESS, 1), 
/* 128 */     PP_BARVERT(TMSchema.Control.PROGRESS, 2), 
/* 129 */     PP_CHUNK(TMSchema.Control.PROGRESS, 3), 
/* 130 */     PP_CHUNKVERT(TMSchema.Control.PROGRESS, 4), 
/*     */ 
/* 132 */     RP_GRIPPER(TMSchema.Control.REBAR, 1), 
/* 133 */     RP_GRIPPERVERT(TMSchema.Control.REBAR, 2), 
/*     */ 
/* 135 */     SBP_SCROLLBAR(TMSchema.Control.SCROLLBAR, 0), 
/* 136 */     SBP_ARROWBTN(TMSchema.Control.SCROLLBAR, 1), 
/* 137 */     SBP_THUMBBTNHORZ(TMSchema.Control.SCROLLBAR, 2), 
/* 138 */     SBP_THUMBBTNVERT(TMSchema.Control.SCROLLBAR, 3), 
/* 139 */     SBP_LOWERTRACKHORZ(TMSchema.Control.SCROLLBAR, 4), 
/* 140 */     SBP_UPPERTRACKHORZ(TMSchema.Control.SCROLLBAR, 5), 
/* 141 */     SBP_LOWERTRACKVERT(TMSchema.Control.SCROLLBAR, 6), 
/* 142 */     SBP_UPPERTRACKVERT(TMSchema.Control.SCROLLBAR, 7), 
/* 143 */     SBP_GRIPPERHORZ(TMSchema.Control.SCROLLBAR, 8), 
/* 144 */     SBP_GRIPPERVERT(TMSchema.Control.SCROLLBAR, 9), 
/* 145 */     SBP_SIZEBOX(TMSchema.Control.SCROLLBAR, 10), 
/*     */ 
/* 147 */     SPNP_UP(TMSchema.Control.SPIN, 1), 
/* 148 */     SPNP_DOWN(TMSchema.Control.SPIN, 2), 
/*     */ 
/* 150 */     TABP_TABITEM(TMSchema.Control.TAB, 1), 
/* 151 */     TABP_TABITEMLEFTEDGE(TMSchema.Control.TAB, 2), 
/* 152 */     TABP_TABITEMRIGHTEDGE(TMSchema.Control.TAB, 3), 
/* 153 */     TABP_PANE(TMSchema.Control.TAB, 9), 
/*     */ 
/* 155 */     TP_TOOLBAR(TMSchema.Control.TOOLBAR, 0), 
/* 156 */     TP_BUTTON(TMSchema.Control.TOOLBAR, 1), 
/* 157 */     TP_SEPARATOR(TMSchema.Control.TOOLBAR, 5), 
/* 158 */     TP_SEPARATORVERT(TMSchema.Control.TOOLBAR, 6), 
/*     */ 
/* 160 */     TKP_TRACK(TMSchema.Control.TRACKBAR, 1), 
/* 161 */     TKP_TRACKVERT(TMSchema.Control.TRACKBAR, 2), 
/* 162 */     TKP_THUMB(TMSchema.Control.TRACKBAR, 3), 
/* 163 */     TKP_THUMBBOTTOM(TMSchema.Control.TRACKBAR, 4), 
/* 164 */     TKP_THUMBTOP(TMSchema.Control.TRACKBAR, 5), 
/* 165 */     TKP_THUMBVERT(TMSchema.Control.TRACKBAR, 6), 
/* 166 */     TKP_THUMBLEFT(TMSchema.Control.TRACKBAR, 7), 
/* 167 */     TKP_THUMBRIGHT(TMSchema.Control.TRACKBAR, 8), 
/* 168 */     TKP_TICS(TMSchema.Control.TRACKBAR, 9), 
/* 169 */     TKP_TICSVERT(TMSchema.Control.TRACKBAR, 10), 
/*     */ 
/* 171 */     TVP_TREEVIEW(TMSchema.Control.TREEVIEW, 0), 
/* 172 */     TVP_GLYPH(TMSchema.Control.TREEVIEW, 2), 
/*     */ 
/* 174 */     WP_WINDOW(TMSchema.Control.WINDOW, 0), 
/* 175 */     WP_CAPTION(TMSchema.Control.WINDOW, 1), 
/* 176 */     WP_MINCAPTION(TMSchema.Control.WINDOW, 3), 
/* 177 */     WP_MAXCAPTION(TMSchema.Control.WINDOW, 5), 
/* 178 */     WP_FRAMELEFT(TMSchema.Control.WINDOW, 7), 
/* 179 */     WP_FRAMERIGHT(TMSchema.Control.WINDOW, 8), 
/* 180 */     WP_FRAMEBOTTOM(TMSchema.Control.WINDOW, 9), 
/* 181 */     WP_SYSBUTTON(TMSchema.Control.WINDOW, 13), 
/* 182 */     WP_MDISYSBUTTON(TMSchema.Control.WINDOW, 14), 
/* 183 */     WP_MINBUTTON(TMSchema.Control.WINDOW, 15), 
/* 184 */     WP_MDIMINBUTTON(TMSchema.Control.WINDOW, 16), 
/* 185 */     WP_MAXBUTTON(TMSchema.Control.WINDOW, 17), 
/* 186 */     WP_CLOSEBUTTON(TMSchema.Control.WINDOW, 18), 
/* 187 */     WP_MDICLOSEBUTTON(TMSchema.Control.WINDOW, 20), 
/* 188 */     WP_RESTOREBUTTON(TMSchema.Control.WINDOW, 21), 
/* 189 */     WP_MDIRESTOREBUTTON(TMSchema.Control.WINDOW, 22);
/*     */ 
/*     */     private final TMSchema.Control control;
/*     */     private final int value;
/*     */ 
/* 195 */     private Part(TMSchema.Control paramControl, int paramInt) { this.control = paramControl;
/* 196 */       this.value = paramInt; }
/*     */ 
/*     */     public int getValue()
/*     */     {
/* 200 */       return this.value;
/*     */     }
/*     */ 
/*     */     public String getControlName(Component paramComponent) {
/* 204 */       String str1 = "";
/* 205 */       if ((paramComponent instanceof JComponent)) {
/* 206 */         JComponent localJComponent = (JComponent)paramComponent;
/* 207 */         String str2 = (String)localJComponent.getClientProperty("XPStyle.subAppName");
/* 208 */         if (str2 != null) {
/* 209 */           str1 = str2 + "::";
/*     */         }
/*     */       }
/* 212 */       return str1 + this.control.toString();
/*     */     }
/*     */ 
/*     */     public String toString() {
/* 216 */       return this.control.toString() + "." + name();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static enum Prop
/*     */   {
/* 473 */     COLOR(Color.class, 204), 
/* 474 */     SIZE(Dimension.class, 207), 
/*     */ 
/* 476 */     FLATMENUS(Boolean.class, 1001), 
/*     */ 
/* 478 */     BORDERONLY(Boolean.class, 2203), 
/*     */ 
/* 480 */     IMAGECOUNT(Integer.class, 2401), 
/* 481 */     BORDERSIZE(Integer.class, 2403), 
/*     */ 
/* 483 */     PROGRESSCHUNKSIZE(Integer.class, 2411), 
/* 484 */     PROGRESSSPACESIZE(Integer.class, 2412), 
/*     */ 
/* 486 */     TEXTSHADOWOFFSET(Point.class, 3402), 
/*     */ 
/* 488 */     NORMALSIZE(Dimension.class, 3409), 
/*     */ 
/* 491 */     SIZINGMARGINS(Insets.class, 3601), 
/* 492 */     CONTENTMARGINS(Insets.class, 3602), 
/* 493 */     CAPTIONMARGINS(Insets.class, 3603), 
/*     */ 
/* 495 */     BORDERCOLOR(Color.class, 3801), 
/* 496 */     FILLCOLOR(Color.class, 3802), 
/* 497 */     TEXTCOLOR(Color.class, 3803), 
/*     */ 
/* 499 */     TEXTSHADOWCOLOR(Color.class, 3818), 
/*     */ 
/* 501 */     BGTYPE(Integer.class, 4001), 
/*     */ 
/* 503 */     TEXTSHADOWTYPE(Integer.class, 4010), 
/*     */ 
/* 505 */     TRANSITIONDURATIONS(Integer.class, 6000);
/*     */ 
/*     */     private final Class type;
/*     */     private final int value;
/*     */ 
/* 511 */     private Prop(Class paramClass, int paramInt) { this.type = paramClass;
/* 512 */       this.value = paramInt; }
/*     */ 
/*     */     public int getValue()
/*     */     {
/* 516 */       return this.value;
/*     */     }
/*     */ 
/*     */     public String toString() {
/* 520 */       return name() + "[" + this.type.getName() + "] = " + this.value;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static enum State
/*     */   {
/* 225 */     ACTIVE, 
/* 226 */     ASSIST, 
/* 227 */     BITMAP, 
/* 228 */     CHECKED, 
/* 229 */     CHECKEDDISABLED, 
/* 230 */     CHECKEDHOT, 
/* 231 */     CHECKEDNORMAL, 
/* 232 */     CHECKEDPRESSED, 
/* 233 */     CHECKMARKNORMAL, 
/* 234 */     CHECKMARKDISABLED, 
/* 235 */     BULLETNORMAL, 
/* 236 */     BULLETDISABLED, 
/* 237 */     CLOSED, 
/* 238 */     DEFAULTED, 
/* 239 */     DISABLED, 
/* 240 */     DISABLEDHOT, 
/* 241 */     DISABLEDPUSHED, 
/* 242 */     DOWNDISABLED, 
/* 243 */     DOWNHOT, 
/* 244 */     DOWNNORMAL, 
/* 245 */     DOWNPRESSED, 
/* 246 */     FOCUSED, 
/* 247 */     HOT, 
/* 248 */     HOTCHECKED, 
/* 249 */     ICONHOT, 
/* 250 */     ICONNORMAL, 
/* 251 */     ICONPRESSED, 
/* 252 */     ICONSORTEDHOT, 
/* 253 */     ICONSORTEDNORMAL, 
/* 254 */     ICONSORTEDPRESSED, 
/* 255 */     INACTIVE, 
/* 256 */     INACTIVENORMAL, 
/* 257 */     INACTIVEHOT, 
/* 258 */     INACTIVEPUSHED, 
/* 259 */     INACTIVEDISABLED, 
/* 260 */     LEFTDISABLED, 
/* 261 */     LEFTHOT, 
/* 262 */     LEFTNORMAL, 
/* 263 */     LEFTPRESSED, 
/* 264 */     MIXEDDISABLED, 
/* 265 */     MIXEDHOT, 
/* 266 */     MIXEDNORMAL, 
/* 267 */     MIXEDPRESSED, 
/* 268 */     NORMAL, 
/* 269 */     PRESSED, 
/* 270 */     OPENED, 
/* 271 */     PUSHED, 
/* 272 */     READONLY, 
/* 273 */     RIGHTDISABLED, 
/* 274 */     RIGHTHOT, 
/* 275 */     RIGHTNORMAL, 
/* 276 */     RIGHTPRESSED, 
/* 277 */     SELECTED, 
/* 278 */     UNCHECKEDDISABLED, 
/* 279 */     UNCHECKEDHOT, 
/* 280 */     UNCHECKEDNORMAL, 
/* 281 */     UNCHECKEDPRESSED, 
/* 282 */     UPDISABLED, 
/* 283 */     UPHOT, 
/* 284 */     UPNORMAL, 
/* 285 */     UPPRESSED, 
/* 286 */     HOVER, 
/* 287 */     UPHOVER, 
/* 288 */     DOWNHOVER, 
/* 289 */     LEFTHOVER, 
/* 290 */     RIGHTHOVER, 
/* 291 */     SORTEDDOWN, 
/* 292 */     SORTEDHOT, 
/* 293 */     SORTEDNORMAL, 
/* 294 */     SORTEDPRESSED, 
/* 295 */     SORTEDUP;
/*     */ 
/*     */     private static EnumMap<TMSchema.Part, State[]> stateMap;
/*     */ 
/*     */     private static synchronized void initStates()
/*     */     {
/* 304 */       stateMap = new EnumMap(TMSchema.Part.class);
/*     */ 
/* 306 */       stateMap.put(TMSchema.Part.EP_EDITTEXT, new State[] { NORMAL, HOT, SELECTED, DISABLED, FOCUSED, READONLY, ASSIST });
/*     */ 
/* 311 */       stateMap.put(TMSchema.Part.BP_PUSHBUTTON, new State[] { NORMAL, HOT, PRESSED, DISABLED, DEFAULTED });
/*     */ 
/* 314 */       stateMap.put(TMSchema.Part.BP_RADIOBUTTON, new State[] { UNCHECKEDNORMAL, UNCHECKEDHOT, UNCHECKEDPRESSED, UNCHECKEDDISABLED, CHECKEDNORMAL, CHECKEDHOT, CHECKEDPRESSED, CHECKEDDISABLED });
/*     */ 
/* 320 */       stateMap.put(TMSchema.Part.BP_CHECKBOX, new State[] { UNCHECKEDNORMAL, UNCHECKEDHOT, UNCHECKEDPRESSED, UNCHECKEDDISABLED, CHECKEDNORMAL, CHECKEDHOT, CHECKEDPRESSED, CHECKEDDISABLED, MIXEDNORMAL, MIXEDHOT, MIXEDPRESSED, MIXEDDISABLED });
/*     */ 
/* 327 */       State[] arrayOfState1 = { NORMAL, HOT, PRESSED, DISABLED };
/* 328 */       stateMap.put(TMSchema.Part.CP_COMBOBOX, arrayOfState1);
/* 329 */       stateMap.put(TMSchema.Part.CP_DROPDOWNBUTTON, arrayOfState1);
/* 330 */       stateMap.put(TMSchema.Part.CP_BACKGROUND, arrayOfState1);
/* 331 */       stateMap.put(TMSchema.Part.CP_TRANSPARENTBACKGROUND, arrayOfState1);
/* 332 */       stateMap.put(TMSchema.Part.CP_BORDER, arrayOfState1);
/* 333 */       stateMap.put(TMSchema.Part.CP_READONLY, arrayOfState1);
/* 334 */       stateMap.put(TMSchema.Part.CP_DROPDOWNBUTTONRIGHT, arrayOfState1);
/* 335 */       stateMap.put(TMSchema.Part.CP_DROPDOWNBUTTONLEFT, arrayOfState1);
/* 336 */       stateMap.put(TMSchema.Part.CP_CUEBANNER, arrayOfState1);
/*     */ 
/* 338 */       stateMap.put(TMSchema.Part.HP_HEADERITEM, new State[] { NORMAL, HOT, PRESSED, SORTEDNORMAL, SORTEDHOT, SORTEDPRESSED, ICONNORMAL, ICONHOT, ICONPRESSED, ICONSORTEDNORMAL, ICONSORTEDHOT, ICONSORTEDPRESSED });
/*     */ 
/* 343 */       stateMap.put(TMSchema.Part.HP_HEADERSORTARROW, new State[] { SORTEDDOWN, SORTEDUP });
/*     */ 
/* 346 */       State[] arrayOfState2 = { NORMAL, HOT, PRESSED, DISABLED, HOVER };
/* 347 */       stateMap.put(TMSchema.Part.SBP_SCROLLBAR, arrayOfState2);
/* 348 */       stateMap.put(TMSchema.Part.SBP_THUMBBTNVERT, arrayOfState2);
/* 349 */       stateMap.put(TMSchema.Part.SBP_THUMBBTNHORZ, arrayOfState2);
/* 350 */       stateMap.put(TMSchema.Part.SBP_GRIPPERVERT, arrayOfState2);
/* 351 */       stateMap.put(TMSchema.Part.SBP_GRIPPERHORZ, arrayOfState2);
/*     */ 
/* 353 */       stateMap.put(TMSchema.Part.SBP_ARROWBTN, new State[] { UPNORMAL, UPHOT, UPPRESSED, UPDISABLED, DOWNNORMAL, DOWNHOT, DOWNPRESSED, DOWNDISABLED, LEFTNORMAL, LEFTHOT, LEFTPRESSED, LEFTDISABLED, RIGHTNORMAL, RIGHTHOT, RIGHTPRESSED, RIGHTDISABLED, UPHOVER, DOWNHOVER, LEFTHOVER, RIGHTHOVER });
/*     */ 
/* 363 */       State[] arrayOfState3 = { NORMAL, HOT, PRESSED, DISABLED };
/* 364 */       stateMap.put(TMSchema.Part.SPNP_UP, arrayOfState3);
/* 365 */       stateMap.put(TMSchema.Part.SPNP_DOWN, arrayOfState3);
/*     */ 
/* 367 */       stateMap.put(TMSchema.Part.TVP_GLYPH, new State[] { CLOSED, OPENED });
/*     */ 
/* 369 */       State[] arrayOfState4 = { NORMAL, HOT, PUSHED, DISABLED, INACTIVENORMAL, INACTIVEHOT, INACTIVEPUSHED, INACTIVEDISABLED };
/*     */ 
/* 377 */       if (ThemeReader.getInt(TMSchema.Control.WINDOW.toString(), TMSchema.Part.WP_CLOSEBUTTON.getValue(), 1, TMSchema.Prop.IMAGECOUNT.getValue()) == 10)
/*     */       {
/* 380 */         arrayOfState4 = new State[] { NORMAL, HOT, PUSHED, DISABLED, null, INACTIVENORMAL, INACTIVEHOT, INACTIVEPUSHED, INACTIVEDISABLED, null };
/*     */       }
/*     */ 
/* 386 */       stateMap.put(TMSchema.Part.WP_MINBUTTON, arrayOfState4);
/* 387 */       stateMap.put(TMSchema.Part.WP_MAXBUTTON, arrayOfState4);
/* 388 */       stateMap.put(TMSchema.Part.WP_RESTOREBUTTON, arrayOfState4);
/* 389 */       stateMap.put(TMSchema.Part.WP_CLOSEBUTTON, arrayOfState4);
/*     */ 
/* 392 */       stateMap.put(TMSchema.Part.TKP_TRACK, new State[] { NORMAL });
/* 393 */       stateMap.put(TMSchema.Part.TKP_TRACKVERT, new State[] { NORMAL });
/*     */ 
/* 395 */       State[] arrayOfState5 = { NORMAL, HOT, PRESSED, FOCUSED, DISABLED };
/*     */ 
/* 397 */       stateMap.put(TMSchema.Part.TKP_THUMB, arrayOfState5);
/* 398 */       stateMap.put(TMSchema.Part.TKP_THUMBBOTTOM, arrayOfState5);
/* 399 */       stateMap.put(TMSchema.Part.TKP_THUMBTOP, arrayOfState5);
/* 400 */       stateMap.put(TMSchema.Part.TKP_THUMBVERT, arrayOfState5);
/* 401 */       stateMap.put(TMSchema.Part.TKP_THUMBRIGHT, arrayOfState5);
/*     */ 
/* 404 */       State[] arrayOfState6 = { NORMAL, HOT, SELECTED, DISABLED, FOCUSED };
/* 405 */       stateMap.put(TMSchema.Part.TABP_TABITEM, arrayOfState6);
/* 406 */       stateMap.put(TMSchema.Part.TABP_TABITEMLEFTEDGE, arrayOfState6);
/* 407 */       stateMap.put(TMSchema.Part.TABP_TABITEMRIGHTEDGE, arrayOfState6);
/*     */ 
/* 410 */       stateMap.put(TMSchema.Part.TP_BUTTON, new State[] { NORMAL, HOT, PRESSED, DISABLED, CHECKED, HOTCHECKED });
/*     */ 
/* 415 */       State[] arrayOfState7 = { ACTIVE, INACTIVE };
/* 416 */       stateMap.put(TMSchema.Part.WP_WINDOW, arrayOfState7);
/* 417 */       stateMap.put(TMSchema.Part.WP_FRAMELEFT, arrayOfState7);
/* 418 */       stateMap.put(TMSchema.Part.WP_FRAMERIGHT, arrayOfState7);
/* 419 */       stateMap.put(TMSchema.Part.WP_FRAMEBOTTOM, arrayOfState7);
/*     */ 
/* 421 */       State[] arrayOfState8 = { ACTIVE, INACTIVE, DISABLED };
/* 422 */       stateMap.put(TMSchema.Part.WP_CAPTION, arrayOfState8);
/* 423 */       stateMap.put(TMSchema.Part.WP_MINCAPTION, arrayOfState8);
/* 424 */       stateMap.put(TMSchema.Part.WP_MAXCAPTION, arrayOfState8);
/*     */ 
/* 426 */       stateMap.put(TMSchema.Part.MP_BARBACKGROUND, new State[] { ACTIVE, INACTIVE });
/*     */ 
/* 428 */       stateMap.put(TMSchema.Part.MP_BARITEM, new State[] { NORMAL, HOT, PUSHED, DISABLED, DISABLEDHOT, DISABLEDPUSHED });
/*     */ 
/* 431 */       stateMap.put(TMSchema.Part.MP_POPUPCHECK, new State[] { CHECKMARKNORMAL, CHECKMARKDISABLED, BULLETNORMAL, BULLETDISABLED });
/*     */ 
/* 434 */       stateMap.put(TMSchema.Part.MP_POPUPCHECKBACKGROUND, new State[] { DISABLEDPUSHED, NORMAL, BITMAP });
/*     */ 
/* 436 */       stateMap.put(TMSchema.Part.MP_POPUPITEM, new State[] { NORMAL, HOT, DISABLED, DISABLEDHOT });
/*     */ 
/* 438 */       stateMap.put(TMSchema.Part.MP_POPUPSUBMENU, new State[] { NORMAL, DISABLED });
/*     */     }
/*     */ 
/*     */     public static synchronized int getValue(TMSchema.Part paramPart, State paramState)
/*     */     {
/* 445 */       if (stateMap == null) {
/* 446 */         initStates();
/*     */       }
/*     */ 
/* 449 */       Enum[] arrayOfEnum = (Enum[])stateMap.get(paramPart);
/* 450 */       if (arrayOfEnum != null) {
/* 451 */         for (int i = 0; i < arrayOfEnum.length; i++) {
/* 452 */           if (paramState == arrayOfEnum[i]) {
/* 453 */             return i + 1;
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/* 458 */       if ((paramState == null) || (paramState == NORMAL)) {
/* 459 */         return 1;
/*     */       }
/*     */ 
/* 462 */       return 0;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static enum TypeEnum
/*     */   {
/* 529 */     BT_IMAGEFILE(TMSchema.Prop.BGTYPE, "imagefile", 0), 
/* 530 */     BT_BORDERFILL(TMSchema.Prop.BGTYPE, "borderfill", 1), 
/*     */ 
/* 532 */     TST_NONE(TMSchema.Prop.TEXTSHADOWTYPE, "none", 0), 
/* 533 */     TST_SINGLE(TMSchema.Prop.TEXTSHADOWTYPE, "single", 1), 
/* 534 */     TST_CONTINUOUS(TMSchema.Prop.TEXTSHADOWTYPE, "continuous", 2);
/*     */ 
/*     */     private final TMSchema.Prop prop;
/*     */     private final String enumName;
/*     */     private final int value;
/*     */ 
/* 538 */     private TypeEnum(TMSchema.Prop paramProp, String paramString, int paramInt) { this.prop = paramProp;
/* 539 */       this.enumName = paramString;
/* 540 */       this.value = paramInt;
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 548 */       return this.prop + "=" + this.enumName + "=" + this.value;
/*     */     }
/*     */ 
/*     */     String getName() {
/* 552 */       return this.enumName;
/*     */     }
/*     */ 
/*     */     static TypeEnum getTypeEnum(TMSchema.Prop paramProp, int paramInt)
/*     */     {
/* 557 */       for (TypeEnum localTypeEnum : values()) {
/* 558 */         if ((localTypeEnum.prop == paramProp) && (localTypeEnum.value == paramInt)) {
/* 559 */           return localTypeEnum;
/*     */         }
/*     */       }
/* 562 */       return null;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.swing.plaf.windows.TMSchema
 * JD-Core Version:    0.6.2
 */