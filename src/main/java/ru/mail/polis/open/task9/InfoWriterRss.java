package ru.mail.polis.open.task9;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class InfoWriterRss {

    private URL url;
    private File file;
    private SyndFeed feed;
    private boolean isBuilding;

    /*
    Класс принимает на вход ссылку на сайт и название файла
        URI - это лишь синтаксическая конструкция, которая содержит различные части строки, описывающей Web-ресурс.
     */
    public InfoWriterRss(String link,String file){
        try{
            this.url = new URL(link);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("The address is incorrect or the Protocol is unsupported");
        }
        this.file = new File(file);
        this.isBuilding = false;
    }

    /*
        Первая строка создает экземпляр SyndFeedInput,
         который будет работать с любым типом канала синдикации (RSS и Atom версии)
        Вторая строка предписывает SyndFeedInput считывать ленту синдикации из входного
        потока на основе char URL-адреса, указывающего на ленту.
        XmlReader-это читатель на основе символов, который разрешает кодировку, соответствующую типам HTTP MIME и правилам XML для нее.
        SyndFeedInput.метод build () возвращает экземпляр SyndFeed, который может быть легко обработан.
     */
    public void build() throws IOException, FeedException {
        SyndFeedInput input = new SyndFeedInput();
        feed = input.build(new XmlReader(url));
        isBuilding = true;
    }

    public void writeInfoToFile() throws BuildFailedException {
        if (!isBuilding){
            throw new BuildFailedException();
        }

        try (FileWriter fw = new FileWriter(file)){

            for (SyndEntry entry : feed.getEntries()) {
                fw.write(
                        (entry.getTitle() != null ? entry.getTitle() : "title : NULL") + "\n"
                        + (entry.getDescription() != null ? entry.getDescription().getValue() : "description : NULL") + "\n"
                        + (entry.getPublishedDate() != null ? entry.getPublishedDate().toString() : "time : NULL") + "\n"
                        + entry.getLink() + "\n\n"
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
