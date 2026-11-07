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

package com.xiboliya.snowpad.base;

import java.awt.Window;
import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 * 用于统一行为的JDialog窗口类
 * 
 * @author 冰原
 * 
 */
public abstract class BaseDialog extends JDialog {
  private static final long serialVersionUID = 1L;
  // protected成员可被同一包中的所有类以及不同包中的子类访问
  protected BaseTextArea txaSource = null;
  protected BaseKeyAdapter keyAdapter = new BaseKeyAdapter(this);
  protected BaseKeyAdapter buttonKeyAdapter = new BaseKeyAdapter(this, false);

  public BaseDialog(JFrame owner, boolean modal) {
    super(owner, modal);
    this.init();
  }

  public BaseDialog(JDialog owner, boolean modal) {
    super(owner, modal);
    this.init();
  }

  public BaseDialog(JFrame owner, boolean modal, BaseTextArea txaSource) {
    super(owner, modal);
    this.init();
    this.setTextArea(txaSource);
  }

  /**
   * 初始化界面
   */
  private void init() {
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
  @Override
  public void setVisible(boolean visible) {
    if (!this.isVisible() && visible) {
      this.setLocationRelativeTo(this.getOwner());
    }
    super.setVisible(visible);
  }

  /**
   * 设置当前编辑的文本域
   * 
   * @param txaSource 当前编辑的文本域
   */
  public void setTextArea(BaseTextArea txaSource) {
    if (txaSource != null) {
      this.txaSource = txaSource;
    }
  }

  /**
   * 回收资源
   */
  public void destroy() {
    this.txaSource = null;
    this.keyAdapter.destroy();
    this.buttonKeyAdapter.destroy();
    this.keyAdapter = null;
    this.buttonKeyAdapter = null;
    this.dispose();
  }

}
