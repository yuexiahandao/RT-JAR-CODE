/*     */ package javax.swing.plaf.synth;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.regex.PatternSyntaxException;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.plaf.FontUIResource;
/*     */ import sun.swing.BakedArrayList;
/*     */ import sun.swing.plaf.synth.DefaultSynthStyle;
/*     */ import sun.swing.plaf.synth.StyleAssociation;
/*     */ 
/*     */ class DefaultSynthStyleFactory extends SynthStyleFactory
/*     */ {
/*     */   public static final int NAME = 0;
/*     */   public static final int REGION = 1;
/*     */   private List<StyleAssociation> _styles;
/*     */   private BakedArrayList _tmpList;
/*     */   private Map<BakedArrayList, SynthStyle> _resolvedStyles;
/*     */   private SynthStyle _defaultStyle;
/*     */ 
/*     */   DefaultSynthStyleFactory()
/*     */   {
/*  75 */     this._tmpList = new BakedArrayList(5);
/*  76 */     this._styles = new ArrayList();
/*  77 */     this._resolvedStyles = new HashMap();
/*     */   }
/*     */ 
/*     */   public synchronized void addStyle(DefaultSynthStyle paramDefaultSynthStyle, String paramString, int paramInt) throws PatternSyntaxException
/*     */   {
/*  82 */     if (paramString == null)
/*     */     {
/*  84 */       paramString = ".*";
/*     */     }
/*  86 */     if (paramInt == 0) {
/*  87 */       this._styles.add(StyleAssociation.createStyleAssociation(paramString, paramDefaultSynthStyle, paramInt));
/*     */     }
/*  90 */     else if (paramInt == 1)
/*  91 */       this._styles.add(StyleAssociation.createStyleAssociation(paramString.toLowerCase(), paramDefaultSynthStyle, paramInt));
/*     */   }
/*     */ 
/*     */   public synchronized SynthStyle getStyle(JComponent paramJComponent, Region paramRegion)
/*     */   {
/* 103 */     BakedArrayList localBakedArrayList = this._tmpList;
/*     */ 
/* 105 */     localBakedArrayList.clear();
/* 106 */     getMatchingStyles(localBakedArrayList, paramJComponent, paramRegion);
/*     */ 
/* 108 */     if (localBakedArrayList.size() == 0) {
/* 109 */       return getDefaultStyle();
/*     */     }
/*     */ 
/* 112 */     localBakedArrayList.cacheHashCode();
/* 113 */     SynthStyle localSynthStyle = getCachedStyle(localBakedArrayList);
/*     */ 
/* 115 */     if (localSynthStyle == null) {
/* 116 */       localSynthStyle = mergeStyles(localBakedArrayList);
/*     */ 
/* 118 */       if (localSynthStyle != null) {
/* 119 */         cacheStyle(localBakedArrayList, localSynthStyle);
/*     */       }
/*     */     }
/* 122 */     return localSynthStyle;
/*     */   }
/*     */ 
/*     */   private SynthStyle getDefaultStyle()
/*     */   {
/* 129 */     if (this._defaultStyle == null) {
/* 130 */       this._defaultStyle = new DefaultSynthStyle();
/* 131 */       ((DefaultSynthStyle)this._defaultStyle).setFont(new FontUIResource("Dialog", 0, 12));
/*     */     }
/*     */ 
/* 134 */     return this._defaultStyle;
/*     */   }
/*     */ 
/*     */   private void getMatchingStyles(List paramList, JComponent paramJComponent, Region paramRegion)
/*     */   {
/* 143 */     String str1 = paramRegion.getLowerCaseName();
/* 144 */     String str2 = paramJComponent.getName();
/*     */ 
/* 146 */     if (str2 == null) {
/* 147 */       str2 = "";
/*     */     }
/* 149 */     for (int i = this._styles.size() - 1; i >= 0; i--) {
/* 150 */       StyleAssociation localStyleAssociation = (StyleAssociation)this._styles.get(i);
/*     */       String str3;
/* 153 */       if (localStyleAssociation.getID() == 0) {
/* 154 */         str3 = str2;
/*     */       }
/*     */       else {
/* 157 */         str3 = str1;
/*     */       }
/*     */ 
/* 160 */       if ((localStyleAssociation.matches(str3)) && (paramList.indexOf(localStyleAssociation.getStyle()) == -1))
/* 161 */         paramList.add(localStyleAssociation.getStyle());
/*     */     }
/*     */   }
/*     */ 
/*     */   private void cacheStyle(List paramList, SynthStyle paramSynthStyle)
/*     */   {
/* 170 */     BakedArrayList localBakedArrayList = new BakedArrayList(paramList);
/*     */ 
/* 172 */     this._resolvedStyles.put(localBakedArrayList, paramSynthStyle);
/*     */   }
/*     */ 
/*     */   private SynthStyle getCachedStyle(List paramList)
/*     */   {
/* 179 */     if (paramList.size() == 0) {
/* 180 */       return null;
/*     */     }
/* 182 */     return (SynthStyle)this._resolvedStyles.get(paramList);
/*     */   }
/*     */ 
/*     */   private SynthStyle mergeStyles(List paramList)
/*     */   {
/* 191 */     int i = paramList.size();
/*     */ 
/* 193 */     if (i == 0) {
/* 194 */       return null;
/*     */     }
/* 196 */     if (i == 1) {
/* 197 */       return (SynthStyle)((DefaultSynthStyle)paramList.get(0)).clone();
/*     */     }
/*     */ 
/* 201 */     DefaultSynthStyle localDefaultSynthStyle = (DefaultSynthStyle)paramList.get(i - 1);
/*     */ 
/* 203 */     localDefaultSynthStyle = (DefaultSynthStyle)localDefaultSynthStyle.clone();
/* 204 */     for (int j = i - 2; j >= 0; j--) {
/* 205 */       localDefaultSynthStyle = ((DefaultSynthStyle)paramList.get(j)).addTo(localDefaultSynthStyle);
/*     */     }
/* 207 */     return localDefaultSynthStyle;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.synth.DefaultSynthStyleFactory
 * JD-Core Version:    0.6.2
 */