package bt.conference.entity;

import com.fierhub.database.annotations.Column;
import com.fierhub.database.annotations.Id;
import com.fierhub.database.annotations.Table;
import com.fierhub.database.annotations.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "meeting_detail")
public class MeetingDetail {
    @Id
    @Column(name = "meetingDetailId")
    long meetingDetailId;

    @Column(name = "meetingId")
    String meetingId;

    @Column(name = "meetingPassword")
    String meetingPassword;

    @Column(name = "organizedBy")
    long organizedBy;

    @Column(name = "agenda")
    String agenda;

    @Column(name = "title")
    String title;

    @Column(name = "startDate")
    Date startDate;

    @Column(name = "durationInSecond")
    int durationInSecond;

    @Transient
    String organizerName;

    @Column(name = "hasQuickMeeting")
    boolean hasQuickMeeting;
}
