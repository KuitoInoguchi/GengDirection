package com.nailong.gengdirection.user.mapper;

import com.nailong.gengdirection.user.entity.UserInfo;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    UserInfo selectByUsername(@Param("username") String username);
    UserInfo selectById(@Param("id") Long id);
    int insert(UserInfo userInfo);

}
