package javax.sound.sampled.spi;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.UnsupportedAudioFileException;

public abstract class AudioFileReader
{
  public abstract AudioFileFormat getAudioFileFormat(InputStream paramInputStream)
    throws UnsupportedAudioFileException, IOException;

  public abstract AudioFileFormat getAudioFileFormat(URL paramURL)
    throws UnsupportedAudioFileException, IOException;

  public abstract AudioFileFormat getAudioFileFormat(File paramFile)
    throws UnsupportedAudioFileException, IOException;

  public abstract AudioInputStream getAudioInputStream(InputStream paramInputStream)
    throws UnsupportedAudioFileException, IOException;

  public abstract AudioInputStream getAudioInputStream(URL paramURL)
    throws UnsupportedAudioFileException, IOException;

  public abstract AudioInputStream getAudioInputStream(File paramFile)
    throws UnsupportedAudioFileException, IOException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.sound.sampled.spi.AudioFileReader
 * JD-Core Version:    0.6.2
 */