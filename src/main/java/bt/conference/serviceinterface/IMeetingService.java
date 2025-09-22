package bt.conference.serviceinterface;

import bt.conference.entity.MeetingDetail;

public interface IMeetingService {
    MeetingDetail generateMeetingService(MeetingDetail meetingDetail) throws Exception;
}
