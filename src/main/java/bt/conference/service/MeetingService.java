package bt.conference.service;

import bt.conference.entity.UserDetail;
import bt.conference.model.ApplicationConstant;
import bt.conference.entity.MeetingDetail;
import bt.conference.serviceinterface.IMeetingService;
import com.fierhub.model.CurrentSession;
import in.bottomhalf.ps.database.service.DbManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MeetingService implements IMeetingService {
    @Autowired
    CurrentSession currentSession;
    @Autowired
    DbManager dbManager;
    public MeetingDetail generateMeetingService(MeetingDetail meetingDetail) throws Exception {
        if (meetingDetail.getTitle() == null || meetingDetail.getTitle().isEmpty())
            throw new Exception("Invalid meeting title");

        if (meetingDetail.getDurationInSecond() <= 0)
            throw new Exception("Invalid meeting duration");

        var user = dbManager.getById(currentSession.getUserId(), UserDetail.class);
        if (user == null)
            throw new Exception("User not found");

        var fullName = user.getFirstName() + (user.getLastName() != null && !user.getLastName().isEmpty() ? " " + user.getLastName() : "");
        meetingDetail.setMeetingId(ManageMeetingService.generateToken(currentSession.getUserId(), fullName));
        meetingDetail.setMeetingPassword(ApplicationConstant.DefaultMeetingPassword);
        meetingDetail.setOrganizedBy(currentSession.getUserId());

        dbManager.save(meetingDetail);

        return meetingDetail;
    }
}
