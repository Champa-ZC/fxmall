package com.qianfeng.fxmall.members.mapper;

import com.qianfeng.fxmall.members.bean.WxbMemeber;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MemberMapper {

    WxbMemeber checkUsername(@Param("account") String account);
}
