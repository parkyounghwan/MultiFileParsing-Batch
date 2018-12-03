package com.pakpark.news.batch.parsing;

import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class PatternUtil {
  private static final String IS_BRACKETS = "\\[|\\]";
  private static final String IS_ANGLE_BRACKETS = "\\<|\\>";
  private static final String BRACKETS_TEXT = "\\[(.*?)\\]";
  private static final String ANGLE_BRACKETS_TEXT = "\\<(.*?)\\>";

  public Boolean hasBrackets(String newsTitle) {
    return patternDetect(newsTitle, IS_BRACKETS);
  }

  public Boolean hasAngleBrackets(String newsTitle) {
    return patternDetect(newsTitle, IS_ANGLE_BRACKETS);
  }

  private Boolean patternDetect(String newsTitle, String regularExpression) {

    Pattern pattern = Pattern.compile(regularExpression);

    Matcher matcher = pattern.matcher(newsTitle);

    return matcher.find();
  }

  public String titleKeyword(String newsTitle) {
    return patternInner(newsTitle, BRACKETS_TEXT, ANGLE_BRACKETS_TEXT);
  }

  private String patternInner(String title, String regularExpression1, String regularExpression2) {

    Pattern pattern1 = Pattern.compile(regularExpression1);
    Pattern pattern2 = Pattern.compile(regularExpression2);

    Matcher matcher1 = pattern1.matcher(title);
    Matcher matcher2 = pattern2.matcher(title);

    if (matcher1.find()) return matcher1.group(1);
    else if (matcher2.find()) return matcher2.group(1);
    else return "";
  }

}
