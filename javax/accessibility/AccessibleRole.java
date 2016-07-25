/*     */ package javax.accessibility;
/*     */ 
/*     */ public class AccessibleRole extends AccessibleBundle
/*     */ {
/*  58 */   public static final AccessibleRole ALERT = new AccessibleRole("alert");
/*     */ 
/*  64 */   public static final AccessibleRole COLUMN_HEADER = new AccessibleRole("columnheader");
/*     */ 
/*  74 */   public static final AccessibleRole CANVAS = new AccessibleRole("canvas");
/*     */ 
/*  81 */   public static final AccessibleRole COMBO_BOX = new AccessibleRole("combobox");
/*     */ 
/*  89 */   public static final AccessibleRole DESKTOP_ICON = new AccessibleRole("desktopicon");
/*     */ 
/* 105 */   public static final AccessibleRole HTML_CONTAINER = new AccessibleRole("htmlcontainer");
/*     */ 
/* 117 */   public static final AccessibleRole INTERNAL_FRAME = new AccessibleRole("internalframe");
/*     */ 
/* 126 */   public static final AccessibleRole DESKTOP_PANE = new AccessibleRole("desktoppane");
/*     */ 
/* 133 */   public static final AccessibleRole OPTION_PANE = new AccessibleRole("optionpane");
/*     */ 
/* 141 */   public static final AccessibleRole WINDOW = new AccessibleRole("window");
/*     */ 
/* 151 */   public static final AccessibleRole FRAME = new AccessibleRole("frame");
/*     */ 
/* 161 */   public static final AccessibleRole DIALOG = new AccessibleRole("dialog");
/*     */ 
/* 167 */   public static final AccessibleRole COLOR_CHOOSER = new AccessibleRole("colorchooser");
/*     */ 
/* 177 */   public static final AccessibleRole DIRECTORY_PANE = new AccessibleRole("directorypane");
/*     */ 
/* 187 */   public static final AccessibleRole FILE_CHOOSER = new AccessibleRole("filechooser");
/*     */ 
/* 195 */   public static final AccessibleRole FILLER = new AccessibleRole("filler");
/*     */ 
/* 201 */   public static final AccessibleRole HYPERLINK = new AccessibleRole("hyperlink");
/*     */ 
/* 207 */   public static final AccessibleRole ICON = new AccessibleRole("icon");
/*     */ 
/* 213 */   public static final AccessibleRole LABEL = new AccessibleRole("label");
/*     */ 
/* 222 */   public static final AccessibleRole ROOT_PANE = new AccessibleRole("rootpane");
/*     */ 
/* 231 */   public static final AccessibleRole GLASS_PANE = new AccessibleRole("glasspane");
/*     */ 
/* 242 */   public static final AccessibleRole LAYERED_PANE = new AccessibleRole("layeredpane");
/*     */ 
/* 252 */   public static final AccessibleRole LIST = new AccessibleRole("list");
/*     */ 
/* 261 */   public static final AccessibleRole LIST_ITEM = new AccessibleRole("listitem");
/*     */ 
/* 273 */   public static final AccessibleRole MENU_BAR = new AccessibleRole("menubar");
/*     */ 
/* 283 */   public static final AccessibleRole POPUP_MENU = new AccessibleRole("popupmenu");
/*     */ 
/* 300 */   public static final AccessibleRole MENU = new AccessibleRole("menu");
/*     */ 
/* 312 */   public static final AccessibleRole MENU_ITEM = new AccessibleRole("menuitem");
/*     */ 
/* 324 */   public static final AccessibleRole SEPARATOR = new AccessibleRole("separator");
/*     */ 
/* 334 */   public static final AccessibleRole PAGE_TAB_LIST = new AccessibleRole("pagetablist");
/*     */ 
/* 343 */   public static final AccessibleRole PAGE_TAB = new AccessibleRole("pagetab");
/*     */ 
/* 349 */   public static final AccessibleRole PANEL = new AccessibleRole("panel");
/*     */ 
/* 355 */   public static final AccessibleRole PROGRESS_BAR = new AccessibleRole("progressbar");
/*     */ 
/* 362 */   public static final AccessibleRole PASSWORD_TEXT = new AccessibleRole("passwordtext");
/*     */ 
/* 372 */   public static final AccessibleRole PUSH_BUTTON = new AccessibleRole("pushbutton");
/*     */ 
/* 382 */   public static final AccessibleRole TOGGLE_BUTTON = new AccessibleRole("togglebutton");
/*     */ 
/* 392 */   public static final AccessibleRole CHECK_BOX = new AccessibleRole("checkbox");
/*     */ 
/* 402 */   public static final AccessibleRole RADIO_BUTTON = new AccessibleRole("radiobutton");
/*     */ 
/* 408 */   public static final AccessibleRole ROW_HEADER = new AccessibleRole("rowheader");
/*     */ 
/* 417 */   public static final AccessibleRole SCROLL_PANE = new AccessibleRole("scrollpane");
/*     */ 
/* 425 */   public static final AccessibleRole SCROLL_BAR = new AccessibleRole("scrollbar");
/*     */ 
/* 434 */   public static final AccessibleRole VIEWPORT = new AccessibleRole("viewport");
/*     */ 
/* 441 */   public static final AccessibleRole SLIDER = new AccessibleRole("slider");
/*     */ 
/* 449 */   public static final AccessibleRole SPLIT_PANE = new AccessibleRole("splitpane");
/*     */ 
/* 456 */   public static final AccessibleRole TABLE = new AccessibleRole("table");
/*     */ 
/* 464 */   public static final AccessibleRole TEXT = new AccessibleRole("text");
/*     */ 
/* 472 */   public static final AccessibleRole TREE = new AccessibleRole("tree");
/*     */ 
/* 480 */   public static final AccessibleRole TOOL_BAR = new AccessibleRole("toolbar");
/*     */ 
/* 489 */   public static final AccessibleRole TOOL_TIP = new AccessibleRole("tooltip");
/*     */ 
/* 497 */   public static final AccessibleRole AWT_COMPONENT = new AccessibleRole("awtcomponent");
/*     */ 
/* 505 */   public static final AccessibleRole SWING_COMPONENT = new AccessibleRole("swingcomponent");
/*     */ 
/* 514 */   public static final AccessibleRole UNKNOWN = new AccessibleRole("unknown");
/*     */ 
/* 521 */   public static final AccessibleRole STATUS_BAR = new AccessibleRole("statusbar");
/*     */ 
/* 528 */   public static final AccessibleRole DATE_EDITOR = new AccessibleRole("dateeditor");
/*     */ 
/* 535 */   public static final AccessibleRole SPIN_BOX = new AccessibleRole("spinbox");
/*     */ 
/* 542 */   public static final AccessibleRole FONT_CHOOSER = new AccessibleRole("fontchooser");
/*     */ 
/* 549 */   public static final AccessibleRole GROUP_BOX = new AccessibleRole("groupbox");
/*     */ 
/* 557 */   public static final AccessibleRole HEADER = new AccessibleRole("header");
/*     */ 
/* 565 */   public static final AccessibleRole FOOTER = new AccessibleRole("footer");
/*     */ 
/* 573 */   public static final AccessibleRole PARAGRAPH = new AccessibleRole("paragraph");
/*     */ 
/* 581 */   public static final AccessibleRole RULER = new AccessibleRole("ruler");
/*     */ 
/* 591 */   public static final AccessibleRole EDITBAR = new AccessibleRole("editbar");
/*     */ 
/* 600 */   public static final AccessibleRole PROGRESS_MONITOR = new AccessibleRole("progressMonitor");
/*     */ 
/*     */   protected AccessibleRole(String paramString)
/*     */   {
/* 659 */     this.key = paramString;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.accessibility.AccessibleRole
 * JD-Core Version:    0.6.2
 */