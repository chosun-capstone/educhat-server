package kr.chosun.educhatserver.openai.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "FileDataEntity")
public class FileDataEntity {
    @Id
    @Column(name = "file_id")
    private Long fileId;

    @Column(name = "file_data", length = 1000000)
    private byte[] data;

    @OneToOne
    @MapsId
    @JoinColumn(name = "file_id")
    private FileEntity file;
}
