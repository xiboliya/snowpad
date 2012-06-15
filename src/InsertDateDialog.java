package com.xiboliya.snowpad;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

/**
 * "插入时间/日期"对话框
 * 
 * @author chen
 * 
 */
public class InsertDateDialog extends BaseDialog implements ActionListener,
    CaretListener {
  private static final long serialVersionUID = 1L;
  private JRadioButton radSelect = new JRadioButton("使用选中的格式(S)", true);
  private JList listStyles = new JList();
  private JScrollPane srpStyles = new JScrollPane(this.listStyles);
  private JRadioButton radUser = new JRadioButton("使用自定义格式(U)", false);
  private JLabel lblWarning = new JLabel("");
  private BaseTextField txtUser = new BaseTextField(Util.DATE_STYLES[0]);
  private JLabel lblView = new JLabel("");
  private JButton btnOk = new JButton("插入");
  private JButton btnCancel = new JButton("关闭");
  private JPanel pnlMain = (JPanel) this.getContentPane();
  private JTextArea txaSource = null;
  private BaseKeyAdapter keyAdapter = new BaseKeyAdapter(this);
  private BaseKeyAdapter buttonKeyAdapter = new BaseKeyAdapter(this, false);
  private BaseMouseAdapter baseMouseAdapter = new BaseMouseAdapter();
  private ButtonGroup bgpStyle = new ButtonGroup();
  private SimpleDateFormat simpleDateFormat = new SimpleDateFormat();

  /**
   * 构造方法
   * 
   * @param owner
   *          父窗口
   * @param modal
   *          是否为模式窗口
   * @param txaSource
   *          针对操作的文本域
   */
  public InsertDateDialog(JFrame owner, boolean modal, JTextArea txaSource) {
    super(owner, modal);
    if (txaSource == null) {
      return;
    }
    this.txaSource = txaSource;
    this.setTitle("插入时间/日期");
    this.init();
    this.setMnemonic();
    this.fillStyleList();
    this.setView();
    this.setComponentEnabledByRadioButton();
    this.addListeners();
    this.setSize(320, 335);
    this.setVisible(true);
  }

  /**
   * 初始化界面
   */
  private void init() {
    this.lblWarning.setForeground(Color.RED);
    this.lblWarning.setHorizontalAlignment(SwingConstants.CENTER);
    this.pnlMain.setLayout(null);
    this.radSelect.setBounds(10, 10, 150, Util.VIEW_HEIGHT);
    this.srpStyles.setBounds(10, 35, 300, 150);
    this.radUser.setBounds(10, 190, 150, Util.VIEW_HEIGHT);
    this.lblWarning.setBounds(170, 190, 140, Util.VIEW_HEIGHT);
    this.txtUser.setBounds(10, 212, 300, Util.INPUT_HEIGHT);
    this.lblView.setBounds(10, 240, 300, Util.VIEW_HEIGHT);
    this.btnOk.setBounds(45, 268, 90, Util.BUTTON_HEIGHT);
    this.btnCancel.setBounds(185, 268, 90, Util.BUTTON_HEIGHT);
    this.pnlMain.add(this.radSelect);
    this.pnlMain.add(this.srpStyles);
    this.pnlMain.add(this.radUser);
    this.pnlMain.add(this.lblWarning);
    this.pnlMain.add(this.txtUser);
    this.pnlMain.add(this.lblView);
    this.pnlMain.add(this.btnOk);
    this.pnlMain.add(this.btnCancel);
    this.bgpStyle.add(this.radSelect);
    this.bgpStyle.add(this.radUser);
  }

  /**
   * 重写父类的方法：设置本窗口是否可见
   */
  public void setVisible(boolean visible) {
    if (visible) {
      this.checkStyle();
    }
    super.setVisible(visible);
  }

  /**
   * 为各组件设置快捷键
   */
  private void setMnemonic() {
    this.radSelect.setMnemonic('S');
    this.radUser.setMnemonic('U');
  }

  /**
   * 根据单选按钮的选择，设置组件是否可用
   */
  private void setComponentEnabledByRadioButton() {
    boolean selected = this.radSelect.isSelected();
    this.listStyles.setEnabled(selected);
    this.txtUser.setEnabled(!selected);
    this.lblWarning.setEnabled(!selected);
    this.lblView.setEnabled(!selected);
  }

  /**
   * 填充"格式"列表
   */
  private void fillStyleList() {
    String[] arrStyles = new String[Util.DATE_STYLES.length];
    Date date = new Date();
    for (int n = 0; n < Util.DATE_STYLES.length; n++) {
      this.simpleDateFormat.applyPattern(Util.DATE_STYLES[n]);
      arrStyles[n] = this.simpleDateFormat.format(date);
    }
    this.listStyles.setListData(arrStyles);
    this.listStyles.setSelectedIndex(0);
    this.listStyles.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
  }

  /**
   * 检测自定义输入框是否符合规范
   */
  private void checkStyle() {
    if (this.txtUser.getText().isEmpty()) {
      this.txtUser.setText(Util.DATE_STYLES[0]);
    }
    try {
      this.simpleDateFormat.applyPattern(this.txtUser.getText());
    } catch (IllegalArgumentException x) {
      this.txtUser.setText(Util.DATE_STYLES[0]);
      // x.printStackTrace();
    }
  }

  /**
   * 根据自定义输入框是否符合规范，来更新警告标签的显示
   * 
   * @param warning
   *          是否需要显示警告，需要显示则为true，反之为false
   */
  private void updateWarning(boolean warning) {
    if (warning) {
      this.lblWarning.setText("警告：格式错误！");
    } else {
      this.lblWarning.setText("");
    }
  }

  /**
   * 设置预览标签的显示
   */
  private void setView() {
    if (!this.lblView.isEnabled()) {
      return;
    }
    try {
      this.simpleDateFormat.applyPattern(this.txtUser.getText());
      String strDate = this.simpleDateFormat.format(new Date());
      this.lblView.setText(strDate);
      this.updateWarning(false);
    } catch (IllegalArgumentException x) {
      this.updateWarning(true);
      // x.printStackTrace();
    }
  }

  /**
   * 接收鼠标事件的抽象适配器的自定义类
   * 
   * @author chen
   * 
   */
  private class BaseMouseAdapter extends MouseAdapter {
    public void mouseClicked(MouseEvent e) {
      if (listStyles.equals(e.getSource()) && listStyles.isEnabled()) {
        if (e.getClickCount() == 2) { // 鼠标双击时，执行操作
          onEnter();
        }
      }
    }
  }

  /**
   * 添加各组件的事件监听器
   */
  private void addListeners() {
    this.btnCancel.addActionListener(this);
    this.btnOk.addActionListener(this);
    this.radSelect.addActionListener(this);
    this.radUser.addActionListener(this);
    this.listStyles.addMouseListener(this.baseMouseAdapter);
    this.txtUser.addCaretListener(this);
    // 以下为各可获得焦点的组件添加键盘事件，即当用户按下Esc键时关闭对话框
    this.radSelect.addKeyListener(this.keyAdapter);
    this.listStyles.addKeyListener(this.keyAdapter);
    this.radUser.addKeyListener(this.keyAdapter);
    this.txtUser.addKeyListener(this.keyAdapter);
    this.btnCancel.addKeyListener(this.buttonKeyAdapter);
    this.btnOk.addKeyListener(this.buttonKeyAdapter);
  }

  /**
   * "关闭"按钮的处理方法
   */
  public void onCancel() {
    this.dispose();
  }

  /**
   * "插入"按钮的处理方法
   */
  public void onEnter() {
    String strStyles = "";
    if (this.radSelect.isSelected()) {
      strStyles = Util.DATE_STYLES[this.listStyles.getSelectedIndex()];
    } else {
      strStyles = this.txtUser.getText();
    }
    if (strStyles.isEmpty()) {
      JOptionPane.showMessageDialog(this, "格式不能为空！", Util.SOFTWARE,
          JOptionPane.ERROR_MESSAGE);
    }
    try {
      this.simpleDateFormat.applyPattern(strStyles);
      this.txaSource.replaceSelection(this.simpleDateFormat.format(new Date()));
    } catch (IllegalArgumentException x) {
      // x.printStackTrace();
      JOptionPane.showMessageDialog(this, "格式错误，请重新输入！", Util.SOFTWARE,
          JOptionPane.ERROR_MESSAGE);
    }
  }

  /**
   * 为各组件添加事件的处理方法
   */
  public void actionPerformed(ActionEvent e) {
    if (this.btnCancel.equals(e.getSource())) {
      this.onCancel();
    } else if (this.btnOk.equals(e.getSource())) {
      this.onEnter();
    } else if (this.radSelect.equals(e.getSource())) {
      this.setComponentEnabledByRadioButton();
    } else if (this.radUser.equals(e.getSource())) {
      this.setComponentEnabledByRadioButton();
    }
  }

  /**
   * 当文本框的光标发生变化时，触发此事件
   */
  public void caretUpdate(CaretEvent e) {
    if (this.txtUser.equals(e.getSource())) {
      this.setView();
    }
  }

}
