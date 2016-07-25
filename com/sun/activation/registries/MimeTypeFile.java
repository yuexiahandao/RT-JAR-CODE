package com.sun.activation.registries;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.Hashtable;
import java.util.StringTokenizer;

public class MimeTypeFile {
    private String fname = null;
    private Hashtable type_hash = new Hashtable();

    public MimeTypeFile(String new_fname)
            throws IOException {
        File mime_file = null;
        FileReader fr = null;

        this.fname = new_fname;

        mime_file = new File(this.fname);

        fr = new FileReader(mime_file);
        try {
            parse(new BufferedReader(fr));
        } finally {
            try {
                fr.close();
            } catch (IOException e) {
            }
        }
    }

    public MimeTypeFile(InputStream is) throws IOException {
        parse(new BufferedReader(new InputStreamReader(is, "iso-8859-1")));
    }

    public MimeTypeFile() {
    }

    public MimeTypeEntry getMimeTypeEntry(String file_ext) {
        return (MimeTypeEntry) this.type_hash.get(file_ext);
    }

    public String getMIMETypeString(String file_ext) {
        MimeTypeEntry entry = getMimeTypeEntry(file_ext);

        if (entry != null) {
            return entry.getMIMEType();
        }
        return null;
    }

    public void appendToRegistry(String mime_types) {
        try {
            parse(new BufferedReader(new StringReader(mime_types)));
        } catch (IOException ex) {
        }
    }

    private void parse(BufferedReader buf_reader)
            throws IOException {
        String line = null;
        String prev = null;

        while ((line = buf_reader.readLine()) != null) {
            if (prev == null)
                prev = line;
            else
                prev = prev + line;
            int end = prev.length();
            if ((prev.length() > 0) && (prev.charAt(end - 1) == '\\')) {
                prev = prev.substring(0, end - 1);
            } else {
                parseEntry(prev);
                prev = null;
            }
        }
        if (prev != null)
            parseEntry(prev);
    }

    private void parseEntry(String line) {
        String mime_type = null;
        String file_ext = null;
        line = line.trim();

        if (line.length() == 0) {
            return;
        }

        if (line.charAt(0) == '#') {
            return;
        }

        if (line.indexOf('=') > 0) {
            LineTokenizer lt = new LineTokenizer(line);
            while (lt.hasMoreTokens()) {
                String name = lt.nextToken();
                String value = null;
                if ((lt.hasMoreTokens()) && (lt.nextToken().equals("=")) && (lt.hasMoreTokens())) {
                    value = lt.nextToken();
                }
                if (value == null) {
                    if (LogSupport.isLoggable())
                        LogSupport.log("Bad .mime.types entry: " + line);
                    return;
                }
                if (name.equals("type")) {
                    mime_type = value;
                } else if (name.equals("exts")) {
                    StringTokenizer st = new StringTokenizer(value, ",");
                    while (st.hasMoreTokens()) {
                        file_ext = st.nextToken();
                        MimeTypeEntry entry = new MimeTypeEntry(mime_type, file_ext);

                        this.type_hash.put(file_ext, entry);
                        if (LogSupport.isLoggable())
                            LogSupport.log("Added: " + entry.toString());
                    }
                }
            }
        } else {
            StringTokenizer strtok = new StringTokenizer(line);
            int num_tok = strtok.countTokens();

            if (num_tok == 0) {
                return;
            }
            mime_type = strtok.nextToken();

            while (strtok.hasMoreTokens()) {
                MimeTypeEntry entry = null;

                file_ext = strtok.nextToken();
                entry = new MimeTypeEntry(mime_type, file_ext);
                this.type_hash.put(file_ext, entry);
                if (LogSupport.isLoggable())
                    LogSupport.log("Added: " + entry.toString());
            }
        }
    }
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.activation.registries.MimeTypeFile
 * JD-Core Version:    0.6.2
 */