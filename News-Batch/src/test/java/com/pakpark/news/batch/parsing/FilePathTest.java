package com.pakpark.news.batch.parsing;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

@Slf4j
@SpringBootTest
public class FilePathTest {

  private static String DIR_PATH = "data/";
  private static String FILE_PATH1 = "data/421_20171004.csv";
  private static String FILE_PATH2 = "data/421_20171005.csv";

  @Test
  public void success_getFilePaths() {
    // when
    List<String> filePathList = FilePath.getFilePaths("2017", "10");

    // then
    assertEquals(1, filePathList.size());
    assertEquals("421_20171004.csv", filePathList.get(0));
  }

  @Test
  public void resourceArrayTest() {
    File[] files = new File(DIR_PATH).listFiles();
    int fileSize = files.length;
    Resource[] re = new Resource[files.length];

    int resIndex = 0;
    for (File file : files) {
      if (!file.getName().contains(".DS_Store")) {
        re[resIndex++] = new FileSystemResource(file.getName());
      }
    }

    try {
      for (Resource resource : re) {
        log.info(resource.getFilename());
      }
    } catch (Exception e) {
    }
  }
}
