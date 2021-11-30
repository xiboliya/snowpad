/**
 * Copyright (C) 2015 ��ԭ
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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * "��ݼ�����"�Ի���
 * 
 * @author ��ԭ
 * 
 */
public class ShortcutManageDialog extends BaseDialog implements ActionListener {
  private static final long serialVersionUID = 1L;
  private Setting setting = null; // �������������
  private SettingAdapter settingAdapter = null; // ���ڽ����ͱ�����������ļ��Ĺ�����
  private JPanel pnlMain = (JPanel) this.getContentPane();
  private JPanel pnlLeft = new JPanel(new BorderLayout());
  private JPanel pnlRight = new JPanel(null);
  private JTable tabMain = null; // ��ʾ���ݵı�����
  private JScrollPane spnMain = null;
  private JButton btnEdit = new JButton("�༭(E)");
  private JButton btnRemove = new JButton("���(D)");
  private JButton btnKeyCheck = new JButton("�������(K)");
  private JButton btnReset = new JButton("�ָ�Ĭ��(R)");
  private JButton btnCancel = new JButton("�ر�");
  private BaseKeyAdapter keyAdapter = new BaseKeyAdapter(this);
  private BaseKeyAdapter buttonKeyAdapter = new BaseKeyAdapter(this, false);
  private Vector<Vector<String>> cells = new Vector<Vector<String>>();
  private Vector<String> cellsTitle = new Vector<String>();
  private BaseDefaultTableModel baseDefaultTableModel = null;
  private ShortcutSetDialog shortcutSetDialog = null;
  private KeyCheckDialog keyCheckDialog = null;
  private boolean enabled = true; // ���ڱ�ʶ"�༭"��"���"��ť�Ƿ����

  /**
   * ���췽��
   * 
   * @param owner
   *          ������ʾ�öԻ���ĸ����
   * @param modal
   *          �Ƿ�Ϊģʽ�Ի���
   * @param txaSource
   *          ��Բ������ı���
   */
  public ShortcutManageDialog(JFrame owner, boolean modal, JTextArea txaSource, Setting setting, SettingAdapter settingAdapter) {
    super(owner, modal);
    if (txaSource == null) {
      return;
    }
    this.setting = setting;
    this.settingAdapter = settingAdapter;
    this.txaSource = txaSource;
    this.init();
    this.setMnemonic();
    this.addTable();
    this.refresh();
    this.addListeners();
    this.setSize(520, 275);
    this.setMinimumSize(new Dimension(550, 275)); // ���ñ����ڵ���С�ߴ�
    this.setResizable(true);
    this.setVisible(true);
  }

  /**
   * ��ʼ������
   */
  private void init() {
    this.setTitle("��ݼ�����");
    this.pnlMain.add(this.pnlLeft, BorderLayout.CENTER);
    this.pnlMain.add(this.pnlRight, BorderLayout.EAST);
    this.btnEdit.setBounds(10, 20, 120, Util.BUTTON_HEIGHT);
    this.btnRemove.setBounds(10, 55, 120, Util.BUTTON_HEIGHT);
    this.btnKeyCheck.setBounds(10, 105, 120, Util.BUTTON_HEIGHT);
    this.btnReset.setBounds(10, 155, 120, Util.BUTTON_HEIGHT);
    this.btnCancel.setBounds(10, 205, 120, Util.BUTTON_HEIGHT);
    this.pnlRight.setPreferredSize(new Dimension(140, 275)); // �����������ʳߴ�
    this.pnlRight.add(this.btnEdit);
    this.pnlRight.add(this.btnRemove);
    this.pnlRight.add(this.btnKeyCheck);
    this.pnlRight.add(this.btnReset);
    this.pnlRight.add(this.btnCancel);
  }

  /**
   * Ϊ������������Ƿ�
   */
  private void setMnemonic() {
    this.btnEdit.setMnemonic('E');
    this.btnRemove.setMnemonic('D');
    this.btnKeyCheck.setMnemonic('K');
    this.btnReset.setMnemonic('R');
  }

