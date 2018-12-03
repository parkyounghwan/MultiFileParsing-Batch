package com.pakpark.news.batch.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class News {

  private String officeId;
  private String articleId;
  private String section;
  private String dt;
  private String title;
  private String content;
  private String reporter;
  private String titleKeyword;
  private String contentKeyword;
}
