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

import java.awt.Color;
import java.awt.Font;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.JTextComponent;

/**
 * ʵ�ù����࣬���������õĸ������Ժͷ��������Ϊfinal���ͣ�ʹ���಻�ɱ��̳�
 * 
 * @author ��ԭ
 * 
 */
public final class Util {
  public static final String SOFTWARE = "��ѩ���±�"; // �������
  public static final String VERSION = "V4.3"; // ����汾��
  public static final String NEW_FILE_NAME = "�½�"; // �½��ļ���Ĭ������
  public static final String HELP_TITLE = "��������"; // ������������Ĭ�ϱ���
  public static final String OS_NAME = System.getProperty("os.name", "Windows"); // ��ǰ����ϵͳ������
  public static final String FILE_SEPARATOR = System.getProperty("file.separator", "/"); // ��ǰ����ϵͳ���ļ��ָ���
  public static final String LINE_SEPARATOR = System.getProperty("line.separator", "\n"); // ��ǰ����ϵͳ���зָ���
  public static final String SETTING_XML = "snowpad.xml"; // ��������������õ������ļ���
  public static final String FILE_HISTORY = "FileHistory"; // ���ڱ�ʶ����༭���ļ�
  public static final String LOOK_AND_FEEL = "LookAndFeel"; // ���ڱ�ʶ��ǰϵͳ���õ����
  public static final String PARAM_SPLIT = "#"; // ���ڷָ�ǰ���ı���ʶ���ַ���
  public static final String INSERT_SPECIAL = "�~�����I�Z�d�e�Y������������������������嗀�����������C���n�j�ߣ��l�h�m�i�������������������������I�J�L�K�ΨO���ܨM���w�y�z�{�|�}�~�����������������x�����שߩϩǩ�������������u�v�r�q�s�t"; // �������
  public static final String INSERT_PUNCTUATION = "���������������t?���U�á��E�䣧��F������~�����o�p.�q�r�s���u������������񡲡���㡾�������ۣݡ�����硴����塸����顺�����v�w�x�y�z�{�������������A�@�졩"; // ������
  public static final String INSERT_MATH = "�֡ԡ٣��P�ܡݨQ�R�����ڡۡ��������£��ҡӡءޡġšơǡȡɡʡߡ�ˡ͡ΡϡСѡաס̩����������N�S�ШG�|��#���}&����S�R"; // ��ѧ����
  public static final String INSERT_UNIT = "����磤����꣥��H��$��������T�L�M�N�Q�O�J�K�P��"; // ��λ����
  public static final String INSERT_DIGIT = "�����������������������������������¢âĢŢƢǢȢɢʢˢ̢͢΢ϢТѢҢӢԢբ֢עآ٢ڢۢܢݢޢߢ���������������������������������������������������"; // ���ַ���
  public static final String INSERT_PINYIN = "�����������������������������������������������������������������ŨƨǨȨɨʨ˨̨ͨΨϨШѨҨӨԨը֨רب٨ڨۨܨݨިߨ����������"; // ƴ������
  public static final String CALCULATOR_ITEM = "C��%��789��456��123����0.="; // ��������ť
  public static final String BRACKETS_LEFT = "([{<"; // ���ı����п��Խ��и���ƥ���������
  public static final String BRACKETS_RIGHT = ")]}>"; // ���ı����п��Խ��и���ƥ���������
  public static final String AUTO_COMPLETE_BRACKETS_LEFT = "([{<'\""; // ���ı����п����Զ���ɵ������
  public static final String AUTO_COMPLETE_BRACKETS_RIGHT = ")]}>'\""; // ���ı����п����Զ���ɵ��ҷ���
  public static final String PATTERN_META_CHARACTER = "$()*+.?[^{|"; // ������ʽԪ�ַ�
  public static final String CTRL = "Ctrl"; // Ctrl��������
  public static final String SHIFT = "Shift"; // Shift��������
  public static final String ALT = "Alt"; // Alt��������
  public static final String KEY_UNDEFINED = "[δ����]"; // δ���尴��
  public static final String CTRL_C = "Ctrl+C"; // ��ϼ�Ctrl+C���ַ���
  public static final String CTRL_H = "Ctrl+H"; // ��ϼ�Ctrl+H���ַ���
  public static final String CTRL_V = "Ctrl+V"; // ��ϼ�Ctrl+V���ַ���
  public static final String CTRL_X = "Ctrl+X"; // ��ϼ�Ctrl+X���ַ���
  public static final String CTRL_Y = "Ctrl+Y"; // ��ϼ�Ctrl+Y���ַ���
  public static final String CTRL_Z = "Ctrl+Z"; // ��ϼ�Ctrl+Z���ַ���
  public static final String TEXT_PREFIX = "*"; // �ļ��ı��޸ĵı�������ʶ��
  public static final String STYLE_PREFIX = "��"; // �ļ���ʽ�޸ĵı�������ʶ��
  public static final String STATE_CHARS = "Chars:"; // ״̬����ʾ��Ϣ-�ı����ַ���
  public static final String STATE_LINES = "Lines:"; // ״̬����ʾ��Ϣ-�ı�������
  public static final String STATE_CUR_LINE = "Ln:"; // ״̬����ʾ��Ϣ-��굱ǰ�к�
  public static final String STATE_CUR_COLUMN = "Col:"; // ״̬����ʾ��Ϣ-��굱ǰ�к�
  public static final String STATE_CUR_SELECT = "Sel:"; // ״̬����ʾ��Ϣ-��ǰѡ����ַ���
  public static final String STATE_LINE_STYLE = "LineStyle:"; // ״̬����ʾ��Ϣ-��ǰ���з���ʽ
  public static final String STATE_ENCODING = "Encoding:"; // ״̬����ʾ��Ϣ-��ǰ�����ʽ
  public static final String SIGN_CHARS_VIEW = "__________\n__________\n__________"; // �б�������Ŵ��ڵ�Ԥ������ĳ�ʼ���ַ���
  public static final String SIGN_CHARS = "�|���������ѡ��������������������"; // �б����
  public static final String IDENTIFIER_CHARS = "0123"; // �б������ͱ�ʶ��
  public static final String IDENTIFIER_TIANGAN = "���ұ����켺�����ɹ�"; // ʮ���
  public static final String IDENTIFIER_DIZHI = "�ӳ���î������δ�����纥"; // ʮ����֧
  public static final String INFO_FILE_PATH = "�ļ�·����"; // ͳ����Ϣ������ʹ�õ��ַ���
  public static final String INFO_FILE_MODIFY_TIME = "�޸�ʱ�䣺"; // ͳ����Ϣ������ʹ�õ��ַ���
  public static final String INFO_FILE_SIZE = "�ļ���С��"; // ͳ����Ϣ������ʹ�õ��ַ���
  public static final String INFO_DOC_CHARS = "��������"; // ͳ����Ϣ������ʹ�õ��ַ���
  public static final String INFO_DOC_LINES = "��������"; // ͳ����Ϣ������ʹ�õ��ַ���
  public static final String INFO_DOC_DIGITS = "��������"; // ͳ����Ϣ������ʹ�õ��ַ���
  public static final String INFO_DOC_LETTERS = "��ĸ����"; // ͳ����Ϣ������ʹ�õ��ַ���
  public static final String INFO_DOC_BLANKS = "�ո�����"; // ͳ����Ϣ������ʹ�õ��ַ���
  public static final String SYSTEM_LOOK_AND_FEEL_CLASS_NAME = UIManager.getSystemLookAndFeelClassName(); // ��ǰϵͳĬ����۵���������
  public static final String[] FONT_FAMILY_NAMES = java.awt.GraphicsEnvironment
      .getLocalGraphicsEnvironment().getAvailableFontFamilyNames(); // ��ȡϵͳ��������������б�
  public static final String[] FILE_ENCODINGS = new String[] { "�Զ����",
      CharEncoding.ANSI.getName(), CharEncoding.UBE.getName(),
      CharEncoding.ULE.getName(), CharEncoding.UTF8.getName(),
      CharEncoding.UTF8_NO_BOM.getName(), CharEncoding.BASE.getName() }; // ѡ������ʽ������
  public static final String[] DATE_STYLES = new String[] { "yyyy-MM-dd",
      "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm:ss:SSS", "yyyy-MM-dd KK:mm:ss",
      "yyyy-MM-dd KK:mm:ss a", "yyyy-MM-dd HH:mm:ss E",
      "yyyy-MM-dd HH:mm:ss zZ", "yyyy��MM��dd�� HHʱmm��ss��",
      "G yyyy-MM-dd HH:mm:ss E zZ", "yy-M-d H:m:s", "yyyy/MM/dd HH:mm:ss",
      "yyyy.MM.dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM/dd", "yyyy.MM.dd",
      "yy/MM/dd", "HH:mm:ss", "KK:mm:ss a", "HH:mm:ss:SSS" }; // ʱ��/���ڸ�ʽ�ַ���
  public static final String[] SIGN_IDENTIFIER_NAMES = new String[] { "���ָ�ʽ", "���ָ�ʽ", "��֧��ʽ", "��ĸ��ʽ" }; // �б������͵���ʾ����
  public static final String HALF_WIDTH_NUMBERS = "0123456789"; // �������
  public static final char[] FULL_WIDTH_NUMBERS = new char[] { '��', '��', '��', '��', '��', '��', '��', '��', '��', '��' }; // ȫ������
  public static final String[] SIMPLIFIED_CHINESE_NUMBERS = new String[] { "��", "һ", "��", "��", "��", "��", "��", "��", "��", "��" }; // ��������
  public static final String[] SIMPLIFIED_CHINESE_UNITS = new String[] { "", "ʮ", "��", "ǧ", "��", "ʮ", "��", "ǧ", "��", "ʮ" }; // �������ֵ�λ
  public static final String[] TRADITIONAL_CHINESE_NUMBERS = new String[] { "��", "Ҽ", "��", "��", "��", "��", "½", "��", "��", "��" }; // ��������
  public static final String[] TRADITIONAL_CHINESE_UNITS = new String[] { "", "ʰ", "��", "Ǫ", "��", "ʰ", "��", "Ǫ", "��", "ʰ" }; // �������ֵ�λ
  public static final String LOWER_CASE_LETTERS = "abcdefghijklmnopqrstuvwxyz"; // СдӢ����ĸ
  public static final String[] TOOL_TOOLTIP_TEXTS = new String[] { "�½�", "��", "����", "���Ϊ", "�ر�", "�ر�ȫ��", "����", "����", "ճ��",
      "����", "����", "����", "�滻", "����Ŵ�", "������С", "����", "ǰ��", "�Զ�����" }; // ��������ʾ��Ϣ
  public static final String[] WINDOW_MANAGE_TABLE_TITLE_TEXTS = new String[] { "�ļ���", "·��", "����" }; // ���ڹ������ı�����
  public static final String[] SHORTCUT_MANAGE_TABLE_TITLE_TEXTS = new String[] { "����", "��ݼ�" }; // ��ݼ��������ı�����
  public static final String[] SHORTCUT_NAMES = new String[] {
      "�½�","��","��ָ�������","����","���Ϊ","������","���������ļ�","ɾ����ǰ�ļ�","�رյ�ǰ","�ر�����",
      "�ر����","�ر��Ҳ�","�ر�ȫ��","�����ļ�","��ӡ","�������༭�б�","�˳�","����","����","����","����","ճ��",
      "ȫѡ","ɾ��","����ļ�","�л�Ϊ��д","�л�ΪСд","���Ƶ�ǰ�ļ�����������","���Ƶ�ǰ�ļ�·����������","���Ƶ�ǰĿ¼·����������","���������ı���������","��д��ǰ��","ɾ����ǰ��",
      "ɾ��������","ɾ������β","ɾ�����ļ���","ɾ�����ļ�β","���Ƶ�ǰ��","���Ƶ�ǰ��","���Ƶ�ǰ��","���е�ǰ��","�����г���","����������",
      "�����ָ���","����ƴ����","�����ϲ���","���и�д","��������","��������","��������","����","�˸�","������׿հ�","�����β�հ�",
      "������׺���β�հ�","���ѡ���ڿհ�","ɾ��ȫ�Ŀ���","ɾ��ѡ������","��ӵ���ע��","�������ע��","���������ַ�","����ʱ�������","��дѡ���ַ�","��תѡ���ַ�",
      "����","������һ��","������һ��","ѡ��������һ��","ѡ��������һ��","�������²���","�������ϲ���","�滻","ת��","��λƥ������",
      "���ʱ߽绻��","�ַ��߽绻��","Windows���з���ʽ","Unix/Linux���з���ʽ","Macintosh���з���ʽ","Ĭ��GB18030�����ʽ","ANSI�����ʽ","UTF-8�����ʽ","UTF-8 No BOM�����ʽ","Unicode Little Endian�����ʽ",
      "Unicode Big Endian�����ʽ","�б��������","����","Tab������","�Զ����","�Զ�����","�ı���ק","�Զ�����","�ָ�Ĭ������","����","ǰ��","��ʾ/���ع�����",
      "��ʾ/����״̬��","��ʾ/�����к���","��ʾ/���ز��ҽ�����","ǰ����ʾ","��������","���б�ǩ","˫���رձ�ǩ","��ʾ/����ָʾͼ��","����Ŵ�","������С",
      "����ָ���ʼ��С","������ɫ","������ɫ","�����ɫ","ѡ��������ɫ","ѡ��������ɫ","ƥ�����ű�����ɫ","��ǰ�б�����ɫ","ȫ����ɫ","ȫ����ɫ",
      "��ɫ����1","��ɫ����2","��ɫ����3","��ɫ����4","��ɫ����5","�ָ�Ĭ����ɫ","������ʾ��ʽ1","������ʾ��ʽ2","������ʾ��ʽ3","������ʾ��ʽ4",
      "������ʾ��ʽ5","���������ʽ1","���������ʽ2","���������ʽ3","���������ʽ4","���������ʽ5","������и�����ʽ","��ǰ�л��ĵ�","����л��ĵ�","ǰһ���ĵ�",
      "��һ���ĵ�","ͳ����Ϣ","���ڹ���","����","����ת��","������","�и��ļ�","ƴ���ļ�","��������","����"
      }; // ��ݼ�������
  public static final String[] SHORTCUT_VALUES = new String[] {
      "Ctrl+78","Ctrl+79","","Ctrl+83","","","","","115","",
      "","","","","","","Ctrl+81","Ctrl+90","Ctrl+89","Ctrl+88","Ctrl+67","Ctrl+86",
      "Ctrl+65","127","","Ctrl+Shift+85","Ctrl+85","","","","Ctrl+Shift+65","Ctrl+68","Ctrl+Shift+68",
      "Ctrl+Alt+37","Ctrl+Alt+39","Ctrl+Alt+Shift+37","Ctrl+Alt+Shift+39","Ctrl+Shift+38","Ctrl+Shift+40","Ctrl+Shift+67","Ctrl+Shift+88","Ctrl+Shift+82","Ctrl+Shift+73",
      "Ctrl+Shift+80","Ctrl+Shift+74","Ctrl+Shift+77","Ctrl+Shift+86","Alt+38","Alt+40","Alt+47","Ctrl+Alt+84","Ctrl+Alt+Shift+84","Ctrl+Shift+83","Ctrl+Shift+69",
      "Ctrl+Shift+76","Ctrl+Shift+84","Ctrl+Alt+65","Ctrl+Alt+83","Ctrl+76","Ctrl+77","","116","Ctrl+82","Ctrl+73",
      "Ctrl+70","114","Shift+114","Ctrl+114","Ctrl+Shift+114","Ctrl+75","Ctrl+Shift+75","Ctrl+72","Ctrl+71","Ctrl+66",
      "","","","","","","","","","",
      "","","","","","","","","","","","",
      "","","","","","","","","Ctrl+38","Ctrl+40",
      "Ctrl+47","","","","","","","","","",
      "","","","","","","","","","",
      "","","","","","","","Ctrl+Shift+87","Ctrl+87","Alt+37",
      "Alt+39","","","","","","","","","112"
      }; // ��ݼ���ֵ
  public static final String[] CAN_NOT_MODIFIED_SHORTCUT_NAMES = new String[] {"����","����","ճ��","ȫѡ","ɾ��"}; // �����޸ĵĿ�ݼ�����
  public static final String[] DIGEST_TYPES = new String[] {"MD5","SHA","SHA-224","SHA-256","SHA-384","SHA-512"}; // ���ܵ�����
  public static final String[] STORAGE_UNIT = new String[] {"B(�ֽ�)","KB(ǧ�ֽ�)","MB(���ֽ�)"}; // �ļ��洢��λ
  public static final int[] ALL_KEY_CODES = new int[] {
    KeyEvent.VK_0, KeyEvent.VK_1, KeyEvent.VK_2, KeyEvent.VK_3, KeyEvent.VK_4, KeyEvent.VK_5, KeyEvent.VK_6, KeyEvent.VK_7,
    KeyEvent.VK_8, KeyEvent.VK_9, KeyEvent.VK_A, KeyEvent.VK_B, KeyEvent.VK_C, KeyEvent.VK_D, KeyEvent.VK_E, KeyEvent.VK_F,
    KeyEvent.VK_G, KeyEvent.VK_H, KeyEvent.VK_I, KeyEvent.VK_J, KeyEvent.VK_K, KeyEvent.VK_L, KeyEvent.VK_M, KeyEvent.VK_N,
    KeyEvent.VK_O, KeyEvent.VK_P, KeyEvent.VK_Q, KeyEvent.VK_R, KeyEvent.VK_S, KeyEvent.VK_T, KeyEvent.VK_U, KeyEvent.VK_V,
    KeyEvent.VK_W, KeyEvent.VK_X, KeyEvent.VK_Y, KeyEvent.VK_Z, KeyEvent.VK_F1, KeyEvent.VK_F2, KeyEvent.VK_F3, KeyEvent.VK_F4,
    KeyEvent.VK_F5, KeyEvent.VK_F6, KeyEvent.VK_F7, KeyEvent.VK_F8, KeyEvent.VK_F9, KeyEvent.VK_F10, KeyEvent.VK_F11, KeyEvent.VK_F12,
    KeyEvent.VK_NUMPAD0, KeyEvent.VK_NUMPAD1, KeyEvent.VK_NUMPAD2, KeyEvent.VK_NUMPAD3, KeyEvent.VK_NUMPAD4, KeyEvent.VK_NUMPAD5, KeyEvent.VK_NUMPAD6, KeyEvent.VK_NUMPAD7,
    KeyEvent.VK_NUMPAD8, KeyEvent.VK_NUMPAD9, KeyEvent.VK_NUM_LOCK, KeyEvent.VK_ADD, KeyEvent.VK_SUBTRACT, KeyEvent.VK_MULTIPLY, KeyEvent.VK_DIVIDE, KeyEvent.VK_DECIMAL,
    KeyEvent.VK_ESCAPE, KeyEvent.VK_TAB, KeyEvent.VK_SPACE, KeyEvent.VK_BACK_SPACE, KeyEvent.VK_BACK_QUOTE, KeyEvent.VK_SLASH, KeyEvent.VK_BACK_SLASH, KeyEvent.VK_OPEN_BRACKET,
    KeyEvent.VK_CLOSE_BRACKET, KeyEvent.VK_COMMA, KeyEvent.VK_PAGE_UP, KeyEvent.VK_PAGE_DOWN, KeyEvent.VK_PERIOD, KeyEvent.VK_QUOTE, KeyEvent.VK_SEMICOLON, KeyEvent.VK_INSERT,
    KeyEvent.VK_DELETE, KeyEvent.VK_HOME, KeyEvent.VK_END, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_ENTER,
    KeyEvent.VK_EQUALS, KeyEvent.VK_MINUS, KeyEvent.VK_CAPS_LOCK, KeyEvent.VK_WINDOWS, KeyEvent.VK_CONTEXT_MENU, KeyEvent.VK_SCROLL_LOCK, KeyEvent.VK_PAUSE }; // ���п���������ݼ��İ�������
  public static final int[] SINGLE_KEY_CODES = new int[] { KeyEvent.VK_F1 ,KeyEvent.VK_F2 ,KeyEvent.VK_F3 ,KeyEvent.VK_F4 ,KeyEvent.VK_F5 ,KeyEvent.VK_F6 ,KeyEvent.VK_F7 ,KeyEvent.VK_F8 ,KeyEvent.VK_F9 ,KeyEvent.VK_F10 ,KeyEvent.VK_F11 ,KeyEvent.VK_F12 }; // ���Ե���������ݼ��İ�������
  public static final int DEFAULT_FRAME_WIDTH = 600; // ����Ĭ�Ͽ��
  public static final int DEFAULT_FRAME_HEIGHT = 500; // ����Ĭ�ϸ߶�
  public static final int DEFAULT_CARET_INDEX = 0; // �ı���Ĭ�ϲ����λ��
  public static final int INPUT_HEIGHT = 22; // ���������ĸ߶�
  public static final int VIEW_HEIGHT = 18; // ��ǩ����ѡ��ť����ѡ��ĸ߶�
  public static final int BUTTON_HEIGHT = 23; // ��ť�ĸ߶�
  public static final int BUFFER_LENGTH = 1024; // �������Ĵ�С
  public static final int BIG_BUFFER_LENGTH = 1024 * 1024; // �󻺳����Ĵ�С
  public static final int MIN_FONT_SIZE = 8; // ������Сֵ
  public static final int MAX_FONT_SIZE = 100; // �������ֵ
  public static final int MIN_TABSIZE = 1; // Tab�ַ���Сֵ
  public static final int MAX_TABSIZE = 99; // Tab�ַ����ֵ
  public static final int DEFAULT_TABSIZE = 4; // Tab�ַ�Ĭ��ֵ
  public static final int FILE_HISTORY_MAX = 15; // ����༭�ļ������洢����
  public static final int BACK_FORWARD_MAX = 15; // �����ʷλ�õ����洢����
  public static final int TEXTAREA_HASHCODE_LIST_MAX = 15; // ����༭���ı���hashCode�����洢����
  public static final int DEFAULT_UNDO_INDEX = 0; // ������ʶ����Ĭ��ֵ
  public static final int DEFAULT_BACK_FORWARD_INDEX = 0; // �����ʷλ�õ�Ĭ��ֵ
  public static final int DEFAULT_TEXTAREA_HASHCODE_LIST_INDEX = 0; // ����༭���ı���������Ĭ��ֵ
  public static final int INSERT_MAX_ROW = 10; // �����ַ�������������
  public static final int INSERT_MAX_COLUMN = 10; // �����ַ�������������
  public static final int INSERT_MAX_ELEMENT = INSERT_MAX_ROW * INSERT_MAX_COLUMN; // �����ַ���������Ԫ����
  public static final int SIGN_MAX_ROW = 5; // �б�������Ž�����������
  public static final int SIGN_MAX_COLUMN = 4; // �б�������Ž�����������
  public static final int SIGN_MAX_ELEMENT = SIGN_MAX_ROW * SIGN_MAX_COLUMN; // �б�������Ž�������Ԫ����
  public static final int MSG_LINE_SIZE = 60; // ��ʾ����ÿ���ַ�����ʾ���������
  public static final int LINE_NUMBER_HEIGHT = 2000000000; // �к����֧�ֵ����߶�
  public static final int LINE_NUMBER_MARGIN = 5; // �к���������ұ߾�
  public static final int LINE_NUMBER_START_OFFSET = 2; // �к��������ʼ��ֱƫ���������ڶ����ı���ĸ���
  public static final int BRACKET_COLOR_STYLE = 11; // ���ı����н��и���ƥ�����ŵ���ɫ��ʶֵ
  public static final Font GLOBAL_FONT = new Font("����", Font.PLAIN, 12); // ȫ�ֵ�Ĭ������
  public static final Font TEXT_FONT = new Font("����", Font.PLAIN, 14); // �ı����Ĭ������
  public static final Font INSERT_VIEW_FONT = new Font("����", Font.PLAIN, 80); // �����ַ�������Ԥ����ǩ������
  public static final Font SIGN_VIEW_FONT = new Font("����", Font.PLAIN, 28); // �б�������Ž�����Ԥ�����������
  public static final Font CALCULATOR_VIEW_FONT = new Font("����", Font.PLAIN, 16); // �����������м������������
  public static final Font CALCULATOR_ITEM_FONT = new Font("����", Font.BOLD, 30); // �����������м������������
  public static final Color COLOR_BRACKET = new Color(20, 20, 20, 35); // ���ı����н��и���ƥ�����ŵı�����ɫ
  public static final Color COLOR_CURRENT_LINE = new Color(0, 100, 200, 25); // ���ı��������ڱ�ʶ��ǰ�еı�����ɫ
  public static final Color[] COLOR_HIGHLIGHTS = new Color[] {
      new Color(255, 0, 0, 40), new Color(0, 255, 0, 40),
      new Color(0, 0, 255, 40), new Color(0, 255, 255, 40),
      new Color(255, 0, 255, 40) }; // ���ڸ�����ʾ����ɫ�����е�4��������ʾ͸���ȣ���ֵԽСԽ͸��
  public static final Color[] COLOR_STYLE_1 = new Color[] {
      new Color(211, 215, 207), new Color(46, 52, 54),
      new Color(211, 215, 207), new Color(238, 238, 236),
      new Color(136, 138, 133), new Color(255, 0, 255, 35),
      new Color(150, 150, 150, 25) };
  public static final Color[] COLOR_STYLE_2 = new Color[] {
      new Color(240, 240, 240), new Color(0, 128, 128),
      new Color(240, 240, 240), new Color(22, 99, 88), new Color(240, 240, 240),
      new Color(180, 0, 255, 35), new Color(240, 240, 10, 25) };
  public static final Color[] COLOR_STYLE_3 = new Color[] {
      new Color(46, 52, 54), new Color(215, 215, 175), new Color(46, 52, 54),
      new Color(255, 251, 240), new Color(46, 52, 54),
      new Color(0, 255, 180, 35), new Color(240, 100, 100, 25) };
  public static final Color[] COLOR_STYLE_4 = new Color[] {
      new Color(51, 53, 49), new Color(204, 232, 207), new Color(51, 53, 49),
      new Color(204, 232, 207), new Color(0, 60, 100),
      new Color(20, 20, 20, 35), new Color(0, 100, 200, 25) };
  public static final Color[] COLOR_STYLE_5 = new Color[] {
      new Color(189, 174, 157), new Color(42, 33, 28), new Color(5, 165, 245),
      new Color(189, 174, 157), new Color(130, 100, 90),
      new Color(255, 255, 0, 35), new Color(240, 200, 180, 25) };
  public static final Color[][] COLOR_STYLES = new Color[][] { COLOR_STYLE_1, COLOR_STYLE_2, COLOR_STYLE_3, COLOR_STYLE_4, COLOR_STYLE_5 }; // �ı�����ɫ����������
  public static final Color[] COLOR_STYLE_DEFAULT = new Color[] {
      (Color) UIManager.getLookAndFeelDefaults().getColor("TextArea.foreground"),
      (Color) UIManager.getLookAndFeelDefaults().getColor("TextArea.background"),
      (Color) UIManager.getLookAndFeelDefaults().getColor("TextArea.caretForeground"),
      (Color) UIManager.getLookAndFeelDefaults().getColor("TextArea.selectionForeground"),
      (Color) UIManager.getLookAndFeelDefaults().getColor("TextArea.selectionBackground"),
      COLOR_BRACKET, COLOR_CURRENT_LINE }; // �ı���Ĭ����ɫ����
  public static final UIManager.LookAndFeelInfo[] LOOK_AND_FEEL_INFOS = UIManager.getInstalledLookAndFeels(); // ��ǰϵͳ���õ������Ϣ����
  public static final ImageIcon SW_ICON = new ImageIcon(ClassLoader.getSystemResource("res/icon.gif")); // ������ͼ��
  public static final ImageIcon CLOSE_ICON = new ImageIcon(ClassLoader.getSystemResource("res/close.png")); // ���ҽ�����ر�ͼ��
  public static final ImageIcon TAB_EXIST_READONLY_ICON = new ImageIcon(ClassLoader.getSystemResource("res/tab_exist_readonly.png")); // ֻ���ļ�ͼ��
  public static final ImageIcon TAB_EXIST_CURRENT_ICON = new ImageIcon(ClassLoader.getSystemResource("res/tab_exist_current.png")); // ��ͨ�ļ�ͼ��
  public static final ImageIcon TAB_NEW_FILE_ICON = new ImageIcon(ClassLoader.getSystemResource("res/tab_new_file.png")); // �½��ļ�ͼ��
  public static final ImageIcon TAB_NOT_EXIST_ICON = new ImageIcon(ClassLoader.getSystemResource("res/tab_not_exist.png")); // ��ʧ�ļ�ͼ��
  public static final ImageIcon TAB_EXIST_READONLY_FROZEN_ICON = new ImageIcon(ClassLoader.getSystemResource("res/tab_exist_readonly_frozen.png")); // ֻ���ļ�����ͼ��
  public static final ImageIcon TAB_EXIST_CURRENT_FROZEN_ICON = new ImageIcon(ClassLoader.getSystemResource("res/tab_exist_current_frozen.png")); // ��ͨ�ļ�����ͼ��
  public static final ImageIcon TAB_NEW_FILE_FROZEN_ICON = new ImageIcon(ClassLoader.getSystemResource("res/tab_new_file_frozen.png")); // �½��ļ�����ͼ��
  public static final ImageIcon TAB_NOT_EXIST_FROZEN_ICON = new ImageIcon(ClassLoader.getSystemResource("res/tab_not_exist_frozen.png")); // ��ʧ�ļ�����ͼ��
  public static final ImageIcon HELP_ICON = new ImageIcon(ClassLoader.getSystemResource("res/help.png")); // ����ͼ��
  public static final ImageIcon[] TOOL_ENABLE_ICONS = new ImageIcon[] {
      new ImageIcon(ClassLoader.getSystemResource("res/enable/tool_new.png")),
      new ImageIcon(ClassLoader.getSystemResource("res/enable/tool_open.png")),
      new ImageIcon(ClassLoader.getSystemResource("res/enable/tool_save.png")),
      new ImageIcon(ClassLoader.getSystemResource("res/enable/tool_save_as.png")),
      new ImageIcon(ClassLoader.getSystemResource("res/enable/tool_close.png")),
      new ImageIcon(ClassLoader.getSystemResource("res/enable/tool_close_all.png")),
      new ImageIcon(ClassLoader.getSystemResource("res/enable/tool_cut.png")),
      new ImageIcon(ClassLoader.getSystemResource("res/enable/tool_copy.png")),
      new ImageIcon(ClassLoader.getSystemResource("res/enable/tool_paste.png")),
      new ImageIcon(ClassLoader.getSystemResource("res/enable/tool_undo.png")),
      new ImageIcon(ClassLoader.getSystemResource("res/enable/tool_redo.png")),
      new ImageIcon(ClassLoader.getSystemResource("res/enable/tool_find.png")),
      new ImageIcon(ClassLoader.getSystemResource("res/enable/tool_replace.png")),
      new ImageIcon(ClassLoader.getSystemResource("res/enable/tool_font_size_plus.png")),
      new ImageIcon(ClassLoader.getSystemResource("res/enable/tool_font_size_minus.png")),
      new ImageIcon(ClassLoader.getSystemResource("res/enable/tool_back.png")),
      new ImageIcon(ClassLoader.getSystemResource("res/enable/tool_forward.png")),
      new ImageIcon(ClassLoader.getSystemResource("res/enable/tool_line_wrap.png")) }; // ����������״̬��ͼ��
  public static final ImageIcon[] TOOL_DISABLE_ICONS = new ImageIcon[] {
      new ImageIcon(ClassLoader.getSystemResource("res/disable/tool_new.png")),
      new ImageIcon(ClassLoader.getSystemResource("res/disable/tool_open.png")),
      new ImageIcon(ClassLoader.getSystemResource("res/disable/tool_save.png")),
      new ImageIcon(ClassLoader.getSystemResource("res/disable/tool_save_as.png")),
      new ImageIcon(ClassLoader.getSystemResource("res/disable/tool_close.png")),
      new ImageIcon(ClassLoader.getSystemResource("res/disable/tool_close_all.png")),
      new ImageIcon(ClassLoader.getSystemResource("res/disable/tool_cut.png")),
      new ImageIcon(ClassLoader.getSystemResource("res/disable/tool_copy.png")),
      new ImageIcon(ClassLoader.getSystemResource("res/disable/tool_paste.png")),
      new ImageIcon(ClassLoader.getSystemResource("res/disable/tool_undo.png")),
      new ImageIcon(ClassLoader.getSystemResource("res/disable/tool_redo.png")),
      new ImageIcon(ClassLoader.getSystemResource("res/disable/tool_find.png")),
      new ImageIcon(ClassLoader.getSystemResource("res/disable/tool_replace.png")),
      new ImageIcon(ClassLoader.getSystemResource("res/disable/tool_font_size_plus.png")),
      new ImageIcon(ClassLoader.getSystemResource("res/disable/tool_font_size_minus.png")),
      new ImageIcon(ClassLoader.getSystemResource("res/disable/tool_back.png")),
      new ImageIcon(ClassLoader.getSystemResource("res/disable/tool_forward.png")),
      new ImageIcon(ClassLoader.getSystemResource("res/disable/tool_line_wrap.png")) }; // ����������״̬��ͼ��

