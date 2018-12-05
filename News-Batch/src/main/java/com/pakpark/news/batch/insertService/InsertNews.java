package com.pakpark.news.batch.insertService;

import com.pakpark.news.batch.dto.News;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface InsertNews {

  void insertNews(boolean isPattern, List<News> news);

  void clusteringResult(boolean isPattern, News news);
}
