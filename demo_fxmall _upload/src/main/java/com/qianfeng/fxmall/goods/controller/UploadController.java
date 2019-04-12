package com.qianfeng.fxmall.goods.controller;

import com.qianfeng.fxmall.commons.Utils.MD5Utils;
import com.qianfeng.fxmall.goods.po.TbImages;
import com.qianfeng.fxmall.goods.service.impl.ImageServiceImpl;
import com.qianfeng.fxmall.goods.vo.JsonResultVO;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * version 20190412
 */
@Controller
public class UploadController {

    @Resource
    private ImageServiceImpl imageService;

    @PostMapping("/upload")
    @ResponseBody
    public JsonResultVO upload(MultipartFile uploadFile) throws IOException{

        JsonResultVO jsonResultVO = new JsonResultVO();
        String imageFileName = uploadFile.getOriginalFilename();
        System.out.println(imageFileName);
        FileOutputStream fileInputStream = null;



        //获取上传文件的MD5值
        byte[] bytes = uploadFile.getBytes();
        String md5 = MD5Utils.md5(bytes);
        System.out.println("文件的MD5值："+md5);

        try{
            //获取数据库是否有相同的MD5文件
            TbImages tbImages = imageService.queryFileMD5(md5);
            //如果数据库没有这个文件，就将文件信息存入数据库，并且完成FTP上传文件
            if (null == tbImages) {

                long timeMillis = System.currentTimeMillis();
                try {
                    //当前项目的路径
                    String imagePath = "F:\\apache-tomcat-8.0.52\\webapps\\images\\";
                    File file = new File(imagePath);
                    //当文件不存在的时候创建文件
                    if (!file.exists()){
                        file.mkdir();
                    }

                    //创建FTP工具类
                    try {

                        FTPClient ftp = new FTPClient();
                        //连接FTP服务器，默认端口是21
                        ftp.connect("192.168.53.9",21);
                        //匿名用户必须使用anonymous登录，密码是邮箱
                        boolean login = ftp.login("anonymous", "1270264305@qq.com");
                        System.out.println(login);

                        int replyCode = ftp.getReplyCode();
                        if (!FTPReply.isPositiveCompletion(replyCode)) {
                            System.out.println("获取响应失败");
                            return null;
                        }
                        //设置接收文件类型为二进制文件
                        ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
                        //服务器上创建images文件夹
                        ftp.makeDirectory("images");
                        //切换ftp默认文件夹
                        ftp.changeWorkingDirectory("images");
                        //获取内存文件
                        InputStream inputStream = uploadFile.getInputStream();

                        //将用户上传的图片上传到ftp服务器上
                        ftp.storeFile(timeMillis+ imageFileName, inputStream);
                        //退出登录
                        ftp.logout();

                        jsonResultVO.setCode(1);
                        //"http://localhost/images/"这是nginx图片服务器的绝对路径
                        jsonResultVO.setData("http://localhost/images/"+timeMillis+ imageFileName);

                        //第一次上传图片完毕之后，需要将图片写入数据库
                        TbImages tbImagesPO = new TbImages();
                        tbImagesPO.setImageMd5(md5);
                        tbImagesPO.setImageUrl("http://localhost/images/"+timeMillis+ imageFileName);
                        imageService.saveImage(tbImagesPO);
                    } catch (IOException e) {
                        e.printStackTrace();
                        jsonResultVO.setCode(0);
                        jsonResultVO.setData("上传失败");
                    }


                }  finally {
                    if (fileInputStream != null) {
                        try {
                            fileInputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                //如果数据库中有这一张图片，则将存在的图片信息返回给用户
                jsonResultVO.setCode(1);
                jsonResultVO.setData(tbImages.getImageUrl());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonResultVO;
    }
}
