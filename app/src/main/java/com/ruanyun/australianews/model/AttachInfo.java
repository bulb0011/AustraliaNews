package com.ruanyun.australianews.model;

import android.os.Parcel;

import com.ruanyun.imagepicker.imagelist.ImageUrlGetter;
import com.ruanyun.australianews.util.FileUtil;


/**
 * @author hdl
 * @description 附件表
 * @date 2019/3/7
 */
public class AttachInfo implements ImageUrlGetter {
   public int attachId;//	图片id
   public String createTime;//	创建时间
   public String fileName;//	文件名称
   public String filePath;//	文件路径
   public String fileSize;//	文件大小
   public String glNum;//	对应表记录的num
   public String userNum;//	用户编号

   @Override
   public String getImageUrl() {
      return FileUtil.getImageUrl(filePath);
   }

   public AttachInfo() {
   }

   @Override
   public int describeContents() {
      return 0;
   }

   @Override
   public void writeToParcel(Parcel dest, int flags) {
      dest.writeInt(this.attachId);
      dest.writeString(this.createTime);
      dest.writeString(this.fileName);
      dest.writeString(this.filePath);
      dest.writeString(this.fileSize);
      dest.writeString(this.glNum);
      dest.writeString(this.userNum);
   }

   protected AttachInfo(Parcel in) {
      this.attachId = in.readInt();
      this.createTime = in.readString();
      this.fileName = in.readString();
      this.filePath = in.readString();
      this.fileSize = in.readString();
      this.glNum = in.readString();
      this.userNum = in.readString();
   }

   public static final Creator<AttachInfo> CREATOR = new Creator<AttachInfo>() {
      @Override
      public AttachInfo createFromParcel(Parcel source) {
         return new AttachInfo(source);
      }

      @Override
      public AttachInfo[] newArray(int size) {
         return new AttachInfo[size];
      }
   };
}
