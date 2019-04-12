package com.qianfeng.fxmall.goods.service;

import com.qianfeng.fxmall.goods.po.TbImages;

public interface ImageService {

    TbImages queryFileMD5(String md5);

    void saveImage(TbImages images);
}
