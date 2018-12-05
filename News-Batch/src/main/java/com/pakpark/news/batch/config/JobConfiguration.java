package com.pakpark.news.batch.config;

import com.pakpark.news.batch.dto.News;
import com.pakpark.news.batch.dto.NewsPattern;
import com.pakpark.news.batch.job.CSVItemProcessor;
import com.pakpark.news.batch.job.CSVItemWriter;
import com.pakpark.news.batch.parsing.CSVParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Configuration
public class JobConfiguration {

  private static final int CHUNK_SIZE = 5;

  private final JobBuilderFactory jobBuilderFactory;
  private final StepBuilderFactory stepBuilderFactory;
  private final CSVParser csvParser;

  //  @Value("${url.server.clustering}")
  private String clusterServerURL;

  @Bean
  public Job readCSVFilesJob() {
    return jobBuilderFactory
            .get("readCSVFilesJob")
            .start(insertStep(null, null, null))
            .build();
  }

  @Bean
  @JobScope
  public Step insertStep(@Value("#{jobParameters[year]}") String year,
                         @Value("#{jobParameters[month]}") String month,
                         @Value("#{jobParameters[param]}") String param) {
    return stepBuilderFactory
            .get("insertStep")
            .<News, NewsPattern>chunk(CHUNK_SIZE)
            .reader(csvParser.multiResourceItemReader(year, month))
            .processor(processor())
            .writer(writer())
            .build();
  }

  @Bean
  @JobScope
  public Step resultStep(@Value("#{jobParameters[month]}") String month) {
    return stepBuilderFactory
            .get("resultStep")
            .tasklet((contribution, chunkContext) -> {

              RestTemplate restTemplate = new RestTemplate();
              HttpHeaders httpHeaders = new HttpHeaders();

              httpHeaders.setContentType(MediaType.APPLICATION_JSON);
              List<String> result = new ArrayList<>();

              result.add("month");
              HttpEntity<List<? extends String>> httpEntity = new HttpEntity<>(result, httpHeaders);
              restTemplate.exchange(clusterServerURL, HttpMethod.POST, httpEntity, Object.class);

              log.info("month: {}", month);

              return RepeatStatus.FINISHED;
            })
            .build();
  }

  @Bean
  @StepScope
  public CSVItemProcessor processor() {
    return new CSVItemProcessor();
  }

  @Bean
  @StepScope
  public CSVItemWriter writer() {
    return new CSVItemWriter();
  }

}