  public static int transfer_count = 0; // ���һ��滻ʱ�����á�ת����չ����ת����ַ�����
  public static int matcher_length = 0; // ͨ��������ʽ�ɹ�ƥ����ַ�����
  private static Matcher matcher = null; // ͨ������Pattern��ָ���ı�ִ��ƥ�����������

  /**
   * ���ڴ���Ϊ�����࣬�ʽ����췽��˽�л�
   */
  private Util() {
  }

  /**
   * �жϸ������ַ����Ƿ�Ϊ��
   * 
   * @param str
   *          ���жϵ��ַ���
   */
  public static boolean isTextEmpty(String str) {
    return (str == null || str.isEmpty());
  }

  /**
   * Ϊ�ļ�ѡ�������Ԥ������ļ�������
   * 
   * @param fileChooser
   *          Ҫ������ļ�ѡ����
   */
  public static void addChoosableFileFilters(JFileChooser fileChooser) {
    FileExt[] arrFileExt = FileExt.values(); // ��ȡ����ö�����г�Ա������
    BaseFileFilter fileFilter = null;
    BaseFileFilter defFileFilter = null; // Ĭ��ѡ����ļ�������
    for (FileExt fileExt : arrFileExt) { // ����ö�ٵ����г�Ա
      fileFilter = new BaseFileFilter(fileExt.toString(), fileExt.getDescription());
      fileChooser.addChoosableFileFilter(fileFilter);
      if (fileExt.equals(FileExt.TXT)) {
        defFileFilter = fileFilter;
      }
    }
    if (defFileFilter != null) {
      fileChooser.setFileFilter(defFileFilter);
    }
  }

