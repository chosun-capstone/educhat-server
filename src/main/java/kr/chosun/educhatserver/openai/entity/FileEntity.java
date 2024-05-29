package kr.chosun.educhatserver.openai.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "FileEntity")
public class FileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "status")
    private FileStatus status;

    @OneToOne(mappedBy = "file", cascade = CascadeType.ALL, optional = false, fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    private FileDataEntity fileData;
}
