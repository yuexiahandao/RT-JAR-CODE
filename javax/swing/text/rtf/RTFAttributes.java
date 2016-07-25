/*     */ package javax.swing.text.rtf;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Dictionary;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Vector;
/*     */ import javax.swing.text.AttributeSet;
/*     */ import javax.swing.text.MutableAttributeSet;
/*     */ import javax.swing.text.StyleConstants;
/*     */ 
/*     */ class RTFAttributes
/*     */ {
/* 131 */   static RTFAttribute[] attributes = arrayOfRTFAttribute;
/*     */ 
/*     */   static Dictionary<String, RTFAttribute> attributesByKeyword()
/*     */   {
/* 136 */     Hashtable localHashtable = new Hashtable(attributes.length);
/*     */ 
/* 138 */     for (RTFAttribute localRTFAttribute : attributes) {
/* 139 */       localHashtable.put(localRTFAttribute.rtfName(), localRTFAttribute);
/*     */     }
/*     */ 
/* 142 */     return localHashtable;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  39 */     Vector localVector = new Vector();
/*  40 */     int i = 0;
/*  41 */     int j = 1;
/*  42 */     int k = 2;
/*  43 */     int m = 3;
/*  44 */     int n = 4;
/*  45 */     Boolean localBoolean1 = Boolean.valueOf(true);
/*  46 */     Boolean localBoolean2 = Boolean.valueOf(false);
/*     */ 
/*  48 */     localVector.addElement(new BooleanAttribute(i, StyleConstants.Italic, "i"));
/*  49 */     localVector.addElement(new BooleanAttribute(i, StyleConstants.Bold, "b"));
/*  50 */     localVector.addElement(new BooleanAttribute(i, StyleConstants.Underline, "ul"));
/*  51 */     localVector.addElement(NumericAttribute.NewTwips(j, StyleConstants.LeftIndent, "li", 0.0F, 0));
/*     */ 
/*  53 */     localVector.addElement(NumericAttribute.NewTwips(j, StyleConstants.RightIndent, "ri", 0.0F, 0));
/*     */ 
/*  55 */     localVector.addElement(NumericAttribute.NewTwips(j, StyleConstants.FirstLineIndent, "fi", 0.0F, 0));
/*     */ 
/*  58 */     localVector.addElement(new AssertiveAttribute(j, StyleConstants.Alignment, "ql", 0));
/*     */ 
/*  60 */     localVector.addElement(new AssertiveAttribute(j, StyleConstants.Alignment, "qr", 2));
/*     */ 
/*  62 */     localVector.addElement(new AssertiveAttribute(j, StyleConstants.Alignment, "qc", 1));
/*     */ 
/*  64 */     localVector.addElement(new AssertiveAttribute(j, StyleConstants.Alignment, "qj", 3));
/*     */ 
/*  66 */     localVector.addElement(NumericAttribute.NewTwips(j, StyleConstants.SpaceAbove, "sa", 0));
/*     */ 
/*  68 */     localVector.addElement(NumericAttribute.NewTwips(j, StyleConstants.SpaceBelow, "sb", 0));
/*     */ 
/*  71 */     localVector.addElement(new AssertiveAttribute(n, "tab_alignment", "tqr", 1));
/*     */ 
/*  73 */     localVector.addElement(new AssertiveAttribute(n, "tab_alignment", "tqc", 2));
/*     */ 
/*  75 */     localVector.addElement(new AssertiveAttribute(n, "tab_alignment", "tqdec", 4));
/*     */ 
/*  79 */     localVector.addElement(new AssertiveAttribute(n, "tab_leader", "tldot", 1));
/*     */ 
/*  81 */     localVector.addElement(new AssertiveAttribute(n, "tab_leader", "tlhyph", 2));
/*     */ 
/*  83 */     localVector.addElement(new AssertiveAttribute(n, "tab_leader", "tlul", 3));
/*     */ 
/*  85 */     localVector.addElement(new AssertiveAttribute(n, "tab_leader", "tlth", 4));
/*     */ 
/*  87 */     localVector.addElement(new AssertiveAttribute(n, "tab_leader", "tleq", 5));
/*     */ 
/*  91 */     localVector.addElement(new BooleanAttribute(i, "caps", "caps"));
/*  92 */     localVector.addElement(new BooleanAttribute(i, "outl", "outl"));
/*  93 */     localVector.addElement(new BooleanAttribute(i, "scaps", "scaps"));
/*  94 */     localVector.addElement(new BooleanAttribute(i, "shad", "shad"));
/*  95 */     localVector.addElement(new BooleanAttribute(i, "v", "v"));
/*  96 */     localVector.addElement(new BooleanAttribute(i, "strike", "strike"));
/*     */ 
/*  98 */     localVector.addElement(new BooleanAttribute(i, "deleted", "deleted"));
/*     */ 
/* 103 */     localVector.addElement(new AssertiveAttribute(m, "saveformat", "defformat", "RTF"));
/* 104 */     localVector.addElement(new AssertiveAttribute(m, "landscape", "landscape"));
/*     */ 
/* 106 */     localVector.addElement(NumericAttribute.NewTwips(m, "paperw", "paperw", 12240));
/*     */ 
/* 108 */     localVector.addElement(NumericAttribute.NewTwips(m, "paperh", "paperh", 15840));
/*     */ 
/* 110 */     localVector.addElement(NumericAttribute.NewTwips(m, "margl", "margl", 1800));
/*     */ 
/* 112 */     localVector.addElement(NumericAttribute.NewTwips(m, "margr", "margr", 1800));
/*     */ 
/* 114 */     localVector.addElement(NumericAttribute.NewTwips(m, "margt", "margt", 1440));
/*     */ 
/* 116 */     localVector.addElement(NumericAttribute.NewTwips(m, "margb", "margb", 1440));
/*     */ 
/* 118 */     localVector.addElement(NumericAttribute.NewTwips(m, "gutter", "gutter", 0));
/*     */ 
/* 121 */     localVector.addElement(new AssertiveAttribute(j, "widowctrl", "nowidctlpar", localBoolean2));
/*     */ 
/* 123 */     localVector.addElement(new AssertiveAttribute(j, "widowctrl", "widctlpar", localBoolean1));
/*     */ 
/* 125 */     localVector.addElement(new AssertiveAttribute(m, "widowctrl", "widowctrl", localBoolean1));
/*     */ 
/* 129 */     RTFAttribute[] arrayOfRTFAttribute = new RTFAttribute[localVector.size()];
/* 130 */     localVector.copyInto(arrayOfRTFAttribute);
/*     */   }
/*     */ 
/*     */   static class AssertiveAttribute extends RTFAttributes.GenericAttribute
/*     */     implements RTFAttribute
/*     */   {
/*     */     Object swingValue;
/*     */ 
/*     */     public AssertiveAttribute(int paramInt, Object paramObject, String paramString)
/*     */     {
/* 270 */       super(paramObject, paramString);
/* 271 */       this.swingValue = Boolean.valueOf(true);
/*     */     }
/*     */ 
/*     */     public AssertiveAttribute(int paramInt, Object paramObject1, String paramString, Object paramObject2)
/*     */     {
/* 276 */       super(paramObject1, paramString);
/* 277 */       this.swingValue = paramObject2;
/*     */     }
/*     */ 
/*     */     public AssertiveAttribute(int paramInt1, Object paramObject, String paramString, int paramInt2)
/*     */     {
/* 282 */       super(paramObject, paramString);
/* 283 */       this.swingValue = Integer.valueOf(paramInt2);
/*     */     }
/*     */ 
/*     */     public boolean set(MutableAttributeSet paramMutableAttributeSet)
/*     */     {
/* 288 */       if (this.swingValue == null)
/* 289 */         paramMutableAttributeSet.removeAttribute(this.swingName);
/*     */       else {
/* 291 */         paramMutableAttributeSet.addAttribute(this.swingName, this.swingValue);
/*     */       }
/* 293 */       return true;
/*     */     }
/*     */ 
/*     */     public boolean set(MutableAttributeSet paramMutableAttributeSet, int paramInt)
/*     */     {
/* 298 */       return false;
/*     */     }
/*     */ 
/*     */     public boolean setDefault(MutableAttributeSet paramMutableAttributeSet)
/*     */     {
/* 303 */       paramMutableAttributeSet.removeAttribute(this.swingName);
/* 304 */       return true;
/*     */     }
/*     */ 
/*     */     public boolean writeValue(Object paramObject, RTFGenerator paramRTFGenerator, boolean paramBoolean)
/*     */       throws IOException
/*     */     {
/* 312 */       if (paramObject == null) {
/* 313 */         return !paramBoolean;
/*     */       }
/*     */ 
/* 316 */       if (paramObject.equals(this.swingValue)) {
/* 317 */         paramRTFGenerator.writeControlWord(this.rtfName);
/* 318 */         return true;
/*     */       }
/*     */ 
/* 321 */       return !paramBoolean;
/*     */     }
/*     */   }
/*     */ 
/*     */   static class BooleanAttribute extends RTFAttributes.GenericAttribute
/*     */     implements RTFAttribute
/*     */   {
/*     */     boolean rtfDefault;
/*     */     boolean swingDefault;
/* 192 */     protected static final Boolean True = Boolean.valueOf(true);
/* 193 */     protected static final Boolean False = Boolean.valueOf(false);
/*     */ 
/*     */     public BooleanAttribute(int paramInt, Object paramObject, String paramString, boolean paramBoolean1, boolean paramBoolean2)
/*     */     {
/* 198 */       super(paramObject, paramString);
/* 199 */       this.swingDefault = paramBoolean1;
/* 200 */       this.rtfDefault = paramBoolean2;
/*     */     }
/*     */ 
/*     */     public BooleanAttribute(int paramInt, Object paramObject, String paramString)
/*     */     {
/* 205 */       super(paramObject, paramString);
/*     */ 
/* 207 */       this.swingDefault = false;
/* 208 */       this.rtfDefault = false;
/*     */     }
/*     */ 
/*     */     public boolean set(MutableAttributeSet paramMutableAttributeSet)
/*     */     {
/* 215 */       paramMutableAttributeSet.addAttribute(this.swingName, True);
/*     */ 
/* 217 */       return true;
/*     */     }
/*     */ 
/*     */     public boolean set(MutableAttributeSet paramMutableAttributeSet, int paramInt)
/*     */     {
/* 223 */       Boolean localBoolean = paramInt != 0 ? True : False;
/*     */ 
/* 225 */       paramMutableAttributeSet.addAttribute(this.swingName, localBoolean);
/*     */ 
/* 227 */       return true;
/*     */     }
/*     */ 
/*     */     public boolean setDefault(MutableAttributeSet paramMutableAttributeSet)
/*     */     {
/* 232 */       if ((this.swingDefault != this.rtfDefault) || (paramMutableAttributeSet.getAttribute(this.swingName) != null))
/*     */       {
/* 234 */         paramMutableAttributeSet.addAttribute(this.swingName, Boolean.valueOf(this.rtfDefault));
/* 235 */       }return true;
/*     */     }
/*     */ 
/*     */     public boolean writeValue(Object paramObject, RTFGenerator paramRTFGenerator, boolean paramBoolean)
/*     */       throws IOException
/*     */     {
/*     */       Boolean localBoolean;
/* 245 */       if (paramObject == null)
/* 246 */         localBoolean = Boolean.valueOf(this.swingDefault);
/*     */       else {
/* 248 */         localBoolean = (Boolean)paramObject;
/*     */       }
/* 250 */       if ((paramBoolean) || (localBoolean.booleanValue() != this.rtfDefault)) {
/* 251 */         if (localBoolean.booleanValue())
/* 252 */           paramRTFGenerator.writeControlWord(this.rtfName);
/*     */         else {
/* 254 */           paramRTFGenerator.writeControlWord(this.rtfName, 0);
/*     */         }
/*     */       }
/* 257 */       return true;
/*     */     }
/*     */   }
/*     */ 
/*     */   static abstract class GenericAttribute
/*     */   {
/*     */     int domain;
/*     */     Object swingName;
/*     */     String rtfName;
/*     */ 
/*     */     protected GenericAttribute(int paramInt, Object paramObject, String paramString)
/*     */     {
/* 156 */       this.domain = paramInt;
/* 157 */       this.swingName = paramObject;
/* 158 */       this.rtfName = paramString;
/*     */     }
/*     */     public int domain() {
/* 161 */       return this.domain; } 
/* 162 */     public Object swingName() { return this.swingName; } 
/* 163 */     public String rtfName() { return this.rtfName; }
/*     */ 
/*     */     abstract boolean set(MutableAttributeSet paramMutableAttributeSet);
/*     */ 
/*     */     abstract boolean set(MutableAttributeSet paramMutableAttributeSet, int paramInt);
/*     */ 
/*     */     abstract boolean setDefault(MutableAttributeSet paramMutableAttributeSet);
/*     */ 
/*     */     public boolean write(AttributeSet paramAttributeSet, RTFGenerator paramRTFGenerator, boolean paramBoolean)
/*     */       throws IOException
/*     */     {
/* 174 */       return writeValue(paramAttributeSet.getAttribute(this.swingName), paramRTFGenerator, paramBoolean);
/*     */     }
/*     */ 
/*     */     public boolean writeValue(Object paramObject, RTFGenerator paramRTFGenerator, boolean paramBoolean)
/*     */       throws IOException
/*     */     {
/* 181 */       return false;
/*     */     }
/*     */   }
/*     */ 
/*     */   static class NumericAttribute extends RTFAttributes.GenericAttribute
/*     */     implements RTFAttribute
/*     */   {
/*     */     int rtfDefault;
/*     */     Number swingDefault;
/*     */     float scale;
/*     */ 
/*     */     protected NumericAttribute(int paramInt, Object paramObject, String paramString)
/*     */     {
/* 336 */       super(paramObject, paramString);
/* 337 */       this.rtfDefault = 0;
/* 338 */       this.swingDefault = null;
/* 339 */       this.scale = 1.0F;
/*     */     }
/*     */ 
/*     */     public NumericAttribute(int paramInt1, Object paramObject, String paramString, int paramInt2, int paramInt3)
/*     */     {
/* 345 */       this(paramInt1, paramObject, paramString, Integer.valueOf(paramInt2), paramInt3, 1.0F);
/*     */     }
/*     */ 
/*     */     public NumericAttribute(int paramInt1, Object paramObject, String paramString, Number paramNumber, int paramInt2, float paramFloat)
/*     */     {
/* 351 */       super(paramObject, paramString);
/* 352 */       this.swingDefault = paramNumber;
/* 353 */       this.rtfDefault = paramInt2;
/* 354 */       this.scale = paramFloat;
/*     */     }
/*     */ 
/*     */     public static NumericAttribute NewTwips(int paramInt1, Object paramObject, String paramString, float paramFloat, int paramInt2)
/*     */     {
/* 360 */       return new NumericAttribute(paramInt1, paramObject, paramString, new Float(paramFloat), paramInt2, 20.0F);
/*     */     }
/*     */ 
/*     */     public static NumericAttribute NewTwips(int paramInt1, Object paramObject, String paramString, int paramInt2)
/*     */     {
/* 366 */       return new NumericAttribute(paramInt1, paramObject, paramString, null, paramInt2, 20.0F);
/*     */     }
/*     */ 
/*     */     public boolean set(MutableAttributeSet paramMutableAttributeSet)
/*     */     {
/* 371 */       return false;
/*     */     }
/*     */ 
/*     */     public boolean set(MutableAttributeSet paramMutableAttributeSet, int paramInt)
/*     */     {
/*     */       Object localObject;
/* 378 */       if (this.scale == 1.0F)
/* 379 */         localObject = Integer.valueOf(paramInt);
/*     */       else
/* 381 */         localObject = new Float(paramInt / this.scale);
/* 382 */       paramMutableAttributeSet.addAttribute(this.swingName, localObject);
/* 383 */       return true;
/*     */     }
/*     */ 
/*     */     public boolean setDefault(MutableAttributeSet paramMutableAttributeSet)
/*     */     {
/* 388 */       Number localNumber = (Number)paramMutableAttributeSet.getAttribute(this.swingName);
/* 389 */       if (localNumber == null)
/* 390 */         localNumber = this.swingDefault;
/* 391 */       if ((localNumber != null) && (((this.scale == 1.0F) && (localNumber.intValue() == this.rtfDefault)) || (Math.round(localNumber.floatValue() * this.scale) == this.rtfDefault)))
/*     */       {
/* 395 */         return true;
/* 396 */       }set(paramMutableAttributeSet, this.rtfDefault);
/* 397 */       return true;
/*     */     }
/*     */ 
/*     */     public boolean writeValue(Object paramObject, RTFGenerator paramRTFGenerator, boolean paramBoolean)
/*     */       throws IOException
/*     */     {
/* 405 */       Number localNumber = (Number)paramObject;
/* 406 */       if (localNumber == null)
/* 407 */         localNumber = this.swingDefault;
/* 408 */       if (localNumber == null)
/*     */       {
/* 413 */         return true;
/*     */       }
/* 415 */       int i = Math.round(localNumber.floatValue() * this.scale);
/* 416 */       if ((paramBoolean) || (i != this.rtfDefault))
/* 417 */         paramRTFGenerator.writeControlWord(this.rtfName, i);
/* 418 */       return true;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.rtf.RTFAttributes
 * JD-Core Version:    0.6.2
 */