  /**
   * �������������ı�������
   * 
   * @param txaSource
   *          ��ǰ�ı���
   * @param index
   *          �����Ĳ����λ��
   * @return ������Ĳ����λ��
   */
  public static int checkCaretPosition(JTextArea txaSource, int index) {
    if (txaSource == null) {
      return Util.DEFAULT_CARET_INDEX;
    }
    int totalIndex = txaSource.getText().length();
    if (index < 0) {
      index = 0;
    } else if (index > totalIndex) {
      index = totalIndex;
    }
    return index;
  }

  /**
   * �������ַ������·��У�����Ӧ�Ի������ʾ
   * 
   * @param str
   *          ��������ַ���
   * @return ��������ַ���
   */
  public static String convertToMsg(String str) {
    String[] arrContents = str.split("\n", -1);
    StringBuilder stbContent = new StringBuilder(); // ���ڴ�Ŵ������ı�
    for (int n = 0; n < arrContents.length; n++) {
      String content = "";
      if (arrContents[n].length() > MSG_LINE_SIZE) {
        int lines = arrContents[n].length() / MSG_LINE_SIZE;
        int remain = arrContents[n].length() % MSG_LINE_SIZE;
        for (int i = 0; i < lines; i++) {
          content = content + arrContents[n].substring(MSG_LINE_SIZE * i, MSG_LINE_SIZE * (i + 1)) + "\n";
        }
        if (remain > 0) {
          content += arrContents[n].substring(MSG_LINE_SIZE * lines);
        } else {
          content = content.substring(0, content.length() - 1);
        }
      } else {
        content = arrContents[n];
      }
      stbContent.append(content + "\n");
    }
    return stbContent.toString();
  }

