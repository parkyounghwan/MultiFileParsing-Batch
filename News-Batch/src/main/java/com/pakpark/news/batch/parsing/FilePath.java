package com.pakpark.news.batch.parsing;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.Null;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class FilePath {

  private static final String FILE_PATH_FORMAT = "../data/%s/%s"; // TODO: 환경별로 다르게 로딩해야 함

  private static String DIR_PATH = "data/";

  public Resource[] getFileList(String year, String month) {

    Resource[] resources = null;

    String dirPath = setDirPath(year, month);

    try {

      File[] files = new File(dirPath).listFiles();

      List<org.springframework.core.io.Resource> fileList = new ArrayList<>();

      for (File file : files) {
        if (!file.getName().contains(".DS_Store"))
          fileList.add(new FileSystemResource(file));
      }

      int listSize = fileList.size();

      resources = new Resource[listSize];

      for (int i = 0; i < listSize; i++) {
        resources[i] = fileList.get(i);
      }

    } catch (NullPointerException e) {
      log.info(e.getMessage());
    }

    return resources;
  }

  public String setDirPath(String year, String month) {
    StringBuilder sb = new StringBuilder(DIR_PATH)
            .append(year)
            .append("/")
            .append(month)
            .append("/");

    return sb.toString();
  }

  static List<String> getFilePaths(String year, String month) {
    List<String> fileList = new ArrayList<>();
    String directoryPath = String.format(FILE_PATH_FORMAT, year, month);
    try {
      File[] files = new File(directoryPath).listFiles();

      for (File file : files) {
        if (file.isFile()) {
          fileList.add(file.getName());
        }
      }
    } catch (NullPointerException e) {
      // TODO: 로거 필요
      log.info(e.getMessage());
    }

    return fileList;
  }
}
