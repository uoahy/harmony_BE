package com.example.harmony.domain.schedule.entity;

import com.example.harmony.domain.gallery.entity.Gallery;
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

    @OneToMany(mappedBy = "schedule")
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
        this.done = false;
        this.gallery = null;
        this.family = family;
    }
}
