package myproject.cliposerver.service.boardlike;

import myproject.cliposerver.config.security.UserDetailsImpl;
import myproject.cliposerver.data.dto.ResponseDTO;

public interface BoardLikeService {
    ResponseDTO like(Long bno, UserDetailsImpl userDetails);
    ResponseDTO unlike(Long bno, UserDetailsImpl userDetails);

}
