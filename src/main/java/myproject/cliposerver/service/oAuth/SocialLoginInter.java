package myproject.cliposerver.service.oAuth;

import com.fasterxml.jackson.core.JsonProcessingException;
import myproject.cliposerver.data.dto.ResponseDTO;
import myproject.cliposerver.data.dto.oAuth.SocialLoginDTO;

public interface SocialLoginInter {
    ResponseDTO login(SocialLoginDTO socialLoginDTO) throws JsonProcessingException;
}
