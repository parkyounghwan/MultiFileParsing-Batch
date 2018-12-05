package com.pakpark.news.batch.insertServiceImpl;

import com.pakpark.news.batch.dto.News;
import com.pakpark.news.batch.insertService.InsertNews;
import com.pakpark.news.batch.mapper.NewsMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Component
public class InsertNewsImpl implements InsertNews {

  @Resource
  private NewsMapper newsMapper;

  @Override
  public void insertNews(boolean isPattern, List<News> news) {
    if(isPattern) newsMapper.insertBracketsNews(news);
    else newsMapper.insertNonBracketsNews(news);
  }

  @Override
  public void clusteringResult(boolean isPattern, News news) {
    if(isPattern) log.info("pattern {}: {}", isPattern, news.getTitle());
    else log.info("pattern {}: {}", isPattern, news.getTitle());
  }
}
