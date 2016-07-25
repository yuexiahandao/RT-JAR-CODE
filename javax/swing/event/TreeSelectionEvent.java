/*     */ package javax.swing.event;
/*     */ 
/*     */ import java.util.EventObject;
/*     */ import javax.swing.tree.TreePath;
/*     */ 
/*     */ public class TreeSelectionEvent extends EventObject
/*     */ {
/*     */   protected TreePath[] paths;
/*     */   protected boolean[] areNew;
/*     */   protected TreePath oldLeadSelectionPath;
/*     */   protected TreePath newLeadSelectionPath;
/*     */ 
/*     */   public TreeSelectionEvent(Object paramObject, TreePath[] paramArrayOfTreePath, boolean[] paramArrayOfBoolean, TreePath paramTreePath1, TreePath paramTreePath2)
/*     */   {
/*  75 */     super(paramObject);
/*  76 */     this.paths = paramArrayOfTreePath;
/*  77 */     this.areNew = paramArrayOfBoolean;
/*  78 */     this.oldLeadSelectionPath = paramTreePath1;
/*  79 */     this.newLeadSelectionPath = paramTreePath2;
/*     */   }
/*     */ 
/*     */   public TreeSelectionEvent(Object paramObject, TreePath paramTreePath1, boolean paramBoolean, TreePath paramTreePath2, TreePath paramTreePath3)
/*     */   {
/*  96 */     super(paramObject);
/*  97 */     this.paths = new TreePath[1];
/*  98 */     this.paths[0] = paramTreePath1;
/*  99 */     this.areNew = new boolean[1];
/* 100 */     this.areNew[0] = paramBoolean;
/* 101 */     this.oldLeadSelectionPath = paramTreePath2;
/* 102 */     this.newLeadSelectionPath = paramTreePath3;
/*     */   }
/*     */ 
/*     */   public TreePath[] getPaths()
/*     */   {
/* 114 */     int i = this.paths.length;
/* 115 */     TreePath[] arrayOfTreePath = new TreePath[i];
/* 116 */     System.arraycopy(this.paths, 0, arrayOfTreePath, 0, i);
/* 117 */     return arrayOfTreePath;
/*     */   }
/*     */ 
/*     */   public TreePath getPath()
/*     */   {
/* 125 */     return this.paths[0];
/*     */   }
/*     */ 
/*     */   public boolean isAddedPath()
/*     */   {
/* 139 */     return this.areNew[0];
/*     */   }
/*     */ 
/*     */   public boolean isAddedPath(TreePath paramTreePath)
/*     */   {
/* 159 */     for (int i = this.paths.length - 1; i >= 0; i--)
/* 160 */       if (this.paths[i].equals(paramTreePath))
/* 161 */         return this.areNew[i];
/* 162 */     throw new IllegalArgumentException("path is not a path identified by the TreeSelectionEvent");
/*     */   }
/*     */ 
/*     */   public boolean isAddedPath(int paramInt)
/*     */   {
/* 181 */     if ((this.paths == null) || (paramInt < 0) || (paramInt >= this.paths.length)) {
/* 182 */       throw new IllegalArgumentException("index is beyond range of added paths identified by TreeSelectionEvent");
/*     */     }
/* 184 */     return this.areNew[paramInt];
/*     */   }
/*     */ 
/*     */   public TreePath getOldLeadSelectionPath()
/*     */   {
/* 191 */     return this.oldLeadSelectionPath;
/*     */   }
/*     */ 
/*     */   public TreePath getNewLeadSelectionPath()
/*     */   {
/* 198 */     return this.newLeadSelectionPath;
/*     */   }
/*     */ 
/*     */   public Object cloneWithSource(Object paramObject)
/*     */   {
/* 206 */     return new TreeSelectionEvent(paramObject, this.paths, this.areNew, this.oldLeadSelectionPath, this.newLeadSelectionPath);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.event.TreeSelectionEvent
 * JD-Core Version:    0.6.2
 */