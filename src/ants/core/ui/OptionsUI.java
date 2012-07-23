/*     */ package ants.core.ui;
/*     */ 
/*     */ import ants.Ant;
/*     */ import ants.core.AntsGameModel;
/*     */ import ants.core.Serialization;
/*     */ import java.awt.Color;
/*     */ import java.awt.Font;
/*     */ import java.awt.datatransfer.DataFlavor;
/*     */ import java.awt.datatransfer.Transferable;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.PrintStream;
/*     */ import java.util.List;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JFileChooser;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JSpinner;
/*     */ import javax.swing.JTextField;
/*     */ import javax.swing.SpinnerNumberModel;
/*     */ import javax.swing.TransferHandler;
/*     */ import javax.swing.TransferHandler.TransferSupport;
/*     */ import javax.swing.filechooser.FileFilter;
/*     */ import javax.tools.JavaCompiler;
/*     */ import javax.tools.ToolProvider;
/*     */ import net.miginfocom.swing.MigLayout;
/*     */ 
/*     */ public class OptionsUI extends JPanel
/*     */ {
/*     */   private static OptionsUI instance;
/*     */   private JTextField fileField;
/*     */   private JSpinner widthSpinner;
/*     */   private JSpinner heightSpinner;
/*     */   private JSpinner numStartingAntsSpinner;
/*     */   private JSpinner numTurnsPerNewAntSpinner;
/*     */   private JButton browseButton;
/*     */   private JButton startButton;
/*     */   private JFileChooser fileChooser;
/* 183 */   private final ActionListener actionListener = new ActionListener()
/*     */   {
/*     */     public void actionPerformed(ActionEvent e) {
/* 186 */       Object src = e.getSource();
/* 187 */       if (src == OptionsUI.this.browseButton)
/* 188 */         OptionsUI.this.showFileChooser();
/* 189 */       else if (src == OptionsUI.this.startButton)
/*     */         try
/*     */         {
/* 192 */           OptionsUI.this.start();
/*     */         } catch (Exception ee) {
/*     */           try {
/* 195 */             ee.printStackTrace(new PrintStream("ERR.txt"));
/*     */           }
/*     */           catch (FileNotFoundException localFileNotFoundException) {
/*     */           }
/* 199 */           throw new RuntimeException(ee);
/*     */         }
/*     */     }
/* 183 */   };
/*     */ 
/*     */   private OptionsUI()
/*     */   {
/*  42 */     initUI();
/*     */ 
/*  44 */     initializeListeners();
/*     */   }
/*     */ 
/*     */   public static OptionsUI get() {
/*  48 */     if (instance == null) {
/*  49 */       instance = new OptionsUI();
/*     */     }
/*  51 */     return instance;
/*     */   }
/*     */ 
/*     */   private void initUI() {
/*  55 */     setLayout(new MigLayout("insets 10, gap 10"));
/*     */ 
/*  57 */     int minSize = 1; int size = 20; int maxSize = 128;
/*     */ 
/*  59 */     this.fileField = new JTextField(32);
/*  60 */     this.browseButton = new JButton("Browse");
/*  61 */     this.widthSpinner = new JSpinner(new SpinnerNumberModel(size, minSize, maxSize, 1));
/*  62 */     this.heightSpinner = new JSpinner(new SpinnerNumberModel(size, minSize, maxSize, 1));
/*  63 */     this.numStartingAntsSpinner = new JSpinner(new SpinnerNumberModel(3, 0, 999, 1));
/*  64 */     this.numTurnsPerNewAntSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 99, 1));
/*  65 */     this.startButton = new JButton("Go!");
/*     */ 
/*  67 */     this.browseButton.setFocusPainted(false);
/*  68 */     this.startButton.setFocusPainted(false);
/*  69 */     this.startButton.setBackground(Color.green);
/*  70 */     this.startButton.setFont(new Font("Arial", 1, 30));
/*  71 */     this.fileField.setTransferHandler(new TransferHandler()
/*     */     {
/*     */       public boolean canImport(TransferHandler.TransferSupport support)
/*     */       {
/*  75 */         return support.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
/*     */       }
/*     */ 
/*     */       public boolean importData(TransferHandler.TransferSupport support)
/*     */       {
/*     */         try
/*     */         {
/*  84 */           List files = (List)support.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
/*  85 */           OptionsUI.this.fileField.setText(((File)files.get(0)).toString());
/*  86 */           return true; } catch (Exception e) {
/*     */         }
/*  88 */         throw new RuntimeException(e);
/*     */       }
/*     */     });
/*  93 */     add(new JLabel("Ant Implementation:"), "");
/*  94 */     add(this.fileField, "");
/*  95 */     add(this.browseButton, "wrap");
/*     */ 
/* 102 */     add(new JLabel("You can drag and drop your .java or .class file into the implementation field."), "span, wrap");
/* 103 */     add(this.startButton, "wrap");
/*     */   }
/*     */ 
/*     */   private void showFileChooser()
/*     */   {
/* 109 */     if (this.fileChooser == null) {
/* 110 */       this.fileChooser = new JFileChooser();
/* 111 */       this.fileChooser.setFileSelectionMode(0);
/* 112 */       this.fileChooser.setFileFilter(new FileFilter()
/*     */       {
/*     */         public boolean accept(File f) {
/* 115 */           return (f.isDirectory()) || (f.getName().endsWith(".java")) || (f.getName().endsWith(".class"));
/*     */         }
/*     */ 
/*     */         public String getDescription()
/*     */         {
/* 120 */           return "JAVA or CLASS file";
/*     */         }
/*     */       });
/*     */     }
/* 125 */     int i = this.fileChooser.showOpenDialog(this);
/* 126 */     if (i != 0) {
/* 127 */       return;
/*     */     }
/*     */ 
/* 130 */     this.fileField.setText(this.fileChooser.getSelectedFile().toString());
/*     */   }
/*     */ 
/*     */   private void start()
/*     */   {
/* 135 */     String filePath = this.fileField.getText().toLowerCase();
/*     */ 
/* 137 */     if (filePath.isEmpty()) {
/* 138 */       JOptionPane.showMessageDialog(this, "You must select your Ant implementation!");
/* 139 */       return;
/*     */     }
/* 141 */     if ((!filePath.endsWith(".java")) && (!filePath.endsWith(".class"))) {
/* 142 */       JOptionPane.showMessageDialog(this, "You must choose a JAVA or CLASS file as your Ant implementation!");
/* 143 */       return;
/*     */     }
/*     */ 
/* 146 */     if (filePath.endsWith(".java")) {
/* 147 */       System.out.println("Compiling java file....");
/* 148 */       JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
/*     */ 
/* 150 */       if (compiler == null) {
/* 151 */         JOptionPane.showMessageDialog(null, "No compiler was found -- try using a .class file instead.");
/* 152 */         return;
/*     */       }
/*     */ 
/* 155 */       int i = compiler.run(null, System.out, System.err, new String[] { filePath });
/* 156 */       if (i != 0) {
/* 157 */         JOptionPane.showMessageDialog(this, "Problem Compiling");
/* 158 */         return;
/*     */       }
/*     */ 
/* 161 */       filePath = filePath.substring(0, filePath.length() - 4) + "class";
/*     */     }
/*     */ 
/* 165 */     File file = new File(this.fileField.getText());
/* 166 */     if (!file.exists()) {
/* 167 */       throw new RuntimeException("Could not find the class file -- " + file);
/*     */     }
/*     */ 
/* 170 */     Class c = Serialization.loadClass(file);
/*     */ 
/* 172 */     if (!Ant.class.isAssignableFrom(c)) {
/* 173 */       JOptionPane.showMessageDialog(this, "Your implementation must implement Ant.java!");
/* 174 */       return;
/*     */     }
/*     */ 
/* 177 */     AntsGameModel model = new AntsGameModel(c, ((Integer)this.widthSpinner.getValue()).intValue(), 
/* 178 */       ((Integer)this.heightSpinner
/* 178 */       .getValue()).intValue(), ((Integer)this.numStartingAntsSpinner.getValue()).intValue(), ((Integer)this.numTurnsPerNewAntSpinner.getValue()).intValue());
/*     */ 
/* 180 */     Ants.get().setContentPane(new AntsView(model));
/*     */   }
/*     */ 
/*     */   private void initializeListeners()
/*     */   {
/* 206 */     this.browseButton.addActionListener(this.actionListener);
/* 207 */     this.startButton.addActionListener(this.actionListener);
/*     */   }
/*     */ }

/* Location:           C:\Users\Richard\Projects\Ants\sim-src\
 * Qualified Name:     ants.core.ui.OptionsUI
 * JD-Core Version:    0.6.0
 */