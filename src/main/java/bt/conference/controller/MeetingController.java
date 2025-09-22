package bt.conference.controller;

import bt.conference.entity.MeetingDetail;
import bt.conference.serviceinterface.IMeetingService;
import in.bottomhalf.common.models.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/meeting/")
public class MeetingController {
    @Autowired
    IMeetingService _meetingService;

    @PostMapping("generateMeeting")
    public ResponseEntity<ApiResponse> generateMeeting(@RequestBody MeetingDetail meetingDetail) throws Exception {
        var result = _meetingService.generateMeetingService(meetingDetail);
        return ResponseEntity.ok(ApiResponse.Ok(result));
    }
}
