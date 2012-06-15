package com.xiboliya.snowpad;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * 选择"文件编码格式"对话框
 * 
 * @author chen
 * 
 */
public class FileEncodingDialog extends BaseDialog implements ActionListener {
  private static final long serialVersionUID = 1L;
  private JPanel pnlMain = (JPanel) this.getContentPane();
  private JLabel lblEncoding = new JLabel("文件编码格式：");
  private JComboBox cmbEncoding = new JComboBox(Util.FILE_ENCODINGS);
  private JButton btnOk = new JButton("确定");
  private JButton btnCancel = new JButton("取消");
  private CharEncoding charEncoding = null;
  private boolean isOk = false;
  private BaseKeyAdapter keyAdapter = new BaseKeyAdapter(this);
  private BaseKeyAdapter buttonKeyAdapter = new BaseKeyAdapter(this, false);

  public FileEncodingDialog(JFrame owner, boolean modal) {
    super(owner, modal);
    this.init();
    this.addListeners();
    this.setSize(290, 110);
    this.setVisible(true);
  }

  /**
   * 重写父类的方法：设置本窗口是否可见
   * 
   * @param isVisible
   *          设置本窗口是否可见，如果可见则为true
   */
  public void setVisible(boolean isVisible) {
    if (isVisible) {
      this.isOk = false;
    }
    super.setVisible(isVisible);
  }

  /**
   * 初始化界面
   */
  private void init() {
    this.setTitle("文件编码格式");
    this.pnlMain.setLayout(null);
    this.lblEncoding.setBounds(20, 10, 110, Util.VIEW_HEIGHT);
    this.cmbEncoding.setBounds(115, 10, 150, Util.INPUT_HEIGHT);
    this.pnlMain.add(this.lblEncoding);
    this.pnlMain.add(this.cmbEncoding);
    this.btnOk.setBounds(40, 50, 85, Util.BUTTON_HEIGHT);
    this.btnCancel.setBounds(160, 50, 85, Util.BUTTON_HEIGHT);
    this.pnlMain.add(this.btnOk);
    this.pnlMain.add(this.btnCancel);
  }

  /**
   * 添加事件监听器
   */
  private void addListeners() {
    this.cmbEncoding.addKeyListener(this.keyAdapter);
    this.btnOk.addActionListener(this);
    this.btnOk.addKeyListener(this.buttonKeyAdapter);
    this.btnCancel.addActionListener(this);
    this.btnCancel.addKeyListener(this.buttonKeyAdapter);
  }

  /**
   * 为各组件添加事件的处理方法
   */
  public void actionPerformed(ActionEvent e) {
    if (this.btnOk.equals(e.getSource())) {
      this.onEnter();
    } else if (this.btnCancel.equals(e.getSource())) {
      this.onCancel();
    }
  }

  /**
   * 获取当前选择的编码格式
   * 
   * @return 编码格式
   */
  public CharEncoding getCharEncoding() {
    return this.charEncoding;
  }

  /**
   * 设置当前的编码格式
   */
  private void setCharEncoding() {
    int index = this.cmbEncoding.getSelectedIndex();
    switch (index) {
    case 0:
      this.charEncoding = null;
      break;
    case 1:
      this.charEncoding = CharEncoding.ANSI;
      break;
    case 2:
      this.charEncoding = CharEncoding.UBE;
      break;
    case 3:
      this.charEncoding = CharEncoding.ULE;
      break;
    case 4:
      this.charEncoding = CharEncoding.UTF8;
      break;
    case 5:
      this.charEncoding = CharEncoding.UTF8_NO_BOM;
      break;
    case 6:
      this.charEncoding = CharEncoding.BASE;
      break;
    }
  }

  /**
   * 获取是否执行了确定
   * 
   * @return 是否执行了确定
   */
  public boolean getOk() {
    return this.isOk;
  }

  /**
   * 默认的"确定"操作方法
   */
  public void onEnter() {
    this.setCharEncoding();
    this.dispose();
    this.isOk = true;
  }

  /**
   * 默认的"取消"操作方法
   */
  public void onCancel() {
    this.charEncoding = null;
    this.dispose();
    this.isOk = false;
  }
}
