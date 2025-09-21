package bt.conference.service;

import bt.conference.model.ApplicationConstant;
import bt.conference.model.LoginDetail;
import bt.conference.model.LoginResponse;
import bt.conference.serviceinterface.IAuthenticateService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fierhub.service.FierhubService;
import in.bottomhalf.common.models.ApiResponse;
import in.bottomhalf.ps.database.utils.DbParameters;
import in.bottomhalf.ps.database.utils.DbProcedureManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Types;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthenticateService implements IAuthenticateService {
    @Autowired
    FierhubService fierhubService;
    @Autowired
    ObjectMapper mapper;
    @Autowired
    DbProcedureManager dbProcedureManager;
    public LoginResponse authenticateUserService(LoginDetail loginDetail) throws Exception {
        if (loginDetail.getEmail() == null || loginDetail.getEmail().isEmpty())
            throw new Exception("Please enter email");

        if (loginDetail.getPassword() == null || loginDetail.getPassword().isEmpty())
            throw new Exception("Please enter password");

        var userDetail = getUserByEmailOrMobile(loginDetail.getEmail(), null);

        String userDetailJson = mapper.writeValueAsString(userDetail);
        Map<String, Object> claims = new HashMap<>(Map.of(
                "userDetail", userDetailJson,
                "userId",  String.valueOf(userDetail.getUserId()),
                "email", userDetail.getEmail(),
                "firstname", userDetail.getFirstName(),
                "lastname", userDetail.getLastName(),
                "code", "BTC",
                "expiration", Instant.now().plusMillis(1000 * 60 * 60).toString()
        ));

        var roleId = 1;
        switch (roleId) {
            case 1:
                claims.put("roles", ApplicationConstant.Admin);
                break;
            case 3:
                claims.put("roles", ApplicationConstant.Client);
                break;
            default:
                claims.put("roles", ApplicationConstant.User);
        }

        ApiResponse response = fierhubService.generateToken(claims);

        return LoginResponse.builder()
                .token(response.getResponseBody().toString())
                .firstName(userDetail.getFirstName())
                .lastName(userDetail.getLastName())
                .email(userDetail.getEmail())
                .userId(userDetail.getUserId())
                .build();
    }

    private LoginDetail getUserByEmailOrMobile(String email, String mobile) throws Exception {
        var loginDetail = dbProcedureManager.execute("sp_login_auth",
                Arrays.asList(
                        new DbParameters("_mobile", mobile, Types.VARCHAR),
                        new DbParameters("_email", email, Types.VARCHAR)
                ), LoginDetail.class
        );
        if (loginDetail == null)
            throw new Exception("Fail to get user detail. Please contact to admin.");

        return loginDetail;
    }
}
