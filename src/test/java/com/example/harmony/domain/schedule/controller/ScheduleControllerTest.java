package com.example.harmony.domain.schedule.controller;

import com.example.harmony.domain.schedule.dto.ScheduleRequest;
import com.example.harmony.domain.schedule.service.ScheduleService;
import com.example.harmony.domain.user.model.User;
import com.example.harmony.global.security.UserDetailsImpl;
import com.example.harmony.global.security.config.WebSecurityConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = ScheduleController.class,
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = WebSecurityConfig.class
                )
        })
@MockBean(JpaMetamodelMappingContext.class)
class ScheduleControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    ScheduleService scheduleService;

    @Nested
    @DisplayName("일정 등록")
    class PostSchedule {

        @Nested
        @DisplayName("실패")
        class Fail {

            @Test
            @WithMockUser
            @DisplayName("유효하지않은 카테고리")
            void invalid_category() throws Exception {
                // given
                ScheduleRequest scheduleRequest = ScheduleRequest.builder()
                        .category("존재하지 않는 카테고리")
                        .title("제목")
                        .startDate(LocalDate.of(2022, 8, 8))
                        .endDate(LocalDate.of(2022, 8, 8))
                        .memberIds(Arrays.asList(1L, 2L))
                        .content("내용")
                        .build();

                String scheduleRequestJson = objectMapper.writeValueAsString(scheduleRequest);

                // when & then
                mvc.perform(post("/schedules")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(scheduleRequestJson))
                        .andExpect(status().isBadRequest());
            }
        }

        @Nested
        @DisplayName("성공")
        class Success {

            @Test
            @WithUserDetails
            @DisplayName("정상 케이스")
            void success() throws Exception {
                // given
                ScheduleRequest scheduleRequest = ScheduleRequest.builder()
                        .category("TRIP")
                        .title("제목")
                        .startDate(LocalDate.of(2022, 8, 8))
                        .endDate(LocalDate.of(2022, 8, 8))
                        .memberIds(Arrays.asList(1L, 2L))
                        .content("내용")
                        .build();

                String scheduleRequestJson = objectMapper.writeValueAsString(scheduleRequest);

                // when & then
                mvc.perform(post("/schedules")
                                .with(csrf())
                                .with(user(new UserDetailsImpl(new User())))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(scheduleRequestJson))
                        .andExpect(status().isCreated());
            }
        }
    }

    @Nested
    @DisplayName("일정 수정")
    class ModifySchedule {

        @Nested
        @DisplayName("실패")
        class Fail {

            @Test
            @WithMockUser
            @DisplayName("유효하지않은 카테고리")
            void invalid_category() throws Exception {
                // given
                ScheduleRequest scheduleRequest = ScheduleRequest.builder()
                        .category("존재하지 않는 카테고리")
                        .title("제목")
                        .startDate(LocalDate.of(2022, 8, 8))
                        .endDate(LocalDate.of(2022, 8, 8))
                        .memberIds(Arrays.asList(1L, 2L))
                        .content("내용")
                        .build();

                String scheduleRequestJson = objectMapper.writeValueAsString(scheduleRequest);

                // when & then
                mvc.perform(put("/schedules/{scheduleId}", "1")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(scheduleRequestJson))
                        .andExpect(status().isBadRequest());
            }
        }

        @Nested
        @DisplayName("성공")
        class Success {

            @Test
            @WithUserDetails
            @DisplayName("정상 케이스")
            void success() throws Exception {
                // given
                ScheduleRequest scheduleRequest = ScheduleRequest.builder()
                        .category("TRIP")
                        .title("제목")
                        .startDate(LocalDate.of(2022, 8, 8))
                        .endDate(LocalDate.of(2022, 8, 8))
                        .memberIds(Arrays.asList(1L, 2L))
                        .content("내용")
                        .build();

                String scheduleRequestJson = objectMapper.writeValueAsString(scheduleRequest);

                // when & then
                mvc.perform(put("/schedules/{scheduleId}", "1")
                                .with(csrf())
                                .with(user(new UserDetailsImpl(new User())))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(scheduleRequestJson))
                        .andExpect(status().isOk());
            }
        }
    }
}