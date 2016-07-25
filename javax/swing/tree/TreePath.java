/*     */ package javax.swing.tree;
/*     */ 
/*     */ import java.beans.ConstructorProperties;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public class TreePath
/*     */   implements Serializable
/*     */ {
/*     */   private TreePath parentPath;
/*     */   private Object lastPathComponent;
/*     */ 
/*     */   @ConstructorProperties({"path"})
/*     */   public TreePath(Object[] paramArrayOfObject)
/*     */   {
/* 101 */     if ((paramArrayOfObject == null) || (paramArrayOfObject.length == 0))
/* 102 */       throw new IllegalArgumentException("path in TreePath must be non null and not empty.");
/* 103 */     this.lastPathComponent = paramArrayOfObject[(paramArrayOfObject.length - 1)];
/* 104 */     if (this.lastPathComponent == null) {
/* 105 */       throw new IllegalArgumentException("Last path component must be non-null");
/*     */     }
/*     */ 
/* 108 */     if (paramArrayOfObject.length > 1)
/* 109 */       this.parentPath = new TreePath(paramArrayOfObject, paramArrayOfObject.length - 1);
/*     */   }
/*     */ 
/*     */   public TreePath(Object paramObject)
/*     */   {
/* 122 */     if (paramObject == null)
/* 123 */       throw new IllegalArgumentException("path in TreePath must be non null.");
/* 124 */     this.lastPathComponent = paramObject;
/* 125 */     this.parentPath = null;
/*     */   }
/*     */ 
/*     */   protected TreePath(TreePath paramTreePath, Object paramObject)
/*     */   {
/* 138 */     if (paramObject == null)
/* 139 */       throw new IllegalArgumentException("path in TreePath must be non null.");
/* 140 */     this.parentPath = paramTreePath;
/* 141 */     this.lastPathComponent = paramObject;
/*     */   }
/*     */ 
/*     */   protected TreePath(Object[] paramArrayOfObject, int paramInt)
/*     */   {
/* 162 */     this.lastPathComponent = paramArrayOfObject[(paramInt - 1)];
/* 163 */     if (this.lastPathComponent == null) {
/* 164 */       throw new IllegalArgumentException("Path elements must be non-null");
/*     */     }
/*     */ 
/* 167 */     if (paramInt > 1)
/* 168 */       this.parentPath = new TreePath(paramArrayOfObject, paramInt - 1);
/*     */   }
/*     */ 
/*     */   protected TreePath()
/*     */   {
/*     */   }
/*     */ 
/*     */   public Object[] getPath()
/*     */   {
/* 187 */     int i = getPathCount();
/* 188 */     Object[] arrayOfObject = new Object[i--];
/*     */ 
/* 190 */     for (TreePath localTreePath = this; localTreePath != null; localTreePath = localTreePath.getParentPath()) {
/* 191 */       arrayOfObject[(i--)] = localTreePath.getLastPathComponent();
/*     */     }
/* 193 */     return arrayOfObject;
/*     */   }
/*     */ 
/*     */   public Object getLastPathComponent()
/*     */   {
/* 202 */     return this.lastPathComponent;
/*     */   }
/*     */ 
/*     */   public int getPathCount()
/*     */   {
/* 211 */     int i = 0;
/* 212 */     for (TreePath localTreePath = this; localTreePath != null; localTreePath = localTreePath.getParentPath()) {
/* 213 */       i++;
/*     */     }
/* 215 */     return i;
/*     */   }
/*     */ 
/*     */   public Object getPathComponent(int paramInt)
/*     */   {
/* 227 */     int i = getPathCount();
/*     */ 
/* 229 */     if ((paramInt < 0) || (paramInt >= i)) {
/* 230 */       throw new IllegalArgumentException("Index " + paramInt + " is out of the specified range");
/*     */     }
/*     */ 
/* 233 */     TreePath localTreePath = this;
/*     */ 
/* 235 */     for (int j = i - 1; j != paramInt; j--) {
/* 236 */       localTreePath = localTreePath.getParentPath();
/*     */     }
/* 238 */     return localTreePath.getLastPathComponent();
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 250 */     if (paramObject == this)
/* 251 */       return true;
/* 252 */     if ((paramObject instanceof TreePath)) {
/* 253 */       TreePath localTreePath1 = (TreePath)paramObject;
/*     */ 
/* 255 */       if (getPathCount() != localTreePath1.getPathCount())
/* 256 */         return false;
/* 257 */       for (TreePath localTreePath2 = this; localTreePath2 != null; 
/* 258 */         localTreePath2 = localTreePath2.getParentPath()) {
/* 259 */         if (!localTreePath2.getLastPathComponent().equals(localTreePath1.getLastPathComponent()))
/*     */         {
/* 261 */           return false;
/*     */         }
/* 263 */         localTreePath1 = localTreePath1.getParentPath();
/*     */       }
/* 265 */       return true;
/*     */     }
/* 267 */     return false;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 277 */     return getLastPathComponent().hashCode();
/*     */   }
/*     */ 
/*     */   public boolean isDescendant(TreePath paramTreePath)
/*     */   {
/* 300 */     if (paramTreePath == this) {
/* 301 */       return true;
/*     */     }
/* 303 */     if (paramTreePath != null) {
/* 304 */       int i = getPathCount();
/* 305 */       int j = paramTreePath.getPathCount();
/*     */ 
/* 307 */       if (j < i)
/*     */       {
/* 309 */         return false;
/* 310 */       }while (j-- > i)
/* 311 */         paramTreePath = paramTreePath.getParentPath();
/* 312 */       return equals(paramTreePath);
/*     */     }
/* 314 */     return false;
/*     */   }
/*     */ 
/*     */   public TreePath pathByAddingChild(Object paramObject)
/*     */   {
/* 326 */     if (paramObject == null) {
/* 327 */       throw new NullPointerException("Null child not allowed");
/*     */     }
/* 329 */     return new TreePath(this, paramObject);
/*     */   }
/*     */ 
/*     */   public TreePath getParentPath()
/*     */   {
/* 339 */     return this.parentPath;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 349 */     StringBuffer localStringBuffer = new StringBuffer("[");
/*     */ 
/* 351 */     int i = 0; for (int j = getPathCount(); i < j; 
/* 352 */       i++) {
/* 353 */       if (i > 0)
/* 354 */         localStringBuffer.append(", ");
/* 355 */       localStringBuffer.append(getPathComponent(i));
/*     */     }
/* 357 */     localStringBuffer.append("]");
/* 358 */     return localStringBuffer.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.tree.TreePath
 * JD-Core Version:    0.6.2
 */