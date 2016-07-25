/*     */ package com.sun.org.apache.xpath.internal.objects;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.utils.WrappedRuntimeException;
/*     */ import com.sun.org.apache.xpath.internal.ExpressionOwner;
/*     */ import com.sun.org.apache.xpath.internal.XPathContext;
/*     */ import com.sun.org.apache.xpath.internal.XPathVisitor;
/*     */ import javax.xml.transform.TransformerException;
/*     */ 
/*     */ public class XNumber extends XObject
/*     */ {
/*     */   static final long serialVersionUID = -2720400709619020193L;
/*     */   double m_val;
/*     */ 
/*     */   public XNumber(double d)
/*     */   {
/*  51 */     this.m_val = d;
/*     */   }
/*     */ 
/*     */   public XNumber(Number num)
/*     */   {
/*  64 */     this.m_val = num.doubleValue();
/*  65 */     setObject(num);
/*     */   }
/*     */ 
/*     */   public int getType()
/*     */   {
/*  75 */     return 2;
/*     */   }
/*     */ 
/*     */   public String getTypeString()
/*     */   {
/*  86 */     return "#NUMBER";
/*     */   }
/*     */ 
/*     */   public double num()
/*     */   {
/*  96 */     return this.m_val;
/*     */   }
/*     */ 
/*     */   public double num(XPathContext xctxt)
/*     */     throws TransformerException
/*     */   {
/* 110 */     return this.m_val;
/*     */   }
/*     */ 
/*     */   public boolean bool()
/*     */   {
/* 120 */     return (!Double.isNaN(this.m_val)) && (this.m_val != 0.0D);
/*     */   }
/*     */ 
/*     */   public String str()
/*     */   {
/* 282 */     if (Double.isNaN(this.m_val))
/*     */     {
/* 284 */       return "NaN";
/*     */     }
/* 286 */     if (Double.isInfinite(this.m_val))
/*     */     {
/* 288 */       if (this.m_val > 0.0D) {
/* 289 */         return "Infinity";
/*     */       }
/* 291 */       return "-Infinity";
/*     */     }
/*     */ 
/* 294 */     double num = this.m_val;
/* 295 */     String s = Double.toString(num);
/* 296 */     int len = s.length();
/*     */ 
/* 298 */     if ((s.charAt(len - 2) == '.') && (s.charAt(len - 1) == '0'))
/*     */     {
/* 300 */       s = s.substring(0, len - 2);
/*     */ 
/* 302 */       if (s.equals("-0")) {
/* 303 */         return "0";
/*     */       }
/* 305 */       return s;
/*     */     }
/*     */ 
/* 308 */     int e = s.indexOf('E');
/*     */ 
/* 310 */     if (e < 0)
/*     */     {
/* 312 */       if (s.charAt(len - 1) == '0') {
/* 313 */         return s.substring(0, len - 1);
/*     */       }
/* 315 */       return s;
/*     */     }
/*     */ 
/* 318 */     int exp = Integer.parseInt(s.substring(e + 1));
/*     */     String sign;
/* 321 */     if (s.charAt(0) == '-')
/*     */     {
/* 323 */       String sign = "-";
/* 324 */       s = s.substring(1);
/*     */ 
/* 326 */       e--;
/*     */     }
/*     */     else {
/* 329 */       sign = "";
/*     */     }
/* 331 */     int nDigits = e - 2;
/*     */ 
/* 333 */     if (exp >= nDigits) {
/* 334 */       return sign + s.substring(0, 1) + s.substring(2, e) + zeros(exp - nDigits);
/*     */     }
/*     */ 
/* 338 */     while (s.charAt(e - 1) == '0') {
/* 339 */       e--;
/*     */     }
/* 341 */     if (exp > 0) {
/* 342 */       return sign + s.substring(0, 1) + s.substring(2, 2 + exp) + "." + s.substring(2 + exp, e);
/*     */     }
/*     */ 
/* 345 */     return sign + "0." + zeros(-1 - exp) + s.substring(0, 1) + s.substring(2, e);
/*     */   }
/*     */ 
/*     */   private static String zeros(int n)
/*     */   {
/* 360 */     if (n < 1) {
/* 361 */       return "";
/*     */     }
/* 363 */     char[] buf = new char[n];
/*     */ 
/* 365 */     for (int i = 0; i < n; i++)
/*     */     {
/* 367 */       buf[i] = '0';
/*     */     }
/*     */ 
/* 370 */     return new String(buf);
/*     */   }
/*     */ 
/*     */   public Object object()
/*     */   {
/* 381 */     if (null == this.m_obj)
/* 382 */       setObject(new Double(this.m_val));
/* 383 */     return this.m_obj;
/*     */   }
/*     */ 
/*     */   public boolean equals(XObject obj2)
/*     */   {
/* 401 */     int t = obj2.getType();
/*     */     try
/*     */     {
/* 404 */       if (t == 4)
/* 405 */         return obj2.equals(this);
/* 406 */       if (t == 1) {
/* 407 */         return obj2.bool() == bool();
/*     */       }
/* 409 */       return this.m_val == obj2.num();
/*     */     }
/*     */     catch (TransformerException te)
/*     */     {
/* 413 */       throw new WrappedRuntimeException(te);
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean isStableNumber()
/*     */   {
/* 427 */     return true;
/*     */   }
/*     */ 
/*     */   public void callVisitors(ExpressionOwner owner, XPathVisitor visitor)
/*     */   {
/* 435 */     visitor.visitNumberLiteral(owner, this);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.objects.XNumber
 * JD-Core Version:    0.6.2
 */