package bt.conference.service;

import bt.conference.entity.UserDetail;
import bt.conference.entity.MeetingDetail;
import bt.conference.serviceinterface.IMeetingService;
import com.fierhub.model.CurrentSession;
import in.bottomhalf.ps.database.service.DbManager;
import in.bottomhalf.ps.database.utils.DbParameters;
import in.bottomhalf.ps.database.utils.DbProcedureManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
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
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom random = new SecureRandom();
    public List<MeetingDetail> generateMeetingService(MeetingDetail meetingDetail) throws Exception {
        if (meetingDetail.getTitle() == null || meetingDetail.getTitle().isEmpty())
            throw new Exception("Invalid meeting title");

        if (meetingDetail.getDurationInSecond() <= 0)
            throw new Exception("Invalid meeting duration");

        meetingDetail.setHasQuickMeeting(false);
        addMeetingDetail(meetingDetail);

        return getAllMeetingByOrganizerService();
    }

    private void addMeetingDetail(MeetingDetail meetingDetail) throws Exception {
        var user = dbManager.getById(currentSession.getUserId(), UserDetail.class);
        if (user == null)
            throw new Exception("User not found");

        var fullName = user.getFirstName() + (user.getLastName() != null && !user.getLastName().isEmpty() ? " " + user.getLastName() : "");
        meetingDetail.setMeetingId(ManageMeetingService.generateToken(currentSession.getUserId(), fullName));
        meetingDetail.setMeetingPassword(generatePassword(6));
        meetingDetail.setOrganizedBy(currentSession.getUserId());

        dbManager.save(meetingDetail);
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
        meetingDetail.setDurationInSecond(36000);
        java.util.Date utilDate = new java.util.Date();
        var date = new java.sql.Timestamp(utilDate.getTime());
        meetingDetail.setStartDate(date);
        meetingDetail.setHasQuickMeeting(true);

        addMeetingDetail(meetingDetail);
        return getAllMeetingByOrganizerService();
    }

    public  MeetingDetail validateMeetingService(MeetingDetail meetingDetail) throws Exception {
        if (meetingDetail.getMeetingDetailId() <= 0)
            throw new Exception("Invalid meeting detail id");

        if (meetingDetail.getMeetingId() == null || meetingDetail.getMeetingId().isEmpty())
            throw new Exception("Invalid meeting id passed");

        var existingMeetingDetail = dbManager.getById(meetingDetail.getMeetingDetailId(), MeetingDetail.class);
        if (existingMeetingDetail == null)
            throw new Exception("Meeting detail not found");

        if (!meetingDetail.getMeetingId().equals(existingMeetingDetail.getMeetingId()))
            throw new Exception("Invalid meeting id");

        return existingMeetingDetail;
    }

    private String generatePassword(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }
}
