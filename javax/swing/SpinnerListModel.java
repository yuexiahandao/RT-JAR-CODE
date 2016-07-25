/*     */ package javax.swing;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ 
/*     */ public class SpinnerListModel extends AbstractSpinnerModel
/*     */   implements Serializable
/*     */ {
/*     */   private List list;
/*     */   private int index;
/*     */ 
/*     */   public SpinnerListModel(List<?> paramList)
/*     */   {
/*  78 */     if ((paramList == null) || (paramList.size() == 0)) {
/*  79 */       throw new IllegalArgumentException("SpinnerListModel(List) expects non-null non-empty List");
/*     */     }
/*  81 */     this.list = paramList;
/*  82 */     this.index = 0;
/*     */   }
/*     */ 
/*     */   public SpinnerListModel(Object[] paramArrayOfObject)
/*     */   {
/*  98 */     if ((paramArrayOfObject == null) || (paramArrayOfObject.length == 0)) {
/*  99 */       throw new IllegalArgumentException("SpinnerListModel(Object[]) expects non-null non-empty Object[]");
/*     */     }
/* 101 */     this.list = Arrays.asList(paramArrayOfObject);
/* 102 */     this.index = 0;
/*     */   }
/*     */ 
/*     */   public SpinnerListModel()
/*     */   {
/* 112 */     this(new Object[] { "empty" });
/*     */   }
/*     */ 
/*     */   public List<?> getList()
/*     */   {
/* 123 */     return this.list;
/*     */   }
/*     */ 
/*     */   public void setList(List<?> paramList)
/*     */   {
/* 141 */     if ((paramList == null) || (paramList.size() == 0)) {
/* 142 */       throw new IllegalArgumentException("invalid list");
/*     */     }
/* 144 */     if (!paramList.equals(this.list)) {
/* 145 */       this.list = paramList;
/* 146 */       this.index = 0;
/* 147 */       fireStateChanged();
/*     */     }
/*     */   }
/*     */ 
/*     */   public Object getValue()
/*     */   {
/* 160 */     return this.list.get(this.index);
/*     */   }
/*     */ 
/*     */   public void setValue(Object paramObject)
/*     */   {
/* 183 */     int i = this.list.indexOf(paramObject);
/* 184 */     if (i == -1) {
/* 185 */       throw new IllegalArgumentException("invalid sequence element");
/*     */     }
/* 187 */     if (i != this.index) {
/* 188 */       this.index = i;
/* 189 */       fireStateChanged();
/*     */     }
/*     */   }
/*     */ 
/*     */   public Object getNextValue()
/*     */   {
/* 204 */     return this.index >= this.list.size() - 1 ? null : this.list.get(this.index + 1);
/*     */   }
/*     */ 
/*     */   public Object getPreviousValue()
/*     */   {
/* 218 */     return this.index <= 0 ? null : this.list.get(this.index - 1);
/*     */   }
/*     */ 
/*     */   Object findNextMatch(String paramString)
/*     */   {
/* 229 */     int i = this.list.size();
/*     */ 
/* 231 */     if (i == 0) {
/* 232 */       return null;
/*     */     }
/* 234 */     int j = this.index;
/*     */     do
/*     */     {
/* 237 */       Object localObject = this.list.get(j);
/* 238 */       String str = localObject.toString();
/*     */ 
/* 240 */       if ((str != null) && (str.startsWith(paramString))) {
/* 241 */         return localObject;
/*     */       }
/* 243 */       j = (j + 1) % i;
/* 244 */     }while (j != this.index);
/* 245 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.SpinnerListModel
 * JD-Core Version:    0.6.2
 */