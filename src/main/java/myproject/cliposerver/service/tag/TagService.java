package myproject.cliposerver.service.tag;

import myproject.cliposerver.config.security.UserDetailsImpl;
import myproject.cliposerver.data.dto.ResponseDTO;
import org.springframework.stereotype.Service;

@Service
public interface TagService {
    // 테그 검색 기능
    ResponseDTO getTagForSearch(int page, UserDetailsImpl userDetails, String search);
}
