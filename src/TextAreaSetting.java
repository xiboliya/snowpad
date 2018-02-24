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

import java.awt.Color;
import java.awt.Font;

/**
 * ���ڳ�ʼ���ı�������������ࡣ
 * 
 * @author ��ԭ
 * 
 */
public class TextAreaSetting {
  public boolean isLineWrap = true; // �Ƿ��Զ�����
  public boolean isWrapStyleWord = true; // �Ƿ񵥴ʱ߽绻��
  public LineSeparator lineSeparator = LineSeparator.DEFAULT; // ���з���ʽ
  public CharEncoding charEncoding = CharEncoding.BASE; // �ַ������ʽ
  public Font font = Util.TEXT_FONT; // �ı�������
  public boolean textDrag = false; // �Ƿ����ק�ı�
  public boolean autoIndent = false; // �Ƿ���Զ�����
  public boolean tabReplaceBySpace = false; // �Ƿ��Կո����Tab��
  public boolean autoComplete = false; // �Ƿ��Զ����
  public Color[] colorStyle = null; // ��ɫ����
  public int tabSize = Util.DEFAULT_TABSIZE; // Tab����ռ�ַ���
  public boolean isSaved = false; // �ļ��Ƿ��ѱ��棬����ѱ�����Ϊtrue
  public boolean isTextChanged = false; // �ı������Ƿ����޸ģ�������޸���Ϊtrue
  public boolean isStyleChanged = false; // �ı���ʽ�Ƿ����޸ģ�������޸���Ϊtrue
  public boolean fileExistsLabel = false; // ���ļ�ɾ�����ƶ������ڱ�ʶ�Ƿ��ѵ�������ʾ��
  public boolean isLineNumberView = false; // �Ƿ���ʾ�к���
}
