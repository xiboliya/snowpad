package com.xiboliya.snowpad;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * "关于"对话框
 * 
 * @author chen
 * 
 */
public class AboutDialog extends BaseDialog implements ActionListener {
  private static final long serialVersionUID = 1L;
  private int lines = 1; // 显示标签的行数
  private GridLayout layout = null;
  private JPanel pnlMain = (JPanel) this.getContentPane();
  private JPanel pnlCenter = new JPanel();
  private JPanel pnlEast = new JPanel();
  private JPanel pnlWest = new JPanel();
  private JPanel pnlSouth = new JPanel();
  private JPanel pnlNorth = new JPanel();
  private JLabel lblWest = new JLabel(" ");
  private JLabel lblNorth = new JLabel();
  private JLabel lblEast = new JLabel(" ");
  private JButton btnOk = new JButton("确定");
  private BaseKeyAdapter buttonKeyAdapter = new BaseKeyAdapter(this, false);
  private LinkedList<JLabel> labelList = new LinkedList<JLabel>(); // 存放显示标签的链表

  public AboutDialog(JFrame owner) {
    this(owner, true, 1);
  }

  public AboutDialog(JFrame owner, boolean modal) {
    this(owner, modal, 1);
  }

  public AboutDialog(JFrame owner, boolean modal, int lines) {
    super(owner, modal);
    this.checkLines(lines);
    this.init();
    this.addListeners();
  }

  public AboutDialog(JFrame owner, boolean modal, String[] arrStrLabel) {
    this(owner, modal, arrStrLabel.length);
    if (arrStrLabel.length > 0) {
      for (int i = 0; i < this.lines; i++) {
        this.labelList.get(i).setText(arrStrLabel[i]);
      }
    }
  }

  public AboutDialog(JFrame owner, boolean modal, String[] arrStrLabel,
      ImageIcon icon) {
    this(owner, modal, arrStrLabel);
    if (icon != null) {
      this.lblNorth.setIcon(icon);
    }
  }

  /**
   * 初始化界面
   */
  private void init() {
    this.setTitle("关于");
    this.pnlWest.add(this.lblWest);
    this.pnlNorth.add(this.lblNorth);
    this.pnlEast.add(this.lblEast);
    this.pnlSouth.add(this.btnOk);
    this.pnlMain.add(this.pnlWest, BorderLayout.WEST);
    this.pnlMain.add(this.pnlNorth, BorderLayout.NORTH);
    this.pnlMain.add(this.pnlEast, BorderLayout.EAST);
    this.pnlMain.add(this.pnlSouth, BorderLayout.SOUTH);
    this.layout = new GridLayout(this.lines, 1);
    this.pnlCenter.setLayout(this.layout);
    this.initLabelList();
    this.pnlMain.add(this.pnlCenter, BorderLayout.CENTER);
  }

  /**
   * 初始化链表
   */
  private void initLabelList() {
    for (int i = 0; i < this.lines; i++) {
      this.appendLabelList();
    }
  }

  /**
   * 追加一个空的显示标签
   * 
   * @return 如果追加成功则返回true，否则返回false
   */
  private boolean appendLabelList() {
    if (this.labelList.size() < this.lines) {
      JLabel lblTemp = new JLabel(" ");
      this.labelList.add(lblTemp);
      this.pnlCenter.add(lblTemp);
      return true;
    }
    return false;
  }

  /**
   * 格式化显示标签行数
   * 
   * @param lines
   *          显示标签的行数
   * @return 经过格式化后的显示标签行数
   */
  private int checkLines(int lines) {
    if (lines <= 0) {
      this.lines = 1;
    } else {
      this.lines = lines;
    }
    return this.lines;
  }

  /**
   * 格式化下标
   * 
   * @param index
   *          下标
   * @return 经过格式化后的下标
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
   * 添加事件监听器
   */
  private void addListeners() {
    this.btnOk.addActionListener(this);
    this.btnOk.addKeyListener(this.buttonKeyAdapter);
  }

  /**
   * 为指定下标的标签设置链接
   * 
   * @param index
   *          标签的下标
   * @param strLink
   *          链接字符串
   */
  public void addLinkByIndex(int index, final String strLink) {
    if (index < 0 || index >= lines || strLink == null || strLink.isEmpty()) {
      return;
    }
    JLabel lblTemp = this.labelList.get(index);
    lblTemp.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    lblTemp.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        try {
          if (Util.OS_NAME.indexOf("Windows") >= 0) {
            // 当用鼠标点击此标签时，将调用系统命令，打开网页
            Runtime.getRuntime().exec("cmd /c start " + strLink);
          } else { // 如为非Windows系统，则试图使用特定浏览器打开
            openLinkByBrowser(0, strLink);
          }
        } catch (Exception x) {
          // 如果操作系统不支持上述命令，将抛出异常
          x.printStackTrace();
        }
      }
    });
  }

  /**
   * 使用特定的浏览器打开链接
   * 
   * @param index
   *          浏览器数组的索引
   * @param strLink
   *          链接字符串
   */
  private void openLinkByBrowser(int index, final String strLink) {
    String[] arrBrowser = new String[] { "firefox", "opera", "chrome" };
    if (index < 0 || index >= arrBrowser.length) {
      return;
    } else {
      try {
        Runtime.getRuntime().exec(arrBrowser[index] + " " + strLink);
      } catch (Exception x) {
        // 如果未能正常打开链接，则递归调用本方法，试图使用上述数组中的下一个浏览器打开
        this.openLinkByBrowser(++index, strLink);
        x.printStackTrace();
      }
    }
  }

  /**
   * 为指定下标的标签设置字符串
   * 
   * @param index
   *          标签的下标
   * @param strLabel
   *          设置的字符串
   */
  public void setStringByIndex(int index, String strLabel) {
    this.labelList.get(this.checkIndex(index)).setText(strLabel);
  }

  /**
   * 获取指定下标的标签的字符串
   * 
   * @param index
   *          标签的下标
   * @return 标签的字符串
   */
  public String getStringByIndex(int index) {
    return this.labelList.get(this.checkIndex(index)).getText();
  }

  /**
   * 为各组件添加事件的处理方法
   */
  public void actionPerformed(ActionEvent e) {
    if (this.btnOk.equals(e.getSource())) {
      this.onCancel();
    }
  }

  /**
   * 默认的"确定"操作方法
   */
  public void onEnter() {
    this.onCancel();
  }

  /**
   * 默认的"取消"操作方法
   */
  public void onCancel() {
    this.dispose();
  }
}
