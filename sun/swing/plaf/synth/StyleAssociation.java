/*     */ package sun.swing.plaf.synth;
/*     */ 
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import java.util.regex.PatternSyntaxException;
/*     */ import javax.swing.plaf.synth.SynthStyle;
/*     */ 
/*     */ public class StyleAssociation
/*     */ {
/*     */   private SynthStyle _style;
/*     */   private Pattern _pattern;
/*     */   private Matcher _matcher;
/*     */   private int _id;
/*     */ 
/*     */   public static StyleAssociation createStyleAssociation(String paramString, SynthStyle paramSynthStyle)
/*     */     throws PatternSyntaxException
/*     */   {
/*  69 */     return createStyleAssociation(paramString, paramSynthStyle, 0);
/*     */   }
/*     */ 
/*     */   public static StyleAssociation createStyleAssociation(String paramString, SynthStyle paramSynthStyle, int paramInt)
/*     */     throws PatternSyntaxException
/*     */   {
/*  79 */     return new StyleAssociation(paramString, paramSynthStyle, paramInt);
/*     */   }
/*     */ 
/*     */   private StyleAssociation(String paramString, SynthStyle paramSynthStyle, int paramInt)
/*     */     throws PatternSyntaxException
/*     */   {
/*  85 */     this._style = paramSynthStyle;
/*  86 */     this._pattern = Pattern.compile(paramString);
/*  87 */     this._id = paramInt;
/*     */   }
/*     */ 
/*     */   public int getID()
/*     */   {
/*  96 */     return this._id;
/*     */   }
/*     */ 
/*     */   public synchronized boolean matches(CharSequence paramCharSequence)
/*     */   {
/* 107 */     if (this._matcher == null) {
/* 108 */       this._matcher = this._pattern.matcher(paramCharSequence);
/*     */     }
/*     */     else {
/* 111 */       this._matcher.reset(paramCharSequence);
/*     */     }
/* 113 */     return this._matcher.matches();
/*     */   }
/*     */ 
/*     */   public String getText()
/*     */   {
/* 122 */     return this._pattern.pattern();
/*     */   }
/*     */ 
/*     */   public SynthStyle getStyle()
/*     */   {
/* 131 */     return this._style;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.swing.plaf.synth.StyleAssociation
 * JD-Core Version:    0.6.2
 */