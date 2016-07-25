/*     */ package javax.accessibility;
/*     */ 
/*     */ import java.awt.IllegalComponentStateException;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.beans.PropertyChangeSupport;
/*     */ import java.util.Locale;
/*     */ import sun.awt.AWTAccessor;
/*     */ import sun.awt.AWTAccessor.AccessibleContextAccessor;
/*     */ import sun.awt.AppContext;
/*     */ 
/*     */ public abstract class AccessibleContext
/*     */ {
/*     */   private volatile AppContext targetAppContext;
/*     */   public static final String ACCESSIBLE_NAME_PROPERTY = "AccessibleName";
/*     */   public static final String ACCESSIBLE_DESCRIPTION_PROPERTY = "AccessibleDescription";
/*     */   public static final String ACCESSIBLE_STATE_PROPERTY = "AccessibleState";
/*     */   public static final String ACCESSIBLE_VALUE_PROPERTY = "AccessibleValue";
/*     */   public static final String ACCESSIBLE_SELECTION_PROPERTY = "AccessibleSelection";
/*     */   public static final String ACCESSIBLE_CARET_PROPERTY = "AccessibleCaret";
/*     */   public static final String ACCESSIBLE_VISIBLE_DATA_PROPERTY = "AccessibleVisibleData";
/*     */   public static final String ACCESSIBLE_CHILD_PROPERTY = "AccessibleChild";
/*     */   public static final String ACCESSIBLE_ACTIVE_DESCENDANT_PROPERTY = "AccessibleActiveDescendant";
/*     */   public static final String ACCESSIBLE_TABLE_CAPTION_CHANGED = "accessibleTableCaptionChanged";
/*     */   public static final String ACCESSIBLE_TABLE_SUMMARY_CHANGED = "accessibleTableSummaryChanged";
/*     */   public static final String ACCESSIBLE_TABLE_MODEL_CHANGED = "accessibleTableModelChanged";
/*     */   public static final String ACCESSIBLE_TABLE_ROW_HEADER_CHANGED = "accessibleTableRowHeaderChanged";
/*     */   public static final String ACCESSIBLE_TABLE_ROW_DESCRIPTION_CHANGED = "accessibleTableRowDescriptionChanged";
/*     */   public static final String ACCESSIBLE_TABLE_COLUMN_HEADER_CHANGED = "accessibleTableColumnHeaderChanged";
/*     */   public static final String ACCESSIBLE_TABLE_COLUMN_DESCRIPTION_CHANGED = "accessibleTableColumnDescriptionChanged";
/*     */   public static final String ACCESSIBLE_ACTION_PROPERTY = "accessibleActionProperty";
/*     */   public static final String ACCESSIBLE_HYPERTEXT_OFFSET = "AccessibleHypertextOffset";
/*     */   public static final String ACCESSIBLE_TEXT_PROPERTY = "AccessibleText";
/*     */   public static final String ACCESSIBLE_INVALIDATE_CHILDREN = "accessibleInvalidateChildren";
/*     */   public static final String ACCESSIBLE_TEXT_ATTRIBUTES_CHANGED = "accessibleTextAttributesChanged";
/*     */   public static final String ACCESSIBLE_COMPONENT_BOUNDS_CHANGED = "accessibleComponentBoundsChanged";
/* 386 */   protected Accessible accessibleParent = null;
/*     */ 
/* 394 */   protected String accessibleName = null;
/*     */ 
/* 402 */   protected String accessibleDescription = null;
/*     */ 
/* 411 */   private PropertyChangeSupport accessibleChangeSupport = null;
/*     */ 
/* 417 */   private AccessibleRelationSet relationSet = new AccessibleRelationSet();
/*     */   private Object nativeAXResource;
/*     */ 
/*     */   public String getAccessibleName()
/*     */   {
/* 438 */     return this.accessibleName;
/*     */   }
/*     */ 
/*     */   public void setAccessibleName(String paramString)
/*     */   {
/* 456 */     String str = this.accessibleName;
/* 457 */     this.accessibleName = paramString;
/* 458 */     firePropertyChange("AccessibleName", str, this.accessibleName);
/*     */   }
/*     */ 
/*     */   public String getAccessibleDescription()
/*     */   {
/* 474 */     return this.accessibleDescription;
/*     */   }
/*     */ 
/*     */   public void setAccessibleDescription(String paramString)
/*     */   {
/* 492 */     String str = this.accessibleDescription;
/* 493 */     this.accessibleDescription = paramString;
/* 494 */     firePropertyChange("AccessibleDescription", str, this.accessibleDescription);
/*     */   }
/*     */ 
/*     */   public abstract AccessibleRole getAccessibleRole();
/*     */ 
/*     */   public abstract AccessibleStateSet getAccessibleStateSet();
/*     */ 
/*     */   public Accessible getAccessibleParent()
/*     */   {
/* 539 */     return this.accessibleParent;
/*     */   }
/*     */ 
/*     */   public void setAccessibleParent(Accessible paramAccessible)
/*     */   {
/* 551 */     this.accessibleParent = paramAccessible;
/*     */   }
/*     */ 
/*     */   public abstract int getAccessibleIndexInParent();
/*     */ 
/*     */   public abstract int getAccessibleChildrenCount();
/*     */ 
/*     */   public abstract Accessible getAccessibleChild(int paramInt);
/*     */ 
/*     */   public abstract Locale getLocale()
/*     */     throws IllegalComponentStateException;
/*     */ 
/*     */   public void addPropertyChangeListener(PropertyChangeListener paramPropertyChangeListener)
/*     */   {
/* 615 */     if (this.accessibleChangeSupport == null) {
/* 616 */       this.accessibleChangeSupport = new PropertyChangeSupport(this);
/*     */     }
/* 618 */     this.accessibleChangeSupport.addPropertyChangeListener(paramPropertyChangeListener);
/*     */   }
/*     */ 
/*     */   public void removePropertyChangeListener(PropertyChangeListener paramPropertyChangeListener)
/*     */   {
/* 629 */     if (this.accessibleChangeSupport != null)
/* 630 */       this.accessibleChangeSupport.removePropertyChangeListener(paramPropertyChangeListener);
/*     */   }
/*     */ 
/*     */   public AccessibleAction getAccessibleAction()
/*     */   {
/* 642 */     return null;
/*     */   }
/*     */ 
/*     */   public AccessibleComponent getAccessibleComponent()
/*     */   {
/* 653 */     return null;
/*     */   }
/*     */ 
/*     */   public AccessibleSelection getAccessibleSelection()
/*     */   {
/* 664 */     return null;
/*     */   }
/*     */ 
/*     */   public AccessibleText getAccessibleText()
/*     */   {
/* 675 */     return null;
/*     */   }
/*     */ 
/*     */   public AccessibleEditableText getAccessibleEditableText()
/*     */   {
/* 687 */     return null;
/*     */   }
/*     */ 
/*     */   public AccessibleValue getAccessibleValue()
/*     */   {
/* 699 */     return null;
/*     */   }
/*     */ 
/*     */   public AccessibleIcon[] getAccessibleIcon()
/*     */   {
/* 712 */     return null;
/*     */   }
/*     */ 
/*     */   public AccessibleRelationSet getAccessibleRelationSet()
/*     */   {
/* 724 */     return this.relationSet;
/*     */   }
/*     */ 
/*     */   public AccessibleTable getAccessibleTable()
/*     */   {
/* 736 */     return null;
/*     */   }
/*     */ 
/*     */   public void firePropertyChange(String paramString, Object paramObject1, Object paramObject2)
/*     */   {
/* 763 */     if (this.accessibleChangeSupport != null)
/* 764 */       if ((paramObject2 instanceof PropertyChangeEvent)) {
/* 765 */         PropertyChangeEvent localPropertyChangeEvent = (PropertyChangeEvent)paramObject2;
/* 766 */         this.accessibleChangeSupport.firePropertyChange(localPropertyChangeEvent);
/*     */       } else {
/* 768 */         this.accessibleChangeSupport.firePropertyChange(paramString, paramObject1, paramObject2);
/*     */       }
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  92 */     AWTAccessor.setAccessibleContextAccessor(new AWTAccessor.AccessibleContextAccessor()
/*     */     {
/*     */       public void setAppContext(AccessibleContext paramAnonymousAccessibleContext, AppContext paramAnonymousAppContext) {
/*  95 */         paramAnonymousAccessibleContext.targetAppContext = paramAnonymousAppContext;
/*     */       }
/*     */ 
/*     */       public AppContext getAppContext(AccessibleContext paramAnonymousAccessibleContext)
/*     */       {
/* 100 */         return paramAnonymousAccessibleContext.targetAppContext;
/*     */       }
/*     */     });
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.accessibility.AccessibleContext
 * JD-Core Version:    0.6.2
 */