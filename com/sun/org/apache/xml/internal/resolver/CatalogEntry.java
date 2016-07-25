/*     */ package com.sun.org.apache.xml.internal.resolver;
/*     */ 
/*     */ import java.util.Hashtable;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class CatalogEntry
/*     */ {
/*  56 */   protected static int nextEntry = 0;
/*     */ 
/*  63 */   protected static Hashtable entryTypes = new Hashtable();
/*     */ 
/*  67 */   protected static Vector entryArgs = new Vector();
/*     */ 
/* 142 */   protected int entryType = 0;
/*     */ 
/* 145 */   protected Vector args = null;
/*     */ 
/*     */   public static int addEntryType(String name, int numArgs)
/*     */   {
/*  81 */     entryTypes.put(name, new Integer(nextEntry));
/*  82 */     entryArgs.add(nextEntry, new Integer(numArgs));
/*  83 */     nextEntry += 1;
/*     */ 
/*  85 */     return nextEntry - 1;
/*     */   }
/*     */ 
/*     */   public static int getEntryType(String name)
/*     */     throws CatalogException
/*     */   {
/*  98 */     if (!entryTypes.containsKey(name)) {
/*  99 */       throw new CatalogException(3);
/*     */     }
/*     */ 
/* 102 */     Integer iType = (Integer)entryTypes.get(name);
/*     */ 
/* 104 */     if (iType == null) {
/* 105 */       throw new CatalogException(3);
/*     */     }
/*     */ 
/* 108 */     return iType.intValue();
/*     */   }
/*     */ 
/*     */   public static int getEntryArgCount(String name)
/*     */     throws CatalogException
/*     */   {
/* 121 */     return getEntryArgCount(getEntryType(name));
/*     */   }
/*     */ 
/*     */   public static int getEntryArgCount(int type)
/*     */     throws CatalogException
/*     */   {
/*     */     try
/*     */     {
/* 134 */       Integer iArgs = (Integer)entryArgs.get(type);
/* 135 */       return iArgs.intValue(); } catch (ArrayIndexOutOfBoundsException e) {
/*     */     }
/* 137 */     throw new CatalogException(3);
/*     */   }
/*     */ 
/*     */   public CatalogEntry()
/*     */   {
/*     */   }
/*     */ 
/*     */   public CatalogEntry(String name, Vector args)
/*     */     throws CatalogException
/*     */   {
/* 164 */     Integer iType = (Integer)entryTypes.get(name);
/*     */ 
/* 166 */     if (iType == null) {
/* 167 */       throw new CatalogException(3);
/*     */     }
/*     */ 
/* 170 */     int type = iType.intValue();
/*     */     try
/*     */     {
/* 173 */       Integer iArgs = (Integer)entryArgs.get(type);
/* 174 */       if (iArgs.intValue() != args.size())
/* 175 */         throw new CatalogException(2);
/*     */     }
/*     */     catch (ArrayIndexOutOfBoundsException e) {
/* 178 */       throw new CatalogException(3);
/*     */     }
/*     */ 
/* 181 */     this.entryType = type;
/* 182 */     this.args = args;
/*     */   }
/*     */ 
/*     */   public CatalogEntry(int type, Vector args)
/*     */     throws CatalogException
/*     */   {
/*     */     try
/*     */     {
/* 198 */       Integer iArgs = (Integer)entryArgs.get(type);
/* 199 */       if (iArgs.intValue() != args.size())
/* 200 */         throw new CatalogException(2);
/*     */     }
/*     */     catch (ArrayIndexOutOfBoundsException e) {
/* 203 */       throw new CatalogException(3);
/*     */     }
/*     */ 
/* 206 */     this.entryType = type;
/* 207 */     this.args = args;
/*     */   }
/*     */ 
/*     */   public int getEntryType()
/*     */   {
/* 216 */     return this.entryType;
/*     */   }
/*     */ 
/*     */   public String getEntryArg(int argNum)
/*     */   {
/*     */     try
/*     */     {
/* 228 */       return (String)this.args.get(argNum);
/*     */     } catch (ArrayIndexOutOfBoundsException e) {
/*     */     }
/* 231 */     return null;
/*     */   }
/*     */ 
/*     */   public void setEntryArg(int argNum, String newspec)
/*     */     throws ArrayIndexOutOfBoundsException
/*     */   {
/* 250 */     this.args.set(argNum, newspec);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.resolver.CatalogEntry
 * JD-Core Version:    0.6.2
 */