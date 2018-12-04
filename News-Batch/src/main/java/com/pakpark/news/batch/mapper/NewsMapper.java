package com.pakpark.news.batch.mapper;

import com.pakpark.news.batch.dto.News;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NewsMapper {

  int insertBracketsNews(News news);

  int insertNonBracketsNews(News news);

}
