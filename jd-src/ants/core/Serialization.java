/*    */ package ants.core;
/*    */ 
/*    */ import com.thoughtworks.xstream.XStream;
/*    */ import com.thoughtworks.xstream.mapper.Mapper;
/*    */ import com.thoughtworks.xstream.mapper.MapperWrapper;
/*    */ import java.io.BufferedReader;
/*    */ import java.io.File;
/*    */ import java.io.FileReader;
/*    */ import java.net.URI;
/*    */ import java.net.URL;
/*    */ import java.net.URLClassLoader;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ public class Serialization
/*    */ {
/*    */   private static final XStream xstream;
/* 17 */   private static Map<String, Class<?>> classMap = new HashMap();
/*    */ 
/*    */   static {
/* 20 */     xstream = new XStream()
/*    */     {
/*    */       protected MapperWrapper wrapMapper(MapperWrapper next) {
/* 23 */         return new MapperWrapper(next)
/*    */         {
/*    */           public Class realClass(String elementName)
/*    */           {
/* 27 */             Class c = (Class)Serialization.classMap.get(elementName);
/* 28 */             if (c != null) {
/* 29 */               return c;
/*    */             }
/* 31 */             return super.realClass(elementName);
/*    */           } } ;
/*    */       }
/*    */     };
/*    */   }
/*    */ 
/*    */   public static Class<?> loadClass(File file) {
/* 40 */     String packageName = null;
/*    */     try
/*    */     {
/* 43 */       BufferedReader reader = new BufferedReader(new FileReader(file));
/* 44 */       packageName = reader.readLine();
/* 45 */       if (!packageName.contains("package"))
/* 46 */         packageName = null;
/*    */       else
/* 48 */         packageName = packageName.substring("package ".length(), packageName.length() - 1);
/*    */     }
/*    */     catch (Exception e) {
/* 51 */       packageName = null;
/*    */     }
/*    */     try
/*    */     {
/* 55 */       URL url = file.getParentFile().toURI().toURL();
/* 56 */       URLClassLoader loader = URLClassLoader.newInstance(new URL[] { url }, ClassLoader.getSystemClassLoader());
/* 57 */       String fileName = file.getName();
/* 58 */       String name = fileName.substring(0, fileName.lastIndexOf('.'));
/* 59 */       if (packageName != null) {
/* 60 */         name = packageName + '.' + name;
/*    */       }
/* 62 */       Class ret = Class.forName(name, true, loader);
/* 63 */       classMap.put(name, ret);
/* 64 */       return ret; } catch (Exception e) {
/*    */     }
/* 66 */     throw new RuntimeException(e);
/*    */   }
/*    */ 
/*    */   public static <T> T clone(T object)
/*    */   {
/* 72 */     String xml = xstream.toXML(object);
/* 73 */     return xstream.fromXML(xml);
/*    */   }
/*    */ }

/* Location:           C:\Users\Richard\Projects\Ants\sim-src\
 * Qualified Name:     ants.core.Serialization
 * JD-Core Version:    0.6.0
 */