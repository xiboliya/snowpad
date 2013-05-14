package com.xiboliya.snowpad;

import java.awt.GridLayout;
import java.util.LinkedList;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EtchedBorder;

/**
 * 自定义的状态栏
 * 
 * @author 冰原
 * 
 */
public class StatePanel extends JPanel {
  private static final long serialVersionUID = 1L;
  private int columns = 1; // 状态栏显示区块的数目
  private GridLayout layout = new GridLayout(1, columns); // 状态栏的布局管理器
  private LinkedList<JLabel> labelList = new LinkedList<JLabel>(); // 存放显示区块的链表

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
   * 初始化状态栏
   */
  private void init() {
    this.setBorder(new EtchedBorder());
    this.setLayout(this.layout);
    this.resetLabelList();
    this.setVisible(true);
  }

  /**
   * 重置链表
   */
  private void resetLabelList() {
    this.labelList.clear();
    for (int i = 0; i < this.columns; i++) {
      this.appendLabelList();
    }
  }

  /**
   * 根据显示区块的数目变化而更新链表
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
   * 追加一个空白的显示区块
   * 
   * @return 如果追加成功则返回true，否则返回false
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
   * 移除末端一个已存在的显示区块
   * 
   * @return 如果移除成功则返回true，否则返回false
   */
  private boolean removeLabelList() {
    if (this.labelList.size() > 0) {
      this.remove(this.labelList.removeLast());
      return true;
    }
    return false;
  }

  /**
   * 为指定下标的显示区块设置字符串和对齐方式
   * 
   * @param index
   *          显示区块的下标
   * @param strLabel
   *          设置的字符串
   * @param alignment
   *          对齐方式
   */
  public void setStringByIndex(int index, String strLabel,
      StatePanelAlignment alignment) {
    this.setStringByIndex(index, strLabel);
    this.setAlignmentByIndex(index, alignment);
  }

  /**
   * 为指定下标的显示区块设置字符串
   * 
   * @param index
   *          显示区块的下标
   * @param strLabel
   *          设置的字符串
   */
  public void setStringByIndex(int index, String strLabel) {
    this.labelList.get(this.checkIndex(index)).setText(strLabel);
  }

  /**
   * 获取指定下标的显示区块的字符串
   * 
   * @param index
   *          显示区块的下标
   * @return 显示区块的字符串
   */
  public String getStringByIndex(int index) {
    return this.labelList.get(this.checkIndex(index)).getText();
  }

  /**
   * 格式化下标
   * 
   * @param index
   *          下标
   * @return 经过格式化的下标
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
   * 格式化显示区块数目
   * 
   * @param columns
   *          显示区块的数目
   * @return 经过格式化的显示区块的数目
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
   * 设置显示区块的数目
   * 
   * @param columns
   *          显示区块的数目
   */
  public void setColumns(int columns) {
    this.checkColumns(columns);
    this.updateLabelList();
    this.layout.setColumns(this.columns);
  }

  /**
   * 获取显示区块的数目
   * 
   * @return 显示区块的数目
   */
  public int getColumns() {
    return this.columns;
  }

  /**
   * 为指定下标的显示区块设置对齐方式
   * 
   * @param index
   *          显示区块的下标
   * @param alignment
   *          显示区块的对齐方式
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
   * 获取指定下标的显示区块的对齐方式
   * 
   * @param index
   *          显示区块的下标
   * @return 显示区块的对齐方式
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
