package ru.job4j.grabber;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Objects;

public class HabrCareerParse {

    private static final String SOURCE_LINK = "https://career.habr.com";
    public static final String PREFIX = "/vacancies?page=";
    public static final String SUFFIX = "&q=Java%20developer&type=all";

    public static void main(String[] args) throws IOException {
        int pageNumber = 1;
        String fullLink = "%s%s%d%s".formatted(SOURCE_LINK, PREFIX, pageNumber, SUFFIX);
        Connection connection = Jsoup.connect(fullLink);
        Document document = connection.get();
        Elements rows = document.select(".vacancy-card__inner");
        rows.forEach(row -> {
            Element dateElement = row.select(".vacancy-card__date").first();
            Element dateTimeElement = Objects.requireNonNull(dateElement).child(0);
            Element titleElement = row.select(".vacancy-card__title").first();
            Element linkElement = Objects.requireNonNull(titleElement).child(0);
            String vacancyNameDate = String.format(
                    "%s, дата публикации:%s", titleElement.text(), dateTimeElement.attr("datetime"));
            String link = String.format("%s%s", SOURCE_LINK, linkElement.attr("href"));
            System.out.printf("%s %s%n", vacancyNameDate, link);
        });
    }
}