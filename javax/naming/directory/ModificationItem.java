/*     */ package javax.naming.directory;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public class ModificationItem
/*     */   implements Serializable
/*     */ {
/*     */   private int mod_op;
/*     */   private Attribute attr;
/*     */   private static final long serialVersionUID = 7573258562534746850L;
/*     */ 
/*     */   public ModificationItem(int paramInt, Attribute paramAttribute)
/*     */   {
/*  72 */     switch (paramInt) {
/*     */     case 1:
/*     */     case 2:
/*     */     case 3:
/*  76 */       if (paramAttribute == null) {
/*  77 */         throw new IllegalArgumentException("Must specify non-null attribute for modification");
/*     */       }
/*  79 */       this.mod_op = paramInt;
/*  80 */       this.attr = paramAttribute;
/*  81 */       break;
/*     */     default:
/*  84 */       throw new IllegalArgumentException("Invalid modification code " + paramInt);
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getModificationOp()
/*     */   {
/*  96 */     return this.mod_op;
/*     */   }
/*     */ 
/*     */   public Attribute getAttribute()
/*     */   {
/* 104 */     return this.attr;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 116 */     switch (this.mod_op) {
/*     */     case 1:
/* 118 */       return "Add attribute: " + this.attr.toString();
/*     */     case 2:
/* 121 */       return "Replace attribute: " + this.attr.toString();
/*     */     case 3:
/* 124 */       return "Remove attribute: " + this.attr.toString();
/*     */     }
/* 126 */     return "";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.naming.directory.ModificationItem
 * JD-Core Version:    0.6.2
 */