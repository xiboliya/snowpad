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

import java.awt.GridLayout;
import java.util.LinkedList;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

/**
 * �Զ����״̬��
 * 
 * @author ��ԭ
 * 
 */
public class StatePanel extends JPanel {
  private static final long serialVersionUID = 1L;
  private int columns = 1; // ״̬����ʾ�������Ŀ
  private GridLayout layout = new GridLayout(1, columns); // ״̬���Ĳ��ֹ�����
  private LinkedList<JLabel> labelList = new LinkedList<JLabel>(); // �����ʾ���������

  public StatePanel() {
    this(1);
  }

  public StatePanel(int columns) {
    this.checkColumns(columns);
    this.init();
  }

  public StatePanel(String[] arrStrLabel) {
    this(arrStrLabel.length);
    if (arrStrLabel.length > 0) {
      for (int i = 0; i < this.columns; i++) {
        this.labelList.get(i).setText(arrStrLabel[i]);
      }
    }
  }

  /**
   * ��ʼ��״̬��
   */
  private void init() {
    this.setLayout(this.layout);
    this.resetLabelList();
    this.setVisible(true);
  }

  /**
   * ��������
   */
  private void resetLabelList() {
    this.labelList.clear();
    for (int i = 0; i < this.columns; i++) {
      this.appendLabelList();
    }
  }

  /**
   * ������ʾ�������Ŀ�仯����������
   */
  private void updateLabelList() {
    int size = this.labelList.size();
    if (size < this.columns) {
      for (int i = size; i < this.columns; i++) {
        this.appendLabelList();
      }
    } else if (size > this.columns) {
      for (int i = this.columns; i < size; i++) {
        this.removeLabelList();
      }
    }
    SwingUtilities.updateComponentTreeUI(this);
  }

  /**
   * ׷��һ���հ׵���ʾ����
   * 
   * @return ���׷�ӳɹ��򷵻�true�����򷵻�false
   */
  private boolean appendLabelList() {
    if (this.labelList.size() < this.columns) {
      JLabel lblTemp = new JLabel(" ");
      lblTemp.setFont(Util.GLOBAL_FONT);
      this.labelList.add(lblTemp);
      this.add(lblTemp);
      return true;
    }
    return false;
  }

  /**
   * �Ƴ�ĩ��һ���Ѵ��ڵ���ʾ����
   * 
   * @return ����Ƴ��ɹ��򷵻�true�����򷵻�false
   */
  private boolean removeLabelList() {
    if (this.labelList.size() > 0) {
      this.remove(this.labelList.removeLast());
      return true;
    }
    return false;
  }

  /**
   * Ϊָ���±����ʾ���������ַ����Ͷ��뷽ʽ
   * 
   * @param index
   *          ��ʾ������±�
   * @param strLabel
   *          ���õ��ַ���
   * @param alignment
   *          ���뷽ʽ
   */
  public void setStringByIndex(int index, String strLabel,
      StatePanelAlignment alignment) {
    this.setStringByIndex(index, strLabel);
    this.setAlignmentByIndex(index, alignment);
  }

  /**
   * Ϊָ���±����ʾ���������ַ���
   * 
   * @param index
   *          ��ʾ������±�
   * @param strLabel
   *          ���õ��ַ���
   */
  public void setStringByIndex(int index, String strLabel) {
    this.labelList.get(this.checkIndex(index)).setText(strLabel);
  }

  /**
   * ��ȡָ���±����ʾ������ַ���
   * 
   * @param index
   *          ��ʾ������±�
   * @return ��ʾ������ַ���
   */
  public String getStringByIndex(int index) {
    return this.labelList.get(this.checkIndex(index)).getText();
  }

  /**
   * ��ʽ���±�
   * 
   * @param index
   *          �±�
   * @return ������ʽ�����±�
   */
  private int checkIndex(int index) {
    if (index < 0) {
      index = 0;
    } else if (index >= this.labelList.size()) {
      index = this.labelList.size() - 1;
    }
    return index;
  }

  /**
   * ��ʽ����ʾ������Ŀ
   * 
   * @param columns
   *          ��ʾ�������Ŀ
   * @return ������ʽ������ʾ�������Ŀ
   */
  private int checkColumns(int columns) {
    if (columns <= 0) {
      this.columns = 1;
    } else {
      this.columns = columns;
    }
    return this.columns;
  }

  /**
   * ������ʾ�������Ŀ
   * 
   * @param columns
   *          ��ʾ�������Ŀ
   */
  public void setColumns(int columns) {
    this.checkColumns(columns);
    this.updateLabelList();
    this.layout.setColumns(this.columns);
  }

  /**
   * ��ȡ��ʾ�������Ŀ
   * 
   * @return ��ʾ�������Ŀ
   */
  public int getColumns() {
    return this.columns;
  }

  /**
   * Ϊָ���±����ʾ�������ö��뷽ʽ
   * 
   * @param index
   *          ��ʾ������±�
   * @param alignment
   *          ��ʾ����Ķ��뷽ʽ
   */
  public void setAlignmentByIndex(int index, StatePanelAlignment alignment) {
    JLabel lblTemp = this.labelList.get(this.checkIndex(index));
    switch (alignment) {
    case X_LEFT:
      lblTemp.setHorizontalAlignment(SwingConstants.LEFT);
      break;
    case X_CENTER:
      lblTemp.setHorizontalAlignment(SwingConstants.CENTER);
      break;
    case X_RIGHT:
      lblTemp.setHorizontalAlignment(SwingConstants.RIGHT);
      break;
    }
  }

  /**
   * ��ȡָ���±����ʾ����Ķ��뷽ʽ
   * 
   * @param index
   *          ��ʾ������±�
   * @return ��ʾ����Ķ��뷽ʽ
   */
  public StatePanelAlignment getAlignmentByIndex(int index) {
    JLabel lblTemp = this.labelList.get(this.checkIndex(index));
    int alignment = lblTemp.getHorizontalAlignment();
    switch (alignment) {
    case SwingConstants.LEFT:
      return StatePanelAlignment.X_LEFT;
    case SwingConstants.CENTER:
      return StatePanelAlignment.X_CENTER;
    case SwingConstants.RIGHT:
      return StatePanelAlignment.X_RIGHT;
    default:
      return StatePanelAlignment.X_LEFT;
    }
  }
}
