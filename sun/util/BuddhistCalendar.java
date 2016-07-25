/*     */ package sun.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.util.GregorianCalendar;
/*     */ import java.util.HashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.ResourceBundle;
/*     */ import java.util.TimeZone;
/*     */ import sun.util.resources.LocaleData;
/*     */ 
/*     */ public class BuddhistCalendar extends GregorianCalendar
/*     */ {
/*     */   private static final long serialVersionUID = -8527488697350388578L;
/*     */   private static final int BUDDHIST_YEAR_OFFSET = 543;
/* 291 */   private transient int yearOffset = 543;
/*     */ 
/*     */   public BuddhistCalendar()
/*     */   {
/*     */   }
/*     */ 
/*     */   public BuddhistCalendar(TimeZone paramTimeZone)
/*     */   {
/*  67 */     super(paramTimeZone);
/*     */   }
/*     */ 
/*     */   public BuddhistCalendar(Locale paramLocale)
/*     */   {
/*  76 */     super(paramLocale);
/*     */   }
/*     */ 
/*     */   public BuddhistCalendar(TimeZone paramTimeZone, Locale paramLocale)
/*     */   {
/*  86 */     super(paramTimeZone, paramLocale);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/*  99 */     return ((paramObject instanceof BuddhistCalendar)) && (super.equals(paramObject));
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 108 */     return super.hashCode() ^ 0x21F;
/*     */   }
/*     */ 
/*     */   public int get(int paramInt)
/*     */   {
/* 118 */     if (paramInt == 1) {
/* 119 */       return super.get(paramInt) + this.yearOffset;
/*     */     }
/* 121 */     return super.get(paramInt);
/*     */   }
/*     */ 
/*     */   public void set(int paramInt1, int paramInt2)
/*     */   {
/* 131 */     if (paramInt1 == 1)
/* 132 */       super.set(paramInt1, paramInt2 - this.yearOffset);
/*     */     else
/* 134 */       super.set(paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public void add(int paramInt1, int paramInt2)
/*     */   {
/* 145 */     int i = this.yearOffset;
/*     */ 
/* 148 */     this.yearOffset = 0;
/*     */     try {
/* 150 */       super.add(paramInt1, paramInt2);
/*     */     } finally {
/* 152 */       this.yearOffset = i;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void roll(int paramInt1, int paramInt2)
/*     */   {
/* 165 */     int i = this.yearOffset;
/*     */ 
/* 168 */     this.yearOffset = 0;
/*     */     try {
/* 170 */       super.roll(paramInt1, paramInt2);
/*     */     } finally {
/* 172 */       this.yearOffset = i;
/*     */     }
/*     */   }
/*     */ 
/*     */   public String getDisplayName(int paramInt1, int paramInt2, Locale paramLocale) {
/* 177 */     if (paramInt1 != 0) {
/* 178 */       return super.getDisplayName(paramInt1, paramInt2, paramLocale);
/*     */     }
/*     */ 
/* 182 */     if ((paramInt1 < 0) || (paramInt1 >= this.fields.length) || (paramInt2 < 1) || (paramInt2 > 2))
/*     */     {
/* 184 */       throw new IllegalArgumentException();
/*     */     }
/* 186 */     if (paramLocale == null) {
/* 187 */       throw new NullPointerException();
/*     */     }
/* 189 */     ResourceBundle localResourceBundle = LocaleData.getDateFormatData(paramLocale);
/* 190 */     String[] arrayOfString = localResourceBundle.getStringArray(getKey(paramInt2));
/* 191 */     return arrayOfString[get(paramInt1)];
/*     */   }
/*     */ 
/*     */   public Map<String, Integer> getDisplayNames(int paramInt1, int paramInt2, Locale paramLocale) {
/* 195 */     if (paramInt1 != 0) {
/* 196 */       return super.getDisplayNames(paramInt1, paramInt2, paramLocale);
/*     */     }
/*     */ 
/* 200 */     if ((paramInt1 < 0) || (paramInt1 >= this.fields.length) || (paramInt2 < 0) || (paramInt2 > 2))
/*     */     {
/* 202 */       throw new IllegalArgumentException();
/*     */     }
/* 204 */     if (paramLocale == null) {
/* 205 */       throw new NullPointerException();
/*     */     }
/*     */ 
/* 208 */     if (paramInt2 == 0) {
/* 209 */       Map localMap1 = getDisplayNamesImpl(paramInt1, 1, paramLocale);
/* 210 */       Map localMap2 = getDisplayNamesImpl(paramInt1, 2, paramLocale);
/* 211 */       if (localMap1 == null) {
/* 212 */         return localMap2;
/*     */       }
/* 214 */       if (localMap2 != null) {
/* 215 */         localMap1.putAll(localMap2);
/*     */       }
/* 217 */       return localMap1;
/*     */     }
/*     */ 
/* 221 */     return getDisplayNamesImpl(paramInt1, paramInt2, paramLocale);
/*     */   }
/*     */ 
/*     */   private Map<String, Integer> getDisplayNamesImpl(int paramInt1, int paramInt2, Locale paramLocale) {
/* 225 */     ResourceBundle localResourceBundle = LocaleData.getDateFormatData(paramLocale);
/* 226 */     String[] arrayOfString = localResourceBundle.getStringArray(getKey(paramInt2));
/* 227 */     HashMap localHashMap = new HashMap(4);
/* 228 */     for (int i = 0; i < arrayOfString.length; i++) {
/* 229 */       localHashMap.put(arrayOfString[i], Integer.valueOf(i));
/*     */     }
/* 231 */     return localHashMap;
/*     */   }
/*     */ 
/*     */   private String getKey(int paramInt) {
/* 235 */     StringBuilder localStringBuilder = new StringBuilder();
/* 236 */     localStringBuilder.append(BuddhistCalendar.class.getName());
/* 237 */     if (paramInt == 1) {
/* 238 */       localStringBuilder.append(".short");
/*     */     }
/* 240 */     localStringBuilder.append(".Eras");
/* 241 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   public int getActualMaximum(int paramInt)
/*     */   {
/* 254 */     int i = this.yearOffset;
/*     */ 
/* 257 */     this.yearOffset = 0;
/*     */     try {
/* 259 */       return super.getActualMaximum(paramInt);
/*     */     } finally {
/* 261 */       this.yearOffset = i;
/*     */     }
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 268 */     String str = super.toString();
/*     */ 
/* 270 */     if (!isSet(1)) {
/* 271 */       return str;
/*     */     }
/*     */ 
/* 275 */     int i = str.indexOf("YEAR=");
/*     */ 
/* 278 */     if (i == -1) {
/* 279 */       return str;
/*     */     }
/* 281 */     i += "YEAR=".length();
/* 282 */     StringBuilder localStringBuilder = new StringBuilder(str.substring(0, i));
/*     */ 
/* 284 */     while (Character.isDigit(str.charAt(i++)));
/* 286 */     int j = internalGet(1) + 543;
/* 287 */     localStringBuilder.append(j).append(str.substring(i - 1));
/* 288 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 295 */     paramObjectInputStream.defaultReadObject();
/* 296 */     this.yearOffset = 543;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.util.BuddhistCalendar
 * JD-Core Version:    0.6.2
 */