  /**
   * ��ȡ�ı����е�ǰѡ���ڵ�������
   * 
   * @param txaSource
   *          �ض����ı���
   * @return ���浱ǰѡ���������е��ַ�����
   */
  public static String[] getCurrentLinesArray(JTextArea txaSource) {
    if (txaSource == null) {
      return null;
    }
    int lineCount = txaSource.getLineCount();
    CurrentLines currentLines = new CurrentLines(txaSource);
    int startIndex = currentLines.getStartIndex();
    int endIndex = currentLines.getEndIndex();
    int endLineNum = currentLines.getEndLineNum() + 1;
    String strContent = currentLines.getStrContent();
    String strText = txaSource.getText();
    if (strContent.endsWith("\n") && (endIndex != strText.length() || (endLineNum == lineCount - 1 && strText.endsWith("\n")))) {
      endIndex--;
    }
    txaSource.select(startIndex, endIndex);
    String strSel = txaSource.getSelectedText();
    if (strSel == null) {
      strSel = "";
    }
    return strSel.split("\n", -1); // ����ǰѡ�����ı����д�������ĩβ�Ķദ����
  }

  /**
   * ��ʽ����������ļ���
   * 
   * @param strFileName
   *          �ļ�������·��
   * @param fileFilter
   *          ��ǰ���ļ����͹�����
   * @param strExt
   *          �������ļ�����չ��
   * @return ��ʽ������ļ�
   */
  public static File checkFileName(String strFileName, BaseFileFilter fileFilter, String strExt) {
    if (isTextEmpty(strFileName) || fileFilter == null || isTextEmpty(strExt)) {
      return null;
    }
    if (fileFilter.getExt().equalsIgnoreCase(strExt)) {
      if (!strFileName.toLowerCase().endsWith(strExt.toLowerCase())) {
        strFileName += strExt;
      }
    }
    return new File(strFileName);
  }

