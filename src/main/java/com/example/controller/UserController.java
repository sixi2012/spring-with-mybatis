package com.example.controller;

import com.example.api.UserApi;
import com.example.domain.api.UserInfo;
import com.example.domain.bo.UserBo;
import com.example.constants.ValidationEnum;
import com.example.exception.ParamCheckException;
import com.example.service.RedisService;
import com.example.service.UserService;
import com.example.domain.bo.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private UserApi userApi;

    @GetMapping("/getById")
    public UserVo getById(@Param("id") int id){
        UserBo userBo = userService.findById(id);

        UserVo userVo = new UserVo();
        userVo.setId(userBo.getId());
        userVo.setName(userBo.getName());
        userVo.setAge(userBo.getAge());
        userVo.setMessage(userBo.getMessage());
        return userVo;
    }

    @GetMapping("/getCache")
    public String getByName(@Param("name") String name){

        redisService.set(name, name + "_redis_test_" + System.currentTimeMillis());
        return redisService.get(name).toString();
    }

    @PostMapping("/getAll")
    public List<UserVo> getAll(@RequestBody UserVo userVo){

        if (StringUtils.isBlank(userVo.getName())){
            throw new ParamCheckException(ValidationEnum.PARAM_IS_NOT_NULL);
        }

        UserVo uv = new UserVo();
        uv.setId(userVo.getId());
        uv.setName(userVo.getName());

        List<UserVo> rsp = new ArrayList<>();
        rsp.add(uv);
        return rsp;
    }

    @GetMapping("/getUserInfo")
    public UserVo getUserInfo(@Param("id") int id){
        UserInfo userInfo = userApi.getUserInfoByID(id);
        log.info("----userInfo-------" + userInfo);

        UserVo userVo = new UserVo();
        userVo.setId(userInfo.getId());
        userVo.setName(userInfo.getName());
        userVo.setAge(userInfo.getAge());
        userVo.setMessage(userInfo.getMessage());
        return userVo;
    }
}
