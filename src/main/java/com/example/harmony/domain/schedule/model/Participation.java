package com.example.harmony.domain.schedule.model;

import com.example.harmony.domain.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
public class Participation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Schedule schedule;

    @ManyToOne
    private User participant;

    public Participation(Schedule schedule, User participant) {
        if (!participant.getFamily().getId().equals(schedule.getFamily().getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "가족 구성원만 일정에 참여할 수 있습니다");
        }
        this.schedule = schedule;
        this.participant = participant;
    }
}
