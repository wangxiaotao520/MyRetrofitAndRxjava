package com.huacheng.myretrofit.http.upload;

import io.reactivex.Observer;

/**
 * 上传文件的RxJava2回调.
 *
 * @author devilwwj
 * @since 2017/7/12
 *
 * @param <T> 模板类
 */
public abstract class FileUploadObserver<T> implements Observer<T> {

    private  long all_total_length;
    private int file_size;
    private long file_writen=0;//已经写了的内容
   // private Map<Integer,Long> data_content_length=  new HashMap<>(); //存放文件长度的集合
  //  private int index=-1;
    private  int  LASTSIZE=0;
    @Override
    public void onNext(T t) {
        onUploadSuccess(t);
    }

    @Override
    public void onError(Throwable e) {
        onUploadFail(e);
    }

    @Override
    public void onComplete() {

    }

    // 上传成功的回调
    public abstract void onUploadSuccess(T t);

    // 上传失败回调
    public abstract void onUploadFail(Throwable e);

    // 上传进度回调

    /**
     *
     * @param persent  每个图片的上传进度
     * @param total_persent 总的上传进度
     */
    public abstract void onProgress(int index,int persent,int  total_persent);

    // 监听进度的改变
    public void onProgressChange(int index,long bytesWritten, long contentLength) {
      //  onProgress((int) (bytesWritten * 100 / contentLength));

    //    计算总进度
//        if (this.index!=index){
//            //每多一个加一个
//            data_content_length.put(index,contentLength);
//            this.index=index;
//        }
//        if (index>0){
//            long writen_already=0;//已经写了的文件
//            int flag=0;
//            for (Integer integer : data_content_length.keySet()) {
//                if (flag==data_content_length.size()-1){
//                    break;
//                }
//                flag++;
//                writen_already+=data_content_length.get(integer);
//            }
//            file_writen=bytesWritten+writen_already;
//        }else {
//            file_writen=bytesWritten;
//        }
        //计算总进度
        if (LASTSIZE==100){//TODO 这个方法一开始就被调用了图片的数量次真是乃格兰了，不知道为啥,后期再解决吧
            LASTSIZE=0;
            return;
        }
        int total_persent;
        int percent = (int) (bytesWritten * 100 / contentLength);
        if (percent > 100) percent = 100;
        if (percent < 0) percent = 0;
        if (percent == 100) {
            LASTSIZE += percent / file_size;
        }

        if ((LASTSIZE + percent / file_size) > 100) {
            total_persent = 100;
        } else {
            total_persent = LASTSIZE + percent / file_size;
        }
        onProgress(index, percent, total_persent);
    }

    /**
     * 设置文件总长度和 数量
     * @param all_total_length
     * @param file_size
     */
    public void setAll_total_length(long all_total_length,int file_size) {
        this.all_total_length = all_total_length;
        this.file_size=file_size;
    }

}
