package ru.mail.polis.open.task9;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.SyndFeedOutput;
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
    //private SyndFeedInput sfinput; // Способен разобрать канал RSS создавая экземпляр SyndFeed
    //private SyndFeedOutput sfoutput; // Способен преобразовать экземпляр SyndFeed в XML-поток RSS

    /*
        URI - это лишь синтаксическая конструкция, которая содержит различные части строки, описывающей Web-ресурс.
    */
    public InfoWriterRss(String link,String file){
        try{
            this.url = new URL(link);
        } catch (MalformedURLException e) {
            e.printStackTrace();
//            throw new IllegalArgumentException("The address is incorrect or the Protocol is unsupported");
        }

        this.file = new File(file);
        this.isBuilding = false;
    }

    /*
        SynfFeed - класс, который включает парсер для обработки каналов синдикации
        Класс SyndFeedInput обрабатывает синтаксические анализаторы, используя правильный,
        основанный на обрабатываемом канале синдикации(работает с с любым типом канала синдикации (RSS и Atom версии)

        Вторая строка предписывает SyndFeed feed считывать ленту синдикации из входного
        потока на основе char URL-адреса, указывающего на ленту.(c помощью
        XmlReader-это читатель на основе символов, который разрешает кодировку, соответствующую типам HTTP MIME и правилам XML для нее.
        Создание некого объектного представления для XML данных, из которых состоит поток.

        SyndFeedInput.метод build () возвращает экземпляр SyndFeed, который может быть легко обработан.
        Непосредственно разбор представления и формирование объекта класса SyndFeed,
        инкапсулирующего данные о потоке и его элементах (т.е. записях, из которых состоит лента).
        Разбор осуществляется в методе build класса SyndFeedInput.
        Данный класс сам определяет формат потока и вызывает нужные обработчики.
     */

    // происходит загрузка канала RSS или ATOM в SyndFeed класс
    public void build() {
        SyndFeedInput input = new SyndFeedInput();
        try {
            feed = input.build(new XmlReader(url));
            isBuilding = true;
        } catch (IOException | FeedException e) {
            e.printStackTrace();
        }

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
