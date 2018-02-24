/**
 * Copyright (C) 2013 冰原
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.xiboliya.snowpad;

import java.awt.Color;
import java.awt.Font;

/**
 * 用于初始化文本域的属性配置类。
 * 
 * @author 冰原
 * 
 */
public class TextAreaSetting {
  public boolean isLineWrap = true; // 是否自动换行
  public boolean isWrapStyleWord = true; // 是否单词边界换行
  public LineSeparator lineSeparator = LineSeparator.DEFAULT; // 换行符格式
  public CharEncoding charEncoding = CharEncoding.BASE; // 字符编码格式
  public Font font = Util.TEXT_FONT; // 文本域字体
  public boolean textDrag = false; // 是否可拖拽文本
  public boolean autoIndent = false; // 是否可自动缩进
  public boolean tabReplaceBySpace = false; // 是否以空格代替Tab键
  public boolean autoComplete = false; // 是否自动完成
  public Color[] colorStyle = null; // 配色方案
  public int tabSize = Util.DEFAULT_TABSIZE; // Tab键所占字符数
  public boolean isSaved = false; // 文件是否已保存，如果已保存则为true
  public boolean isTextChanged = false; // 文本内容是否已修改，如果已修改则为true
  public boolean isStyleChanged = false; // 文本格式是否已修改，如果已修改则为true
  public boolean fileExistsLabel = false; // 当文件删除或移动后，用于标识是否已弹出过提示框
  public boolean isLineNumberView = false; // 是否显示行号栏
}
