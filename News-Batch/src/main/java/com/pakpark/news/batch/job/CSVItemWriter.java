package com.pakpark.news.batch.job;

import com.pakpark.news.batch.dto.News;
import com.pakpark.news.batch.dto.NewsPattern;
import com.pakpark.news.batch.mapper.NewsMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
public class CSVItemWriter implements ItemWriter<NewsPattern> {

  @Resource
  private NewsMapper newsMapper;

  @Value("${spring.profiles.active}")
  private String activeEnv;

  @Override
  public void write(List<? extends NewsPattern> list) throws Exception {

    List<? extends NewsPattern> newsList = list;

    News news = null;
    NewsPattern newsPattern = null;

    for (int i = 0; i < newsList.size(); i++) {

      news = newsList.get(i).getNews();
      newsPattern = newsList.get(i);

      log.info("패턴 유무: {} , keyword: {},  title : {}",
              newsPattern.isPattern(),
              news.getTitleKeyword(),
              news.getTitle());

      /**
       * 실행 환경에서 'local' 로 실행하면 출력만, 'mysql' 실행 시 INSERT 작업 실행.
       * application.yml > 'spring.profiles.active'
       */
      if (!activeEnv.equals("local")) {
        if (newsPattern.isPattern())
          newsMapper.insertBracketsNews(news);
        else
          newsMapper.insertNonBracketsNews(news);
      }

    }

  }
}
