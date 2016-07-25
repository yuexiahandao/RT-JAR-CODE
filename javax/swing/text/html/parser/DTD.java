/*     */ package javax.swing.text.html.parser;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ import java.util.BitSet;
/*     */ import java.util.Hashtable;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.Vector;
/*     */ import sun.awt.AppContext;
/*     */ 
/*     */ public class DTD
/*     */   implements DTDConstants
/*     */ {
/*     */   public String name;
/*  61 */   public Vector<Element> elements = new Vector();
/*  62 */   public Hashtable<String, Element> elementHash = new Hashtable();
/*     */ 
/*  64 */   public Hashtable<Object, Entity> entityHash = new Hashtable();
/*     */ 
/*  66 */   public final Element pcdata = getElement("#pcdata");
/*  67 */   public final Element html = getElement("html");
/*  68 */   public final Element meta = getElement("meta");
/*  69 */   public final Element base = getElement("base");
/*  70 */   public final Element isindex = getElement("isindex");
/*  71 */   public final Element head = getElement("head");
/*  72 */   public final Element body = getElement("body");
/*  73 */   public final Element applet = getElement("applet");
/*  74 */   public final Element param = getElement("param");
/*  75 */   public final Element p = getElement("p");
/*  76 */   public final Element title = getElement("title");
/*  77 */   final Element style = getElement("style");
/*  78 */   final Element link = getElement("link");
/*  79 */   final Element script = getElement("script");
/*     */   public static final int FILE_VERSION = 1;
/* 321 */   private static final Object DTD_HASH_KEY = new Object();
/*     */ 
/*     */   protected DTD(String paramString)
/*     */   {
/*  88 */     this.name = paramString;
/*  89 */     defEntity("#RE", 65536, 13);
/*  90 */     defEntity("#RS", 65536, 10);
/*  91 */     defEntity("#SPACE", 65536, 32);
/*  92 */     defineElement("unknown", 17, false, true, null, null, null, null);
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 100 */     return this.name;
/*     */   }
/*     */ 
/*     */   public Entity getEntity(String paramString)
/*     */   {
/* 109 */     return (Entity)this.entityHash.get(paramString);
/*     */   }
/*     */ 
/*     */   public Entity getEntity(int paramInt)
/*     */   {
/* 118 */     return (Entity)this.entityHash.get(Integer.valueOf(paramInt));
/*     */   }
/*     */ 
/*     */   boolean elementExists(String paramString)
/*     */   {
/* 130 */     return (!"unknown".equals(paramString)) && (this.elementHash.get(paramString) != null);
/*     */   }
/*     */ 
/*     */   public Element getElement(String paramString)
/*     */   {
/* 142 */     Element localElement = (Element)this.elementHash.get(paramString);
/* 143 */     if (localElement == null) {
/* 144 */       localElement = new Element(paramString, this.elements.size());
/* 145 */       this.elements.addElement(localElement);
/* 146 */       this.elementHash.put(paramString, localElement);
/*     */     }
/* 148 */     return localElement;
/*     */   }
/*     */ 
/*     */   public Element getElement(int paramInt)
/*     */   {
/* 159 */     return (Element)this.elements.elementAt(paramInt);
/*     */   }
/*     */ 
/*     */   public Entity defineEntity(String paramString, int paramInt, char[] paramArrayOfChar)
/*     */   {
/* 175 */     Entity localEntity = (Entity)this.entityHash.get(paramString);
/* 176 */     if (localEntity == null) {
/* 177 */       localEntity = new Entity(paramString, paramInt, paramArrayOfChar);
/* 178 */       this.entityHash.put(paramString, localEntity);
/* 179 */       if (((paramInt & 0x10000) != 0) && (paramArrayOfChar.length == 1)) {
/* 180 */         switch (paramInt & 0xFFFEFFFF) {
/*     */         case 1:
/*     */         case 11:
/* 183 */           this.entityHash.put(Integer.valueOf(paramArrayOfChar[0]), localEntity);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 188 */     return localEntity;
/*     */   }
/*     */ 
/*     */   public Element defineElement(String paramString, int paramInt, boolean paramBoolean1, boolean paramBoolean2, ContentModel paramContentModel, BitSet paramBitSet1, BitSet paramBitSet2, AttributeList paramAttributeList)
/*     */   {
/* 208 */     Element localElement = getElement(paramString);
/* 209 */     localElement.type = paramInt;
/* 210 */     localElement.oStart = paramBoolean1;
/* 211 */     localElement.oEnd = paramBoolean2;
/* 212 */     localElement.content = paramContentModel;
/* 213 */     localElement.exclusions = paramBitSet1;
/* 214 */     localElement.inclusions = paramBitSet2;
/* 215 */     localElement.atts = paramAttributeList;
/* 216 */     return localElement;
/*     */   }
/*     */ 
/*     */   public void defineAttributes(String paramString, AttributeList paramAttributeList)
/*     */   {
/* 227 */     Element localElement = getElement(paramString);
/* 228 */     localElement.atts = paramAttributeList;
/*     */   }
/*     */ 
/*     */   public Entity defEntity(String paramString, int paramInt1, int paramInt2)
/*     */   {
/* 237 */     char[] arrayOfChar = { (char)paramInt2 };
/* 238 */     return defineEntity(paramString, paramInt1, arrayOfChar);
/*     */   }
/*     */ 
/*     */   protected Entity defEntity(String paramString1, int paramInt, String paramString2)
/*     */   {
/* 247 */     int i = paramString2.length();
/* 248 */     char[] arrayOfChar = new char[i];
/* 249 */     paramString2.getChars(0, i, arrayOfChar, 0);
/* 250 */     return defineEntity(paramString1, paramInt, arrayOfChar);
/*     */   }
/*     */ 
/*     */   protected Element defElement(String paramString, int paramInt, boolean paramBoolean1, boolean paramBoolean2, ContentModel paramContentModel, String[] paramArrayOfString1, String[] paramArrayOfString2, AttributeList paramAttributeList)
/*     */   {
/* 261 */     BitSet localBitSet = null;
/* 262 */     if ((paramArrayOfString1 != null) && (paramArrayOfString1.length > 0)) {
/* 263 */       localBitSet = new BitSet();
/* 264 */       for (String str1 : paramArrayOfString1) {
/* 265 */         if (str1.length() > 0) {
/* 266 */           localBitSet.set(getElement(str1).getIndex());
/*     */         }
/*     */       }
/*     */     }
/* 270 */     ??? = null;
/* 271 */     if ((paramArrayOfString2 != null) && (paramArrayOfString2.length > 0)) {
/* 272 */       ??? = new BitSet();
/* 273 */       for (String str2 : paramArrayOfString2) {
/* 274 */         if (str2.length() > 0) {
/* 275 */           ((BitSet)???).set(getElement(str2).getIndex());
/*     */         }
/*     */       }
/*     */     }
/* 279 */     return defineElement(paramString, paramInt, paramBoolean1, paramBoolean2, paramContentModel, localBitSet, (BitSet)???, paramAttributeList);
/*     */   }
/*     */ 
/*     */   protected AttributeList defAttributeList(String paramString1, int paramInt1, int paramInt2, String paramString2, String paramString3, AttributeList paramAttributeList)
/*     */   {
/* 288 */     Vector localVector = null;
/*     */     StringTokenizer localStringTokenizer;
/* 289 */     if (paramString3 != null) {
/* 290 */       localVector = new Vector();
/* 291 */       for (localStringTokenizer = new StringTokenizer(paramString3, "|"); localStringTokenizer.hasMoreTokens(); ) {
/* 292 */         String str = localStringTokenizer.nextToken();
/* 293 */         if (str.length() > 0) {
/* 294 */           localVector.addElement(str);
/*     */         }
/*     */       }
/*     */     }
/* 298 */     return new AttributeList(paramString1, paramInt1, paramInt2, paramString2, localVector, paramAttributeList);
/*     */   }
/*     */ 
/*     */   protected ContentModel defContentModel(int paramInt, Object paramObject, ContentModel paramContentModel)
/*     */   {
/* 307 */     return new ContentModel(paramInt, paramObject, paramContentModel);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 315 */     return this.name;
/*     */   }
/*     */ 
/*     */   public static void putDTDHash(String paramString, DTD paramDTD)
/*     */   {
/* 324 */     getDtdHash().put(paramString, paramDTD);
/*     */   }
/*     */ 
/*     */   public static DTD getDTD(String paramString)
/*     */     throws IOException
/*     */   {
/* 337 */     paramString = paramString.toLowerCase();
/* 338 */     DTD localDTD = (DTD)getDtdHash().get(paramString);
/* 339 */     if (localDTD == null) {
/* 340 */       localDTD = new DTD(paramString);
/*     */     }
/* 342 */     return localDTD;
/*     */   }
/*     */ 
/*     */   private static Hashtable<String, DTD> getDtdHash() {
/* 346 */     AppContext localAppContext = AppContext.getAppContext();
/*     */ 
/* 348 */     Hashtable localHashtable = (Hashtable)localAppContext.get(DTD_HASH_KEY);
/*     */ 
/* 350 */     if (localHashtable == null) {
/* 351 */       localHashtable = new Hashtable();
/*     */ 
/* 353 */       localAppContext.put(DTD_HASH_KEY, localHashtable);
/*     */     }
/*     */ 
/* 356 */     return localHashtable;
/*     */   }
/*     */ 
/*     */   public void read(DataInputStream paramDataInputStream)
/*     */     throws IOException
/*     */   {
/* 364 */     if (paramDataInputStream.readInt() != 1);
/* 370 */     String[] arrayOfString1 = new String[paramDataInputStream.readShort()];
/* 371 */     for (int i = 0; i < arrayOfString1.length; i++) {
/* 372 */       arrayOfString1[i] = paramDataInputStream.readUTF();
/*     */     }
/*     */ 
/* 379 */     i = paramDataInputStream.readShort();
/*     */     int k;
/*     */     int m;
/* 380 */     for (int j = 0; j < i; j++) {
/* 381 */       k = paramDataInputStream.readShort();
/* 382 */       m = paramDataInputStream.readByte();
/* 383 */       String str = paramDataInputStream.readUTF();
/* 384 */       defEntity(arrayOfString1[k], m | 0x10000, str);
/*     */     }
/*     */ 
/* 389 */     i = paramDataInputStream.readShort();
/* 390 */     for (j = 0; j < i; j++) {
/* 391 */       k = paramDataInputStream.readShort();
/* 392 */       m = paramDataInputStream.readByte();
/* 393 */       int n = paramDataInputStream.readByte();
/* 394 */       ContentModel localContentModel = readContentModel(paramDataInputStream, arrayOfString1);
/* 395 */       String[] arrayOfString2 = readNameArray(paramDataInputStream, arrayOfString1);
/* 396 */       String[] arrayOfString3 = readNameArray(paramDataInputStream, arrayOfString1);
/* 397 */       AttributeList localAttributeList = readAttributeList(paramDataInputStream, arrayOfString1);
/* 398 */       defElement(arrayOfString1[k], m, (n & 0x1) != 0, (n & 0x2) != 0, localContentModel, arrayOfString2, arrayOfString3, localAttributeList);
/*     */     }
/*     */   }
/*     */ 
/*     */   private ContentModel readContentModel(DataInputStream paramDataInputStream, String[] paramArrayOfString)
/*     */     throws IOException
/*     */   {
/* 406 */     int i = paramDataInputStream.readByte();
/*     */     int j;
/*     */     Object localObject;
/*     */     ContentModel localContentModel;
/* 407 */     switch (i) {
/*     */     case 0:
/* 409 */       return null;
/*     */     case 1:
/* 411 */       j = paramDataInputStream.readByte();
/* 412 */       localObject = readContentModel(paramDataInputStream, paramArrayOfString);
/* 413 */       localContentModel = readContentModel(paramDataInputStream, paramArrayOfString);
/* 414 */       return defContentModel(j, localObject, localContentModel);
/*     */     case 2:
/* 417 */       j = paramDataInputStream.readByte();
/* 418 */       localObject = getElement(paramArrayOfString[paramDataInputStream.readShort()]);
/* 419 */       localContentModel = readContentModel(paramDataInputStream, paramArrayOfString);
/* 420 */       return defContentModel(j, localObject, localContentModel);
/*     */     }
/*     */ 
/* 423 */     throw new IOException("bad bdtd");
/*     */   }
/*     */ 
/*     */   private String[] readNameArray(DataInputStream paramDataInputStream, String[] paramArrayOfString)
/*     */     throws IOException
/*     */   {
/* 429 */     int i = paramDataInputStream.readShort();
/* 430 */     if (i == 0) {
/* 431 */       return null;
/*     */     }
/* 433 */     String[] arrayOfString = new String[i];
/* 434 */     for (int j = 0; j < i; j++) {
/* 435 */       arrayOfString[j] = paramArrayOfString[paramDataInputStream.readShort()];
/*     */     }
/* 437 */     return arrayOfString;
/*     */   }
/*     */ 
/*     */   private AttributeList readAttributeList(DataInputStream paramDataInputStream, String[] paramArrayOfString)
/*     */     throws IOException
/*     */   {
/* 443 */     AttributeList localAttributeList = null;
/* 444 */     for (int i = paramDataInputStream.readByte(); i > 0; i--) {
/* 445 */       int j = paramDataInputStream.readShort();
/* 446 */       int k = paramDataInputStream.readByte();
/* 447 */       int m = paramDataInputStream.readByte();
/* 448 */       int n = paramDataInputStream.readShort();
/* 449 */       String str = n == -1 ? null : paramArrayOfString[n];
/* 450 */       Vector localVector = null;
/* 451 */       int i1 = paramDataInputStream.readShort();
/* 452 */       if (i1 > 0) {
/* 453 */         localVector = new Vector(i1);
/* 454 */         for (int i2 = 0; i2 < i1; i2++) {
/* 455 */           localVector.addElement(paramArrayOfString[paramDataInputStream.readShort()]);
/*     */         }
/*     */       }
/* 458 */       localAttributeList = new AttributeList(paramArrayOfString[j], k, m, str, localVector, localAttributeList);
/*     */     }
/*     */ 
/* 463 */     return localAttributeList;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.html.parser.DTD
 * JD-Core Version:    0.6.2
 */