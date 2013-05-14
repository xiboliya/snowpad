package com.xiboliya.snowpad;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTextArea;

/**
 * 用于统一行为的JDialog窗口类
 * 
 * @author 冰原
 * 
 */
public abstract class BaseDialog extends JDialog {
  private static final long serialVersionUID = 1L;
  private JFrame owner = null;
  protected JTextArea txaSource = null; // protected成员可被同一包中的所有类以及不同包中的子类访问

  public BaseDialog(JFrame owner, boolean modal) {
    super(owner, modal);
    this.owner = owner;
    this.setResizable(false);
    this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
  }

  /**
   * "确定"操作的默认方法。此为抽象方法，子类必须实现
   */
  public abstract void onEnter();

  /**
   * "取消"操作的默认方法。此为抽象方法，子类必须实现
   */
  public abstract void onCancel();

  /**
   * 重写父类的方法：显示或隐藏当前窗口
   */
  public void setVisible(boolean visible) {
    if (visible) {
      this.setLocationRelativeTo(this.owner);
    }
    super.setVisible(visible);
  }

  /**
   * 设置当前编辑的文本域
   * 
   * @param txaSource
   *          当前编辑的文本域
   */
  public void setTextArea(JTextArea txaSource) {
    if (txaSource != null) {
      this.txaSource = txaSource;
    }
  }
}
