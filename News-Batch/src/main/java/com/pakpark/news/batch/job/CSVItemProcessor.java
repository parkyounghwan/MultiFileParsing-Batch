package com.pakpark.news.batch.job;

import com.pakpark.news.batch.dto.News;
import com.pakpark.news.batch.dto.NewsPattern;
import com.pakpark.news.batch.parsing.PatternUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class CSVItemProcessor implements ItemProcessor<News, NewsPattern> {

  @Autowired
  private PatternUtil patternUtil;

  @Override
  public NewsPattern process(News news) {

    String title = news.getTitle();

    /**
     * FlatFileItemReader 파싱 시, Delimeter 버그로 인해 파싱 오작동하는 경우가 발생하여 예외처리.
     */
    if (title == null || news.getReporter().length() > 200) {
      return null;
    }

    if(patternUtil.hasBrackets(title) || patternUtil.hasAngleBrackets(title)) {
      news.setTitleKeyword(patternUtil.titleKeyword(title));
      return new NewsPattern(news, patternUtil.hasBrackets(title));
    } else {
      news.setTitleKeyword(title);
      return new NewsPattern(news, patternUtil.hasBrackets(title));
    }
  }
}
