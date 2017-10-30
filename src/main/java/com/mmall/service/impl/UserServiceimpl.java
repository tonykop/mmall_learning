package com.mmall.service.impl;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.common.TokenCache;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Created in 2017/10/25
 * @author tony
 */
@Service("iUserService")
public class UserServiceimpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public ServerResponse<User> login(String username, String password) {
        int resultCount = userMapper.checkUsername(username);
        if(resultCount == 0){
            return ServerResponse.createByErrorMessage("do not this user!");
        }
        String md5Password = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selectLogin(username,md5Password);
        if(user == null){
            return ServerResponse.createByErrorMessage("password error!");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.cretaeBySuccess("login success!",user);
    }

    /**
     * user regist
     */
    @Override
    public ServerResponse<String> register(User user){

        ServerResponse validResponse = this.checkValid(user.getUsername(),Const.USERNAME);
        if(!validResponse.isSuccess()){
            return validResponse;
        }
        validResponse = this.checkValid(user.getEmail(),Const.EMAIL);
        if(!validResponse.isSuccess()){
            return validResponse;
        }

        user.setRole(Const.Role.ROLE_CUSTOMER);
        //MD5 jia
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));

        int  resultCount = userMapper.insert(user);

        if(resultCount == 0){
            return ServerResponse.createBySuccessMessage("regist faild!");
        }

        return ServerResponse.createBySuccessMessage("regist success!");
    }


    public ServerResponse<String> checkValid(String str,String type){
        if(org.apache.commons.lang3.StringUtils.isNotBlank(type)){
            if(Const.USERNAME.equals(type)){
                int resultCount = userMapper.checkUsername(str);
                if(resultCount > 0){
                    return ServerResponse.createByErrorMessage("user have in system!");
                }

            }
            if(Const.EMAIL.equals(type)){
                int resultCount = userMapper.checkEmail(str);
                if(resultCount > 0 ){
                    return ServerResponse.createByErrorMessage("email have in system!");
                }
            }
        }else{
            return ServerResponse.createBySuccessMessage("param error!");
        }

        return ServerResponse.createBySuccessMessage("valid success!");
    }

    public ServerResponse selectQuestion(String username){
        ServerResponse validResponse = this.checkValid(username,Const.USERNAME);
        if(validResponse.isSuccess()){
            //user not in
            return ServerResponse.createByErrorMessage("user not in!");
        }
        String question = userMapper.selectQuestionByUsername(username);
        if(org.apache.commons.lang3.StringUtils.isNotBlank(question)){
             return ServerResponse.createBySuccess(question);
        }

        return ServerResponse.createByErrorMessage("find password question is null!");
    }

    public ServerResponse<String> checkAnswer(String username, String question, String answer){
          int resultCount = userMapper.checkAnswer(username,question,answer);
           if(resultCount>0){
               String forgetToken = UUID.randomUUID().toString();
               TokenCache.setKey(TokenCache.TOKEN_PREFIX+username,forgetToken);
               return ServerResponse.createBySuccess(forgetToken);
           }

         return ServerResponse.createByErrorMessage("answer question is error!");
    }

    public ServerResponse<String> forgetResetPassword(String username,String passwordNew, String forgetToken){
        if(org.apache.commons.lang3.StringUtils.isBlank(forgetToken)){
         return ServerResponse.createByErrorMessage("param error,token need to be take in");
        }

        ServerResponse validResponse = this.checkValid(username,Const.USERNAME);
        if(validResponse.isSuccess()){
            //user is no in
            return ServerResponse.createByErrorMessage("username is not in");
        }

        String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX+username);

        if(org.apache.commons.lang3.StringUtils.isNotBlank(token)){
             return ServerResponse.createByErrorMessage("token timeout");
        }

        if(org.apache.commons.lang3.StringUtils.equals(forgetToken,token)){
          String md5Password = MD5Util.MD5EncodeUtf8(passwordNew);
            int rowCount = userMapper.updatePasswordByUsername(username,md5Password);
            if(rowCount > 0){
                return ServerResponse.createBySuccessMessage("reset password success!");
            }
        }else{
            return ServerResponse.createByErrorMessage("token error please  re get forgetpassword token");
        }
        return ServerResponse.createByErrorMessage("reset password faild!");
    }

    public ServerResponse<String> resetPassword(String passwordOld,String passwordNew, User user){
        int resultCount = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(passwordOld),user.getId());
        if(resultCount == 0){
            return ServerResponse.createByErrorMessage("old password error!");
        }
        user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        if(updateCount >0){
            return ServerResponse.createBySuccessMessage("password uodate success!");
        }
        return ServerResponse.createByErrorMessage("password update faild!");
    }

    public ServerResponse<User> updateInformation(User user){
        int resultCount = userMapper.checkEmailByUserId(user.getEmail(),user.getId());
        if(resultCount > 0){
          return ServerResponse.createByErrorMessage("email have ben user.please turn!");
        }
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());
        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        if(updateCount > 0){
           return ServerResponse.createBySuccessMessage("update user success!");
        }

        return ServerResponse.createByErrorMessage("update user faild!");
    }

    public ServerResponse<User> getInformation(Integer userId){
        User user = userMapper.selectByPrimaryKey(userId);
        if(user == null){
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        user.setPassword(StringUtils.EMPTY);    //密码置为空字符串
        return ServerResponse.createBySuccess(user);
    }

    public ServerResponse checkAdminRole(User user){
        if(user != null && user.getRole().intValue() == Const.Role.ROLE_ADMIN){
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();
    }
}
