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

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * 实现限制用户输入符合指定正则表达式的PlainDocument文档
 * 
 * @author 冰原
 * 
 */
public class BasePlainDocument extends PlainDocument {
  private static final long serialVersionUID = 1L;
  private Matcher matcher = null;
  private Pattern pattern = null;

  /**
   * 默认的构造方法
   */
  public BasePlainDocument() {
    super();
  }

  /**
   * 带参数的构造方法
   * 
   * @param content
   *          描述可编辑的字符序列内容的接口
   */
  public BasePlainDocument(AbstractDocument.Content content) {
    super(content);
  }

  /**
   * 带参数的构造方法
   * 
   * @param content
   *          描述可编辑的字符序列内容的接口
   * @param pattern
   *          限制用户输入的正则表达式
   */
  public BasePlainDocument(AbstractDocument.Content content, String pattern) {
    super(content);
    this.setPatternByString(pattern);
  }

  /**
   * 带参数的构造方法
   * 
   * @param pattern
   *          限制用户输入的正则表达式
   */
  public BasePlainDocument(String pattern) {
    super();
    this.setPatternByString(pattern);
  }

  /**
   * 设置正则表达式
   * 
   * @param pattern
   *          限制用户输入的正则表达式
   */
  public void setPatternByString(String pattern) {
    this.pattern = Pattern.compile(pattern);
    try {
      this.matcher = this.pattern.matcher(this.getText(0, this.getLength()));
      if (!this.matcher.matches()) {
        this.remove(0, this.getLength());
      }
    } catch (BadLocationException x) {
      // x.printStackTrace();
    }
  }

  /**
   * 向文档中插入字符串
   * 
   * @param offset
   *          起始偏移量
   * @param str
   *          要插入的字符串
   * @param set
   *          插入内容的属性
   */
  public void insertString(int offset, String str, AttributeSet set)
      throws BadLocationException {
    String targetStr = this.getText(0, offset) + str +
        this.getText(offset, this.getLength() - offset);
    if (this.matcher != null) {
      this.matcher.reset(targetStr);
      if (!this.matcher.matches()) {
        return;
      }
    }
    super.insertString(offset, str, set);
  }

}
