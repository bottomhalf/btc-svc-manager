package bt.conference.serviceinterface;

import bt.conference.model.LoginDetail;
import bt.conference.model.LoginResponse;

public interface IAuthenticateService {
    LoginResponse authenticateUserService(LoginDetail loginDetail) throws Exception;
}
