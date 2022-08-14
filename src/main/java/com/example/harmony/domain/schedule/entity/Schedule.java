package com.example.harmony.domain.schedule.entity;

import com.example.harmony.domain.gallery.entity.Gallery;
import com.example.harmony.domain.schedule.dto.ScheduleDoneRequest;
import com.example.harmony.domain.schedule.dto.ScheduleRequest;
import com.example.harmony.domain.user.entity.Family;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // TODO: enum? String?
    private String category;

    private String title;

    private LocalDate startDate;

    private LocalDate endDate;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.REMOVE)
    private List<Participation> participations;

    private String content;

    private boolean done;

    @OneToOne
    private Gallery gallery;

    @ManyToOne
    private Family family;

    public Schedule(ScheduleRequest scheduleRequest, Family family) {
        this.category = scheduleRequest.getCategory();
        this.title = scheduleRequest.getTitle();
        this.startDate = scheduleRequest.getStartDate();
        this.endDate = scheduleRequest.getEndDate();
        this.content = scheduleRequest.getContent();
        this.done = endDate.isBefore(LocalDate.now());
        this.gallery = null;
        this.family = family;
    }

    public void modify(ScheduleRequest scheduleRequest, List<Participation> participations) {
        this.category = scheduleRequest.getCategory();
        this.title = scheduleRequest.getTitle();
        if (!done) {
            this.startDate = scheduleRequest.getStartDate();
            this.endDate = scheduleRequest.getEndDate();
            this.participations = participations;
            this.content = scheduleRequest.getContent();
        }
    }

    public void setDone(ScheduleDoneRequest scheduleDoneRequest) {
        this.done = scheduleDoneRequest.isDone();

        if (participations.size() >= 2) {
            if (done) {
                family.plusScore(10);
            } else {
                family.minusScore(10);
            }
        }
    }
}