  /**
   * �޸����������Ĭ������
   */
  public static void setDefaultFont() {
    FontUIResource fontRes = new FontUIResource(GLOBAL_FONT);
    Enumeration<Object> keys = UIManager.getDefaults().keys();
    while (keys.hasMoreElements()) {
      Object key = keys.nextElement();
      Object value = UIManager.get(key);
      if (value instanceof FontUIResource) {
        UIManager.put(key, fontRes);
      }
    }
  }

  /**
   * ���ı�����в����ַ���
   * 
   * @param strFindText
   *          ���ҵ��ַ���
   * @param txcSource
   *          �ı����
   * @param isFindDown
   *          �Ƿ����²���
   * @param isMatchCase
   *          �Ƿ����ִ�Сд
   * @param isWrap
   *          �Ƿ�ѭ������
   * @param searchStyle
   *          ����ģʽ
   * @return ���ҵ��ַ���λ���ı�����е�����
   */
  public static int findText(String strFindText, JTextComponent txcSource,
    boolean isFindDown, boolean isMatchCase, boolean isWrap, SearchStyle searchStyle) {
    if (isFindDown) {
      return findDownText(strFindText, txcSource, isMatchCase, isWrap, searchStyle);
    } else {
      return findUpText(strFindText, txcSource, isMatchCase, isWrap, searchStyle);
    }
  }

