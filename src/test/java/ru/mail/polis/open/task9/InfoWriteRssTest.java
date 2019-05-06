package ru.mail.polis.open.task9;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class InfoWriteRssTest {

    private static String link;
    private static String file;
    private static InfoWriterRss writerRss;

    @BeforeAll
    static void initWriterRss() throws MalformedURLException {
        link = "http://mcomp.org/feed/";
        file = "result.txt";
        writerRss = new InfoWriterRss(link, file);
    }

    @Test
    void testThrowExceptionIncorrectArguments() {
        assertThrows(MalformedURLException.class, () -> new InfoWriterRss(null, file));
        assertThrows(NullPointerException.class, () -> new InfoWriterRss(link, null));
        assertThrows(Exception.class, () -> new InfoWriterRss(null, null));
    }

    @Test
    void testWorkingMethods() {
        assertThrows(BuildFailedException.class, () -> writerRss.writeInfoToFile());

        writerRss.build();
        assertDoesNotThrow(() -> writerRss.writeInfoToFile());

        List<String[]> listInfo = new ArrayList<>();
        try {
            SyndFeed feed = new SyndFeedInput().build(new XmlReader(new File("XTest.xml")));

            for (SyndEntry entry : feed.getEntries()) {
                String[] info = new String[4];
                info[0] = entry.getTitle();
                info[1] = entry.getDescription().getValue();
                info[2] = entry.getPublishedDate().toString();
                info[3] = entry.getLink();

                listInfo.add(info);
            }
        } catch (FeedException | IOException e) {
            e.printStackTrace();
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(new File(file)))) {
            for (String[] info : listInfo) {
                assertEquals(
                        info[0].replaceAll(" ", "").replaceAll("\n", ""),
                        reader.readLine().replaceAll(" ", "")
                );
                assertEquals(
                        info[1].replaceAll(" ", "").replaceAll("\n", ""),
                        reader.readLine().replaceAll(" ", "")
                );
                assertEquals(
                        info[2].replaceAll(" ", "").replaceAll("\n", ""),
                        reader.readLine().replaceAll(" ", "")
                );
                assertEquals(
                        info[3].replaceAll(" ", "").replaceAll("\n", ""),
                        reader.readLine().replaceAll(" ", "")
                );
                reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}