package com.xiboliya.snowpad;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * 当用户按下Esc键时关闭当前对话框，按Enter键时执行相应操作的键盘事件类
 * 
 * @author chen
 * 
 */
public class BaseKeyAdapter extends KeyAdapter {
  private BaseDialog dialog = null;
  private boolean applyEnter = true;

  public BaseKeyAdapter(BaseDialog dialog) {
    this.dialog = dialog;
  }

  public BaseKeyAdapter(BaseDialog dialog, boolean applyEnter) {
    this.dialog = dialog;
    this.applyEnter = applyEnter;
  }

  public void keyPressed(KeyEvent e) {
    if (null == this.dialog) {
      return;
    }
    if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
      this.dialog.onCancel();
    } else if (e.getKeyCode() == KeyEvent.VK_ENTER && this.applyEnter) {
      this.dialog.onEnter();
    }
  }
}