  /**
   * ���ı���������²����ַ���
   * 
   * @param strFindText
   *          ���ҵ��ַ���
   * @param txcSource
   *          �ı����
   * @param isMatchCase
   *          �Ƿ����ִ�Сд
   * @param isWrap
   *          �Ƿ�ѭ������
   * @param searchStyle
   *          ����ģʽ
   * @return ���ҵ��ַ���λ���ı�����е�����
   */
  private static int findDownText(String strFindText, JTextComponent txcSource,
      boolean isMatchCase, boolean isWrap, SearchStyle searchStyle) {
    if (isTextEmpty(strFindText) || txcSource == null || isTextEmpty(txcSource.getText())) {
      return -1;
    }
    if (searchStyle == SearchStyle.TRANSFER) {
      int len1 = strFindText.length();
      strFindText = transfer(strFindText);
      int len2 = strFindText.length();
      transfer_count = len1 - len2;
    }
    int result = -1;
    String strSourceAll = txcSource.getText();
    if (!isMatchCase) {
      if (searchStyle == SearchStyle.PATTERN) {
        strFindText = "(?i)" + strFindText; // ������ʽ�У�����(?i)�򿪲����ִ�Сд������
      } else {
        strFindText = strFindText.toLowerCase();
      }
      strSourceAll = strSourceAll.toLowerCase();
    }
    int caretPos = txcSource.getCaretPosition();
    String strSource = strSourceAll.substring(caretPos);
    if (searchStyle == SearchStyle.PATTERN) {
      try {
        matcher = Pattern.compile(strFindText).matcher(strSource);
      } catch (PatternSyntaxException x) {
        // x.printStackTrace();
        JOptionPane.showMessageDialog(txcSource, "������ʽ�﷨����\n" + x.getMessage(), Util.SOFTWARE, JOptionPane.NO_OPTION);
        return result;
      }
      matcher_length = 0;
      if (matcher.find()) {
        result = caretPos + matcher.start();
        matcher_length = matcher.end() - matcher.start();
      } else {
        if (isWrap) {
          matcher = Pattern.compile(strFindText).matcher(strSourceAll);
          if (matcher.find()) {
            result = matcher.start();
            matcher_length = matcher.end() - matcher.start();
          }
        }
      }
    } else {
      int index = strSource.indexOf(strFindText);
      if (index >= 0) {
        result = caretPos + index;
      } else {
        if (isWrap) {
          result = strSourceAll.indexOf(strFindText);
        }
      }
    }
    return result;
  }

  /**
   * ���ı���������ϲ����ַ���
   * 
   * @param strFindText
   *          ���ҵ��ַ���
   * @param txcSource
   *          �ı����
   * @param isMatchCase
   *          �Ƿ����ִ�Сд
   * @param isWrap
   *          �Ƿ�ѭ������
   * @param searchStyle
   *          ����ģʽ
   * @return ���ҵ��ַ���λ���ı�����е�����
   */
  private static int findUpText(String strFindText, JTextComponent txcSource,
      boolean isMatchCase, boolean isWrap, SearchStyle searchStyle) {
    if (isTextEmpty(strFindText) || txcSource == null || isTextEmpty(txcSource.getText())) {
      return -1;
    }
    if (searchStyle == SearchStyle.TRANSFER) {
      int len1 = strFindText.length();
      strFindText = transfer(strFindText);
      int len2 = strFindText.length();
      transfer_count = len1 - len2;
    }
    int result = -1;
    int caretPos = txcSource.getCaretPosition();
    String strSel = txcSource.getSelectedText();
    if (strSel != null) {
      if (!isMatchCase) {
        if (searchStyle == SearchStyle.PATTERN) {
          if (strSel.matches("(?i)" + strFindText)) { // ������ʽ�У�����(?i)�򿪲����ִ�Сд������
            caretPos -= strSel.length();
          }
        } else if (strSel.equalsIgnoreCase(strFindText)) {
          caretPos -= strSel.length();
        }
      } else {
        if (searchStyle == SearchStyle.PATTERN) {
          if (strSel.matches(strFindText)) {
            caretPos -= strSel.length();
          }
        } else if (strSel.equals(strFindText)) {
          caretPos -= strSel.length();
        }
      }
    }
    String strSourceAll = txcSource.getText();
    if (!isMatchCase) {
      if (searchStyle == SearchStyle.PATTERN) {
        strFindText = "(?i)" + strFindText; // ������ʽ�У�����(?i)�򿪲����ִ�Сд������
      } else {
        strFindText = strFindText.toLowerCase();
      }
      strSourceAll = strSourceAll.toLowerCase();
    }
    String strSource = strSourceAll.substring(0, caretPos);
    if (searchStyle == SearchStyle.PATTERN) {
      try {
        matcher = Pattern.compile(strFindText).matcher(strSource);
      } catch (PatternSyntaxException x) {
        // x.printStackTrace();
        JOptionPane.showMessageDialog(txcSource, "������ʽ�﷨����\n" + x.getMessage(), Util.SOFTWARE, JOptionPane.NO_OPTION);
        return result;
      }
      matcher_length = 0;
      while (matcher.find()) {
        result = matcher.start();
        matcher_length = matcher.end() - matcher.start();
      }
      if (result < 0 && isWrap) {
        matcher = Pattern.compile(strFindText).matcher(strSourceAll);
        while (matcher.find()) {
          result = matcher.start();
          matcher_length = matcher.end() - matcher.start();
        }
      }
    } else {
      result = strSource.lastIndexOf(strFindText);
      if (result < 0 && isWrap) {
        result = strSourceAll.lastIndexOf(strFindText);
      }
    }
    return result;
  }

  /**
   * �Ը�������ʼ�������ı�����в����ַ���
   * 
   * @param strFindText
   *          ���ҵ��ַ���
   * @param txcSource
   *          �ı����
   * @param caretPos
   *          ָ������ʼ����
   * @param isMatchCase
   *          �Ƿ����ִ�Сд
   * @param searchStyle
   *          ����ģʽ
   * @return ���ҵ��ַ���λ���ı�����е�����
   */
  public static int findText(String strFindText, JTextComponent txcSource, int caretPos,
      boolean isMatchCase, SearchStyle searchStyle) {
    if (isTextEmpty(strFindText) || txcSource == null || isTextEmpty(txcSource.getText())) {
      return -1;
    }
    if (searchStyle == SearchStyle.TRANSFER) {
      int len1 = strFindText.length();
      strFindText = transfer(strFindText);
      int len2 = strFindText.length();
      transfer_count = len1 - len2;
    }
    int result = -1;
    String strSourceAll = txcSource.getText();
    if (!isMatchCase) {
      if (searchStyle == SearchStyle.PATTERN) {
        strFindText = "(?i)" + strFindText; // ������ʽ�У�����(?i)�򿪲����ִ�Сд������
      } else {
        strFindText = strFindText.toLowerCase();
      }
      strSourceAll = strSourceAll.toLowerCase();
    }
    String strSource = strSourceAll.substring(caretPos);
    if (searchStyle == SearchStyle.PATTERN) {
      try {
        matcher = Pattern.compile(strFindText).matcher(strSource);
      } catch (PatternSyntaxException x) {
        // x.printStackTrace();
        JOptionPane.showMessageDialog(txcSource, "������ʽ�﷨����\n" + x.getMessage(), Util.SOFTWARE, JOptionPane.NO_OPTION);
        return result;
      }
      matcher_length = 0;
      if (matcher.find()) {
        result = caretPos + matcher.start();
        matcher_length = matcher.end() - matcher.start();
      }
    } else {
      int index = strSource.indexOf(strFindText);
      if (index >= 0) {
        result = caretPos + index;
      }
    }
    return result;
  }

