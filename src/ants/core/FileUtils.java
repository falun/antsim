/*     */ package ants.core;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ 
/*     */ public class FileUtils
/*     */ {
/*     */   public static byte[] read(File file)
/*     */   {
/*     */     try
/*     */     {
/*  16 */       FileInputStream fileInputStream = new FileInputStream(file);
/*  17 */       byte[] ret = read(fileInputStream);
/*  18 */       fileInputStream.close();
/*  19 */       return ret; } catch (Exception e) {
/*     */     }
/*  21 */     throw new RuntimeException(e);
/*     */   }
/*     */ 
/*     */   public static byte[] read(InputStream is)
/*     */   {
/*  26 */     ByteArrayOutputStream bos = new ByteArrayOutputStream();
/*     */ 
/*  28 */     transfer(is, bos);
/*     */ 
/*  30 */     return bos.toByteArray();
/*     */   }
/*     */ 
/*     */   public static void transfer(InputStream is, File targetFile, byte[] buf) {
/*     */     try {
/*  35 */       FileOutputStream fos = new FileOutputStream(targetFile);
/*  36 */       transfer(is, fos, buf);
/*  37 */       fos.close();
/*     */     } catch (IOException e) {
/*  39 */       throw new RuntimeException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void transfer(File subFile, OutputStream outputStream, byte[] buf) {
/*     */     try {
/*  45 */       transfer(new FileInputStream(subFile), outputStream, buf);
/*     */     } catch (FileNotFoundException e) {
/*  47 */       throw new RuntimeException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static void transfer(InputStream is, OutputStream os) {
/*  52 */     transfer(is, os, new byte[1024]);
/*     */   }
/*     */ 
/*     */   private static void transfer(InputStream is, OutputStream os, byte[] buf)
/*     */   {
/*     */     try {
/*     */       while (true) {
/*  59 */         int read = is.read(buf);
/*  60 */         if (read < 0) {
/*     */           break;
/*     */         }
/*  63 */         os.write(buf, 0, read);
/*     */       }
/*     */     } catch (Exception e) {
/*  66 */       throw new RuntimeException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void delete(File file) {
/*  71 */     if (file == null) {
/*  72 */       throw new NullArgumentException("file");
/*     */     }
/*  74 */     if (!file.exists()) {
/*  75 */       throw new IllegalArgumentException("File does not exist: " + file);
/*     */     }
/*  77 */     if (file.isDirectory()) {
/*  78 */       throw new IllegalArgumentException("Use deleteDirectory() to delete a directory. " + file);
/*     */     }
/*  80 */     if (!file.delete())
/*  81 */       throw new RuntimeException("Could not delete: " + file);
/*     */   }
/*     */ 
/*     */   public static void deleteDirectory(File file)
/*     */   {
/*  86 */     if (file == null) {
/*  87 */       throw new NullArgumentException("file");
/*     */     }
/*  89 */     if (!file.exists()) {
/*  90 */       throw new IllegalArgumentException("Directory does not exist: " + file);
/*     */     }
/*  92 */     if (!file.isDirectory()) {
/*  93 */       throw new IllegalArgumentException("Not a directory.");
/*     */     }
/*  95 */     recursiveDelete(file);
/*     */   }
/*     */ 
/*     */   private static void recursiveDelete(File file) {
/*  99 */     for (File child : file.listFiles()) {
/* 100 */       if (child.isDirectory()) {
/* 101 */         recursiveDelete(child);
/*     */       }
/* 103 */       else if (!child.delete()) {
/* 104 */         throw new RuntimeException("Could not delete: " + child);
/*     */       }
/*     */     }
/*     */ 
/* 108 */     if (!file.delete())
/* 109 */       throw new RuntimeException("Could not delete: " + file);
/*     */   }
/*     */ 
/*     */   public static void mkdirs(File file)
/*     */   {
/* 114 */     if (!file.mkdirs())
/* 115 */       throw new RuntimeException("Could not mkdirs: " + file);
/*     */   }
/*     */ 
/*     */   // ERROR //
/*     */   public static void copy(File from, File to)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aconst_null
/*     */     //   1: astore_2
/*     */     //   2: aconst_null
/*     */     //   3: astore_3
/*     */     //   4: new 16	java/io/FileInputStream
/*     */     //   7: dup
/*     */     //   8: aload_0
/*     */     //   9: invokespecial 18	java/io/FileInputStream:<init>	(Ljava/io/File;)V
/*     */     //   12: astore_2
/*     */     //   13: new 59	java/io/FileOutputStream
/*     */     //   16: dup
/*     */     //   17: aload_1
/*     */     //   18: invokespecial 61	java/io/FileOutputStream:<init>	(Ljava/io/File;)V
/*     */     //   21: astore_3
/*     */     //   22: sipush 1024
/*     */     //   25: newarray byte
/*     */     //   27: astore 4
/*     */     //   29: aload_2
/*     */     //   30: aload 4
/*     */     //   32: invokevirtual 81	java/io/InputStream:read	([B)I
/*     */     //   35: istore 5
/*     */     //   37: iload 5
/*     */     //   39: iconst_m1
/*     */     //   40: if_icmpne +6 -> 46
/*     */     //   43: goto +63 -> 106
/*     */     //   46: aload_3
/*     */     //   47: aload 4
/*     */     //   49: iconst_0
/*     */     //   50: iload 5
/*     */     //   52: invokevirtual 86	java/io/OutputStream:write	([BII)V
/*     */     //   55: goto -26 -> 29
/*     */     //   58: astore 4
/*     */     //   60: new 27	java/lang/RuntimeException
/*     */     //   63: dup
/*     */     //   64: aload 4
/*     */     //   66: invokespecial 29	java/lang/RuntimeException:<init>	(Ljava/lang/Throwable;)V
/*     */     //   69: athrow
/*     */     //   70: astore 6
/*     */     //   72: aload_2
/*     */     //   73: ifnull +7 -> 80
/*     */     //   76: aload_2
/*     */     //   77: invokevirtual 155	java/io/InputStream:close	()V
/*     */     //   80: aload_3
/*     */     //   81: ifnull +22 -> 103
/*     */     //   84: aload_3
/*     */     //   85: invokevirtual 156	java/io/OutputStream:close	()V
/*     */     //   88: goto +15 -> 103
/*     */     //   91: astore 7
/*     */     //   93: new 27	java/lang/RuntimeException
/*     */     //   96: dup
/*     */     //   97: aload 7
/*     */     //   99: invokespecial 29	java/lang/RuntimeException:<init>	(Ljava/lang/Throwable;)V
/*     */     //   102: athrow
/*     */     //   103: aload 6
/*     */     //   105: athrow
/*     */     //   106: aload_2
/*     */     //   107: ifnull +7 -> 114
/*     */     //   110: aload_2
/*     */     //   111: invokevirtual 155	java/io/InputStream:close	()V
/*     */     //   114: aload_3
/*     */     //   115: ifnull +22 -> 137
/*     */     //   118: aload_3
/*     */     //   119: invokevirtual 156	java/io/OutputStream:close	()V
/*     */     //   122: goto +15 -> 137
/*     */     //   125: astore 7
/*     */     //   127: new 27	java/lang/RuntimeException
/*     */     //   130: dup
/*     */     //   131: aload 7
/*     */     //   133: invokespecial 29	java/lang/RuntimeException:<init>	(Ljava/lang/Throwable;)V
/*     */     //   136: athrow
/*     */     //   137: return
/*     */     //
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   4	58	58	java/lang/Exception
/*     */     //   4	70	70	finally
/*     */     //   72	88	91	java/lang/Exception
/*     */     //   106	122	125	java/lang/Exception
/*     */   }
/*     */ }

/* Location:           C:\Users\Richard\Projects\Ants\sim-src\
 * Qualified Name:     ants.core.FileUtils
 * JD-Core Version:    0.6.0
 */