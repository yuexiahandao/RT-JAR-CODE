/*     */ package com.sun.corba.se.spi.orb;
/*     */ 
/*     */ import java.util.AbstractMap;
/*     */ import java.util.AbstractSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ 
/*     */ public abstract class ParserImplTableBase extends ParserImplBase
/*     */ {
/*     */   private final ParserData[] entries;
/*     */ 
/*     */   public ParserImplTableBase(ParserData[] paramArrayOfParserData)
/*     */   {
/*  46 */     this.entries = paramArrayOfParserData;
/*  47 */     setDefaultValues();
/*     */   }
/*     */ 
/*     */   protected PropertyParser makeParser()
/*     */   {
/*  52 */     PropertyParser localPropertyParser = new PropertyParser();
/*  53 */     for (int i = 0; i < this.entries.length; i++) {
/*  54 */       ParserData localParserData = this.entries[i];
/*  55 */       localParserData.addToParser(localPropertyParser);
/*     */     }
/*     */ 
/*  58 */     return localPropertyParser;
/*     */   }
/*     */ 
/*     */   protected void setDefaultValues()
/*     */   {
/* 164 */     FieldMap localFieldMap = new FieldMap(this.entries, true);
/* 165 */     setFields(localFieldMap);
/*     */   }
/*     */ 
/*     */   public void setTestValues()
/*     */   {
/* 170 */     FieldMap localFieldMap = new FieldMap(this.entries, false);
/* 171 */     setFields(localFieldMap);
/*     */   }
/*     */ 
/*     */   private static class FieldMap extends AbstractMap
/*     */   {
/*     */     private final ParserData[] entries;
/*     */     private final boolean useDefault;
/*     */ 
/*     */     public FieldMap(ParserData[] paramArrayOfParserData, boolean paramBoolean)
/*     */     {
/* 117 */       this.entries = paramArrayOfParserData;
/* 118 */       this.useDefault = paramBoolean;
/*     */     }
/*     */ 
/*     */     public Set entrySet()
/*     */     {
/* 123 */       return new AbstractSet()
/*     */       {
/*     */         public Iterator iterator()
/*     */         {
/* 127 */           return new Iterator()
/*     */           {
/* 129 */             int ctr = 0;
/*     */ 
/*     */             public boolean hasNext()
/*     */             {
/* 133 */               return this.ctr < ParserImplTableBase.FieldMap.this.entries.length;
/*     */             }
/*     */ 
/*     */             public Object next()
/*     */             {
/* 138 */               ParserData localParserData = ParserImplTableBase.FieldMap.this.entries[(this.ctr++)];
/* 139 */               ParserImplTableBase.MapEntry localMapEntry = new ParserImplTableBase.MapEntry(localParserData.getFieldName());
/* 140 */               if (ParserImplTableBase.FieldMap.this.useDefault)
/* 141 */                 localMapEntry.setValue(localParserData.getDefaultValue());
/*     */               else
/* 143 */                 localMapEntry.setValue(localParserData.getTestValue());
/* 144 */               return localMapEntry;
/*     */             }
/*     */ 
/*     */             public void remove()
/*     */             {
/* 149 */               throw new UnsupportedOperationException();
/*     */             }
/*     */           };
/*     */         }
/*     */ 
/*     */         public int size()
/*     */         {
/* 156 */           return ParserImplTableBase.FieldMap.this.entries.length;
/*     */         }
/*     */       };
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final class MapEntry
/*     */     implements Map.Entry
/*     */   {
/*     */     private Object key;
/*     */     private Object value;
/*     */ 
/*     */     public MapEntry(Object paramObject)
/*     */     {
/*  67 */       this.key = paramObject;
/*     */     }
/*     */ 
/*     */     public Object getKey()
/*     */     {
/*  72 */       return this.key;
/*     */     }
/*     */ 
/*     */     public Object getValue()
/*     */     {
/*  77 */       return this.value;
/*     */     }
/*     */ 
/*     */     public Object setValue(Object paramObject)
/*     */     {
/*  82 */       Object localObject = this.value;
/*  83 */       this.value = paramObject;
/*  84 */       return localObject;
/*     */     }
/*     */ 
/*     */     public boolean equals(Object paramObject)
/*     */     {
/*  89 */       if (!(paramObject instanceof MapEntry)) {
/*  90 */         return false;
/*     */       }
/*  92 */       MapEntry localMapEntry = (MapEntry)paramObject;
/*     */ 
/*  94 */       return (this.key.equals(localMapEntry.key)) && (this.value.equals(localMapEntry.value));
/*     */     }
/*     */ 
/*     */     public int hashCode()
/*     */     {
/* 100 */       return this.key.hashCode() ^ this.value.hashCode();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.orb.ParserImplTableBase
 * JD-Core Version:    0.6.2
 */