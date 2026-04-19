package com.nailong.gengdirection.user.service.impl;

import com.nailong.gengdirection.exception.GengException;
import com.nailong.gengdirection.user.dto.LoginDTO;
import com.nailong.gengdirection.user.dto.RegisterDTO;
import com.nailong.gengdirection.user.dto.UserVO;
import com.nailong.gengdirection.user.entity.UserInfo;
import com.nailong.gengdirection.user.mapper.UserMapper;
import com.nailong.gengdirection.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    /**
     * BCrypt Encrypter
     */
    private final PasswordEncoder encoder;

    @Override
    public Long register(RegisterDTO dto) {
        // 1. 查重
        if (userMapper.selectByUsername(dto.getUsername()) != null) {
            throw new GengException("用户名已被占用");
        }
        // 2. 加密密码 + 组装实体
        UserInfo user = new UserInfo();
        user.setUsername(dto.getUsername());
        user.setPasswordHash(encoder.encode(dto.getPassword()));
        user.setNickname(dto.getNickname());
        user.setStatus(1);
        // 3. 入库（id 会被自动回填）
        userMapper.insert(user);
        log.info("注册成功 id={} username={}", user.getId(), user.getUsername());
        return user.getId();
    }

    @Override
    public UserVO login(LoginDTO dto) {
        UserInfo user = userMapper.selectByUsername(dto.getUsername());
        if (user == null) {
            throw new GengException("用户名或密码错误");
        }
        if (user.getStatus() == null || user.getStatus() != 1) {
            throw new GengException("账号已禁用");
        }
        // Anti-Intruder
        if (!encoder.matches(dto.getPassword(), user.getPasswordHash())) {
            throw new GengException("用户名或密码错误");
        }
        return toVO(user);
    }

    @Override
    public UserVO getById(Long id) {
        if (id == null || id <= 0) {
            throw new GengException("id 不能为空且必须大于 0");
        }
        UserInfo user = userMapper.selectById(id);
        if (user == null) {
            throw new GengException(404, "用户不存在: " + id);
        }
        return toVO(user);
    }

    /** 实体 -> 视图，**只在这里完成字段转换，避免 passwordHash 漏出去** */
    private UserVO toVO(UserInfo user) {
        UserVO vo = new UserVO();
        vo.setId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setNickname(user.getNickname());
        vo.setStatus(user.getStatus());
        vo.setCreatedAt(user.getCreatedAt());
        return vo;
    }
}