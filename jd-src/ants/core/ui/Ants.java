package ants.core.ui;

import java.awt.Container;
import javax.swing.JFrame;

public class Ants extends JFrame
{
  private static Ants instance = null;
  private OptionsUI optionsUi = null;

  public static Ants get() {
      return get(null);
  }

  public static Ants get(String file) {
    if (instance == null) {
      instance = new Ants(file);
    }
    return instance;
  }

  public Ants(String f) {
    super("Ants!");

    setDefaultCloseOperation(3);
    optionsUi = OptionsUI.get();
    setContentPane(optionsUi);
    pack();
    setResizable(false);
    setLocationRelativeTo(null);
    setVisible(true);
    if (f != null) {
        optionsUi.setClass(f);
    }
  }

  public void setContentPane(Container contentPane)
  {
    super.setContentPane(contentPane);
    invalidate();
    validate();
    pack();
    setLocationRelativeTo(null);
  }

  public static void main(String[] args) throws Exception {
      String clazzPath = null;
      if (args.length != 0) {
          clazzPath = args[0];
      }
      get(clazzPath);
  }
}

/* Location:           C:\Users\Richard\Projects\Ants\sim-src\
 * Qualified Name:     ants.core.ui.Ants
 * JD-Core Version:    0.6.0
 */
