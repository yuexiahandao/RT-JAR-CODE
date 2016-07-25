/*     */ package javax.swing.event;
/*     */ 
/*     */ import java.util.EventObject;
/*     */ import javax.swing.tree.TreePath;
/*     */ 
/*     */ public class TreeModelEvent extends EventObject
/*     */ {
/*     */   protected TreePath path;
/*     */   protected int[] childIndices;
/*     */   protected Object[] children;
/*     */ 
/*     */   public TreeModelEvent(Object paramObject, Object[] paramArrayOfObject1, int[] paramArrayOfInt, Object[] paramArrayOfObject2)
/*     */   {
/* 131 */     this(paramObject, new TreePath(paramArrayOfObject1), paramArrayOfInt, paramArrayOfObject2);
/*     */   }
/*     */ 
/*     */   public TreeModelEvent(Object paramObject, TreePath paramTreePath, int[] paramArrayOfInt, Object[] paramArrayOfObject)
/*     */   {
/* 156 */     super(paramObject);
/* 157 */     this.path = paramTreePath;
/* 158 */     this.childIndices = paramArrayOfInt;
/* 159 */     this.children = paramArrayOfObject;
/*     */   }
/*     */ 
/*     */   public TreeModelEvent(Object paramObject, Object[] paramArrayOfObject)
/*     */   {
/* 186 */     this(paramObject, new TreePath(paramArrayOfObject));
/*     */   }
/*     */ 
/*     */   public TreeModelEvent(Object paramObject, TreePath paramTreePath)
/*     */   {
/* 208 */     super(paramObject);
/* 209 */     this.path = paramTreePath;
/* 210 */     this.childIndices = new int[0];
/*     */   }
/*     */ 
/*     */   public TreePath getTreePath()
/*     */   {
/* 228 */     return this.path;
/*     */   }
/*     */ 
/*     */   public Object[] getPath()
/*     */   {
/* 239 */     if (this.path != null)
/* 240 */       return this.path.getPath();
/* 241 */     return null;
/*     */   }
/*     */ 
/*     */   public Object[] getChildren()
/*     */   {
/* 256 */     if (this.children != null) {
/* 257 */       int i = this.children.length;
/* 258 */       Object[] arrayOfObject = new Object[i];
/*     */ 
/* 260 */       System.arraycopy(this.children, 0, arrayOfObject, 0, i);
/* 261 */       return arrayOfObject;
/*     */     }
/* 263 */     return null;
/*     */   }
/*     */ 
/*     */   public int[] getChildIndices()
/*     */   {
/* 277 */     if (this.childIndices != null) {
/* 278 */       int i = this.childIndices.length;
/* 279 */       int[] arrayOfInt = new int[i];
/*     */ 
/* 281 */       System.arraycopy(this.childIndices, 0, arrayOfInt, 0, i);
/* 282 */       return arrayOfInt;
/*     */     }
/* 284 */     return null;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 294 */     StringBuffer localStringBuffer = new StringBuffer();
/*     */ 
/* 296 */     localStringBuffer.append(getClass().getName() + " " + Integer.toString(hashCode()));
/*     */ 
/* 298 */     if (this.path != null)
/* 299 */       localStringBuffer.append(" path " + this.path);
/*     */     int i;
/* 300 */     if (this.childIndices != null) {
/* 301 */       localStringBuffer.append(" indices [ ");
/* 302 */       for (i = 0; i < this.childIndices.length; i++)
/* 303 */         localStringBuffer.append(Integer.toString(this.childIndices[i]) + " ");
/* 304 */       localStringBuffer.append("]");
/*     */     }
/* 306 */     if (this.children != null) {
/* 307 */       localStringBuffer.append(" children [ ");
/* 308 */       for (i = 0; i < this.children.length; i++)
/* 309 */         localStringBuffer.append(this.children[i] + " ");
/* 310 */       localStringBuffer.append("]");
/*     */     }
/* 312 */     return localStringBuffer.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.event.TreeModelEvent
 * JD-Core Version:    0.6.2
 */