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

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
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
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
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
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
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
import java.util.Collections;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * ��ѩ���±� ����һ����Windows�ġ����±���������ͬ��java�汾��
 * ��Ȼ��ֻ�����Ҫ������Ŀ����ʵ��һ������ͬʱ��Windows��Linux�����е���ǿ�ͼ��±���
 * GitHub��https://github.com/xiboliya/snowpad
 * ���ƣ�https://gitee.com/xiboliya/snowpad
 * Coding��https://coding.net/u/xiboliya/p/SnowPad
 * CSDN���룺https://code.csdn.net/chenzhengfeng/snowpad
 * �ȸ���룺http://code.google.com/p/snowpad
 * 
 * @author ��ԭ
 * 
 */
public class SnowPadFrame extends JFrame implements ActionListener,
    CaretListener, UndoableEditListener, WindowFocusListener, ChangeListener, DropTargetListener, ComponentListener {
  private static final long serialVersionUID = 1L; // ���л�����ʱʹ�õ�һ���汾�ţ����뵱ǰ�����л��������
  private BaseTextArea txaMain = null; // ��ǰ�༭���ı���
  private JTabbedPane tpnMain = new JTabbedPane(); // ��ʾ�ı����ѡ����
  private JSplitPane spnMain = new JSplitPane(JSplitPane.VERTICAL_SPLIT); // ���ڷָ�ѡ��������ҽ���������
  private JToolBar tlbMain = new JToolBar(); // ��ʾ���ð�ť�Ĺ��������
  private JMenuBar menuBar = new JMenuBar();
  private JMenu menuFile = new JMenu("�ļ�(F)");
  private JMenuItem itemNew = new JMenuItem("�½�(N)", 'N');
  private JMenuItem itemOpen = new JMenuItem("��(O)...", 'O');
  private JMenuItem itemOpenByEncoding = new JMenuItem("��ָ�������(E)...", 'E');
  private JMenuItem itemReOpen = new JMenuItem("���������ļ�(L)", 'L');
  private JMenuItem itemReName = new JMenuItem("������(R)...", 'R');
  private JMenuItem itemSave = new JMenuItem("����(S)", 'S');
  private JMenuItem itemSaveAs = new JMenuItem("���Ϊ(A)...", 'A');
  private JMenuItem itemClose = new JMenuItem("�رյ�ǰ(C)", 'C');
  private JMenuItem itemCloseOther = new JMenuItem("�ر�����(T)", 'T');
  private JMenuItem itemCloseLeft = new JMenuItem("�ر����(F)", 'F');
  private JMenuItem itemCloseRight = new JMenuItem("�ر��Ҳ�(G)", 'G');
  private JMenuItem itemCloseAll = new JMenuItem("�ر�ȫ��(Q)", 'Q');
  private JCheckBoxMenuItem itemFrozenFile = new JCheckBoxMenuItem("�����ļ�(Z)");
  private JMenuItem itemPrint = new JMenuItem("��ӡ(P)...");
  private JMenuItem itemDelFile = new JMenuItem("ɾ����ǰ�ļ�(D)", 'D');
  private JMenu menuFileHistory = new JMenu("����༭(H)");
  private JMenuItem itemClearFileHistory = new JMenuItem("�������༭�б�(Y)", 'Y');
  private JMenuItem itemExit = new JMenuItem("�˳�(X)", 'X');
  private JMenu menuEdit = new JMenu("�༭(E)");
  private JMenuItem itemUnDo = new JMenuItem("����(U)", 'U');
  private JMenuItem itemReDo = new JMenuItem("����(Y)", 'Y');
  private JMenuItem itemCut = new JMenuItem("����(T)", 'T');
  private JMenuItem itemCopy = new JMenuItem("����(C)", 'C');
  private JMenuItem itemPaste = new JMenuItem("ճ��(P)", 'P');
  private JMenuItem itemDel = new JMenuItem("ɾ��(L)", 'L');
  private JMenuItem itemSlicing = new JMenuItem("����ļ�(S)...", 'S');
  private JMenu menuCase = new JMenu("�л���Сд");
  private JMenuItem itemCaseUp = new JMenuItem("�л�Ϊ��д");
  private JMenuItem itemCaseLow = new JMenuItem("�л�ΪСд");
  private JMenu menuCopyToClip = new JMenu("���Ƶ�������");
  private JMenuItem itemToCopyFileName = new JMenuItem("��ǰ�ļ���(F)", 'F');
  private JMenuItem itemToCopyFilePath = new JMenuItem("��ǰ�ļ�·��(P)", 'P');
  private JMenuItem itemToCopyDirPath = new JMenuItem("��ǰĿ¼·��(I)", 'I');
  private JMenuItem itemToCopyAllText = new JMenuItem("�����ı�");
  private JMenu menuLine = new JMenu("������");
  private JMenuItem itemLineCopy = new JMenuItem("��д��ǰ��");
  private JMenuItem itemLineDel = new JMenuItem("ɾ����ǰ��");
  private JMenuItem itemLineDelDuplicate = new JMenuItem("ɾ���ظ���");
  private JMenuItem itemLineDelToStart = new JMenuItem("ɾ��������");
  private JMenuItem itemLineDelToEnd = new JMenuItem("ɾ������β");
  private JMenuItem itemLineDelToFileStart = new JMenuItem("ɾ�����ļ���");
  private JMenuItem itemLineDelToFileEnd = new JMenuItem("ɾ�����ļ�β");
  private JMenuItem itemLineToUp = new JMenuItem("���Ƶ�ǰ��");
  private JMenuItem itemLineToDown = new JMenuItem("���Ƶ�ǰ��");
  private JMenuItem itemLineToCopy = new JMenuItem("���Ƶ�ǰ��");
  private JMenuItem itemLineToCut = new JMenuItem("���е�ǰ��");
  private JMenu menuLineBatch = new JMenu("��������");
  private JMenuItem itemLineBatchRemove = new JMenuItem("�г�(R)...", 'R');
  private JMenuItem itemLineBatchInsert = new JMenuItem("����(I)...", 'I');
  private JMenuItem itemLineBatchSeparate = new JMenuItem("�ָ���(S)...", 'S');
  private JMenuItem itemLineBatchJoin = new JMenuItem("ƴ����(J)...", 'J');
  private JMenuItem itemLineBatchMerge = new JMenuItem("�ϲ���(M)", 'M');
  private JMenuItem itemLineBatchRewrite = new JMenuItem("���и�д(C)", 'C');
  private JMenu menuSort = new JMenu("����");
  private JMenuItem itemSortUp = new JMenuItem("����");
  private JMenuItem itemSortDown = new JMenuItem("����");
  private JMenuItem itemSortReverse = new JMenuItem("����");
  private JMenu menuIndent = new JMenu("����");
  private JMenuItem itemIndentAdd = new JMenuItem("����");
  private JMenuItem itemIndentBack = new JMenuItem("�˸�");
  private JMenu menuTrim = new JMenu("����հ�");
  private JMenuItem itemTrimStart = new JMenuItem("����");
  private JMenuItem itemTrimEnd = new JMenuItem("��β");
  private JMenuItem itemTrimAll = new JMenuItem("����+��β");
  private JMenuItem itemTrimSelected = new JMenuItem("ѡ����");
  private JMenu menuDelNullLine = new JMenu("ɾ������");
  private JMenuItem itemDelNullLineAll = new JMenuItem("ȫ�ķ�Χ");
  private JMenuItem itemDelNullLineSelected = new JMenuItem("ѡ����Χ");
  private JMenu menuComment = new JMenu("���ע��(M)");
  private JMenuItem itemCommentForLine = new JMenuItem("����ע��(L)", 'L');
  private JMenuItem itemCommentForBlock = new JMenuItem("����ע��(B)", 'B');
  private JMenuItem itemSelAll = new JMenuItem("ȫѡ(A)", 'A');
  private JMenu menuInsert = new JMenu("����(I)");
  private JMenuItem itemInsertDateTime = new JMenuItem("ʱ��/����(D)...", 'D');
  private JMenuItem itemInsertChar = new JMenuItem("�����ַ�(S)...", 'S');
  private JMenu menuSelection = new JMenu("ѡ������");
  private JMenuItem itemSelCopy = new JMenuItem("��дѡ���ַ�(W)", 'W');
  private JMenuItem itemSelInvert = new JMenuItem("��תѡ���ַ�(I)", 'I');
  private JMenu menuSearch = new JMenu("����(S)");
  private JMenuItem itemFind = new JMenuItem("����(F)...", 'F');
  private JMenuItem itemFindNext = new JMenuItem("������һ��(N)", 'N');
  private JMenuItem itemFindPrevious = new JMenuItem("������һ��(P)", 'P');
  private JMenuItem itemSelFindNext = new JMenuItem("ѡ��������һ��(T)", 'T');
  private JMenuItem itemSelFindPrevious = new JMenuItem("ѡ��������һ��(S)", 'S');
  private JMenu menuQuickFind = new JMenu("���ٲ���(Q)");
  private JMenuItem itemQuickFindDown = new JMenuItem("�������²���");
  private JMenuItem itemQuickFindUp = new JMenuItem("�������ϲ���");
  private JMenuItem itemReplace = new JMenuItem("�滻(R)...", 'R');
  private JMenuItem itemGoto = new JMenuItem("ת��(G)...", 'G');
  private JMenu menuBookmark = new JMenu("��ǩ(M)");
  private JMenuItem itemBookmarkSwitch = new JMenuItem("����/ȡ����ǩ(S)", 'S');
  private JMenuItem itemBookmarkNext = new JMenuItem("��һ����ǩ(N)", 'N');
  private JMenuItem itemBookmarkPrevious = new JMenuItem("��һ����ǩ(P)", 'P');
  private JMenuItem itemBookmarkCopy = new JMenuItem("������ǩ��(C)", 'C');
  private JMenuItem itemBookmarkClear = new JMenuItem("���������ǩ(L)", 'L');
  private JMenuItem itemFindBracket = new JMenuItem("��λƥ������(B)", 'B');
  private JMenu menuStyle = new JMenu("��ʽ(O)");
  private JMenu menuLineWrapStyle = new JMenu("���з�ʽ(L)");
  private JRadioButtonMenuItem itemLineWrapByWord = new JRadioButtonMenuItem("���ʱ߽绻��(W)");
  private JRadioButtonMenuItem itemLineWrapByChar = new JRadioButtonMenuItem("�ַ��߽绻��(C)");
  private JMenuItem itemFont = new JMenuItem("����(F)...", 'F');
  private JMenuItem itemTabSet = new JMenuItem("Tab������...", 'T');
  private JMenuItem itemAutoComplete = new JMenuItem("�Զ����(A)...", 'A');
  private JMenuItem itemShortcutManage = new JMenuItem("��ݼ�����(H)...", 'H');
  private JCheckBoxMenuItem itemLineWrap = new JCheckBoxMenuItem("�Զ�����(W)");
  private JCheckBoxMenuItem itemAutoIndent = new JCheckBoxMenuItem("�Զ�����(I)");
  private JMenuItem itemReset = new JMenuItem("�ָ�Ĭ������(R)...", 'R');
  private JMenu menuLineStyle = new JMenu("���з���ʽ(S)");
  private JRadioButtonMenuItem itemLineStyleWin = new JRadioButtonMenuItem(LineSeparator.WINDOWS.getName() + "��ʽ");
  private JRadioButtonMenuItem itemLineStyleUnix = new JRadioButtonMenuItem(LineSeparator.UNIX.getName() + "��ʽ");
  private JRadioButtonMenuItem itemLineStyleMac = new JRadioButtonMenuItem(LineSeparator.MACINTOSH.getName() + "��ʽ");
  private JMenu menuCharset = new JMenu("�ַ������ʽ(C)");
  private JRadioButtonMenuItem itemCharsetBASE = new JRadioButtonMenuItem("Ĭ�ϸ�ʽ(" + CharEncoding.BASE.toString() + ")");
  private JRadioButtonMenuItem itemCharsetANSI = new JRadioButtonMenuItem("ANSI��ʽ");
  private JRadioButtonMenuItem itemCharsetUTF8 = new JRadioButtonMenuItem("UTF-8��ʽ");
  private JRadioButtonMenuItem itemCharsetUTF8_NO_BOM = new JRadioButtonMenuItem("UTF-8 No BOM��ʽ");
  private JRadioButtonMenuItem itemCharsetULE = new JRadioButtonMenuItem("Unicode Little Endian��ʽ");
  private JRadioButtonMenuItem itemCharsetUBE = new JRadioButtonMenuItem("Unicode Big Endian��ʽ");
  private JMenuItem itemSignIdentifier = new JMenuItem("�б��������(G)...", 'G');
  private JMenu menuView = new JMenu("�鿴(V)");
  private JMenuItem itemBack = new JMenuItem("����(O)", 'O');
  private JMenuItem itemForward = new JMenuItem("ǰ��(Q)", 'Q');
  private JCheckBoxMenuItem itemToolBar = new JCheckBoxMenuItem("������(T)");
  private JCheckBoxMenuItem itemStateBar = new JCheckBoxMenuItem("״̬��(S)");
  private JCheckBoxMenuItem itemLineNumber = new JCheckBoxMenuItem("�к���(L)");
  private JCheckBoxMenuItem itemSearchResult = new JCheckBoxMenuItem("���ҽ��(E)");
  private JCheckBoxMenuItem itemAlwaysOnTop = new JCheckBoxMenuItem("ǰ����ʾ(A)");
  private JCheckBoxMenuItem itemLockResizable = new JCheckBoxMenuItem("��������(R)");
  private JMenu menuTab = new JMenu("��ǩ����(B)");
  private JCheckBoxMenuItem itemTabPolicy = new JCheckBoxMenuItem("���б�ǩ(P)");
  private JCheckBoxMenuItem itemClickToClose = new JCheckBoxMenuItem("˫���رձ�ǩ(D)");
  private JCheckBoxMenuItem itemTabIcon = new JCheckBoxMenuItem("ָʾͼ��(I)");
  private JMenu menuFontSize = new JMenu("��������(F)");
  private JMenuItem itemFontSizePlus = new JMenuItem("�Ŵ�(B)", 'B');
  private JMenuItem itemFontSizeMinus = new JMenuItem("��С(S)", 'S');
  private JMenuItem itemFontSizeReset = new JMenuItem("�ָ���ʼ(D)", 'D');
  private JMenu menuColor = new JMenu("��ɫ����(C)");
  private JMenuItem itemColorFont = new JMenuItem("������ɫ(F)...", 'F');
  private JMenuItem itemColorBack = new JMenuItem("������ɫ(B)...", 'B');
  private JMenuItem itemColorCaret = new JMenuItem("�����ɫ(C)...", 'C');
  private JMenuItem itemColorSelFont = new JMenuItem("ѡ��������ɫ(T)...", 'T');
  private JMenuItem itemColorSelBack = new JMenuItem("ѡ��������ɫ(K)...", 'K');
  private JMenuItem itemColorBracketBack = new JMenuItem("ƥ�����ű�����ɫ(E)...", 'E');
  private JMenuItem itemColorLineBack = new JMenuItem("��ǰ�б�����ɫ(L)...", 'L');
  private JMenuItem itemColorAnti = new JMenuItem("ȫ����ɫ(A)", 'A');
  private JMenuItem itemColorComplementary = new JMenuItem("ȫ����ɫ(R)", 'R');
  private JMenu menuColorStyle = new JMenu("��ɫ����(Y)");
  private JMenuItem itemColorStyle1 = new JMenuItem("��ɫ����(1)", '1');
  private JMenuItem itemColorStyle2 = new JMenuItem("��ɫ����(2)", '2');
  private JMenuItem itemColorStyle3 = new JMenuItem("��ɫ����(3)", '3');
  private JMenuItem itemColorStyle4 = new JMenuItem("��ɫ����(4)", '4');
  private JMenuItem itemColorStyle5 = new JMenuItem("��ɫ����(5)", '5');
  private JMenuItem itemColorStyleDefault = new JMenuItem("�ָ�Ĭ����ɫ(0)", '0');
  private JMenu menuHighlight = new JMenu("������ʾ(H)");
  private JMenuItem itemHighlight1 = new JMenuItem("��ʽ(1)", '1');
  private JMenuItem itemHighlight2 = new JMenuItem("��ʽ(2)", '2');
  private JMenuItem itemHighlight3 = new JMenuItem("��ʽ(3)", '3');
  private JMenuItem itemHighlight4 = new JMenuItem("��ʽ(4)", '4');
  private JMenuItem itemHighlight5 = new JMenuItem("��ʽ(5)", '5');
  private JMenu menuRmHighlight = new JMenu("�������(M)");
  private JMenuItem itemRmHighlight1 = new JMenuItem("��ʽ(1)", '1');
  private JMenuItem itemRmHighlight2 = new JMenuItem("��ʽ(2)", '2');
  private JMenuItem itemRmHighlight3 = new JMenuItem("��ʽ(3)", '3');
  private JMenuItem itemRmHighlight4 = new JMenuItem("��ʽ(4)", '4');
  private JMenuItem itemRmHighlight5 = new JMenuItem("��ʽ(5)", '5');
  private JMenuItem itemRmHighlightAll = new JMenuItem("���и�ʽ(0)", '0');
  private JMenu menuLookAndFeel = new JMenu("�л����(K)");
  private JMenuItem itemInformation = new JMenuItem("ͳ����Ϣ(N)...", 'N');
  private JMenuItem itemWindowManage = new JMenuItem("���ڹ���(W)...", 'W');
  private JMenu menuTextAreaSwitch = new JMenu("�ĵ��л�(I)");
  private JMenuItem itemTextAreaSwitchPrevious = new JMenuItem("��ǰ�л�(P)", 'P');
  private JMenuItem itemTextAreaSwitchNext = new JMenuItem("����л�(N)", 'N');
  private JMenuItem itemTextAreaHistoryBack = new JMenuItem("ǰһ���ĵ�(B)", 'B');
  private JMenuItem itemTextAreaHistoryNext = new JMenuItem("��һ���ĵ�(X)", 'X');
  private JMenu menuTool = new JMenu("����(T)");
  private JMenuItem itemEncrypt = new JMenuItem("����(E)...", 'E');
  private JMenuItem itemNumberConvert = new JMenuItem("����ת��(N)...", 'N');
  private JMenuItem itemCalculator = new JMenuItem("������(C)...", 'C');
  private JMenuItem itemCuttingFile = new JMenuItem("�и��ļ�(T)...", 'T');
  private JMenuItem itemMergeFile = new JMenuItem("ƴ���ļ�(M)...", 'M');
  private JMenu menuHelp = new JMenu("����(H)");
  private JMenuItem itemHelp = new JMenuItem("��������(H)", 'H');
  private JMenuItem itemAbout = new JMenuItem("����(A)", 'A');
  private JPopupMenu popMenuMain = new JPopupMenu();
  private JMenuItem itemPopUnDo = new JMenuItem("����(U)", 'U');
  private JMenuItem itemPopReDo = new JMenuItem("����(Y)", 'Y');
  private JMenuItem itemPopCut = new JMenuItem("����(T)", 'T');
  private JMenuItem itemPopCopy = new JMenuItem("����(C)", 'C');
  private JMenuItem itemPopPaste = new JMenuItem("ճ��(P)", 'P');
  private JMenuItem itemPopDel = new JMenuItem("ɾ��(D)", 'D');
  private JMenuItem itemPopSelAll = new JMenuItem("ȫѡ(A)", 'A');
  private JMenu menuPopHighlight = new JMenu("������ʾ(H)");
  private JMenuItem itemPopHighlight1 = new JMenuItem("��ʽ(1)", '1');
  private JMenuItem itemPopHighlight2 = new JMenuItem("��ʽ(2)", '2');
  private JMenuItem itemPopHighlight3 = new JMenuItem("��ʽ(3)", '3');
  private JMenuItem itemPopHighlight4 = new JMenuItem("��ʽ(4)", '4');
  private JMenuItem itemPopHighlight5 = new JMenuItem("��ʽ(5)", '5');
  private JMenu menuPopRmHighlight = new JMenu("�������(M)");
  private JMenuItem itemPopRmHighlight1 = new JMenuItem("��ʽ(1)", '1');
  private JMenuItem itemPopRmHighlight2 = new JMenuItem("��ʽ(2)", '2');
  private JMenuItem itemPopRmHighlight3 = new JMenuItem("��ʽ(3)", '3');
  private JMenuItem itemPopRmHighlight4 = new JMenuItem("��ʽ(4)", '4');
  private JMenuItem itemPopRmHighlight5 = new JMenuItem("��ʽ(5)", '5');
  private JMenuItem itemPopRmHighlightAll = new JMenuItem("���и�ʽ(0)", '0');
  private JMenuItem itemPopCommentForLine = new JMenuItem("����ע��(L)", 'L');
  private JMenuItem itemPopCommentForBlock = new JMenuItem("����ע��(B)", 'B');
  private JPopupMenu popMenuTabbed = new JPopupMenu();
  private JMenuItem itemPopCloseCurrent = new JMenuItem("�رյ�ǰ(C)", 'C');
  private JMenuItem itemPopCloseOthers = new JMenuItem("�ر�����(O)", 'O');
  private JMenuItem itemPopCloseLeft = new JMenuItem("�ر����(F)", 'F');
  private JMenuItem itemPopCloseRight = new JMenuItem("�ر��Ҳ�(G)", 'G');
  private JMenuItem itemPopSave = new JMenuItem("����(S)", 'S');
  private JMenuItem itemPopSaveAs = new JMenuItem("���Ϊ(A)...", 'A');
  private JMenuItem itemPopReName = new JMenuItem("������(N)...", 'N');
  private JMenuItem itemPopDelFile = new JMenuItem("ɾ���ļ�(D)", 'D');
  private JMenuItem itemPopReOpen = new JMenuItem("��������(R)", 'R');
  private JCheckBoxMenuItem itemPopFrozenFile = new JCheckBoxMenuItem("�����ļ�(Z)");
  private JMenu menuPopCopyToClip = new JMenu("���Ƶ�������(P)");
  private JMenuItem itemPopToCopyFileName = new JMenuItem("�����ļ���(F)", 'F');
  private JMenuItem itemPopToCopyFilePath = new JMenuItem("�����ļ�·��(P)", 'P');
  private JMenuItem itemPopToCopyDirPath = new JMenuItem("����Ŀ¼·��(I)", 'I');

  private ButtonGroup bgpLineWrapStyle = new ButtonGroup(); // ���ڴ�Ż��з�ʽ�İ�ť��
  private ButtonGroup bgpLineStyle = new ButtonGroup(); // ���ڴ�Ż��з���ʽ�İ�ť��
  private ButtonGroup bgpCharset = new ButtonGroup(); // ���ڴ���ַ������ʽ�İ�ť��
  private ButtonGroup bgpLookAndFeel = new ButtonGroup(); // ���ڴ����۵İ�ť��
  private Clipboard clip = this.getToolkit().getSystemClipboard(); // ������
  private File file = null; // ��ǰ�༭���ļ�
  private LinkedList<FileHistoryBean> fileHistoryList = new LinkedList<FileHistoryBean>(); // �������༭���ļ���������
  private LinkedList<BaseTextArea> textAreaList = new LinkedList<BaseTextArea>(); // ��Ž����������ı��������
  private LinkedList<AbstractButton> toolButtonList = new LinkedList<AbstractButton>(); // ��Ź����������а�ť������
  private LinkedList<JMenuItem> menuItemList = new LinkedList<JMenuItem>(); // ������п����ڿ�ݼ����õĲ˵��������
  private LinkedList<Integer> textAreaHashCodeList = new LinkedList<Integer>(); // �������༭���ı���hashCode������
  private StringBuilder stbTitle = new StringBuilder(Util.SOFTWARE); // �������ַ���
  private String strLookAndFeel = Util.SYSTEM_LOOK_AND_FEEL_CLASS_NAME; // ��ǰ��۵���������
  private StatePanel pnlState = new StatePanel(4); // ״̬�����
  private SearchResultPanel pnlSearchResult = new SearchResultPanel(this); // ���ҽ�����
  private UndoManager undoManager = null; // ����������
  private TextAreaSetting textAreaSetting = new TextAreaSetting(); // �ı������������
  private Setting setting = null; // �������������
  private SettingAdapter settingAdapter = null; // ���ڽ����ͱ�����������ļ��Ĺ�����
  private boolean clickToClose = true; // �Ƿ�˫���رյ�ǰ��ǩ
  private boolean isTabIconView = true; // �Ƿ���ʾ��ǩ���ļ�״ָ̬ʾͼ��
  private static boolean checking = false; // �Ƿ����ڼ�������ļ���״̬
  private int targetBracketIndex = -1; // ƥ�����ŵ�����ֵ
  private int textAreaHistoryIndex = 0; // ����༭���ı����������е�����ֵ

  private OpenFileChooser openFileChooser = null; // "��"�ļ�ѡ����
  private SaveFileChooser saveFileChooser = null; // "����"�ļ�ѡ����
  private FontChooser fontChooser = null; // ����Ի���
  private FindReplaceDialog findReplaceDialog = null; // ���ҡ��滻�Ի���
  private GotoDialog gotoDialog = null; // ת���Ի���
  private AboutDialog aboutDialog = null; // ���ڶԻ���
  private TabSetDialog tabSetDialog = null; // Tab�ַ����öԻ���
  private AutoCompleteDialog autoCompleteDialog = null; // �Զ���ɶԻ���
  private ShortcutManageDialog shortcutManageDialog = null; // ��ݼ�����Ի���
  private InsertCharDialog insertCharDialog = null; // �����ַ��Ի���
  private InsertDateDialog insertDateDialog = null; // ����ʱ��/���ڶԻ���
  private FileEncodingDialog fileEncodingDialog = null; // �ļ������ʽ�Ի���
  private SlicingFileDialog slicingFileDialog = null; // ����ļ��Ի���
  private BatchRemoveDialog batchRemoveDialog = null; // ������"�г�"�Ի���
  private BatchInsertDialog batchInsertDialog = null; // ������"����"�Ի���
  private BatchSeparateDialog batchSeparateDialog = null; // ������"�ָ���"�Ի���
  private BatchJoinDialog batchJoinDialog = null; // ������"ƴ����"�Ի���
  private SignIdentifierDialog signIdentifierDialog = null; // ��Ŀ�������ŶԻ���
  private InformationDialog informationDialog = null; // ͳ����Ϣ�Ի���
  private WindowManageDialog windowManageDialog = null; // ���ڹ���Ի���
  private EncryptDialog encryptDialog = null; // ���ܶԻ���
  private NumberConvertDialog numberConvertDialog = null; // ����ת���Ի���
  private CalculatorDialog calculatorDialog = null; // �������Ի���
  private CuttingFileDialog cuttingFileDialog = null; // �и��ļ��Ի���
  private MergeFileDialog mergeFileDialog = null; // ƴ���ļ��Ի���
  private HelpFrame helpFrame = null; // �������ⴰ��

  /**
   * �������Ĺ��췽����ͨ�������ļ���������
   * 
   * @param setting
   *          �������������
   * @param settingAdapter
   *          ���ڽ����ͱ�����������ļ��Ĺ�����
   */
  public SnowPadFrame(Setting setting, SettingAdapter settingAdapter) {
    this.setting = setting;
    this.settingAdapter = settingAdapter;
    this.initTextAreaSetting();
    this.setTitle(this.stbTitle.toString());
    this.setSize();
    this.setMinimumSize(new Dimension(300, 300)); // �������������С�ߴ�
    this.setLocationRelativeTo(null); // ʹ���ھ�����ʾ
    this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // ����Ĭ�Ϲرղ���Ϊ�գ��Ա���Ӵ��ڼ����¼�
    this.init();
    this.setIcon();
    this.setVisible(true);
    if (this.setting.fileHistoryList.isEmpty()) {
      return;
    }
    boolean toCreateNew = false; // ���ڱ�ʶ�´򿪵��ļ�����Ҫ�ڵ�ǰ�ı����д򿪣�����Ҫ�½��ı���
    for (FileHistoryBean bean : this.setting.fileHistoryList) {
      if (!Util.isTextEmpty(bean.getFileName())) {
        File file = new File(bean.getFileName());
        if (file.exists()) {
          if (!toCreateNew) {
            this.toOpenFile(file, true, false);
            toCreateNew = true; // ��֤��һ�����򿪵��ļ��ڵ�ǰ�ı�������ʾ�������ļ�����Ҫ�½��ı���
          } else {
            this.toOpenFile(file, true, true);
          }
          this.txaMain.setFrozen(bean.getFrozen());
          this.itemFrozenFile.setSelected(bean.getFrozen());
          this.itemPopFrozenFile.setSelected(bean.getFrozen());
          this.setAfterOpenFile(Util.DEFAULT_CARET_INDEX);
          this.setFileNameAndPath(file);
        }
      }
    }
    this.findReplaceDialog = new FindReplaceDialog(this, false, this.txaMain, this.pnlSearchResult,
        this.setting, false);
  }

  /**
   * ���ô��ڵĴ�С
   */
  private void setSize() {
    int width = Util.DEFAULT_FRAME_WIDTH;
    int height = Util.DEFAULT_FRAME_HEIGHT;
    if (this.setting.viewFrameSize != null) {
      width = this.setting.viewFrameSize[0];
      height = this.setting.viewFrameSize[1];
    }
    if (width > 0 && height > 0) {
      this.setSize(width, height);
    } else if (width <= 0 && height <= 0) {
      this.setSize(Util.DEFAULT_FRAME_WIDTH, Util.DEFAULT_FRAME_HEIGHT);
      this.setExtendedState(MAXIMIZED_BOTH);
    } else if (width <= 0) {
      this.setSize(Util.DEFAULT_FRAME_WIDTH, height);
      this.setExtendedState(MAXIMIZED_HORIZ);
    } else {
      this.setSize(width, Util.DEFAULT_FRAME_HEIGHT);
      this.setExtendedState(MAXIMIZED_VERT);
    }
  }

  /**
   * �����Զ���Ĵ���ͼ��
   */
  private void setIcon() {
    try {
      this.setIconImage(Util.SW_ICON.getImage());
    } catch (Exception x) {
      // x.printStackTrace();
    }
  }

  /**
   * ��ʼ���ı������������
   */
  private void initTextAreaSetting() {
    if (this.setting != null) {
      this.textAreaSetting.isLineWrap = this.setting.isLineWrap;
      this.textAreaSetting.isWrapStyleWord = this.setting.isWrapStyleWord;
      this.textAreaSetting.font = this.setting.font;
      this.textAreaSetting.autoIndent = this.setting.autoIndent;
      this.textAreaSetting.tabReplaceBySpace = this.setting.tabReplaceBySpace;
      this.textAreaSetting.autoComplete = this.setting.autoComplete;
      this.textAreaSetting.colorStyle = this.setting.colorStyle;
      this.textAreaSetting.tabSize = this.setting.tabSize;
      this.textAreaSetting.isLineNumberView = this.setting.viewLineNumber;
    }
  }

  /**
   * ��ʼ���������Ӽ�����
   */
  private void init() {
    this.addToolBar();
    this.addTabbedPane();
    this.addMenuItem();
    this.addStatePanel();
    this.addPopMenu();
    this.setMenuDefault();
    this.setMenuDefaultInit();
    this.setMenuMnemonic();
    this.setMenuAccelerator();
    this.addListeners();
  }

  /**
   * ��Ӹ�������¼�������
   */
  private void addListeners() {
    this.itemAbout.addActionListener(this);
    this.itemCopy.addActionListener(this);
    this.itemCut.addActionListener(this);
    this.itemInsertChar.addActionListener(this);
    this.itemInsertDateTime.addActionListener(this);
    this.itemDel.addActionListener(this);
    this.itemSlicing.addActionListener(this);
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
    this.itemAutoComplete.addActionListener(this);
    this.itemShortcutManage.addActionListener(this);
    this.itemGoto.addActionListener(this);
    this.itemBookmarkSwitch.addActionListener(this);
    this.itemBookmarkNext.addActionListener(this);
    this.itemBookmarkPrevious.addActionListener(this);
    this.itemBookmarkCopy.addActionListener(this);
    this.itemBookmarkClear.addActionListener(this);
    this.itemFindBracket.addActionListener(this);
    this.itemToCopyFileName.addActionListener(this);
    this.itemToCopyFilePath.addActionListener(this);
    this.itemToCopyDirPath.addActionListener(this);
    this.itemToCopyAllText.addActionListener(this);
    this.itemLineCopy.addActionListener(this);
    this.itemLineDel.addActionListener(this);
    this.itemLineDelDuplicate.addActionListener(this);
    this.itemLineDelToStart.addActionListener(this);
    this.itemLineDelToEnd.addActionListener(this);
    this.itemLineDelToFileStart.addActionListener(this);
    this.itemLineDelToFileEnd.addActionListener(this);
    this.itemLineToUp.addActionListener(this);
    this.itemLineToDown.addActionListener(this);
    this.itemLineToCopy.addActionListener(this);
    this.itemLineToCut.addActionListener(this);
    this.itemSortUp.addActionListener(this);
    this.itemSortDown.addActionListener(this);
    this.itemSortReverse.addActionListener(this);
    this.itemIndentAdd.addActionListener(this);
    this.itemIndentBack.addActionListener(this);
    this.itemSelCopy.addActionListener(this);
    this.itemSelInvert.addActionListener(this);
    this.itemLineBatchRemove.addActionListener(this);
    this.itemLineBatchInsert.addActionListener(this);
    this.itemLineBatchSeparate.addActionListener(this);
    this.itemLineBatchJoin.addActionListener(this);
    this.itemLineBatchMerge.addActionListener(this);
    this.itemLineBatchRewrite.addActionListener(this);
    this.itemTrimStart.addActionListener(this);
    this.itemTrimEnd.addActionListener(this);
    this.itemTrimAll.addActionListener(this);
    this.itemTrimSelected.addActionListener(this);
    this.itemDelNullLineAll.addActionListener(this);
    this.itemDelNullLineSelected.addActionListener(this);
    this.itemCommentForLine.addActionListener(this);
    this.itemCommentForBlock.addActionListener(this);
    this.itemEncrypt.addActionListener(this);
    this.itemNumberConvert.addActionListener(this);
    this.itemCalculator.addActionListener(this);
    this.itemCuttingFile.addActionListener(this);
    this.itemMergeFile.addActionListener(this);
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
    this.itemAutoIndent.addActionListener(this);
    this.itemReset.addActionListener(this);
    this.itemAlwaysOnTop.addActionListener(this);
    this.itemLockResizable.addActionListener(this);
    this.itemBack.addActionListener(this);
    this.itemForward.addActionListener(this);
    this.itemTabPolicy.addActionListener(this);
    this.itemClickToClose.addActionListener(this);
    this.itemTabIcon.addActionListener(this);
    this.itemFontSizePlus.addActionListener(this);
    this.itemFontSizeMinus.addActionListener(this);
    this.itemFontSizeReset.addActionListener(this);
    this.itemColorFont.addActionListener(this);
    this.itemColorBack.addActionListener(this);
    this.itemColorCaret.addActionListener(this);
    this.itemColorSelFont.addActionListener(this);
    this.itemColorSelBack.addActionListener(this);
    this.itemColorBracketBack.addActionListener(this);
    this.itemColorLineBack.addActionListener(this);
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
    this.itemInformation.addActionListener(this);
    this.itemWindowManage.addActionListener(this);
    this.itemTextAreaSwitchPrevious.addActionListener(this);
    this.itemTextAreaSwitchNext.addActionListener(this);
    this.itemTextAreaHistoryBack.addActionListener(this);
    this.itemTextAreaHistoryNext.addActionListener(this);
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
    this.itemPopCommentForLine.addActionListener(this);
    this.itemPopCommentForBlock.addActionListener(this);
    this.itemPopCloseCurrent.addActionListener(this);
    this.itemPopCloseOthers.addActionListener(this);
    this.itemPopCloseLeft.addActionListener(this);
    this.itemPopCloseRight.addActionListener(this);
    this.itemPopSave.addActionListener(this);
    this.itemPopSaveAs.addActionListener(this);
    this.itemPopReName.addActionListener(this);
    this.itemPopDelFile.addActionListener(this);
    this.itemPopReOpen.addActionListener(this);
    this.itemPopFrozenFile.addActionListener(this);
    this.itemPopToCopyFileName.addActionListener(this);
    this.itemPopToCopyFilePath.addActionListener(this);
    this.itemPopToCopyDirPath.addActionListener(this);
    this.itemReplace.addActionListener(this);
    this.itemSave.addActionListener(this);
    this.itemSaveAs.addActionListener(this);
    this.itemClose.addActionListener(this);
    this.itemCloseOther.addActionListener(this);
    this.itemCloseLeft.addActionListener(this);
    this.itemCloseRight.addActionListener(this);
    this.itemCloseAll.addActionListener(this);
    this.itemFrozenFile.addActionListener(this);
    this.itemPrint.addActionListener(this);
    this.itemDelFile.addActionListener(this);
    this.itemClearFileHistory.addActionListener(this);
    this.itemSelAll.addActionListener(this);
    this.itemToolBar.addActionListener(this);
    this.itemStateBar.addActionListener(this);
    this.itemLineNumber.addActionListener(this);
    this.itemSearchResult.addActionListener(this);
    this.itemReDo.addActionListener(this);
    this.itemUnDo.addActionListener(this);
    // Ϊ��������¼�������
    this.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        exit();
      }
    });
    // Ϊ������ӽ��������
    this.addWindowFocusListener(this);
    // Ϊ����������������
    this.addComponentListener(this);
    this.addTabbedPaneMouseListener();
  }

  /**
   * Ϊ�ı����������¼�������
   */
  private void addTextAreaMouseListener() {
    this.txaMain.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) { // ����Ҽ�ʱ����ʾ��ݲ˵�
          popMenuMain.show(txaMain, e.getX(), e.getY());
        }
      }
    });
  }

  /**
   * Ϊѡ�����������¼�������
   */
  private void addTabbedPaneMouseListener() {
    tpnMain.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        Rectangle rect = tpnMain.getBoundsAt(tpnMain.getSelectedIndex());
        int x = e.getX();
        int y = e.getY();
        if (rect.contains(x, y)) { // ���������λ�ڵ�ǰѡ���Χ��ʱ��ִ����Ӧ�Ĳ���
          if (e.getButton() == MouseEvent.BUTTON3) { // ����Ҽ�ʱ����ʾ��ݲ˵�
            popMenuTabbed.show(tpnMain, x, y);
          } else if (e.getClickCount() == 2) { // ˫��ʱ���رյ�ǰ��ǩ
            if (clickToClose) {
              closeFile(true);
            }
          }
        } else if (e.getClickCount() == 2) { // ˫���հ�����ʱ���½���ǩ
          rect = tpnMain.getBoundsAt(tpnMain.getTabCount() - 1); // ��ȡѡ����һ����ǩ����ʾ����
          double lastX = rect.getX() + rect.getWidth();
          double lastY = rect.getY() + rect.getHeight();
          if (x > lastX && y > rect.getY() && y < lastY) {
            createNew(null);
          }
        }
      }
    });
  }

  /**
   * ��ȡ��ǰ�༭���ı���
   * 
   * @return ��ǰ�༭���ı�������������򷵻�null
   */
  private BaseTextArea getCurrentTextArea() {
    try {
      JViewport viewport = ((JScrollPane) this.tpnMain.getSelectedComponent()).getViewport();
      return (BaseTextArea) viewport.getView();
    } catch (Exception x) {
      // x.printStackTrace();
      return null;
    }
  }

  /**
   * ����������ѡ���ͼ
   */
  private void addTabbedPane() {
    this.getContentPane().add(this.spnMain, BorderLayout.CENTER);
    this.spnMain.setTopComponent(this.tpnMain);
    this.spnMain.setBottomComponent(this.pnlSearchResult);
    this.spnMain.setResizeWeight(0.6);
    this.tpnMain.setFocusable(false);
    this.tpnMain.setFont(Util.GLOBAL_FONT);
    this.tpnMain.addChangeListener(this);
    this.createNew(null);
    new DropTarget(this.tpnMain, this); // �����Ϸ�Ŀ�꣬������ĳ���������drop����
  }

  /**
   * ���������ӹ�������ͼ
   */
  private void addToolBar() {
    this.getContentPane().add(this.tlbMain, BorderLayout.NORTH);
    for (int i = 0; i < Util.TOOL_ENABLE_ICONS.length; i++) {
      AbstractButton btnTool = null;
      if (i == Util.TOOL_ENABLE_ICONS.length - 1) {
        btnTool = new JToggleButton(Util.TOOL_DISABLE_ICONS[i]);
        btnTool.setSelectedIcon(Util.TOOL_ENABLE_ICONS[i]);
      } else {
        btnTool = new JButton(Util.TOOL_ENABLE_ICONS[i]);
        btnTool.setDisabledIcon(Util.TOOL_DISABLE_ICONS[i]);
      }
      btnTool.setToolTipText(Util.TOOL_TOOLTIP_TEXTS[i]);
      btnTool.setFocusable(false);
      btnTool.addActionListener(this);
      this.tlbMain.add(btnTool);
      this.toolButtonList.add(btnTool);
      switch (i) {
      case 5:
      case 8:
      case 10:
      case 12:
      case 14:
      case 16:
        this.tlbMain.addSeparator();
        break;
      }
    }
  }

  /**
   * ����������״̬��
   */
  private void addStatePanel() {
    String strStateChars = Util.STATE_CHARS + "0";
    String strStateLines = Util.STATE_LINES + "1";
    String strStateCurLn = Util.STATE_CUR_LINE + "1";
    String strStateCurCol = Util.STATE_CUR_COLUMN + "1";
    String strStateCurSel = Util.STATE_CUR_SELECT + "0";
    String strStateLineStyle = Util.STATE_LINE_STYLE;
    String strStateEncoding = Util.STATE_ENCODING;
    this.pnlState.setStringByIndex(0, strStateChars + ", " + strStateLines,StatePanelAlignment.X_CENTER);
    this.pnlState.setStringByIndex(1, strStateCurLn + ", " + strStateCurCol + ", " + strStateCurSel);
    this.pnlState.setStringByIndex(2, strStateLineStyle);
    this.pnlState.setStringByIndex(3, strStateEncoding);
    this.getContentPane().add(this.pnlState, BorderLayout.SOUTH);
  }

  /**
   * ���������Ӳ˵���
   */
  private void addMenuItem() {
    this.setJMenuBar(this.menuBar);
    this.menuBar.add(this.menuFile);
    this.menuFile.add(this.itemNew);
    this.menuFile.add(this.itemOpen);
    this.menuFile.add(this.itemOpenByEncoding);
    this.menuFile.add(this.itemSave);
    this.menuFile.add(this.itemSaveAs);
    this.menuFile.add(this.itemReName);
    this.menuFile.addSeparator();
    this.menuFile.add(this.itemReOpen);
    this.menuFile.add(this.itemDelFile);
    this.menuFile.addSeparator();
    this.menuFile.add(this.itemClose);
    this.menuFile.add(this.itemCloseOther);
    this.menuFile.add(this.itemCloseLeft);
    this.menuFile.add(this.itemCloseRight);
    this.menuFile.add(this.itemCloseAll);
    this.menuFile.addSeparator();
    this.menuFile.add(this.itemFrozenFile);
    this.menuFile.addSeparator();
    this.menuFile.add(this.itemPrint);
    this.menuFile.addSeparator();
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
    this.menuEdit.add(this.itemSlicing);
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
    this.menuLine.add(this.itemLineDelDuplicate);
    this.menuLine.addSeparator();
    this.menuLine.add(this.itemLineDelToStart);
    this.menuLine.add(this.itemLineDelToEnd);
    this.menuLine.addSeparator();
    this.menuLine.add(this.itemLineDelToFileStart);
    this.menuLine.add(this.itemLineDelToFileEnd);
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
    this.menuLineBatch.add(this.itemLineBatchJoin);
    this.menuLineBatch.add(this.itemLineBatchMerge);
    this.menuLineBatch.add(this.itemLineBatchRewrite);
    this.menuEdit.add(this.menuSort);
    this.menuSort.add(this.itemSortUp);
    this.menuSort.add(this.itemSortDown);
    this.menuSort.add(this.itemSortReverse);
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
    this.menuEdit.add(this.menuComment);
    this.menuComment.add(this.itemCommentForLine);
    this.menuComment.add(this.itemCommentForBlock);
    this.menuEdit.add(this.menuInsert);
    this.menuInsert.add(this.itemInsertChar);
    this.menuInsert.add(this.itemInsertDateTime);
    this.menuEdit.add(this.menuSelection);
    this.menuSelection.add(this.itemSelCopy);
    this.menuSelection.add(this.itemSelInvert);
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
    this.menuSearch.add(this.menuBookmark);
    this.menuBookmark.add(this.itemBookmarkSwitch);
    this.menuBookmark.add(this.itemBookmarkNext);
    this.menuBookmark.add(this.itemBookmarkPrevious);
    this.menuBookmark.addSeparator();
    this.menuBookmark.add(this.itemBookmarkCopy);
    this.menuBookmark.add(this.itemBookmarkClear);
    this.menuSearch.add(this.itemFindBracket);
    this.menuBar.add(this.menuStyle);
    this.menuStyle.add(this.menuLineWrapStyle);
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
    this.menuStyle.add(this.itemAutoComplete);
    this.menuStyle.add(this.itemShortcutManage);
    this.menuStyle.addSeparator();
    this.menuStyle.add(this.itemLineWrap);
    this.menuStyle.add(this.itemAutoIndent);
    this.menuStyle.addSeparator();
    this.menuStyle.add(this.itemReset);
    this.menuBar.add(this.menuView);
    this.menuView.add(this.itemBack);
    this.menuView.add(this.itemForward);
    this.menuView.addSeparator();
    this.menuView.add(this.itemToolBar);
    this.menuView.add(this.itemStateBar);
    this.menuView.add(this.itemLineNumber);
    this.menuView.add(this.itemSearchResult);
    this.menuView.add(this.itemAlwaysOnTop);
    this.menuView.add(this.itemLockResizable);
    this.menuView.addSeparator();
    this.menuView.add(this.menuTab);
    this.menuTab.add(this.itemTabPolicy);
    this.menuTab.add(this.itemClickToClose);
    this.menuTab.add(this.itemTabIcon);
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
    this.menuColor.add(this.itemColorBracketBack);
    this.menuColor.add(this.itemColorLineBack);
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
    this.menuView.add(this.menuTextAreaSwitch);
    this.menuTextAreaSwitch.add(this.itemTextAreaSwitchPrevious);
    this.menuTextAreaSwitch.add(this.itemTextAreaSwitchNext);
    this.menuTextAreaSwitch.add(this.itemTextAreaHistoryBack);
    this.menuTextAreaSwitch.add(this.itemTextAreaHistoryNext);
    this.menuView.addSeparator();
    this.menuView.add(this.itemInformation);
    this.menuView.add(this.itemWindowManage);
    this.menuBar.add(this.menuTool);
    this.menuTool.add(this.itemEncrypt);
    this.menuTool.add(this.itemNumberConvert);
    this.menuTool.add(this.itemCalculator);
    this.menuTool.add(this.itemCuttingFile);
    this.menuTool.add(this.itemMergeFile);
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
    this.initMenuItemList();
  }

  /**
   * ��ʼ�������ڿ�ݼ����õĲ˵��������
   */
  private void initMenuItemList() {
    this.menuItemList.clear();
    this.menuItemList.add(this.itemNew);
    this.menuItemList.add(this.itemOpen);
    this.menuItemList.add(this.itemOpenByEncoding);
    this.menuItemList.add(this.itemSave);
    this.menuItemList.add(this.itemSaveAs);
    this.menuItemList.add(this.itemReName);
    this.menuItemList.add(this.itemReOpen);
    this.menuItemList.add(this.itemDelFile);
    this.menuItemList.add(this.itemClose);
    this.menuItemList.add(this.itemCloseOther);
    this.menuItemList.add(this.itemCloseLeft);
    this.menuItemList.add(this.itemCloseRight);
    this.menuItemList.add(this.itemCloseAll);
    this.menuItemList.add(this.itemFrozenFile);
    this.menuItemList.add(this.itemPrint);
    this.menuItemList.add(this.itemClearFileHistory);
    this.menuItemList.add(this.itemExit);
    this.menuItemList.add(this.itemUnDo);
    this.menuItemList.add(this.itemReDo);
    this.menuItemList.add(this.itemCut);
    this.menuItemList.add(this.itemCopy);
    this.menuItemList.add(this.itemPaste);
    this.menuItemList.add(this.itemSelAll);
    this.menuItemList.add(this.itemDel);
    this.menuItemList.add(this.itemSlicing);
    this.menuItemList.add(this.itemCaseUp);
    this.menuItemList.add(this.itemCaseLow);
    this.menuItemList.add(this.itemToCopyFileName);
    this.menuItemList.add(this.itemToCopyFilePath);
    this.menuItemList.add(this.itemToCopyDirPath);
    this.menuItemList.add(this.itemToCopyAllText);
    this.menuItemList.add(this.itemLineCopy);
    this.menuItemList.add(this.itemLineDel);
    this.menuItemList.add(this.itemLineDelDuplicate);
    this.menuItemList.add(this.itemLineDelToStart);
    this.menuItemList.add(this.itemLineDelToEnd);
    this.menuItemList.add(this.itemLineDelToFileStart);
    this.menuItemList.add(this.itemLineDelToFileEnd);
    this.menuItemList.add(this.itemLineToUp);
    this.menuItemList.add(this.itemLineToDown);
    this.menuItemList.add(this.itemLineToCopy);
    this.menuItemList.add(this.itemLineToCut);
    this.menuItemList.add(this.itemLineBatchRemove);
    this.menuItemList.add(this.itemLineBatchInsert);
    this.menuItemList.add(this.itemLineBatchSeparate);
    this.menuItemList.add(this.itemLineBatchJoin);
    this.menuItemList.add(this.itemLineBatchMerge);
    this.menuItemList.add(this.itemLineBatchRewrite);
    this.menuItemList.add(this.itemSortUp);
    this.menuItemList.add(this.itemSortDown);
    this.menuItemList.add(this.itemSortReverse);
    this.menuItemList.add(this.itemIndentAdd);
    this.menuItemList.add(this.itemIndentBack);
    this.menuItemList.add(this.itemTrimStart);
    this.menuItemList.add(this.itemTrimEnd);
    this.menuItemList.add(this.itemTrimAll);
    this.menuItemList.add(this.itemTrimSelected);
    this.menuItemList.add(this.itemDelNullLineAll);
    this.menuItemList.add(this.itemDelNullLineSelected);
    this.menuItemList.add(this.itemCommentForLine);
    this.menuItemList.add(this.itemCommentForBlock);
    this.menuItemList.add(this.itemInsertChar);
    this.menuItemList.add(this.itemInsertDateTime);
    this.menuItemList.add(this.itemSelCopy);
    this.menuItemList.add(this.itemSelInvert);
    this.menuItemList.add(this.itemFind);
    this.menuItemList.add(this.itemFindNext);
    this.menuItemList.add(this.itemFindPrevious);
    this.menuItemList.add(this.itemSelFindNext);
    this.menuItemList.add(this.itemSelFindPrevious);
    this.menuItemList.add(this.itemQuickFindDown);
    this.menuItemList.add(this.itemQuickFindUp);
    this.menuItemList.add(this.itemReplace);
    this.menuItemList.add(this.itemGoto);
    this.menuItemList.add(this.itemBookmarkSwitch);
    this.menuItemList.add(this.itemBookmarkNext);
    this.menuItemList.add(this.itemBookmarkPrevious);
    this.menuItemList.add(this.itemBookmarkCopy);
    this.menuItemList.add(this.itemBookmarkClear);
    this.menuItemList.add(this.itemFindBracket);
    this.menuItemList.add(this.itemLineWrapByWord);
    this.menuItemList.add(this.itemLineWrapByChar);
    this.menuItemList.add(this.itemLineStyleWin);
    this.menuItemList.add(this.itemLineStyleUnix);
    this.menuItemList.add(this.itemLineStyleMac);
    this.menuItemList.add(this.itemCharsetBASE);
    this.menuItemList.add(this.itemCharsetANSI);
    this.menuItemList.add(this.itemCharsetUTF8);
    this.menuItemList.add(this.itemCharsetUTF8_NO_BOM);
    this.menuItemList.add(this.itemCharsetULE);
    this.menuItemList.add(this.itemCharsetUBE);
    this.menuItemList.add(this.itemSignIdentifier);
    this.menuItemList.add(this.itemFont);
    this.menuItemList.add(this.itemTabSet);
    this.menuItemList.add(this.itemAutoComplete);
    this.menuItemList.add(this.itemLineWrap);
    this.menuItemList.add(this.itemAutoIndent);
    this.menuItemList.add(this.itemReset);
    this.menuItemList.add(this.itemBack);
    this.menuItemList.add(this.itemForward);
    this.menuItemList.add(this.itemToolBar);
    this.menuItemList.add(this.itemStateBar);
    this.menuItemList.add(this.itemLineNumber);
    this.menuItemList.add(this.itemSearchResult);
    this.menuItemList.add(this.itemAlwaysOnTop);
    this.menuItemList.add(this.itemLockResizable);
    this.menuItemList.add(this.itemTabPolicy);
    this.menuItemList.add(this.itemClickToClose);
    this.menuItemList.add(this.itemTabIcon);
    this.menuItemList.add(this.itemFontSizePlus);
    this.menuItemList.add(this.itemFontSizeMinus);
    this.menuItemList.add(this.itemFontSizeReset);
    this.menuItemList.add(this.itemColorFont);
    this.menuItemList.add(this.itemColorBack);
    this.menuItemList.add(this.itemColorCaret);
    this.menuItemList.add(this.itemColorSelFont);
    this.menuItemList.add(this.itemColorSelBack);
    this.menuItemList.add(this.itemColorBracketBack);
    this.menuItemList.add(this.itemColorLineBack);
    this.menuItemList.add(this.itemColorAnti);
    this.menuItemList.add(this.itemColorComplementary);
    this.menuItemList.add(this.itemColorStyle1);
    this.menuItemList.add(this.itemColorStyle2);
    this.menuItemList.add(this.itemColorStyle3);
    this.menuItemList.add(this.itemColorStyle4);
    this.menuItemList.add(this.itemColorStyle5);
    this.menuItemList.add(this.itemColorStyleDefault);
    this.menuItemList.add(this.itemHighlight1);
    this.menuItemList.add(this.itemHighlight2);
    this.menuItemList.add(this.itemHighlight3);
    this.menuItemList.add(this.itemHighlight4);
    this.menuItemList.add(this.itemHighlight5);
    this.menuItemList.add(this.itemRmHighlight1);
    this.menuItemList.add(this.itemRmHighlight2);
    this.menuItemList.add(this.itemRmHighlight3);
    this.menuItemList.add(this.itemRmHighlight4);
    this.menuItemList.add(this.itemRmHighlight5);
    this.menuItemList.add(this.itemRmHighlightAll);
    this.menuItemList.add(this.itemTextAreaSwitchPrevious);
    this.menuItemList.add(this.itemTextAreaSwitchNext);
    this.menuItemList.add(this.itemTextAreaHistoryBack);
    this.menuItemList.add(this.itemTextAreaHistoryNext);
    this.menuItemList.add(this.itemInformation);
    this.menuItemList.add(this.itemWindowManage);
    this.menuItemList.add(this.itemEncrypt);
    this.menuItemList.add(this.itemNumberConvert);
    this.menuItemList.add(this.itemCalculator);
    this.menuItemList.add(this.itemCuttingFile);
    this.menuItemList.add(this.itemMergeFile);
    this.menuItemList.add(this.itemHelp);
    this.menuItemList.add(this.itemAbout);
  }

  /**
   * ��ʼ����ݲ˵�
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
    this.popMenuMain.addSeparator();
    this.popMenuMain.add(this.itemPopCommentForLine);
    this.popMenuMain.add(this.itemPopCommentForBlock);
    Dimension popSize = this.popMenuMain.getPreferredSize();
    popSize.width += popSize.width / 5; // Ϊ�����ۣ��ʵ��ӿ�˵�����ʾ
    this.popMenuMain.setPopupSize(popSize);

    this.popMenuTabbed.add(this.itemPopCloseCurrent);
    this.popMenuTabbed.add(this.itemPopCloseOthers);
    this.popMenuTabbed.add(this.itemPopCloseLeft);
    this.popMenuTabbed.add(this.itemPopCloseRight);
    this.popMenuTabbed.addSeparator();
    this.popMenuTabbed.add(this.itemPopSave);
    this.popMenuTabbed.add(this.itemPopSaveAs);
    this.popMenuTabbed.add(this.itemPopReName);
    this.popMenuTabbed.add(this.itemPopDelFile);
    this.popMenuTabbed.add(this.itemPopReOpen);
    this.popMenuTabbed.addSeparator();
    this.popMenuTabbed.add(this.itemPopFrozenFile);
    this.popMenuTabbed.addSeparator();
    this.popMenuTabbed.add(this.menuPopCopyToClip);
    this.menuPopCopyToClip.add(this.itemPopToCopyFileName);
    this.menuPopCopyToClip.add(this.itemPopToCopyFilePath);
    this.menuPopCopyToClip.add(this.itemPopToCopyDirPath);
    popSize = this.popMenuTabbed.getPreferredSize();
    popSize.width += popSize.width / 5; // Ϊ�����ۣ��ʵ��ӿ�˵�����ʾ
    this.popMenuTabbed.setPopupSize(popSize);
  }

  /**
   * ���ø��˵���ĳ�ʼ״̬�����Ƿ����
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
    this.itemCaseUp.setEnabled(false);
    this.itemCaseLow.setEnabled(false);
    this.itemFind.setEnabled(false);
    this.itemFindNext.setEnabled(false);
    this.itemFindPrevious.setEnabled(false);
    this.itemSelFindNext.setEnabled(false);
    this.itemSelFindPrevious.setEnabled(false);
    this.itemQuickFindDown.setEnabled(false);
    this.itemQuickFindUp.setEnabled(false);
    this.itemSelCopy.setEnabled(false);
    this.itemSelInvert.setEnabled(false);
    this.itemLineBatchRemove.setEnabled(false);
    this.itemLineBatchInsert.setEnabled(false);
    this.itemLineBatchSeparate.setEnabled(false);
    this.itemLineBatchJoin.setEnabled(false);
    this.itemLineBatchMerge.setEnabled(false);
    this.itemLineBatchRewrite.setEnabled(false);
    this.itemReplace.setEnabled(false);
    this.itemGoto.setEnabled(false);
    this.itemFindBracket.setEnabled(false);
    this.itemTrimSelected.setEnabled(false);
    this.itemDelNullLineSelected.setEnabled(false);
    this.itemBack.setEnabled(false);
    this.itemForward.setEnabled(false);
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
    this.toolButtonList.get(6).setEnabled(false);
    this.toolButtonList.get(7).setEnabled(false);
    this.toolButtonList.get(9).setEnabled(false);
    this.toolButtonList.get(10).setEnabled(false);
    this.toolButtonList.get(11).setEnabled(false);
    this.toolButtonList.get(12).setEnabled(false);
    this.toolButtonList.get(15).setEnabled(false);
    this.toolButtonList.get(16).setEnabled(false);
    this.setFileHistoryMenuEnabled();
  }

  /**
   * �����ʼ��ʱ�������йز˵��ĳ�ʼ״̬�빦��
   */
  private void setMenuDefaultInit() {
    this.setMenuReset();
    this.setLineStyleString(LineSeparator.DEFAULT, true);
    this.setCharEncoding(CharEncoding.BASE, true);
  }

  /**
   * ���ò��ֲ˵���״̬�빦��
   */
  private void setMenuReset() {
    this.itemLineWrap.setSelected(this.textAreaSetting.isLineWrap);
    this.itemLineWrapByWord.setSelected(this.textAreaSetting.isWrapStyleWord);
    this.itemLineWrapByChar.setSelected(!this.textAreaSetting.isWrapStyleWord);
    this.itemAutoIndent.setSelected(this.textAreaSetting.autoIndent);
    this.itemToolBar.setSelected(this.setting.viewToolBar);
    this.itemStateBar.setSelected(this.setting.viewStateBar);
    this.itemLineNumber.setSelected(this.textAreaSetting.isLineNumberView);
    this.itemSearchResult.setSelected(this.setting.viewSearchResult);
    this.itemAlwaysOnTop.setSelected(this.setting.viewAlwaysOnTop);
    this.itemLockResizable.setSelected(this.setting.viewLockResizable);
    this.itemTabPolicy.setSelected(this.setting.viewTabPolicy);
    this.itemClickToClose.setSelected(this.setting.viewClickToClose);
    this.itemTabIcon.setSelected(this.setting.viewTabIcon);
    this.toolButtonList.get(17).setSelected(this.textAreaSetting.isLineWrap);
    this.setLineWrap();
    this.setLineWrapStyle();
    this.setToolBar();
    this.setStateBar();
    this.setSearchResult();
    this.setAlwaysOnTop();
    this.setLockResizable();
    this.setTabLayoutPolicy();
    this.setClickToClose();
    this.setTabIcon();
  }

  /**
   * �ָ�Ĭ������ʱ�������йز˵���Ĭ��״̬�빦��
   */
  private void setMenuDefaultSetting() {
    this.setLookAndFeel(Util.SYSTEM_LOOK_AND_FEEL_CLASS_NAME);
    this.setMenuReset();
    for (BaseTextArea textArea : this.textAreaList) {
      textArea.setTabSize(this.textAreaSetting.tabSize);
      textArea.setTabReplaceBySpace(this.textAreaSetting.tabReplaceBySpace);
      textArea.setAutoIndent(this.textAreaSetting.autoIndent);
      textArea.setAutoComplete(this.textAreaSetting.autoComplete);
    }
    this.setTextAreaFont();
    this.setColorStyle(0);
  }

  /**
   * ����"����༭"�˵��Ƿ����
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
   * �����ı����е��ַ��Ƿ�Ϊ�գ�������ز˵���״̬
   */
  private void setMenuStateByTextArea() {
    boolean isExist = true;
    if (this.txaMain == null || Util.isTextEmpty(this.txaMain.getText())) {
      isExist = false;
    }
    this.menuSort.setEnabled(isExist);
    this.itemCaseUp.setEnabled(isExist);
    this.itemCaseLow.setEnabled(isExist);
    this.itemLineBatchRemove.setEnabled(isExist);
    this.itemLineBatchInsert.setEnabled(isExist);
    this.itemLineBatchSeparate.setEnabled(isExist);
    this.itemLineBatchJoin.setEnabled(isExist);
    this.itemLineBatchMerge.setEnabled(isExist);
    this.itemLineBatchRewrite.setEnabled(isExist);
    this.itemFind.setEnabled(isExist);
    this.itemFindNext.setEnabled(isExist);
    this.itemFindPrevious.setEnabled(isExist);
    this.itemSelFindNext.setEnabled(isExist);
    this.itemSelFindPrevious.setEnabled(isExist);
    this.itemReplace.setEnabled(isExist);
    this.itemGoto.setEnabled(isExist);
    this.itemSelAll.setEnabled(isExist);
    this.itemPopSelAll.setEnabled(isExist);
    this.toolButtonList.get(11).setEnabled(isExist);
    this.toolButtonList.get(12).setEnabled(isExist);
  }

  /**
   * �����ı�����ѡ����ַ����Ƿ�Ϊ�գ�������ز˵���״̬
   */
  private void setMenuStateBySelectedText() {
    boolean isExist = false;
    String selText = this.txaMain.getSelectedText();
    if (!Util.isTextEmpty(selText)) {
      isExist = true;
    }
    this.itemCopy.setEnabled(isExist);
    this.itemCut.setEnabled(isExist);
    this.itemDel.setEnabled(isExist);
    this.itemQuickFindDown.setEnabled(isExist);
    this.itemQuickFindUp.setEnabled(isExist);
    this.itemSelCopy.setEnabled(isExist);
    this.itemSelInvert.setEnabled(isExist);
    this.itemPopCopy.setEnabled(isExist);
    this.itemPopCut.setEnabled(isExist);
    this.itemPopDel.setEnabled(isExist);
    this.itemTrimSelected.setEnabled(isExist);
    this.itemDelNullLineSelected.setEnabled(isExist);
    this.menuHighlight.setEnabled(isExist);
    this.menuPopHighlight.setEnabled(isExist);
    this.toolButtonList.get(6).setEnabled(isExist);
    this.toolButtonList.get(7).setEnabled(isExist);
  }

  /**
   * ���ó����������˵����״̬
   */
  private void setMenuStateUndoRedo() {
    boolean canRedo = this.undoManager.canRedo();
    boolean canUndo = this.undoManager.canUndo();
    this.itemReDo.setEnabled(canRedo);
    this.itemUnDo.setEnabled(canUndo);
    this.itemPopUnDo.setEnabled(canUndo);
    this.itemPopReDo.setEnabled(canRedo);
    this.toolButtonList.get(9).setEnabled(canUndo);
    this.toolButtonList.get(10).setEnabled(canRedo);
  }

  /**
   * ���ú��˺�ǰ���˵���״̬
   */
  private void setMenuStateBackForward() {
    LinkedList<PartnerBean> backForwardList = this.txaMain.getBackForwardList();
    int size = backForwardList.size();
    int backForwardIndex = this.txaMain.getBackForwardIndex();
    boolean canBack = backForwardIndex < (size - 1);
    boolean canForward = backForwardIndex > Util.DEFAULT_BACK_FORWARD_INDEX;
    this.itemBack.setEnabled(canBack);
    this.toolButtonList.get(15).setEnabled(canBack);
    this.itemForward.setEnabled(canForward);
    this.toolButtonList.get(16).setEnabled(canForward);
  }

  /**
   * ���á������ļ����˵����״̬
   */
  private void setMenuStateFrozen() {
    boolean isFrozen = this.txaMain.getFrozen();
    this.itemFrozenFile.setSelected(isFrozen);
    this.itemPopFrozenFile.setSelected(isFrozen);
  }

  /**
   * ���õ���ע��������ע�Ͳ˵����״̬
   */
  private void setMenuStateComment() {
    boolean hasLine = true;
    boolean hasBlock = true;
    if (this.txaMain.getFileExt().getCommentForLine() == null) {
      hasLine = false;
    }
    if (this.txaMain.getFileExt().getCommentForBlockBegin() == null) {
      hasBlock = false;
    }
    this.itemCommentForLine.setEnabled(hasLine);
    this.itemPopCommentForLine.setEnabled(hasLine);
    this.itemCommentForBlock.setEnabled(hasBlock);
    this.itemPopCommentForBlock.setEnabled(hasBlock);
  }

  /**
   * Ϊ���˵����������Ƿ�
   */
  private void setMenuMnemonic() {
    this.menuFile.setMnemonic('F');
    this.menuHelp.setMnemonic('H');
    this.menuTool.setMnemonic('T');
    this.menuEdit.setMnemonic('E');
    this.menuFileHistory.setMnemonic('H');
    this.menuSearch.setMnemonic('S');
    this.menuQuickFind.setMnemonic('Q');
    this.menuBookmark.setMnemonic('M');
    this.menuStyle.setMnemonic('O');
    this.menuView.setMnemonic('V');
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
    this.menuComment.setMnemonic('M');
    this.itemLineWrap.setMnemonic('W');
    this.menuLineWrapStyle.setMnemonic('L');
    this.itemLineWrapByWord.setMnemonic('W');
    this.itemLineWrapByChar.setMnemonic('C');
    this.itemAutoIndent.setMnemonic('I');
    this.itemToolBar.setMnemonic('T');
    this.itemStateBar.setMnemonic('S');
    this.itemLineNumber.setMnemonic('L');
    this.itemSearchResult.setMnemonic('E');
    this.itemAlwaysOnTop.setMnemonic('A');
    this.itemLockResizable.setMnemonic('R');
    this.menuTab.setMnemonic('B');
    this.itemTabPolicy.setMnemonic('P');
    this.itemClickToClose.setMnemonic('D');
    this.itemTabIcon.setMnemonic('I');
    this.menuTextAreaSwitch.setMnemonic('I');
    this.menuColor.setMnemonic('C');
    this.menuFontSize.setMnemonic('F');
    this.menuColorStyle.setMnemonic('Y');
    this.menuHighlight.setMnemonic('H');
    this.menuRmHighlight.setMnemonic('M');
    this.menuLookAndFeel.setMnemonic('K');
    this.menuPopHighlight.setMnemonic('H');
    this.menuPopRmHighlight.setMnemonic('M');
    this.menuPopCopyToClip.setMnemonic('P');
    this.itemFrozenFile.setMnemonic('Z');
    this.itemPopFrozenFile.setMnemonic('Z');
  }

  /**
   * Ϊ���˵������ÿ�ݼ�
   */
  public void setMenuAccelerator() {
    int size = this.menuItemList.size();
    for (int i = 0; i < size; i++) {
      JMenuItem item = this.menuItemList.get(i);
      item.setAccelerator(Util.transferKeyStroke(this.setting.shortcutMap.get(Util.SHORTCUT_NAMES[i])));
    }
  }

  /**
   * Ϊ���˵�������¼��Ĵ�����
   */
  public void actionPerformed(ActionEvent e) {
    if (this.itemAbout.equals(e.getSource())) {
      this.showAbout();
    } else if (this.itemCopy.equals(e.getSource())
        || this.itemPopCopy.equals(e.getSource())
        || this.toolButtonList.get(7).equals(e.getSource())) {
      this.copyText();
    } else if (this.itemCut.equals(e.getSource())
        || this.itemPopCut.equals(e.getSource())
        || this.toolButtonList.get(6).equals(e.getSource())) {
      this.cutText();
    } else if (this.itemInsertChar.equals(e.getSource())) {
      this.openInsertCharDialog();
    } else if (this.itemInsertDateTime.equals(e.getSource())) {
      this.openInsertDateDialog();
    } else if (this.itemDel.equals(e.getSource())
        || this.itemPopDel.equals(e.getSource())) {
      this.deleteText();
    } else if (this.itemSlicing.equals(e.getSource())) {
      this.openSlicingFileDialog();
    } else if (this.itemCaseUp.equals(e.getSource())) {
      this.switchCase(true);
    } else if (this.itemCaseLow.equals(e.getSource())) {
      this.switchCase(false);
    } else if (this.itemExit.equals(e.getSource())) {
      this.exit();
    } else if (this.itemFind.equals(e.getSource())
        || this.toolButtonList.get(11).equals(e.getSource())) {
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
    } else if (this.itemBookmarkSwitch.equals(e.getSource())) {
      this.bookmarkSwitch();
    } else if (this.itemBookmarkNext.equals(e.getSource())) {
      this.bookmarkNext();
    } else if (this.itemBookmarkPrevious.equals(e.getSource())) {
      this.bookmarkPrevious();
    } else if (this.itemBookmarkCopy.equals(e.getSource())) {
      this.bookmarkCopy();
    } else if (this.itemBookmarkClear.equals(e.getSource())) {
      this.bookmarkClear();
    } else if (this.itemFindBracket.equals(e.getSource())) {
      this.findTargetBracket();
    } else if (this.itemToCopyFileName.equals(e.getSource())
        || this.itemPopToCopyFileName.equals(e.getSource())) {
      this.toCopyFileName();
    } else if (this.itemToCopyFilePath.equals(e.getSource())
        || this.itemPopToCopyFilePath.equals(e.getSource())) {
      this.toCopyFilePath();
    } else if (this.itemToCopyDirPath.equals(e.getSource())
        || this.itemPopToCopyDirPath.equals(e.getSource())) {
      this.toCopyDirPath();
    } else if (this.itemToCopyAllText.equals(e.getSource())) {
      this.toCopyAllText();
    } else if (this.itemLineCopy.equals(e.getSource())) {
      this.copyLines();
    } else if (this.itemLineDel.equals(e.getSource())) {
      this.deleteLines();
    } else if (this.itemLineDelDuplicate.equals(e.getSource())) {
      this.deleteDuplicateLines();
    } else if (this.itemLineDelToStart.equals(e.getSource())) {
      this.deleteLineToStart();
    } else if (this.itemLineDelToEnd.equals(e.getSource())) {
      this.deleteLineToEnd();
    } else if (this.itemLineDelToFileStart.equals(e.getSource())) {
      this.deleteLineToFileStart();
    } else if (this.itemLineDelToFileEnd.equals(e.getSource())) {
      this.deleteLineToFileEnd();
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
    } else if (this.itemLineBatchJoin.equals(e.getSource())) {
      this.openBatchJoinDialog();
    } else if (this.itemLineBatchMerge.equals(e.getSource())) {
      this.mergeLines();
    } else if (this.itemLineBatchRewrite.equals(e.getSource())) {
      this.rewriteLines();
    } else if (this.itemSortUp.equals(e.getSource())) {
      this.sortLines(true);
    } else if (this.itemSortDown.equals(e.getSource())) {
      this.sortLines(false);
    } else if (this.itemSortReverse.equals(e.getSource())) {
      this.reverseLines();
    } else if (this.itemIndentAdd.equals(e.getSource())) {
      this.toIndent(true);
    } else if (this.itemIndentBack.equals(e.getSource())) {
      this.toIndent(false);
    } else if (this.itemSelCopy.equals(e.getSource())) {
      this.copySelectedText();
    } else if (this.itemSelInvert.equals(e.getSource())) {
      this.invertSelectedText();
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
      this.showHelpFrame();
    } else if (this.itemEncrypt.equals(e.getSource())) {
      this.openEncryptDialog();
    } else if (this.itemNumberConvert.equals(e.getSource())) {
      this.openNumberConvertDialog();
    } else if (this.itemCalculator.equals(e.getSource())) {
      this.openCalculatorDialog();
    } else if (this.itemCuttingFile.equals(e.getSource())) {
      this.openCuttingFileDialog();
    } else if (this.itemMergeFile.equals(e.getSource())) {
      this.openMergeFileDialog();
    } else if (this.itemLineWrap.equals(e.getSource())) {
      this.toolButtonList.get(17).setSelected(this.itemLineWrap.isSelected());
      this.setLineWrap();
    } else if (this.toolButtonList.get(17).equals(e.getSource())) {
      this.itemLineWrap.setSelected(this.toolButtonList.get(17).isSelected());
      this.setLineWrap();
    } else if (this.itemLineWrapByWord.equals(e.getSource())) {
      this.setLineWrapStyle();
    } else if (this.itemLineWrapByChar.equals(e.getSource())) {
      this.setLineWrapStyle();
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
    } else if (this.itemAutoIndent.equals(e.getSource())) {
      this.setAutoIndent();
    } else if (this.itemReset.equals(e.getSource())) {
      this.resetSetting();
    } else if (this.itemNew.equals(e.getSource())
        || this.toolButtonList.get(0).equals(e.getSource())) {
      this.createNew(null);
    } else if (this.itemOpen.equals(e.getSource())
        || this.toolButtonList.get(1).equals(e.getSource())) {
      this.openFile();
    } else if (this.itemOpenByEncoding.equals(e.getSource())) {
      this.openFileByEncoding();
    } else if (this.itemReOpen.equals(e.getSource())
        || this.itemPopReOpen.equals(e.getSource())) {
      this.reOpenFile();
    } else if (this.itemReName.equals(e.getSource())
        || this.itemPopReName.equals(e.getSource())) {
      this.reNameFile();
    } else if (this.itemPaste.equals(e.getSource())
        || this.itemPopPaste.equals(e.getSource())
        || this.toolButtonList.get(8).equals(e.getSource())) {
      this.pasteText();
    } else if (this.itemSelAll.equals(e.getSource())
        || this.itemPopSelAll.equals(e.getSource())) {
      this.selectAll();
    } else if (this.itemUnDo.equals(e.getSource())
        || this.itemPopUnDo.equals(e.getSource())
        || this.toolButtonList.get(9).equals(e.getSource())) {
      this.undoAction();
    } else if (this.itemReDo.equals(e.getSource())
        || this.itemPopReDo.equals(e.getSource())
        || this.toolButtonList.get(10).equals(e.getSource())) {
      this.redoAction();
    } else if (this.itemReplace.equals(e.getSource())
        || this.toolButtonList.get(12).equals(e.getSource())) {
      this.openReplaceDialog();
    } else if (this.itemSave.equals(e.getSource())
        || this.itemPopSave.equals(e.getSource())
        || this.toolButtonList.get(2).equals(e.getSource())) {
      this.saveFile(false);
    } else if (this.itemSaveAs.equals(e.getSource())
        || this.itemPopSaveAs.equals(e.getSource())
        || this.toolButtonList.get(3).equals(e.getSource())) {
      this.saveAsFile();
    } else if (this.itemClose.equals(e.getSource())
        || this.itemPopCloseCurrent.equals(e.getSource())
        || this.toolButtonList.get(4).equals(e.getSource())) {
      this.closeFile(true);
    } else if (this.itemCloseOther.equals(e.getSource())
        || this.itemPopCloseOthers.equals(e.getSource())) {
      this.closeOthers();
    } else if (this.itemCloseLeft.equals(e.getSource())
        || this.itemPopCloseLeft.equals(e.getSource())) {
      this.closeLeft();
    } else if (this.itemCloseRight.equals(e.getSource())
        || this.itemPopCloseRight.equals(e.getSource())) {
      this.closeRight();
    } else if (this.itemCloseAll.equals(e.getSource())
        || this.toolButtonList.get(5).equals(e.getSource())) {
      this.closeAll();
    } else if (this.itemFrozenFile.equals(e.getSource())) {
      this.itemPopFrozenFile.setSelected(this.itemFrozenFile.isSelected());
      this.frozenFile();
    } else if (this.itemPopFrozenFile.equals(e.getSource())) {
      this.itemFrozenFile.setSelected(this.itemPopFrozenFile.isSelected());
      this.frozenFile();
    } else if (this.itemPrint.equals(e.getSource())) {
      this.print();
    } else if (this.itemDelFile.equals(e.getSource())
        || this.itemPopDelFile.equals(e.getSource())) {
      this.deleteFile();
    } else if (this.itemBack.equals(e.getSource())
        || this.toolButtonList.get(15).equals(e.getSource())) {
      this.back();
    } else if (this.itemForward.equals(e.getSource())
        || this.toolButtonList.get(16).equals(e.getSource())) {
      this.forward();
    } else if (this.itemToolBar.equals(e.getSource())) {
      this.setToolBar();
    } else if (this.itemStateBar.equals(e.getSource())) {
      this.setStateBar();
    } else if (this.itemLineNumber.equals(e.getSource())) {
      this.setLineNumber(this.itemLineNumber.isSelected());
    } else if (this.itemSearchResult.equals(e.getSource())) {
      this.setSearchResult();
    } else if (this.itemAlwaysOnTop.equals(e.getSource())) {
      this.setAlwaysOnTop();
    } else if (this.itemLockResizable.equals(e.getSource())) {
      this.setLockResizable();
    } else if (this.itemTabPolicy.equals(e.getSource())) {
      this.setTabLayoutPolicy();
    } else if (this.itemClickToClose.equals(e.getSource())) {
      this.setClickToClose();
    } else if (this.itemTabIcon.equals(e.getSource())) {
      this.setTabIcon();
    } else if (this.itemFontSizePlus.equals(e.getSource())
        || this.toolButtonList.get(13).equals(e.getSource())) {
      this.setFontSizePlus();
    } else if (this.itemFontSizeMinus.equals(e.getSource())
        || this.toolButtonList.get(14).equals(e.getSource())) {
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
    } else if (this.itemColorBracketBack.equals(e.getSource())) {
      this.setBracketBackColor();
    } else if (this.itemColorLineBack.equals(e.getSource())) {
      this.setLineBackColor();
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
    } else if (this.itemHighlight1.equals(e.getSource())
        || this.itemPopHighlight1.equals(e.getSource())) {
      this.setHighlight(1);
    } else if (this.itemHighlight2.equals(e.getSource())
        || this.itemPopHighlight2.equals(e.getSource())) {
      this.setHighlight(2);
    } else if (this.itemHighlight3.equals(e.getSource())
        || this.itemPopHighlight3.equals(e.getSource())) {
      this.setHighlight(3);
    } else if (this.itemHighlight4.equals(e.getSource())
        || this.itemPopHighlight4.equals(e.getSource())) {
      this.setHighlight(4);
    } else if (this.itemHighlight5.equals(e.getSource())
        || this.itemPopHighlight5.equals(e.getSource())) {
      this.setHighlight(5);
    } else if (this.itemRmHighlight1.equals(e.getSource())
        || this.itemPopRmHighlight1.equals(e.getSource())) {
      this.rmHighlight(1);
    } else if (this.itemRmHighlight2.equals(e.getSource())
        || this.itemPopRmHighlight2.equals(e.getSource())) {
      this.rmHighlight(2);
    } else if (this.itemRmHighlight3.equals(e.getSource())
        || this.itemPopRmHighlight3.equals(e.getSource())) {
      this.rmHighlight(3);
    } else if (this.itemRmHighlight4.equals(e.getSource())
        || this.itemPopRmHighlight4.equals(e.getSource())) {
      this.rmHighlight(4);
    } else if (this.itemRmHighlight5.equals(e.getSource())
        || this.itemPopRmHighlight5.equals(e.getSource())) {
      this.rmHighlight(5);
    } else if (this.itemRmHighlightAll.equals(e.getSource())
        || this.itemPopRmHighlightAll.equals(e.getSource())) {
      this.rmHighlight(0);
    } else if (this.itemInformation.equals(e.getSource())) {
      this.openInformationDialog();
    } else if (this.itemWindowManage.equals(e.getSource())) {
      this.openWindowManageDialog();
    } else if (this.itemTextAreaSwitchPrevious.equals(e.getSource())) {
      this.textAreaSwitch(false);
    } else if (this.itemTextAreaSwitchNext.equals(e.getSource())) {
      this.textAreaSwitch(true);
    } else if (this.itemTextAreaHistoryBack.equals(e.getSource())) {
      this.textAreaHistory(false);
    } else if (this.itemTextAreaHistoryNext.equals(e.getSource())) {
      this.textAreaHistory(true);
    } else if (this.itemCommentForLine.equals(e.getSource())
        || this.itemPopCommentForLine.equals(e.getSource())) {
      this.setCommentForLine();
    } else if (this.itemCommentForBlock.equals(e.getSource())
        || this.itemPopCommentForBlock.equals(e.getSource())) {
      this.setCommentForBlock();
    } else if (this.itemTabSet.equals(e.getSource())) {
      this.openTabSetDialog();
    } else if (this.itemAutoComplete.equals(e.getSource())) {
      this.openAutoCompleteDialog();
    } else if (this.itemShortcutManage.equals(e.getSource())) {
      this.openShortcutManageDialog();
    } else if (this.itemClearFileHistory.equals(e.getSource())) {
      this.clearFileHistory();
    } else if (Util.FILE_HISTORY.equals(e.getActionCommand())) { // ����༭���ļ��˵�
      JMenuItem itemFile = (JMenuItem) e.getSource();
      this.openFileHistory(itemFile.getText());
    } else if (e.getActionCommand() != null
        && e.getActionCommand().startsWith(Util.LOOK_AND_FEEL)) { // ��ǰϵͳ֧�ֵ���۲˵�
      JRadioButtonMenuItem itemInfo = (JRadioButtonMenuItem) e.getSource();
      this.setLookAndFeel(itemInfo.getActionCommand().substring(
          (Util.LOOK_AND_FEEL + Util.PARAM_SPLIT).length()));
    }
  }

  /**
   * "�ָ�Ĭ������"�Ĵ�����
   */
  private void resetSetting() {
    int result = JOptionPane.showConfirmDialog(this,
        Util.convertToMsg("�˲������ָ����е����ã���������" + Util.SETTING_XML + "�����ļ���\n�Ƿ������"),
        Util.SOFTWARE, JOptionPane.YES_NO_OPTION);
    if (result != JOptionPane.YES_OPTION) {
      return;
    }
    this.setting = new Setting();
    this.initTextAreaSetting();
    this.settingAdapter.setSetting(this.setting);
    this.settingAdapter.createSettingFile();
    this.settingAdapter.initShortcuts();
    this.setMenuDefaultSetting();
    this.setMenuAccelerator();
  }

  /**
   * "ͳ����Ϣ"�Ĵ�����
   */
  private void openInformationDialog() {
    if (this.informationDialog == null) {
      this.informationDialog = new InformationDialog(this, true, this.txaMain);
    } else {
      this.informationDialog.setTextArea(this.txaMain);
      this.informationDialog.setVisible(true);
    }
  }

  /**
   * "���ڹ���"�Ĵ�����
   */
  private void openWindowManageDialog() {
    if (this.windowManageDialog == null) {
      this.windowManageDialog = new WindowManageDialog(this, true, this.txaMain, this.tpnMain);
    } else {
      this.windowManageDialog.setTextArea(this.txaMain);
      this.windowManageDialog.refresh();
      this.windowManageDialog.setVisible(true);
    }
  }

  /**
   * "�ĵ��л�-���/��ǰ"�Ĵ�����
   * 
   * @param isNext
   *          �л��ĵ��ķ���true��ʾ���false��ʾ��ǰ��
   */
  private void textAreaSwitch(boolean isNext) {
    int index = this.tpnMain.getSelectedIndex();
    int tabCount = this.tpnMain.getTabCount();
    if (isNext) {
      if (index + 1 == tabCount) {
        index = 0;
      } else {
        index++;
      }
    } else {
      if (index == 0) {
        index = tabCount - 1;
      } else {
        index--;
      }
    }
    this.tpnMain.setSelectedIndex(index);
  }

  /**
   * "�ĵ��л�-ǰһ��/��һ��"�Ĵ�����
   * 
   * @param isNext
   *          �л��ĵ��ķ���true��ʾ��һ����false��ʾǰһ����
   */
  private void textAreaHistory(boolean isNext) {
    int size = this.textAreaHashCodeList.size();
    if (size <= 1) {
      return;
    }
    int index = this.tpnMain.getSelectedIndex();
    int hashCode = 0;
    if (isNext) {
      if (this.textAreaHistoryIndex <= 0) {
        return;
      }
      this.textAreaHistoryIndex--;
      hashCode = this.textAreaHashCodeList.get(this.textAreaHistoryIndex);
    } else {
      if (this.textAreaHistoryIndex >= size - 1) {
        return;
      }
      this.textAreaHistoryIndex++;
      hashCode = this.textAreaHashCodeList.get(this.textAreaHistoryIndex);
    }
    index = this.getTextAreaIndex(hashCode);
    this.tpnMain.setSelectedIndex(index);
  }

  /**
   * ����hashCode��ȡ�ı�������ѡ����������ֵ
   * 
   * @param hashCode
   *          �ı����hashCode
   * @return ����ֵ
   */
  private int getTextAreaIndex(int hashCode) {
    int result = -1;
    for (int i = 0; i < this.textAreaList.size(); i++) {
      BaseTextArea textArea = this.textAreaList.get(i);
      int hash = textArea.hashCode();
      if (hash == hashCode) {
        result = i;
        break;
      }
    }
    return result;
  }

  /**
   * "��λƥ������"�Ĵ�����
   */
  private void findTargetBracket() {
    if (this.targetBracketIndex < 0) {
      return;
    }
    this.txaMain.setCaretPosition(this.targetBracketIndex);
  }

  /**
   * "����ע��"�Ĵ�����
   */
  private void setCommentForLine() {
    String comment = this.txaMain.getFileExt().getCommentForLine();
    if (comment == null) {
      return;
    }
    CurrentLines currentLines = new CurrentLines(this.txaMain);
    int startIndex = currentLines.getStartIndex();
    String strContent = currentLines.getStrContent();
    boolean label = false;
    if (strContent.endsWith("\n")) {
      strContent = strContent.substring(0, strContent.length() - 1);
      label = true;
    }
    strContent = comment + strContent.replaceAll("\n", "\n" + comment);
    if (label) {
      strContent = strContent + "\n";
    }
    this.txaMain.replaceRange(strContent, startIndex, currentLines.getEndIndex());
    this.txaMain.select(startIndex, startIndex + strContent.length());
  }

  /**
   * "����ע��"�Ĵ�����
   */
  private void setCommentForBlock() {
    String commentBegin = this.txaMain.getFileExt().getCommentForBlockBegin();
    if (commentBegin == null) {
      return;
    }
    String commentEnd = this.txaMain.getFileExt().getCommentForBlockEnd();
    CurrentLines currentLines = new CurrentLines(this.txaMain);
    int startIndex = currentLines.getStartIndex();
    String strContent = currentLines.getStrContent();
    boolean label = false;
    if (strContent.endsWith("\n")) {
      strContent = strContent.substring(0, strContent.length() - 1);
      label = true;
    }
    strContent = commentBegin + strContent + commentEnd;
    if (label) {
      strContent = strContent + "\n";
    }
    this.txaMain.replaceRange(strContent, startIndex, currentLines.getEndIndex());
    this.txaMain.select(startIndex, startIndex + strContent.length());
  }

  /**
   * "˫���رձ�ǩ"�Ĵ�����
   */
  private void setClickToClose() {
    this.setting.viewClickToClose = this.clickToClose = this.itemClickToClose.isSelected();
  }

  /**
   * "ָʾͼ��"�Ĵ�����
   */
  private void setTabIcon() {
    this.setting.viewTabIcon = this.isTabIconView = this.itemTabIcon.isSelected();
    int tabCount = this.tpnMain.getTabCount();
    for (int i = 0; i < tabCount; i++) {
      this.tpnMain.setIconAt(i, this.getTabIcon(this.textAreaList.get(i)));
    }
  }

  private ImageIcon getTabIcon(BaseTextArea textArea) {
    if (this.isTabIconView) {
      File fileTemp = textArea.getFile();
      boolean isFrozen = textArea.getFrozen();
      ImageIcon tabIcon = null;
      if (fileTemp == null) {
        if (isFrozen) {
          tabIcon = Util.TAB_NEW_FILE_FROZEN_ICON;
        } else {
          tabIcon = Util.TAB_NEW_FILE_ICON;
        }
      } else if (!fileTemp.exists()) {
        if (isFrozen) {
          tabIcon = Util.TAB_NOT_EXIST_FROZEN_ICON;
        } else {
          tabIcon = Util.TAB_NOT_EXIST_ICON;
        }
      } else if (!fileTemp.canWrite()) {
        if (isFrozen) {
          tabIcon = Util.TAB_EXIST_READONLY_FROZEN_ICON;
        } else {
          tabIcon = Util.TAB_EXIST_READONLY_ICON;
        }
      } else {
        if (isFrozen) {
          tabIcon = Util.TAB_EXIST_CURRENT_FROZEN_ICON;
        } else {
          tabIcon = Util.TAB_EXIST_CURRENT_ICON;
        }
      }
      return tabIcon;
    }
    return null;
  }

  /**
   * "�ر�����"�Ĵ�����
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
        if (!this.closeFile(true)) { // �رյ�ǰ���ļ�
          break;
        }
      }
    }
  }

  /**
   * "�ر����"�Ĵ�����
   */
  private void closeLeft() {
    int index = this.tpnMain.getSelectedIndex();
    this.tpnMain.setSelectedIndex(0);
    for (int i = 0; i < index; i++) {
      if (!this.closeFile(true)) { // �رյ�ǰ���ļ�
        break;
      }
    }
  }

  /**
   * "�ر��Ҳ�"�Ĵ�����
   */
  private void closeRight() {
    int index = this.tpnMain.getSelectedIndex();
    int size = this.textAreaList.size();
    index++;
    if (index >= size) {
      return;
    }
    this.tpnMain.setSelectedIndex(index);
    for (int i = index; i < size; i++) {
      if (!this.closeFile(true)) { // �رյ�ǰ���ļ�
        break;
      }
    }
  }

  /**
   * "�����ļ�"�Ĵ�����
   */
  private void frozenFile() {
    boolean isFrozen = this.itemFrozenFile.isSelected();
    this.txaMain.setFrozen(isFrozen);
    int index = this.tpnMain.getSelectedIndex();
    this.tpnMain.setIconAt(index, this.getTabIcon(this.txaMain));
  }

  /**
   * "��ӡ"�Ĵ�����
   */
  private void print() {
    try {
      this.txaMain.print();
    } catch (Exception x) {
      // x.printStackTrace();
    }
  }

  /**
   * "ȫ���ر�"�Ĵ�����
   */
  private void closeAll() {
    int size = this.textAreaList.size();
    for (int i = 0; i < size; i++) {
      this.tpnMain.setSelectedIndex(0);
      if (!this.closeFile(true)) { // �رյ�ǰ���ļ�
        break;
      }
    }
  }

  /**
   * "���б�ǩ"�Ĵ�����
   */
  private void setTabLayoutPolicy() {
    if (this.itemTabPolicy.isSelected()) {
      this.tpnMain.setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);
    } else {
      this.tpnMain.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
    }
    this.setting.viewTabPolicy = this.itemTabPolicy.isSelected();
  }

  /**
   * ����Ϊ"�л����"��ĳ����۵Ĵ�����
   * 
   * @param className
   *          ��Ҫ���õ������������
   */
  private void setLookAndFeel(String className) {
    if (this.strLookAndFeel.equals(className)) {
      return;
    }
    try {
      int start = this.txaMain.getSelectionStart();
      int end = this.txaMain.getSelectionEnd();
      UIManager.setLookAndFeel(className);
      this.strLookAndFeel = className;
      SwingUtilities.updateComponentTreeUI(this);
      SwingUtilities.updateComponentTreeUI(this.popMenuMain);
      SwingUtilities.updateComponentTreeUI(this.popMenuTabbed);
      this.popMenuMain.updateUI();
      this.popMenuTabbed.updateUI();
      this.destroyAllDialogs();
      this.txaMain.select(start, end);
    } catch (Exception x) {
      // x.printStackTrace();
    }
    int index = -1;
    for (int n = 0; n < Util.LOOK_AND_FEEL_INFOS.length; n++) {
      UIManager.LookAndFeelInfo info = Util.LOOK_AND_FEEL_INFOS[n];
      if (info.getClassName().equals(className)) {
        index = n;
        break;
      }
    }
    this.setting.viewLookAndFeel = index;
  }

  /**
   * �����д�������Ϊnull���Ա��ڽ��л�������������Ӧ���ڸ�����
   */
  private void destroyAllDialogs() {
    this.openFileChooser = null;
    this.saveFileChooser = null;
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
    if (this.autoCompleteDialog != null) {
      this.autoCompleteDialog.dispose();
      this.autoCompleteDialog = null;
    }
    if (this.shortcutManageDialog != null) {
      this.shortcutManageDialog.dispose();
      this.shortcutManageDialog = null;
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
    if (this.slicingFileDialog != null) {
      this.slicingFileDialog.dispose();
      this.slicingFileDialog = null;
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
    if (this.batchJoinDialog != null) {
      this.batchJoinDialog.dispose();
      this.batchJoinDialog = null;
    }
    if (this.signIdentifierDialog != null) {
      this.signIdentifierDialog.dispose();
      this.signIdentifierDialog = null;
    }
    if (this.informationDialog != null) {
      this.informationDialog.dispose();
      this.informationDialog = null;
    }
    if (this.windowManageDialog != null) {
      this.windowManageDialog.dispose();
      this.windowManageDialog = null;
    }
    if (this.encryptDialog != null) {
      this.encryptDialog.dispose();
      this.encryptDialog = null;
    }
    if (this.numberConvertDialog != null) {
      this.numberConvertDialog.dispose();
      this.numberConvertDialog = null;
    }
    if (this.calculatorDialog != null) {
      this.calculatorDialog.dispose();
      this.calculatorDialog = null;
    }
    if (this.cuttingFileDialog != null) {
      this.cuttingFileDialog.dispose();
      this.cuttingFileDialog = null;
    }
    if (this.mergeFileDialog != null) {
      this.mergeFileDialog.dispose();
      this.mergeFileDialog = null;
    }
    if (this.helpFrame != null) {
      this.helpFrame.dispose();
      this.helpFrame = null;
    }
  }

  /**
   * ��"�л����"�˵��£���ӵ�ǰϵͳ���õ����
   */
  private void addLookAndFeelItem() {
    UIManager.LookAndFeelInfo[] infos = Util.LOOK_AND_FEEL_INFOS;
    int index = 0;
    for (UIManager.LookAndFeelInfo info : infos) {
      JRadioButtonMenuItem itemInfo = new JRadioButtonMenuItem(info.getName());
      String className = info.getClassName();
      itemInfo.setActionCommand(Util.LOOK_AND_FEEL + Util.PARAM_SPLIT + className);
      itemInfo.addActionListener(this);
      this.menuLookAndFeel.add(itemInfo);
      this.bgpLookAndFeel.add(itemInfo);
      if (this.setting.viewLookAndFeel < 0 && Util.SYSTEM_LOOK_AND_FEEL_CLASS_NAME.equals(className)) {
        itemInfo.setSelected(true);
      } else if (this.setting.viewLookAndFeel >= 0 && this.setting.viewLookAndFeel == index) {
        itemInfo.setSelected(true);
      }
      index++;
    }
  }

  /**
   * "�к���"�Ĵ�����
   * 
   * @param enable
   *          �Ƿ���ʾ�к���
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
    this.setting.viewLineNumber = this.textAreaSetting.isLineNumberView = enable;
  }

  /**
   * �ڿ���"�к���"���õ�����£��½�������ļ�ʱ������к�������ʾ
   */
  private void setLineNumberForNew() {
    if (this.textAreaSetting.isLineNumberView && this.itemLineNumber.isEnabled()) {
      LineNumberView lineNumberView = new LineNumberView(this.textAreaList.getLast());
      JScrollPane srp = ((JScrollPane) this.tpnMain.getComponentAt(this.tpnMain.getTabCount() - 1));
      srp.setRowHeaderView(lineNumberView);
    }
  }

  /**
   * "���ҽ��"�Ĵ�����
   */
  private void setSearchResult() {
    boolean enable = this.itemSearchResult.isSelected();
    this.pnlSearchResult.setVisible(enable);
    if (enable) {
      this.spnMain.setDividerSize(3);
      this.spnMain.setDividerLocation(this.getDividerLocation());
    } else {
      this.spnMain.setDividerSize(0); // ���طָ���
    }
    this.setting.viewSearchResult = enable;
  }

  /**
   * ��ȡ���ҽ������λ��
   * 
   * @return ���ҽ������λ��
   */
  private int getDividerLocation() {
    int height = this.spnMain.getHeight();
    if (height <= 0) {
      height = 280;
    } else {
      height = height / 3 * 2;
    }
    return height;
  }

  /**
   * ��ʾ��رղ��ҽ�����
   * 
   * @param isView
   *          �Ƿ���ʾ���ҽ����壬true��ʾ��ʾ��false��ʾ�رա�
   */
  public void viewSearchResult(boolean isView) {
    if (isView && this.pnlSearchResult.isVisible()) {
      return;
    } else if (!isView && !this.pnlSearchResult.isVisible()) {
      return;
    }
    this.itemSearchResult.setSelected(isView);
    this.pnlSearchResult.setVisible(isView);
    this.setting.viewSearchResult = isView;
    if (isView) {
      this.spnMain.setDividerSize(3);
      this.spnMain.setDividerLocation(this.getDividerLocation());
    } else {
      this.spnMain.setDividerSize(0); // ���طָ���
    }
  }

  /**
   * "���ҽ��"�����˫����ת�Ĵ�����
   * 
   * @param hashCode
   *          ��Ҫ������ı����hashCode
   */
  public void searchResultToSwitchFile(int hashCode) {
    int index = this.getTextAreaIndex(hashCode);
    if (index >= 0) {
      this.tpnMain.setSelectedIndex(index);
    }
  }

  /**
   * ���ø��ı������塣�������ʾ"�к���"����ͬ�����������塣
   */
  private void setTextAreaFont() {
    for (BaseTextArea textArea : this.textAreaList) {
      textArea.setFont(this.textAreaSetting.font);
    }
    if (this.textAreaSetting.isLineNumberView) {
      JScrollPane srp = null;
      for (int i = 0; i < this.tpnMain.getTabCount(); i++) {
        srp = (JScrollPane) this.tpnMain.getComponentAt(i);
        if (srp.getRowHeader() != null && srp.getRowHeader().getView() != null) {
          srp.getRowHeader().getView().setFont(this.textAreaSetting.font);
        }
      }
    }
  }

  /**
   * "�ر�"�Ĵ�����
   * 
   * @param check
   *          �Ƿ��鵱ǰ�ļ����޸�״̬�����Ϊtrue��ʾ��Ҫ��飬��֮����Ҫ��顣
   * 
   * @return �ɹ��ر��ļ�ʱ����true��δ�ܹر�ʱ����false
   */
  private boolean closeFile(boolean check) {
    if (check && !this.saveFileBeforeAct()) {
      return false;
    }
    this.removeFromTextAreaHashCodeList();
    int index = this.tpnMain.getSelectedIndex();
    this.tpnMain.remove(index);
    this.textAreaList.remove(index);
    if (this.textAreaList.isEmpty()) {
      this.createNew(null);
    }
    return true;
  }

  /**
   * �ı�����ļ�·�������ڴ��ڹ���������ز���
   * 
   * @param textArea
   *          �ı���
   * @return ��ǰ�ı�����ļ�·��
   */
  private String getFilePathForWindowManage(BaseTextArea textArea) {
    File file = textArea.getFile();
    String path = file == null ? "" : file.getParent();
    String title = textArea.getPrefix() + textArea.getTitle();
    return path + title;
  }

  /**
   * "���ڹ���"������"����"�Ĵ�����
   * 
   * @param switchPath
   *          ��Ҫ������ļ�·��
   */
  public void windowManageToSwitchFile(String switchPath) {
    if (Util.isTextEmpty(switchPath)) {
      return;
    }
    for (int i = 0; i < this.textAreaList.size(); i++) {
      BaseTextArea textArea = this.textAreaList.get(i);
      if (switchPath.equals(this.getFilePathForWindowManage(textArea))) {
        this.tpnMain.setSelectedIndex(i);
        break;
      }
    }
  }

  /**
   * "���ڹ���"������"����"�Ĵ�����
   * 
   * @param savePaths
   *          ��Ҫ������ļ�·���б�
   */
  public void windowManageToSaveFile(LinkedList<String> savePaths) {
    for (String savePath : savePaths) {
      for (int i = 0; i < this.textAreaList.size(); i++) {
        BaseTextArea textArea = this.textAreaList.get(i);
        if (savePath.equals(this.getFilePathForWindowManage(textArea))) {
          this.tpnMain.setSelectedIndex(i);
          this.saveFile(false);
          break;
        }
      }
    }
  }

  /**
   * "���ڹ���"������"�ر�"�Ĵ�����
   * 
   * @param closePaths
   *          ��Ҫ�رյ��ļ�·���б�
   */
  public void windowManageToCloseFile(LinkedList<String> closePaths) {
    for (String closePath : closePaths) {
      for (int i = 0; i < this.textAreaList.size(); i++) {
        BaseTextArea textArea = this.textAreaList.get(i);
        if (closePath.equals(this.getFilePathForWindowManage(textArea))) {
          this.tpnMain.setSelectedIndex(i);
          if (!this.saveFileBeforeAct()) {
            break;
          }
          this.removeFromTextAreaHashCodeList();
          this.tpnMain.remove(i);
          this.textAreaList.remove(i);
          if (this.textAreaList.isEmpty()) {
            this.createNew(null);
          }
          break;
        }
      }
    }
  }

  /**
   * "���ڹ���"������"����"�Ĵ�����
   * 
   * @param sortedPaths
   *          ��Ҫ������ļ�·���б�
   * @param index
   *          ��ǰѡ����ļ�����ֵ
   */
  public void windowManageToSortFile(LinkedList<String> sortedPaths, int index) {
    if (sortedPaths == null) {
      return;
    }
    LinkedList<BaseTextArea> sortedTextAreaList = new LinkedList<BaseTextArea>();
    for (String sortedPath : sortedPaths) {
      for (BaseTextArea textArea : this.textAreaList) {
        if (sortedPath.equals(this.getFilePathForWindowManage(textArea))) {
          sortedTextAreaList.add(textArea);
          JScrollPane srpNew = new JScrollPane(textArea);
          this.tpnMain.addTab(textArea.getPrefix() + textArea.getTitle(), this.getTabIcon(textArea), srpNew);
          break;
        }
      }
    }
    if (index >= 0) {
      this.tpnMain.setSelectedIndex(index);
    }
    this.textAreaList.clear();
    this.textAreaList = sortedTextAreaList;
  }

  /**
   * ������"�ָ���"�Ĵ�����
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
   * ������"ƴ����"�Ĵ�����
   */
  private void openBatchJoinDialog() {
    if (this.batchJoinDialog == null) {
      this.batchJoinDialog = new BatchJoinDialog(this, true, this.txaMain);
    } else {
      this.batchJoinDialog.setTextArea(this.txaMain);
      this.batchJoinDialog.setVisible(true);
    }
  }

  /**
   * ������"�ϲ���"�Ĵ�����
   */
  private void mergeLines() {
    if (this.txaMain.getLineCount() <= 1) {
      return;
    }
    CurrentLines currentLines = new CurrentLines(this.txaMain);
    int lineCount = currentLines.getLineCount();
    if (lineCount < 2) {
      this.txaMain.selectAll();
    } else {
      this.txaMain.select(currentLines.getStartIndex(), currentLines.getEndIndex());
    }
    String[] arrText = Util.getCurrentLinesArray(this.txaMain);
    if (arrText.length <= 1) {
      return;
    }
    currentLines = new CurrentLines(this.txaMain);
    int startIndex = currentLines.getStartIndex();
    StringBuilder stbLines = new StringBuilder();
    for (String str : arrText) {
      stbLines.append(str);
    }
    this.txaMain.replaceSelection(stbLines.toString());
    this.txaMain.select(startIndex, startIndex + stbLines.length());
  }

  /**
   * ������"���и�д"�Ĵ�����
   */
  private void rewriteLines() {
    String[] arrText = Util.getCurrentLinesArray(this.txaMain);
    if (arrText.length <= 0) {
      return;
    }
    CurrentLines currentLines = new CurrentLines(this.txaMain);
    int startIndex = currentLines.getStartIndex();
    StringBuilder stbLines = new StringBuilder();
    for (String str : arrText) {
      stbLines.append(str + str + "\n");
    }
    this.txaMain.replaceSelection(stbLines.deleteCharAt(stbLines.length() - 1).toString());
    this.txaMain.select(startIndex, startIndex + stbLines.length());
  }

  /**
   * "�б��������"�Ĵ�����
   */
  private void openSignIdentifierDialog() {
    if (this.signIdentifierDialog == null) {
      Hashtable<String, String> hashtable = new Hashtable<String, String>();
      hashtable.put("����", Util.SIGN_CHARS);
      hashtable.put("���", Util.IDENTIFIER_CHARS);
      this.signIdentifierDialog = new SignIdentifierDialog(this, true, this.txaMain, hashtable);
    } else {
      this.signIdentifierDialog.setTextArea(this.txaMain);
      this.signIdentifierDialog.setVisible(true);
    }
  }

  /**
   * "����"�Ĵ�����
   * 
   * @param indent
   *          �����ķ�������Ϊtrue���˸�Ϊfalse
   */
  private void toIndent(boolean indent) {
    CurrentLines currentLines = new CurrentLines(this.txaMain);
    int startIndex = currentLines.getStartIndex();
    String[] arrText = Util.getCurrentLinesArray(this.txaMain);
    String strIndent = "\t"; // �����������ַ���Ĭ��ΪTAB�ַ�
    String strIndentBlanks = ""; // ��ǰ�ȼ���Tab���Ķ���ո�
    int tabSize = this.txaMain.getTabSize();
    for (int i = 0; i < tabSize; i++) {
      strIndentBlanks += " ";
    }
    if (this.txaMain.getTabReplaceBySpace()) { // ���������Ϊ���Կո����Tab�������������ַ�����Ϊ��Ӧ�����Ŀո�
      strIndent = strIndentBlanks;
    }
    StringBuilder stbIndent = new StringBuilder();
    boolean label = false; // ������ʶ�Ƿ�������˸�
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
    this.txaMain.replaceSelection(stbIndent.deleteCharAt(stbIndent.length() - 1).toString()); // ɾ���ַ���ĩβ����Ļ��з�
    this.txaMain.select(startIndex, startIndex + stbIndent.length());
  }

  /**
   * "ѡ������"�Ĵ�����
   * 
   * @param isFindDown
   *          ���ҵķ���������²�����Ϊtrue����֮��Ϊfalse
   */
  private void findSelNextText(boolean isFindDown) {
    String strSel = this.txaMain.getSelectedText();
    if (this.findReplaceDialog == null) {
      this.findReplaceDialog = new FindReplaceDialog(this, false, this.txaMain, this.pnlSearchResult,
          this.setting, false);
    } else {
      this.findReplaceDialog.setTextArea(this.txaMain);
    }
    if (!Util.isTextEmpty(strSel)) {
      this.findReplaceDialog.setFindText(strSel, true);
    }
    this.findReplaceDialog.findText(isFindDown);
  }

  /**
   * ������"�г�"�Ĵ�����
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
   * ������"����"�Ĵ�����
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
   * "������ʾ"�и���ʽ�Ĵ�����
   * 
   * @param style
   *          ����ĳ����ɫ���и�����ʾ����ȡֵ��1��2��3��4��5
   */
  private void setHighlight(int style) {
    String strSelText = this.txaMain.getSelectedText();
    if (Util.isTextEmpty(strSelText)) {
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
    Color color = Util.COLOR_HIGHLIGHTS[style - 1];
    for (Integer startIndex : linkedList) {
      try {
        this.txaMain.getHighlighter().addHighlight(startIndex, startIndex + strSelText.length(),
            new DefaultHighlighter.DefaultHighlightPainter(color));
        Highlighter.Highlight[] arrHighlight = this.txaMain.getHighlighter().getHighlights();
        this.txaMain.getHighlighterList().add(new PartnerBean(arrHighlight[arrHighlight.length - 1], style));
      } catch (BadLocationException x) {
        // x.printStackTrace();
      }
    }
  }

  /**
   * "�������"�и���ʽ�Ĵ�����
   * 
   * @param style
   *          ���ĳ����ɫ�ĸ�����ʾ����ȡֵ��1��2��3��4��5��������и�����0���ȹ�6��
   */
  private void rmHighlight(int style) {
    if (style == 0) {
      this.rmHighlightAll();
      return;
    }
    PartnerBean partnerBean = null;
    for (int n = 0; n < this.txaMain.getHighlighterList().size(); n++) {
      partnerBean = this.txaMain.getHighlighterList().get(n);
      if (partnerBean.getIndex() == style) {
        this.txaMain.getHighlighter().removeHighlight((Highlighter.Highlight) partnerBean.getObject());
        this.txaMain.getHighlighterList().remove(n);
        n--;
      }
    }
  }

  /**
   * "�������"��"���и�ʽ"�Ĵ�����
   */
  private void rmHighlightAll() {
    PartnerBean partnerBean = null;
    for (int n = 0; n < this.txaMain.getHighlighterList().size(); n++) {
      partnerBean = this.txaMain.getHighlighterList().get(n);
      if (partnerBean.getIndex() >= 1 && partnerBean.getIndex() <= 5) {
        this.txaMain.getHighlighter().removeHighlight((Highlighter.Highlight) partnerBean.getObject());
        this.txaMain.getHighlighterList().remove(n);
        n--;
      }
    }
  }

  /**
   * "��ɫ����"��"ȫ����ɫ/ȫ����ɫ"�Ĵ�����
   * 
   * @param mode
   *          ���ڱ�ʶ��ɫ/��ɫ�����Ϊtrue��ʾ��ɫ����֮��Ϊ��ɫ
   */
  private void setColorTransform(boolean mode) {
    Color colorFont = this.getConvertColor(this.txaMain.getForeground(), mode);
    Color colorBack = this.getConvertColor(this.txaMain.getBackground(), mode);
    Color colorCaret = this.getConvertColor(this.txaMain.getCaretColor(), mode);
    Color colorSelFont = this.getConvertColor(this.txaMain.getSelectedTextColor(), mode);
    Color colorSelBack = this.getConvertColor(this.txaMain.getSelectionColor(), mode);
    Color[] colorStyle = new Color[] { colorFont, colorBack, colorCaret, colorSelFont, colorSelBack };
    for (BaseTextArea textArea : this.textAreaList) {
      textArea.setColorStyle(colorStyle);
    }
    this.setting.colorStyle = this.textAreaSetting.colorStyle = colorStyle;
  }

  /**
   * ��ָ����rgbģʽ��ɫת��Ϊ��ɫ/��ɫ
   * 
   * @param color
   *          �������rgbģʽ��ɫ
   * @param mode
   *          ���ڱ�ʶ��ɫ/��ɫ�����Ϊtrue��ʾ��ɫ����֮��Ϊ��ɫ
   * @return ��������ɫ
   */
  private Color getConvertColor(Color color, boolean mode) {
    if (color != null) {
      int red = color.getRed();
      int green = color.getGreen();
      int blue = color.getBlue();
      if (mode) { // ��ɫ
        red = 255 - red;
        green = 255 - green;
        blue = 255 - blue;
      } else { // ��ɫ
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
   * ����"��ɫ����"�Ĵ�����
   * 
   * @param style
   *          ��ɫ������ţ���1��2��3��4��5�Լ�Ĭ����ɫ��0���ȹ�6��
   */
  private void setColorStyle(int style) {
    if (style > 0 && style <= Util.COLOR_STYLES.length) {
      this.setting.colorStyle = this.textAreaSetting.colorStyle = Util.COLOR_STYLES[style - 1];
    } else {
      this.setting.colorStyle = this.textAreaSetting.colorStyle = Util.COLOR_STYLE_DEFAULT;
    }
    for (BaseTextArea textArea : this.textAreaList) {
      textArea.setColorStyle(this.textAreaSetting.colorStyle);
    }
    this.searchTargetBracket();
  }

  /**
   * "����"�Ĵ�����
   * 
   * @param order
   *          �����˳������Ϊtrue������Ϊfalse
   */
  private void sortLines(boolean order) {
    if (Util.isTextEmpty(this.txaMain.getText())) {
      return;
    }
    CurrentLines currentLines = new CurrentLines(this.txaMain);
    int lineCount = currentLines.getLineCount();
    if (lineCount < 2) {
      this.txaMain.selectAll();
    } else {
      this.txaMain.select(currentLines.getStartIndex(), currentLines.getEndIndex());
    }
    String strSelText = this.txaMain.getSelectedText();
    String[] arrText = strSelText.split("\n", -1); // ����ǰѡ�����ı����д�������ĩβ�Ķദ����
    if (arrText.length <= 1) {
      return;
    }
    for (int i = 0; i < arrText.length; i++) { // ð������
      for (int j = 0; j < i; j++) {
        if (arrText[i].compareTo(arrText[j]) < 0) {
          String str = arrText[i];
          arrText[i] = arrText[j];
          arrText[j] = str;
        }
      }
    }
    StringBuilder stbSorted = new StringBuilder();
    if (order) { // ����
      for (String str : arrText) {
        stbSorted.append(str + "\n");
      }
      if (stbSorted.toString().startsWith("\n")) {
        stbSorted.deleteCharAt(0); // ɾ���ַ�����ͷ����Ļ��з�
      } else {
        stbSorted.deleteCharAt(stbSorted.length() - 1); // ɾ���ַ���ĩβ����Ļ��з�
      }
      this.txaMain.replaceSelection(stbSorted.toString());
    } else { // ����
      for (String str : arrText) {
        stbSorted.insert(0, str + "\n");
      }
      this.txaMain.replaceSelection(stbSorted.deleteCharAt(stbSorted.length() - 1).toString()); // ɾ���ַ���ĩβ����Ļ��з�
    }
  }

  /**
   * "����"�Ĵ�����
   */
  private void reverseLines() {
    if (Util.isTextEmpty(this.txaMain.getText())) {
      return;
    }
    CurrentLines currentLines = new CurrentLines(this.txaMain);
    int lineCount = currentLines.getLineCount();
    int endLineNum = currentLines.getEndLineNum();
    // ѡ����ĩ���Ƿ����ı���ĩ��
    boolean isNotEndLine = false;
    // �����ǰʵ��ѡ����ĩ�в����ı���ĩ�У�Ӧ��һ������
    if (endLineNum < this.txaMain.getLineCount() - 1) {
      isNotEndLine = true;
    }
    if (lineCount < 2) {
      this.txaMain.selectAll();
      isNotEndLine = false;
    } else {
      this.txaMain.select(currentLines.getStartIndex(), currentLines.getEndIndex());
    }
    String strSelText = this.txaMain.getSelectedText();
    String[] arrText = strSelText.split("\n", -1); // ����ǰѡ�����ı����д�������ĩβ�Ķദ����
    if (arrText.length <= 1) {
      return;
    }
    StringBuilder stbReversed = new StringBuilder();
    for (String str : arrText) {
      stbReversed.insert(0, str + "\n");
    }
    if (isNotEndLine) {
      this.txaMain.replaceSelection(stbReversed.deleteCharAt(0).toString()); // ɾ���ַ�����ͷ����Ļ��з�
    } else {
      this.txaMain.replaceSelection(stbReversed.deleteCharAt(stbReversed.length() - 1).toString()); // ɾ���ַ���ĩβ����Ļ��з�
    }
  }

  /**
   * "�Զ�����"�Ĵ�����
   */
  private void setAutoIndent() {
    boolean enable = this.itemAutoIndent.isSelected();
    for (BaseTextArea textArea : this.textAreaList) {
      textArea.setAutoIndent(enable);
    }
    this.setting.autoIndent = this.textAreaSetting.autoIndent = enable;
  }

  /**
   * "���Ƶ�ǰ��"�Ĵ�����
   */
  private void moveLineToUp() {
    CurrentLines currentLines = new CurrentLines(this.txaMain, CurrentLines.LineExtend.EXTEND_UP);
    String strContentExtend = currentLines.getStrContentExtend();
    if (strContentExtend == null) {
      return;
    }
    int startIndex = currentLines.getStartIndex();
    int endIndex = currentLines.getEndIndex();
    int endLineNum = currentLines.getEndLineNum();
    String strContentCurrent = currentLines.getStrContentCurrent();
    if (endLineNum == this.txaMain.getLineCount() - 1) {
      // �����ǰʵ��ѡ����ĩ��Ϊ�ı���ĩ�У�Ӧ��һ������
      strContentCurrent += "\n";
      strContentExtend = strContentExtend.substring(0, strContentExtend.length() - 1);
    }
    String strMoved = strContentCurrent + strContentExtend;
    this.txaMain.replaceRange(strMoved, startIndex, endIndex);
    this.txaMain.select(startIndex, startIndex + strContentCurrent.length() - 1);
  }

  /**
   * "���Ƶ�ǰ��"�Ĵ�����
   */
  private void moveLineToDown() {
    CurrentLines currentLines = new CurrentLines(this.txaMain, CurrentLines.LineExtend.EXTEND_DOWN);
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
      // �����չ��Ϊ�ı���ĩ�У�Ӧ��һ������
      strContentCurrent = strContentCurrent.substring(0, strContentCurrent.length() - 1);
      strContentExtend += "\n";
      isReachEnd = true;
    }
    String strMoved = strContentExtend + strContentCurrent;
    this.txaMain.replaceRange(strMoved, startIndex, endIndex);
    int selectEndIndex = startIndex + strContent.length() - 1; // �ƶ���֮������ѡ���ƶ����е���ĩƫ����
    if (isReachEnd) {
      selectEndIndex++;
    }
    this.txaMain.select(startIndex + strContentExtend.length(), selectEndIndex);
  }

  /**
   * "�������༭�б�"�Ĵ�����
   */
  private void clearFileHistory() {
    if (!this.fileHistoryList.isEmpty()) {
      this.fileHistoryList.clear();
      this.menuFileHistory.removeAll();
      this.setFileHistoryMenuEnabled();
    }
  }

  /**
   * ���"ѡ���ڿհ�"�Ĵ�����
   */
  private void trimSelected() {
    StringBuilder stbSelected = new StringBuilder(this.txaMain.getSelectedText());
    if (Util.isTextEmpty(stbSelected.toString())) {
      return;
    }
    boolean label = false; // �Ƿ���ڿհ��ַ��ı�ʶ��
    for (int i = 0; i < stbSelected.length(); i++) {
      switch (stbSelected.charAt(i)) {
      case ' ':
      case '\t':
      case '��':
        stbSelected.deleteCharAt(i);
        label = true; // ���ҵ��հ��ַ�
        i--; // ����ɾ���˲��ҵ��Ŀհ��ַ�����Ҫ��������ֵ
        break;
      }
    }
    if (label) {
      this.txaMain.replaceSelection(stbSelected.toString());
    }
  }

  /**
   * ����"�ַ������ʽ"�Ĵ�����
   * 
   * @param encoding
   *          �ַ������ʽ��ö��ֵ
   * @param isUpdateMenu
   *          �Ƿ���²˵���ѡ��
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
   * ���ݲ��õ��ַ������ʽ�����²˵���ѡ��
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
   * ����"���з���ʽ"�Ĵ�����
   * 
   * @param lineSeparator
   *          �����õĻ��з�
   * @param isUpdateMenu
   *          �Ƿ���²˵���ѡ��
   */
  private void setLineStyleString(LineSeparator lineSeparator, boolean isUpdateMenu) {
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
   * ���ݲ��õĻ��з������²˵���ѡ��
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
      if (LineSeparator.DEFAULT.toString().equals(LineSeparator.WINDOWS.toString())) {
        this.itemLineStyleWin.setSelected(true);
      } else if (LineSeparator.DEFAULT.toString().equals(LineSeparator.UNIX.toString())) {
        this.itemLineStyleUnix.setSelected(true);
      } else if (LineSeparator.DEFAULT.toString().equals(LineSeparator.MACINTOSH.toString())) {
        this.itemLineStyleMac.setSelected(true);
      }
    }
  }

  /**
   * ����"�����ַ�"�Ĵ�����
   */
  private void openInsertCharDialog() {
    if (this.insertCharDialog == null) {
      Hashtable<String, String> hashtable = new Hashtable<String, String>();
      hashtable.put("�������", Util.INSERT_SPECIAL);
      hashtable.put("������", Util.INSERT_PUNCTUATION);
      hashtable.put("��ѧ����", Util.INSERT_MATH);
      hashtable.put("��λ����", Util.INSERT_UNIT);
      hashtable.put("���ַ���", Util.INSERT_DIGIT);
      hashtable.put("ƴ������", Util.INSERT_PINYIN);
      this.insertCharDialog = new InsertCharDialog(this, false, this.txaMain, hashtable);
    } else if (!this.insertCharDialog.isVisible()) {
      this.insertCharDialog.setTextArea(this.txaMain);
      this.insertCharDialog.setVisible(true);
    }
  }

  /**
   * "���з�ʽ"�Ĵ�����
   */
  private void setLineWrapStyle() {
    boolean isByWord = this.itemLineWrapByWord.isSelected();
    for (BaseTextArea textArea : this.textAreaList) {
      textArea.setWrapStyleWord(isByWord);
    }
    this.setting.isWrapStyleWord = this.textAreaSetting.isWrapStyleWord = isByWord;
  }

  /**
   * "ɾ��������"�Ĵ�����
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
   * "ɾ������β"�Ĵ�����
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
   * "ɾ�����ļ���"�Ĵ�����
   */
  private void deleteLineToFileStart() {
    CurrentLine currentLine = new CurrentLine(this.txaMain);
    int currentIndex = currentLine.getCurrentIndex();
    if (currentIndex > 0) {
      this.txaMain.replaceRange("", 0, currentIndex);
    }
  }

  /**
   * "ɾ�����ļ�β"�Ĵ�����
   */
  private void deleteLineToFileEnd() {
    CurrentLine currentLine = new CurrentLine(this.txaMain);
    int currentIndex = currentLine.getCurrentIndex();
    int length = this.txaMain.getText().length();
    if (currentIndex < length) {
      this.txaMain.replaceRange("", currentIndex, length);
    }
  }

  /**
   * �������༭���ļ��Ӳ˵�
   * 
   * @param strFile
   *          �������ļ�·��
   */
  private void addFileHistoryItem(String strFile) {
    if (Util.isTextEmpty(strFile)) {
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
      this.fileHistoryList.add(new FileHistoryBean(strFile, this.txaMain.getFrozen()));
      this.menuFileHistory.add(itemFile);
      this.setFileHistoryMenuEnabled();
    }
  }

  /**
   * ����ļ����Ƿ��Ѵ���
   * 
   * @param strFile
   *          �������ļ�·��
   * @return ��Ҫ��ӵ�����༭��������-1��ʾ�����쳣
   */
  private int checkFileInHistory(String strFile) {
    if (Util.isTextEmpty(strFile)) {
      return -1;
    }
    int index = -1;
    int listSize = this.fileHistoryList.size();
    if (listSize == 0) {
      index = 0;
    } else {
      for (int i = 0; i < listSize; i++) {
        FileHistoryBean bean = this.fileHistoryList.get(i);
        if (strFile.equals(bean.getFileName())) {
          index = i;
          break;
        }
      }
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
   * ������༭���ļ�
   * 
   * @param strFile
   *          ����༭�������ļ�·��
   */
  private void openFileHistory(String strFile) {
    if (Util.isTextEmpty(strFile)) {
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
      JOptionPane.showMessageDialog(this, Util.convertToMsg("�ļ���" + file + " �����ڣ�"),
          Util.SOFTWARE, JOptionPane.ERROR_MESSAGE);
    }
  }

  /**
   * "ɾ��ȫ�Ŀ���"�Ĵ�����
   */
  private void delAllNullLines() {
    String strTextAll = this.txaMain.getText();
    String strText = this.delNullLines(strTextAll);
    if (!strTextAll.equals(strText)) {
      this.txaMain.setText(strText);
    }
  }

  /**
   * "ɾ��ѡ���ڿ���"�Ĵ�����
   */
  private void delSelectedNullLines() {
    String strTextSel = this.txaMain.getSelectedText();
    String strText = this.delNullLines(strTextSel);
    if (!strTextSel.equals(strText)) {
      this.txaMain.replaceSelection(strText);
    }
  }

  /**
   * ɾ���ı��ڿ���
   * 
   * @param strText
   *          ��������ı�
   * @return ɾ�����к���ı�
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
   * ������ɫ����
   * 
   * @param color
   *          �����õ���ɫ
   * @param index
   *          ��ɫ��������Ҫ�޸ĵ���ɫ����
   */
  private void changeColorStyle(Color color, int index) {
    if (color != null) {
      Color[] colorStyle = this.txaMain.getColorStyle();
      if (colorStyle == null) {
        colorStyle = Util.COLOR_STYLE_DEFAULT;
      }
      colorStyle[index] = color;
      for (BaseTextArea textArea : this.textAreaList) {
        textArea.setColorStyle(colorStyle);
      }
      this.setting.colorStyle = this.textAreaSetting.colorStyle = colorStyle;
    }
  }

  /**
   * "������ɫ"�Ĵ�����
   */
  private void setFontColor() {
    Color color = JColorChooser.showDialog(this, "������ɫ", this.txaMain.getForeground());
    this.changeColorStyle(color, 0);
  }

  /**
   * "������ɫ"�Ĵ�����
   */
  private void setBackColor() {
    Color color = JColorChooser.showDialog(this, "������ɫ", this.txaMain.getBackground());
    this.changeColorStyle(color, 1);
  }

  /**
   * "�����ɫ"�Ĵ�����
   */
  private void setCaretColor() {
    Color color = JColorChooser.showDialog(this, "�����ɫ", this.txaMain.getCaretColor());
    this.changeColorStyle(color, 2);
  }

  /**
   * "ѡ��������ɫ"�Ĵ�����
   */
  private void setSelFontColor() {
    Color color = JColorChooser.showDialog(this, "ѡ��������ɫ", this.txaMain.getSelectedTextColor());
    this.changeColorStyle(color, 3);
  }

  /**
   * "ѡ��������ɫ"�Ĵ�����
   */
  private void setSelBackColor() {
    Color color = JColorChooser.showDialog(this, "ѡ��������ɫ", this.txaMain.getSelectionColor());
    this.changeColorStyle(color, 4);
  }

  /**
   * "ƥ�����ű�����ɫ"�Ĵ�����
   */
  private void setBracketBackColor() {
    Color color = JColorChooser.showDialog(this, "ƥ�����ű�����ɫ", this.txaMain.getBracketBackColor());
    this.changeColorStyle(color, 5);
  }

  /**
   * "��ǰ�б�����ɫ"�Ĵ�����
   */
  private void setLineBackColor() {
    Color color = JColorChooser.showDialog(this, "��ǰ�б�����ɫ", this.txaMain.getLineBackColor());
    this.changeColorStyle(color, 6);
    this.txaMain.repaint(); // �ػ浱ǰ�ı����Խ�����޸���ɫ�󣬻��Ƶ�ǰ�б������ҵ�����
  }

  /**
   * "Tab������"�Ĵ�����
   */
  private void openTabSetDialog() {
    if (this.tabSetDialog == null) {
      this.tabSetDialog = new TabSetDialog(this, true, this.txaMain);
    } else {
      this.tabSetDialog.setTextArea(this.txaMain);
      this.tabSetDialog.setVisible(true);
    }
    int tabSize = this.tabSetDialog.getTabSize();
    boolean enable = this.tabSetDialog.getReplaceBySpace();
    for (BaseTextArea textArea : this.textAreaList) {
      textArea.setTabSize(tabSize);
      textArea.setTabReplaceBySpace(enable);
    }
    this.setting.tabSize = this.textAreaSetting.tabSize = tabSize;
    this.setting.tabReplaceBySpace = this.textAreaSetting.tabReplaceBySpace = enable;
  }

  /**
   * "�Զ����"�Ĵ�����
   */
  private void openAutoCompleteDialog() {
    if (this.autoCompleteDialog == null) {
      this.autoCompleteDialog = new AutoCompleteDialog(this, true, this.txaMain);
    } else {
      this.autoCompleteDialog.setTextArea(this.txaMain);
      this.autoCompleteDialog.setVisible(true);
    }
    boolean enable = this.autoCompleteDialog.getAutoComplete();
    for (BaseTextArea textArea : this.textAreaList) {
      textArea.setAutoComplete(enable);
    }
    this.setting.autoComplete = this.textAreaSetting.autoComplete = enable;
  }

  /**
   * "��ݼ�����"�Ĵ�����
   */
  private void openShortcutManageDialog() {
    if (this.shortcutManageDialog == null) {
      this.shortcutManageDialog = new ShortcutManageDialog(this, true,
          this.txaMain, this.setting, this.settingAdapter);
    } else {
      this.shortcutManageDialog.setVisible(true);
    }
  }

  /**
   * "��ݼ�����"�������޸Ŀ�ݼ��Ĵ�����
   * 
   * @param index
   *          ��Ҫ�޸Ŀ�ݼ��Ĳ˵��������ֵ
   */
  public void shortcutManageToSetMenuAccelerator(int index) {
    if (index < 0) {
      return;
    }
    JMenuItem item = this.menuItemList.get(index);
    item.setAccelerator(Util.transferKeyStroke(this.setting.shortcutMap.get(Util.SHORTCUT_NAMES[index])));
  }

  /**
   * �������ţ�"�Ŵ�"�Ĵ�����
   */
  private void setFontSizePlus() {
    Font font = this.txaMain.getFont();
    if (font.getSize() >= Util.MAX_FONT_SIZE) {
      return;
    }
    this.setting.font = this.textAreaSetting.font = new Font(font.getFamily(),
        font.getStyle(), font.getSize() + 1);
    this.setTextAreaFont();
  }

  /**
   * �������ţ�"��С"�Ĵ�����
   */
  private void setFontSizeMinus() {
    Font font = this.txaMain.getFont();
    if (font.getSize() <= Util.MIN_FONT_SIZE) {
      return;
    }
    this.setting.font = this.textAreaSetting.font = new Font(font.getFamily(),
        font.getStyle(), font.getSize() - 1);
    this.setTextAreaFont();
  }

  /**
   * �������ţ�"�ָ���ʼ"�Ĵ�����
   */
  private void setFontSizeReset() {
    Font font = this.txaMain.getFont();
    this.setting.font = this.textAreaSetting.font = new Font(font.getFamily(),
        font.getStyle(), Util.TEXT_FONT.getSize());
    this.setTextAreaFont();
  }

  /**
   * ����հ׵Ĵ�����
   * 
   * @param position
   *          ����հ׵�λ�ã�0Ϊ�����ס��հף�1Ϊ����β���հף�2Ϊ������+��β���հ�
   */
  private void trimLines(int position) {
    CurrentLines currentLines = new CurrentLines(this.txaMain);
    String strContent = currentLines.getStrContent();
    if (Util.isTextEmpty(strContent)) {
      return;
    }
    String[] arrContents = strContent.split("\n", -1); // ����ǰѡ�����ı����д�������ĩβ�Ķദ����
    StringBuilder stbContent = new StringBuilder(); // ���ڴ�Ŵ������ı�
    for (int n = 0; n < arrContents.length; n++) {
      String strLine = arrContents[n];
      if (Util.isTextEmpty(strLine)) {
        stbContent.append("\n");
        continue;
      }
      if (position == 0) { // ���"����"�հ�
        stbContent.append(this.trimLine(strLine, true) + "\n");
      } else if (position == 1) { // ���"��β"�հ�
        stbContent.append(this.trimLine(strLine, false) + "\n");
      } else if (position == 2) { // ���"����+��β"�հ�
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
   * ���һ���ı��Ŀհ�
   * 
   * @param strLine
   *          �������һ���ı�
   * @param position
   *          ����հ׵�λ�ã�trueΪ�����ס��հף�falseΪ����β���հ�
   * @return ���ָ���հ׺���ı�
   */
  private String trimLine(String strLine, boolean position) {
    if (Util.isTextEmpty(strLine)) {
      return strLine;
    }
    int blank = 0; // �հ��ַ��ĸ���
    boolean label = false; // ���ڱ�ʶ�հ��ַ��Ľ���
    if (position) { // ���"����"�հ�
      for (int i = 0; i < strLine.length(); i++) {
        switch (strLine.charAt(i)) {
        case ' ':
        case '\t':
        case '��':
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
    } else { // ���"��β"�հ�
      for (int i = strLine.length() - 1; i >= 0; i--) {
        switch (strLine.charAt(i)) {
        case ' ':
        case '\t':
        case '��':
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
   * ���ñ�������ͷ��"*"�ű�ʶ���Ա�ʾ�ı��Ƿ����޸�
   */
  private void setTextPrefix() {
    if (this.txaMain.getTextChanged()) {
      if (!this.stbTitle.toString().startsWith(Util.TEXT_PREFIX)) {
        this.stbTitle.insert(0, Util.TEXT_PREFIX); // �ڱ������Ŀ�ͷ���"*"
      }
    } else {
      if (this.stbTitle.toString().startsWith(Util.TEXT_PREFIX)) {
        this.stbTitle.deleteCharAt(0); // ɾ����������ͷ��"*"
      }
    }
    this.setTitle(this.stbTitle.toString());
    this.tpnMain.setTitleAt(this.tpnMain.getSelectedIndex(), this.txaMain.getPrefix()
        + this.txaMain.getTitle());
  }

  /**
   * ���ñ�������ͷ��"��"�ű�ʶ���Ա�ʾ�ı���ʽ�Ƿ����޸�
   */
  private void setStylePrefix() {
    if (this.txaMain.getStyleChanged()) {
      if (!this.stbTitle.toString().startsWith(Util.TEXT_PREFIX + Util.STYLE_PREFIX)) {
        if (this.stbTitle.toString().startsWith(Util.TEXT_PREFIX)) {
          this.stbTitle.insert(1, Util.STYLE_PREFIX);
        } else if (!this.stbTitle.toString().startsWith(Util.STYLE_PREFIX)) {
          this.stbTitle.insert(0, Util.STYLE_PREFIX);
        }
      }
    } else {
      if (this.stbTitle.toString().startsWith(Util.TEXT_PREFIX + Util.STYLE_PREFIX)) {
        this.stbTitle.deleteCharAt(1);
      } else if (this.stbTitle.toString().startsWith(Util.STYLE_PREFIX)) {
        this.stbTitle.deleteCharAt(0);
      }
    }
    this.setTitle(this.stbTitle.toString());
    this.tpnMain.setTitleAt(this.tpnMain.getSelectedIndex(), this.txaMain.getPrefix()
        + this.txaMain.getTitle());
  }

  /**
   * ����ϵͳ�����������
   * 
   * @param strText
   *          Ҫ�����������ı�
   */
  public void setClipboardContents(String strText) {
    if (Util.isTextEmpty(strText)) {
      return;
    }
    StringSelection ss = new StringSelection(strText);
    this.clip.setContents(ss, ss);
    this.itemPaste.setEnabled(true);
    this.itemPopPaste.setEnabled(true);
    this.toolButtonList.get(8).setEnabled(true);
  }

  /**
   * ����"��ǰ�ļ���"��������Ĵ�����
   */
  private void toCopyFileName() {
    this.setClipboardContents(this.txaMain.getTitle());
  }

  /**
   * ����"��ǰ�ļ�·��"��������Ĵ�����
   */
  private void toCopyFilePath() {
    String fileName = this.txaMain.getFileName();
    if (fileName == null) {
      fileName = this.txaMain.getTitle();
    }
    this.setClipboardContents(fileName);
  }

  /**
   * ����"��ǰĿ¼·��"��������Ĵ�����
   */
  private void toCopyDirPath() {
    String dirPath = " ";
    if (this.file != null) {
      dirPath = this.file.getParent();
    }
    this.setClipboardContents(dirPath);
  }

  /**
   * ����"��ǰ��"��������Ĵ�����
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
   * ����"��ǰ��"��������Ĵ�����
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
   * ����"�����ı�"��������Ĵ�����
   */
  private void toCopyAllText() {
    this.setClipboardContents(this.txaMain.getText());
  }

  /**
   * "��������"�Ĵ�����
   */
  private void setLockResizable() {
    this.setResizable(!this.itemLockResizable.isSelected());
    this.setting.viewLockResizable = this.itemLockResizable.isSelected();
  }

  /**
   * "ǰ����ʾ"�Ĵ�����
   */
  private void setAlwaysOnTop() {
    this.setAlwaysOnTop(this.itemAlwaysOnTop.isSelected());
    this.setting.viewAlwaysOnTop = this.itemAlwaysOnTop.isSelected();
  }

  /**
   * "��дѡ���ַ�"�Ĵ�����
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
   * "��תѡ���ַ�"�Ĵ�����
   */
  private void invertSelectedText() {
    int start = this.txaMain.getSelectionStart();
    int end = this.txaMain.getSelectionEnd();
    if (start != end) {
      StringBuilder stbSel = new StringBuilder(this.txaMain.getSelectedText());
      this.txaMain.replaceSelection(stbSel.reverse().toString());
      this.txaMain.select(start, end);
    }
  }

  /**
   * "��д��ǰ��"�Ĵ�����
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
   * "ɾ����ǰ��"�Ĵ�����
   */
  private void deleteLines() {
    CurrentLines currentLines = new CurrentLines(this.txaMain);
    int startIndex = currentLines.getStartIndex();
    int endIndex = currentLines.getEndIndex();
    int length = this.txaMain.getText().length();
    if (length > 0) {
      if (startIndex > 0 && endIndex == length) {
        startIndex--; // λ���ı����зǿյ����һ��ʱ��ȷ��ɾ�����з�
      }
      this.txaMain.replaceRange("", startIndex, endIndex);
    }
  }

  /**
   * "ɾ���ظ���"�Ĵ�����
   */
  private void deleteDuplicateLines() {
    if (Util.isTextEmpty(this.txaMain.getText())) {
      return;
    }
    CurrentLines currentLines = new CurrentLines(this.txaMain);
    int lineCount = currentLines.getLineCount();
    if (lineCount < 2) {
      this.txaMain.selectAll();
    } else {
      this.txaMain.select(currentLines.getStartIndex(), currentLines.getEndIndex());
    }
    String strSelText = this.txaMain.getSelectedText();
    String[] arrText = strSelText.split("\n", -1); // ����ǰѡ�����ı����д�������ĩβ�Ķദ����
    int arrSize = arrText.length;
    if (arrSize <= 1) {
      return;
    }
    ArrayList<String> listText = new ArrayList<String>(arrSize);
    Collections.addAll(listText, arrText);
    arrSize = listText.size();
    StringBuilder stbResult = new StringBuilder();
    for (int i = 0; i < arrSize; i++) {
      String str1 = listText.get(i);
      stbResult.append(str1 + "\n");
      if (Util.isTextEmpty(str1) || i >= (arrSize - 1)) {
        continue;
      }
      for (int j = i + 1; j < arrSize; j++) {
        String str2 = listText.get(j);
        if (str1.equals(str2)) {
          // ɾ���ظ���
          listText.remove(j);
          j--;
          arrSize--;
        }
      }
    }
    this.txaMain.replaceSelection(stbResult.deleteCharAt(stbResult.length() - 1).toString()); // ɾ���ַ���ĩβ����Ļ��з�
  }

  /**
   * "���ٲ���"�Ĵ�����
   * 
   * @param isFindDown
   *          ���ҷ������²���Ϊtrue�����ϲ���Ϊfalse
   */
  private void quickFindText(boolean isFindDown) {
    String strFindText = this.txaMain.getSelectedText();
    if (!Util.isTextEmpty(strFindText)) {
      int index = Util.findText(strFindText, this.txaMain, isFindDown, true,
          true, SearchStyle.DEFAULT);
      if (index >= 0) {
        this.txaMain.select(index, index + strFindText.length());
      }
    }
  }

  /**
   * "�л���Сд"�Ĵ�����
   * 
   * @param isCaseUp
   *          Ϊtrue��ʾ�л�Ϊ��д��Ϊfalse��ʾ�л�ΪСд
   */
  private void switchCase(boolean isCaseUp) {
    String strSel = this.txaMain.getSelectedText();
    if (Util.isTextEmpty(strSel)) {
      this.txaMain.selectAll();
      strSel = this.txaMain.getText();
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
   * "ɾ����ǰ�ļ�"�Ĵ�����
   */
  private void deleteFile() {
    int result = JOptionPane.showConfirmDialog(this,
        Util.convertToMsg("�˲�����ɾ�������ļ���" + this.file + "\n�Ƿ������"), Util.SOFTWARE,
        JOptionPane.YES_NO_CANCEL_OPTION);
    if (result != JOptionPane.YES_OPTION) {
      return;
    }
    if (this.file.delete()) {
      this.closeFile(false);
    } else {
      JOptionPane.showMessageDialog(this, Util.convertToMsg("�ļ���" + this.file + "ɾ��ʧ�ܣ�"),
          Util.SOFTWARE, JOptionPane.ERROR_MESSAGE);
    }
  }

  /**
   * "������"�Ĵ�����
   */
  private void reNameFile() {
    if (this.saveFileChooser == null) {
      this.saveFileChooser = new SaveFileChooser();
    }
    this.saveFileChooser.setSelectedFile(this.file);
    this.saveFileChooser.setDialogTitle("������");
    if (JFileChooser.APPROVE_OPTION != this.saveFileChooser.showSaveDialog(this)) {
      return;
    }
    File fileReName = this.saveFileChooser.getSelectedFile();
    if (this.file.equals(fileReName)) { // �ļ���δ�޸�ʱ����������
      return;
    }
    try {
      this.toSaveFile(fileReName);
    } catch (Exception x) {
      // x.printStackTrace();
      this.showSaveErrorDialog(fileReName);
      return;
    }
    this.file.delete(); // ɾ��ԭ�ļ�
    this.setFileNameAndPath(fileReName);
  }

  /**
   * "����"�Ĵ�����
   */
  private void undoAction() {
    if (this.txaMain.getFrozen()) {
      return;
    }
    if (this.undoManager.canUndo()) { // �ж��Ƿ���Գ���
      this.undoManager.undo(); // ִ�г�������
      this.txaMain.setUndoIndex(this.txaMain.getUndoIndex() - 1); // ������ʶ���ݼ�
      this.updateStateAll();
    }
    this.setAfterUndoRedo();
  }

  /**
   * "����"�Ĵ�����
   */
  private void redoAction() {
    if (this.txaMain.getFrozen()) {
      return;
    }
    if (this.undoManager.canRedo()) { // �ж��Ƿ��������
      this.undoManager.redo(); // ִ����������
      this.txaMain.setUndoIndex(this.txaMain.getUndoIndex() + 1); // ������ʶ������
      this.updateStateAll();
    }
    this.setAfterUndoRedo();
  }

  /**
   * ִ��"����"��"����"����������
   */
  private void setAfterUndoRedo() {
    if (this.txaMain.getUndoIndex() == Util.DEFAULT_UNDO_INDEX) { // ������ʶ����Ĭ��ֵ��ȣ���ʾ�ı�δ�޸�
      this.txaMain.setTextChanged(false);
    } else {
      this.txaMain.setTextChanged(true);
    }
    this.setTextPrefix();
    this.setMenuStateUndoRedo(); // ���ó����������˵���״̬
    this.setMenuStateByTextArea();
  }

  /**
   * "����"�Ĵ�����
   */
  private void back() {
    LinkedList<PartnerBean> backForwardList = this.txaMain.getBackForwardList();
    if (backForwardList.size() >= 2) {
      int backForwardIndex = this.txaMain.getBackForwardIndex();
      int length = this.txaMain.getText().length();
      Integer cursorIndex = (Integer)backForwardList.get(backForwardIndex + 1).getObject();
      if (length <= 0) {
        cursorIndex = 0;
      } else if (length <= cursorIndex) {
        cursorIndex = length - 1;
      }
      this.txaMain.setBackForwardIndex(backForwardIndex + 1);
      this.txaMain.setCaretPosition(cursorIndex);
    }
    this.setMenuStateBackForward();
  }

  /**
   * "ǰ��"�Ĵ�����
   */
  private void forward() {
    LinkedList<PartnerBean> backForwardList = this.txaMain.getBackForwardList();
    if (backForwardList.size() >= 2) {
      int backForwardIndex = this.txaMain.getBackForwardIndex();
      int length = this.txaMain.getText().length();
      Integer cursorIndex = (Integer)backForwardList.get(backForwardIndex - 1).getObject();
      if (length <= 0) {
        cursorIndex = 0;
      } else if (length <= cursorIndex) {
        cursorIndex = length - 1;
      }
      this.txaMain.setBackForwardIndex(backForwardIndex - 1);
      this.txaMain.setCaretPosition(cursorIndex);
    }
    this.setMenuStateBackForward();
  }

  /**
   * "ʱ��/����"�Ĵ�����
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
   * "�˳�"�Ĵ�����
   */
  private void exit() {
    boolean toExit = true;
    this.setting.fileHistoryList.clear();
    for (int i = 0; i < this.textAreaList.size(); i++) {
      this.tpnMain.setSelectedIndex(i);
      BaseTextArea textArea = this.textAreaList.get(i);
      File file = textArea.getFile();
      if (file != null && file.exists()) {
        try {
          String strFile = file.getCanonicalPath();
          this.setting.fileHistoryList.add(new FileHistoryBean(strFile, textArea.getFrozen()));
        } catch (Exception x) {
          // x.printStackTrace();
        }
      }
      if (!saveFileBeforeAct()) { // �رճ���ǰ����ļ��Ƿ����޸�
        toExit = false;
        break;
      }
    }
    this.settingAdapter.save();
    if (toExit) {
      System.exit(0);
    }
  }

  /**
   * "����"�Ĵ�����
   */
  private void showAbout() {
    if (this.aboutDialog == null) {
      final String strQQ = "155222113";
      final String strEmail = "chenzhengfeng@163.com";
      final String strGithubCode = "https://github.com/xiboliya/snowpad";
      final String strCodeCloud = "https://gitee.com/xiboliya/snowpad";
      final String strCoding = "https://coding.net/u/xiboliya/p/SnowPad";
      final String strCsdnCode = "https://code.csdn.net/chenzhengfeng/snowpad";
      String[] arrStrLabel = new String[] {
          "������ƣ�" + Util.SOFTWARE,
          "����汾��" + Util.VERSION,
          "������ߣ���ԭ",
          "����QQ��" + strQQ,
          "�������䣺" + strEmail,
          "<html>GitHub��<a href='" + strGithubCode + "'>" + strGithubCode + "</a></html>",
          "<html>���ƣ�<a href='" + strCodeCloud + "'>" + strCodeCloud + "</a></html>",
          "<html>Coding��<a href='" + strCoding + "'>" + strCoding + "</a></html>",
          "<html>CSDN���룺<a href='" + strCsdnCode + "'>" + strCsdnCode + "</a></html>",
          "�����Ȩ����ѭGNU GPL�����濪Դ���Э����������" };
      this.aboutDialog = new AboutDialog(this, true, arrStrLabel, Util.SW_ICON);
      this.aboutDialog.addLinkByIndex(5, strGithubCode);
      this.aboutDialog.addLinkByIndex(6, strCodeCloud);
      this.aboutDialog.addLinkByIndex(7, strCoding);
      this.aboutDialog.addLinkByIndex(8, strCsdnCode);
      this.aboutDialog.pack(); // �Զ��������ڴ�С������Ӧ�����
    }
    this.aboutDialog.setVisible(true);
  }

  /**
   * "����"�Ĵ�����
   */
  private void openEncryptDialog() {
    if (this.encryptDialog == null) {
      this.encryptDialog = new EncryptDialog(this, false, this.txaMain);
    } else {
      this.encryptDialog.setTextArea(this.txaMain);
      this.encryptDialog.refreshView();
      this.encryptDialog.setVisible(true);
    }
  }

  /**
   * "����ת��"�Ĵ�����
   */
  private void openNumberConvertDialog() {
    if (this.numberConvertDialog == null) {
      this.numberConvertDialog = new NumberConvertDialog(this, false, this.txaMain);
    } else {
      this.numberConvertDialog.setVisible(true);
    }
  }

  /**
   * "������"�Ĵ�����
   */
  private void openCalculatorDialog() {
    if (this.calculatorDialog == null) {
      this.calculatorDialog = new CalculatorDialog(this, false);
    } else {
      this.calculatorDialog.setVisible(true);
    }
  }

  /**
   * "�и��ļ�"�Ĵ�����
   */
  private void openCuttingFileDialog() {
    if (this.cuttingFileDialog == null) {
      this.cuttingFileDialog = new CuttingFileDialog(this, false);
    } else {
      this.cuttingFileDialog.setVisible(true);
    }
  }

  /**
   * "ƴ���ļ�"�Ĵ�����
   */
  private void openMergeFileDialog() {
    if (this.mergeFileDialog == null) {
      this.mergeFileDialog = new MergeFileDialog(this, false);
    } else {
      this.mergeFileDialog.setVisible(true);
    }
  }

  /**
   * "��������"�Ĵ�����
   */
  private void showHelpFrame() {
    if (this.helpFrame == null) {
      this.helpFrame = new HelpFrame();
    } else {
      this.helpFrame.setVisible(true);
      this.helpFrame.requestFocus();
    }
  }

  /**
   * "�Զ�����"�Ĵ�����
   */
  private void setLineWrap() {
    boolean isLineWrap = this.itemLineWrap.isSelected();
    this.menuLineWrapStyle.setEnabled(isLineWrap);
    for (BaseTextArea textArea : this.textAreaList) {
      textArea.setLineWrap(isLineWrap);
    }
    this.itemLineNumber.setEnabled(!isLineWrap); // ���������"�Զ�����"����"�к���"����ʧЧ
    this.setLineNumber(this.textAreaSetting.isLineNumberView); // �����к����Ƿ���ʾ
    this.setting.isLineWrap = this.textAreaSetting.isLineWrap = isLineWrap;
  }

  /**
   * ��ǰ�༭���ı����ݻ��ʽ���޸ģ���ִ���½����رղ���ʱ�������Ի������û�ѡ����Ӧ�Ĳ���
   * 
   * @return �û�ѡ�����ǻ��ʱ����true��ѡ��ȡ����ر�ʱ����false
   */
  private boolean saveFileBeforeAct() {
    if (this.txaMain.getTextChanged() || this.txaMain.getStyleChanged()) {
      String strChanged = "����";
      if (this.txaMain.getTextChanged() && this.txaMain.getStyleChanged()) {
        strChanged = "�������ʽ";
      } else if (this.txaMain.getStyleChanged()) {
        strChanged = "��ʽ";
      }
      String str = "\"" + this.txaMain.getTitle() + "\" ��" + strChanged + "�Ѿ��޸ġ�\n�뱣���ļ���";
      if (this.file != null) {
        str = "�ļ���" + this.file + " ��" + strChanged + "�Ѿ��޸ġ�\n�뱣���ļ���";
      }
      int result = JOptionPane.showConfirmDialog(this, Util.convertToMsg(str), Util.SOFTWARE, JOptionPane.YES_NO_CANCEL_OPTION);
      if (result == JOptionPane.YES_OPTION) {
        return this.saveFile(false);
      } else if (result == JOptionPane.CANCEL_OPTION || result == JOptionPane.CLOSED_OPTION) {
        return false;
      }
    }
    return true;
  }

  /**
   * "�½�"�Ĵ�����
   * 
   * @param file
   *          ���򿪵��ļ�
   */
  private void createNew(File file) {
    BaseTextArea txaNew = new BaseTextArea(this.textAreaSetting);
    JScrollPane srpNew = new JScrollPane(txaNew);
    String title = null;
    if (file != null) {
      title = file.getName();
      txaNew.setFileExt(this.getFileExtByName(title));
      txaNew.setFile(file);
    } else {
      int index = this.toSetNewFileIndex();
      title = Util.NEW_FILE_NAME + index;
      txaNew.setNewFileIndex(index);
    }
    txaNew.setTitle(title);
    txaNew.addCaretListener(this);
    txaNew.getDocument().addUndoableEditListener(this);
    this.tpnMain.addTab(title, this.getTabIcon(txaNew), srpNew);
    this.tpnMain.setSelectedComponent(srpNew);
    this.textAreaList.add(txaNew);
    this.setLineNumberForNew();
    this.addTextAreaMouseListener();
    this.setMenuStateComment();
    new DropTarget(txaNew, this); // �����Ϸ�Ŀ�꣬������ĳ���������drop����
  }

  /**
   * ��ȡ��ǰ�༭���ļ���չ������
   * 
   * @param fileName
   *          ��ǰ�༭���ļ���
   * @return ���ڱ�ʶ�ļ���չ������
   */
  private FileExt getFileExtByName(String fileName) {
    FileExt[] arrFileExt = FileExt.values(); // ��ȡ����ö�����г�Ա������
    FileExt fileExt = FileExt.TXT; // ��ǰ���ļ�����
    for (FileExt tempFileExt : arrFileExt) {
      if (fileName.toLowerCase().endsWith(tempFileExt.toString().toLowerCase())) {
        fileExt = tempFileExt;
        break;
      }
    }
    return fileExt;
  }

  /**
   * �½��ļ�ʱ�����������ļ����
   * 
   * @return �½��ļ������
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
   * "ɾ��"�Ĵ�����
   */
  private void deleteText() {
    this.txaMain.replaceSelection("");
  }

  /**
   * "����ļ�"�Ĵ�����
   */
  private void openSlicingFileDialog() {
    if (this.slicingFileDialog == null) {
      this.slicingFileDialog = new SlicingFileDialog(this, true, this.txaMain);
    } else {
      this.slicingFileDialog.setTextArea(this.txaMain);
      this.slicingFileDialog.setVisible(true);
    }
  }

  /**
   * "����"�Ĵ�����
   */
  private void copyText() {
    this.setClipboardContents(this.txaMain.getSelectedText());
  }

  /**
   * "����"�Ĵ�����
   */
  private void cutText() {
    this.copyText(); // ����ѡ���ı�
    this.deleteText(); // ɾ��ѡ���ı�
  }

  /**
   * "ճ��"�Ĵ�����
   */
  private void pasteText() {
    try {
      Transferable tf = this.clip.getContents(this);
      if (tf != null) {
        String str = tf.getTransferData(DataFlavor.stringFlavor).toString(); // ����������ڵ����ݲ����ı������׳��쳣
        if (str != null) {
          str = str.replaceAll(LineSeparator.WINDOWS.toString(),
              LineSeparator.UNIX.toString()); // ��Windows��ʽ�Ļ��з�\r\n��ת��ΪUNIX/Linux��ʽ
          str = str.replaceAll(LineSeparator.MACINTOSH.toString(),
              LineSeparator.UNIX.toString()); // Ϊ���ݴ������ܲ����\r�ַ��滻Ϊ\n
          this.txaMain.replaceSelection(str);
        }
      }
    } catch (Exception x) {
      // �������쳣
      // x.printStackTrace();
    }
  }

  /**
   * "ȫѡ"�Ĵ�����
   */
  private void selectAll() {
    this.txaMain.selectAll();
  }

  /**
   * "����"�Ĵ�����
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
    this.setting.font = this.textAreaSetting.font = this.fontChooser
        .getTextAreaFont();
    this.setTextAreaFont();
  }

  /**
   * Ԥ����ǰѡ�е��ı����Ա����ڲ��ҶԻ������ʾ
   * 
   * @return ������֮����ı�
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
   * "����"�Ĵ�����
   */
  private void openFindDialog() {
    if (this.findReplaceDialog == null) {
      this.findReplaceDialog = new FindReplaceDialog(this, false, this.txaMain, this.pnlSearchResult,
          this.setting, true);
    } else {
      this.findReplaceDialog.setTextArea(this.txaMain);
      this.txaMain.requestFocus();
      this.findReplaceDialog.setVisible(true);
    }
    this.findReplaceDialog.setTabbedIndex(0); // �򿪲���ѡ�
    String strSel = this.checkSelText();
    if (!Util.isTextEmpty(strSel)) {
      this.findReplaceDialog.setFindText(strSel, true);
    } else {
      this.findReplaceDialog.setFindTextSelect();
    }
  }

  /**
   * "������һ��"�Ĵ�����
   * 
   * @param isFindDown
   *          ���ҵķ���������²�����Ϊtrue����֮��Ϊfalse
   */
  private void findNextText(boolean isFindDown) {
    String strSel = this.checkSelText();
    if (this.findReplaceDialog == null) {
      this.findReplaceDialog = new FindReplaceDialog(this, false, this.txaMain, this.pnlSearchResult,
          this.setting, true);
      this.findReplaceDialog.setTabbedIndex(0); // �򿪲���ѡ�
      if (!Util.isTextEmpty(strSel)) {
        this.findReplaceDialog.setFindText(strSel, true);
      }
    } else if (Util.isTextEmpty(this.findReplaceDialog.getFindText())) {
      this.findReplaceDialog.setTextArea(this.txaMain);
      if (!Util.isTextEmpty(strSel)) {
        this.findReplaceDialog.setFindText(strSel, true);
      }
      this.findReplaceDialog.setTabbedIndex(0); // �򿪲���ѡ�
      this.findReplaceDialog.setVisible(true);
    } else {
      this.findReplaceDialog.setTextArea(this.txaMain);
      this.findReplaceDialog.findText(isFindDown);
    }
  }

  /**
   * "�滻"�Ĵ�����
   */
  private void openReplaceDialog() {
    if (this.findReplaceDialog == null) {
      this.findReplaceDialog = new FindReplaceDialog(this, false, this.txaMain, this.pnlSearchResult,
          this.setting, true);
    } else {
      this.findReplaceDialog.setTextArea(this.txaMain);
      this.findReplaceDialog.setVisible(true);
    }
    this.findReplaceDialog.setTabbedIndex(1); // ���滻ѡ�
    String strSel = this.checkSelText();
    if (!Util.isTextEmpty(strSel)) {
      this.findReplaceDialog.setFindText(strSel, true);
    } else {
      this.findReplaceDialog.setFindTextSelect();
    }
    this.findReplaceDialog.setReplaceText("");
  }

  /**
   * "ת��"�Ĵ�����
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
   * "����/ȡ����ǩ"�Ĵ�����
   */
  private void bookmarkSwitch() {
    CurrentLine currentLine = new CurrentLine(this.txaMain);
    this.txaMain.switchBookmark(currentLine.getLineNum());
    this.repaintLineNumberView();
  }

  /**
   * �ػ��к���
   */
  private void repaintLineNumberView() {
    JViewport viewport = ((JScrollPane) this.tpnMain.getSelectedComponent()).getRowHeader();
    if (viewport != null) {
      LineNumberView lineNumberView = (LineNumberView)viewport.getView();
      if (lineNumberView != null) {
        lineNumberView.repaint();
      }
    }
  }

  /**
   * "��һ����ǩ"�Ĵ�����
   */
  private void bookmarkNext() {
    CurrentLine currentLine = new CurrentLine(this.txaMain);
    int lineNum = currentLine.getLineNum();
    LinkedList<Integer> bookmarks = this.txaMain.getBookmarks();
    int size = bookmarks.size();
    if (size <= 0) {
      return;
    }
    for (int bookmark : bookmarks) {
      if (bookmark > lineNum) {
        int total = this.txaMain.getLineCount(); // �ı���������
        if (bookmark >= total) {
          return;
        }
        try {
          int offset = this.txaMain.getLineStartOffset(bookmark);
          this.txaMain.setCaretPosition(offset);
        } catch (BadLocationException x) {
          // x.printStackTrace();
        }
        break;
      }
    }
  }

  /**
   * "��һ����ǩ"�Ĵ�����
   */
  private void bookmarkPrevious() {
    CurrentLine currentLine = new CurrentLine(this.txaMain);
    int lineNum = currentLine.getLineNum();
    LinkedList<Integer> bookmarks = this.txaMain.getBookmarks();
    int size = bookmarks.size();
    if (size <= 0) {
      return;
    }
    for (int i = size - 1; i >= 0; i--) {
      int bookmark = bookmarks.get(i);
      if (bookmark < lineNum) {
        try {
          int offset = this.txaMain.getLineStartOffset(bookmark);
          this.txaMain.setCaretPosition(offset);
        } catch (BadLocationException x) {
          // x.printStackTrace();
        }
        break;
      }
    }
  }

  /**
   * "������ǩ��"�Ĵ�����
   */
  private void bookmarkCopy() {
    LinkedList<Integer> bookmarks = this.txaMain.getBookmarks();
    int size = bookmarks.size();
    if (size <= 0) {
      return;
    }
    int lineCount = this.txaMain.getLineCount(); // �ı���������
    StringBuilder stbLines = new StringBuilder();
    for (int bookmark : bookmarks) {
      if (bookmark < lineCount) {
        try {
          int start = this.txaMain.getLineStartOffset(bookmark);
          int end = this.txaMain.getLineEndOffset(bookmark);
          stbLines.append(this.txaMain.getText(start, end - start));
        } catch (BadLocationException x) {
          // x.printStackTrace();
        }
      }
    }
    if (stbLines.length() > 0) {
      this.setClipboardContents(stbLines.toString());
    }
  }

  /**
   * "���������ǩ"�Ĵ�����
   */
  private void bookmarkClear() {
    LinkedList<Integer> bookmarks = this.txaMain.getBookmarks();
    bookmarks.clear();
    this.repaintLineNumberView();
  }

  /**
   * "������"�Ĵ�����
   */
  private void setToolBar() {
    this.tlbMain.setVisible(this.itemToolBar.isSelected());
    this.setting.viewToolBar = this.itemToolBar.isSelected();
  }

  /**
   * "״̬��"�Ĵ�����
   */
  private void setStateBar() {
    this.pnlState.setVisible(this.itemStateBar.isSelected());
    this.setting.viewStateBar = this.itemStateBar.isSelected();
  }

  /**
   * "���������ļ�"�Ĵ�����
   */
  private void reOpenFile() {
    if (this.file == null) {
      return;
    }
    if (this.txaMain.getTextChanged() || this.txaMain.getStyleChanged()) {
      String strTemp = "����";
      if (this.txaMain.getTextChanged() && this.txaMain.getStyleChanged()) {
        strTemp = "�������ʽ";
      } else if (this.txaMain.getStyleChanged()) {
        strTemp = "��ʽ";
      }
      int result = JOptionPane.showConfirmDialog(this,
          Util.convertToMsg("�ļ���" + this.file + " ��" + strTemp + "�Ѿ��޸ġ�\n������޸�����������"),
          Util.SOFTWARE, JOptionPane.YES_NO_CANCEL_OPTION);
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
      int result = JOptionPane.showConfirmDialog(this,
          Util.convertToMsg("�ļ���" + this.file + "�����ڡ�\nҪ���´�����"),
          Util.SOFTWARE, JOptionPane.YES_NO_CANCEL_OPTION);
      if (result == JOptionPane.YES_OPTION) {
        Util.checkFile(this.file);
        try {
          this.toSaveFile(this.file);
        } catch (Exception x) {
          // x.printStackTrace();
          this.showSaveErrorDialog(this.file);
          return;
        }
        this.setAfterSaveFile();
        this.setTextPrefix();
        this.setStylePrefix();
      }
    }
    this.txaMain.setFileLastModified(this.file.lastModified()); // �����ļ�����޸ĵ�ʱ���
  }

  /**
   * ���ļ��������
   * 
   * @param index
   *          �ı���Ĳ����λ��
   */
  private void setAfterOpenFile(int index) {
    this.txaMain.setCaretPosition(Util.checkCaretPosition(this.txaMain, index)); // ���ò����λ��
    this.txaMain.setTextChanged(false);
    this.txaMain.setStyleChanged(false);
    this.txaMain.setSaved(true);
    this.undoManager.discardAllEdits();
    this.txaMain.setUndoIndex(Util.DEFAULT_UNDO_INDEX); // ������ʶ���ָ�Ĭ��ֵ
    this.setMenuStateUndoRedo(); // ���ó����������˵���״̬
    this.setMenuStateBackForward(); // ���ú��˺�ǰ���˵���״̬
    this.itemReOpen.setEnabled(true);
    this.itemReName.setEnabled(true);
    this.itemDelFile.setEnabled(true);
    this.itemPopReName.setEnabled(true);
    this.itemPopDelFile.setEnabled(true);
    this.itemPopReOpen.setEnabled(true);
  }

  /**
   * "��"�Ĵ�����
   */
  private void openFile() {
    if (this.openFileChooser == null) {
      this.openFileChooser = new OpenFileChooser();
    }
    this.openFileChooser.setSelectedFile(null);
    if (JFileChooser.APPROVE_OPTION != this.openFileChooser.showOpenDialog(this)) {
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
   * ����Ƿ���Ҫ�½��ı���
   * 
   * @param file
   *          ���򿪵��ļ�
   * @return �Ƿ���Ҫ�½��ı���true��ʾ��Ҫ�½�����֮����Ҫ�½�
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
    if (this.file == null && !this.txaMain.getTextChanged() && !this.txaMain.getStyleChanged()) {
      toCreateNew = false;
    }
    return toCreateNew;
  }

  /**
   * ����´򿪵��ļ��뵱ǰ�༭���ļ���ͬ�����ȡ��ǰ�ı���Ĳ����λ�ã����򷵻�Ĭ��λ��
   * 
   * @param file
   *          �´򿪵��ļ�
   * @return ��Ҫ���õĲ����λ��
   */
  private int getCurrentIndexBySameFile(File file) {
    int index = Util.DEFAULT_CARET_INDEX;
    if (file.equals(this.file)) {
      index = this.txaMain.getCaretPosition();
    }
    return index;
  }

  /**
   * "��ָ�������"�Ĵ�����
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
    if (JFileChooser.APPROVE_OPTION != this.openFileChooser.showOpenDialog(this)) {
      return;
    }
    File file = this.openFileChooser.getSelectedFile();
    if (file != null && file.exists()) {
      boolean toCreateNew = this.checkToCreateNew(file);
      int index = this.getCurrentIndexBySameFile(file);
      if (toCreateNew) {
        this.createNew(file);
      }
      if (charEncoding != null) {
        this.setCharEncoding(charEncoding, true);
        this.toOpenFile(file, false, false);
      } else {
        this.toOpenFile(file, true, false);
      }
      this.setAfterOpenFile(index);
      this.setFileNameAndPath(file);
    }
  }

  /**
   * ���ļ�����������ʾ���ı�����
   * 
   * @param file
   *          �򿪵��ļ�
   * @param isAutoCheckEncoding
   *          �Ƿ��Զ��������ʽ
   * @param toCreateNew
   *          �Ƿ���Ҫ�½��ı���
   */
  private void toOpenFile(File file, boolean isAutoCheckEncoding, boolean toCreateNew) {
    InputStreamReader inputStreamReader = null;
    try {
      if (toCreateNew) {
        this.createNew(file);
      } else if (file != null) {
        this.txaMain.setFile(file);
      }
      if (isAutoCheckEncoding) {
        this.setCharEncoding(Util.checkFileEncoding(file), true);
      }
      String strCharset = this.txaMain.getCharEncoding().toString();
      inputStreamReader = new InputStreamReader(new FileInputStream(file), strCharset);
      char[] chrBuf = new char[Util.BUFFER_LENGTH];
      int len = 0;
      StringBuilder stbTemp = new StringBuilder();
      switch (this.txaMain.getCharEncoding()) {
      case UTF8:
      case ULE:
      case UBE:
        inputStreamReader.read(); // ȥ���ļ���ͷ��BOM
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
      } else { // ���ļ����ݲ���1��ʱ��������ΪϵͳĬ�ϵĻ��з�
        this.setLineStyleString(LineSeparator.DEFAULT, true);
      }
      this.txaMain.setText(strTemp);
      this.addFileHistoryItem(file.getCanonicalPath()); // �������༭���ļ��б�
      this.txaMain.setFileExistsLabel(true);
      this.txaMain.setFileChangedLabel(false);
      this.txaMain.setNewFileIndex(0);
      this.txaMain.setFileExt(this.getFileExtByName(file.getName()));
      this.setMenuStateComment();
      this.tpnMain.setIconAt(this.tpnMain.getSelectedIndex(), this.getTabIcon(txaMain));
    } catch (Exception x) {
      // x.printStackTrace();
    } finally {
      try {
        inputStreamReader.close();
      } catch (IOException x) {
        // x.printStackTrace();
      }
    }
  }

  /**
   * "����"�Ĵ�����
   * 
   * @param isSaveAs
   *          �Ƿ�Ϊ"���Ϊ"
   * @return �Ƿ񱣴�ɹ�������ɹ�����true�����ʧ����Ϊfalse
   */
  private boolean saveFile(boolean isSaveAs) {
    boolean isFileExist = true; // ��ǰ�ļ��Ƿ����
    if (isSaveAs || !this.txaMain.getSaved()) {
      if (this.saveFileChooser == null) {
        this.saveFileChooser = new SaveFileChooser();
      }
      if (isSaveAs) {
        this.saveFileChooser.setDialogTitle("���Ϊ");
      } else {
        this.saveFileChooser.setDialogTitle("����");
      }
      this.saveFileChooser.setSelectedFile(null);
      if (JFileChooser.APPROVE_OPTION != this.saveFileChooser.showSaveDialog(this)) {
        return false;
      }
      File file = this.saveFileChooser.getSelectedFile();
      if (file != null) {
        try {
          this.toSaveFile(file);
        } catch (Exception x) {
          // x.printStackTrace();
          this.showSaveErrorDialog(file);
          return false;
        }
        this.setFileNameAndPath(file);
      } else {
        return false;
      }
    } else {
      if (this.file != null) {
        isFileExist = Util.checkFile(this.file);
        try {
          this.toSaveFile(this.file);
        } catch (Exception x) {
          // x.printStackTrace();
          this.showSaveErrorDialog(this.file);
          return false;
        }
        this.txaMain.setFileLastModified(this.file.lastModified()); // �����ļ�����޸ĵ�ʱ���
      } else {
        return false;
      }
    }
    this.setAfterSaveFile();
    this.setTextPrefix();
    this.setStylePrefix();
    if (!isFileExist) {
      JOptionPane.showMessageDialog(this,
          Util.convertToMsg("��ʧ���ļ���" + this.file.getAbsolutePath() + "\n�����´�����"),
          Util.SOFTWARE, JOptionPane.CANCEL_OPTION);
    }
    this.txaMain.setNewFileIndex(0);
    this.tpnMain.setIconAt(this.tpnMain.getSelectedIndex(),this.getTabIcon(this.txaMain));
    return true;
  }

  /**
   * "���Ϊ"�Ĵ�����
   */
  private void saveAsFile() {
    this.saveFile(true);
  }

  /**
   * ���ı����е��ı����浽�ļ�
   * 
   * @param file
   *          ������ļ�
   */
  private void toSaveFile(File file) throws Exception {
    FileOutputStream fileOutputStream = null;
    try {
      fileOutputStream = new FileOutputStream(file);
      String strText = this.txaMain.getText();
      strText = strText.replaceAll(LineSeparator.UNIX.toString(),
          this.txaMain.getLineSeparator().toString());
      byte[] byteStr;
      int[] charBOM = new int[] { -1, -1, -1 }; // ���ݵ�ǰ���ַ����룬���BOM������
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
      this.addFileHistoryItem(file.getCanonicalPath()); // �������༭���ļ��б�
      this.txaMain.setFileExistsLabel(true);
      this.txaMain.setFileChangedLabel(false);
      this.txaMain.setFileExt(this.getFileExtByName(file.getName()));
      this.setMenuStateComment();
    } catch (Exception x) {
      // x.printStackTrace();
      throw x; // ��������쳣�׳����Ա���ô����Խ����쳣����
    } finally {
      try {
        fileOutputStream.flush();
        fileOutputStream.close();
      } catch (IOException x) {
        // x.printStackTrace();
      }
    }
  }

  /**
   * �ڱ����ļ�ʧ�ܺ󣬵�����ʾ��
   * 
   * @param file
   *          ��ǰ�༭���ļ�
   */
  private void showSaveErrorDialog(File file) {
    JOptionPane.showMessageDialog(this,
        Util.convertToMsg("�ļ���" + file.getAbsolutePath() + "\n����ʧ�ܣ���ȷ���ļ����Ƿ��зǷ��ַ����Ƿ���дȨ�ޣ�"),
        Util.SOFTWARE, JOptionPane.CANCEL_OPTION);
  }

  /**
   * �����ļ��������
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
    this.txaMain.setUndoIndex(Util.DEFAULT_UNDO_INDEX); // ������ʶ���ָ�Ĭ��ֵ
  }

  /**
   * �����ļ������ƺ�·��
   * 
   * @param file
   *          ��ǰ�༭���ļ�
   */
  private void setFileNameAndPath(File file) {
    this.txaMain.setFile(file);
    this.stbTitle = new StringBuilder(Util.SOFTWARE);
    if (file != null && file.exists()) {
      this.file = this.txaMain.getFile();
      this.stbTitle.insert(0, this.file.getAbsolutePath() + " - ");
      this.txaMain.setFileLastModified(file.lastModified()); // �����ļ�����޸ĵ�ʱ���
    }
    this.setTitle(this.stbTitle.toString());
    this.tpnMain.setTitleAt(this.tpnMain.getSelectedIndex(), this.txaMain.getTitle());
  }

  /**
   * ���ñ���������ʾ
   */
  private void setAutoTitle() {
    this.stbTitle = new StringBuilder(Util.SOFTWARE);
    this.file = this.txaMain.getFile();
    if (this.file != null) {
      this.stbTitle.insert(0, this.txaMain.getPrefix() + this.file.getAbsolutePath() + " - ");
    } else {
      this.stbTitle.insert(0, this.txaMain.getPrefix() + this.txaMain.getTitle() + " - ");
    }
    this.setTitle(this.stbTitle.toString());
  }

  /**
   * ����ĳЩ�˵�����ʾ״̬
   * 
   * @param enable
   *          �˵�����ʾ״̬��true��ʾ��ʾ����֮��ʾ����ʾ
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
   * �������ø��Ի������ڵ��ı���
   */
  private void setTextAreaInDialogs() {
    if (this.findReplaceDialog != null) {
      this.findReplaceDialog.setTextArea(this.txaMain);
    }
    if (this.insertCharDialog != null) {
      this.insertCharDialog.setTextArea(this.txaMain);
    }
    if (this.insertDateDialog != null) {
      this.insertDateDialog.setTextArea(this.txaMain);
    }
  }

  /**
   * ����״̬�����ı�������
   */
  private void updateStateAll() {
    String strStateChars = Util.STATE_CHARS + this.txaMain.getText().length();
    String strStateLines = Util.STATE_LINES + this.txaMain.getLineCount();
    this.pnlState.setStringByIndex(0, strStateChars + ", " + strStateLines);
  }

  /**
   * ����״̬����ǰ������ڵ������������뵱ǰѡ����ַ���
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
    this.pnlState.setStringByIndex(1, strStateCurLn + ", " + strStateCurCol + ", " + strStateCurSel);
  }

  /**
   * ����״̬����ǰ�Ļ��з���ʽ
   */
  private void updateStateLineStyle() {
    this.pnlState.setStringByIndex(2, Util.STATE_LINE_STYLE
        + this.txaMain.getLineSeparator().getName());
  }

  /**
   * ����״̬����ǰ���ַ������ʽ
   */
  private void updateStateEncoding() {
    this.pnlState.setStringByIndex(3, Util.STATE_ENCODING
        + this.txaMain.getCharEncoding().getName());
  }

  /**
   * ���ҵ�ǰ��괦���ŵ�ƥ���������ڵ�����ֵ
   * 
   * @param charCurrent
   *          ��ǰ��괦���ַ�
   * @param currentIndex
   *          ��ǰ������������ֵ
   * @param strMain
   *          ��ǰ�ı�����ı�
   * @return ƥ�����ŵ�����ֵ�����δ�ҵ�ƥ�����ţ��򷵻�-1
   */
  private int getBracketTargetIndex(char charCurrent, int currentIndex,
      String strMain) {
    int targetIndex = -1; // ���ҵ���ƥ���������ڵ�����ֵ
    if (charCurrent == ' ') {
      return targetIndex;
    }
    int style = -1; // ��ƥ�����ŵ����ͣ�Ŀǰ��Ҫ�м������ţ�()[]{}<>
    boolean isLeft = true; // ��ǰ��괦���ַ��Ƿ�����ߵ�����
    char charTarget = ' ';
    style = Util.BRACKETS_LEFT.indexOf(charCurrent);
    if (style >= 0) {
      charTarget = Util.BRACKETS_RIGHT.charAt(style);
    } else {
      style = Util.BRACKETS_RIGHT.indexOf(charCurrent);
      if (style >= 0) {
        isLeft = false;
        charTarget = Util.BRACKETS_LEFT.charAt(style);
      }
    }
    if (style < 0) { // ������������������ţ��򷵻�
      return targetIndex;
    }
    String tempText = strMain.substring(currentIndex);
    if (isLeft) {
      targetIndex = tempText.indexOf(charTarget);
      if (targetIndex >= 0) {
        targetIndex += currentIndex;
      }
    } else {
      tempText = strMain.substring(0, currentIndex + 1);
      targetIndex = tempText.lastIndexOf(charTarget);
    }
    if (targetIndex < 0) { // �����1��û���ҵ�ƥ������ţ��򷵻�
      return targetIndex;
    }
    int timesLeft = 0; // �����ų��ִ���
    int timesRight = 0; // �����ų��ִ���
    int index = 0; // ��ʱ����ֵ
    boolean label = false; // �Ƿ���ҵ�ƥ�������
    if (isLeft) {
      for (index = 0; index < tempText.length(); index++) {
        char charTemp = tempText.charAt(index);
        if (charTemp == charCurrent) {
          timesLeft++;
        } else if (charTemp == charTarget) {
          timesRight++;
          if (timesLeft == timesRight) {
            label = true;
            break;
          }
        }
      }
      if (label) {
        targetIndex = currentIndex + index;
      }
    } else {
      for (index = tempText.length() - 1; index >= 0; index--) {
        char charTemp = tempText.charAt(index);
        if (charTemp == charCurrent) {
          timesRight++;
        } else if (charTemp == charTarget) {
          timesLeft++;
          if (timesLeft == timesRight) {
            label = true;
            break;
          }
        }
      }
      if (label) {
        targetIndex = index;
      }
    }
    if (!label) {
      targetIndex = -1;
    } else {
      this.targetBracketIndex = targetIndex + 1;
    }
    return targetIndex;
  }

  /**
   * ���ҵ�ǰ��괦�����ŵ�ƥ�����ţ������и�����ʾ
   */
  private void searchTargetBracket() {
    this.rmHighlight(Util.BRACKET_COLOR_STYLE); // ȡ����һ�ε�����ƥ�����
    String strMain = this.txaMain.getText();
    char charLeft = ' '; // ��ǰ��������ַ�
    char charRight = ' '; // ��ǰ����Ҳ���ַ�
    int currentIndex = this.txaMain.getCaretPosition(); // ��Ҫƥ������ŵ�����ֵ
    currentIndex--;
    int targetIndex = -1; // ���ҵ���ƥ���������ڵ�����ֵ
    this.targetBracketIndex = targetIndex;
    if (currentIndex >= 0) {
      charLeft = strMain.charAt(currentIndex);
    }
    if (currentIndex < strMain.length() - 1) {
      charRight = strMain.charAt(currentIndex + 1);
    }
    targetIndex = this.getBracketTargetIndex(charLeft, currentIndex, strMain);
    if (targetIndex < 0) {
      targetIndex = this.getBracketTargetIndex(charRight, ++currentIndex, strMain);
      if (targetIndex < 0) {
        this.itemFindBracket.setEnabled(false);
        return;
      }
    }
    this.itemFindBracket.setEnabled(true);
    try {
      this.txaMain.getHighlighter().addHighlight(currentIndex, currentIndex + 1,
          new DefaultHighlighter.DefaultHighlightPainter(this.setting.colorStyle[5]));
      Highlighter.Highlight[] arrHighlight = this.txaMain.getHighlighter().getHighlights();
      this.txaMain.getHighlighterList().add(new PartnerBean(arrHighlight[arrHighlight.length - 1],
              Util.BRACKET_COLOR_STYLE));
      this.txaMain.getHighlighter().addHighlight(targetIndex, targetIndex + 1,
          new DefaultHighlighter.DefaultHighlightPainter(this.setting.colorStyle[5]));
      arrHighlight = this.txaMain.getHighlighter().getHighlights();
      this.txaMain.getHighlighterList().add(new PartnerBean(arrHighlight[arrHighlight.length - 1],
              Util.BRACKET_COLOR_STYLE));
    } catch (BadLocationException x) {
      // x.printStackTrace();
    }
  }

  /**
   * ˢ�¹����ʷλ�õ�����
   */
  private void refreshBackForwardList() {
    CurrentLine currentLine = new CurrentLine(this.txaMain);
    int currentIndex = currentLine.getCurrentIndex();
    int lineNum = currentLine.getLineNum();
    LinkedList<PartnerBean> backForwardList = this.txaMain.getBackForwardList();
    int backForwardIndex = this.txaMain.getBackForwardIndex();
    int size = backForwardList.size();
    boolean exist = false;
    for (int i = 0; i < size; i++) {
      PartnerBean bean = backForwardList.get(i);
      if (bean.getIndex() == lineNum) {
        bean.setObject(currentIndex);
        if (backForwardIndex == Util.DEFAULT_BACK_FORWARD_INDEX) {
          backForwardList.remove(i);
          backForwardList.addFirst(bean);
        }
        exist = true;
        break;
      }
    }
    if (!exist) {
      // ��������Ѻ��˵�״̬�£������ǰ��������в�����ʷ��¼��򽫺��˵���ʷλ�ý��з�ת��
      if (backForwardIndex != Util.DEFAULT_BACK_FORWARD_INDEX) {
        for (int i = 0; i <= backForwardIndex; i++) {
          PartnerBean bean = backForwardList.remove(i);
          backForwardList.addFirst(bean);
        }
        this.txaMain.setBackForwardIndex(Util.DEFAULT_BACK_FORWARD_INDEX);
      }
      if (size >= Util.BACK_FORWARD_MAX) {
        backForwardList.removeLast();
      }
      backForwardList.addFirst(new PartnerBean(currentIndex, lineNum));
    }
    this.setMenuStateBackForward();
  }

  /**
   * ˢ�´������༭���ı���hashCode������
   */
  private void refreshTextAreaHashCodeList() {
    int hashCode = this.txaMain.hashCode();
    int size = this.textAreaHashCodeList.size();
    if (size > 0) {
      // ��������Ѻ��˵�״̬�£��򲻴���
      int value = this.textAreaHashCodeList.get(this.textAreaHistoryIndex);
      if (value == hashCode) {
        return;
      }
      // �����ǰ�ı����Ѵ�������ͷ���򲻴���
      if (this.textAreaHashCodeList.get(0) == hashCode) {
        return;
      }
    }
    this.textAreaHashCodeList.addFirst(hashCode);
    size++;
    if (size > Util.TEXTAREA_HASHCODE_LIST_MAX) {
      this.textAreaHashCodeList.removeLast();
    }
  }

  /**
   * ����ǰ�ı����hashCode��������ɾ��
   */
  private void removeFromTextAreaHashCodeList() {
    int hashCode = this.txaMain.hashCode();
    int size = this.textAreaHashCodeList.size();
    for (int i = size - 1; i >= 0; i--) {
      int value = this.textAreaHashCodeList.get(i);
      if (value == hashCode) {
        this.textAreaHashCodeList.remove(i);
        this.checkTextAreaHistoryIndex(i);
      }
    }
    this.checkTextAreaHashCodeList();
  }

  /**
   * ��鲢ɾ���������������ظ�hashCode
   */
  private void checkTextAreaHashCodeList() {
    int size = this.textAreaHashCodeList.size();
    if (size <= 1) {
      return;
    }
    int tempValue = this.textAreaHashCodeList.get(size - 1);
    for (int i = size - 2; i >= 0; i--) {
      int value = this.textAreaHashCodeList.get(i);
      if (value == tempValue) {
        this.textAreaHashCodeList.remove(i);
        this.checkTextAreaHistoryIndex(i);
      } else {
        tempValue = value;
      }
    }
  }

  /**
   * ��鲢��������༭���ı��������ֵ
   * 
   * @param index
   *          ��ɾ����Ԫ������ֵ
   */
  private void checkTextAreaHistoryIndex(int index) {
    // ��ɾ����Ԫ������ֵС�ڵ�������༭������ֵ�����������ֵ
    if (this.textAreaHistoryIndex > 0 && this.textAreaHistoryIndex >= index) {
      this.textAreaHistoryIndex--;
    }
  }

  /**
   * ���������е������ǲ����ı���������ı�����ʹճ���˵����ã���֮�������á�
   */
  private void checkClipboard() {
    try {
      Transferable tf = this.clip.getContents(this);
      if (tf == null) {
        this.itemPaste.setEnabled(false);
        this.itemPopPaste.setEnabled(false);
        this.toolButtonList.get(8).setEnabled(false);
      } else {
        String str = tf.getTransferData(DataFlavor.stringFlavor).toString(); // ����������е����ݲ����ı������׳��쳣
        if (!Util.isTextEmpty(str)) {
          this.itemPaste.setEnabled(true);
          this.itemPopPaste.setEnabled(true);
          this.toolButtonList.get(8).setEnabled(true);
        }
      }
    } catch (Exception x) {
      // �������쳣
      // x.printStackTrace();
      this.itemPaste.setEnabled(false);
      this.itemPopPaste.setEnabled(false);
      this.toolButtonList.get(8).setEnabled(false);
    }
  }

  /**
   * ��������ļ���״̬������ļ������ڻ����������޸��򵯳���ʾ
   */
  private void checkFiles() {
    for (int i = 0; i < this.textAreaList.size(); i++) { // ѭ����������ļ���״̬������ļ������ڻ����������޸��򵯳���ʾ
      BaseTextArea txaTemp = this.textAreaList.get(i);
      File fileTemp = txaTemp.getFile();
      if (fileTemp == null) {
        continue;
      }
      if (!fileTemp.exists()) {
        if (txaTemp.getFileExistsLabel()) {
          this.tpnMain.setSelectedIndex(i);
        } else {
          continue; // ����û���ѡ����ˡ����������������һ���ļ��ļ��
        }
        int result = JOptionPane.showOptionDialog(this,
            Util.convertToMsg("�ļ���" + this.file + "�����ڡ�"),
            Util.SOFTWARE, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE,
            null, new String[] {"�ؽ�", "�Ƴ�", "����"}, null);
        if (result == JOptionPane.YES_OPTION) {
          Util.checkFile(this.file);
          try {
            this.toSaveFile(this.file);
          } catch (Exception x) {
            // x.printStackTrace();
            this.showSaveErrorDialog(this.file);
            this.tpnMain.setIconAt(i, this.getTabIcon(txaTemp));
            continue; // �����������쳣���������һ���ļ��ļ��
          }
          this.setAfterSaveFile();
          this.setTextPrefix();
          this.setStylePrefix();
        } else if (result == JOptionPane.NO_OPTION) {
          this.closeFile(false);
        } else {
          this.txaMain.setFileExistsLabel(false);
          this.tpnMain.setIconAt(i, this.getTabIcon(txaTemp));
        }
      } else {
        txaTemp.setFileExistsLabel(true);
        this.tpnMain.setIconAt(i, this.getTabIcon(txaTemp));
        if (!txaTemp.getFileChangedLabel() && txaTemp.getFileLastModified() != fileTemp.lastModified()) {
          this.tpnMain.setSelectedIndex(i);
          String strMsg = "�ļ���" + this.file + "�������ѱ����������޸ġ�\nҪ���¼�����";
          if (txaTemp.getFrozen()) {
            strMsg = "�ļ���" + this.file + "�������ѱ����������޸ġ�\nҪ������ᣬ���¼�����";
          }
          int result = JOptionPane.showConfirmDialog(this,
              Util.convertToMsg(strMsg), Util.SOFTWARE, JOptionPane.YES_NO_CANCEL_OPTION);
          if (result == JOptionPane.YES_OPTION) {
            if (txaTemp.getFrozen()) {
              this.itemFrozenFile.setSelected(false);
              this.frozenFile();
            }
            this.reOpenFile();
          } else {
            txaTemp.setFileChangedLabel(true);
          }
        }
      }
    }
  }

  /**
   * ���ı����еĹ��仯ʱ�����������¼�
   */
  public void caretUpdate(CaretEvent e) {
    this.txaMain.repaint(); // �ػ浱ǰ�ı����Խ�����ض�����»��Ƶ�ǰ�б������ҵ�����
    this.updateStateCur();
    this.setMenuStateBySelectedText();
    this.searchTargetBracket();
    this.refreshBackForwardList();
  }

  /**
   * ���ı����е��ı������仯ʱ�����������¼�
   */
  public void undoableEditHappened(UndoableEditEvent e) {
    this.undoManager.addEdit(e.getEdit());
    this.txaMain.setUndoIndex(this.txaMain.getUndoIndex() + 1); // ������ʶ������
    this.setMenuStateUndoRedo(); // ���ó����������˵���״̬
    this.setMenuStateByTextArea();
    this.txaMain.setTextChanged(true);
    this.updateStateAll();
    this.setTextPrefix();
  }

  /**
   * �������ڻ�ý���ʱ�����������¼�
   */
  public void windowGainedFocus(WindowEvent e) {
    this.checkClipboard();
    if (checking) {
      return;
    }
    checking = true;
    this.checkFiles();
    checking = false;
  }

  /**
   * ��������ʧȥ����ʱ�����������¼�
   */
  public void windowLostFocus(WindowEvent e) {

  }

  /**
   * �����������״̬�仯ʱ�����������¼�
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
        this.setMenuStateBackForward();
        this.setMenuStateComment();
        this.setMenuStateByTextArea();
        this.setMenuStateBySelectedText();
        this.setMenuStateFrozen();
        this.refreshTextAreaHashCodeList();
      }
    }
  }

  /**
   * ���û��϶���꣬�����뵽�ɷ��õ�����ʱ�����ô˷�����
   */
  public void dragEnter(DropTargetDragEvent e) {
    e.acceptDrag(DnDConstants.ACTION_COPY_OR_MOVE); // ʹ�á��������ƶ�����ʽ�����϶�����
  }

  /**
   * ���û��϶���꣬���ӿɷ��õ������Ƴ�ʱ�����ô˷�����
   */
  public void dragExit(DropTargetEvent e) {
  }

  /**
   * ���û��϶���꣬���ڿɷ��õ��������ƶ�ʱ�����ô˷�����
   */
  public void dragOver(DropTargetDragEvent e) {
  }

  /**
   * ���û��޸��˵�ǰ���ò����󣬵��ô˷�����
   */
  public void dropActionChanged(DropTargetDragEvent e) {
  }

  /**
   * ���û��϶���꣬���ڿɷ��õ������ڷ���ʱ�����ô˷�����
   */
  public synchronized void drop(DropTargetDropEvent e) {
    try {
      Transferable tr = e.getTransferable();
      if (tr.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) { // ���Transferable����֧���Ϸţ�����д���
        e.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE); // ʹ�á��������ƶ�����ʽ���շ��ò���
        List fileList = (List) tr.getTransferData(DataFlavor.javaFileListFlavor); // ��Transferable�����л�ȡ�ļ��б�
        Iterator iterator = fileList.iterator(); // ��ȡ�ļ��б�ĵ�����
        while (iterator.hasNext()) {
          File file = (File) iterator.next();
          if (file != null && file.exists()) {
            boolean toCreateNew = this.checkToCreateNew(file);
            if (!toCreateNew && !this.saveFileBeforeAct()) {
              break;
            }
            int index = this.getCurrentIndexBySameFile(file);
            this.toOpenFile(file, true, toCreateNew);
            this.setAfterOpenFile(index);
            this.setFileNameAndPath(file);
          }
        }
        e.getDropTargetContext().dropComplete(true); // ���÷��ò����ɹ�����
      } else {
        e.rejectDrop(); // ���Transferable����֧���Ϸţ���ܾ�����
      }
    } catch (Exception x) {
      // x.printStackTrace();
      e.rejectDrop(); // ������ù����г����쳣����ܾ�����
    }
  }

  /**
   * ���ڴ�С����ʱ�����ô˷�����
   */
  public void componentResized(ComponentEvent e) {
    int width = this.getWidth();
    int height = this.getHeight();
    int state = this.getExtendedState();
    if (state == MAXIMIZED_BOTH) {
      width = 0;
      height = 0;
    } else if (state == MAXIMIZED_HORIZ) {
      width = 0;
    } else if (state == MAXIMIZED_VERT) {
      height = 0;
    }
    if (this.setting.viewFrameSize == null) {
      this.setting.viewFrameSize = new int[] { width, height };
    } else {
      this.setting.viewFrameSize[0] = width;
      this.setting.viewFrameSize[1] = height;
    }
  }

  /**
   * ����λ�ø���ʱ�����ô˷�����
   */
  public void componentMoved(ComponentEvent e) {
  }

  /**
   * ���ڱ�ÿɼ�ʱ�����ô˷�����
   */
  public void componentShown(ComponentEvent e) {
  }

  /**
   * ���ڱ�ò��ɼ�ʱ�����ô˷�����
   */
  public void componentHidden(ComponentEvent e) {
  }

}
