/*     */ package javax.accessibility;
/*     */ 
/*     */ public class AccessibleRelation extends AccessibleBundle
/*     */ {
/*  62 */   private Object[] target = new Object[0];
/*     */ 
/*  73 */   public static final String LABEL_FOR = new String("labelFor");
/*     */ 
/*  84 */   public static final String LABELED_BY = new String("labeledBy");
/*     */ 
/*  96 */   public static final String MEMBER_OF = new String("memberOf");
/*     */ 
/* 108 */   public static final String CONTROLLER_FOR = new String("controllerFor");
/*     */ 
/* 120 */   public static final String CONTROLLED_BY = new String("controlledBy");
/*     */   public static final String FLOWS_TO = "flowsTo";
/*     */   public static final String FLOWS_FROM = "flowsFrom";
/*     */   public static final String SUBWINDOW_OF = "subwindowOf";
/*     */   public static final String PARENT_WINDOW_OF = "parentWindowOf";
/*     */   public static final String EMBEDS = "embeds";
/*     */   public static final String EMBEDDED_BY = "embeddedBy";
/*     */   public static final String CHILD_NODE_OF = "childNodeOf";
/*     */   public static final String LABEL_FOR_PROPERTY = "labelForProperty";
/*     */   public static final String LABELED_BY_PROPERTY = "labeledByProperty";
/*     */   public static final String MEMBER_OF_PROPERTY = "memberOfProperty";
/*     */   public static final String CONTROLLER_FOR_PROPERTY = "controllerForProperty";
/*     */   public static final String CONTROLLED_BY_PROPERTY = "controlledByProperty";
/*     */   public static final String FLOWS_TO_PROPERTY = "flowsToProperty";
/*     */   public static final String FLOWS_FROM_PROPERTY = "flowsFromProperty";
/*     */   public static final String SUBWINDOW_OF_PROPERTY = "subwindowOfProperty";
/*     */   public static final String PARENT_WINDOW_OF_PROPERTY = "parentWindowOfProperty";
/*     */   public static final String EMBEDS_PROPERTY = "embedsProperty";
/*     */   public static final String EMBEDDED_BY_PROPERTY = "embeddedByProperty";
/*     */   public static final String CHILD_NODE_OF_PROPERTY = "childNodeOfProperty";
/*     */ 
/*     */   public AccessibleRelation(String paramString)
/*     */   {
/* 285 */     this.key = paramString;
/* 286 */     this.target = null;
/*     */   }
/*     */ 
/*     */   public AccessibleRelation(String paramString, Object paramObject)
/*     */   {
/* 300 */     this.key = paramString;
/* 301 */     this.target = new Object[1];
/* 302 */     this.target[0] = paramObject;
/*     */   }
/*     */ 
/*     */   public AccessibleRelation(String paramString, Object[] paramArrayOfObject)
/*     */   {
/* 316 */     this.key = paramString;
/* 317 */     this.target = paramArrayOfObject;
/*     */   }
/*     */ 
/*     */   public String getKey()
/*     */   {
/* 332 */     return this.key;
/*     */   }
/*     */ 
/*     */   public Object[] getTarget()
/*     */   {
/* 341 */     if (this.target == null) {
/* 342 */       this.target = new Object[0];
/*     */     }
/* 344 */     Object[] arrayOfObject = new Object[this.target.length];
/* 345 */     for (int i = 0; i < this.target.length; i++) {
/* 346 */       arrayOfObject[i] = this.target[i];
/*     */     }
/* 348 */     return arrayOfObject;
/*     */   }
/*     */ 
/*     */   public void setTarget(Object paramObject)
/*     */   {
/* 357 */     this.target = new Object[1];
/* 358 */     this.target[0] = paramObject;
/*     */   }
/*     */ 
/*     */   public void setTarget(Object[] paramArrayOfObject)
/*     */   {
/* 367 */     this.target = paramArrayOfObject;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.accessibility.AccessibleRelation
 * JD-Core Version:    0.6.2
 */