package com.BezuhlyiBohdanK22_1.trackingProfessionalQualificationsWebSys.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Сутність, що представляє завантажений документ (наприклад, скан сертифіката).
 * Зберігає метадані файлу та його бінарний вміст.
 * Відображається на таблицю "documents" у базі даних.
 */
@Entity
@Table(name = "documents")
@Getter
@Setter
public class DocumentEntity {
    /**
     * Унікальний ідентифікатор документа.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "document_id")
    private Long documentId;

    /**
     * Тип документа у системі (наприклад, "Свідоцтво про підвищення кваліфікації").
     */
    @NotBlank
    @Size(max = 255)
    @Column(name = "document_type")
    private String documentType;

    /**
     * Дата та час завантаження документа в систему.
     */
    @Column(name = "upload_at")
    private LocalDateTime uploadAt = LocalDateTime.now();

    /**
     * MIME-тип файлу (наприклад, "application/pdf" або "image/png").
     */
    @NotBlank @Size(max = 100)
    @Column(name = "content_type")
    private String contentType;

    /**
     * Оригінальна назва завантаженого файлу.
     */
    @NotBlank @Size(max = 255)
    @Column(name = "file_name")
    private String fileName;

    /**
     * Розмір файлу в байтах.
     */
    @Positive
    @Column(name = "file_size")
    private Long fileSize;

    /**
     * Бінарний вміст файлу.
     */
    @Lob
    @Column(name = "file_data", nullable = false)
    private byte[] fileData;
}