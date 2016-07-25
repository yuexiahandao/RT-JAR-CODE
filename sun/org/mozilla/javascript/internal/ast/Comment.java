/*     */ package sun.org.mozilla.javascript.internal.ast;
/*     */ 
/*     */ import sun.org.mozilla.javascript.internal.Token.CommentType;
/*     */ 
/*     */ public class Comment extends AstNode
/*     */ {
/*     */   private String value;
/*     */   private Token.CommentType commentType;
/*     */ 
/*     */   public Comment(int paramInt1, int paramInt2, Token.CommentType paramCommentType, String paramString)
/*     */   {
/*  90 */     super(paramInt1, paramInt2);
/*     */ 
/*  79 */     this.type = 161;
/*     */ 
/*  91 */     this.commentType = paramCommentType;
/*  92 */     this.value = paramString;
/*     */   }
/*     */ 
/*     */   public Token.CommentType getCommentType()
/*     */   {
/*  99 */     return this.commentType;
/*     */   }
/*     */ 
/*     */   public void setCommentType(Token.CommentType paramCommentType)
/*     */   {
/* 108 */     this.commentType = paramCommentType;
/*     */   }
/*     */ 
/*     */   public String getValue()
/*     */   {
/* 115 */     return this.value;
/*     */   }
/*     */ 
/*     */   public String toSource(int paramInt)
/*     */   {
/* 120 */     StringBuilder localStringBuilder = new StringBuilder(getLength() + 10);
/* 121 */     localStringBuilder.append(makeIndent(paramInt));
/* 122 */     localStringBuilder.append(this.value);
/* 123 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   public void visit(NodeVisitor paramNodeVisitor)
/*     */   {
/* 132 */     paramNodeVisitor.visit(this);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.ast.Comment
 * JD-Core Version:    0.6.2
 */