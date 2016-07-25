/*     */ package com.sun.activation.registries;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.StringReader;
/*     */ import java.util.Hashtable;
/*     */ import java.util.StringTokenizer;
/*     */ 
/*     */ public class MimeTypeFile
/*     */ {
/*  32 */   private String fname = null;
/*  33 */   private Hashtable type_hash = new Hashtable();
/*     */ 
/*     */   public MimeTypeFile(String new_fname)
/*     */     throws IOException
/*     */   {
/*  41 */     File mime_file = null;
/*  42 */     FileReader fr = null;
/*     */ 
/*  44 */     this.fname = new_fname;
/*     */ 
/*  46 */     mime_file = new File(this.fname);
/*     */ 
/*  48 */     fr = new FileReader(mime_file);
/*     */     try
/*     */     {
/*  51 */       parse(new BufferedReader(fr));
/*     */     } finally {
/*     */       try {
/*  54 */         fr.close();
/*     */       }
/*     */       catch (IOException e) {
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public MimeTypeFile(InputStream is) throws IOException {
/*  62 */     parse(new BufferedReader(new InputStreamReader(is, "iso-8859-1")));
/*     */   }
/*     */ 
/*     */   public MimeTypeFile()
/*     */   {
/*     */   }
/*     */ 
/*     */   public MimeTypeEntry getMimeTypeEntry(String file_ext)
/*     */   {
/*  75 */     return (MimeTypeEntry)this.type_hash.get(file_ext);
/*     */   }
/*     */ 
/*     */   public String getMIMETypeString(String file_ext)
/*     */   {
/*  82 */     MimeTypeEntry entry = getMimeTypeEntry(file_ext);
/*     */ 
/*  84 */     if (entry != null) {
/*  85 */       return entry.getMIMEType();
/*     */     }
/*  87 */     return null;
/*     */   }
/*     */ 
/*     */   public void appendToRegistry(String mime_types)
/*     */   {
/*     */     try
/*     */     {
/* 107 */       parse(new BufferedReader(new StringReader(mime_types)));
/*     */     }
/*     */     catch (IOException ex)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   private void parse(BufferedReader buf_reader)
/*     */     throws IOException
/*     */   {
/* 117 */     String line = null; String prev = null;
/*     */ 
/* 119 */     while ((line = buf_reader.readLine()) != null) {
/* 120 */       if (prev == null)
/* 121 */         prev = line;
/*     */       else
/* 123 */         prev = prev + line;
/* 124 */       int end = prev.length();
/* 125 */       if ((prev.length() > 0) && (prev.charAt(end - 1) == '\\')) {
/* 126 */         prev = prev.substring(0, end - 1);
/*     */       }
/*     */       else {
/* 129 */         parseEntry(prev);
/* 130 */         prev = null;
/*     */       }
/*     */     }
/* 132 */     if (prev != null)
/* 133 */       parseEntry(prev);
/*     */   }
/*     */ 
/*     */   private void parseEntry(String line)
/*     */   {
/* 140 */     String mime_type = null;
/* 141 */     String file_ext = null;
/* 142 */     line = line.trim();
/*     */ 
/* 144 */     if (line.length() == 0) {
/* 145 */       return;
/*     */     }
/*     */ 
/* 148 */     if (line.charAt(0) == '#') {
/* 149 */       return;
/*     */     }
/*     */ 
/* 152 */     if (line.indexOf('=') > 0)
/*     */     {
/* 154 */       LineTokenizer lt = new LineTokenizer(line);
/* 155 */       while (lt.hasMoreTokens()) {
/* 156 */         String name = lt.nextToken();
/* 157 */         String value = null;
/* 158 */         if ((lt.hasMoreTokens()) && (lt.nextToken().equals("=")) && (lt.hasMoreTokens()))
/*     */         {
/* 160 */           value = lt.nextToken();
/* 161 */         }if (value == null) {
/* 162 */           if (LogSupport.isLoggable())
/* 163 */             LogSupport.log("Bad .mime.types entry: " + line);
/* 164 */           return;
/*     */         }
/* 166 */         if (name.equals("type")) {
/* 167 */           mime_type = value;
/* 168 */         } else if (name.equals("exts")) {
/* 169 */           StringTokenizer st = new StringTokenizer(value, ",");
/* 170 */           while (st.hasMoreTokens()) {
/* 171 */             file_ext = st.nextToken();
/* 172 */             MimeTypeEntry entry = new MimeTypeEntry(mime_type, file_ext);
/*     */ 
/* 174 */             this.type_hash.put(file_ext, entry);
/* 175 */             if (LogSupport.isLoggable())
/* 176 */               LogSupport.log("Added: " + entry.toString());
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 183 */       StringTokenizer strtok = new StringTokenizer(line);
/* 184 */       int num_tok = strtok.countTokens();
/*     */ 
/* 186 */       if (num_tok == 0) {
/* 187 */         return;
/*     */       }
/* 189 */       mime_type = strtok.nextToken();
/*     */ 
/* 191 */       while (strtok.hasMoreTokens()) {
/* 192 */         MimeTypeEntry entry = null;
/*     */ 
/* 194 */         file_ext = strtok.nextToken();
/* 195 */         entry = new MimeTypeEntry(mime_type, file_ext);
/* 196 */         this.type_hash.put(file_ext, entry);
/* 197 */         if (LogSupport.isLoggable())
/* 198 */           LogSupport.log("Added: " + entry.toString());
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.activation.registries.MimeTypeFile
 * JD-Core Version:    0.6.2
 */