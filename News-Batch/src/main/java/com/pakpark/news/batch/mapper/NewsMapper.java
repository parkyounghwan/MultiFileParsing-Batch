package com.pakpark.news.batch.mapper;

import com.pakpark.news.batch.dto.News;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NewsMapper {

  int insertBracketsNews(List<News> news);

  int insertNonBracketsNews(List<News> news);

}
