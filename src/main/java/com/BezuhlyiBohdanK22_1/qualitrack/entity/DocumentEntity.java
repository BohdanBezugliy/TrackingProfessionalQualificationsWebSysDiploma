package com.BezuhlyiBohdanK22_1.qualitrack.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "documents")
@Getter
@Setter
public class DocumentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "document_id")
    private Long documentId;

    @NotBlank
    @Size(max = 255)
    @Column(name = "document_type")
    private String documentType;

    @Column(name = "upload_at")
    private LocalDateTime uploadAt = LocalDateTime.now();

    @NotBlank @Size(max = 100)
    @Column(name = "content_type")
    private String contentType;

    @NotBlank @Size(max = 255)
    @Column(name = "file_name")
    private String fileName;

    @Positive
    @Column(name = "file_size")
    private Long fileSize;

    @Lob
    @Column(name = "file_data", nullable = false)
    private byte[] fileData;
}