  /**
   * ���������ӱ����ͼ
   */
  private void addTable() {
    for (String title : Util.SHORTCUT_MANAGE_TABLE_TITLE_TEXTS) {
      this.cellsTitle.add(title);
    }
    this.baseDefaultTableModel = new BaseDefaultTableModel(this.cells, this.cellsTitle);
    this.tabMain = new JTable(this.baseDefaultTableModel);
    this.tabMain.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    this.tabMain.getTableHeader().setReorderingAllowed(false); // ���������ƶ�
    this.spnMain = new JScrollPane(this.tabMain);
    this.pnlLeft.add(this.spnMain, BorderLayout.CENTER);
    this.tabMain.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
      public void valueChanged(ListSelectionEvent e) {
        int index = tabMain.getSelectedRow();
        if (index < 0) {
          return;
        }
        String strName = tabMain.getValueAt(index, 0).toString();
        enabled = true;
        for (String str : Util.CAN_NOT_MODIFIED_SHORTCUT_NAMES) {
          if (str.equalsIgnoreCase(strName)) {
            enabled = false;
            break;
          }
        }
        btnEdit.setEnabled(enabled);
        btnRemove.setEnabled(enabled);
      }
    });
  }

  /**
   * ��ȡ������
   */
  private void getCells() {
    this.cells.clear();
    Vector<String> cellsLine = null;
    int length = Util.SHORTCUT_NAMES.length;
    for (int i = 0; i < length; i++) {
      cellsLine = new Vector<String>();
      String name = Util.SHORTCUT_NAMES[i];
      cellsLine.add(name);
      cellsLine.add(Util.transferShortcut(this.setting.shortcutMap.get(name)));
      this.cells.add(cellsLine);
    }
  }

  /**
   * ˢ�±���е�����
   */
  public void refresh() {
    this.getCells();
    this.tabMain.updateUI();
    this.tabMain.setRowSelectionInterval(0, 0); // �Զ�ѡ�е�ǰ������ļ���
  }

  /**
   * ��Ӻͳ�ʼ���¼�������
   */
  private void addListeners() {
    this.btnEdit.addActionListener(this);
    this.btnRemove.addActionListener(this);
    this.btnKeyCheck.addActionListener(this);
    this.btnReset.addActionListener(this);
    this.btnCancel.addActionListener(this);
    this.btnEdit.addKeyListener(this.buttonKeyAdapter);
    this.btnRemove.addKeyListener(this.buttonKeyAdapter);
    this.btnKeyCheck.addKeyListener(this.buttonKeyAdapter);
    this.btnReset.addKeyListener(this.buttonKeyAdapter);
    this.btnCancel.addKeyListener(this.buttonKeyAdapter);
    this.tabMain.addKeyListener(this.keyAdapter);
  }

  /**
   * Ϊ���������¼��Ĵ�����
   */
  public void actionPerformed(ActionEvent e) {
    if (this.btnEdit.equals(e.getSource())) {
      this.onEnter();
    } else if (this.btnRemove.equals(e.getSource())) {
      this.removeShortcut();
    } else if (this.btnKeyCheck.equals(e.getSource())) {
      this.keyCheck();
    } else if (this.btnReset.equals(e.getSource())) {
      this.resetShortcuts();
    } else if (this.btnCancel.equals(e.getSource())) {
      this.onCancel();
    }
  }

  /**
   * "���"�Ĳ�������
   */
  private void removeShortcut() {
    int index = this.tabMain.getSelectedRow();
    this.setting.shortcutMap.put(this.tabMain.getValueAt(index, 0).toString(), "");
    this.tabMain.setValueAt("", index, 1);
    ((SnowPadFrame) this.getOwner()).shortcutManageToSetMenuAccelerator(index);
  }

  /**
   * "�������"�Ĳ�������
   */
  private void keyCheck() {
    if (this.keyCheckDialog == null) {
      this.keyCheckDialog = new KeyCheckDialog(this, true);
    } else {
      this.keyCheckDialog.setVisible(true);
    }
  }

  /**
   * "�ָ�Ĭ��"�Ĳ�������
   */
  private void resetShortcuts() {
    int result = JOptionPane.showConfirmDialog(this, 
        "�˲������ָ����еĿ�ݼ����ã�\n�Ƿ������",
        Util.SOFTWARE, JOptionPane.YES_NO_OPTION);
    if (result != JOptionPane.YES_OPTION) {
      return;
    }
    this.settingAdapter.initShortcuts();
    this.refresh();
    ((SnowPadFrame) this.getOwner()).setMenuAccelerator();
  }

  /**
   * Ĭ�ϵ�"ȷ��"��������
   */
  public void onEnter() {
    int index = this.tabMain.getSelectedRow();
    String keyName = this.tabMain.getValueAt(index, 0).toString();
    if (this.shortcutSetDialog == null) {
      this.shortcutSetDialog = new ShortcutSetDialog(this, true, this.setting, keyName);
    } else {
      this.shortcutSetDialog.setKeyName(keyName);
      this.shortcutSetDialog.setVisible(true);
    }
    if (!this.shortcutSetDialog.getOk()) {
      return;
    }
    this.tabMain.setValueAt(Util.transferShortcut(this.setting.shortcutMap.get(keyName).toString()), index, 1);
    ((SnowPadFrame) this.getOwner()).shortcutManageToSetMenuAccelerator(index);
  }

  /**
   * Ĭ�ϵ�"ȡ��"��������
   */
  public void onCancel() {
    this.dispose();
  }

}
