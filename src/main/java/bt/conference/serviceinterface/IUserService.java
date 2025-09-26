package bt.conference.serviceinterface;

import bt.conference.entity.UserDetail;

import java.util.List;

public interface IUserService {
    List<UserDetail> getAllUserService() throws Exception;
}
