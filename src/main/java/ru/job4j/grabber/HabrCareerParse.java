package ru.job4j.grabber;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.utils.DateTimeParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HabrCareerParse implements Parse {

    public static final String PREFIX = "/vacancies?page=";
    public static final String SUFFIX = "&q=Java%20developer&type=all";
    private final DateTimeParser dateTimeParser;

    public HabrCareerParse(DateTimeParser dateTimeParser) {
        this.dateTimeParser = dateTimeParser;
    }

    private String retrieveDescription(String link) {
        String str = null;
        Connection connection = Jsoup.connect(link);
        try {
            Document document = connection.get();
            Elements rows = document.select(".basic-section--appearance-vacancy-description");
            str = rows.text();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }

    private Post createPost(Element row, String link, int id) {
        Element dateElement = row.select(".vacancy-card__date").first();
        Element dateTimeElement = Objects.requireNonNull(dateElement).child(0);
        Element titleElement = row.select(".vacancy-card__title").first();
        Element linkElement = Objects.requireNonNull(titleElement).child(0);
        String vacancyName = titleElement.text();
        String dateTime = dateTimeElement.attr("datetime");
        String linkVacancy = String.format("%s%s", link, linkElement.attr("href"));
        return new Post(id, vacancyName, retrieveDescription(linkVacancy),
                linkVacancy, dateTimeParser.parse(dateTime));
    }

    @Override
    public List<Post> list(String link) {
        List<Post> postList = new ArrayList<>();
        int pageNumber = 1;
        int id = 1;
        while (pageNumber <= 5) {
            String fullLink = "%s%s%d%s".formatted(link, PREFIX, pageNumber, SUFFIX);
            Connection connection = Jsoup.connect(fullLink);
            Document document;
            try {
                document = connection.get();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Elements rows = document.select(".vacancy-card__inner");
            for (Element row : rows) {
                postList.add(createPost(row, link, id++));
            }
            pageNumber++;
        }
        return postList;
    }
}