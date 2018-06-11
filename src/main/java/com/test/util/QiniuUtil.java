package com.test.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;

public class QiniuUtil {
    
    //设置账号的ACCESS_KEY和SECRET_KEY
    private static final String ACCESS_KEY = "ONduN65OPfjOn1Nl8Cn--EJ4ugP12VfPJSUv_xLB";
    private static final String SECRET_KEY = "qk5eJrcdF72kI5uhDQNW1t17qXWCRF0vrhaa5Ai7";
    
    static String bucketname = "private";
    
    //上传文件路径
    String FilePath = "src/test.png";
    
    //密钥配置
    static Auth auth = Auth.create(QiniuUtil.ACCESS_KEY, QiniuUtil.SECRET_KEY);
    
    //创建上传对象
    UploadManager uploadManager = new UploadManager();
    
    static BucketManager bucketManager = new BucketManager(auth);
    
    //简单上传，使用默认策略，只需要设置上传的空间名就可以
    public String getUpToken() {
        return auth.uploadToken(bucketname);
    }
    
    public void upload() throws IOException {
        try {
            Response res = uploadManager.put(FilePath, "test2", getUpToken());
            System.out.println(res.bodyString());
        } catch (QiniuException e) {
            System.out.println(e.toString());
            System.out.println(e.response.bodyString());
        }
    }
    
    public void upload2() throws IOException {
        try {
            DefaultPutRet res = bucketManager.fetch("http://7xq7ik.com1.z0.glb.clouddn.com/testimage", "public", "testimg");
            System.out.println(res);
        } catch (QiniuException e) {
            System.out.println(e.toString());
            System.out.println(e.response.bodyString());
        }
    }
    
    public void download(String targetUrl) throws IOException {
        String downloadUrl = auth.privateDownloadUrl(targetUrl);
        String filePath = "E:\\";
        System.out.println(downloadUrl);
        OkHttpClient client = new OkHttpClient();
        Request req = new Request.Builder().url(downloadUrl).build();
        okhttp3.Response resp = null;
        resp = client.newCall(req).execute();
        System.out.println(resp.isSuccessful());
        if (resp.isSuccessful()) {
            ResponseBody body = resp.body();
            InputStream is = body.byteStream();
            ByteArrayOutputStream writer = new ByteArrayOutputStream();
            byte[] buff = new byte[1024 * 2];
            int len = 0;
            try {
                while ((len = is.read(buff)) != -1) {
                    writer.write(buff, 0, len);
                }
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            byte[] data = writer.toByteArray();
            File pngFile = new File(filePath + "/test/ss.png");
            FileOutputStream fos = new FileOutputStream(pngFile);
            fos.write(data);
            fos.close();
        }
    }
    
    public static void main(String[] args) throws IOException {
//        new QiniuUtil().upload();
//        new QiniuUtil().upload2();
//        bucketManager.delete("public", "testimg");
//        bucketManager.
        new QiniuUtil().download("http://ohqmxaqtq.bkt.clouddn.com/test2");
    }
    
}
