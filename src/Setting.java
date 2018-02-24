/**
 * Copyright (C) 2014 ��ԭ
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
import java.util.HashMap;
import java.util.LinkedList;

/**
 * �������������
 * 
 * @author ��ԭ
 * 
 */
public class Setting {
  // �ı�����������
  public boolean isLineWrap = true; // �Ƿ��Զ�����
  public boolean isWrapStyleWord = true; // �Ƿ񵥴ʱ߽绻��
  public boolean textDrag = false; // �Ƿ����ק�ı�
  public boolean autoIndent = false; // �Ƿ���Զ�����
  public boolean tabReplaceBySpace = false; // �Ƿ��Կո����Tab��
  public boolean autoComplete = false; // �Ƿ��Զ����
  public int tabSize = Util.DEFAULT_TABSIZE; // Tab����ռ�ַ���
  public Font font = Util.TEXT_FONT; // �ı�������
  public Color[] colorStyle = null; // ��ɫ����
  // ����/�滻���������
  public boolean matchCase = true; // �Ƿ����ִ�Сд
  public boolean isWrap = false; // �Ƿ�ѭ������
  public boolean findDown = true; // �Ƿ����²���
  public SearchStyle searchStyle = SearchStyle.DEFAULT; // ����ģʽ
  // ��ʾ���������
  public boolean viewToolBar = true; // �Ƿ���ʾ������
  public boolean viewStateBar = true; // �Ƿ���ʾ״̬��
  public boolean viewLineNumber = false; // �Ƿ���ʾ�к���
  public boolean viewSearchResult = false; // �Ƿ���ʾ���ҽ�����
  public boolean viewAlwaysOnTop = false; // �Ƿ�ǰ����ʾ
  public boolean viewLockResizable = false; // �Ƿ���������
  public boolean viewTabPolicy = true; // �Ƿ���ʾ���б�ǩ
  public boolean viewClickToClose = true; // �Ƿ�˫���رձ�ǩ
  public boolean viewTabIcon = true; // �Ƿ���ʾָʾͼ��
  public int viewLookAndFeel = -1; // ��ǰ��۵�������
  // �����Ѵ򿪵��ļ�
  public LinkedList<String> fileHistoryList = new LinkedList<String>(); // ��������Ѵ򿪵��ļ���������
  // ���п�ݼ�������
  public HashMap<String, String> shortcutMap = new HashMap<String, String>(); // ������п�ݼ������õĹ�ϣ��
}
