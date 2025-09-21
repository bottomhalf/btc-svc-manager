package bt.conference.controller;

import bt.conference.model.LoginDetail;
import bt.conference.serviceinterface.IAuthenticateService;
import in.bottomhalf.common.models.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/auth/")
public class AuthenticateController {
    @Autowired
    IAuthenticateService _authService;

    @PostMapping("authenticateUser")
    public ResponseEntity<ApiResponse> authenticateUser(@RequestBody LoginDetail loginDetail) throws Exception {
        var result = _authService.authenticateUserService(loginDetail);
        return ResponseEntity.ok(ApiResponse.Ok(result));
    }
}
