package com.pakpark.news.batch.job;

import com.pakpark.news.batch.dto.News;
import com.pakpark.news.batch.dto.NewsPattern;
import com.pakpark.news.batch.insertService.InsertNews;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class CSVItemWriter implements ItemWriter<NewsPattern> {

  @Autowired
  private InsertNews insertNews;

  @Value("${spring.profiles.active}")
  private String activeEnv;

  @Override
  public void write(List<? extends NewsPattern> list) throws Exception {

    News news = null;
    NewsPattern newsPattern = null;

    List<NewsPattern> bracketsNewsList = list.parallelStream().filter(x -> x.isPattern()).collect(Collectors.toList());
    List<NewsPattern> nonBracketsNewsList = list.parallelStream().filter(x -> !x.isPattern()).collect(Collectors.toList());

    List<News> newsList = bracketsNewsList.parallelStream().map(x -> x.getNews()).collect(Collectors.toList());
    insertNews.insertNews(false, newsList);
    insertNews.insertNews(true, newsList);

    /**
     * 실행 환경에서 'local' 로 실행하면 출력만, 'mysql' 실행 시 INSERT 작업 실행.
     * application.yml > 'spring.profiles.active'
     */
    if (activeEnv.equals("local")) {
      for (int i = 0; i < list.size(); i++) {

        news = list.get(i).getNews();
        newsPattern = list.get(i);

        insertNews.clusteringResult(newsPattern.isPattern(), news);
      }
    }
  }
}
