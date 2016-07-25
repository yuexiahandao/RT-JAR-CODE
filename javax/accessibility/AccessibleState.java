/*     */ package javax.accessibility;
/*     */ 
/*     */ public class AccessibleState extends AccessibleBundle
/*     */ {
/*  65 */   public static final AccessibleState ACTIVE = new AccessibleState("active");
/*     */ 
/*  75 */   public static final AccessibleState PRESSED = new AccessibleState("pressed");
/*     */ 
/*  84 */   public static final AccessibleState ARMED = new AccessibleState("armed");
/*     */ 
/*  95 */   public static final AccessibleState BUSY = new AccessibleState("busy");
/*     */ 
/* 105 */   public static final AccessibleState CHECKED = new AccessibleState("checked");
/*     */ 
/* 115 */   public static final AccessibleState EDITABLE = new AccessibleState("editable");
/*     */ 
/* 126 */   public static final AccessibleState EXPANDABLE = new AccessibleState("expandable");
/*     */ 
/* 137 */   public static final AccessibleState COLLAPSED = new AccessibleState("collapsed");
/*     */ 
/* 148 */   public static final AccessibleState EXPANDED = new AccessibleState("expanded");
/*     */ 
/* 157 */   public static final AccessibleState ENABLED = new AccessibleState("enabled");
/*     */ 
/* 166 */   public static final AccessibleState FOCUSABLE = new AccessibleState("focusable");
/*     */ 
/* 173 */   public static final AccessibleState FOCUSED = new AccessibleState("focused");
/*     */ 
/* 183 */   public static final AccessibleState ICONIFIED = new AccessibleState("iconified");
/*     */ 
/* 192 */   public static final AccessibleState MODAL = new AccessibleState("modal");
/*     */ 
/* 205 */   public static final AccessibleState OPAQUE = new AccessibleState("opaque");
/*     */ 
/* 215 */   public static final AccessibleState RESIZABLE = new AccessibleState("resizable");
/*     */ 
/* 226 */   public static final AccessibleState MULTISELECTABLE = new AccessibleState("multiselectable");
/*     */ 
/* 238 */   public static final AccessibleState SELECTABLE = new AccessibleState("selectable");
/*     */ 
/* 250 */   public static final AccessibleState SELECTED = new AccessibleState("selected");
/*     */ 
/* 260 */   public static final AccessibleState SHOWING = new AccessibleState("showing");
/*     */ 
/* 270 */   public static final AccessibleState VISIBLE = new AccessibleState("visible");
/*     */ 
/* 282 */   public static final AccessibleState VERTICAL = new AccessibleState("vertical");
/*     */ 
/* 294 */   public static final AccessibleState HORIZONTAL = new AccessibleState("horizontal");
/*     */ 
/* 300 */   public static final AccessibleState SINGLE_LINE = new AccessibleState("singleline");
/*     */ 
/* 306 */   public static final AccessibleState MULTI_LINE = new AccessibleState("multiline");
/*     */ 
/* 322 */   public static final AccessibleState TRANSIENT = new AccessibleState("transient");
/*     */ 
/* 334 */   public static final AccessibleState MANAGES_DESCENDANTS = new AccessibleState("managesDescendants");
/*     */ 
/* 345 */   public static final AccessibleState INDETERMINATE = new AccessibleState("indeterminate");
/*     */ 
/* 356 */   public static final AccessibleState TRUNCATED = new AccessibleState("truncated");
/*     */ 
/*     */   protected AccessibleState(String paramString)
/*     */   {
/* 373 */     this.key = paramString;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.accessibility.AccessibleState
 * JD-Core Version:    0.6.2
 */