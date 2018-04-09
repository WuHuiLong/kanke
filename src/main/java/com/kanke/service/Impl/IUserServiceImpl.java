package com.kanke.service.Impl;

import com.kanke.commom.Const;
import com.kanke.commom.ServerResponse;
import com.kanke.commom.TokenCache;
import com.kanke.dao.UserMapper;
import com.kanke.pojo.User;
import com.kanke.service.IUserService;
import com.kanke.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service("iUserService")
public class IUserServiceImpl implements IUserService{

    @Autowired
    private UserMapper userMapper;


    @Override
    public ServerResponse<User> login(String username, String password) {
        int resultCount =userMapper.checkUsername(username);
        if (resultCount==0){
            return ServerResponse.createByErrorMsg("用户名不存在");
        }
        //todo 密码登录md5
        String md5Password= MD5Util.MD5EncodeUtf8(password);//注册时密码经过加密，所以在这要拿加密后的密码进行比较
        User user=userMapper.selectLogin(username,md5Password);
        if (user==null){
            return ServerResponse.createByErrorMsg("密码错误");//上一个已经验证用户名，如果还为空即密码错误
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess("登录成功",user);
    }

    @Override
    public ServerResponse<String> register(User user) {
        ServerResponse validResponse =this.checkValid(user.getUsername(), Const.USERNAME);
        if(!validResponse.isSuccess()){
            return validResponse;
        }
        validResponse =this.checkValid(user.getEmail(),Const.EMAIL);
        if(!validResponse.isSuccess()){
            return validResponse;
        }
        user.setRole(Const.Role.ROLE_CUSTOMER);
        //md5加密
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        //查看数据数，如果为零，代表没插入进来，则表示插入失败
        int resultCount=userMapper.insert(user);
        if(resultCount==0){
            return  ServerResponse.createByErrorMsg("注册失败");
        }
        return  ServerResponse.createBySuccessMsg("注册成功") ;
    }

    @Override
    public ServerResponse<String> checkValid(String str, String type) {
        if (StringUtils.isNotBlank(type)){
            //开始校验
            if(Const.USERNAME.equals(type)){
                int resultCount =userMapper.checkUsername(str);
                if(resultCount>0){
                    return ServerResponse.createByErrorMsg("用户名已存在");
                }
            }
            if(Const.EMAIL.equals(type)){
                int resultCount =userMapper.checkEmail(str);
                if (resultCount>0){
                    return ServerResponse.createByErrorMsg("邮箱已存在");
                }
            }
        }
        else {
            return ServerResponse.createByErrorMsg("参数错误");
        }
        return ServerResponse.createBySuccessMsg("校验成功");
    }

    @Override
    public ServerResponse selectQuestion(String username){
        ServerResponse validResponse =this.checkValid(username,Const.USERNAME);
        if(validResponse.isSuccess()){//代表用户不存在
            return ServerResponse.createByErrorMsg("用户不存在");
        }
        String question=userMapper.selectQuestionByUsername(username);
        if(StringUtils.isNotBlank(username)){
            return ServerResponse.createBySuccess(question);
        }
        return ServerResponse.createByErrorMsg("找回密码的问题是空的");
    }
    @Override
    public ServerResponse<String> checkAnswer(String username,String question,String answer){
        int resultCount =userMapper.checkAnswer(username,question,answer);
        if(resultCount>0){
            //证明是回答是正确的
            String forgetToken= UUID.randomUUID().toString();
            TokenCache.setKey(TokenCache.TOKEN_PREFIX+username,forgetToken);
            return ServerResponse.createBySuccess(forgetToken);
        }
        return ServerResponse.createByErrorMsg("问题的答案错误");
    }
    @Override
    public ServerResponse<String> forgetResetPassword(String username,String newPassword,String forgetToken){
        if(StringUtils.isBlank(forgetToken)){
            return ServerResponse.createByErrorMsg("参数错误，Token需要传递");
        }
        ServerResponse validResponse =this.checkValid(username,Const.USERNAME);
        if(validResponse.isSuccess()){//代表用户不存在
            return ServerResponse.createByErrorMsg("用户不存在");
        }
        String token =TokenCache.getKey(TokenCache.TOKEN_PREFIX+username);
        if(StringUtils.isBlank(token)){
            return ServerResponse.createByErrorMsg("token不存在或者无效");
        }
        if(StringUtils.equals(token,forgetToken)){
            String md5Password=MD5Util.MD5EncodeUtf8(newPassword);
            int rowCount=userMapper.updatePasswordByUsername(username,md5Password);
            if(rowCount>0){
                return ServerResponse.createBySuccessMsg("修改密码成功");
            }
        }else {
            return ServerResponse.createByErrorMsg("token错误，请重新获取");
        }
        return ServerResponse.createByErrorMsg("修改密码失败");
    }
    @Override
    public ServerResponse<String> resetPassword(String oldPassword,String newPassword,User user){
        //防止横向越权，一定要校验一下这个用户的旧密码，一定要指定这个用户，因为我们会查询一个count（1）
        //如果不指定id，那结果就是ture了，count>0;
        int resultCount=userMapper.checkPassword(MD5Util.MD5EncodeUtf8(oldPassword),user.getId());
        if(resultCount==0){
            return ServerResponse.createByErrorMsg("用户名密码无法对应，请重新输入");
        }
        user.setPassword(MD5Util.MD5EncodeUtf8(newPassword));
        int updateCount=userMapper.updateByPrimaryKeySelective(user);
        if(updateCount>0){
            return  ServerResponse.createBySuccessMsg("密码更新成功");
        }
        return ServerResponse.createByErrorMsg("密码更新失败");
    }
    @Override
    public ServerResponse<User> update_information(User user){
        //更新时username是无法更新的
        //更新email需要校验，检查email是否存在，如果存在的话，不能是我们当前用户的
        int resultCount=userMapper.checkEmailByUserId(user.getEmail(),user.getId());
        if(resultCount>0){
            return ServerResponse.createByErrorMsg("邮箱已经占用,");
        }
        User updateUser=new User();
        updateUser.setId(user.getId());
        updateUser.setAnswer(user.getAnswer());
        updateUser.setEmail(user.getEmail());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setTelephone(user.getTelephone());
        int updateCount=userMapper.updateByPrimaryKeySelective(updateUser);
        if(updateCount>0){
            return ServerResponse.createBySuccess("个人信息跟新失败",updateUser);
        }
        return ServerResponse.createByErrorMsg("个人信息更新失败");
    }
    @Override
    public ServerResponse<User> get_information(Integer userId){
        User user=userMapper.selectByPrimaryKey(userId);
        if(user==null){
            return ServerResponse.createByErrorMsg("找不到当前用户");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess(user);
    }

    //后台

    /**
     * 校验是否是管理员
     * @param user
     * @return
     */
    @Override
    public ServerResponse checkAdminRole(User user){
        if(user!=null&&user.getRole().intValue() == Const.Role.ROLE_ADMIN){
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();
    }

}
