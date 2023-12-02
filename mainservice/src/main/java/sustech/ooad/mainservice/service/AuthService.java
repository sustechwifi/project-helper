package sustech.ooad.mainservice.service;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequestEntityConverter;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthorizationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.UnknownContentTypeException;
import sustech.ooad.mainservice.mapper.AuthUserMapper;
import sustech.ooad.mainservice.model.AuthUser;
import sustech.ooad.mainservice.util.ConstantField;

import java.util.Map;

import static sustech.ooad.mainservice.util.ConstantField.*;

@Slf4j
@Service
public class AuthService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private static final ParameterizedTypeReference<Map<String, Object>> PARAMETERIZED_RESPONSE_TYPE = new ParameterizedTypeReference<>() {

    };
    Converter<OAuth2UserRequest, RequestEntity<?>> requestEntityConverter = new OAuth2UserRequestEntityConverter();
    final RestOperations restOperations;

    final AuthUserMapper authUserMapper;

    public AuthService(AuthUserMapper authUserMapper) {
        this.restOperations = new RestTemplate();
        this.authUserMapper = authUserMapper;
    }

    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 1. 校验OAuth2UserRequest、用户信息查询接口不能为空
        Assert.notNull(userRequest, "userRequest cannot be null");
        if (!StringUtils.hasText(userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUri())) {
            OAuth2Error oauth2Error = new OAuth2Error("missing_user_info_uri", "Missing required UserInfo Uri in UserInfoEndpoint for Client Registration: " + userRequest.getClientRegistration().getRegistrationId(), (String) null);
            throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());
        } else {
            // 3. 查询第三方平台用户信息
            RequestEntity<?> request = this.requestEntityConverter.convert(userRequest);
            ResponseEntity<Map<String, Object>> response = this.getResponse(userRequest, request);
            // 用户信息
            Map<String, Object> userAttributes = response.getBody();
            Dict dict = Dict.parse(userAttributes);
            String login = dict.getStr("login");
            // 4. 取出对应的第三方平台用户ID
            Assert.notNull(userAttributes, "userAttributes cannot be null");
            // 5.查询绑定表
            String registrationId = userRequest.getClientRegistration().getRegistrationId(); // 平台名称
            AuthUser userBindThirdLogin = authUserMapper.selectThirdUser(registrationId, login);
            if (ObjectUtil.isNull(userBindThirdLogin)) {
                // 未查询到第三方对应绑定信息，直接创建用户
                AuthUser user = new AuthUser(
                        login,
                        dict.getStr("name"),
                        "tmp",
                        dict.getStr("avatar_url")
                );
                if (ConstantField.THIRD_GITEE.equals(registrationId)) {
                    user.setPlatform(ConstantField.THIRD_GITEE);
                } else if (ConstantField.THIRD_GITHUB.equals(registrationId)) {
                    // github
                    user.setPlatform(ConstantField.THIRD_GITHUB);
                } else {
                    OAuth2Error oauth2Error = new OAuth2Error("unsupported platform", "unsupported platform: " + registrationId, null);
                    throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());
                }
                user.setPassword(ConstantField.randomPassword());
                authUserMapper.saveThirdUser(user);
                log.info("============ [NEW USER] ==========\n" + user.getLogin());
                userBindThirdLogin = authUserMapper.selectThirdUser(registrationId, login);
            }
            return userBindThirdLogin;
        }
    }

    private ResponseEntity<Map<String, Object>> getResponse(OAuth2UserRequest userRequest, RequestEntity<?> request) {
        OAuth2Error oauth2Error;
        try {
            return this.restOperations.exchange(request, PARAMETERIZED_RESPONSE_TYPE);
        } catch (OAuth2AuthorizationException var6) {
            oauth2Error = var6.getError();
            StringBuilder errorDetails = new StringBuilder();
            errorDetails.append("Error details: [");
            errorDetails.append("UserInfo Uri: ").append(userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUri());
            errorDetails.append(", Error Code: ").append(oauth2Error.getErrorCode());
            if (oauth2Error.getDescription() != null) {
                errorDetails.append(", Error Description: ").append(oauth2Error.getDescription());
            }
            errorDetails.append("]");
            oauth2Error = new OAuth2Error("invalid_user_info_response", "An error occurred while attempting to retrieve the UserInfo Resource: " + errorDetails.toString(), (String) null);
            throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString(), var6);
        } catch (UnknownContentTypeException var7) {
            String var10000 = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUri();
            String errorMessage = "An error occurred while attempting to retrieve the UserInfo Resource from '" + var10000 + "': response contains invalid content type '" + var7.getContentType().toString() + "'. The UserInfo Response should return a JSON object (content type 'application/json') that contains a collection of name and value pairs of the claims about the authenticated End-User. Please ensure the UserInfo Uri in UserInfoEndpoint for Client Registration '" + userRequest.getClientRegistration().getRegistrationId() + "' conforms to the UserInfo Endpoint, as defined in OpenID Connect 1.0: 'https://openid.net/specs/openid-connect-core-1_0.html#UserInfo'";
            oauth2Error = new OAuth2Error("invalid_user_info_response", errorMessage, (String) null);
            throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString(), var7);
        } catch (RestClientException var8) {
            oauth2Error = new OAuth2Error("invalid_user_info_response", "An error occurred while attempting to retrieve the UserInfo Resource: " + var8.getMessage(), (String) null);
            throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString(), var8);
        }
    }

    @Resource
    RedisTemplate<String,String> redisTemplate;

    public boolean authRole(String email, String role) {
        AuthUser r = null;
        try {
            r = authUserMapper.selectByEmail(email);
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
        if (StrUtil.isEmpty(r.getEmail())){
            return false;
        }
        switch (role){
            case ROLE_STUDENT: {
                if (r.getRole().equals(ROLE_DEFAULT)){
                    authUserMapper.updateRole(role,r.getId().longValue());
                    return true;
                }
            }
            case ROLE_TEACHER:{
                // TODO 认证老师邮箱

            }
            default:return false;
        }
    }

    public boolean bindEmail(long uid, String email) {
        long cnt = authUserMapper.checkEmailHasBound(email);
        if (cnt != 0){
            return false;
        }
        authUserMapper.bindEmail(uid,email);
        return true;
    }

    public void editInfo(AuthUser authUser) {
        authUserMapper.updateInfo(authUser);
    }
}
