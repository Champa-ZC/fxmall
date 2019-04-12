package com.qianfeng.fxmall.goods.service.impl;

import com.qianfeng.fxmall.goods.mapper.ImageMapper;
import com.qianfeng.fxmall.goods.po.TbImages;
import com.qianfeng.fxmall.goods.service.ImageService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ImageServiceImpl implements ImageService {

    @Resource
    private ImageMapper imageMapper;

    @Override
    public TbImages queryFileMD5(String md5) {
        return imageMapper.queryFileMD5(md5);
    }

    @Override
    public void saveImage(TbImages images) {
        imageMapper.saveImage(images);
    }
}
