/**
 * Copyright (C) 2013 ��ԭ
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
 * ʵ�������û��������ָ��������ʽ��PlainDocument�ĵ�
 * 
 * @author ��ԭ
 * 
 */
public class BasePlainDocument extends PlainDocument {
  private static final long serialVersionUID = 1L;
  private Matcher matcher = null;
  private Pattern pattern = null;

  /**
   * Ĭ�ϵĹ��췽��
   */
  public BasePlainDocument() {
    super();
  }

  /**
   * �������Ĺ��췽��
   * 
   * @param content
   *          �����ɱ༭���ַ��������ݵĽӿ�
   */
  public BasePlainDocument(AbstractDocument.Content content) {
    super(content);
  }

  /**
   * �������Ĺ��췽��
   * 
   * @param content
   *          �����ɱ༭���ַ��������ݵĽӿ�
   * @param pattern
   *          �����û������������ʽ
   */
  public BasePlainDocument(AbstractDocument.Content content, String pattern) {
    super(content);
    this.setPatternByString(pattern);
  }

  /**
   * �������Ĺ��췽��
   * 
   * @param pattern
   *          �����û������������ʽ
   */
  public BasePlainDocument(String pattern) {
    super();
    this.setPatternByString(pattern);
  }

  /**
   * ����������ʽ
   * 
   * @param pattern
   *          �����û������������ʽ
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
   * ���ĵ��в����ַ���
   * 
   * @param offset
   *          ��ʼƫ����
   * @param str
   *          Ҫ������ַ���
   * @param set
   *          �������ݵ�����
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
