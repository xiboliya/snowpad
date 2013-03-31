package com.xiboliya.snowpad;

import java.awt.Color;
import java.awt.Font;

/**
 * 用于初始化文本域的属性配置类。
 * 
 * @author chen
 * 
 */
public class Setting {
  public boolean isLineWrap = true; // 是否自动换行
  public boolean isWrapStyleWord = true; // 是否单词边界换行
  public LineSeparator lineSeparator = LineSeparator.DEFAULT; // 换行符格式
  public CharEncoding charEncoding = CharEncoding.BASE; // 字符编码格式
  public Font font = Util.TEXT_FONT; // 文本域字体
  public boolean textDrag = false; // 是否可拖拽文本
  public boolean autoIndent = false; // 是否可自动缩进
  public boolean tabReplaceBySpace = false; // 是否以空格代替Tab键
  public Color[] colorStyle = null; // 配色方案
  public int tabSize = Util.DEFAULT_TABSIZE; // Tab键所占字符数
  public boolean isSaved = false; // 文件是否已保存，如果已保存则为true
  public boolean isTextChanged = false; // 文本内容是否已修改，如果已修改则为true
  public boolean isStyleChanged = false; // 文本格式是否已修改，如果已修改则为true
  public boolean fileExistsLabel = false; // 当文件删除或移动后，用于标识是否已弹出过提示框
}
