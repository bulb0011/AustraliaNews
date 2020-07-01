package com.ruanyun.australianews.util;

/**
 * @author hdl
 * @description
 * @date 2019/9/4
 */
public class HtmlFormat {

    public static String getHtmlContent(String htmlText) {
        String css = "<style type=\"text/css\"> </style>";
//        String imgCss = "<style>p img{width:100% !important}</style>";
        String imgCss = "";
        String header = "<header><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no>" + css + imgCss + "</header>";
        String html = "<html>" + header + "<body>" + htmlText + "</body>" + "</html>";
        return html;
    }

    public static String getHtmlContent1(String htmlText) {
        String content = "<div id=\"div_0\">"+htmlText+"</div>";
        String css = "<style type=\"text/css\"> </style>";
        String imgCss = "<style>#div_0 img{width: 100%;height: auto;}</style>";
        String header = "<header><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no>" + css + imgCss + "</header>";
        String html = "<html>" + header + "<body>" + content + "</body>" + "</html>";
        return html;
    }

    public static String getNewContent(String title, String author, String createtime, String subTitle, String htmlText) {
        String css = "<style type=\"text/css\"> </style>";
        String imgCss = "<style>p img{width:100% !important}body{margin:8px}</style>";
        String header = "<header><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no>" + css + imgCss + "</header>";
        String titleHtml = "<div style=\"font-size: 19px;color: #333333;padding: 7px 7px;\">"+title+"</div><div style=\"margin-top: 5px;margin-bottom:10px;font-size: 14px;color: #333333;background: #F1F2F4;margin-left: -8px;padding: 10px 8px 10px 15px;width: calc(100vw - 23px);\">"+subTitle+"</div><div style=\"height: 28px;line-height: 28px;padding: 0 7px;font-size: 14px;color: #333333;\"><span style=\"float: left;\">"+author+"</span><span style=\"float: left;margin-left: 6px;margin-top: 1px;font-size: 12px;\">"+createtime+"</span></div>";
        String html = "<html>" + header + "<body>" + titleHtml +"<div style='padding:3px 7px'>"+ htmlText + "</div></body>" + "</html>";
        return html;
    }
}