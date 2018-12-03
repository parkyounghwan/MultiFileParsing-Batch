package com.pakpark.news.batch.parsing;

import com.pakpark.news.batch.dto.News;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CSVParser {

  @Autowired
  private FilePath filePath;

  public MultiResourceItemReader<News> multiResourceItemReader(String year, String month) {
    MultiResourceItemReader<News> resourceItemReader = new MultiResourceItemReader<News>();
    resourceItemReader.setResources(filePath.getFileList(year, month));
    resourceItemReader.setDelegate(read());
    return resourceItemReader;
  }

  public FlatFileItemReader<News> read() {

    DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer(DelimitedLineTokenizer.DELIMITER_TAB);
    lineTokenizer.setQuoteCharacter('∴');
    lineTokenizer.setStrict(true);

    DefaultLineMapper<News> lineMapper = new DefaultLineMapper<>();
    lineMapper.setLineTokenizer(lineTokenizer);
    lineMapper.setFieldSetMapper(fieldSet -> {
      try {
        return News.builder()
                .officeId(fieldSet.readString(0))
                .articleId(fieldSet.readString(1))
                .section(fieldSet.readString(2))
                .dt(fieldSet.readString(4))
                .title(fieldSet.readString(5))
                .content(fieldSet.readString(6))
                .reporter(fieldSet.readString(7))
                .build();
      } catch (Exception e) {
        return new News(null, null, null, null, null, null, null, null, null);
      }
    });

    FlatFileItemReader<News> itemReader = new FlatFileItemReader<>();
    itemReader.setLineMapper(lineMapper);
    itemReader.setLinesToSkip(1);   //header

    return itemReader;
  }

}
