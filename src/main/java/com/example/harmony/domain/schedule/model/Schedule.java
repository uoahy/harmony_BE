package com.example.harmony.domain.schedule.model;

import com.example.harmony.domain.gallery.entity.Gallery;
import com.example.harmony.domain.schedule.dto.ScheduleRequest;
import com.example.harmony.domain.user.model.Family;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Category category;

    private String title;

    private LocalDate startDate;

    private LocalDate endDate;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.REMOVE)
    private List<Participation> participations;

    @Lob
    private String content;

    private boolean done;

    @OneToMany(mappedBy = "schedule")
    @OrderBy("date asc")
    private List<Gallery> galleries;

    @ManyToOne
    private Family family;

    public Schedule(ScheduleRequest scheduleRequest, Family family) {
        validateDates(scheduleRequest.getStartDate(), scheduleRequest.getEndDate());
        this.category = Category.valueOf(scheduleRequest.getCategory());
        this.title = scheduleRequest.getTitle();
        this.startDate = scheduleRequest.getStartDate();
        this.endDate = scheduleRequest.getEndDate();
        this.content = scheduleRequest.getContent();
        this.done = endDate.isBefore(LocalDate.now());
        this.galleries = null;
        this.family = family;
    }

    public void modify(ScheduleRequest scheduleRequest, List<Participation> participations) {
        this.category = Category.valueOf(scheduleRequest.getCategory());
        this.title = scheduleRequest.getTitle();
        this.content = scheduleRequest.getContent();
        if (!done) {
            validateDates(scheduleRequest.getStartDate(), scheduleRequest.getEndDate());
            this.startDate = scheduleRequest.getStartDate();
            this.endDate = scheduleRequest.getEndDate();
            this.participations = participations;
        }
    }

    public void setDone() {
        if (!done && endDate.isAfter(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "종료일이 현재 이전인 일정만 완료할 수 있습니다");
        }

        done = !done;

        if (participations.size() >= 2) {
            if (done) {
                family.plusScore(10);
            } else {
                family.minusScore(10);
            }
        }
    }

    public void validateDates(LocalDate startDate, LocalDate endDate) {
        if (endDate.isBefore(startDate)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "종료일은 시작일 이후여야 합니다");
        }
    }

    public void addGallery(Gallery gallery) {
        galleries.add(gallery);
        gallery.setSchedule(this);
    }
}
