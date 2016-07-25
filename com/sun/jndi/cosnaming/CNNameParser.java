/*     */ package com.sun.jndi.cosnaming;
/*     */ 
/*     */ import java.util.Enumeration;
/*     */ import java.util.Properties;
/*     */ import java.util.Vector;
/*     */ import javax.naming.CompositeName;
/*     */ import javax.naming.CompoundName;
/*     */ import javax.naming.InvalidNameException;
/*     */ import javax.naming.Name;
/*     */ import javax.naming.NameParser;
/*     */ import javax.naming.NamingException;
/*     */ import org.omg.CosNaming.NameComponent;
/*     */ 
/*     */ public final class CNNameParser
/*     */   implements NameParser
/*     */ {
/*  48 */   private static final Properties mySyntax = new Properties();
/*     */   private static final char kindSeparator = '.';
/*     */   private static final char compSeparator = '/';
/*     */   private static final char escapeChar = '\\';
/*     */ 
/*     */   public Name parse(String paramString)
/*     */     throws NamingException
/*     */   {
/*  70 */     Vector localVector = insStringToStringifiedComps(paramString);
/*  71 */     return new CNCompoundName(localVector.elements());
/*     */   }
/*     */ 
/*     */   static NameComponent[] nameToCosName(Name paramName)
/*     */     throws InvalidNameException
/*     */   {
/*  82 */     int i = paramName.size();
/*  83 */     if (i == 0) {
/*  84 */       return new NameComponent[0];
/*     */     }
/*     */ 
/*  87 */     NameComponent[] arrayOfNameComponent = new NameComponent[i];
/*  88 */     for (int j = 0; j < i; j++) {
/*  89 */       arrayOfNameComponent[j] = parseComponent(paramName.get(j));
/*     */     }
/*  91 */     return arrayOfNameComponent;
/*     */   }
/*     */ 
/*     */   static String cosNameToInsString(NameComponent[] paramArrayOfNameComponent)
/*     */   {
/*  99 */     StringBuffer localStringBuffer = new StringBuffer();
/* 100 */     for (int i = 0; i < paramArrayOfNameComponent.length; i++) {
/* 101 */       if (i > 0) {
/* 102 */         localStringBuffer.append('/');
/*     */       }
/* 104 */       localStringBuffer.append(stringifyComponent(paramArrayOfNameComponent[i]));
/*     */     }
/* 106 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   static Name cosNameToName(NameComponent[] paramArrayOfNameComponent)
/*     */   {
/* 115 */     CompositeName localCompositeName = new CompositeName();
/* 116 */     for (int i = 0; (paramArrayOfNameComponent != null) && (i < paramArrayOfNameComponent.length); i++)
/*     */       try {
/* 118 */         localCompositeName.add(stringifyComponent(paramArrayOfNameComponent[i]));
/*     */       }
/*     */       catch (InvalidNameException localInvalidNameException)
/*     */       {
/*     */       }
/* 123 */     return localCompositeName;
/*     */   }
/*     */ 
/*     */   private static Vector insStringToStringifiedComps(String paramString)
/*     */     throws InvalidNameException
/*     */   {
/* 134 */     int i = paramString.length();
/* 135 */     Vector localVector = new Vector(10);
/* 136 */     char[] arrayOfChar1 = new char[i];
/* 137 */     char[] arrayOfChar2 = new char[i];
/*     */ 
/* 140 */     for (int n = 0; n < i; )
/*     */     {
/*     */       int k;
/* 141 */       int j = k = 0;
/* 142 */       int m = 1;
/* 143 */       while ((n < i) && 
/* 144 */         (paramString.charAt(n) != '/'))
/*     */       {
/* 147 */         if (paramString.charAt(n) == '\\') {
/* 148 */           if (n + 1 >= i) {
/* 149 */             throw new InvalidNameException(paramString + ": unescaped \\ at end of component");
/*     */           }
/* 151 */           if (isMeta(paramString.charAt(n + 1))) {
/* 152 */             n++;
/* 153 */             if (m != 0)
/* 154 */               arrayOfChar1[(j++)] = paramString.charAt(n++);
/*     */             else
/* 156 */               arrayOfChar2[(k++)] = paramString.charAt(n++);
/*     */           }
/*     */           else {
/* 159 */             throw new InvalidNameException(paramString + ": invalid character being escaped");
/*     */           }
/*     */ 
/*     */         }
/* 163 */         else if ((m != 0) && (paramString.charAt(n) == '.'))
/*     */         {
/* 165 */           n++;
/* 166 */           m = 0;
/*     */         }
/* 169 */         else if (m != 0) {
/* 170 */           arrayOfChar1[(j++)] = paramString.charAt(n++);
/*     */         } else {
/* 172 */           arrayOfChar2[(k++)] = paramString.charAt(n++);
/*     */         }
/*     */       }
/*     */ 
/* 176 */       localVector.addElement(stringifyComponent(new NameComponent(new String(arrayOfChar1, 0, j), new String(arrayOfChar2, 0, k))));
/*     */ 
/* 180 */       if (n < i) {
/* 181 */         n++;
/*     */       }
/*     */     }
/*     */ 
/* 185 */     return localVector;
/*     */   }
/*     */ 
/*     */   private static NameComponent parseComponent(String paramString)
/*     */     throws InvalidNameException
/*     */   {
/* 193 */     NameComponent localNameComponent = new NameComponent();
/* 194 */     int i = -1;
/* 195 */     int j = paramString.length();
/*     */ 
/* 197 */     int k = 0;
/* 198 */     char[] arrayOfChar = new char[j];
/* 199 */     int m = 0;
/*     */ 
/* 202 */     for (int n = 0; (n < j) && (i < 0); n++) {
/* 203 */       if (m != 0) {
/* 204 */         arrayOfChar[(k++)] = paramString.charAt(n);
/* 205 */         m = 0;
/* 206 */       } else if (paramString.charAt(n) == '\\') {
/* 207 */         if (n + 1 >= j) {
/* 208 */           throw new InvalidNameException(paramString + ": unescaped \\ at end of component");
/*     */         }
/* 210 */         if (isMeta(paramString.charAt(n + 1)))
/* 211 */           m = 1;
/*     */         else {
/* 213 */           throw new InvalidNameException(paramString + ": invalid character being escaped");
/*     */         }
/*     */       }
/* 216 */       else if (paramString.charAt(n) == '.') {
/* 217 */         i = n;
/*     */       } else {
/* 219 */         arrayOfChar[(k++)] = paramString.charAt(n);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 224 */     localNameComponent.id = new String(arrayOfChar, 0, k);
/*     */ 
/* 227 */     if (i < 0) {
/* 228 */       localNameComponent.kind = "";
/*     */     }
/*     */     else {
/* 231 */       k = 0;
/* 232 */       m = 0;
/* 233 */       for (n = i + 1; n < j; n++) {
/* 234 */         if (m != 0) {
/* 235 */           arrayOfChar[(k++)] = paramString.charAt(n);
/* 236 */           m = 0;
/* 237 */         } else if (paramString.charAt(n) == '\\') {
/* 238 */           if (n + 1 >= j) {
/* 239 */             throw new InvalidNameException(paramString + ": unescaped \\ at end of component");
/*     */           }
/* 241 */           if (isMeta(paramString.charAt(n + 1)))
/* 242 */             m = 1;
/*     */           else
/* 244 */             throw new InvalidNameException(paramString + ": invalid character being escaped");
/*     */         }
/*     */         else
/*     */         {
/* 248 */           arrayOfChar[(k++)] = paramString.charAt(n);
/*     */         }
/*     */       }
/* 251 */       localNameComponent.kind = new String(arrayOfChar, 0, k);
/*     */     }
/* 253 */     return localNameComponent;
/*     */   }
/*     */ 
/*     */   private static String stringifyComponent(NameComponent paramNameComponent) {
/* 257 */     StringBuffer localStringBuffer = new StringBuffer(escape(paramNameComponent.id));
/* 258 */     if ((paramNameComponent.kind != null) && (!paramNameComponent.kind.equals(""))) {
/* 259 */       localStringBuffer.append('.' + escape(paramNameComponent.kind));
/*     */     }
/* 261 */     if (localStringBuffer.length() == 0) {
/* 262 */       return ".";
/*     */     }
/* 264 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   private static String escape(String paramString)
/*     */   {
/* 273 */     if ((paramString.indexOf('.') < 0) && (paramString.indexOf('/') < 0) && (paramString.indexOf('\\') < 0))
/*     */     {
/* 276 */       return paramString;
/*     */     }
/* 278 */     int i = paramString.length();
/* 279 */     int j = 0;
/* 280 */     char[] arrayOfChar = new char[i + i];
/* 281 */     for (int k = 0; k < i; k++) {
/* 282 */       if (isMeta(paramString.charAt(k))) {
/* 283 */         arrayOfChar[(j++)] = '\\';
/*     */       }
/* 285 */       arrayOfChar[(j++)] = paramString.charAt(k);
/*     */     }
/* 287 */     return new String(arrayOfChar, 0, j);
/*     */   }
/*     */ 
/*     */   private static boolean isMeta(char paramChar)
/*     */   {
/* 295 */     switch (paramChar) {
/*     */     case '.':
/*     */     case '/':
/*     */     case '\\':
/* 299 */       return true;
/*     */     }
/* 301 */     return false;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  53 */     mySyntax.put("jndi.syntax.direction", "left_to_right");
/*  54 */     mySyntax.put("jndi.syntax.separator", "/");
/*  55 */     mySyntax.put("jndi.syntax.escape", "\\");
/*     */   }
/*     */ 
/*     */   static final class CNCompoundName extends CompoundName
/*     */   {
/*     */     private static final long serialVersionUID = -6599252802678482317L;
/*     */ 
/*     */     CNCompoundName(Enumeration paramEnumeration)
/*     */     {
/* 310 */       super(CNNameParser.mySyntax);
/*     */     }
/*     */ 
/*     */     public Object clone() {
/* 314 */       return new CNCompoundName(getAll());
/*     */     }
/*     */ 
/*     */     public Name getPrefix(int paramInt) {
/* 318 */       Enumeration localEnumeration = super.getPrefix(paramInt).getAll();
/* 319 */       return new CNCompoundName(localEnumeration);
/*     */     }
/*     */ 
/*     */     public Name getSuffix(int paramInt) {
/* 323 */       Enumeration localEnumeration = super.getSuffix(paramInt).getAll();
/* 324 */       return new CNCompoundName(localEnumeration);
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/*     */       try {
/* 330 */         return CNNameParser.cosNameToInsString(CNNameParser.nameToCosName(this)); } catch (InvalidNameException localInvalidNameException) {
/*     */       }
/* 332 */       return super.toString();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.cosnaming.CNNameParser
 * JD-Core Version:    0.6.2
 */