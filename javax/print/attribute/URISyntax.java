/*     */ package javax.print.attribute;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.net.URI;
/*     */ 
/*     */ public abstract class URISyntax
/*     */   implements Serializable, Cloneable
/*     */ {
/*     */   private static final long serialVersionUID = -7842661210486401678L;
/*     */   private URI uri;
/*     */ 
/*     */   protected URISyntax(URI paramURI)
/*     */   {
/*  59 */     this.uri = verify(paramURI);
/*     */   }
/*     */ 
/*     */   private static URI verify(URI paramURI) {
/*  63 */     if (paramURI == null) {
/*  64 */       throw new NullPointerException(" uri is null");
/*     */     }
/*  66 */     return paramURI;
/*     */   }
/*     */ 
/*     */   public URI getURI()
/*     */   {
/*  74 */     return this.uri;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/*  83 */     return this.uri.hashCode();
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 106 */     return (paramObject != null) && ((paramObject instanceof URISyntax)) && (this.uri.equals(((URISyntax)paramObject).uri));
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 118 */     return this.uri.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.attribute.URISyntax
 * JD-Core Version:    0.6.2
 */