package javax.swing.filechooser;

import java.io.File;

public abstract class FileFilter
{
  public abstract boolean accept(File paramFile);

  public abstract String getDescription();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.filechooser.FileFilter
 * JD-Core Version:    0.6.2
 */