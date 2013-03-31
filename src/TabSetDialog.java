package com.xiboliya.snowpad;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 * "Tab字符设置"对话框
 * 
 * @author chen
 * 
 */
public class TabSetDialog extends BaseDialog implements ActionListener {
  private static final long serialVersionUID = 1L;
  private JPanel pnlMain = (JPanel) this.getContentPane();
  private JLabel lblTabSize = new JLabel("Tab键所占字符数：");
  private BaseTextField txtTabSize = new BaseTextField();
  private JCheckBox chkReplaceBySpace = new JCheckBox("以空格代替Tab键(R)", false);
  private JButton btnOk = new JButton("确定");
  private JButton btnCancel = new JButton("取消");
  private BaseKeyAdapter keyAdapter = new BaseKeyAdapter(this);
  private BaseKeyAdapter buttonKeyAdapter = new BaseKeyAdapter(this, false);
  private boolean isReplaceBySpace = false; // 以空格代替Tab键

  public TabSetDialog(JFrame owner, boolean modal, JTextArea txaSource) {
    super(owner, modal);
    if (txaSource == null) {
      return;
    }
    this.txaSource = txaSource;
    this.init();
    this.initTabSize();
    this.addListeners();
    this.setSize(240, 130);
    this.setVisible(true);
  }

  /**
   * 初始化界面
   */
  private void init() {
    this.setTitle("Tab字符设置");
    this.pnlMain.setLayout(null);
    this.lblTabSize.setBounds(20, 10, 120, Util.VIEW_HEIGHT);
    this.txtTabSize.setBounds(140, 10, 50, Util.INPUT_HEIGHT);
    this.pnlMain.add(this.lblTabSize);
    this.pnlMain.add(this.txtTabSize);
    this.chkReplaceBySpace.setBounds(36, 40, 150, Util.VIEW_HEIGHT);
    this.chkReplaceBySpace.setMnemonic('R');
    this.pnlMain.add(this.chkReplaceBySpace);
    this.btnOk.setBounds(20, 65, 85, Util.BUTTON_HEIGHT);
    this.btnCancel.setBounds(130, 65, 85, Util.BUTTON_HEIGHT);
    this.pnlMain.add(this.btnOk);
    this.pnlMain.add(this.btnCancel);
  }

  /**
   * 重写父类的方法：设置本窗口是否可见
   */
  public void setVisible(boolean visible) {
    if (visible) {
      this.initTabSize();
    }
    super.setVisible(visible);
  }

  /**
   * 初始化文本框
   */
  private void initTabSize() {
    this.txtTabSize.setText(String.valueOf(this.txaSource.getTabSize()));
    this.txtTabSize.selectAll();
  }

  /**
   * 添加事件监听器
   */
  private void addListeners() {
    this.txtTabSize.addKeyListener(this.keyAdapter);
    this.btnOk.addActionListener(this);
    this.btnOk.addKeyListener(this.buttonKeyAdapter);
    this.btnCancel.addActionListener(this);
    this.btnCancel.addKeyListener(this.buttonKeyAdapter);
    this.chkReplaceBySpace.addActionListener(this);
    this.chkReplaceBySpace.addKeyListener(this.keyAdapter);
  }

  /**
   * 为各组件添加事件的处理方法
   */
  public void actionPerformed(ActionEvent e) {
    if (this.btnOk.equals(e.getSource())) {
      this.onEnter();
    } else if (this.btnCancel.equals(e.getSource())) {
      this.onCancel();
    } else if (this.chkReplaceBySpace.equals(e.getSource())) {
      this.setReplaceBySpace(this.chkReplaceBySpace.isSelected());
    }
  }

  /**
   * 设置Tab所占字符数
   */
  private void setTabSize() {
    int tabSize = Util.DEFAULT_TABSIZE; // Tab键所占字符数
    try {
      tabSize = Integer.parseInt(this.txtTabSize.getText().trim());
    } catch (NumberFormatException x) {
      x.printStackTrace();
      JOptionPane.showMessageDialog(this, "格式错误，请输入数字！", Util.SOFTWARE,
          JOptionPane.CANCEL_OPTION);
      this.txtTabSize.setText("");
      return;
    }
    if (tabSize < Util.MIN_TABSIZE) {
      JOptionPane.showMessageDialog(this, "必须大于等于" + Util.MIN_TABSIZE + "！",
          Util.SOFTWARE, JOptionPane.CANCEL_OPTION);
      this.txtTabSize.setText(String.valueOf(Util.MIN_TABSIZE));
    } else if (tabSize > Util.MAX_TABSIZE) {
      JOptionPane.showMessageDialog(this, "必须小于等于" + Util.MAX_TABSIZE + "！",
          Util.SOFTWARE, JOptionPane.CANCEL_OPTION);
      this.txtTabSize.setText(String.valueOf(Util.MAX_TABSIZE));
    } else {
      this.txaSource.setTabSize(tabSize);
      this.dispose();
    }
  }

  /**
   * 获取是否以空格代替Tab键
   */
  public boolean getReplaceBySpace() {
    return this.isReplaceBySpace;
  }

  /**
   * 设置是否以空格代替Tab键
   */
  public void setReplaceBySpace(boolean isReplaceBySpace) {
    this.isReplaceBySpace = isReplaceBySpace;
  }

  /**
   * 默认的"确定"操作方法
   */
  public void onEnter() {
    this.setTabSize();
  }

  /**
   * 默认的"取消"操作方法
   */
  public void onCancel() {
    this.dispose();
  }
}
