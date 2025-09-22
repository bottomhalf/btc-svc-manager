package bt.conference.service;

import bt.conference.entity.LoginDetail;
import bt.conference.entity.UserDetail;
import bt.conference.model.ApplicationConstant;
import bt.conference.entity.MeetingDetail;
import bt.conference.serviceinterface.IMeetingService;
import com.fierhub.model.CurrentSession;
import in.bottomhalf.ps.database.service.DbManager;
import in.bottomhalf.ps.database.utils.DbParameters;
import in.bottomhalf.ps.database.utils.DbProcedureManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Types;
import java.util.Arrays;
import java.util.List;

@Service
public class MeetingService implements IMeetingService {
    @Autowired
    CurrentSession currentSession;
    @Autowired
    DbProcedureManager dbProcedureManager;
    @Autowired
    DbManager dbManager;
    public List<MeetingDetail> generateMeetingService(MeetingDetail meetingDetail) throws Exception {
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

        return getAllMeetingByOrganizerService();
    }

    public List<MeetingDetail> getAllMeetingByOrganizerService() throws Exception {

        return dbProcedureManager.getRecords("sp_get_all_meeting_userid",
                Arrays.asList(
                        new DbParameters("_userid", currentSession.getUserId(), Types.BIGINT)
                ),
                MeetingDetail.class
        );
    }

    public List<MeetingDetail> generateQuickMeetingService(MeetingDetail meetingDetail) throws Exception {
        meetingDetail.setDurationInSecond(50000);
        java.util.Date utilDate = new java.util.Date();
        var date = new java.sql.Timestamp(utilDate.getTime());
        meetingDetail.setStartDate(date);
        return generateMeetingService(meetingDetail);
    }
}
