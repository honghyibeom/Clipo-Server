package myproject.cliposerver.service.Oauth2;

import com.fasterxml.jackson.core.JsonProcessingException;
import myproject.cliposerver.data.dto.ResponseDTO;
import myproject.cliposerver.data.dto.Oauth2.SocialLoginDTO;

public interface SocialLoginInter {
    ResponseDTO login(SocialLoginDTO socialLoginDTO) throws JsonProcessingException;
}
