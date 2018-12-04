package com.pakpark.news.batch.insertService;

import com.pakpark.news.batch.dto.News;
import org.springframework.stereotype.Component;

@Component
public interface InsertNews {

  void insertNews(boolean isPattern, News news);

  void clusteringResult(boolean isPattern, News news);
}
