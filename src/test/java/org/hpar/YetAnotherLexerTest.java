package org.hpar;

import junit.framework.TestCase;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.File;
import java.io.IOException;

/**
 * Created by sxf on 6/28/16.
 */
public class YetAnotherLexerTest extends TestCase {
    public void testFind() throws Exception {
        loadAllFiles("src/test/extern/websites");
    }
    int all;
    public void loadAllFiles(String filePath) {
        File f = new File(filePath);
        File[] files = f.listFiles();
        all = files.length;
        for(File file : files) {
            try {
                loadOneFile(file.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private tag lastt;
    int span = 0;
    private void loadOneFile(String path) throws IOException {
        final String data = App.readFile(path);
        YetAnotherLexer lexer = new YetAnotherLexer(data.toCharArray());
        lexer.callback = new YetAnotherLexer.Callback() {

            @Override
            public void find(tag t) {
                if (t.pos - lastt.pos >= span && t.getStatus() == tag.WorkStatus.undo) {
                    assert (data.charAt(t.pos) == '<' && data.charAt(t.pos + t.size - 1) == '>');
                    lastt = t;
                }
            }
        };
        tag t = new tag(0, 0, tag.other_begin);
        lastt = lexer.tail = lexer.tags = t;
        lexer.find();
    }
}