  /**
   * �����ļ���ͷ��BOM��������ڵĻ������ж��ļ��ı����ʽ�� �ı��ļ��и��ֲ�ͬ�ı����ʽ������ж�������ᵼ����ʾ�򱣴����
   * Ϊ�˱�ʶ�ļ��ı����ʽ�����ڱ༭�ͱ��棬�����ļ���ͷ������BOM�����Ա�ʶ�����ʽ�� UTF-8��ʽ��0xef 0xbb 0xbf�� Unicode
   * Little Endian��ʽ��0xff 0xfe�� Unicode Big Endian��ʽ��0xfe
   * 0xff����ANSI��ʽ��û��BOM�ġ�����һ�ֲ���BOM��UTF-8��ʽ���ļ���������ANSI�����֣������Ҫ��һ����⡣
   * 
   * @param file
   *          ���жϵ��ļ�
   */
  public static CharEncoding checkFileEncoding(File file) {
    FileInputStream fileInputStream = null;
    int[] charArr = new int[3];
    try {
      fileInputStream = new FileInputStream(file);
      for (int i = 0; i < charArr.length; i++) {
        charArr[i] = fileInputStream.read();
      }
    } catch (Exception x) {
      // x.printStackTrace();
    } finally {
      try {
        fileInputStream.close();
      } catch (IOException x) {
        // x.printStackTrace();
      }
    }
    if (charArr[0] == 0xff && charArr[1] == 0xfe) {
      return CharEncoding.ULE;
    } else if (charArr[0] == 0xfe && charArr[1] == 0xff) {
      return CharEncoding.UBE;
    } else if (charArr[0] == 0xef && charArr[1] == 0xbb && charArr[2] == 0xbf) {
      return CharEncoding.UTF8;
    } else {
      if (isUTF8NoBom(file)) {
        return CharEncoding.UTF8_NO_BOM;
      } else {
        return CharEncoding.BASE;
      }
    }
  }

