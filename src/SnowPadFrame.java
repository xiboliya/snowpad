package com.xiboliya.snowpad;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.undo.UndoManager;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Hashtable;
import java.util.LinkedList;

/**
 * 冰雪记事本 打造一个与Windows的“记事本”功能相同的java版本。
 * 当然这只是最低要求，最终目标是实现一个可以同时在Windows和Linux下运行的增强型记事本。
 * 百度空间：http://hi.baidu.com/xiboliya 谷歌代码：http://code.google.com/p/snowpad
 * GitHub：https://github.com/xiboliya/snowpad
 * 
 * @author 冰原
 * 
 */
public class SnowPadFrame extends JFrame implements ActionListener,
    CaretListener, UndoableEditListener, WindowFocusListener, ChangeListener {
  private static final long serialVersionUID = 1L; // 序列化运行时使用的一个版本号，以与当前可序列化类相关联
  private BaseTextArea txaMain = null; // 当前编辑的文本域
  private JTabbedPane tpnMain = new JTabbedPane(); // 显示文本域的选项卡组件
  private JMenuBar menuBar = new JMenuBar();
  private JMenu menuFile = new JMenu("文件(F)");
  private JMenuItem itemNew = new JMenuItem("新建(N)", 'N');
  private JMenuItem itemOpen = new JMenuItem("打开(O)...", 'O');
  private JMenuItem itemOpenByEncoding = new JMenuItem("以指定编码打开(E)...", 'E');
  private JMenuItem itemReOpen = new JMenuItem("重新载入文件(L)", 'L');
  private JMenuItem itemReName = new JMenuItem("重命名(R)...", 'R');
  private JMenuItem itemSave = new JMenuItem("保存(S)", 'S');
  private JMenuItem itemSaveAs = new JMenuItem("另存为(A)...", 'A');
  private JMenuItem itemClose = new JMenuItem("关闭(C)", 'C');
  private JMenuItem itemCloseAll = new JMenuItem("全部关闭(Q)", 'Q');
  private JMenuItem itemDelFile = new JMenuItem("删除当前文件(D)", 'D');
  private JMenu menuFileHistory = new JMenu("最近编辑");
  private JMenuItem itemClearFileHistory = new JMenuItem("清空最近编辑列表");
  private JMenuItem itemExit = new JMenuItem("退出(X)", 'X');
  private JMenu menuEdit = new JMenu("编辑(E)");
  private JMenuItem itemUnDo = new JMenuItem("撤销(U)", 'U');
  private JMenuItem itemReDo = new JMenuItem("重做(Y)", 'Y');
  private JMenuItem itemCut = new JMenuItem("剪切(T)", 'T');
  private JMenuItem itemCopy = new JMenuItem("复制(C)", 'C');
  private JMenuItem itemPaste = new JMenuItem("粘贴(P)", 'P');
  private JMenuItem itemDel = new JMenuItem("删除(L)", 'L');
  private JMenu menuCase = new JMenu("切换大小写");
  private JMenuItem itemCaseUp = new JMenuItem("切换为大写");
  private JMenuItem itemCaseLow = new JMenuItem("切换为小写");
  private JMenu menuCopyToClip = new JMenu("复制到剪贴板");
  private JMenuItem itemToCopyFileName = new JMenuItem("当前文件名");
  private JMenuItem itemToCopyFilePath = new JMenuItem("当前文件路径");
  private JMenuItem itemToCopyDirPath = new JMenuItem("当前目录路径");
  private JMenuItem itemToCopyAllText = new JMenuItem("所有文本");
  private JMenu menuLine = new JMenu("操作行");
  private JMenuItem itemLineCopy = new JMenuItem("复写当前行");
  private JMenuItem itemLineDel = new JMenuItem("删除当前行");
  private JMenuItem itemLineDelToStart = new JMenuItem("删除至行首");
  private JMenuItem itemLineDelToEnd = new JMenuItem("删除至行尾");
  private JMenuItem itemLineToUp = new JMenuItem("上移当前行");
  private JMenuItem itemLineToDown = new JMenuItem("下移当前行");
  private JMenuItem itemLineToCopy = new JMenuItem("复制当前行");
  private JMenuItem itemLineToCut = new JMenuItem("剪切当前行");
  private JMenu menuLineBatch = new JMenu("批处理行");
  private JMenuItem itemLineBatchRemove = new JMenuItem("切除(R)...", 'R');
  private JMenuItem itemLineBatchInsert = new JMenuItem("插入(I)...", 'I');
  private JMenuItem itemLineBatchSeparate = new JMenuItem("分割行(S)...", 'S');
  private JMenuItem itemLineBatchMerge = new JMenuItem("合并行(M)", 'M');
  private JMenu menuSort = new JMenu("排序");
  private JMenuItem itemSortUp = new JMenuItem("升序");
  private JMenuItem itemSortDown = new JMenuItem("降序");
  private JMenu menuIndent = new JMenu("缩进");
  private JMenuItem itemIndentAdd = new JMenuItem("缩进");
  private JMenuItem itemIndentBack = new JMenuItem("退格");
  private JMenuItem itemSelCopy = new JMenuItem("复写当前选择(W)", 'W');
  private JMenu menuTrim = new JMenu("清除空白");
  private JMenuItem itemTrimStart = new JMenuItem("行首");
  private JMenuItem itemTrimEnd = new JMenuItem("行尾");
  private JMenuItem itemTrimAll = new JMenuItem("行首+行尾");
  private JMenuItem itemTrimSelected = new JMenuItem("选区内");
  private JMenu menuDelNullLine = new JMenu("删除空行");
  private JMenuItem itemDelNullLineAll = new JMenuItem("全文范围");
  private JMenuItem itemDelNullLineSelected = new JMenuItem("选区范围");
  private JMenuItem itemSelAll = new JMenuItem("全选(A)", 'A');
  private JMenu menuInsert = new JMenu("插入(I)");
  private JMenuItem itemInsertDateTime = new JMenuItem("时间/日期(D)...", 'D');
  private JMenuItem itemInsertChar = new JMenuItem("特殊字符(S)...", 'S');
  private JMenu menuSearch = new JMenu("搜索(S)");
  private JMenuItem itemFind = new JMenuItem("查找(F)...", 'F');
  private JMenuItem itemFindNext = new JMenuItem("查找下一个(N)", 'N');
  private JMenuItem itemFindPrevious = new JMenuItem("查找上一个(P)", 'P');
  private JMenuItem itemSelFindNext = new JMenuItem("选定查找下一个(T)", 'T');
  private JMenuItem itemSelFindPrevious = new JMenuItem("选定查找上一个(S)", 'S');
  private JMenu menuQuickFind = new JMenu("快速查找");
  private JMenuItem itemQuickFindDown = new JMenuItem("快速向下查找");
  private JMenuItem itemQuickFindUp = new JMenuItem("快速向上查找");
  private JMenuItem itemReplace = new JMenuItem("替换(R)...", 'R');
  private JMenuItem itemGoto = new JMenuItem("转到(G)...", 'G');
  private JMenu menuStyle = new JMenu("格式(O)");
  private JMenu menuLineWrapSet = new JMenu("换行设置(L)");
  private JCheckBoxMenuItem itemLineWrap = new JCheckBoxMenuItem("自动换行(W)");
  private JMenu menuLineWrapStyle = new JMenu("换行方式(S)");
  private JRadioButtonMenuItem itemLineWrapByWord = new JRadioButtonMenuItem(
      "单词边界换行(W)");
  private JRadioButtonMenuItem itemLineWrapByChar = new JRadioButtonMenuItem(
      "字符边界换行(C)");
  private JMenuItem itemFont = new JMenuItem("字体(F)...", 'F');
  private JMenuItem itemTabSet = new JMenuItem("Tab键设置...", 'T');
  private JCheckBoxMenuItem itemTextDrag = new JCheckBoxMenuItem("文本拖拽(D)");
  private JCheckBoxMenuItem itemAutoIndent = new JCheckBoxMenuItem("自动缩进(I)");
  private JMenu menuLineStyle = new JMenu("换行符格式(S)");
  private JRadioButtonMenuItem itemLineStyleWin = new JRadioButtonMenuItem(
      LineSeparator.WINDOWS.getName() + "格式");
  private JRadioButtonMenuItem itemLineStyleUnix = new JRadioButtonMenuItem(
      LineSeparator.UNIX.getName() + "格式");
  private JRadioButtonMenuItem itemLineStyleMac = new JRadioButtonMenuItem(
      LineSeparator.MACINTOSH.getName() + "格式");
  private JMenu menuCharset = new JMenu("字符编码格式(C)");
  private JRadioButtonMenuItem itemCharsetBASE = new JRadioButtonMenuItem(
      "默认格式(" + CharEncoding.BASE.toString() + ")");
  private JRadioButtonMenuItem itemCharsetANSI = new JRadioButtonMenuItem(
      "ANSI格式");
  private JRadioButtonMenuItem itemCharsetUTF8 = new JRadioButtonMenuItem(
      "UTF-8格式");
  private JRadioButtonMenuItem itemCharsetUTF8_NO_BOM = new JRadioButtonMenuItem(
      "UTF-8 No BOM格式");
  private JRadioButtonMenuItem itemCharsetULE = new JRadioButtonMenuItem(
      "Unicode Little Endian格式");
  private JRadioButtonMenuItem itemCharsetUBE = new JRadioButtonMenuItem(
      "Unicode Big Endian格式");
  private JMenuItem itemSignIdentifier = new JMenuItem("列表符号与编号(G)...", 'G');
  private JMenu menuView = new JMenu("查看(V)");
  private JCheckBoxMenuItem itemStateBar = new JCheckBoxMenuItem("状态栏(S)");
  private JCheckBoxMenuItem itemLineNumber = new JCheckBoxMenuItem("行号栏(L)");
  private JCheckBoxMenuItem itemAlwaysOnTop = new JCheckBoxMenuItem("前端显示(A)");
  private JCheckBoxMenuItem itemResizable = new JCheckBoxMenuItem("锁定窗口(R)");
  private JCheckBoxMenuItem itemTabPolicy = new JCheckBoxMenuItem("多行标签(P)");
  private JCheckBoxMenuItem itemClickToClose = new JCheckBoxMenuItem(
      "双击关闭标签(D)");
  private JMenu menuFontSize = new JMenu("字体缩放(F)");
  private JMenuItem itemFontSizePlus = new JMenuItem("放大(B)", 'B');
  private JMenuItem itemFontSizeMinus = new JMenuItem("缩小(S)", 'S');
  private JMenuItem itemFontSizeReset = new JMenuItem("恢复初始(D)", 'D');
  private JMenu menuColor = new JMenu("颜色设置(C)");
  private JMenuItem itemColorFont = new JMenuItem("字体颜色(F)...", 'F');
  private JMenuItem itemColorBack = new JMenuItem("背景颜色(B)...", 'B');
  private JMenuItem itemColorCaret = new JMenuItem("光标颜色(C)...", 'C');
  private JMenuItem itemColorSelFont = new JMenuItem("选区字体颜色(T)...", 'T');
  private JMenuItem itemColorSelBack = new JMenuItem("选区背景颜色(K)...", 'K');
  private JMenuItem itemColorAnti = new JMenuItem("全部反色(A)", 'A');
  private JMenuItem itemColorComplementary = new JMenuItem("全部补色(R)", 'R');
  private JMenu menuColorStyle = new JMenu("配色方案(Y)");
  private JMenuItem itemColorStyle1 = new JMenuItem("配色方案(1)", '1');
  private JMenuItem itemColorStyle2 = new JMenuItem("配色方案(2)", '2');
  private JMenuItem itemColorStyle3 = new JMenuItem("配色方案(3)", '3');
  private JMenuItem itemColorStyle4 = new JMenuItem("配色方案(4)", '4');
  private JMenuItem itemColorStyle5 = new JMenuItem("配色方案(5)", '5');
  private JMenuItem itemColorStyleDefault = new JMenuItem("恢复默认配色(0)", '0');
  private JMenu menuHighlight = new JMenu("高亮显示(H)");
  private JMenuItem itemHighlight1 = new JMenuItem("格式(1)", '1');
  private JMenuItem itemHighlight2 = new JMenuItem("格式(2)", '2');
  private JMenuItem itemHighlight3 = new JMenuItem("格式(3)", '3');
  private JMenuItem itemHighlight4 = new JMenuItem("格式(4)", '4');
  private JMenuItem itemHighlight5 = new JMenuItem("格式(5)", '5');
  private JMenu menuRmHighlight = new JMenu("清除高亮(M)");
  private JMenuItem itemRmHighlight1 = new JMenuItem("格式(1)", '1');
  private JMenuItem itemRmHighlight2 = new JMenuItem("格式(2)", '2');
  private JMenuItem itemRmHighlight3 = new JMenuItem("格式(3)", '3');
  private JMenuItem itemRmHighlight4 = new JMenuItem("格式(4)", '4');
  private JMenuItem itemRmHighlight5 = new JMenuItem("格式(5)", '5');
  private JMenuItem itemRmHighlightAll = new JMenuItem("所有格式(0)", '0');
  private JMenu menuLookAndFeel = new JMenu("切换外观(K)");
  private JMenu menuHelp = new JMenu("帮助(H)");
  private JMenuItem itemHelp = new JMenuItem("帮助主题(H)", 'H');
  private JMenuItem itemAbout = new JMenuItem("关于记事本(A)", 'A');
  private JPopupMenu popMenuMain = new JPopupMenu();
  private JMenuItem itemPopUnDo = new JMenuItem("撤销(U)", 'U');
  private JMenuItem itemPopReDo = new JMenuItem("重做(Y)", 'Y');
  private JMenuItem itemPopCut = new JMenuItem("剪切(T)", 'T');
  private JMenuItem itemPopCopy = new JMenuItem("复制(C)", 'C');
  private JMenuItem itemPopPaste = new JMenuItem("粘贴(P)", 'P');
  private JMenuItem itemPopDel = new JMenuItem("删除(D)", 'D');
  private JMenuItem itemPopSelAll = new JMenuItem("全选(A)", 'A');
  private JMenu menuPopHighlight = new JMenu("高亮显示(H)");
  private JMenuItem itemPopHighlight1 = new JMenuItem("格式(1)", '1');
  private JMenuItem itemPopHighlight2 = new JMenuItem("格式(2)", '2');
  private JMenuItem itemPopHighlight3 = new JMenuItem("格式(3)", '3');
  private JMenuItem itemPopHighlight4 = new JMenuItem("格式(4)", '4');
  private JMenuItem itemPopHighlight5 = new JMenuItem("格式(5)", '5');
  private JMenu menuPopRmHighlight = new JMenu("清除高亮(M)");
  private JMenuItem itemPopRmHighlight1 = new JMenuItem("格式(1)", '1');
  private JMenuItem itemPopRmHighlight2 = new JMenuItem("格式(2)", '2');
  private JMenuItem itemPopRmHighlight3 = new JMenuItem("格式(3)", '3');
  private JMenuItem itemPopRmHighlight4 = new JMenuItem("格式(4)", '4');
  private JMenuItem itemPopRmHighlight5 = new JMenuItem("格式(5)", '5');
  private JMenuItem itemPopRmHighlightAll = new JMenuItem("所有格式(0)", '0');
  private JPopupMenu popMenuTabbed = new JPopupMenu();
  private JMenuItem itemPopCloseCurrent = new JMenuItem("关闭当前(C)", 'C');
  private JMenuItem itemPopCloseOthers = new JMenuItem("关闭其它(O)", 'O');
  private JMenuItem itemPopSave = new JMenuItem("保存(S)", 'S');
  private JMenuItem itemPopSaveAs = new JMenuItem("另存为(A)...", 'A');
  private JMenuItem itemPopReName = new JMenuItem("重命名(N)...", 'N');
  private JMenuItem itemPopDelFile = new JMenuItem("删除文件(D)", 'D');
  private JMenuItem itemPopReOpen = new JMenuItem("重新载入(R)", 'R');
  private JMenuItem itemPopToCopyFileName = new JMenuItem("复制文件名(F)", 'F');
  private JMenuItem itemPopToCopyFilePath = new JMenuItem("复制文件路径(P)", 'P');
  private JMenuItem itemPopToCopyDirPath = new JMenuItem("复制目录路径(I)", 'I');

  private ButtonGroup bgpLineWrapStyle = new ButtonGroup(); // 用于存放换行方式的按钮组
  private ButtonGroup bgpLineStyle = new ButtonGroup(); // 用于存放换行符格式的按钮组
  private ButtonGroup bgpCharset = new ButtonGroup(); // 用于存放字符编码格式的按钮组
  private ButtonGroup bgpLookAndFeel = new ButtonGroup(); // 用于存放外观的按钮组
  private Clipboard clip = this.getToolkit().getSystemClipboard(); // 剪贴板
  private Color[] defColorStyle = null; // 文本域默认配色方案
  private File file = null; // 当前编辑的文件
  private ImageIcon icon = null; // 本程序图标
  private LinkedList<String> fileHistoryList = new LinkedList<String>(); // 存放最近编辑的文件名的链表
  private LinkedList<BaseTextArea> textAreaList = new LinkedList<BaseTextArea>(); // 存放界面中所有文本域的链表
  private StringBuilder stbTitle = new StringBuilder(Util.SOFTWARE); // 标题栏字符串
  private String strLookAndFeel = Util.SYSTEM_LOOK_AND_FEEL_CLASS_NAME; // 当前外观的完整类名
  private StatePanel pnlState = new StatePanel(4); // 状态栏面板
  private UndoManager undoManager = null; // 撤销管理器
  private Setting setting = new Setting(); // 文本域参数配置类
  private boolean clickToClose = true; // 是否双击关闭当前标签

  private OpenFileChooser openFileChooser = null; // "打开"文件选择器
  private SaveFileChooser saveFileChooser = null; // "保存"文件选择器
  private FontChooser fontChooser = null; // 字体对话框
  private FindReplaceDialog findReplaceDialog = null; // 查找、替换对话框
  private GotoDialog gotoDialog = null; // 转到对话框
  private AboutDialog aboutDialog = null; // 关于对话框
  private TabSetDialog tabSetDialog = null; // Tab字符设置对话框
  private InsertCharDialog insertCharDialog = null; // 插入字符对话框
  private InsertDateDialog insertDateDialog = null; // 插入时间/日期对话框
  private FileEncodingDialog fileEncodingDialog = null; // 文件编码格式对话框
  private BatchRemoveDialog batchRemoveDialog = null; // 批处理"切除"对话框
  private BatchInsertDialog batchInsertDialog = null; // 批处理"插入"对话框
  private BatchSeparateDialog batchSeparateDialog = null; // 批处理"分割行"对话框
  private SignIdentifierDialog signIdentifierDialog = null; // 项目符号与编号对话框

  /**
   * 构造方法 用于初始化界面和设置
   */
  public SnowPadFrame() {
    this.setTitle(this.stbTitle.toString());
    this.setSize(600, 500);
    this.setMinimumSize(new Dimension(200, 200)); // 设置主界面的最小尺寸
    this.setLocationRelativeTo(null); // 使窗口居中显示
    this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // 设置默认关闭操作为空，以便添加窗口监听事件
    this.init();
    this.setIcon();
    this.setVisible(true);
  }

  /**
   * 带参数的构造方法，用于在程序启动的同时打开文件
   * 
   * @param strFile
   *          表示文件路径的字符串。
   *          可以是绝对路径，如：E:\file\test.txt或/home/chen/test.txt等。也可以是相对路径
   *          ，如：../test.txt或chen/test.txt或test.txt等。
   */
  public SnowPadFrame(String strFile) {
    this();
    if (strFile != null && !strFile.isEmpty()) {
      File file = new File(strFile);
      if (file.exists()) {
        this.toOpenFile(file, true, false);
        this.setAfterOpenFile(Util.DEFAULT_CARET_INDEX);
        this.setFileNameAndPath(file);
      }
    }
  }

  /**
   * 设置自定义的窗口图标
   */
  private void setIcon() {
    try {
      this.icon = new ImageIcon(ClassLoader.getSystemResource("res/icon.gif"));
      this.setIconImage(icon.getImage());
    } catch (Exception x) {
      x.printStackTrace();
    }
  }

  /**
   * 初始化界面和添加监听器
   */
  private void init() {
    this.addTabbedPane();
    this.addMenuItem();
    this.addStatePanel();
    this.addPopMenu();
    this.setMenuDefault();
    this.setMenuDefaultInit();
    this.setMenuMnemonic();
    this.addListeners();
  }

  /**
   * 添加各组件的事件监听器
   */
  private void addListeners() {
    this.itemAbout.addActionListener(this);
    this.itemCopy.addActionListener(this);
    this.itemCut.addActionListener(this);
    this.itemInsertChar.addActionListener(this);
    this.itemInsertDateTime.addActionListener(this);
    this.itemDel.addActionListener(this);
    this.itemCaseUp.addActionListener(this);
    this.itemCaseLow.addActionListener(this);
    this.itemQuickFindDown.addActionListener(this);
    this.itemQuickFindUp.addActionListener(this);
    this.itemExit.addActionListener(this);
    this.itemFind.addActionListener(this);
    this.itemFindNext.addActionListener(this);
    this.itemFindPrevious.addActionListener(this);
    this.itemSelFindNext.addActionListener(this);
    this.itemSelFindPrevious.addActionListener(this);
    this.itemFont.addActionListener(this);
    this.itemTabSet.addActionListener(this);
    this.itemGoto.addActionListener(this);
    this.itemToCopyFileName.addActionListener(this);
    this.itemToCopyFilePath.addActionListener(this);
    this.itemToCopyDirPath.addActionListener(this);
    this.itemToCopyAllText.addActionListener(this);
    this.itemLineCopy.addActionListener(this);
    this.itemLineDel.addActionListener(this);
    this.itemLineDelToStart.addActionListener(this);
    this.itemLineDelToEnd.addActionListener(this);
    this.itemLineToUp.addActionListener(this);
    this.itemLineToDown.addActionListener(this);
    this.itemLineToCopy.addActionListener(this);
    this.itemLineToCut.addActionListener(this);
    this.itemSortUp.addActionListener(this);
    this.itemSortDown.addActionListener(this);
    this.itemIndentAdd.addActionListener(this);
    this.itemIndentBack.addActionListener(this);
    this.itemSelCopy.addActionListener(this);
    this.itemLineBatchRemove.addActionListener(this);
    this.itemLineBatchInsert.addActionListener(this);
    this.itemLineBatchSeparate.addActionListener(this);
    this.itemLineBatchMerge.addActionListener(this);
    this.itemTrimStart.addActionListener(this);
    this.itemTrimEnd.addActionListener(this);
    this.itemTrimAll.addActionListener(this);
    this.itemTrimSelected.addActionListener(this);
    this.itemDelNullLineAll.addActionListener(this);
    this.itemDelNullLineSelected.addActionListener(this);
    this.itemHelp.addActionListener(this);
    this.itemLineWrap.addActionListener(this);
    this.itemLineWrapByWord.addActionListener(this);
    this.itemLineWrapByChar.addActionListener(this);
    this.itemLineStyleWin.addActionListener(this);
    this.itemLineStyleUnix.addActionListener(this);
    this.itemLineStyleMac.addActionListener(this);
    this.itemCharsetBASE.addActionListener(this);
    this.itemCharsetANSI.addActionListener(this);
    this.itemCharsetUTF8.addActionListener(this);
    this.itemCharsetUTF8_NO_BOM.addActionListener(this);
    this.itemCharsetULE.addActionListener(this);
    this.itemCharsetUBE.addActionListener(this);
    this.itemSignIdentifier.addActionListener(this);
    this.itemTextDrag.addActionListener(this);
    this.itemAutoIndent.addActionListener(this);
    this.itemAlwaysOnTop.addActionListener(this);
    this.itemResizable.addActionListener(this);
    this.itemTabPolicy.addActionListener(this);
    this.itemClickToClose.addActionListener(this);
    this.itemFontSizePlus.addActionListener(this);
    this.itemFontSizeMinus.addActionListener(this);
    this.itemFontSizeReset.addActionListener(this);
    this.itemColorFont.addActionListener(this);
    this.itemColorBack.addActionListener(this);
    this.itemColorCaret.addActionListener(this);
    this.itemColorSelFont.addActionListener(this);
    this.itemColorSelBack.addActionListener(this);
    this.itemColorAnti.addActionListener(this);
    this.itemColorComplementary.addActionListener(this);
    this.itemColorStyle1.addActionListener(this);
    this.itemColorStyle2.addActionListener(this);
    this.itemColorStyle3.addActionListener(this);
    this.itemColorStyle4.addActionListener(this);
    this.itemColorStyle5.addActionListener(this);
    this.itemColorStyleDefault.addActionListener(this);
    this.itemHighlight1.addActionListener(this);
    this.itemHighlight2.addActionListener(this);
    this.itemHighlight3.addActionListener(this);
    this.itemHighlight4.addActionListener(this);
    this.itemHighlight5.addActionListener(this);
    this.itemRmHighlight1.addActionListener(this);
    this.itemRmHighlight2.addActionListener(this);
    this.itemRmHighlight3.addActionListener(this);
    this.itemRmHighlight4.addActionListener(this);
    this.itemRmHighlight5.addActionListener(this);
    this.itemRmHighlightAll.addActionListener(this);
    this.itemNew.addActionListener(this);
    this.itemOpen.addActionListener(this);
    this.itemOpenByEncoding.addActionListener(this);
    this.itemReOpen.addActionListener(this);
    this.itemReName.addActionListener(this);
    this.itemPaste.addActionListener(this);
    this.itemPopCopy.addActionListener(this);
    this.itemPopCut.addActionListener(this);
    this.itemPopDel.addActionListener(this);
    this.itemPopPaste.addActionListener(this);
    this.itemPopSelAll.addActionListener(this);
    this.itemPopUnDo.addActionListener(this);
    this.itemPopReDo.addActionListener(this);
    this.itemPopHighlight1.addActionListener(this);
    this.itemPopHighlight2.addActionListener(this);
    this.itemPopHighlight3.addActionListener(this);
    this.itemPopHighlight4.addActionListener(this);
    this.itemPopHighlight5.addActionListener(this);
    this.itemPopRmHighlight1.addActionListener(this);
    this.itemPopRmHighlight2.addActionListener(this);
    this.itemPopRmHighlight3.addActionListener(this);
    this.itemPopRmHighlight4.addActionListener(this);
    this.itemPopRmHighlight5.addActionListener(this);
    this.itemPopRmHighlightAll.addActionListener(this);
    this.itemPopCloseCurrent.addActionListener(this);
    this.itemPopCloseOthers.addActionListener(this);
    this.itemPopSave.addActionListener(this);
    this.itemPopSaveAs.addActionListener(this);
    this.itemPopReName.addActionListener(this);
    this.itemPopDelFile.addActionListener(this);
    this.itemPopReOpen.addActionListener(this);
    this.itemPopToCopyFileName.addActionListener(this);
    this.itemPopToCopyFilePath.addActionListener(this);
    this.itemPopToCopyDirPath.addActionListener(this);
    this.itemReplace.addActionListener(this);
    this.itemSave.addActionListener(this);
    this.itemSaveAs.addActionListener(this);
    this.itemClose.addActionListener(this);
    this.itemCloseAll.addActionListener(this);
    this.itemDelFile.addActionListener(this);
    this.itemClearFileHistory.addActionListener(this);
    this.itemSelAll.addActionListener(this);
    this.itemStateBar.addActionListener(this);
    this.itemLineNumber.addActionListener(this);
    this.itemReDo.addActionListener(this);
    this.itemUnDo.addActionListener(this);
    // 为窗口添加事件监听器
    this.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        exit();
      }
    });
    // 为窗口添加焦点监听器
    this.addWindowFocusListener(this);
    this.addTabbedPaneMouseListener();
  }

  /**
   * 为文本域添加鼠标事件监听器
   */
  private void addTextAreaMouseListener() {
    this.txaMain.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) { // 点击右键时，显示快捷菜单
          popMenuMain.show(txaMain, e.getX(), e.getY());
        }
      }
    });
  }

  /**
   * 为选项卡组件添加鼠标事件监听器
   */
  private void addTabbedPaneMouseListener() {
    tpnMain.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        Rectangle rect = tpnMain.getBoundsAt(tpnMain.getSelectedIndex());
        if (rect.contains(e.getX(), e.getY())) { // 当点击区域位于当前选项卡范围内时，执行相应的操作
          if (e.getButton() == MouseEvent.BUTTON3) { // 点击右键时，显示快捷菜单
            popMenuTabbed.show(tpnMain, e.getX(), e.getY());
          } else if (e.getClickCount() == 2) { // 双击时，关闭当前标签
            if (clickToClose) {
              closeFile(true);
            }
          }
        }
      }
    });
  }

  /**
   * 获取当前编辑的文本域
   * 
   * @return 当前编辑的文本域，如果不存在则返回null
   */
  private BaseTextArea getCurrentTextArea() {
    try {
      JViewport viewport = ((JScrollPane) this.tpnMain.getSelectedComponent())
          .getViewport();
      return (BaseTextArea) viewport.getView();
    } catch (Exception x) {
      return null;
    }
  }

  /**
   * 主面板上添加选项卡视图
   */
  private void addTabbedPane() {
    this.getContentPane().add(this.tpnMain, BorderLayout.CENTER);
    this.tpnMain.setFocusable(false);
    this.tpnMain.setFont(Util.GLOBAL_FONT);
    this.tpnMain.addChangeListener(this);
    this.createNew(null);
    this.defColorStyle = new Color[] { this.txaMain.getForeground(),
        this.txaMain.getBackground(), this.txaMain.getCaretColor(),
        this.txaMain.getSelectedTextColor(), this.txaMain.getSelectionColor() };
  }

  /**
   * 主面板上添加状态栏
   */
  private void addStatePanel() {
    String strStateChars = Util.STATE_CHARS + "0";
    String strStateLines = Util.STATE_LINES + "1";
    String strStateCurLn = Util.STATE_CUR_LINE + "1";
    String strStateCurCol = Util.STATE_CUR_COLUMN + "1";
    String strStateCurSel = Util.STATE_CUR_SELECT + "0";
    String strStateLineStyle = Util.STATE_LINE_STYLE;
    String strStateEncoding = Util.STATE_ENCODING;
    this.pnlState.setStringByIndex(0, strStateChars + ", " + strStateLines,
        StatePanelAlignment.X_CENTER);
    this.pnlState.setStringByIndex(1, strStateCurLn + ", " + strStateCurCol
        + ", " + strStateCurSel);
    this.pnlState.setStringByIndex(2, strStateLineStyle);
    this.pnlState.setStringByIndex(3, strStateEncoding);
    this.getContentPane().add(this.pnlState, BorderLayout.SOUTH);
  }

  /**
   * 主面板上添加菜单栏
   */
  private void addMenuItem() {
    this.setJMenuBar(this.menuBar);
    this.menuBar.add(this.menuFile);
    this.menuFile.add(this.itemNew);
    this.menuFile.add(this.itemOpen);
    this.menuFile.add(this.itemOpenByEncoding);
    this.menuFile.add(this.itemReOpen);
    this.menuFile.add(this.itemReName);
    this.menuFile.add(this.itemSave);
    this.menuFile.add(this.itemSaveAs);
    this.menuFile.addSeparator();
    this.menuFile.add(this.itemClose);
    this.menuFile.add(this.itemCloseAll);
    this.menuFile.addSeparator();
    this.menuFile.add(this.itemDelFile);
    this.menuFile.add(this.menuFileHistory);
    this.menuFile.add(this.itemClearFileHistory);
    this.menuFile.addSeparator();
    this.menuFile.add(this.itemExit);
    this.menuBar.add(this.menuEdit);
    this.menuEdit.add(this.itemUnDo);
    this.menuEdit.add(this.itemReDo);
    this.menuEdit.addSeparator();
    this.menuEdit.add(this.itemCut);
    this.menuEdit.add(this.itemCopy);
    this.menuEdit.add(this.itemPaste);
    this.menuEdit.add(this.itemSelAll);
    this.menuEdit.add(this.itemDel);
    this.menuEdit.addSeparator();
    this.menuEdit.add(this.menuCase);
    this.menuCase.add(this.itemCaseUp);
    this.menuCase.add(this.itemCaseLow);
    this.menuEdit.add(this.menuCopyToClip);
    this.menuCopyToClip.add(this.itemToCopyFileName);
    this.menuCopyToClip.add(this.itemToCopyFilePath);
    this.menuCopyToClip.add(this.itemToCopyDirPath);
    this.menuCopyToClip.addSeparator();
    this.menuCopyToClip.add(this.itemToCopyAllText);
    this.menuEdit.add(this.menuLine);
    this.menuLine.add(this.itemLineCopy);
    this.menuLine.add(this.itemLineDel);
    this.menuLine.addSeparator();
    this.menuLine.add(this.itemLineDelToStart);
    this.menuLine.add(this.itemLineDelToEnd);
    this.menuLine.addSeparator();
    this.menuLine.add(this.itemLineToUp);
    this.menuLine.add(this.itemLineToDown);
    this.menuLine.addSeparator();
    this.menuLine.add(this.itemLineToCopy);
    this.menuLine.add(this.itemLineToCut);
    this.menuEdit.add(this.menuLineBatch);
    this.menuLineBatch.add(this.itemLineBatchRemove);
    this.menuLineBatch.add(this.itemLineBatchInsert);
    this.menuLineBatch.add(this.itemLineBatchSeparate);
    this.menuLineBatch.add(this.itemLineBatchMerge);
    this.menuEdit.add(this.menuSort);
    this.menuSort.add(this.itemSortUp);
    this.menuSort.add(this.itemSortDown);
    this.menuEdit.add(this.menuIndent);
    this.menuIndent.add(this.itemIndentAdd);
    this.menuIndent.add(this.itemIndentBack);
    this.menuEdit.add(this.menuTrim);
    this.menuTrim.add(this.itemTrimStart);
    this.menuTrim.add(this.itemTrimEnd);
    this.menuTrim.add(this.itemTrimAll);
    this.menuTrim.addSeparator();
    this.menuTrim.add(this.itemTrimSelected);
    this.menuEdit.add(this.menuDelNullLine);
    this.menuDelNullLine.add(this.itemDelNullLineAll);
    this.menuDelNullLine.add(this.itemDelNullLineSelected);
    this.menuEdit.add(this.itemSelCopy);
    this.menuEdit.add(this.menuInsert);
    this.menuInsert.add(this.itemInsertChar);
    this.menuInsert.add(this.itemInsertDateTime);
    this.menuBar.add(this.menuSearch);
    this.menuSearch.add(this.itemFind);
    this.menuSearch.add(this.itemFindNext);
    this.menuSearch.add(this.itemFindPrevious);
    this.menuSearch.add(this.itemSelFindNext);
    this.menuSearch.add(this.itemSelFindPrevious);
    this.menuSearch.add(this.menuQuickFind);
    this.menuQuickFind.add(this.itemQuickFindDown);
    this.menuQuickFind.add(this.itemQuickFindUp);
    this.menuSearch.add(this.itemReplace);
    this.menuSearch.add(this.itemGoto);
    this.menuBar.add(this.menuStyle);
    this.menuStyle.add(this.menuLineWrapSet);
    this.menuLineWrapSet.add(this.itemLineWrap);
    this.menuLineWrapSet.add(this.menuLineWrapStyle);
    this.menuLineWrapStyle.add(this.itemLineWrapByWord);
    this.menuLineWrapStyle.add(this.itemLineWrapByChar);
    this.menuStyle.add(this.menuLineStyle);
    this.menuLineStyle.add(this.itemLineStyleWin);
    this.menuLineStyle.add(this.itemLineStyleUnix);
    this.menuLineStyle.add(this.itemLineStyleMac);
    this.menuStyle.add(this.menuCharset);
    this.menuCharset.add(this.itemCharsetBASE);
    this.menuCharset.addSeparator();
    this.menuCharset.add(this.itemCharsetANSI);
    this.menuCharset.add(this.itemCharsetUTF8);
    this.menuCharset.add(this.itemCharsetUTF8_NO_BOM);
    this.menuCharset.add(this.itemCharsetULE);
    this.menuCharset.add(this.itemCharsetUBE);
    this.menuStyle.add(this.itemSignIdentifier);
    this.menuStyle.add(this.itemFont);
    this.menuStyle.add(this.itemTabSet);
    this.menuStyle.add(this.itemTextDrag);
    this.menuStyle.add(this.itemAutoIndent);
    this.menuBar.add(this.menuView);
    this.menuView.add(this.itemStateBar);
    this.menuView.add(this.itemLineNumber);
    this.menuView.add(this.itemAlwaysOnTop);
    this.menuView.add(this.itemResizable);
    this.menuView.add(this.itemTabPolicy);
    this.menuView.add(this.itemClickToClose);
    this.menuView.addSeparator();
    this.menuView.add(this.menuFontSize);
    this.menuFontSize.add(this.itemFontSizePlus);
    this.menuFontSize.add(this.itemFontSizeMinus);
    this.menuFontSize.add(this.itemFontSizeReset);
    this.menuView.add(this.menuColor);
    this.menuColor.add(this.itemColorFont);
    this.menuColor.add(this.itemColorBack);
    this.menuColor.add(this.itemColorCaret);
    this.menuColor.add(this.itemColorSelFont);
    this.menuColor.add(this.itemColorSelBack);
    this.menuColor.addSeparator();
    this.menuColor.add(this.itemColorAnti);
    this.menuColor.add(this.itemColorComplementary);
    this.menuView.add(this.menuColorStyle);
    this.menuColorStyle.add(this.itemColorStyle1);
    this.menuColorStyle.add(this.itemColorStyle2);
    this.menuColorStyle.add(this.itemColorStyle3);
    this.menuColorStyle.add(this.itemColorStyle4);
    this.menuColorStyle.add(this.itemColorStyle5);
    this.menuColorStyle.addSeparator();
    this.menuColorStyle.add(this.itemColorStyleDefault);
    this.menuView.add(this.menuHighlight);
    this.menuHighlight.add(this.itemHighlight1);
    this.menuHighlight.add(this.itemHighlight2);
    this.menuHighlight.add(this.itemHighlight3);
    this.menuHighlight.add(this.itemHighlight4);
    this.menuHighlight.add(this.itemHighlight5);
    this.menuView.add(this.menuRmHighlight);
    this.menuRmHighlight.add(this.itemRmHighlight1);
    this.menuRmHighlight.add(this.itemRmHighlight2);
    this.menuRmHighlight.add(this.itemRmHighlight3);
    this.menuRmHighlight.add(this.itemRmHighlight4);
    this.menuRmHighlight.add(this.itemRmHighlight5);
    this.menuRmHighlight.addSeparator();
    this.menuRmHighlight.add(this.itemRmHighlightAll);
    this.menuView.add(this.menuLookAndFeel);
    this.menuBar.add(this.menuHelp);
    this.menuHelp.add(this.itemHelp);
    this.menuHelp.addSeparator();
    this.menuHelp.add(this.itemAbout);
    this.addLookAndFeelItem();
    this.bgpLineWrapStyle.add(this.itemLineWrapByWord);
    this.bgpLineWrapStyle.add(this.itemLineWrapByChar);
    this.bgpLineStyle.add(this.itemLineStyleWin);
    this.bgpLineStyle.add(this.itemLineStyleUnix);
    this.bgpLineStyle.add(this.itemLineStyleMac);
    this.bgpCharset.add(this.itemCharsetBASE);
    this.bgpCharset.add(this.itemCharsetANSI);
    this.bgpCharset.add(this.itemCharsetUTF8);
    this.bgpCharset.add(this.itemCharsetUTF8_NO_BOM);
    this.bgpCharset.add(this.itemCharsetULE);
    this.bgpCharset.add(this.itemCharsetUBE);
  }

  /**
   * 初始化快捷菜单
   */
  private void addPopMenu() {
    this.popMenuMain.add(this.itemPopUnDo);
    this.popMenuMain.add(this.itemPopReDo);
    this.popMenuMain.addSeparator();
    this.popMenuMain.add(this.itemPopCut);
    this.popMenuMain.add(this.itemPopCopy);
    this.popMenuMain.add(this.itemPopPaste);
    this.popMenuMain.add(this.itemPopDel);
    this.popMenuMain.add(this.itemPopSelAll);
    this.popMenuMain.addSeparator();
    this.popMenuMain.add(this.menuPopHighlight);
    this.menuPopHighlight.add(itemPopHighlight1);
    this.menuPopHighlight.add(itemPopHighlight2);
    this.menuPopHighlight.add(itemPopHighlight3);
    this.menuPopHighlight.add(itemPopHighlight4);
    this.menuPopHighlight.add(itemPopHighlight5);
    this.popMenuMain.add(this.menuPopRmHighlight);
    this.menuPopRmHighlight.add(itemPopRmHighlight1);
    this.menuPopRmHighlight.add(itemPopRmHighlight2);
    this.menuPopRmHighlight.add(itemPopRmHighlight3);
    this.menuPopRmHighlight.add(itemPopRmHighlight4);
    this.menuPopRmHighlight.add(itemPopRmHighlight5);
    this.menuPopRmHighlight.addSeparator();
    this.menuPopRmHighlight.add(itemPopRmHighlightAll);
    Dimension popSize = this.popMenuMain.getPreferredSize();
    popSize.width += popSize.width / 5; // 为了美观，适当加宽菜单的显示
    this.popMenuMain.setPopupSize(popSize);

    this.popMenuTabbed.add(this.itemPopCloseCurrent);
    this.popMenuTabbed.add(this.itemPopCloseOthers);
    this.popMenuTabbed.addSeparator();
    this.popMenuTabbed.add(this.itemPopSave);
    this.popMenuTabbed.add(this.itemPopSaveAs);
    this.popMenuTabbed.add(this.itemPopReName);
    this.popMenuTabbed.add(this.itemPopDelFile);
    this.popMenuTabbed.add(this.itemPopReOpen);
    this.popMenuTabbed.addSeparator();
    this.popMenuTabbed.add(this.itemPopToCopyFileName);
    this.popMenuTabbed.add(this.itemPopToCopyFilePath);
    this.popMenuTabbed.add(this.itemPopToCopyDirPath);
    popSize = this.popMenuTabbed.getPreferredSize();
    popSize.width += popSize.width / 5; // 为了美观，适当加宽菜单的显示
    this.popMenuTabbed.setPopupSize(popSize);
  }

  /**
   * 设置各菜单项的初始状态，即是否可用
   */
  private void setMenuDefault() {
    this.menuSort.setEnabled(false);
    this.itemReOpen.setEnabled(false);
    this.itemReName.setEnabled(false);
    this.itemDelFile.setEnabled(false);
    this.itemUnDo.setEnabled(false);
    this.itemReDo.setEnabled(false);
    this.itemCut.setEnabled(false);
    this.itemCopy.setEnabled(false);
    this.itemSelAll.setEnabled(false);
    this.itemDel.setEnabled(false);
    this.menuCase.setEnabled(false);
    this.itemFind.setEnabled(false);
    this.itemFindNext.setEnabled(false);
    this.itemFindPrevious.setEnabled(false);
    this.itemSelFindNext.setEnabled(false);
    this.itemSelFindPrevious.setEnabled(false);
    this.menuQuickFind.setEnabled(false);
    this.itemSelCopy.setEnabled(false);
    this.itemLineBatchRemove.setEnabled(false);
    this.itemLineBatchInsert.setEnabled(false);
    this.itemLineBatchSeparate.setEnabled(false);
    this.itemLineBatchMerge.setEnabled(false);
    this.itemReplace.setEnabled(false);
    this.itemGoto.setEnabled(false);
    this.itemTrimSelected.setEnabled(false);
    this.itemDelNullLineSelected.setEnabled(false);
    this.itemSignIdentifier.setEnabled(false);
    this.itemLineNumber.setEnabled(false);
    this.itemPopCopy.setEnabled(false);
    this.itemPopCut.setEnabled(false);
    this.itemPopDel.setEnabled(false);
    this.itemPopSelAll.setEnabled(false);
    this.itemPopUnDo.setEnabled(false);
    this.itemPopReDo.setEnabled(false);
    this.itemPopReOpen.setEnabled(false);
    this.itemPopReName.setEnabled(false);
    this.itemPopDelFile.setEnabled(false);
    this.menuHighlight.setEnabled(false);
    this.menuPopHighlight.setEnabled(false);
    this.setFileHistoryMenuEnabled();
  }

  /**
   * 界面初始化时，设置有关菜单的初始状态与功能
   */
  private void setMenuDefaultInit() {
    this.itemLineWrap.setSelected(true);
    this.itemLineWrapByWord.setSelected(true);
    this.itemTextDrag.setSelected(false);
    this.itemAutoIndent.setSelected(false);
    this.itemStateBar.setSelected(true);
    this.itemLineNumber.setSelected(false);
    this.itemAlwaysOnTop.setSelected(false);
    this.itemResizable.setSelected(false);
    this.itemTabPolicy.setSelected(true);
    this.itemClickToClose.setSelected(true);
    this.setLineWrap();
    this.setLineWrapStyle(true);
    this.setTextDrag();
    this.setStateBar();
    this.setAlwaysOnTop();
    this.setResizable();
    this.setLineStyleString(LineSeparator.DEFAULT, true);
    this.setCharEncoding(CharEncoding.BASE, true);
  }

  /**
   * 设置"最近编辑"菜单是否可用
   */
  private void setFileHistoryMenuEnabled() {
    if (this.menuFileHistory.getItemCount() == 0) {
      this.menuFileHistory.setEnabled(false);
      this.itemClearFileHistory.setEnabled(false);
    } else {
      this.menuFileHistory.setEnabled(true);
      this.itemClearFileHistory.setEnabled(true);
    }
  }

  /**
   * 根据文本域中的字符是否为空，设置相关菜单的状态
   */
  private void setMenuStateByTextArea() {
    boolean isExist = true;
    if (this.txaMain == null || this.txaMain.getText().isEmpty()) {
      isExist = false;
    }
    this.menuSort.setEnabled(isExist);
    this.itemLineBatchRemove.setEnabled(isExist);
    this.itemLineBatchInsert.setEnabled(isExist);
    this.itemLineBatchSeparate.setEnabled(isExist);
    this.itemLineBatchMerge.setEnabled(isExist);
    this.itemFind.setEnabled(isExist);
    this.itemFindNext.setEnabled(isExist);
    this.itemFindPrevious.setEnabled(isExist);
    this.itemSelFindNext.setEnabled(isExist);
    this.itemSelFindPrevious.setEnabled(isExist);
    this.itemReplace.setEnabled(isExist);
    this.itemGoto.setEnabled(isExist);
    this.itemSelAll.setEnabled(isExist);
    this.itemPopSelAll.setEnabled(isExist);
  }

  /**
   * 根据文本域中选择的字符串是否为空，设置相关菜单的状态
   */
  private void setMenuStateBySelectedText() {
    boolean isNull = false;
    String selText = this.txaMain.getSelectedText();
    if (selText != null && selText.length() > 0) {
      isNull = true;
    }
    this.itemCopy.setEnabled(isNull);
    this.itemCut.setEnabled(isNull);
    this.itemDel.setEnabled(isNull);
    this.menuCase.setEnabled(isNull);
    this.itemLineBatchMerge.setEnabled(isNull);
    this.menuQuickFind.setEnabled(isNull);
    this.itemSelCopy.setEnabled(isNull);
    this.itemPopCopy.setEnabled(isNull);
    this.itemPopCut.setEnabled(isNull);
    this.itemPopDel.setEnabled(isNull);
    this.itemTrimSelected.setEnabled(isNull);
    this.itemDelNullLineSelected.setEnabled(isNull);
    this.itemSignIdentifier.setEnabled(isNull);
    this.menuHighlight.setEnabled(isNull);
    this.menuPopHighlight.setEnabled(isNull);
  }

  /**
   * 设置撤销与重做菜单项的状态
   */
  private void setMenuStateUndoRedo() {
    boolean canRedo = this.undoManager.canRedo();
    boolean canUndo = this.undoManager.canUndo();
    this.itemReDo.setEnabled(canRedo);
    this.itemUnDo.setEnabled(canUndo);
    this.itemPopUnDo.setEnabled(canUndo);
    this.itemPopReDo.setEnabled(canRedo);
  }

  /**
   * 为各菜单项设置助记符和快捷键
   */
  private void setMenuMnemonic() {
    this.menuFile.setMnemonic('F');
    this.menuHelp.setMnemonic('H');
    this.menuEdit.setMnemonic('E');
    this.menuSearch.setMnemonic('S');
    this.menuStyle.setMnemonic('O');
    this.menuView.setMnemonic('V');
    this.menuLineWrapSet.setMnemonic('L');
    this.menuLineStyle.setMnemonic('S');
    this.itemLineStyleWin.setMnemonic('W');
    this.itemLineStyleUnix.setMnemonic('U');
    this.itemLineStyleMac.setMnemonic('M');
    this.menuCharset.setMnemonic('C');
    this.itemCharsetANSI.setMnemonic('A');
    this.itemCharsetUTF8.setMnemonic('U');
    this.itemCharsetUTF8_NO_BOM.setMnemonic('N');
    this.itemCharsetULE.setMnemonic('L');
    this.itemCharsetUBE.setMnemonic('B');
    this.menuInsert.setMnemonic('I');
    this.itemLineWrap.setMnemonic('W');
    this.menuLineWrapStyle.setMnemonic('S');
    this.itemLineWrapByWord.setMnemonic('W');
    this.itemLineWrapByChar.setMnemonic('C');
    this.itemTextDrag.setMnemonic('D');
    this.itemAutoIndent.setMnemonic('I');
    this.itemStateBar.setMnemonic('S');
    this.itemLineNumber.setMnemonic('L');
    this.itemAlwaysOnTop.setMnemonic('A');
    this.itemResizable.setMnemonic('R');
    this.itemTabPolicy.setMnemonic('P');
    this.itemClickToClose.setMnemonic('D');
    this.menuColor.setMnemonic('C');
    this.menuFontSize.setMnemonic('F');
    this.menuColorStyle.setMnemonic('Y');
    this.menuHighlight.setMnemonic('H');
    this.menuRmHighlight.setMnemonic('M');
    this.menuLookAndFeel.setMnemonic('K');
    this.menuPopHighlight.setMnemonic('H');
    this.menuPopRmHighlight.setMnemonic('M');
    this.itemNew.setAccelerator(KeyStroke.getKeyStroke('N',
        InputEvent.CTRL_DOWN_MASK)); // 快捷键：Ctrl+N
    this.itemOpen.setAccelerator(KeyStroke.getKeyStroke('O',
        InputEvent.CTRL_DOWN_MASK)); // 快捷键：Ctrl+O
    this.itemSave.setAccelerator(KeyStroke.getKeyStroke('S',
        InputEvent.CTRL_DOWN_MASK)); // 快捷键：Ctrl+S
    this.itemExit.setAccelerator(KeyStroke.getKeyStroke('Q',
        InputEvent.CTRL_DOWN_MASK)); // 快捷键：Ctrl+Q
    this.itemAbout.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0)); // 快捷键：F1
    this.itemClose.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, 0)); // 快捷键：F4
    this.itemUnDo.setAccelerator(KeyStroke.getKeyStroke('Z',
        InputEvent.CTRL_DOWN_MASK)); // 快捷键：Ctrl+Z
    this.itemReDo.setAccelerator(KeyStroke.getKeyStroke('Y',
        InputEvent.CTRL_DOWN_MASK)); // 快捷键：Ctrl+Y
    this.itemCut.setAccelerator(KeyStroke.getKeyStroke('X',
        InputEvent.CTRL_DOWN_MASK)); // 快捷键：Ctrl+X
    this.itemCopy.setAccelerator(KeyStroke.getKeyStroke('C',
        InputEvent.CTRL_DOWN_MASK)); // 快捷键：Ctrl+C
    this.itemPaste.setAccelerator(KeyStroke.getKeyStroke('V',
        InputEvent.CTRL_DOWN_MASK)); // 快捷键：Ctrl+V
    this.itemDel.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0)); // 快捷键：Delete
    this.itemCaseUp.setAccelerator(KeyStroke.getKeyStroke('U',
        InputEvent.CTRL_DOWN_MASK + InputEvent.SHIFT_DOWN_MASK)); // 快捷键：Ctrl+Shift+U
    this.itemCaseLow.setAccelerator(KeyStroke.getKeyStroke('U',
        InputEvent.CTRL_DOWN_MASK)); // 快捷键：Ctrl+U
    this.itemFind.setAccelerator(KeyStroke.getKeyStroke('F',
        InputEvent.CTRL_DOWN_MASK)); // 快捷键：Ctrl+F
    this.itemFindNext.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0)); // 快捷键：F3
    this.itemFindPrevious.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F3,
        InputEvent.SHIFT_DOWN_MASK)); // 快捷键：Shift+F3
    this.itemSelFindNext.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F3,
        InputEvent.CTRL_DOWN_MASK)); // 快捷键：Ctrl+F3
    this.itemSelFindPrevious
        .setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F3,
            InputEvent.CTRL_DOWN_MASK + InputEvent.SHIFT_DOWN_MASK)); // 快捷键：Ctrl+Shift+F3
    this.itemQuickFindDown.setAccelerator(KeyStroke.getKeyStroke('K',
        InputEvent.CTRL_DOWN_MASK)); // 快捷键：Ctrl+K
    this.itemQuickFindUp.setAccelerator(KeyStroke.getKeyStroke('K',
        InputEvent.CTRL_DOWN_MASK + InputEvent.SHIFT_DOWN_MASK)); // 快捷键：Ctrl+Shift+K
    this.itemReplace.setAccelerator(KeyStroke.getKeyStroke('H',
        InputEvent.CTRL_DOWN_MASK)); // 快捷键：Ctrl+H
    this.itemGoto.setAccelerator(KeyStroke.getKeyStroke('G',
        InputEvent.CTRL_DOWN_MASK)); // 快捷键：Ctrl+G
    this.itemToCopyAllText.setAccelerator(KeyStroke.getKeyStroke('A',
        InputEvent.CTRL_DOWN_MASK + InputEvent.SHIFT_DOWN_MASK)); // 快捷键：Ctrl+Shift+A
    this.itemLineCopy.setAccelerator(KeyStroke.getKeyStroke('D',
        InputEvent.CTRL_DOWN_MASK)); // 快捷键：Ctrl+D
    this.itemLineDel.setAccelerator(KeyStroke.getKeyStroke('D',
        InputEvent.CTRL_DOWN_MASK + InputEvent.SHIFT_DOWN_MASK)); // 快捷键：Ctrl+Shift+D
    this.itemSortUp.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_UP,
        InputEvent.ALT_DOWN_MASK)); // 快捷键：Alt+向上方向键
    this.itemSortDown.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN,
        InputEvent.ALT_DOWN_MASK)); // 快捷键：Alt+向下方向键
    this.itemIndentAdd.setAccelerator(KeyStroke.getKeyStroke('T',
        InputEvent.CTRL_DOWN_MASK + InputEvent.ALT_DOWN_MASK)); // 快捷键：Ctrl+Alt+T
    this.itemIndentBack.setAccelerator(KeyStroke.getKeyStroke('T',
        InputEvent.CTRL_DOWN_MASK + InputEvent.ALT_DOWN_MASK
            + InputEvent.SHIFT_DOWN_MASK)); // 快捷键：Ctrl+Alt+Shift+T
    this.itemSelCopy.setAccelerator(KeyStroke.getKeyStroke('R',
        InputEvent.CTRL_DOWN_MASK)); // 快捷键：Ctrl+R
    this.itemTrimStart.setAccelerator(KeyStroke.getKeyStroke('S',
        InputEvent.CTRL_DOWN_MASK + InputEvent.SHIFT_DOWN_MASK)); // 快捷键：Ctrl+Shift+S
    this.itemTrimEnd.setAccelerator(KeyStroke.getKeyStroke('E',
        InputEvent.CTRL_DOWN_MASK + InputEvent.SHIFT_DOWN_MASK)); // 快捷键：Ctrl+Shift+E
    this.itemTrimAll.setAccelerator(KeyStroke.getKeyStroke('L',
        InputEvent.CTRL_DOWN_MASK + InputEvent.SHIFT_DOWN_MASK)); // 快捷键：Ctrl+Shift+L
    this.itemTrimSelected.setAccelerator(KeyStroke.getKeyStroke('T',
        InputEvent.CTRL_DOWN_MASK + InputEvent.SHIFT_DOWN_MASK)); // 快捷键：Ctrl+Shift+T
    this.itemDelNullLineAll.setAccelerator(KeyStroke.getKeyStroke('A',
        InputEvent.CTRL_DOWN_MASK + InputEvent.ALT_DOWN_MASK)); // 快捷键：Ctrl+Alt+A
    this.itemDelNullLineSelected.setAccelerator(KeyStroke.getKeyStroke('S',
        InputEvent.CTRL_DOWN_MASK + InputEvent.ALT_DOWN_MASK)); // 快捷键：Ctrl+Alt+S
    this.itemSelAll.setAccelerator(KeyStroke.getKeyStroke('A',
        InputEvent.CTRL_DOWN_MASK)); // 快捷键：Ctrl+A
    this.itemInsertDateTime.setAccelerator(KeyStroke.getKeyStroke(
        KeyEvent.VK_F5, 0)); // 快捷键：F5
    this.itemFontSizePlus.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_UP,
        InputEvent.CTRL_DOWN_MASK)); // 快捷键：Ctrl+向上方向键
    this.itemFontSizeMinus.setAccelerator(KeyStroke.getKeyStroke(
        KeyEvent.VK_DOWN, InputEvent.CTRL_DOWN_MASK)); // 快捷键：Ctrl+向下方向键
    this.itemFontSizeReset.setAccelerator(KeyStroke.getKeyStroke(
        KeyEvent.VK_SLASH, InputEvent.CTRL_DOWN_MASK)); // 快捷键：Ctrl+'/'键
    this.itemLineDelToStart
        .setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT,
            InputEvent.CTRL_DOWN_MASK + InputEvent.ALT_DOWN_MASK)); // 快捷键：Ctrl+Alt+向左方向键
    this.itemLineDelToEnd.setAccelerator(KeyStroke
        .getKeyStroke(KeyEvent.VK_RIGHT, InputEvent.CTRL_DOWN_MASK
            + InputEvent.ALT_DOWN_MASK)); // 快捷键：Ctrl+Alt+向右方向键
    this.itemLineToUp.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_UP,
        InputEvent.CTRL_DOWN_MASK + InputEvent.SHIFT_DOWN_MASK)); // 快捷键：Ctrl+Shift+向上方向键
    this.itemLineToDown.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN,
        InputEvent.CTRL_DOWN_MASK + InputEvent.SHIFT_DOWN_MASK)); // 快捷键：Ctrl+Shift+向下方向键
    this.itemLineToCopy.setAccelerator(KeyStroke.getKeyStroke('C',
        InputEvent.CTRL_DOWN_MASK + InputEvent.SHIFT_DOWN_MASK)); // 快捷键：Ctrl+Shift+C
    this.itemLineToCut.setAccelerator(KeyStroke.getKeyStroke('X',
        InputEvent.CTRL_DOWN_MASK + InputEvent.SHIFT_DOWN_MASK)); // 快捷键：Ctrl+Shift+X
    this.itemLineBatchRemove.setAccelerator(KeyStroke.getKeyStroke('R',
        InputEvent.CTRL_DOWN_MASK + InputEvent.SHIFT_DOWN_MASK)); // 快捷键：Ctrl+Shift+R
    this.itemLineBatchInsert.setAccelerator(KeyStroke.getKeyStroke('I',
        InputEvent.CTRL_DOWN_MASK + InputEvent.SHIFT_DOWN_MASK)); // 快捷键：Ctrl+Shift+I
    this.itemLineBatchSeparate.setAccelerator(KeyStroke.getKeyStroke('P',
        InputEvent.CTRL_DOWN_MASK + InputEvent.SHIFT_DOWN_MASK)); // 快捷键：Ctrl+Shift+P
    this.itemLineBatchMerge.setAccelerator(KeyStroke.getKeyStroke('M',
        InputEvent.CTRL_DOWN_MASK + InputEvent.SHIFT_DOWN_MASK)); // 快捷键：Ctrl+Shift+M
  }

  /**
   * 为各菜单项添加事件的处理方法
   */
  public void actionPerformed(ActionEvent e) {
    if (this.itemAbout.equals(e.getSource())) {
      this.showAbout();
    } else if (this.itemCopy.equals(e.getSource())) {
      this.copyText();
    } else if (this.itemCut.equals(e.getSource())) {
      this.cutText();
    } else if (this.itemInsertChar.equals(e.getSource())) {
      this.openInsertCharDialog();
    } else if (this.itemInsertDateTime.equals(e.getSource())) {
      this.openInsertDateDialog();
    } else if (this.itemDel.equals(e.getSource())) {
      this.deleteText();
    } else if (this.itemCaseUp.equals(e.getSource())) {
      this.switchCase(true);
    } else if (this.itemCaseLow.equals(e.getSource())) {
      this.switchCase(false);
    } else if (this.itemExit.equals(e.getSource())) {
      this.exit();
    } else if (this.itemFind.equals(e.getSource())) {
      this.openFindDialog();
    } else if (this.itemFindNext.equals(e.getSource())) {
      this.findNextText(true);
    } else if (this.itemFindPrevious.equals(e.getSource())) {
      this.findNextText(false);
    } else if (this.itemSelFindNext.equals(e.getSource())) {
      this.findSelNextText(true);
    } else if (this.itemSelFindPrevious.equals(e.getSource())) {
      this.findSelNextText(false);
    } else if (this.itemQuickFindDown.equals(e.getSource())) {
      this.quickFindText(true);
    } else if (this.itemQuickFindUp.equals(e.getSource())) {
      this.quickFindText(false);
    } else if (this.itemFont.equals(e.getSource())) {
      this.openFontChooser();
    } else if (this.itemGoto.equals(e.getSource())) {
      this.openGotoDialog();
    } else if (this.itemToCopyFileName.equals(e.getSource())) {
      this.toCopyFileName();
    } else if (this.itemToCopyFilePath.equals(e.getSource())) {
      this.toCopyFilePath();
    } else if (this.itemToCopyDirPath.equals(e.getSource())) {
      this.toCopyDirPath();
    } else if (this.itemToCopyAllText.equals(e.getSource())) {
      this.toCopyAllText();
    } else if (this.itemLineCopy.equals(e.getSource())) {
      this.copyLines();
    } else if (this.itemLineDel.equals(e.getSource())) {
      this.deleteLines();
    } else if (this.itemLineDelToStart.equals(e.getSource())) {
      this.deleteLineToStart();
    } else if (this.itemLineDelToEnd.equals(e.getSource())) {
      this.deleteLineToEnd();
    } else if (this.itemLineToUp.equals(e.getSource())) {
      this.moveLineToUp();
    } else if (this.itemLineToDown.equals(e.getSource())) {
      this.moveLineToDown();
    } else if (this.itemLineToCopy.equals(e.getSource())) {
      this.toCopyCurLine();
    } else if (this.itemLineToCut.equals(e.getSource())) {
      this.toCutCurLine();
    } else if (this.itemLineBatchRemove.equals(e.getSource())) {
      this.openBatchRemoveDialog();
    } else if (this.itemLineBatchInsert.equals(e.getSource())) {
      this.openBatchInsertDialog();
    } else if (this.itemLineBatchSeparate.equals(e.getSource())) {
      this.openBatchSeparateDialog();
    } else if (this.itemLineBatchMerge.equals(e.getSource())) {
      this.mergeLines();
    } else if (this.itemSortUp.equals(e.getSource())) {
      this.sortLines(true);
    } else if (this.itemSortDown.equals(e.getSource())) {
      this.sortLines(false);
    } else if (this.itemIndentAdd.equals(e.getSource())) {
      this.toIndent(true);
    } else if (this.itemIndentBack.equals(e.getSource())) {
      this.toIndent(false);
    } else if (this.itemSelCopy.equals(e.getSource())) {
      this.copySelectedText();
    } else if (this.itemTrimStart.equals(e.getSource())) {
      this.trimLines(0);
    } else if (this.itemTrimEnd.equals(e.getSource())) {
      this.trimLines(1);
    } else if (this.itemTrimAll.equals(e.getSource())) {
      this.trimLines(2);
    } else if (this.itemTrimSelected.equals(e.getSource())) {
      this.trimSelected();
    } else if (this.itemDelNullLineAll.equals(e.getSource())) {
      this.delAllNullLines();
    } else if (this.itemDelNullLineSelected.equals(e.getSource())) {
      this.delSelectedNullLines();
    } else if (this.itemHelp.equals(e.getSource())) {

    } else if (this.itemLineWrap.equals(e.getSource())) {
      this.setLineWrap();
    } else if (this.itemLineWrapByWord.equals(e.getSource())) {
      this.setLineWrapStyle(true);
    } else if (this.itemLineWrapByChar.equals(e.getSource())) {
      this.setLineWrapStyle(false);
    } else if (this.itemLineStyleWin.equals(e.getSource())) {
      this.setLineStyleString(LineSeparator.WINDOWS, false);
    } else if (this.itemLineStyleUnix.equals(e.getSource())) {
      this.setLineStyleString(LineSeparator.UNIX, false);
    } else if (this.itemLineStyleMac.equals(e.getSource())) {
      this.setLineStyleString(LineSeparator.MACINTOSH, false);
    } else if (this.itemCharsetBASE.equals(e.getSource())) {
      this.setCharEncoding(CharEncoding.BASE, false);
    } else if (this.itemCharsetANSI.equals(e.getSource())) {
      this.setCharEncoding(CharEncoding.ANSI, false);
    } else if (this.itemCharsetUTF8.equals(e.getSource())) {
      this.setCharEncoding(CharEncoding.UTF8, false);
    } else if (this.itemCharsetUTF8_NO_BOM.equals(e.getSource())) {
      this.setCharEncoding(CharEncoding.UTF8_NO_BOM, false);
    } else if (this.itemCharsetULE.equals(e.getSource())) {
      this.setCharEncoding(CharEncoding.ULE, false);
    } else if (this.itemCharsetUBE.equals(e.getSource())) {
      this.setCharEncoding(CharEncoding.UBE, false);
    } else if (this.itemSignIdentifier.equals(e.getSource())) {
      this.openSignIdentifierDialog();
    } else if (this.itemTextDrag.equals(e.getSource())) {
      this.setTextDrag();
    } else if (this.itemAutoIndent.equals(e.getSource())) {
      this.setAutoIndent();
    } else if (this.itemNew.equals(e.getSource())) {
      this.createNew(null);
    } else if (this.itemOpen.equals(e.getSource())) {
      this.openFile();
    } else if (this.itemOpenByEncoding.equals(e.getSource())) {
      this.openFileByEncoding();
    } else if (this.itemReOpen.equals(e.getSource())) {
      this.reOpenFile();
    } else if (this.itemReName.equals(e.getSource())) {
      this.reNameFile();
    } else if (this.itemPaste.equals(e.getSource())) {
      this.pasteText();
    } else if (this.itemPopCopy.equals(e.getSource())) {
      this.copyText();
    } else if (this.itemPopCut.equals(e.getSource())) {
      this.cutText();
    } else if (this.itemPopDel.equals(e.getSource())) {
      this.deleteText();
    } else if (this.itemPopPaste.equals(e.getSource())) {
      this.pasteText();
    } else if (this.itemPopSelAll.equals(e.getSource())) {
      this.selectAll();
    } else if (this.itemPopUnDo.equals(e.getSource())) {
      this.undoAction();
    } else if (this.itemPopReDo.equals(e.getSource())) {
      this.redoAction();
    } else if (this.itemReDo.equals(e.getSource())) {
      this.redoAction();
    } else if (this.itemReplace.equals(e.getSource())) {
      this.openReplaceDialog();
    } else if (this.itemSave.equals(e.getSource())) {
      this.saveFile(false);
    } else if (this.itemSaveAs.equals(e.getSource())) {
      this.saveAsFile();
    } else if (this.itemClose.equals(e.getSource())) {
      this.closeFile(true);
    } else if (this.itemCloseAll.equals(e.getSource())) {
      this.closeAll();
    } else if (this.itemDelFile.equals(e.getSource())) {
      this.deleteFile();
    } else if (this.itemSelAll.equals(e.getSource())) {
      this.selectAll();
    } else if (this.itemStateBar.equals(e.getSource())) {
      this.setStateBar();
    } else if (this.itemLineNumber.equals(e.getSource())) {
      this.setLineNumber(this.itemLineNumber.isSelected());
    } else if (this.itemAlwaysOnTop.equals(e.getSource())) {
      this.setAlwaysOnTop();
    } else if (this.itemResizable.equals(e.getSource())) {
      this.setResizable();
    } else if (this.itemTabPolicy.equals(e.getSource())) {
      this.setTabLayoutPolicy();
    } else if (this.itemClickToClose.equals(e.getSource())) {
      this.setClickToClose();
    } else if (this.itemFontSizePlus.equals(e.getSource())) {
      this.setFontSizePlus();
    } else if (this.itemFontSizeMinus.equals(e.getSource())) {
      this.setFontSizeMinus();
    } else if (this.itemFontSizeReset.equals(e.getSource())) {
      this.setFontSizeReset();
    } else if (this.itemColorFont.equals(e.getSource())) {
      this.setFontColor();
    } else if (this.itemColorBack.equals(e.getSource())) {
      this.setBackColor();
    } else if (this.itemColorCaret.equals(e.getSource())) {
      this.setCaretColor();
    } else if (this.itemColorSelFont.equals(e.getSource())) {
      this.setSelFontColor();
    } else if (this.itemColorSelBack.equals(e.getSource())) {
      this.setSelBackColor();
    } else if (this.itemColorAnti.equals(e.getSource())) {
      this.setColorTransform(true);
    } else if (this.itemColorComplementary.equals(e.getSource())) {
      this.setColorTransform(false);
    } else if (this.itemColorStyle1.equals(e.getSource())) {
      this.setColorStyle(1);
    } else if (this.itemColorStyle2.equals(e.getSource())) {
      this.setColorStyle(2);
    } else if (this.itemColorStyle3.equals(e.getSource())) {
      this.setColorStyle(3);
    } else if (this.itemColorStyle4.equals(e.getSource())) {
      this.setColorStyle(4);
    } else if (this.itemColorStyle5.equals(e.getSource())) {
      this.setColorStyle(5);
    } else if (this.itemColorStyleDefault.equals(e.getSource())) {
      this.setColorStyle(0);
    } else if (this.itemHighlight1.equals(e.getSource())) {
      this.setHighlight(1);
    } else if (this.itemHighlight2.equals(e.getSource())) {
      this.setHighlight(2);
    } else if (this.itemHighlight3.equals(e.getSource())) {
      this.setHighlight(3);
    } else if (this.itemHighlight4.equals(e.getSource())) {
      this.setHighlight(4);
    } else if (this.itemHighlight5.equals(e.getSource())) {
      this.setHighlight(5);
    } else if (this.itemRmHighlight1.equals(e.getSource())) {
      this.rmHighlight(1);
    } else if (this.itemRmHighlight2.equals(e.getSource())) {
      this.rmHighlight(2);
    } else if (this.itemRmHighlight3.equals(e.getSource())) {
      this.rmHighlight(3);
    } else if (this.itemRmHighlight4.equals(e.getSource())) {
      this.rmHighlight(4);
    } else if (this.itemRmHighlight5.equals(e.getSource())) {
      this.rmHighlight(5);
    } else if (this.itemRmHighlightAll.equals(e.getSource())) {
      this.rmHighlight(0);
    } else if (this.itemPopHighlight1.equals(e.getSource())) {
      this.setHighlight(1);
    } else if (this.itemPopHighlight2.equals(e.getSource())) {
      this.setHighlight(2);
    } else if (this.itemPopHighlight3.equals(e.getSource())) {
      this.setHighlight(3);
    } else if (this.itemPopHighlight4.equals(e.getSource())) {
      this.setHighlight(4);
    } else if (this.itemPopHighlight5.equals(e.getSource())) {
      this.setHighlight(5);
    } else if (this.itemPopRmHighlight1.equals(e.getSource())) {
      this.rmHighlight(1);
    } else if (this.itemPopRmHighlight2.equals(e.getSource())) {
      this.rmHighlight(2);
    } else if (this.itemPopRmHighlight3.equals(e.getSource())) {
      this.rmHighlight(3);
    } else if (this.itemPopRmHighlight4.equals(e.getSource())) {
      this.rmHighlight(4);
    } else if (this.itemPopRmHighlight5.equals(e.getSource())) {
      this.rmHighlight(5);
    } else if (this.itemPopRmHighlightAll.equals(e.getSource())) {
      this.rmHighlight(0);
    } else if (this.itemPopCloseCurrent.equals(e.getSource())) {
      this.closeFile(true);
    } else if (this.itemPopCloseOthers.equals(e.getSource())) {
      this.closeOthers();
    } else if (this.itemPopSave.equals(e.getSource())) {
      this.saveFile(false);
    } else if (this.itemPopSaveAs.equals(e.getSource())) {
      this.saveAsFile();
    } else if (this.itemPopReName.equals(e.getSource())) {
      this.reNameFile();
    } else if (this.itemPopDelFile.equals(e.getSource())) {
      this.deleteFile();
    } else if (this.itemPopReOpen.equals(e.getSource())) {
      this.reOpenFile();
    } else if (this.itemPopToCopyFileName.equals(e.getSource())) {
      this.toCopyFileName();
    } else if (this.itemPopToCopyFilePath.equals(e.getSource())) {
      this.toCopyFilePath();
    } else if (this.itemPopToCopyDirPath.equals(e.getSource())) {
      this.toCopyDirPath();
    } else if (this.itemTabSet.equals(e.getSource())) {
      this.openTabSetDialog();
    } else if (this.itemUnDo.equals(e.getSource())) {
      this.undoAction();
    } else if (this.itemClearFileHistory.equals(e.getSource())) {
      this.clearFileHistory();
    } else if (Util.FILE_HISTORY.equals(e.getActionCommand())) { // 最近编辑的文件菜单
      JMenuItem itemFile = (JMenuItem) e.getSource();
      this.openFileHistory(itemFile.getText());
    } else if (e.getActionCommand() != null
        && e.getActionCommand().startsWith(Util.LOOK_AND_FEEL)) { // 当前系统支持的外观菜单
      JRadioButtonMenuItem itemInfo = (JRadioButtonMenuItem) e.getSource();
      this.setLookAndFeel(itemInfo.getActionCommand().substring(
          (Util.LOOK_AND_FEEL + Util.PARAM_SPLIT).length()));
    }
  }

  /**
   * "双击关闭标签"的处理方法
   */
  private void setClickToClose() {
    this.clickToClose = this.itemClickToClose.isSelected();
  }

  /**
   * "关闭其它"的处理方法
   */
  private void closeOthers() {
    int index = this.tpnMain.getSelectedIndex();
    int size = this.textAreaList.size();
    this.tpnMain.setSelectedIndex(0);
    for (int i = 0; i < size; i++) {
      if (index == i) {
        if (this.tpnMain.getTabCount() > 1) {
          this.tpnMain.setSelectedIndex(1);
        }
        continue;
      } else {
        if (!this.closeFile(true)) { // 关闭当前的文件
          break;
        }
      }
    }
  }

  /**
   * "全部关闭"的处理方法
   */
  private void closeAll() {
    int size = this.textAreaList.size();
    for (int i = 0; i < size; i++) {
      this.tpnMain.setSelectedIndex(0);
      if (!this.closeFile(true)) { // 关闭当前的文件
        break;
      }
    }
  }

  /**
   * "多行标签"的处理方法
   */
  private void setTabLayoutPolicy() {
    if (this.itemTabPolicy.isSelected()) {
      this.tpnMain.setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);
    } else {
      this.tpnMain.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
    }
  }

  /**
   * 设置为"切换外观"下某个外观的处理方法
   * 
   * @param className
   *          将要设置的外观完整类名
   */
  private void setLookAndFeel(String className) {
    if (this.strLookAndFeel.equals(className)) {
      return;
    }
    try {
      UIManager.setLookAndFeel(className);
      this.strLookAndFeel = className;
      SwingUtilities.updateComponentTreeUI(this);
      SwingUtilities.updateComponentTreeUI(this.popMenuMain);
      SwingUtilities.updateComponentTreeUI(this.popMenuTabbed);
      this.popMenuMain.updateUI();
      this.popMenuTabbed.updateUI();
      this.destroyAllDialogs();
    } catch (Exception x) {
      x.printStackTrace();
    }
  }

  /**
   * 将所有窗口设置为null，以便于将切换后的新外观完整应用于各窗口
   */
  private void destroyAllDialogs() {
    if (this.openFileChooser != null) {
      this.openFileChooser = null;
    }
    if (this.saveFileChooser != null) {
      this.saveFileChooser = null;
    }
    if (this.fontChooser != null) {
      this.fontChooser.dispose();
      this.fontChooser = null;
    }
    if (this.findReplaceDialog != null) {
      this.findReplaceDialog.dispose();
      this.findReplaceDialog = null;
    }
    if (this.gotoDialog != null) {
      this.gotoDialog.dispose();
      this.gotoDialog = null;
    }
    if (this.aboutDialog != null) {
      this.aboutDialog.dispose();
      this.aboutDialog = null;
    }
    if (this.tabSetDialog != null) {
      this.tabSetDialog.dispose();
      this.tabSetDialog = null;
    }
    if (this.insertCharDialog != null) {
      this.insertCharDialog.dispose();
      this.insertCharDialog = null;
    }
    if (this.insertDateDialog != null) {
      this.insertDateDialog.dispose();
      this.insertDateDialog = null;
    }
    if (this.fileEncodingDialog != null) {
      this.fileEncodingDialog.dispose();
      this.fileEncodingDialog = null;
    }
    if (this.batchRemoveDialog != null) {
      this.batchRemoveDialog.dispose();
      this.batchRemoveDialog = null;
    }
    if (this.batchInsertDialog != null) {
      this.batchInsertDialog.dispose();
      this.batchInsertDialog = null;
    }
    if (this.batchSeparateDialog != null) {
      this.batchSeparateDialog.dispose();
      this.batchSeparateDialog = null;
    }
    if (this.signIdentifierDialog != null) {
      this.signIdentifierDialog.dispose();
      this.signIdentifierDialog = null;
    }
  }

  /**
   * 在"切换外观"菜单下，添加当前系统可用的外观
   */
  private void addLookAndFeelItem() {
    UIManager.LookAndFeelInfo[] infos = Util.LOOK_AND_FEEL_INFOS;
    for (UIManager.LookAndFeelInfo info : infos) {
      JRadioButtonMenuItem itemInfo = new JRadioButtonMenuItem(info.getName());
      String className = info.getClassName();
      itemInfo.setActionCommand(Util.LOOK_AND_FEEL + Util.PARAM_SPLIT
          + className);
      itemInfo.addActionListener(this);
      this.menuLookAndFeel.add(itemInfo);
      this.bgpLookAndFeel.add(itemInfo);
      if (Util.SYSTEM_LOOK_AND_FEEL_CLASS_NAME.equals(className)) {
        itemInfo.setSelected(true);
      }
    }
  }

  /**
   * "行号栏"的处理方法
   * 
   * @param enable
   *          是否显示行号栏
   */
  private void setLineNumber(boolean enable) {
    LineNumberView lineNumberView = null;
    JScrollPane srp = null;
    boolean menuEnabled = this.itemLineNumber.isEnabled();
    for (int i = 0; i < this.tpnMain.getTabCount(); i++) {
      srp = ((JScrollPane) this.tpnMain.getComponentAt(i));
      if (enable && menuEnabled) {
        lineNumberView = new LineNumberView(this.textAreaList.get(i));
        srp.setRowHeaderView(lineNumberView);
      } else {
        srp.setRowHeaderView(null);
      }
    }
    this.setting.isLineNumberView = enable;
  }

  /**
   * 在开启"行号栏"设置的情况下，新建或打开新文件时，添加行号栏的显示
   */
  private void setLineNumberForNew() {
    if (this.setting.isLineNumberView && this.itemLineNumber.isEnabled()) {
      LineNumberView lineNumberView = new LineNumberView(this.textAreaList
          .getLast());
      JScrollPane srp = ((JScrollPane) this.tpnMain.getComponentAt(this.tpnMain
          .getTabCount() - 1));
      srp.setRowHeaderView(lineNumberView);
    }
  }

  /**
   * 设置各文本域字体。如果已显示"行号栏"，则同步设置其字体。
   */
  private void setTextAreaFont() {
    for (BaseTextArea textArea : this.textAreaList) {
      textArea.setFont(this.setting.font);
    }
    if (this.setting.isLineNumberView) {
      JScrollPane srp = null;
      for (int i = 0; i < this.tpnMain.getTabCount(); i++) {
        srp = (JScrollPane) this.tpnMain.getComponentAt(i);
        srp.getRowHeader().getView().setFont(this.setting.font);
      }
    }
  }

  /**
   * "关闭"的处理方法
   * 
   * @param check
   *          是否检查当前文件的修改状态，如果为true表示需要检查，反之则不需要检查。
   * 
   * @return 成功关闭文件时返回true，未能关闭时返回false
   */
  private boolean closeFile(boolean check) {
    if (check && !this.saveFileBeforeAct()) {
      return false;
    }
    int index = this.tpnMain.getSelectedIndex();
    this.tpnMain.remove(index);
    this.textAreaList.remove(index);
    if (this.textAreaList.size() == 0) {
      this.createNew(null);
    }
    return true;
  }

  /**
   * 批处理"分割行"的处理方法
   */
  private void openBatchSeparateDialog() {
    if (this.batchSeparateDialog == null) {
      this.batchSeparateDialog = new BatchSeparateDialog(this, true,
          this.txaMain);
    } else {
      this.batchSeparateDialog.setTextArea(this.txaMain);
      this.batchSeparateDialog.setVisible(true);
    }
  }

  /**
   * 批处理"合并行"的处理方法
   */
  private void mergeLines() {
    String[] arrText = Util.getCurrentLinesArray(this.txaMain);
    if (arrText.length <= 1) {
      return;
    }
    CurrentLines currentLines = new CurrentLines(this.txaMain);
    int startIndex = currentLines.getStartIndex();
    StringBuilder stbLines = new StringBuilder();
    for (String str : arrText) {
      stbLines.append(str);
    }
    this.txaMain.replaceSelection(stbLines.toString());
    this.txaMain.select(startIndex, startIndex + stbLines.length());
  }

  /**
   * "列表符号与编号"的处理方法
   */
  private void openSignIdentifierDialog() {
    if (this.signIdentifierDialog == null) {
      Hashtable<String, String> hashtable = new Hashtable<String, String>();
      hashtable.put("符号", Util.SIGN_CHARS);
      hashtable.put("编号", Util.IDENTIFIER_CHARS);
      this.signIdentifierDialog = new SignIdentifierDialog(this, true,
          this.txaMain, hashtable);
    } else {
      this.signIdentifierDialog.setVisible(true);
    }
  }

  /**
   * "缩进"的处理方法
   * 
   * @param indent
   *          缩进的方向，缩进为true，退格为false
   */
  private void toIndent(boolean indent) {
    CurrentLines currentLines = new CurrentLines(this.txaMain);
    int startIndex = currentLines.getStartIndex();
    String[] arrText = Util.getCurrentLinesArray(this.txaMain);
    String strIndent = "\t"; // 用于缩进的字符，默认为TAB字符
    String strIndentBlanks = ""; // 当前等价于Tab键的多个空格
    int tabSize = this.txaMain.getTabSize();
    for (int i = 0; i < tabSize; i++) {
      strIndentBlanks += " ";
    }
    if (this.txaMain.getTabReplaceBySpace()) { // 如果被设置为“以空格代替Tab键”，则将缩进字符设置为相应个数的空格
      strIndent = strIndentBlanks;
    }
    StringBuilder stbIndent = new StringBuilder();
    boolean label = false; // 用来标识是否进行了退格
    if (indent) {
      for (String str : arrText) {
        stbIndent.append(strIndent + str + "\n");
      }
    } else {
      for (String str : arrText) {
        if (str.startsWith(strIndentBlanks)) {
          str = str.substring(strIndentBlanks.length());
          label = true;
        } else if (str.startsWith("\t")) {
          str = str.substring(1);
          label = true;
        } else if (str.startsWith(" ")) {
          int i = 1;
          for (; i < str.length() - 1; i++) {
            if (str.charAt(i) != ' ') {
              break;
            }
          }
          str = str.substring(i);
          label = true;
        }
        stbIndent.append(str + "\n");
      }
      if (!label) {
        return;
      }
    }
    this.txaMain.replaceSelection(stbIndent
        .deleteCharAt(stbIndent.length() - 1).toString()); // 删除字符串末尾多余的换行符
    this.txaMain.select(startIndex, startIndex + stbIndent.length());
  }

  /**
   * "选定查找"的处理方法
   * 
   * @param isFindDown
   *          查找的方向，如果向下查找则为true，反之则为false
   */
  private void findSelNextText(boolean isFindDown) {
    String strSel = this.txaMain.getSelectedText();
    if (this.findReplaceDialog == null) {
      this.findReplaceDialog = new FindReplaceDialog(this, false, this.txaMain,
          false);
    } else {
      this.findReplaceDialog.setTextArea(this.txaMain);
    }
    if (strSel != null && !strSel.isEmpty()) {
      this.findReplaceDialog.setFindText(strSel, false);
    }
    this.findReplaceDialog.findText(isFindDown);
  }

  /**
   * 批处理"切除"的处理方法
   */
  private void openBatchRemoveDialog() {
    if (this.batchRemoveDialog == null) {
      this.batchRemoveDialog = new BatchRemoveDialog(this, true, this.txaMain);
    } else {
      this.batchRemoveDialog.setTextArea(this.txaMain);
      this.batchRemoveDialog.setVisible(true);
    }
  }

  /**
   * 批处理"插入"的处理方法
   */
  private void openBatchInsertDialog() {
    if (this.batchInsertDialog == null) {
      this.batchInsertDialog = new BatchInsertDialog(this, true, this.txaMain);
    } else {
      this.batchInsertDialog.setTextArea(this.txaMain);
      this.batchInsertDialog.setVisible(true);
    }
  }

  /**
   * "高亮显示"中各格式的处理方法
   * 
   * @param style
   *          按照某种颜色进行高亮显示，其取值有1、2、3、4、5
   */
  private void setHighlight(int style) {
    String strSelText = this.txaMain.getSelectedText();
    if (strSelText == null || strSelText.isEmpty()) {
      return;
    }
    String strText = this.txaMain.getText();
    LinkedList<Integer> linkedList = new LinkedList<Integer>();
    int index = -1;
    do {
      int start = 0;
      if (index >= 0) {
        start += index + strSelText.length();
      }
      index = strText.indexOf(strSelText, start);
      if (index >= 0) {
        linkedList.add(index);
      }
    } while (index >= 0);
    Color color = null;
    switch (style) {
    case 1:
      color = Util.COLOR_HIGHLIGHT_1;
      break;
    case 2:
      color = Util.COLOR_HIGHLIGHT_2;
      break;
    case 3:
      color = Util.COLOR_HIGHLIGHT_3;
      break;
    case 4:
      color = Util.COLOR_HIGHLIGHT_4;
      break;
    case 5:
      color = Util.COLOR_HIGHLIGHT_5;
      break;
    }
    for (Integer startIndex : linkedList) {
      try {
        this.txaMain.getHighlighter().addHighlight(startIndex,
            startIndex + strSelText.length(),
            new DefaultHighlighter.DefaultHighlightPainter(color));
        Highlighter.Highlight[] arrHighlight = this.txaMain.getHighlighter()
            .getHighlights();
        this.txaMain.getHighlighterList().add(
            new PartnerBean(arrHighlight[arrHighlight.length - 1], style));
      } catch (BadLocationException x) {
        x.printStackTrace();
      }
    }
  }

  /**
   * "清除高亮"中各格式的处理方法
   * 
   * @param style
   *          清除某种颜色的高亮显示，其取值有1、2、3、4、5和清除所有高亮（0）等共6种
   */
  private void rmHighlight(int style) {
    if (style == 0) {
      this.txaMain.getHighlighter().removeAllHighlights();
      this.txaMain.getHighlighterList().clear();
      return;
    }
    for (int n = 0; n < this.txaMain.getHighlighterList().size(); n++) {
      PartnerBean partnerBean = this.txaMain.getHighlighterList().get(n);
      if (partnerBean.getIndex() == style) {
        this.txaMain.getHighlighter().removeHighlight(
            (Highlighter.Highlight) partnerBean.getObject());
        this.txaMain.getHighlighterList().remove(n);
        n--;
      }
    }
  }

  /**
   * "颜色设置"中"全部反色/全部补色"的处理方法
   * 
   * @param mode
   *          用于标识反色/补色，如果为true表示反色，反之则为补色
   */
  private void setColorTransform(boolean mode) {
    Color colorFont = this.getConvertColor(this.txaMain.getForeground(), mode);
    Color colorBack = this.getConvertColor(this.txaMain.getBackground(), mode);
    Color colorCaret = this.getConvertColor(this.txaMain.getCaretColor(), mode);
    Color colorSelFont = this.getConvertColor(this.txaMain
        .getSelectedTextColor(), mode);
    Color colorSelBack = this.getConvertColor(this.txaMain.getSelectionColor(),
        mode);
    Color[] colorStyle = new Color[] { colorFont, colorBack, colorCaret,
        colorSelFont, colorSelBack };
    for (BaseTextArea textArea : this.textAreaList) {
      textArea.setColorStyle(colorStyle);
    }
    this.setting.colorStyle = colorStyle;
  }

  /**
   * 将指定的rgb模式颜色转化为反色/补色
   * 
   * @param color
   *          待处理的rgb模式颜色
   * @param mode
   *          用于标识反色/补色，如果为true表示反色，反之则为补色
   * @return 处理后的颜色
   */
  private Color getConvertColor(Color color, boolean mode) {
    if (color != null) {
      int red = color.getRed();
      int green = color.getGreen();
      int blue = color.getBlue();
      if (mode) { // 反色
        red = 255 - red;
        green = 255 - green;
        blue = 255 - blue;
      } else { // 补色
        int min = red;
        int max = red;
        if (min > green) {
          min = green;
        }
        if (min > blue) {
          min = blue;
        }
        if (max < green) {
          max = green;
        }
        if (max < blue) {
          max = blue;
        }
        int total = max + min;
        red = total - red;
        green = total - green;
        blue = total - blue;
      }
      color = new Color(red, green, blue);
    }
    return color;
  }

  /**
   * 设置"配色方案"的处理方法
   * 
   * @param style
   *          配色方案序号，有1、2、3、4、5以及默认配色（0）等共6种
   */
  private void setColorStyle(int style) {
    if (style > 0 && style <= Util.COLOR_STYLES.length) {
      this.setting.colorStyle = Util.COLOR_STYLES[style - 1];
    } else {
      this.setting.colorStyle = this.defColorStyle;
    }
    for (BaseTextArea textArea : this.textAreaList) {
      textArea.setColorStyle(this.setting.colorStyle);
    }
  }

  /**
   * "排序"的处理方法
   * 
   * @param order
   *          排序的顺序，升序为true，降序为false
   */
  private void sortLines(boolean order) {
    if (this.txaMain.getText().isEmpty()) {
      return;
    }
    CurrentLines currentLines = new CurrentLines(this.txaMain);
    int lineCount = currentLines.getLineCount();
    if (lineCount < 2) {
      this.txaMain.selectAll();
    } else {
      this.txaMain.select(currentLines.getStartIndex(), currentLines
          .getEndIndex());
    }
    String strSelText = this.txaMain.getSelectedText();
    String[] arrText = strSelText.split("\n", -1); // 将当前选区的文本分行处理，包括末尾的多处空行
    if (arrText.length <= 1) {
      return;
    }
    for (int i = 0; i < arrText.length; i++) { // 冒泡排序
      for (int j = 0; j < i; j++) {
        if (arrText[i].compareTo(arrText[j]) < 0) {
          String str = arrText[i];
          arrText[i] = arrText[j];
          arrText[j] = str;
        }
      }
    }
    StringBuilder stbSorted = new StringBuilder();
    if (order) { // 升序
      for (String str : arrText) {
        stbSorted.append(str + "\n");
      }
      if (stbSorted.toString().startsWith("\n")) {
        stbSorted.deleteCharAt(0); // 删除字符串开头多余的换行符
      } else {
        stbSorted.deleteCharAt(stbSorted.length() - 1); // 删除字符串末尾多余的换行符
      }
      this.txaMain.replaceSelection(stbSorted.toString());
    } else { // 降序
      for (String str : arrText) {
        stbSorted.insert(0, str + "\n");
      }
      this.txaMain.replaceSelection(stbSorted.deleteCharAt(
          stbSorted.length() - 1).toString()); // 删除字符串末尾多余的换行符
    }
  }

  /**
   * "自动缩进"的处理方法
   */
  private void setAutoIndent() {
    boolean enable = this.itemAutoIndent.isSelected();
    for (BaseTextArea textArea : this.textAreaList) {
      textArea.setAutoIndent(enable);
    }
    this.setting.autoIndent = enable;
  }

  /**
   * "上移当前行"的处理方法
   */
  private void moveLineToUp() {
    CurrentLines currentLines = new CurrentLines(this.txaMain,
        CurrentLines.LineExtend.EXTEND_UP);
    String strContentExtend = currentLines.getStrContentExtend();
    if (strContentExtend == null) {
      return;
    }
    int startIndex = currentLines.getStartIndex();
    int endIndex = currentLines.getEndIndex();
    int endLineNum = currentLines.getEndLineNum();
    String strContentCurrent = currentLines.getStrContentCurrent();
    if (endLineNum == this.txaMain.getLineCount() - 1) {
      // 如果当前实际选区的末行为文本域末行，应作一定处理
      strContentCurrent += "\n";
      strContentExtend = strContentExtend.substring(0, strContentExtend
          .length() - 1);
    }
    String strMoved = strContentCurrent + strContentExtend;
    this.txaMain.replaceRange(strMoved, startIndex, endIndex);
    this.txaMain
        .select(startIndex, startIndex + strContentCurrent.length() - 1);
  }

  /**
   * "下移当前行"的处理方法
   */
  private void moveLineToDown() {
    CurrentLines currentLines = new CurrentLines(this.txaMain,
        CurrentLines.LineExtend.EXTEND_DOWN);
    String strContentExtend = currentLines.getStrContentExtend();
    if (strContentExtend == null) {
      return;
    }
    int startIndex = currentLines.getStartIndex();
    int endIndex = currentLines.getEndIndex();
    int endLineNum = currentLines.getEndLineNum();
    String strContent = currentLines.getStrContent();
    String strContentCurrent = currentLines.getStrContentCurrent();
    boolean isReachEnd = false;
    if (endLineNum == this.txaMain.getLineCount() - 1) {
      // 如果扩展行为文本域末行，应作一定处理
      strContentCurrent = strContentCurrent.substring(0, strContentCurrent
          .length() - 1);
      strContentExtend += "\n";
      isReachEnd = true;
    }
    String strMoved = strContentExtend + strContentCurrent;
    this.txaMain.replaceRange(strMoved, startIndex, endIndex);
    int selectEndIndex = startIndex + strContent.length() - 1; // 移动行之后，重新选择被移动多行的行末偏移量
    if (isReachEnd) {
      selectEndIndex++;
    }
    this.txaMain.select(startIndex + strContentExtend.length(), selectEndIndex);
  }

  /**
   * "清空最近编辑列表"的处理方法
   */
  private void clearFileHistory() {
    if (!this.fileHistoryList.isEmpty()) {
      this.fileHistoryList.clear();
      this.menuFileHistory.removeAll();
      this.setFileHistoryMenuEnabled();
    }
  }

  /**
   * 清除"选区内空白"的处理方法
   */
  private void trimSelected() {
    StringBuilder stbSelected = new StringBuilder(this.txaMain
        .getSelectedText());
    if (stbSelected.toString().isEmpty()) {
      return;
    }
    boolean label = false; // 是否存在空白字符的标识符
    for (int i = 0; i < stbSelected.length(); i++) {
      switch (stbSelected.charAt(i)) {
      case ' ':
      case '\t':
      case '　':
        stbSelected.deleteCharAt(i);
        label = true; // 查找到空白字符
        i--; // 由于删除了查找到的空白字符，需要回退索引值
        break;
      }
    }
    if (label) {
      this.txaMain.replaceSelection(stbSelected.toString());
    }
  }

  /**
   * 设置"字符编码格式"的处理方法
   * 
   * @param encoding
   *          字符编码格式的枚举值
   * @param isUpdateMenu
   *          是否更新菜单的选择
   */
  private void setCharEncoding(CharEncoding encoding, boolean isUpdateMenu) {
    if (isUpdateMenu) {
      this.txaMain.setCharEncoding(encoding);
      this.setCharEncodingSelected();
    } else if (!this.txaMain.getCharEncoding().equals(encoding)) {
      this.txaMain.setCharEncoding(encoding);
      this.txaMain.setStyleChanged(true);
      this.setStylePrefix();
    }
    this.updateStateEncoding();
  }

  /**
   * 根据采用的字符编码格式，更新菜单的选择
   */
  private void setCharEncodingSelected() {
    switch (this.txaMain.getCharEncoding()) {
    case UTF8:
      this.itemCharsetUTF8.setSelected(true);
      break;
    case UTF8_NO_BOM:
      this.itemCharsetUTF8_NO_BOM.setSelected(true);
      break;
    case ULE:
      this.itemCharsetULE.setSelected(true);
      break;
    case UBE:
      this.itemCharsetUBE.setSelected(true);
      break;
    case ANSI:
      this.itemCharsetANSI.setSelected(true);
      break;
    default:
      this.itemCharsetBASE.setSelected(true);
      break;
    }
  }

  /**
   * 设置"换行符格式"的处理方法
   * 
   * @param lineSeparator
   *          待采用的换行符
   * @param isUpdateMenu
   *          是否更新菜单的选择
   */
  private void setLineStyleString(LineSeparator lineSeparator,
      boolean isUpdateMenu) {
    if (isUpdateMenu) {
      this.txaMain.setLineSeparator(lineSeparator);
      this.setLineStyleSelected();
    } else if (!this.txaMain.getLineSeparator().equals(lineSeparator)) {
      this.txaMain.setLineSeparator(lineSeparator);
      this.txaMain.setStyleChanged(true);
      this.setStylePrefix();
    }
    this.updateStateLineStyle();
  }

  /**
   * 根据采用的换行符，更新菜单的选择
   */
  private void setLineStyleSelected() {
    switch (this.txaMain.getLineSeparator()) {
    case UNIX:
      this.itemLineStyleUnix.setSelected(true);
      break;
    case MACINTOSH:
      this.itemLineStyleMac.setSelected(true);
      break;
    case WINDOWS:
      this.itemLineStyleWin.setSelected(true);
      break;
    case DEFAULT:
      if (LineSeparator.DEFAULT.toString().equals(
          LineSeparator.WINDOWS.toString())) {
        this.itemLineStyleWin.setSelected(true);
      } else if (LineSeparator.DEFAULT.toString().equals(
          LineSeparator.UNIX.toString())) {
        this.itemLineStyleUnix.setSelected(true);
      } else if (LineSeparator.DEFAULT.toString().equals(
          LineSeparator.MACINTOSH.toString())) {
        this.itemLineStyleMac.setSelected(true);
      }
    }
  }

  /**
   * 插入"特殊字符"的处理方法
   */
  private void openInsertCharDialog() {
    if (this.insertCharDialog == null) {
      Hashtable<String, String> hashtable = new Hashtable<String, String>();
      hashtable.put("特殊符号", Util.INSERT_SPECIAL);
      hashtable.put("标点符号", Util.INSERT_PUNCTUATION);
      hashtable.put("数学符号", Util.INSERT_MATH);
      hashtable.put("单位符号", Util.INSERT_UNIT);
      hashtable.put("数字符号", Util.INSERT_DIGIT);
      hashtable.put("拼音符号", Util.INSERT_PINYIN);
      this.insertCharDialog = new InsertCharDialog(this, false, this.txaMain,
          hashtable);
    } else if (!this.insertCharDialog.isVisible()) {
      this.insertCharDialog.setTextArea(this.txaMain);
      this.insertCharDialog.setVisible(true);
    }
  }

  /**
   * "换行方式"的处理方法
   * 
   * @param isByWord
   *          换行方式，true表示以单词边界换行，false表示以字符边界换行
   */
  private void setLineWrapStyle(boolean isByWord) {
    for (BaseTextArea textArea : this.textAreaList) {
      textArea.setWrapStyleWord(isByWord);
    }
    this.setting.isWrapStyleWord = isByWord;
  }

  /**
   * "删除至行首"的处理方法
   */
  private void deleteLineToStart() {
    CurrentLine currentLine = new CurrentLine(this.txaMain);
    int startIndex = currentLine.getStartIndex();
    int currentIndex = currentLine.getCurrentIndex();
    if (currentIndex != startIndex) {
      this.txaMain.replaceRange("", startIndex, currentIndex);
    }
  }

  /**
   * "删除至行尾"的处理方法
   */
  private void deleteLineToEnd() {
    CurrentLine currentLine = new CurrentLine(this.txaMain);
    int endIndex = currentLine.getEndIndex();
    int currentIndex = currentLine.getCurrentIndex();
    int lineNum = currentLine.getLineNum();
    int lineCount = this.txaMain.getLineCount();
    if (currentIndex != endIndex) {
      if (lineCount > lineNum + 1) {
        endIndex--;
      }
      this.txaMain.replaceRange("", currentIndex, endIndex);
    }
  }

  /**
   * 添加最近编辑的文件子菜单
   * 
   * @param strFile
   *          完整的文件路径
   */
  private void addFileHistoryItem(String strFile) {
    if (strFile == null || strFile.isEmpty()) {
      return;
    }
    int index = this.checkFileInHistory(strFile);
    if (index >= 0) {
      JMenuItem itemFile = new JMenuItem(strFile);
      itemFile.setActionCommand(Util.FILE_HISTORY);
      itemFile.addActionListener(this);
      if (this.fileHistoryList.size() > index) {
        this.fileHistoryList.remove(index);
        this.menuFileHistory.remove(index);
      }
      this.fileHistoryList.add(strFile);
      this.menuFileHistory.add(itemFile);
      this.setFileHistoryMenuEnabled();
    }
  }

  /**
   * 检测文件名是否已存在
   * 
   * @param strFile
   *          完整的文件路径
   * @return 将要添加到最近编辑的索引，-1表示产生异常
   */
  private int checkFileInHistory(String strFile) {
    if (strFile == null || strFile.isEmpty()) {
      return -1;
    }
    int index = -1;
    int listSize = this.fileHistoryList.size();
    if (listSize == 0) {
      index = 0;
    } else {
      index = this.fileHistoryList.indexOf(strFile);
      if (index < 0) {
        if (listSize >= Util.FILE_HISTORY_MAX) {
          index = 0;
        } else {
          index = listSize;
        }
      }
    }
    return index;
  }

  /**
   * 打开最近编辑的文件
   * 
   * @param strFile
   *          最近编辑的完整文件路径
   */
  private void openFileHistory(String strFile) {
    if (!this.saveFileBeforeAct()) {
      return;
    }
    if (strFile == null || strFile.isEmpty()) {
      return;
    }
    File file = new File(strFile);
    if (file != null && file.exists()) {
      boolean toCreateNew = this.checkToCreateNew(file);
      if (!toCreateNew && !this.saveFileBeforeAct()) {
        return;
      }
      int index = this.getCurrentIndexBySameFile(file);
      this.toOpenFile(file, true, toCreateNew);
      this.setAfterOpenFile(index);
      this.setFileNameAndPath(file);
    } else {
      JOptionPane.showMessageDialog(this, Util.convertToMsg("文件：" + file
          + " 不存在！"), Util.SOFTWARE, JOptionPane.ERROR_MESSAGE);
    }
  }

  /**
   * "删除全文空行"的处理方法
   */
  private void delAllNullLines() {
    String strTextAll = this.txaMain.getText();
    String strText = this.delNullLines(strTextAll);
    if (!strTextAll.equals(strText)) {
      this.txaMain.setText(strText);
    }
  }

  /**
   * "删除选区内空行"的处理方法
   */
  private void delSelectedNullLines() {
    String strTextSel = this.txaMain.getSelectedText();
    String strText = this.delNullLines(strTextSel);
    if (!strTextSel.equals(strText)) {
      this.txaMain.replaceSelection(strText);
    }
  }

  /**
   * 删除文本内空行
   * 
   * @param strText
   *          待处理的文本
   * @return 删除空行后的文本
   */
  private String delNullLines(String strText) {
    if (strText == null) {
      return strText;
    }
    String strDouble = "\n\n";
    int index = strText.indexOf(strDouble);
    boolean hasEnter = false;
    if (strText.startsWith("\n") || strText.endsWith("\n")) {
      hasEnter = true;
    }
    if (index < 0 && !hasEnter) {
      return strText;
    }
    while (index >= 0) {
      strText = strText.replaceAll(strDouble, "\n");
      index = strText.indexOf(strDouble);
    }
    if (strText.startsWith("\n")) {
      strText = strText.substring(1, strText.length());
    }
    if (strText.endsWith("\n")) {
      strText = strText.substring(0, strText.length() - 1);
    }
    return strText;
  }

  /**
   * 更改配色方案
   * 
   * @param color
   *          待设置的颜色
   * @param index
   *          配色方案中需要修改的颜色索引
   */
  private void changeColorStyle(Color color, int index) {
    if (color != null) {
      Color[] colorStyle = this.txaMain.getColorStyle();
      if (colorStyle == null) {
        colorStyle = this.defColorStyle;
      }
      colorStyle[index] = color;
      for (BaseTextArea textArea : this.textAreaList) {
        textArea.setColorStyle(colorStyle);
      }
      this.setting.colorStyle = colorStyle;
    }
  }

  /**
   * "字体颜色"的处理方法
   */
  private void setFontColor() {
    Color color = JColorChooser.showDialog(this, "字体颜色", this.txaMain
        .getForeground());
    this.changeColorStyle(color, 0);
  }

  /**
   * "背景颜色"的处理方法
   */
  private void setBackColor() {
    Color color = JColorChooser.showDialog(this, "背景颜色", this.txaMain
        .getBackground());
    this.changeColorStyle(color, 1);
  }

  /**
   * "光标颜色"的处理方法
   */
  private void setCaretColor() {
    Color color = JColorChooser.showDialog(this, "光标颜色", this.txaMain
        .getCaretColor());
    this.changeColorStyle(color, 2);
  }

  /**
   * "选区字体颜色"的处理方法
   */
  private void setSelFontColor() {
    Color color = JColorChooser.showDialog(this, "选区字体颜色", this.txaMain
        .getSelectedTextColor());
    this.changeColorStyle(color, 3);
  }

  /**
   * "选区背景颜色"的处理方法
   */
  private void setSelBackColor() {
    Color color = JColorChooser.showDialog(this, "选区背景颜色", this.txaMain
        .getSelectionColor());
    this.changeColorStyle(color, 4);
  }

  /**
   * "Tab键设置"的处理方法
   */
  private void openTabSetDialog() {
    if (this.tabSetDialog == null) {
      this.tabSetDialog = new TabSetDialog(this, true, this.txaMain);
    } else {
      this.tabSetDialog.setVisible(true);
    }
    boolean enable = this.tabSetDialog.getReplaceBySpace();
    for (BaseTextArea textArea : this.textAreaList) {
      textArea.setTabReplaceBySpace(enable);
    }
    this.setting.tabReplaceBySpace = enable;
  }

  /**
   * "文本拖拽"的处理方法
   */
  private void setTextDrag() {
    boolean isTextDrag = this.itemTextDrag.isSelected();
    for (BaseTextArea textArea : this.textAreaList) {
      textArea.setDragEnabled(isTextDrag);
    }
    this.setting.textDrag = isTextDrag;
  }

  /**
   * 字体缩放："放大"的处理方法
   */
  private void setFontSizePlus() {
    Font font = this.txaMain.getFont();
    if (font.getSize() >= Util.MAX_FONT_SIZE) {
      return;
    }
    this.setting.font = new Font(font.getFamily(), font.getStyle(), font
        .getSize() + 1);
    this.setTextAreaFont();
  }

  /**
   * 字体缩放："缩小"的处理方法
   */
  private void setFontSizeMinus() {
    Font font = this.txaMain.getFont();
    if (font.getSize() <= Util.MIN_FONT_SIZE) {
      return;
    }
    this.setting.font = new Font(font.getFamily(), font.getStyle(), font
        .getSize() - 1);
    this.setTextAreaFont();
  }

  /**
   * 字体缩放："恢复初始"的处理方法
   */
  private void setFontSizeReset() {
    Font font = this.txaMain.getFont();
    this.setting.font = new Font(font.getFamily(), font.getStyle(),
        Util.TEXT_FONT.getSize());
    this.setTextAreaFont();
  }

  /**
   * 清除空白的处理方法
   * 
   * @param position
   *          清除空白的位置，0为“行首”空白，1为“行尾”空白，2为“行首+行尾”空白
   */
  private void trimLines(int position) {
    CurrentLines currentLines = new CurrentLines(this.txaMain);
    String strContent = currentLines.getStrContent();
    if (strContent == null || strContent.isEmpty()) {
      return;
    }
    String arrContents[] = strContent.split("\n", -1); // 将当前选区的文本分行处理，包括末尾的多处空行
    StringBuilder stbContent = new StringBuilder(); // 用于存放处理后的文本
    for (int n = 0; n < arrContents.length; n++) {
      String strLine = arrContents[n];
      if (strLine.isEmpty()) {
        stbContent.append("\n");
        continue;
      }
      if (position == 0) { // 清除"行首"空白
        stbContent.append(this.trimLine(strLine, true) + "\n");
      } else if (position == 1) { // 清除"行尾"空白
        stbContent.append(this.trimLine(strLine, false) + "\n");
      } else if (position == 2) { // 清除"行首+行尾"空白
        strLine = this.trimLine(strLine, true);
        stbContent.append(this.trimLine(strLine, false) + "\n");
      }
    }
    if (stbContent != null) {
      int startIndex = currentLines.getStartIndex();
      int endIndex = currentLines.getEndIndex();
      this.txaMain.replaceRange(stbContent
          .deleteCharAt(stbContent.length() - 1).toString(), startIndex,
          endIndex);
    }
  }

  /**
   * 清除一行文本的空白
   * 
   * @param strLine
   *          待处理的一行文本
   * @param position
   *          清除空白的位置，true为“行首”空白，false为“行尾”空白
   * @return 清除指定空白后的文本
   */
  private String trimLine(String strLine, boolean position) {
    if (strLine == null || strLine.isEmpty()) {
      return strLine;
    }
    int blank = 0; // 空白字符的个数
    boolean label = false; // 用于标识空白字符的结束
    if (position) { // 清除"行首"空白
      for (int i = 0; i < strLine.length(); i++) {
        switch (strLine.charAt(i)) {
        case ' ':
        case '\t':
        case '　':
          blank++;
          break;
        default:
          label = true;
          break;
        }
        if (label) {
          break;
        }
      }
      strLine = strLine.substring(blank);
    } else { // 清除"行尾"空白
      for (int i = strLine.length() - 1; i >= 0; i--) {
        switch (strLine.charAt(i)) {
        case ' ':
        case '\t':
        case '　':
          blank++;
          break;
        default:
          label = true;
          break;
        }
        if (label) {
          break;
        }
      }
      strLine = strLine.substring(0, strLine.length() - blank);
    }
    return strLine;
  }

  /**
   * 设置标题栏开头的"*"号标识，以表示文本是否已修改
   */
  private void setTextPrefix() {
    if (this.txaMain.getTextChanged()) {
      if (!this.stbTitle.toString().startsWith(Util.TEXT_PREFIX)) {
        this.stbTitle.insert(0, Util.TEXT_PREFIX); // 在标题栏的开头添加"*"
      }
    } else {
      if (this.stbTitle.toString().startsWith(Util.TEXT_PREFIX)) {
        this.stbTitle.deleteCharAt(0); // 删除标题栏开头的"*"
      }
    }
    this.setTitle(this.stbTitle.toString());
    this.tpnMain.setTitleAt(this.tpnMain.getSelectedIndex(), this.getPrefix()
        + this.txaMain.getTitle());
  }

  /**
   * 设置标题栏开头的"※"号标识，以表示文本格式是否已修改
   */
  private void setStylePrefix() {
    if (this.txaMain.getStyleChanged()) {
      if (this.stbTitle.toString().startsWith(
          Util.TEXT_PREFIX + Util.STYLE_PREFIX)) {
      } else if (this.stbTitle.toString().startsWith(Util.TEXT_PREFIX)) {
        this.stbTitle.insert(1, Util.STYLE_PREFIX);
      } else if (this.stbTitle.toString().startsWith(Util.STYLE_PREFIX)) {
      } else {
        this.stbTitle.insert(0, Util.STYLE_PREFIX);
      }
    } else {
      if (this.stbTitle.toString().startsWith(
          Util.TEXT_PREFIX + Util.STYLE_PREFIX)) {
        this.stbTitle.deleteCharAt(1);
      } else if (this.stbTitle.toString().startsWith(Util.TEXT_PREFIX)) {
      } else if (this.stbTitle.toString().startsWith(Util.STYLE_PREFIX)) {
        this.stbTitle.deleteCharAt(0);
      } else {
      }
    }
    this.setTitle(this.stbTitle.toString());
    this.tpnMain.setTitleAt(this.tpnMain.getSelectedIndex(), this.getPrefix()
        + this.txaMain.getTitle());
  }

  /**
   * 获取因文本或格式修改产生的标题栏前缀字符"*"和"※"
   * 
   * @return 标题栏前缀字符，可能为：""、"*"、"※"、"*※"
   */
  private String getPrefix() {
    String prefix = "";
    if (this.txaMain.getTextChanged() && this.txaMain.getStyleChanged()) {
      prefix = Util.TEXT_PREFIX + Util.STYLE_PREFIX;
    } else if (this.txaMain.getTextChanged()) {
      prefix = Util.TEXT_PREFIX;
    } else if (this.txaMain.getStyleChanged()) {
      prefix = Util.STYLE_PREFIX;
    }
    return prefix;
  }

  /**
   * 设置系统剪贴板的内容
   * 
   * @param strText
   *          要存入剪贴板的文本
   */
  private void setClipboardContents(String strText) {
    if (strText == null || strText.isEmpty()) {
      return;
    }
    StringSelection ss = new StringSelection(strText);
    this.clip.setContents(ss, ss);
    this.itemPaste.setEnabled(true);
    this.itemPopPaste.setEnabled(true);
  }

  /**
   * 复制"当前文件名"到剪贴板的处理方法
   */
  private void toCopyFileName() {
    this.setClipboardContents(this.txaMain.getTitle());
  }

  /**
   * 复制"当前文件路径"到剪贴板的处理方法
   */
  private void toCopyFilePath() {
    String fileName = this.txaMain.getFileName();
    if (fileName == null) {
      fileName = this.txaMain.getTitle();
    }
    this.setClipboardContents(fileName);
  }

  /**
   * 复制"当前目录路径"到剪贴板的处理方法
   */
  private void toCopyDirPath() {
    String dirPath = " ";
    if (this.file != null) {
      dirPath = this.file.getParent();
    }
    this.setClipboardContents(dirPath);
  }

  /**
   * 复制"当前行"到剪贴板的处理方法
   */
  private void toCopyCurLine() {
    CurrentLines currentLines = new CurrentLines(this.txaMain);
    String strContent = currentLines.getStrContent();
    if (!strContent.endsWith("\n")) {
      strContent += "\n";
    }
    this.setClipboardContents(strContent);
  }

  /**
   * 剪切"当前行"到剪贴板的处理方法
   */
  private void toCutCurLine() {
    CurrentLines currentLines = new CurrentLines(this.txaMain);
    String strContent = currentLines.getStrContent();
    int startIndex = currentLines.getStartIndex();
    int endIndex = currentLines.getEndIndex();
    if (!strContent.endsWith("\n")) {
      strContent += "\n";
    }
    this.setClipboardContents(strContent);
    this.txaMain.replaceRange("", startIndex, endIndex);
  }

  /**
   * 复制"所有文本"到剪贴板的处理方法
   */
  private void toCopyAllText() {
    this.setClipboardContents(this.txaMain.getText());
  }

  /**
   * "锁定窗口"的处理方法
   */
  private void setResizable() {
    this.setResizable(!this.itemResizable.isSelected());
  }

  /**
   * "前端显示"的处理方法
   */
  private void setAlwaysOnTop() {
    this.setAlwaysOnTop(this.itemAlwaysOnTop.isSelected());
  }

  /**
   * "复写当前选择"的处理方法
   */
  private void copySelectedText() {
    int start = this.txaMain.getSelectionStart();
    int end = this.txaMain.getSelectionEnd();
    if (start != end) {
      this.txaMain.insert(this.txaMain.getSelectedText(), end);
      this.txaMain.select(start, end);
    }
  }

  /**
   * "复写当前行"的处理方法
   */
  private void copyLines() {
    CurrentLines currentLines = new CurrentLines(this.txaMain);
    String strContent = currentLines.getStrContent();
    int endIndex = currentLines.getEndIndex();
    int currentIndex = currentLines.getCurrentIndex();
    int endLineNum = currentLines.getEndLineNum();
    if (currentIndex == this.txaMain.getText().length()
        || endLineNum == this.txaMain.getLineCount() - 1) {
      strContent = "\n" + strContent;
    }
    this.txaMain.insert(strContent, endIndex);
  }

  /**
   * "删除当前行"的处理方法
   */
  private void deleteLines() {
    CurrentLines currentLines = new CurrentLines(this.txaMain);
    int startIndex = currentLines.getStartIndex();
    int endIndex = currentLines.getEndIndex();
    int length = this.txaMain.getText().length();
    if (length > 0) {
      if (startIndex > 0 && endIndex == length) {
        startIndex--; // 位于文本域中非空的最后一行时，确保删除换行符
      }
      this.txaMain.replaceRange("", startIndex, endIndex);
    }
  }

  /**
   * "快速查找"的处理方法
   * 
   * @param isFindDown
   *          查找方向：向下查找为true，向上查找为false
   */
  private void quickFindText(boolean isFindDown) {
    String strFindText = this.txaMain.getSelectedText();
    if (strFindText != null && !strFindText.isEmpty()) {
      int index = Util.findText(strFindText, this.txaMain, isFindDown, false,
          true);
      if (index >= 0) {
        this.txaMain.select(index, index + strFindText.length());
      }
    }
  }

  /**
   * "切换大小写"的处理方法
   * 
   * @param isCaseUp
   *          为true表示切换为大写，为false表示切换为小写
   */
  private void switchCase(boolean isCaseUp) {
    String strSel = this.txaMain.getSelectedText();
    if (strSel.isEmpty()) {
      return;
    }
    int start = this.txaMain.getSelectionStart();
    int end = this.txaMain.getSelectionEnd();
    if (isCaseUp) {
      this.txaMain.replaceSelection(strSel.toUpperCase());
    } else {
      this.txaMain.replaceSelection(strSel.toLowerCase());
    }
    this.txaMain.select(start, end);
  }

  /**
   * "删除当前文件"的处理方法
   */
  private void deleteFile() {
    int result = JOptionPane.showConfirmDialog(this, Util
        .convertToMsg("此操作将删除磁盘文件：" + this.file + "\n是否继续？"), Util.SOFTWARE,
        JOptionPane.YES_NO_CANCEL_OPTION);
    if (result != JOptionPane.YES_OPTION) {
      return;
    }
    if (this.file.delete()) {
      this.closeFile(false);
    } else {
      JOptionPane.showMessageDialog(this, Util.convertToMsg("文件：" + this.file
          + "删除失败！"), Util.SOFTWARE, JOptionPane.ERROR_MESSAGE);
    }
  }

  /**
   * "重命名"的处理方法
   */
  private void reNameFile() {
    if (this.saveFileChooser == null) {
      this.saveFileChooser = new SaveFileChooser();
    }
    this.saveFileChooser.setSelectedFile(this.file);
    this.saveFileChooser.setDialogTitle("重命名");
    if (JFileChooser.APPROVE_OPTION != this.saveFileChooser
        .showSaveDialog(this)) {
      return;
    }
    File fileReName = this.saveFileChooser.getSelectedFile();
    if (this.file.equals(fileReName)) { // 文件名未修改时，不做操作
      return;
    }
    try {
      this.toSaveFile(fileReName);
    } catch (Exception x) {
      this.showSaveErrorDialog(fileReName);
      return;
    }
    this.file.delete(); // 删除原文件
    this.setFileNameAndPath(fileReName);
  }

  /**
   * "撤销"的处理方法
   */
  private void undoAction() {
    if (this.undoManager.canUndo()) { // 判断是否可以撤销
      this.undoManager.undo(); // 执行撤销操作
      this.txaMain.setUndoIndex(this.txaMain.getUndoIndex() - 1); // 撤销标识符递减
      this.updateStateAll();
    }
    this.setAfterUndoRedo();
  }

  /**
   * "重做"的处理方法
   */
  private void redoAction() {
    if (this.undoManager.canRedo()) { // 判断是否可以重做
      this.undoManager.redo(); // 执行重做操作
      this.txaMain.setUndoIndex(this.txaMain.getUndoIndex() + 1); // 撤销标识符递增
      this.updateStateAll();
    }
    this.setAfterUndoRedo();
  }

  /**
   * 执行"撤销"或"重做"后的相关设置
   */
  private void setAfterUndoRedo() {
    if (this.txaMain.getUndoIndex() == Util.DEFAULT_UNDO_INDEX) { // 撤销标识符与默认值相等，表示文本未修改
      this.txaMain.setTextChanged(false);
    } else {
      this.txaMain.setTextChanged(true);
    }
    this.setTextPrefix();
    this.setMenuStateUndoRedo(); // 设置撤销和重做菜单的状态
    this.setMenuStateByTextArea();
  }

  /**
   * "时间/日期"的处理方法
   */
  private void openInsertDateDialog() {
    if (this.insertDateDialog == null) {
      this.insertDateDialog = new InsertDateDialog(this, false, this.txaMain);
    } else if (!this.insertDateDialog.isVisible()) {
      this.insertDateDialog.setTextArea(this.txaMain);
      this.insertDateDialog.setVisible(true);
    }
  }

  /**
   * "退出"的处理方法
   */
  private void exit() {
    boolean toExit = true;
    for (int i = 0; i < this.textAreaList.size(); i++) {
      this.tpnMain.setSelectedIndex(i);
      if (!saveFileBeforeAct()) { // 关闭程序前检测文件是否已修改
        toExit = false;
        break;
      }
    }
    if (toExit) {
      System.exit(0);
    }
  }

  /**
   * "关于"的处理方法
   */
  private void showAbout() {
    if (this.aboutDialog == null) {
      final String strBaiduSpace = "http://hi.baidu.com/xiboliya";
      final String strGoogleCode = "http://code.google.com/p/snowpad";
      final String strGithubCode = "https://github.com/xiboliya/snowpad";
      String[] arrStrLabel = new String[] {
          "软件名称：" + Util.SOFTWARE,
          "软件版本：" + Util.VERSION,
          "软件作者：冰原",
          "<html>百度空间：<a href='" + strBaiduSpace + "'>" + strBaiduSpace
              + "</a></html>",
          "<html>谷歌代码：<a href='" + strGoogleCode + "'>" + strGoogleCode
              + "</a></html>",
          "<html>GitHub：<a href='" + strGithubCode + "'>" + strGithubCode
              + "</a></html>", "软件版权：此为自由软件可以任意引用或修改" };
      this.aboutDialog = new AboutDialog(this, true, arrStrLabel, this.icon);
      this.aboutDialog.addLinkByIndex(3, strBaiduSpace);
      this.aboutDialog.addLinkByIndex(4, strGoogleCode);
      this.aboutDialog.addLinkByIndex(5, strGithubCode);
      this.aboutDialog.pack(); // 自动调整窗口大小，以适应各组件
    }
    this.aboutDialog.setVisible(true);
  }

  /**
   * "自动换行"的处理方法
   */
  private void setLineWrap() {
    boolean isLineWrap = this.itemLineWrap.isSelected();
    this.menuLineWrapStyle.setEnabled(isLineWrap);
    for (BaseTextArea textArea : this.textAreaList) {
      textArea.setLineWrap(isLineWrap);
    }
    this.itemLineNumber.setEnabled(!isLineWrap); // 如果开启了"自动换行"，则"行号栏"功能失效
    this.setLineNumber(this.setting.isLineNumberView); // 设置行号栏是否显示
    this.setting.isLineWrap = isLineWrap;
  }

  /**
   * 当前编辑的文本内容或格式已修改，当执行新建、关闭操作时，弹出对话框，让用户选择相应的操作
   * 
   * @return 用户选择了是或否时返回true，选择取消或关闭时返回false
   */
  private boolean saveFileBeforeAct() {
    if (this.txaMain.getTextChanged() || this.txaMain.getStyleChanged()) {
      String strChanged = "内容";
      if (this.txaMain.getTextChanged() && this.txaMain.getStyleChanged()) {
        strChanged = "内容与格式";
      } else if (this.txaMain.getStyleChanged()) {
        strChanged = "格式";
      }
      String str = "\"" + this.txaMain.getTitle() + "\"" + " 的" + strChanged
          + "已经修改。\n想保存文件吗？";
      if (this.file != null) {
        str = "文件：" + this.file + " 的" + strChanged + "已经修改。\n想保存文件吗？";
      }
      int result = JOptionPane.showConfirmDialog(this, Util.convertToMsg(str),
          Util.SOFTWARE, JOptionPane.YES_NO_CANCEL_OPTION);
      if (result == JOptionPane.YES_OPTION) {
        return this.saveFile(false);
      } else if (result == JOptionPane.CANCEL_OPTION
          || result == JOptionPane.CLOSED_OPTION) {
        return false;
      }
    }
    return true;
  }

  /**
   * "新建"的处理方法
   * 
   * @param file
   *          待打开的文件
   */
  private void createNew(File file) {
    BaseTextArea txaNew = new BaseTextArea(this.setting);
    JScrollPane srpNew = new JScrollPane(txaNew);
    String title = null;
    if (file != null) {
      title = file.getName();
      txaNew.setFile(file);
    } else {
      int index = this.toSetNewFileIndex();
      title = Util.NEW_FILE_NAME + index;
      txaNew.setNewFileIndex(index);
    }
    txaNew.setTitle(title);
    txaNew.addCaretListener(this);
    txaNew.getDocument().addUndoableEditListener(this);
    this.tpnMain.add(srpNew, title);
    this.tpnMain.setSelectedComponent(srpNew);
    this.textAreaList.add(txaNew);
    this.setLineNumberForNew();
    this.addTextAreaMouseListener();
  }

  /**
   * 新建文件时，重新设置文件序号
   * 
   * @return 新建文件的序号
   */
  private int toSetNewFileIndex() {
    int size = this.textAreaList.size();
    if (size == 0) {
      return 1;
    }
    LinkedList<Integer> indexList = new LinkedList<Integer>();
    for (int i = 0; i < size; i++) {
      BaseTextArea txa = (BaseTextArea) this.textAreaList.get(i);
      int newFileIndex = txa.getNewFileIndex();
      if (newFileIndex > 0) {
        indexList.add(newFileIndex);
      }
    }
    int index = 1;
    for (int i = 0; i < indexList.size(); i++) {
      for (Integer j : indexList) {
        if (index == j) {
          index++;
          break;
        }
      }
    }
    return index;
  }

  /**
   * "删除"的处理方法
   */
  private void deleteText() {
    this.txaMain.replaceSelection("");
  }

  /**
   * "复制"的处理方法
   */
  private void copyText() {
    this.setClipboardContents(this.txaMain.getSelectedText());
  }

  /**
   * "剪切"的处理方法
   */
  private void cutText() {
    this.copyText(); // 复制选中文本
    this.deleteText(); // 删除选中文本
  }

  /**
   * "粘贴"的处理方法
   */
  private void pasteText() {
    try {
      Transferable tf = this.clip.getContents(this);
      if (tf != null) {
        String str = tf.getTransferData(DataFlavor.stringFlavor).toString(); // 如果剪贴板内的内容不是文本，则将抛出异常
        if (str != null) {
          str = str.replaceAll(LineSeparator.WINDOWS.toString(),
              LineSeparator.UNIX.toString()); // 将Windows格式的换行符\n\r，转换为UNIX/Linux格式
          str = str.replaceAll(LineSeparator.MACINTOSH.toString(),
              LineSeparator.UNIX.toString()); // 为了容错，将可能残余的\r字符替换为\n
          this.txaMain.replaceSelection(str);
        }
      }
    } catch (Exception x) {
      // 剪贴板异常
      x.printStackTrace();
    }
  }

  /**
   * "全选"的处理方法
   */
  private void selectAll() {
    this.txaMain.selectAll();
  }

  /**
   * "字体"的处理方法
   */
  private void openFontChooser() {
    if (this.fontChooser == null) {
      this.fontChooser = new FontChooser(this, true, this.txaMain);
    } else {
      this.fontChooser.updateListView();
      this.fontChooser.setFontView();
      this.fontChooser.setStyleView();
      this.fontChooser.setSizeView();
      this.fontChooser.setVisible(true);
    }
    if (!this.fontChooser.getOk()) {
      return;
    }
    this.setting.font = this.fontChooser.getTextAreaFont();
    this.setTextAreaFont();
  }

  /**
   * 是否已经打开了查找或替换对话框
   * 
   * @return 如果已经打开了查找或替换对话框则返回false
   */
  private boolean canOpenDialog() {
    if (this.findReplaceDialog != null && this.findReplaceDialog.isVisible()) {
      return false;
    }
    return true;
  }

  /**
   * 预处理当前选中的文本，以便用于查找对话框的显示
   * 
   * @return 经处理之后的文本
   */
  private String checkSelText() {
    String strSel = this.txaMain.getSelectedText();
    if (strSel != null) {
      int index = strSel.indexOf("\n");
      if (index >= 0) {
        strSel = strSel.substring(0, index);
      }
    }
    return strSel;
  }

  /**
   * "查找"的处理方法
   */
  private void openFindDialog() {
    if (!this.canOpenDialog()) {
      this.findReplaceDialog.setTabbedIndex(0); // 打开查找选项卡
      return;
    }
    if (this.findReplaceDialog == null) {
      this.findReplaceDialog = new FindReplaceDialog(this, false, this.txaMain,
          true);
    } else {
      this.findReplaceDialog.setTextArea(this.txaMain);
      this.findReplaceDialog.setVisible(true);
    }
    this.findReplaceDialog.setTabbedIndex(0); // 打开查找选项卡
    String strSel = this.checkSelText();
    if (strSel != null && !strSel.isEmpty()) {
      this.findReplaceDialog.setFindText(strSel, true);
    } else {
      this.findReplaceDialog.setFindTextSelect();
    }
  }

  /**
   * "查找下一个"的处理方法
   * 
   * @param isFindDown
   *          查找的方向，如果向下查找则为true，反之则为false
   */
  private void findNextText(boolean isFindDown) {
    if (!this.canOpenDialog()) {
      this.findReplaceDialog.setTabbedIndex(0); // 打开查找选项卡
      return;
    }
    String strSel = this.checkSelText();
    if (this.findReplaceDialog == null) {
      this.findReplaceDialog = new FindReplaceDialog(this, false, this.txaMain,
          true);
      this.findReplaceDialog.setTabbedIndex(0); // 打开查找选项卡
      if (strSel != null && !strSel.isEmpty()) {
        this.findReplaceDialog.setFindText(strSel, true);
      }
    } else if (this.findReplaceDialog.getFindText().isEmpty()) {
      this.findReplaceDialog.setTextArea(this.txaMain);
      if (strSel != null && !strSel.isEmpty()) {
        this.findReplaceDialog.setFindText(strSel, true);
      }
      this.findReplaceDialog.setTabbedIndex(0); // 打开查找选项卡
      this.findReplaceDialog.setVisible(true);
    } else {
      this.findReplaceDialog.setTextArea(this.txaMain);
      this.findReplaceDialog.findText(isFindDown);
    }
  }

  /**
   * "替换"的处理方法
   */
  private void openReplaceDialog() {
    if (!this.canOpenDialog()) {
      this.findReplaceDialog.setTabbedIndex(1); // 打开替换选项卡
      return;
    }
    if (this.findReplaceDialog == null) {
      this.findReplaceDialog = new FindReplaceDialog(this, false, this.txaMain,
          true);
    } else {
      this.findReplaceDialog.setTextArea(this.txaMain);
      this.findReplaceDialog.setVisible(true);
    }
    this.findReplaceDialog.setTabbedIndex(1); // 打开替换选项卡
    String strSel = this.checkSelText();
    if (strSel != null && !strSel.isEmpty()) {
      this.findReplaceDialog.setFindText(strSel, true);
    } else {
      this.findReplaceDialog.setFindTextSelect();
    }
    this.findReplaceDialog.setReplaceText("");
  }

  /**
   * "转到"的处理方法
   */
  private void openGotoDialog() {
    if (this.gotoDialog == null) {
      this.gotoDialog = new GotoDialog(this, true, this.txaMain);
    } else {
      this.gotoDialog.setTextArea(this.txaMain);
      this.gotoDialog.setVisible(true);
    }
  }

  /**
   * "状态栏"的处理方法
   */
  private void setStateBar() {
    this.pnlState.setVisible(this.itemStateBar.isSelected());
  }

  /**
   * "重新载入文件"的处理方法
   */
  private void reOpenFile() {
    if (this.file == null) {
      return;
    }
    if (this.txaMain.getTextChanged() || this.txaMain.getStyleChanged()) {
      String strTemp = "内容";
      if (this.txaMain.getTextChanged() && this.txaMain.getStyleChanged()) {
        strTemp = "内容与格式";
      } else if (this.txaMain.getStyleChanged()) {
        strTemp = "格式";
      }
      int result = JOptionPane.showConfirmDialog(this, Util.convertToMsg("文件："
          + this.file + " 的" + strTemp + "已经修改。\n想放弃修改重新载入吗？"), Util.SOFTWARE,
          JOptionPane.YES_NO_CANCEL_OPTION);
      if (result != JOptionPane.YES_OPTION) {
        return;
      }
    }
    if (this.file.exists()) {
      int index = this.getCurrentIndexBySameFile(this.file);
      this.toOpenFile(this.file, true, false);
      this.setAfterOpenFile(index);
      this.setTextPrefix();
      this.setStylePrefix();
    } else {
      int result = JOptionPane.showConfirmDialog(this, Util.convertToMsg("文件："
          + this.file + "不存在。\n要重新创建吗？"), Util.SOFTWARE,
          JOptionPane.YES_NO_CANCEL_OPTION);
      if (result == JOptionPane.YES_OPTION) {
        this.checkFile(this.file);
        try {
          this.toSaveFile(this.file);
        } catch (Exception x) {
          this.showSaveErrorDialog(this.file);
          return;
        }
        this.setAfterSaveFile();
        this.setTextPrefix();
        this.setStylePrefix();
      }
    }
  }

  /**
   * 打开文件后的设置
   * 
   * @param index
   *          文本域的插入点位置
   */
  private void setAfterOpenFile(int index) {
    this.txaMain.setCaretPosition(Util.checkCaretPosition(this.txaMain, index)); // 设置插入点位置
    this.txaMain.setTextChanged(false);
    this.txaMain.setStyleChanged(false);
    this.txaMain.setSaved(true);
    this.undoManager.discardAllEdits();
    this.txaMain.setUndoIndex(Util.DEFAULT_UNDO_INDEX); // 撤销标识符恢复默认值
    this.setMenuStateUndoRedo(); // 设置撤销和重做菜单的状态
    this.itemReOpen.setEnabled(true);
    this.itemReName.setEnabled(true);
    this.itemDelFile.setEnabled(true);
    this.itemPopReName.setEnabled(true);
    this.itemPopDelFile.setEnabled(true);
    this.itemPopReOpen.setEnabled(true);
  }

  /**
   * "打开"的处理方法
   */
  private void openFile() {
    if (this.openFileChooser == null) {
      this.openFileChooser = new OpenFileChooser();
    }
    this.openFileChooser.setSelectedFile(null);
    if (JFileChooser.APPROVE_OPTION != this.openFileChooser
        .showOpenDialog(this)) {
      return;
    }
    File[] files = this.openFileChooser.getSelectedFiles();
    for (File file : files) {
      if (file != null && file.exists()) {
        boolean toCreateNew = this.checkToCreateNew(file);
        if (!toCreateNew && !this.saveFileBeforeAct()) {
          return;
        }
        int index = this.getCurrentIndexBySameFile(file);
        this.toOpenFile(file, true, toCreateNew);
        this.setAfterOpenFile(index);
        this.setFileNameAndPath(file);
      }
    }
  }

  /**
   * 检测是否需要新建文本域
   * 
   * @param file
   *          待打开的文件
   * @return 是否需要新建文本域，true表示需要新建，反之则不需要新建
   */
  private boolean checkToCreateNew(File file) {
    boolean toCreateNew = true;
    if (file != null) {
      for (int i = 0; i < this.textAreaList.size(); i++) {
        File fileTemp = this.textAreaList.get(i).getFile();
        if (file.equals(fileTemp)) {
          this.tpnMain.setSelectedIndex(i);
          toCreateNew = false;
          break;
        }
      }
    }
    if (this.file == null && !this.txaMain.getTextChanged()
        && !this.txaMain.getStyleChanged()) {
      toCreateNew = false;
    }
    return toCreateNew;
  }

  /**
   * 如果新打开的文件与当前编辑的文件相同，则获取当前文本域的插入点位置，否则返回默认位置
   * 
   * @param file
   *          新打开的文件
   * @return 将要设置的插入点位置
   */
  private int getCurrentIndexBySameFile(File file) {
    int index = Util.DEFAULT_CARET_INDEX;
    if (file.equals(this.file)) {
      index = this.txaMain.getCaretPosition();
    }
    return index;
  }

  /**
   * "以指定编码打开"的处理方法
   */
  private void openFileByEncoding() {
    if (this.fileEncodingDialog == null) {
      this.fileEncodingDialog = new FileEncodingDialog(this, true);
    } else {
      this.fileEncodingDialog.setVisible(true);
    }
    if (!this.fileEncodingDialog.getOk()) {
      return;
    }
    CharEncoding charEncoding = this.fileEncodingDialog.getCharEncoding();
    if (this.openFileChooser == null) {
      this.openFileChooser = new OpenFileChooser();
    }
    this.openFileChooser.setSelectedFile(null);
    if (JFileChooser.APPROVE_OPTION != this.openFileChooser
        .showOpenDialog(this)) {
      return;
    }
    File file = this.openFileChooser.getSelectedFile();
    if (file != null && file.exists()) {
      boolean toCreateNew = this.checkToCreateNew(file);
      int index = this.getCurrentIndexBySameFile(file);
      if (charEncoding != null) {
        this.setCharEncoding(charEncoding, true);
        this.toOpenFile(file, false, toCreateNew);
      } else {
        this.toOpenFile(file, true, toCreateNew);
      }
      this.setAfterOpenFile(index);
      this.setFileNameAndPath(file);
    }
  }

  /**
   * 根据文件开头的BOM（如果存在的话），判断文件的编码格式。 文本文件有各种不同的编码格式，如果判断有误，则会导致显示或保存错误。
   * 为了标识文件的编码格式，便于编辑和保存，则在文件开头加入了BOM，用以标识编码格式。 UTF-8格式：0xef 0xbb 0xbf， Unicode
   * Little Endian格式：0xff 0xfe， Unicode Big Endian格式：0xfe
   * 0xff。而ANSI格式是没有BOM的。另有一种不含BOM的UTF-8格式的文件，则不易与ANSI相区分，因此未能识别此类格式。
   * 
   * @param file
   *          待判断的文件
   */
  private void checkFileEncoding(File file) {
    FileInputStream fileInputStream = null;
    int charArr[] = new int[3];
    try {
      fileInputStream = new FileInputStream(file);
      for (int i = 0; i < charArr.length; i++) {
        charArr[i] = fileInputStream.read();
      }
    } catch (Exception x) {
      x.printStackTrace();
    } finally {
      try {
        fileInputStream.close();
      } catch (IOException x) {
        x.printStackTrace();
      }
    }
    if (charArr[0] == 0xff && charArr[1] == 0xfe) {
      this.setCharEncoding(CharEncoding.ULE, true);
    } else if (charArr[0] == 0xfe && charArr[1] == 0xff) {
      this.setCharEncoding(CharEncoding.UBE, true);
    } else if (charArr[0] == 0xef && charArr[1] == 0xbb && charArr[2] == 0xbf) {
      this.setCharEncoding(CharEncoding.UTF8, true);
    } else {
      this.setCharEncoding(CharEncoding.BASE, true);
    }
  }

  /**
   * 打开文件并将内容显示在文本域中
   * 
   * @param file
   *          打开的文件
   * @param isAutoCheckEncoding
   *          是否自动检测编码格式
   * @param toCreateNew
   *          是否需要新建文本域
   */
  private void toOpenFile(File file, boolean isAutoCheckEncoding,
      boolean toCreateNew) {
    InputStreamReader inputStreamReader = null;
    try {
      if (toCreateNew) {
        this.createNew(file);
      }
      if (isAutoCheckEncoding) {
        this.checkFileEncoding(file);
      }
      String strCharset = this.txaMain.getCharEncoding().toString();
      inputStreamReader = new InputStreamReader(new FileInputStream(file),
          strCharset);
      char chrBuf[] = new char[Util.BUFFER_LENGTH];
      int len = 0;
      StringBuilder stbTemp = new StringBuilder();
      switch (this.txaMain.getCharEncoding()) {
      case UTF8:
      case ULE:
      case UBE:
        inputStreamReader.read(); // 去掉文件开头的BOM
        break;
      }
      while ((len = inputStreamReader.read(chrBuf)) != -1) {
        stbTemp.append(chrBuf, 0, len);
      }
      String strTemp = stbTemp.toString();
      if (strTemp.indexOf(LineSeparator.WINDOWS.toString()) >= 0) {
        strTemp = strTemp.replaceAll(LineSeparator.WINDOWS.toString(),
            LineSeparator.UNIX.toString());
        this.setLineStyleString(LineSeparator.WINDOWS, true);
      } else if (strTemp.indexOf(LineSeparator.MACINTOSH.toString()) >= 0) {
        strTemp = strTemp.replaceAll(LineSeparator.MACINTOSH.toString(),
            LineSeparator.UNIX.toString());
        this.setLineStyleString(LineSeparator.MACINTOSH, true);
      } else if (strTemp.indexOf(LineSeparator.UNIX.toString()) >= 0) {
        this.setLineStyleString(LineSeparator.UNIX, true);
      } else { // 当文件内容不足1行时，则设置为系统默认的换行符
        this.setLineStyleString(LineSeparator.DEFAULT, true);
      }
      this.txaMain.setText(strTemp);
      this.addFileHistoryItem(file.getCanonicalPath()); // 添加最近编辑的文件列表
      this.txaMain.setFileExistsLabel(true);
      this.txaMain.setNewFileIndex(0);
    } catch (Exception x) {
      x.printStackTrace();
    } finally {
      try {
        inputStreamReader.close();
      } catch (IOException x) {
        x.printStackTrace();
      }
    }
  }

  /**
   * "保存"的处理方法
   * 
   * @param isSaveAs
   *          是否为"另存为"
   * @return 是否保存成功，如果成功返回true，如果失败则为false
   */
  private boolean saveFile(boolean isSaveAs) {
    boolean isFileExist = true; // 当前文件是否存在
    if (isSaveAs || !this.txaMain.getSaved()) {
      if (this.saveFileChooser == null) {
        this.saveFileChooser = new SaveFileChooser();
      }
      if (isSaveAs) {
        this.saveFileChooser.setDialogTitle("另存为");
      } else {
        this.saveFileChooser.setDialogTitle("保存");
      }
      this.saveFileChooser.setSelectedFile(null);
      if (JFileChooser.APPROVE_OPTION != this.saveFileChooser
          .showSaveDialog(this)) {
        return false;
      }
      File file = this.saveFileChooser.getSelectedFile();
      if (file != null) {
        try {
          this.toSaveFile(file);
        } catch (Exception x) {
          this.showSaveErrorDialog(file);
          return false;
        }
        this.setFileNameAndPath(file);
      } else {
        return false;
      }
    } else {
      if (this.file != null) {
        isFileExist = this.checkFile(this.file);
        try {
          this.toSaveFile(this.file);
        } catch (Exception x) {
          this.showSaveErrorDialog(this.file);
          return false;
        }
      } else {
        return false;
      }
    }
    this.setAfterSaveFile();
    this.setTextPrefix();
    this.setStylePrefix();
    if (!isFileExist) {
      JOptionPane.showMessageDialog(this, Util.convertToMsg("丢失的文件："
          + this.file.getAbsolutePath() + "\n已重新创建！"), Util.SOFTWARE,
          JOptionPane.CANCEL_OPTION);
    }
    return true;
  }

  /**
   * 检测文件以及所在的目录是否存在
   * 
   * @param file
   *          被检测的文件
   * @return 被检测文件是否存在，如果存在返回true，反之则为false
   */
  private boolean checkFile(File file) {
    File fileParent = new File(file.getParent()); // 获取文件的父目录
    if (!fileParent.exists()) {
      fileParent.mkdirs(); // 如果父目录不存在，则创建之
    }
    return file.exists();
  }

  /**
   * "另存为"的处理方法
   */
  private void saveAsFile() {
    this.saveFile(true);
  }

  /**
   * 将文本域中的文本保存到文件
   * 
   * @param file
   *          保存的文件
   */
  private void toSaveFile(File file) {
    FileOutputStream fileOutputStream = null;
    try {
      fileOutputStream = new FileOutputStream(file);
      String strText = this.txaMain.getText();
      strText = strText.replaceAll(LineSeparator.UNIX.toString(), this.txaMain
          .getLineSeparator().toString());
      byte byteStr[];
      int charBOM[] = new int[] { -1, -1, -1 }; // 根据当前的字符编码，存放BOM的数组
      switch (this.txaMain.getCharEncoding()) {
      case UTF8:
        charBOM[0] = 0xef;
        charBOM[1] = 0xbb;
        charBOM[2] = 0xbf;
        break;
      case ULE:
        charBOM[0] = 0xff;
        charBOM[1] = 0xfe;
        break;
      case UBE:
        charBOM[0] = 0xfe;
        charBOM[1] = 0xff;
        break;
      }
      byteStr = strText.getBytes(this.txaMain.getCharEncoding().toString());
      for (int i = 0; i < charBOM.length; i++) {
        if (charBOM[i] == -1) {
          break;
        }
        fileOutputStream.write(charBOM[i]);
      }
      fileOutputStream.write(byteStr);
      this.addFileHistoryItem(file.getCanonicalPath()); // 添加最近编辑的文件列表
      this.txaMain.setFileExistsLabel(true);
    } catch (Exception x) {
      x.printStackTrace();
    } finally {
      try {
        fileOutputStream.flush();
        fileOutputStream.close();
      } catch (IOException x) {
        x.printStackTrace();
      }
    }
  }

  /**
   * 在保存文件失败后，弹出提示框
   * 
   * @param file
   *          当前编辑的文件
   */
  private void showSaveErrorDialog(File file) {
    JOptionPane.showMessageDialog(this, Util.convertToMsg("文件："
        + file.getAbsolutePath() + "\n保存失败！请确认是否有写权限！"), Util.SOFTWARE,
        JOptionPane.CANCEL_OPTION);
  }

  /**
   * 保存文件后的设置
   */
  private void setAfterSaveFile() {
    this.itemReOpen.setEnabled(true);
    this.itemReName.setEnabled(true);
    this.itemDelFile.setEnabled(true);
    this.itemPopReName.setEnabled(true);
    this.itemPopDelFile.setEnabled(true);
    this.itemPopReOpen.setEnabled(true);
    this.txaMain.setTextChanged(false);
    this.txaMain.setStyleChanged(false);
    this.txaMain.setSaved(true);
    this.txaMain.setUndoIndex(Util.DEFAULT_UNDO_INDEX); // 撤销标识符恢复默认值
  }

  /**
   * 设置文件的名称和路径
   * 
   * @param file
   *          当前编辑的文件
   */
  private void setFileNameAndPath(File file) {
    this.txaMain.setFile(file);
    this.stbTitle = new StringBuilder(Util.SOFTWARE);
    if (file != null && file.exists()) {
      this.file = this.txaMain.getFile();
      this.stbTitle.insert(0, this.file.getAbsolutePath() + " - ");
    }
    this.setTitle(this.stbTitle.toString());
    this.tpnMain.setTitleAt(this.tpnMain.getSelectedIndex(), this.txaMain
        .getTitle());
  }

  /**
   * 设置标题栏的显示
   */
  private void setAutoTitle() {
    this.stbTitle = new StringBuilder(Util.SOFTWARE);
    this.file = this.txaMain.getFile();
    if (this.file != null) {
      this.stbTitle.insert(0, this.getPrefix() + this.file.getAbsolutePath()
          + " - ");
    } else {
      this.stbTitle.insert(0, this.getPrefix() + this.txaMain.getTitle()
          + " - ");
    }
    this.setTitle(this.stbTitle.toString());
  }

  /**
   * 设置某些菜单的显示状态
   * 
   * @param enable
   *          菜单的显示状态，true表示显示，反之表示不显示
   */
  private void setAutoMenuEnabled(boolean enable) {
    this.itemReOpen.setEnabled(enable);
    this.itemReName.setEnabled(enable);
    this.itemDelFile.setEnabled(enable);
    this.itemPopReName.setEnabled(enable);
    this.itemPopDelFile.setEnabled(enable);
    this.itemPopReOpen.setEnabled(enable);
  }

  /**
   * 重新设置各对话框所在的文本域
   */
  private void setTextAreaInDialogs() {
    if (this.findReplaceDialog != null) {
      this.findReplaceDialog.setTextArea(this.txaMain);
    }
    if (this.gotoDialog != null) {
      this.gotoDialog.setTextArea(this.txaMain);
    }
    if (this.tabSetDialog != null) {
      this.tabSetDialog.setTextArea(this.txaMain);
    }
    if (this.insertCharDialog != null) {
      this.insertCharDialog.setTextArea(this.txaMain);
    }
    if (this.insertDateDialog != null) {
      this.insertDateDialog.setTextArea(this.txaMain);
    }
    if (this.batchRemoveDialog != null) {
      this.batchRemoveDialog.setTextArea(this.txaMain);
    }
    if (this.signIdentifierDialog != null) {
      this.signIdentifierDialog.setTextArea(this.txaMain);
    }
  }

  /**
   * 更新状态栏的文本总字数
   */
  private void updateStateAll() {
    String strStateChars = Util.STATE_CHARS + this.txaMain.getText().length();
    String strStateLines = Util.STATE_LINES + this.txaMain.getLineCount();
    this.pnlState.setStringByIndex(0, strStateChars + ", " + strStateLines);
  }

  /**
   * 更新状态栏当前光标所在的行数、列数与当前选择的字符数
   */
  private void updateStateCur() {
    int curLn = 1;
    int curCol = 1;
    int curSel = 0;
    CurrentLine currentLine = new CurrentLine(this.txaMain);
    int currentIndex = currentLine.getCurrentIndex();
    int startIndex = currentLine.getStartIndex();
    curLn = currentLine.getLineNum() + 1;
    curCol += currentIndex - startIndex;
    String strSel = this.txaMain.getSelectedText();
    if (strSel != null) {
      curSel = strSel.length();
    }
    String strStateCurLn = Util.STATE_CUR_LINE + curLn;
    String strStateCurCol = Util.STATE_CUR_COLUMN + curCol;
    String strStateCurSel = Util.STATE_CUR_SELECT + curSel;
    this.pnlState.setStringByIndex(1, strStateCurLn + ", " + strStateCurCol
        + ", " + strStateCurSel);
  }

  /**
   * 更新状态栏当前的换行符格式
   */
  private void updateStateLineStyle() {
    this.pnlState.setStringByIndex(2, Util.STATE_LINE_STYLE
        + this.txaMain.getLineSeparator().getName());
  }

  /**
   * 更新状态栏当前的字符编码格式
   */
  private void updateStateEncoding() {
    this.pnlState.setStringByIndex(3, Util.STATE_ENCODING
        + this.txaMain.getCharEncoding().getName());
  }

  /**
   * 当文本域中的光标变化时，将触发此事件
   */
  public void caretUpdate(CaretEvent e) {
    this.updateStateCur();
    this.setMenuStateBySelectedText();
  }

  /**
   * 当文本域中的文本发生变化时，将触发此事件
   */
  public void undoableEditHappened(UndoableEditEvent e) {
    this.undoManager.addEdit(e.getEdit());
    this.txaMain.setUndoIndex(this.txaMain.getUndoIndex() + 1); // 撤销标识符递增
    this.setMenuStateUndoRedo(); // 设置撤销和重做菜单的状态
    this.setMenuStateByTextArea();
    this.txaMain.setTextChanged(true);
    this.updateStateAll();
    this.setTextPrefix();
  }

  /**
   * 当主窗口获得焦点时，将触发此事件
   */
  public void windowGainedFocus(WindowEvent e) {
    try {
      Transferable tf = this.clip.getContents(this);
      if (tf == null) {
        this.itemPaste.setEnabled(false);
        this.itemPopPaste.setEnabled(false);
      } else {
        String str = tf.getTransferData(DataFlavor.stringFlavor).toString(); // 如果剪贴板内的内容不是文本，则将抛出异常
        if (str != null && str.length() > 0) {
          this.itemPaste.setEnabled(true);
          this.itemPopPaste.setEnabled(true);
        }
      }
    } catch (Exception x) {
      // 剪贴板异常
      // x.printStackTrace();
      this.itemPaste.setEnabled(false);
      this.itemPopPaste.setEnabled(false);
    }
    if (this.file != null && !this.file.exists()) {
      if (this.txaMain.getFileExistsLabel()) {
        int result = JOptionPane.showConfirmDialog(this, Util
            .convertToMsg("文件：" + this.file + "不存在。\n要重新创建吗？"), Util.SOFTWARE,
            JOptionPane.YES_NO_CANCEL_OPTION);
        if (result == JOptionPane.YES_OPTION) {
          this.checkFile(this.file);
          try {
            this.toSaveFile(this.file);
          } catch (Exception x) {
            this.showSaveErrorDialog(this.file);
            return;
          }
          this.setAfterSaveFile();
          this.setTextPrefix();
          this.setStylePrefix();
        } else {
          this.txaMain.setFileExistsLabel(false);
        }
      }
    }
  }

  /**
   * 当主窗口失去焦点时，将触发此事件
   */
  public void windowLostFocus(WindowEvent e) {

  }

  /**
   * 当监听的组件状态变化时，将触发此事件
   */
  public void stateChanged(ChangeEvent e) {
    if (this.tpnMain.equals(e.getSource())) {
      BaseTextArea baseTextArea = this.getCurrentTextArea();
      if (baseTextArea != null) {
        this.txaMain = baseTextArea;
        this.txaMain.requestFocus();
        this.setAutoTitle();
        this.setLineStyleString(this.txaMain.getLineSeparator(), true);
        this.setCharEncoding(this.txaMain.getCharEncoding(), true);
        this.updateStateAll();
        this.updateStateCur();
        boolean enable = true;
        if (this.txaMain.getFile() == null) {
          enable = false;
        }
        this.setAutoMenuEnabled(enable);
        this.setTextAreaInDialogs();
        this.undoManager = this.txaMain.getUndoManager();
        this.setMenuStateUndoRedo();
        this.setMenuStateByTextArea();
        this.setMenuStateBySelectedText();
      }
    }
  }

}
