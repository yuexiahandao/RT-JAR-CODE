/*     */ package javax.print.attribute.standard;
/*     */ 
/*     */ import javax.print.attribute.Attribute;
/*     */ import javax.print.attribute.EnumSyntax;
/*     */ 
/*     */ public class ReferenceUriSchemesSupported extends EnumSyntax
/*     */   implements Attribute
/*     */ {
/*     */   private static final long serialVersionUID = -8989076942813442805L;
/*  70 */   public static final ReferenceUriSchemesSupported FTP = new ReferenceUriSchemesSupported(0);
/*     */ 
/*  76 */   public static final ReferenceUriSchemesSupported HTTP = new ReferenceUriSchemesSupported(1);
/*     */ 
/*  81 */   public static final ReferenceUriSchemesSupported HTTPS = new ReferenceUriSchemesSupported(2);
/*     */ 
/*  86 */   public static final ReferenceUriSchemesSupported GOPHER = new ReferenceUriSchemesSupported(3);
/*     */ 
/*  91 */   public static final ReferenceUriSchemesSupported NEWS = new ReferenceUriSchemesSupported(4);
/*     */ 
/*  96 */   public static final ReferenceUriSchemesSupported NNTP = new ReferenceUriSchemesSupported(5);
/*     */ 
/* 101 */   public static final ReferenceUriSchemesSupported WAIS = new ReferenceUriSchemesSupported(6);
/*     */ 
/* 106 */   public static final ReferenceUriSchemesSupported FILE = new ReferenceUriSchemesSupported(7);
/*     */ 
/* 118 */   private static final String[] myStringTable = { "ftp", "http", "https", "gopher", "news", "nntp", "wais", "file" };
/*     */ 
/* 129 */   private static final ReferenceUriSchemesSupported[] myEnumValueTable = { FTP, HTTP, HTTPS, GOPHER, NEWS, NNTP, WAIS, FILE };
/*     */ 
/*     */   protected ReferenceUriSchemesSupported(int paramInt)
/*     */   {
/* 115 */     super(paramInt);
/*     */   }
/*     */ 
/*     */   protected String[] getStringTable()
/*     */   {
/* 144 */     return (String[])myStringTable.clone();
/*     */   }
/*     */ 
/*     */   protected EnumSyntax[] getEnumValueTable()
/*     */   {
/* 152 */     return (EnumSyntax[])myEnumValueTable.clone();
/*     */   }
/*     */ 
/*     */   public final Class<? extends Attribute> getCategory()
/*     */   {
/* 166 */     return ReferenceUriSchemesSupported.class;
/*     */   }
/*     */ 
/*     */   public final String getName()
/*     */   {
/* 180 */     return "reference-uri-schemes-supported";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.attribute.standard.ReferenceUriSchemesSupported
 * JD-Core Version:    0.6.2
 */