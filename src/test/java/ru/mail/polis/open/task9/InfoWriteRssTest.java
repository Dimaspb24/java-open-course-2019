package ru.mail.polis.open.task9;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class InfoWriteRssTest {

    private static String link;
    private static String file;
    private static InfoWriterRss writerRss;

    interface Executable {
        void execute();
    }

    class Runner {
        // в параметр должны передать какой-то объект реализующий интрефейс Executable
        // у этого объекта выполнится свой метод execute
        public Executable run(Executable e) {
            e.execute();
            return e;
        }
    }

    @BeforeAll
    static void initWriterRss() {
        link = "http://mcomp.org/feed/";
        file = "result.txt";
        writerRss = new InfoWriterRss(link, file);
    }

    @Test
    void testThrowExceptionIncorrectArguments() {
//        Runner runner = new Runner();
//        assertThrows(MalformedURLException.class, (org.junit.jupiter.api.function.Executable) runner.run(new Executable() {
//            @Override
//            public void execute(){
//                InfoWriterRss ifr = new InfoWriterRss(null, file);
//            }
//        }));

        Assertions.assertThrows(IllegalArgumentException.class, () -> new InfoWriterRss(null, file));
        Assertions.assertThrows(NullPointerException.class, () -> new InfoWriterRss(link, null));
        Assertions.assertThrows(Exception.class, () -> new InfoWriterRss(null, null));
    }


    @Test
    void testWorkingMethods() {
        /*writerRss.build();
        try {
            writerRss.writeInfoToFile();
        } catch (BuildFailedException e) {
            e.printStackTrace();
        }*/

        //Проверка что нельзя записать файл до построения
        Assertions.assertThrows(BuildFailedException.class, () -> writerRss.writeInfoToFile());

        //ПРоверка что после потроения не вылетает исключение
        writerRss.build();
        Assertions.assertDoesNotThrow(() -> writerRss.writeInfoToFile());

        //Запись из xml файла в коллекцию из массивов строк
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

//        writerRss.build();
//
//        try {
//            writerRss.writeInfoToFile();
//        } catch (BuildFailedException e) {
//            e.printStackTrace();
//        }

        //Проверки на то соответствуют ли инфа с сайта с инфой их XTest.xml
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