/*     */ package sun.awt.shell;
/*     */ 
/*     */ import java.util.Comparator;
/*     */ import javax.swing.SortOrder;
/*     */ 
/*     */ public class ShellFolderColumnInfo
/*     */ {
/*     */   private String title;
/*     */   private Integer width;
/*     */   private boolean visible;
/*     */   private Integer alignment;
/*     */   private SortOrder sortOrder;
/*     */   private Comparator comparator;
/*     */   private boolean compareByColumn;
/*     */ 
/*     */   public ShellFolderColumnInfo(String paramString, Integer paramInteger1, Integer paramInteger2, boolean paramBoolean1, SortOrder paramSortOrder, Comparator paramComparator, boolean paramBoolean2)
/*     */   {
/*  54 */     this.title = paramString;
/*  55 */     this.width = paramInteger1;
/*  56 */     this.alignment = paramInteger2;
/*  57 */     this.visible = paramBoolean1;
/*  58 */     this.sortOrder = paramSortOrder;
/*  59 */     this.comparator = paramComparator;
/*  60 */     this.compareByColumn = paramBoolean2;
/*     */   }
/*     */ 
/*     */   public ShellFolderColumnInfo(String paramString, Integer paramInteger1, Integer paramInteger2, boolean paramBoolean, SortOrder paramSortOrder, Comparator paramComparator)
/*     */   {
/*  66 */     this(paramString, paramInteger1, paramInteger2, paramBoolean, paramSortOrder, paramComparator, false);
/*     */   }
/*     */ 
/*     */   public ShellFolderColumnInfo(String paramString, int paramInt1, int paramInt2, boolean paramBoolean)
/*     */   {
/*  75 */     this(paramString, Integer.valueOf(paramInt1), Integer.valueOf(paramInt2), paramBoolean, null, null);
/*     */   }
/*     */ 
/*     */   public String getTitle() {
/*  79 */     return this.title;
/*     */   }
/*     */ 
/*     */   public void setTitle(String paramString) {
/*  83 */     this.title = paramString;
/*     */   }
/*     */ 
/*     */   public Integer getWidth() {
/*  87 */     return this.width;
/*     */   }
/*     */ 
/*     */   public void setWidth(Integer paramInteger) {
/*  91 */     this.width = paramInteger;
/*     */   }
/*     */ 
/*     */   public Integer getAlignment() {
/*  95 */     return this.alignment;
/*     */   }
/*     */ 
/*     */   public void setAlignment(Integer paramInteger) {
/*  99 */     this.alignment = paramInteger;
/*     */   }
/*     */ 
/*     */   public boolean isVisible() {
/* 103 */     return this.visible;
/*     */   }
/*     */ 
/*     */   public void setVisible(boolean paramBoolean) {
/* 107 */     this.visible = paramBoolean;
/*     */   }
/*     */ 
/*     */   public SortOrder getSortOrder() {
/* 111 */     return this.sortOrder;
/*     */   }
/*     */ 
/*     */   public void setSortOrder(SortOrder paramSortOrder) {
/* 115 */     this.sortOrder = paramSortOrder;
/*     */   }
/*     */ 
/*     */   public Comparator getComparator() {
/* 119 */     return this.comparator;
/*     */   }
/*     */ 
/*     */   public void setComparator(Comparator paramComparator) {
/* 123 */     this.comparator = paramComparator;
/*     */   }
/*     */ 
/*     */   public boolean isCompareByColumn() {
/* 127 */     return this.compareByColumn;
/*     */   }
/*     */ 
/*     */   public void setCompareByColumn(boolean paramBoolean) {
/* 131 */     this.compareByColumn = paramBoolean;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.shell.ShellFolderColumnInfo
 * JD-Core Version:    0.6.2
 */