package com.qianfeng.fxmall.goods.mapper;

import com.qianfeng.fxmall.goods.po.TbImages;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ImageMapper {

    TbImages queryFileMD5(@Param("imageMd5") String md5);

    void saveImage(TbImages images);
}
