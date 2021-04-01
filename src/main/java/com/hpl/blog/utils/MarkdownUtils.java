package com.hpl.blog.utils;

import org.commonmark.Extension;
import org.commonmark.ext.gfm.tables.TableBlock;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.ext.heading.anchor.HeadingAnchorExtension;
import org.commonmark.node.Image;
import org.commonmark.node.Link;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.AttributeProvider;
import org.commonmark.renderer.html.AttributeProviderContext;
import org.commonmark.renderer.html.AttributeProviderFactory;
import org.commonmark.renderer.html.HtmlRenderer;

import java.util.*;

public class MarkdownUtils {

    /**
     * markdown转换html (基本)
     * @param markdown
     * @return
     */
    public static String markdown_to_html(String markdown){
        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdown);

        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(document);
    }

    /**
     * markdown转换html (扩展：1、标题生成id  2、table转html)
     * @param markdown
     * @return
     */
    public static String markdown_to_html_Extensions(String markdown) {
        //1、标题生成id
        Set<Extension> headingAnchorExtensions = Collections.singleton(HeadingAnchorExtension.create());

        //2、table转html
        List<Extension> tableExtension = Arrays.asList(TablesExtension.create());
        Parser parser = Parser.builder()
                .extensions(tableExtension)
                .build();
        Node document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder()
                .extensions(headingAnchorExtensions)
                .extensions(tableExtension)
                .attributeProviderFactory(new AttributeProviderFactory() {
                    public AttributeProvider create(AttributeProviderContext context) {
                        return new CustomAttributeProvider();
                    }
                })
                .build();
        return renderer.render(document);
    }
    /**
     * 处理标签的属性
     */
    static class CustomAttributeProvider implements AttributeProvider {
        @Override
        public void setAttributes(Node node, String tagName, Map<String, String> attributes) {
            //改变a标签的target属性为_blank
            if (node instanceof Link) {
                attributes.put("target", "_blank");
            }
            //若是table，则增加css属性
            if (node instanceof TableBlock) {
                attributes.put("class", "ui celled table");
            }
            //若是图片，则修改大小后
            if(node instanceof Image){
                attributes.put("style", "width:70%;height:70%;");
            }
        }
    }

    public static void main(String[] args){

        String HeadId= "# 如何看待 Typo.css\n" +
                "## fdfsdf\n" +
                "#### dfgdf\n" +
                "- gd";

        String table = "| hello | hi   | 哈哈哈   |\n" +
                "| ----- | ---- | ----- |\n" +
                "| 斯维尔多  | 士大夫  | f啊    |\n" +
                "| 阿什顿发  | 非固定杆 | 撒阿什顿发 |\n" +
                "\n";

        String link = "[imCoding 爱编程](http://www.lirenmi.cn)";
        System.out.println(markdown_to_html_Extensions(HeadId));
    }
}
