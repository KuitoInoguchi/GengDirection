package com.nailong.gengdirection.user.service;

import com.nailong.gengdirection.user.dto.LoginDTO;
import com.nailong.gengdirection.user.dto.RegisterDTO;
import com.nailong.gengdirection.user.dto.UserVO;

public interface UserService {
    Long register(RegisterDTO dto);
    UserVO login(LoginDTO dto);
    UserVO getById(Long id);
}