  /**
   * �ж��ļ��Ƿ�ΪUTF8��BOM��ʽ��
   * UTF-8�ı������ܼ򵥣�ֻ��2����
   * 1.���ڵ��ֽڵ��ַ����ֽڵĵ�һλ��Ϊ0������7λΪ������ŵ�unicode�롣��˶���Ӣ����ĸ��UTF-8�����ASCII������ͬ�ġ�
   * 2.����n�ֽڵ��ַ�(n>1 && n<=6)����һ���ֽڵ�ǰnλ��Ϊ1����n+1λΪ0�������ֽڵ�ǰ��λ��Ϊ10��ʣ�µ�����λ����Ϊ���ַ���unicode�롣
   * 
   * @param file
   *          ���жϵ��ļ�
   * @return �Ƿ�ΪUTF8��BOM��ʽ����UTF8��BOM��ʽ����true����֮����false
   */
  private static boolean isUTF8NoBom(File file) {
    FileInputStream fileInputStream = null;
    int maxLength = 1024 * 1024;
    byte[] rawtext = new byte[maxLength];
    try {
      fileInputStream = new FileInputStream(file);
      fileInputStream.read(rawtext);
    } catch (Exception x) {
      // x.printStackTrace();
    } finally {
      try {
        fileInputStream.close();
      } catch (IOException x) {
        // x.printStackTrace();
        return false;
      }
    }
    int score = 0;
    int goodbytes = 0;
    int asciibytes = 0;
    int rawtextlen = rawtext.length;
    for (int i = 0; i < rawtextlen; i++) {
      if ((rawtext[i] & (byte) 0x7F) == rawtext[i]) { // ���ֽ��ַ�
        asciibytes++;
      } else if (-64 <= rawtext[i] && rawtext[i] <= -33
        && i + 1 < rawtextlen
        && -128 <= rawtext[i + 1]
        && rawtext[i + 1] <= -65) { // ˫�ֽ��ַ�
        goodbytes += 2;
        i++;
      } else if (-32 <= rawtext[i] && rawtext[i] <= -17
        && i + 2 < rawtextlen
        && -128 <= rawtext[i + 1] && rawtext[i + 1] <= -65
        && -128 <= rawtext[i + 2] && rawtext[i + 2] <= -65) { // ���ֽ��ַ�
        goodbytes += 3;
        i += 2;
      } else if (-16 <= rawtext[i] && rawtext[i] <= -9
        && i + 3 < rawtextlen
        && -128 <= rawtext[i + 1] && rawtext[i + 1] <= -65
        && -128 <= rawtext[i + 2] && rawtext[i + 2] <= -65
        && -128 <= rawtext[i + 3] && rawtext[i + 3] <= -65) { // ���ֽ��ַ�
        goodbytes += 4;
        i += 3;
      }
    }
    if (asciibytes == rawtextlen) {
      return false;
    }
    score = 100 * goodbytes / (rawtextlen - asciibytes);
    // If not above 98, reduce to zero to prevent coincidental matches
    // Allows for some (few) bad formed sequences
    if (score > 98) {
      return true;
    } else if (score > 95 && goodbytes > 30) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * ɾ��Ŀ¼�µ������ļ�
   * 
   * @param file
   *          Ŀ¼�ļ�
   */
  public static void deleteAllFiles(File file) {
    if (!file.exists()) {
      return;
    }
    if (file.isFile()) {
      file.delete();
      return;
    }
    File[] files = file.listFiles();
    for (int i = 0; i < files.length; i++) {
      deleteAllFiles(files[i]);
    }
  }

  /**
   * ���������ַ�������ת���滻�������ַ����е�\n�滻Ϊ���з���\t�滻Ϊtab�ַ�
   * 
   * @param strSource
   *          ������ַ���
   * @return �滻����ַ���
   */
  public static String transfer(String strSource) {
    return strSource.replace("\\n", "\n").replace("\\t", "\t"); // ���ַ����е�\n�滻Ϊ���з���\t�滻Ϊtab�ַ�
  }

  /**
   * �������Ŀ�ݼ���ϵĳ����ַ�����ʽת��Ϊ��ݼ�����
   * 
   * @param shortcut
   *          ��ʾ��ݼ���ϵĳ����ַ���
   * @return ��ʾ��ݼ��������ַ���
   */
  public static String transferShortcut(String shortcut) {
    String value = "";
    if (isTextEmpty(shortcut)) {
      return value;
    }
    boolean hasCtrl = false; // �Ƿ���Ctrl��
    boolean hasAlt = false; // �Ƿ���Alt��
    boolean hasShift = false; // �Ƿ���Shift��
    String[] arrKeys = shortcut.split("\\+");
    for (String str : arrKeys) {
      if (CTRL.equalsIgnoreCase(str)) {
        hasCtrl = true;
      } else if (ALT.equalsIgnoreCase(str)) {
        hasAlt = true;
      } else if (SHIFT.equalsIgnoreCase(str)) {
        hasShift = true;
      } else { // �����Ƽ�֮��İ���
        String strKey = transferKeyCode(str);
        if (!isTextEmpty(strKey)) {
          value = strKey;
        }
      }
    }
    if (!isTextEmpty(value)) {
      if (hasShift) {
        value = SHIFT + "+" + value;
      }
      if (hasAlt) {
        value = ALT + "+" + value;
      }
      if (hasCtrl) {
        value = CTRL + "+" + value;
      }
    }
    return value;
  }

  /**
   * ���������ַ�����ʽ�İ�������ת��Ϊ��������
   * 
   * @param strKeyCode
   *          ��ʾ�����������ַ���
   * @return ��ʾ�����������ַ���
   */
  public static String transferKeyCode(String strKeyCode) {
    String strKey = "";
    try {
      strKey = KeyEvent.getKeyText(Integer.parseInt(strKeyCode));
    } catch (Exception x) {
      // x.printStackTrace();
      return "";
    }
    return strKey;
  }

  /**
   * �������Ŀ�ݼ���ϵĳ����ַ�����ʽת��Ϊ�����ڿ�ݼ����õĶ���
   * 
   * @param shortcut
   *          ��ʾ��ݼ���ϵĳ����ַ���
   * @return ��ʾ�����ڿ�ݼ����õĶ���
   */
  public static KeyStroke transferKeyStroke(String shortcut) {
    KeyStroke keyStroke = null;
    if (isTextEmpty(shortcut)) {
      return keyStroke;
    }
    int modifiers = 0; // ���Ƽ�����չ���η�������0��ʾû�п��Ƽ�
    int keyCode = -1; // �����Ƽ�֮��İ�������
    String[] arrKeys = shortcut.split("\\+");
    for (String str : arrKeys) {
      if (CTRL.equalsIgnoreCase(str)) {
        modifiers += InputEvent.CTRL_DOWN_MASK;
      } else if (ALT.equalsIgnoreCase(str)) {
        modifiers += InputEvent.ALT_DOWN_MASK;
      } else if (SHIFT.equalsIgnoreCase(str)) {
        modifiers += InputEvent.SHIFT_DOWN_MASK;
      } else { // �����Ƽ�֮��İ���
        try {
          keyCode = Integer.parseInt(str);
        } catch (Exception x) {
          // x.printStackTrace();
        }
      }
    }
    if (keyCode >= 0) {
      keyStroke = KeyStroke.getKeyStroke(keyCode, modifiers);
    }
    return keyStroke;
  }

  /**
   * ������������ת��Ϊ���ָ�ʽ
   * 
   * @param number
   *          ��ת��������
   * @param isTraditional
   *          �Ƿ�ת��Ϊ���庺�֣�Ϊtrue��ʾת��Ϊ���壬��֮ת��Ϊ����
   * @return ת������ַ���
   */
  public static String intToChinese(int number, boolean isTraditional) {
    String str = "";
    StringBuffer sb = new StringBuffer(String.valueOf(number));
    sb = sb.reverse();
    String[] arrChineseNumbers = SIMPLIFIED_CHINESE_NUMBERS;
    String[] arrChineseUnits = SIMPLIFIED_CHINESE_UNITS;
    if (isTraditional) {
      arrChineseNumbers = TRADITIONAL_CHINESE_NUMBERS;
      arrChineseUnits = TRADITIONAL_CHINESE_UNITS;
    }
    int r = 0;
    int l = 0;
    for (int j = 0; j < sb.length(); j++) {
      r = Integer.valueOf(sb.substring(j, j + 1)); // ��ǰ����
      if (j != 0) {
        l = Integer.valueOf(sb.substring(j - 1, j)); // ��һ������
      }
      if (j == 0) {
        if (r != 0 || sb.length() == 1) {
          str = arrChineseNumbers[r];
        }
        continue;
      }
      if (j == 1 || j == 2 || j == 3 || j == 5 || j == 6 || j == 7 || j == 9) {
        if (r != 0) {
          str = arrChineseNumbers[r] + arrChineseUnits[j] + str;
        } else if (l != 0) {
          str = arrChineseNumbers[r] + str;
        }
        continue;
      }
      if (j == 4 || j == 8) {
        str =  arrChineseUnits[j] + str;
        if ((l != 0 && r == 0) || r != 0) {
          str = arrChineseNumbers[r] + str;
        }
        continue;
      }
    }
    // Ϊ�˽����ֵΪ��10~19ʱ�����ڿ�ͷ�ࡰһ��������
    if (number >= 10 && number <= 19) {
      str = str.substring(1);
    }
    return str;
  }

  /**
   * ������������ת��Ϊȫ������
   * 
   * @param number
   *          ��ת��������
   * @return ת������ַ���
   */
  public static String intToFullWidth(int number) {
    char[] arrChar = String.valueOf(number).toCharArray();
    for (int i = 0; i < arrChar.length; i++) {
      char ch = arrChar[i];
      switch (ch) {
        case '0':
          arrChar[i] = FULL_WIDTH_NUMBERS[0];
          break;
        case '1':
          arrChar[i] = FULL_WIDTH_NUMBERS[1];
          break;
        case '2':
          arrChar[i] = FULL_WIDTH_NUMBERS[2];
          break;
        case '3':
          arrChar[i] = FULL_WIDTH_NUMBERS[3];
          break;
        case '4':
          arrChar[i] = FULL_WIDTH_NUMBERS[4];
          break;
        case '5':
          arrChar[i] = FULL_WIDTH_NUMBERS[5];
          break;
        case '6':
          arrChar[i] = FULL_WIDTH_NUMBERS[6];
          break;
        case '7':
          arrChar[i] = FULL_WIDTH_NUMBERS[7];
          break;
        case '8':
          arrChar[i] = FULL_WIDTH_NUMBERS[8];
          break;
        case '9':
          arrChar[i] = FULL_WIDTH_NUMBERS[9];
          break;
      }
    }
    return new String(arrChar);
  }

  /**
   * ������������ת��ΪӢ����ĸ
   * 
   * @param number
   *          ��ת��������
   * @param isUpperCase
   *          �Ƿ�ת��Ϊ��д��Ϊtrue��ʾת��Ϊ��д����֮ת��ΪСд
   * @return ת������ַ���
   */
  public static String intToLetter(int number, boolean isUpperCase) {
    String strNumber = Integer.toString(number, LOWER_CASE_LETTERS.length()).toLowerCase();
    char[] arrChar = strNumber.toCharArray();
    for (int i = 0; i < arrChar.length; i++) {
      char ch = arrChar[i];
      int index = HALF_WIDTH_NUMBERS.indexOf(ch);
      if (index >= 0) {
        // ���ֵ����λ��Ҫ���⴦��
        if (i == 0) {
          arrChar[i] = LOWER_CASE_LETTERS.charAt(index - 1);
        } else {
          arrChar[i] = LOWER_CASE_LETTERS.charAt(index);
        }
      } else {
        index = LOWER_CASE_LETTERS.indexOf(ch);
        if (index >= 0) {
          arrChar[i] = LOWER_CASE_LETTERS.charAt(index + HALF_WIDTH_NUMBERS.length());
        }
      }
    }
    String result = new String(arrChar);
    if (isUpperCase) {
      result = result.toUpperCase();
    }
    return result;
  }

  /**
   * ������������ת��Ϊ��֧
   * 
   * @param number
   *          ��ת��������
   * @return ת������ַ���
   */
  public static String intToGanZhi(int number) {
    int len1 = IDENTIFIER_TIANGAN.length();
    int len2 = IDENTIFIER_DIZHI.length();
    return String.valueOf(IDENTIFIER_TIANGAN.charAt(number % len1)) + String.valueOf(IDENTIFIER_DIZHI.charAt(number % len2));
  }

  /**
   * ��ȡ�ַ����ļ���ֵ
   * 
   * @param string
   *          �ַ���
   * @param digestType
   *          �������ͣ�֧�֣�MD5��SHA��SHA-224��SHA-256��SHA-384��SHA-512
   * @return �ַ����ļ���ֵ
   */
  public static String getStringDigest(String string, String digestType) {
    if (isTextEmpty(string)) {
      return "";
    }
    MessageDigest digest = null;
    StringBuilder hex = new StringBuilder();
    try {
      digest = MessageDigest.getInstance(digestType);
      byte[] bytes = digest.digest(string.getBytes("utf-8"));
      String hexStr = "0123456789abcdef";
      for (int i = 0; i < bytes.length; i++) {
        // �ֽڸ�4λ
        hex.append(String.valueOf(hexStr.charAt((bytes[i] & 0xF0) >> 4)));
        // �ֽڵ�4λ
        hex.append(String.valueOf(hexStr.charAt(bytes[i] & 0x0F)));
      }
    } catch (Exception x) {
      // x.printStackTrace();
    }
    return hex.toString();
  }

  /**
   * ��ȡ�ļ��ļ���ֵ
   * 
   * @param file
   *          �ļ�
   * @param digestType
   *          �������ͣ�֧�֣�MD5��SHA��SHA-224��SHA-256��SHA-384��SHA-512
   * @return �ļ��ļ���ֵ
   */
  public static String getFileDigest(File file, String digestType) {
    if (!file.isFile()) {
      return "";
    }
    MessageDigest digest = null;
    FileInputStream in = null;
    byte[] buffer = new byte[1024];
    int len;
    try {
      digest = MessageDigest.getInstance(digestType);
      in = new FileInputStream(file);
      while ((len = in.read(buffer)) != -1) {
        digest.update(buffer, 0, len);
      }
      in.close();
    } catch (Exception x) {
      // x.printStackTrace();
      return "";
    }
    BigInteger bigInt = new BigInteger(1, digest.digest());
    return bigInt.toString(16);
  }

  /**
   * ����ļ��Լ����ڵ�Ŀ¼�Ƿ����
   * 
   * @param file
   *          �������ļ�
   * @return ������ļ��Ƿ���ڣ�������ڷ���true����֮��Ϊfalse
   */
  public static boolean checkFile(File file) {
    File fileParent = new File(file.getParent()); // ��ȡ�ļ��ĸ�Ŀ¼
    if (!fileParent.exists()) {
      fileParent.mkdirs(); // �����Ŀ¼�����ڣ��򴴽�֮
    }
    return file.exists();
  }
}
