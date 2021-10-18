0冰雪记事本简介\n\n冰雪记事本是一款纯java开发的文本编辑器。其特点如下：\n1.支持跨平台运行。支持3大操作系统：Windows、Unix/Linux和Macintosh。\n2.编辑功能。完全可替代传统的记事本程序，并在此基础上添加了很多方便实用的功能。\n3.定制功能。支持外观切换、简单的配色方案、高亮显示、自定义快捷键等功能。\n\n软件作者：冰原\nQQ：155222113\n邮箱：chenzhengfeng@163.com\nGitHub代码：https://github.com/xiboliya/snowpad\n码云：https://gitee.com/xiboliya/snowpad\nCoding：https://coding.net/u/xiboliya/p/SnowPad\nCSDN代码：https://code.csdn.net/chenzhengfeng/snowpad\n谷歌代码：http://code.google.com/p/snowpad
1菜单栏#位于界面顶端，几乎所有的功能都会在此菜单栏中找到。
2文件#此菜单中包含一些与文件相关的功能。
3新建#在编辑区新建一个文本域以供用户输入。
3打开#打开一个或多个位于硬盘上的文件。
3以指定编码打开#可以指定编码（自动检测、ANSI、UTF-8、UTF-8 No BOM、Unicode Little Endian、Unicode Big Endian和GB18030）打开一个或多个位于硬盘上的文件。
3保存#将当前编辑的文件保存到硬盘。
3另存为#为当前编辑的文件保存一个副本。
3重命名#重命名当前编辑的文件，包括文件名或文件路径。
3重新载入文件#从当前硬盘上重新读取当前编辑的文件，并刷新显示。
3删除当前文件#从硬盘上删除当前编辑的文件。
3关闭当前#关闭当前编辑的文件。
3关闭其它#关闭除了当前编辑的所有其它文件。
3关闭左侧#关闭当前编辑文件的所有左侧文件。
3关闭右侧#关闭当前编辑文件的所有右侧文件。
3关闭全部#关闭编辑区的所有文件。
3冻结文件#冻结当前文件，亦即禁止修改当前文件。
3打印#打印当前文件。
3最近编辑#此菜单中会显示最近15个打开过的文件。
3清空最近编辑列表#清空最近编辑列表中的所有记录。
3退出#退出本程序。
2编辑#此菜单中包含一些与文件编辑相关的功能。
3撤销#撤销当前文本域中最近一次的编辑。
3重做#恢复当前文本域中最近一次的撤销操作。
3剪切#剪切当前文本域中选择的文本。
3复制#复制当前文本域中选择的文本。
3粘贴#将剪贴板中的文本粘贴到当前文本域中。
3全选#选择当前文本域中所有文本。
3删除#删除当前文本域中选择的文本。
3拆分文件#通过指定字符串作为关键字，将当前文件或指定文件拆分为多个子文件。
3切换大小写#切换当前文本域中选择文本的大小写。
4切换为大写#切换当前文本域中选择文本为大写。
4切换为小写#切换当前文本域中选择文本为小写。
3复制到剪贴板#将指定的文本复制到系统剪贴板。
4当前文件名#将当前编辑的文件名复制到剪贴板。
4当前文件路径#将当前编辑的文件完整路径复制到剪贴板。
4当前目录路径#将当前编辑的目录路径复制到剪贴板。
4所有文本#将当前编辑的所有文本复制到剪贴板。
3操作行#与当前选区或光标所处的行相关的操作。
4复写当前行#复写当前选区所处的行。
4删除当前行#删除当前选区所处的行。
4删除重复行#删除当前选区内重复的行。
4删除至行首#将当前行中光标之前的文本删除。
4删除至行尾#将当前行中光标之后的文本删除。
4删除至文件首#将当前文件中光标之前的文本删除。
4删除至文件尾#将当前文件中光标之后的文本删除。
4上移当前行#将当前选区所处的行向上移动一行，原先前面的一行移动到该选区的后面。
4下移当前行#将当前选区所处的行向下移动一行，原先后面的一行移动到该选区的前面。
4复制当前行#将当前选区所处的行复制到剪贴板。
4剪切当前行#将当前选区所处的行剪切到剪贴板。
3批处理行#批量处理当前选区所处的每一行。
4切除#将当前选区所处的每一行从行首/行尾删除指定数目的字符。
4插入#将当前选区所处的每一行从行首/行尾插入指定的文本。
4分割行#将当前选区所处的每一行从行首/行尾在指定偏移量处分割成两行。
4拼接行#将当前选区所处的每一行按指定的行数拼接成一行。
4合并行#将当前选区所处的所有行合并为一行。
4逐行复写#将当前选区的所有行，以行为单位，进行逐行复写。
3排序#将当前选区所处的行进行排序。
4升序#将当前选区所处的行进行升序排序。
4降序#将当前选区所处的行进行降序排序。
4反序#将当前选区所处的行进行反序排序。
3缩进#将当前选区所处的行进行缩进或退格。
4缩进#将当前选区所处的行进行缩进。一次缩进一个Tab键位或等同于一个Tab键位的多个空格。
4退格#将当前选区所处的行进行退格。一次删除每行首一个Tab键位或等同于一个Tab键位的多个空格。
3清除空白#删除当前行或当前选区所在行的指定区域的空白字符。其中空白字符包括：空格、中文全角空格与Tab字符。
4行首#删除当前行或当前选区所在行的行首空白字符。
4行尾#删除当前行或当前选区所在行的行尾空白字符。
4行首+行尾#删除当前行或当前选区所在行的行首与行尾空白字符。
4选区内#删除当前选区内的所有空白字符。
3删除空行#删除指定范围的空行。空行是指整行内没有任何字符的行。
4全文范围#删除全文范围的空行。
4选区范围#删除选区范围的空行。
3添加注释#为各种编程语言的源文件快速添加注释。
4单行注释#为各种编程语言的源文件快速添加单行注释。常见的单行注释有：\nada、lua、sql --\nc、cpp、cs、go、h、java、js、jsp、m、pas、php //\ninf、ini、lisp、reg ;\nmk、pl、py、rb、sh 井号\nbat ::\nasp、vb '
4区块注释#为各种编程语言的源文件快速添加区块注释。常见的区块注释有：\nc、cpp、cs、css、go、h、java、js、jsp、m、php、sql /* */\nhtm、html、xml <!-- -->\nlua --[[ --]]\npas { }\npl =pod =cut\npy ''' '''\nrb ==begin ==end\nsh :<<'COMMENT' COMMENT\nst " "
3插入#向当前文本域中插入特殊字符或时间/日期。
4特殊字符#向当前文本域中插入特殊字符。\n那特殊字符共分为：特殊符号、标点符号、数学符号、单位符号、数字符号和拼音符号。
4时间/日期#向当前文本域中插入时间/日期。\n共有十余种预置格式可供选择，另外还可以使用自定义格式。
3选区操作#对当前选区的一些操作。
4复写选区字符#复写当前选区的全部字符。
4反转选区字符#将当前选区的全部字符反序重排。
2搜索#此菜单中包含一些与查找、替换等相关的功能。并且分为：普通、转义扩展和正则表达式共3种模式。
3查找#在当前文本域中查找指定字符串。
3查找下一个#在当前文本域中向后查找指定字符串。
3查找上一个#在当前文本域中向前查找指定字符串。
3选定查找下一个#在当前文本域中向后查找当前选择的字符串，并将当前选择的字符串作为默认查找的字符串。
3选定查找上一个#在当前文本域中向前查找当前选择的字符串，并将当前选择的字符串作为默认查找的字符串。
3快速查找#在当前文本域中快速查找当前选择的字符串。
4快速向下查找#在当前文本域中快速向后查找当前选择的字符串，而并不改变默认查找的字符串。
4快速向上查找#在当前文本域中快速向前查找当前选择的字符串，而并不改变默认查找的字符串。
3替换#将当前文本域中某字符串替换为指定字符串。
3转到#转到指定的行号、偏移量或百分比。
3定位匹配括号#如果当前光标处是括号，则跳转到匹配的另一半括号所在的位置。\n其中支持的括号包括：()[]{}<>。
2格式#此菜单中包含一些与换行、字符编码、字体和Tab键等相关的功能。
3换行方式#在自动换行的情况下，换行方式的设置。
4单词边界换行#在自动换行时，以单词边界方式换行。即保证每个单词完整的处于同一行中。
4字符边界换行#在自动换行时，以字符边界方式换行。即只会在合适的位置换行，而不考虑单词的完整性。
3换行符格式#设置当前文件的换行符格式。共有3种格式可供选择：Windows、Unix/Linux和Macintosh格式。
4Windows格式#设置当前文件的换行符格式为Windows格式，此是Windows系统的默认格式，换行符为：\ r \ n。
4Unix/Linux格式#设置当前文件的换行符格式为Unix/Linux格式，此是Unix/Linux系统的默认格式，换行符为：\ n。
4Macintosh格式#设置当前文件的换行符格式为Macintosh格式，此是Macintosh系统的默认格式，换行符为：\ r。
3字符编码格式#设置当前文件的编码格式。共有6种格式可供选择：默认格式(GB18030)、ANSI、UTF-8、UTF-8 No BOM、Unicode Little Endian和Unicode Big Endian格式。
4默认格式(GB18030)#设置当前文件的编码格式为GB18030格式，此为默认格式。GB18030格式是继GB2312与GBK之后的支持汉字最多的新编码格式。
4ANSI格式#设置当前文件的编码格式为ANSI格式。ANSI格式是最简单的编码格式，它只支持ASCII字符，而并不支持中文。
4UTF-8格式#设置当前文件的编码格式为UTF-8格式。使用此格式编码的文件开头会有BOM，共有3个字节：0xef 0xbb 0xbf。
4UTF-8 No BOM格式#设置当前文件的编码格式为UTF-8 No BOM格式。此格式与UTF-8格式编码规则相同，不过此格式的文件开头没有BOM。
4Unicode Little Endian格式#设置当前文件的编码格式为Unicode Little Endian格式。使用此格式编码的文件开头会有BOM，共有2个字节：0xff 0xfe。
4Unicode Big Endian格式#设置当前文件的编码格式为Unicode Big Endian格式。使用此格式编码的文件开头会有BOM，共有2个字节：0xfe 0xff。
3列表符号与编号#为当前选择的行，依次在各行首添加递增的编号或固定的符号。\n编号有：十进制数、小写/大写十六进制数、干支计数。\n符号有：﹟·※＊§¤⊙◎○●△▲▽▼◇◆□■☆★。
3字体#设置文本域的字体。包括：字体类型、字体格式和字体大小。\n字体类型：包括当前操作系统上的所有字体。\n字体格式：常规、粗体、斜体和粗斜体。\n字体大小：8~100像素。
3Tab键设置#Tab键的相关设置。\n设置Tab键所占字符数，范围是：1~99个字符。\n还可设置是否可以空格代替Tab键，设置后，当按下Tab键时，系统会以相应个数的空格代替Tab字符。
3自动完成#设置成对字符的自动输入，包括：小括号()、中括号[]、大括号{}、尖括号<>、单引号''、双引号""。
3快捷键管理#管理所有菜单的快捷键。\n可以编辑和清除列表中所有的快捷键。
3文本拖拽#此功能开启后，允许在文本域中用鼠标拖拽选中的文本到其他位置。
3自动缩进#此功能开启后，当按回车键换行时，系统会根据前一行的行首空白进行缩进。空白字符包括：空格、Tab字符与全角空格字符。
3恢复默认设置#恢复软件所有设置项为默认值。
2查看#此菜单中包含一些与界面显示相关的功能。
3后退#后退到当前文本域中光标所在的前一个历史位置。
3前进#前进到当前文本域中光标所在的后一个历史位置。
3工具栏#开启此设置后，则会显示文本域顶部的工具栏。
3状态栏#开启此设置后，则会显示文本域底部的状态栏。
3行号栏#开启此设置后，则会显示文本域左侧的行号栏。\n注：目前还不够完善，此功能与“自动换行”功能不能同时使用。如要显示行号栏，则必须首先取消“自动换行”功能。
3查找结果#开启此设置后，则会显示文本域底部的查找结果面板。
3前端显示#开启此设置后，本软件将始终处于其他程序界面的顶部。
3锁定窗口#开启此设置后，用户将无法改变程序窗口的大小。用户只能最小化，而不能最大化或使用鼠标调整。
3标签设置#显示文本域的标签栏相关的设置。
4多行标签#开启此设置后，当文本域标签栏的宽度大于一行时，则会自动分行显示。如关闭此设置，当宽度大于一行时，则会在标签栏右侧显示左右切换的按钮。
4双击关闭标签#开启此设置后，用鼠标双击某标签，则会将其关闭。
4指示图标#开启此设置后，标签上会显示文件状态指示图标。
3字体缩放#更改所有文本域中字体的大小。范围是：8~100。
4放大#将文本域中的字体放大一个像素。
4缩小#将文本域中的字体缩小一个像素。
4恢复初始#将文本域中的字体恢复为默认大小。
3颜色设置#设置文本域中各状态下字体、背景和光标的颜色，包括：字体、背景、光标、选区字体、选区背景颜色、匹配括号背景颜色、当前行背景颜色等。
4字体颜色#设置普通状态下的字体颜色。
4背景颜色#设置普通状态下的背景颜色。
4光标颜色#设置光标颜色。
4选区字体颜色#设置选区内的字体颜色。
4选区背景颜色#设置选区内的背景颜色。
4匹配括号背景颜色#设置匹配括号的背景颜色。
4当前行背景颜色#设置光标所在行的背景颜色。
4全部反色#将文本域中各状态下字体、背景和光标的颜色进行反色设置。
4全部补色#将文本域中各状态下字体、背景和光标的颜色进行补色设置。
3配色方案#设置预置的配色方案，包括：字体、背景、光标、选区字体、选区背景颜色、匹配括号背景颜色、当前行背景颜色等。
4配色方案1#设置第1种预置的配色方案。
4配色方案2#设置第2种预置的配色方案。
4配色方案3#设置第3种预置的配色方案。
4配色方案4#设置第4种预置的配色方案。
4配色方案5#设置第5种预置的配色方案。
4恢复默认配色#恢复成默认的配色方案。
3高亮显示#在文本域中查找所有与当前选区相同的字符串，并将所有出现的文本的背景色设置为指定颜色。
4格式1#将所有与当前选区相同的文本的背景色设置为预置的第1种颜色。
4格式2#将所有与当前选区相同的文本的背景色设置为预置的第2种颜色。
4格式3#将所有与当前选区相同的文本的背景色设置为预置的第3种颜色。
4格式4#将所有与当前选区相同的文本的背景色设置为预置的第4种颜色。
4格式5#将所有与当前选区相同的文本的背景色设置为预置的第5种颜色。
3清除高亮#清除已经使用的高亮显示。
4格式1#清除已经使用的第1种高亮显示。
4格式2#清除已经使用的第2种高亮显示。
4格式3#清除已经使用的第3种高亮显示。
4格式4#清除已经使用的第4种高亮显示。
4格式5#清除已经使用的第5种高亮显示。
4所有格式#清除已经使用的所有高亮显示。
3切换外观#切换当前软件的外观显示。
4Metal#将当前外观切换为Metal，此外观为java默认经典外观，效率高。可跨平台。
4Nimbus#将当前外观切换为Nimbus，此外观较美观。可跨平台。
4CDE/Motif#将当前外观切换为CDE/Motif，此外观不太美观，但效率高。可跨平台。
4GTK+#将当前外观切换为GTK+，此外观为Linux系统下的一种外观，较美观。不支持跨平台，只能在某些Linux系统下使用。
4Windows#将当前外观切换为Windows，此外观为Windows风格外观，较美观。不支持跨平台，只能在Windows系统下使用。
4Windows Classic#将当前外观切换为Windows Classic，此外观为Windows经典外观，不太美观，但效率相对较高。不支持跨平台，只能在Windows系统下使用。
3文档切换#在主界面的选项卡组件中，切换当前编辑的文档。
4向前切换#在主界面的选项卡组件中，切换至当前编辑的前一个文档。
4向后切换#在主界面的选项卡组件中，切换至当前编辑的后一个文档。
4前一个文档#在主界面的选项卡组件中，切换至历史编辑的前一个文档。
4后一个文档#在主界面的选项卡组件中，切换至历史编辑的后一个文档。
3统计信息#显示当前的文件信息与当前编辑的文本域信息。
3窗口管理#管理当前编辑的所有文档，包括：激活、保存、关闭和排序等功能。
2工具#此菜单中包含一些工具类功能。
3加密#查看指定文本或文件的加密值，包括：MD5、SHA、SHA-224、SHA-256、SHA-384、SHA-512加密算法。
3进制转换#将数字在各种进制之间转换，范围包括：二进制~三十六进制。
3计算器#简单的计算器，主要功能包括：整数和小数的＋－×÷四则运算，百分比%运算，圆周率π运算等。
3切割文件#通过指定的切割大小或切割文件数量，将指定文件切割为多个子文件。
3拼接文件#将多个文件拼接为一个文件。
2帮助#此菜单中包含一些与帮助主题和关于等相关的功能。
3帮助主题#此功能包括所有本软件的功能介绍。
3关于#显示软件基本信息的界面。包括软件名称、作者、版本号以及本项目网址等。
1工具栏#位于菜单栏与文本域区域之间的带有图标的一行功能按钮。\n各按钮功能与应对菜单相同，其从左到右依次为：新建、打开、保存、另存为、关闭、关闭全部、剪切、复制、粘贴、撤销、重做、查找、替换、字体放大、字体缩小、后退、前进、自动换行。
1编辑区#此区域包括文本域和标签栏。\n文本域用于编辑文本文件，标签栏用于显示编辑的文件名。
1状态栏#位于编辑区的下方，主要显示当前文件的状态。其共分4个区域，从左到右依次为：全文状态、当前状态、文件换行符和文件编码。
2全文状态#显示文本域中的总字符数和总行数。其中一个中文或英文字符均算一个字符。
2当前状态#显示当前光标所在的行号、列号，以及当前选区的字符数。
2文件换行符#显示当前文件的换行符。
2文件编码#显示当前文件的编码格式。