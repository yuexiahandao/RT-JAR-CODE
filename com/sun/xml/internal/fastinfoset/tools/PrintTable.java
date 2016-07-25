/*     */ package com.sun.xml.internal.fastinfoset.tools;
/*     */ 
/*     */ import com.sun.xml.internal.fastinfoset.QualifiedName;
/*     */ import com.sun.xml.internal.fastinfoset.util.CharArrayArray;
/*     */ import com.sun.xml.internal.fastinfoset.util.ContiguousCharArrayArray;
/*     */ import com.sun.xml.internal.fastinfoset.util.PrefixArray;
/*     */ import com.sun.xml.internal.fastinfoset.util.QualifiedNameArray;
/*     */ import com.sun.xml.internal.fastinfoset.util.StringArray;
/*     */ import com.sun.xml.internal.fastinfoset.vocab.ParserVocabulary;
/*     */ import java.io.File;
/*     */ import java.io.PrintStream;
/*     */ import javax.xml.parsers.SAXParser;
/*     */ import javax.xml.parsers.SAXParserFactory;
/*     */ 
/*     */ public class PrintTable
/*     */ {
/*     */   public static void printVocabulary(ParserVocabulary vocabulary)
/*     */   {
/*  51 */     printArray("Attribute Name Table", vocabulary.attributeName);
/*  52 */     printArray("Attribute Value Table", vocabulary.attributeValue);
/*  53 */     printArray("Character Content Chunk Table", vocabulary.characterContentChunk);
/*  54 */     printArray("Element Name Table", vocabulary.elementName);
/*  55 */     printArray("Local Name Table", vocabulary.localName);
/*  56 */     printArray("Namespace Name Table", vocabulary.namespaceName);
/*  57 */     printArray("Other NCName Table", vocabulary.otherNCName);
/*  58 */     printArray("Other String Table", vocabulary.otherString);
/*  59 */     printArray("Other URI Table", vocabulary.otherURI);
/*  60 */     printArray("Prefix Table", vocabulary.prefix);
/*     */   }
/*     */ 
/*     */   public static void printArray(String title, StringArray a) {
/*  64 */     System.out.println(title);
/*     */ 
/*  66 */     for (int i = 0; i < a.getSize(); i++)
/*  67 */       System.out.println("" + (i + 1) + ": " + a.getArray()[i]);
/*     */   }
/*     */ 
/*     */   public static void printArray(String title, PrefixArray a)
/*     */   {
/*  72 */     System.out.println(title);
/*     */ 
/*  74 */     for (int i = 0; i < a.getSize(); i++)
/*  75 */       System.out.println("" + (i + 1) + ": " + a.getArray()[i]);
/*     */   }
/*     */ 
/*     */   public static void printArray(String title, CharArrayArray a)
/*     */   {
/*  80 */     System.out.println(title);
/*     */ 
/*  82 */     for (int i = 0; i < a.getSize(); i++)
/*  83 */       System.out.println("" + (i + 1) + ": " + a.getArray()[i]);
/*     */   }
/*     */ 
/*     */   public static void printArray(String title, ContiguousCharArrayArray a)
/*     */   {
/*  88 */     System.out.println(title);
/*     */ 
/*  90 */     for (int i = 0; i < a.getSize(); i++)
/*  91 */       System.out.println("" + (i + 1) + ": " + a.getString(i));
/*     */   }
/*     */ 
/*     */   public static void printArray(String title, QualifiedNameArray a)
/*     */   {
/*  96 */     System.out.println(title);
/*     */ 
/*  98 */     for (int i = 0; i < a.getSize(); i++) {
/*  99 */       QualifiedName name = a.getArray()[i];
/* 100 */       System.out.println("" + (name.index + 1) + ": " + "{" + name.namespaceName + "}" + name.prefix + ":" + name.localName);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/*     */     try
/*     */     {
/* 111 */       SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
/* 112 */       saxParserFactory.setNamespaceAware(true);
/*     */ 
/* 114 */       SAXParser saxParser = saxParserFactory.newSAXParser();
/*     */ 
/* 116 */       ParserVocabulary referencedVocabulary = new ParserVocabulary();
/*     */ 
/* 118 */       VocabularyGenerator vocabularyGenerator = new VocabularyGenerator(referencedVocabulary);
/* 119 */       File f = new File(args[0]);
/* 120 */       saxParser.parse(f, vocabularyGenerator);
/*     */ 
/* 122 */       printVocabulary(referencedVocabulary);
/*     */     } catch (Exception e) {
/* 124 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.fastinfoset.tools.PrintTable
 * JD-Core Version:    0.6.2
 */