package bt.conference.serviceinterface;

import bt.conference.entity.MeetingDetail;

import java.util.List;

public interface IMeetingService {
    List<MeetingDetail> generateMeetingService(MeetingDetail meetingDetail) throws Exception;
    List<MeetingDetail> getAllMeetingByOrganizerService() throws Exception;
    List<MeetingDetail> generateQuickMeetingService(MeetingDetail meetingDetail) throws Exception;
    MeetingDetail validateMeetingService(MeetingDetail meetingDetail) throws Exception;
}
