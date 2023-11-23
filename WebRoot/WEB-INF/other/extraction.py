# -*- coding: utf-8 -*-

#顺序读取文字和表格、图片内容
import os
import docx
from docx.document import Document
from docx.oxml.table import CT_Tbl
from docx.oxml.text.paragraph import CT_P
from docx.table import _Cell, Table
from docx.text.paragraph import Paragraph

import base64
import sys
import argparse

#定义参数
parse = argparse.ArgumentParser(description="神经网络相关工具部件")
parse.add_argument('--path', type=str, required=False, help="要解析的文件url")
parse.add_argument('--name', type=str, required=False, help="要解析的文件名")
args = parse.parse_args()

def iter_block_items(parent):
    """
    Yield each paragraph and table child within *parent*, in document order.
    Each returned value is an instance of either Table or Paragraph. *parent*
    would most commonly be a reference to a main Document object, but
    also works for a _Cell object, which itself can contain paragraphs and tables.
    """
    if isinstance(parent, Document):
        parent_elm = parent.element.body
    #     elif isinstance(parent, _Cell):#感觉没用上
    #         parent_elm = parent._tc
    else:
        raise ValueError("something's not right")

    for child in parent_elm.iterchildren():
        if isinstance(child, CT_P):
            yield Paragraph(child, parent)
            # print(Paragraph(child, parent))
        elif isinstance(child, CT_Tbl):
            yield Table(child, parent)
            # print(Table(child, parent))

def read_table(table):
    return [[cell.text for cell in row.cells] for row in table.rows]


def extract_text_and_images(paragraph,path):
    base64_data = None
    isPicture = False
    # 检查段落是否包含图片
    for run in paragraph.runs:
        # 获取运行元素的XML表示
        run_xml = run._r
        # 检查是否包含图片
        if '<w:drawing>' in run_xml.xml:
            isPicture = True
            # print("found an image")
            # 解析图片信息
            image_start = run_xml.xml.find('<w:drawing>')
            image_end = run_xml.xml.find('</w:drawing>') + len('</w:drawing>')
            image_xml = run_xml.xml[image_start:image_end]

            #获取关联id
            image_id_start = image_xml.find('r:embed="') + len('r:embed="')
            image_id_end = image_xml.find('"', image_id_start)
            image_id = image_xml[image_id_start:image_id_end]

            d=docx.Document(path)
            related_part = d.part.related_parts[image_id]
            #获取图片
            image= related_part.image
            image_format = image.ext
            #转换为base64
            base64_data = base64.b64encode(image.blob).decode('utf-8')

            imageStr = "data:image/"+image_format+";base64,"+ base64_data

    if(isPicture):
        print ({"image"},{paragraph.style.name},imageStr)
        # print (base64_data)
    else:
        if paragraph.text!="":
            print ({"text"},{paragraph.style.name},paragraph.text.encode('gbk', 'ignore').decode('gbk'))
        # print (paragraph.text.encode('gbk', 'ignore').decode('gbk'))
def read_word(word_path):
    doc = docx.Document(word_path)
    for block in iter_block_items(doc):
        if isinstance(block, Paragraph):#Paragraph（对象）
            extract_text_and_images(block,word_path)
        elif isinstance(block, Table):#Table对象
            print({"table"},{"表格"},read_table(block))
            # print(read_table(block))



if __name__ == '__main__':
    # word_path = "E:\\lab\\file-extraction\项目文件\\案例 (1)\\案例\\批复的立项方案_测试.docx"
    word_path = args.path.strip( '\"' )
    file_name = args.name.strip( '\"' )
    print({"text"},{"Heading 0"},file_name)
    print({"text"},{"Heading 1"},"封面内容")
    read_word(word_path)