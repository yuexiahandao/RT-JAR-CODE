/*     */ package sun.org.mozilla.javascript.internal.ast;
/*     */ 
/*     */ import java.util.SortedSet;
/*     */ import java.util.TreeSet;
/*     */ import sun.org.mozilla.javascript.internal.Node;
/*     */ 
/*     */ public class AstRoot extends ScriptNode
/*     */ {
/*     */   private SortedSet<Comment> comments;
/*     */   private boolean inStrictMode;
/*     */ 
/*     */   public AstRoot()
/*     */   {
/*  63 */     this.type = 136;
/*     */   }
/*     */ 
/*     */   public AstRoot(int paramInt)
/*     */   {
/*  70 */     super(paramInt);
/*     */ 
/*  63 */     this.type = 136;
/*     */   }
/*     */ 
/*     */   public SortedSet<Comment> getComments()
/*     */   {
/*  78 */     return this.comments;
/*     */   }
/*     */ 
/*     */   public void setComments(SortedSet<Comment> paramSortedSet)
/*     */   {
/*  87 */     if (paramSortedSet == null) {
/*  88 */       this.comments = null;
/*     */     } else {
/*  90 */       if (this.comments != null)
/*  91 */         this.comments.clear();
/*  92 */       for (Comment localComment : paramSortedSet)
/*  93 */         addComment(localComment);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void addComment(Comment paramComment)
/*     */   {
/* 103 */     assertNotNull(paramComment);
/* 104 */     if (this.comments == null) {
/* 105 */       this.comments = new TreeSet(new AstNode.PositionComparator());
/*     */     }
/* 107 */     this.comments.add(paramComment);
/* 108 */     paramComment.setParent(this);
/*     */   }
/*     */ 
/*     */   public void setInStrictMode(boolean paramBoolean) {
/* 112 */     this.inStrictMode = paramBoolean;
/*     */   }
/*     */ 
/*     */   public boolean isInStrictMode() {
/* 116 */     return this.inStrictMode;
/*     */   }
/*     */ 
/*     */   public void visitComments(NodeVisitor paramNodeVisitor)
/*     */   {
/* 127 */     if (this.comments != null)
/* 128 */       for (Comment localComment : this.comments)
/* 129 */         paramNodeVisitor.visit(localComment);
/*     */   }
/*     */ 
/*     */   public void visitAll(NodeVisitor paramNodeVisitor)
/*     */   {
/* 142 */     visit(paramNodeVisitor);
/* 143 */     visitComments(paramNodeVisitor);
/*     */   }
/*     */ 
/*     */   public String toSource(int paramInt)
/*     */   {
/* 148 */     StringBuilder localStringBuilder = new StringBuilder();
/* 149 */     for (Node localNode : this) {
/* 150 */       localStringBuilder.append(((AstNode)localNode).toSource(paramInt));
/*     */     }
/* 152 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   public String debugPrint()
/*     */   {
/* 160 */     AstNode.DebugPrintVisitor localDebugPrintVisitor = new AstNode.DebugPrintVisitor(new StringBuilder(1000));
/* 161 */     visitAll(localDebugPrintVisitor);
/* 162 */     return localDebugPrintVisitor.toString();
/*     */   }
/*     */ 
/*     */   public void checkParentLinks()
/*     */   {
/* 171 */     visit(new NodeVisitor() {
/*     */       public boolean visit(AstNode paramAnonymousAstNode) {
/* 173 */         int i = paramAnonymousAstNode.getType();
/* 174 */         if (i == 136)
/* 175 */           return true;
/* 176 */         if (paramAnonymousAstNode.getParent() == null) {
/* 177 */           throw new IllegalStateException("No parent for node: " + paramAnonymousAstNode + "\n" + paramAnonymousAstNode.toSource(0));
/*     */         }
/*     */ 
/* 180 */         return true;
/*     */       }
/*     */     });
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.ast.AstRoot
 * JD-Core Version:    0.6.2
 */