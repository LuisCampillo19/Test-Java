package com.talentboard.entity;

import com.talentboard.enums.VacancyStatus;
import com.talentboard.enums.WorkModality;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "vacancies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vacancy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 2000)
    private String description;

    /** Area or category, e.g. "Backend", "Design", "Sales". */
    @Column(nullable = false)
    private String area;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WorkModality workModality;

    /** Salary range is optional (both can be null). */
    private BigDecimal salaryMin;
    private BigDecimal salaryMax;

    @Column(nullable = false)
    private LocalDate publicationDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VacancyStatus status;

    /**
     * The recruiter responsible for this vacancy.
     * LAZY: we don't always need the full user when loading a vacancy.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "responsible_user_id")
    private User responsibleUser;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
