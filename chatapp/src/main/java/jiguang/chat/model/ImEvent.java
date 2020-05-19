package jiguang.chat.model;

import java.io.File;

import cn.jpush.im.android.api.enums.ContentType;

/**
 * @author hdl
 * @description im消息通知
 * @date 2018/11/9
 */
public class ImEvent {
    public String sendUserOid;
    public String receiveUserOid;
    public String content;
    public File msgFile;
    public String type;//ContentType.text.toString()

    public ImEvent(String receiveUserOid, String content, String type) {
        this.receiveUserOid = receiveUserOid;
        this.content = content;
        this.type = type;
    }

    public ImEvent(String receiveUserOid, File msgFile, String type) {
        this.receiveUserOid = receiveUserOid;
        this.msgFile = msgFile;
        this.type = type;
    }
}
