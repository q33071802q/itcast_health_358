package com.itheiam;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
public class QiNiuDemo {
    public static void main1(String[] args) {
//        //构造一个带指定 Region 对象的配置类
//        Configuration cfg = new Configuration(Region.huadong());
////...其他参数参考类注释
//
//        UploadManager uploadManager = new UploadManager(cfg);
////...生成上传凭证，然后准备上传
//        String accessKey = "_E6hY6otHUkNMpJAVf_32i2SU7GxEfUe8s3f8hyM";
//        String secretKey = "e-aaythGI0AoeAHwlrYnbERKzpAeO3jshUndyGr0";
//        //空间名称
//        String bucket = "hm358";
////如果是Windows情况下，格式是 D:\\qiniu\\test.png
//        //文件的路径
//        String localFilePath = "C:\\Users\\sun\\Desktop\\1.jpg";
////默认不指定key的情况下，以文件内容的hash值作为文件名
//        //文件名
//        String key = null;
//
//        Auth auth = Auth.create(accessKey, secretKey);
//        String upToken = auth.uploadToken(bucket);
//
//        try {
//            Response response = uploadManager.put(localFilePath, key, upToken);
//            //解析上传成功的结果
//            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
//            System.out.println(putRet.key);
//            System.out.println(putRet.hash);
//        } catch (QiniuException ex) {
//            Response r = ex.response;
//            System.err.println(r.toString());
//            try {
//                System.err.println(r.bodyString());
//            } catch (QiniuException ex2) {
//                //ignore
//            }
//        }

    }


    public static void main(String[] args) {
//        //构造一个带指定 Region 对象的配置类
//        Configuration cfg = new Configuration(Region.region0());
////...其他参数参考类注释
//
//        String accessKey = "_E6hY6otHUkNMpJAVf_32i2SU7GxEfUe8s3f8hyM";
//        String secretKey = "e-aaythGI0AoeAHwlrYnbERKzpAeO3jshUndyGr0";
//
//        String bucket = "hm358";
//        String key = "FuM1Sa5TtL_ekLsdkYWcf5pyjKGu";
//
//        Auth auth = Auth.create(accessKey, secretKey);
//        BucketManager bucketManager = new BucketManager(auth, cfg);
//        try {
//            bucketManager.delete(bucket, key);
//        } catch (QiniuException ex) {
//            //如果遇到异常，说明删除失败
//            System.err.println(ex.code());
//            System.err.println(ex.response.toString());
//        }

    }

}
